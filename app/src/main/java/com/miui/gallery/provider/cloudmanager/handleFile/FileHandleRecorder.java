package com.miui.gallery.provider.cloudmanager.handleFile;

import ch.qos.logback.classic.Level;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.recorder.BaseRecorder;
import com.miui.gallery.util.recorder.RecordEntity;
import com.miui.gallery.util.thread.ThreadManager;

/* loaded from: classes2.dex */
public class FileHandleRecorder extends BaseRecorder implements FileHandleRecordHelper.IEntityManagerInvoker {
    public static volatile FileHandleRecorder mInstance;

    /* renamed from: $r8$lambda$hRpzhv1eBBmc2O_yb-Sc5CiLxwA */
    public static /* synthetic */ void m1229$r8$lambda$hRpzhv1eBBmc2O_ybSc5CiLxwA(int i, String str, String str2, String str3) {
        lambda$recordFileHandle$0(i, str, str2, str3);
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public long getExpireTime() {
        return 2592000000L;
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public int getMaxCount() {
        return Level.INFO_INT;
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public String getTag() {
        return "galleryAction_FileHandleRecorder";
    }

    @Override // com.miui.gallery.util.recorder.IRecorder
    public boolean isObserveAccountChanged() {
        return false;
    }

    public static FileHandleRecorder getInstance() {
        if (mInstance == null) {
            synchronized (FileHandleRecorder.class) {
                if (mInstance == null) {
                    mInstance = new FileHandleRecorder();
                }
            }
        }
        return mInstance;
    }

    public static void bindFileHandleRecordInvoker() {
        FileHandleRecordHelper.setInvoker(getInstance());
        DefaultLogger.d("galleryAction_FileHandleRecorder", "bindFileHandleRecordInvoker");
    }

    @Override // com.miui.gallery.util.recorder.BaseRecorder
    public Class<? extends RecordEntity> getEntityClass() {
        return FileHandleRecord.class;
    }

    @Override // com.miui.gallery.util.FileHandleRecordHelper.IEntityManagerInvoker
    public void recordFileHandle(final String str, final String str2, final int i, final String str3) {
        ThreadManager.execute(111, new Runnable() { // from class: com.miui.gallery.provider.cloudmanager.handleFile.FileHandleRecorder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FileHandleRecorder.m1229$r8$lambda$hRpzhv1eBBmc2O_ybSc5CiLxwA(i, str2, str, str3);
            }
        });
    }

    public static /* synthetic */ void lambda$recordFileHandle$0(int i, String str, String str2, String str3) {
        FileHandleRecord fileHandleRecord = new FileHandleRecord();
        fileHandleRecord.setHandleType(i);
        fileHandleRecord.setFromPath(str);
        fileHandleRecord.setFilePath(str2);
        fileHandleRecord.setTag(str3);
        if (GalleryEntityManager.getInstance().insertWithOnConflict(fileHandleRecord, 5)) {
            DefaultLogger.d("galleryAction_FileHandleRecorder", "record %s file [%s], invokerTag = [%s]", i != 1 ? i != 2 ? "delete" : "copy" : "move", str2, str3);
        }
    }
}
