package com.mobileprinter.BluetoothLibrary;

import com.mobileprinter.BluetoothLibrary.Packages.ImageLinePackage;
import com.mobileprinter.ImageLibrary.BinaryImage;

import java.util.function.Consumer;

/**
 * Created by Влад on 25.10.2016.
 */

public class Printer {


    public Printer(String address) {

        transceiver = new BluetoothTransceiver(address);
    }

    private BluetoothTransceiver transceiver;

    private Consumer onStart;
    private Consumer onEnd;

    public void printImageAsync(BinaryImage image){



    }
    public void printImage(BinaryImage image){

        transceiver.open();

        int rowBytes = (image.getWidth() + 7) / 8;
        int rowBytesClipped = (rowBytes >= 48) ? 48 : rowBytes;

        for(int i = 0; i < image.getHeight(); i++){

            byte current = 0;
            byte tmp;
            byte[] array = new byte[rowBytesClipped];

            for(int k = 0; k<rowBytesClipped; k++) {

                current = 0;
                int k8 = k*8;

                for(int z = 0; z < 8; z++){

                    if(k8 + z < image.getWidth()) {
                        tmp = image.getByte(k8 + z, i) == 0 ? (byte) 1 : (byte) 0;
                    }
                    else{
                        tmp = 0;
                    }

                    current |= tmp << (7-z);
                }

                array[k] = current;
            }

            transceiver.send(new ImageLinePackage(array));
        }

        transceiver.close();

    }


}
