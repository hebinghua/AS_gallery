package com.miui.gallery.util;

import android.text.TextUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: ExifUtil.java */
/* loaded from: classes2.dex */
public class UserComment {
    public ExifInterfaceWrapper mExif;
    public boolean mIsOriginalUserCommentUsable;
    public JSONObject mJsonUserComment;

    public UserComment(ExifInterfaceWrapper exifInterfaceWrapper) throws Exception {
        this.mIsOriginalUserCommentUsable = false;
        this.mExif = exifInterfaceWrapper;
        String userComment = exifInterfaceWrapper.getUserComment();
        try {
            if (TextUtils.isEmpty(userComment)) {
                this.mJsonUserComment = new JSONObject();
            } else {
                this.mJsonUserComment = new JSONObject(userComment);
                this.mIsOriginalUserCommentUsable = true;
            }
        } catch (JSONException e) {
            this.mJsonUserComment = new JSONObject();
            DefaultLogger.d("UserComment", "userComment %s is not a json object", userComment);
            DefaultLogger.e("UserComment", e);
        }
    }

    public boolean isOriginalUserCommentUsable() {
        return this.mIsOriginalUserCommentUsable;
    }

    public String getSha1() {
        return this.mJsonUserComment.optString("sha1");
    }

    public String getFileExt() {
        return this.mJsonUserComment.optString("ext");
    }

    public void setData(ExifUtil.UserCommentData userCommentData) throws Exception {
        if (userCommentData == null) {
            return;
        }
        this.mJsonUserComment.put("sha1", userCommentData.getSha1());
        this.mJsonUserComment.put("ext", userCommentData.getExt());
        this.mExif.setUserComment(toString());
    }

    public String toString() {
        return (!TextUtils.isEmpty(getSha1()) || !TextUtils.isEmpty(getFileExt())) ? this.mJsonUserComment.toString() : "";
    }
}
