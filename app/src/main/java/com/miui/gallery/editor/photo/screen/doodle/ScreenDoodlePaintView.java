package com.miui.gallery.editor.photo.screen.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.editor.photo.app.doodle.DoodlePaintItem;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenDoodlePaintView extends View {
    public Rect mBasePaintRect;
    public int mCurrentPaintIndex;
    public List<DoodlePaintItem> mDoodlePaintItemList;

    public ScreenDoodlePaintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentPaintIndex = 1;
        this.mBasePaintRect = new Rect();
        init();
    }

    public final void init() {
        this.mDoodlePaintItemList = DoodlePaintItem.getList(getResources());
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setPaintLocation(i, i2);
    }

    public final void setPaintLocation(int i, int i2) {
        this.mBasePaintRect.set(0, 0, i, i2);
        for (int i3 = 0; i3 < this.mDoodlePaintItemList.size(); i3++) {
            DoodlePaintItem doodlePaintItem = this.mDoodlePaintItemList.get(i3);
            doodlePaintItem.setBounds(this.mBasePaintRect);
            doodlePaintItem.setSelect(true);
            doodlePaintItem.setBigSize((int) (i - getResources().getDisplayMetrics().density));
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).draw(canvas);
    }

    public void setPaintColor(int i) {
        for (int i2 = 0; i2 < this.mDoodlePaintItemList.size(); i2++) {
            this.mDoodlePaintItemList.get(i2).setCurrentColor(i);
        }
    }

    public DoodlePaintItem.PaintType getPaintType() {
        return this.mDoodlePaintItemList.get(this.mCurrentPaintIndex).paintType;
    }

    /* renamed from: com.miui.gallery.editor.photo.screen.doodle.ScreenDoodlePaintView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType;

        static {
            int[] iArr = new int[DoodlePaintItem.PaintType.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType = iArr;
            try {
                iArr[DoodlePaintItem.PaintType.HEAVY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[DoodlePaintItem.PaintType.MEDIUM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[DoodlePaintItem.PaintType.LIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public void setPaintType(DoodlePaintItem.PaintType paintType) {
        if (paintType == null) {
            return;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$app$doodle$DoodlePaintItem$PaintType[paintType.ordinal()];
        if (i == 1) {
            int i2 = this.mCurrentPaintIndex + 1;
            this.mCurrentPaintIndex = i2;
            this.mCurrentPaintIndex = i2 % this.mDoodlePaintItemList.size();
        } else if (i != 3) {
        } else {
            int i3 = this.mCurrentPaintIndex + 2;
            this.mCurrentPaintIndex = i3;
            this.mCurrentPaintIndex = i3 % this.mDoodlePaintItemList.size();
        }
    }
}
