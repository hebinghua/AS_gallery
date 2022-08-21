package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseLogicBranch extends com.miui.gallery.provider.cloudmanager.LogicBranch {
    public final String mFileName;
    public final String mLocalFile;
    public final int mLocalFlag;
    public final long mLocalGroupId;
    public final String mMicroThumbnailFile;
    public final String mServerId;
    public final String mServerStatus;
    public final int mServerType;
    public final String mSha1;
    public final String mThumbnailFile;

    public BaseLogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider) {
        super(context, arrayList);
        this.mLocalFlag = iDataProvider.getLocalFlag();
        this.mLocalGroupId = iDataProvider.getLocalGroupId();
        this.mSha1 = iDataProvider.getSha1();
        this.mFileName = iDataProvider.getFileName();
        this.mServerType = iDataProvider.getServerType();
        this.mMicroThumbnailFile = iDataProvider.getMicroThumbnailFile();
        this.mThumbnailFile = iDataProvider.getThumbnailFile();
        this.mLocalFile = iDataProvider.getLocalFile();
        this.mServerId = iDataProvider.getServerId();
        this.mServerStatus = iDataProvider.getServerStatus();
    }
}
