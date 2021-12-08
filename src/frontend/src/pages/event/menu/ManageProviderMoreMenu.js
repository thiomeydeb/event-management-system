import { Icon } from '@iconify/react';
import { useRef, useState } from 'react';
import refreshFill from '@iconify/icons-eva/refresh-fill';
import { Link as RouterLink } from 'react-router-dom';
import trash2Outline from '@iconify/icons-eva/trash-2-outline';
import moreVerticalFill from '@iconify/icons-eva/more-vertical-fill';
import closeCircleFill from '@iconify/icons-eva/close-circle-fill';
import checkmarkCircle2Fill from '@iconify/icons-eva/checkmark-circle-2-fill';
// material
import { Menu, MenuItem, IconButton, ListItemIcon, ListItemText } from '@mui/material';

// ----------------------------------------------------------------------

export default function ManageProviderMoreMenu({
  row,
  onChangeViewClick,
  status,
  plannedDetailsId,
  eventId,
  onUpdateProviderStatus
}) {
  const ref = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const event = row === undefined ? {} : row;
  const menuText = status ? 'Incomplete' : 'Complete';
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
          onClick={() => onUpdateProviderStatus(plannedDetailsId, { status: !status })}
        >
          <ListItemIcon>
            <Icon icon={refreshFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText
            primary={'Update to '.concat(menuText)}
            primaryTypographyProps={{ variant: 'body2' }}
          />
        </MenuItem>
      </Menu>
    </>
  );
}
