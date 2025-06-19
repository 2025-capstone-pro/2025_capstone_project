
import React, { useState, useRef } from 'react';
import { ActionButton } from './ActionButton';
import { VideoAnalysisService } from '@/services/videoAnalysisService';

export const WorkoutRecorder: React.FC = () => {
  const [isRecording, setIsRecording] = useState(false);
  const [hasRecording, setHasRecording] = useState(false);
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [recordedBlob, setRecordedBlob] = useState<Blob | null>(null);
  const videoRef = useRef<HTMLVideoElement>(null);
  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const recordedChunksRef = useRef<Blob[]>([]);

  const startRecording = async () => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia({ 
        video: { facingMode: 'user' }, 
        audio: false 
      });
      
      if (videoRef.current) {
        videoRef.current.srcObject = stream;
      }

      const mediaRecorder = new MediaRecorder(stream);
      mediaRecorderRef.current = mediaRecorder;
      recordedChunksRef.current = [];
      
      mediaRecorder.ondataavailable = (event) => {
        if (event.data.size > 0) {
          recordedChunksRef.current.push(event.data);
        }
      };

      mediaRecorder.onstop = () => {
        const blob = new Blob(recordedChunksRef.current, { type: 'video/webm' });
        setRecordedBlob(blob);
        setHasRecording(true);
        
        // Create object URL for playback
        if (videoRef.current) {
          videoRef.current.src = URL.createObjectURL(blob);
          videoRef.current.srcObject = null;
        }
      };
      
      mediaRecorder.start();
      setIsRecording(true);
    } catch (error) {
      console.error('Error accessing camera:', error);
      alert('카메라에 접근할 수 없습니다.');
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current && isRecording) {
      mediaRecorderRef.current.stop();
      setIsRecording(false);
      
      // Stop all video tracks
      if (videoRef.current?.srcObject) {
        const stream = videoRef.current.srcObject as MediaStream;
        stream.getTracks().forEach(track => track.stop());
      }
    }
  };

  const handleStartWorkout = async () => {
    if (!isRecording && !hasRecording) {
      startRecording();
    } else if (isRecording) {
      stopRecording();
    } else {
      // Restart recording
      setHasRecording(false);
      setRecordedBlob(null);
      startRecording();
    }
  };

  const handleLoadFile = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.accept = 'video/*';
    input.onchange = (e) => {
      const file = (e.target as HTMLInputElement).files?.[0];
      if (file && videoRef.current) {
        const url = URL.createObjectURL(file);
        videoRef.current.src = url;
        setRecordedBlob(file);
        setHasRecording(true);
      }
    };
    input.click();
  };

  const handleAnalyzeVideo = async () => {
    if (!recordedBlob) return;
    
    setIsAnalyzing(true);
    try {
      const result = await VideoAnalysisService.analyzeVideo(recordedBlob);
      console.log('Analysis result:', result);
      
      // Dispatch custom event with analysis result
      const event = new CustomEvent('videoAnalysisComplete', { 
        detail: result 
      });
      window.dispatchEvent(event);
    } catch (error) {
      console.error('Video analysis failed:', error);
    } finally {
      setIsAnalyzing(false);
    }
  };

  return (
    <main className="w-full h-[749px] relative">
      <div className="w-full h-[520px] relative overflow-hidden rounded-t-[10px]">
        {/* Video Background */}
        <div className="w-full h-full bg-[#C4C4C4] rounded-t-[10px] relative">
          <video
            ref={videoRef}
            autoPlay={isRecording}
            muted
            playsInline
            controls={hasRecording && !isRecording}
            className="w-full h-full object-cover"
          />
          
          {/* Gradient Overlay */}
          <div className="absolute inset-0 bg-gradient-to-b from-black/30 via-transparent to-black/60" />
          
          {/* Border */}
          <div className="absolute inset-0 border border-white border-solid opacity-30 rounded-t-[10px]" />
        </div>

        {/* Recording Status */}
        {isRecording && (
          <div className="absolute top-[194px] left-1/2 transform -translate-x-1/2">
            <div className="bg-black px-2.5 py-2.5 rounded flex items-center gap-2">
              <div className="w-2 h-2 bg-red-500 rounded-full animate-pulse" />
              <span className="text-white text-sm font-normal">운동 녹화</span>
            </div>
          </div>
        )}

        {/* Analyzing Status */}
        {isAnalyzing && (
          <div className="absolute top-[194px] left-1/2 transform -translate-x-1/2">
            <div className="bg-black px-2.5 py-2.5 rounded flex items-center gap-2">
              <div className="w-2 h-2 bg-blue-500 rounded-full animate-pulse" />
              <span className="text-white text-sm font-normal">분석 중...</span>
            </div>
          </div>
        )}

        {/* Load File Button */}
        <button
          onClick={handleLoadFile}
          className="absolute left-[5px] top-[367px] w-[50px] h-[50px] bg-[#F3F3F3] rounded-[5px] flex items-center justify-center text-black text-[10px] font-normal hover:bg-gray-200 transition-colors"
        >
          불러오기
        </button>

        {/* Analyze Button */}
        {hasRecording && !isRecording && (
          <button
            onClick={handleAnalyzeVideo}
            disabled={isAnalyzing}
            className="absolute right-[5px] top-[367px] w-[50px] h-[50px] bg-[#4285F4] rounded-[5px] flex items-center justify-center text-white text-[10px] font-normal hover:bg-[#3367D6] transition-colors disabled:opacity-50"
          >
            {isAnalyzing ? '분석중' : '분석'}
          </button>
        )}
      </div>

      {/* Title */}
      <header className="absolute left-[25px] top-[451px]">
        <h1 className="text-white text-[25px] font-medium">자세 교정</h1>
      </header>

      {/* Start Button */}
      <div className="absolute left-[25px] top-[520px] w-[310px]">
        <ActionButton
          variant="primary"
          className="w-full"
          onClick={handleStartWorkout}
          disabled={isAnalyzing}
        >
          {isRecording ? '녹화 중지' : hasRecording ? '다시 시작' : '시작하기'}
        </ActionButton>
      </div>
    </main>
  );
};
