import React from 'react';
import { cn } from '@/lib/utils';

interface ActionButtonProps {
  children: React.ReactNode;
  variant?: 'primary' | 'secondary' | 'outline';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  onClick?: () => void;
  disabled?: boolean;
}

export const ActionButton: React.FC<ActionButtonProps> = ({
  children,
  variant = 'primary',
  size = 'md',
  className,
  onClick,
  disabled = false,
}) => {
  const baseClasses = 'font-normal rounded-[10px] transition-colors duration-200 flex items-center justify-center';
  
  const variantClasses = {
    primary: 'bg-white text-black hover:bg-gray-100',
    secondary: 'bg-black text-white border border-white hover:bg-gray-900',
    outline: 'bg-transparent text-white border border-white hover:bg-white hover:text-black',
  };
  
  const sizeClasses = {
    sm: 'px-2.5 py-1 text-xs h-8',
    md: 'px-2.5 py-2.5 text-sm h-[41px]',
    lg: 'px-4 py-3 text-base h-12',
  };

  return (
    <button
      className={cn(
        baseClasses,
        variantClasses[variant],
        sizeClasses[size],
        disabled && 'opacity-50 cursor-not-allowed',
        className
      )}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
};
