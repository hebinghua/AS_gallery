package com.miui.gallery.base_optimization.mvp.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.base_optimization.util.GenericUtils;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class FragmentDelegate<P extends IPresenter> implements IView<P> {
    public WeakReference<FragmentActivity> mActivity;
    public Fragment<P> mFragment;
    public Handler mHandler;
    public P mPresenter;
    public View mRootView;
    public Runnable mTaskOnUserFirstVisible;
    public boolean mUserFirstVisible = false;
    public SparseArray<View> mViews;

    public void onPause() {
    }

    public void onResume() {
    }

    public FragmentDelegate(Fragment<P> fragment) {
        this.mFragment = fragment;
    }

    public void onCreate(Bundle bundle) {
        int themeRes = this.mFragment.getThemeRes();
        if (themeRes != 0) {
            setThemeRes(themeRes);
        }
        initPresenter();
    }

    public void initPresenter() {
        if (this.mPresenter == null) {
            P findPresenterFromClass = findPresenterFromClass(0);
            this.mPresenter = findPresenterFromClass;
            if (findPresenterFromClass == null) {
                this.mPresenter = findPresenterFromClass(1);
            }
            P p = this.mPresenter;
            if (p == null) {
                return;
            }
            p.onAttachView(this.mFragment);
        }
    }

    public final P findPresenterFromClass(int i) {
        Object superClassT = GenericUtils.getSuperClassT(this.mFragment, i);
        if (superClassT == null || !(superClassT instanceof IPresenter)) {
            return null;
        }
        return (P) superClassT;
    }

    public void onAttach(android.app.Activity activity) {
        if (this.mActivity == null) {
            this.mActivity = new WeakReference<>((FragmentActivity) activity);
        }
    }

    public void onStop() {
        P p = this.mPresenter;
        if (p != null) {
            p.onStop();
        }
    }

    public void onDetach() {
        WeakReference<FragmentActivity> weakReference = this.mActivity;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mActivity = null;
        P p = this.mPresenter;
        if (p != null) {
            p.onDetachView();
        }
        this.mRootView = null;
        SparseArray<View> sparseArray = this.mViews;
        if (sparseArray == null) {
            return;
        }
        sparseArray.clear();
        this.mViews = null;
    }

    public void setThemeRes(int i) {
        Fragment<P> fragment = this.mFragment;
        if (fragment == null) {
            return;
        }
        fragment.setThemeRes(i);
    }

    public String getPageName() {
        Fragment<P> fragment = this.mFragment;
        return fragment != null ? fragment.getClass().getSimpleName() : "";
    }

    public View getView() {
        View view = this.mRootView;
        if (view == null) {
            return null;
        }
        return view;
    }

    public P getPresenter() {
        return this.mPresenter;
    }

    public void setUserVisibleHint(boolean z) {
        Fragment<P> fragment = this.mFragment;
        if (fragment != null && z) {
            if (!this.mUserFirstVisible) {
                if (this.mPresenter == null) {
                    initPresenter();
                }
                this.mUserFirstVisible = true;
                this.mTaskOnUserFirstVisible = new Runnable() { // from class: com.miui.gallery.base_optimization.mvp.view.FragmentDelegate.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!FragmentDelegate.this.mFragment.isAdded()) {
                            return;
                        }
                        FragmentDelegate.this.mFragment.onFragmentViewVisible(true);
                        FragmentDelegate.this.mTaskOnUserFirstVisible = null;
                    }
                };
                getHandler().post(this.mTaskOnUserFirstVisible);
                return;
            }
            fragment.onFragmentViewVisible(false);
        }
    }

    public final Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        return this.mHandler;
    }

    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (-1 == this.mFragment.getLayoutId()) {
            throw new IllegalArgumentException("layoutId can't null");
        }
        View view = this.mRootView;
        if (view != null) {
            ((ViewGroup) view).removeAllViews();
        }
        View inflate = layoutInflater.inflate(this.mFragment.getLayoutId(), viewGroup, false);
        this.mRootView = inflate;
        this.mFragment.initView(inflate, bundle, viewGroup);
        return this.mRootView;
    }

    public FragmentActivity getSafeActivity() {
        if (this.mFragment.getActivity() != null) {
            return this.mFragment.getActivity();
        }
        WeakReference<FragmentActivity> weakReference = this.mActivity;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public <V extends View> V findViewById(int i) {
        if (this.mRootView == null) {
            return null;
        }
        if (this.mViews == null) {
            this.mViews = new SparseArray<>();
        }
        if (this.mViews.get(i) != null) {
            return (V) this.mViews.get(i);
        }
        V v = (V) this.mRootView.findViewById(i);
        this.mViews.put(i, v);
        return v;
    }
}
