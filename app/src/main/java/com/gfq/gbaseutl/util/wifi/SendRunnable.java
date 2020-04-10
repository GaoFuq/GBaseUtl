package com.gfq.gbaseutl.util.wifi;

/**
 * Created by 14942 on 2018/3/26.
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.gfq.gbaseutl.util.HexUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class SendRunnable implements Runnable {

    private static final String TAG = "SendRunnable";
    private String ip;
    private int port;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Handler mainHandler;
    private Socket s;

    private int len;
    private long start;

    public static boolean isLoop = true;

    public interface OnSocketOpenedListener {
        void onOpened();

        void openFailed();
    }

    private OnSocketOpenedListener listener;

    public SendRunnable(String ip, int port, Handler mainHandler, int dataLength, OnSocketOpenedListener listener) {     //IP，端口，数据
        this.ip = ip;
        this.port = port;
        this.mainHandler = mainHandler;
        this.len = dataLength;
        this.listener = listener;
        start = System.currentTimeMillis();

    }

    /**
     * 套接字的打开
     */
    boolean reachable = false;

    private boolean open() {
        try {
            if (!reachable) {
                s = new Socket(ip, port);
                InetAddress byAddress = InetAddress.getByName("192.168.4.1");
                reachable = byAddress.isReachable(1000);
                Log.e(TAG, "open: reachable = " + reachable);
                inputStream = s.getInputStream();
                outputStream = s.getOutputStream();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "open: " + e.getMessage());
//            Message msg = mainHandler.obtainMessage();
//            msg.what = 0x01;
//            msg.obj = "wifi传输通道建立失败";
//            mainHandler.sendMessage(msg);
            Log.e("open", "open: " + e.getMessage());
            return false;
        }
    }


//(len = in.read(chars)) != -1
//     Message msg = mainHandler.obtainMessage();
//                            msg.what = 0x00;
//                            msg.obj = receiveMsg;
//                            mainHandler.sendMessage(msg);

    byte[] bb = new byte[]{0x5A, 0x5A, 0x00, 0x05};


    long delay = 1000;

    @Override
    public void run() {

        boolean open = open();
        while (!open) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            open = open();
            if (timeOut(delay)) {
                if (listener != null) {
                    listener.openFailed();
                }
//                returnFailed(delay, "socket连接超时");
                return;
            }
        }

        if (listener != null) {
            listener.onOpened();
        }

        Log.e(TAG, "run: isLoop == " + isLoop);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (isLoop) {
                    byte[] measureData = new byte[len];
                    try {
                        Thread.sleep(30);
                        inputStream.read(measureData);
                        String hex = HexUtil.getHex(measureData);
                        Log.e(TAG, "run: read = " + hex);
                        if (measureData[1] == 0x55 && measureData[2] == 0 && measureData[3] == 2 && measureData[len - 2] == (byte) 0xAA && measureData[len - 1] == (byte) 0xAA) {
                            Message msg = mainHandler.obtainMessage();
                            msg.what = 0x00;
                            msg.obj = hex;
                            mainHandler.sendMessage(msg);
                            Log.d("XXXX", "run: " + hex);

                            for (int i = 0; i < 30; i++) {
                                Thread.sleep(50);
                                outputStream.write(new byte[]{0x5A, 0x5A, 0x00, 0x04});//收到数据的响应
                                Log.d("xxxx", "run: send 0x5A, 0x5A, 0x00, 0x04");
                            }
                            isLoop = false;
                            Log.d("XXXX", "run: isLoop = false");

                            s.shutdownInput();
                            s.shutdownOutput();
                            s.close();
                            Log.d("XXXX", "shutdown   close");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "read: " + e.getMessage());
                    }
                }
            }
        }, "read data thread").start();


        loopWrite();

    }


    private boolean timeOut(long delay) {
        return System.currentTimeMillis() - start > delay;
    }

    private void returnFailed(long delay, String sss) {
        if (timeOut(delay)) {
            Message msg = mainHandler.obtainMessage();
            msg.what = 0x02;
            msg.obj = sss;
            mainHandler.sendMessage(msg);
        }
    }


    private void loopWrite() {
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(100);
                outputStream.write(bb);//开始接收数据的响应
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "loopWrite: " + e.getMessage());
            }
        }
        Log.e(TAG, "loopWrite: 已下发指令");

    }
}
 