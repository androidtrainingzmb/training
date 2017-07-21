package tcd.training.com.trainingproject.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 18/07/2017.
 */

public class CircleView extends View {

    private Context mContext;

    // attributes
    private int mCircleColor, mLabelColor, mCircleSize;
    private String mCircleLabel;
    private Paint circlePaint;

    // drawing properties
    private float radius;
    private float centerX, centerY;

    // gesture
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        circlePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0);
        try {
            mCircleColor = a.getInteger(R.styleable.CircleView_circleColor, 0);
            mCircleLabel = a.getString(R.styleable.CircleView_circleLabel);
            mLabelColor = a.getInteger(R.styleable.CircleView_labelColor, 0);
            mCircleSize = a.getInteger(R.styleable.CircleView_circleSize, 0);
        } finally {
            a.recycle();
        }
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        invalidate();
        requestLayout();
    }

    public int getLabelColor() {
        return mLabelColor;
    }

    public void setLabelColor(int labelColor) {
        this.mLabelColor = labelColor;
        invalidate();
        requestLayout();
    }

    public String getCircleLabel() {
        return mCircleLabel;
    }

    public void setCircleLabel(String circleLabel) {
        this.mCircleLabel = circleLabel;
        invalidate();
        requestLayout();
    }

    public int getCircleSize() {
        return mCircleSize;
    }

    public void setCircleSize(int circleSize) {
        this.mCircleSize = circleSize;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        // draw the circle
        circlePaint.setColor(mCircleColor);
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        // draw the label
        circlePaint.setColor(mLabelColor);
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        canvas.drawText(mCircleLabel, centerX, centerY, circlePaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        centerX = ((float)w - xpad) / 2;
        centerY = ((float)h - ypad) / 2;

        // Figure out how big we can make the circle
        radius = Math.min(centerX, centerY);
        switch (mCircleSize) {
            case 1: radius /= 2; break;
            case 2: radius = radius * 2 / 3; break;
            case 3: break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                return true;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX > 0) {
                        Toast.makeText(mContext, "Left to right swipe", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "Right to left swipe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Tap or top-down swipe", Toast.LENGTH_SHORT).show();
                } 
                break;
        }
        return super.onTouchEvent(event);
    }
}
