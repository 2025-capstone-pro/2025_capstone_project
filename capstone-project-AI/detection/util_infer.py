import json
import logging

import numpy as np

from scipy.signal import find_peaks

# ───────────────────────────────────────────────────────────────────────────────
# --- 1) 관절 각도 계산 함수 ------------------------------------------------------
# ───────────────────────────────────────────────────────────────────────────────
def compute_joint_angle(A, B, C):
    """
    세 점 A, B, C (각각 (x, y) 형태)에서 ∠ABC를 계산하여 도(degree) 단위로 반환.
    """
    try:
        BA = np.array([A[0] - B[0], A[1] - B[1]])
        BC = np.array([C[0] - B[0], C[1] - B[1]])
        denom = (np.linalg.norm(BA) * np.linalg.norm(BC) + 1e-6)
        cos_phi = np.dot(BA, BC) / denom
        cos_phi = np.clip(cos_phi, -1.0, 1.0)
        phi = np.arccos(cos_phi)
        return np.degrees(phi)
    except Exception as e:
        logging.error(f"Error computing joint angle: {str(e)}")
        return None


# ───────────────────────────────────────────────────────────────────────────────
# --- 2) 엉덩이에 맞춘 상대적 좌표 계산 함수 --------------------------------------
# ───────────────────────────────────────────────────────────────────────────────
def adjust_landmarks_to_hip_center(landmarks, scale_factor=1000):
    """
    엉덩이 중심을 기준으로 랜드마크들의 상대적 위치를 계산하고,
    x, y, z 모두 고려하여 크기를 확대하여 반환합니다.
    """
    try:
        pts = np.array(landmarks)  # shape (33, 3)

        # 엉덩이 좌표 (x, y, z) 추출
        hip_left = pts[23][:3]  # 좌엉덩이 (x, y, z)
        hip_right = pts[24][:3]  # 우엉덩이 (x, y, z)
        
        # 엉덩이 중심 계산
        hip_center = (hip_left + hip_right) / 2.0

        # 각 랜드마크를 엉덩이 중심 기준으로 이동하고, 크기 확대
        adjusted_landmarks = []
        for i in range(33):  # 33개의 랜드마크
            x, y, z = pts[i]
            
            # x, y, z 차이를 계산하여 상대적 위치로 변환 (엉덩이 중심 기준)
            x_ = (x - hip_center[0]) * scale_factor  # x축 기준으로 상대적 위치, 크기 확대
            y_ = (y - hip_center[1]) * scale_factor  # y축 기준으로 상대적 위치, 크기 확대
            z_ = (z - hip_center[2]) * scale_factor  # z축 기준으로 상대적 위치, 크기 확대 (깊이도 고려)

            adjusted_landmarks.append((x_, y_, z_))

        return np.array(adjusted_landmarks)  # shape (33, 3)
    
    except Exception as e:
        logging.error(f"Error adjusting landmarks to hip center: {str(e)}")
        return None

# ───────────────────────────────────────────────────────────────────────────────
# --- 3) 랜드마크 JSON 파싱 함수 --------------------------------------------------
def parse_landmarks_json(data):
    """딕셔너리 형태의 landmarks 데이터를 파싱"""

    frames = data.get("frames", [])
    if not frames:
        raise ValueError("입력 JSON에 'frames' 데이터가 없습니다.")

    # frameIndex 기준으로 정렬 (안 되어 있을 경우 대비)
    frames.sort(key=lambda x: x["frameIndex"])

    landmark_sequence = []
    for frame in frames:
        landmarks = frame.get("landmarks", [])
        if len(landmarks) != 33:
            raise ValueError(f"frameIndex {frame['frameIndex']}의 landmark 수가 33이 아닙니다.")

        # 각 landmark는 {'x': float, 'y': float, 'z': float} 형태
        coords = [[lm["x"], lm["y"], lm["z"]] for lm in landmarks]
        landmark_sequence.append(coords)

    # (N, 33, 3) 형태의 NumPy 배열로 변환
    return np.array(landmark_sequence)

def extract_features_from_landmarks(landmark_array, min_peak_distance=15, prominence=0.01):
    """
    이미 추출된 landmark 좌표 배열에서 dynamic feature 추출.

    Parameters:
    - landmark_array: shape (N, 33, 3) ndarray
    - 반환: numpy array of shape (n_reps, 6)
    """
    try:
        if landmark_array.ndim != 3 or landmark_array.shape[1:] != (33, 3):
            raise ValueError("입력 배열은 (N, 33, 3) 형태여야 합니다.")

        hip_center_y_list = []
        knee_angle_left_list = []
        knee_angle_right_list = []
        hip_angle_left_list = []
        hip_angle_right_list = []

        for frame_landmarks in landmark_array:
            # 엉덩이 중심 기준 상대좌표 (scale은 그대로 사용 가능)
            adjusted_lms = adjust_landmarks_to_hip_center(frame_landmarks)
            if adjusted_lms is None:
                continue

            # hip_center_y
            ly23 = frame_landmarks[23][1]
            ry24 = frame_landmarks[24][1]
            hip_center_y = (ly23 + ry24) / 2.0
            hip_center_y_list.append(hip_center_y)

            # 관절 각도 계산
            lk = compute_joint_angle(frame_landmarks[23][:2], frame_landmarks[25][:2], frame_landmarks[27][:2])
            rk = compute_joint_angle(frame_landmarks[24][:2], frame_landmarks[26][:2], frame_landmarks[28][:2])
            lh = compute_joint_angle(frame_landmarks[11][:2], frame_landmarks[23][:2], frame_landmarks[25][:2])
            rh = compute_joint_angle(frame_landmarks[12][:2], frame_landmarks[24][:2], frame_landmarks[26][:2])

            knee_angle_left_list.append(lk)
            knee_angle_right_list.append(rk)
            hip_angle_left_list.append(lh)
            hip_angle_right_list.append(rh)

        if len(hip_center_y_list) < 3:
            logging.warning("[util] 충분한 프레임이 없습니다.")
            return None

        # 시계열 데이터
        hip_center_y_arr = np.array(hip_center_y_list)
        knee_left_arr = np.array(knee_angle_left_list)
        knee_right_arr = np.array(knee_angle_right_list)
        hip_left_arr = np.array(hip_angle_left_list)
        hip_right_arr = np.array(hip_angle_right_list)

        # peaks & valleys
        peaks, _ = find_peaks(hip_center_y_arr, distance=min_peak_distance, prominence=prominence)
        valleys, _ = find_peaks(-hip_center_y_arr, distance=min_peak_distance, prominence=prominence)

        dynamic_features = []

        for v in valleys:
            prev_peaks = peaks[peaks < v]
            next_peaks = peaks[peaks > v]
            if len(prev_peaks) == 0 or len(next_peaks) == 0:
                continue
            p_prev = prev_peaks.max()
            p_next = next_peaks.min()

            descend_len = v - p_prev
            ascend_len = p_next - v
            if descend_len <= 0 or ascend_len <= 0:
                continue

            min_knee_angle = min(knee_left_arr[v], knee_right_arr[v])
            min_hip_angle = min(hip_left_arr[v], hip_right_arr[v])
            avg_descend_speed = (hip_center_y_arr[p_prev] - hip_center_y_arr[v]) / descend_len
            avg_ascend_speed = (hip_center_y_arr[p_next] - hip_center_y_arr[v]) / ascend_len

            dynamic_features.append([
                float(descend_len),
                float(ascend_len),
                float(min_knee_angle),
                float(min_hip_angle),
                float(avg_descend_speed),
                float(avg_ascend_speed)
            ])

        if len(dynamic_features) == 0:
            logging.warning("[util] Dynamic feature 추출 실패")
            return None

        return np.array(dynamic_features, dtype=np.float32)

    except Exception as e:
        logging.error(f"[util] landmark 기반 feature 추출 중 오류: {str(e)}")
        return None