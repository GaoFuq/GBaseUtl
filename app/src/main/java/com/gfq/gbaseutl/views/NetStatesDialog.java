package com.gfq.gbaseutl.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gfq.gbaseutl.net.NetworkReceiver;


/**
 * create by 高富强
 * on {2019/11/7} {11:39}
 * desctapion:
 */

/*
//举个粟子
public class MainActivity extends AppCompatActivity {

    private NetStatesDialog netStatesDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netStatesDialog = new NetStatesDialog(this) {
            TextView textView ;
            @Override
            protected void setDialogContentView() {
                setContentView(R.layout.states_view);
                textView = findViewById(R.id.text);
            }

            @Override
            public void onNetLost() {
                textView.setText("xxxx");
                show();
            }

            @Override
            public void onNetAvailable() {
                dismiss();
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        netStatesDialog.unRegist(this);
    }
}
*/

public abstract class NetStatesDialog extends Dialog {

    private Context context;
    private TextView textView;
    private NetworkReceiver networkReceiver;

    public NetStatesDialog(Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init() {
//        setContentView(R.layout.states_view);
//        textView = findViewById(com.gfq.gbaseutl.R.id.text);
        setDialogContentView();
        initNetworkReceiver((Activity) context);
    }

    protected abstract void setDialogContentView();

    private void initNetworkReceiver(Activity activity) {
        if (networkReceiver == null) {
            networkReceiver = new NetworkReceiver(activity, new NetworkReceiver.NetworkCallback() {
                @Override
                public void onAvailable() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                          onNetAvailable();
                        }
                    });
                }

                @Override
                public void onLost() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onNetLost();
                        }
                    });
                }
            });
        }
    }

    public abstract void onNetLost() ;

    public abstract void onNetAvailable() ;


    public void unRegist(Activity activity) {
        if (networkReceiver != null && activity != null)
            networkReceiver.unRegister(activity);
    }
}
