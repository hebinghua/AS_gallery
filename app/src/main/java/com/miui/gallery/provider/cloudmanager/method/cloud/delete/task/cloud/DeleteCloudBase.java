package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchTaskById2;
import com.miui.gallery.util.MediaStoreUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class DeleteCloudBase extends BatchTaskById2 {
    public static final String[] PROJECTION = {"thumbnailFile", "localFile"};
    public static final SafeDBUtil.QueryHandler<List<String>> QUERY_HANDLER = DeleteCloudBase$$ExternalSyntheticLambda1.INSTANCE;
    public int mDeleteReason;

    /* renamed from: $r8$lambda$-uCq1uTG7_fqnVoM-70RpR7Cofg */
    public static /* synthetic */ Object m1235$r8$lambda$uCq1uTG7_fqnVoM70RpR7Cofg(DeleteCloudBase deleteCloudBase, Collection collection, ThreadPool.JobContext jobContext) {
        return deleteCloudBase.lambda$doMarkAsDirty$1(collection, jobContext);
    }

    public abstract List<String> getFilePathsById(Context context, Collection<Long> collection);

    public abstract String getTableName();

    public static /* synthetic */ List lambda$static$0(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            return Collections.emptyList();
        }
        LinkedList linkedList = new LinkedList();
        while (cursor.moveToNext()) {
            String string = cursor.getString(1);
            if (!TextUtils.isEmpty(string)) {
                linkedList.add(string);
            } else {
                String string2 = cursor.getString(0);
                if (!TextUtils.isEmpty(string2)) {
                    linkedList.add(string2);
                }
            }
        }
        return linkedList;
    }

    public DeleteCloudBase(Context context, ArrayList<Long> arrayList, long[] jArr, int i) {
        super(context, arrayList, jArr);
        this.mDeleteReason = i;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public Cursor queryCursor(SupportSQLiteDatabase supportSQLiteDatabase, Long[] lArr) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(getTableName()).columns(Contracts.PROJECTION).selection(String.format("%s IN (%s)", j.c, TextUtils.join(", ", lArr)), null).create());
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask.BatchCursorTask2
    public void doMarkAsDirty(final Collection<Long> collection) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteCloudBase$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return DeleteCloudBase.m1235$r8$lambda$uCq1uTG7_fqnVoM70RpR7Cofg(DeleteCloudBase.this, collection, jobContext);
            }
        });
    }

    public /* synthetic */ Object lambda$doMarkAsDirty$1(Collection collection, ThreadPool.JobContext jobContext) {
        MediaStoreUtils.makeInvalid(GalleryApp.sGetAndroidContext(), getFilePathsById(GalleryApp.sGetAndroidContext(), collection));
        return null;
    }
}
