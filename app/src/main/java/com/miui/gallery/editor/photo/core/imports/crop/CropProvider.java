package com.miui.gallery.editor.photo.core.imports.crop;

import android.content.Context;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractCropFragment;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.util.StaticContext;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class CropProvider extends SdkProvider<CropData, AbstractCropFragment> {
    public List<CropData> mDataList;

    public CropProvider() {
        super(Effect.CROP);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        notifyInitializeFinish();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractCropFragment onCreateFragment() {
        return new CropFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new CropEngine();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends CropData> list() {
        List<CropData> asList = Arrays.asList(new CropData.AspectRatio((short) 1, StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_free), R.drawable.crop_menu_item_free_selector, R.string.photo_editor_talkback_crop_free, 0, 0), new CropData.AspectRatio((short) 1, StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_origin), R.drawable.crop_menu_item_origin_selector, R.string.photo_editor_talkback_crop_original, -1, -1), new CropData.AspectRatio((short) 1, StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_fullscreen), R.drawable.crop_menu_item_screen_selector, R.string.photo_editor_talkback_crop_screen, -2, -2), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 1, 1), R.drawable.crop_menu_item_1_1_selector, R.string.photo_editor_talkback_crop_1_1, 1, 1), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 3, 4), R.drawable.crop_menu_item_3_4_selector, R.string.photo_editor_talkback_crop_3_4, 3, 4), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 4, 3), R.drawable.crop_menu_item_4_3_selector, R.string.photo_editor_talkback_crop_4_3, 4, 3), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 16, 9), R.drawable.crop_menu_item_16_9_selector, R.string.photo_editor_talkback_crop_16_9, 16, 9), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 9, 16), R.drawable.crop_menu_item_9_16_selector, R.string.photo_editor_talkback_crop_9_16, 9, 16), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 2, 3), R.drawable.crop_menu_item_2_3_selector, R.string.photo_editor_talkback_crop_2_3, 2, 3), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 3, 2), R.drawable.crop_menu_item_3_2_selector, R.string.photo_editor_talkback_crop_3_2, 3, 2), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 18, 9), R.drawable.crop_menu_item_18_9_selector, R.string.photo_editor_talkback_crop_18_9, 18, 9), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_former_latter), 9, 18), R.drawable.crop_menu_item_9_18_selector, R.string.photo_editor_talkback_crop_9_18, 9, 18), new CropData.AspectRatio((short) 2, String.format(StaticContext.sGetAndroidContext().getString(R.string.photo_editor_crop_size_movie), Double.valueOf(2.39d), 1), R.drawable.crop_menu_item_movie_selector, R.string.photo_editor_talkback_crop_movie, 239, 100));
        this.mDataList = asList;
        return asList;
    }

    static {
        SdkManager.INSTANCE.register(new CropProvider());
    }
}
