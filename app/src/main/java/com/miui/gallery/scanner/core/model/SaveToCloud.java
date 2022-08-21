package com.miui.gallery.scanner.core.model;

import android.text.TextUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import java.io.File;

/* loaded from: classes2.dex */
public class SaveToCloud {
    public boolean mIsExifSha1;
    public long mLastModify;
    public String mMimeType;
    public String mName;
    public String mPath;
    public String mSha1;
    public long mSize;
    public long mSpecifiedModified = -1;
    public long mSpecifiedTaken = -1;
    public String mTitle;
    public UbifocusEntry mUbiEntry;

    public static SaveToCloud fromSaveParams(SaveParams saveParams) {
        if (saveParams == null || saveParams.getSaveFile() == null || !saveParams.getSaveFile().exists()) {
            return null;
        }
        File saveFile = saveParams.getSaveFile();
        String absolutePath = saveFile.getAbsolutePath();
        SaveToCloud saveToCloud = new SaveToCloud();
        saveToCloud.mPath = absolutePath;
        String fileName = TextUtils.isEmpty(saveParams.getFileName()) ? BaseFileUtils.getFileName(saveToCloud.mPath) : saveParams.getFileName();
        saveToCloud.mName = fileName;
        saveToCloud.mTitle = BaseFileUtils.getFileTitle(fileName);
        saveToCloud.mSize = saveFile.length();
        saveToCloud.mMimeType = TextUtils.isEmpty(saveParams.getMimeType()) ? BaseFileMimeUtil.getMimeType(saveToCloud.mPath) : saveParams.getMimeType();
        saveToCloud.mLastModify = saveFile.lastModified();
        saveToCloud.mSpecifiedTaken = saveParams.getSpecifiedTakenTime();
        saveToCloud.mSpecifiedModified = saveParams.getSpecifiedModifiedTime();
        return saveToCloud;
    }
}
