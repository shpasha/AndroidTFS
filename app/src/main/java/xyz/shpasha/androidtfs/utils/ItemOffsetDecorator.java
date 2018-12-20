package xyz.shpasha.androidtfs.utils;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemOffsetDecorator extends RecyclerView.ItemDecoration {
    private int offset;

    public ItemOffsetDecorator(int offset) {
        this.offset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = offset;
        outRect.left = offset;
        outRect.top = offset;
        outRect.bottom = offset;
    }
}
