import React, { useState } from 'react';
import { MobileLayout } from '@/components/MobileLayout';
import { CalendarPlaceholder } from '@/components/CalendarPlaceholder';
import { ExerciseCard } from '@/components/ExerciseCard';
import { NavigationBar } from '@/components/NavigationBar';

export default function Index() {
  const [activeNavItem, setActiveNavItem] = useState<string>('home');

  const handleNavigation = (itemId: string) => {
    setActiveNavItem(itemId);
    console.log(`Navigated to: ${itemId}`);
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <MobileLayout>
        <CalendarPlaceholder />
        <ExerciseCard />
        <NavigationBar onNavigate={handleNavigation} />
      </MobileLayout>
    </div>
  );
}
