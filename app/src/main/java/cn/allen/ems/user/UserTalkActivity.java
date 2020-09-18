package cn.allen.ems.user;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.allen.ems.R;
import cn.allen.ems.adapter.TalkAdapter;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.entry.TalkMsg;
import cn.allen.ems.utils.Constants;

public class UserTalkActivity extends AllenBaseActivity {

    @BindView(R.id.toolbar)
    Toolbar bar;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.talk)
    AppCompatEditText talk;
    private SharedPreferences shared;
    private int uid;
    private String messege,url;
    private TalkAdapter adapter;

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_talk;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shared = AllenManager.getInstance().getStoragePreference();
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        uid = shared.getInt(Constants.User_Id,-1);
        url = shared.getString(Constants.User_HeadImage_Url,"");
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        adapter = new TalkAdapter(uid);
        rv.setAdapter(adapter);
    }

    @Override
    protected void addEvent() {
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                finish();
                view.setEnabled(true);
            }
        });
        talk.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    view.setEnabled(false);
                    if(checkIsOk()){
                        sendMsg(messege);
                    }
                    view.setEnabled(true);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkIsOk(){
        messege = talk.getText().toString().trim();
        if(StringUtils.empty(messege)){
            MsgUtils.showShortToast(context,"请输入内容!");
            return false;
        }
        return true;
    }

    private void sendMsg(String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mess = WebHelper.init().sendWaitMessage(handler,uid,msg);
                if(StringUtils.notEmpty(mess)){
                    Message message = new Message();
                    message.what = 0;
                    message.obj = msg;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    adapter.addMsg(new TalkMsg(uid,messege,url));
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rv.scrollToPosition(adapter.getItemCount()-1);
                        }
                    },200);
                    break;
                case 1:
                    talk.setText("");
                    break;
                case -1:
                    MsgUtils.showShortToast(context, (String) msg.obj);
                    break;
            }
        }
    };

}
