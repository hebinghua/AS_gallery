package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.util.AttributeSet;

/* loaded from: classes3.dex */
public class nexEffectPreviewView extends com.nexstreaming.app.common.nexasset.preview.a {
    public nexEffectPreviewView(Context context) {
        super(context);
        super.setAspectRatio(nexApplicationConfig.getAspectRatio());
    }

    public nexEffectPreviewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        super.setAspectRatio(nexApplicationConfig.getAspectRatio());
    }

    @Override // com.nexstreaming.app.common.nexasset.preview.a
    public void setEffect(String str) {
        super.setEffect(str);
    }

    @Override // com.nexstreaming.app.common.nexasset.preview.a
    public void setEffectTime(int i) {
        super.setEffectTime(i);
    }

    @Override // com.nexstreaming.app.common.nexasset.preview.a
    public void setEffectOptions(String str) {
        super.setEffectOptions(str);
    }
}
