package com.mobileprinter.BluetoothLibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.mobileprinter.BluetoothLibrary.Packages.BluetoothPackage;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * Created by Влад on 25.10.2016.
 */

public class BluetoothTransceiver {

    public BluetoothTransceiver(String address){

        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);


        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);

            //socket.connect();

            outPackages = new LinkedBlockingQueue<BluetoothPackage>();

            Thread listen = new Thread(() -> listen());

            listen.start();


        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket socket;
    private BlockingQueue<BluetoothPackage> outPackages;
    private BluetoothPackage last;

    ExecutorService pool = Executors.newCachedThreadPool();


    public void addPackage(BluetoothPackage pck){

        outPackages.add(pck);
    }


    private void listen(){

        while (true) {

            try {
                last = outPackages.take();

                send(last);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void send(BluetoothPackage pack){

        try {

            socket.getOutputStream().write(pack.getByte());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(){
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
