package com.mobileprinter.ImageLibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;

/**
 * Created by Влад on 24.10.2016.
 */

public class BinaryImage {


    public BinaryImage(Bitmap bitmap) {

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        this.config = bitmap.getConfig();

        makeMono(bitmap);


    }

    private BinaryImage(){

    }

    private byte[] buffer;
    private int width, height;
    private Bitmap.Config config;


    private void makeMono(Bitmap bitmap){

        int size = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer originBuffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(originBuffer);
        byte[] array = originBuffer.array();

        buffer = new byte[size/4];

        int tmp1 = 0, tmp4 = 0;
        byte y;

        for(int i = 0; i< width*height; i++){

            int r = array[tmp4 + 2] & 0xFF;
            int g = array[tmp4 + 1] & 0xFF;
            int b = array[tmp4] & 0xFF;

            y = (byte)(r * 0.21 + g * 0.72 + b * 0.07);

            buffer[tmp1] = (byte)y;

            tmp4 += 4;
            tmp1++;
        }

    }

    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getByte(int x, int y) {

        if(x >= 0 && x < width
                && y >= 0 && y < height){


            return buffer[y * width + x] & 0xFF;

        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public void setByte(int x, int y, int b){

        if(x >= 0 && x < width
                && y >= 0 && y < height){

            buffer[y * width + x] = (byte)b;
        }

    }


    public Bitmap getBitmap(){

        byte[] tmp = new byte[width*height*4];

        int tmp1 = 0, tmp4 = 0;

        for(int i = 0; i<width*height; i++){

            tmp[tmp4] = buffer[tmp1];
            tmp[tmp4 + 1] = buffer[tmp1];
            tmp[tmp4 + 2] = buffer[tmp1];

            tmp[tmp4 + 3] = -1;

            tmp4 += 4;
            tmp1++;
        }

        Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, config);
        ByteBuffer buffer_tmp = ByteBuffer.wrap(tmp);
        bitmap_tmp.copyPixelsFromBuffer(buffer_tmp);

        return bitmap_tmp;
    }
    public BinaryImage copy(){

        BinaryImage image = new BinaryImage();

        image.buffer = buffer.clone();
        image.width = width;
        image.height = height;
        image.config = config;

        return image;
    }


}
