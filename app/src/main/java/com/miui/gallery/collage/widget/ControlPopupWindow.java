package com.miui.gallery.collage.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import com.miui.gallery.R;

/* loaded from: classes.dex */
public class ControlPopupWindow extends PopupWindow {
    public Context mContext;
    public ControlListener mControlListener;
    public int mHeight;
    public int mMargin;
    public View.OnClickListener mOnClickListener;
    public int mWidth;
    public ImageView mirror;
    public ImageView replace;
    public ImageView rotate;

    /* loaded from: classes.dex */
    public interface ControlListener {
        void onDismiss();

        void onMirror();

        void onReplace();

        void onRotate();
    }

    public ControlPopupWindow(Context context) {
        this(context, true);
    }

    public ControlPopupWindow(Context context, boolean z) {
        super(context);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.collage.widget.ControlPopupWindow.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.mirror) {
                    if (ControlPopupWindow.this.mControlListener == null) {
                        return;
                    }
                    ((Animatable) ControlPopupWindow.this.mirror.getDrawable()).start();
                    ControlPopupWindow.this.mControlListener.onMirror();
                } else if (id != R.id.replace) {
                    if (id != R.id.rotate || ControlPopupWindow.this.mControlListener == null) {
                        return;
                    }
                    ((Animatable) ControlPopupWindow.this.rotate.getDrawable()).start();
                    ControlPopupWindow.this.mControlListener.onRotate();
                } else if (ControlPopupWindow.this.mControlListener == null) {
                } else {
                    ((Animatable) ControlPopupWindow.this.replace.getDrawable()).start();
                    ControlPopupWindow.this.mControlListener.onReplace();
                }
            }
        };
        init(context, z);
    }

    public final void init(Context context, boolean z) {
        this.mContext = context;
        View inflate = View.inflate(context, R.layout.collage_dialog, null);
        setContentView(inflate);
        setBackgroundDrawable(new ColorDrawable(0));
        setOutsideTouchable(false);
        setFocusable(false);
        setAnimationStyle(2131951831);
        setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.miui.gallery.collage.widget.ControlPopupWindow.1
            @Override // android.widget.PopupWindow.OnDismissListener
            public void onDismiss() {
                if (ControlPopupWindow.this.mControlListener != null) {
                    ControlPopupWindow.this.mControlListener.onDismiss();
                }
            }
        });
        this.mWidth = context.getResources().getDimensionPixelSize(z ? R.dimen.collage_image_chose_icon_width : R.dimen.collage_image_chose_icon_width_no_rotation);
        this.mHeight = context.getResources().getDimensionPixelSize(R.dimen.collage_image_chose_icon_height);
        this.mMargin = context.getResources().getDimensionPixelSize(R.dimen.collage_dialog_margin);
        setWidth(this.mWidth);
        setHeight(this.mHeight);
        this.replace = (ImageView) inflate.findViewById(R.id.replace);
        this.rotate = (ImageView) inflate.findViewById(R.id.rotate);
        this.mirror = (ImageView) inflate.findViewById(R.id.mirror);
        if (!z) {
            this.rotate.setVisibility(8);
        }
        this.replace.setOnClickListener(this.mOnClickListener);
        this.rotate.setOnClickListener(this.mOnClickListener);
        this.mirror.setOnClickListener(this.mOnClickListener);
    }

    public void setControlListener(ControlListener controlListener) {
        this.mControlListener = controlListener;
    }

    public void showAtLocation(View view, View view2) {
        showAtLocation(view, view2, false);
    }

    public void showAtLocation(View view, View view2, boolean z) {
        int i;
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        view2.getLocationInWindow(iArr);
        view.getLocationInWindow(iArr2);
        if (iArr[1] + (view2.getHeight() / 2) < iArr2[1] + (view.getHeight() / 2) && !z) {
            i = iArr[1] + view2.getHeight() + this.mMargin;
        } else {
            i = (iArr[1] - this.mHeight) - this.mMargin;
        }
        if (i < 0) {
            i = Math.round(iArr[1] - (this.mHeight / 2.0f));
        }
        if (i < 0) {
            i = iArr[1] + this.mMargin;
        }
        int width = (iArr[0] + (view2.getWidth() / 2)) - (this.mWidth / 2);
        if (isShowing()) {
            update(width, i, this.mWidth, this.mHeight);
        } else {
            super.showAtLocation(view, 8388659, width, i);
        }
    }
}
