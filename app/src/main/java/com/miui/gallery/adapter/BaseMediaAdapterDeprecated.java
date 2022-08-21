package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.CursorAdapter;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;

/* loaded from: classes.dex */
public abstract class BaseMediaAdapterDeprecated extends CursorAdapter {
    public Context mContext;
    public int mViewScrollState;

    public abstract void doBindView(View view, Context context, Cursor cursor);

    public abstract long getItemKey(int i);

    public BaseMediaAdapterDeprecated(Context context) {
        super(context, (Cursor) null, false);
        this.mViewScrollState = 0;
        this.mContext = context;
    }

    public final boolean isValidPosition(int i) {
        return i >= 0 && i < getCount();
    }

    public Cursor getCursorByPosition(int i) {
        if (isValidPosition(i)) {
            return (Cursor) getItem(i);
        }
        throw new IllegalArgumentException(String.format("Wrong cursor position %d, total count %d", Integer.valueOf(i), Integer.valueOf(getCount())));
    }

    @Override // android.widget.CursorAdapter
    public final void bindView(View view, Context context, Cursor cursor) {
        view.setTag(R.id.tag_item_unique_id, Long.valueOf(getItemKey(cursor.getPosition())));
        doBindView(view, context, cursor);
    }

    public static String getMicroPath(Cursor cursor, int i, int i2) {
        String string = cursor.getString(i);
        return (!TextUtils.isEmpty(string) || i2 < 0) ? string : CloudUtils.getMicroPath(cursor.getString(i2));
    }
}
