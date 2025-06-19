import React from 'react';

interface WorkoutCardProps {
  imageUrl: string;
  timeAgo: string;
  exerciseName: string;
  imageColor?: string;
}

export const WorkoutCard: React.FC<WorkoutCardProps> = ({
  imageUrl,
  timeAgo,
  exerciseName,
  imageColor = '#8B4513',
}) => {
  return (
    <article className="flex w-full h-[70px] items-center gap-4">
      <div className="w-[110px] h-[70px] shrink-0 rounded-md overflow-hidden">
        <img
          src={imageUrl}
          alt={exerciseName}
          className="w-full h-full object-cover"
          style={{ backgroundColor: imageColor }}
        />
      </div>
      <div className="flex flex-col justify-center gap-1">
        <time className="text-white text-sm font-normal">
          {timeAgo}
        </time>
        <h3 className="text-white text-sm font-normal">
          {exerciseName}
        </h3>
      </div>
    </article>
  );
};
