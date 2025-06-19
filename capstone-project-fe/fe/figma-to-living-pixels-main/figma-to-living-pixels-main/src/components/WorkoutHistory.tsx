
import React, { useState } from 'react';
import { WorkoutCard } from './WorkoutCard';
import { ActionButton } from './ActionButton';

interface WorkoutRecord {
  id: string;
  imageUrl: string;
  timeAgo: string;
  exerciseName: string;
  imageColor?: string;
}

export const WorkoutHistory: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  
  const workoutRecords: WorkoutRecord[] = [
    {
      id: '1',
      imageUrl: 'https://placehold.co/110x70/8B4513/8B4513',
      timeAgo: '3일전',
      exerciseName: '원암 덤벨 로우',
      imageColor: '#8B4513',
    },
    {
      id: '2',
      imageUrl: 'https://placehold.co/110x70/4A90E2/4A90E2',
      timeAgo: '2일전',
      exerciseName: '스쿼트',
      imageColor: '#4A90E2',
    },
  ];

  const additionalRecords: WorkoutRecord[] = [
    {
      id: '3',
      imageUrl: 'https://placehold.co/110x70/E74C3C/E74C3C',
      timeAgo: '4일전',
      exerciseName: '푸쉬업',
      imageColor: '#E74C3C',
    },
    {
      id: '4',
      imageUrl: 'https://placehold.co/110x70/2ECC71/2ECC71',
      timeAgo: '5일전',
      exerciseName: '플랭크',
      imageColor: '#2ECC71',
    },
  ];

  const handleToggleExpanded = () => {
    setIsExpanded(!isExpanded);
  };

  return (
    <section className="w-full">
      <header className="mb-6">
        <h2 className="text-white text-sm font-medium">이전 기록</h2>
      </header>
      
      <div className="space-y-4 mb-6">
        {workoutRecords.map((record, index) => (
          <div key={record.id} className="relative">
            <WorkoutCard
              imageUrl={record.imageUrl}
              timeAgo={record.timeAgo}
              exerciseName={record.exerciseName}
              imageColor={record.imageColor}
            />
            {index === 1 && !isExpanded && (
              <div className="absolute inset-0 bg-gradient-to-b from-transparent to-black/50 pointer-events-none" />
            )}
          </div>
        ))}
        
        {isExpanded && (
          <div className="space-y-4">
            {additionalRecords.map((record) => (
              <WorkoutCard
                key={record.id}
                imageUrl={record.imageUrl}
                timeAgo={record.timeAgo}
                exerciseName={record.exerciseName}
                imageColor={record.imageColor}
              />
            ))}
          </div>
        )}
      </div>

      <div className="relative">
        <ActionButton
          variant="secondary"
          className="w-full h-10 bg-black border-none"
          onClick={handleToggleExpanded}
        >
          <span className="text-[#727272] text-sm font-normal">{isExpanded ? '접기' : '더보기'}</span>
          <svg
            width="12"
            height="6"
            viewBox="0 0 12 6"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className={`ml-2 transition-transform duration-200 ${isExpanded ? 'rotate-180' : ''}`}
          >
            <path
              d="M1 1L6 5L11 1"
              stroke="#727272"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </ActionButton>
      </div>
    </section>
  );
};
