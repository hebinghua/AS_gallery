package com.miui.gallery.assistant.model;

import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.util.GsonUtils;
import com.xiaomi.player.videoAnalytic;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FaceClusterInfo extends Entity {
    public videoAnalytic.FaceCluster.FaceSubClusterNode[] mClusterCenter;
    public long mClusterId;
    public int mVersion = 0;

    public FaceClusterInfo() {
    }

    public FaceClusterInfo(long j, videoAnalytic.FaceCluster.FaceSubClusterNode[] faceSubClusterNodeArr) {
        this.mClusterId = j;
        this.mClusterCenter = faceSubClusterNodeArr;
    }

    public long getClusterId() {
        return this.mClusterId;
    }

    public videoAnalytic.FaceCluster.FaceSubClusterNode[] getClusterCenter() {
        return this.mClusterCenter;
    }

    public void setClusterCenter(videoAnalytic.FaceCluster.FaceSubClusterNode[] faceSubClusterNodeArr) {
        this.mClusterCenter = faceSubClusterNodeArr;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "clusterId", "INTEGER");
        Entity.addColumn(arrayList, "clusterCenter", "TEXT");
        Entity.addColumn(arrayList, "version", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mClusterId = Entity.getLong(cursor, "clusterId");
        this.mClusterCenter = (videoAnalytic.FaceCluster.FaceSubClusterNode[]) GsonUtils.fromJson(Entity.getStringDefault(cursor, "clusterCenter", ""), (Class<Object>) videoAnalytic.FaceCluster.FaceSubClusterNode[].class);
        this.mVersion = Entity.getInt(cursor, "version");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("clusterId", Long.valueOf(this.mClusterId));
        contentValues.put("clusterCenter", GsonUtils.toString(this.mClusterCenter));
        contentValues.put("version", Integer.valueOf(this.mVersion));
    }
}
