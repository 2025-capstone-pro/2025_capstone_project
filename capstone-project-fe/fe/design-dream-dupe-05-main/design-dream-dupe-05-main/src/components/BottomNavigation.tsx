import React from 'react';

interface NavigationItem {
  id: string;
  imageUrl: string;
  label: string;
  onClick?: () => void;
}

interface BottomNavigationProps {
  items?: NavigationItem[];
  activeItemId?: string;
}

const defaultItems: NavigationItem[] = [
  { id: 'home', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/71d5119df575584469456e37664b3369dedc89c4?placeholderIfAbsent=true', label: '홈' },
  { id: 'search', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/21b68df91c30b5ee34b8ea6ea42f32cbb32dae31?placeholderIfAbsent=true', label: '검색' },
  { id: 'workout', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/f08d67a334bad9fef72d452aa7c136d753de7182?placeholderIfAbsent=true', label: '운동' },
  { id: 'profile', imageUrl: 'https://cdn.builder.io/api/v1/image/assets/5badb085e309497795b1b7a2d35530d5/f2311c030041f14f41f5c3645dc3df931c4b7154?placeholderIfAbsent=true', label: '프로필' }
];

export const BottomNavigation: React.FC<BottomNavigationProps> = ({
  items = defaultItems,
  activeItemId = 'home'
}) => {
  return (
    <nav className="flex w-full flex-col overflow-hidden items-stretch bg-[#131313] mt-2 pb-3.5">
      <div className="border bg-[#232323] min-h-px w-full border-[rgba(35,35,35,1)] border-solid" />
      <div className="self-center flex w-full max-w-[290px] items-stretch gap-5 justify-between mt-[13px]">
        {items.map((item) => (
          <button
            key={item.id}
            onClick={item.onClick}
            className={`focus:outline-none focus:ring-2 focus:ring-[#64FFCE] rounded p-1 transition-all duration-200 ${
              activeItemId === item.id ? 'opacity-100' : 'opacity-70 hover:opacity-100'
            }`}
            aria-label={item.label}
          >
            <img
              src={item.imageUrl}
              alt={item.label}
              className="aspect-[1] object-contain w-[26px] shrink-0"
            />
          </button>
        ))}
      </div>
    </nav>
  );
};
