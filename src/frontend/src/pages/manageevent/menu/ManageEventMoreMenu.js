import { Icon } from '@iconify/react';
import { useRef, useState } from 'react';
import eyeFill from '@iconify/icons-eva/eye-fill';
import { Link as RouterLink } from 'react-router-dom';
import closeCircleFill from '@iconify/icons-eva/close-circle-fill';
import loaderOutline from '@iconify/icons-eva/loader-outline';
import checkmarkCircleFill from '@iconify/icons-eva/checkmark-circle-2-fill';
import bookOpenFill from '@iconify/icons-eva/book-open-fill';
import moreVerticalFill from '@iconify/icons-eva/more-vertical-fill';
// material
import { Menu, MenuItem, IconButton, ListItemIcon, ListItemText } from '@mui/material';

// ----------------------------------------------------------------------

export default function ManageEventMoreMenu({ row, onChangeViewClick, updateEventStatus }) {
  const ref = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const event = row === undefined ? {} : row;
  const { eventId } = event;
  return (
    <>
      <IconButton ref={ref} onClick={() => setIsOpen(true)}>
        <Icon icon={moreVerticalFill} width={20} height={20} />
      </IconButton>

      <Menu
        open={isOpen}
        anchorEl={ref.current}
        onClose={() => setIsOpen(false)}
        PaperProps={{
          sx: { width: 220, maxWidth: '100%' }
        }}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => onChangeViewClick(event, 'view')}
        >
          <ListItemIcon>
            <Icon icon={eyeFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText primary="View" primaryTypographyProps={{ variant: 'body2' }} />
        </MenuItem>
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => updateEventStatus(eventId, { status: 4 })}
        >
          <ListItemIcon>
            <Icon icon={closeCircleFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText primary="Update to Cancel" primaryTypographyProps={{ variant: 'body2' }} />
        </MenuItem>
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => updateEventStatus(eventId, { status: 1 })}
        >
          <ListItemIcon>
            <Icon icon={bookOpenFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText
            primary="Set Planner Assigned"
            primaryTypographyProps={{ variant: 'body2' }}
          />
        </MenuItem>
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => updateEventStatus(eventId, { status: 2 })}
        >
          <ListItemIcon>
            <Icon icon={loaderOutline} width={25} height={25} />
          </ListItemIcon>
          <ListItemText primary="Set In progress" primaryTypographyProps={{ variant: 'body2' }} />
        </MenuItem>
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => updateEventStatus(eventId, { status: 3 })}
        >
          <ListItemIcon>
            <Icon icon={checkmarkCircleFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText primary="Set Complete" primaryTypographyProps={{ variant: 'body2' }} />
        </MenuItem>
      </Menu>
    </>
  );
}
