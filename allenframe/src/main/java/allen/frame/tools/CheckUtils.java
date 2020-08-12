package allen.frame.tools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具（手机号,IP,身份证）
 * @author maojing
 *
 */
public class CheckUtils {
	/**
	 * 判断手机号是否合法
	 * @param phone
	 * @return
	 */
	public static boolean phoneIsOk(String phone){
//		移动：134、135、136、137、138、139、150、151、152、158、159、182、183、184、157、187、188
//		147、178
//		联通：130、131、132、155、156、145、185、186、176
//		电信：133、153、180、181、189、177、173、149
//		卫星通信：1349；虚拟运营商：170、171
		Pattern p = Pattern.compile("^((13[0-9])|(14[1,4-9])|(15[^4,\\D])|(166)|(17[0-8])|(18[0-9])|(19[8,9]))\\d{8}$");
		Matcher m = p.matcher(phone);
	    if (TextUtils.isEmpty(phone)) return false;  
	    else return m.matches();
	}
	/** 
	 * 判断是否为合法IP 
	 * @param ipAddress
	 * @return
	 */  
    public static boolean isboolIp(String ipAddress) {
    	if(StringUtils.empty(ipAddress)){
    		return false;
    	}
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";   
        Pattern pattern = Pattern.compile(ip);  
        Matcher matcher = pattern.matcher(ipAddress);  
        return matcher.matches();  
    }
    /**
     * 判断身份证是否合法
     * @param id
     * @return
     */
    public static boolean IDIsOk(String id){
    	if(StringUtils.empty(id)){
    		return false;
    	}
    	String erro = IDUtils.getInstance().IDCardValidate(id);
    	return StringUtils.empty(erro)?true:false;
    }
    /**
	 * 判断是否有某APP
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppAvilible(Context context, String packageName) {  
		//获取packagemanager  
		final PackageManager packageManager = context.getPackageManager();  
       	//获取所有已安装程序的包信息  
       	List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);  
       	//用于存储所有已安装程序的包名  
       	List<String> packageNames = new ArrayList<String>();  
       	//从pinfo中将包名字逐一取出，压入pName list中  
       	if (packageInfos != null) {
       		for (int i = 0; i < packageInfos.size(); i++) {  
       			String packName = packageInfos.get(i).packageName;  
       			packageNames.add(packName);  
       		}  
       	}  
       	//判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE  
       	return packageNames.contains(packageName);  
	}
	/**
	 * 判断字符串是否存在中文
	 * @param str
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		if(StringUtils.empty(str)){
			return false;
		}
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static String nameFormPath(String path){
		if(StringUtils.empty(path)) {
			return "";
		}else{
			String name = path.substring(path.lastIndexOf("/")+1).replaceAll(" ","");
			if(isContainChinese(name)){
				if(name.contains(".")){
					return EncryptUtils.MD5Encoder(name)+name.substring(name.indexOf("."));
				}else{
					return EncryptUtils.MD5Encoder(name);
				}
			}
			return name;
		}
	}

	/**
	 * 检测密码强弱
	 * @param psw
	 * @return
	 */
	public static boolean passWordIsNotEasy(String psw){

		/*String regexZ = "\\d*";
        String regexS = "[a-zA-Z]+";
        String regexT = "\\W+$";
        String regexZT = "\\D*";
        String regexST = "[\\d\\W]*";
        String regexZS = "\\w*";
        String regexZST = "[\\w\\W]*";

        if(paw.length()<6){
        	return false;
        }
        if (paw.matches(regexZ)) {
            return false;
        }
        if (paw.matches(regexS)) {
            return false;
        }
        if (paw.matches(regexT)) {
            return false;
        }
        if (paw.matches(regexZT)) {
            return true;
        }
        if (paw.matches(regexST)) {
            return true;
        }
        if (paw.matches(regexZS)) {
            return true;
        }
        if (paw.matches(regexZST)) {
            return true;
        }*/
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < psw.length(); i++) {
			int A = psw.charAt(i);
			if (A >= 48 && A <= 57) {// 数字
				map.put("数字", "数字");
			} else if (A >= 65 && A <= 90) {// 大写
				map.put("大写", "大写");
			} else if (A >= 97 && A <= 122) {// 小写
				map.put("小写", "小写");
			} else {
				map.put("特殊", "特殊");
			}
		}
		Set<String> sets = map.keySet();
		int pwdSize = sets.size();// 密码字符种类数
		int pwdLength = psw.length();// 密码长度
		if (pwdSize >= 3 && pwdLength >= 6) {
			return true;// 强密码
		} else {
			return false;// 弱密码
		}
	}

	public static int passWordType(String psw){

		if(StringUtils.empty(psw)){
			return 0;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < psw.length(); i++) {
			int A = psw.charAt(i);
			if (A >= 48 && A <= 57) {// 数字
				map.put("数字", "数字");
			} else if (A >= 65 && A <= 90) {// 大写
				map.put("大写", "大写");
			} else if (A >= 97 && A <= 122) {// 小写
				map.put("小写", "小写");
			} else {
				map.put("特殊", "特殊");
			}
		}
		Set<String> sets = map.keySet();
		int pwdSize = sets.size();// 密码字符种类数
		int pwdLength = psw.length();// 密码长度
		return pwdSize;
	}

	public static boolean isEquals(String qrq,String equese){
		if(StringUtils.empty(qrq)){
			return false;
		}
		return qrq.equals(equese);
	}

}
