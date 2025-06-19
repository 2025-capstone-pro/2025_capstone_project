
import React, { useState, useEffect } from 'react';
import { ActionButton } from './ActionButton';

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

export const ExerciseAnalysis: React.FC = () => {
  const [showMore, setShowMore] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [feedbackList, setFeedbackList] = useState<FeedbackItem[]>([]);
  const [hasAnalysis, setHasAnalysis] = useState(false);

  useEffect(() => {
    const handleAnalysisComplete = (event: CustomEvent<AnalysisResponse>) => {
      if (event.detail.success) {
        setFeedbackList(event.detail.data.feedbackList);
        setHasAnalysis(true);
      }
    };

    window.addEventListener('videoAnalysisComplete', handleAnalysisComplete as EventListener);
    
    return () => {
      window.removeEventListener('videoAnalysisComplete', handleAnalysisComplete as EventListener);
    };
  }, []);

  const handleShowMore = () => {
    setShowMore(!showMore);
  };

  const handleSaveRecord = async () => {
    setIsSaving(true);
    // Simulate saving process
    setTimeout(() => {
      setIsSaving(false);
      alert('운동 기록이 저장되었습니다!');
    }, 1000);
  };

  const displayedFeedback = showMore ? feedbackList : feedbackList.slice(0, 2);
  const hasMoreFeedback = feedbackList.length > 2;

  return (
    <section className="w-full space-y-6">
      <div className="w-full h-0 opacity-10 bg-white" />
      
      <div>
        <h2 className="text-white text-sm font-normal mb-3">자세 분석</h2>
        <div className="text-white text-sm font-normal opacity-70 leading-relaxed">
          {!hasAnalysis ? (
            <>
              <p>동영상을 촬영하거나 불러온 후 분석 버튼을 눌러주세요.</p>
              <p>AI가 자세를 분석해드립니다.</p>
            </>
          ) : (
            <>
              {displayedFeedback.map((feedback, index) => (
                <p key={index} className="mb-1">
                  프레임 {feedback.frame}: {feedback.text}
                </p>
              ))}
              {hasMoreFeedback && (
                <button
                  onClick={handleShowMore}
                  className="text-white text-xs font-normal opacity-40 mt-2 hover:opacity-60 transition-opacity"
                >
                  {showMore ? '간단히' : '더보기'}
                </button>
              )}
            </>
          )}
        </div>
      </div>

      <form onSubmit={(e) => { e.preventDefault(); handleSaveRecord(); }}>
        <ActionButton
          variant="outline"
          className="w-full h-[42px] border border-white"
          onClick={handleSaveRecord}
          disabled={isSaving || !hasAnalysis}
        >
          <span className="text-white text-[13px] font-normal leading-[19.5px]">
            {isSaving ? '저장 중...' : '운동 기록 저장'}
          </span>
        </ActionButton>
      </form>
    </section>
  );
};
