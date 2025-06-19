import React from 'react';
import { cn } from '@/lib/utils';

interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  error?: string;
}

export const FormInput = React.forwardRef<HTMLInputElement, FormInputProps>(
  ({ label, error, className, ...props }, ref) => {
    return (
      <div className="w-full h-[39px] shrink-0 relative max-md:w-full max-sm:relative max-sm:mb-[15px] max-sm:left-auto max-sm:top-auto">
        <div className="w-full h-[39px] shrink-0 absolute bg-[#D9D9D9] rounded-[5px] left-0 top-0" />
        <input
          ref={ref}
          className={cn(
            "w-full h-full absolute left-0 top-0 bg-transparent rounded-[5px] px-4 py-3 text-sm text-black placeholder:text-black focus:outline-none focus:ring-2 focus:ring-white/20",
            className
          )}
          placeholder={label}
          {...props}
        />
        {error && (
          <span className="absolute -bottom-5 left-0 text-xs text-red-400">
            {error}
          </span>
        )}
      </div>
    );
  }
);

FormInput.displayName = 'FormInput';
