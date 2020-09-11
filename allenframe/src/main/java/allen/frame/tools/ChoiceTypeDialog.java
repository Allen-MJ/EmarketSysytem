package allen.frame.tools;

import java.util.List;

import allen.frame.R;
import allen.frame.adapter.CityAdapter;
import allen.frame.adapter.TypeAdapter;
import allen.frame.entry.City;
import allen.frame.entry.Type;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ChoiceTypeDialog {
	private Context context;
	private Handler handler;
	private int reciveid;

	public ChoiceTypeDialog(Context context, Handler handler, int reciveid) {
		this.context = context;
		this.handler = handler;
		this.reciveid = reciveid;
	}
	public ChoiceTypeDialog(Context context, OnClickListener listener) {
		this.context = context;
		this.listener = listener;
	}

	private OnClickListener listener;

	public interface OnClickListener{
		void choiceCity(City city);
	}

	public void showDialog(final String sname, final TextView et,
			final List<Type> list) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.alen_type_layout, null);
		ListView lv = (ListView) v.findViewById(R.id.type_lv);
		TypeAdapter adapter = new TypeAdapter(context, list);
		lv.setAdapter(adapter);
		final AlertDialog adialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(sname).setView(v)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).show();
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				et.setText(list.get(arg2).getName());
				adialog.dismiss();
				if (handler != null) {
					Message message = new Message();
					message.what = reciveid;
					message.obj = arg2;
					handler.sendMessage(message);
				}
			}

		});

	}

	public void showCityDialog(final String sname, final TextView et,
			final List<City> list) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.alen_type_layout, null);
		ListView lv = (ListView) v.findViewById(R.id.type_lv);
		CityAdapter adapter = new CityAdapter(context, list);
		lv.setAdapter(adapter);
		final AlertDialog adialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(sname).setView(v)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).show();
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				et.setText(list.get(arg2).getName());
				adialog.dismiss();
				if (listener != null) {
					listener.choiceCity(list.get(arg2));
				}
			}

		});

	}

	public void showDialog(final String sname, final EditText et,
			final List<Type> list) {
		View v = LayoutInflater.from(context).inflate(
				R.layout.alen_type_layout, null);
		ListView lv = (ListView) v.findViewById(R.id.type_lv);
		TypeAdapter adapter = new TypeAdapter(context, list);
		lv.setAdapter(adapter);
		final AlertDialog adialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(sname).setView(v)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).show();
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				et.setText(list.get(arg2).getName());
				adialog.dismiss();
				if (handler != null) {
					Message message = new Message();
					message.what = reciveid;
					message.obj = arg2;
					handler.sendMessage(message);
				}
			}

		});

	}
	public void showChoiceDialog(final String sname,
			final List<Type> list){
		View v = LayoutInflater.from(context).inflate(
				R.layout.alen_type_layout, null);
		ListView lv = (ListView) v.findViewById(R.id.type_lv);
		final TypeAdapter adapter = new TypeAdapter(context, list, true);
		lv.setAdapter(adapter);
		final AlertDialog adialog = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(sname).setView(v)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Message msg = new Message();
						msg.what = reciveid;
						msg.obj = adapter.getSelect();
						handler.sendMessage(msg);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();
					}
				}).show();
	}
}