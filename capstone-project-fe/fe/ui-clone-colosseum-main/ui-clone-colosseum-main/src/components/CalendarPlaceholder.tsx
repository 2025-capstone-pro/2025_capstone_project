import React from 'react';

export const CalendarPlaceholder: React.FC = () => {
  return (
    <section className="w-[330px] h-[400px] border absolute bg-black border-solid border-white left-3.5 top-[15px] max-md:w-[calc(100%_-_30px)] max-md:left-[15px] max-sm:w-[calc(100%_-_20px)] max-sm:h-[350px] max-sm:left-2.5 max-sm:top-2.5">
      <div className="text-white text-xl font-normal w-14 h-[23px] absolute left-[138px] top-[188px] max-sm:left-[calc(50%_-_28px)] max-sm:top-[170px]">
        캘린더
      </div>
    </section>
  );
};
