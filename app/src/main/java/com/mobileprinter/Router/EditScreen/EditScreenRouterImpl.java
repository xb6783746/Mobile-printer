package com.mobileprinter.Router.EditScreen;

import android.content.Context;
import android.graphics.Bitmap;

import com.mobileprinter.Interfaces.EditScreenRouter;
import com.mobileprinter.Interfaces.ScreenHost;
import com.mobileprinter.Presenter.DitherScreen.DitherScreenPresenterImpl;
import com.mobileprinter.View.DitherScreen.DitherView;

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

        DitherView view = new DitherView();
        DitherScreenPresenterImpl presenter = new DitherScreenPresenterImpl(view, null, image);

        view.setPresenter(presenter);

        host.attach(view);
        //presenter.start();

    }
}
