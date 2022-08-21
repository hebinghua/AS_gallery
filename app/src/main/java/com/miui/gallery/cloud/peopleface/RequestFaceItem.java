package com.miui.gallery.cloud.peopleface;

import android.text.TextUtils;
import com.miui.gallery.cloud.RequestItemBase;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class RequestFaceItem extends RequestItemBase {
    public PeopleFace face;

    @Override // com.miui.gallery.cloud.RequestItemBase
    public int getRequestLimitAGroup() {
        return 50;
    }

    public RequestFaceItem(int i, PeopleFace peopleFace) {
        super(i, 0L);
        this.face = peopleFace;
        init();
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public ArrayList<RequestItemBase> getItems() {
        ArrayList<RequestItemBase> arrayList = new ArrayList<>();
        arrayList.add(this);
        return arrayList;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean isInSameAlbum(RequestItemBase requestItemBase) {
        PeopleFace peopleFace = ((RequestFaceItem) requestItemBase).face;
        int i = peopleFace.localFlag;
        PeopleFace peopleFace2 = this.face;
        if (i == peopleFace2.localFlag) {
            if (i == 5) {
                return TextUtils.equals(peopleFace.localGroupId, peopleFace2.localGroupId);
            }
            if (i != 2) {
                return true;
            }
            return TextUtils.equals(peopleFace.groupId, peopleFace2.groupId);
        }
        return true;
    }

    @Override // com.miui.gallery.cloud.RequestItemBase
    public boolean supportMultiRequest() {
        return this.face.localFlag == 5;
    }
}
