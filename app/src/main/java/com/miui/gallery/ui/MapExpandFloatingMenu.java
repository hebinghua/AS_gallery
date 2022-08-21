package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class MapExpandFloatingMenu extends LinearLayout {
    public OnMenuItemClickListener mMenuClickListener;

    /* loaded from: classes2.dex */
    public interface OnMenuItemClickListener {
        void click(int i);
    }

    public MapExpandFloatingMenu(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MapExpandFloatingMenu(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    public final void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.map_expand_floating_menu_layout, this);
        ((ImageView) findViewById(R.id.expand_location_btn)).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.MapExpandFloatingMenu$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MapExpandFloatingMenu.this.lambda$initView$0(view);
            }
        });
        ((ImageView) findViewById(R.id.hide_nearby_btn)).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.MapExpandFloatingMenu$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MapExpandFloatingMenu.this.lambda$initView$1(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$0(View view) {
        OnMenuItemClickListener onMenuItemClickListener = this.mMenuClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.click(R.id.expand_location_btn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initView$1(View view) {
        OnMenuItemClickListener onMenuItemClickListener = this.mMenuClickListener;
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.click(R.id.hide_nearby_btn);
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mMenuClickListener = onMenuItemClickListener;
    }
}
