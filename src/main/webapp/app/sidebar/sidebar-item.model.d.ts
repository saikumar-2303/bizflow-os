type SidebarItem = {
  name: string;
  route: string;
  translationKey: string;
  icon: string;
  authorities?: any;
  submenu: { name: string; url: string; icon: string; translationKey: string; authorities?: any }[];
};

export default SidebarItem;
