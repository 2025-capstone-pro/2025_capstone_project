
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { FormInput } from './ui/form-input';
import { CustomCheckbox } from './ui/custom-checkbox';

interface SignupFormData {
  nickname: string;
  gender: string;
  email: string;
  phone: string;
}

interface AgreementState {
  all: boolean;
  age: boolean;
  privacy: boolean;
}

export const SignupForm: React.FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    setValue
  } = useForm<SignupFormData>();

  const [agreements, setAgreements] = useState<AgreementState>({
    all: false,
    age: false,
    privacy: false
  });

  const handleAgreementChange = (key: keyof AgreementState, value: boolean) => {
    if (key === 'all') {
      setAgreements({
        all: value,
        age: value,
        privacy: value
      });
    } else {
      const newAgreements = { ...agreements, [key]: value };
      newAgreements.all = newAgreements.age && newAgreements.privacy;
      setAgreements(newAgreements);
    }
  };

  const onSubmit = (data: SignupFormData) => {
    if (!agreements.age || !agreements.privacy) {
      alert('필수 약관에 동의해주세요.');
      return;
    }
    console.log('Form submitted:', { ...data, agreements });
    alert('회원가입이 완료되었습니다!');
  };

  return (
    <main className="flex w-[360px] flex-col items-center h-auto min-h-[640px] box-border bg-black pt-[38px] pb-[50px] px-[19px] rounded-[10px] max-md:w-full max-md:max-w-[360px] max-md:mx-auto max-md:my-0 max-sm:w-full max-sm:h-auto max-sm:min-h-screen max-sm:p-5">
      <div className="w-[322px] relative max-md:w-full max-md:max-w-[322px]">
        <header>
          <h1 className="font-normal text-xl text-white w-[120px] text-center absolute h-[23px] left-[101px] top-0 max-sm:relative max-sm:w-full max-sm:text-center max-sm:mb-[30px] max-sm:left-auto max-sm:top-auto">
            회원가입
          </h1>
        </header>

        <form 
          onSubmit={handleSubmit(onSubmit)}
          className="w-[322px] absolute left-0 top-[34px] max-md:w-full max-sm:relative max-sm:w-full max-sm:mb-[40px] max-sm:left-auto max-sm:top-auto"
        >
          <fieldset className="border-0 p-0 m-0">
            <legend className="sr-only">회원 정보 입력</legend>
            
            <div className="space-y-[20px] max-sm:space-y-[25px]">
              <FormInput
                {...register('nickname', { 
                  required: '닉네임을 입력해주세요',
                  minLength: { value: 2, message: '닉네임은 2자 이상이어야 합니다' }
                })}
                label="닉네임"
                error={errors.nickname?.message}
                autoComplete="username"
              />

              <FormInput
                {...register('gender', { required: '성별을 입력해주세요' })}
                label="성별"
                error={errors.gender?.message}
                list="gender-options"
              />
              <datalist id="gender-options">
                <option value="남성" />
                <option value="여성" />
                <option value="기타" />
              </datalist>

              <FormInput
                {...register('email', { 
                  required: '이메일을 입력해주세요',
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: '올바른 이메일 형식을 입력해주세요'
                  }
                })}
                label="메일"
                type="email"
                error={errors.email?.message}
                autoComplete="email"
              />

              <FormInput
                {...register('phone', { 
                  required: '전화번호를 입력해주세요',
                  pattern: {
                    value: /^[0-9-+\s()]+$/,
                    message: '올바른 전화번호 형식을 입력해주세요'
                  }
                })}
                label="전화번호"
                type="tel"
                error={errors.phone?.message}
                autoComplete="tel"
              />
            </div>
          </fieldset>
        </form>

        <fieldset className="w-72 absolute left-1 top-[280px] max-md:w-full max-md:max-w-72 max-sm:relative max-sm:w-full max-sm:left-auto max-sm:top-auto max-sm:mt-[30px] border-0 p-0 m-0">
          <legend className="sr-only">약관 동의</legend>
          
          <div className="space-y-[11px] max-sm:space-y-[15px]">
            <CustomCheckbox
              id="agree-all"
              label="모두 동의합니다."
              checked={agreements.all}
              onChange={(checked) => handleAgreementChange('all', checked)}
              className="w-[137px]"
            />

            <CustomCheckbox
              id="agree-age"
              label="만 14세이상 가입 동의 (필수)"
              checked={agreements.age}
              onChange={(checked) => handleAgreementChange('age', checked)}
              className="w-52"
            />

            <CustomCheckbox
              id="agree-privacy"
              label="사용자의 개인정보, 이용약관에 동의합니다."
              checked={agreements.privacy}
              onChange={(checked) => handleAgreementChange('privacy', checked)}
              className="w-72"
            />
          </div>
        </fieldset>

        <button
          type="submit"
          onClick={handleSubmit(onSubmit)}
          className="absolute top-[420px] left-1/2 transform -translate-x-1/2 bg-white text-black px-8 py-3 rounded-[5px] font-medium hover:bg-gray-100 transition-colors max-sm:relative max-sm:top-auto max-sm:left-auto max-sm:transform-none max-sm:mt-8 max-sm:w-full"
        >
          회원가입
        </button>
      </div>
    </main>
  );
};
