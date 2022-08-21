package com.miui.gallery.util;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class ViewUtils {
    public static final LazyValue<Void, Integer> LONG_PRESS_TIMEOUT = new LazyValue<Void, Integer>() { // from class: com.miui.gallery.util.ViewUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Integer mo1272onInit(Void r2) {
            return Integer.valueOf(Math.max(550, ViewConfiguration.getLongPressTimeout()));
        }
    };

    public static boolean isMove(int i, int i2, int i3) {
        return i > i2 ? i - i2 > i3 : i2 - i > i3;
    }

    public static void setRootViewClickable(View view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (parent != null && (parent instanceof View)) {
            ((View) parent).setClickable(true);
        } else {
            view.setClickable(true);
        }
    }

    public static String getTextContentExceedMaxWidth(Context context, float f, String str, float f2) {
        return TextUtils.isEmpty(str) ? "" : getTextContentExceedMaxWidth(context, f, Collections.singletonList(str), f2);
    }

    public static String getTextContentExceedMaxWidth(Context context, float f, List<String> list, float f2) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        Paint paint = new Paint();
        paint.setTextSize(f2);
        String str = list.get(0);
        float measureText = paint.measureText(str);
        if (measureText > f) {
            return str;
        }
        String string = context.getString(R.string.name_split);
        float measureText2 = paint.measureText(string);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 1; i < list.size(); i++) {
            float f3 = measureText + measureText2;
            if (f3 > f) {
                break;
            }
            String str2 = list.get(i);
            measureText = f3 + paint.measureText(str2);
            if (measureText > f) {
                break;
            }
            sb.append(string);
            sb.append(str2);
        }
        return sb.toString();
    }

    public static void removeParent(View view) {
        if (view == null) {
            return;
        }
        ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup)) {
            return;
        }
        ((ViewGroup) parent).removeView(view);
    }

    public static int getRealLongPressedTimeout() {
        return LONG_PRESS_TIMEOUT.get(null).intValue();
    }

    public static View getChildRecyclerView(View view) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(view);
        while (!arrayList.isEmpty()) {
            View view2 = (View) arrayList.remove(0);
            if (view2 instanceof RecyclerView) {
                return view2;
            }
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    arrayList.add(viewGroup.getChildAt(i));
                }
            }
        }
        return null;
    }
}
