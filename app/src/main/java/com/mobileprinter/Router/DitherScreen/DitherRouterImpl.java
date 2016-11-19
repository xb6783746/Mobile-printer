package com.mobileprinter.Router.DitherScreen;

import android.content.Context;
import android.graphics.Bitmap;

import com.mobileprinter.Interfaces.DitherRouter;
import com.mobileprinter.Interfaces.EditScreenRouter;
import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ScreenHost;
import com.mobileprinter.Presenter.EditScreen.ImageEditorPresenterImpl;
import com.mobileprinter.Router.BaseRouter;
import com.mobileprinter.Router.EditScreen.EditScreenRouterImpl;
import com.mobileprinter.View.EditScreen.ImageEditorView;

/**
 * Created by Влад on 16.11.2016.
 */

public class DitherRouterImpl extends BaseRouter implements DitherRouter{


    public DitherRouterImpl(ScreenHost host, Context context) {
        super(host, context);
    }

    @Override
    public void openEditScreen(Bitmap b) {

        ImageEditorView view = new ImageEditorView();
        host.attach(view, view);

        EditScreenRouter router = new EditScreenRouterImpl(host, context);

        ImageEditorPresenterImpl presenter = new ImageEditorPresenterImpl(view, context, router);
        view.setPresenter(presenter);

        presenter.loadImage(b);
    }
}
