import { Icon } from '@iconify/react';
import pieChart2Fill from '@iconify/icons-eva/pie-chart-2-fill';
import peopleFill from '@iconify/icons-eva/people-fill';
import fileTextFill from '@iconify/icons-eva/file-text-fill';
import lockFill from '@iconify/icons-eva/lock-fill';
import personAddFill from '@iconify/icons-eva/person-add-fill';
import alertTriangleFill from '@iconify/icons-eva/alert-triangle-fill';
import clipboardFill from '@iconify/icons-eva/clipboard-fill';
import settingsFill from '@iconify/icons-eva/settings-2-fill';
import ArchiveFill from '@iconify/icons-eva/archive-fill';
import calendarFill from '@iconify/icons-eva/calendar-fill';
import SettingsFill from '@iconify/icons-eva/settings-fill';
import arrowDownFill from '@iconify/icons-eva/arrow-down-fill';
import arrowUpFill from '@iconify/icons-eva/arrow-up-fill';
import bookOpenFill from '@iconify/icons-eva/book-open-fill';
import calendarOutline from '@iconify/icons-eva/calendar-outline';

// ----------------------------------------------------------------------

const getIcon = (name) => <Icon icon={name} width={25} height={25} />;

export const plannerConfig = [
  {
    title: 'Plan Event',
    path: '/dashboard/planevent',
    icon: getIcon(clipboardFill)
  }
];

export const clientConfig = [
  {
    title: 'Event (Book)',
    path: '/dashboard/event',
    icon: getIcon(calendarOutline)
  }
];

export const adminConfig = [
  {
    title: 'dashboard',
    path: '/dashboard/app',
    icon: getIcon(pieChart2Fill)
  },
  {
    title: 'Event Type',
    path: '/dashboard/eventsetup/eventtype',
    icon: getIcon(fileTextFill)
  },
  {
    title: 'Provider Category',
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
  },
  {
    title: 'Plan Event',
    path: '/dashboard/planevent',
    icon: getIcon(clipboardFill)
  },
  {
    title: 'Manage Event',
    path: '/dashboard/manageevent',
    icon: getIcon(settingsFill)
  },
  {
    title: 'user',
    path: '/dashboard/user',
    icon: getIcon(peopleFill)
  },
  {
    title: 'Logs',
    path: '/dashboard/logs',
    icon: getIcon(bookOpenFill)
  }
];

const sidebarConfig = adminConfig;

export default sidebarConfig;
