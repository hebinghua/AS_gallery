package com.miui.gallery.editor.photo.app;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.photo.app.adjust2.Adjust2LibraryLoaderHelper;
import com.miui.gallery.editor.photo.app.remover2.sdk.Remover2LibraryLoaderHelper;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyLibraryLoaderHelper;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractLongCropFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.common.model.FilterCategory;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider;
import com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

/* loaded from: classes2.dex */
public abstract class AbstractNaviFragment extends AndroidFragment {
    public static final Property<View, Float> VIEW_RELATIVE_Y = new Property<View, Float>(Float.class, "relative_y") { // from class: com.miui.gallery.editor.photo.app.AbstractNaviFragment.1
        @Override // android.util.Property
        public Float get(View view) {
            return null;
        }

        @Override // android.util.Property
        public void set(View view, Float f) {
            view.setY((((View) view.getParent()).getHeight() - view.getHeight()) + f.floatValue());
        }
    };
    public static int sAnimAppearDelay;
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public Callbacks mCallbacks;
    public List<NavigatorData> mData;
    public boolean mNavigatorCreated;
    public NavigatorData[] mSupportedData;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        void onNavigate(Effect effect);
    }

    public View onCreateNavigator(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    public void onNavigatorCreated(View view, Bundle bundle) {
    }

    public AbstractNaviFragment() {
        NavigatorData[] navigatorDataArr = new NavigatorData[Effect.values().length];
        this.mSupportedData = navigatorDataArr;
        Effect<SdkProvider<FilterCategory, AbstractEffectFragment>> effect = Effect.FILTER;
        navigatorDataArr[effect.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_filter).setName(R.string.photo_editor_filter).setEffect(effect).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr2 = this.mSupportedData;
        Effect<SdkProvider<Adjust2Data, AbstractEffectFragment>> effect2 = Effect.ADJUST2;
        navigatorDataArr2[effect2.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_adjust).setName(R.string.photo_editor_adjust).setEffect(effect2).setDownloadState(Adjust2LibraryLoaderHelper.getInstance().getLoaderStateConsistent()).build();
        NavigatorData[] navigatorDataArr3 = this.mSupportedData;
        Effect<SdkProvider<AdjustData, AbstractEffectFragment>> effect3 = Effect.ADJUST;
        navigatorDataArr3[effect3.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_adjust).setName(R.string.photo_editor_adjust).setEffect(effect3).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr4 = this.mSupportedData;
        Effect<SdkProvider<CropData, AbstractCropFragment>> effect4 = Effect.CROP;
        navigatorDataArr4[effect4.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_crop).setName(R.string.photo_editor_crop_and_rotate).setEffect(effect4).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr5 = this.mSupportedData;
        Effect<SdkProvider<DoodleData, AbstractDoodleFragment>> effect5 = Effect.DOODLE;
        navigatorDataArr5[effect5.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_doodle).setName(R.string.photo_editor_doodle).setEffect(effect5).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr6 = this.mSupportedData;
        Effect<AbstractStickerProvider> effect6 = Effect.STICKER;
        navigatorDataArr6[effect6.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_sticker).setName(R.string.photo_editor_sticker).setEffect(effect6).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr7 = this.mSupportedData;
        Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect7 = Effect.BEAUTIFY2;
        navigatorDataArr7[effect7.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_beautify).setName(R.string.photo_editor_beautify).setEffect(effect7).setIconJson(R.raw.beautify).setDownloadState(Adjust2LibraryLoaderHelper.getInstance().getLoaderStateConsistent()).build();
        NavigatorData[] navigatorDataArr8 = this.mSupportedData;
        Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect8 = Effect.BEAUTIFY;
        navigatorDataArr8[effect8.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_beautify).setName(R.string.photo_editor_beautify).setEffect(effect8).setIconJson(R.raw.beautify).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr9 = this.mSupportedData;
        Effect<SdkProvider<Object, AbstractLongCropFragment>> effect9 = Effect.LONG_CROP;
        navigatorDataArr9[effect9.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_crop).setName(R.string.photo_editor_long_crop).setEffect(effect9).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr10 = this.mSupportedData;
        Effect<AbstractTextProvider> effect10 = Effect.TEXT;
        navigatorDataArr10[effect10.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_text).setName(R.string.photo_editor_text).setEffect(effect10).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr11 = this.mSupportedData;
        Effect<SdkProvider<MosaicData, AbstractMosaicFragment>> effect11 = Effect.MOSAIC;
        navigatorDataArr11[effect11.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_mosaic).setName(R.string.photo_editor_mosaic).setEffect(effect11).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr12 = this.mSupportedData;
        Effect<SdkProvider<RemoverData, AbstractRemoverFragment>> effect12 = Effect.REMOVER;
        navigatorDataArr12[effect12.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_remover).setName(R.string.photo_editor_remover).setEffect(effect12).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr13 = this.mSupportedData;
        Effect<SdkProvider<FrameData, AbstractFrameFragment>> effect13 = Effect.FRAME;
        navigatorDataArr13[effect13.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_frame).setName(R.string.photo_editor_frame).setEffect(effect13).setDownloadState(17).build();
        NavigatorData[] navigatorDataArr14 = this.mSupportedData;
        Effect<SdkProvider<SkyCategory, AbstractEffectFragment>> effect14 = Effect.SKY;
        navigatorDataArr14[effect14.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_sky).setName(R.string.photo_editor_sky).setEffect(effect14).setDownloadState(SkyLibraryLoaderHelper.INSTANCE.getLoaderStateConsistent()).build();
        NavigatorData[] navigatorDataArr15 = this.mSupportedData;
        Effect<SdkProvider<Remover2Data, AbstractRemover2Fragment>> effect15 = Effect.REMOVER2;
        navigatorDataArr15[effect15.ordinal()] = new NavigatorData.Builder().setIcon(R.drawable.navigator_icon_remover).setName(R.string.photo_editor_remover).setEffect(effect15).setDownloadState(Remover2LibraryLoaderHelper.INSTANCE.getLoaderStateConsistent()).build();
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mCallbacks == null) {
            throw new IllegalStateException("illegal state: no callbacks bound");
        }
        if (sAnimOffset == 0) {
            sAnimOffset = getActivity().getResources().getDimensionPixelSize(R.dimen.photo_editor_enter_sub_editor_main_menu_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_disappear_duration);
        }
        if (sAnimAppearDelay == 0) {
            sAnimAppearDelay = getActivity().getResources().getInteger(R.integer.photo_editor_sub_editor_main_menu_appear_delay);
        }
        initialize();
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateNavigator = onCreateNavigator(layoutInflater, viewGroup, bundle);
        this.mNavigatorCreated = onCreateNavigator != null;
        return onCreateNavigator;
    }

    @Override // androidx.fragment.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (this.mNavigatorCreated) {
            onNavigatorCreated(view, bundle);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        DefaultLogger.d("AbstractNaviFragment", "onCreateAnimator:" + i + ":" + z + ":" + i2);
        if (8194 != i || !z) {
            ObjectAnimator objectAnimator = new ObjectAnimator();
            if (z) {
                objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -sAnimOffset, 0.0f), PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
                objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
                getView().setAlpha(0.0f);
                objectAnimator.setStartDelay(sAnimAppearDelay);
                objectAnimator.setDuration(sAnimAppearDuration);
            } else {
                objectAnimator.setValues(PropertyValuesHolder.ofFloat(VIEW_RELATIVE_Y, 0.0f, -sAnimOffset), PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f));
                objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
                objectAnimator.setDuration(sAnimDisappearDuration);
            }
            return objectAnimator;
        }
        return null;
    }

    public final List<NavigatorData> getNaviData() {
        return this.mData;
    }

    public final void notifyNavigate(Effect effect) {
        this.mCallbacks.onNavigate(effect);
    }

    public final void initialize() {
        ArrayList<Integer> integerArrayList = getArguments().getIntegerArrayList(MiStat.Param.CONTENT);
        this.mData = new ArrayList();
        Iterator<Integer> it = integerArrayList.iterator();
        while (it.hasNext()) {
            int intValue = it.next().intValue();
            NavigatorData[] navigatorDataArr = this.mSupportedData;
            if (intValue > navigatorDataArr.length) {
                DefaultLogger.w("AbstractNaviFragment", "invalid effect index of " + intValue);
            } else {
                this.mData.add(navigatorDataArr[intValue]);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class NavigatorData {
        public int downloadState;
        public final Effect effect;
        public final int icon;
        public final int iconJson;
        public final int name;

        public NavigatorData(Builder builder) {
            this.icon = builder.icon;
            this.name = builder.name;
            this.effect = builder.effect;
            this.downloadState = builder.downloadState;
            this.iconJson = builder.iconJson;
        }

        public String toString() {
            return "NavigatorData{icon=" + this.icon + ", iconJson=" + this.iconJson + ", name=" + this.name + ", effect=" + this.effect + ", downloadState=" + this.downloadState + '}';
        }

        /* loaded from: classes2.dex */
        public static class Builder {
            public int downloadState;
            public Effect effect;
            public int icon;
            public int iconJson;
            public int name;

            public Builder setName(int i) {
                this.name = i;
                return this;
            }

            public Builder setIcon(int i) {
                this.icon = i;
                return this;
            }

            public Builder setIconJson(int i) {
                this.iconJson = i;
                return this;
            }

            public Builder setEffect(Effect effect) {
                this.effect = effect;
                return this;
            }

            public Builder setDownloadState(int i) {
                this.downloadState = i;
                return this;
            }

            public NavigatorData build() {
                return new NavigatorData(this);
            }
        }

        public boolean isSelectedEffect(Effect effect) {
            return this.effect == effect;
        }
    }
}
