package com.mobileprinter.Interfaces;

/**
 * Created by Влад on 13.11.2016.
 */

public interface DitherScreenPresenter {

    void floydSteinbergDithering();
    void atkinsonDithering();
    void stuckiDithering();

    void print();
}
