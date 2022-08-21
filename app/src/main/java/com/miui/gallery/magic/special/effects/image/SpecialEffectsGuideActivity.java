package com.miui.gallery.magic.special.effects.image;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.editor.R$integer;
import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseGuideActivity;
import com.miui.gallery.magic.fetch.ArtRequest;
import com.miui.gallery.magic.fetch.ArtResourceFetcher;
import com.miui.gallery.magic.special.effects.image.adapter.SpecialGuideAdapter;
import com.miui.gallery.magic.special.effects.image.bean.SpecialIconItem;
import com.miui.gallery.magic.special.effects.image.bean.SpecialItem;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicNetUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import java.util.ArrayList;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class SpecialEffectsGuideActivity extends BaseGuideActivity implements View.OnClickListener {
    public RelativeLayout idpSeCancel;
    public ImageView ivTittleBack;
    public SpecialGuideAdapter mAdapter;
    public int mCurrentIndex;
    public StaggeredGridLayoutManager mGridLayoutManager;
    public RecyclerView mListView;
    public TextView tvBaseTittle;

    public static /* synthetic */ void $r8$lambda$ApLRZq70cFLaxFVFc7cX7z6azJM(SpecialEffectsGuideActivity specialEffectsGuideActivity, int i, boolean z, boolean z2) {
        specialEffectsGuideActivity.lambda$initData$0(i, z, z2);
    }

    /* renamed from: $r8$lambda$klyFrWjnoPSOIK8-U8nLk6TnX0o */
    public static /* synthetic */ boolean m1043$r8$lambda$klyFrWjnoPSOIK8U8nLk6TnX0o(SpecialEffectsGuideActivity specialEffectsGuideActivity, androidx.recyclerview.widget.RecyclerView recyclerView, View view, int i) {
        return specialEffectsGuideActivity.lambda$initData$1(recyclerView, view, i);
    }

    @Override // com.miui.gallery.magic.base.BaseGuideActivity, com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        setContentView(R$layout.ts_magic_special_effects_guide);
        initView();
        initData();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        MagicSampler.getInstance().recordCategory("art", "enter");
    }

    public final void initData() {
        SpecialGuideAdapter specialGuideAdapter = new SpecialGuideAdapter(getListData());
        this.mAdapter = specialGuideAdapter;
        this.mListView.setAdapter(specialGuideAdapter);
        this.mAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public final boolean OnItemClick(androidx.recyclerview.widget.RecyclerView recyclerView, View view, int i) {
                return SpecialEffectsGuideActivity.m1043$r8$lambda$klyFrWjnoPSOIK8U8nLk6TnX0o(SpecialEffectsGuideActivity.this, recyclerView, view, i);
            }
        });
    }

    public /* synthetic */ boolean lambda$initData$1(androidx.recyclerview.widget.RecyclerView recyclerView, View view, final int i) {
        if (i == 4) {
            boolean downLoad = SpecialIconItem.getDownLoad();
            if (downLoad && SpecialIconItem.isDownloading()) {
                return false;
            }
            if (downLoad) {
                if (!MagicNetUtil.isNetworkAvailable(MagicUtils.getGalleryApp())) {
                    MagicToast.showToast(MagicUtils.getGalleryApp(), R$string.magic_network_error);
                } else if (MagicNetUtil.IsMobileNetConnect(MagicUtils.getGalleryApp())) {
                    NetworkConsider.consider(this, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                        public final void onConfirmed(boolean z, boolean z2) {
                            SpecialEffectsGuideActivity.$r8$lambda$ApLRZq70cFLaxFVFc7cX7z6azJM(SpecialEffectsGuideActivity.this, i, z, z2);
                        }
                    });
                } else {
                    fetchDownLoad(i);
                }
            } else {
                selectImage(i);
            }
        } else {
            selectImage(i);
        }
        return false;
    }

    public /* synthetic */ void lambda$initData$0(int i, boolean z, boolean z2) {
        if (z) {
            fetchDownLoad(i);
        }
    }

    public final void fetchDownLoad(final int i) {
        ArtRequest artRequest = new ArtRequest("artphoto_windowfog", 14693595597635680L);
        artRequest.setListener(new Request.Listener() { // from class: com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity.1
            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onFail() {
            }

            {
                SpecialEffectsGuideActivity.this = this;
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onStart() {
                SpecialEffectsGuideActivity.this.mAdapter.notifyDownloading(i, true);
            }

            @Override // com.miui.gallery.net.fetch.Request.Listener
            public void onSuccess() {
                SpecialEffectsGuideActivity.this.mAdapter.notifyItem(i, false, false);
                SpecialEffectsGuideActivity.this.selectImage(i);
            }
        });
        ArtResourceFetcher.INSTANCE.fetch(artRequest);
    }

    public final void selectImage(int i) {
        this.mCurrentIndex = i;
        startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1021);
    }

    public final List<SpecialItem> getListData() {
        ArrayList arrayList = new ArrayList();
        String[] arrayById = ResourceUtil.getArrayById(R$array.magic_effects_icon);
        String[] arrayById2 = ResourceUtil.getArrayById(R$array.magic_effects_title);
        String[] arrayById3 = ResourceUtil.getArrayById(R$array.magic_effects_description);
        SpecialIconItem.setDownload(!ArtResourceFetcher.INSTANCE.isExistResource());
        for (int i = 0; i < 6; i++) {
            arrayList.add(new SpecialItem(arrayById[i], arrayById2[i], arrayById3[i]));
        }
        return arrayList;
    }

    public final void initView() {
        this.mListView = (RecyclerView) findViewById(R$id.magic_effects_guide_list);
        this.idpSeCancel = (RelativeLayout) findViewById(R$id.idp_se_cancel);
        TextView textView = (TextView) findViewById(R$id.tv_base_tittle);
        this.tvBaseTittle = textView;
        textView.setText(R$string.magic_effect_text);
        this.ivTittleBack = (ImageView) findViewById(R$id.iv_tittle_back);
        if (BaseMiscUtil.isRTLDirection()) {
            this.ivTittleBack.setRotation(180.0f);
        }
        FolmeUtil.setCustomTouchAnim(this.idpSeCancel, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, null, true);
        if (BaseBuildUtil.isPad()) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(ResourceUtil.getInt(R$integer.special_grid_span_count), 1);
            this.mGridLayoutManager = staggeredGridLayoutManager;
            this.mListView.setLayoutManager(staggeredGridLayoutManager);
            return;
        }
        this.mListView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (BaseBuildUtil.isPad()) {
            this.mListView.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity.2
                {
                    SpecialEffectsGuideActivity.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) SpecialEffectsGuideActivity.this.mListView.getLayoutParams();
                    int dimensionPixelSize = SpecialEffectsGuideActivity.this.getResources().getDimensionPixelSize(R$dimen.magic_effects_guide_list_magin_start);
                    layoutParams.setMarginStart(dimensionPixelSize);
                    layoutParams.setMarginEnd(dimensionPixelSize);
                    SpecialEffectsGuideActivity.this.mListView.setLayoutParams(layoutParams);
                }
            });
        }
        StaggeredGridLayoutManager staggeredGridLayoutManager = this.mGridLayoutManager;
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.setSpanCount(ResourceUtil.getInt(R$integer.special_grid_span_count));
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.idp_se_cancel) {
            finish();
        }
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1021) {
            if (i != 1024) {
                return;
            }
            this.mAdapter.notifyDataSetChanged();
        } else if (intent == null || intent.getData() == null) {
        } else {
            Uri data = intent.getData();
            if (ImageFormatUtils.isSupportImageFormat(data)) {
                if (MagicFileUtil.checkMaxPX(this, data)) {
                    MagicToast.showToast(this, R$string.magic_max_px);
                    return;
                } else {
                    openMakeActivity(data, this.mCurrentIndex);
                    return;
                }
            }
            MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_image_edit));
        }
    }

    public final void openMakeActivity(Uri uri, int i) {
        Intent intent = new Intent(this, SpecialEffectsActivity.class);
        intent.setData(uri);
        intent.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, i);
        startActivityForResult(intent, 1024);
    }

    @Override // com.miui.gallery.magic.base.BaseGuideActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        ArtResourceFetcher.INSTANCE.cancelAll();
    }
}
