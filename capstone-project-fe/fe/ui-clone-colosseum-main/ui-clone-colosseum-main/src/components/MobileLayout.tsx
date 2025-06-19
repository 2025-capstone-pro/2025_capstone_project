import React from 'react';

interface MobileLayoutProps {
  children: React.ReactNode;
}

export const MobileLayout: React.FC<MobileLayoutProps> = ({ children }) => {
  return (
    <main className="w-[360px] h-[800px] relative bg-black mx-auto my-0 rounded-[10px] max-md:w-full max-md:max-w-[360px] max-sm:w-full max-sm:max-w-[360px] max-sm:h-screen">
      {children}
    </main>
  );
};
