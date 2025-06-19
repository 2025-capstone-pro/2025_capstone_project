
import React, { useState } from 'react';
import { Header } from '@/components/Header';
import { TagPill } from '@/components/TagPill';
import { WorkoutCard } from '@/components/WorkoutCard';
import { AdBanner } from '@/components/AdBanner';
import { BottomNavigation } from '@/components/BottomNavigation';

const Index: React.FC = () => {
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [activeNavItem, setActiveNavItem] = useState('home');

  const workoutTags = [
    '요가', '필라테스', '기구없이', '하체', '상체', '다이어트', '체력 강화', '유산소', '복근'
  ];

  const workoutRecommendations = [
    { id: '1', title: '운동 타이틀\n2줄까지', duration: '16min' },
    { id: '2', title: '운동 타이틀\n2줄까지', duration: '16min' }
  ];

  const navigationItems = [
    { id: 'home', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/71d5119df575584469456e37664b3369dedc89c4?placeholderIfAbsent=true', label: '홈' },
    { id: 'search', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/21b68df91c30b5ee34b8ea6ea42f32cbb32dae31?placeholderIfAbsent=true', label: '검색' },
    { id: 'workout', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/f08d67a334bad9fef72d452aa7c136d753de7182?placeholderIfAbsent=true', label: '운동' },
    { id: 'profile', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/f2311c030041f14f41f5c3645dc3df931c4b7154?placeholderIfAbsent=true', label: '프로필' }
  ];

  const handleTagClick = (tag: string) => {
    setSelectedTags(prev => 
      prev.includes(tag) 
        ? prev.filter(t => t !== tag)
        : [...prev, tag]
    );
  };

  const handleWorkoutClick = (workoutId: string) => {
    console.log('Workout clicked:', workoutId);
  };

  const handleNavClick = (itemId: string) => {
    setActiveNavItem(itemId);
    console.log('Navigation clicked:', itemId);
    
    if (itemId === 'profile') {
      window.open('https://figma-to-living-pixels.lovable.app/', '_blank');
    } else if (itemId === 'search') {
      window.open('https://fitness-cal.lovable.app/', '_blank');
    }
  };

  const handleProfileClick = () => {
    console.log('Profile clicked');
    window.open('https://fitness-login.lovable.app/', '_blank');
  };

  const handleAdClick = () => {
    console.log('Ad clicked');
  };

  return (
    <div className="bg-black flex max-w-[480px] w-full flex-col overflow-hidden items-stretch mx-auto py-12 rounded-[10px]">
      <Header 
        onProfileClick={handleProfileClick}
      />
      
      <img
        src="https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/636a5cadae0ef095a1eb7459232cc2764f599ebf?placeholderIfAbsent=true"
        alt="메인 배너 이미지"
        className="aspect-[2.07] object-contain w-full mt-3.5"
      />
      
      <main className="flex w-full flex-col font-normal mt-[30px] pl-[22px]">
        <section aria-label="운동 카테고리 필터">
          <div className="flex w-full items-stretch gap-2 text-sm text-[#64FFCE] whitespace-nowrap">
            {workoutTags.slice(0, 5).map((tag) => (
              <TagPill
                key={tag}
                isSelected={selectedTags.includes(tag)}
                onClick={() => handleTagClick(tag)}
              >
                {tag}
              </TagPill>
            ))}
          </div>
          
          <div className="flex w-full gap-2 ml-2.5 mt-2">
            <div className="self-stretch flex flex-col">
              <div className="flex w-full items-stretch gap-2 text-sm text-[#64FFCE] ml-[13px]">
                {workoutTags.slice(5, 7).map((tag) => (
                  <TagPill
                    key={tag}
                    isSelected={selectedTags.includes(tag)}
                    onClick={() => handleTagClick(tag)}
                    className="whitespace-nowrap"
                  >
                    {tag}
                  </TagPill>
                ))}
              </div>
              
              <h2 className="text-white text-lg leading-[1.4] mt-[25px]">
                오늘의 추천 운동
              </h2>
            </div>
            
            {workoutTags.slice(7).map((tag) => (
              <TagPill
                key={tag}
                isSelected={selectedTags.includes(tag)}
                onClick={() => handleTagClick(tag)}
                className="text-sm whitespace-nowrap"
              >
                {tag}
              </TagPill>
            ))}
          </div>
        </section>
        
        <section 
          className="self-center flex w-full max-w-[278px] items-stretch gap-[40px_100px] text-white mt-[183px]"
          aria-label="추천 운동 목록"
        >
          {workoutRecommendations.map((workout) => (
            <WorkoutCard
              key={workout.id}
              title={workout.title}
              duration={workout.duration}
              onClick={() => handleWorkoutClick(workout.id)}
            />
          ))}
        </section>
        
        <AdBanner onClick={handleAdClick} />
      </main>
      
      <BottomNavigation
        items={navigationItems.map(item => ({
          ...item,
          onClick: () => handleNavClick(item.id)
        }))}
        activeItemId={activeNavItem}
      />
    </div>
  );
};

export default Index;
