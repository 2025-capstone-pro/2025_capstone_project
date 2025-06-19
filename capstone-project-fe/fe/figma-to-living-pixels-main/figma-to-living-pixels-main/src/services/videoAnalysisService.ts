interface Landmark {
  x: number;
  y: number;
  z: number;
}

interface Frame {
  frameIndex: number;
  timestamp: number;
  landmarks: Landmark[];
}

interface AnalysisData {
  frames: Frame[];
}

interface FeedbackItem {
  frame: number;
  text: string;
}

interface AnalysisResponse {
  success: boolean;
  data: {
    feedbackList: FeedbackItem[];
  };
}

export class VideoAnalysisService {
  private static readonly API_ENDPOINT = 'https://3daedetection.xyz/api/analyze-pose';

  static async analyzeVideo(videoBlob: Blob): Promise<AnalysisResponse> {
    // Extract frames and landmarks from video
    const analysisData = await this.extractPoseLandmarks(videoBlob);
    
    // Send to backend for analysis
    const response = await fetch(this.API_ENDPOINT, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(analysisData),
    });

    if (!response.ok) {
      throw new Error('Analysis failed');
    }

    return await response.json();
  }

  private static async extractPoseLandmarks(videoBlob: Blob): Promise<AnalysisData> {
    return new Promise((resolve) => {
      const video = document.createElement('video');
      const canvas = document.createElement('canvas');
      const ctx = canvas.getContext('2d')!;

      video.src = URL.createObjectURL(videoBlob);
      video.onloadedmetadata = () => {
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        
        const frames: Frame[] = [];
        let frameIndex = 0;
        const fps = 30; // Assuming 30 fps
        
        video.ontimeupdate = () => {
          if (video.currentTime >= video.duration) {
            resolve({ frames });
            return;
          }

          ctx.drawImage(video, 0, 0);
          
          // This is where MediaPipe pose detection would happen
          // For now, we'll generate mock landmarks
          const landmarks: Landmark[] = this.generateMockLandmarks();
          
          frames.push({
            frameIndex,
            timestamp: video.currentTime,
            landmarks,
          });

          frameIndex++;
          video.currentTime += 1 / fps;
        };

        video.currentTime = 0;
      };
    });
  }

  private static generateMockLandmarks(): Landmark[] {
    // Generate 33 pose landmarks (MediaPipe pose format)
    const landmarks: Landmark[] = [];
    for (let i = 0; i < 33; i++) {
      landmarks.push({
        x: Math.random() * 0.8 + 0.1, // Random x between 0.1 and 0.9
        y: Math.random() * 0.8 + 0.1, // Random y between 0.1 and 0.9
        z: Math.random() * 0.4 - 0.2, // Random z between -0.2 and 0.2
      });
    }
    return landmarks;
  }
}
