package com.miui.gallery.ui.renameface;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.ui.renameface.FaceAlbumHandlerBase;
import com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class RemoveFromFaceAlbumHandler extends FaceAlbumHandlerBase {
    public RemoveFromFaceAlbumHandler(FragmentActivity fragmentActivity, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, FaceAlbumHandlerBase.FaceAlbumHandlerListener faceAlbumHandlerListener) {
        super(fragmentActivity, normalPeopleFaceMediaSet, faceAlbumHandlerListener);
    }

    public void finishWhenGetContact(PeopleContactInfo peopleContactInfo) {
        if (peopleContactInfo == null || TextUtils.isEmpty(peopleContactInfo.name) || this.mListener == null) {
            return;
        }
        FaceAlbumHandlerBase.FaceNewFolerItem faceNewFolerItem = new FaceAlbumHandlerBase.FaceNewFolerItem(peopleContactInfo.name);
        faceNewFolerItem.setContactjson(peopleContactInfo.formatContactJson());
        this.mListener.onGetFolderItem(faceNewFolerItem);
    }

    public void show() {
        new AlertDialog.Builder(this.mActivity).setItems(new String[]{this.mActivity.getString(R.string.remove_from_current_album), this.mActivity.getString(R.string.remove_to_other_album)}, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.renameface.RemoveFromFaceAlbumHandler.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (i == 0) {
                    FaceAlbumHandlerBase.FaceAlbumHandlerListener faceAlbumHandlerListener = RemoveFromFaceAlbumHandler.this.mListener;
                    if (faceAlbumHandlerListener == null) {
                        return;
                    }
                    faceAlbumHandlerListener.onGetFolderItem(null);
                } else if (i == 1) {
                    RemoveFromFaceAlbumHandler.this.showRemoveDialog();
                } else {
                    throw new IllegalStateException("unknown item clicked: " + i);
                }
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create().show();
    }

    public void showRemoveDialog() {
        final PeopleFaceMergeDialogFragment peopleFaceMergeDialogFragment = new PeopleFaceMergeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("people_face_Merge_title", this.mActivity.getString(R.string.remove_to_album_title));
        bundle.putParcelable("merge_action_from_album", this.mFaceSet);
        peopleFaceMergeDialogFragment.setArguments(bundle);
        peopleFaceMergeDialogFragment.showAllowingStateLoss(this.mActivity.getSupportFragmentManager(), "PeopleFaceMergeDialogFragment");
        peopleFaceMergeDialogFragment.setPeopleSelectListener(new PeopleFaceMergeDialogFragment.PeopleSelectListener() { // from class: com.miui.gallery.ui.renameface.RemoveFromFaceAlbumHandler.2
            @Override // com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment.PeopleSelectListener
            public void onPeopleSelect(FaceDisplayFolderItem faceDisplayFolderItem, boolean z, int i) {
                if (i == 0) {
                    RemoveFromFaceAlbumHandler.this.showInputFolderNameDialog(17, null, false);
                } else {
                    FaceAlbumHandlerBase.FaceAlbumHandlerListener faceAlbumHandlerListener = RemoveFromFaceAlbumHandler.this.mListener;
                    if (faceAlbumHandlerListener != null) {
                        faceAlbumHandlerListener.onGetFolderItem(new FaceAlbumHandlerBase.FaceFolderItemImpl(faceDisplayFolderItem));
                    }
                }
                peopleFaceMergeDialogFragment.dismiss();
            }
        });
    }
}
