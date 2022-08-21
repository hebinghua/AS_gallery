package com.miui.gallery.ui.photoPage.bars;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.tracing.Trace;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.model.UriItem;
import com.miui.gallery.ui.BasePhotoPageBarsDelegateFragment;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.data.DataPrepareHelper;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.ui.photoPage.bars.data.GalleryMenuBuilder;
import com.miui.gallery.ui.photoPage.bars.data.GalleryMenuItem;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.menuitem.AbstractMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.menuitem.AddCloud;
import com.miui.gallery.ui.photoPage.bars.menuitem.Cast;
import com.miui.gallery.ui.photoPage.bars.menuitem.CorrectDoc;
import com.miui.gallery.ui.photoPage.bars.menuitem.Delete;
import com.miui.gallery.ui.photoPage.bars.menuitem.Details;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.ui.photoPage.bars.menuitem.Edit;
import com.miui.gallery.ui.photoPage.bars.menuitem.Favorite;
import com.miui.gallery.ui.photoPage.bars.menuitem.GoogleLens;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.menuitem.More;
import com.miui.gallery.ui.photoPage.bars.menuitem.Ocr;
import com.miui.gallery.ui.photoPage.bars.menuitem.PhotoRename;
import com.miui.gallery.ui.photoPage.bars.menuitem.PlaySlideShow;
import com.miui.gallery.ui.photoPage.bars.menuitem.Purge;
import com.miui.gallery.ui.photoPage.bars.menuitem.Recovery;
import com.miui.gallery.ui.photoPage.bars.menuitem.RemoveSecret;
import com.miui.gallery.ui.photoPage.bars.menuitem.Save;
import com.miui.gallery.ui.photoPage.bars.menuitem.Send;
import com.miui.gallery.ui.photoPage.bars.menuitem.SetSlideWallPaper;
import com.miui.gallery.ui.photoPage.bars.menuitem.SetVideoWallPaper;
import com.miui.gallery.ui.photoPage.bars.menuitem.SetWallPaper;
import com.miui.gallery.ui.photoPage.bars.menuitem.ToPdf;
import com.miui.gallery.ui.photoPage.bars.menuitem.VideoCompress;
import com.miui.gallery.ui.photoPage.bars.menuitem.WaterMark;
import com.miui.gallery.ui.photoPage.bars.view.ActionBarCustomViewBuilder;
import com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu;
import com.miui.gallery.ui.photoPage.bars.view.IViewProvider;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.ui.photoPage.menufilter.extra.ExtraParams;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IdleUITaskHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.VideoFrameSeekBar;
import com.miui.gallery.view.menu.IMenuItem;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class PhotoPageMenuManager implements IPhotoPageMenuManager, IPhotoPageMenuItemManager {
    public BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack mConfigMenuCallBack;
    public int mCurrentWindowOrientation;
    public final IDataProvider mDataProvider;
    public boolean mMenuIsShowing;
    public final MenuItemPrepareHelper mMenuItemPrepareHelper;
    public boolean mNeedRestoreMoreActions;
    public final IMenuOwner mOwner;
    public final IViewProvider mViewProvider;
    public IPhotoPageMenu vPhotoPageMenu;
    public int mCurrentWindowMode = -1;
    public final MenuConfigurator mMenuConfigurator = new MenuConfigurator();
    public final MenuEnableItemsFilter mEnableItemsFilter = new MenuEnableItemsFilter();
    public final IMenuItemDelegate.ClickHelper mMenuClickHandler = new MenuClickHelper();
    public final MenuItemHolder mMenuItemHolder = new MenuItemHolder();
    public final MenuItemOrderHelper mMenuItemOrderHelper = new MenuItemOrderHelper();

    /* loaded from: classes2.dex */
    public interface IConfigMenuCallback {
        void configCompleted(ConcurrentHashMap<MenuItemType, IMenuItemDelegate> concurrentHashMap);
    }

    /* loaded from: classes2.dex */
    public interface IFilterCallback {
        void onFilterFinished(BaseDataItem baseDataItem, ArrayList<FilterResult> arrayList, ArrayList<FilterResult> arrayList2);
    }

    /* loaded from: classes2.dex */
    public interface IMenuOwner extends IBarsOwner {
        void checkDismissKeyGuard(boolean z);

        void configForLargeScreenDevice(Configuration configuration);

        void doAfterHideAnimByClickSpecialEnter(BasePhotoPageBarsDelegateFragment.SimpleCallback simpleCallback);

        PhotoPageAdapter getAdapter();

        float getCurrentItemScale();

        int getPageCount();

        int getPageHeight();

        int getPageWidth();

        boolean isActionBarShowing();

        boolean isCurrentImageOverDisplayArea();

        boolean isLandscapeWindowMode();

        boolean isNeedConfirmPassWord(int i);

        boolean isSecretAlbum();

        void onDownloadComplete(BaseDataItem baseDataItem);

        void onMenuActionsShown();

        void onMenuInflated();

        void onSendClick();

        void postRecordStringPropertyEvent(String str, String str2);

        boolean prohibitOperateProcessingItem(BaseDataItem baseDataItem);

        void refreshTheme(boolean z);

        void setActionBarVisible(boolean z, boolean z2);

        void setCurrentFocusView(View view);

        void setCurrentPosition(int i, boolean z);

        void setScreenSceneEffect(boolean z);
    }

    /* loaded from: classes2.dex */
    public enum MenuItemType {
        SEND,
        EDIT,
        FAVORITE,
        DELETE,
        MORE,
        SAVE,
        ADD_CLOUD,
        TO_PDF,
        OCR,
        CORRECT_DOC,
        REMOVE_SECRET,
        CAST,
        SET_WALLPAPER,
        SET_SLIDE_WALLPAPER,
        SET_VIDEO_WALLPAPER,
        PLAY_SLIDESHOW,
        VIDEO_COMPRESS,
        PHOTO_RENAME,
        DOWNLOAD_ORIGINAL,
        GOOGLE_LENS,
        DETAILS,
        WATERMARK,
        RECOVERY,
        PURGE
    }

    public static /* synthetic */ void $r8$lambda$FZZ6IhpEe3MHqYQ4M8UaTtVWWzk(PhotoPageMenuManager photoPageMenuManager, ConcurrentHashMap concurrentHashMap) {
        photoPageMenuManager.lambda$refreshMenuItems$2(concurrentHashMap);
    }

    /* renamed from: $r8$lambda$dtR-eXDE-hgWRxYE4WlZp9-BToE */
    public static /* synthetic */ void m1611$r8$lambda$dtReXDEhgWRxYE4WlZp9BToE(PhotoPageMenuManager photoPageMenuManager, ConcurrentHashMap concurrentHashMap) {
        photoPageMenuManager.lambda$configMenu$0(concurrentHashMap);
    }

    public static /* synthetic */ void $r8$lambda$l39Ba5NmygwraiQgkRo22IejMa0(PhotoPageMenuManager photoPageMenuManager, boolean z) {
        photoPageMenuManager.lambda$restoreMoreActions$4(z);
    }

    public PhotoPageMenuManager(IMenuOwner iMenuOwner, IDataProvider iDataProvider, IViewProvider iViewProvider) {
        this.mOwner = iMenuOwner;
        this.mDataProvider = iDataProvider;
        this.mViewProvider = iViewProvider;
        this.mMenuItemPrepareHelper = new MenuItemPrepareHelper(iDataProvider, iMenuOwner.getActivity(), iMenuOwner.getOwnerImpl());
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void configMenu(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack) {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "configMenu =>");
        this.mConfigMenuCallBack = iConfigMenuCallBack;
        this.mMenuConfigurator.asyncConfigMenu(new IConfigMenuCallback() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IConfigMenuCallback
            public final void configCompleted(ConcurrentHashMap concurrentHashMap) {
                PhotoPageMenuManager.m1611$r8$lambda$dtReXDEhgWRxYE4WlZp9BToE(PhotoPageMenuManager.this, concurrentHashMap);
            }
        });
    }

    public /* synthetic */ void lambda$configMenu$0(ConcurrentHashMap concurrentHashMap) {
        this.mMenuItemHolder.onConfiguredMenuItems(concurrentHashMap, this.mConfigMenuCallBack);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void prepareViews() {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "prepareViews =>");
        this.mViewProvider.prepareMenuViews();
        prepareMenu();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void attach() {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "attach =>");
        FragmentActivity activity = this.mOwner.getActivity();
        if (activity != null) {
            swapMenu(activity.getResources().getConfiguration());
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void onConfigurationChanged(Configuration configuration) {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "onConfigurationChanged =>");
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu != null && !this.mNeedRestoreMoreActions) {
            this.mNeedRestoreMoreActions = iPhotoPageMenu.isMoreActionsShown();
        }
        if (windowModeChanged()) {
            resetMenuMode();
        }
        swapMenu(configuration);
        this.mCurrentWindowOrientation = configuration.orientation;
    }

    public final boolean windowModeChanged() {
        if (this.mCurrentWindowMode == -1) {
            return false;
        }
        boolean isInMultiWindowMode = isInMultiWindowMode();
        int i = this.mCurrentWindowMode;
        if ((i == 2 || !isInMultiWindowMode) && (i == 1 || isInMultiWindowMode)) {
            if (!isInMultiWindowMode) {
                return false;
            }
            IMenuOwner iMenuOwner = this.mOwner;
            if (iMenuOwner != null && iMenuOwner.getActivity() != null && this.mCurrentWindowOrientation == this.mOwner.getActivity().getResources().getConfiguration().orientation) {
                return false;
            }
        }
        return true;
    }

    public final void resetMenuMode() {
        if (this.mMenuItemPrepareHelper == null || this.mDataProvider == null) {
            return;
        }
        setWindowModeValue();
        DefaultLogger.d("PhotoPageFragment_MenuManager", "reset menu mode");
        this.mMenuItemPrepareHelper.clearAllPreparedResult();
        refreshMenuItems(this.mDataProvider.getFieldData().mCurrent.getDataItem());
    }

    public final void setWindowModeValue() {
        int i = isInMultiWindowMode() ? 2 : 1;
        DefaultLogger.d("PhotoPageFragment_MenuManager", "windowMode [%s] change to [%s]", Integer.valueOf(this.mCurrentWindowMode), Integer.valueOf(i));
        this.mCurrentWindowMode = i;
    }

    public void prepareMenu() {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu != null) {
            iPhotoPageMenu.setVisibility(8);
            this.vPhotoPageMenu.hideMenuView(false);
            this.vPhotoPageMenu.hideMoreActions(true);
            this.mDataProvider.getViewModelData().removeResidentMenuItemsObserver(this.mOwner.getActivity(), this.vPhotoPageMenu.getResidentMenuHelper());
            this.mDataProvider.getViewModelData().removeNonResidentMenuItemsObserver(this.mOwner.getActivity(), this.vPhotoPageMenu.getNonResidentMenuHelper());
        }
        this.vPhotoPageMenu = this.mViewProvider.getGalleryMenu(this, this.mOwner.getActivity(), this.mMenuClickHandler, this.mOwner.getCustomViewType());
    }

    public void swapMenu(Configuration configuration) {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "swapMenu");
        prepareMenu();
        this.vPhotoPageMenu.addRootLayout((ViewGroup) this.mOwner.getOwnerImpl().getView());
        this.vPhotoPageMenu.setVisibility(this.mDataProvider.getFieldData().mCurrent.isSlipped ? 4 : 0);
        this.vPhotoPageMenu.hideMenuView(false);
        this.vPhotoPageMenu.hideMoreActions(true);
        this.vPhotoPageMenu.reAddResidentMenuItems(this.mMenuItemHolder.getResident());
        this.mDataProvider.getViewModelData().addResidentMenuItemsObserver(this.mOwner.getActivity(), this.mOwner.getOwnerImpl(), this.vPhotoPageMenu.getResidentMenuHelper());
        this.mDataProvider.getViewModelData().addNonResidentMenuItemsObserver(this.mOwner.getActivity(), this.mOwner.getOwnerImpl(), this.vPhotoPageMenu.getNonResidentMenuHelper());
        refreshMoreActionsMaxHeight(configuration);
        this.vPhotoPageMenu.relayoutForConfigChanged(ScreenUtils.dpToPixel(configuration.screenWidthDp * 1.0f));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void setCurrentFocusView(View view) {
        this.mOwner.setCurrentFocusView(view);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void onMenuActionsShown() {
        this.mOwner.onMenuActionsShown();
        DefaultLogger.d("PhotoPageFragment_MenuManager", "onMenuActionsShown");
    }

    public final void refreshMoreActionsMaxHeight(Configuration configuration) {
        if (this.vPhotoPageMenu == null || configuration.orientation != 1) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMoreActionsMaxHeight vPhotoPageMenu is null or ORIENTATION_PORTRAIT");
            return;
        }
        float dpToPixel = ScreenUtils.dpToPixel(configuration.screenHeightDp);
        int stationaryActionBarHeight = PhotoPageActionBarManager.getStationaryActionBarHeight();
        if (this.mOwner.getActivity() != null && ActivityCompat.isInFreeFormWindow(this.mOwner.getActivity())) {
            stationaryActionBarHeight *= 3;
        }
        this.vPhotoPageMenu.refreshMoreActionsMaxHeight(dpToPixel - stationaryActionBarHeight);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void initFunction(MenuItemType menuItemType) {
        final IMenuItemDelegate item = this.mMenuItemHolder.getItem(menuItemType);
        if (item != null) {
            this.mOwner.getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.this.lambda$initFunction$1(item);
                }
            });
        }
    }

    /* renamed from: initFunction */
    public void lambda$initFunction$1(IMenuItemDelegate iMenuItemDelegate) {
        iMenuItemDelegate.initFunction(this.mDataProvider, this);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void refreshMenuItemsIfPrepared(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItemsIfPrepared => [%s]", Long.valueOf(baseDataItem.getKey()));
        try {
            Trace.beginSection("refreshMenuItemsIfPrepared");
            if (this.mDataProvider.getFieldData().isFromTrash) {
                DefaultLogger.d("PhotoPageFragment_MenuManager", "from Trash no need prepare =>");
            } else if (!this.mMenuItemPrepareHelper.isPreparedResultItem(baseDataItem)) {
                DefaultLogger.d("PhotoPageFragment_MenuManager", "is not Prepared =>");
            } else if (!this.mMenuItemHolder.isConfiguredMenuItems()) {
                DefaultLogger.e("PhotoPageFragment_MenuManager", "is not Configured MenuItems （IfPrepared） =>");
            } else {
                if (this.mMenuItemPrepareHelper.isPreparedResultItem(baseDataItem)) {
                    if (this.mMenuItemPrepareHelper.isApplyPreparedResult(baseDataItem)) {
                        return;
                    }
                    DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItemsIfPrepared");
                    this.mMenuItemPrepareHelper.applyPreparedResult(baseDataItem);
                }
            }
        } finally {
            Trace.endSection();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void refreshMenuItems(BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItems => [%s]", Long.valueOf(baseDataItem.getKey()));
        if (!this.mMenuItemHolder.isConfiguredMenuItems()) {
            DefaultLogger.w("PhotoPageFragment_MenuManager", "is not Configured MenuItems （do syncConfigMenu） =>");
            this.mMenuConfigurator.syncConfigMenu(new IConfigMenuCallback() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IConfigMenuCallback
                public final void configCompleted(ConcurrentHashMap concurrentHashMap) {
                    PhotoPageMenuManager.$r8$lambda$FZZ6IhpEe3MHqYQ4M8UaTtVWWzk(PhotoPageMenuManager.this, concurrentHashMap);
                }
            });
        }
        if (!this.mMenuItemHolder.isConfiguredMenuItems()) {
            DefaultLogger.e("PhotoPageFragment_MenuManager", "is not Configured MenuItems （after syncConfigMenu） =>");
        } else if (this.mMenuItemPrepareHelper.isPreparedResultItem(baseDataItem)) {
            if (this.mMenuItemPrepareHelper.isApplyPreparedResult(baseDataItem)) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItems =>");
            this.mMenuItemPrepareHelper.applyPreparedResult(baseDataItem);
        } else {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItems =>");
            this.mEnableItemsFilter.filter(baseDataItem, this.mMenuItemHolder);
        }
    }

    public /* synthetic */ void lambda$refreshMenuItems$2(ConcurrentHashMap concurrentHashMap) {
        this.mMenuItemHolder.onConfiguredMenuItems(concurrentHashMap, this.mConfigMenuCallBack);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStart(LifecycleOwner lifecycleOwner) {
        if (this.mCurrentWindowMode == -1) {
            setWindowModeValue();
        } else if (!windowModeChanged()) {
        } else {
            resetMenuMode();
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public void onStop(LifecycleOwner lifecycleOwner) {
        BaseDataItem dataItem;
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null || this.mMenuItemPrepareHelper == null || (dataItem = iDataProvider.getFieldData().mCurrent.getDataItem()) == null || !this.mMenuItemPrepareHelper.isPreparedResultItem(dataItem)) {
            return;
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager", "clearPreparedResult => %d", Long.valueOf(dataItem.getKey()));
        this.mMenuItemPrepareHelper.clearPreparedResult(dataItem);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void uninstallFunctions() {
        MenuItemHolder menuItemHolder = this.mMenuItemHolder;
        if (menuItemHolder == null) {
            return;
        }
        menuItemHolder.uninstallFunctions();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void refreshMenuItem(final IMenuItemDelegate iMenuItemDelegate) {
        IdleUITaskHelper.getInstance().addTask(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PhotoPageMenuManager.this.lambda$refreshMenuItem$3(iMenuItemDelegate);
            }
        });
    }

    public /* synthetic */ void lambda$refreshMenuItem$3(IMenuItemDelegate iMenuItemDelegate) {
        if (iMenuItemDelegate == null || this.vPhotoPageMenu == null || this.mMenuItemHolder == null) {
            return;
        }
        CharSequence title = iMenuItemDelegate.getItemDataState().getTitle();
        Trace.beginSection("refreshMenuItem_" + ((Object) title));
        DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshMenuItem => " + ((Object) title));
        this.vPhotoPageMenu.refreshMenuItem(iMenuItemDelegate);
        this.mMenuItemHolder.updateMenuItem(iMenuItemDelegate);
        Trace.endSection();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void refreshNonResidentData(IMenuItemDelegate iMenuItemDelegate, boolean z) {
        DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshNonResidentData =>");
        this.mMenuItemHolder.onNonResidentDataChanged(iMenuItemDelegate, z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void requestFocus(IMenuItemDelegate iMenuItemDelegate) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return;
        }
        iPhotoPageMenu.viewRequestFocus(iMenuItemDelegate);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void toggleMoreActions(boolean z) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null || !this.mMenuIsShowing) {
            return;
        }
        iPhotoPageMenu.toggleMoreActions(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void release() {
        this.mMenuConfigurator.release();
        this.mEnableItemsFilter.release();
        this.mMenuItemHolder.release();
        this.mMenuItemOrderHelper.release();
    }

    /* loaded from: classes2.dex */
    public class MenuItemOrderHelper {
        public final Handler mHandler;
        public final HandlerThread mHandlerThread;

        public MenuItemOrderHelper() {
            PhotoPageMenuManager.this = r2;
            HandlerThread handlerThread = new HandlerThread("ItemOrderThread");
            this.mHandlerThread = handlerThread;
            handlerThread.start();
            this.mHandler = new Handler(handlerThread.getLooper());
        }

        public void orderItems(final ConcurrentHashMap<MenuItemType, FilterResult> concurrentHashMap, final BaseDataItem baseDataItem, final IFilterCallback iFilterCallback) {
            this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemOrderHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.MenuItemOrderHelper.this.lambda$orderItems$2(baseDataItem, concurrentHashMap, iFilterCallback);
                }
            });
        }

        public /* synthetic */ void lambda$orderItems$2(BaseDataItem baseDataItem, ConcurrentHashMap concurrentHashMap, IFilterCallback iFilterCallback) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "order start => %d", Long.valueOf(baseDataItem.getKey()));
            ArrayList<FilterResult> arrayList = new ArrayList<>();
            ArrayList<FilterResult> arrayList2 = new ArrayList<>();
            boolean isNeedCollapsed = PhotoPageMenuManager.this.isNeedCollapsed();
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                MenuConfigurator.ExpectInfo expectInfo = PhotoPageMenuManager.this.mMenuConfigurator.getExpectInfo((MenuItemType) entry.getKey(), PhotoPageMenuManager.this.mDataProvider.getFieldData(), baseDataItem, isNeedCollapsed);
                FilterResult filterResult = (FilterResult) entry.getValue();
                filterResult.setOrder(expectInfo.order);
                filterResult.setResident(expectInfo.isResident);
                if (filterResult.getSupport() && filterResult.getVisible()) {
                    if (filterResult.getResident()) {
                        arrayList.add(filterResult);
                    } else {
                        arrayList2.add(filterResult);
                    }
                }
            }
            arrayList.sort(PhotoPageMenuManager$MenuItemOrderHelper$$ExternalSyntheticLambda1.INSTANCE);
            arrayList2.sort(PhotoPageMenuManager$MenuItemOrderHelper$$ExternalSyntheticLambda2.INSTANCE);
            resetReallyOrder(arrayList);
            resetReallyOrder(arrayList2);
            DefaultLogger.d("PhotoPageFragment_MenuManager", "order end => %d", Long.valueOf(baseDataItem.getKey()));
            if (iFilterCallback != null) {
                iFilterCallback.onFilterFinished(baseDataItem, arrayList, arrayList2);
            }
        }

        public static /* synthetic */ int lambda$orderItems$0(FilterResult filterResult, FilterResult filterResult2) {
            return filterResult.getOrder() - filterResult2.getOrder();
        }

        public static /* synthetic */ int lambda$orderItems$1(FilterResult filterResult, FilterResult filterResult2) {
            return filterResult.getOrder() - filterResult2.getOrder();
        }

        public final void resetReallyOrder(ArrayList<FilterResult> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                FilterResult filterResult = arrayList.get(i);
                if (filterResult != null) {
                    filterResult.setOrder(i);
                }
            }
        }

        public void release() {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            }
            HandlerThread handlerThread = this.mHandlerThread;
            if (handlerThread != null) {
                handlerThread.quitSafely();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class MenuItemHolder implements IFilterCallback {
        public final ConcurrentHashMap<MenuItemType, IMenuItemDelegate> mMenuItemMap;
        public ArrayList<IMenuItemDelegate> mNonResident;
        public ArrayList<IMenuItemDelegate> mResident;

        /* renamed from: $r8$lambda$vhv-i6kLpTUXDSTABfylCpt6SXQ */
        public static /* synthetic */ void m1615$r8$lambda$vhvi6kLpTUXDSTABfylCpt6SXQ(ConcurrentHashMap concurrentHashMap, Map.Entry entry) {
            lambda$getDefaultPageItemsMap$2(concurrentHashMap, entry);
        }

        public MenuItemHolder() {
            PhotoPageMenuManager.this = r1;
            this.mMenuItemMap = new ConcurrentHashMap<>();
        }

        public void onConfiguredMenuItems(ConcurrentHashMap<MenuItemType, IMenuItemDelegate> concurrentHashMap, BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "onConfiguredMenuItems => ");
            saveMenuItemDefaultState(concurrentHashMap, iConfigMenuCallBack);
            this.mMenuItemMap.clear();
            this.mMenuItemMap.putAll(concurrentHashMap);
            initResidentEarlierUseFunction();
        }

        public final void saveMenuItemDefaultState(ConcurrentHashMap<MenuItemType, IMenuItemDelegate> concurrentHashMap, final BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack) {
            concurrentHashMap.entrySet().forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhotoPageMenuManager.MenuItemHolder.lambda$saveMenuItemDefaultState$0(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack.this, (Map.Entry) obj);
                }
            });
        }

        public static /* synthetic */ void lambda$saveMenuItemDefaultState$0(BasePhotoPageBarsDelegateFragment.IConfigMenuCallBack iConfigMenuCallBack, Map.Entry entry) {
            IMenuItemDelegate iMenuItemDelegate = (IMenuItemDelegate) entry.getValue();
            iMenuItemDelegate.saveDefaultState();
            iMenuItemDelegate.setConfigMenuCallBack(iConfigMenuCallBack);
        }

        public final void initResidentEarlierUseFunction() {
            FragmentActivity activity;
            final Favorite favorite = (Favorite) getItem(MenuItemType.FAVORITE);
            final Cast cast = (Cast) getItem(MenuItemType.CAST);
            if ((favorite == null && cast == null) || (activity = PhotoPageMenuManager.this.mOwner.getActivity()) == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.MenuItemHolder.this.lambda$initResidentEarlierUseFunction$1(favorite, cast);
                }
            });
        }

        public /* synthetic */ void lambda$initResidentEarlierUseFunction$1(Favorite favorite, Cast cast) {
            if (favorite != null) {
                PhotoPageMenuManager.this.lambda$initFunction$1(favorite);
            }
            if (cast != null) {
                PhotoPageMenuManager.this.lambda$initFunction$1(cast);
            }
        }

        public void uninstallFunctions() {
            ArrayList<IMenuItemDelegate> arrayList = this.mResident;
            if (arrayList != null && !arrayList.isEmpty()) {
                this.mResident.forEach(PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda6.INSTANCE);
            }
            ArrayList<IMenuItemDelegate> arrayList2 = this.mNonResident;
            if (arrayList2 == null || arrayList2.isEmpty()) {
                return;
            }
            this.mNonResident.forEach(PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda6.INSTANCE);
        }

        public boolean isConfiguredMenuItems() {
            return this.mMenuItemMap.size() > 0;
        }

        public ConcurrentHashMap<MenuItemType, FilterResult> getDefaultPageItemsMap() {
            final ConcurrentHashMap<MenuItemType, FilterResult> concurrentHashMap = new ConcurrentHashMap<>();
            this.mMenuItemMap.entrySet().forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhotoPageMenuManager.MenuItemHolder.m1615$r8$lambda$vhvi6kLpTUXDSTABfylCpt6SXQ(concurrentHashMap, (Map.Entry) obj);
                }
            });
            return concurrentHashMap;
        }

        public static /* synthetic */ void lambda$getDefaultPageItemsMap$2(ConcurrentHashMap concurrentHashMap, Map.Entry entry) {
            MenuItemType menuItemType = (MenuItemType) entry.getKey();
            AbstractMenuItemDelegate.ItemDataStateCache defaultState = ((IMenuItemDelegate) entry.getValue()).getDefaultState();
            FilterResult filterResult = new FilterResult();
            filterResult.setKey(menuItemType);
            filterResult.applyDefaultState(defaultState);
            concurrentHashMap.put(menuItemType, filterResult);
        }

        public ConcurrentHashMap<MenuItemType, IMenuItemDelegate> getCurrentPageItemsMap() {
            return this.mMenuItemMap;
        }

        public <I extends IMenuItemDelegate> I getItem(MenuItemType menuItemType) {
            I i = (I) this.mMenuItemMap.get(menuItemType);
            if (i != null) {
                return i;
            }
            return null;
        }

        public void release() {
            this.mMenuItemMap.clear();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IFilterCallback
        public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<FilterResult> arrayList, ArrayList<FilterResult> arrayList2) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "onFilterFinished => ");
            PhotoPageMenuManager.this.mMenuItemPrepareHelper.savePreparedResult(baseDataItem, arrayList, arrayList2);
            PhotoPageMenuManager.this.mMenuItemPrepareHelper.setCurrentApplyPreparedResultItemKey(baseDataItem.getKey());
            applyPreparedResult(baseDataItem, arrayList, arrayList2);
        }

        public void applyPreparedResult(final BaseDataItem baseDataItem, final ArrayList<FilterResult> arrayList, final ArrayList<FilterResult> arrayList2) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "applyPreparedResult => %d", Long.valueOf(baseDataItem.getKey()));
            ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.MenuItemHolder.this.lambda$applyPreparedResult$3(arrayList, arrayList2, baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$applyPreparedResult$3(ArrayList arrayList, ArrayList arrayList2, BaseDataItem baseDataItem) {
            Trace.beginSection("setFilterResult");
            ArrayList<IMenuItemDelegate> filterResult = setFilterResult(arrayList);
            ArrayList<IMenuItemDelegate> filterResult2 = setFilterResult(arrayList2);
            Trace.endSection();
            Trace.beginSection("updateData");
            updateData(filterResult, filterResult2);
            Trace.endSection();
            Trace.beginSection("refreshUI");
            refreshUI(baseDataItem, filterResult, filterResult2);
            Trace.endSection();
        }

        public final ArrayList<IMenuItemDelegate> setFilterResult(ArrayList<FilterResult> arrayList) {
            final ArrayList<IMenuItemDelegate> arrayList2 = new ArrayList<>();
            if (arrayList != null && !arrayList.isEmpty()) {
                final ConcurrentHashMap<MenuItemType, IMenuItemDelegate> currentPageItemsMap = PhotoPageMenuManager.this.mMenuItemHolder.getCurrentPageItemsMap();
                arrayList.forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda4
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhotoPageMenuManager.MenuItemHolder.this.lambda$setFilterResult$4(currentPageItemsMap, arrayList2, (FilterResult) obj);
                    }
                });
            }
            return arrayList2;
        }

        public /* synthetic */ void lambda$setFilterResult$4(ConcurrentHashMap concurrentHashMap, ArrayList arrayList, FilterResult filterResult) {
            IMenuItemDelegate iMenuItemDelegate;
            if (filterResult == null || filterResult.getKey() == null || (iMenuItemDelegate = (IMenuItemDelegate) concurrentHashMap.get(filterResult.getKey())) == null) {
                return;
            }
            Trace.beginSection("applyFilterResult");
            iMenuItemDelegate.applyFilterResult(filterResult);
            Trace.endSection();
            Trace.beginSection("initFunction");
            PhotoPageMenuManager.this.lambda$initFunction$1(iMenuItemDelegate);
            Trace.endSection();
            if (!iMenuItemDelegate.isSupport() || !iMenuItemDelegate.isVisible()) {
                return;
            }
            arrayList.add(iMenuItemDelegate);
        }

        public final void refreshUI(final BaseDataItem baseDataItem, final ArrayList<IMenuItemDelegate> arrayList, final ArrayList<IMenuItemDelegate> arrayList2) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "refreshUI =>");
            FragmentActivity activity = PhotoPageMenuManager.this.mOwner.getActivity();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemHolder$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.MenuItemHolder.this.lambda$refreshUI$5(arrayList, arrayList2, baseDataItem);
                }
            });
        }

        public /* synthetic */ void lambda$refreshUI$5(ArrayList arrayList, ArrayList arrayList2, BaseDataItem baseDataItem) {
            More more = (More) getItem(MenuItemType.MORE);
            if (more != null) {
                ArrayList arrayList3 = more.isResident() ? arrayList : arrayList2;
                if (BaseMiscUtil.isValid(arrayList3) && arrayList3.contains(more)) {
                    more.onFilterFinished(baseDataItem, arrayList, arrayList2);
                }
            }
            Cast cast = (Cast) getItem(MenuItemType.CAST);
            if (cast != null) {
                ArrayList arrayList4 = cast.isResident() ? arrayList : arrayList2;
                if (BaseMiscUtil.isValid(arrayList4) && arrayList4.contains(cast)) {
                    cast.onFilterFinished(baseDataItem, arrayList, arrayList2);
                }
            }
            Favorite favorite = (Favorite) getItem(MenuItemType.FAVORITE);
            if (favorite != null) {
                ArrayList arrayList5 = favorite.isResident() ? arrayList : arrayList2;
                if (BaseMiscUtil.isValid(arrayList5) && arrayList5.contains(favorite)) {
                    favorite.onFilterFinished(baseDataItem, arrayList, arrayList2);
                }
            }
            DownloadOriginal downloadOriginal = (DownloadOriginal) getItem(MenuItemType.DOWNLOAD_ORIGINAL);
            if (downloadOriginal != null) {
                ArrayList arrayList6 = downloadOriginal.isResident() ? arrayList : arrayList2;
                if (BaseMiscUtil.isValid(arrayList6) && arrayList6.contains(downloadOriginal)) {
                    downloadOriginal.onFilterFinished(baseDataItem, arrayList, arrayList2);
                }
            }
            GoogleLens googleLens = (GoogleLens) getItem(MenuItemType.GOOGLE_LENS);
            if (googleLens != null) {
                ArrayList arrayList7 = googleLens.isResident() ? arrayList : arrayList2;
                if (!BaseMiscUtil.isValid(arrayList7) || !arrayList7.contains(googleLens)) {
                    return;
                }
                googleLens.onFilterFinished(baseDataItem, arrayList, arrayList2);
            }
        }

        public final void updateData(ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "updateData =>");
            ArrayList<IMenuItemDelegate> arrayList3 = this.mResident;
            if (arrayList3 == null || arrayList3.size() != arrayList.size()) {
                this.mResident = arrayList;
                PhotoPageMenuManager.this.mDataProvider.getViewModelData().setResidentMenuItems(arrayList);
            }
            this.mNonResident = arrayList2;
            PhotoPageMenuManager.this.mDataProvider.getViewModelData().setNonResidentMenuItems(arrayList2);
        }

        public ArrayList<IMenuItemDelegate> getResident() {
            return this.mResident;
        }

        public void updateMenuItem(IMenuItemDelegate iMenuItemDelegate) {
            int indexOf;
            int indexOf2;
            ArrayList<IMenuItemDelegate> arrayList = this.mResident;
            if (arrayList != null && (indexOf2 = arrayList.indexOf(iMenuItemDelegate)) >= 0) {
                this.mResident.set(indexOf2, iMenuItemDelegate);
                return;
            }
            ArrayList<IMenuItemDelegate> arrayList2 = this.mNonResident;
            if (arrayList2 == null || (indexOf = arrayList2.indexOf(iMenuItemDelegate)) < 0) {
                return;
            }
            this.mNonResident.set(indexOf, iMenuItemDelegate);
            PhotoPageMenuManager.this.mDataProvider.getViewModelData().setNonResidentMenuItems(this.mNonResident);
        }

        public void onNonResidentDataChanged(IMenuItemDelegate iMenuItemDelegate, boolean z) {
            if (iMenuItemDelegate == null || iMenuItemDelegate.isResident()) {
                return;
            }
            if (z) {
                addNonResidentItem(iMenuItemDelegate);
            } else {
                removeNonResidentItem(iMenuItemDelegate);
            }
        }

        public final void addNonResidentItem(IMenuItemDelegate iMenuItemDelegate) {
            ArrayList<IMenuItemDelegate> arrayList = this.mNonResident;
            if (arrayList == null || arrayList.isEmpty() || this.mNonResident.indexOf(iMenuItemDelegate) >= 0) {
                return;
            }
            int i = 0;
            Iterator<IMenuItemDelegate> it = this.mNonResident.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().getOrder() > iMenuItemDelegate.getOrder()) {
                    this.mNonResident.add(i, iMenuItemDelegate);
                    break;
                } else {
                    i++;
                }
            }
            PhotoPageMenuManager.this.mDataProvider.getViewModelData().setNonResidentMenuItems(this.mNonResident);
        }

        public final void removeNonResidentItem(IMenuItemDelegate iMenuItemDelegate) {
            int indexOf;
            ArrayList<IMenuItemDelegate> arrayList = this.mNonResident;
            if (arrayList == null || arrayList.isEmpty() || (indexOf = this.mNonResident.indexOf(iMenuItemDelegate)) <= 0) {
                return;
            }
            this.mNonResident.remove(indexOf);
            PhotoPageMenuManager.this.mDataProvider.getViewModelData().setNonResidentMenuItems(this.mNonResident);
        }
    }

    /* loaded from: classes2.dex */
    public static class ItemEntry {
        public MenuItemType key;
        public IMenuItemDelegate value;

        public ItemEntry(MenuItemType menuItemType, IMenuItemDelegate iMenuItemDelegate) {
            this.key = menuItemType;
            this.value = iMenuItemDelegate;
        }
    }

    /* loaded from: classes2.dex */
    public class MenuConfigurator {
        public List<MenuItemType> mCollapsedCommonNonResidentExpect;
        public List<MenuItemType> mCollapsedResidentExpect;
        public List<MenuItemType> mCollapsedSecretNonResidentExpect;
        public List<MenuItemType> mCollapsedShareNonResidentExpect;
        public List<MenuItemType> mCollapsedTrashNonResidentExpect;
        public List<MenuItemType> mCollapsedUriNonResidentExpect;
        public List<MenuItemType> mCommonNonResidentExpect;
        public List<MenuItemType> mCommonResidentExpect;
        public ConfigMenuTask mConfigMenuTask;
        public List<MenuItemType> mSecretResidentExpect;
        public List<MenuItemType> mShareResidentExpect;
        public List<MenuItemType> mTrashResidentExpect;
        public List<MenuItemType> mUriResidentExpect;

        /* renamed from: $r8$lambda$aaLnQ_hJi0gmtNsSOISD-aVqAHc */
        public static /* synthetic */ void m1613$r8$lambda$aaLnQ_hJi0gmtNsSOISDaVqAHc(MenuConfigurator menuConfigurator, ConcurrentHashMap concurrentHashMap, IMenuItem iMenuItem) {
            menuConfigurator.lambda$buildMenuDataMap$0(concurrentHashMap, iMenuItem);
        }

        public MenuConfigurator() {
            PhotoPageMenuManager.this = r44;
            MenuItemType menuItemType = MenuItemType.SEND;
            MenuItemType menuItemType2 = MenuItemType.EDIT;
            MenuItemType menuItemType3 = MenuItemType.FAVORITE;
            MenuItemType menuItemType4 = MenuItemType.DELETE;
            MenuItemType menuItemType5 = MenuItemType.MORE;
            this.mCommonResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType2, menuItemType3, menuItemType4, menuItemType5));
            MenuItemType menuItemType6 = MenuItemType.ADD_CLOUD;
            MenuItemType menuItemType7 = MenuItemType.TO_PDF;
            MenuItemType menuItemType8 = MenuItemType.OCR;
            MenuItemType menuItemType9 = MenuItemType.CORRECT_DOC;
            MenuItemType menuItemType10 = MenuItemType.CAST;
            MenuItemType menuItemType11 = MenuItemType.SET_WALLPAPER;
            MenuItemType menuItemType12 = MenuItemType.SET_SLIDE_WALLPAPER;
            MenuItemType menuItemType13 = MenuItemType.SET_VIDEO_WALLPAPER;
            MenuItemType menuItemType14 = MenuItemType.PLAY_SLIDESHOW;
            MenuItemType menuItemType15 = MenuItemType.VIDEO_COMPRESS;
            MenuItemType menuItemType16 = MenuItemType.PHOTO_RENAME;
            MenuItemType menuItemType17 = MenuItemType.DOWNLOAD_ORIGINAL;
            MenuItemType menuItemType18 = MenuItemType.GOOGLE_LENS;
            MenuItemType menuItemType19 = MenuItemType.DETAILS;
            this.mCommonNonResidentExpect = new ArrayList(Arrays.asList(menuItemType6, menuItemType7, menuItemType8, menuItemType9, MenuItemType.WATERMARK, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
            MenuItemType menuItemType20 = MenuItemType.RECOVERY;
            MenuItemType menuItemType21 = MenuItemType.PURGE;
            this.mTrashResidentExpect = new ArrayList(Arrays.asList(menuItemType20, menuItemType21));
            this.mShareResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType4, menuItemType6, menuItemType5));
            MenuItemType menuItemType22 = MenuItemType.REMOVE_SECRET;
            this.mSecretResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType2, menuItemType4, menuItemType22, menuItemType5));
            MenuItemType menuItemType23 = MenuItemType.SAVE;
            this.mUriResidentExpect = new ArrayList(Arrays.asList(menuItemType23, menuItemType5));
            this.mCollapsedResidentExpect = new ArrayList(Collections.singletonList(menuItemType5));
            this.mCollapsedCommonNonResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType2, menuItemType3, menuItemType4, menuItemType6, menuItemType7, menuItemType8, menuItemType9, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
            this.mCollapsedShareNonResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType4, menuItemType6, menuItemType7, menuItemType8, menuItemType9, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
            this.mCollapsedSecretNonResidentExpect = new ArrayList(Arrays.asList(menuItemType, menuItemType2, menuItemType4, menuItemType22, menuItemType6, menuItemType7, menuItemType8, menuItemType9, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
            this.mCollapsedTrashNonResidentExpect = new ArrayList(Arrays.asList(menuItemType20, menuItemType21, menuItemType6, menuItemType7, menuItemType8, menuItemType9, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
            this.mCollapsedUriNonResidentExpect = new ArrayList(Arrays.asList(menuItemType23, menuItemType6, menuItemType7, menuItemType8, menuItemType9, menuItemType10, menuItemType11, menuItemType12, menuItemType13, menuItemType14, menuItemType15, menuItemType16, menuItemType17, menuItemType18, menuItemType19));
        }

        /* loaded from: classes2.dex */
        public class ConfigMenuTask implements Runnable {
            public WeakReference<IConfigMenuCallback> mCallback;
            public WeakReference<PhotoPageMenuManager> mManager;
            public IConfigMenuCallback menuCallback;
            public PhotoPageMenuManager menuManager;

            public ConfigMenuTask() {
                MenuConfigurator.this = r1;
            }

            public ConfigMenuTask(PhotoPageMenuManager photoPageMenuManager, IConfigMenuCallback iConfigMenuCallback) {
                MenuConfigurator.this = r1;
                this.mManager = new WeakReference<>(photoPageMenuManager);
                this.mCallback = new WeakReference<>(iConfigMenuCallback);
            }

            public final PhotoPageMenuManager getManager() {
                PhotoPageMenuManager photoPageMenuManager = this.menuManager;
                if (photoPageMenuManager != null) {
                    return photoPageMenuManager;
                }
                WeakReference<PhotoPageMenuManager> weakReference = this.mManager;
                if (weakReference == null || weakReference.get() == null) {
                    DefaultLogger.e("PhotoPageFragment_MenuManager", "menuManager weakReference has released ! =>");
                    return null;
                }
                return this.mManager.get();
            }

            public final IConfigMenuCallback getCallback() {
                IConfigMenuCallback iConfigMenuCallback = this.menuCallback;
                if (iConfigMenuCallback != null) {
                    return iConfigMenuCallback;
                }
                WeakReference<IConfigMenuCallback> weakReference = this.mCallback;
                if (weakReference == null || weakReference.get() == null) {
                    DefaultLogger.e("PhotoPageFragment_MenuManager", "menuCallback weakReference has released ! =>");
                    return null;
                }
                return this.mCallback.get();
            }

            public void setMenuManager(PhotoPageMenuManager photoPageMenuManager) {
                this.menuManager = photoPageMenuManager;
            }

            public void setMenuCallback(IConfigMenuCallback iConfigMenuCallback) {
                this.menuCallback = iConfigMenuCallback;
            }

            @Override // java.lang.Runnable
            public void run() {
                PhotoPageMenuManager manager = getManager();
                IConfigMenuCallback callback = getCallback();
                if (manager != null && callback != null) {
                    FragmentActivity activity = manager.mOwner.getActivity();
                    if (activity == null) {
                        DefaultLogger.e("PhotoPageFragment_MenuManager", "ConfigMenuTask error ! owner activity is null ! =>");
                        return;
                    }
                    DefaultLogger.d("PhotoPageFragment_MenuManager", "ConfigMenuTask run =>");
                    GalleryMenuBuilder galleryMenuBuilder = new GalleryMenuBuilder(activity);
                    activity.getMenuInflater().inflate(manager.mDataProvider.getFieldData().isFromTrash ? R.menu.trash_menu : R.menu.photo_page, galleryMenuBuilder);
                    ConcurrentHashMap<MenuItemType, IMenuItemDelegate> buildMenuDataMap = MenuConfigurator.this.buildMenuDataMap(galleryMenuBuilder, manager.mDataProvider.getFieldData().isFromTrash);
                    manager.mEnableItemsFilter.filterByConfig(buildMenuDataMap);
                    callback.configCompleted(buildMenuDataMap);
                    return;
                }
                DefaultLogger.d("PhotoPageFragment_MenuManager", "ConfigMenuTask error ！ manager or iConfigMenuCallback is null.");
            }

            public void release() {
                WeakReference<PhotoPageMenuManager> weakReference = this.mManager;
                if (weakReference != null) {
                    weakReference.clear();
                    this.mManager = null;
                }
                WeakReference<IConfigMenuCallback> weakReference2 = this.mCallback;
                if (weakReference2 != null) {
                    weakReference2.clear();
                    this.mCallback = null;
                }
            }
        }

        public void asyncConfigMenu(IConfigMenuCallback iConfigMenuCallback) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "asyncConfigMenu =>");
            if (this.mConfigMenuTask == null) {
                this.mConfigMenuTask = new ConfigMenuTask(PhotoPageMenuManager.this, iConfigMenuCallback);
            }
            ThreadManager.getWorkHandler().post(this.mConfigMenuTask);
        }

        public void syncConfigMenu(IConfigMenuCallback iConfigMenuCallback) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "syncConfigMenu =>");
            if (this.mConfigMenuTask != null) {
                ThreadManager.getWorkHandler().removeCallbacks(this.mConfigMenuTask);
                this.mConfigMenuTask.release();
            }
            ConfigMenuTask configMenuTask = new ConfigMenuTask();
            this.mConfigMenuTask = configMenuTask;
            configMenuTask.setMenuManager(PhotoPageMenuManager.this);
            this.mConfigMenuTask.setMenuCallback(iConfigMenuCallback);
            this.mConfigMenuTask.run();
        }

        public void release() {
            ConfigMenuTask configMenuTask = this.mConfigMenuTask;
            if (configMenuTask != null) {
                configMenuTask.release();
                ThreadManager.getWorkHandler().removeCallbacks(this.mConfigMenuTask);
            }
        }

        public final ConcurrentHashMap<MenuItemType, IMenuItemDelegate> buildMenuDataMap(GalleryMenuBuilder galleryMenuBuilder, boolean z) {
            final ConcurrentHashMap<MenuItemType, IMenuItemDelegate> concurrentHashMap = new ConcurrentHashMap<>();
            galleryMenuBuilder.getItems().forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuConfigurator$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhotoPageMenuManager.MenuConfigurator.m1613$r8$lambda$aaLnQ_hJi0gmtNsSOISDaVqAHc(PhotoPageMenuManager.MenuConfigurator.this, concurrentHashMap, (IMenuItem) obj);
                }
            });
            if (!z) {
                GalleryMenuItem galleryMenuItem = new GalleryMenuItem(galleryMenuBuilder, 0, R.id.more, 4, GalleryApp.sGetAndroidContext().getResources().getString(R.string.more));
                galleryMenuItem.setIcon(R.drawable.button_more_light);
                galleryMenuItem.setVisible(true);
                galleryMenuItem.setEnabled(true);
                ItemEntry build = build(galleryMenuItem);
                galleryMenuItem.getIcon();
                concurrentHashMap.put(build.key, build.value);
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager", "buildMenuDataMap itemMap.size()：" + concurrentHashMap.size());
            return concurrentHashMap;
        }

        public /* synthetic */ void lambda$buildMenuDataMap$0(ConcurrentHashMap concurrentHashMap, IMenuItem iMenuItem) {
            ItemEntry build = build(iMenuItem);
            iMenuItem.getIcon();
            concurrentHashMap.put(build.key, build.value);
        }

        /* loaded from: classes2.dex */
        public class ExpectInfo {
            public boolean isResident;
            public int order;

            public ExpectInfo() {
                MenuConfigurator.this = r1;
            }
        }

        public final ExpectInfo getExpectInfo(MenuItemType menuItemType, DataProvider.FieldData fieldData, BaseDataItem baseDataItem, boolean z) {
            if (fieldData.isFromTrash) {
                return getExpectInfoInternal(menuItemType, z ? this.mCollapsedResidentExpect : this.mTrashResidentExpect, z ? this.mCollapsedTrashNonResidentExpect : this.mCommonNonResidentExpect);
            } else if (baseDataItem instanceof UriItem) {
                return getExpectInfoInternal(menuItemType, z ? this.mCollapsedResidentExpect : this.mUriResidentExpect, z ? this.mCollapsedUriNonResidentExpect : this.mCommonNonResidentExpect);
            } else {
                boolean z2 = baseDataItem instanceof CloudItem;
                if (z2 && ((CloudItem) baseDataItem).isShare()) {
                    return getExpectInfoInternal(menuItemType, z ? this.mCollapsedResidentExpect : this.mShareResidentExpect, z ? this.mCollapsedShareNonResidentExpect : this.mCommonNonResidentExpect);
                } else if (z2 && ((CloudItem) baseDataItem).isSecret()) {
                    return getExpectInfoInternal(menuItemType, z ? this.mCollapsedResidentExpect : this.mSecretResidentExpect, z ? this.mCollapsedSecretNonResidentExpect : this.mCommonNonResidentExpect);
                } else {
                    return getExpectInfoInternal(menuItemType, z ? this.mCollapsedResidentExpect : this.mCommonResidentExpect, z ? this.mCollapsedCommonNonResidentExpect : this.mCommonNonResidentExpect);
                }
            }
        }

        public final ExpectInfo getExpectInfoInternal(MenuItemType menuItemType, List<MenuItemType> list, List<MenuItemType> list2) {
            ExpectInfo expectInfo = new ExpectInfo();
            int indexOf = list.indexOf(menuItemType);
            expectInfo.isResident = true;
            if (indexOf < 0) {
                indexOf = list2.indexOf(menuItemType);
                expectInfo.isResident = false;
                if (indexOf < 0) {
                    indexOf = Integer.MAX_VALUE;
                }
            }
            expectInfo.order = indexOf;
            return expectInfo;
        }

        public final ItemEntry build(IMenuItem iMenuItem) {
            switch (iMenuItem.getItemId()) {
                case R.id.action_OCR /* 2131361842 */:
                    return new ItemEntry(MenuItemType.OCR, Ocr.instance(iMenuItem));
                case R.id.action_add_cloud /* 2131361843 */:
                    return new ItemEntry(MenuItemType.ADD_CLOUD, AddCloud.instance(iMenuItem));
                case R.id.action_cast /* 2131361863 */:
                    return new ItemEntry(MenuItemType.CAST, Cast.instance(iMenuItem));
                case R.id.action_correct_doc /* 2131361871 */:
                    return new ItemEntry(MenuItemType.CORRECT_DOC, CorrectDoc.instance(iMenuItem));
                case R.id.action_delete /* 2131361872 */:
                    return new ItemEntry(MenuItemType.DELETE, Delete.instance(iMenuItem));
                case R.id.action_details /* 2131361873 */:
                    return new ItemEntry(MenuItemType.DETAILS, Details.instance(iMenuItem));
                case R.id.action_download_original /* 2131361875 */:
                    return new ItemEntry(MenuItemType.DOWNLOAD_ORIGINAL, DownloadOriginal.instance(iMenuItem));
                case R.id.action_edit /* 2131361876 */:
                    return new ItemEntry(MenuItemType.EDIT, Edit.instance(iMenuItem));
                case R.id.action_favorite /* 2131361877 */:
                    return new ItemEntry(MenuItemType.FAVORITE, Favorite.instance(iMenuItem));
                case R.id.action_google_lens /* 2131361878 */:
                    return new ItemEntry(MenuItemType.GOOGLE_LENS, GoogleLens.instance(iMenuItem));
                case R.id.action_photo_rename /* 2131361892 */:
                    return new ItemEntry(MenuItemType.PHOTO_RENAME, PhotoRename.instance(iMenuItem));
                case R.id.action_pic_to_pdf /* 2131361893 */:
                    return new ItemEntry(MenuItemType.TO_PDF, ToPdf.instance(iMenuItem));
                case R.id.action_play_slideshow /* 2131361894 */:
                    return new ItemEntry(MenuItemType.PLAY_SLIDESHOW, PlaySlideShow.instance(iMenuItem));
                case R.id.action_remove_secret /* 2131361898 */:
                    return new ItemEntry(MenuItemType.REMOVE_SECRET, RemoveSecret.instance(iMenuItem));
                case R.id.action_save /* 2131361900 */:
                    return new ItemEntry(MenuItemType.SAVE, Save.instance(iMenuItem));
                case R.id.action_send /* 2131361903 */:
                    return new ItemEntry(MenuItemType.SEND, Send.instance(iMenuItem));
                case R.id.action_set_slide_wallpaper /* 2131361905 */:
                    return new ItemEntry(MenuItemType.SET_SLIDE_WALLPAPER, SetSlideWallPaper.instance(iMenuItem));
                case R.id.action_set_video_wallpaper /* 2131361906 */:
                    return new ItemEntry(MenuItemType.SET_VIDEO_WALLPAPER, SetVideoWallPaper.instance(iMenuItem));
                case R.id.action_set_wallpaper /* 2131361907 */:
                    return new ItemEntry(MenuItemType.SET_WALLPAPER, SetWallPaper.instance(iMenuItem));
                case R.id.action_video_compress /* 2131361910 */:
                    return new ItemEntry(MenuItemType.VIDEO_COMPRESS, VideoCompress.instance(iMenuItem));
                case R.id.action_watermark /* 2131361911 */:
                    return new ItemEntry(MenuItemType.WATERMARK, WaterMark.instance(iMenuItem));
                case R.id.purge /* 2131363144 */:
                    return new ItemEntry(MenuItemType.PURGE, Purge.instance(iMenuItem));
                case R.id.recovery /* 2131363176 */:
                    return new ItemEntry(MenuItemType.RECOVERY, Recovery.instance(iMenuItem));
                default:
                    return new ItemEntry(MenuItemType.MORE, More.instance(iMenuItem));
            }
        }
    }

    /* loaded from: classes2.dex */
    public class MenuEnableItemsFilter {
        public Disposable mDisposable;
        public final PublishProcessor<MenuConfigInfo> mPublishProcessor = PublishProcessor.create();
        public final MenuFilterController mMenuFilterController = new MenuFilterController();
        public final ExecutorService mFilterExecutor = Executors.newSingleThreadExecutor();

        public static /* synthetic */ MenuConfigInfo $r8$lambda$0MZHiH3ufNRiQHbtTdidoB3LBFw(MenuEnableItemsFilter menuEnableItemsFilter, MenuConfigInfo menuConfigInfo) {
            return menuEnableItemsFilter.lambda$subscribe$0(menuConfigInfo);
        }

        public MenuEnableItemsFilter() {
            PhotoPageMenuManager.this = r1;
        }

        public void filterByConfig(ConcurrentHashMap<MenuItemType, IMenuItemDelegate> concurrentHashMap) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "filterByConfig =>");
            this.mMenuFilterController.filterByConfig(concurrentHashMap);
        }

        public void filter(BaseDataItem baseDataItem, IFilterCallback iFilterCallback) {
            ExtraParams extraParams = new ExtraParams();
            extraParams.setPhotoRenameAble(PhotoPageMenuManager.this.mDataProvider.getFieldData().mArguments.getBoolean("photodetail_is_photo_renamable", true));
            extraParams.setOperationMask(PhotoPageMenuManager.this.mDataProvider.getFieldData().mOperationMask);
            extraParams.setStartWhenLockedAndSecret(PhotoPageMenuManager.this.mDataProvider.getFieldData().isStartWhenLockedAndSecret);
            Disposable disposable = this.mDisposable;
            if (disposable == null || disposable.isDisposed()) {
                subscribe();
            }
            this.mPublishProcessor.onNext(new MenuConfigInfo(PhotoPageMenuManager.this.mDataProvider.getFieldData().mEnterType, baseDataItem, extraParams, iFilterCallback));
        }

        public void subscribe() {
            this.mDisposable = this.mPublishProcessor.observeOn(Schedulers.from(this.mFilterExecutor)).map(new Function() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuEnableItemsFilter$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return PhotoPageMenuManager.MenuEnableItemsFilter.$r8$lambda$0MZHiH3ufNRiQHbtTdidoB3LBFw(PhotoPageMenuManager.MenuEnableItemsFilter.this, (PhotoPageMenuManager.MenuEnableItemsFilter.MenuConfigInfo) obj);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe();
        }

        public /* synthetic */ MenuConfigInfo lambda$subscribe$0(MenuConfigInfo menuConfigInfo) throws Exception {
            ConcurrentHashMap<MenuItemType, FilterResult> defaultPageItemsMap = PhotoPageMenuManager.this.mMenuItemHolder.getDefaultPageItemsMap();
            this.mMenuFilterController.filter(defaultPageItemsMap, menuConfigInfo.enterType, menuConfigInfo.baseDataItem, menuConfigInfo.extraParams);
            PhotoPageMenuManager.this.mMenuItemOrderHelper.orderItems(defaultPageItemsMap, menuConfigInfo.baseDataItem, menuConfigInfo.callback);
            return menuConfigInfo;
        }

        public void release() {
            Disposable disposable = this.mDisposable;
            if (disposable == null || disposable.isDisposed()) {
                return;
            }
            this.mDisposable.dispose();
        }

        /* loaded from: classes2.dex */
        public class MenuConfigInfo {
            public BaseDataItem baseDataItem;
            public IFilterCallback callback;
            public EnterTypeUtils.EnterType enterType;
            public ExtraParams extraParams;

            public MenuConfigInfo(EnterTypeUtils.EnterType enterType, BaseDataItem baseDataItem, ExtraParams extraParams, IFilterCallback iFilterCallback) {
                MenuEnableItemsFilter.this = r1;
                this.enterType = enterType;
                this.baseDataItem = baseDataItem;
                this.extraParams = extraParams;
                this.callback = iFilterCallback;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class MenuClickHelper implements IMenuItemDelegate.ClickHelper {
        public final boolean isOperationWithoutKeyGuard(int i) {
            return i == R.id.action_send || i == R.id.action_edit || i == R.id.action_set_wallpaper || i == R.id.action_add_cloud;
        }

        public MenuClickHelper() {
            PhotoPageMenuManager.this = r1;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate.ClickHelper
        public boolean onMenuItemClick(IMenuItemDelegate iMenuItemDelegate) {
            DefaultLogger.d("PhotoPageFragment_MenuManager", "onMenuItemClick");
            if (!(iMenuItemDelegate instanceof More)) {
                PhotoPageMenuManager.this.hideMoreActions(!(iMenuItemDelegate instanceof Edit));
            }
            BaseDataItem dataItem = PhotoPageMenuManager.this.mDataProvider.getFieldData().mCurrent.getDataItem();
            if (dataItem != null && !isProcessing()) {
                PhotoPageMenuManager.this.mOwner.checkDismissKeyGuard(isOperationWithoutKeyGuard(iMenuItemDelegate.getItemDataState().getItemId()));
                LinearMotorHelper.performHapticFeedback(PhotoPageMenuManager.this.mDataProvider.getFieldData().mCurrent.itemView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                iMenuItemDelegate.onClick(dataItem);
            }
            return false;
        }

        public final boolean isProcessing() {
            return PhotoPageMenuManager.this.mOwner.prohibitOperateProcessingItem(PhotoPageMenuManager.this.mDataProvider.getFieldData().mCurrent.getDataItem());
        }
    }

    /* loaded from: classes2.dex */
    public class MenuItemPrepareHelper extends DataPrepareHelper implements IFilterCallback {
        public long mCurrentApplyPreparedResultItemKey;
        public final ConcurrentHashMap<Long, PrepareResult> mPrepareDataMap;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MenuItemPrepareHelper(IDataProvider iDataProvider, FragmentActivity fragmentActivity, LifecycleOwner lifecycleOwner) {
            super(iDataProvider, fragmentActivity, lifecycleOwner);
            PhotoPageMenuManager.this = r1;
            this.mPrepareDataMap = new ConcurrentHashMap<>();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.data.DataPrepareHelper
        public void doPrepare(BaseDataItem baseDataItem, int i) {
            if (!PhotoPageMenuManager.this.mMenuItemHolder.isConfiguredMenuItems()) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager", "prepareMenuData prepare => %d", Long.valueOf(baseDataItem.getKey()));
            PhotoPageMenuManager.this.mEnableItemsFilter.filter(baseDataItem, this);
        }

        @Override // com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager.IFilterCallback
        public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<FilterResult> arrayList, ArrayList<FilterResult> arrayList2) {
            savePreparedResult(baseDataItem, arrayList, arrayList2);
        }

        public void savePreparedResult(final BaseDataItem baseDataItem, ArrayList<FilterResult> arrayList, ArrayList<FilterResult> arrayList2) {
            if (baseDataItem == null) {
                return;
            }
            DefaultLogger.d("PhotoPageFragment_MenuManager", "prepareMenuData save => %d", Long.valueOf(baseDataItem.getKey()));
            this.mPrepareDataMap.put(Long.valueOf(baseDataItem.getKey()), new PrepareResult(arrayList, arrayList2));
            if (BaseMiscUtil.isValid(arrayList)) {
                arrayList.forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemPrepareHelper$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        PhotoPageMenuManager.MenuItemPrepareHelper.this.lambda$savePreparedResult$0(baseDataItem, (FilterResult) obj);
                    }
                });
            }
            if (!BaseMiscUtil.isValid(arrayList2)) {
                return;
            }
            arrayList2.forEach(new Consumer() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$MenuItemPrepareHelper$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    PhotoPageMenuManager.MenuItemPrepareHelper.this.lambda$savePreparedResult$1(baseDataItem, (FilterResult) obj);
                }
            });
        }

        public /* synthetic */ void lambda$savePreparedResult$0(BaseDataItem baseDataItem, FilterResult filterResult) {
            IMenuItemDelegate item = PhotoPageMenuManager.this.mMenuItemHolder.getItem(filterResult.getKey());
            if (item != null) {
                item.prepareMenuData(baseDataItem, filterResult);
            }
        }

        public /* synthetic */ void lambda$savePreparedResult$1(BaseDataItem baseDataItem, FilterResult filterResult) {
            IMenuItemDelegate item = PhotoPageMenuManager.this.mMenuItemHolder.getItem(filterResult.getKey());
            if (item != null) {
                item.prepareMenuData(baseDataItem, filterResult);
            }
        }

        public boolean isPreparedResultItem(BaseDataItem baseDataItem) {
            PrepareResult prepareResult = this.mPrepareDataMap.get(Long.valueOf(baseDataItem.getKey()));
            return (prepareResult == null || prepareResult.resident == null || prepareResult.nonResident == null) ? false : true;
        }

        public boolean isApplyPreparedResult(BaseDataItem baseDataItem) {
            return this.mPrepareDataMap.get(Long.valueOf(baseDataItem.getKey())) != null && this.mCurrentApplyPreparedResultItemKey == baseDataItem.getKey();
        }

        public void applyPreparedResult(BaseDataItem baseDataItem) {
            PrepareResult prepareResult = this.mPrepareDataMap.get(Long.valueOf(baseDataItem.getKey()));
            if (prepareResult != null) {
                PhotoPageMenuManager.this.mMenuItemHolder.applyPreparedResult(baseDataItem, prepareResult.resident, prepareResult.nonResident);
                this.mCurrentApplyPreparedResultItemKey = baseDataItem.getKey();
                return;
            }
            DefaultLogger.e("PhotoPageFragment_MenuManager", "prepareResult is null.");
        }

        public void setCurrentApplyPreparedResultItemKey(long j) {
            this.mCurrentApplyPreparedResultItemKey = j;
        }

        public void clearPreparedResult(BaseDataItem baseDataItem) {
            this.mCurrentApplyPreparedResultItemKey = 0L;
            if (baseDataItem == null || !this.mPrepareDataMap.containsKey(Long.valueOf(baseDataItem.getKey()))) {
                return;
            }
            this.mPrepareDataMap.remove(Long.valueOf(baseDataItem.getKey()));
        }

        public void clearAllPreparedResult() {
            if (BaseMiscUtil.isValid(this.mPrepareDataMap)) {
                this.mPrepareDataMap.clear();
            }
            this.mCurrentApplyPreparedResultItemKey = 0L;
            DefaultLogger.d("PhotoPageFragment_MenuManager", "clear All Prepared Result");
        }

        /* loaded from: classes2.dex */
        public class PrepareResult {
            public ArrayList<FilterResult> nonResident;
            public ArrayList<FilterResult> resident;

            public PrepareResult(ArrayList<FilterResult> arrayList, ArrayList<FilterResult> arrayList2) {
                MenuItemPrepareHelper.this = r1;
                this.resident = arrayList;
                this.nonResident = arrayList2;
            }
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public IMenuOwner getMenuOwner() {
        return this.mOwner;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public boolean isSupportPhotoRename() {
        PhotoRename photoRename = (PhotoRename) this.mMenuItemHolder.getItem(MenuItemType.PHOTO_RENAME);
        return photoRename != null && photoRename.isSupport();
    }

    public boolean isSupportDelete() {
        Delete delete = (Delete) this.mMenuItemHolder.getItem(MenuItemType.DELETE);
        return delete != null && delete.isSupport();
    }

    public boolean isSupportPurge() {
        Purge purge = (Purge) this.mMenuItemHolder.getItem(MenuItemType.PURGE);
        return purge != null && purge.isSupport();
    }

    public void doPurge() {
        this.mMenuClickHandler.onMenuItemClick((Purge) this.mMenuItemHolder.getItem(MenuItemType.PURGE));
    }

    public void doDelete() {
        this.mMenuClickHandler.onMenuItemClick((Delete) this.mMenuItemHolder.getItem(MenuItemType.DELETE));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public boolean isFavorite() {
        Favorite favorite = (Favorite) this.mMenuItemHolder.getItem(MenuItemType.FAVORITE);
        return favorite != null && favorite.isChecked();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public boolean isNeedDownloadOriginal() {
        DownloadOriginal downloadOriginal = (DownloadOriginal) this.mMenuItemHolder.getItem(MenuItemType.DOWNLOAD_ORIGINAL);
        return downloadOriginal != null && downloadOriginal.isVisible() && downloadOriginal.isSupport();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public boolean isNeedProjectEnter() {
        Cast cast = (Cast) this.mMenuItemHolder.getItem(MenuItemType.CAST);
        if (cast == null || !cast.isConnected() || this.mOwner.isSecretAlbum()) {
            return false;
        }
        cast.enterSlideShow(this.mDataProvider.getFieldData().mCurrent.getPosition());
        return true;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void downloadOrigin(BaseDataItem baseDataItem, DownloadOriginal.DownloadCallback downloadCallback) {
        DownloadOriginal downloadOriginal = (DownloadOriginal) this.mMenuItemHolder.getItem(MenuItemType.DOWNLOAD_ORIGINAL);
        if (downloadOriginal != null) {
            downloadOriginal.downloadOrigin(baseDataItem, downloadCallback);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void onProjectedClicked() {
        Cast cast = (Cast) this.mMenuItemHolder.getItem(MenuItemType.CAST);
        if (cast != null) {
            cast.projectClicked();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuItemManager
    public void refreshProjectState() {
        Cast cast = (Cast) this.mMenuItemHolder.getItem(MenuItemType.CAST);
        if (cast != null) {
            cast.refreshProjectState();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public Cast.ProjectionManager checkAndCreateProjectionManager() {
        Cast cast = (Cast) this.mMenuItemHolder.getItem(MenuItemType.CAST);
        if (cast != null) {
            lambda$initFunction$1(cast);
            return cast.getProjectionManager();
        }
        return null;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void setFrameBar(VideoFrameSeekBar videoFrameSeekBar) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return;
        }
        iPhotoPageMenu.setFrameBar(videoFrameSeekBar);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void showMenuView(boolean z) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return;
        }
        this.mMenuIsShowing = true;
        iPhotoPageMenu.setVisibility(0);
        this.vPhotoPageMenu.showMenuView(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void hideMenuView(boolean z) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return;
        }
        this.mMenuIsShowing = false;
        iPhotoPageMenu.hideMenuView(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public boolean isMoreActionsShown() {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        return iPhotoPageMenu != null && iPhotoPageMenu.isMoreActionsShown();
    }

    /* renamed from: showMoreActions */
    public void lambda$restoreMoreActions$4(boolean z) {
        if (this.vPhotoPageMenu != null) {
            IMenuOwner iMenuOwner = this.mOwner;
            if (iMenuOwner != null && iMenuOwner.getActivity() != null && (this.mOwner.getActivity().isFinishing() || this.mOwner.getActivity().isDestroyed())) {
                return;
            }
            this.vPhotoPageMenu.showMoreActions(z);
            DefaultLogger.d("PhotoPageFragment_MenuManager", "showMoreActions withAnim: %b", Boolean.valueOf(z));
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void hideMoreActions(boolean z) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return;
        }
        iPhotoPageMenu.hideMoreActions(z);
        DefaultLogger.d("PhotoPageFragment_MenuManager", "hideMoreActions withAnim: %b", Boolean.valueOf(z));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public void restoreMoreActions(final boolean z) {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu != null && this.mNeedRestoreMoreActions) {
            iPhotoPageMenu.postDelayed(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoPageMenuManager.$r8$lambda$l39Ba5NmygwraiQgkRo22IejMa0(PhotoPageMenuManager.this, z);
                }
            }, 250L);
            this.mNeedRestoreMoreActions = false;
            DefaultLogger.d("PhotoPageFragment_MenuManager", "restoreMoreActions");
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public int getMenuCollapsedHeight() {
        IPhotoPageMenu iPhotoPageMenu = this.vPhotoPageMenu;
        if (iPhotoPageMenu == null) {
            return 0;
        }
        return iPhotoPageMenu.getMenuCollapsedHeight();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public boolean isInTalkBackModel() {
        return this.mOwner.isInTalkBackModel();
    }

    public final boolean isNeedCollapsed() {
        try {
            return isInMultiWindowMode() && (this.mOwner.getCustomViewType() == ActionBarCustomViewBuilder.CustomViewType.TopMenu);
        } catch (Exception e) {
            DefaultLogger.e("PhotoPageFragment_MenuManager", e);
            return false;
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public boolean isInMultiWindowMode() {
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null) {
            return false;
        }
        return iDataProvider.getFieldData().mCurrent.isInMultiWindowMode();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public boolean isVideoPlayerSupportActionBarAdjust() {
        IDataProvider iDataProvider = this.mDataProvider;
        if (iDataProvider == null) {
            return false;
        }
        return iDataProvider.getFieldData().isVideoPlayerSupportActionBarAdjust;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager
    public boolean isActivityActive() {
        IMenuOwner iMenuOwner = this.mOwner;
        return iMenuOwner != null && iMenuOwner.getActivity() != null && !this.mOwner.getActivity().isFinishing() && !this.mOwner.getActivity().isDestroyed();
    }
}
