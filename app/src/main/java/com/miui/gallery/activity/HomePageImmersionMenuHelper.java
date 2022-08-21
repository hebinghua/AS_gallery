package com.miui.gallery.activity;

import android.content.res.Configuration;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.ImmersionMenuSupport;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.ArtStillEntranceUtils;
import com.miui.gallery.util.ArtStillLibraryLoaderHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IDPhotoEntranceUtils;
import com.miui.gallery.util.IDPhotoLibraryLoaderHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.MagicMattingEntranceUtils;
import com.miui.gallery.util.MagicMattingLibraryLoaderHelper;
import com.miui.gallery.util.MovieLibraryLoaderHelper;
import com.miui.gallery.util.PhotoMovieEntranceUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.VideoPostDownloadManager;
import com.miui.gallery.util.VideoPostEntranceUtils;
import com.miui.gallery.util.VlogLibraryLoaderHelper;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.util.market.PrintInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.menu.ImmersionMenu;
import com.miui.gallery.widget.menu.ImmersionMenuItem;
import com.miui.gallery.widget.menu.ImmersionMenuListener;
import com.miui.gallery.widget.menu.PhoneImmersionMenu;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.miui.mediaeditor.api.MediaEditorIntentUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class HomePageImmersionMenuHelper implements ImmersionMenuListener {
    public LibraryLoaderHelper.DownloadStateListener mArtStillDownloadStateListener;
    public FragmentActivity mContext;
    public ImmersionMenuSupport mCurrentPage;
    public boolean mHasRedDotItem;
    public LibraryLoaderHelper.DownloadStateListener mIDPhotoDownloadStateListener;
    public PrintInstaller.InstallStateListener mInstallStateListener;
    public LibraryLoaderHelper.DownloadStateListener mMagicMattingDownloadStateListener;
    public MenuItemsCheckListener mMenuItemsCheckListener;
    public LibraryLoaderHelper.DownloadStateListener mMovieDownloadStateListener;
    public PhoneImmersionMenu mPhoneImmersionMenu;
    public PrintStatusObserver mPrintStatusObserver;
    public VideoPostDownloadManager.DownloadStateListener mVideoPostDownloadListener;
    public LibraryLoaderHelper.DownloadStateListener mVlogDownloadStateListener;
    public List<Integer> mCreativityList = new LinkedList();
    public boolean mIsCreativityFunctionOn = true;
    public Map<Integer, String> mFeatureItemsMap = new HashMap();

    /* loaded from: classes.dex */
    public interface MenuItemsCheckListener {
        void onMenuItemsChecked(boolean z);
    }

    /* renamed from: $r8$lambda$mvIA9km8pDUIGPwix25jr1I-q_I */
    public static /* synthetic */ void m458$r8$lambda$mvIA9km8pDUIGPwix25jr1Iq_I(HomePageImmersionMenuHelper homePageImmersionMenuHelper, boolean z) {
        homePageImmersionMenuHelper.lambda$onImmersionMenuSelected$0(z);
    }

    public HomePageImmersionMenuHelper(FragmentActivity fragmentActivity) {
        this.mContext = fragmentActivity;
        this.mPhoneImmersionMenu = new PhoneImmersionMenu(fragmentActivity, this);
        registerPrintStatusObserver();
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onCreateImmersionMenu(final ImmersionMenu immersionMenu) {
        FragmentActivity fragmentActivity;
        int i;
        if (this.mContext == null) {
            return;
        }
        if (MapInitializerImpl.checkMapAvailable()) {
            immersionMenu.add(R.id.menu_map, this.mContext.getString(R.string.map_album));
            registerFeature(R.id.menu_map, "map_album");
        }
        GalleryPreferences.HomePage.isHomePageShowAllPhotos();
        immersionMenu.add(R.id.menu_camera_or_all_photos, this.mContext.getString(R.string.home_menu_all_photos));
        if (!MediaFeatureManager.isDeviceSupportStoryFunction()) {
            if (VlogEntranceUtils.isAvailable()) {
                final ImmersionMenuItem add = immersionMenu.add(R.id.menu_vlog, this.mContext.getString(VlogUtils.getVlogNameResId()));
                registerFeature(R.id.menu_vlog, "vlog");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_vlog));
                this.mVlogDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.1
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        add.setRemainWhenClick(true);
                        add.setInformation(R.string.photo_vlog_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        add.setRemainWhenClick(false);
                        add.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                VlogLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mVlogDownloadStateListener);
            }
            immersionMenu.add(R.id.menu_collage, this.mContext.getString(R.string.home_menu_collage));
            registerFeature(R.id.menu_collage, "collage");
            this.mCreativityList.add(Integer.valueOf((int) R.id.menu_collage));
            if (PhotoMovieEntranceUtils.isDeviceSupportPhotoMovie()) {
                final ImmersionMenuItem add2 = immersionMenu.add(R.id.menu_photo_movie, this.mContext.getString(R.string.home_menu_photo_movie));
                registerFeature(R.id.menu_photo_movie, "photo_movie");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_photo_movie));
                this.mMovieDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.2
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        add2.setRemainWhenClick(true);
                        add2.setInformation(R.string.photo_movie_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        add2.setRemainWhenClick(false);
                        add2.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                MovieLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mMovieDownloadStateListener);
            }
            if (MacaronInstaller.isFunctionOn()) {
                immersionMenu.add(R.id.menu_macarons, this.mContext.getString(R.string.home_menu_macarons));
                registerFeature(R.id.menu_macarons, "macarons");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_macarons));
            }
            if (MagicMattingEntranceUtils.isAvailable()) {
                final ImmersionMenuItem add3 = immersionMenu.add(R.id.menu_magic_matting, this.mContext.getString(R.string.home_menu_magic_matting));
                registerFeature(R.id.menu_magic_matting, "magic_matting");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_magic_matting));
                this.mMagicMattingDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.3
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        add3.setRemainWhenClick(true);
                        add3.setInformation(R.string.photo_print_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        add3.setRemainWhenClick(false);
                        add3.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                MagicMattingLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mMagicMattingDownloadStateListener);
            }
            if (IDPhotoEntranceUtils.isAvailable()) {
                final ImmersionMenuItem add4 = immersionMenu.add(R.id.menu_id_photo, this.mContext.getString(R.string.home_menu_id_photo));
                registerFeature(R.id.menu_id_photo, "id_photo");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_id_photo));
                this.mIDPhotoDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.4
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        add4.setRemainWhenClick(true);
                        add4.setInformation(R.string.photo_print_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        add4.setRemainWhenClick(false);
                        add4.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                IDPhotoLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mIDPhotoDownloadStateListener);
            }
            if (ArtStillEntranceUtils.isAvailable()) {
                final ImmersionMenuItem add5 = immersionMenu.add(R.id.menu_art_still, this.mContext.getString(R.string.home_menu_art_still));
                registerFeature(R.id.menu_art_still, "art_still");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_art_still));
                this.mArtStillDownloadStateListener = new LibraryLoaderHelper.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.5
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onDownloading() {
                        add5.setRemainWhenClick(true);
                        add5.setInformation(R.string.photo_print_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.net.library.LibraryLoaderHelper.DownloadStateListener
                    public void onFinish(boolean z, int i2) {
                        add5.setRemainWhenClick(false);
                        add5.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                ArtStillLibraryLoaderHelper.getInstance().addDownloadStateListener(this.mArtStillDownloadStateListener);
            }
            if (VideoPostEntranceUtils.isAvailable()) {
                final ImmersionMenuItem add6 = immersionMenu.add(R.id.menu_video_post, this.mContext.getString(R.string.home_menu_video_post));
                registerFeature(R.id.menu_video_post, "video_post");
                this.mCreativityList.add(Integer.valueOf((int) R.id.menu_video_post));
                this.mVideoPostDownloadListener = new VideoPostDownloadManager.DownloadStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.6
                    {
                        HomePageImmersionMenuHelper.this = this;
                    }

                    @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
                    public void onDownloading() {
                        add6.setRemainWhenClick(true);
                        add6.setInformation(R.string.photo_print_menu_loading);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }

                    @Override // com.miui.gallery.util.VideoPostDownloadManager.DownloadStateListener
                    public void onFinish(boolean z) {
                        add6.setRemainWhenClick(false);
                        add6.setInformation((CharSequence) null);
                        HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
                    }
                };
                VideoPostDownloadManager.getInstance().setDownloadStateListener(this.mVideoPostDownloadListener);
            }
        }
        immersionMenu.add(R.id.menu_cleaner, this.mContext.getString(R.string.home_menu_cleaner));
        registerFeature(R.id.menu_cleaner, "photo_cleaner");
        immersionMenu.add(R.id.sort, this.mContext.getString(R.string.sort));
        if (AlbumPageConfig.getInstance().isGridPageMode()) {
            fragmentActivity = this.mContext;
            i = R.string.album_tab_page_switch_view_style_to_linear;
        } else {
            fragmentActivity = this.mContext;
            i = R.string.album_tab_page_switch_view_style_to_grid;
        }
        immersionMenu.add(R.id.change_album_show_mode, fragmentActivity.getString(i));
        final ImmersionMenuItem add7 = immersionMenu.add(R.id.menu_photo_print, this.mContext.getString(R.string.home_menu_print));
        registerFeature(R.id.menu_photo_print, "photo_print");
        add7.setVisible(PrintInstaller.getInstance().isPhotoPrintEnable());
        this.mInstallStateListener = new PrintInstaller.InstallStateListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper.7
            {
                HomePageImmersionMenuHelper.this = this;
            }

            @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
            public void onInstallLimited() {
                ToastUtils.makeText(HomePageImmersionMenuHelper.this.mContext, (int) R.string.try_again_later);
            }

            @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
            public void onInstalling() {
                add7.setRemainWhenClick(true);
                add7.setInformation(R.string.photo_print_menu_loading);
                HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
            }

            @Override // com.miui.gallery.util.market.PrintInstaller.InstallStateListener
            public void onFinish(boolean z, int i2, int i3) {
                if (z) {
                    ToastUtils.makeText(HomePageImmersionMenuHelper.this.mContext, (int) R.string.photo_print_package_finish);
                } else {
                    int failReasonMsg = PrintInstaller.getFailReasonMsg(i2, i3);
                    if (failReasonMsg != 0) {
                        ToastUtils.makeText(HomePageImmersionMenuHelper.this.mContext, failReasonMsg);
                    }
                }
                add7.setRemainWhenClick(false);
                add7.setInformation((CharSequence) null);
                HomePageImmersionMenuHelper.this.updateImmersionMenu(immersionMenu);
            }
        };
        PrintInstaller.getInstance().addInstallStateListener(this.mInstallStateListener);
        immersionMenu.add(R.id.menu_settings, this.mContext.getString(R.string.gallery_setting));
        registerFeature(R.id.menu_settings, "settings");
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public boolean onPrepareImmersionMenu(ImmersionMenu immersionMenu) {
        boolean z;
        if (this.mContext == null) {
            return false;
        }
        boolean isPhotoPrintEnable = PrintInstaller.getInstance().isPhotoPrintEnable();
        boolean menuVisibility = setMenuVisibility(immersionMenu.mo1822findItem(R.id.menu_photo_print), isPhotoPrintEnable);
        boolean menuVisibility2 = setMenuVisibility(immersionMenu.mo1822findItem(R.id.sort), (this.mCurrentPage.getSupportedAction() & 2) > 0);
        boolean menuVisibility3 = setMenuVisibility(immersionMenu.mo1822findItem(R.id.menu_cleaner), (this.mCurrentPage.getSupportedAction() & 8) > 0);
        boolean menuVisibility4 = setMenuVisibility(immersionMenu.mo1822findItem(R.id.change_album_show_mode), (this.mCurrentPage.getSupportedAction() & 4) > 0);
        boolean menuVisibility5 = setMenuVisibility(immersionMenu.mo1822findItem(R.id.menu_map), this.mContext.getResources().getConfiguration().smallestScreenWidthDp < BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD);
        ImmersionMenuItem mo1822findItem = immersionMenu.mo1822findItem(R.id.menu_camera_or_all_photos);
        boolean menuVisibility6 = setMenuVisibility(mo1822findItem, (this.mCurrentPage.getSupportedAction() & 16) > 0 && BaseBuildUtil.isLargeScreenDevice() && BaseBuildUtil.isLargeHorizontalWindow());
        mo1822findItem.setTitle(GalleryPreferences.HomePage.isHomePageShowAllPhotos() ? R.string.home_menu_camera : R.string.home_menu_all_photos);
        if (isPhotoPrintEnable) {
            SamplingStatHelper.recordCountEvent("photo_print", "photo_print_menu_displayed");
        }
        DisplayStatusManager.setRedDotClicked("action_bar_more");
        boolean isCreativityFunctionOn = GalleryPreferences.Assistant.isCreativityFunctionOn();
        if (MediaFeatureManager.isDeviceSupportStoryFunction() || isCreativityFunctionOn == this.mIsCreativityFunctionOn) {
            z = false;
        } else {
            this.mIsCreativityFunctionOn = isCreativityFunctionOn;
            for (Integer num : this.mCreativityList) {
                setMenuVisibility(num.intValue(), isCreativityFunctionOn);
            }
            z = true;
        }
        return menuVisibility || menuVisibility2 || menuVisibility4 || menuVisibility3 || menuVisibility6 || z || menuVisibility5;
    }

    public final boolean checkAndInstallMediaEditorIfNotExist() {
        return MediaEditorInstaller.getInstance().installIfNotExist(this.mContext, null, false);
    }

    public void onConfigurationChange(Configuration configuration) {
        setMenuVisibility(R.id.menu_map, configuration.smallestScreenWidthDp < BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuListener
    public void onImmersionMenuSelected(ImmersionMenu immersionMenu, ImmersionMenuItem immersionMenuItem) {
        if (this.mContext == null) {
            return;
        }
        int itemId = immersionMenuItem.getItemId();
        String str = this.mFeatureItemsMap.get(Integer.valueOf(itemId));
        if (itemId == R.id.menu_settings && DisplayStatusManager.getRedDotStatus("settings")) {
            TrackController.trackClick("403.29.0.1.16068");
        }
        if (str != null && !str.isEmpty()) {
            DisplayStatusManager.setRedDotClicked(str);
            HashMap hashMap = new HashMap();
            Locale locale = Locale.US;
            hashMap.put("page", String.format(locale, "%s_redDotDisplayed", this.mCurrentPage.getPageName()));
            hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(this.mHasRedDotItem));
            SamplingStatHelper.recordCountEvent("feature_red_dot", String.format(locale, "%s_%s", "click_menu", str), hashMap);
        }
        checkRedDotFeature();
        boolean isMediaEditorAvailable = MediaEditorUtils.isMediaEditorAvailable();
        switch (itemId) {
            case R.id.change_album_show_mode /* 2131362123 */:
                if (this.mCurrentPage.isActionSupport(4)) {
                    this.mCurrentPage.onActionClick(4);
                }
                TrackController.trackClick("403.29.0.1.10327", AutoTracking.getRef());
                immersionMenuItem.setTitle(AlbumPageConfig.getInstance().isGridPageMode() ? R.string.album_tab_page_switch_view_style_to_linear : R.string.album_tab_page_switch_view_style_to_grid);
                return;
            case R.id.menu_art_still /* 2131362877 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportArtStill()) {
                    MediaEditorIntentUtils.startActivityWithTag(this.mContext, "artStill");
                    return;
                } else if (!ArtStillLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                    return;
                } else {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_ART_STILL);
                    return;
                }
            case R.id.menu_camera_or_all_photos /* 2131362879 */:
                if (this.mCurrentPage.isActionSupport(16)) {
                    this.mCurrentPage.onActionClick(16);
                }
                immersionMenuItem.setTitle(GalleryPreferences.HomePage.isHomePageShowAllPhotos() ? R.string.home_menu_camera : R.string.home_menu_all_photos);
                return;
            case R.id.menu_cleaner /* 2131362882 */:
                ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_CLEANER_PAGE);
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_cleaner");
                TrackController.trackClick("403.29.0.1.10321", AutoTracking.getRef());
                return;
            case R.id.menu_collage /* 2131362883 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable) {
                    MediaEditorIntentUtils.startCollagePicker(this.mContext);
                } else {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_COLLAGE_PAGE);
                }
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_collage");
                return;
            case R.id.menu_id_photo /* 2131362895 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportIDPhoto()) {
                    MediaEditorIntentUtils.startActivityWithTag(this.mContext, "idPhoto");
                    return;
                } else if (!IDPhotoLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                    return;
                } else {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_ID_PHOTO);
                    return;
                }
            case R.id.menu_macarons /* 2131362897 */:
                IntentUtil.startMacaronsPicker(this.mContext);
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_macaron");
                return;
            case R.id.menu_magic_matting /* 2131362898 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportMagicMatting()) {
                    MediaEditorIntentUtils.startMagicMattingFromPickerTemp(this.mContext);
                    return;
                } else if (!MagicMattingLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                    return;
                } else {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_MAGIC_MATTING);
                    return;
                }
            case R.id.menu_map /* 2131362899 */:
                TrackController.trackClick("403.29.0.1.15378", AutoTracking.getRef());
                if (AgreementsUtils.isNetworkingAgreementAccepted()) {
                    IntentUtil.goToMapAlbumDirectly(this.mContext);
                    return;
                } else {
                    AgreementsUtils.showUserAgreements(this.mContext, new OnAgreementInvokedListener() { // from class: com.miui.gallery.activity.HomePageImmersionMenuHelper$$ExternalSyntheticLambda0
                        @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                        public final void onAgreementInvoked(boolean z) {
                            HomePageImmersionMenuHelper.m458$r8$lambda$mvIA9km8pDUIGPwix25jr1Iq_I(HomePageImmersionMenuHelper.this, z);
                        }
                    });
                    return;
                }
            case R.id.menu_photo_movie /* 2131362907 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportPhotoMovie() && PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK() && MediaEditorApiHelper.isDeviceSupportPhotoMovie()) {
                    MediaEditorIntentUtils.startPickerForPhotoMovieTemp(this.mContext);
                } else if (MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_PHOTO_MOVIE);
                }
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_photo_movie");
                return;
            case R.id.menu_photo_print /* 2131362908 */:
                PrintInstaller.getInstance().start(this.mContext);
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_photo_print");
                return;
            case R.id.menu_settings /* 2131362918 */:
                IntentUtil.enterGallerySetting(this.mContext);
                TrackController.trackClick("403.29.0.1.10322", AutoTracking.getRef());
                return;
            case R.id.menu_video_post /* 2131362926 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportVideoPost()) {
                    MediaEditorIntentUtils.startActivityWithTag(this.mContext, "videoPost");
                    return;
                } else if (!VideoPostDownloadManager.getInstance().checkAbleOrDownload(this.mContext)) {
                    return;
                } else {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_VIDEO_POST);
                    return;
                }
            case R.id.menu_vlog /* 2131362927 */:
                if (!checkAndInstallMediaEditorIfNotExist()) {
                    return;
                }
                if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportVlog()) {
                    MediaEditorIntentUtils.startVlogFromPickerTemp(this.mContext);
                } else if (VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(this.mContext)) {
                    ActionURIHandler.handleUri(this.mContext, GalleryContract.Common.URI_VLOG);
                }
                SamplingStatHelper.recordCountEvent("home", "home_page_menu_vlog");
                return;
            case R.id.sort /* 2131363392 */:
                if (!this.mCurrentPage.isActionSupport(2)) {
                    return;
                }
                this.mCurrentPage.onActionClick(2);
                return;
            case R.id.trash_bin /* 2131363637 */:
                IntentUtil.gotoTrashBin(this.mContext, "HomePageActivity");
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$onImmersionMenuSelected$0(boolean z) {
        if (z) {
            IntentUtil.goToMapAlbumDirectly(this.mContext);
        } else {
            dismissImmersionMenu();
        }
    }

    public final void registerFeature(int i, String str) {
        this.mFeatureItemsMap.put(Integer.valueOf(i), str);
    }

    public void checkRedDotFeature() {
        ImmersionMenu immersionMenu;
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu == null || (immersionMenu = phoneImmersionMenu.getImmersionMenu()) == null) {
            return;
        }
        this.mHasRedDotItem = false;
        for (Map.Entry<Integer, String> entry : this.mFeatureItemsMap.entrySet()) {
            ImmersionMenuItem mo1822findItem = immersionMenu.mo1822findItem(entry.getKey().intValue());
            boolean z = DisplayStatusManager.getRedDotStatus(entry.getValue()) && mo1822findItem.isVisible();
            this.mHasRedDotItem |= z;
            mo1822findItem.setIsRedDotDisplayed(z);
        }
        MenuItemsCheckListener menuItemsCheckListener = this.mMenuItemsCheckListener;
        if (menuItemsCheckListener != null) {
            menuItemsCheckListener.onMenuItemsChecked(this.mHasRedDotItem);
        }
        updateImmersionMenu(immersionMenu);
    }

    public void showImmersionMenu(View view, ImmersionMenuSupport immersionMenuSupport) {
        this.mCurrentPage = immersionMenuSupport;
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null) {
            phoneImmersionMenu.show(view, null);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("page", String.format(Locale.US, "%s_redDotDisplayed", this.mCurrentPage.getPageName()));
        hashMap.put(CallMethod.ARG_EXTRA_STRING, String.valueOf(this.mHasRedDotItem));
        SamplingStatHelper.recordCountEvent("feature_red_dot", "show_immersion_menu", hashMap);
        TrackController.trackExpose("403.29.0.1.16653", AutoTracking.getRef());
    }

    public void dismissImmersionMenu() {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null) {
            phoneImmersionMenu.dismiss();
        }
    }

    public void updateImmersionMenu(ImmersionMenu immersionMenu) {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null) {
            phoneImmersionMenu.update(immersionMenu);
        }
    }

    public void onActivityDestroy() {
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu != null && phoneImmersionMenu.isShowing()) {
            this.mPhoneImmersionMenu.dismiss();
        }
        PrintInstaller.getInstance().removeInstallStateListener(this.mInstallStateListener);
        MovieLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mMovieDownloadStateListener);
        VlogLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mVlogDownloadStateListener);
        VideoPostDownloadManager.getInstance().removeDownloadStateListener();
        MagicMattingLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mMagicMattingDownloadStateListener);
        IDPhotoLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mIDPhotoDownloadStateListener);
        ArtStillLibraryLoaderHelper.getInstance().removeDownloadStateListener(this.mArtStillDownloadStateListener);
        unregisterPrintStatusObserver();
    }

    public void registerMenuItemsCheckListener(MenuItemsCheckListener menuItemsCheckListener) {
        this.mMenuItemsCheckListener = menuItemsCheckListener;
    }

    public void unregisterMenuItemsCheckListener() {
        this.mMenuItemsCheckListener = null;
    }

    public final void registerPrintStatusObserver() {
        this.mPrintStatusObserver = new PrintStatusObserver();
        onPrintStatueChanged(CloudControlManager.getInstance().registerStatusObserver("photo-print", this.mPrintStatusObserver));
    }

    public final void unregisterPrintStatusObserver() {
        if (this.mPrintStatusObserver != null) {
            CloudControlManager.getInstance().unregisterStatusObserver(this.mPrintStatusObserver);
        }
    }

    public final boolean setMenuVisibility(int i, boolean z) {
        ImmersionMenu immersionMenu;
        PhoneImmersionMenu phoneImmersionMenu = this.mPhoneImmersionMenu;
        if (phoneImmersionMenu == null || (immersionMenu = phoneImmersionMenu.getImmersionMenu()) == null) {
            return false;
        }
        return setMenuVisibility(immersionMenu.mo1822findItem(i), z);
    }

    public final boolean setMenuVisibility(ImmersionMenuItem immersionMenuItem, boolean z) {
        if (immersionMenuItem == null || immersionMenuItem.isVisible() == z) {
            return false;
        }
        immersionMenuItem.setVisible(z);
        checkRedDotFeature();
        return true;
    }

    public final void onPrintStatueChanged(FeatureProfile.Status status) {
        if (PrintInstaller.getInstance().isPhotoPrintEnable()) {
            DisplayStatusManager.regenerateRedDotMap();
            setMenuVisibility(R.id.menu_photo_print, true);
            SamplingStatHelper.recordStringPropertyEvent("photo_print_enable", "true");
        }
    }

    /* loaded from: classes.dex */
    public class PrintStatusObserver extends FeatureStatusObserver {
        public PrintStatusObserver() {
            HomePageImmersionMenuHelper.this = r1;
        }

        @Override // com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver
        public void onStatusChanged(String str, FeatureProfile.Status status) {
            if ("photo-print".equals(str)) {
                HomePageImmersionMenuHelper.this.onPrintStatueChanged(status);
            }
        }
    }
}
