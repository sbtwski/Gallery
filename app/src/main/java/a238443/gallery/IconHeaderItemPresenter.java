package a238443.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class IconHeaderItemPresenter extends RowHeaderPresenter {
    private ArrayList<Bitmap> icons;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewGroup nullParent = null;
        View view = inflater.inflate(R.layout.header_main, viewGroup, false);
        return new ViewHolder(view);
    }

    void addIcons(ArrayList<Bitmap> iconsList) {
        icons = iconsList;
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        IconHeaderItem iconHeaderItem = (IconHeaderItem) ((ListRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);

        ImageView iconView = rootView.findViewById(R.id.header_icon);

        if(icons.size() > 0) {
            BitmapDrawable temp = new BitmapDrawable(rootView.getResources(), icons.get(iconHeaderItem.getIconPosition()));
            iconView.setImageDrawable(temp);
        }
        else {
            Log.i("TAG","FAIL");
            iconView.setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_image));
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }
}
