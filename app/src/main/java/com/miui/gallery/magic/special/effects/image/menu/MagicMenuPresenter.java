package com.miui.gallery.magic.special.effects.image.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.MagicFilterInvoker;
import com.miui.gallery.magic.MagicFilterType;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.fetch.ArtRequest;
import com.miui.gallery.magic.fetch.ArtResourceFetcher;
import com.miui.gallery.magic.special.effects.image.adapter.SpecialEffectsAdapter;
import com.miui.gallery.magic.special.effects.image.bean.SpecialIconItem;
import com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter;
import com.miui.gallery.magic.special.effects.image.preview.PreviewPresenter;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicMainHandler;
import com.miui.gallery.magic.util.MagicNetUtil;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.magic.widget.MagicLoadingDialog;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.utils.BitmapUtils;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class MagicMenuPresenter extends BasePresenter<MagicMenuFragment, MagicMenuModel, IMenu$VP> {
    public volatile boolean isMagicFinish;
    public SpecialEffectsAdapter mAdapter;
    public Bitmap mMagicFilter;
    public MagicFilterInvoker mMagicFilterInvoker;
    public MattingInvoker mMattingInvoker;
    public LruCache<String, Bitmap> mMemoryCache = new LruCache<>(6144);
    public boolean mNotFace;
    public MattingInvoker.SegmentResult result;

    public static /* synthetic */ void $r8$lambda$Y3mNfC9Z7Hh3UE0ffTA2qjmXQfw(MagicMenuPresenter magicMenuPresenter) {
        magicMenuPresenter.lambda$unBindView$0();
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public MagicMenuModel getModelInstance() {
        return new MagicMenuModel(this);
    }

    /* renamed from: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IMenu$VP {
        public static /* synthetic */ void $r8$lambda$8lgdG2gXrSk6Dp1btOi_DWhKpaI(AnonymousClass1 anonymousClass1, Bitmap bitmap, int i) {
            anonymousClass1.lambda$loadFinish$3(bitmap, i);
        }

        public static /* synthetic */ void $r8$lambda$L999g7CWazKvxweWm4prF2D4UyY(AnonymousClass1 anonymousClass1, int i, SpecialIconItem specialIconItem, boolean z, boolean z2) {
            anonymousClass1.lambda$itemClick$0(i, specialIconItem, z, z2);
        }

        public static /* synthetic */ void $r8$lambda$LT0KFonvw37pATbmIsNzFZ4LdSA(AnonymousClass1 anonymousClass1, BaseFragmentActivity baseFragmentActivity, int i) {
            anonymousClass1.lambda$loadFinish$2(baseFragmentActivity, i);
        }

        /* renamed from: $r8$lambda$TztUxtqcvQFxIMz3ANwav-Tz31M */
        public static /* synthetic */ void m1049$r8$lambda$TztUxtqcvQFxIMz3ANwavTz31M(AnonymousClass1 anonymousClass1, BaseFragmentActivity baseFragmentActivity) {
            anonymousClass1.lambda$loadFinish$1(baseFragmentActivity);
        }

        public AnonymousClass1() {
            MagicMenuPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
        public void setAdapter(Adapter adapter) {
            MagicMenuPresenter.this.mAdapter = (SpecialEffectsAdapter) adapter;
            ((MagicMenuFragment) MagicMenuPresenter.this.mView.get()).getContract().setAdapter(adapter);
            MagicMenuPresenter.this.mAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter.1.1
                {
                    AnonymousClass1.this = this;
                }

                @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
                public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                    AnonymousClass1.this.itemClick(i, MagicMenuPresenter.this.mAdapter.getItem(i));
                    AnonymousClass1.this.scrollTo(i);
                    return false;
                }
            });
        }

        public final void itemClick(final int i, final SpecialIconItem specialIconItem) {
            if (PreviewPresenter.isStartSave()) {
                return;
            }
            if (i != 4) {
                MagicMenuPresenter.this.mAdapter.selectItem(i, true);
                selectItem(i, new Bitmap[0]);
                return;
            }
            boolean downLoad = SpecialIconItem.getDownLoad();
            if (downLoad && SpecialIconItem.isDownloading()) {
                return;
            }
            if (downLoad) {
                if (!MagicNetUtil.isNetworkAvailable(MagicUtils.getGalleryApp())) {
                    MagicToast.showToast(MagicUtils.getGalleryApp(), R$string.magic_network_error);
                } else if (MagicNetUtil.IsMobileNetConnect(MagicUtils.getGalleryApp())) {
                    BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
                    if (activityWithSync == null) {
                        return;
                    }
                    NetworkConsider.consider(activityWithSync, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$1$$ExternalSyntheticLambda1
                        @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                        public final void onConfirmed(boolean z, boolean z2) {
                            MagicMenuPresenter.AnonymousClass1.$r8$lambda$L999g7CWazKvxweWm4prF2D4UyY(MagicMenuPresenter.AnonymousClass1.this, i, specialIconItem, z, z2);
                        }
                    });
                } else {
                    fetchDownLoad(i, specialIconItem);
                }
            } else if (specialIconItem.getMasks() == null) {
            } else {
                MagicMenuPresenter.this.mAdapter.selectItem(i, true);
                selectItem(i, MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[0]), MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[1]), MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[2]));
            }
        }

        public /* synthetic */ void lambda$itemClick$0(int i, SpecialIconItem specialIconItem, boolean z, boolean z2) {
            if (z) {
                fetchDownLoad(i, specialIconItem);
            }
        }

        public final void fetchDownLoad(final int i, final SpecialIconItem specialIconItem) {
            final ArtRequest artRequest = new ArtRequest("artphoto_windowfog", 14693595597635680L);
            artRequest.setListener(new Request.Listener() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter.1.2
                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onFail() {
                }

                {
                    AnonymousClass1.this = this;
                }

                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onStart() {
                    MagicMenuPresenter.this.mAdapter.notifyDownloading(i, true);
                }

                @Override // com.miui.gallery.net.fetch.Request.Listener
                public void onSuccess() {
                    if (MagicMenuPresenter.this.getActivityWithSync() == null) {
                        return;
                    }
                    MagicMenuPresenter.this.mAdapter.selectItem(i, true);
                    FetchManager.INSTANCE.cancel(artRequest);
                    MagicMenuPresenter.this.mAdapter.notifyItem(i, false, false);
                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                    anonymousClass1.selectItem(i, MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[0]), MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[1]), MagicMenuPresenter.this.getBitmap(specialIconItem.getMasks()[2]));
                }
            });
            ArtResourceFetcher.INSTANCE.fetch(artRequest);
        }

        public final void selectItem(int i, final Bitmap... bitmapArr) {
            final MagicFilterType magicFilterType = MagicMenuPresenter.this.getMagicFilterType(i);
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter.1.3
                {
                    AnonymousClass1.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
                    if (activityWithSync != null) {
                        Bitmap copy = ((Bitmap) activityWithSync.event(1, null)).copy(Bitmap.Config.ARGB_8888, true);
                        final Bitmap addEffects = AnonymousClass1.this.addEffects(magicFilterType, copy, bitmapArr);
                        copy.recycle();
                        MagicMainHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter.1.3.1
                            {
                                AnonymousClass3.this = this;
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                if (MagicMenuPresenter.this.isMagicFinish) {
                                    Bitmap bitmap = addEffects;
                                    if (bitmap == null) {
                                        return;
                                    }
                                    bitmap.recycle();
                                    return;
                                }
                                BaseFragmentActivity activityWithSync2 = MagicMenuPresenter.this.getActivityWithSync();
                                if (activityWithSync2 == null) {
                                    return;
                                }
                                MagicFilterType magicFilterType2 = magicFilterType;
                                if (magicFilterType2 == MagicFilterType.GLASS_WINDOW || magicFilterType2 == MagicFilterType.SHUTTER) {
                                    activityWithSync2.event(3, addEffects);
                                } else {
                                    activityWithSync2.event(0, addEffects);
                                }
                            }
                        });
                    }
                }
            });
        }

        public final Bitmap addEffects(MagicFilterType magicFilterType, Bitmap bitmap, Bitmap... bitmapArr) {
            MagicLog.INSTANCE.startLog("effects_add", "人像滤镜添加效果");
            try {
                Bitmap magic = MagicMenuPresenter.this.mMagicFilterInvoker.magic(bitmap, magicFilterType, MagicMenuPresenter.this.mMagicFilter, bitmapArr);
                MagicLog.INSTANCE.endLog("effects_add", "人像滤镜添加效果");
                return magic;
            } catch (Exception unused) {
                BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
                if (activityWithSync == null) {
                    return null;
                }
                MagicToast.showToast(activityWithSync, activityWithSync.getString(R$string.magic_error_adding_filter));
                return null;
            }
        }

        @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
        public void loadListData() {
            List<SpecialIconItem> data = ((MagicMenuModel) MagicMenuPresenter.this.mModel).getContract().getData();
            BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
            if (activityWithSync != null) {
                setAdapter(new SpecialEffectsAdapter(data, activityWithSync));
            }
        }

        @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
        public void loadFinish(final Bitmap bitmap) {
            final int intExtra;
            MagicMenuPresenter.this.isMagicFinish = false;
            final BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
            if (activityWithSync == null || (intExtra = activityWithSync.getIntent().getIntExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, -1)) == -1) {
                return;
            }
            activityWithSync.showLoading(false, new MagicLoadingDialog.Callback() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.magic.widget.MagicLoadingDialog.Callback
                public final void doCancel() {
                    MagicMenuPresenter.AnonymousClass1.m1049$r8$lambda$TztUxtqcvQFxIMz3ANwavTz31M(MagicMenuPresenter.AnonymousClass1.this, activityWithSync);
                }
            });
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    MagicMenuPresenter.AnonymousClass1.$r8$lambda$8lgdG2gXrSk6Dp1btOi_DWhKpaI(MagicMenuPresenter.AnonymousClass1.this, bitmap, intExtra);
                }
            });
        }

        public /* synthetic */ void lambda$loadFinish$1(BaseFragmentActivity baseFragmentActivity) {
            MagicMenuPresenter.this.isMagicFinish = true;
            baseFragmentActivity.removeLoadingDialog();
            baseFragmentActivity.finish();
        }

        public /* synthetic */ void lambda$loadFinish$3(Bitmap bitmap, final int i) {
            if (!MagicMenuPresenter.this.isMagicFinish) {
                MagicMenuPresenter magicMenuPresenter = MagicMenuPresenter.this;
                if (magicMenuPresenter.mMattingInvoker == null) {
                    magicMenuPresenter.mMattingInvoker = new MattingInvoker();
                }
                MagicLog.INSTANCE.startLog("effects_nativeInitModel", "算法初始化");
                MagicMenuPresenter.this.mMattingInvoker.initModel();
                MagicLog.INSTANCE.endLog("effects_nativeInitModel", "算法初始化");
                if (MagicMenuPresenter.this.isMagicFinish || bitmap == null || bitmap.isRecycled()) {
                    return;
                }
                MagicMenuPresenter magicMenuPresenter2 = MagicMenuPresenter.this;
                magicMenuPresenter2.initMagicFilter(bitmap, magicMenuPresenter2.initSegment(bitmap, true));
                final BaseFragmentActivity activityWithSync = MagicMenuPresenter.this.getActivityWithSync();
                if (activityWithSync == null) {
                    return;
                }
                activityWithSync.runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$1$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        MagicMenuPresenter.AnonymousClass1.$r8$lambda$LT0KFonvw37pATbmIsNzFZ4LdSA(MagicMenuPresenter.AnonymousClass1.this, activityWithSync, i);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$loadFinish$2(BaseFragmentActivity baseFragmentActivity, int i) {
            if (MagicMenuPresenter.this.isMagicFinish) {
                return;
            }
            baseFragmentActivity.removeLoadingDialog();
            MagicMenuPresenter.this.mAdapter.selectItem(i, true);
            scrollTo(i);
            itemClick(i, MagicMenuPresenter.this.mAdapter.getItem(i));
        }

        @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
        public boolean getNotFace() {
            return MagicMenuPresenter.this.mNotFace;
        }

        @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
        public void scrollTo(int i) {
            ((MagicMenuFragment) MagicMenuPresenter.this.mView.get()).getContract().scrollTo(i);
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final MattingInvoker.SegmentResult initSegment(Bitmap bitmap, boolean z) {
        BaseFragmentActivity activityWithSync;
        long currentTimeMillis = System.currentTimeMillis();
        MagicLog.INSTANCE.startLog("effects_nativeSegmentPredict", "人像滤镜分割nativeSegmentPredict");
        if (this.result == null || z) {
            this.result = this.mMattingInvoker.segmentPredict(bitmap);
        }
        MagicLog.INSTANCE.endLog("effects_nativeSegmentPredict", "人像滤镜分割nativeSegmentPredict");
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(currentTimeMillis2));
        MagicSampler.getInstance().recordCategory("art", "check_time_consuming", hashMap);
        if (!this.isMagicFinish && this.result != null && (activityWithSync = getActivityWithSync()) != null) {
            if (this.result.getPersonCount() <= 0 && !PreviewPresenter.isStartSave()) {
                MagicToast.showToast(activityWithSync, R$string.magic_no_human_face);
            }
            if (this.result.isEmpty()) {
                activityWithSync.removeLoadingDialog();
            }
        }
        return this.result;
    }

    public final void initMagicFilter(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult) {
        MagicLog.INSTANCE.startLog("effects_MagicFilter", "获得人像滤镜需要传入bitmap_mask_filterr");
        this.mMagicFilterInvoker = new MagicFilterInvoker();
        if (!this.isMagicFinish && bitmap != null && !bitmap.isRecycled()) {
            this.mMagicFilter = this.mMagicFilterInvoker.getFilter(bitmap, segmentResult);
        }
        MagicLog.INSTANCE.endLog("effects_MagicFilter", "获得人像滤镜需要传入bitmap_mask_filterr");
    }

    public final MagicFilterType getMagicFilterType(int i) {
        this.mNotFace = false;
        if (i != 0) {
            if (i == 1) {
                return MagicFilterType.WATER_COLOR;
            }
            if (i == 2) {
                return MagicFilterType.RGB_SHIFT;
            }
            if (i == 3) {
                MagicFilterType magicFilterType = MagicFilterType.SHUTTER;
                this.mNotFace = true;
                return magicFilterType;
            } else if (i == 4) {
                MagicFilterType magicFilterType2 = MagicFilterType.GLASS_WINDOW;
                this.mNotFace = true;
                return magicFilterType2;
            } else if (i == 5) {
                return MagicFilterType.WAVE;
            } else {
                throw new IllegalStateException("Unexpected value: " + i);
            }
        }
        return MagicFilterType.SKETCH;
    }

    public final Bitmap getBitmap(String str) {
        Bitmap bitmap = this.mMemoryCache.get(str);
        if (bitmap == null && (bitmap = BitmapUtils.getBitmapFromFile(str)) != null && str != null) {
            this.mMemoryCache.put(str, bitmap);
        }
        return bitmap;
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void unBindView() {
        super.unBindView();
        this.mMemoryCache = null;
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuPresenter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MagicMenuPresenter.$r8$lambda$Y3mNfC9Z7Hh3UE0ffTA2qjmXQfw(MagicMenuPresenter.this);
            }
        });
        ArtResourceFetcher.INSTANCE.cancelAll();
    }

    public /* synthetic */ void lambda$unBindView$0() {
        MattingInvoker mattingInvoker = this.mMattingInvoker;
        if (mattingInvoker != null) {
            mattingInvoker.destoryModel();
        }
        this.result = null;
        this.mMattingInvoker = null;
    }
}
