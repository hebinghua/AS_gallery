package com.miui.gallery.magic.matting.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$drawable;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.fetch.MattingRequest;
import com.miui.gallery.magic.fetch.MattingResourceFetcher;
import com.miui.gallery.magic.matting.adapter.BackgroundAdapter;
import com.miui.gallery.magic.matting.adapter.BackgroundIconItem;
import com.miui.gallery.magic.matting.adapter.IconItem;
import com.miui.gallery.magic.matting.adapter.StrokeIconItem;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.magic.matting.menu.MattingMenuPresenter;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.ImageFormatUtils;
import com.miui.gallery.magic.util.MagicFileUtil;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicNetUtil;
import com.miui.gallery.magic.util.MagicThreadHandler;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.xiaomi.milab.videosdk.utils.BitmapUtils;
import java.io.File;
import java.io.IOException;

/* loaded from: classes2.dex */
public class MattingMenuPresenter extends BasePresenter<MattingMenuFragment, MattingMenuModel, IMenu$VP> {
    public BackgroundAdapter mAdapter;
    public String mBgUrl;
    public int mCurrentStrokeIndex;
    public int[] mImageSize = {1000, 1200};
    public int mBackgroundIndex = 0;
    public int mStrokeIndex = 0;
    public int mCurrentModel = 1;
    public Uri mUriGallery = null;
    public LruCache<String, Bitmap> mMemoryCache = new LruCache<>(6144);

    /* renamed from: $r8$lambda$GV-82NQfCV10b2zpFrklc69BiOo */
    public static /* synthetic */ void m1034$r8$lambda$GV82NQfCV10b2zpFrklc69BiOo(MattingMenuPresenter mattingMenuPresenter, BackgroundIconItem backgroundIconItem) {
        mattingMenuPresenter.lambda$checkDownload$2(backgroundIconItem);
    }

    public static /* synthetic */ void $r8$lambda$tFPHzPaOtDVTM7F60ivRCLWr9K4(MattingMenuPresenter mattingMenuPresenter) {
        mattingMenuPresenter.lambda$execBackgroundItem$0();
    }

    public static /* synthetic */ void $r8$lambda$wjHEg8nWFbhGzqD7doQLfSoCdOU(MattingMenuPresenter mattingMenuPresenter, int i, BackgroundIconItem backgroundIconItem, int i2, boolean z, boolean z2) {
        mattingMenuPresenter.lambda$checkDownload$1(i, backgroundIconItem, i2, z, z2);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public MattingMenuModel getModelInstance() {
        return new MattingMenuModel(this);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
        if (i != 1001 || intent == null || intent.getData() == null) {
            return;
        }
        Uri data = intent.getData();
        this.mUriGallery = data;
        if (ImageFormatUtils.isSupportImageFormat(data)) {
            if (!MagicFileUtil.checkIsBitmap(getActivity(), this.mUriGallery)) {
                BaseFragmentActivity activityWithSync = getActivityWithSync();
                if (activityWithSync == null) {
                    return;
                }
                MagicToast.showToast(activityWithSync, R$string.magic_bitmap_damaged);
                return;
            } else if (MagicFileUtil.checkMattingSelectImage(getActivity(), this.mUriGallery)) {
                BaseFragmentActivity activityWithSync2 = getActivityWithSync();
                if (activityWithSync2 == null) {
                    return;
                }
                MagicToast.showToast(activityWithSync2, activityWithSync2.getResources().getString(R$string.magic_matting_error_px));
                return;
            } else {
                try {
                    this.mImageSize = (int[]) getActivity().event(6);
                    Bitmap bitmap = MagicFileUtil.getBitmap(getActivity(), this.mUriGallery, 1200);
                    String path = this.mUriGallery.getPath();
                    this.mBgUrl = path;
                    this.mMemoryCache.put(path, bitmap);
                    getActivity().event(1, bitmap);
                    this.mAdapter.selectIndex(1);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        BaseFragmentActivity activityWithSync3 = getActivityWithSync();
        if (activityWithSync3 == null) {
            return;
        }
        MagicToast.showToast(activityWithSync3, activityWithSync3.getResources().getString(R$string.magic_cut_video_no_support_image_edit));
    }

    /* renamed from: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IMenu$VP {
        public static /* synthetic */ boolean $r8$lambda$ibJQQov1OW80WnYyTlkYQxorGIw(AnonymousClass1 anonymousClass1, RecyclerView recyclerView, View view, int i) {
            return anonymousClass1.lambda$initListData$0(recyclerView, view, i);
        }

        public AnonymousClass1() {
            MattingMenuPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void setAdapter(Adapter adapter) {
            ((MattingMenuFragment) MattingMenuPresenter.this.mView.get()).getContract().setAdapter(adapter);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void initListData() {
            MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
            mattingMenuPresenter.mAdapter = new BackgroundAdapter(((MattingMenuModel) mattingMenuPresenter.mModel).getContract().getBgData(), MattingMenuPresenter.this.getActivity());
            setAdapter(MattingMenuPresenter.this.mAdapter);
            MattingMenuPresenter.this.mAdapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
                public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                    return MattingMenuPresenter.AnonymousClass1.$r8$lambda$ibJQQov1OW80WnYyTlkYQxorGIw(MattingMenuPresenter.AnonymousClass1.this, recyclerView, view, i);
                }
            });
        }

        public /* synthetic */ boolean lambda$initListData$0(RecyclerView recyclerView, View view, int i) {
            scrollTo(i);
            BaseFragmentActivity activity = MattingMenuPresenter.this.getActivity();
            if (activity != null) {
                activity.event(8);
            }
            IconItem item = MattingMenuPresenter.this.mAdapter.getItem(i);
            if (item instanceof BackgroundIconItem) {
                MattingMenuPresenter.this.mBackgroundIndex = i;
                MagicLog.INSTANCE.startLog("matting_change_bg", "魔法抠图换背景");
                MattingMenuPresenter.this.execBackgroundItem((BackgroundIconItem) item, i);
                return false;
            } else if (!(item instanceof StrokeIconItem)) {
                return false;
            } else {
                MattingMenuPresenter.this.mCurrentStrokeIndex = i;
                MattingMenuPresenter.this.execStroke((StrokeIconItem) item, i);
                MattingMenuPresenter.this.mStrokeIndex = i;
                return false;
            }
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void onPaintColorSelected(int i) {
            StrokeIconItem strokeIconItem = (StrokeIconItem) MattingMenuPresenter.this.mAdapter.getItem(MattingMenuPresenter.this.mCurrentStrokeIndex);
            strokeIconItem.setColor(i);
            MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
            mattingMenuPresenter.execStroke(strokeIconItem, mattingMenuPresenter.mCurrentStrokeIndex);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void onPaintSizeSelected(int i) {
            StrokeIconItem strokeIconItem = (StrokeIconItem) MattingMenuPresenter.this.mAdapter.getItem(MattingMenuPresenter.this.mCurrentStrokeIndex);
            if (i <= 3) {
                strokeIconItem.setEmpty(true);
                i = 0;
            } else {
                strokeIconItem.setEmpty(false);
            }
            if (MattingMenuPresenter.this.mCurrentStrokeIndex < 5) {
                strokeIconItem.setStrokeWidth((int) ((i / 100.0f) * strokeIconItem.getmMaxW()));
            }
            if (MattingMenuPresenter.this.mCurrentStrokeIndex == 5) {
                strokeIconItem.setDistance((int) ((i / 100.0f) * strokeIconItem.getmMaxW()));
            }
            MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
            mattingMenuPresenter.execStroke(strokeIconItem, mattingMenuPresenter.mCurrentStrokeIndex);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void showPaintSelect(boolean z) {
            ((MattingMenuFragment) MattingMenuPresenter.this.mView.get()).getContract().showPaintSelect(z);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public BackgroundItem getBackgroundItem() {
            MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
            return new BackgroundItem(mattingMenuPresenter.mBackgroundIndex, mattingMenuPresenter.mBgUrl, MattingMenuPresenter.this.mUriGallery);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public Bitmap getBackgroundBitmap(String str) {
            MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
            mattingMenuPresenter.mImageSize = (int[]) mattingMenuPresenter.getActivity().event(6);
            return MattingMenuPresenter.this.getBitmap(str);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void scrollToPosition(int i) {
            ((MattingMenuFragment) MattingMenuPresenter.this.mView.get()).getContract().scrollToPosition(i);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void setStrokeWidthToProgress(int i) {
            ((MattingMenuFragment) MattingMenuPresenter.this.mView.get()).getContract().setStrokeWidthToProgress(i);
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void setSelectBackgroundIndex(int i) {
            if (MattingMenuPresenter.this.mCurrentModel == 1) {
                MattingMenuPresenter mattingMenuPresenter = MattingMenuPresenter.this;
                mattingMenuPresenter.mBackgroundIndex = i;
                mattingMenuPresenter.mAdapter.selectIndex(i);
                scrollToPosition(i);
            }
        }

        @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
        public void scrollTo(int i) {
            ((MattingMenuFragment) MattingMenuPresenter.this.mView.get()).getContract().scrollTo(i);
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final void execStroke(StrokeIconItem strokeIconItem, int i) {
        getContract().showPaintSelect(i < 6);
        getActivity().event(4, strokeIconItem);
        getContract().setStrokeWidthToProgress(strokeIconItem.getProgress());
    }

    public final void execBackgroundItem(BackgroundIconItem backgroundIconItem, int i) {
        this.mImageSize = (int[]) getActivity().event(6);
        if (i == 0) {
            this.mAdapter.selectIndex(i);
            this.mBgUrl = "first_bbg";
            getActivity().event(1, null);
        }
        if (i == 1) {
            ((MattingMenuFragment) this.mView.get()).startActivityForResult(MagicFileUtil.getSelectImageIntent(), 1001);
        } else if (i == 2) {
            this.mAdapter.selectIndex(i);
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    MattingMenuPresenter.$r8$lambda$tFPHzPaOtDVTM7F60ivRCLWr9K4(MattingMenuPresenter.this);
                }
            });
        } else if (TextUtils.isEmpty(backgroundIconItem.getResPath())) {
        } else {
            checkDownload(i, backgroundIconItem, i - 1);
        }
    }

    public /* synthetic */ void lambda$execBackgroundItem$0() {
        getActivity().event(1, getTransparent());
    }

    public final void checkDownload(final int i, final BackgroundIconItem backgroundIconItem, final int i2) {
        if (backgroundIconItem.isDownload()) {
            if (!MagicNetUtil.isNetworkAvailable(MagicUtils.getGalleryApp())) {
                MagicToast.showToast(MagicUtils.getGalleryApp(), R$string.magic_network_error);
                return;
            } else if (MagicNetUtil.IsMobileNetConnect(MagicUtils.getGalleryApp())) {
                BaseFragmentActivity activity = getActivity();
                if (activity == null) {
                    return;
                }
                NetworkConsider.consider(activity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z, boolean z2) {
                        MattingMenuPresenter.$r8$lambda$wjHEg8nWFbhGzqD7doQLfSoCdOU(MattingMenuPresenter.this, i, backgroundIconItem, i2, z, z2);
                    }
                });
                return;
            } else {
                fetchDownload(i, backgroundIconItem, i2);
                return;
            }
        }
        this.mAdapter.selectIndex(i);
        MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MattingMenuPresenter.m1034$r8$lambda$GV82NQfCV10b2zpFrklc69BiOo(MattingMenuPresenter.this, backgroundIconItem);
            }
        });
    }

    public /* synthetic */ void lambda$checkDownload$1(int i, BackgroundIconItem backgroundIconItem, int i2, boolean z, boolean z2) {
        if (z) {
            fetchDownload(i, backgroundIconItem, i2);
        }
    }

    public /* synthetic */ void lambda$checkDownload$2(BackgroundIconItem backgroundIconItem) {
        getActivity().event(1, getBitmap(backgroundIconItem.getResPath()));
        MagicLog.INSTANCE.endLog("matting_change_bg", "魔法抠图换背景");
    }

    /* renamed from: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements Request.Listener {
        public final /* synthetic */ BackgroundIconItem val$item;
        public final /* synthetic */ int val$position;

        public static /* synthetic */ void $r8$lambda$LLS4iORt9tZC73JynZbxrelAH70(AnonymousClass2 anonymousClass2, BackgroundIconItem backgroundIconItem) {
            anonymousClass2.lambda$onSuccess$0(backgroundIconItem);
        }

        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onFail() {
        }

        public AnonymousClass2(int i, BackgroundIconItem backgroundIconItem) {
            MattingMenuPresenter.this = r1;
            this.val$position = i;
            this.val$item = backgroundIconItem;
        }

        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onStart() {
            MattingMenuPresenter.this.mAdapter.notifyDownloading(this.val$position, true);
        }

        @Override // com.miui.gallery.net.fetch.Request.Listener
        public void onSuccess() {
            this.val$item.setDownload(true);
            MattingMenuPresenter.this.mAdapter.selectIndex(this.val$position);
            MattingMenuPresenter.this.mAdapter.notifyItem(this.val$position, false, false);
            final BackgroundIconItem backgroundIconItem = this.val$item;
            MagicThreadHandler.post(new Runnable() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuPresenter$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MattingMenuPresenter.AnonymousClass2.$r8$lambda$LLS4iORt9tZC73JynZbxrelAH70(MattingMenuPresenter.AnonymousClass2.this, backgroundIconItem);
                }
            });
        }

        public /* synthetic */ void lambda$onSuccess$0(BackgroundIconItem backgroundIconItem) {
            if (MattingMenuPresenter.this.getActivity() != null) {
                MattingMenuPresenter.this.getActivity().event(1, MattingMenuPresenter.this.getBitmap(backgroundIconItem.getResPath()));
                MagicLog.INSTANCE.endLog("matting_change_bg", "魔法抠图换背景");
            }
        }
    }

    public final void fetchDownload(int i, BackgroundIconItem backgroundIconItem, int i2) {
        MattingRequest mattingRequest = new MattingRequest(backgroundIconItem.getResKey(), backgroundIconItem.getResId());
        mattingRequest.setListener(new AnonymousClass2(i, backgroundIconItem));
        MattingResourceFetcher.INSTANCE.fetch(mattingRequest);
    }

    public final Bitmap getBitmap(String str) {
        return getBitmap(str, null);
    }

    public final Bitmap getTransparent() {
        Bitmap bitmap = this.mMemoryCache.get("Transparent");
        if (bitmap == null) {
            Bitmap decodeResource = BitmapFactory.decodeResource(getActivity().getResources(), R$drawable.magic_matting_transparent);
            int[] iArr = this.mImageSize;
            bitmap = MagicFileUtil.getBitmapBackground(decodeResource, iArr[0], iArr[1]);
            this.mMemoryCache.put("Transparent", bitmap);
        }
        this.mBgUrl = "Transparent";
        return bitmap;
    }

    public final Bitmap getBitmap(String str, Bitmap bitmap) {
        if (TextUtils.isEmpty(str) || str.equals("first_bbg")) {
            return null;
        }
        this.mBgUrl = str;
        Bitmap bitmap2 = this.mMemoryCache.get(str);
        if (bitmap2 != null || bitmap != null) {
            if (bitmap == null) {
                return bitmap2;
            }
            int[] iArr = this.mImageSize;
            Bitmap zoomBitmap = BitmapUtils.zoomBitmap(bitmap, iArr[0], iArr[1], true);
            this.mMemoryCache.put(str, zoomBitmap);
            return zoomBitmap;
        }
        Bitmap bitmapFromFile = BitmapUtils.getBitmapFromFile(new File(str), 3000, 4000);
        if (bitmapFromFile == null) {
            return null;
        }
        int[] iArr2 = this.mImageSize;
        Bitmap bitmapBackground = MagicFileUtil.getBitmapBackground(bitmapFromFile, iArr2[0], iArr2[1]);
        this.mMemoryCache.put(str, bitmapBackground);
        return bitmapBackground;
    }
}
