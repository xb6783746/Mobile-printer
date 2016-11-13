package com.mobileprinter.Presenter.EditScreen.States;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ImageEditorScreen;

import java.lang.FunctionalInterface;

/**
 * Created by Влад on 12.11.2016.
 */

public abstract class BaseState {


    public BaseState(Owner owner) {
        this.owner = owner;
    }

    protected ImageEditorScreen screen;
    protected Bitmap current;
    protected Owner owner;

    public abstract void update(Bitmap b);
    public abstract void start();
    public abstract void cancel();
    public abstract Bitmap apply(Bitmap b);

    public abstract void seekBarChanged(int progress);

    public abstract void acceptButtonClick();
    public abstract void cancelButtonClick();

    public abstract void touch(int x, int y);
    public abstract void release(int x, int y);
    public abstract void move(Point to);

    public abstract void brightnessButtonClick();
    public abstract void contrastButtonClick();
    public abstract void saturationButtonClick();
}
