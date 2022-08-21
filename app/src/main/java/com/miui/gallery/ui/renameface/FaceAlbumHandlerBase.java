package com.miui.gallery.ui.renameface;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.activity.facebaby.InputFaceNameActivity;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;

/* loaded from: classes2.dex */
public abstract class FaceAlbumHandlerBase {
    public FragmentActivity mActivity;
    public NormalPeopleFaceMediaSet mFaceSet;
    public FaceAlbumHandlerListener mListener;

    /* loaded from: classes2.dex */
    public interface FaceAlbumHandlerListener {
        void onGetFolderItem(FaceFolderItem faceFolderItem);
    }

    /* loaded from: classes2.dex */
    public interface FaceFolderItem {
        String getId();

        String getName();
    }

    /* loaded from: classes2.dex */
    public static class FaceNewFolerItem implements FaceFolderItem {
        public boolean isCreatedInDb = false;
        public String mContactJson;
        public String mId;
        public String mName;

        public FaceNewFolerItem(String str) {
            this.mName = str;
        }

        @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceFolderItem
        public String getName() {
            return this.mName;
        }

        @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceFolderItem
        public String getId() {
            return this.mId;
        }

        public void setId(String str) {
            this.mId = str;
        }

        public void setContactjson(String str) {
            this.mContactJson = str;
        }

        public String getContactjson() {
            return this.mContactJson;
        }

        public boolean isCreatedInDb() {
            return this.isCreatedInDb;
        }

        public void setCreatedInDb() {
            this.isCreatedInDb = true;
        }
    }

    /* loaded from: classes2.dex */
    public static class FaceFolderItemImpl implements FaceFolderItem {
        public String mLocalGroupId;
        public String mName;

        public FaceFolderItemImpl(FaceDisplayFolderItem faceDisplayFolderItem) {
            this.mName = faceDisplayFolderItem.name;
            this.mLocalGroupId = faceDisplayFolderItem.localGroupId;
        }

        public FaceFolderItemImpl(String str, String str2) {
            this.mName = str;
            this.mLocalGroupId = str2;
        }

        @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceFolderItem
        public String getName() {
            return this.mName;
        }

        @Override // com.miui.gallery.ui.renameface.FaceAlbumHandlerBase.FaceFolderItem
        public String getId() {
            return this.mLocalGroupId;
        }
    }

    public FaceAlbumHandlerBase(FragmentActivity fragmentActivity, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, FaceAlbumHandlerListener faceAlbumHandlerListener) {
        this.mActivity = fragmentActivity;
        this.mFaceSet = normalPeopleFaceMediaSet;
        this.mListener = faceAlbumHandlerListener;
    }

    public void showInputFolderNameDialog(int i, String str, boolean z) {
        Intent intent = new Intent(this.mActivity, InputFaceNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("original_name", str);
        bundle.putBoolean("is_relation_setted", z);
        NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = this.mFaceSet;
        if (normalPeopleFaceMediaSet != null && normalPeopleFaceMediaSet.hasName()) {
            bundle.putString("original_path_album_local_id", String.valueOf(this.mFaceSet.getBucketId()));
        }
        if (i == 19) {
            bundle.putBoolean("only_use_contact", true);
        }
        intent.putExtras(bundle);
        this.mActivity.startActivityForResult(intent, i);
    }
}
