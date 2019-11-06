package com.gfq.gbaseutl;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Intent;

import com.gfq.gbaseutl.a_gfq_test.OnCallBack;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * create by 高富强
 * on {2019/11/5} {15:56}
 * desctapion:
 */
public class App extends Application implements ComponentCallbacks2 {

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
//            Glide.get(this).clearMemory();
        }
//        Glide.get(this).trimMemory(level);
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        Glide.get(this).clearMemory();
    }


    //自定义抓取崩溃信息，并上传到服务器
    private void initCrashHandler(){
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);//printWriter 把信息打印到 -> stringWriter 的StringBuffer里面
                e.printStackTrace(pw);//error信息 -> printWriter
                System.out.println(sw.toString());//通过sw.toString()方法，得到错误的文本信息;Return the buffer's current value as a string.
               /* submitLog(MachineLog.LogLevel.ERROR, sw.toString(), new Runnable() {
                    @Override
                    public void run() {

                    }
                });*/
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                pw.close();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra("crash", true);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

   /* private void submitLog(MachineLog.LogLevel logLevel, String content, Runnable  complete){
        String machineId = getCurrentMachineId();
        MachineLog log = new MachineLog();
        log.setMachineNO(machineId);
        log.setType(MachineLog.LogType.RUN);
        log.setContent(content);
        if (getCurrentOrder() != null){
            log.setContent(log.getContent()+" 订单号:"+getCurrentOrder().getNo());
        }
        log.setLogLevel(logLevel);
        APIService.call(APIService.api().submitMachineLog(log), this, new OnCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                if (complete != null){
                    complete.run();
                }
            }
        });
    }*/


}
