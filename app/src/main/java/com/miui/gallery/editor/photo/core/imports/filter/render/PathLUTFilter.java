package com.miui.gallery.editor.photo.core.imports.filter.render;

import com.miui.filtersdk.filter.base.ColorLookupFilter;

/* loaded from: classes2.dex */
public class PathLUTFilter extends ColorLookupFilter {
    public PathLUTFilter(String str) {
        super(str);
    }

    @Override // com.miui.filtersdk.filter.base.ColorLookupFilter, com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInitialized() {
        runOnDraw(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.render.PathLUTFilter.1
            @Override // java.lang.Runnable
            public void run() {
                PathLUTFilter pathLUTFilter = PathLUTFilter.this;
                pathLUTFilter.mLookupSourceTexture = com.miui.filtersdk.utils.OpenGlUtils.loadTexture(com.miui.filtersdk.utils.OpenGlUtils.getImageFromPath(pathLUTFilter.mTable), -1, true);
            }
        });
    }
}
