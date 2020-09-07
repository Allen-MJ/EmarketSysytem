package cn.allen.ems.data;

import org.json.JSONException;
import org.json.JSONObject;

import allen.frame.tools.Logger;
import allen.frame.tools.StringUtils;
import cn.allen.ems.entry.Response;

public class WebService {
    public static int Get = 0;
    public static int Post = 1;
    private HttpUtil httpUtil;

    public WebService(){
        httpUtil = new HttpUtil();
    }

    public Response getWebservice(String MethodName, Object[] arrays, int type) {
        if (type == Get) {
            return getWebservice(MethodName, arrays);
        } else {
            return getResult(getOkHttpPost(MethodName, arrays));
        }
    }

    public Response getWebservice(String MethodName, Object[] arrays) {
        return getResult(getOkHttpGet(MethodName, arrays));
    }

    public String getOkHttpGet(String MethodName, Object[] arrays) {
        StringBuilder sb = new StringBuilder();
        if (arrays != null) {
            for (int i = 0; i < arrays.length; i++) {
                sb.append((i == 0 ? "" : "&") + arrays[i++] + "="
                        + (arrays[i] == null ? "" : StringUtils.null2Empty(arrays[i].toString())));
            }
        }
        Logger.http(MethodName,  "[" + sb.toString() + "]");
        return httpUtil.okhttpget(MethodName, sb.toString());
    }

    public String getOkHttpPost(String MethodName, Object[] arrays) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (arrays != null) {
            for (int i = 0; i < arrays.length; i++) {
                sb.append((i == 0 ? "\"" : ",\"") + arrays[i++] + "\":" + StringUtils.getObject(arrays[i]));
            }
        }
        sb.append("}");
        Logger.http(MethodName,  "[" + sb.toString() + "]");
        return httpUtil.okhttppost(MethodName, sb.toString());
    }

    public Response getResult(String data) {
        Logger.http("result.", data);
        Response response = new Response();
        if (StringUtils.empty(data)) {
            response.setCode("-200");
            response.setMessage("服务连接异常");
            response.setData("");
        } else {
            JSONObject object;
            try {
                object = new JSONObject(data);
                try {
                    response.setCode(object.getString("code"));
                } catch (Exception e) {
                    response.setCode("-201");
                }
                try {
                    response.setMessage(object.getString("Msg"));
                } catch (Exception e) {
                    response.setMessage("解析异常,请更新应用!");
                }
                try {
                    response.setData(object.getString("data"));
                } catch (Exception e) {
                    response.setData("");
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                response.setCode("-201");
                response.setMessage("解析异常,请更新应用!");
                response.setData("");
                e.printStackTrace();
            }
        }
        return response;
    }

}
