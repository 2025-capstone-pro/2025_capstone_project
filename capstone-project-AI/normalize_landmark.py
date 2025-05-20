import cv2
import mediapipe as mp
import numpy as np
import os
import glob # 파일 목록을 가져오기 위해 추가

# --- 설정 ---
mp_pose = mp.solutions.pose
mp_drawing = mp.solutions.drawing_utils

# 저장할 랜드마크 인덱스 리스트 정의 (MediaPipe Pose 전체 랜드마크 기준)
# L_SHOULDER:11, R_SHOULDER:12, L_HIP:23, R_HIP:24는 정규화에 필수
# 나머지 랜드마크는 필요에 따라 추가
# 순서 중요: 정규화 함수에서 L_SHOULDER, R_SHOULDER, L_HIP, R_HIP에 접근할 때
# 이 리스트 내의 인덱스를 사용하므로, 초반에 위치시키는 것이 편리함.
landmark_indices_to_save = [
    11, 12, 23, 24, # 어깨, 엉덩이 (정규화 기준) - 순서 유지 권장
    25, 26, 27, 28, 29, 30, 31, 32 # 무릎, 발목, 발꿈치, 발끝
]
num_selected_landmarks = len(landmark_indices_to_save) # 저장할 랜드마크 개수

# 기준 랜드마크가 landmark_indices_to_save 리스트 내에서 몇 번째 인덱스인지 확인
# (이 값들은 정규화 함수 내부에서 사용됨)
try:
    L_SHOULDER_IDX_IN_LIST = landmark_indices_to_save.index(11)
    R_SHOULDER_IDX_IN_LIST = landmark_indices_to_save.index(12)
    L_HIP_IDX_IN_LIST = landmark_indices_to_save.index(23)
    R_HIP_IDX_IN_LIST = landmark_indices_to_save.index(24)
except ValueError:
    print("오류: 정규화에 필요한 랜드마크(11, 12, 23, 24)가 landmark_indices_to_save 리스트에 포함되어야 합니다.")
    exit()


# 비디오 파일이 있는 디렉토리 경로
input_video_dir = './../data/Video_Dataset/good/1115_video' # 예시: 비디오들이 있는 폴더

# 처리할 비디오 파일 확장자 목록
allowed_extensions = ('.mp4', '.avi', '.mov', '.mkv')

# 결과를 저장할 디렉토리 경로
output_dir = './../data/mediapipe_res/good_normalized' # 정규화된 데이터 저장 폴더

# --- Helper Functions for Normalization ---

def normalize_vector(v):
    norm = np.linalg.norm(v)
    if norm == 0:
        return v
    return v / norm

def cross_product(v1, v2):
    return np.cross(v1, v2)

def dot_product(v1, v2):
    return np.dot(v1, v2)

def normalize_pose(landmarks_np):
    """
    주어진 랜드마크 세트에 대해 자세 정규화를 수행합니다.
    landmarks_np: (num_selected_landmarks, 3) 형태의 NumPy 배열
    이 배열은 landmark_indices_to_save 순서대로 랜드마크를 포함해야 합니다.
    """
    if landmarks_np.shape[0] != num_selected_landmarks or landmarks_np.shape[1] != 3:
        # print(f"Debug: landmarks_np shape: {landmarks_np.shape}")
        # print(f"Debug: num_selected_landmarks: {num_selected_landmarks}")
        raise ValueError("normalize_pose: 입력 랜드마크 배열의 차원이 올바르지 않습니다.")

    # 1. 위치 정규화 (엉덩이 중심을 원점으로)
    left_hip = landmarks_np[L_HIP_IDX_IN_LIST]
    right_hip = landmarks_np[R_HIP_IDX_IN_LIST]
    hip_center = (left_hip + right_hip) / 2.0

    # 모든 랜드마크에서 hip_center를 빼서 위치 정규화
    translated_landmarks = landmarks_np - hip_center

    # 2. 회전 정규화
    # Y축: 엉덩이 중심 -> 어깨 중심 (또는 그 반대, 일관성 중요)
    # 여기서는 어깨 중심에서 엉덩이 중심을 향하는 벡터를 Y축으로 정의 
    left_shoulder = translated_landmarks[L_SHOULDER_IDX_IN_LIST]
    right_shoulder = translated_landmarks[R_SHOULDER_IDX_IN_LIST]
    shoulder_center = (left_shoulder + right_shoulder) / 2.0
    
    # 주의: hip_center는 이미 원점이므로, shoulder_center 자체가 y축 방향 벡터가 됨
    # (단, hip_center가 (0,0,0)이 된 translated_landmarks 기준)
    # y_axis_vec = normalize_vector(shoulder_center - np.array([0,0,0])) # shoulder_center 자체가 벡터임
    y_axis_vec = normalize_vector(np.array([0,0,0]) - shoulder_center) # 어깨 중심 -> 엉덩이 중심 (원점)

    # 어깨선 벡터를 사용한 초기 X축 방향 참조
    left_hip_at_origin = translated_landmarks[L_HIP_IDX_IN_LIST] # 이미 (0,0,0) 근처 값
    right_hip_at_origin = translated_landmarks[R_HIP_IDX_IN_LIST]

    left_side_center_at_origin = (left_shoulder + left_hip_at_origin) / 2.0
    right_side_center_at_origin = (right_shoulder + right_hip_at_origin) / 2.0
    left_to_right_vec = normalize_vector(right_side_center_at_origin - left_side_center_at_origin)

    # Z축: leftToRight 와 Y축에 수직
    z_axis_vec = normalize_vector(cross_product(left_to_right_vec, y_axis_vec))

    # X축: Y축과 Z축에 수직 (새로운, 정제된 X축)
    x_axis_vec = normalize_vector(cross_product(y_axis_vec, z_axis_vec))

    # 3. 모든 점을 새로운 좌표계로 변환 (회전)
    normalized_landmarks_np = np.zeros_like(translated_landmarks)
    for i, point in enumerate(translated_landmarks):
        normalized_landmarks_np[i, 0] = dot_product(x_axis_vec, point)
        normalized_landmarks_np[i, 1] = dot_product(y_axis_vec, point)
        normalized_landmarks_np[i, 2] = dot_product(z_axis_vec, point)
        
    return normalized_landmarks_np

# --- MediaPipe Pose 객체 생성 (한 번만 생성) ---
pose = None # 초기화
try:
    pose = mp_pose.Pose(
        static_image_mode=False, # 비디오 처리므로 False
        model_complexity=1,      # 모델 복잡도 (0, 1, 2)
        enable_segmentation=False, # 세그멘테이션 사용 안 함
        min_detection_confidence=0.5,
        min_tracking_confidence=0.5)
    print("MediaPipe Pose 객체 생성 성공.")
except Exception as e:
    print(f"MediaPipe Pose 객체 생성 중 오류 발생: {e}")
    exit()

# --- 저장 디렉토리 생성 (한 번만 실행) ---
os.makedirs(output_dir, exist_ok=True)
print(f"결과 저장 디렉토리: {output_dir}")

# --- 지정된 디렉토리의 모든 비디오 파일 처리 ---
all_files = os.listdir(input_video_dir)
video_files = [f for f in all_files if os.path.isfile(os.path.join(input_video_dir, f)) and f.lower().endswith(allowed_extensions)]

print(f"\n총 {len(video_files)}개의 비디오 파일을 처리합니다: {video_files}")

if not video_files:
    print(f"오류: '{input_video_dir}' 디렉토리에서 비디오 파일을 찾을 수 없습니다.")
    if pose:
        try:
            pose.close()
            print("MediaPipe Pose 객체를 닫았습니다 (비디오 없음).")
        except Exception as e:
             print(f"MediaPipe Pose 객체를 닫는 중 오류 발생 (비디오 없음): {e}")
    exit()

for video_filename in video_files:
    video_path = os.path.join(input_video_dir, video_filename)
    print(f"\n--- 비디오 처리 시작: {video_path} ---")

    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        print(f"오류: 비디오 파일을 열 수 없습니다 -> {video_path}. 다음 파일로 건너<0xEB><0x9B><0x84>니다.")
        continue

    all_frame_normalized_landmarks = [] # 정규화된 랜드마크 저장 리스트
    frame_count = 0
    print(f"'{video_filename}' 처리 중... (선택된 {num_selected_landmarks}개 랜드마크 저장, 정규화 적용)")

    while cap.isOpened():
        success, image = cap.read()
        if not success:
            print(f"'{video_filename}' 처리 완료 (총 {frame_count} 프레임) 또는 프레임 읽기 실패.")
            break

        frame_count += 1
        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image_rgb.flags.writeable = False
        results = pose.process(image_rgb)
        image_rgb.flags.writeable = True

        current_frame_landmarks_np = np.full((num_selected_landmarks, 3), np.nan, dtype=np.float32)
        normalized_frame_landmarks_np = np.full((num_selected_landmarks, 3), np.nan, dtype=np.float32)


        if results.pose_world_landmarks: # 월드 랜드마크 사용
            landmarks = results.pose_world_landmarks.landmark
            try:
                selected_coords_list = []
                valid_landmarks = True
                for i in landmark_indices_to_save:
                    # visibility 체크 추가 (선택 사항, 월드 랜드마크는 visibility가 항상 높을 수 있음)
                    # if landmarks[i].visibility < 0.5: # 예시 임계값
                    #     selected_coords_list.append([np.nan, np.nan, np.nan])
                    #     # valid_landmarks = False # 하나라도 안보이면 정규화 스킵할 수도 있음
                    #     # break
                    # else:
                    selected_coords_list.append([landmarks[i].x, landmarks[i].y, landmarks[i].z])
                
                # if not valid_landmarks:
                #    print(f"프레임 {frame_count} ({video_filename}): 일부 랜드마크 가시성 낮음. 정규화 건너뜀.")
                #    all_frame_normalized_landmarks.append(normalized_frame_landmarks_np) # NaN 배열 추가
                #    continue


                current_frame_landmarks_np = np.array(selected_coords_list, dtype=np.float32)

                # 모든 좌표값이 유효한지 (너무 크거나 작지 않은지) 한 번 더 확인
                if not np.all(np.isfinite(current_frame_landmarks_np)) or \
                   np.any(current_frame_landmarks_np > 1e6) or \
                   np.any(current_frame_landmarks_np < -1e6):
                    print(f"프레임 {frame_count} ({video_filename}): 비정상적인 월드 랜드마크 좌표 감지. NaN으로 처리.")
                    # normalized_frame_landmarks_np는 이미 NaN으로 초기화됨
                elif np.isnan(current_frame_landmarks_np).any(): # 혹시 모를 NaN 값 체크
                    print(f"프레임 {frame_count} ({video_filename}): NaN 랜드마크 좌표 감지. NaN으로 처리.")
                else:
                    # 정규화 수행
                    normalized_frame_landmarks_np = normalize_pose(current_frame_landmarks_np.copy()) # 원본 보존을 위해 copy

            except IndexError:
                print(f"프레임 {frame_count} ({video_filename}): 랜드마크 인덱스 접근 오류 발생 (월드). NaN으로 처리.")
            except ValueError as ve: # normalize_pose 내부에서 발생 가능
                 print(f"프레임 {frame_count} ({video_filename}): 정규화 중 오류: {ve}. NaN으로 처리.")
            except Exception as e:
                print(f"프레임 {frame_count} ({video_filename}): 랜드마크 처리 또는 정규화 중 오류 발생: {e}. NaN으로 처리.")
        
        all_frame_normalized_landmarks.append(normalized_frame_landmarks_np)

        # (선택 사항) 화면 표시 (정규화 전/후 랜드마크를 그릴 수 있음)
        # ... (기존 시각화 코드 여기에 적용 가능) ...
        # if cv2.waitKey(5) & 0xFF == ord('q'):
        #     print("사용자 요청으로 처리 중단.")
        #     break

    cap.release()

    if all_frame_normalized_landmarks:
        try:
            landmark_data_np = np.array(all_frame_normalized_landmarks)
            # NaN 값 확인 (디버깅용)
            if np.isnan(landmark_data_np).all():
                print(f"'{video_filename}'의 모든 랜드마크 데이터가 NaN입니다. 파일을 저장하지 않습니다.")
            elif np.isnan(landmark_data_np).any():
                nan_frames = np.unique(np.where(np.isnan(landmark_data_np))[0])
                print(f"'{video_filename}' 데이터에 NaN 값이 포함된 프레임 수: {len(nan_frames)} / {landmark_data_np.shape[0]}")


            print(f"'{video_filename}' 최종 정규화된 데이터 형태: {landmark_data_np.shape}")

            base_filename = os.path.splitext(video_filename)[0]
            output_filename = f"{base_filename}_normalized_world_landmarks.npy" # 파일명에 정보 추가
            output_filepath = os.path.join(output_dir, output_filename)

            np.save(output_filepath, landmark_data_np)
            print(f"정규화된 랜드마크 데이터 저장 완료: {output_filepath}")

        except ValueError as ve:
             print(f"'{video_filename}' 데이터 배열 변환 중 오류 발생: {ve}.")
        except Exception as e:
            print(f"'{output_filepath}' 파일 저장 또는 데이터 처리 중 오류 발생: {e}")
    else:
        print(f"'{video_filename}'에서 저장할 정규화된 랜드마크 데이터가 없습니다.")

if pose:
    try:
        pose.close()
        print("\nMediaPipe Pose 객체를 성공적으로 닫았습니다.")
    except Exception as e:
        print(f"\nMediaPipe Pose 객체를 닫는 중 오류 발생: {e}")
else:
    print("\nMediaPipe Pose 객체가 유효하지 않아 닫기를 건너<0xEB><0x9B><0x84>니다.")

print("\n모든 비디오 파일 처리 완료.")