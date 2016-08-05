package rhg.com.sample.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/*
 *desc 自定义缩放登录按钮
 *author rhg
 *time 2016/7/30 17:58
 *email 1013773046@qq.com
 */
public class CustomLoginButton extends View {

    /*UI的宽*/
    int width;
    /*UI的高*/
    int height;


    /*记录按钮状态*/
    boolean isLogin;

    /*绘制文字的paint*/
    Paint textPaint;
    /*文字文本*/
    private String text = "hhhhhhh";
    /*文字颜色*/
    private int textColor;
    /*文字大小*/
    private float textSize = dip2px(15);

    /*绘制实心圆的paint*/
    Paint fillCirclePaint;
    /*实心圆的半径*/
    private float fillRadius = dip2px(10);
    /*实心圆/实心矩形的填充颜色*/
    private int fillColor;

    /*绘制圆弧的paint*/
    Paint strokeArcPaint;
    /*空心圆弧的半径*/
    private float arcRadius = dip2px(10);
    /*空心圆弧的线宽*/
    private float arcWidth = dip2px(2);
    /*空心圆弧的填充颜色*/
    private int arcColor;

    /*绘制矩形的paint，矩形的高就是fillRadius*2*/
    Paint rectPaint;
    /*矩形的区域*/
    RectF buttonRect;

    /*动画*/
    RotateAnimation rotateAnimation;


    public CustomLoginButton(Context context) {
        super(context);
    }

    public CustomLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainAttr(context, attrs);
        textPaint = getTextPaint();
        fillCirclePaint = getFillCirclePaint();
        strokeArcPaint = getArcPaint();
        rectPaint = getRoundRectPaint();
    }

    /*获取文字画笔*/
    private Paint getTextPaint() {
        Paint _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(textColor);
        _paint.setTextSize(textSize);
        return _paint;
    }

    /*获取实心圆的画笔*/
    private Paint getFillCirclePaint() {
        Paint _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(textColor);
        _paint.setStyle(Paint.Style.FILL);
        return _paint;
    }

    /*获取圆弧的画笔*/
    private Paint getArcPaint() {
        Paint _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(arcColor);
        _paint.setStrokeWidth(arcWidth);
        _paint.setStyle(Paint.Style.STROKE);
        return _paint;
    }

    /*获取圆角矩形/直线画笔*/
    private Paint getRoundRectPaint() {
        Paint _paint = new Paint();
        _paint.setAntiAlias(true);
        _paint.setColor(fillColor);
        _paint.setStrokeCap(Paint.Cap.ROUND);/*圆角矩形*/
        _paint.setStyle(Paint.Style.FILL);
        return _paint;
    }

    private void obtainAttr(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoginButtonStyle);
            /*getDimension和getDimensionPixelOffset的效果是一样的，返回的值都乘了一个density,getDimensionPixelSize则不会*/
        textSize = a.getDimension(R.styleable.LoginButtonStyle_textSize, textSize);
        textColor = a.getColor(R.styleable.LoginButtonStyle_textColor, context.getResources().getColor(R.color.colorPrimary));
        fillColor = a.getColor(R.styleable.LoginButtonStyle_fillColor, context.getResources().getColor(R.color.colorAccent));
        arcColor = a.getColor(R.styleable.LoginButtonStyle_strokeArcColor, Color.BLACK);
        arcRadius = a.getDimension(R.styleable.LoginButtonStyle_strokeArcRadius, arcRadius);
        fillRadius = a.getDimension(R.styleable.LoginButtonStyle_fillRadius, fillRadius);
        a.recycle();
    }

    /*测量UI的尺寸*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureHeight(int heightMeasureSpec) {
        return (int) (fillRadius * 2);
    }

    private int measureWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }

    /*记录绘制UI的宽和高，在onMeasure调用之后才有值*/
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        buttonRect = new RectF(0, 0, width, height);

    }

    /*设置好尺寸后开始绘制UI*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButton(canvas);
        drawText(canvas, text);
        if (isLogin) {
            drawLoading(canvas);
        }
    }


    private void drawButton(Canvas canvas) {
        canvas.drawRoundRect(buttonRect, fillRadius, fillRadius, rectPaint);
    }

    /*文字的绘制以baseline为基准*/
    private void drawText(Canvas canvas, String text) {
        float x = (width - textPaint.measureText(text)) / 2;
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float y = fillRadius - (fm.ascent + fm.descent) / 2;/*理解android中文字的结构，top、accent、baseline、decent、bottom*/
        canvas.drawText(text, x, y, textPaint);
    }

    private void drawLoading(Canvas canvas) {
//        canvas.translate(width / 2, (height - arcRadius) / 2);/*将弧心移到中间*/
        RectF arcRect = new RectF(width / 2 - arcRadius, height / 2 - arcRadius, width / 2 + arcRadius, height / 2 + arcRadius);/*设置弧的矩形区域    */
        canvas.rotate(0);
        canvas.scale(1F, 1F);
        canvas.drawArc(arcRect, -90, 180, false, strokeArcPaint);/*画弧，角度是以3点钟方向为0°，顺时针转向为正.*/
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void doLogin() {
        setClickable(false);
        final ValueAnimator valueAnimator = getValueAnimator(0F, 1F);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float afloat = Float.parseFloat(animation.getAnimatedValue().toString());
                float left = afloat * (width - height) / 2;
                textPaint.setAlpha((int) (255 * (1 - afloat)));/*设置字体透明度*/
                if (afloat == 1) {
                    if (buttonRect != null)
                        buttonRect.set((width - height) / 2, 0, (width + height) / 2, height);
                    else
                        buttonRect = new RectF((width - height) / 2, 0, (width + height) / 2, height);
                    isLogin = true;
                    invalidate();
                    valueAnimator.cancel();
                    startLoading();
                    return;
                }
                float right = width - left;
                if (buttonRect != null)
                    buttonRect.set(left, 0, right, height);
                else buttonRect = new RectF(left, 0, right, height);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private void startLoading() {
        rotateAnimation = new RotateAnimation(0F, 360F, width / 2, height / 2);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(700);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        startAnimation(rotateAnimation);
    }

    private void stopLoading() {
        isLogin = false;
        if (rotateAnimation != null)
            rotateAnimation.cancel();
    }

    /*获取属性动画*/
    private ValueAnimator getValueAnimator(float start, float end) {
        ValueAnimator _animator = ValueAnimator.ofFloat(start, end);/*属性动画区间*/
        _animator.setDuration(500);/*属性动画运行时间*/
        _animator.setInterpolator(new LinearInterpolator());/*属性动画插值器*/
        _animator.setRepeatMode(0);/*属性动画模式，0表示不重复*/
        return _animator;
    }

    /*停止正在运行的动画*/
    public void stopRunningAnimation() {
        if (rotateAnimation != null)
            rotateAnimation.cancel();
    }

}
