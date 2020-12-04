package cn.allen.ems.data;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import allen.frame.tools.Logger;
import allen.frame.tools.StringUtils;
import cn.allen.ems.utils.Constants;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final long TIME_OUT = 120;//单位秒S
    private String url;

    public HttpUtil(){
        url = Constants.Url;
    }

    /**
     * get请求
     *
     * @param mothed
     * @param array
     * @return
     */
    public String okhttpget(String mothed, String array) {
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url + mothed + (StringUtils.empty(array) ? "" : "?" + array)).build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                data = response.body().string();
            }
        } catch (Exception e) {
            Logger.http("erro:", e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    public String okhttppost(String mothed, String json) {
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIME_OUT, TimeUnit.SECONDS).readTimeout(TIME_OUT, TimeUnit.SECONDS)
                    .build();// 创建OkHttpClient对象。
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");// 数据类型为json格式，
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url + mothed).post(body).build();
            Response response = null;
            Logger.http("url>>", url+mothed+"--"+json);
            response = client.newCall(request).execute();
            Logger.http("Response>>", response.toString());
            if (response.isSuccessful()) {
                data = response.body().string();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }
}
