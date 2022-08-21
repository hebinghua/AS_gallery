package com.miui.gallery.vlog.clip.speed;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;

/* loaded from: classes2.dex */
public class SpeedView extends FrameLayout {
    public FrameLayout mSpeedLayout;
    public TextView mSpeedTxv;

    public SpeedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        View inflate = FrameLayout.inflate(context, R$layout.vlog_single_clip_speed_view_layout, this);
        this.mSpeedLayout = (FrameLayout) inflate.findViewById(R$id.speed_layout);
        this.mSpeedTxv = (TextView) inflate.findViewById(R$id.speed_txv);
    }

    /* renamed from: com.miui.gallery.vlog.clip.speed.SpeedView$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed;

        static {
            int[] iArr = new int[Speed.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed = iArr;
            try {
                iArr[Speed.QUARTER.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.HALF.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.TREBLE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public void setSpeed(Speed speed) {
        String string;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[speed.ordinal()];
        if (i == 1) {
            string = getResources().getString(R$string.vlog_speed_quarter);
        } else if (i == 2) {
            string = getResources().getString(R$string.vlog_speed_half);
        } else if (i == 3) {
            this.mSpeedLayout.setVisibility(0);
            this.mSpeedTxv.setVisibility(8);
            return;
        } else if (i == 4) {
            string = getResources().getString(R$string.vlog_speed_double);
        } else {
            string = i != 5 ? "" : getResources().getString(R$string.vlog_speed_treble);
        }
        this.mSpeedLayout.setVisibility(8);
        this.mSpeedTxv.setVisibility(0);
        this.mSpeedTxv.setText(string);
    }
}
