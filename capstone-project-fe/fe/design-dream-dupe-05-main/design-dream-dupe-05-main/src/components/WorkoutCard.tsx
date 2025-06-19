import React from 'react';

interface WorkoutCardProps {
  title: string;
  duration: string;
  onClick?: () => void;
}

export const WorkoutCard: React.FC<WorkoutCardProps> = ({
  title,
  duration,
  onClick
}) => {
  return (
    <article 
      className="flex flex-col items-stretch flex-1 cursor-pointer group"
      onClick={onClick}
      role="button"
      tabIndex={0}
      onKeyDown={(e) => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          onClick?.();
        }
      }}
    >
      <h3 className="text-white text-[17px] group-hover:text-[#64FFCE] transition-colors duration-200">
        {title.split('\n').map((line, index) => (
          <React.Fragment key={index}>
            {line}
            {index < title.split('\n').length - 1 && <br />}
          </React.Fragment>
        ))}
      </h3>
      <div className="text-white text-xs mt-[15px] group-hover:text-[#64FFCE] transition-colors duration-200">
        {duration}
      </div>
    </article>
  );
};
