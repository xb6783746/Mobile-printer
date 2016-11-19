package com.mobileprinter.BluetoothLibrary.Packages;

/**
 * Created by Влад on 25.10.2016.
 */

public class ImageLinePackage extends BluetoothPackage{

    public ImageLinePackage(byte[] data ){

        if(data.length > 48 ){
            throw new IllegalArgumentException("");
        }

        this.data = data;

    }

    private byte[] data;

    @Override
    public byte[] getByte() {

        byte[] res = new byte[data.length + 4];

        res[0] = 18;
        res[1] = 42;
        res[2] = 1;
        res[3] = (byte)data.length;

        for(int i = 0; i < data.length; i++){
            res[i + 4] = data[i];
        }

        return res;

    }
}
