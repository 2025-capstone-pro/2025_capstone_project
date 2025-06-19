import React from 'react';

interface LoginHeaderProps {
  title: string;
  subtitle: string;
}

export const LoginHeader: React.FC<LoginHeaderProps> = ({ title, subtitle }) => {
  return (
    <header className="flex flex-col justify-center items-start gap-[30.581px]">
      <div className="flex flex-col justify-center items-start gap-[11.468px]">
        <h1 className="w-[313px] text-white text-[31px] font-bold leading-[40.3px] tracking-[-0.612px] max-md:text-[28px] max-md:w-full max-sm:text-2xl max-sm:leading-[28.8px]">
          {title}
        </h1>
        <p className="w-[287px] text-[#6C7278] text-[11px] font-medium leading-[15.4px] tracking-[-0.115px] max-md:text-xs max-md:w-full max-sm:text-[11px]">
          {subtitle}
        </p>
      </div>
    </header>
  );
};
