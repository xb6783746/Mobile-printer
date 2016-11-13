package com.mobileprinter.Interfaces;

import android.graphics.Point;
import android.net.Uri;


/**
 * Created by Влад on 08.11.2016.
 */

public interface ImageEditorPresenter extends Presenter{

    void loadImage(Uri uri);
    void editButtonClick();
    void cutButtonClick();
    void printButtonClick();

    void brightnessButtonClick();
    void contrastButtonClick();
    void saturationButtonClick();

    void seekBarChanged(int progress);

    void acceptButtonClick();
    void cancelButtonClick();

    void touch(int x, int y);
    void release(int x, int y);
    void move(Point to);
}
