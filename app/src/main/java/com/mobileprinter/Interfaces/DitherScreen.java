package com.mobileprinter.Interfaces;

import android.graphics.Bitmap;

/**
 * Created by Влад on 13.11.2016.
 */

public interface DitherScreen extends BaseView{

    void setImage(Bitmap b);

    void openProgressDialog();
    void closeProgressDialog();
}
