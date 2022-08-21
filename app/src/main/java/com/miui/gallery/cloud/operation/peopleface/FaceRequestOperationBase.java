package com.miui.gallery.cloud.operation.peopleface;

import android.content.Context;
import com.miui.gallery.cloud.RequestOperationBase;

/* loaded from: classes.dex */
public abstract class FaceRequestOperationBase extends RequestOperationBase {
    public int getLimitCountForOperation() {
        return -1;
    }

    public FaceRequestOperationBase(Context context) {
        super(context);
    }
}
