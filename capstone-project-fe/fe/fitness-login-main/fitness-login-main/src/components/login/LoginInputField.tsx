import React, { useState } from 'react';
import { Eye, EyeOff } from 'lucide-react';

interface LoginInputFieldProps {
  label: string;
  type: 'email' | 'password' | 'text';
  value: string;
  onChange: (value: string) => void;
  placeholder?: string;
  required?: boolean;
  error?: string;
}

export const LoginInputField: React.FC<LoginInputFieldProps> = ({
  label,
  type,
  value,
  onChange,
  placeholder,
  required = false,
  error
}) => {
  const [showPassword, setShowPassword] = useState(false);
  const [isFocused, setIsFocused] = useState(false);

  const inputType = type === 'password' && showPassword ? 'text' : type;
  const displayValue = type === 'password' && value && !showPassword ? '*'.repeat(value.length) : value;

  return (
    <div className="flex w-[313px] flex-col items-start gap-[1.911px] max-md:w-full">
      <label 
        className="text-[#6C7278] text-xs font-medium leading-[19.2px] tracking-[-0.24px] h-5 gap-[9.557px] rounded-[95.566px]"
        htmlFor={`input-${label.toLowerCase()}`}
      >
        {label}
      </label>
      <div className="flex h-11 items-center gap-[9.557px] self-stretch shadow-[0px_0.956px_1.911px_0px_rgba(228,229,231,0.24)] relative bg-white px-[13.379px] py-[25.803px] rounded-[9.557px] border-[0.956px] border-solid border-[#EDF1F3] max-md:h-12 max-sm:h-11 max-sm:px-3 max-sm:py-5">
        <input
          id={`input-${label.toLowerCase()}`}
          type={inputType}
          value={isFocused ? value : displayValue}
          onChange={(e) => onChange(e.target.value)}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          placeholder={placeholder}
          required={required}
          className="w-[198px] h-5 overflow-hidden text-[#1A1C1E] text-ellipsis whitespace-nowrap text-sm font-medium leading-[19.6px] tracking-[-0.14px] gap-[11.468px] flex-[1_0_0] bg-transparent border-none outline-none"
          aria-invalid={error ? 'true' : 'false'}
          aria-describedby={error ? `${label.toLowerCase()}-error` : undefined}
        />
        {type === 'password' && (
          <button
            type="button"
            onClick={() => setShowPassword(!showPassword)}
            className="w-[15px] h-[15px] absolute right-[13.379px] flex items-center justify-center"
            aria-label={showPassword ? 'Hide password' : 'Show password'}
          >
            {showPassword ? (
              <Eye className="w-[11px] h-[11px] stroke-[#ACB5BB] stroke-[1.242px]" />
            ) : (
              <EyeOff className="w-[11px] h-[11px] stroke-[#ACB5BB] stroke-[1.242px]" />
            )}
          </button>
        )}
      </div>
      {error && (
        <span 
          id={`${label.toLowerCase()}-error`}
          className="text-red-500 text-xs mt-1"
          role="alert"
        >
          {error}
        </span>
      )}
    </div>
  );
};
