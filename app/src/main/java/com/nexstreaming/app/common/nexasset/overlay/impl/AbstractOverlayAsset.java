package com.nexstreaming.app.common.nexasset.overlay.impl;

import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAsset;
import com.nexstreaming.kminternal.kinemaster.config.a;
import java.io.IOException;

/* loaded from: classes3.dex */
public abstract class AbstractOverlayAsset implements OverlayAsset {
    private final f itemInfo;

    public AbstractOverlayAsset(f fVar) {
        this.itemInfo = fVar;
    }

    public f getItemInfo() {
        return this.itemInfo;
    }

    public AssetPackageReader getAssetPackageReader() throws IOException {
        return AssetPackageReader.a(a.a().b(), this.itemInfo.getPackageURI(), this.itemInfo.getAssetPackage().getAssetId());
    }
}
