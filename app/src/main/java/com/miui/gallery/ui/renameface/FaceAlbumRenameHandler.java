package com.miui.gallery.ui.renameface;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.DisplayFolderItem;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.ui.renameface.FaceAlbumHandlerBase;
import com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class FaceAlbumRenameHandler extends FaceAlbumHandlerBase {
    public ConfirmListener mConfirmListener;
    public List<String> mFacePathsToBeMoved;
    public ArrayList<NormalPeopleFaceMediaSet> mFaceSets;
    public boolean mIsFaceSetsMergeOperation;
    public boolean mIsFacesMoveOperation;
    public boolean mIsRelationSetted;

    /* loaded from: classes2.dex */
    public interface ConfirmListener {
        void onConfirm(String str, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface FaceOperationTask {
        void run();
    }

    public final void moveFacesTo(String str, FaceAlbumHandlerBase.FaceFolderItem faceFolderItem) {
    }

    public FaceAlbumRenameHandler(FragmentActivity fragmentActivity, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, ConfirmListener confirmListener) {
        this(fragmentActivity, normalPeopleFaceMediaSet, confirmListener, false);
    }

    public FaceAlbumRenameHandler(FragmentActivity fragmentActivity, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, ConfirmListener confirmListener, boolean z) {
        super(fragmentActivity, normalPeopleFaceMediaSet, null);
        this.mIsFaceSetsMergeOperation = false;
        this.mConfirmListener = confirmListener;
        this.mIsRelationSetted = z;
    }

    public FaceAlbumRenameHandler(FragmentActivity fragmentActivity, ArrayList<NormalPeopleFaceMediaSet> arrayList, ConfirmListener confirmListener) {
        super(fragmentActivity, null, null);
        this.mIsFaceSetsMergeOperation = false;
        this.mIsFaceSetsMergeOperation = true;
        this.mFaceSets = arrayList;
        this.mConfirmListener = confirmListener;
        if (arrayList == null || arrayList.size() != 1 || !this.mFaceSets.get(0).hasName()) {
            return;
        }
        this.mFaceSet = this.mFaceSets.get(0);
    }

    public void finishWhenGetContact(PeopleContactInfo peopleContactInfo) {
        if (peopleContactInfo == null || TextUtils.isEmpty(peopleContactInfo.name)) {
            return;
        }
        String checkFileNameValid = CreateGroupItem.checkFileNameValid(this.mActivity, peopleContactInfo.name);
        if (!TextUtils.isEmpty(checkFileNameValid)) {
            ToastUtils.makeText(this.mActivity, checkFileNameValid);
        } else {
            onClick(peopleContactInfo);
        }
    }

    public final void showInputFolderNameDialog(boolean z, boolean z2) {
        int i = z ? 19 : 16;
        NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = this.mFaceSet;
        showInputFolderNameDialog(i, normalPeopleFaceMediaSet != null ? normalPeopleFaceMediaSet.getName() : "", z2);
    }

    public final void showInputFolderNameDialog(boolean z) {
        showInputFolderNameDialog(z, false);
    }

    public void show() {
        if (!this.mIsFaceSetsMergeOperation) {
            showInputFolderNameDialog(false, this.mIsRelationSetted);
            TrackController.trackClick("403.47.1.1.11253", AutoTracking.getRef());
            return;
        }
        final PeopleFaceMergeDialogFragment peopleFaceMergeDialogFragment = new PeopleFaceMergeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("merge_action_from_album", this.mFaceSet);
        bundle.putString("people_face_Merge_title", this.mActivity.getString(R.string.merge_album_title));
        peopleFaceMergeDialogFragment.setArguments(bundle);
        peopleFaceMergeDialogFragment.showAllowingStateLoss(this.mActivity.getSupportFragmentManager(), "PeopleFaceMergeDialogFragment");
        peopleFaceMergeDialogFragment.setPeopleSelectListener(new PeopleFaceMergeDialogFragment.PeopleSelectListener() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.1
            @Override // com.miui.gallery.ui.renameface.PeopleFaceMergeDialogFragment.PeopleSelectListener
            public void onPeopleSelect(FaceDisplayFolderItem faceDisplayFolderItem, boolean z, int i) {
                if (i == 0) {
                    FaceAlbumRenameHandler.this.showInputFolderNameDialog(true);
                } else {
                    FaceAlbumRenameHandler.this.onClick(faceDisplayFolderItem.name, z);
                }
                peopleFaceMergeDialogFragment.dismiss();
            }
        });
    }

    public static DisplayFolderItem getDisplayFolderItem(ArrayList<DisplayFolderItem> arrayList, String str) {
        if (arrayList == null) {
            return null;
        }
        Iterator<DisplayFolderItem> it = arrayList.iterator();
        while (it.hasNext()) {
            DisplayFolderItem next = it.next();
            if (next.name.equalsIgnoreCase(str)) {
                return next;
            }
        }
        return null;
    }

    public final void onClick(final PeopleContactInfo peopleContactInfo) {
        FaceOperationTask faceOperationTask;
        ArrayList<NormalPeopleFaceMediaSet> arrayList;
        final FaceAlbumHandlerBase.FaceFolderItem faceNewFolerItem;
        String str = peopleContactInfo.name;
        boolean z = peopleContactInfo.isRepeatName;
        String str2 = peopleContactInfo.localGroupId;
        final String trim = str.trim();
        int i = 1;
        if (this.mIsFacesMoveOperation) {
            i = this.mFacePathsToBeMoved.size();
            if (z) {
                faceNewFolerItem = new FaceAlbumHandlerBase.FaceFolderItemImpl(str, str2);
            } else {
                faceNewFolerItem = new FaceAlbumHandlerBase.FaceNewFolerItem(trim);
            }
            faceOperationTask = new FaceOperationTask() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.2
                @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.FaceOperationTask
                public void run() {
                    FaceAlbumRenameHandler.this.moveFacesTo(trim, faceNewFolerItem);
                }
            };
        } else if (!this.mIsFaceSetsMergeOperation || ((arrayList = this.mFaceSets) != null && arrayList.size() == 1 && !z)) {
            faceOperationTask = new FaceOperationTask() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.3
                @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.FaceOperationTask
                public void run() {
                    FaceAlbumRenameHandler.this.rename(trim, peopleContactInfo);
                }
            };
        } else {
            i = this.mFaceSets.size();
            faceOperationTask = new FaceOperationTask() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.4
                @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.FaceOperationTask
                public void run() {
                    FaceAlbumRenameHandler.this.mergeLotsPeopleTo(trim);
                }
            };
        }
        if (z) {
            dialogToast(faceOperationTask, getMessage(i, trim));
        } else {
            faceOperationTask.run();
        }
    }

    public final void onClick(String str, boolean z) {
        final String trim = str.trim();
        int size = this.mFaceSets.size();
        FaceOperationTask faceOperationTask = new FaceOperationTask() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.5
            @Override // com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.FaceOperationTask
            public void run() {
                FaceAlbumRenameHandler.this.mergeLotsPeopleTo(trim);
            }
        };
        if (z) {
            dialogToast(faceOperationTask, getMessage(size, trim));
        } else {
            faceOperationTask.run();
        }
    }

    public final CharSequence getMessage(int i, String str) {
        return i > 1 ? Html.fromHtml(this.mActivity.getString(R.string.confirm_merge_many_face_albums, new Object[]{str})) : Html.fromHtml(this.mActivity.getString(R.string.confirm_merge_face_albums, new Object[]{str}));
    }

    public final void dialogToast(final FaceOperationTask faceOperationTask, CharSequence charSequence) {
        new AlertDialog.Builder(this.mActivity).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.6
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                faceOperationTask.run();
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setMessage(charSequence).show();
    }

    public final void rename(final String str, final PeopleContactInfo peopleContactInfo) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.7
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                FaceAlbumRenameHandler faceAlbumRenameHandler = FaceAlbumRenameHandler.this;
                NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = faceAlbumRenameHandler.mFaceSet;
                boolean z = false;
                if (normalPeopleFaceMediaSet == null) {
                    normalPeopleFaceMediaSet = (NormalPeopleFaceMediaSet) faceAlbumRenameHandler.mFaceSets.get(0);
                }
                if (normalPeopleFaceMediaSet != null) {
                    z = normalPeopleFaceMediaSet.rename(FaceAlbumRenameHandler.this.mActivity, str, peopleContactInfo);
                }
                if (FaceAlbumRenameHandler.this.mConfirmListener != null) {
                    FaceAlbumRenameHandler.this.mConfirmListener.onConfirm(str, z);
                    return null;
                }
                return null;
            }
        });
    }

    public final void mergeLotsPeopleTo(final String str) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.renameface.FaceAlbumRenameHandler.8
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                FaceAlbumRenameHandler faceAlbumRenameHandler = FaceAlbumRenameHandler.this;
                NormalPeopleFaceMediaSet.merge(faceAlbumRenameHandler.mActivity, faceAlbumRenameHandler.mFaceSets, str);
                return null;
            }
        });
        this.mConfirmListener.onConfirm(str, true);
    }
}
