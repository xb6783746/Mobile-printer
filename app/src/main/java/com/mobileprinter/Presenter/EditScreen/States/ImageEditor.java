package com.mobileprinter.Presenter.EditScreen.States;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import com.mobileprinter.Interfaces.ImageEditorScreen;

/**
 * Created by Влад on 12.11.2016.
 */

public class ImageEditor extends BaseState {

    public enum Edit {
        Brightness, Contrast, Saturation, None
    }

    public ImageEditor(ImageEditorScreen screen, Owner owner) {
        super(owner);
        this.screen = screen;
    }

    private float brightness = 0, contrast = 5, saturation;
    private Edit type;
    private Bitmap tmp;
    private int brightnessStart = -255, brightnessEnd = 255;
    private int contrastStart = 0, contrastEnd = 2;
    private int satStart = 0, satEnd = 2;

    private Matrix matrix;
    private Canvas canvas;
    Paint paint = new Paint();


    @Override
    public void start() {

        screen.showEditButtons();
        type = Edit.None;
    }

    @Override
    public void update(Bitmap b) {
        if(tmp != null){
            tmp.recycle();
        }

        brightness = 0;
        contrast = 1;
        saturation = 1;

        this.current = b;
        tmp = b.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(tmp);

        type = Edit.None;
    }

    @Override
    public void cancel() {
        type = Edit.None;
        screen.setImage(current);
    }

    @Override
    public Bitmap apply(Bitmap b) {

        Bitmap res = Bitmap.createBitmap(tmp);
        //tmp.recycle();

        return res;
    }

    @Override
    public void seekBarChanged(int progress) {

        float tmp = progress / 100f;

        switch (type) {
            case Brightness:
                changeBrightness(tmp * brightnessStart + brightnessEnd * (1 - tmp));
                break;
            case Contrast:
                changeContrast(tmp * contrastStart + contrastEnd * (1 - tmp));
                break;
            case Saturation:
                changeSaturation(tmp * satStart + satEnd * (1 - tmp));
                break;
        }
    }

    @Override
    public void acceptButtonClick() {
        screen.showSeekBar(false);
        screen.showEditButtons();
    }

    @Override
    public void cancelButtonClick() {
        screen.showSeekBar(false);
        screen.showEditButtons();

        screen.setImage(current);
    }

    @Override
    public void touch(int x, int y) {

    }
    @Override
    public void release(int x, int y) {

    }

    @Override
    public void move(Point to) {

    }

    @Override
    public void brightnessButtonClick() {
        screen.showOptionButtons();
        screen.showSeekBar(true);
        screen.setSeekBar(getProgress(Edit.Brightness));

        type = Edit.Brightness;
    }
    @Override
    public void contrastButtonClick() {
        screen.showOptionButtons();
        screen.showSeekBar(true);
        screen.setSeekBar(getProgress(Edit.Contrast));

        type = Edit.Contrast;
    }
    @Override
    public void saturationButtonClick() {
        screen.showOptionButtons();
        screen.showSeekBar(true);
        screen.setSeekBar(getProgress(Edit.Saturation));

        type = Edit.Saturation;
    }

    private void changeBrightness(float newValue) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, newValue,
                0, 1, 0, 0, newValue,
                0, 0, 1, 0, newValue,
                0, 0, 0, 1, 0
        });

        applyFilter(matrix);

        brightness = newValue;

    }
    private void changeContrast(float newValue){

        float t = (1 - newValue) / 2;

        ColorMatrix matrix = new ColorMatrix(new float[]{
                newValue, 0, 0, 0, t,
                0, newValue, 0, 0, t,
                0, 0, newValue, 0, t,
                0, 0, 0, 1, 0
        });

        applyFilter(matrix);

        contrast = newValue;
    }
    private void changeSaturation(float newValue){
        ColorMatrix matrix = new ColorMatrix();

        matrix.setSaturation(newValue);

        applyFilter(matrix);

        saturation = newValue;
    }

    private void applyFilter(ColorMatrix matrix){

        ColorMatrixColorFilter f = new ColorMatrixColorFilter(matrix);



        paint.setColorFilter(new ColorMatrixColorFilter(matrix));

        canvas.drawBitmap(current, 0, 0, paint);

        screen.setImage(tmp);
    }

    private int getProgress(Edit type){

        int start = 0, end = 0;
        float current = 0;

        switch (type) {
            case Brightness:
                start = brightnessStart;
                end = brightnessEnd;
                current = brightness;
                break;
            case Contrast:
                start = contrastStart;
                end = contrastEnd;
                current = contrast;
                break;
            case Saturation:
                start = satStart;
                end = satEnd;
                current = saturation;
                break;
        }

        float val = (current - start)/(end - start);

        return Math.round(val*100);
    }
}
