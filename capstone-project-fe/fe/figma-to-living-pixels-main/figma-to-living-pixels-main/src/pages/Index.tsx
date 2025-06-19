
import React from 'react';
import { WorkoutRecorder } from '@/components/WorkoutRecorder';
import { ExerciseAnalysis } from '@/components/ExerciseAnalysis';
import { WorkoutHistory } from '@/components/WorkoutHistory';

const Index: React.FC = () => {
  return (
    <>
      <link
        href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&family=Noto+Sans+CJK+KR:wght@400&family=Montserrat:wght@400;500&display=swap"
        rel="stylesheet"
      />
      <div className="w-[360px] min-h-[1105px] bg-black mx-auto my-0 rounded-[10px] max-md:w-full max-md:max-w-[360px] max-sm:w-full max-sm:m-0 pb-6 flex flex-col">
        {/* Main Workout Recording Section */}
        <WorkoutRecorder />
        
        {/* Exercise Analysis Section */}
        <section className="mx-[25px] mt-[20px] max-sm:mx-[5%]">
          <ExerciseAnalysis />
        </section>
        
        {/* Previous Workout History Section */}
        <section className="mx-[25px] mt-[49px] max-sm:mx-[5%]">
          <WorkoutHistory />
        </section>
      </div>
    </>
  );
};

export default Index;
