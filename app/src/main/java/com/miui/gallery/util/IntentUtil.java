package com.miui.gallery.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.joran.action.Action;
import com.android.internal.SystemPropertiesCompat;
import com.android.internal.WindowCompat;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.miui.account.AccountHelper;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.AlbumDetailGroupingActivity;
import com.miui.gallery.activity.BackupSettingsMoreActivity;
import com.miui.gallery.activity.BurstPhotoActivity;
import com.miui.gallery.activity.CloudGuideWelcomeActivity;
import com.miui.gallery.activity.CloudSettingsActivity;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.activity.GalleryMiplayActivity;
import com.miui.gallery.activity.GallerySettingsActivity;
import com.miui.gallery.activity.MacaronsActivity;
import com.miui.gallery.activity.MapActivity;
import com.miui.gallery.activity.PermissionSettingsActivity;
import com.miui.gallery.activity.PhotoDetailActivity;
import com.miui.gallery.activity.PhotoPreviewSelectActivity;
import com.miui.gallery.activity.PicToPdfPreviewActivity;
import com.miui.gallery.activity.PrivacySettingsActivity;
import com.miui.gallery.activity.TrashActivity;
import com.miui.gallery.activity.facebaby.BabyAlbumDetailActivity;
import com.miui.gallery.activity.facebaby.FacePageActivity;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.ui.detail.StoryAlbumActivity;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.editor.photo.app.PrivacyWatermarkActivity;
import com.miui.gallery.glide.load.model.PreloadModel;
import com.miui.gallery.magic.idphoto.CertificatesGuideActivity;
import com.miui.gallery.magic.matting.MattingActivity;
import com.miui.gallery.magic.special.effects.image.SpecialEffectsActivity;
import com.miui.gallery.magic.special.effects.image.SpecialEffectsGuideActivity;
import com.miui.gallery.magic.special.effects.video.VideoCutActivity;
import com.miui.gallery.magic.special.effects.video.VideoEffectsGuideActivity;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.movie.ui.activity.MovieActivity;
import com.miui.gallery.picker.PickerActivity;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.picker.uri.OriginUrlRequestor;
import com.miui.gallery.picker.uri.UriGenerator;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.ui.AuthenticatePrivacyPasswordFragment;
import com.miui.gallery.ui.LoginAndSyncCheckFragment;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MacaronInstaller;
import com.miui.gallery.util.market.PrintInstaller;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.vlog.rule.VlogTemplateMatchActivity;
import com.miui.mediaeditor.api.MediaEditorApiHelper;
import com.miui.mediaeditor.api.MediaEditorIntentUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.miui.os.Rom;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;
import com.xiaomi.stat.c.b;
import com.xiaomi.stat.d;
import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miui.cloud.util.SyncAutoSettingUtil;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.hybrid.HybridActivity;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes2.dex */
public class IntentUtil {
    public static final String[] sXiaoaiBlackList;
    public static boolean sXiaoaiLoaded;
    public static final String[] CONTACT_PHOTO_PACKAGE = {"com.jeejen.family.miui", "com.android.contacts"};
    public static final String[] CONTACT_PHOTO_CLASS = {"com.jeejen.contact.ui.AttachPhotoActivity", "com.android.contacts.activities.AttachPhotoActivity"};
    public static final String[] PLAY_VIDEO_PACKAGE = {"com.miui.videoplayer", "com.miui.video"};
    public static final String[] PLAY_VIDEO_CLASS = {"com.miui.videoplayer.VideoPlayerActivity", "com.miui.videoplayer.VideoPlayerActivity"};
    public static final String[] DEVICE_SUPPORT_MEITU_XX_EDITOR = {"vela"};
    public static final SupportMeituCollage SUPPORT_MEITU_COLLAGE = new SupportMeituCollage();
    public static final SupportMeituEditor SUPPORT_MEITU_EDITOR = new SupportMeituEditor();
    public static final EnterTypeUtils.EnterType FROM_NO_CARE = EnterTypeUtils.EnterType.FROM_NO_CARE;

    static {
        String[] strArr = {"mocha", "latte", "cappu", "clover"};
        sXiaoaiBlackList = strArr;
        sXiaoaiLoaded = true;
        for (String str : strArr) {
            if (Build.DEVICE.equals(str)) {
                sXiaoaiLoaded = false;
                return;
            }
        }
    }

    public static void gotoPreviewSelectPage(Fragment fragment, Uri uri, int i, int i2, String str, String[] strArr, String str2, ImageLoadParams imageLoadParams, long[] jArr, int[] iArr) {
        gotoPreviewSelectPage(fragment, uri, i, i2, str, strArr, str2, imageLoadParams, jArr, iArr, null, null);
    }

    public static void gotoPreviewSelectPage(Fragment fragment, Uri uri, int i, int i2, String str, String[] strArr, String str2, ImageLoadParams imageLoadParams, long[] jArr, int[] iArr, String str3, String str4) {
        Intent intent = new Intent(fragment.getActivity(), PhotoPreviewSelectActivity.class);
        intent.setData(uri);
        intent.putExtra("photo_init_position", i);
        intent.putExtra("photo_count", i2);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("photo_selection", str);
        }
        if (strArr != null) {
            intent.putExtra("photo_selection_args", strArr);
        }
        if (!TextUtils.isEmpty(str2)) {
            intent.putExtra("photo_order_by", str2);
        }
        if (imageLoadParams != null) {
            intent.putExtra("photo_transition_data", imageLoadParams);
        }
        if (jArr != null) {
            GeneralDataHolder.getInstance().save("PhotoPreviewSelectFragmentpreview_selected_ids_key", jArr);
            intent.putExtra("photo_preview_initial_selected_ids_retrieve_key", "PhotoPreviewSelectFragmentpreview_selected_ids_key");
        }
        if (iArr != null) {
            intent.putExtra("photo_preview_initial_selected_positions", iArr);
        }
        fragment.startActivity(intent);
    }

    public static void gotoPicToPdfPreviewPage(FragmentActivity fragmentActivity, String str) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        gotoPicToPdfPreviewPage(fragmentActivity, (ArrayList<String>) arrayList);
    }

    public static void gotoPicToPdfPreviewPage(FragmentActivity fragmentActivity, ArrayList<String> arrayList) {
        Intent intent = new Intent(fragmentActivity, PicToPdfPreviewActivity.class);
        intent.putExtra("pic_to_pdf_data", arrayList);
        fragmentActivity.startActivity(intent);
    }

    public static void gotoPicToPdfPreviewPage(final FragmentActivity fragmentActivity, List<CheckableAdapter.CheckedItem> list) {
        if (list.isEmpty()) {
            DefaultLogger.e("IntentUtil", "No selected Item!");
        } else {
            new UriGenerator(fragmentActivity, list, Picker.ImageType.THUMBNAIL, new UriGenerator.UriGenerateListener() { // from class: com.miui.gallery.util.IntentUtil.1
                @Override // com.miui.gallery.picker.uri.UriGenerator.UriGenerateListener
                public void onFail() {
                }

                @Override // com.miui.gallery.picker.uri.UriGenerator.UriGenerateListener
                public void onSuccess(Uri[] uriArr, List<OriginUrlRequestor.OriginInfo> list2) {
                    ArrayList arrayList = new ArrayList();
                    for (Uri uri : uriArr) {
                        arrayList.add(uri.getPath());
                    }
                    IntentUtil.gotoPicToPdfPreviewPage(FragmentActivity.this, (ArrayList<String>) arrayList);
                }
            }).generate();
        }
    }

    public static void gotoPhotoDetailPage(FragmentActivity fragmentActivity, BaseDataItem baseDataItem, boolean z, boolean z2, boolean z3, boolean z4) {
        Intent intent = new Intent(fragmentActivity, PhotoDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("photo_detail_target", baseDataItem);
        bundle.putBoolean("StartActivityWhenLocked", z);
        bundle.putBoolean("photodetail_is_photo_datetime_editable", z2);
        bundle.putBoolean("photodetail_is_photo_renamable", z4);
        bundle.putBoolean("photo_detail_is_need_download_originphoto", z3);
        intent.putExtras(bundle);
        fragmentActivity.startActivityForResult(intent, 38);
    }

    public static void goToMipalyControlActivity(FragmentActivity fragmentActivity, MediaMetaData mediaMetaData) {
        Intent intent = new Intent(fragmentActivity, GalleryMiplayActivity.class);
        intent.putExtra("video_miplay_target", mediaMetaData);
        fragmentActivity.startActivityForResult(intent, 65);
    }

    public static void gotoDailyAlbumDetailPage(FragmentActivity fragmentActivity, int i) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
        intent.putExtra("daily_album", true);
        intent.putExtra("daily_album_date", i);
        intent.putExtra("album_name", GalleryDateUtils.formatRelativeDate(GalleryDateUtils.format(i)));
        intent.putExtra("album_unwriteable", false);
        intent.putExtra("album_id", 2147483643L);
        intent.putExtra("album_server_id", String.valueOf(-2147483643L));
        fragmentActivity.startActivity(intent);
    }

    public static void goToMapPhotoListPage(Fragment fragment, String str) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
        intent.putExtra("album_name", fragment.getResources().getString(R.string.map_album_detail_page_title));
        intent.putExtra("album_id", 2147383646L);
        intent.putExtra("is_from_map", true);
        intent.putExtra("media_ids", str);
        fragment.startActivity(intent);
    }

    public static void gotoBurstPhotoActivity(FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment, BaseDataItem baseDataItem, long j, String str) {
        if (fragmentActivity == null || photoPageFragment == null || baseDataItem == null) {
            return;
        }
        boolean z = photoPageFragment.getArguments().getBoolean("skip_interception", false);
        Intent intent = new Intent(fragmentActivity, BurstPhotoActivity.class);
        Bundle bundle = new Bundle();
        Uri build = GalleryContract.Media.URI.buildUpon().build();
        bundle.putParcelable("photo_data", build);
        bundle.putLong("album_id", j);
        bundle.putString("photo_selection", str);
        bundle.putString("photo_order_by", "burst_index DESC ");
        bundle.putBoolean("unford_burst", true);
        bundle.putInt("notch_height", WindowCompat.getTopNotchHeight(fragmentActivity));
        intent.putExtra("is_from_app_lock", z);
        intent.putExtra("extra_custom_transition", true);
        intent.setData(build);
        intent.putExtras(bundle);
        intent.putExtra("is_time_burst", baseDataItem.isTimeBurstItem());
        intent.putExtra("extra_image_url", baseDataItem.getPathDisplayBetter());
        fragmentActivity.startActivityFromFragment(photoPageFragment, intent, 52, (Bundle) null);
    }

    public static void setDataAndType(Intent intent, Uri uri, String str) {
        setDataAndType(intent, uri, str, GalleryOpenProvider.needReturnContentUri());
    }

    public static void setDataAndType(Intent intent, Uri uri, String str, boolean z) {
        if (Action.FILE_ATTRIBUTE.equals(uri.getScheme()) && z) {
            intent.setDataAndType(GalleryOpenProvider.translateToContent(uri.getPath()), str);
            intent.setFlags(1);
            return;
        }
        intent.setDataAndType(uri, str);
    }

    public static String ensureMimeType(Uri uri, String str) {
        return !TextUtils.isEmpty(str) ? str : BaseFileMimeUtil.getMimeType(uri.toString());
    }

    public static void startMacaronsPicker(FragmentActivity fragmentActivity) {
        boolean checkInstall = MacaronInstaller.getInstance().checkInstall(fragmentActivity);
        if (checkInstall) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.putExtra("pick_close_type", 4);
            intent.setPackage("com.miui.gallery");
            intent.putExtra("pick_intent", new Intent(fragmentActivity, MacaronsActivity.class));
            intent.putExtra("pick-upper-bound", 1);
            intent.putExtra("extra_filter_media_type", MacaronInstaller.sUnSupportImageMimeType);
            intent.putExtra("extra_from_type", b.h);
            fragmentActivity.startActivity(intent);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state", "isInstall_" + checkInstall);
        SamplingStatHelper.recordCountEvent("macaron", "enter_from_picker", hashMap);
    }

    public static void startVideoPost(FragmentActivity fragmentActivity, Uri[] uriArr) {
        Intent intent = new Intent(fragmentActivity, VideoCutActivity.class);
        intent.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
        intent.setFlags(1);
        fragmentActivity.startActivity(intent);
    }

    public static void startArtStill(FragmentActivity fragmentActivity, Uri[] uriArr) {
        Intent intent = new Intent(fragmentActivity, SpecialEffectsActivity.class);
        intent.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
        intent.setFlags(1);
        intent.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, 0);
        fragmentActivity.startActivity(intent);
    }

    public static void startIDPhotoFromCreation(FragmentActivity fragmentActivity, Uri[] uriArr) {
        Intent intent = new Intent(fragmentActivity, CertificatesGuideActivity.class);
        intent.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
        intent.setFlags(1);
        intent.putExtra("from_creation", true);
        fragmentActivity.startActivity(intent);
    }

    public static void startMagicMatting(FragmentActivity fragmentActivity, Uri[] uriArr) {
        Intent intent = new Intent(fragmentActivity, MattingActivity.class);
        intent.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
        intent.setFlags(1);
        fragmentActivity.startActivity(intent);
    }

    public static void startMacarons(FragmentActivity fragmentActivity, Uri[] uriArr) {
        boolean checkInstall = MacaronInstaller.getInstance().checkInstall(fragmentActivity);
        if (checkInstall) {
            if (uriArr == null || uriArr.length == 0 || uriArr[0] == null) {
                return;
            }
            Intent intent = new Intent(fragmentActivity, MacaronsActivity.class);
            intent.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
            intent.setFlags(1);
            fragmentActivity.startActivity(intent);
            SamplingStatHelper.recordCountEvent("creation", "creation_macaron");
        }
        HashMap hashMap = new HashMap();
        hashMap.put("state", "isInstall_" + checkInstall);
        SamplingStatHelper.recordCountEvent("macaron", "enter_from_creation", hashMap);
    }

    public static boolean playViaMiVideo(Context context, Uri uri, String str, boolean z, boolean z2, int i, Boolean bool, boolean z3, boolean z4, Bundle bundle, int i2) {
        Intent intent = new Intent();
        String ensureMimeType = ensureMimeType(uri, str);
        if ("*/*".equals(ensureMimeType)) {
            ensureMimeType = "video/*";
        }
        try {
            try {
                DefaultLogger.d("IntentUtil", "showGalleryWhenLocked: %s, requestOrientation: %d", Boolean.valueOf(z2), Integer.valueOf(i));
                if (z3) {
                    setDataAndType(intent, uri, null, true);
                } else if (Action.FILE_ATTRIBUTE.equals(uri.getScheme()) && !GalleryOpenProvider.needReturnContentUri()) {
                    setDataAndType(intent, uri, null, false);
                } else {
                    if (!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme())) {
                        setDataAndType(intent, uri, ensureMimeType);
                    }
                    setDataAndType(intent, uri, null);
                }
                if (!TextUtils.isEmpty(VideoPlayerCompat.getMiuiVideoPackageName())) {
                    intent.setPackage(VideoPlayerCompat.getMiuiVideoPackageName());
                }
                if (isActionVideoEdit(i2)) {
                    intent.setAction("com.miui.videoplayer.VIDEO_EDIT");
                } else {
                    intent.setAction("com.miui.videoplayer.GALLERY_VIDEO_PLAY");
                }
                if (!z || !BaseMiscUtil.isIntentSupported(intent)) {
                    intent.setAction("com.miui.videoplayer.LOCAL_VIDEO_PLAY");
                }
                intent.putExtra("StartActivityWhenLocked", z2);
                intent.putExtra("com.miui.video.extra.play_video_request_orientation", i);
                if (bool != null) {
                    intent.putExtra("com.miui.video.extra.is.lock", bool);
                }
                intent.putExtra("com.miui.video.extra.is_secret", z3);
                intent.putExtra("gallery_miplay_circulate_status", z4);
                intent.putExtras(bundle);
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    String callingPackage = activity.getCallingPackage();
                    DefaultLogger.d("IntentUtil", "play video extra calling package: %s", callingPackage);
                    intent.putExtra("miui_video_extra_calling_package", callingPackage);
                    activity.startActivityForResult(intent, isActionVideoEdit(i2) ? 66 : 45);
                } else {
                    context.startActivity(intent);
                }
                return true;
            } catch (ActivityNotFoundException unused) {
                DefaultLogger.e("IntentUtil", "local video player not found!");
                return false;
            }
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", e);
            return false;
        }
    }

    public static boolean playVideoViaView(Context context, Uri uri, String str) {
        Intent intent = new Intent();
        String ensureMimeType = ensureMimeType(uri, str);
        if ("*/*".equals(ensureMimeType)) {
            ensureMimeType = "video/*";
        }
        intent.setAction("android.intent.action.VIEW");
        setDataAndType(intent, uri, ensureMimeType);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            ToastUtils.makeText(context, context.getString(R.string.video_err));
            return false;
        }
    }

    public static boolean isActionVideoEdit(int i) {
        return (i == 1 || i == 2) && VideoPlayerCompat.isVideoPlayerSupportEnterAnimation();
    }

    public static boolean isDeviceSupportMeituXX() {
        for (String str : DEVICE_SUPPORT_MEITU_XX_EDITOR) {
            if (Build.DEVICE.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean gotoGalleryPage(Context context, String str) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setPackage(context.getPackageName());
            if (intent.resolveActivity(GalleryApp.sGetAndroidContext().getPackageManager()) == null) {
                return false;
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", "gotoUri failed: %s", e);
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class SupportMeituCollage extends LazyValue<Void, Boolean> {
        public SupportMeituCollage() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            if (!IntentUtil.isDeviceSupportMeituXX()) {
                return Boolean.FALSE;
            }
            Intent intent = new Intent("com.xiaomi.intent.action.PUZZLE");
            intent.setPackage("com.mt.mtxx.mtxx");
            intent.setType("image/*");
            return Boolean.valueOf(BaseMiscUtil.isIntentSupported(intent));
        }
    }

    /* loaded from: classes2.dex */
    public static class SupportMeituEditor extends LazyValue<Void, Boolean> {
        public SupportMeituEditor() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r2) {
            if (!IntentUtil.isDeviceSupportMeituXX()) {
                return Boolean.FALSE;
            }
            Intent intent = new Intent("com.xiaomi.intent.action.BEAUTY");
            intent.setPackage("com.mt.mtxx.mtxx");
            intent.setType("image/*");
            return Boolean.valueOf(BaseMiscUtil.isIntentSupported(intent));
        }
    }

    public static boolean isSupportMeituCollage() {
        return SUPPORT_MEITU_COLLAGE.get(null).booleanValue();
    }

    public static int getCollageMaxImageSize() {
        return isSupportMeituCollage() ? 9 : 6;
    }

    public static void startVlogFromPicker(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("video/*");
        intent.putExtra("pick-upper-bound", 100);
        intent.putExtra("pick-lower-bound", 1);
        intent.putExtra("pick_close_type", 3);
        intent.putExtra("disable_pending_transition", true);
        intent.putExtra("pick-need-origin", true);
        intent.putExtra("extra_from_type", b.i);
        intent.putExtra("pick_intent", new Intent("com.miui.gallery.VLOG_SELECT_TEMPLATE"));
        intent.putExtra("pick-need-origin", true);
        intent.setPackage("com.miui.gallery");
        context.startActivity(intent);
    }

    public static void startVideoPostGuide(FragmentActivity fragmentActivity) {
        fragmentActivity.startActivity(new Intent(fragmentActivity, VideoEffectsGuideActivity.class));
    }

    public static void startArtStillGuide(FragmentActivity fragmentActivity) {
        fragmentActivity.startActivity(new Intent(fragmentActivity, SpecialEffectsGuideActivity.class));
    }

    public static void startIDPhotoGuide(FragmentActivity fragmentActivity) {
        fragmentActivity.startActivity(new Intent(fragmentActivity, CertificatesGuideActivity.class));
    }

    public static void startMagicMattingFromPicker(FragmentActivity fragmentActivity) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.setPackage("com.miui.gallery");
        intent.putExtra("pick_intent", new Intent(fragmentActivity, MattingActivity.class));
        intent.putExtra("pick-upper-bound", 1);
        intent.putExtra("pick_close_type", 3);
        fragmentActivity.startActivity(intent);
    }

    public static void startVlogFromStory(FragmentActivity fragmentActivity, String str, ArrayList<String> arrayList) {
        Intent intent;
        if (TextUtils.isEmpty(str) || arrayList == null || arrayList.size() == 0) {
            return;
        }
        if (MediaEditorUtils.isMediaEditorAvailable()) {
            if (MediaEditorApiHelper.isVlogAvailable()) {
                intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.vlog.match.vlog.rule.VlogTemplateMatchActivity"));
            } else {
                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "vlog");
                return;
            }
        } else if (!VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity, IntentUtil$$ExternalSyntheticLambda1.INSTANCE)) {
            return;
        } else {
            intent = new Intent("com.miui.gallery.VLOG_SELECT_TEMPLATE");
        }
        intent.putExtra("com.miui.gallery.vlog.extra.template", str);
        intent.putStringArrayListExtra("com.miui.gallery.vlog.extra.paths", arrayList);
        fragmentActivity.startActivity(intent);
    }

    public static /* synthetic */ void lambda$startVlogFromStory$0() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photo_vlog_module_loading);
    }

    public static void startCollagePicker(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("pick_close_type", 3);
        intent.setPackage("com.miui.gallery");
        Intent intent2 = new Intent();
        intent2.setData(GalleryContract.Common.URI_COLLAGE_FROM_PICKER);
        intent.putExtra("pick_intent", intent2);
        intent.putExtra("pick-upper-bound", getCollageMaxImageSize());
        intent.putExtra("extra_from_type", b.k);
        context.startActivity(intent);
    }

    public static boolean startMeituCollageAction(FragmentActivity fragmentActivity, List<Uri> list, int i) {
        if (fragmentActivity != null && list != null) {
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(list.size());
            for (Uri uri : list) {
                if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
                    uri = GalleryOpenProvider.translateToContent(uri.getPath());
                }
                arrayList.add(uri);
                if ("com.miui.gallery.open".equals(uri.getAuthority())) {
                    fragmentActivity.grantUriPermission("com.mt.mtxx.mtxx", uri, 1);
                }
            }
            Intent intent = new Intent("com.xiaomi.intent.action.PUZZLE");
            intent.putExtra("meitu_edit_result_path", StorageUtils.getPathInPrimaryStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE));
            intent.putExtra("edit_from_xiaomi_album", true);
            intent.putParcelableArrayListExtra("extra_key_out_puzzle_image_info", arrayList);
            intent.setType("image/*");
            intent.setPackage("com.mt.mtxx.mtxx");
            if (BaseMiscUtil.isIntentSupported(intent)) {
                try {
                    if (i > 0) {
                        fragmentActivity.startActivityForResult(intent, i);
                    } else {
                        fragmentActivity.startActivity(intent);
                    }
                    return true;
                } catch (Exception e) {
                    DefaultLogger.w("IntentUtil", e);
                }
            }
        }
        return false;
    }

    public static boolean editPreCheck(BaseDataItem baseDataItem, GalleryActivity galleryActivity, GalleryFragment galleryFragment) {
        if (baseDataItem == null || galleryActivity == null || galleryFragment == null) {
            return false;
        }
        if (baseDataItem.isVideo()) {
            if (baseDataItem.is8KVideo()) {
                ToastUtils.makeText(galleryActivity, galleryActivity.getString(R.string.video_editor_not_support_8k_tips, new Object[]{8}));
                return false;
            } else if (VlogEntranceUtils.isSuperLowLoaded() && baseDataItem.is2KVideo()) {
                ToastUtils.makeText(galleryActivity, galleryActivity.getString(R.string.video_editor_not_support_8k_tips, new Object[]{2}));
                return false;
            } else if (TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                ToastUtils.makeText(galleryActivity, (int) R.string.video_edit_file_not_exits_tip);
                return false;
            }
        }
        if (baseDataItem.getContentUriForExternal() != null) {
            return true;
        }
        ToastUtils.makeText(galleryActivity, (int) R.string.photo_edit_file_not_exits_tip);
        return false;
    }

    public static boolean startEditAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, GalleryFragment galleryFragment, boolean z, int i, ImageView imageView) {
        return startEditAction(baseDataItem, fragmentActivity, galleryFragment, true, z, i, imageView);
    }

    /* JADX WARN: Removed duplicated region for block: B:71:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0214  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0220  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean startEditAction(com.miui.gallery.model.BaseDataItem r9, androidx.fragment.app.FragmentActivity r10, com.miui.gallery.app.fragment.GalleryFragment r11, boolean r12, boolean r13, int r14, android.widget.ImageView r15) {
        /*
            Method dump skipped, instructions count: 553
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.IntentUtil.startEditAction(com.miui.gallery.model.BaseDataItem, androidx.fragment.app.FragmentActivity, com.miui.gallery.app.fragment.GalleryFragment, boolean, boolean, int, android.widget.ImageView):boolean");
    }

    public static boolean showOnMap(Context context, double d, double d2) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, "http://maps.google.com/maps?f=q&q=(%f,%f)", Double.valueOf(d), Double.valueOf(d2)))).setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")));
            return true;
        } catch (ActivityNotFoundException e) {
            try {
                DefaultLogger.e("IntentUtil", "GMM activity not found!", e);
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(String.format(Locale.US, "geo:%f,%f", Double.valueOf(d), Double.valueOf(d2)))));
                return true;
            } catch (ActivityNotFoundException e2) {
                DefaultLogger.e("IntentUtil", "GEO activity not found!", e2);
                return false;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
    }

    public static void createShortCutForBabyAlbum(Context context, boolean z, long j, String str, Bitmap bitmap, String str2, String str3, String str4, String str5) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_SHARED_BABY_ALBUM");
        intent.putExtra("album_id", j);
        intent.putExtra("album_name", str);
        intent.putExtra("other_share_album", z);
        intent.putExtra("people_id", str2);
        intent.putExtra("thumbnail_info_of_baby", str3);
        intent.putExtra("baby_info", str4);
        intent.putExtra("baby_sharer_info", str5);
        if (TextUtils.isEmpty(str2)) {
            str2 = String.valueOf(j);
        }
        ShortcutUtil.createShortcut(context, str2, str, bitmap, R.drawable.baby_default_icon, intent);
        if (Build.VERSION.SDK_INT >= 26) {
            str = str2;
        }
        GalleryPreferences.Baby.recordBabyAlbumHasShortcut(str);
    }

    public static void removeAllShortCutForBabyAlbums(Context context) {
        Iterator<String> it = GalleryPreferences.Baby.getBabyAlbumsHasShortcut().iterator();
        while (it.hasNext()) {
            String next = it.next();
            removeShortCutForBabyAlbumByName(context, next);
            GalleryPreferences.Baby.removeBabyAlbumShortcut(next);
        }
    }

    public static void removeShortCutForBabyAlbumByName(Context context, String str) {
        Intent intent = new Intent("com.miui.home.launcher.action.UNINSTALL_SHORTCUT");
        intent.setPackage(SystemPropertiesCompat.get("ro.miui.product.home", "com.miui.home"));
        if (Build.VERSION.SDK_INT < 26) {
            intent.putExtra("android.intent.extra.shortcut.NAME", str);
        } else {
            intent.putExtra("shortcut_id", str);
        }
        intent.putExtra("android.intent.extra.shortcut.INTENT", new Intent());
        context.sendBroadcast(intent);
    }

    public static void startCameraActivity(Context context) {
        try {
            context.startActivity(new Intent("android.media.action.STILL_IMAGE_CAMERA").setFlags(335544320));
        } catch (ActivityNotFoundException e) {
            DefaultLogger.e("IntentUtil", e);
        }
    }

    public static void pickPeople(FragmentActivity fragmentActivity, String str) {
        Intent intent = new Intent("com.miui.gallery.action.PICK_PEOPLE");
        intent.putExtra("pick_people", true);
        intent.putExtra("pick_people_candidate_name", str);
        fragmentActivity.startActivityForResult(intent, 14);
    }

    public static void pickFace(FragmentActivity fragmentActivity, String str, String str2, String str3, ArrayList<Long> arrayList, int i, boolean z) {
        Intent intent = new Intent("com.miui.gallery.action.PICK_FACE");
        intent.putExtra("server_id_of_album", str2);
        intent.putExtra("local_id_of_album", str3);
        intent.putExtra("album_name", str);
        intent.putExtra("pick_face_direct", true);
        intent.putExtra("need_pick_face_id", z);
        if (arrayList != null && arrayList.size() > 0) {
            intent.putExtra("pick_face_ids_in", TextUtils.join(",", arrayList));
        }
        intent.putExtra("pick-upper-bound", i);
        intent.putExtra("picker_result_set", PickerActivity.copyPicker(null));
        fragmentActivity.startActivityForResult(intent, 31);
    }

    public static void enterPrivateAlbum(FragmentActivity fragmentActivity) {
        GalleryPreferences.Secret.setLastEnterPrivateAlbumTime(DateUtils.getCurrentTimeMillis());
        Intent intent = new Intent("com.miui.gallery.action.VIEW_SECRET_ALBUM_DETAIL");
        intent.putExtra("album_id", -1000L);
        intent.putExtra("album_name", fragmentActivity.getResources().getString(R.string.secret_album_display_name));
        intent.putExtra("album_server_id", String.valueOf(1000L));
        intent.putExtra("album_unwriteable", false);
        fragmentActivity.startActivityForResult(intent, 0);
    }

    public static void gotoTurnOnSyncSwitch(Context context) {
        if (context == null) {
            return;
        }
        if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
            context.startActivity(new Intent("com.xiaomi.action.MICLOUD_MAIN"));
            return;
        }
        Intent intent = new Intent(context, CloudSettingsActivity.class);
        intent.setAction("com.miui.gallery.cloud.provider.SYNC_SETTINGS");
        context.startActivity(intent);
    }

    public static void gotoTurnOnSyncSwitchInner(Context context) {
        if (context == null) {
            return;
        }
        if (!SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically()) {
            context.startActivity(new Intent("com.xiaomi.action.MICLOUD_MAIN"));
            return;
        }
        Intent intent = new Intent(context, CloudSettingsActivity.class);
        intent.setAction("com.miui.gallery.cloud.provider.SYNC_SETTINGS");
        intent.putExtra("use_dialog", true);
        context.startActivity(intent);
    }

    public static void enterGallerySetting(Context context) {
        Intent intent = new Intent(context, GallerySettingsActivity.class);
        intent.putExtra("use_dialog", true);
        context.startActivity(intent);
    }

    public static void startMotionPhotoAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment) {
        startExtraPhotoActivity(baseDataItem, fragmentActivity, photoPageFragment, "com.miui.extraphoto.action.MOTION_PHOTO_REPICK", 50);
    }

    public static void startAdvancedRefocusAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment) {
        startExtraPhotoActivity(baseDataItem, fragmentActivity, photoPageFragment, "com.miui.extraphoto.action.VIEW_ADVANCED_REFOCUS", 44);
    }

    public static void startDocPhotoAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment) {
        startExtraPhotoActivity(baseDataItem, fragmentActivity, photoPageFragment, "com.miui.extraphoto.action.VIEW_DOCUMENT_PHOTO", 53);
    }

    public static void startWatermarkAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment, View view, ImageView imageView) {
        int i;
        int i2;
        if (baseDataItem == null || fragmentActivity == null || fragmentActivity.isFinishing() || fragmentActivity.isDestroyed() || photoPageFragment == null) {
            return;
        }
        Uri contentUriForExternal = baseDataItem.getContentUriForExternal();
        if (contentUriForExternal == null) {
            ToastUtils.makeText(fragmentActivity, (int) R.string.photo_edit_file_not_exits_tip);
            return;
        }
        float[] fArr = new float[9];
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            i = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            imageView.getImageMatrix().getValues(fArr);
        } else {
            i = 0;
            i2 = 0;
        }
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            if (bitmap != null) {
                Glide.with(StaticContext.sGetAndroidContext()).mo985asBitmap().mo962load(PreloadModel.of(contentUriForExternal.toString(), bitmap)).mo950diskCacheStrategy(DiskCacheStrategy.NONE).preload();
            }
            DefaultLogger.d("IntentUtil", "cache preview: %s");
        }
        Intent intent = new Intent();
        intent.setClass(fragmentActivity, PrivacyWatermarkActivity.class);
        intent.setDataAndType(contentUriForExternal, "image/*");
        Bundle bundle = null;
        ActivityOptionsCompat makeSceneTransitionAnimation = (i <= 0 || i2 <= 0 || fragmentActivity.getResources().getConfiguration().orientation != 1 || !com.miui.gallery.compat.view.WindowCompat.supportActivityTransitions(fragmentActivity.getWindow())) ? null : ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, Pair.create(view, "tag_transition_view"));
        if (baseDataItem.isSecret()) {
            intent.putExtra("extra_is_secret", baseDataItem.isSecret());
            intent.putExtra("extra_secret_key", baseDataItem.getSecretKey());
            intent.putExtra("photo_secret_id", baseDataItem.getKey());
        }
        if (makeSceneTransitionAnimation != null) {
            bundle = makeSceneTransitionAnimation.toBundle();
        }
        try {
            fragmentActivity.startActivityFromFragment(photoPageFragment, intent, 64, bundle);
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", "start watermark activity error\n", e);
        }
    }

    public static void startCorrectDocAction(String str, FragmentActivity fragmentActivity, GalleryFragment galleryFragment, boolean z) {
        Intent intent = new Intent("com.miui.extraphoto.action.CORRECT_DOCUMENT");
        intent.setPackage("com.miui.extraphoto");
        intent.putExtra("StartActivityWhenLocked", z);
        intent.putExtra("extra_screen_brightness", fragmentActivity.getWindow().getAttributes().screenBrightness);
        intent.putExtra("extra_path", str);
        try {
            if (galleryFragment != null) {
                fragmentActivity.startActivityFromFragment(galleryFragment, intent, 59);
            } else {
                fragmentActivity.startActivityForResult(intent, 59);
            }
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", "start doc photo activity error\n", e);
        }
    }

    public static void startExtraPhotoActivity(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, PhotoPageFragment photoPageFragment, String str, int i) {
        View view;
        int i2;
        int i3;
        ActivityOptionsCompat activityOptionsCompat;
        Drawable drawable;
        if (baseDataItem == null || fragmentActivity == null || fragmentActivity.isFinishing() || fragmentActivity.isDestroyed() || photoPageFragment == null || (view = photoPageFragment.getView()) == null) {
            return;
        }
        View findViewById = view.findViewById(R.id.photo_pager);
        ImageView photoView = photoPageFragment.getPhotoView();
        float[] fArr = new float[9];
        if (photoView == null || (drawable = photoView.getDrawable()) == null) {
            i2 = 0;
            i3 = 0;
        } else {
            i3 = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            photoView.getImageMatrix().getValues(fArr);
        }
        Intent intent = new Intent(str);
        intent.setPackage("com.miui.extraphoto");
        intent.putExtra("extra_screen_brightness", fragmentActivity.getWindow().getAttributes().screenBrightness);
        intent.putExtra("extra_path", baseDataItem.getOriginalPath());
        Bundle bundle = null;
        if (i3 <= 0 || i2 <= 0 || fragmentActivity.getResources().getConfiguration().orientation != 1 || !com.miui.gallery.compat.view.WindowCompat.supportActivityTransitions(fragmentActivity.getWindow())) {
            activityOptionsCompat = null;
        } else {
            intent.putExtra("extra_preview_image_width", i3);
            intent.putExtra("extra_preview_image_height", i2);
            intent.putExtra("extra_preview_image_matrix", fArr);
            intent.putExtra("extra_has_transition", true);
            if (TextUtils.equals(str, "com.miui.extraphoto.action.MOTION_PHOTO_REPICK")) {
                intent.putExtra("extra_gif_display_matrix", ExtraPhotoSDK.calculateGifMatrixForMotionPhoto(photoView));
            }
            activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(fragmentActivity, Pair.create(findViewById, "tag_transition_view"), Pair.create(findViewById, "tag_transition_view_menu"));
        }
        if (activityOptionsCompat != null) {
            bundle = activityOptionsCompat.toBundle();
        }
        try {
            fragmentActivity.startActivityFromFragment(photoPageFragment, intent, i, bundle);
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", "start extra photo activity error\n", e);
        }
    }

    public static void guideToLoginXiaomiAccount(Activity activity, GalleryIntent$CloudGuideSource galleryIntent$CloudGuideSource, int i) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cloud_guide_source", galleryIntent$CloudGuideSource);
        Intent intent = new Intent(activity, CloudGuideWelcomeActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("use_dialog", true);
        activity.startActivityForResult(intent, i);
    }

    public static void guideToLoginXiaomiAccount(Context context, GalleryIntent$CloudGuideSource galleryIntent$CloudGuideSource) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("cloud_guide_source", galleryIntent$CloudGuideSource);
        guideToLoginXiaomiAccount(context, bundle);
    }

    public static void guideToLoginXiaomiAccount(Context context, Bundle bundle) {
        Intent intent = new Intent(context, CloudGuideWelcomeActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra("use_dialog", true);
        context.startActivity(intent);
    }

    public static void gotoTrashBin(Context context, String str) {
        context.startActivity(new Intent(context, TrashActivity.class));
        HashMap hashMap = new HashMap();
        hashMap.put("from", str);
        SamplingStatHelper.recordCountEvent("trash_bin", "enter_trash_bin_page", hashMap);
        TrackController.trackClick("403.7.2.1.10336", "403.7.0.1.10328");
    }

    public static void gotoWindControlManagement(Context context, String str) {
        String manageUrlFormat = HostManager.RiskControl.getManageUrlFormat(str);
        Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB_DEVICE_ID");
        intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, manageUrlFormat);
        context.startActivity(intent);
    }

    public static void gotoOperationCard(Context context, String str) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB_OPERATION_STORY");
        intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, str);
        context.startActivity(intent);
    }

    public static String getMiCloudVipPageSource(String str) {
        if (BaseBuildUtil.isInternational()) {
            return String.format("%s_%s", str, "oversea");
        }
        Locale localeFromContext = FileSizeFormatter.localeFromContext(GalleryApp.sGetAndroidContext());
        if (localeFromContext == null) {
            DefaultLogger.d("IntentUtil", "local is null");
            return str;
        }
        String language = localeFromContext.getLanguage();
        String country = localeFromContext.getCountry();
        if (TextUtils.equals("zh", language) && (TextUtils.equals("CN", country) || TextUtils.equals("TW", country))) {
            return String.format("%s_%s", str, "cncn");
        }
        return String.format("%s_%s", str, "cnuncn");
    }

    public static void gotoMiCloudVipPage(Context context, android.util.Pair<String, String>... pairArr) {
        gotoMiCloudVipPage(context, null, pairArr);
    }

    public static void gotoMiCloudVipPage(Context context, Intent intent, android.util.Pair<String, String>... pairArr) {
        Intent intent2 = new Intent("android.intent.action.VIEW");
        if (pairArr == null || pairArr.length == 0) {
            pairArr = new android.util.Pair[]{new android.util.Pair<>("source", "miui_gallery")};
        }
        try {
            intent2.setPackage("com.miui.cloudservice");
            String format = String.format(Locale.US, "micloud://com.miui.cloudservice/promotion?a=plsso&u=%s", URLEncoder.encode(HttpUtils.appendUrl("https://i.mi.com/vip", pairArr)));
            intent2.setData(Uri.parse(format));
            DefaultLogger.e("IntentUtil", "first url : %s", format);
            if (intent != null) {
                MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, intent);
            }
            context.startActivity(intent2);
        } catch (Exception e) {
            DefaultLogger.e("IntentUtil", "can't goto micloud vip page with first choice url", e);
            try {
                intent2.setComponent(new ComponentName("com.miui.cloudservice", "com.miui.cloudservice.ui.MiCloudHybridActivity"));
                String appendUrl = HttpUtils.appendUrl("https://i.mi.com/vip", pairArr);
                intent2.putExtra(HybridActivity.EXTRA_URL, appendUrl);
                DefaultLogger.e("IntentUtil", "spare url : %s", appendUrl);
                if (intent != null) {
                    MultiAppFloatingActivitySwitcher.configureFloatingService(intent2, intent);
                }
                context.startActivity(intent2);
            } catch (Exception e2) {
                DefaultLogger.e("IntentUtil", "can't goto micloud vip page with spare tire url", e2);
            }
        }
    }

    public static void gotoCloudPrivacy(Context context) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_WEB");
        intent.putExtra(MapBundleKey.MapObjKey.OBJ_URL, HostManager.CloudPrivacy.getCloudPrivacyUrl());
        context.startActivity(intent);
    }

    public static boolean gotoProblemFeedback(Context context) {
        HashMap hashMap = new HashMap();
        hashMap.put("appTitle", context.getString(R.string.continue_feedback));
        return gotoBugreport(context, hashMap);
    }

    public static void gotoDeepClean(Context context) {
        try {
            Intent intent = new Intent("miui.intent.action.GARBAGE_DEEPCLEAN");
            intent.putExtra("enter_homepage_way", "com.miui.gallery");
            intent.setPackage(getCorrectCleanerPkgName());
            if (!BaseMiscUtil.isIntentSupported(intent)) {
                return;
            }
            context.startActivity(intent);
        } catch (Exception unused) {
            DefaultLogger.e("IntentUtil", "can't goto deep clean page");
        }
    }

    public static String getCorrectCleanerPkgName() {
        return (!BaseBuildUtil.isInternational() || !BaseMiscUtil.isPackageInstalled("com.miui.cleaner")) ? "com.miui.cleanmaster" : "com.miui.cleaner";
    }

    public static boolean isContactPackageInstalled() {
        return BaseMiscUtil.isPackageInstalled("com.android.contacts") || BaseMiscUtil.isPackageInstalled("com.google.android.dialer");
    }

    public static boolean gotoAppDetailTraffic(Context context) {
        Intent intent = new Intent("miui.intent.action.NETWORKASSISTANT_APP_DETAIL");
        Bundle bundle = new Bundle();
        bundle.putString(d.am, context.getPackageName());
        bundle.putInt("title_type", 2);
        bundle.putInt("sort_type", 0);
        intent.putExtras(bundle);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            DefaultLogger.e("IntentUtil", "can't go to traffic page");
            return false;
        }
    }

    public static boolean gotoSettings(Context context) {
        try {
            context.startActivity(new Intent("android.settings.SETTINGS"));
            return true;
        } catch (Exception unused) {
            DefaultLogger.e("IntentUtil", "can't go to settings page");
            return false;
        }
    }

    public static boolean gotoWiFiSettings(Context context) {
        try {
            context.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
            return true;
        } catch (Exception unused) {
            DefaultLogger.e("IntentUtil", "can't go to wifi settings page");
            return gotoSettings(context);
        }
    }

    public static boolean gotoBugreport(Context context, Map<String, String> map) {
        try {
            Intent intent = new Intent("miui.intent.action.BUGREPORT");
            intent.putExtra("packageName", "com.miui.gallery");
            if (map != null && !map.isEmpty()) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    intent.putExtra(entry.getKey(), entry.getValue());
                }
            }
            context.startActivity(intent);
            return true;
        } catch (Exception unused) {
            DefaultLogger.w("IntentUtil", "can't go to bug report app");
            try {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.addCategory("android.intent.category.BROWSABLE");
                intent2.setData(Uri.parse("http://www.miui.com"));
                context.startActivity(intent2);
                return true;
            } catch (Exception unused2) {
                DefaultLogger.e("IntentUtil", "can't go to www.miui.com");
                return false;
            }
        }
    }

    public static boolean startCloudMainPage(Context context) {
        try {
            context.startActivity(new Intent("com.xiaomi.action.MICLOUD_MAIN"));
            return true;
        } catch (ActivityNotFoundException unused) {
            DefaultLogger.e("IntentUtil", "cloud main page start fail");
            return false;
        }
    }

    public static void jumpToExplore(Context context, String str) {
        try {
            Intent intent = new Intent("miui.intent.action.OPEN");
            intent.addFlags(268435456);
            intent.addFlags(Box.MAX_BOX_SIZE);
            if (Rom.IS_INTERNATIONAL) {
                intent.setPackage("com.mi.android.globalFileexplorer");
            } else {
                intent.setPackage("com.android.fileexplorer");
            }
            intent.putExtra("explorer_path", str);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isSupportXiaoai() {
        return sXiaoaiLoaded;
    }

    public static boolean isSpeechInputSupported() {
        if (BaseBuildUtil.isInternational() || !isSupportXiaoai()) {
            return false;
        }
        Intent intent = new Intent("com.xiaomi.mibrain.action.RECOGNIZE_SPEECH");
        intent.setPackage("com.xiaomi.mibrain.speech");
        return BaseMiscUtil.isIntentSupported(intent);
    }

    public static boolean startSpeechInput(FragmentActivity fragmentActivity, int i, boolean z, ArrayList<String> arrayList) {
        try {
            Intent intent = new Intent("com.xiaomi.mibrain.action.RECOGNIZE_SPEECH");
            intent.setPackage("com.xiaomi.mibrain.speech");
            intent.putExtra("miref", fragmentActivity.getPackageName());
            intent.putExtra("appId", "2882303761517492012");
            intent.putExtra("appToken", "527730249789");
            intent.putExtra("client_id", "2882303761517492012");
            intent.putExtra("api_key", "NwRthN_W6eI4dvXX47gZIlZdwBoDMT5t2Xu-7uciaqw");
            intent.putExtra("sign_secret", "wyRVnz7UEHQqNcwYAvANxiXBL25taWWPsSQGHbWIcNcEVYrU72NO0MBZjkqXuOk-iOXXiAnrMlZo78sJDhFreg");
            intent.putExtra("needNlp", z);
            if (BaseMiscUtil.isValid(arrayList)) {
                intent.putStringArrayListExtra("suggestedSpeechTexts", arrayList);
            }
            fragmentActivity.startActivityForResult(intent, i);
            return true;
        } catch (ActivityNotFoundException unused) {
            DefaultLogger.e("IntentUtil", "Start speech input error");
            return false;
        }
    }

    public static void gotoSeeMoreSettingsActivity(Context context, boolean z, boolean z2) {
        Intent intent = new Intent(context, BackupSettingsMoreActivity.class);
        intent.putExtra("use_dialog", z);
        if (z2) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        context.startActivity(intent);
    }

    public static void gotoPrivacySettingsActivity(Context context, boolean z, boolean z2) {
        Intent intent = new Intent(context, PrivacySettingsActivity.class);
        intent.putExtra("use_dialog", z);
        if (z2) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        context.startActivity(intent);
    }

    public static void gotoPermissionSettingsActivity(Context context, boolean z, boolean z2) {
        Intent intent = new Intent(context, PermissionSettingsActivity.class);
        intent.putExtra("use_dialog", z);
        if (z2) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        context.startActivity(intent);
    }

    public static void gotoPrivacyPolicy(FragmentActivity fragmentActivity) {
        fragmentActivity.startActivity(CtaAgreement.Licence.getPrivacyIntent());
    }

    public static void gotoBDMapPrivacyPolicy(FragmentActivity fragmentActivity) {
        fragmentActivity.startActivity(CtaAgreement.Licence.getMapPrivacyIntent());
    }

    public static String getCallingPackage(FragmentActivity fragmentActivity) {
        Uri referrer;
        ComponentName callingActivity;
        String callingPackage = fragmentActivity.getCallingPackage();
        if (TextUtils.isEmpty(callingPackage) && (callingActivity = fragmentActivity.getCallingActivity()) != null) {
            callingPackage = callingActivity.getPackageName();
        }
        return (!TextUtils.isEmpty(callingPackage) || Build.VERSION.SDK_INT < 22 || (referrer = fragmentActivity.getReferrer()) == null) ? callingPackage : referrer.toString();
    }

    public static /* synthetic */ void lambda$startPhotoMovie$1() {
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.photo_movie_module_loading);
    }

    public static void startPhotoMovie(FragmentActivity fragmentActivity, List<MediaInfo> list, long j, int i, String str, String str2) {
        Intent intent;
        if (MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity, IntentUtil$$ExternalSyntheticLambda0.INSTANCE) && BaseMiscUtil.isValid(list)) {
            int i2 = 0;
            ClipData clipData = null;
            for (int i3 = 0; i3 < list.size() && i2 < 20; i3++) {
                if (list.get(i3) != null) {
                    Uri translateToContent = GalleryOpenProvider.translateToContent(list.get(i3).getUri());
                    String sha1 = list.get(i3).getSha1();
                    if (clipData == null) {
                        clipData = new ClipData("data", new String[]{"image/*", "text/uri-list"}, new ClipData.Item(sha1, null, translateToContent));
                    } else {
                        clipData.addItem(new ClipData.Item(sha1, null, translateToContent));
                    }
                    i2++;
                }
            }
            if (MediaEditorUtils.isMediaEditorAvailable() && PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK()) {
                intent = new Intent();
                if (MediaEditorApiHelper.isPhotoMovieAvailable()) {
                    intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.movie.ui.activity.MovieActivity"));
                } else {
                    intent.putExtra("loadType", "photoMovie");
                    intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.mediaeditor.activity.DownloadLibraryActivity"));
                }
            } else {
                intent = new Intent(fragmentActivity, MovieActivity.class);
            }
            intent.putExtra("movie_extra_preview_mode", true);
            intent.putExtra("card_id", j);
            intent.putExtra("card_title", str);
            intent.putExtra("card_sub_title", str2);
            intent.putExtra("movie_extra_template", i);
            intent.setClipData(clipData);
            fragmentActivity.startActivity(intent);
        }
    }

    public static void gotoStoryAlbum(FragmentActivity fragmentActivity, long j) {
        if (fragmentActivity != null) {
            Intent intent = new Intent(fragmentActivity, StoryAlbumActivity.class);
            intent.putExtra("card_id", j);
            fragmentActivity.startActivity(intent);
        }
    }

    public static boolean checkCreationCondition(FragmentActivity fragmentActivity, int i) {
        if (i == 8) {
            return PrintInstaller.getInstance().ensurePrintFucntionAvailable();
        }
        if (i == 4) {
            return true;
        }
        if (i == 6 && !PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK()) {
            return MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (MediaEditorUtils.isMediaEditorAvailable() || i == 9) {
            return true;
        }
        if (i == 0) {
            return MagicMattingLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (i == 1) {
            return IDPhotoLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (i == 2) {
            return ArtStillLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (i == 3) {
            return VideoPostDownloadManager.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (i == 5) {
            return true;
        }
        if (i == 6) {
            return MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        if (i == 7) {
            return VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity);
        }
        return false;
    }

    public static void doCreation(final FragmentActivity fragmentActivity, final int i, final List<CheckableAdapter.CheckedItem> list, Picker.ImageType imageType) {
        if (list.isEmpty()) {
            DefaultLogger.e("IntentUtil", "No selected Item!");
        } else {
            new UriGenerator(fragmentActivity, list, imageType, new UriGenerator.UriGenerateListener() { // from class: com.miui.gallery.util.IntentUtil.2
                @Override // com.miui.gallery.picker.uri.UriGenerator.UriGenerateListener
                public void onFail() {
                    IntentUtil.recordUriGenerateError();
                }

                @Override // com.miui.gallery.picker.uri.UriGenerator.UriGenerateListener
                public void onSuccess(Uri[] uriArr, List<OriginUrlRequestor.OriginInfo> list2) {
                    Intent intent;
                    boolean isMediaEditorAvailable = MediaEditorUtils.isMediaEditorAvailable();
                    switch (i) {
                        case 0:
                            if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportMagicMatting()) {
                                if (MediaEditorApiHelper.isMagicMattingAvailable()) {
                                    Intent intent2 = new Intent();
                                    intent2.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.magic.matting.MattingActivity"));
                                    intent2.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
                                    intent2.setFlags(1);
                                    fragmentActivity.startActivity(intent2);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "magicMatting");
                                return;
                            } else if (!MagicMattingLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.startMagicMatting(fragmentActivity, uriArr);
                                return;
                            }
                        case 1:
                            if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportIDPhoto()) {
                                if (MediaEditorApiHelper.isIDPhotoAvailable()) {
                                    Intent intent3 = new Intent();
                                    intent3.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.magic.idphoto.CertificatesGuideActivity"));
                                    intent3.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
                                    intent3.setFlags(1);
                                    intent3.putExtra("from_creation", true);
                                    fragmentActivity.startActivity(intent3);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "idPhoto");
                                return;
                            } else if (!IDPhotoLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.startIDPhotoFromCreation(fragmentActivity, uriArr);
                                return;
                            }
                        case 2:
                            if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportArtStill()) {
                                if (MediaEditorApiHelper.isArtStillAvailable()) {
                                    Intent intent4 = new Intent();
                                    intent4.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.magic.special.effects.image.SpecialEffectsActivity"));
                                    intent4.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
                                    intent4.setFlags(1);
                                    intent4.putExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, 0);
                                    fragmentActivity.startActivity(intent4);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "artStill");
                                return;
                            } else if (!ArtStillLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.startArtStill(fragmentActivity, uriArr);
                                return;
                            }
                        case 3:
                            if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportVideoPost()) {
                                if (MediaEditorApiHelper.isVideoPostAvailable()) {
                                    Intent intent5 = new Intent();
                                    intent5.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.magic.special.effects.video.VideoCutActivity"));
                                    intent5.setData(GalleryOpenProvider.translateToContent(uriArr[0].getPath()));
                                    intent5.setFlags(1);
                                    fragmentActivity.startActivity(intent5);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "videoPost");
                                return;
                            } else if (!VideoPostDownloadManager.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.startVideoPost(fragmentActivity, uriArr);
                                return;
                            }
                        case 4:
                            IntentUtil.startMacarons(fragmentActivity, uriArr);
                            return;
                        case 5:
                            if (isMediaEditorAvailable) {
                                intent = new Intent();
                                intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.collage.CollageActivity"));
                            } else {
                                intent = new Intent(fragmentActivity, CollageActivity.class);
                            }
                            IntentUtil.dispatchImageUrisInternalIntent(fragmentActivity, intent, uriArr);
                            return;
                        case 6:
                            if (isMediaEditorAvailable && PhotoMovieEntranceUtils.isPhotoMovieUseMiSDK() && MediaEditorApiHelper.isDeviceSupportPhotoMovie()) {
                                if (MediaEditorApiHelper.isPhotoMovieAvailable()) {
                                    Intent intent6 = new Intent();
                                    intent6.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.movie.ui.activity.MovieActivity"));
                                    IntentUtil.dispatchImageUrisInternalIntent(fragmentActivity, intent6, uriArr);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "photoMovie");
                                return;
                            } else if (!MovieLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.dispatchImageUrisInternalIntent(fragmentActivity, new Intent(fragmentActivity, MovieActivity.class), uriArr);
                                return;
                            }
                        case 7:
                            if (isMediaEditorAvailable && MediaEditorApiHelper.isDeviceSupportVlog()) {
                                if (MediaEditorApiHelper.isVlogAvailable()) {
                                    Intent intent7 = new Intent();
                                    intent7.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.vlog.match.vlog.rule.VlogTemplateMatchActivity"));
                                    IntentUtil.dispatchVideoUrisInternalIntent(fragmentActivity, intent7, uriArr);
                                    return;
                                }
                                MediaEditorIntentUtils.loadLibraryInMediaEditor(fragmentActivity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", "vlog");
                                return;
                            } else if (!VlogLibraryLoaderHelper.getInstance().checkAbleOrDownload(fragmentActivity)) {
                                return;
                            } else {
                                IntentUtil.dispatchVideoUrisInternalIntent(fragmentActivity, new Intent(fragmentActivity, VlogTemplateMatchActivity.class), uriArr);
                                return;
                            }
                        case 8:
                            PrintInstaller.getInstance().printPhotos(fragmentActivity, uriArr, list2);
                            return;
                        case 9:
                            PicToPdfHelper.prepareGotoPicToPdfPreviewPage(fragmentActivity, list);
                            return;
                        default:
                            return;
                    }
                }
            }).generate();
        }
    }

    public static void recordUriGenerateError() {
        DefaultLogger.e("IntentUtil", "checkedItem Uri generate error");
        SamplingStatHelper.recordCountEvent("creation", "checked_item_uri_error");
    }

    public static void dispatchImageUrisInternalIntent(FragmentActivity fragmentActivity, Intent intent, Uri[] uriArr) {
        if (uriArr.length == 0) {
            DefaultLogger.e("IntentUtil", "No result Uris to dispatch!");
            return;
        }
        if (GalleryOpenProvider.needReturnContentUri(fragmentActivity, "")) {
            for (int i = 0; i < uriArr.length; i++) {
                if (uriArr[i] != null) {
                    uriArr[i] = GalleryOpenProvider.translateToContent(uriArr[i].getPath());
                }
            }
        }
        ClipData clipData = new ClipData("data", new String[]{"image/*", "text/uri-list"}, new ClipData.Item(uriArr[0]));
        for (int i2 = 1; i2 < uriArr.length; i2++) {
            clipData.addItem(new ClipData.Item(uriArr[i2]));
        }
        intent.setClipData(clipData);
        intent.setFlags(1);
        try {
            fragmentActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DefaultLogger.e("IntentUtil", e);
        }
    }

    public static void dispatchVideoUrisInternalIntent(FragmentActivity fragmentActivity, Intent intent, Uri[] uriArr) {
        if (uriArr.length == 0) {
            DefaultLogger.e("IntentUtil", "No result Uris to dispatch!");
            return;
        }
        if (GalleryOpenProvider.needReturnContentUri(fragmentActivity, "")) {
            for (int i = 0; i < uriArr.length; i++) {
                if (uriArr[i] != null) {
                    uriArr[i] = GalleryOpenProvider.translateToContent(uriArr[i].getPath());
                }
            }
        }
        ClipData clipData = new ClipData("data", new String[]{"video/*", "text/uri-list"}, new ClipData.Item(uriArr[0]));
        for (int i2 = 1; i2 < uriArr.length; i2++) {
            clipData.addItem(new ClipData.Item(uriArr[i2]));
        }
        intent.setClipData(clipData);
        intent.setFlags(1);
        try {
            fragmentActivity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DefaultLogger.e("IntentUtil", e);
        }
    }

    public static void gotoSinglePhotoPage(Context context, Uri uri) {
        Intent intent = new Intent(context, ExternalPhotoPageActivity.class);
        intent.setData(uri);
        ArrayList arrayList = new ArrayList();
        arrayList.add(uri);
        intent.putExtra("com.miui.gallery.extra.photo_items", arrayList);
        intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        context.startActivity(intent);
    }

    public static void gotoHiddenAlbumPage(Context context, boolean z, boolean z2) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("com.miui.gallery.action.SETTING_MAIN");
        intent.putExtra("extra_to_type", 1001);
        intent.putExtra("use_dialog", z);
        if (z2) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        context.startActivity(intent);
    }

    public static void gotoCloudAlbumListPage(Context context, boolean z, boolean z2) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("com.miui.gallery.action.SETTING_MAIN");
        intent.putExtra("extra_to_type", 1002);
        intent.putExtra("use_dialog", z);
        if (z2) {
            SplitUtils.addMiuiFlags(intent, 16);
        }
        context.startActivity(intent);
    }

    public static void gotoAlbumDetailPage(Context context, Album album, int i, int i2) {
        Intent intent;
        Intent intent2;
        if (album == null) {
            return;
        }
        long albumId = album.getAlbumId();
        if (album.isAllPhotosAlbum()) {
            TimeMonitor.createNewTimeMonitor("403.44.0.1.13763");
            intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(GalleryContract.RecentAlbum.ALL_PHOTOS_VIEW_PAGE_URI.buildUpon().appendQueryParameter("source", "album_page").build());
            intent2.setPackage(context.getPackageName());
        } else {
            boolean z = false;
            if (album.isScreenshotsRecorders()) {
                intent = new Intent(context, AlbumDetailGroupingActivity.class);
                intent.putExtra("group_first_album_id", AlbumCacheManager.getInstance().getScreenshotsAlbumId());
                intent.putExtra("group_second_album_id", AlbumCacheManager.getInstance().getScreenRecordersAlbumId());
                intent.putExtra("group_first_album_name", context.getString(R.string.album_screenshot_name));
                intent.putExtra("group_second_album_name", context.getString(R.string.album_screen_record));
            } else if (album.isBabyAlbum()) {
                intent = new Intent(context, BabyAlbumDetailActivity.class);
                intent.putExtra("people_id", album.getPeopleId());
                intent.putExtra("baby_info", album.getBabyInfo());
                intent.putExtra("thumbnail_info_of_baby", album.getThumbnailInfoOfBaby());
                intent.putExtra("baby_sharer_info", album.getShareInfo());
                intent.putExtra("photodetail_is_photo_datetime_editable", false);
            } else {
                intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
            }
            boolean isOtherShareAlbum = album.isOtherShareAlbum();
            intent.putExtra("other_share_album", isOtherShareAlbum);
            if (isOtherShareAlbum) {
                intent.putExtra("photodetail_is_photo_datetime_editable", false);
            }
            intent.putExtra("owner_share_album", album.isOwnerShareAlbum());
            if (!SyncUtil.existXiaomiAccount(GalleryApp.sGetAndroidContext()) || !album.isAutoUploadedAlbum()) {
                z = true;
            }
            intent.putExtra("is_local_album", z);
            intent.putExtra("screenshot_album", album.isScreenshotsAlbum());
            intent.putExtra("screenrecorder_album", album.isScreenRecorderAlbum());
            intent.putExtra("screenshot_recorder_album", album.isScreenshotsRecorders());
            intent.putExtra("video_album", album.isVideoAlbum());
            intent.putExtra("album_id", albumId);
            intent.putExtra("album_name", album.getDisplayedAlbumName());
            intent.putExtra("album_server_id", album.getServerId());
            intent.putExtra("attributes", album.getAttributes());
            intent.putExtra("album_unwriteable", album.isImmutable());
            intent.putExtra("album_local_path", album.getLocalPath());
            intent.putExtra("extra_from_type", i);
            intent2 = intent;
        }
        intent2.putExtra("extra_is_manual_set_cover", album.isManualSetCover());
        statGoToAlbumDetail(album, i);
        trackGoToAlbumDetail(album, i);
        if (context == null) {
            return;
        }
        if (-1 != i2 && (context instanceof Activity)) {
            ((Activity) context).startActivityForResult(intent2, i2);
        } else {
            context.startActivity(intent2);
        }
    }

    public static void statGoToAlbumDetail(Album album, int i) {
        String str;
        if (i == 1003) {
            return;
        }
        if (album.isAllPhotosAlbum()) {
            str = "all_photo";
        } else if (album.isFavoritesAlbum()) {
            str = "favorite";
        } else if (album.isCameraAlbum()) {
            str = "camera";
        } else if (album.isVideoAlbum()) {
            str = "video";
        } else if (album.isScreenshotsAlbum()) {
            str = "screen_capture";
        } else if (album.isScreenshotsRecorders()) {
            str = "screenshot_and_recorder";
        } else if (album.isUserCreateAlbum()) {
            str = "owner_album";
        } else if (album.isBabyAlbum()) {
            str = "baby_photo";
        } else {
            str = album.isShareAlbum() ? "share_photo" : "app";
        }
        statGoToAlbumDetail(str, album.getLocalPath());
    }

    public static void statGoToAlbumDetail(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
            hashMap.put(nexExportFormat.TAG_FORMAT_PATH, str2);
            SamplingStatHelper.recordCountEvent("album", "click_album", hashMap);
        }
    }

    public static void checkLoginAndSyncState(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putInt("check_login_and_sync", 3);
        bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SECRET);
        LoginAndSyncCheckFragment.checkLoginAndSyncState(fragment, bundle);
    }

    public static void gotoPrivateAlbum(Fragment fragment) {
        if (AccountHelper.getXiaomiAccount(fragment.getActivity()) != null) {
            AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(fragment);
        }
    }

    public static void enterGalleryAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", GalleryApp.sGetAndroidContext().getPackageName(), null));
        context.startActivity(intent);
    }

    public static void enterGalleryPermissionSetting(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        if (packageManager.queryIntentActivities(intent, 131072).size() > 0) {
            intent.putExtra("extra_pkgname", context.getApplicationContext().getPackageName());
            intent.addFlags(268435456);
            context.startActivity(intent);
            return;
        }
        enterGalleryAppSetting(context);
    }

    public static void enterManageExternalStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= 30) {
            context.startActivity(new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"));
        }
    }

    public static void gotoAlbumDetailPage(Context context, Album album, int i) {
        gotoAlbumDetailPage(context, album, i, -1);
    }

    public static void gotoAlbumDetailPage(Context context, Album album) {
        gotoAlbumDetailPage(context, album, -1);
    }

    public static void gotoAlbumDetailPageFromOtherAlbum(Context context, Album album) {
        gotoAlbumDetailPage(context, album, 1003, 1003);
    }

    public static void gotoAlbumDetailPageFromRubbishAlbum(Context context, Album album) {
        gotoAlbumDetailPage(context, album, 1004);
    }

    public static void gotoOtherAlbumPage(Context context) {
        Intent intent = new Intent("com.miui.gallery.action.ALBUM_MAIN");
        intent.putExtra("extra_to_type", 1003);
        if (context != null) {
            context.startActivity(intent);
        }
        TrackController.trackClick("403.7.2.1.10337", "403.7.0.1.10328");
    }

    public static void gotoAIAlbumPage(Context context) {
        Intent intent = new Intent("com.miui.gallery.action.ALBUM_MAIN");
        intent.putExtra("extra_to_type", 1005);
        statGoToAlbumDetail("smart_album", "");
        if (context != null) {
            context.startActivity(intent);
        }
        TrackController.trackClick("403.7.2.1.10338", "403.7.0.1.10328");
    }

    public static void gotoAIAlbumPage(Context context, Uri uri) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        if (uri != null) {
            intent.setData(uri);
            intent.setPackage(GalleryApp.sGetAndroidContext().getPackageName());
            context.startActivity(intent);
            return;
        }
        DefaultLogger.e("IntentUtil", "error goto ai album");
    }

    public static void gotoPeopleListAlbumPage(Context context) {
        gotoAIAlbumPage(context, GalleryContract.Common.URI_PEOPLE_LIST_PAGE);
    }

    public static void gotoPeopleDetailFacePage(Context context, String str, String str2, String str3, int i, String str4, FaceRegionRectF faceRegionRectF) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("server_id_of_album", str);
        bundle.putString("local_id_of_album", str2);
        bundle.putString("album_name", str3);
        bundle.putInt("relationType", i);
        bundle.putString("face_album_cover", str4);
        bundle.putParcelable("face_position_rect", faceRegionRectF);
        intent.putExtras(bundle);
        intent.setClass(context, FacePageActivity.class);
        context.startActivity(intent);
    }

    public static void gotoRubbishAlbumPage(Context context) {
        Intent intent = new Intent("com.miui.gallery.action.ALBUM_MAIN");
        intent.putExtra("extra_to_type", 1004);
        if (context != null) {
            context.startActivity(intent);
        }
    }

    public static void gotoPhotoPage(Context context, String str) {
        Intent intent = new Intent(context, ExternalPhotoPageActivity.class);
        intent.setData(Uri.fromFile(new File(str)));
        intent.putExtra("com.miui.gallery.extra.deleting_include_cloud", true);
        context.startActivity(intent);
    }

    public static boolean insertTextToNotes(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.setType("text/plain");
        intent.setPackage("com.miui.notes");
        if (checkIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        }
        ToastUtils.makeText(context, (int) R.string.ocr_note_not_existed);
        return false;
    }

    public static boolean checkIntent(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, 0).size() > 0;
    }

    public static void shareText(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", str);
        context.startActivity(Intent.createChooser(intent, ""));
    }

    public static void trackGoToAlbumDetail(Album album, int i) {
        String str;
        HashMap hashMap = new HashMap();
        if (album.isBabyAlbum()) {
            str = "baby";
        } else if (album.isOtherShareAlbum()) {
            str = "sharer";
        } else if (album.isScreenshotsAlbum()) {
            str = "screenshot";
        } else if (album.isScreenRecorderAlbum()) {
            str = "screen_recorder";
        } else if (album.isScreenshotsRecorders()) {
            str = "screenshot_and_recorder";
        } else if (album.isSystemAlbum()) {
            trackEnterSystemAlbum(album);
            str = "system";
        } else if (album.isUserCreateAlbum()) {
            str = "owner";
        } else {
            hashMap.put(nexExportFormat.TAG_FORMAT_PATH, album.getLocalPath());
            str = "app";
        }
        hashMap.put("ref_tip", i == 1003 ? "403.39.0.1.11132" : "403.7.0.1.10328");
        hashMap.put("tip", i == 1003 ? "403.40.1.1.11124" : "403.7.2.1.10339");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        TrackController.trackClick(hashMap);
    }

    public static void trackEnterSystemAlbum(Album album) {
        if (album.isAllPhotosAlbum()) {
            TrackController.trackClick("403.7.2.1.10332", "403.7.0.1.10328");
        } else if (album.isVideoAlbum()) {
            TrackController.trackClick("403.7.2.1.10335", "403.7.0.1.10328");
        } else if (album.isCameraAlbum()) {
            TrackController.trackClick("403.7.2.1.10334", "403.7.0.1.10328");
        } else if (!album.isFavoritesAlbum()) {
        } else {
            TrackController.trackClick("403.7.2.1.10333", "403.7.0.1.10328");
        }
    }

    public static void gotoStorageTestPage(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent("com.miui.gallery.action.STORAGE_TEST"));
    }

    public static void gotoAlbumPermissionActivity(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent("com.miui.gallery.action.OPERATE_ALBUM_PERMISSION"));
    }

    public static void checkAndGoToMapAlbum(Context context, String str, double[] dArr, boolean z) {
        if (!MapInitializerImpl.checkInitialized()) {
            handleEnterMapAlbumFail();
            return;
        }
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("extra_initial_location", dArr);
        intent.putExtra("extra_show_nearby", z);
        intent.putExtra("extra_initial_media_title", str);
        context.startActivity(intent);
    }

    public static void goToMapAlbumDirectly(Context context, String str, double[] dArr, boolean z) {
        Intent intent = new Intent(context, MapActivity.class);
        intent.putExtra("extra_initial_location", dArr);
        intent.putExtra("extra_show_nearby", z);
        intent.putExtra("extra_initial_media_title", str);
        context.startActivity(intent);
    }

    public static void goToMapAlbumDirectly(Context context) {
        context.startActivity(new Intent(context, MapActivity.class));
    }

    public static void handleEnterMapAlbumFail() {
        ToastUtils.makeText(StaticContext.sGetAndroidContext(), (int) R.string.map_common_download_failed_msg);
        DefaultLogger.w("MapActivity", "Enter map album failed, from: %s", AutoTracking.getRef());
    }

    public static void goToLocationServivePage(Context context) {
        context.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
    }

    public static void goToAppPermissionEditor(Context context) {
        try {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.putExtra("extra_pkgname", "com.miui.gallery");
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            Intent intent2 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent2.setData(Uri.fromParts("package", "com.miui.gallery", null));
            context.startActivity(intent2);
        }
    }
}
