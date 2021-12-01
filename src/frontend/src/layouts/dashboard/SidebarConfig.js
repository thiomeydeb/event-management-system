import { Icon } from '@iconify/react';
import pieChart2Fill from '@iconify/icons-eva/pie-chart-2-fill';
import peopleFill from '@iconify/icons-eva/people-fill';
import fileTextFill from '@iconify/icons-eva/file-text-fill';
import lockFill from '@iconify/icons-eva/lock-fill';
import personAddFill from '@iconify/icons-eva/person-add-fill';
import alertTriangleFill from '@iconify/icons-eva/alert-triangle-fill';
import clipboardFill from '@iconify/icons-eva/clipboard-fill';
import ArchiveFill from '@iconify/icons-eva/archive-fill';
import calendarFill from '@iconify/icons-eva/calendar-fill';
import SettingsFill from '@iconify/icons-eva/settings-fill';
import arrowDownFill from '@iconify/icons-eva/arrow-down-fill';
import arrowUpFill from '@iconify/icons-eva/arrow-up-fill';

// ----------------------------------------------------------------------

const getIcon = (name) => <Icon icon={name} width={25} height={25} />;

const sidebarConfig = [
  {
    title: 'dashboard',
    path: '/dashboard/app',
    icon: getIcon(pieChart2Fill)
  },
  {
    title: 'Event Setup',
    path: '/dashboard/eventsetup',
    icon: getIcon(SettingsFill),
    iconClosed: getIcon(arrowDownFill),
    iconOpened: getIcon(arrowUpFill),
    subNav: [
      {
        title: 'eventtype',
        path: '/dashboard/eventsetup/eventtype',
        icon: getIcon(fileTextFill)
      },
      {
        title: 'providercategory',
        path: '/dashboard/eventsetup/providercategory',
        icon: getIcon(ArchiveFill)
      },
      {
        title: 'provider',
        path: '/dashboard/eventsetup/provider',
        icon: getIcon(calendarFill)
      },
      {
        title: 'venue',
        path: '/dashboard/eventsetup/venue',
        icon: getIcon(ArchiveFill)
      }
    ]
  },
  {
    title: 'Plan Event',
    path: '/dashboard/planevent',
    icon: getIcon(clipboardFill)
  },
  {
    title: 'user',
    path: '/dashboard/user',
    icon: getIcon(peopleFill)
  },
  {
    title: 'login',
    path: '/login',
    icon: getIcon(lockFill)
  },
  {
    title: 'register',
    path: '/register',
    icon: getIcon(personAddFill)
  },
  {
    title: 'Not found',
    path: '/404',
    icon: getIcon(alertTriangleFill)
  }
];

export default sidebarConfig;
