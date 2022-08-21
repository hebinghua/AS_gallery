package com.miui.gallery.vlog.sdk.interfaces;

import com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailPermanentEntity;
import java.util.List;

/* loaded from: classes2.dex */
public interface IHeaderTailManager {
    String getHeadTailLabel();

    void removeHeaderTail();

    void removePermanentHeaderTail();

    void setCustomHeaderTail(boolean z, String str);

    void setHeaderTail(boolean z, String str, int i, long j, String str2);

    void setHeaderTail(boolean z, String str, HeaderTailInfoEntity headerTailInfoEntity, String str2);

    void setHeaderTail(boolean z, String str, HeaderTailPermanentEntity headerTailPermanentEntity);

    void setHeaderTailText(String str, String str2);

    void setIRemoveHeadTail(IRemoveHeadTailCallback iRemoveHeadTailCallback);

    void setPermanentEntityList(List<HeaderTailPermanentEntity> list);
}
