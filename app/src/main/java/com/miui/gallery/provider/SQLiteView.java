package com.miui.gallery.provider;

import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SQLiteView.kt */
/* loaded from: classes2.dex */
public abstract class SQLiteView {
    public static final Companion Companion = new Companion(null);
    public final int latestVersion;
    public final String name;

    public static final SQLiteView of(String str) {
        return Companion.of(str);
    }

    public abstract Map<Integer, String> getStatements();

    public SQLiteView(String name, int i) {
        Intrinsics.checkNotNullParameter(name, "name");
        this.name = name;
        this.latestVersion = i;
    }

    public final String getName() {
        return this.name;
    }

    public final void createByVersion(SupportSQLiteDatabase database, int i) {
        Intrinsics.checkNotNullParameter(database, "database");
        DefaultLogger.i("SQLiteView", "Create view [" + this.name + "], pre version: " + GalleryPreferences.DataBase.getViewVersion(this.name) + ", curr version: " + i);
        database.beginTransaction();
        try {
            database.execSQL("DROP VIEW IF EXISTS " + getName() + ';');
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE VIEW ");
            sb.append(getName());
            sb.append(" AS ");
            String str = getStatements().get(Integer.valueOf(i));
            if (str == null) {
                throw new IllegalStateException(Intrinsics.stringPlus("Unrecognized version: ", Integer.valueOf(i)).toString());
            }
            sb.append(str);
            sb.append(';');
            database.execSQL(sb.toString());
            Unit unit = Unit.INSTANCE;
            database.setTransactionSuccessful();
            database.endTransaction();
            GalleryPreferences.DataBase.setViewVersion(this.name, i);
        } catch (Throwable th) {
            database.endTransaction();
            throw th;
        }
    }

    public final void createLatest(SupportSQLiteDatabase database, boolean z) {
        Intrinsics.checkNotNullParameter(database, "database");
        DefaultLogger.i("SQLiteView", "Create view [" + this.name + "] as latest, force: " + z);
        if (z || this.latestVersion != GalleryPreferences.DataBase.getViewVersion(this.name)) {
            createByVersion(database, this.latestVersion);
        }
    }

    /* compiled from: SQLiteView.kt */
    /* loaded from: classes2.dex */
    public static final class ExtendedCloud extends SQLiteView {
        public static final Companion Companion = new Companion(null);
        public final Map<Integer, String> statements;

        public ExtendedCloud() {
            super("extended_cloud", 5);
            this.statements = MapsKt__MapsKt.mapOf(TuplesKt.to(1, "SELECT cloud._id AS _id,cloud.sha1 AS sha1,microthumbfile,thumbnailFile,localFile,serverType,title,duration,description,location,size,localGroupId,mimeType,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,exifOrientation,secretKey,localFlag,mixedDateTime,dateTaken,exifImageWidth,exifImageLength,serverStatus,dateModified,creatorId,serverTag,serverId,fileName,groupId,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,babyInfoJson,isFavorite FROM cloud LEFT OUTER JOIN favorites ON cloud.sha1 = favorites.sha1"), TuplesKt.to(2, "SELECT cloud._id AS _id,cloud.sha1 AS sha1,microthumbfile,thumbnailFile,localFile,serverType,title,duration,description,location,size,localGroupId,mimeType,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,exifOrientation,secretKey,localFlag,mixedDateTime,dateTaken,exifImageWidth,exifImageLength,serverStatus,dateModified,creatorId,serverTag,serverId,fileName,groupId,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,babyInfoJson,isFavorite,specialTypeFlags FROM cloud LEFT OUTER JOIN favorites ON cloud.sha1 = favorites.sha1"), TuplesKt.to(3, "SELECT cloud._id AS _id,cloud.sha1 AS sha1,microthumbfile,thumbnailFile,localFile,serverType,title,duration,description,location,size,localGroupId,mimeType,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,exifOrientation,secretKey,localFlag,mixedDateTime,dateTaken,exifImageWidth,exifImageLength,serverStatus,dateModified,creatorId,serverTag,serverId,fileName,groupId,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,babyInfoJson,isFavorite,specialTypeFlags FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud LEFT OUTER JOIN favorites ON cloud.sha1 = favorites.sha1"), TuplesKt.to(4, "SELECT cloud._id AS _id,cloud.sha1 AS sha1,microthumbfile,thumbnailFile,localFile,serverType,title,duration,description,location,size,localGroupId,mimeType,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,exifOrientation,secretKey,localFlag,mixedDateTime,dateTaken,exifImageWidth,exifImageLength,serverStatus,dateModified,creatorId,serverTag,serverId,fileName,groupId,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,babyInfoJson,isFavorite,specialTypeFlags,sortBy FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud LEFT OUTER JOIN favorites ON cloud.sha1 = favorites.sha1"), TuplesKt.to(5, "SELECT cloud._id AS _id,cloud.sha1 AS sha1,microthumbfile,thumbnailFile,localFile,serverType,title,duration,description,location,size,localGroupId,mimeType,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,exifOrientation,secretKey,localFlag,mixedDateTime,dateTaken,exifImageWidth,exifImageLength,serverStatus,dateModified,creatorId,serverTag,serverId,fileName,groupId,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,babyInfoJson,isFavorite,specialTypeFlags,sortBy FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud LEFT OUTER JOIN favorites ON cloud._id = favorites.cloud_id"), TuplesKt.to(6, "SELECT   cloud._id AS _id,   cloud.sha1 AS sha1,   microthumbfile AS microthumbfile,   thumbnailFile AS thumbnailFile,   localFile AS localFile,   serverType AS serverType,   title AS title,   duration AS duration,   description AS description,   location AS location,   size AS size,   localGroupId AS localGroupId,   mimeType AS mimeType,   exifGPSLatitude AS exifGPSLatitude,   exifGPSLatitudeRef AS exifGPSLatitudeRef,   exifGPSLongitude AS exifGPSLongitude,   exifGPSLongitudeRef AS exifGPSLongitudeRef,   exifOrientation AS exifOrientation,   secretKey AS secretKey,   localFlag AS localFlag,   mixedDateTime AS mixedDateTime,   dateTaken AS dateTaken,   exifImageWidth AS exifImageWidth,   exifImageLength AS exifImageLength,   serverStatus AS serverStatus,   dateModified AS dateModified,   creatorId AS creatorId,   serverTag AS serverTag,   serverId AS serverId,   fileName AS fileName,   groupId AS groupId,   ubiSubImageCount AS ubiSubImageCount,   ubiSubIndex AS ubiSubIndex,   ubiFocusIndex AS ubiFocusIndex,   babyInfoJson AS babyInfoJson,   isFavorite AS isFavorite,   specialTypeFlags AS specialTypeFlags,   sortBy AS sortBy FROM   (    SELECT       *     FROM       cloud       LEFT JOIN (        SELECT           _id AS localGroupId,           json_extract(extra, '$.babyInfoJson') AS babyInfoJson         FROM           album      ) using(localGroupId)  ) cloud   LEFT OUTER JOIN favorites ON cloud._id = favorites.cloud_id"));
        }

        @Override // com.miui.gallery.provider.SQLiteView
        public Map<Integer, String> getStatements() {
            return this.statements;
        }

        /* compiled from: SQLiteView.kt */
        /* loaded from: classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }

    /* compiled from: SQLiteView.kt */
    /* loaded from: classes2.dex */
    public static final class ExtendedFaceImage extends SQLiteView {
        public static final Companion Companion = new Companion(null);
        public final Map<Integer, String> statements;

        public ExtendedFaceImage() {
            super("extended_faceImage", 5);
            this.statements = MapsKt__MapsKt.mapOf(TuplesKt.to(1, "SELECT peopleFace._id AS _id,peopleFace.serverId AS serverId,peopleFace.groupId AS groupId,peopleFace.localGroupId AS localGroupId,microthumbfile,thumbnailFile,dateTaken,mixedDateTime,mimeType,duration,location,size,exifImageWidth,exifImageLength,exifOrientation,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,serverType,localFile,specialTypeFlags,fileName,sha1,title,dateModified,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,secretKey,faceXScale,faceYScale,faceWScale,faceHScale,leftEyeXScale,leftEyeYScale,RightEyeXScale,RightEyeYScale,peopleFace.localFlag AS localFlag,peopleFace.serverStatus AS serverStatus,cloud._id AS photo_id,cloud.serverId AS photo_server_id,faceCoverScore FROM peopleFace JOIN faceToImages JOIN cloud ON peopleFace.serverId = faceId AND imageServerId = cloud.serverId WHERE type = 'FACE' AND (cloud.localFlag NOT IN (11, 0, -1, 2) OR (cloud.localFlag = 0 AND (cloud.serverStatus = 'custom' OR cloud.serverStatus = 'recovery'))) AND cloud.groupId != 1000 AND cloud.localGroupId != -1000 ORDER BY cloud._id DESC"), TuplesKt.to(2, "SELECT peopleFace._id AS _id,peopleFace.serverId AS serverId,peopleFace.groupId AS groupId,peopleFace.localGroupId AS localGroupId,microthumbfile,thumbnailFile,dateTaken,mixedDateTime,mimeType,duration,location,size,exifImageWidth,exifImageLength,exifOrientation,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,serverType,localFile,specialTypeFlags,fileName,sha1,title,dateModified,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,secretKey,faceXScale,faceYScale,faceWScale,faceHScale,leftEyeXScale,leftEyeYScale,RightEyeXScale,RightEyeYScale,peopleFace.localFlag AS localFlag,peopleFace.serverStatus AS serverStatus,cloud._id AS photo_id,cloud.serverId AS photo_server_id,faceCoverScore FROM peopleFace JOIN faceToImages JOIN cloud ON peopleFace.serverId = faceId AND imageServerId = cloud.serverId WHERE type = 'FACE' AND (cloud.localFlag NOT IN (11, 0, -1, 2, 15) OR (cloud.localFlag = 0 AND (cloud.serverStatus = 'custom' OR cloud.serverStatus = 'recovery'))) AND cloud.groupId != 1000 AND cloud.localGroupId != -1000 ORDER BY cloud._id DESC"), TuplesKt.to(3, "SELECT peopleFace._id AS _id,peopleFace.serverId AS serverId,peopleFace.groupId AS groupId,peopleFace.localGroupId AS localGroupId,microthumbfile,thumbnailFile,dateTaken,mixedDateTime,mimeType,duration,location,size,exifImageWidth,exifImageLength,exifOrientation,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,serverType,localFile,specialTypeFlags,fileName,sha1,title,dateModified,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,secretKey,faceXScale,faceYScale,faceWScale,faceHScale,leftEyeXScale,leftEyeYScale,RightEyeXScale,RightEyeYScale,peopleFace.localFlag AS localFlag,peopleFace.serverStatus AS serverStatus,cloud._id AS photo_id,cloud.serverId AS photo_server_id,faceCoverScore,selectCoverId FROM peopleFace JOIN faceToImages JOIN cloud ON peopleFace.serverId = faceId AND imageServerId = cloud.serverId WHERE type = 'FACE' AND (cloud.localFlag NOT IN (11, 0, -1, 2, 15) OR (cloud.localFlag = 0 AND (cloud.serverStatus = 'custom' OR cloud.serverStatus = 'recovery'))) AND cloud.groupId != 1000 AND cloud.localGroupId != -1000 ORDER BY cloud._id DESC"), TuplesKt.to(4, "SELECT peopleFace._id AS _id,peopleFace.serverId AS serverId,peopleFace.groupId AS groupId,peopleFace.localGroupId AS localGroupId,cloud._id AS cloud_id,microthumbfile,thumbnailFile,dateTaken,mixedDateTime,mimeType,duration,location,size,exifImageWidth,exifImageLength,exifOrientation,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,serverType,localFile,specialTypeFlags,fileName,sha1,title,dateModified,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,secretKey,faceXScale,faceYScale,faceWScale,faceHScale,leftEyeXScale,leftEyeYScale,RightEyeXScale,RightEyeYScale,peopleFace.localFlag AS localFlag,peopleFace.serverStatus AS serverStatus,cloud._id AS photo_id,cloud.serverId AS photo_server_id,faceCoverScore,selectCoverId FROM peopleFace JOIN faceToImages JOIN cloud ON peopleFace.serverId = faceId AND imageServerId = cloud.serverId WHERE type = 'FACE' AND (cloud.localFlag NOT IN (11, 0, -1, 2, 15) OR (cloud.localFlag = 0 AND (cloud.serverStatus = 'custom' OR cloud.serverStatus = 'recovery'))) AND cloud.groupId != 1000 AND cloud.localGroupId != -1000 ORDER BY cloud._id DESC"), TuplesKt.to(5, "SELECT peopleFace._id AS _id,peopleFace.serverId AS serverId,peopleFace.groupId AS groupId,peopleFace.localGroupId AS localGroupId,cloud._id AS cloud_id,microthumbfile,thumbnailFile,dateTaken,mixedDateTime,mimeType,duration,location,size,exifImageWidth,exifImageLength,exifOrientation,exifGPSLatitude,exifGPSLatitudeRef,exifGPSLongitude,exifGPSLongitudeRef,serverType,localFile,specialTypeFlags,fileName,sha1,title,dateModified,ubiSubImageCount,ubiSubIndex,ubiFocusIndex,secretKey,faceXScale,faceYScale,faceWScale,faceHScale,leftEyeXScale,leftEyeYScale,RightEyeXScale,RightEyeYScale,peopleFace.localFlag AS localFlag,peopleFace.serverStatus AS serverStatus,cloud._id AS photo_id,cloud.serverId AS photo_server_id,faceCoverScore,selectCoverId FROM peopleFace JOIN faceToImages JOIN cloud ON peopleFace.serverId = faceId AND imageServerId = cloud.serverId WHERE type = 'FACE' AND (cloud.localFlag NOT IN (11, 0, -1, 2, 15) OR (cloud.localFlag = 0 AND (cloud.serverStatus = 'custom' OR cloud.serverStatus = 'recovery'))) AND cloud.groupId != 1000 AND cloud.localGroupId != -1000 ORDER BY cloud._id DESC"));
        }

        @Override // com.miui.gallery.provider.SQLiteView
        public Map<Integer, String> getStatements() {
            return this.statements;
        }

        /* compiled from: SQLiteView.kt */
        /* loaded from: classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }

    /* compiled from: SQLiteView.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final SQLiteView of(String viewName) {
            Intrinsics.checkNotNullParameter(viewName, "viewName");
            if (Intrinsics.areEqual(viewName, "extended_cloud")) {
                return new ExtendedCloud();
            }
            if (!Intrinsics.areEqual(viewName, "extended_faceImage")) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unknown view: ", viewName));
            }
            return new ExtendedFaceImage();
        }
    }
}
