package com.mobileprinter.Presenter.DitherScreen;

import android.graphics.Bitmap;

import com.mobileprinter.ImageLibrary.BinaryImage;
import com.mobileprinter.ImageLibrary.DitherAlgorithms;
import com.mobileprinter.Interfaces.DitherRouter;
import com.mobileprinter.Interfaces.DitherScreen;
import com.mobileprinter.Interfaces.DitherScreenPresenter;

/**
 * Created by Влад on 14.11.2016.
 */

public class DitherScreenPresenterImpl implements DitherScreenPresenter{

    public DitherScreenPresenterImpl(DitherScreen screen, DitherRouter router, Bitmap current) {
        this.screen = screen;
        this.current = current;
        this.router = router;

        binary = new BinaryImage(current);
    }

    private Bitmap current;
    private BinaryImage binary;
    private DitherScreen screen;
    private DitherRouter router;

    @Override
    public void created() {
        screen.setImage(current);
    }

    @Override
    public void floydSteinbergDithering() {

        BinaryImage clone = binary.copy();

        DitherAlgorithms.floydSteinbergDithering(clone);

        screen.setImage(clone.getBitmap());

    }
    @Override
    public void atkinsonDithering() {
        BinaryImage clone = binary.copy();

        DitherAlgorithms.atkinsonDithering(clone);

        screen.setImage(clone.getBitmap());
    }
    @Override
    public void stuckiDithering() {
        BinaryImage clone = binary.copy();

        DitherAlgorithms.stuckiDithering(clone);

        screen.setImage(clone.getBitmap());
    }
    @Override
    public void print() {

    }

    @Override
    public void start() {

        //screen.setImage(current);
    }
}
