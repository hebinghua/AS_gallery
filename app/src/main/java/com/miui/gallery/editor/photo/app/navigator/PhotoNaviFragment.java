package com.miui.gallery.editor.photo.app.navigator;

import android.animation.Animator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.miui.gallery.R;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.editor.photo.app.AbstractNaviFragment;
import com.miui.gallery.editor.photo.app.adjust2.Adjust2LibraryLoaderHelper;
import com.miui.gallery.editor.photo.app.remover2.sdk.Remover2LibraryLoaderHelper;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyLibraryLoaderHelper;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import miuix.popupwidget.widget.GuidePopupWindow;

/* loaded from: classes2.dex */
public class PhotoNaviFragment extends AbstractNaviFragment {
    public NaviAdapter mAdapter;
    public AdjustNaviManager mAdjustNaviManager;
    public Context mContext;
    public HashMap<Long, AbstractNaviFragment.NavigatorData> mDownloadSoLibraryMap;
    public Disposable mGetLibraryStateDisposable;
    public GuidePopupWindow mGuideView;
    public SimpleRecyclerView mNavigator;
    public OnItemClickListener mOnItemSelectedListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.3
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            AbstractNaviFragment.NavigatorData navigatorData = (AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.getNaviData().get(i);
            if (navigatorData.effect != Effect.SKY || !PhotoNaviFragment.this.mSkyNaviManager.onNaviItemClick(i, SkyLibraryLoaderHelper.INSTANCE)) {
                Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect = navigatorData.effect;
                if (((effect == Effect.ADJUST2 || effect == Effect.BEAUTIFY2) && PhotoNaviFragment.this.mAdjustNaviManager.onNaviItemClick(i)) || PhotoNaviFragment.this.mRemover2NaviManager.onNaviItemClick(i)) {
                    return true;
                }
                PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                return true;
            }
            return true;
        }
    };
    public OnPreparePhotoNaviFragmentListener mPrepareListener;
    public Remover2NaviManager mRemover2NaviManager;
    public CommonNaviManager mSkyNaviManager;

    /* loaded from: classes2.dex */
    public interface OnPreparePhotoNaviFragmentListener {
        boolean isBeautifyApplied();
    }

    @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSkyNaviManager = new CommonNaviManager();
        this.mAdjustNaviManager = new AdjustNaviManager();
        this.mRemover2NaviManager = new Remover2NaviManager();
    }

    @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment
    public View onCreateNavigator(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.photo_editor_nav_menu, viewGroup, false);
    }

    @Override // com.miui.gallery.editor.photo.app.AbstractNaviFragment
    public void onNavigatorCreated(View view, Bundle bundle) {
        super.onNavigatorCreated(view, bundle);
        this.mContext = getActivity();
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mNavigator = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        NaviAdapter naviAdapter = new NaviAdapter(getNaviData(), this.mPrepareListener);
        this.mAdapter = naviAdapter;
        this.mNavigator.setAdapter(naviAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemSelectedListener);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_editor_nav_item_start);
        this.mNavigator.addItemDecoration(new EditorBlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.photo_editor_nav_item_horizontal_interval), 0, 0));
        syncLibraryState();
    }

    public final void syncLibraryState() {
        int i;
        List<AbstractNaviFragment.NavigatorData> naviData = getNaviData();
        if (naviData != null && naviData.size() > 0) {
            this.mDownloadSoLibraryMap = new HashMap<>(naviData.size());
            for (AbstractNaviFragment.NavigatorData navigatorData : naviData) {
                if (navigatorData.effect != Effect.BEAUTIFY2 && (i = navigatorData.downloadState) != 17 && i != 0) {
                    this.mDownloadSoLibraryMap.put(getLibraryId(navigatorData), navigatorData);
                }
            }
        }
        HashMap<Long, AbstractNaviFragment.NavigatorData> hashMap = this.mDownloadSoLibraryMap;
        if (hashMap == null || hashMap.size() == 0) {
            return;
        }
        DefaultLogger.d("PhotoNaviFragment", "check library size = %d  , detail = %s", Integer.valueOf(this.mDownloadSoLibraryMap.size()), this.mDownloadSoLibraryMap.toString());
        this.mGetLibraryStateDisposable = Observable.interval(3000L, 3000L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.1
            @Override // io.reactivex.functions.Consumer
            public void accept(Long l) throws Exception {
                if (l.longValue() > 60 || PhotoNaviFragment.this.mDownloadSoLibraryMap.isEmpty() || BaseNetworkUtils.isActiveNetworkMetered()) {
                    DefaultLogger.d("PhotoNaviFragment", "receive interval  map is empty  %b  ， isInitialized = %b", Boolean.valueOf(PhotoNaviFragment.this.mDownloadSoLibraryMap.isEmpty()), Boolean.valueOf(LibraryManager.getInstance().isInitialized()));
                    PhotoNaviFragment.this.clearGetLibraryStateDisposable();
                    return;
                }
                Iterator it = PhotoNaviFragment.this.mDownloadSoLibraryMap.keySet().iterator();
                while (it.hasNext()) {
                    Long l2 = (Long) it.next();
                    Library.LibraryStatus libraryIsDownload = LibraryManager.getInstance().getLibraryIsDownload(l2.longValue());
                    if (libraryIsDownload != null && (libraryIsDownload == Library.LibraryStatus.STATE_AVAILABLE || libraryIsDownload == Library.LibraryStatus.STATE_LOADED)) {
                        AbstractNaviFragment.NavigatorData navigatorData2 = (AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.mDownloadSoLibraryMap.get(l2);
                        if (navigatorData2 != null) {
                            DefaultLogger.d("PhotoNaviFragment", "navigatorData  before %s = ", navigatorData2.toString());
                            navigatorData2.downloadState = 17;
                            DefaultLogger.d("PhotoNaviFragment", "navigatorData  after  %s = ", navigatorData2.toString());
                            int indexOf = PhotoNaviFragment.this.getNaviData().indexOf(navigatorData2);
                            if (indexOf < 0 || indexOf >= PhotoNaviFragment.this.mAdapter.mNavigatorData.size()) {
                                return;
                            }
                            PhotoNaviFragment.this.mAdapter.notifyItemChanged(indexOf);
                            if (navigatorData2.effect == Effect.ADJUST2) {
                                int findEffectIndex = PhotoNaviFragment.this.findEffectIndex(Effect.BEAUTIFY2);
                                if (findEffectIndex == -1) {
                                    return;
                                }
                                ((AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.mAdapter.mNavigatorData.get(findEffectIndex)).downloadState = 17;
                                PhotoNaviFragment.this.mAdapter.notifyItemChanged(findEffectIndex);
                            }
                            it.remove();
                        } else {
                            continue;
                        }
                    }
                }
                if (!PhotoNaviFragment.this.mDownloadSoLibraryMap.isEmpty()) {
                    return;
                }
                PhotoNaviFragment.this.clearGetLibraryStateDisposable();
            }
        }, new Consumer<Throwable>() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                DefaultLogger.d("PhotoNaviFragment", "error -> " + th.toString());
            }
        });
    }

    public final void clearGetLibraryStateDisposable() {
        Disposable disposable = this.mGetLibraryStateDisposable;
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                this.mGetLibraryStateDisposable.dispose();
            }
            this.mGetLibraryStateDisposable = null;
        }
    }

    public final int findEffectIndex(Effect effect) {
        List<AbstractNaviFragment.NavigatorData> naviData = getNaviData();
        if (naviData == null || naviData.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < naviData.size(); i++) {
            if (naviData.get(i).effect == effect) {
                return i;
            }
        }
        return -1;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        clearGetLibraryStateDisposable();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        GuidePopupWindow guidePopupWindow = this.mGuideView;
        if (guidePopupWindow != null) {
            guidePopupWindow.dismiss();
        }
        CommonNaviManager commonNaviManager = this.mSkyNaviManager;
        if (commonNaviManager != null) {
            commonNaviManager.onDestroy(SkyLibraryLoaderHelper.INSTANCE);
        }
        AdjustNaviManager adjustNaviManager = this.mAdjustNaviManager;
        if (adjustNaviManager != null) {
            adjustNaviManager.onDestroy();
        }
        Remover2NaviManager remover2NaviManager = this.mRemover2NaviManager;
        if (remover2NaviManager != null) {
            remover2NaviManager.onDestroy();
        }
    }

    public void showItemGuideView(Effect effect, int i, int i2) {
        RecyclerView.ViewHolder findItemViewHolderByEffect = findItemViewHolderByEffect(effect);
        if (findItemViewHolderByEffect != null) {
            GuidePopupWindow guidePopupWindow = new GuidePopupWindow(this.mContext);
            this.mGuideView = guidePopupWindow;
            guidePopupWindow.setArrowMode(i);
            this.mGuideView.setGuideText(i2);
            this.mGuideView.show(((NavigatorHolder) findItemViewHolderByEffect).getImageView(), 0, getActivity().getResources().getDimensionPixelSize(R.dimen.photo_editor_nav_sky_guide_hint_offsetY), true);
        }
    }

    public final RecyclerView.ViewHolder findItemViewHolderByEffect(Effect effect) {
        if (effect == null) {
            return null;
        }
        for (int i = 0; i < getNaviData().size(); i++) {
            if (getNaviData().get(i).isSelectedEffect(effect)) {
                return this.mNavigator.findViewHolderForLayoutPosition(i);
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static final class NaviAdapter extends Adapter<NavigatorHolder> {
        public List<AbstractNaviFragment.NavigatorData> mNavigatorData;
        public OnPreparePhotoNaviFragmentListener mOnPrepareListener;

        public NaviAdapter(List<AbstractNaviFragment.NavigatorData> list, OnPreparePhotoNaviFragmentListener onPreparePhotoNaviFragmentListener) {
            this.mNavigatorData = list;
            this.mOnPrepareListener = onPreparePhotoNaviFragmentListener;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public NavigatorHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new NavigatorHolder(getInflater().inflate(R.layout.photo_editor_navigator_item, viewGroup, false));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(NavigatorHolder navigatorHolder, int i) {
            super.onBindViewHolder((NaviAdapter) navigatorHolder, i);
            AbstractNaviFragment.NavigatorData navigatorData = this.mNavigatorData.get(i);
            OnPreparePhotoNaviFragmentListener onPreparePhotoNaviFragmentListener = this.mOnPrepareListener;
            navigatorHolder.bind(navigatorData, i, Boolean.valueOf(onPreparePhotoNaviFragmentListener == null ? false : onPreparePhotoNaviFragmentListener.isBeautifyApplied()));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mNavigatorData.size();
        }
    }

    /* loaded from: classes2.dex */
    public static final class NavigatorHolder extends RecyclerView.ViewHolder {
        public Animator.AnimatorListener mAnimatorListener;
        public Context mContext;
        public ImageView mImageAnimView;
        public ImageView mImageView;
        public boolean mIsPlay;
        public TextView mView;

        public NavigatorHolder(View view) {
            super(view);
            this.mAnimatorListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.NavigatorHolder.2
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    NavigatorHolder.this.itemView.setEnabled(false);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    NavigatorHolder.this.itemView.setEnabled(true);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    NavigatorHolder.this.itemView.setEnabled(true);
                }
            };
            this.mContext = view.getContext();
            this.mView = (TextView) view.findViewById(R.id.label);
            this.mImageView = (ImageView) view.findViewById(R.id.img_nav);
            this.mImageAnimView = (ImageView) view.findViewById(R.id.img_nav_anim);
            FolmeUtil.setDefaultTouchAnim(view, this.mImageView, null, false, true, true);
        }

        public void bind(AbstractNaviFragment.NavigatorData navigatorData, int i, Boolean bool) {
            this.mView.setText(navigatorData.downloadState == 18 ? R.string.loading : navigatorData.name);
            this.mImageView.setImageResource(navigatorData.icon);
            this.mImageView.setBackgroundResource(R.drawable.editor_circle_item_background);
            this.mImageView.setSelected(false);
            this.mImageAnimView.setVisibility(8);
            this.mImageView.setColorFilter(-1, PorterDuff.Mode.SRC_IN);
            if (i != 0) {
                return;
            }
            this.itemView.setContentDescription(bool.booleanValue() ? this.mContext.getResources().getString(R.string.photo_editor_talkback_beautity_applied) : this.mContext.getResources().getString(R.string.photo_editor_talkback_beautity_unapplied));
            if (!bool.booleanValue()) {
                this.mIsPlay = true;
                return;
            }
            this.mImageView.setColorFilter(-16777216, PorterDuff.Mode.SRC_IN);
            this.mImageView.setSelected(true);
            if (!this.mIsPlay) {
                return;
            }
            this.mImageView.setImageResource(R.color.transparent);
            final LottieDrawable lottieDrawable = new LottieDrawable();
            this.mImageAnimView.setImageDrawable(lottieDrawable);
            LottieComposition.Factory.fromRawFile(this.mContext, navigatorData.iconJson, new OnCompositionLoadedListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.NavigatorHolder.1
                @Override // com.airbnb.lottie.OnCompositionLoadedListener
                public void onCompositionLoaded(LottieComposition lottieComposition) {
                    NavigatorHolder.this.mImageAnimView.setVisibility(0);
                    lottieDrawable.setComposition(lottieComposition);
                    lottieDrawable.playAnimation();
                    NavigatorHolder.this.mIsPlay = false;
                }
            });
            lottieDrawable.addAnimatorListener(this.mAnimatorListener);
        }

        public ImageView getImageView() {
            return this.mImageView;
        }
    }

    /* loaded from: classes2.dex */
    public class CommonNaviManager {
        public LibraryLoaderHelper.DownloadStateListener mDownloadStartListener;

        public CommonNaviManager() {
        }

        public <T extends LibraryLoaderHelper> boolean onNaviItemClick(final int i, T t) {
            final AbstractNaviFragment.NavigatorData navigatorData = (AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.getNaviData().get(i);
            if (this.mDownloadStartListener == null) {
                LibraryLoaderHelper.DownloadStateListener downloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.CommonNaviManager.1
                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        navigatorData.downloadState = 18;
                        PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        if (z) {
                            navigatorData.downloadState = 0;
                        } else {
                            navigatorData.downloadState = 20;
                        }
                        PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                    }
                };
                this.mDownloadStartListener = downloadStateListener;
                t.addDownloadStateListener(downloadStateListener);
            }
            int i2 = navigatorData.downloadState;
            if (i2 == 19 || i2 == 20) {
                if (t.checkHasDownload()) {
                    navigatorData.downloadState = 0;
                    PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                    return true;
                } else if (t.checkAbleOrDownload(PhotoNaviFragment.this.getActivity())) {
                    PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                }
            } else if (i2 == 0 || i2 == 17) {
                navigatorData.downloadState = 17;
                PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
            }
            return true;
        }

        public <T extends LibraryLoaderHelper> void onDestroy(T t) {
            t.removeDownloadStateListener(this.mDownloadStartListener);
        }
    }

    /* loaded from: classes2.dex */
    public class AdjustNaviManager {
        public LibraryLoaderHelper.DownloadStateListener mDownloadStartListener;

        public AdjustNaviManager() {
        }

        public boolean onNaviItemClick(final int i) {
            final AbstractNaviFragment.NavigatorData navigatorData = (AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.getNaviData().get(i);
            Effect<SdkProvider<BeautifyData, AbstractEffectFragment>> effect = navigatorData.effect;
            if (effect == Effect.ADJUST2 || effect == Effect.BEAUTIFY2) {
                if (this.mDownloadStartListener == null) {
                    LibraryLoaderHelper.DownloadStateListener downloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.AdjustNaviManager.1
                        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                        public void onDownloading() {
                            navigatorData.downloadState = 18;
                            PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                        }

                        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                        public void onFinish(boolean z, int i2) {
                            if (z) {
                                navigatorData.downloadState = 0;
                            } else {
                                navigatorData.downloadState = 20;
                            }
                            PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                        }
                    };
                    this.mDownloadStartListener = downloadStateListener;
                    Adjust2LibraryLoaderHelper.INSTANCE.addDownloadStateListener(downloadStateListener);
                }
                int i2 = navigatorData.downloadState;
                if (i2 == 19 || i2 == 20) {
                    if (Adjust2LibraryLoaderHelper.getInstance().checkHasDownload()) {
                        navigatorData.downloadState = 0;
                        PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                        return true;
                    } else if (Adjust2LibraryLoaderHelper.INSTANCE.checkAbleOrDownload(PhotoNaviFragment.this.getActivity())) {
                        PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                    }
                } else if (i2 == 0 || i2 == 17) {
                    navigatorData.downloadState = 17;
                    PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                    PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                }
                return true;
            }
            return false;
        }

        public void onDestroy() {
            Adjust2LibraryLoaderHelper.INSTANCE.removeDownloadStateListener(this.mDownloadStartListener);
        }
    }

    public final Long getLibraryId(AbstractNaviFragment.NavigatorData navigatorData) {
        Effect<SdkProvider<Adjust2Data, AbstractEffectFragment>> effect = navigatorData.effect;
        if (effect == Effect.SKY) {
            return Long.valueOf(SkyCheckHelper.getLibraryId());
        }
        if (effect == Effect.ADJUST2) {
            return Long.valueOf(Adjust2LibraryLoaderHelper.getInstance().getLibraryId());
        }
        return 0L;
    }

    public void setListener(OnPreparePhotoNaviFragmentListener onPreparePhotoNaviFragmentListener) {
        this.mPrepareListener = onPreparePhotoNaviFragmentListener;
    }

    /* loaded from: classes2.dex */
    public class Remover2NaviManager {
        public LibraryLoaderHelper.DownloadStateListener mDownloadStartListener;

        public Remover2NaviManager() {
        }

        public boolean onNaviItemClick(final int i) {
            final AbstractNaviFragment.NavigatorData navigatorData = (AbstractNaviFragment.NavigatorData) PhotoNaviFragment.this.getNaviData().get(i);
            if (navigatorData.effect == Effect.REMOVER2) {
                if (this.mDownloadStartListener == null) {
                    LibraryLoaderHelper.DownloadStateListener downloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.editor.photo.app.navigator.PhotoNaviFragment.Remover2NaviManager.1
                        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                        public void onDownloading() {
                            navigatorData.downloadState = 18;
                            PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                        }

                        @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                        public void onFinish(boolean z, int i2) {
                            if (z) {
                                navigatorData.downloadState = 0;
                            } else {
                                navigatorData.downloadState = 20;
                            }
                            PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                        }
                    };
                    this.mDownloadStartListener = downloadStateListener;
                    Remover2LibraryLoaderHelper.INSTANCE.addDownloadStateListener(downloadStateListener);
                }
                int i2 = navigatorData.downloadState;
                if (i2 == 19 || i2 == 20) {
                    if (!Remover2LibraryLoaderHelper.INSTANCE.checkAbleOrDownload(PhotoNaviFragment.this.getActivity())) {
                        return true;
                    }
                    PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                    return true;
                } else if (i2 != 0 && i2 != 17) {
                    return true;
                } else {
                    navigatorData.downloadState = 17;
                    PhotoNaviFragment.this.mAdapter.notifyItemChanged(i);
                    PhotoNaviFragment.this.notifyNavigate(navigatorData.effect);
                    return true;
                }
            }
            return false;
        }

        public void onDestroy() {
            Remover2LibraryLoaderHelper.INSTANCE.removeDownloadStateListener(this.mDownloadStartListener);
        }
    }
}
