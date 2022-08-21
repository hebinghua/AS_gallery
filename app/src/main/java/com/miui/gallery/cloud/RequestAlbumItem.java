package com.miui.gallery.cloud;

import com.miui.gallery.data.DBAlbum;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RequestAlbumItem extends RequestItemBase {
    public DBAlbum dbAlbum;

    @Override // com.miui.gallery.cloud.RequestItemBase
    public int getRequestLimitAGroup() {
        return 10;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean isInSameAlbum(RequestItemBase requestItemBase) {
        return true;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean supportMultiRequest() {
        return false;
    }

    public RequestAlbumItem(int i, DBAlbum dBAlbum) {
        this(i, dBAlbum, false);
    }

    public RequestAlbumItem(int i, DBAlbum dBAlbum, boolean z) {
        super(i, z ? 0L : RequestItemBase.getDelay(dBAlbum.getLocalFlag(), dBAlbum.getLocalPath()));
        this.dbAlbum = dBAlbum;
        init();
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public ArrayList<RequestItemBase> getItems() {
        ArrayList<RequestItemBase> arrayList = new ArrayList<>();
        arrayList.add(this);
        return arrayList;
    }
}
