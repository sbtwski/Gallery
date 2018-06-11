package a238443.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class MainFragment extends BrowseFragment {
    ArrayObjectAdapter imagesAdapter;
    BackgroundManager backgroundManager;
    Drawable defaultBackground;
    DisplayMetrics metrics;
    ArrayList<BitmapDrawable> images;
    ArrayList<BitmapDrawable> simplified;
    ArrayList<Target> targets;
    IconHeaderItemPresenter presenter;

    public static final int IMAGES_AMOUNT = 11;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        targets = new ArrayList<>();
        images = new ArrayList<>();
        simplified = new ArrayList<>();
        presenter = new IconHeaderItemPresenter();

        setupImages();
        prepareBackgroundManager();
        setupUIElements();
        buildRowsAdapter();
        setupEventListeners();

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                presenter.addIcons(simplified);
                return presenter;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        clearBackground();
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
        downloadBitmap(getActivity(),"https://bit.ly/2HHDeqr");
        downloadBitmap(getActivity(),"https://bit.ly/2JKrvMZ");
        downloadBitmap(getActivity(),"https://bit.ly/2sQg1wW");
        downloadBitmap(getActivity(),"https://bit.ly/2l2Z3YS");
        downloadBitmap(getActivity(),"https://bit.ly/2HC9w68");
        downloadBitmap(getActivity(),"https://bit.ly/2sW2rIh");
        downloadBitmap(getActivity(),"https://bit.ly/2l35lYu");
        downloadBitmap(getActivity(),"https://bit.ly/2HBKFj8");
        downloadBitmap(getActivity(),"https://bit.ly/2sZxJOK");
        downloadBitmap(getActivity(),"https://bit.ly/2JCjIO7");
        downloadBitmap(getActivity(),"https://bit.ly/2LHM0r4");
    }

    protected void updateBackground(Drawable drawable) {
        backgroundManager.setDrawable(drawable);
    }

    protected void clearBackground() {
        backgroundManager.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.default_background));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CosmosActivity.class);
                startActivity(intent);
            }
        });

        setOnItemViewClickedListener(new OnItemViewClickedListener() {
            @Override
            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                updateBackground(images.get(imagesAdapter.indexOf(row)));
            }
        });

        setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                if(images.size() == 0)
                    clearBackground();
                else
                    updateBackground(images.get(imagesAdapter.indexOf(row)));
            }
        });
    }

    public void downloadBitmap(final Context context, final String url) {
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.i("TAG","Bitmap loaded");
                images.add(new BitmapDrawable(getResources(), bitmap));

                Bitmap icon;
                if(simplified.size() == 0)
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.header);
                else
                    icon = Bitmap.createScaledBitmap(bitmap, 480, 300, false);

                simplified.add(new BitmapDrawable(getResources(), icon));
                presenter.addIcons(simplified);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.i("TAG","Bitmap failed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.i("TAG","Bitmap prepared");
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

    private void buildRowsAdapter() {
        imagesAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        ArrayObjectAdapter listRowAdapter;
        IconHeaderItem header;

        for (int i = 0; i < IMAGES_AMOUNT; i++) {
            listRowAdapter = new ArrayObjectAdapter(new ListRowPresenter());
            header = new IconHeaderItem(i,"",i);
            imagesAdapter.add(new ListRow(header, listRowAdapter));
        }
        setAdapter(imagesAdapter);
    }
}