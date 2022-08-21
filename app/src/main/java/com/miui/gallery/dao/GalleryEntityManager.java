package com.miui.gallery.dao;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.model.FaceClusterInfo;
import com.miui.gallery.assistant.model.FaceInfo;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.model.PeopleEvent;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.SyncTag;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.dao.base.EntityManager;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.RecommendWidgetDBEntity;
import com.miui.gallery.model.PersistentResponse;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.pendingtask.base.PendingTaskInfo;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleRecord;
import com.miui.gallery.provider.cloudmanager.remark.info.MediaRemarkEntity;
import com.miui.gallery.provider.peoplecover.PeopleCover;
import com.miui.gallery.scanner.core.model.UnhandledScanTaskRecord;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.trash.TrashSyncTag;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class GalleryEntityManager extends EntityManager {
    public static GalleryEntityManager instance;

    @Override // com.miui.gallery.dao.base.EntityManager
    public int getTablesCount() {
        return 18;
    }

    public GalleryEntityManager() {
        super(GalleryApp.sGetAndroidContext(), "gallery_sub.db", 28);
    }

    public static synchronized GalleryEntityManager getInstance() {
        GalleryEntityManager galleryEntityManager;
        synchronized (GalleryEntityManager.class) {
            if (instance == null) {
                instance = new GalleryEntityManager();
            }
            galleryEntityManager = instance;
        }
        return galleryEntityManager;
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onInitTableList() {
        addTable(CustomWidgetDBEntity.class);
        addTable(RecommendWidgetDBEntity.class);
        addTable(Card.class);
        addTable(PendingTaskInfo.class);
        addTable(PersistentResponse.class);
        addTable(MediaRemarkEntity.class);
        addTable(PeopleCover.class);
        addTable(DeleteRecord.class);
        addTable(Record.class);
        addTable(Library.class);
        addTable(SyncTag.class);
        addTable(TrashBinItem.class);
        addTable(TrashSyncTag.class);
        addTable(FaceInfo.class);
        addTable(FaceClusterInfo.class);
        addTable(PeopleEvent.class);
        addTable(MediaFeature.class);
        addTable(MediaScene.class);
        addTable(UnhandledScanTaskRecord.class);
        addTable(FileHandleRecord.class);
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onDatabaseUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        DefaultLogger.i("GalleryEntityManager", "onDatabaseUpgrade: from %d to %d", Integer.valueOf(i), Integer.valueOf(i2));
        if (i == 7) {
            supportSQLiteDatabase.execSQL(" UPDATE " + Card.class.getSimpleName() + " SET localFlag = 0, updateTime = createTime, createdBy = 0");
        }
        if (i2 >= 12) {
            if (i == 10) {
                DefaultLogger.i("GalleryEntityManager", "drop table %s", Album.class.getSimpleName());
                supportSQLiteDatabase.execSQL(EntityManager.getDropTableSql(Album.class.getSimpleName()));
            } else if (i == 11) {
                DefaultLogger.i("GalleryEntityManager", "drop table %s & %s", PeopleItem.class.getSimpleName(), Album.class.getSimpleName());
                supportSQLiteDatabase.execSQL(EntityManager.getDropTableSql(PeopleItem.class.getSimpleName()));
                supportSQLiteDatabase.execSQL(EntityManager.getDropTableSql(Album.class.getSimpleName()));
            }
        }
        if (i2 >= 15 && i >= 7 && i < 15) {
            DefaultLogger.i("GalleryEntityManager", "drop table %s ", "ImageFeature");
            supportSQLiteDatabase.execSQL(EntityManager.getDropTableSql("ImageFeature"));
        }
        if (i == 15) {
            supportSQLiteDatabase.execSQL(" UPDATE " + MediaScene.class.getSimpleName() + " SET isQuickResult = 0");
        }
        if (i >= 13 && i <= 16) {
            supportSQLiteDatabase.execSQL(" UPDATE " + TrashBinItem.class.getSimpleName() + " SET imageSize = -1");
        }
        if (i >= 13 && i <= 18) {
            supportSQLiteDatabase.execSQL(" UPDATE " + TrashBinItem.class.getSimpleName() + " SET status = 0");
        }
        if (i >= 15 && i <= 22) {
            supportSQLiteDatabase.execSQL(" UPDATE " + MediaScene.class.getSimpleName() + " SET leftTopX = -1.0, leftTopY = -1.0, rightBottomX = -1.0, rightBottomY = -1.0, point_position = ''");
        }
        if (i == 22) {
            supportSQLiteDatabase.execSQL(" UPDATE " + TrashBinItem.class.getSimpleName() + " SET microFilePath =  NULL");
        }
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onDatabaseDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        DefaultLogger.w("GalleryEntityManager", "onDatabaseDowngrade from %s to %s", Integer.valueOf(i), Integer.valueOf(i2));
    }
}
