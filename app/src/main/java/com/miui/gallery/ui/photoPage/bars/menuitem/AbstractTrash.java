package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.TrashDataItem;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class AbstractTrash extends BaseMenuItemDelegate {
    public static /* synthetic */ Void $r8$lambda$TnNHbAJfitFK8HnoACTb3P81yNo(BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback, Void[] voidArr) {
        return simpleCallback.duringAction();
    }

    /* renamed from: $r8$lambda$kNY2hj4-j3zv1BqQslfRQBvs-QE */
    public static /* synthetic */ void m1623$r8$lambda$kNY2hj4j3zv1BqQslfRQBvsQE(AbstractTrash abstractTrash, Void r1) {
        abstractTrash.lambda$executeTask$1(r1);
    }

    public abstract String getInvokerTag();

    public AbstractTrash(IMenuItem iMenuItem) {
        super(iMenuItem);
    }

    public void executeTask(final BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback) {
        if (!this.isFunctionInit) {
            return;
        }
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.AbstractTrash$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public final Object doProcess(Object[] objArr) {
                return AbstractTrash.$r8$lambda$TnNHbAJfitFK8HnoACTb3P81yNo(BasePhotoPageBarsDelegateFragment.SimpleCallback.this, (Void[]) objArr);
            }
        }, new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.AbstractTrash$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public final void onCompleteProcess(Object obj) {
                AbstractTrash.m1623$r8$lambda$kNY2hj4j3zv1BqQslfRQBvsQE(AbstractTrash.this, (Void) obj);
            }
        });
        GalleryActivity galleryActivity = this.mContext;
        processTask.showProgress(galleryActivity, galleryActivity.getString(R.string.purge_or_recovery_processing));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public /* synthetic */ void lambda$executeTask$1(Void r3) {
        int position = this.mDataProvider.getFieldData().mCurrent.getPosition();
        int i = position + 1;
        if (this.mOwner.getAdapter() != null && position == this.mOwner.getAdapter().getCount() - 1) {
            i = position - 1;
        }
        if (this.mOwner.isNeedConfirmPassWord(i)) {
            this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(this.mFragment, false);
        }
        this.mDataProvider.onContentChanged();
    }

    public ArrayList<TrashBinItem> getPurgeOrRecoveryList() {
        BaseDataItem dataItem = this.mDataProvider.getFieldData().mCurrent.getDataItem();
        TrashDataItem trashDataItem = dataItem instanceof TrashDataItem ? (TrashDataItem) dataItem : null;
        if (trashDataItem == null) {
            return null;
        }
        ArrayList<TrashBinItem> arrayList = new ArrayList<>();
        TrashBinItem trashBinItem = new TrashBinItem(trashDataItem.getFileName(), trashDataItem.getCloudId(), trashDataItem.getCloudServerId(), trashDataItem.getSha1(), trashDataItem.getLocalGroupId(), trashDataItem.getAlbumName(), trashDataItem.getAlbumServerId(), trashDataItem.getAlbumPath(), 0L, trashDataItem.getSize());
        trashBinItem.setRowId(trashDataItem.getKey());
        trashBinItem.setMimeType(trashDataItem.getMimeType());
        if (TextUtils.isEmpty(trashDataItem.getOriginalPath())) {
            trashBinItem.setTrashFilePath(trashDataItem.getThumnailPath());
        } else {
            trashBinItem.setTrashFilePath(trashDataItem.getOriginalPath());
        }
        trashBinItem.setServerTag(trashDataItem.getServerTag());
        trashBinItem.setIsOrigin(trashDataItem.getIsOrigin());
        trashBinItem.setSize(trashDataItem.getSize());
        trashBinItem.setMixedDateTime(trashDataItem.getCreateTime());
        trashBinItem.setInvokerTag(getInvokerTag());
        arrayList.add(trashBinItem);
        return arrayList;
    }
}
