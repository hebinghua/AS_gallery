package com.miui.gallery.editor.photo.core.imports.filter.render;

import com.miui.filtersdk.filter.base.BaseOriginalFilter;

/* loaded from: classes2.dex */
public final class EmptyGPUImageFilter extends BaseOriginalFilter implements IFilterEmptyValidate {
    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    public boolean isEmpty() {
        return true;
    }
}
