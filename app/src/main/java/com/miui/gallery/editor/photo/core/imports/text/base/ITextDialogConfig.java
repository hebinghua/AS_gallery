package com.miui.gallery.editor.photo.core.imports.text.base;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;

/* loaded from: classes2.dex */
public interface ITextDialogConfig extends ITextSetting {
    void appendUserLocationX(float f);

    void appendUserLocationY(float f);

    boolean contains(float f, float f2);

    void countLocation(boolean z, float f);

    void draw(Canvas canvas);

    BaseDialogModel getDialogModel();

    void getDialogStatusData(DialogStatusData dialogStatusData);

    default String getName() {
        return "";
    }

    void getOutLineRect(RectF rectF);

    float getRotateDegrees();

    String getSampleName();

    float getUserLocationX();

    float getUserLocationY();

    float getUserScaleMultiple();

    boolean isActivation();

    boolean isDialogEnable();

    boolean isReverseColor();

    boolean isSignature();

    boolean isWatermark();

    void onDetachedFromWindow();

    void refreshRotateDegree();

    void reverseColor(int i);

    void setActivation(boolean z);

    void setDialogModel(BaseDialogModel baseDialogModel, Resources resources);

    void setDialogWithStatusData(DialogStatusData dialogStatusData);

    void setDrawOutline(boolean z);

    void setImageInitDisplayRect(RectF rectF);

    default void setName(String str) {
    }

    void setRotateDegrees(float f);

    void setSignatureDrawable(BaseDialogModel baseDialogModel, Drawable drawable);

    void setUserScaleMultiple(float f);

    void toggleMirror();
}
