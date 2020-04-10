package com.gfq.gbaseutl.util.bluetoothPrint;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;



import java.io.IOException;

/**
 * @author glsite.com
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class BlueBoothPrintUtil {
    public static final int TASK_TYPE_TEST = 3;//测试
    private BluetoothSocket mSocket;
    private AsyncTask mConnectTask;
    public final static int TASK_TYPE_PRINT = 2;


    private BlueBoothPrintUtil() {
    }

    public static BlueBoothPrintUtil getInstance() {
        return Inner.instance;
    }

    private static class Inner {
        private static final BlueBoothPrintUtil instance = new BlueBoothPrintUtil();
    }



    /**
     * 检查蓝牙状态，如果已打开，则查找已绑定设备
     *
     * @return
     */
//    public boolean checkBluetoothState() {
//        if (BluetoothUtil.isBluetoothOn()) {
//            return true;
//        } else {
//            BluetoothUtil.openBluetooth((Activity) context);
//            return false;
//        }
//    }
    public BlueBoothPrintUtil connectDevice(BluetoothDevice device, int taskType,OnTaskListener onTaskListener) {
        if (device != null) {
            mConnectTask = new ConnectBluetoothTask(taskType,onTaskListener).execute(device);
        }
        return this;
    }

    public BlueBoothPrintUtil connectDevice(BluetoothDevice device, Object data,OnTaskListener onTaskListener) {
        if (device != null) {
            mConnectTask = new ConnectBluetoothTask(data,onTaskListener).execute(device);
        }
        return this;
    }

    public interface OnTaskListener {
        void onPre();

        void onEnd(boolean success);
    }

//    OnTaskListener onTaskListener;
//
//    public void setOnTaskListener(OnTaskListener onTaskListener) {
//        this.onTaskListener = onTaskListener;
//    }

    @SuppressLint("StaticFieldLeak")
    protected class ConnectBluetoothTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {

        int mTaskType;
        Object data;
        OnTaskListener onTaskListener;
        public ConnectBluetoothTask(int taskType,OnTaskListener onTaskListener) {
            this.mTaskType = taskType;
            this.onTaskListener=onTaskListener;
        }

        public ConnectBluetoothTask(Object data,OnTaskListener onTaskListener) {
            this.data = data;
            this.onTaskListener=onTaskListener;
        }

        @Override
        protected void onPreExecute() {
            if (onTaskListener != null) {
                onTaskListener.onPre();
            }
            super.onPreExecute();
        }

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... params) {
            if (mSocket != null) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mSocket = BluetoothUtil.connectDevice(params[0], null);
            if (mSocket != null) {
                if (data != null) {
                    onConnected(mSocket, data);
                } else {
                    onConnected(mSocket, mTaskType);
                }
            }
            return mSocket;
        }

        @Override
        protected void onPostExecute(BluetoothSocket socket) {
            if (socket == null || !socket.isConnected()) {
                if (onTaskListener != null) {
                    onTaskListener.onEnd(false);
                }
            } else {
                if (onTaskListener != null) {
                    onTaskListener.onEnd(true);
                }
            }
            super.onPostExecute(socket);
        }
    }


    public void cancelConnectTask() {
        if (mConnectTask != null) {
            mConnectTask.cancel(true);
            mConnectTask = null;
        }
    }

    public void closeSocket() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                mSocket = null;
                e.printStackTrace();
            }
        }
    }



    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                PrintUtil.printOne(socket, "工作面");
                PrintUtil.printTwo(socket, "2019年02月05日", "早班");
                PrintUtil.printOne(socket, "K1值");
                PrintUtil.printThree(socket, "孔号", "位置", "值");
                PrintUtil.printOne(socket, "结论:");
                PrintUtil.printTwo(socket, "动力现象", "无/3次");
                break;
            case TASK_TYPE_TEST:
                PrintUtil.printOne(socket, "测试打印");
                break;
        }
    }

    /**
     *
     * @param socket
     * @param data 要打印的数据的对象
     */
    public void onConnected(BluetoothSocket socket, Object data) {
        PrintUtil.printOne(socket, "           ");
        PrintUtil.printOne(socket, "           ");

        PrintUtil.printOne(socket, "----------");
        PrintUtil.printOne(socket, "----------");
        PrintUtil.printOne(socket, "           ");
        PrintUtil.printOne(socket, "           ");
    }
}
