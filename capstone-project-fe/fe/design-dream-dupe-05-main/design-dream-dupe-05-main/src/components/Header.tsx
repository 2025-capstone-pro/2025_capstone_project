import React from 'react';

interface HeaderProps {
  logoText?: string;
  profileImageUrl?: string;
  onProfileClick?: () => void;
}

export const Header: React.FC<HeaderProps> = ({
  logoText = "로고",
  profileImageUrl = "https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/32107157071102e72336bb6cda20f1f47a4693e1?placeholderIfAbsent=true",
  onProfileClick
}) => {
  return (
    <header className="flex w-full max-w-[318px] gap-5 text-lg text-white font-normal whitespace-nowrap justify-between">
      <div className="mt-2.5">
        {logoText}
      </div>
      <button
        onClick={onProfileClick}
        className="focus:outline-none focus:ring-2 focus:ring-[#64FFCE] rounded"
        aria-label="프로필 메뉴"
      >
        <img
          src={profileImageUrl}
          alt="프로필"
          className="aspect-[1] object-contain w-7 shrink-0"
        />
      </button>
    </header>
  );
};
