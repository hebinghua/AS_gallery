package com.miui.gallery.cloud;

import com.miui.gallery.data.DBShareAlbum;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RequestSharerAlbumItem extends RequestItemBase {
    public final DBShareAlbum mDBItem;

    @Override // com.miui.gallery.cloud.RequestItemBase
    public int getRequestLimitAGroup() {
        return 1;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean isInSameAlbum(RequestItemBase requestItemBase) {
        return false;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean supportMultiRequest() {
        return false;
    }

    public RequestSharerAlbumItem(int i, DBShareAlbum dBShareAlbum) {
        super(i);
        this.mDBItem = dBShareAlbum;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public ArrayList<RequestItemBase> getItems() {
        ArrayList<RequestItemBase> arrayList = new ArrayList<>();
        arrayList.add(this);
        return arrayList;
    }
}
