package com.miui.gallery.collage.core;

import com.miui.gallery.collage.core.layout.LayoutPresenter;
import com.miui.gallery.collage.core.poster.PosterPresenter;
import com.miui.gallery.collage.core.stitching.StitchingPresenter;

/* loaded from: classes.dex */
public enum Effect {
    POSTER,
    LAYOUT,
    STITCHING;

    /* renamed from: com.miui.gallery.collage.core.Effect$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$collage$core$Effect;

        static {
            int[] iArr = new int[Effect.values().length];
            $SwitchMap$com$miui$gallery$collage$core$Effect = iArr;
            try {
                iArr[Effect.LAYOUT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$collage$core$Effect[Effect.STITCHING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$collage$core$Effect[Effect.POSTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public CollagePresenter generatePresenter() {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$collage$core$Effect[ordinal()];
        if (i != 1) {
            if (i == 2) {
                return new StitchingPresenter();
            }
            return new PosterPresenter();
        }
        return new LayoutPresenter();
    }
}
