import React from 'react';

interface ExerciseCardProps {
  category?: string;
  title?: string;
  timeLabel?: string;
  time?: string;
  description?: string;
}

export const ExerciseCard: React.FC<ExerciseCardProps> = ({
  category = "today's exercise",
  title = "대둔근, 대퇴사두근, 슬괴근 운동",
  timeLabel = "오늘 저녁",
  time = "오후 8:00",
  description = "스쿼트와 런지로 통한 하체 근육의 협응력을 길러보세요."
}) => {
  return (
    <article className="absolute left-[25px] top-[423px] max-sm:left-5 max-sm:top-[380px]">
      <header className="text-[#64FFCE] text-[13px] font-normal w-[102px] h-4 mb-[10px]">
        {category}
      </header>
      
      <h1 className="w-[275px] text-white text-[26px] font-medium leading-[36.4px] h-[72px] mb-[22px] max-sm:w-[calc(100vw_-_40px)] max-sm:text-[22px]">
        {title}
      </h1>
      
      <div className="w-[126px] h-[17px] mb-[34px] flex items-center gap-[14px]">
        <time className="text-[#727272] text-[13px] font-normal">
          {timeLabel}
        </time>
        <time className="text-[#727272] text-sm font-medium">
          {time}
        </time>
      </div>
      
      <p className="w-[310px] h-[63px] text-white text-sm font-medium leading-[21px] max-md:w-[calc(100vw_-_50px)] max-sm:w-[calc(100vw_-_40px)] max-sm:text-[13px]">
        {description}
      </p>
    </article>
  );
};
