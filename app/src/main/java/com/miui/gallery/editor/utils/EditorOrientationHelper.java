package com.miui.gallery.editor.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class EditorOrientationHelper {
    public static boolean isLayoutPortrait(Context context) {
        if (context == null) {
            return true;
        }
        return isLayoutPortrait(context.getResources().getConfiguration());
    }

    public static boolean isLayoutPortrait(Configuration configuration) {
        return configuration == null || layoutOrientation(configuration) == 1;
    }

    public static int layoutOrientation(Configuration configuration) {
        return (configuration.orientation != 2 || configuration.screenWidthDp < 1000) ? 1 : 2;
    }

    public static void copyLayoutParams(View view, View view2, boolean z) {
        if (view == null || view2 == null || view.getClass() != view2.getClass() || view.getId() != view2.getId()) {
            return;
        }
        if (view.getLayoutParams() != null) {
            view2.setLayoutParams(view.getLayoutParams());
            view2.setPadding(view.getPaddingStart(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            if ((view instanceof LinearLayout) && (view2 instanceof LinearLayout)) {
                ((LinearLayout) view2).setOrientation(((LinearLayout) view).getOrientation());
            }
        }
        if (!z || !(view instanceof ViewGroup) || !(view2 instanceof ViewGroup) || (view instanceof RecyclerView) || (view2 instanceof RecyclerView)) {
            return;
        }
        int i = 0;
        while (true) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (i >= viewGroup.getChildCount()) {
                return;
            }
            copyLayoutParams(viewGroup.getChildAt(i), ((ViewGroup) view2).getChildAt(i), z);
            i++;
        }
    }
}
