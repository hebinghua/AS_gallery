package com.miui.gallery.cloud.peopleface;

import android.accounts.Account;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.RetryOperation;
import com.miui.gallery.cloud.SyncFromLocalBase;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.operation.peopleface.CreatePeopleOperation;
import com.miui.gallery.cloud.operation.peopleface.DeleteFaceOperation;
import com.miui.gallery.cloud.operation.peopleface.FaceRequestOperationBase;
import com.miui.gallery.cloud.operation.peopleface.IgnorePeopleOperation;
import com.miui.gallery.cloud.operation.peopleface.MergePeopleOperation;
import com.miui.gallery.cloud.operation.peopleface.MoveFaceOperation;
import com.miui.gallery.cloud.operation.peopleface.RecoveryPeopleOperation;
import com.miui.gallery.cloud.operation.peopleface.RenamePeopleOperation;
import com.miui.gallery.cloud.operation.peopleface.UpdatePeopleInfoOperation;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.util.SyncLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class SyncFaceFromLocal extends SyncFromLocalBase {
    public RequestItemGroup mCreatePeople;
    public RequestItemGroup mDeleteFace;
    public RequestItemGroup mIgnorePeople;
    public RequestItemGroup mMergePeople;
    public RequestItemGroup mMoveFace;
    public RequestItemGroup mRecoveryPeople;
    public RequestItemGroup mRenamePeople;
    public RequestItemGroup mUpdateInfoPeople;

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public String getSortOrder() {
        return null;
    }

    public SyncFaceFromLocal(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public Uri getBaseUri() {
        return FaceDataManager.PEOPLE_FACE_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    /* renamed from: generateDBImage */
    public DBItem mo689generateDBImage(Cursor cursor) {
        return new PeopleFace(cursor);
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public String getSelectionClause() {
        return String.format(" (%s) ", "localFlag != 0");
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void initRequestCloudItemList() {
        this.mCreatePeople = new RequestItemGroup();
        this.mMoveFace = new RequestItemGroup();
        this.mDeleteFace = new RequestItemGroup();
        this.mMergePeople = new RequestItemGroup();
        this.mRenamePeople = new RequestItemGroup();
        this.mIgnorePeople = new RequestItemGroup();
        this.mRecoveryPeople = new RequestItemGroup();
        this.mUpdateInfoPeople = new RequestItemGroup();
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void putToRequestCloudItemList(DBItem dBItem) {
        PeopleFace peopleFace = (PeopleFace) dBItem;
        boolean equalsIgnoreCase = peopleFace.type.equalsIgnoreCase("PEOPLE");
        if (peopleFace.visibilityType == 4) {
            this.mRecoveryPeople.addItem(new RequestFaceItem(0, peopleFace));
        }
        int i = peopleFace.localFlag;
        if (i == 2) {
            RequestFaceItem requestFaceItem = new RequestFaceItem(0, peopleFace);
            if (equalsIgnoreCase) {
                return;
            }
            this.mDeleteFace.addItem(requestFaceItem);
        } else if (i == 5) {
            if (equalsIgnoreCase) {
                return;
            }
            this.mMoveFace.addItem(new RequestFaceItem(0, peopleFace));
        } else if (i == 8) {
            this.mCreatePeople.addItem(new RequestFaceItem(0, peopleFace));
        } else if (i == 10) {
            if (!equalsIgnoreCase) {
                return;
            }
            this.mRenamePeople.addItem(new RequestFaceItem(0, peopleFace));
        } else if (i == 16) {
            if (!equalsIgnoreCase) {
                return;
            }
            this.mUpdateInfoPeople.addItem(new RequestFaceItem(0, peopleFace));
        } else if (i != 12) {
            if (i != 13) {
                return;
            }
            this.mIgnorePeople.addItem(new RequestFaceItem(0, peopleFace));
        } else if (TextUtils.equals(peopleFace.groupId, peopleFace.serverId)) {
        } else {
            this.mMergePeople.addItem(new RequestFaceItem(0, peopleFace));
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromLocalBase
    public void handleRequestCloudItemList() {
        try {
            if (this.mCreatePeople.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start create group items");
                GallerySyncResult request = this.mCreatePeople.request(new CreatePeopleOperation(this.mContext));
                if (request != null && request.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mCreatePeople CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mRecoveryPeople.needRequest()) {
                GallerySyncResult request2 = this.mRecoveryPeople.request(new RecoveryPeopleOperation(this.mContext));
                if (request2 != null && request2.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mRecoveryPeople CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mMoveFace.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start move image items");
                GallerySyncResult request3 = this.mMoveFace.request(new MoveFaceOperation(this.mContext));
                if (request3 != null && request3.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mMoveFace CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mDeleteFace.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start delete image items");
                GallerySyncResult request4 = this.mDeleteFace.request(new DeleteFaceOperation(this.mContext));
                if (request4 != null && request4.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mDeleteFace CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mRenamePeople.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start rename group items");
                GallerySyncResult request5 = this.mRenamePeople.request(new RenamePeopleOperation(this.mContext));
                if (request5 != null && request5.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mRenamePeople CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mMergePeople.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start delete group items");
                GallerySyncResult request6 = this.mMergePeople.request(new MergePeopleOperation(this.mContext));
                if (request6 != null && request6.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mMergePeople CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (this.mUpdateInfoPeople.needRequest()) {
                SyncLogger.v("SyncFaceFromLocal", "start update info group items");
                GallerySyncResult request7 = this.mUpdateInfoPeople.request(new UpdatePeopleInfoOperation(this.mContext));
                if (request7 != null && request7.code == GallerySyncCode.CONDITION_INTERRUPTED) {
                    SyncLogger.e("SyncFaceFromLocal", "mUpdateInfoPeople CONDITION_INTERRUPTED");
                    return;
                }
            }
            if (!this.mIgnorePeople.needRequest()) {
                return;
            }
            SyncLogger.v("SyncFaceFromLocal", "start delete group items");
            GallerySyncResult request8 = this.mIgnorePeople.request(new IgnorePeopleOperation(this.mContext));
            if (request8 == null || request8.code != GallerySyncCode.CONDITION_INTERRUPTED) {
                return;
            }
            SyncLogger.e("SyncFaceFromLocal", "mIgnorePeople CONDITION_INTERRUPTED");
        } catch (Exception e) {
            SyncLogger.e("SyncFaceFromLocal", "error sync to server", e);
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    public class RequestItemGroup {
        public List<RequestFaceItem> mRequestItems = new ArrayList();

        public RequestItemGroup() {
        }

        public void addItem(RequestFaceItem requestFaceItem) {
            this.mRequestItems.add(requestFaceItem);
        }

        public boolean needRequest() {
            return this.mRequestItems.size() > 0;
        }

        public GallerySyncResult request(FaceRequestOperationBase faceRequestOperationBase) {
            int limitCountForOperation = faceRequestOperationBase.getLimitCountForOperation();
            if (limitCountForOperation != -1 && limitCountForOperation < this.mRequestItems.size()) {
                int i = 0;
                int size = this.mRequestItems.size();
                GallerySyncResult gallerySyncResult = null;
                while (i < size) {
                    int i2 = i + limitCountForOperation;
                    GallerySyncResult doRequest = doRequest(faceRequestOperationBase, this.mRequestItems.subList(i, i2 > size ? size : i2));
                    GallerySyncCode gallerySyncCode = doRequest.code;
                    if (gallerySyncCode == GallerySyncCode.CANCEL || gallerySyncCode == GallerySyncCode.NOT_CONTINUE_ERROR || gallerySyncCode == GallerySyncCode.CONDITION_INTERRUPTED) {
                        return doRequest;
                    }
                    gallerySyncResult = doRequest;
                    i = i2;
                }
                return gallerySyncResult;
            }
            return doRequest(faceRequestOperationBase, this.mRequestItems);
        }

        public final GallerySyncResult doRequest(RequestOperationBase requestOperationBase, List<RequestFaceItem> list) {
            try {
                return RetryOperation.doOperation(SyncFaceFromLocal.this.mContext, SyncFaceFromLocal.this.mAccount, SyncFaceFromLocal.this.mExtendedAuthToken, list, requestOperationBase);
            } catch (Exception e) {
                SyncLogger.e("SyncFaceFromLocal", "error when do request: ", e);
                e.printStackTrace();
                return new GallerySyncResult.Builder().setCode(GallerySyncCode.UNKNOWN).setException(e).build();
            }
        }
    }
}
