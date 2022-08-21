package com.miui.gallery.util;

import android.content.ComponentName;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.JumpDialogFragment;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import com.xiaomi.stat.MiStat;
import java.util.Map;

/* loaded from: classes2.dex */
public class ActionURIHandler {
    public static final UriMatcher sURIMatcher;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sURIMatcher = uriMatcher;
        uriMatcher.addURI("gallery.i.mi.com", "people", 1);
        uriMatcher.addURI("gallery.i.mi.com", "album", 2);
        uriMatcher.addURI("gallery.i.mi.com", "trash_bin", 3);
        uriMatcher.addURI("gallery.i.mi.com", "secret_album", 4);
        uriMatcher.addURI("gallery.i.mi.com", "peoples", 5);
        uriMatcher.addURI("gallery.i.mi.com", "result", 6);
        uriMatcher.addURI("gallery.i.mi.com", MiStat.Event.SEARCH, 7);
        uriMatcher.addURI("gallery.i.mi.com", "cloud_guide", 8);
        uriMatcher.addURI("gallery.i.mi.com", "sync_switch", 9);
        uriMatcher.addURI("gallery.i.mi.com", "photo_movie", 10);
        uriMatcher.addURI("gallery.i.mi.com", "collage", 11);
        uriMatcher.addURI("gallery.i.mi.com", "card_action", 12);
        uriMatcher.addURI("gallery.i.mi.com", "collage_from_picker", 13);
        uriMatcher.addURI("gallery.i.mi.com", "filter_sky", 14);
        uriMatcher.addURI("gallery.i.mi.com", "vlog", 15);
        uriMatcher.addURI("gallery.i.mi.com", "macarons", 16);
        uriMatcher.addURI("gallery.i.mi.com", "magic_matting", 17);
        uriMatcher.addURI("gallery.i.mi.com", "id_photo", 18);
        uriMatcher.addURI("gallery.i.mi.com", "art_still", 19);
        uriMatcher.addURI("gallery.i.mi.com", "video_post", 20);
        uriMatcher.addURI("gallery.i.mi.com", "map_album", 21);
    }

    public static void handleUri(FragmentActivity fragmentActivity, Uri uri) {
        handleUri(fragmentActivity, uri, null);
    }

    public static void handleUri(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
        if (fragmentActivity == null || fragmentActivity.isDestroyed() || uri == null) {
            return;
        }
        int match = sURIMatcher.match(uri);
        String queryParameter = uri.getQueryParameter("query");
        Map<String, String> buildSearchEventParams = SearchStatUtils.buildSearchEventParams(bundle);
        switch (match) {
            case 1:
                String queryParameter2 = uri.getQueryParameter("serverId");
                JumpDialogFragment.showPeoplePage(fragmentActivity, queryParameter2);
                if (buildSearchEventParams == null) {
                    return;
                }
                buildSearchEventParams.put("serverIds", queryParameter2);
                buildSearchEventParams.put("queryText", queryParameter);
                SearchStatUtils.reportEvent(null, "client_jump_to_people_page", buildSearchEventParams);
                return;
            case 2:
                JumpDialogFragment.showAlbumPage(fragmentActivity, uri, bundle);
                if (buildSearchEventParams == null) {
                    return;
                }
                String queryParameter3 = uri.getQueryParameter("screenshotAppName");
                buildSearchEventParams.put("Uri", uri.toString());
                buildSearchEventParams.put("queryText", queryParameter3);
                SearchStatUtils.reportEvent(null, "client_jump_to_album_page", buildSearchEventParams);
                return;
            case 3:
                IntentUtil.gotoTrashBin(fragmentActivity, "Search");
                if (buildSearchEventParams == null) {
                    return;
                }
                SearchStatUtils.reportEvent(null, "client_jump_to_trash_bin", buildSearchEventParams);
                return;
            case 4:
                JumpDialogFragment.enterPrivateAlbum(fragmentActivity);
                if (buildSearchEventParams == null) {
                    return;
                }
                SearchStatUtils.reportEvent(null, "client_jump_to_secret_album", buildSearchEventParams);
                return;
            case 5:
                if (uri.getBooleanQueryParameter("markMode", false)) {
                    startActivityForResult(fragmentActivity, uri, bundle, 41);
                    if (buildSearchEventParams == null) {
                        return;
                    }
                    buildSearchEventParams.put(MapBundleKey.MapObjKey.OBJ_URL, uri.toString());
                    SearchStatUtils.reportEvent(null, "suggestion_open_mark_page", buildSearchEventParams);
                    return;
                }
                openDirectly(fragmentActivity, uri, bundle);
                if (buildSearchEventParams != null) {
                    buildSearchEventParams.put(MapBundleKey.MapObjKey.OBJ_URL, uri.toString());
                    SearchStatUtils.reportEvent(null, "client_jump_to_people_list_page", buildSearchEventParams);
                }
                TrackController.trackClick("403.20.0.1.11268", AutoTracking.getRef());
                return;
            case 6:
                openDirectly(fragmentActivity, uri, bundle);
                return;
            case 7:
                openDirectly(fragmentActivity, uri, bundle);
                return;
            case 8:
                IntentUtil.guideToLoginXiaomiAccount(fragmentActivity, GalleryIntent$CloudGuideSource.URL);
                return;
            case 9:
                IntentUtil.gotoTurnOnSyncSwitch(fragmentActivity);
                return;
            case 10:
                PhotoMovieEntranceUtils.startPicker(fragmentActivity);
                return;
            case 11:
                IntentUtil.startCollagePicker(fragmentActivity);
                return;
            case 12:
                String queryParameter4 = uri.getQueryParameter("actionType");
                if ("album".equals(queryParameter4)) {
                    if (bundle != null) {
                        IntentUtil.gotoStoryAlbum(fragmentActivity, bundle.getLong("card_id"));
                        return;
                    } else {
                        DefaultLogger.e("ActionURIHandler", "go to strory album ,invalid extra");
                        return;
                    }
                } else if ("operation".equals(queryParameter4)) {
                    IntentUtil.gotoOperationCard(fragmentActivity, bundle.getString("card_url"));
                    return;
                } else {
                    DefaultLogger.w("ActionURIHandler", "type %s can not delivery", queryParameter4);
                    return;
                }
            case 13:
                if (bundle == null) {
                    return;
                }
                Intent intent = (Intent) bundle.getParcelable("extra_intent");
                boolean z = bundle.getBoolean("start_activity_for_result", false);
                int i = bundle.getInt("request_code", -1);
                if (intent == null) {
                    return;
                }
                boolean isMediaEditorAvailable = MediaEditorUtils.isMediaEditorAvailable();
                if ((IntentUtil.isSupportMeituCollage() ? IntentUtil.startMeituCollageAction(fragmentActivity, intent.getParcelableArrayListExtra("pick-result-data"), i) : false) || !MediaEditorInstaller.getInstance().installIfNotExist(fragmentActivity, null, false)) {
                    return;
                }
                if (isMediaEditorAvailable) {
                    intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.collage.CollageActivity"));
                } else {
                    intent.setClass(fragmentActivity, CollageActivity.class);
                }
                if (z) {
                    fragmentActivity.startActivityForResult(intent, i);
                    return;
                } else {
                    fragmentActivity.startActivity(intent);
                    return;
                }
            case 14:
                FilterSkyEntranceUtils.startFilterSkyFromPicker(fragmentActivity);
                return;
            case 15:
                IntentUtil.startVlogFromPicker(fragmentActivity);
                return;
            case 16:
                IntentUtil.startMacaronsPicker(fragmentActivity);
                return;
            case 17:
                IntentUtil.startMagicMattingFromPicker(fragmentActivity);
                return;
            case 18:
                IntentUtil.startIDPhotoGuide(fragmentActivity);
                return;
            case 19:
                IntentUtil.startArtStillGuide(fragmentActivity);
                return;
            case 20:
                IntentUtil.startVideoPostGuide(fragmentActivity);
                return;
            case 21:
                IntentUtil.goToMapAlbumDirectly(fragmentActivity);
                return;
            default:
                openDirectly(fragmentActivity, uri, bundle);
                return;
        }
    }

    public static void startActivityForResult(FragmentActivity fragmentActivity, Uri uri, Bundle bundle, int i) {
        try {
            fragmentActivity.startActivityForResult(buildIntent(fragmentActivity, uri, bundle), i);
        } catch (Exception e) {
            DefaultLogger.e("ActionURIHandler", "openDirectly %s occur error.\n", uri, e);
        }
    }

    public static void openDirectly(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
        int i;
        boolean z = false;
        if (bundle != null) {
            try {
                z = bundle.getBoolean("start_activity_for_result", false);
                bundle.remove("start_activity_for_result");
                i = bundle.getInt("request_code");
                bundle.remove("request_code");
                String extractSourceFromBundle = SearchStatUtils.extractSourceFromBundle(bundle);
                if (extractSourceFromBundle != null) {
                    if (extractSourceFromBundle.equals("from_location_list")) {
                        TimeMonitor.createNewTimeMonitor("403.18.0.1.13787");
                    } else if (extractSourceFromBundle.equals("from_tag_list")) {
                        TimeMonitor.createNewTimeMonitor("403.19.0.1.13788");
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("ActionURIHandler", "openDirectly %s occur error.\n", uri, e);
                return;
            }
        } else {
            i = 0;
        }
        Intent buildIntent = buildIntent(fragmentActivity, uri, bundle);
        if (z) {
            fragmentActivity.startActivityForResult(buildIntent, i);
        } else {
            fragmentActivity.startActivity(buildIntent);
        }
    }

    public static Intent buildIntent(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.setPackage(fragmentActivity.getApplicationContext().getPackageName());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        return intent;
    }
}
