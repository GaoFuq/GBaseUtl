package com.gfq.gbaseutl.apk_update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.gfq.gbaseutl.App;
import com.gfq.gbaseutl.R;
import com.gfq.gbaseutl.net.APIService;
import com.gfq.gbaseutl.net.OnCallBack;
import com.gfq.gbaseutl.util.ComUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.util.List;

import static com.gfq.gbaseutl.views.jiugongge.Simple.FILE_PROVIDER;

/**
 * @created GaoFuq
 * @Date 2020/7/8 10:42
 * @Descaption
 */
public class Simple extends AppCompatActivity {
    private static final int NOTIFY_ID = 999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int localVersion = ComUtil.getLocalVersion(this);
        APIService.call(APIService.api().getAppVersion(), new OnCallBack<Integer>() {
            @Override
            public void onSuccess(Integer version) {
                if (localVersion < version) {

                        startUpdate("url");
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private NotificationManager createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //以下属性的设置须在创建通道前设置,创建后设置属性无效
            CharSequence name = "channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String description = "威客下载";
            NotificationChannel notificationChannel = new NotificationChannel("版本更新", name, importance);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setVibrationPattern(new long[]{0});
            notificationChannel.setSound(null, null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
            return notificationManager;
        }
        return null;
    }

    NotificationManager mNotifyManager;

    private void startUpdate(String url) {
        FileDownloader.setup(this);
        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_download);
        remoteViews.setProgressBar(R.id.pb_progress, 100, 0, false);
        remoteViews.setTextViewText(R.id.tv_progress, "已下载0%");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "版本更新");
        mNotifyManager = createNotificationChannel();
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setCustomContentView(remoteViews);
        builder.setTicker("正在下载");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{0});
        builder.setSound(null);
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Notification mNotification = builder.build();

        String fileName = url.substring(url.lastIndexOf('/') + 1);
        FileDownloader.getImpl().create(url)
                .setPath("APP_DOWNLOAD_DIRECTORY" + File.separator + fileName)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        logE("pending totalBytes=" + totalBytes);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
//                        logE("connected totalBytes=" + totalBytes);
                        remoteViews.setProgressBar(R.id.pb_progress, 100, 0, false);
                        remoteViews.setTextViewText(R.id.tv_progress, "已下载0%");
                        remoteViews.setTextViewText(R.id.tv_total, "共" + ComUtil.getFileSize(totalBytes+""));
                        mNotifyManager.notify(NOTIFY_ID, mNotification);

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        logE("progress soFarBytes=" + soFarBytes);
                        float l = (float) soFarBytes / totalBytes;
                        int progress = (int) (l * 100);
                        remoteViews.setProgressBar(R.id.pb_progress, 100, progress, false);
                        remoteViews.setTextViewText(R.id.tv_progress, "已下载" + progress + "%");
                        remoteViews.setTextViewText(R.id.tv_total, "共" + ComUtil.getFileSize(totalBytes+""));
                        mNotifyManager.notify(NOTIFY_ID, mNotification);

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
//                        logE("blockComplete ");
                        String targetFilePath = task.getTargetFilePath();
                        File file = new File(targetFilePath);
                        installApp(file);

                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
//                        logE("retry ");

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
//                        logE("completed ");
                        remoteViews.setProgressBar(R.id.pb_progress, 100, 100, false);
                        remoteViews.setTextViewText(R.id.tv_progress, "下载完成");
                        mNotifyManager.notify(NOTIFY_ID, mNotification);
                        String targetFilePath = task.getTargetFilePath();
                        File file = new File(targetFilePath);
                        installApp(file);

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        logE("paused ");

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
//                        logE("error ");

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
//                        logE("warn ");

                    }
                }).start();
    }

    private void installApp(File apkFile) {
        if (apkFile == null) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//fileprovider
            Uri uriForFile = FileProvider.getUriForFile(this, FILE_PROVIDER, apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriForFile, getContentResolver().getType(uriForFile));
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), getMIMEType(apkFile));
        }
        try {
            startActivity(intent);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(this, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }
    }

    public String getMIMEType(File file) {
        String var1 = "";
        String var2 = file.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }

}
