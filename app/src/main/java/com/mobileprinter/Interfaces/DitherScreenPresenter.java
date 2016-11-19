package com.mobileprinter.Interfaces;

/**
 * Created by Влад on 13.11.2016.
 */

public interface DitherScreenPresenter extends Presenter{

    void created();

    void floydSteinbergDithering();
    void atkinsonDithering();
    void stuckiDithering();

    void back();

    void print();
}
