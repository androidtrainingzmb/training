package tcd.training.com.trainingproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by cpu10661-local on 18/07/2017.
 */

public class CircleView extends View {

    private Context mContext;
    private int mCircleColor, mLabelColor;
    private String mCircleLabel;
    private Paint circlePaint;

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        circlePaint = new Paint();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0);

        try {
            mCircleColor = a.getInteger(R.styleable.CircleView_circleColor, 0);
            mCircleLabel = a.getString(R.styleable.CircleView_circleLabel);
            mLabelColor = a.getInteger(R.styleable.CircleView_labelColor, 0);
        } finally {
            a.recycle();
        }
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
        invalidate();
        requestLayout();
    }

    public int getLabelColor() {
        return mLabelColor;
    }

    public void setLabelColor(int mLabelColor) {
        this.mLabelColor = mLabelColor;
        invalidate();
        requestLayout();
    }

    public String getCircleLabel() {
        return mCircleLabel;
    }

    public void setCircleLabel(String mCircleLabel) {
        this.mCircleLabel = mCircleLabel;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // determine the radius
        int halfWidth = this.getMeasuredWidth() / 2;
        int halfHeight = this.getMeasuredHeight() / 2;
        
        int radius = halfWidth < halfHeight ? halfWidth - 10 : halfHeight - 10;

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        // draw the circle
        circlePaint.setColor(mCircleColor);
        canvas.drawCircle(halfWidth, halfHeight, radius, circlePaint);

        // draw the label
        circlePaint.setColor(mLabelColor);
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        canvas.drawText(mCircleLabel, halfWidth, halfHeight, circlePaint);

    }
}
