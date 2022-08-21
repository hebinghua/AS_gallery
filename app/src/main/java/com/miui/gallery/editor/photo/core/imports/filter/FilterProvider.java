package com.miui.gallery.editor.photo.core.imports.filter;

import android.content.Context;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class FilterProvider<T extends Metadata> extends SdkProvider<T, AbstractEffectFragment> {
    public static FilterProvider FILTER = new FilterProvider<FilterCategory>(Effect.FILTER) { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterProvider.1
        @Override // com.miui.gallery.editor.photo.core.imports.filter.FilterProvider, com.miui.gallery.editor.photo.core.SdkProvider
        public /* bridge */ /* synthetic */ AbstractEffectFragment onCreateFragment() {
            return super.onCreateFragment();
        }

        @Override // com.miui.gallery.editor.photo.core.SdkProvider
        public List<? extends FilterCategory> list() {
            return FilterManager.getFilterCategory();
        }
    };
    public static FilterProvider ADJUST = new FilterProvider<AdjustData>(Effect.ADJUST) { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterProvider.2
        @Override // com.miui.gallery.editor.photo.core.imports.filter.FilterProvider, com.miui.gallery.editor.photo.core.SdkProvider
        public /* bridge */ /* synthetic */ AbstractEffectFragment onCreateFragment() {
            return super.onCreateFragment();
        }

        @Override // com.miui.gallery.editor.photo.core.SdkProvider
        public List<? extends AdjustData> list() {
            return FilterManager.getAdjustData();
        }
    };
    public static FilterProvider BEAUTIFY = new FilterProvider<BeautifyData>(Effect.BEAUTIFY) { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterProvider.3
        @Override // com.miui.gallery.editor.photo.core.imports.filter.FilterProvider, com.miui.gallery.editor.photo.core.SdkProvider
        public /* bridge */ /* synthetic */ AbstractEffectFragment onCreateFragment() {
            return super.onCreateFragment();
        }

        @Override // com.miui.gallery.editor.photo.core.SdkProvider
        public List<? extends BeautifyData> list() {
            return FilterManager.getBeautifyData();
        }
    };

    public FilterProvider(Effect<? extends SdkProvider<T, AbstractEffectFragment>> effect) {
        super(effect);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new FilterRenderFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new FilterEngine(context);
    }

    static {
        SdkManager sdkManager = SdkManager.INSTANCE;
        sdkManager.register(FILTER);
        sdkManager.register(ADJUST);
        sdkManager.register(BEAUTIFY);
    }
}
