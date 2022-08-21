package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.net.Uri;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.data.LocationGenerator;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.model.dto.utils.Insertable;
import com.miui.gallery.provider.GalleryDBHelper;

/* loaded from: classes2.dex */
public class GalleryDBUpdater73 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(final SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("location").setType("TEXT").build());
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("attributes").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        }
        if (!updateResult.isRecreateTableShareImage()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareImage", new TableColumn.Builder().setName("location").setType("TEXT").build());
        }
        if (!updateResult.isRecreateTableShareAlbum()) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", new TableColumn.Builder().setName("attributes").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        }
        LocationGenerator.getInstance().generate(GalleryApp.sGetAndroidContext());
        AlbumDataHelper.addRecordsForCameraAndScreenshots(new Insertable() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater73.1
            @Override // com.miui.gallery.model.dto.utils.Insertable
            public long insert(Uri uri, String str, String str2, ContentValues contentValues) {
                return supportSQLiteDatabase.insert(str, 0, contentValues);
            }
        });
        GalleryDBHelper.refillLocalGroupId(supportSQLiteDatabase, updateResult.isRecreateTableCloud(), updateResult.isRecreateTableShareImage(), updateResult.isRecreateTableShareAlbum());
        return UpdateResult.defaultResult();
    }
}
