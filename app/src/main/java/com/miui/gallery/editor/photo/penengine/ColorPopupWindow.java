package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.penengine.ColorPickView;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class ColorPopupWindow extends ToolPopupWindow {
    public int mColor;
    public ColorPickView mColorPickView;
    public Context mContext;
    public OnColorChangeListener mOnColorChangeListener;
    public View mRootView;

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface OnColorChangeListener extends ColorPickView.OnColorChangeListener {
    }

    public static /* synthetic */ void $r8$lambda$Gk0Satf17EoskAe8kOVcXzQCFJ8(Class cls, ColorPopupWindow colorPopupWindow, String str, Object obj) {
        lambda$applyParams$1(cls, colorPopupWindow, str, obj);
    }

    /* renamed from: $r8$lambda$tYonr-GoBqdkQVvEzKkzUp2pX_E */
    public static /* synthetic */ void m916$r8$lambda$tYonrGoBqdkQVvEzKkzUp2pX_E(ColorPopupWindow colorPopupWindow, ColorPickView.ColorChangeEvent colorChangeEvent) {
        colorPopupWindow.lambda$initView$0(colorChangeEvent);
    }

    public ColorPopupWindow(Context context) {
        super(context);
        this.mColor = -1;
        this.mContext = context;
        init();
    }

    public final void init() {
        this.mRootView = View.inflate(this.mContext, R.layout.brush_color_layout, null);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_color_popwin_width);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(R.dimen.screen_color_popwin_height);
        setContentView(this.mRootView);
        setWidth(dimensionPixelSize);
        setHeight(dimensionPixelSize2);
        initView();
    }

    public final void initView() {
        ColorPickView colorPickView = (ColorPickView) this.mRootView.findViewById(R.id.color_pick);
        this.mColorPickView = colorPickView;
        colorPickView.setOnColorChangeListener(new ColorPickView.OnColorChangeListener() { // from class: com.miui.gallery.editor.photo.penengine.ColorPopupWindow$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.editor.photo.penengine.ColorPickView.OnColorChangeListener
            public final void onColorChange(ColorPickView.ColorChangeEvent colorChangeEvent) {
                ColorPopupWindow.m916$r8$lambda$tYonrGoBqdkQVvEzKkzUp2pX_E(ColorPopupWindow.this, colorChangeEvent);
            }
        });
    }

    public /* synthetic */ void lambda$initView$0(ColorPickView.ColorChangeEvent colorChangeEvent) {
        this.mColor = colorChangeEvent.getColor();
        OnColorChangeListener onColorChangeListener = this.mOnColorChangeListener;
        if (onColorChangeListener == null) {
            return;
        }
        onColorChangeListener.onColorChange(colorChangeEvent);
        dismiss(true);
    }

    public final void applyParams(Map<String, Object> map) {
        final Class<?> cls = getClass();
        map.forEach(new BiConsumer() { // from class: com.miui.gallery.editor.photo.penengine.ColorPopupWindow$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ColorPopupWindow.$r8$lambda$Gk0Satf17EoskAe8kOVcXzQCFJ8(cls, this, (String) obj, obj2);
            }
        });
    }

    public static /* synthetic */ void lambda$applyParams$1(Class cls, ColorPopupWindow colorPopupWindow, String str, Object obj) {
        try {
            cls.getDeclaredField(str).set(colorPopupWindow, obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void setColor(int i) {
        this.mColor = i;
    }

    public void setPenColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.mOnColorChangeListener = onColorChangeListener;
    }

    public final void setDataToView() {
        this.mColorPickView.setColor(this.mColor);
        this.mColorPickView.updateLabel();
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        setDataToView();
        super.showAtLocation(view, i, i2, i3);
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public final Context mContext;
        public final Map<String, Object> mParams = new HashMap();

        public Builder(Context context) {
            this.mContext = context;
        }

        public ColorPopupWindow create() {
            ColorPopupWindow colorPopupWindow = new ColorPopupWindow(this.mContext);
            colorPopupWindow.applyParams(this.mParams);
            return colorPopupWindow;
        }
    }
}
