import { Navigate, useRoutes } from 'react-router-dom';
// layouts
import DashboardLayout from './layouts/dashboard';
import LogoOnlyLayout from './layouts/LogoOnlyLayout';
//
import Login from './pages/login/Login';
import Register from './pages/registration/Register';
import DashboardApp from './pages/DashboardApp';
import User from './pages/User';
import NotFound from './pages/Page404';
import EventType from './pages/eventtype/EventType';
import Provider from './pages/provider/Provider';
import ProviderCategory from './pages/providercategory/ProviderCategory';
import Venue from './pages/venue/Venue';
import PlanEvent from './pages/planevent/PlanEvent';
import { EventSetup } from './pages/EventSetup';
import Event from './pages/event/Event';
import ManageEvent from './pages/manageevent/ManageEvent';
import EventLogs from './pages/logs/EventLogs';

// ----------------------------------------------------------------------

export default function Router() {
  return useRoutes([
    {
      path: '/dashboard',
      element: <DashboardLayout />,
      children: [
        { element: <Navigate to="/dashboard/app" replace /> },
        { path: 'app', element: <DashboardApp /> },
        { path: 'user', element: <User /> },
        { path: 'planevent', element: <PlanEvent /> },
        { path: 'eventsetup', element: <EventSetup /> },
        { path: 'eventsetup/eventtype', element: <EventType /> },
        { path: 'eventsetup/providercategory', element: <ProviderCategory /> },
        { path: 'eventsetup/venue', element: <Venue /> },
        { path: 'eventsetup/provider', element: <Provider /> },
        { path: 'event', element: <Event /> },
        { path: 'manageevent', element: <ManageEvent /> },
        { path: 'logs', element: <EventLogs /> }
      ]
    },
    {
      path: '/',
      element: <LogoOnlyLayout />,
      children: [
        { path: 'login', element: <Login /> },
        { path: 'register', element: <Register /> },
        { path: '404', element: <NotFound /> },
        { path: '/', element: <Navigate to="/dashboard" /> },
        { path: '*', element: <Navigate to="/404" /> }
      ]
    },
    { path: '*', element: <Navigate to="/404" replace /> }
  ]);
}
