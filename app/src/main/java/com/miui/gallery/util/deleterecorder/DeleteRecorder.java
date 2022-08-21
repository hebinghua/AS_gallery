package com.miui.gallery.util.deleterecorder;

import android.text.TextUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.recorder.BaseRecorder;
import com.miui.gallery.util.recorder.RecordEntity;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class DeleteRecorder extends BaseRecorder {
    public static final DeleteRecorder mInstance = new DeleteRecorder();

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public long getExpireTime() {
        return 2592000000L;
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public int getMaxCount() {
        return 10000;
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public String getTag() {
        return "galleryAction_DeleteRecorder";
    }

    @Override // com.miui.gallery.util.recorder.IRecorder
    public boolean isObserveAccountChanged() {
        return false;
    }

    public static DeleteRecorder getInstance() {
        return mInstance;
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public Class<? extends RecordEntity> getEntityClass() {
        return DeleteRecord.class;
    }

    public void recordForDeleteAccount(String str) {
        record(new DeleteRecord(91, str, "galleryAction_DeleteRecorder"));
    }

    public void record(DeleteRecord deleteRecord) {
        record(deleteRecord, (BaseRecorder.RecordCallback) null);
    }

    public void record(DeleteRecord... deleteRecordArr) {
        record((BaseRecorder.RecordCallback) null, deleteRecordArr);
    }

    public void record(final DeleteRecord deleteRecord, final BaseRecorder.RecordCallback recordCallback) {
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.util.deleterecorder.DeleteRecorder.1
            @Override // java.lang.Runnable
            public void run() {
                boolean z = false;
                z = false;
                z = false;
                if (deleteRecord == null) {
                    DefaultLogger.w("galleryAction_DeleteRecorder", "illegal record");
                } else {
                    try {
                        if (-1 != GalleryEntityManager.getInstance().insert(deleteRecord)) {
                            z = true;
                        }
                    } catch (Exception e) {
                        DefaultLogger.e("galleryAction_DeleteRecorder", "insert delete record failed, [%s].", e);
                    }
                    DefaultLogger.d("galleryAction_DeleteRecorder", "Done inserting operation, result %s", Boolean.valueOf(z));
                }
                BaseRecorder.RecordCallback recordCallback2 = recordCallback;
                if (recordCallback2 != null) {
                    int i = z ? 1 : 0;
                    int i2 = z ? 1 : 0;
                    int i3 = z ? 1 : 0;
                    int i4 = z ? 1 : 0;
                    recordCallback2.onRecord(i);
                }
            }
        });
    }

    public void record(final BaseRecorder.RecordCallback recordCallback, DeleteRecord... deleteRecordArr) {
        final List asList = Arrays.asList(deleteRecordArr);
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.util.deleterecorder.DeleteRecorder.2
            @Override // java.lang.Runnable
            public void run() {
                int i = 0;
                if (!BaseMiscUtil.isValid(asList)) {
                    DefaultLogger.w("galleryAction_DeleteRecorder", "illegal operationRecords");
                } else {
                    DefaultLogger.d("galleryAction_DeleteRecorder", "Start to insert %s", TextUtils.join("\n", asList));
                    try {
                        i = GalleryEntityManager.getInstance().insert(asList);
                    } catch (Exception e) {
                        DefaultLogger.e("galleryAction_DeleteRecorder", "insert delete record failed, [%s].", e);
                    }
                    DefaultLogger.d("galleryAction_DeleteRecorder", "Done inserting %d operations, affected %d rows", Integer.valueOf(asList.size()), Integer.valueOf(i));
                }
                BaseRecorder.RecordCallback recordCallback2 = recordCallback;
                if (recordCallback2 != null) {
                    recordCallback2.onRecord(i);
                }
            }
        });
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder, com.miui.gallery.util.recorder.IRecorder
    public void onAddAccount(String str) {
        List<DeleteRecord> queryRecords = queryRecords("reason=?", new String[]{String.valueOf(91)});
        String filePath = BaseMiscUtil.isValid(queryRecords) ? queryRecords.get(0).getFilePath() : null;
        if (TextUtils.isEmpty(filePath)) {
            DefaultLogger.d("galleryAction_DeleteRecorder", "Old account is null, skip clean process");
        } else if (TextUtils.equals(filePath, str)) {
            DefaultLogger.d("galleryAction_DeleteRecorder", "New account is same as old account, skip clean process");
        } else if (!clearAllRecords()) {
            DefaultLogger.w("galleryAction_DeleteRecorder", "Fail to clear records after logged in with a different account");
        } else {
            DefaultLogger.d("galleryAction_DeleteRecorder", "Done clearing records after logged in with a different account");
        }
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder, com.miui.gallery.util.recorder.IRecorder
    public void onDeleteAccount(String str) {
        getInstance().recordForDeleteAccount(str);
        DefaultLogger.d("galleryAction_DeleteRecorder", "On record delete account operation, %s", str);
    }

    public List<DeleteRecord> queryRecords(String str, String[] strArr) {
        return GalleryEntityManager.getInstance().query(DeleteRecord.class, str, strArr);
    }
}
