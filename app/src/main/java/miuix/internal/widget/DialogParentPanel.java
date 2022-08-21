package miuix.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import miuix.appcompat.app.floatingactivity.FloatingABOLayoutSpec;

/* loaded from: classes3.dex */
public class DialogParentPanel extends LinearLayout {
    public FloatingABOLayoutSpec mFloatingWindowSize;

    public DialogParentPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        FloatingABOLayoutSpec floatingABOLayoutSpec = new FloatingABOLayoutSpec(context, attributeSet);
        this.mFloatingWindowSize = floatingABOLayoutSpec;
        floatingABOLayoutSpec.setIsInDialogMode(true);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(this.mFloatingWindowSize.getWidthMeasureSpecForDialog(i), this.mFloatingWindowSize.getHeightMeasureSpecForDialog(i2));
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFloatingWindowSize.onConfigurationChanged();
    }
}
