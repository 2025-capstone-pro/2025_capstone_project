import React from 'react';

interface LoginButtonProps {
  children: React.ReactNode;
  onClick?: () => void;
  type?: 'button' | 'submit';
  variant?: 'primary' | 'secondary';
  disabled?: boolean;
  className?: string;
}

export const LoginButton: React.FC<LoginButtonProps> = ({
  children,
  onClick,
  type = 'button',
  variant = 'primary',
  disabled = false,
  className = ''
}) => {
  const baseClasses = "w-[313px] h-[46px] gap-[9.557px] cursor-pointer px-[22.936px] py-[9.557px] rounded-[9.557px] border-[0.956px] border-solid max-md:w-full max-md:h-12 max-sm:h-11 transition-all duration-200";
  
  const variantClasses = {
    primary: "text-white text-center text-[13px] font-medium leading-[18.2px] tracking-[-0.134px] shadow-[0px_0.956px_1.911px_0px_rgba(37,62,167,0.48),0px_0px_0px_0.956px_#375DFB] bg-gradient-to-b from-[#4D81E7] to-[#375DFB] border-white hover:from-[#5A8AE8] hover:to-[#4068E8] active:from-[#3E6FD6] active:to-[#2B5AE5]",
    secondary: "text-[#1A1C1E] text-center text-sm font-semibold leading-[19.6px] tracking-[-0.14px] shadow-[0px_-2.867px_5.734px_0px_rgba(244,245,250,0.60)_inset] bg-white border-[#EFF0F6] hover:bg-gray-50 active:bg-gray-100"
  };

  const disabledClasses = disabled ? "opacity-50 cursor-not-allowed" : "";

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseClasses} ${variantClasses[variant]} ${disabledClasses} ${className}`}
      aria-disabled={disabled}
    >
      {children}
    </button>
  );
};
