package allen.frame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import allen.frame.widget.MaterialRefreshLayout;

public class ActivityHelper {
	
	private LinearLayout progress;
	private TextView result;
	private View resultLay;
	private View parent;
	private Context context;
	private SharedPreferences shared;
	
	public static int PROGRESS_STATE_START = 0;
	public static int PROGRESS_STATE_SUCCES = 1;
	public static int PROGRESS_STATE_FAIL = -1;
	
	/**
	 * activity引用
	 * @param context
	 */
	public ActivityHelper(Context context) {
		this(context, null);
	}
	/**
	 * fragment引用
	 * @param context
	 * @param parent
	 */
	public ActivityHelper(Context context,View parent) {
		this.context = context;
		this.parent = parent;
		initSharedPreferences();
	}
	private void initSharedPreferences(){
		shared = AllenManager.getInstance().getStoragePreference();
	}
	
	/**
	 * SharedPreferences
	 * @return
	 */
	public SharedPreferences getSharedPreferences(){
		return shared;
	}
	/**
	 * 界面加载处理
	 * @param code 0初始化 1加载完成 -1加载失败
	 * @param msg
	 */
	public void setLoadUi(int code,String msg){
		if(parent!=null){
			progress = parent.findViewById(R.id.app_frame_progress_layout);
			result = parent.findViewById(R.id.app_frame_result_tv);
			resultLay = parent.findViewById(R.id.app_frame_result_layout);
		}else{
			progress = findLinearLayoutById(R.id.app_frame_progress_layout);
			result = findTextViewById(R.id.app_frame_result_tv);
			resultLay = findViewById(R.id.app_frame_result_layout);
		}
		switch (code) {
		case 0:
			progress.setVisibility(View.VISIBLE);
			resultLay.setVisibility(View.GONE);
			break;
		case 1:
			progress.setVisibility(View.GONE);
			break;
		case -1:
			progress.setVisibility(View.VISIBLE);
			resultLay.setVisibility(View.VISIBLE);
			result.setText(StringUtils.empty(msg)?context.getText(R.string.app_frame_result_null):msg);
			result.setOnClickListener(l);
			break;
		default:
			progress.setVisibility(View.GONE);
			break;
		}
	}
	
	private TextView findTextViewById(int resid) {
		return (TextView) ((Activity) context).findViewById(resid);
	}
	private LinearLayout findLinearLayoutById(int resid) {
		return (LinearLayout) ((Activity) context).findViewById(resid);
	}

	private View findViewById(int resId){
		return 	((Activity) context).findViewById(resId);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.app_frame_result_tv){
				if(clickListener!=null){
					clickListener.onAgainClick(v);
				}
			}
		}
	};
	
	private OnProgressClickListener clickListener;
	private long firstTime;
	public void setProgressClickListener(OnProgressClickListener clickListener) {
		this.clickListener = clickListener;
	}
	public interface OnProgressClickListener{
		void onAgainClick(View v);
	}
	/** 隐藏软键盘
	  * hideSoftInputView
	  * @Title: hideSoftInputView
	  * @param
	  * @return void
	  * @throws
	  */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (((Activity) context).getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (((Activity) context).getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}	
	
	/** 显示软键盘
	  * hideSoftInputView
	  * @Title: hideSoftInputView
	  * @param  
	  * @return void
	  * @throws
	  */
	public void showSoftInputView(View v) {
		((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v, 0); 
	}
	
	/**
	 *  连续点击两次退出应用
	 */
	public void doClickTwiceExit(){
		if (firstTime + 3000 > System.currentTimeMillis()) {
			AllenManager.getInstance().exitApp();
		} else {
			MsgUtils.showShortToast(context,"3秒内再按一次退出应用!");
		}
		firstTime = System.currentTimeMillis();
	}
	
	public void doClickTwiceExit(View v){
		if (firstTime + 3000 > System.currentTimeMillis()) {
			AllenManager.getInstance().exitApp();
		} else {
			MsgUtils.showSnackbar(v, "3秒内再按一次退出应用!", "知道了");
		}
		firstTime = System.currentTimeMillis();
	}
	
	ProgressDialog dialog;
	public void showProgressDialog(String msg){
		if(dialog==null||!dialog.isShowing()){
			dialog = new ProgressDialog(context, R.style.Allen_Dialog_Theme);
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
			dialog.setCancelable(false);
			dialog.show();
			dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_bar));
//			dialog.setIndeterminateDrawable(context.getDrawable(R.drawable.dialog_bar));
		}else{
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
		}
	}
	
	public void showProgressDialog(String msg,DialogInterface.OnClickListener clickListener){
		if(dialog==null||!dialog.isShowing()){
			dialog = new ProgressDialog(context, R.style.Allen_Dialog_Theme);
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
			dialog.setCancelable(false);
			if(clickListener!=null){
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", clickListener);
			}
			dialog.show();
			dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_bar));
//			dialog.setIndeterminateDrawable(context.getDrawable(R.drawable.dialog_bar));
		}else{
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void showOldProgressDialog(String msg){
		if(dialog==null?true:!dialog.isShowing()){
			dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
			dialog.setCancelable(false);
			dialog.show();
			dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_bar));
		}else{
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
		}
	}
	@SuppressWarnings("deprecation")
	public void showOldProgressDialog(String msg,DialogInterface.OnClickListener clickListener){
		if(dialog==null?true:!dialog.isShowing()){
			dialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
			dialog.setCancelable(clickListener!=null);
			if(clickListener!=null){
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", clickListener);
			}
			dialog.show();
			dialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.dialog_bar));
		}else{
			dialog.setMessage(StringUtils.notEmpty(msg)?msg:"正在处理,请稍等...");
		}
	}
	public void dismissProgressDialog(){
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
	}
	
	public void setCanLoadMore(MaterialRefreshLayout mater,int pageSize,int csize){
		if(csize>0&&csize%pageSize==0){
			mater.setLoadMore(true);
		}else{
			mater.setLoadMore(false);
		}
	}
	
	public void setCanLoadMore(MaterialRefreshLayout mater,int pageSize,List<?> list){
		int csize = list==null ? 0 : list.size();
		if(csize>0&&csize%pageSize==0){
			mater.setLoadMore(true);
		}else{
			mater.setLoadMore(false);
		}
	}
	
	public void startActivity(Class<?> cls){
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}
	
	public void startActivity(Intent intent){
		context.startActivity(intent);
	}
}
