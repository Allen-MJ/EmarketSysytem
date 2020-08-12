package allen.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import allen.frame.R;
import allen.frame.tools.AllenBannerUtil;
import allen.frame.tools.Logger;
import androidx.annotation.Nullable;

public class PassWordStrongView extends View {

    private int strongType = 0;
    public static final int None = 0;
    public static final int Low = 1;
    public static final int Center = 2;
    public static final int Height = 3;

    private String name = "";
    private static final String LowName = "低";
    private static final String CenterName = "中";
    private static final String HeightName = "高";

    private int initColor = Color.GRAY;
    private int lowColor = Color.RED;
    private int centerColor = Color.YELLOW;
    private int heightColor = Color.GREEN;
    private int textColor = Color.BLACK;
    private float textSize,padding;

    private RectF lowRect,cenRect,heiRect;
    private Paint lowPaint,cenPaint,heiPaint,initPaint;

    public PassWordStrongView(Context context) {
        this(context,null);
    }

    public PassWordStrongView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PassWordStrongView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lowRect = new RectF();
        cenRect = new RectF();
        heiRect = new RectF();
        textSize = AllenBannerUtil.sp2px(context, 20);
        padding = AllenBannerUtil.sp2px(context, 2);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PassWordStrongView);
        initColor = a.getColor(R.styleable.PassWordStrongView_initColor,initColor);
        lowColor = a.getColor(R.styleable.PassWordStrongView_lowColor,lowColor);
        centerColor = a.getColor(R.styleable.PassWordStrongView_centerColor,centerColor);
        heightColor = a.getColor(R.styleable.PassWordStrongView_heightColor,heightColor);
        textColor = a.getColor(R.styleable.PassWordStrongView_android_textColor,textColor);
        textSize = a.getDimension(R.styleable.PassWordStrongView_android_textSize, textSize);
        padding = a.getDimension(R.styleable.PassWordStrongView_android_padding, padding);
        Logger.e("debug","PassWordStrongView"+textSize);


        initPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        initPaint.setStyle(Paint.Style.FILL);//实心矩形框
        initPaint.setColor(initColor);
        initPaint.setTextSize(textSize);
        initPaint.setTextAlign(Paint.Align.CENTER);

        lowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lowPaint.setStyle(Paint.Style.FILL);//实心矩形框
        lowPaint.setColor(lowColor);
        lowPaint.setTextSize(textSize);
        lowPaint.setTextAlign(Paint.Align.CENTER);

        cenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cenPaint.setStyle(Paint.Style.FILL);//实心矩形框
        cenPaint.setColor(centerColor);
        cenPaint.setTextSize(textSize);
        cenPaint.setTextAlign(Paint.Align.CENTER);

        heiPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        heiPaint.setStyle(Paint.Style.FILL);//实心矩形框
        heiPaint.setColor(heightColor);
        heiPaint.setTextSize(textSize);
        heiPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算baseline
        Paint.FontMetrics fontMetrics=initPaint.getFontMetrics();
        float distance=(fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        float baseline=padding+textSize/2+distance;
        switch (strongType){
            case None:
                canvas.drawRect(lowRect,initPaint);
                canvas.drawRect(cenRect,initPaint);
                canvas.drawRect(heiRect,initPaint);
                break;
            case Low:
                canvas.drawRect(lowRect,lowPaint);
                canvas.drawRect(cenRect,initPaint);
                canvas.drawRect(heiRect,initPaint);
                canvas.drawText(TextUtils.isEmpty(name)?LowName:name,heiRect.right+padding+textSize/2,baseline,lowPaint);
                break;
            case Center:
                canvas.drawRect(lowRect,cenPaint);
                canvas.drawRect(cenRect,cenPaint);
                canvas.drawRect(heiRect,initPaint);
                canvas.drawText(TextUtils.isEmpty(name)?CenterName:name,heiRect.right+padding+textSize/2,baseline,cenPaint);
                break;
            case Height:
                canvas.drawRect(lowRect,heiPaint);
                canvas.drawRect(cenRect,heiPaint);
                canvas.drawRect(heiRect,heiPaint);
                canvas.drawText(TextUtils.isEmpty(name)?HeightName:name,heiRect.right+padding+textSize/2,baseline,heiPaint);
                break;
        }
    }

    public void setStrongType(int type){
        this.strongType = type;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        lowRect = new RectF();
        cenRect = new RectF();
        heiRect = new RectF();
        float item = (w-textSize-padding*5)/3f;
        lowRect.set(padding,padding,padding+item,h-padding);
        cenRect.set(lowRect.right+padding,padding,lowRect.right+padding+item,h-padding);
        heiRect.set(cenRect.right+padding,padding,cenRect.right+padding+item,h-padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定并无固定依据,根据需要灵活设置
        // 类似TextView,ImageView等针对wrap_content均在onMeasure()对设置默认宽 / 高值有特殊处理,具体读者可以自行查看
        int mWidth = (int) (textSize*10+padding*5);
        int mHeight = (int) (textSize+padding*2);

        // 当布局参数设置为wrap_content时，设置默认值
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个布局参数为= wrap_content时，都设置默认值
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }
}
