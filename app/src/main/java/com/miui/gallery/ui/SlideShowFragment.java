package com.miui.gallery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.SlideShowAdapter;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.loader.PhotoLoaderManager;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.SlideShowView;
import com.miui.privacy.LockSettingsHelper;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class SlideShowFragment extends BaseFragment {
    public SlideShowAdapter mAdapter;
    public SlideLoaderCallBack mLoaderCallBack;
    public boolean mNeedConfirmPassWord;
    public SlideShowView mSlideView;
    public int mLoadDuration = 2000;
    public Handler mSlideHandler = new Handler() { // from class: com.miui.gallery.ui.SlideShowFragment.1
        {
            SlideShowFragment.this = this;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                SlideShowFragment.this.loadNextBitmap();
            } else if (i != 2) {
            } else {
                Object obj = message.obj;
                SlideShowFragment.this.showPendingItem(obj == null ? null : (SlideShowAdapter.ShowItem) obj);
            }
        }
    };

    /* renamed from: $r8$lambda$fiuQ8f9D42dy0RHEzTsF-ee5CaA */
    public static /* synthetic */ Fragment m1565$r8$lambda$fiuQ8f9D42dy0RHEzTsFee5CaA(Bundle bundle, String str) {
        return lambda$showSlideShowFragment$0(bundle, str);
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "slide_show";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 0;
    }

    public static void showSlideShowFragment(BaseActivity baseActivity, final Bundle bundle) {
        baseActivity.startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.ui.SlideShowFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return SlideShowFragment.m1565$r8$lambda$fiuQ8f9D42dy0RHEzTsFee5CaA(bundle, str);
            }
        }, null, true, false);
    }

    public static /* synthetic */ Fragment lambda$showSlideShowFragment$0(Bundle bundle, String str) {
        SlideShowFragment slideShowFragment = new SlideShowFragment();
        slideShowFragment.setArguments(bundle);
        return slideShowFragment;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.slide_show_page, viewGroup, false);
        this.mSlideView = (SlideShowView) inflate.findViewById(R.id.slide);
        int slideDuration = getSlideDuration();
        this.mAdapter = new SlideShowAdapter((ImageLoadParams) getArguments().getParcelable("photo_transition_data"), getArguments().getInt("photo_init_position", 0), getArguments().getBoolean(GalleryPreferences.PrefKeys.SLIDESHOW_LOOP, false));
        this.mSlideView.setSlideDuration(slideDuration);
        this.mSlideView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.ui.SlideShowFragment.2
            {
                SlideShowFragment.this = this;
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1 && SlideShowFragment.this.isResumed()) {
                    SlideShowFragment.this.finish();
                }
                return true;
            }
        });
        return inflate;
    }

    public final int getSlideDuration() {
        int max = Math.max(3500, GalleryPreferences.SlideShow.getSlideShowInterval() * 1000);
        this.mLoadDuration = max - 500;
        return max;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mLoaderCallBack = new SlideLoaderCallBack();
        getLoaderManager().initLoader(-191519586, getArguments(), this.mLoaderCallBack);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (this.mActivity.getWindow() != null) {
            this.mActivity.getWindow().addFlags(128);
        }
        this.mAdapter.resume();
        loadNextBitmap();
    }

    @Override // com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View decorView = getActivity().getWindow().getDecorView();
        AppCompatActivity appCompatActivity = this.mActivity;
        SystemUiUtil.showSystemBars(decorView, appCompatActivity != null && appCompatActivity.isInMultiWindowMode());
    }

    @Override // com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isNeedConfirmPassWord()) {
            LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
            return;
        }
        View decorView = getActivity().getWindow().getDecorView();
        AppCompatActivity appCompatActivity = this.mActivity;
        SystemUiUtil.hideSystemBars(decorView, appCompatActivity != null && appCompatActivity.isInMultiWindowMode());
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 27) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 != -1) {
            finish();
        } else {
            this.mNeedConfirmPassWord = false;
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mActivity.getWindow() != null) {
            this.mActivity.getWindow().clearFlags(128);
        }
        this.mAdapter.pause();
        this.mSlideView.release();
        this.mSlideHandler.removeCallbacksAndMessages(null);
        this.mNeedConfirmPassWord = true;
    }

    public final void showPendingItem(SlideShowAdapter.ShowItem showItem) {
        if (showItem == null || !showItem.isValid()) {
            if (!isAdded() || !isResumed()) {
                return;
            }
            finish();
            return;
        }
        this.mSlideView.next(showItem.getBitmap(), 0);
        setResult(showItem);
        this.mSlideHandler.sendEmptyMessageDelayed(1, this.mLoadDuration);
    }

    public final void setResult(SlideShowAdapter.ShowItem showItem) {
        ImageLoadParams cacheItem;
        int index = showItem.getIndex();
        int count = this.mAdapter.getCount();
        if (index < 0) {
            DefaultLogger.e("SlideShowFragment", "invalid item index %d", Integer.valueOf(index));
            index = 0;
        } else if (index >= count) {
            int i = count - 1;
            DefaultLogger.e("SlideShowFragment", "item index [%d] exceed upper bound [%d]", Integer.valueOf(index), Integer.valueOf(i));
            index = Math.max(0, i);
        }
        DefaultLogger.d("SlideShowFragment", "setResult %d", Integer.valueOf(index));
        BaseDataItem baseDataItem = this.mAdapter.getBaseDataItem(index);
        if (baseDataItem != null) {
            cacheItem = new ImageLoadParams.Builder().setKey(baseDataItem.getKey()).setFilePath(baseDataItem.getPathDisplayBetter()).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setInitPosition(index).setMimeType(baseDataItem.getMimeType()).setSecretKey(baseDataItem.getSecretKey()).setFileLength(baseDataItem.getSize()).build();
        } else {
            cacheItem = this.mAdapter.getCacheItem();
        }
        getArguments().putParcelable("photo_transition_data", cacheItem);
        getArguments().putInt("photo_init_position", index);
        getArguments().putInt("photo_count", this.mAdapter.getCount());
    }

    public final void loadNextBitmap() {
        this.mAdapter.nextBitmap(new FutureListener<SlideShowAdapter.ShowItem>() { // from class: com.miui.gallery.ui.SlideShowFragment.3
            {
                SlideShowFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<SlideShowAdapter.ShowItem> future) {
                if (!future.isCancelled()) {
                    SlideShowFragment.this.mSlideHandler.obtainMessage(2, future.get()).sendToTarget();
                }
            }
        });
    }

    public final boolean isNeedConfirmPassWord() {
        return getArguments().getBoolean("is_secret_photo") && this.mNeedConfirmPassWord;
    }

    /* loaded from: classes2.dex */
    public class SlideLoaderCallBack implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public SlideLoaderCallBack() {
            SlideShowFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            return PhotoLoaderManager.getInstance().getPhotoDataSet(SlideShowFragment.this.getActivity(), Uri.parse(bundle.getString("photo_uri")), bundle);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            SlideShowFragment.this.mAdapter.changeDataSet((BaseDataSet) obj);
            SlideShowAdapter.ShowItem currentShowItem = SlideShowFragment.this.mAdapter.getCurrentShowItem();
            if (currentShowItem != null) {
                SlideShowFragment.this.setResult(currentShowItem);
            }
        }
    }
}
