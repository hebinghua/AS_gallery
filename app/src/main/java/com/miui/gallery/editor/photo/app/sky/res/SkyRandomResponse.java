package com.miui.gallery.editor.photo.app.sky.res;

import androidx.annotation.Keep;
import java.sql.Array;

@Keep
/* loaded from: classes2.dex */
public class SkyRandomResponse {
    private int code;
    private Array details;
    private String message;

    public int getCode() {
        return this.code;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String str) {
        this.message = str;
    }

    public Array getDetails() {
        return this.details;
    }

    public void setDetails(Array array) {
        this.details = array;
    }
}
