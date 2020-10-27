package cn.allen.ems.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MyTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView img;

    public MyTask(ImageView img) {
        this.img = img;
    }

    //运行在后台，进行网络请求，子线程
    @Override
    protected Bitmap doInBackground(String... strings) {//...可变长类型 string....相当于 String[]

        return getNetVideoThumbnail(strings[0]);
    }

    @Override
    protected void onPostExecute(Bitmap s) {
        img.setImageBitmap(s);

    }
    public Bitmap getNetVideoThumbnail(String url) {
        Bitmap b = null;

        //FFmpegMediaMetadataRetriever
        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();

        try {
            retriever.setDataSource(url);
            b = retriever.getFrameAtTime(1000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
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
}
