package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseLogicBranch extends com.miui.gallery.provider.cloudmanager.LogicBranch {
    public final String mFileName;
    public final long mGroupId;
    public final String mLocalFile;
    public final int mLocalFlag;
    public final long mServerId;
    public final String mServerStatus;
    public final String mSha1;
    public final String mThumbnailFile;

    public BaseLogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider) {
        super(context, arrayList);
        this.mServerId = iDataProvider.getServerId();
        this.mLocalFile = iDataProvider.getLocalFile();
        this.mThumbnailFile = iDataProvider.getThumbnailFile();
        this.mServerStatus = iDataProvider.getServerStatus();
        this.mFileName = iDataProvider.getFileName();
        this.mSha1 = iDataProvider.getSha1();
        this.mLocalFlag = iDataProvider.getLocalFlag();
        this.mGroupId = iDataProvider.getGroupId();
    }
}
