package com.example.olio;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

public class CheckMarkView extends View {
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private float length;
    private float animationProgress = 0f;

    public CheckMarkView(Context context) {
        super(context);
        init();
    }

    public CheckMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Create checkmark path
        float centerX = w / 2f;
        float centerY = h / 2f;
        path.reset();
        path.moveTo(centerX - 30f, centerY);
        path.lineTo(centerX - 10f, centerY + 20f);
        path.lineTo(centerX + 30f, centerY - 20f);

        pathMeasure = new PathMeasure(path, false);
        length = pathMeasure.getLength();

        startAnimation();
    }

    private void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(1000);
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path drawPath = new Path();
        pathMeasure.getSegment(0f, length * animationProgress, drawPath, true);
        canvas.drawPath(drawPath, paint);
    }
}