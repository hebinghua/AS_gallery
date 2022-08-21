package com.nexstreaming.app.common.nexasset.assetpackage.db;

import com.nexstreaming.app.common.nexasset.assetpackage.a;
import com.nexstreaming.app.common.norm.b;

/* loaded from: classes3.dex */
public class CategoryRecord extends b implements a {
    public long _id;
    public String categoryIconURL;
    @b.e
    @b.g
    public long categoryId;
    public String categoryName;
    public String categoryURL;

    public long getCategoryId() {
        return this.categoryId;
    }

    public String getCategoryIconURL() {
        return this.categoryIconURL;
    }

    public String getCategoryURL() {
        return this.categoryURL;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.a
    public String getCategoryAlias() {
        return this.categoryName;
    }
}
