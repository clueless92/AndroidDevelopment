package bg.tilchev.hw04task1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

//NOTE: Decoration is kinda useless after image setting was moved to RV adapter
public class RecyclerViewCustomDecoration extends RecyclerView.ItemDecoration {

    private static final int OFFSET = 0;

    private int mBackgroundColor;

    private Paint getPaint() {
        Paint background = new Paint();
        background.setColor(this.mBackgroundColor);
        background.setStyle(Paint.Style.FILL);
        return background;
    }

    public RecyclerViewCustomDecoration(Context context) {
        this.mBackgroundColor = ContextCompat.getColor(context, R.color.color_background);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(OFFSET, OFFSET, OFFSET, OFFSET);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        Paint backgroundPaint = this.getPaint();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            c.drawRect(
                    layoutManager.getDecoratedLeft(child) + OFFSET,
                    layoutManager.getDecoratedTop(child) + OFFSET,
                    layoutManager.getDecoratedRight(child) - OFFSET,
                    layoutManager.getDecoratedBottom(child) - OFFSET,
                    backgroundPaint
            );
        }
    }
}
