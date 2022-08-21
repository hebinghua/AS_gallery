package com.nexstreaming.app.common.nexasset.assetpackage;

import java.util.Map;

/* compiled from: ItemInfo.java */
/* loaded from: classes3.dex */
public interface f {
    b getAssetPackage();

    ItemCategory getCategory();

    String getFilePath();

    String getIconPath();

    String getId();

    Map<String, String> getLabel();

    String getPackageURI();

    String getSampleText();

    String getThumbPath();

    ItemType getType();

    boolean isHidden();
}
