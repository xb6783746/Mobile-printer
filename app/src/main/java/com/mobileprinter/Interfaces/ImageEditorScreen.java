package com.mobileprinter.Interfaces;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.List;

/**
 * Created by Влад on 08.11.2016.
 */

public interface ImageEditorScreen extends BaseView{

    void showEditButtons();
    void showOptionButtons();
    void hideOptions();
    void showSeekBar(Boolean visible);
    void setSeekBar(int progress);

    void setImage(Bitmap b);
    Rect getImageRect();

    void drawRect(Rect rect, List<Point> dots);

    void hideRect();

    int getImageWidth();
    int getImageHeight();
    float getScale();
}
