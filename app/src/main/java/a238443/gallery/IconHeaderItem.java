package a238443.gallery;

import android.support.v17.leanback.widget.HeaderItem;

class IconHeaderItem extends HeaderItem {
    private static final int ICON_NONE = -1;
    private int iconPosition = ICON_NONE;

    IconHeaderItem(long id, String name, int iconPosition) {
        super(id, name);
        this.iconPosition = iconPosition;
    }

    int getIconPosition() {
        return iconPosition;
    }

}
