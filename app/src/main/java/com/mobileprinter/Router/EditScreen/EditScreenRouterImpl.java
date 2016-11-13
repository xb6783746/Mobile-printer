package com.mobileprinter.Router.EditScreen;

import android.content.Context;
import android.graphics.Bitmap;

import com.mobileprinter.Interfaces.EditScreenRouter;
import com.mobileprinter.Interfaces.ScreenHost;

/**
 * Created by Влад on 13.11.2016.
 */

public class EditScreenRouterImpl implements EditScreenRouter{


    public EditScreenRouterImpl(ScreenHost host, Context context) {
        this.host = host;
        this.context = context;
    }

    private ScreenHost host;
    private Context context;



    @Override
    public void openDitheringScreen(Bitmap image) {

    }
}
