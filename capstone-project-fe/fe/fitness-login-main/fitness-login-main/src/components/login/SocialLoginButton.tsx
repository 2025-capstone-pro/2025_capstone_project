import React from 'react';

interface SocialLoginButtonProps {
  provider: 'google' | 'kakao';
  onClick?: () => void;
  disabled?: boolean;
}

const GoogleIcon: React.FC = () => (
  <div className="w-[17px] h-[17px] shrink-0 relative">
    <svg
      width="8"
      height="8"
      viewBox="0 0 8 8"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className="w-[7px] h-[7px] flex-shrink-0 absolute left-[5px] top-[5px]"
    >
      <path
        d="M7.9355 1.64969C7.9355 1.03091 7.88426 0.579353 7.77338 0.111084H0.563232V2.90398H4.79543C4.71013 3.59806 4.24937 4.64332 3.22542 5.34569L3.21107 5.4392L5.49078 7.16994L5.64872 7.18539C7.09926 5.87253 7.9355 3.94089 7.9355 1.64969Z"
        fill="#4285F4"
      />
    </svg>
    <svg
      width="13"
      height="7"
      viewBox="0 0 13 7"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className="w-[12px] h-[6px] flex-shrink-0 absolute left-0 top-[7px]"
    >
      <path
        d="M7.56272 7.00842C9.63614 7.00842 11.3768 6.33941 12.6482 5.18547L10.2249 3.34576C9.57643 3.78895 8.70608 4.09835 7.56272 4.09835C5.53196 4.09835 3.80837 2.78553 3.19396 0.970947L3.1039 0.978442L0.733423 2.77631L0.702423 2.86076C1.96525 5.31922 4.55919 7.00842 7.56272 7.00842Z"
        fill="#34A853"
      />
    </svg>
    <svg
      width="5"
      height="7"
      viewBox="0 0 5 7"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className="w-[3px] h-[7px] flex-shrink-0 absolute left-[-1px] top-[3px]"
    >
      <path
        d="M4.19442 4.97124C4.0323 4.50297 3.93848 4.00121 3.93848 3.48278C3.93848 2.9643 4.0323 2.4626 4.18589 1.99433L4.1816 1.8946L1.78141 0.0678711L1.70288 0.104477C1.18241 1.12466 0.883759 2.27029 0.883759 3.48278C0.883759 4.69528 1.18241 5.84085 1.70288 6.86103L4.19442 4.97124Z"
        fill="#FBBC05"
      />
    </svg>
    <svg
      width="13"
      height="7"
      viewBox="0 0 13 7"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      className="w-[12px] h-[6px] flex-shrink-0 absolute left-0 top-[-2px]"
    >
      <path
        d="M7.56276 3.86677C9.00477 3.86677 9.97749 4.47719 10.5321 4.98732L12.6994 2.91351C11.3684 1.70101 9.63619 0.956787 7.56276 0.956787C4.55921 0.956787 1.96526 2.64591 0.702423 5.10434L3.18544 6.99419C3.80839 5.17963 5.53198 3.86677 7.56276 3.86677Z"
        fill="#EB4335"
      />
    </svg>
  </div>
);

const KakaoIcon: React.FC = () => (
  <div className="w-[17px] h-[17px] shrink-0 relative bg-[#3C1E1E] rounded-full flex items-center justify-center">
    <div className="w-[11px] h-[11px] bg-black rounded-full"></div>
  </div>
);

export const SocialLoginButton: React.FC<SocialLoginButtonProps> = ({
  provider,
  onClick,
  disabled = false
}) => {
  const config = {
    google: {
      text: 'Continue with Google',
      icon: <GoogleIcon />,
      className: 'flex w-[313px] h-[46px] justify-center items-center gap-[9.557px] shadow-[0px_-2.867px_5.734px_0px_rgba(244,245,250,0.60)_inset] cursor-pointer bg-white px-[22.936px] py-[9.557px] rounded-[9.557px] border-[0.956px] border-solid border-[#EFF0F6] max-md:w-full max-md:h-12 max-sm:h-11 hover:bg-gray-50 active:bg-gray-100 transition-colors duration-200'
    },
    kakao: {
      text: 'Continue with Kakao',
      icon: <KakaoIcon />,
      className: 'flex w-[313px] h-[46px] justify-center items-center gap-[9.557px] shadow-[0px_-2.867px_5.734px_0px_rgba(244,245,250,0.60)_inset] cursor-pointer bg-[#FC0] px-[22.936px] py-[9.557px] rounded-[9.557px] border-[0.956px] border-solid border-[#FC0] max-md:w-full max-sm:h-11 hover:bg-[#E6B800] active:bg-[#CCA300] transition-colors duration-200'
    }
  };

  const { text, icon, className } = config[provider];
  const disabledClasses = disabled ? 'opacity-50 cursor-not-allowed' : '';

  return (
    <button
      type="button"
      onClick={onClick}
      disabled={disabled}
      className={`${className} ${disabledClasses}`}
      aria-disabled={disabled}
    >
      {icon}
      <span className="text-[#1A1C1E] text-center text-sm font-semibold leading-[19.6px] tracking-[-0.14px]">
        {text}
      </span>
    </button>
  );
};
