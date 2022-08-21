package com.miui.gallery.ui.photoPage.bars.menuitem;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.lifecycle.LifecycleOwner;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.FavoriteInfo;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.miui.gallery.view.menu.IMenuItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public class Favorite extends BaseMenuItemDelegate {
    public BaseDataSet mDataSet;
    public FavoritesManager mFavoritesManager;
    public final ConcurrentMap<Long, FilterResult> mFilterResultMap;
    public Handler mHandler;
    public long mPreKey;
    public final ConcurrentMap<Long, FavoritesManager.QueryInfo> mQueryFinishedMap;

    /* loaded from: classes2.dex */
    public interface ISyncCallback {
        void onFavoriteStateQueryFinished(boolean z);
    }

    public static Favorite instance(IMenuItem iMenuItem) {
        return new Favorite(iMenuItem);
    }

    public Favorite(IMenuItem iMenuItem) {
        super(iMenuItem);
        this.mFilterResultMap = new ConcurrentHashMap();
        this.mQueryFinishedMap = new ConcurrentHashMap();
        this.mPreKey = -1L;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate
    public void doInitFunction() {
        this.mFavoritesManager = new FavoritesManager();
        this.mHandler = new FavoritesHandler(this.mContext, this.mFavoritesManager);
        BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack = this.mConfigMenuCallBack;
        if (iConfigMenuCallBack != null) {
            iConfigMenuCallBack.setFavoritesManager(this.mFavoritesManager);
        }
        super.doInitFunction();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mFavoritesManager.toggle(baseDataItem, this.mDataProvider.getFieldData().mCurrent.getPosition());
        TrackController.trackClick("403.11.5.1.11161", AutoTracking.getRef());
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onDestroy(LifecycleOwner lifecycleOwner) {
        if (!this.isFunctionInit) {
            return;
        }
        this.mFavoritesManager.onDestroy();
    }

    public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
        if (!this.isFunctionInit || baseDataItem == null) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "onFilterFinished => %d", Long.valueOf(baseDataItem.getKey()));
        this.mFavoritesManager.onFilterFinished(baseDataItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.BaseMenuItemDelegate, com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void prepareMenuData(BaseDataItem baseDataItem, FilterResult filterResult) {
        super.prepareMenuData(baseDataItem, filterResult);
        if (baseDataItem == null || filterResult == null || !filterResult.getSupport() || this.mFavoritesManager == null) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "prepareMenuData => %d", Long.valueOf(baseDataItem.getKey()));
        this.mFavoritesManager.saveFilterResult(baseDataItem, filterResult);
        this.mFavoritesManager.prepareMenuData(baseDataItem, filterResult, null);
    }

    /* loaded from: classes2.dex */
    public static class FavoritesHandler extends Handler {
        public final WeakReference<GalleryActivity> mActivityRef;
        public final WeakReference<FavoritesManager> mFavoritesManagerRef;

        public FavoritesHandler(GalleryActivity galleryActivity, FavoritesManager favoritesManager) {
            this.mActivityRef = new WeakReference<>(galleryActivity);
            this.mFavoritesManagerRef = new WeakReference<>(favoritesManager);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            GalleryActivity galleryActivity = this.mActivityRef.get();
            FavoritesManager favoritesManager = this.mFavoritesManagerRef.get();
            if (galleryActivity == null || galleryActivity.isFinishing() || galleryActivity.isDestroyed() || favoritesManager == null) {
                removeCallbacksAndMessages(null);
                return;
            }
            int i = message.what;
            if (i == 1) {
                Bundle bundle = (Bundle) message.obj;
                boolean z = bundle.getBoolean("isChecked");
                favoritesManager.mIsToggling = true;
                favoritesManager.updateData(!z, bundle.getInt("currentIndex"), (BaseDataItem) bundle.getSerializable("current"));
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "handleMessage => wrong msg => msg.what = %d", Integer.valueOf(i));
        }
    }

    /* loaded from: classes2.dex */
    public static class FunctionMap implements Function<FavoritesManager.QueryInfo, FavoritesManager.QueryInfo> {
        public WeakReference<Favorite> mFavorite;

        public FunctionMap(Favorite favorite) {
            this.mFavorite = new WeakReference<>(favorite);
        }

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public FavoritesManager.QueryInfo mo2564apply(FavoritesManager.QueryInfo queryInfo) throws Exception {
            WeakReference<Favorite> weakReference = this.mFavorite;
            if (weakReference != null && weakReference.get() != null && queryInfo != null && queryInfo.item != null && queryInfo.result != null) {
                Favorite favorite = this.mFavorite.get();
                boolean z = false;
                try {
                    z = favorite.mDataProvider.getFieldData().isFromCamera;
                } catch (Exception e) {
                    DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Favorite", e);
                }
                boolean isFavorite = queryInfo.item.queryFavoriteInfo(!z).isFavorite();
                queryInfo.result.setFavorite(isFavorite);
                favorite.mQueryFinishedMap.put(Long.valueOf(queryInfo.item.getKey()), queryInfo);
                DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "saveFavoriteState %d isFavorite = %b", Long.valueOf(queryInfo.item.getKey()), Boolean.valueOf(isFavorite));
            }
            return queryInfo;
        }
    }

    /* loaded from: classes2.dex */
    public class FavoritesManager {
        public Disposable mDisposable;
        public Executor mExecutorService;
        public HandleFavoriteCallBack mFavoriteCallBack;
        public FunctionMap mFunctionMap;
        public boolean mIsToggling;
        public PublishProcessor<QueryInfo> mPublishProcessor;
        public int mRetryCount;
        public Runnable mRetryTask;

        public static /* synthetic */ void $r8$lambda$ZA18tXX5M6bzZjgBsNs_CxeUPrs(FavoritesManager favoritesManager, BaseDataItem baseDataItem, boolean z) {
            favoritesManager.lambda$prepareMenuDataWithRefresh$1(baseDataItem, z);
        }

        public static /* synthetic */ void $r8$lambda$Zgo7n1yxXKdT1SdtkvzTJH0P1Qg(FavoritesManager favoritesManager, boolean z, int i, BaseDataItem baseDataItem) {
            favoritesManager.lambda$toggle$0(z, i, baseDataItem);
        }

        public FavoritesManager() {
            Favorite.this = r1;
        }

        public static /* synthetic */ int access$904(FavoritesManager favoritesManager) {
            int i = favoritesManager.mRetryCount + 1;
            favoritesManager.mRetryCount = i;
            return i;
        }

        public void toggle(final BaseDataItem baseDataItem, final int i) {
            final boolean favorite;
            Favorite favorite2 = Favorite.this;
            if (favorite2.mFragment == null || baseDataItem == null || this.mIsToggling) {
                return;
            }
            if (favorite2.mDataProvider.isProcessingMedia(baseDataItem)) {
                ToastUtils.makeText(Favorite.this.mContext, (int) R.string.operate_processing_file_error);
                return;
            }
            Long valueOf = Long.valueOf(baseDataItem.getKey());
            FilterResult filterResult = (FilterResult) Favorite.this.mFilterResultMap.get(valueOf);
            if (filterResult != null) {
                if (valueOf.longValue() != Favorite.this.mPreKey) {
                    FavoriteInfo favoriteInfo = baseDataItem.getFavoriteInfo();
                    if (Favorite.this.mQueryFinishedMap != null && !Favorite.this.mQueryFinishedMap.containsKey(valueOf) && baseDataItem.isNeedQueryFavoriteInfo()) {
                        DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Favorite", "toggle => item [%s] need queryFavoriteInfo !", valueOf);
                    }
                    favorite = favoriteInfo.isFavorite();
                    Favorite.this.mPreKey = valueOf.longValue();
                } else {
                    favorite = filterResult.getFavorite();
                }
                filterResult.setFavorite(!favorite);
                refreshUI(!favorite, true);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isChecked", favorite);
                bundle.putInt("currentIndex", i);
                bundle.putSerializable("current", baseDataItem);
                Message obtainMessage = Favorite.this.mHandler.obtainMessage(1, bundle);
                Favorite.this.mHandler.removeMessages(1);
                Favorite.this.mHandler.sendMessageDelayed(obtainMessage, 500L);
                if (!Favorite.this.mDataProvider.getFieldData().isFromCamera) {
                    return;
                }
                this.mRetryTask = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Favorite$FavoritesManager$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Favorite.FavoritesManager.$r8$lambda$Zgo7n1yxXKdT1SdtkvzTJH0P1Qg(Favorite.FavoritesManager.this, favorite, i, baseDataItem);
                    }
                };
                return;
            }
            DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Favorite", "filterResult == null ！");
        }

        public /* synthetic */ void lambda$toggle$0(boolean z, int i, BaseDataItem baseDataItem) {
            updateData(!z, i, baseDataItem);
        }

        public void updateSet(BaseDataSet baseDataSet) {
            Favorite.this.mDataSet = baseDataSet;
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "updateSet => dataSet = %s", baseDataSet == null ? "null" : Integer.valueOf(baseDataSet.hashCode()));
            checkRefreshIfNeed();
        }

        public final void checkRefreshIfNeed() {
            BaseDataItem item = Favorite.this.mDataSet.getItem(null, Favorite.this.mDataProvider.getFieldData().mCurrent.getPosition());
            if (item == null || item.getFavoriteInfo() == null) {
                return;
            }
            prepareMenuDataWithRefresh(item);
        }

        public final void release() {
            Disposable disposable = this.mDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mDisposable.dispose();
            }
            Favorite.this.mFilterResultMap.clear();
            Favorite.this.mQueryFinishedMap.clear();
            Favorite.this.mHandler.removeCallbacksAndMessages(null);
        }

        /* loaded from: classes2.dex */
        public class QueryInfo {
            public ISyncCallback callback;
            public BaseDataItem item;
            public FilterResult result;

            public QueryInfo(BaseDataItem baseDataItem, FilterResult filterResult, ISyncCallback iSyncCallback) {
                FavoritesManager.this = r1;
                this.item = baseDataItem;
                this.result = filterResult;
                this.callback = iSyncCallback;
            }
        }

        public void onFilterFinished(BaseDataItem baseDataItem) {
            QueryInfo queryInfo = (QueryInfo) Favorite.this.mQueryFinishedMap.get(Long.valueOf(baseDataItem.getKey()));
            if (queryInfo == null) {
                prepareMenuDataWithRefresh(baseDataItem);
            } else if (Favorite.this.isChecked() == queryInfo.result.getFavorite()) {
            } else {
                DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "state dif refresh ui =>");
                refreshUI(queryInfo.result.getFavorite(), false);
            }
        }

        public void saveFilterResult(BaseDataItem baseDataItem, FilterResult filterResult) {
            Favorite.this.mFilterResultMap.put(Long.valueOf(baseDataItem.getKey()), filterResult);
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "saveFilterResult => %d", Long.valueOf(baseDataItem.getKey()));
        }

        public final void prepareMenuDataWithRefresh(final BaseDataItem baseDataItem) {
            if (baseDataItem == null) {
                return;
            }
            FilterResult filterResult = (FilterResult) Favorite.this.mFilterResultMap.get(Long.valueOf(baseDataItem.getKey()));
            if (filterResult == null) {
                DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Favorite", "prepareMenuData after onFilterFinished ！！！ %d", Long.valueOf(baseDataItem.getKey()));
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "prepareMenuDataWithRefresh => %d", Long.valueOf(baseDataItem.getKey()));
            prepareMenuData(baseDataItem, filterResult, new ISyncCallback() { // from class: com.miui.gallery.ui.photoPage.bars.menuitem.Favorite$FavoritesManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.photoPage.bars.menuitem.Favorite.ISyncCallback
                public final void onFavoriteStateQueryFinished(boolean z) {
                    Favorite.FavoritesManager.$r8$lambda$ZA18tXX5M6bzZjgBsNs_CxeUPrs(Favorite.FavoritesManager.this, baseDataItem, z);
                }
            });
        }

        public /* synthetic */ void lambda$prepareMenuDataWithRefresh$1(BaseDataItem baseDataItem, boolean z) {
            IDataProvider iDataProvider;
            if (baseDataItem == null || (iDataProvider = Favorite.this.mDataProvider) == null || iDataProvider.getFieldData().mCurrent.getDataItem() == null || baseDataItem.getKey() != Favorite.this.mDataProvider.getFieldData().mCurrent.getDataItem().getKey()) {
                return;
            }
            refreshUI(z, false);
        }

        public final void prepareMenuData(BaseDataItem baseDataItem, FilterResult filterResult, ISyncCallback iSyncCallback) {
            if (this.mExecutorService == null) {
                this.mExecutorService = ThreadManager.getExecutor(BaiduSceneResult.BLACK_WHITE);
            }
            if (this.mPublishProcessor == null) {
                this.mPublishProcessor = PublishProcessor.create();
            }
            if (this.mFunctionMap == null) {
                this.mFunctionMap = new FunctionMap(Favorite.this);
            }
            Disposable disposable = this.mDisposable;
            if (disposable == null || disposable.isDisposed()) {
                subscribe();
            }
            this.mPublishProcessor.onNext(new QueryInfo(baseDataItem, filterResult, iSyncCallback));
        }

        public final void subscribe() {
            this.mDisposable = this.mPublishProcessor.observeOn(Schedulers.from(this.mExecutorService)).map(this.mFunctionMap).observeOn(AndroidSchedulers.mainThread()).subscribe(Favorite$FavoritesManager$$ExternalSyntheticLambda1.INSTANCE);
        }

        public static /* synthetic */ void lambda$subscribe$2(QueryInfo queryInfo) throws Exception {
            ISyncCallback iSyncCallback;
            FilterResult filterResult;
            if (queryInfo == null || (iSyncCallback = queryInfo.callback) == null || (filterResult = queryInfo.result) == null) {
                return;
            }
            iSyncCallback.onFavoriteStateQueryFinished(filterResult.getFavorite());
        }

        public void refreshUI(boolean z, boolean z2) {
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "refreshUI => checked = " + z);
            Favorite.this.setChecked(z);
            Favorite.this.setIconId(!z ? R.drawable.avd_likeanimation : R.drawable.avd_anilikeanimation);
            if (BaseBuildUtil.isLowRamDevice() || !z2) {
                return;
            }
            exeAnim();
        }

        public final void exeAnim() {
            Favorite favorite = Favorite.this;
            if (favorite.mFragment == null || favorite.getIconId() == 0) {
                return;
            }
            Drawable drawable = Favorite.this.mFragment.getResources().getDrawable(Favorite.this.getIconId());
            if (!(drawable instanceof Animatable)) {
                return;
            }
            Animatable animatable = (Animatable) drawable;
            if (animatable.isRunning()) {
                return;
            }
            animatable.start();
        }

        public final void updateData(boolean z, int i, BaseDataItem baseDataItem) {
            if (Favorite.this.mDataSet == null) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "updateData => check = %b currentIndex = %d", Boolean.valueOf(z), Integer.valueOf(i));
            if (this.mFavoriteCallBack == null) {
                this.mFavoriteCallBack = new HandleFavoriteCallBack(this);
            }
            this.mFavoriteCallBack.setCurrentBaseDataItem(baseDataItem);
            if (z) {
                Favorite.this.mDataSet.addToFavorites(Favorite.this.mContext, i, this.mFavoriteCallBack.setIsAddFavorite(true));
                Favorite.this.mOwner.postRecordCountEvent("favorites", "add_to_favorites");
                return;
            }
            Favorite.this.mDataSet.removeFromFavorites(Favorite.this.mContext, i, this.mFavoriteCallBack.setIsAddFavorite(false));
            Favorite.this.mOwner.postRecordCountEvent("favorites", "remove_from_favorites");
        }

        /* loaded from: classes2.dex */
        public class HandleFavoriteCallBack implements MediaAndAlbumOperations.OnCompleteListener {
            public boolean isAddFavorite;
            public BaseDataItem mBaseDataItem;
            public WeakReference<FavoritesManager> mManager;

            public HandleFavoriteCallBack(FavoritesManager favoritesManager) {
                FavoritesManager.this = r1;
                this.mManager = new WeakReference<>(favoritesManager);
            }

            public void setCurrentBaseDataItem(BaseDataItem baseDataItem) {
                this.mBaseDataItem = baseDataItem;
            }

            public HandleFavoriteCallBack setIsAddFavorite(boolean z) {
                this.isAddFavorite = z;
                return this;
            }

            @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnCompleteListener
            public void onComplete(long[] jArr) {
                Resources resources;
                int i;
                if (!isValidContext() || this.mBaseDataItem == null) {
                    return;
                }
                FavoritesManager favoritesManager = this.mManager.get();
                BaseActivity baseActivity = (BaseActivity) Favorite.this.mFragment.getActivity();
                if (jArr != null && jArr.length != 0 && jArr[0] > 0) {
                    favoritesManager.prepareMenuDataWithRefresh(this.mBaseDataItem);
                    if (this.isAddFavorite && GalleryPreferences.Favorites.isFirstTimeAddToFavorites()) {
                        ToastUtils.makeText(baseActivity, (int) R.string.added_to_favorites_tip);
                    }
                    if (this.isAddFavorite) {
                        resources = baseActivity.getResources();
                        i = R.string.added_to_favorites_desc;
                    } else {
                        resources = baseActivity.getResources();
                        i = R.string.removed_from_favorites_desc;
                    }
                    resources.getString(i);
                    favoritesManager.mIsToggling = false;
                    return;
                }
                DefaultLogger.e("PhotoPageFragment_MenuManager_MenuItem_Favorite", "add or remove Favorite error ! result = [%d]", Long.valueOf((jArr == null || jArr.length == 0) ? 100L : jArr[0]));
                if (Favorite.this.mDataProvider.getFieldData().isFromCamera && favoritesManager.mRetryTask != null && FavoritesManager.access$904(favoritesManager) <= 3) {
                    DefaultLogger.d("PhotoPageFragment_MenuManager_MenuItem_Favorite", "onComplete => from camera and failed, retry(%d)", Integer.valueOf(favoritesManager.mRetryCount));
                    com.miui.gallery.util.concurrent.ThreadManager.getMainHandler().removeCallbacks(favoritesManager.mRetryTask);
                    com.miui.gallery.util.concurrent.ThreadManager.getMainHandler().postDelayed(favoritesManager.mRetryTask, 500L);
                    return;
                }
                favoritesManager.refreshUI(!this.isAddFavorite, true);
                ToastUtils.makeText(baseActivity, this.isAddFavorite ? R.string.add_to_favorites_failed : R.string.remove_from_favorites_failed);
                favoritesManager.mIsToggling = false;
            }

            public final boolean isValidContext() {
                GalleryFragment galleryFragment = Favorite.this.mFragment;
                return galleryFragment != null && galleryFragment.getActivity() != null && !Favorite.this.mFragment.getActivity().isDestroyed() && !Favorite.this.mFragment.getActivity().isFinishing();
            }

            public void release() {
                WeakReference<FavoritesManager> weakReference = this.mManager;
                if (weakReference != null) {
                    weakReference.clear();
                    this.mManager = null;
                }
            }
        }

        public void onDestroy() {
            Favorite.this.mFavoritesManager.release();
            HandleFavoriteCallBack handleFavoriteCallBack = this.mFavoriteCallBack;
            if (handleFavoriteCallBack != null) {
                handleFavoriteCallBack.release();
            }
            Favorite.this.mDataSet = null;
            if (this.mRetryTask != null) {
                com.miui.gallery.util.concurrent.ThreadManager.getMainHandler().removeCallbacks(this.mRetryTask);
                this.mRetryTask = null;
            }
        }
    }
}
