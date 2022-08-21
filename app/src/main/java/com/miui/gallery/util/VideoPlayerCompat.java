package com.miui.gallery.util;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.VideoFrameProvider;
import com.miui.preload.PreloadedAppHelper;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final class VideoPlayerCompat {
    public static final SupportNewVideoPlayer SUPPORT_NEW_VIDEO_PLAYER = new SupportNewVideoPlayer();
    public static final VideoPlayerCapabilities VIDEO_PLAYER_CAPABILITIES = new VideoPlayerCapabilities();
    public static final VideoPlayerSupportEditSubtitle VIDEO_PLAYER_SUPPORT_EDIT_SUBTITLE = new VideoPlayerSupportEditSubtitle();
    public static final VideoPlayerSupportViewTags VIDEO_PLAYER_SUPPORT_VIEW_TAGS = new VideoPlayerSupportViewTags();
    public static final VideoPlayerSupportZoom VIDEO_PLAYER_SUPPORT_ZOOM = new VideoPlayerSupportZoom();
    public static final VideoPlayerSupportImmersive VIDEO_PLAYER_SUPPORT_IMMERSIVE = new VideoPlayerSupportImmersive();
    public static final VideoPlayerSupportShortLandscape VIDEO_PLAYER_SUPPORT_LANDSCAPE = new VideoPlayerSupportShortLandscape();
    public static final VideoPlayerSupportActionBarAdjust VIDEO_PLAYER_SUPPORT_ACTION_BAR_ADJUST = new VideoPlayerSupportActionBarAdjust();
    public static final VideoPlayerSupportHideNavigationBar VIDEO_PLAYER_SUPPORT_HIDE_NAVIGATION_BAR = new VideoPlayerSupportHideNavigationBar();
    public static final VideoPlayerSupportBanHdrFrame VIDEO_PLAYER_SUPPORT_BAN_HDR_FRAME = new VideoPlayerSupportBanHdrFrame();
    public static final VideoPlayerSupportRotateScreen VIDEO_PLAYER_SUPPORT_ROTATE_SCREEN = new VideoPlayerSupportRotateScreen();
    public static final VideoPlayerSupportCutoutModeShortEdges VIDEO_PLAYER_SUPPORT_CUTOUT_MODE_SHORT_EDGES = new VideoPlayerSupportCutoutModeShortEdges();
    public static final VideoPlayerSupportEdit480FPS VIDEO_PLAYER_SUPPORT_EDIT_480FPS = new VideoPlayerSupportEdit480FPS();
    public static final VideoPlayerSupportEdit1920FPS VIDEO_PLAYER_SUPPORT_EDIT_1920FPS = new VideoPlayerSupportEdit1920FPS();
    public static final VideoPlayerSupportEdit3840FPS VIDEO_PLAYER_SUPPORT_EDIT_3840_FPS = new VideoPlayerSupportEdit3840FPS();
    public static final VideoPlayerSupportEnterAnimation VIDEO_PLAYER_SUPPORT_ENTER_ANIMATION = new VideoPlayerSupportEnterAnimation();
    public static final VideoPlayerSupportOpenVideoFormat VIDEO_PLAYER_SUPPORT_OPEN_VIDEO_FORMAT = new VideoPlayerSupportOpenVideoFormat();
    public static final VideoPlayerSupportLockOrientation VIDEO_PLAYER_SUPPORT_LOCK_ORIENTATION = new VideoPlayerSupportLockOrientation();
    public static final IsCNVideoPlayer IS_CN_VIDEO_PLAYER = new IsCNVideoPlayer();
    public static final IsGlobalVideoPlayer IS_GLOBAL_VIDEO_PLAYER = new IsGlobalVideoPlayer();
    public static final IsIndiaVideoPlayer IS_INDIA_VIDEO_PLAYER = new IsIndiaVideoPlayer();
    public static AtomicBoolean sInstalling = new AtomicBoolean(false);

    public static /* synthetic */ void $r8$lambda$BRN4HYzNpkCUYsjkCknkn2NdX2M(PreloadedAppHelper.PackageInstallObserver packageInstallObserver, String str, int i, String str2, Bundle bundle) {
        lambda$tryInstallVideoPlayer$0(packageInstallObserver, str, i, str2, bundle);
    }

    public static void prepareVideoPlayerStatus() {
        SUPPORT_NEW_VIDEO_PLAYER.get(null);
        VIDEO_PLAYER_CAPABILITIES.get(null);
        VIDEO_PLAYER_SUPPORT_EDIT_SUBTITLE.get(null);
        VIDEO_PLAYER_SUPPORT_VIEW_TAGS.get(null);
        VIDEO_PLAYER_SUPPORT_ZOOM.get(null);
        VIDEO_PLAYER_SUPPORT_IMMERSIVE.get(null);
        VIDEO_PLAYER_SUPPORT_LANDSCAPE.get(null);
        VIDEO_PLAYER_SUPPORT_ACTION_BAR_ADJUST.get(null);
        VIDEO_PLAYER_SUPPORT_HIDE_NAVIGATION_BAR.get(null);
        VIDEO_PLAYER_SUPPORT_BAN_HDR_FRAME.get(null);
        VIDEO_PLAYER_SUPPORT_ROTATE_SCREEN.get(null);
        VIDEO_PLAYER_SUPPORT_CUTOUT_MODE_SHORT_EDGES.get(null);
        VIDEO_PLAYER_SUPPORT_LOCK_ORIENTATION.get(null);
    }

    public static void resetVideoPlayerStatus() {
        SUPPORT_NEW_VIDEO_PLAYER.reset();
        VIDEO_PLAYER_CAPABILITIES.reset();
        VIDEO_PLAYER_SUPPORT_EDIT_SUBTITLE.reset();
        VIDEO_PLAYER_SUPPORT_VIEW_TAGS.reset();
        VIDEO_PLAYER_SUPPORT_ZOOM.reset();
        VIDEO_PLAYER_SUPPORT_IMMERSIVE.reset();
        VIDEO_PLAYER_SUPPORT_LANDSCAPE.reset();
        VIDEO_PLAYER_SUPPORT_ACTION_BAR_ADJUST.reset();
        VIDEO_PLAYER_SUPPORT_HIDE_NAVIGATION_BAR.reset();
        VIDEO_PLAYER_SUPPORT_BAN_HDR_FRAME.reset();
        VIDEO_PLAYER_SUPPORT_ROTATE_SCREEN.reset();
        VIDEO_PLAYER_SUPPORT_CUTOUT_MODE_SHORT_EDGES.reset();
        VIDEO_PLAYER_SUPPORT_LOCK_ORIENTATION.reset();
        IS_CN_VIDEO_PLAYER.reset();
        IS_GLOBAL_VIDEO_PLAYER.reset();
        IS_INDIA_VIDEO_PLAYER.reset();
    }

    public static boolean isSupportNewVideoPlayer() {
        return SUPPORT_NEW_VIDEO_PLAYER.get(null).booleanValue();
    }

    public static boolean isSupportOnlinePlayer() {
        return SUPPORT_NEW_VIDEO_PLAYER.isSupportedOnlineVideo();
    }

    public static boolean isVideoPlayerSupportEditSubtitle() {
        return VIDEO_PLAYER_SUPPORT_EDIT_SUBTITLE.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportViewTags() {
        return VIDEO_PLAYER_SUPPORT_VIEW_TAGS.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportZoom() {
        return VIDEO_PLAYER_SUPPORT_ZOOM.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportImmersive() {
        return VIDEO_PLAYER_SUPPORT_IMMERSIVE.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportShortLandscape() {
        return VIDEO_PLAYER_SUPPORT_LANDSCAPE.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportActionBarAdjust() {
        return VIDEO_PLAYER_SUPPORT_ACTION_BAR_ADJUST.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportHideNavigationBar() {
        return VIDEO_PLAYER_SUPPORT_HIDE_NAVIGATION_BAR.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportBanHdrFrame() {
        return VIDEO_PLAYER_SUPPORT_BAN_HDR_FRAME.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportRotateScreen() {
        return VIDEO_PLAYER_SUPPORT_ROTATE_SCREEN.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportCutoutModeShortEdges() {
        return VIDEO_PLAYER_SUPPORT_CUTOUT_MODE_SHORT_EDGES.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportEdit1920FPS() {
        return VIDEO_PLAYER_SUPPORT_EDIT_1920FPS.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportEdit480FPS() {
        return VIDEO_PLAYER_SUPPORT_EDIT_480FPS.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportEdit3840FPS() {
        return VIDEO_PLAYER_SUPPORT_EDIT_3840_FPS.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportEnterAnimation() {
        return VIDEO_PLAYER_SUPPORT_ENTER_ANIMATION.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportOpenVideoFormat() {
        return VIDEO_PLAYER_SUPPORT_OPEN_VIDEO_FORMAT.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupportLockOrientation() {
        return VIDEO_PLAYER_SUPPORT_LOCK_ORIENTATION.get(null).booleanValue();
    }

    public static boolean isVideoPlayerSupport(String str) {
        String[] strArr;
        if (!TextUtils.isEmpty(str) && (strArr = VIDEO_PLAYER_CAPABILITIES.get(null)) != null) {
            for (String str2 : strArr) {
                if (TextUtils.equals(str, str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /* loaded from: classes2.dex */
    public static class SupportNewVideoPlayer extends LazyValue<Void, Boolean> {
        public volatile boolean mSupportedOnlineVideo;

        public SupportNewVideoPlayer() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r3) {
            if (!VideoFrameProvider.isDeviceSupport()) {
                return Boolean.FALSE;
            }
            if (TextUtils.isEmpty(VideoPlayerCompat.getMiuiVideoPackageName())) {
                return Boolean.FALSE;
            }
            Intent intent = new Intent("com.miui.videoplayer.GALLERY_VIDEO_PLAY");
            intent.setPackage(VideoPlayerCompat.getMiuiVideoPackageName());
            intent.setData(Uri.parse("content:///"));
            List<ResolveInfo> queryActivitiesWithMeta = MiscUtil.queryActivitiesWithMeta(intent);
            if (queryActivitiesWithMeta != null && !queryActivitiesWithMeta.isEmpty()) {
                Iterator<ResolveInfo> it = queryActivitiesWithMeta.iterator();
                while (true) {
                    if (it.hasNext()) {
                        Bundle bundle = it.next().activityInfo.metaData;
                        if (bundle != null && bundle.getInt("com.miui.video.GALLERY_ONLINE_PLAY_SUPPORT") == 2) {
                            this.mSupportedOnlineVideo = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

        public boolean isSupportedOnlineVideo() {
            get(null);
            return this.mSupportedOnlineVideo;
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerCapabilities extends LazyValue<Void, String[]> {
        public VideoPlayerCapabilities() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public String[] mo1272onInit(Void r2) {
            String applicationMetaData = MiscUtil.getApplicationMetaData(VideoPlayerCompat.getMiuiVideoPackageName(), "com.miui.video.EDIT_CAPABILITIES");
            if (TextUtils.isEmpty(applicationMetaData)) {
                return null;
            }
            return applicationMetaData.split("\\|");
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportEditSubtitle extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportEditSubtitle() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("subtitle"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportViewTags extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportViewTags() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("tags"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportZoom extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportZoom() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("zoom"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportImmersive extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportImmersive() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.FALSE;
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportShortLandscape extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportShortLandscape() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("short_landscape"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportActionBarAdjust extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportActionBarAdjust() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("action_bar_adjust"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportHideNavigationBar extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportHideNavigationBar() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("hide_navigation_bar"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportBanHdrFrame extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportBanHdrFrame() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("ban_hdr_frame"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportRotateScreen extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportRotateScreen() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("rotate_screen"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportCutoutModeShortEdges extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportCutoutModeShortEdges() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("cutout_mode_short_edges"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportEdit480FPS extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportEdit480FPS() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("edit_480"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportEdit1920FPS extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportEdit1920FPS() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("edit_1920"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportEdit3840FPS extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportEdit3840FPS() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("edit_3840"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportEnterAnimation extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportEnterAnimation() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("edit_page_animation"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportOpenVideoFormat extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportOpenVideoFormat() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("open_video_format"));
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoPlayerSupportLockOrientation extends LazyValue<Void, Boolean> {
        public VideoPlayerSupportLockOrientation() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(VideoPlayerCompat.isVideoPlayerSupport("lock_orientation"));
        }
    }

    /* loaded from: classes2.dex */
    public static class IsIndiaVideoPlayer extends LazyValue<Void, Boolean> {
        public IsIndiaVideoPlayer() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r2) {
            return Boolean.valueOf(MiscUtil.getAppVersionCode("com.gallery.player") != -1);
        }
    }

    /* loaded from: classes2.dex */
    public static class IsCNVideoPlayer extends LazyValue<Void, Boolean> {
        public IsCNVideoPlayer() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(MiscUtil.getAppLaunchIntent("com.miui.video") != null);
        }
    }

    /* loaded from: classes2.dex */
    public static class IsGlobalVideoPlayer extends LazyValue<Void, Boolean> {
        public IsGlobalVideoPlayer() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(MiscUtil.getAppLaunchIntent("com.miui.videoplayer") != null);
        }
    }

    public static String getMiuiVideoPackageName() {
        return BaseBuildUtil.isInternational() ? IS_GLOBAL_VIDEO_PLAYER.get(null).booleanValue() ? "com.miui.videoplayer" : IS_INDIA_VIDEO_PLAYER.get(null).booleanValue() ? "com.gallery.player" : "" : IS_CN_VIDEO_PLAYER.get(null).booleanValue() ? "com.miui.video" : "";
    }

    public static boolean tryInstallVideoPlayer(final PreloadedAppHelper.PackageInstallObserver packageInstallObserver) {
        if (BaseBuildUtil.isInternational()) {
            DefaultLogger.w("VideoPlayerCompat", "not supported on internal rom");
            return false;
        } else if (BaseMiscUtil.isPackageInstalled(getMiuiVideoPackageName())) {
            DefaultLogger.d("VideoPlayerCompat", "the package %s has installed", getMiuiVideoPackageName());
            return false;
        } else if (!sInstalling.compareAndSet(false, true)) {
            return true;
        } else {
            return PreloadedAppHelper.installPreloadedDataApp(StaticContext.sGetAndroidContext(), "com.miui.video", new PreloadedAppHelper.PackageInstallObserver() { // from class: com.miui.gallery.util.VideoPlayerCompat$$ExternalSyntheticLambda0
                @Override // com.miui.preload.PreloadedAppHelper.PackageInstallObserver
                public final void onPackageInstalled(String str, int i, String str2, Bundle bundle) {
                    VideoPlayerCompat.$r8$lambda$BRN4HYzNpkCUYsjkCknkn2NdX2M(PreloadedAppHelper.PackageInstallObserver.this, str, i, str2, bundle);
                }
            }, 1);
        }
    }

    public static /* synthetic */ void lambda$tryInstallVideoPlayer$0(PreloadedAppHelper.PackageInstallObserver packageInstallObserver, String str, int i, String str2, Bundle bundle) {
        DefaultLogger.d("VideoPlayerCompat", "%s install result: %s", str, str2);
        if (i == 1) {
            resetVideoPlayerStatus();
        }
        packageInstallObserver.onPackageInstalled(str, i, str2, bundle);
        sInstalling.compareAndSet(true, false);
    }
}
