package com.gfq.gbaseutl.util;

import android.util.Log;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author glsite.com
 * @version $
 * @des
 * @updateAuthor $
 * @updateDes
 */
public class IPUtils {
    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    /**
     * 测试某个ip地址能否连接
     * @param ip
     * @param port
     * @return
     */
    public static boolean ipConnectTest(String ip,int port){
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port),1000);//建立连接
            return socket.isConnected();//通过现有方法查看连通状态
        } catch (IOException e) {
            return false;
        }finally{
            try {
                socket.close();
            } catch (IOException e) { }
        }

    }

    public static void main(String[] args) {
        boolean b = ipConnectTest("120.79.26.20", 812);
        System.out.println(b);
    }
}
