import React from 'react';

interface LoginFooterProps {
  onSignUpClick?: () => void;
}

export const LoginFooter: React.FC<LoginFooterProps> = ({ onSignUpClick }) => {
  return (
    <footer className="flex justify-center items-center gap-[5.734px] self-stretch">
      <span className="text-[#6C7278] text-[11px] font-medium leading-[15.4px] tracking-[-0.115px]">
        Don't have an account?
      </span>
      <button
        type="button"
        onClick={onSignUpClick}
        className="text-[#4D81E7] text-[11px] font-semibold leading-[15.4px] tracking-[-0.115px] cursor-pointer hover:text-[#3E6FD6] transition-colors duration-200"
      >
        Sign Up
      </button>
    </footer>
  );
};
