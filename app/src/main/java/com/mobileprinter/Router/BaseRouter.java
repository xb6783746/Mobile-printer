package com.mobileprinter.Router;

import android.content.Context;

import com.mobileprinter.Interfaces.ScreenHost;

/**
 * Created by Влад on 16.11.2016.
 */

public abstract class BaseRouter {

    public BaseRouter(ScreenHost host, Context context) {
        this.host = host;
        this.context = context;
    }

    protected ScreenHost host;
    protected Context context;
}
