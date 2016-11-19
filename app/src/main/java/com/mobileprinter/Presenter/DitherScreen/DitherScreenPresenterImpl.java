package com.mobileprinter.Presenter.DitherScreen;

import android.graphics.Bitmap;

import com.mobileprinter.BluetoothLibrary.Printer;
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
        //this.current = current;
        this.router = router;

        int tmp = (int)(348f / current.getWidth() * current.getHeight());

        Bitmap print = Bitmap.createScaledBitmap(current, 348, tmp, false);
        this.current = print;

        binary = new BinaryImage(print);
    }

    private Bitmap current;
    private BinaryImage binary;
    private BinaryImage dithered;
    private DitherScreen screen;
    private DitherRouter router;
    Printer printer = new Printer("20:16:08:08:00:75");

    @Override
    public void created() {
        screen.setImage(current);
    }

    @Override
    public void floydSteinbergDithering() {

        dithered = binary.copy();

        DitherAlgorithms.floydSteinbergDithering(dithered);

        screen.setImage(dithered.getBitmap());
    }

    @Override
    public void atkinsonDithering() {
        dithered = binary.copy();

        DitherAlgorithms.atkinsonDithering(dithered);

        screen.setImage(dithered.getBitmap());
    }
    @Override
    public void stuckiDithering() {
        dithered = binary.copy();

        DitherAlgorithms.stuckiDithering(dithered);

        screen.setImage(dithered.getBitmap());
    }

    @Override
    public void back() {
        router.openEditScreen(current);
    }

    @Override
    public void print() {

        screen.openProgressDialog();

        new Thread(() -> {
            printer.printImage(dithered);

            screen.closeProgressDialog();
        }).start();
    }

    @Override
    public void start() {

        //screen.setImage(current);
    }
}
