
import React from 'react';
import { cn } from '@/lib/utils';

interface CustomCheckboxProps {
  id: string;
  label: string;
  checked: boolean;
  onChange: (checked: boolean) => void;
  className?: string;
}

export const CustomCheckbox: React.FC<CustomCheckboxProps> = ({
  id,
  label,
  checked,
  onChange,
  className
}) => {
  return (
    <div className={cn("h-[25px] shrink-0 relative flex items-center max-sm:relative max-sm:mb-[15px] max-sm:left-auto max-sm:top-auto", className)}>
      <div className="relative">
        <input
          type="checkbox"
          id={id}
          checked={checked}
          onChange={(e) => onChange(e.target.checked)}
          className="sr-only"
        />
        <label
          htmlFor={id}
          className="flex items-center cursor-pointer"
        >
          <div className="w-[25px] h-[25px] shrink-0 bg-[#D9D9D9] rounded-[5px] relative">
            {checked && (
              <div className="w-[21px] h-[21px] shrink-0 absolute bg-black rounded-[5px] left-0.5 top-0.5" />
            )}
          </div>
          <span className="font-normal text-sm text-white ml-[14px] whitespace-nowrap">
            {label}
          </span>
        </label>
      </div>
    </div>
  );
};
