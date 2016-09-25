package bg.tilchev.hw02resourcesviewseventhandlerslayouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

public class SquareView extends View {

    public static final byte MARK_EMPTY = 0;
    public static final byte MARK_PLAYER1 = 1;
    public static final byte MARK_PLAYER2 = 2;

    private byte mMark;
    private WeakReference<Paint> mPaintRef;

    private Paint getPaint() {
        Paint result;
        if (this.mPaintRef == null) {
            result = new Paint();
            this.mPaintRef = new WeakReference<>(result);
            return result;
        }
        result = this.mPaintRef.get();
        if (result == null) {
            result = new Paint();
            this.mPaintRef = new WeakReference<>(result);
        }
        return result;
    }

    public byte getMark() {
        return this.mMark;
    }

    public void setMark(byte mark) {
        this.mMark = mark;
    }

    public SquareView(Context context) {
        super(context);
       this.setMark(MARK_EMPTY);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setMark(MARK_EMPTY);
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setMark(MARK_EMPTY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = this.getPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(20f);
        int squareWidth = this.getWidth();
        int squareHeight = this.getHeight();
        int paddingWidth = squareWidth >> 3;
        int paddingHeight = squareHeight >> 3;
        switch (this.mMark) {
            case MARK_PLAYER1:
                paint.setColor(Color.RED);
                int paddedBottom = squareWidth - paddingWidth;
                int paddedRight = squareHeight - paddingHeight;
                canvas.drawLine(paddingWidth, paddingHeight, paddedBottom, paddedRight, paint);
                canvas.drawLine(paddingWidth, paddedRight, paddedBottom, paddingHeight, paint);
                break;
            case MARK_PLAYER2:
                paint.setColor(Color.BLUE);
                float ox = squareWidth >> 1;
                float oy = squareHeight >> 1;
                float radius = ox - paddingWidth;
                canvas.drawCircle(ox, oy, radius, paint);
                break;
        }
    }
}
