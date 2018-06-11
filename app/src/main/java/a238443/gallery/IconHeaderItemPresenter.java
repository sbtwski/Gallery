package a238443.gallery;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
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
    private ArrayList<BitmapDrawable> icons;
    private Handler updateHandler;
    private ArrayList<View> viewsToUpdate = new ArrayList<>();
    private ArrayList<Integer> updatePositions = new ArrayList<>();
    private boolean iconsLoaded = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        updateHandler = new Handler();
        updateIcons.run();

        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.header_main, viewGroup, false);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
        return new ViewHolder(view);
    }

    void addIcons(ArrayList<BitmapDrawable> iconsList) {
        if(!iconsLoaded) {
            if(iconsList != null) {
                icons = iconsList;
                if(icons.size() == MainFragment.IMAGES_AMOUNT)
                    iconsLoaded = true;
            }
        }
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        IconHeaderItem iconHeaderItem = (IconHeaderItem) ((ListRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setFocusable(true);
        rootView.setClickable(true);

        ImageView iconView = rootView.findViewById(R.id.header_icon);

        if(icons.size() == MainFragment.IMAGES_AMOUNT) {
            iconView.setImageDrawable(icons.get(iconHeaderItem.getIconPosition()));
        }
        else {
            Log.i("TAG","Icon not found ");
            iconView.setImageDrawable(ContextCompat.getDrawable(rootView.getContext(), R.drawable.ic_image));
            viewsToUpdate.add(rootView);
            updatePositions.add(iconHeaderItem.getIconPosition());
        }
    }

    private Runnable updateIcons = new Runnable() {
        @Override
        public void run() {
            if(viewsToUpdate.size()>0 && icons.size() > 0) {
                View rootView;
                ImageView icon;
                while (viewsToUpdate.size() > 0 && updatePositions.get(0) < icons.size()) {
                    rootView = viewsToUpdate.get(0);
                    icon = rootView.findViewById(R.id.header_icon);
                    icon.setImageDrawable(icons.get(updatePositions.get(0)));
                    viewsToUpdate.remove(0);
                    updatePositions.remove(0);
                }
            }
            updateHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {}
}
