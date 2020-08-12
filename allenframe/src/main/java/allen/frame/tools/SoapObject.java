package allen.frame.tools;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SoapObject {
    private String code = "-1";
    private String message = "数据获取失败!";
    private String data;
    private String result;
    private String num;
    public SoapObject(){}
    public SoapObject(String code,String message,String data, String num){
        this.code = code;
        this.message = message;
        this.data = data;
        this.num = num;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getResult(){

        return result;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "SoapObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", result='" + result + '\'' +
                ", num='" + num + '\'' +
                '}';
    }

    public SoapObject xmlPull(String xml, String method){

        String genTAG = method+"Result";
        Logger.http("xml->",xml);
        ByteArrayInputStream tInputStringStream = null;
        try
        {
            if (StringUtils.notEmpty(xml)) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
                Logger.http("soap->","1");
            }else{
                return new SoapObject("-1","网络异常!","","");
            }
        }
        catch (Exception e) {
            Logger.http("soap->","2");
            return this;
        }
        if(!xml.contains("<"+genTAG+">")){
            Logger.http("soap->","3");
            return this;
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(tInputStringStream,"UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        if(name.equals("respCode")){
                            Logger.http("soap->","4");
                            setCode(parser.nextText());
                            }else if(name.equals("respMessage")){
                            Logger.http("soap->","5");
                            setMessage(parser.nextText());
                        }else if(name.equals("respData")){
                            Logger.http("soap->","6");
                            setData(parser.nextText());
                        }else if(name.equals("respMsgCount")){
                            Logger.http("soap->","6");
                            setNum(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = parser.next();
            }
            tInputStringStream.close();
            Logger.http("soap->","++");
            return  this;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Logger.http("soap->","7");
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            Logger.http("soap->","8");
            return this;
        }
    }
}
