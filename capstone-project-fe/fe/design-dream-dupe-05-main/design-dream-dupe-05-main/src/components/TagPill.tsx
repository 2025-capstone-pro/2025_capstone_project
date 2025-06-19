
import React from 'react';
import { cn } from '@/lib/utils';

interface TagPillProps {
  children: React.ReactNode;
  isSelected?: boolean;
  onClick?: () => void;
  className?: string;
}

export const TagPill: React.FC<TagPillProps> = ({
  children,
  isSelected = false,
  onClick,
  className
}) => {
  return (
    <button
      onClick={onClick}
      className={cn(
        "px-3 py-1.5 h-8 rounded-full border border-[#64FFCE] text-[#64FFCE] text-sm transition-colors duration-200 whitespace-nowrap flex items-center justify-center",
        isSelected && "bg-[#64FFCE] text-black",
        !isSelected && "hover:bg-[#64FFCE] hover:bg-opacity-10",
        className
      )}
    >
      {children}
    </button>
  );
};
