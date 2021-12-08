import { Icon } from '@iconify/react';
import { useRef, useState } from 'react';
import editFill from '@iconify/icons-eva/edit-fill';
import { Link as RouterLink } from 'react-router-dom';
import moreVerticalFill from '@iconify/icons-eva/more-vertical-fill';
import closeCircleFill from '@iconify/icons-eva/close-circle-fill';
import checkmarkCircle2Fill from '@iconify/icons-eva/checkmark-circle-2-fill';
// material
import { Menu, MenuItem, IconButton, ListItemIcon, ListItemText } from '@mui/material';

// ----------------------------------------------------------------------

export default function ProviderCategoryMoreMenu({
  row,
  updateProviderCategoryStatus,
  url,
  onEditClick
}) {
  const ref = useRef(null);
  const [isOpen, setIsOpen] = useState(false);
  const category = row === undefined ? {} : row;
  const updateUrl = url.concat('/').concat('status/').concat(row.id);
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
          sx: { width: 200, maxWidth: '100%' }
        }}
        anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      >
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => onEditClick(row)}
        >
          <ListItemIcon>
            <Icon icon={editFill} width={25} height={25} />
          </ListItemIcon>
          <ListItemText primary="Edit" primaryTypographyProps={{ variant: 'body2' }} />
        </MenuItem>
        <MenuItem
          component={RouterLink}
          to="#"
          sx={{ color: 'text.secondary' }}
          onClick={() => updateProviderCategoryStatus(updateUrl, { status: !category.active })}
        >
          <ListItemIcon>
            <Icon
              icon={category.active ? closeCircleFill : checkmarkCircle2Fill}
              width={25}
              height={25}
            />
          </ListItemIcon>
          <ListItemText
            primary={category.active ? 'Deactivate' : 'Activate'}
            primaryTypographyProps={{ variant: 'body2' }}
          />
        </MenuItem>
      </Menu>
    </>
  );
}
