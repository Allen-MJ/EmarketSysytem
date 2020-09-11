package allen.frame.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import allen.frame.entry.City;

public class CityUtil {
    private static CityUtil util;

    private List<City> list;
    private CityUtil(){

    }
    public static CityUtil init(){
        if(util==null){
            util = new CityUtil();
        }
        return util;
    }

    public CityUtil loadData(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data = getFromAssets(context,"city.txt");
                Gson gson = new Gson();
                list = gson.fromJson(data,new TypeToken<List<City>>(){}.getType());
                handler.sendEmptyMessage(0);
            }
        }).start();
        return this;
    }

    public String getFromAssets(Context context,String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) );
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void newCall(CallBack callBack){
        this.callBack = callBack;
    }

    public CityUtil loadData(final Context context, final String area, final String city){
        if(list==null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data = getFromAssets(context,"city.txt");
                    Gson gson = new Gson();
                    list = gson.fromJson(data,new TypeToken<List<City>>(){}.getType());
                    if(StringUtils.notEmpty(area)){
                        for (City c:list){
                            if(c.getName().equals(area)){
                                if(StringUtils.empty(city)){
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = c.getArea();
                                    handler.sendMessage(msg);
                                    break;
                                }else{
                                    for(City b:c.getArea()){
                                        if(b.getName().equals(city)){
                                            Message msg = new Message();
                                            msg.what = 1;
                                            msg.obj = b.getArea();
                                            handler.sendMessage(msg);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(StringUtils.notEmpty(area)){
                        for (City c:list){
                            if(c.getName().equals(area)){
                                if(StringUtils.empty(city)){
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = c.getArea();
                                    handler.sendMessage(msg);
                                    break;
                                }else{
                                    for(City b:c.getArea()){
                                        if(b.getName().equals(city)){
                                            Message msg = new Message();
                                            msg.what = 1;
                                            msg.obj = b.getArea();
                                            handler.sendMessage(msg);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        }
        return this;
    }

    private CallBack callBack;
    public interface CallBack{
        void onResponse(List<City> list);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(callBack!=null){
                        callBack.onResponse(list);
                    }
                    break;
                case 1:
                    if(callBack!=null){
                        callBack.onResponse((List<City>) msg.obj);
                    }
                    break;
            }
        }
    };
}
