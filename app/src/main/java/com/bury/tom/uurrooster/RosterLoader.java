package com.bury.tom.uurrooster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

public class RosterLoader extends AsyncTaskLoader {

    private String url;

    private final String LOG_TAG = RosterLoader.class.getSimpleName();

    public RosterLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Nullable
    @Override
    public Object loadInBackground() {
        if (this.url == null) {
            System.err.println("You can't load a URL that is null!");
            return null;
        }


        return QueryUtils.fetchData(this.url);
    }
}

