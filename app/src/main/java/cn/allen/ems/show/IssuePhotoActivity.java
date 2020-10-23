package cn.allen.ems.show;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import allen.frame.AllenBaseActivity;
import allen.frame.AllenManager;
import allen.frame.tools.CommonPopupWindow;
import allen.frame.tools.CommonUtil;
import allen.frame.tools.FileUtils;
import allen.frame.tools.MsgUtils;
import allen.frame.tools.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.allen.ems.R;
import cn.allen.ems.data.WebHelper;
import cn.allen.ems.utils.Constants;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class IssuePhotoActivity extends AllenBaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_des)
    AppCompatEditText etDes;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.regist_bt)
    AppCompatButton registBt;
    @BindView(R.id.image)
    ImageView image;

    private String des = "";
    private SharedPreferences shared;
    private int uid;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    dismissProgressDialog();
                    setResult(RESULT_OK);
                    finish();
                    break;
                case -1:
                    dismissProgressDialog();
                    MsgUtils.showMDMessage(context, (String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected boolean isStatusBarColorWhite() {
        return false;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_issue_photo;
    }

    @Override
    protected void initBar() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initUI(@Nullable Bundle savedInstanceState) {
        shared = AllenManager.getInstance().getStoragePreference();
        uid = shared.getInt(Constants.User_Id, -1);
    }

    @Override
    protected void addEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                finish();
                view.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 拍照
                case REQUEST_TAKE_PHOTO:
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    if (isAndroidQ) {
                        intent.setData(imgUri);
                    } else {
                        intent.setData(Uri.fromFile(imgFile));
                    }
                    sendBroadcast(intent);
                    isCropDialog(imgUri, true);
                    break;

                // 打开图库获取图片
                case SCAN_OPEN_PHONE:
//                    Logger.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    String path = FileUtils.getPath(context, data.getData());
                    file = new File(path);
                    String end = file.getName().substring(file.getName().lastIndexOf(".") + 1,
                            file.getName().length()).toLowerCase();
                    if ("image".equals(FileUtils.fileType(end))) {
                        isCropDialog(data.getData(), false);
                    } else {
                        commitFile(data.getData());
                    }
                    break;
                case SCAN_OPEN_VIDEO:
//                    Logger.e(TAG, "onActivityResult: SCAN_OPEN_PHONE:" + data.getData().toString());
                    String videoPath = FileUtils.getPath(context, data.getData());
                    file = new File(videoPath);
                    String videoEnd = file.getName().substring(file.getName().lastIndexOf(".") + 1,
                            file.getName().length()).toLowerCase();
                    if ("video".equals(FileUtils.fileType(videoEnd))) {
                        commitVideo(data.getData());
                    }
                    break;
            }

        }
    }


    private void isCropDialog(Uri uri, boolean fromCapture) {
        if (fromCapture && !isAndroidQ) {
            commitFile(Uri.fromFile(imgFile));
        } else {
            commitFile(uri);
        }
    }

    private File file;

    private void commitFile(Uri uri) {
        String path = FileUtils.getPath(context, uri);
        file = new File(path);
        Glide.with(context).load(file).into(image);
    }

    private void commitVideo(Uri uri) {
        String path = FileUtils.getPath(context, uri);
//        file = new File(path);
        Bitmap b = getVideoThumbnail(path);
        image.setImageBitmap(b);
    }
    public Bitmap getVideoThumbnail(String filePath) {
        Bitmap b=null;
        //使用MediaMetadataRetriever
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        //FFmpegMediaMetadataRetriever
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        file = new File(filePath);
        try {

            retriever.setDataSource(file.getPath());
            b=retriever.getFrameAtTime(1000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return b;
    }


    private String file2byte(File f) {
        FileInputStream fs = null;
        ByteArrayOutputStream outStream = null;
        try {
            fs = new FileInputStream(f);
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while (-1 != (len = fs.read(buffer))) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String base = Base64.encodeToString(outStream.toByteArray(), Base64.DEFAULT);
        return base;
    }

    private CommonPopupWindow popupWindow;

    public void showPop() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up, null);
        //测量View的宽高
        CommonUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(this.findViewById(android.R.id.content), Gravity
                .BOTTOM, 0, 0);
    }

    private boolean checkIsOk() {
        des = etDes.getText().toString().trim();
        if (StringUtils.empty(des)) {
            MsgUtils.showMDMessage(context, "请输入描述文字!");
            return false;
        } else if (file == null) {
            MsgUtils.showMDMessage(context, "请输入选择上传图片!");
            return false;
        }
        return true;
    }

    @OnClick({R.id.image, R.id.regist_bt})
    public void onViewClicked(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.image:
                showPop();
                break;
            case R.id.regist_bt:
                if (checkIsOk()) {
                    submit();
                }
                break;
        }
        view.setEnabled(true);
    }

    private void submit() {
        showProgressDialog("");
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebHelper.init().putshowPhoto(handler, uid, des, file2byte(file));
            }
        }).start();
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.popup_up:
                Button btn_take_photo = view.findViewById(R.id.btn_take_photo);
                Button btn_select_photo = view.findViewById(R.id.btn_select_photo);
                Button btn_select_video = view.findViewById(R.id.btn_select_video);
                Button btn_cancel = view.findViewById(R.id.btn_cancel);
                btn_take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //("拍照");
                        takePhone();
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });

                btn_select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ("相册选取");
                        if (ContextCompat.checkSelfPermission(context, Manifest
                                .permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                            ActivityCompat.requestPermissions(IssuePhotoActivity.this, new
                                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SCAN_OPEN_PHONE);
                        } else {
                            //已授权，获取照片
                            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intentToPickPic, SCAN_OPEN_PHONE);
                        }
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });

                btn_select_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ("相册选取");
                        if (ContextCompat.checkSelfPermission(context, Manifest
                                .permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            //未授权，申请授权(从相册选择图片需要读取存储卡的权限)
                            ActivityCompat.requestPermissions(IssuePhotoActivity.this, new
                                    String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SCAN_OPEN_VIDEO);
                        } else {
                            //已授权，获取照片
                            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
                            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
                            startActivityForResult(intentToPickPic, SCAN_OPEN_VIDEO);
                        }
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                    }
                });
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        return true;
                    }
                });
                break;
        }
    }

    private Uri imgUri; // 拍照时返回的uri
    private File imgFile;// 拍照保存的图片文件
    private static final int REQUEST_TAKE_PHOTO = 0;// 拍照
    private static final int SCAN_OPEN_VIDEO = 1;// 视频
    private static final int SCAN_OPEN_PHONE = 2;// 相册
    /**
     * 是否是Android 10以上手机
     */
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;

    private void takePhone() {
        // 要保存的文件名
        String time = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String fileName = "market_" + time;
        // 创建一个文件夹
        // 要保存的图片文件
        imgFile = FileUtils.getInstance().creatNewFile("take_photo", fileName + ".jpg");
        // 将file转换成uri
        if (isAndroidQ) {
            // 适配android 10
            imgUri = createImageUri();
        } else {
            // 注意7.0及以上与之前获取的uri不一样了，返回的是provider路径
            imgUri = getUriForFile(context, imgFile);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 添加Uri读取权限
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        // 或者
//        getActivity().grantUriPermission(getActivity().getPackageName(), imgUri, Intent
//                .FLAG_GRANT_READ_URI_PERMISSION);
        // 添加图片保存位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     *
     * @return 图片的uri
     */
    private Uri createImageUri() {
        //设置保存参数到ContentValues中
        ContentValues contentValues = new ContentValues();
        //设置文件名
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + "");
        //兼容Android Q和以下版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
            //TODO RELATIVE_PATH是相对路径不是绝对路径;照片存储的地方为：内部存储/Pictures/preventpro
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/marketCache");
        }
        //设置文件类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        //执行insert操作，向系统文件夹中添加文件
        //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;

    }

    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "cn.allen.ems" +
                    ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


}