
import React, { useState } from 'react';
import { LoginHeader } from './LoginHeader';
import { LoginInputField } from './LoginInputField';
import { LoginButton } from './LoginButton';
import { SocialLoginButton } from './SocialLoginButton';
import { LoginFooter } from './LoginFooter';

interface LoginFormData {
  email: string;
  password: string;
}

interface LoginFormErrors {
  email?: string;
  password?: string;
}

export const LoginForm: React.FC = () => {
  const [formData, setFormData] = useState<LoginFormData>({
    email: '',
    password: ''
  });
  
  const [errors, setErrors] = useState<LoginFormErrors>({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const validateForm = (): boolean => {
    const newErrors: LoginFormErrors = {};

    if (!formData.email) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }

    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      console.log('Login attempt:', formData);
      // Handle successful login here
    } catch (error) {
      console.error('Login failed:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleInputChange = (field: keyof LoginFormData) => (value: string) => {
    setFormData(prev => ({ ...prev, [field]: value }));
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({ ...prev, [field]: undefined }));
    }
  };

  const handleForgotPassword = () => {
    console.log('Forgot password clicked');
    // Handle forgot password logic here
  };

  const handleSocialLogin = (provider: 'google' | 'kakao') => {
    console.log(`${provider} login clicked`);
    // Handle social login logic here
  };

  const handleSignUp = () => {
    console.log('Sign up clicked');
    window.location.href = 'https://fitness-signin.lovable.app/';
  };

  return (
    <div className="inline-flex flex-col justify-end items-center w-[360px] h-[780px] box-border bg-black pl-0 pr-[4.626px] pt-[69px] pb-[32.478px] rounded-[10px] max-md:w-full max-md:max-w-[400px] max-md:h-auto max-md:min-h-screen max-md:px-5 max-md:py-10 max-sm:px-4 max-sm:py-[30px]">
      <div className="flex w-[358px] flex-col justify-between items-center h-[679px] box-border pt-0 pb-[15.291px] px-[22.936px] max-md:w-full max-md:h-auto max-md:gap-[30px] max-md:pt-0 max-md:pb-5 max-md:px-5 max-sm:pt-0 max-sm:pb-4 max-sm:px-4">
        <form onSubmit={handleSubmit} className="flex flex-col items-start gap-[30.581px]" noValidate>
          <LoginHeader
            title="Sign in to your Account"
            subtitle="Enter your email and password to log in"
          />
          
          <fieldset className="flex flex-col items-start gap-[22.936px] self-stretch border-none p-0 m-0">
            <legend className="sr-only">Login credentials</legend>
            
            <div className="flex flex-col items-start gap-[15.291px] self-stretch">
              <LoginInputField
                label="Email"
                type="email"
                value={formData.email}
                onChange={handleInputChange('email')}
                placeholder="Enter your email"
                required
                error={errors.email}
              />
              
              <LoginInputField
                label="Password"
                type="password"
                value={formData.password}
                onChange={handleInputChange('password')}
                placeholder="Enter your password"
                required
                error={errors.password}
              />
              
              <button
                type="button"
                onClick={handleForgotPassword}
                className="self-stretch text-[#4D81E7] text-right text-[11px] font-semibold leading-[15.4px] tracking-[-0.115px] hover:text-[#3E6FD6] transition-colors duration-200"
              >
                Forgot Password ?
              </button>
            </div>
            
            <div className="flex flex-col items-start gap-[22.936px] self-stretch">
              <LoginButton
                type="submit"
                variant="primary"
                disabled={isSubmitting}
              >
                {isSubmitting ? 'Logging in...' : 'Log In'}
              </LoginButton>
              
              <div className="flex flex-col items-start gap-[15.291px] self-stretch">
                <div className="flex items-center gap-[15.291px] self-stretch">
                  <div className="w-[134px] h-px bg-[#EDF1F3]" />
                  <span className="text-[#6C7278] text-center text-[11px] font-normal leading-[16.5px] tracking-[-0.115px]">
                    Or
                  </span>
                  <div className="w-[134px] h-px bg-[#EDF1F3]" />
                </div>
                
                <div className="flex flex-col items-start gap-[14.335px] self-stretch">
                  <SocialLoginButton
                    provider="google"
                    onClick={() => handleSocialLogin('google')}
                    disabled={isSubmitting}
                  />
                  
                  <SocialLoginButton
                    provider="kakao"
                    onClick={() => handleSocialLogin('kakao')}
                    disabled={isSubmitting}
                  />
                </div>
              </div>
            </div>
          </fieldset>
        </form>
        
        <LoginFooter onSignUpClick={handleSignUp} />
      </div>
    </div>
  );
};
