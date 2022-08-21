package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.Cast;
import com.miui.gallery.ui.photoPage.bars.menuitem.CorrectDoc;
import com.miui.gallery.ui.photoPage.bars.menuitem.Edit;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import com.miui.gallery.ui.photoPage.bars.menuitem.Send;
import com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.ui.photoPage.bars.view.IViewProvider;
import com.miui.gallery.util.AccessibilityUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.VideoFrameSeekBar;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BasePhotoPageBarsDelegateFragment extends AbstractViewPagerFragment implements PhotoPageMenuManager.IMenuOwner, PhotoPageActionBarManager.IActionBarOwner {
    public boolean isTransitEndSoonCallbackDone;
    public IPhotoPageActionBarManager mActionBarManager;
    public Send.ChoiceManager mChoiceManager;
    public View mCurrentFocusView;
    public CorrectDoc.DocCorrectionManager mDocCorrectionManager;
    public CloudImageLoadingListener mDownloadListener;
    public Edit.PhotoEditorManager mEditorManager;
    public int mExtraThemeRes;
    public Favorite.FavoritesManager mFavoritesManager;
    public int mMenuCollapsedHeight;
    public IPhotoPageMenuManager mMenuManager;
    public Cast.ProjectionManager mProjectManager;
    public Context mThemedContext;
    public VideoCompress.VideoCompressManager mVideoCompressManager;
    public IViewProvider mViewProvider;
    public boolean isExiting = false;
    public boolean isEntering = true;
    public int mTalkBackModel = 0;
    public PhotoPageKeyboardShortcutCallback mShortcutCallback = new PhotoPageKeyboardShortcutCallback();

    /* loaded from: classes2.dex */
    public interface IConfigMenuCallBack {
        void setChoiceManager(Send.ChoiceManager choiceManager);

        void setCloudImageLoadingListener(CloudImageLoadingListener cloudImageLoadingListener);

        void setDocCorrectionManager(CorrectDoc.DocCorrectionManager docCorrectionManager);

        void setFavoritesManager(Favorite.FavoritesManager favoritesManager);

        void setPhotoEditorManager(Edit.PhotoEditorManager photoEditorManager);

        void setProjectionManager(Cast.ProjectionManager projectionManager);

        void setVideoCompressManager(VideoCompress.VideoCompressManager videoCompressManager);
    }

    /* loaded from: classes2.dex */
    public interface SimpleCallback {
        void duringAction();
    }

    public static /* synthetic */ void $r8$lambda$09UxraRVUPlnPwN9DSR66YZxwlg(String str, String str2) {
        SamplingStatHelper.recordStringPropertyEvent(str, str2);
    }

    public static /* synthetic */ void $r8$lambda$oA17sDGRYPsBtIHg2WxFiMxCOF0(String str, String str2) {
        SamplingStatHelper.recordCountEvent(str, str2);
    }

    /* renamed from: $r8$lambda$tZ9-3hwwaV8cJU3sDqkMZ9TLSJ0 */
    public static /* synthetic */ void m1430$r8$lambda$tZ93hwwaV8cJU3sDqkMZ9TLSJ0(BasePhotoPageBarsDelegateFragment basePhotoPageBarsDelegateFragment) {
        basePhotoPageBarsDelegateFragment.lambda$refreshTopBarAllElements$0();
    }

    public abstract void delayDoAfterTransit();

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public GalleryFragment getOwnerImpl() {
        return this;
    }

    public int getThemeRes() {
        return 2131952018;
    }

    public abstract void setDownloadListener(CloudImageLoadingListener cloudImageLoadingListener);

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner, com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager.IActionBarOwner
    public void setCurrentFocusView(View view) {
        this.mCurrentFocusView = view;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void onMenuActionsShown() {
        View view = this.mCurrentFocusView;
        if (view != null) {
            view.setFocusable(true);
            this.mCurrentFocusView.setFocusableInTouchMode(true);
            this.mCurrentFocusView.requestFocus(BaiduSceneResult.VISA);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDataProvider.getViewModelData().setConfiguration(configuration);
        this.mMenuManager.onConfigurationChanged(configuration);
        this.mActionBarManager.onConfigurationChanged(configuration);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        super.setHasOptionsMenu(false);
        super.setMenuVisibility(false);
        int themeRes = getThemeRes();
        if (themeRes != 0) {
            setThemeRes(themeRes);
        }
        initManagers();
    }

    public final void initManagers() {
        com.miui.gallery.ui.photoPage.bars.view.ViewProvider viewProvider = new com.miui.gallery.ui.photoPage.bars.view.ViewProvider(getThemedContext(), this.mDataProvider);
        this.mViewProvider = viewProvider;
        this.mActionBarManager = new PhotoPageActionBarManager(this, this.mDataProvider, viewProvider);
        this.mMenuManager = new PhotoPageMenuManager(this, this.mDataProvider, this.mViewProvider);
        getLifecycle().addObserver(this.mActionBarManager);
        getLifecycle().addObserver(this.mMenuManager);
        this.mMenuManager.configMenu(new IConfigMenuCallBack() { // from class: com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.1
            {
                BasePhotoPageBarsDelegateFragment.this = this;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setChoiceManager(Send.ChoiceManager choiceManager) {
                BasePhotoPageBarsDelegateFragment.this.mChoiceManager = choiceManager;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setFavoritesManager(Favorite.FavoritesManager favoritesManager) {
                BasePhotoPageBarsDelegateFragment.this.mFavoritesManager = favoritesManager;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setProjectionManager(Cast.ProjectionManager projectionManager) {
                BasePhotoPageBarsDelegateFragment basePhotoPageBarsDelegateFragment = BasePhotoPageBarsDelegateFragment.this;
                basePhotoPageBarsDelegateFragment.mProjectManager = projectionManager;
                if (basePhotoPageBarsDelegateFragment.mAdapter == null) {
                    return;
                }
                int currentItem = basePhotoPageBarsDelegateFragment.mPager.getCurrentItem();
                BasePhotoPageBarsDelegateFragment basePhotoPageBarsDelegateFragment2 = BasePhotoPageBarsDelegateFragment.this;
                basePhotoPageBarsDelegateFragment2.mProjectManager.updateSet(basePhotoPageBarsDelegateFragment2.mAdapter.getDataSet());
                BasePhotoPageBarsDelegateFragment basePhotoPageBarsDelegateFragment3 = BasePhotoPageBarsDelegateFragment.this;
                basePhotoPageBarsDelegateFragment3.mProjectManager.settleItem(basePhotoPageBarsDelegateFragment3.mAdapter.getDataItem(currentItem), currentItem);
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setVideoCompressManager(VideoCompress.VideoCompressManager videoCompressManager) {
                BasePhotoPageBarsDelegateFragment.this.mVideoCompressManager = videoCompressManager;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setPhotoEditorManager(Edit.PhotoEditorManager photoEditorManager) {
                BasePhotoPageBarsDelegateFragment.this.mEditorManager = photoEditorManager;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setDocCorrectionManager(CorrectDoc.DocCorrectionManager docCorrectionManager) {
                BasePhotoPageBarsDelegateFragment.this.mDocCorrectionManager = docCorrectionManager;
            }

            @Override // com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack
            public void setCloudImageLoadingListener(CloudImageLoadingListener cloudImageLoadingListener) {
                BasePhotoPageBarsDelegateFragment basePhotoPageBarsDelegateFragment = BasePhotoPageBarsDelegateFragment.this;
                basePhotoPageBarsDelegateFragment.mDownloadListener = cloudImageLoadingListener;
                basePhotoPageBarsDelegateFragment.setDownloadListener(cloudImageLoadingListener);
            }
        });
    }

    @Override // miuix.appcompat.app.Fragment
    public void setThemeRes(int i) {
        super.setThemeRes(i);
        this.mExtraThemeRes = i;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            this.mThemedContext = this.mActivity;
            if (this.mExtraThemeRes != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mThemedContext, this.mExtraThemeRes);
            }
        }
        return this.mThemedContext;
    }

    public void prepareViews() {
        this.mViewProvider.startPrepare();
        this.mMenuManager.prepareViews();
        this.mActionBarManager.prepareViews();
    }

    public void uninstallFunctionsIfReInflate() {
        this.mMenuManager.uninstallFunctions();
    }

    public void prepareData(BaseDataItem baseDataItem) {
        this.mMenuManager.refreshMenuItems(baseDataItem);
    }

    public void attachBars() {
        this.mActionBarManager.attach();
        this.mMenuManager.attach();
    }

    public void initBars() {
        this.mActionBarManager.delegate(super.getActionBar());
        this.mActionBarManager.hide(true);
        onMenuInflated();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public void showBars(boolean z) {
        DefaultLogger.d("PhotoPageFragment", "showBars withAnim:" + z);
        this.mActionBarManager.show(z);
        this.mMenuManager.showMenuView(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public void hideBars(boolean z) {
        if (isMoreActionsShown()) {
            hideMoreActions(z);
            return;
        }
        DefaultLogger.d("PhotoPageFragment", "hideBars withAnim:" + z);
        this.mActionBarManager.hide(z);
        this.mMenuManager.hideMenuView(z);
    }

    public void hideMoreActions(boolean z) {
        this.mMenuManager.hideMoreActions(z);
    }

    public void restoreMoreActions(boolean z) {
        this.mMenuManager.restoreMoreActions(z);
    }

    public boolean isMoreActionsShown() {
        return this.mMenuManager.isMoreActionsShown();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean isActionBarShowing() {
        return this.mActionBarManager.isShowing();
    }

    public int getMenuCollapsedHeight() {
        return this.mMenuManager.getMenuCollapsedHeight();
    }

    public int getSplitBarHeight() {
        if (this.mMenuCollapsedHeight == -1) {
            this.mMenuCollapsedHeight = getMenuCollapsedHeight();
        }
        int i = this.mMenuCollapsedHeight;
        if (i <= 0) {
            i = MiscUtil.getDefaultSplitActionBarHeight(this.mActivity);
        }
        return i + ViewCompat.getSystemWindowInsetBottom(this.mActivity.getWindow().getDecorView());
    }

    public void refreshMenuItems(BaseDataItem baseDataItem) {
        this.mMenuManager.refreshMenuItems(baseDataItem);
    }

    public void refreshMenuItemsIfPrepared(BaseDataItem baseDataItem) {
        this.mMenuManager.refreshMenuItemsIfPrepared(baseDataItem);
    }

    public void setFrameBar(VideoFrameSeekBar videoFrameSeekBar) {
        ViewGroup viewGroup = (ViewGroup) videoFrameSeekBar.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(videoFrameSeekBar);
        }
        this.mMenuManager.setFrameBar(videoFrameSeekBar);
    }

    public void inflateActionBarCustomView() {
        this.mActionBarManager.inflateActionBarCustomView();
    }

    public int getActionBarHeight() {
        return this.mActionBarManager.getActionBarHeight();
    }

    public /* synthetic */ void lambda$refreshTopBarAllElements$0() {
        this.mActionBarManager.refreshTopBarAllElements();
    }

    public void refreshTopBarAllElements() {
        IdleUITaskHelper.getInstance().addTask(new Runnable() { // from class: com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                BasePhotoPageBarsDelegateFragment.m1430$r8$lambda$tZ93hwwaV8cJU3sDqkMZ9TLSJ0(BasePhotoPageBarsDelegateFragment.this);
            }
        });
    }

    public void refreshTopBarLocation(int i, int i2) {
        this.mActionBarManager.refreshTopBarLocation(i, i2);
    }

    public void refreshTopBarInfo(BaseDataItem baseDataItem) {
        this.mActionBarManager.refreshTopBarInfo(baseDataItem);
    }

    public void setTopBarContentVisibility(boolean z) {
        this.mActionBarManager.setTopBarContentVisibility(z);
    }

    public void refreshTopBarLockEnter(boolean z, boolean z2) {
        this.mActionBarManager.refreshTopBarLockEnter(z, z2);
    }

    public void refreshTopBarSpecialTypeEnter(BaseDataItem baseDataItem, View.OnClickListener onClickListener) {
        this.mActionBarManager.refreshTopBarSpecialTypeEnter(baseDataItem, onClickListener);
    }

    public void refreshTopBarMotionPhotoEnter(boolean z, View.OnClickListener onClickListener) {
        this.mActionBarManager.refreshTopBarMotionPhotoEnter(z, onClickListener);
    }

    public void showLockButtonGuide() {
        this.mActionBarManager.showLockButtonGuide();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public void postRecordCountEvent(final String str, final String str2) {
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                BasePhotoPageBarsDelegateFragment.$r8$lambda$oA17sDGRYPsBtIHg2WxFiMxCOF0(str, str2);
            }
        });
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public void postRecordStringPropertyEvent(final String str, final String str2) {
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                BasePhotoPageBarsDelegateFragment.$r8$lambda$09UxraRVUPlnPwN9DSR66YZxwlg(str, str2);
            }
        });
    }

    public Cast.ProjectionManager checkAndCreateProjectionManager() {
        return this.mMenuManager.checkAndCreateProjectionManager();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean isNeedConfirmPassWord(int i) {
        return isNeedConfirmPassWord(this.mAdapter.getDataItem(i));
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IMenuOwner
    public boolean isSecretAlbum() {
        return super.isSecretAlbum();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.adapter.PhotoPageAdapter.OnPreviewedListener
    public final void onPreviewed() {
        this.isEntering = false;
        if (!isAdded()) {
            DefaultLogger.w("PhotoPageFragment", "PhotoPage removed before preview finish");
            return;
        }
        delayDoAfterTransit();
        super.onPreviewed();
    }

    public boolean isExiting() {
        return this.isExiting;
    }

    public boolean isEntering() {
        return this.isEntering;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mViewProvider.release();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mActionBarManager.release();
        this.mMenuManager.release();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onPause();
        }
        View view = this.mCurrentFocusView;
        if (view != null) {
            view.setFocusableInTouchMode(false);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        Send.ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onDetach();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public PhotoPageAdapter.PhotoPageInteractionListener getPhotoPageInteractionListener() {
        return new PhotoPageAdapter.PhotoPageInteractionListener() { // from class: com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment.2
            {
                BasePhotoPageBarsDelegateFragment.this = this;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public void notifyDataChanged() {
                BasePhotoPageBarsDelegateFragment.this.onContentChanged();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public int getActionBarHeight() {
                return BasePhotoPageBarsDelegateFragment.this.getActionBarHeight();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public int getMenuBarHeight() {
                if (BasePhotoPageBarsDelegateFragment.this.isAdded()) {
                    return BasePhotoPageBarsDelegateFragment.this.getMenuCollapsedHeight();
                }
                return 0;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public void playVideo(BaseDataItem baseDataItem, String str) {
                BasePhotoPageBarsDelegateFragment.this.onPlayVideo(baseDataItem, str);
            }
        };
    }

    public long getCurrentItemKey() {
        PhotoPageAdapter photoPageAdapter = this.mAdapter;
        if (photoPageAdapter == null || photoPageAdapter.isPreviewing()) {
            ImageLoadParams imageLoadParams = getImageLoadParams();
            if (imageLoadParams == null) {
                return -1L;
            }
            return imageLoadParams.getKey();
        }
        BaseDataItem dataItem = this.mAdapter.getDataItem(this.mPager.getCurrentItem());
        if (dataItem == null) {
            return -1L;
        }
        return dataItem.getKey();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public boolean isInTalkBackModel() {
        if (this.mTalkBackModel == 0 && getContext() != null) {
            this.mTalkBackModel = AccessibilityUtils.isTalkBackEnabled(getContext()) ? 2 : 1;
        }
        return this.mTalkBackModel == 2;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IBarsOwner
    public ActionBarCustomViewBuilder.CustomViewType getCustomViewType() {
        boolean z = requireActivity().getResources().getConfiguration().smallestScreenWidthDp >= BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD;
        boolean isInMultiWindowMode = this.mDataProvider.getFieldData().mCurrent.isInMultiWindowMode();
        if (BaseBuildUtil.isLargeScreenDevice()) {
            return z ? ActionBarCustomViewBuilder.CustomViewType.PadTopMenu : ActionBarCustomViewBuilder.CustomViewType.TopBar;
        } else if (!MiscUtil.isLandMode(this.mActivity) || isInMultiWindowMode) {
            return ActionBarCustomViewBuilder.CustomViewType.TopBar;
        } else {
            return z ? ActionBarCustomViewBuilder.CustomViewType.TopBar : ActionBarCustomViewBuilder.CustomViewType.TopMenu;
        }
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (((PhotoPageMenuManager) this.mMenuManager).isSupportDelete() || ((PhotoPageMenuManager) this.mMenuManager).isSupportPurge()) {
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class PhotoPageKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public PhotoPageKeyboardShortcutCallback() {
            BasePhotoPageBarsDelegateFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (((PhotoPageMenuManager) BasePhotoPageBarsDelegateFragment.this.mMenuManager).isSupportDelete()) {
                ((PhotoPageMenuManager) BasePhotoPageBarsDelegateFragment.this.mMenuManager).doDelete();
                return true;
            } else if (!((PhotoPageMenuManager) BasePhotoPageBarsDelegateFragment.this.mMenuManager).isSupportPurge()) {
                return true;
            } else {
                ((PhotoPageMenuManager) BasePhotoPageBarsDelegateFragment.this.mMenuManager).doPurge();
                return true;
            }
        }
    }
}
