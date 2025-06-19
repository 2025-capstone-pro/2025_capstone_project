import React from 'react';

interface AdBannerProps {
  content?: string;
  onClick?: () => void;
}

export const AdBanner: React.FC<AdBannerProps> = ({
  content = "광고",
  onClick
}) => {
  return (
    <div
      className="border self-center w-full max-w-[304px] text-base text-black whitespace-nowrap text-center leading-[1.4] mt-7 px-[70px] py-[84px] border-white border-solid cursor-pointer hover:bg-gray-100 transition-colors duration-200"
      onClick={onClick}
      role="banner"
      aria-label="광고 배너"
    >
      {content}
    </div>
  );
};
