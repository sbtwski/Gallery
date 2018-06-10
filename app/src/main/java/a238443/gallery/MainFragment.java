package a238443.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MainFragment extends BrowseFragment {
    ArrayObjectAdapter imagesAdapter;
    BackgroundManager backgroundManager;
    Drawable defaultBackground;
    DisplayMetrics metrics;
    ArrayList<Bitmap> images;
    ArrayList<Bitmap> simplified;
    ArrayList<Target> targets;
    IconHeaderItemPresenter presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        targets = new ArrayList<>();
        images = new ArrayList<>();
        simplified = new ArrayList<>();

        setupImages();

        prepareBackgroundManager();
        setupUIElements();

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                presenter = new IconHeaderItemPresenter();
                presenter.addIcons(simplified);
                return presenter;
            }
        });

        buildRowsAdapter();
        setupEventListeners();
    }

    private void prepareBackgroundManager() {
        backgroundManager = BackgroundManager.getInstance(getActivity());
        backgroundManager.attach(getActivity().getWindow());
        defaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        backgroundManager.setDrawable(defaultBackground);
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    private void setupImages() {
        downloadBitmap(getActivity(),"https://bit.ly/2MgcZv1");
        downloadBitmap(getActivity(),"https://bit.ly/2sQg1wW");
        downloadBitmap(getActivity(),"https://bit.ly/2l2Z3YS");
        downloadBitmap(getActivity(),"https://bit.ly/2HC9w68");
        downloadBitmap(getActivity(),"https://bit.ly/2sW2rIh");
        downloadBitmap(getActivity(),"https://bit.ly/2l35lYu");
        downloadBitmap(getActivity(),"https://bit.ly/2HBKFj8");
    }

    private ArrayList<Bitmap> simplifyBitmap() {
        ArrayList<Bitmap> result = new ArrayList<>();

        for(int i=0; i<images.size();i++)
            result.add(Bitmap.createScaledBitmap(images.get(i),600,360,false));

        return result;
    }

    private class IconsThread extends Thread {
        IconHeaderItemPresenter toNotify;
        boolean notified = false;

        IconsThread(IconHeaderItemPresenter toNotify) {
            this.toNotify = toNotify;
        }

        public void run() {
            if(images != null && images.size() > 0 && !notified) {
                ArrayList<Bitmap> simplified = simplifyBitmap();
                toNotify.addIcons(simplified);
                notified = true;
                Log.i("TAG","Thread running");
            }
        }
    }

    protected void updateBackground(Drawable drawable) {
        backgroundManager.setDrawable(drawable);
    }

    protected void clearBackground() {
        backgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                updateBackground(new BitmapDrawable(getResources(), images.get(imagesAdapter.indexOf(row))));
            }
        });

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if(images.size() == 0)
                    clearBackground();
                else
                    updateBackground(new BitmapDrawable(getResources(), images.get(imagesAdapter.indexOf(row))));
            }
        });
    }

    public void downloadBitmap(final Context context, final String url) {
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("TAG","loaded");
                images.add(bitmap);
                simplified.add(Bitmap.createScaledBitmap(bitmap,600,360,false));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("TAG","failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("TAG","prepared");
            }
        };
        targets.add(target);
        Picasso.with(context).load(url).into(target);
    }

    private void setupUIElements() {
        setBadgeDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.banner));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.colorSearch));
    }

    private static final int NUM_ROWS = 7;

    private void buildRowsAdapter() {
        imagesAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        for (int i = 0; i < NUM_ROWS; ++i) {
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(
                    new ListRowPresenter());
            IconHeaderItem header = new IconHeaderItem(i,"",i);
            imagesAdapter.add(new ListRow(header, listRowAdapter));
        }
        setAdapter(imagesAdapter);
    }
}