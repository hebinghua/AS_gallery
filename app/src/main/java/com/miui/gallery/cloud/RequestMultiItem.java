package com.miui.gallery.cloud;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class RequestMultiItem extends RequestItemBase {
    public ArrayList<RequestItemBase> multiRequestItems;

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

    public RequestMultiItem(ArrayList<RequestItemBase> arrayList, int i) {
        super(i);
        this.multiRequestItems = arrayList;
        init();
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public ArrayList<RequestItemBase> getItems() {
        ArrayList<RequestItemBase> arrayList = new ArrayList<>();
        arrayList.addAll(this.multiRequestItems);
        return arrayList;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean compareAndSetStatus(int i, int i2) {
        boolean compareAndSetStatus = super.compareAndSetStatus(i, i2);
        Iterator<RequestItemBase> it = this.multiRequestItems.iterator();
        while (it.hasNext()) {
            it.next().compareAndSetStatus(i, i2);
        }
        return compareAndSetStatus;
    }
}
