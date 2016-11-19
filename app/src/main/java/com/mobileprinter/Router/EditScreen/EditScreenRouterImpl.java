package com.mobileprinter.Router.EditScreen;

import android.content.Context;
import android.graphics.Bitmap;

import com.mobileprinter.Interfaces.EditScreenRouter;
import com.mobileprinter.Interfaces.ScreenHost;
import com.mobileprinter.Presenter.DitherScreen.DitherScreenPresenterImpl;
import com.mobileprinter.Router.BaseRouter;
import com.mobileprinter.Router.DitherScreen.DitherRouterImpl;
import com.mobileprinter.View.DitherScreen.DitherView;

/**
 * Created by Влад on 13.11.2016.
 */

public class EditScreenRouterImpl extends BaseRouter implements EditScreenRouter{


    public EditScreenRouterImpl(ScreenHost host, Context context) {
        super(host, context);
    }

    @Override
    public void openDitheringScreen(Bitmap image) {

        DitherView view = new DitherView();
        DitherRouterImpl router = new DitherRouterImpl(host, context);
        DitherScreenPresenterImpl presenter = new DitherScreenPresenterImpl(view, router, image);

        view.setPresenter(presenter);

        host.attach(view, view);
        //presenter.start();

    }
}
