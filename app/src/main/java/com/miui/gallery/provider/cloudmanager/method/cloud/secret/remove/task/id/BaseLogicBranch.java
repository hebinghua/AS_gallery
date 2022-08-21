package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseLogicBranch extends com.miui.gallery.provider.cloudmanager.LogicBranch {
    public final String mFileName;
    public final String mLocalFile;
    public final int mLocalFlag;
    public final long mLocalGroupId;
    public final String mMicroThumbnailFile;
    public final byte[] mSecretKey;
    public final String mServerId;
    public final int mServerType;
    public final String mSha1;
    public final String mThumbnailFile;
    public final String mTitle;

    public BaseLogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider) {
        super(context, arrayList);
        this.mFileName = iDataProvider.getFileName();
        this.mSha1 = iDataProvider.getSha1();
        this.mLocalFlag = iDataProvider.getLocalFlag();
        this.mServerId = iDataProvider.getServerId();
        this.mLocalGroupId = iDataProvider.getLocalGroupId();
        this.mLocalFile = iDataProvider.getLocalFile();
        this.mThumbnailFile = iDataProvider.getThumbnailFile();
        this.mMicroThumbnailFile = iDataProvider.getMicroThumbnailFile();
        this.mSecretKey = iDataProvider.getSecretKey();
        this.mServerType = iDataProvider.getServerType();
        this.mTitle = iDataProvider.getTitle();
    }
}
