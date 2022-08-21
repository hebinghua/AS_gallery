package com.miui.gallery.preference;

import android.content.SharedPreferences;
import android.text.TextUtils;
import androidx.annotation.Keep;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class GalleryPreferences {

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface OneshotAction {
        boolean exec();
    }

    @Keep
    /* loaded from: classes2.dex */
    public interface PrefKeys {
        public static final String ADDRESS_VERSION = "address_version";
        public static final String ADD_TO_ALBUM_DELETE_ORIGIN_CHECKED = "add_to_album_delete_origin_checked";
        public static final String ADD_TO_ALBUM_PAGE_LAST_SELECTED_ALBUM_LIST = "add_to_album_page_last_selected_album_list";
        public static final String ALBUM_CACHED_SORT_BY_CREATE_TIME_ALBUM_IDS = "key_cached_album_sort_by_create_time_album_ids";
        public static final String ALBUM_CLEANABLE_COUNT = "album_cleanable_count";
        public static final String ALBUM_DETAIL_SORT = "key_album_detail_sort";
        public static final String ALBUM_DETAIL_SORT_HAS_UPDATED = "key_album_detail_sort_has_updated";
        public static final String ALBUM_DETAIL_TIME_GROUP = "key_album_detail_time_group";
        public static final String ALBUM_FIRST_VISIT_OWNER_ALBUM_DETAIL = "key_first_visit_owner_album_detail";
        public static final String ALBUM_MIGRATION = "key_album_migration";
        public static final String ALBUM_NEXT_FORCE_TOP_TIME = "key_next_force_top_create_time";
        public static final String ALBUM_SHOW_SCREENSHOT = "album_show_screenshot";
        public static final String ALBUM_SORT_TYPE = "key_album_sort_type";
        public static final String ALBUM_VIEW_MODE = "album_view_mode";
        public static final String ALBUM_VIRTUAL_ALBUM_PREFIX = "key_virtual_album_prefix_%d";
        public static final String ALBUM_VISIT_PREFIX = "key_album_visit_prefix_%d";
        public static final String ASSISTANT_CARD_HEATMAP_CALCULATED = "assistant_card_heatmap_calculated";
        public static final String ASSISTANT_CARD_SYNC_DIRTY = "assistant_card_sync_dirty";
        public static final String ASSISTANT_COVER_UPDATED = "assistant_cover_updated";
        public static final String ASSISTANT_CREATIVITY_FUNCTION = "assistant_creativity_function";
        public static final String ASSISTANT_FIRST_SHOW_IMAGE_SELECTION = "assistant_first_show_image_selection";
        public static final String ASSISTANT_FIRST_TRIGGER_TIME = "assistant_first_trigger_time";
        public static final String ASSISTANT_FORCE_REFRESH_LIBRARY_INFO = "assistant_force_refresh_library_info";
        public static final String ASSISTANT_HAS_CARD_EVER = "assistant_has_card_ever";
        public static final String ASSISTANT_HAS_TRIGGER_NEW_SCENARIO = "assistant_has_trigger_new_scenario";
        public static final String ASSISTANT_IMAGE_SELECTION_FUNCTION = "assistant_image_selection_function";
        public static final String ASSISTANT_LAST_GUARANTEE_TRIGGER_TIME = "assistant_last_guarantee_trigger_time";
        public static final String ASSISTANT_LAST_IMAGE_FEATURE_TASK_TIME = "assistant_last_image_feature_task_time";
        public static final String ASSISTANT_LAST_TRIGGER_TIME = "assistant_last_trigger_time";
        public static final String ASSISTANT_STORY_FUNCTION = "assistant_story_function";
        public static final String ASSISTANT_WATERMARK_FUNCTION = "assistant_watermark_function";
        public static final String BABY_SERVER_TAG_SAVE_PREFIX = "save_server_tag_of_new_image_prefix";
        public static final String CARD_ADDED_TIME = "card_added_time";
        public static final String CARD_OPERATION_SYNC_TOKEN = "card_operation_sync_token";
        public static final String CARD_OPERATION_WATERMARK = "card_operation_watermark";
        public static final String CLOUD_COMPLETELY_FULL_LAST_POPPED = "cloud_completely_full_last_popped";
        public static final String CLOUD_COMPLETELY_FULL_TIP_COUNTS = "cloud_completely_full_tip_counts";
        public static final String CLOUD_CONTROL_LAST_REQUEST_SUCCEED_TIME = "cloud_control_last_request_succeed_time";
        public static final String CLOUD_CONTROL_LAST_REQUEST_TIME = "cloud_control_last_request_time";
        public static final String CLOUD_CONTROL_PUSH_TAG = "cloud_control_push_tag";
        public static final String CLOUD_CONTROL_SYNC_TOKEN = "cloud_control_sync_token";
        public static final String CLOUD_FULL_LAST_CHECK_TIME = "cloud_full_last_check_time";
        public static final String CLOUD_FULL_LAST_POPPED = "cloud_full_last_popped";
        public static final String CLOUD_FULL_TIP_COUNTS = "cloud_full_tip_counts";
        public static final String CLOUD_GUIDE_EVER_NOTIFY_SLIM_RESULT = "ever_notify_slim_result";
        public static final String CLOUD_GUIDE_SLIM_NOTIFICATION_CLICKED = "slim_notification_clicked";
        public static final String CLOUD_GUIDE_SLIM_NOTIFICATION_TIMES = "slim_notification_times";
        public static final String CLOUD_GUIDE_TOPBAR_AUTO_SHOW_TIMES = "cloud_guide_topbar_auto_show_times";
        public static final String CLOUD_GUIDE_TOPBAR_CLICKED = "cloud_guide_topbar_clicked";
        public static final String CLOUD_GUIDE_TOPBAR_LAST_AUTO_SHOW_TIME = "cloud_guide_topbar_last_auto_show_time";
        public static final String CLOUD_GUIDE_TOPBAR_LAST_TEXT_ID = "cloud_guide_topbar_last_text_id";
        public static final String CLOUD_PRIVACY_POLICY_AGREED = "cloud_privacy_policy_agreed";
        public static final String CLOUD_PRIVACY_POLICY_REJECTED = "cloud_privacy_policy_rejected";
        public static final String DATABASE_EVER_UPDATE_CAMERA_ALBUM_ATTRIBUTES = "ever_update_camera_album_attributes";
        public static final String DATABASE_EVER_UPGRADE_ALBUM_EDITED_COLUMNS = "ever_upgrade_album_edited_columns";
        public static final String DATABASE_EVER_UPGRADE_DB_FOR_SCREENSHOT = "ever_upgrade_db_for_screenshots";
        public static final String DATABASE_VIEW_VERSION_PREFIX = "view_version_prefix_%s";
        public static final String DELETE_FIRST_DELETE_FROM_ALBUM = "first_delete_from_album";
        public static final String DELETE_FIRST_DELETE_FROM_HOMEPAGE = "first_delete_from_homepage";
        public static final String FACE_CLOUD_STATUS = "cloud_face_status";
        public static final String FACE_CLOUD_STATUS_NEXT_CHECK_TIME = "cloud_face_status_retry_time";
        public static final String FACE_CLOUD_STATUS_SYNC_TIME = "cloud_face_status_sync_time";
        public static final String FACE_FEATURE_SWITCH_PENDING = "face_feature_switch_pending";
        public static final String FACE_FIRST_CONFIRM_RECOMMEND_FACE = "first_confirm_recommend_face";
        public static final String FACE_FIRST_SYNC_COMPLETED = "face_first_sync_completed";
        public static final String FACE_HAS_TOAST_SET_GROUP = "has_toast_how_to_set_group";
        public static final String FACE_MARK_MYSELF_RESULT = "face_mark_myself_result";
        public static final String FACE_MARK_MYSELF_TRIGGERED_COUNT = "face_mark_myself_triggered_count";
        public static final String FACE_RECOMMEND_GROUP_HIDDEN = "face_recommend_group_hidden";
        public static final String FACE_URL_FOR_QUEUING = "face_url_for_queuing";
        public static final String FACE_URL_FOR_WAITING = "face_url_for_waiting";
        public static final String FAVORITES_ALBUM_COVER_ID = "favorites_album_cover_id";
        public static final String FEATURE_EVER_DISPLAYED = "feature_ever_displayed";
        public static final String FEATURE_EVER_USED_FORMATTER = "new_feature_ever_used_%s";
        public static final String FEATURE_RED_DOT_VALID_END_TIME = "feature_red_dot_valid_end_time";
        public static final String FEATURE_RED_DOT_VALID_START_TIME = "feature_red_dot_valid_start_time";
        public static final String FILE_DOWNLOAD_CONN_TIMEOUT_FORMAT = "file_download_conn_timeout_%s";
        public static final String FIRST_TIME_ADD_TO_FAVORITES = "first_time_add_to_favorites";
        public static final String HAS_CLEAN_FILE_NAME_AND_PATH = "has_clean_file_name_and_path";
        public static final String HAS_IGNORE_VIP_TIP = "online_has_ignore_vip_tip";
        @AutoBackup
        public static final String HIDDEN_ALBUM_SHOW = "show_hidden_album";
        public static final String HOME_PAGE_DISCOVER_PHOTOS = "home_page_discover_photos";
        public static final String HOME_PAGE_IMAGE_IDS = "home_page_image_ids";
        public static final String HOME_PAGE_SHOW_ALL_PHOTOS_TIP = "home_page_show_all_photos_tip";
        public static final String HOME_PAGE_SWITCH_ALL_PHOTOS = "home_page_switch_all_photos";
        public static final String HOME_PAGE_VIEW_MODE = "home_page_view_mode";
        public static final String INCOMPATIBLE_MEDIA_AUTO_CONVERT = "incompatible_media_auto_convert";
        public static final String INCOMPATIBLE_MEDIA_SCANNED_TIME = "incompatible_media_scanned_time";
        public static final String IS_FIRST_SHOW_DRAG_TIP_VIEW = "is_first_show_drag_tip_view";
        public static final String IS_LOCAL_HAVE_TRASH_FILE = "is_local_have_trash_file";
        public static final String IS_NEED_REFRESH_HOME_PAGE_THUNMNAIL_DATA = "is_need_refresh_home_page_data1";
        public static final String IS_SET_EXEMPT_MASTER_SYNC_AUTO = "is_set_exempt_master_sync_auto";
        public static final String IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX = "is_user_manual_setter_sort_position_ai_album_index";
        public static final String IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX = "is_user_manual_setter_sort_position_other_album_index";
        public static final String IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX = "is_user_manual_setter_sort_position_trash_album_index";
        public static final String JOB_SCHEDULER_CLEANUP = "job_scheduler_cleanup";
        public static final String KEY_DOWNLOAD_MEDIA_EDITOR_APP = "key_download_media_editor_app";
        public static final String LAST_ENTER_PRIVATE_ALBUM_TIME = "last_enter_private_album_time";
        public static final String LAST_MODIFIED_TIME_ADD_TO_SECRET = "last_modified_time_add_to_secret";
        public static final String LAST_UNREDDEN_TIME = "last_unredden_time";
        @AutoBackup
        public static final String LOCAL_MODE_ONLY_SHOW_LOCAL_PHOTO = "only_show_local_photo";
        public static final String LOCATIONS_ALBUM_COVER_SERVER_IDS = "locations_album_cover_server_ids";
        public static final String LOCK_ORIENTATION_NOTICE_SHOWED = "lock_orientation_notice_showed";
        public static final String MAML_ASSETS_VERSION = "maml_assets_version";
        public static final String MAP_ALBUM_NOTICE_SHOWED = "map_album_notice_showed";
        public static final String MAP_SHOW_PRIVACY_POLICY = "map_show_privacy_policy";
        public static final String MEDIA_TYPE = "media_type_preference";
        public static final String MONTH_VIEW_HAS_SHOWN_FIRST_UPGRADE_TIP = "month_view_has_shown_first_upgrade_tip";
        public static final String MONTH_VIEW_HAS_SHOWN_TIP = "month_view_has_shown_tip";
        public static final String NEED_CLEAN_MICRO_THUMB = "need_clean_micro_thumb";
        public static final String PHOTO_EDITOR_COMPARE_TIP = "photo_editor_compare_tip";
        public static final String PHOTO_EDITOR_CROP_TIPS_TIMES = "photo_editor_crop_tips_times";
        public static final String PHOTO_EDITOR_DOODLE_COLOR_LOCATION = "photo_editor_doodle_color_location";
        public static final String PHOTO_EDITOR_HAS_TIP_DYNAMIC_SKY = "photo_editor_has_tip_dynamic_sky";
        public static final String PHOTO_FILTER_PORTRAIT_COLOR_GUIDE_COMPLETED = "photo_filter_portrait_color_guide_completed";
        public static final String PHOTO_FILTER_SKY_GUIDE_COMPLETED = "photo_filter_sky_guide_completed";
        public static final String PHOTO_PRINT_SILENT_INSTALL_TIMES = "photo_print_silent_install_times";
        public static final String PHOTO_SLIM_FIRST_USE = "photo_slim_first_use";
        public static final String PRINT_FIRST_CLICKED = "print_first_clicked";
        public static final String PROJECTION_DEVICE_KEY_PREFIX = "big_screen_device_key_prefix_";
        public static final String RECENT_ALBUM_COVER_ID = "recent_album_cover_id";
        public static final String RED_DOT_SAW_TIME = "red_dot_saw_time";
        public static final String RISK_CONTROL_DELETE = "risk_control_delete";
        public static final String SCANNER_EVER_CHANGE_CASE_FOR_ALBUM = "ever_change_case_for_album";
        public static final String SCANNER_EVER_CLEAN_EMPTY_PATH_ALBUM = "ever_clean_empty_path_album";
        public static final String SCANNER_EVER_CLEAN_INTERNAL_ITEMS = "ever_clean_internal_items";
        public static final String SCANNER_EVER_CLEAN_INVALID_TEMP_SERVER_STATUS_FILES = "ever_clean_invalid_temp_server_status_files";
        public static final String SCANNER_EVER_CLEAN_INVALID_TEMP_SERVER_STATUS_FILES_V2 = "ever_clean_invalid_temp_server_status_files_v2";
        public static final String SCANNER_EVER_CLEAN_UNSUPPORT_UPLOAD_ITEMS = "ever_clean_unsupport_upload_items";
        public static final String SCANNER_EVER_FILL_SPECIAL_TYPE_FLAGS_PREFIX = "ever_fill_special_type_flags_v%d";
        public static final String SCANNER_EVER_FORCE_SCAN_ALL_ALBUMS_FOR_FORMAT_EXPANSION = "ever_force_scan_all_albums_for_format_expansion";
        public static final String SCANNER_EVER_RECOVERY_MUST_VISIBLE_ALBUMS_FROM_RUBBISH = "ever_recovery_must_visible_albums_from_rubbish";
        public static final String SCANNER_EVER_REFILL_LOCATION_FOR_SCREENSHOTS = "ever_refill_location_for_screenshots_1";
        public static final String SCANNER_EVER_RESTORE_SECRET_ITEMS = "ever_restore_secret_items";
        public static final String SCANNER_LAST_IMAGES_GENERATION_MODIFIED = "last_images_generation_modified";
        public static final String SCANNER_LAST_LOCAL_RESTORE_TIME = "last_local_restore_time";
        public static final String SCANNER_LAST_REFRESHED_IGNORE_LIST_VERSION = "last_refreshed_ignore_list_version";
        public static final String SCANNER_LAST_VEDIOS_GENERATION_MODIFIED = "last_vedios_generation_modified";
        public static final String SCANNER_LAST_VIDEOS_SCAN_TIME = "last_videos_scan_time";
        public static final String SCREENSHOTS_RECORDERS_ALBUM_COVER_ID = "screenshots_recorders_cover_id";
        public static final String SCREEN_EDITOR_IS_SEND_AND_DELETE = "screen_editor_is_send_and_delete";
        public static final String SEARCH_CACHE_STATUS = "search_cache_status";
        public static final String SEARCH_FEEDBACK_SHOULD_SHOW_POLICY = "search_feedback_task_should_show_policy";
        public static final String SEARCH_FEEDBACK_TASK_REPORTED_TAGS = "search_feedback_task_reported_tags";
        public static final String SEARCH_USER_LAST_REQUEST_OPEN_TIME = "search_user_last_request_open_time";
        public static final String SEARCH_USER_SWITCH_STATUS = "search_user_switch_status";
        public static final String SECRET_FIRST_ADD_SECRET = "first_add_secret";
        public static final String SECRET_FIRST_ADD_SECRET_VIDEO = "first_add_secret_video";
        public static final String SECRET_FIRST_USE_PRIVACY_PASSWORD = "first_use_privacy_password";
        public static final String SECRET_TUTORIAL_REST_TIME = "secret_tutorial_rest_time";
        public static final String SERVER_CONTROL_LIST_EVER_CLEAR_OLD_FILE = "ever_clear_old_file";
        public static final String SETTINGS_DOWNLOAD_LAST_TIME = "settings_download_last_time";
        public static final String SETTINGS_SYNC_FIRST_UPLOAD_COMPLETE = "settings_sync_first_upload_complete";
        public static final String SETTINGS_SYNC_IS_DIRTY = "settings_sync_is_dirty";
        public static final String SHOW_AUTO_DOWNLOAD_DIALOG = "show_auto_download_dialog";
        @AutoBackup
        public static final String SLIDESHOW_INTERVAL = "slideshow_interval";
        @AutoBackup
        public static final String SLIDESHOW_LOOP = "slideshow_loop";
        public static final String SLIMABLE_SIZE = "slimable_size";
        public static final String SLIM_DIALOG_LAST_POPPED_UP = "slim_dialog_last_popped_up";
        public static final String SLIM_DIALOG_SHOW_COUNT = "slim_dialog_show_count";
        public static final String SLIM_LAST_SCAN_TIMESTAMP = "slim_last_scan_timestamp";
        public static final String SLIM_NOTIFICATION_LAST_SHOWED = "slim_notification_last_showed";
        public static final String SLIM_TEXT_LINK_LAST_SHOWED = "slim_text_link_last_showed";
        public static final String SLIM_TEXT_LINK_SHOULD_SHOW = "slim_text_link_should_show";
        public static final String SORT_POSITION_AI_ALBUM_INDEX = "sort_position_ai_album_index";
        public static final String SORT_POSITION_BABY_ALBUM_PREV_INDEX = "sort_position_baby_album_prev_index";
        public static final String SORT_POSITION_BABY_FIRST_INDEX = "sort_position_baby_album_first_index";
        public static final String SORT_POSITION_FAVORITES_ALBUM_INDEX = "sort_position_favorites_album_index";
        public static final String SORT_POSITION_NANO_NEXT_INDEX = "sort_position_nano_next_index";
        public static final String SORT_POSITION_NEXT_TOP_ALBUM = "sort_position_next_top_album";
        public static final String SORT_POSITION_OTHER_ALBUM_INDEX = "sort_position_other_album_index";
        public static final String SORT_POSITION_RECENT_ALBUM_INDEX = "sort_position_recent_album_index";
        public static final String SORT_POSITION_SCREENSHOTS_RECORDERS_ALBUM_INDEX = "sort_position_screenshots_recorders_album_index";
        public static final String SORT_POSITION_TRASH_ALBUM_INDEX = "sort_position_trash_album_index";
        public static final String SORT_POSITION_VIDEO_ALBUM_INDEX = "sort_position_video_album_index";
        public static final String SYNC_ACTIVE_PULL_TIMES = "active_pull_times";
        public static final String SYNC_AUTO_DOWNLOAD = "auto_download";
        public static final String SYNC_AUTO_DOWNLOAD_TIME = "auto_download_time";
        @AutoBackup
        public static final String SYNC_BACKUP_ONLY_IN_WIFI = "backup_only_in_wifi";
        public static final String SYNC_COMPLETELY_FINISH = "sync_completely_finish";
        public static final String SYNC_DEVICE_STORAGE_LOW = "device_storage_low";
        public static final String SYNC_DEVICE_STORAGE_TOO_LOW = "device_storage_too_low";
        public static final String SYNC_DOWNLOAD_TYPE = "download_type";
        public static final String SYNC_EVER_AUTO_DOWNLOADED = "ever_auto_download";
        public static final String SYNC_EVER_REFILL_LOCAL_GROUP_ID = "ever_refill_local_group_id";
        public static final String SYNC_EVER_RESET_CLOUD_SETTING = "ever_reset_cloud_setting";
        public static final String SYNC_EVER_RESET_SLIM_SWITCH = "ever_reset_slim_switch";
        public static final String SYNC_EVER_SYNCED_SYSTEM_ALBUM = "ever_synced_system_album";
        public static final String SYNC_FIRST_SYNC_FAIL_COUNT = "first_sync_fail_count";
        public static final String SYNC_FIRST_SYNC_START_TIME = "first_sync_start_time";
        public static final String SYNC_IS_PLUGGED = "is_plugged";
        public static final String SYNC_LAST_SYNC_TIMESTAMP = "last_sync_timestamp";
        public static final String SYNC_POWER_CAN_SYNC = "power_can_sync";
        public static final String SYNC_REQUEST_START_PREFIX = "sync_request_start_%s";
        @AutoBackup
        public static final String SYNC_SLIM_AFTER_BACKUPED = "slim_after_backuped_v2";
        public static final String SYNC_SYNC_TAG_OF_RESET_CLOUD_SETTING = "sync_tag_of_reset_cloud_setting";
        public static final String TAGS_ALBUM_COVER_SERVER_IDS = "tags_album_cover_server_ids";
        public static final String TOP_BAR_CANNOT_SYNC_PREFIX = "top_bar_cannot_sync_";
        public static final String TOP_BAR_LAST_SYNC_STATUS = "top_bar_last_sync_status";
        public static final String UNIQUE_ID = "unique_id";
        public static final String UPDATE_FEATURE_STATED = "update_feature_stated";
        public static final String UPDATE_IS_CONFIRM_NEW_VERSION = "update_confirm_find_new_version";
        public static final String UPDATE_IS_DELAY = "update_is_delay";
        public static final String UPDATE_IS_FIND_NEW_VERSION = "update_is_find_new_version";
        public static final String UPDATE_IS_FORCE_UPDATE_FINISH = "update_is_force_update_finish";
        public static final String UPDATE_IS_IGNORE = "update_is_ignore";
        public static final String UPDATE_IS_NEED_HINT = "update_is_need_hint";
        public static final String UPDATE_LAST_DELAY_DATE = "update_last_delay_date";
        public static final String UPDATE_LAST_REQUEST_DATE = "update_last_request_date";
        public static final String UPDATE_LAST_VERSION_CODE = "update_last_version_code";
        public static final String UPDATE_NEWEST_VERSION_CODE = "update_newest_version_code";
        public static final String UPDATE_SLIM_PROFILE_STATUS = "update_slim_profile_status";
        public static final String UPGRADE_DEPRECATED_GLIDE_CACHE_DELETED = "deprecated_glide_cache_deleted";
        public static final String UPGRADE_LEGACY_THUMB_CACHE_DELETED = "legacy_thumb_cache_deleted";
        public static final String UPGRADE_LOCAL_THUMBNAIL_CACHE_FILE_DELETED = "local_thumbnail_cache_file_deleted";
        public static final String UPGRADE_OLD_CACHE_CLEANED = "old_cache_cleaned";
        public static final String UPGRADE_OLD_PREFERENCES_TRANSFERED = "old_preferences_transfered";
        public static final String VIDEO_ALBUM_COVER_ID = "video_album_cover_id";
        public static final String VIP_INFO = "vip_info";
        public static final String VIP_TIP_SHOW_TIME = "online_vip_tip_show_time";
        public static final String WHITE_TO_VIP_TIME = "white_to_vip_time";
    }

    public static List<String> getAutoBackupPrefKeys() {
        Field[] declaredFields = PrefKeys.class.getDeclaredFields();
        if (declaredFields != null) {
            LinkedList linkedList = new LinkedList();
            try {
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(AutoBackup.class)) {
                        linkedList.add(field.get(null).toString());
                    }
                }
            } catch (IllegalAccessException e) {
                DefaultLogger.e("GalleryPreferences", e);
            } catch (Exception e2) {
                DefaultLogger.e("GalleryPreferences", e2);
            }
            return linkedList;
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class Album {
        public static boolean isFixedAlbum(long j) {
            return j == 2147483639 || j == 2147483641 || j == 2147483638;
        }

        public static boolean isForceTopAlbumByTopTime(long j) {
            return j > 0;
        }

        public static long getVirtualAlbumSortBy(long j, long j2) {
            return AlbumConfigSharedPreferences.getInstance().getLong(String.format(Locale.US, PrefKeys.ALBUM_VIRTUAL_ALBUM_PREFIX, Long.valueOf(j)), j2);
        }

        public static int getAlbumViewMode() {
            return PreferenceHelper.getInt(PrefKeys.ALBUM_VIEW_MODE, 1);
        }

        public static void setAlbumViewMode(int i) {
            PreferenceHelper.putInt(PrefKeys.ALBUM_VIEW_MODE, i);
        }

        public static String getFixedAlbumSortInfo(long j) {
            if (j == 2147483639) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_AI_ALBUM_INDEX, String.valueOf(994L));
            }
            if (j == 2147483641) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX, String.valueOf(2147484647L));
            }
            return j == 2147483638 ? AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX, String.valueOf(2147483747L)) : "";
        }

        public static void setFixedAlbumSortInfo(long j, String str) {
            if (j == 2147483639) {
                AlbumConfigSharedPreferences.getInstance().edit().putString(PrefKeys.SORT_POSITION_AI_ALBUM_INDEX, str).putBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX, true).apply();
            } else if (j == 2147483641) {
                AlbumConfigSharedPreferences.getInstance().edit().putString(PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX, str).putBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX, true).apply();
            } else if (j != 2147483638) {
            } else {
                AlbumConfigSharedPreferences.getInstance().edit().putString(PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX, str).putBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX, true).apply();
            }
        }

        public static boolean isFixedAlbumAlreadySetter(long j) {
            if (j == 2147483639) {
                return AlbumConfigSharedPreferences.getInstance().getBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX, false);
            }
            if (j == 2147483641) {
                return AlbumConfigSharedPreferences.getInstance().getBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX, false);
            }
            if (j != 2147483638) {
                return false;
            }
            return AlbumConfigSharedPreferences.getInstance().getBoolean(PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX, false);
        }

        public static void setAlbumCleanableCount(long j) {
            PreferenceHelper.putLong(PrefKeys.ALBUM_CLEANABLE_COUNT, j);
        }

        public static long getAlbumCleanableCount() {
            return PreferenceHelper.getLong(PrefKeys.ALBUM_CLEANABLE_COUNT, -1L);
        }

        public static String getVirtualAlbumSortPosition(long j) {
            if (com.miui.gallery.model.dto.Album.isAllPhotosAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_RECENT_ALBUM_INDEX, String.valueOf(1001L));
            }
            if (com.miui.gallery.model.dto.Album.isVideoAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_VIDEO_ALBUM_INDEX, String.valueOf(998L));
            }
            if (com.miui.gallery.model.dto.Album.isFavoritesAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_FAVORITES_ALBUM_INDEX, String.valueOf(1000L));
            }
            if (com.miui.gallery.model.dto.Album.isScreenshotsRecorders(j)) {
                return AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_SCREENSHOTS_RECORDERS_ALBUM_INDEX, String.valueOf(996L));
            }
            throw new IllegalArgumentException("cant support getVirtualAlbumSortPosition by id:" + j);
        }

        public static boolean setVirtualAlbumSortPosition(long j, String str) {
            if (com.miui.gallery.model.dto.Album.isAllPhotosAlbum(j)) {
                AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_RECENT_ALBUM_INDEX, str);
                return true;
            } else if (com.miui.gallery.model.dto.Album.isVideoAlbum(j)) {
                AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_VIDEO_ALBUM_INDEX, str);
                return true;
            } else if (com.miui.gallery.model.dto.Album.isFavoritesAlbum(j)) {
                AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_FAVORITES_ALBUM_INDEX, str);
                return true;
            } else if (!com.miui.gallery.model.dto.Album.isScreenshotsRecorders(j)) {
                return false;
            } else {
                AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_SCREENSHOTS_RECORDERS_ALBUM_INDEX, str);
                return true;
            }
        }

        public static long getVirtualAlbumCoverId(long j) {
            if (com.miui.gallery.model.dto.Album.isAllPhotosAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getLong(PrefKeys.RECENT_ALBUM_COVER_ID, -1L);
            }
            if (com.miui.gallery.model.dto.Album.isVideoAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getLong(PrefKeys.VIDEO_ALBUM_COVER_ID, -1L);
            }
            if (com.miui.gallery.model.dto.Album.isFavoritesAlbum(j)) {
                return AlbumConfigSharedPreferences.getInstance().getLong(PrefKeys.FAVORITES_ALBUM_COVER_ID, -1L);
            }
            if (!com.miui.gallery.model.dto.Album.isScreenshotsRecorders(j)) {
                return -1L;
            }
            return AlbumConfigSharedPreferences.getInstance().getLong(PrefKeys.SCREENSHOTS_RECORDERS_ALBUM_COVER_ID, -1L);
        }

        public static Set<String> getCachedSortByCreateTimeAlbumIds() {
            return PreferenceHelper.getStringSet(PrefKeys.ALBUM_CACHED_SORT_BY_CREATE_TIME_ALBUM_IDS, null);
        }

        public static void setCachedSortByCreateTimeAlbumIds(Set<String> set) {
            PreferenceHelper.putStringSet(PrefKeys.ALBUM_CACHED_SORT_BY_CREATE_TIME_ALBUM_IDS, set);
        }

        public static void setFirstVisitOwnerAlbumDetail(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ALBUM_FIRST_VISIT_OWNER_ALBUM_DETAIL, z);
        }

        public static boolean isFirstVisitOwnerAlbumDetail() {
            return PreferenceHelper.getBoolean(PrefKeys.ALBUM_FIRST_VISIT_OWNER_ALBUM_DETAIL, true);
        }

        public static void setAlbumDetailTimeGroup(long j, boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ALBUM_DETAIL_TIME_GROUP + j, z);
        }

        public static boolean getAlbumDetailTimeGroup(long j, boolean z) {
            return PreferenceHelper.getBoolean(PrefKeys.ALBUM_DETAIL_TIME_GROUP + j, z);
        }

        public static void setAlbumDetailSort(long j, int i) {
            PreferenceHelper.putInt(PrefKeys.ALBUM_DETAIL_SORT + j, i);
        }

        public static int getAlbumDetailSort(long j, int i) {
            return PreferenceHelper.getInt(PrefKeys.ALBUM_DETAIL_SORT + j, i);
        }

        public static void removeUserCreateAlbumSort(List<Long> list) {
            SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
            for (Long l : list) {
                long longValue = l.longValue();
                edit.remove(PrefKeys.ALBUM_DETAIL_SORT + longValue);
            }
            edit.apply();
        }

        public static void setHasUpdatedAlbumDetailSort(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ALBUM_DETAIL_SORT_HAS_UPDATED, z);
        }

        public static boolean hasUpdatedAlbumDetailSort() {
            return PreferenceHelper.getBoolean(PrefKeys.ALBUM_DETAIL_SORT_HAS_UPDATED, false);
        }

        public static long getAlbumMigrationState() {
            return PreferenceHelper.getLong(PrefKeys.ALBUM_MIGRATION, 0L);
        }

        public static void applyAlbumMigrationState(boolean z, long j) {
            long albumMigrationState = getAlbumMigrationState();
            PreferenceHelper.putLong(PrefKeys.ALBUM_MIGRATION, z ? albumMigrationState | j : (~j) & albumMigrationState);
        }

        public static void setNextHeadGroupFirstAlbumSort(double d) {
            AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, Double.toString(d - 1.0d));
        }

        public static double getNextHeadGroupFirstAlbumSort() {
            double parseDouble = Double.parseDouble(AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, String.valueOf(2147483646)));
            if (parseDouble < SearchStatUtils.POW) {
                parseDouble = 2.147483646E9d;
            }
            AlbumConfigSharedPreferences.getInstance().putString(PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, Double.toString(parseDouble - 1.0d));
            return parseDouble;
        }

        public static void setIsShowScreenshot(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ALBUM_SHOW_SCREENSHOT, z);
        }

        public static boolean isShowScreenshot() {
            return PreferenceHelper.getBoolean(PrefKeys.ALBUM_SHOW_SCREENSHOT, true);
        }

        public static void setAddToAlbumPageLastSelectedAlbumId(long j) {
            String[] split;
            try {
                String string = AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.ADD_TO_ALBUM_PAGE_LAST_SELECTED_ALBUM_LIST, null);
                LinkedList linkedList = new LinkedList();
                if (string != null && (split = string.split(",")) != null && split.length > 0) {
                    if (split[0].equals(String.valueOf(j))) {
                        return;
                    }
                    for (String str : split) {
                        linkedList.add(str);
                    }
                }
                linkedList.addFirst(String.valueOf(j));
                if (linkedList.size() > 2) {
                    linkedList.removeLast();
                }
                AlbumConfigSharedPreferences.getInstance().getPreferences().edit().putString(PrefKeys.ADD_TO_ALBUM_PAGE_LAST_SELECTED_ALBUM_LIST, (String) linkedList.stream().distinct().collect(Collectors.joining(","))).apply();
            } catch (Exception e) {
                DefaultLogger.e("GalleryPreferences", "setAddToAlbumPageLastSelectedAlbumId error:%s", e);
            }
        }

        public static List<Long> getAddToAlbumPageLastSelectedAlbum() {
            String[] split;
            String string = AlbumConfigSharedPreferences.getInstance().getString(PrefKeys.ADD_TO_ALBUM_PAGE_LAST_SELECTED_ALBUM_LIST, null);
            if (string == null || (split = string.split(",")) == null) {
                return null;
            }
            LinkedList linkedList = new LinkedList();
            for (String str : split) {
                linkedList.add(Long.valueOf(Long.parseLong(str)));
            }
            return linkedList;
        }
    }

    /* loaded from: classes2.dex */
    public static class CTA extends BaseGalleryPreferences.CTA {
        public static void onCreateOrDestroyHomePage() {
            BaseGalleryPreferences.CTA.setCanConnectToNetworkTemp(false);
            BaseGalleryPreferences.CTA.setToAllowUseOnOfflineGlobal(false);
        }
    }

    /* loaded from: classes2.dex */
    public static class BabyLock {
        public static String createBabyLockWallpaperAlbumStateKey(String str) {
            return "baby_lock_wallpaper_album_state_prefix/" + str;
        }

        public static boolean isBabyLockWallpaperAlbumStateKey(String str) {
            return str != null && str.startsWith("baby_lock_wallpaper_album_state_prefix/");
        }

        public static String createPathSuffix(long j, boolean z) {
            if (z) {
                return "share/" + j;
            }
            return "owner/" + j;
        }

        public static void setBabyAlbumForLockWallpaper(String str, boolean z) {
            PreferenceHelper.putBoolean(createBabyLockWallpaperAlbumStateKey(str), z);
        }

        public static boolean isBabyAlbumForLockWallpaper(long j, boolean z) {
            return PreferenceHelper.getBoolean(createBabyLockWallpaperAlbumStateKey(createPathSuffix(j, z)), false);
        }

        public static boolean isBabyAlbumForLockWallpaper(String str) {
            return PreferenceHelper.getBoolean(createBabyLockWallpaperAlbumStateKey(str), false);
        }

        public static void cleanBabyLockWallpaperAlbumState() {
            SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
            for (String str : PreferenceHelper.getPreferences().getAll().keySet()) {
                if (isBabyLockWallpaperAlbumStateKey(str)) {
                    edit.remove(str);
                }
            }
            edit.apply();
        }
    }

    /* loaded from: classes2.dex */
    public static class Sync {
        public static final LazyValue<Void, String> DEFAULT_DOWNLOAD_TYPE = new LazyValue<Void, String>() { // from class: com.miui.gallery.preference.GalleryPreferences.Sync.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public String mo1272onInit(Void r1) {
                return CloudControlStrategyHelper.getSyncStrategy().getAutoDownloadType() == 0 ? "thumbnail" : MiStat.Param.ORIGIN;
            }
        };

        public static void setPowerCanSync(boolean z) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.SYNC_POWER_CAN_SYNC, z);
        }

        public static boolean getPowerCanSync() {
            return MemoryPreferenceHelper.getBoolean(PrefKeys.SYNC_POWER_CAN_SYNC, false);
        }

        public static void setIsPlugged(boolean z) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.SYNC_IS_PLUGGED, z);
        }

        public static boolean getIsPlugged() {
            return MemoryPreferenceHelper.getBoolean(PrefKeys.SYNC_IS_PLUGGED, false);
        }

        public static void setSyncCompletelyFinish(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SYNC_COMPLETELY_FINISH, z);
        }

        public static boolean getSyncCompletelyFinish() {
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_COMPLETELY_FINISH, false);
        }

        public static void setBackupOnlyInWifi(boolean z) {
            if (!FeatureUtil.isSupportBackupOnlyWifi()) {
                return;
            }
            PreferenceHelper.putBoolean(PrefKeys.SYNC_BACKUP_ONLY_IN_WIFI, z);
        }

        public static boolean getBackupOnlyInWifi() {
            if (!FeatureUtil.isSupportBackupOnlyWifi()) {
                return true;
            }
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_BACKUP_ONLY_IN_WIFI, true);
        }

        public static boolean getEverRefillLocalGroupId() {
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_EVER_REFILL_LOCAL_GROUP_ID, false);
        }

        public static void setEverRefillLocalGroupId() {
            PreferenceHelper.putBoolean(PrefKeys.SYNC_EVER_REFILL_LOCAL_GROUP_ID, true);
        }

        public static boolean getEverSyncedSystemAlbum() {
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_EVER_SYNCED_SYSTEM_ALBUM, false);
        }

        public static void setEverSyncedSystemAlbum() {
            PreferenceHelper.putBoolean(PrefKeys.SYNC_EVER_SYNCED_SYSTEM_ALBUM, true);
        }

        public static boolean isEverAutoDownloaded() {
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_EVER_AUTO_DOWNLOADED, false);
        }

        public static void setEverAutoDownloaded() {
            PreferenceHelper.putBoolean(PrefKeys.SYNC_EVER_AUTO_DOWNLOADED, true);
        }

        public static long getAutoDownloadTime() {
            return PreferenceHelper.getLong(PrefKeys.SYNC_AUTO_DOWNLOAD_TIME, -1L);
        }

        public static void setAutoDownloadTime(long j) {
            PreferenceHelper.putLong(PrefKeys.SYNC_AUTO_DOWNLOAD_TIME, j);
        }

        public static boolean isAutoDownload() {
            return AppStartupPres.isAutoDownload();
        }

        public static void setAutoDownload(boolean z) {
            AppStartupPres.setAutoDownload(z);
        }

        public static DownloadType getDownloadType() {
            String string = PreferenceHelper.getString(PrefKeys.SYNC_DOWNLOAD_TYPE, DEFAULT_DOWNLOAD_TYPE.get(null));
            if ("thumbnail".equals(string)) {
                return DownloadType.THUMBNAIL;
            }
            if (!MiStat.Param.ORIGIN.equals(string)) {
                return null;
            }
            return DownloadType.ORIGIN;
        }

        public static void setDownloadType(DownloadType downloadType) {
            DownloadType downloadType2 = getDownloadType();
            if (downloadType == DownloadType.THUMBNAIL) {
                PreferenceHelper.putString(PrefKeys.SYNC_DOWNLOAD_TYPE, "thumbnail");
            } else if (downloadType == DownloadType.ORIGIN) {
                PreferenceHelper.putString(PrefKeys.SYNC_DOWNLOAD_TYPE, MiStat.Param.ORIGIN);
            }
            if (downloadType2 != downloadType) {
                PreferenceHelper.removeKey(PrefKeys.SYNC_AUTO_DOWNLOAD_TIME);
                PreferenceHelper.removeKey(PrefKeys.SYNC_EVER_AUTO_DOWNLOADED);
            }
        }

        public static void setNeedShowAutoDownloadDialog(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SHOW_AUTO_DOWNLOAD_DIALOG, z);
        }

        public static boolean isNeedShowAutoDownloadDialog() {
            return PreferenceHelper.getBoolean(PrefKeys.SHOW_AUTO_DOWNLOAD_DIALOG, false);
        }

        public static void setNeedCleanMicroThumb(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.NEED_CLEAN_MICRO_THUMB, z);
        }

        public static boolean isNeedCleanMicroThumb() {
            return PreferenceHelper.getBoolean(PrefKeys.NEED_CLEAN_MICRO_THUMB, false);
        }

        public static boolean isDeviceStorageLow() {
            return MemoryPreferenceHelper.getBoolean(PrefKeys.SYNC_DEVICE_STORAGE_LOW, false);
        }

        public static void setDeviceStorageLow(boolean z) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.SYNC_DEVICE_STORAGE_LOW, z);
        }

        public static boolean isDeviceStorageTooLow() {
            return MemoryPreferenceHelper.getBoolean(PrefKeys.SYNC_DEVICE_STORAGE_TOO_LOW, false);
        }

        public static void setDeviceStorageTooLow(boolean z) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.SYNC_DEVICE_STORAGE_TOO_LOW, z);
        }

        public static long getSlimLastScanTimestamp() {
            return PreferenceHelper.getLong(PrefKeys.SLIM_LAST_SCAN_TIMESTAMP, 0L);
        }

        public static void setSlimLastScanTimestamp(long j) {
            PreferenceHelper.putLong(PrefKeys.SLIM_LAST_SCAN_TIMESTAMP, j);
        }

        public static long getSlimDialogLastPoppedUpTimestamp() {
            return PreferenceHelper.getLong(PrefKeys.SLIM_DIALOG_LAST_POPPED_UP, 0L);
        }

        public static void setSlimDialogPoppedUpTimestamp(long j) {
            PreferenceHelper.putLong(PrefKeys.SLIM_DIALOG_LAST_POPPED_UP, j);
        }

        public static long getSlimTextLinkLastShowedTimestamp() {
            return PreferenceHelper.getLong(PrefKeys.SLIM_TEXT_LINK_LAST_SHOWED, 0L);
        }

        public static void setSlimTextLinkShowedTimestamp(long j) {
            PreferenceHelper.putLong(PrefKeys.SLIM_TEXT_LINK_LAST_SHOWED, j);
        }

        public static int getSlimDialogShowCount() {
            return PreferenceHelper.getInt(PrefKeys.SLIM_DIALOG_SHOW_COUNT, 0);
        }

        public static void setSlimDialogShowCount(int i) {
            PreferenceHelper.putInt(PrefKeys.SLIM_DIALOG_SHOW_COUNT, i);
        }

        public static Boolean getSlimTextLinkShouldShow() {
            return Boolean.valueOf(MemoryPreferenceHelper.getBoolean(PrefKeys.SLIM_TEXT_LINK_SHOULD_SHOW, false));
        }

        public static void setSlimTextLinkShouldShow(Boolean bool) {
            MemoryPreferenceHelper.putBoolean(PrefKeys.SLIM_TEXT_LINK_SHOULD_SHOW, bool.booleanValue());
        }

        public static void setSlimableSize(long j) {
            PreferenceHelper.putLong(PrefKeys.SLIMABLE_SIZE, j);
        }

        public static long getSlimableSize() {
            return PreferenceHelper.getLong(PrefKeys.SLIMABLE_SIZE, 0L);
        }

        public static long getSlimNotificationShowedTimestamp() {
            return PreferenceHelper.getLong(PrefKeys.SLIM_NOTIFICATION_LAST_SHOWED, 0L);
        }

        public static long getLastSyncTimestamp() {
            return MemoryPreferenceHelper.getLong(PrefKeys.SYNC_LAST_SYNC_TIMESTAMP, 0L);
        }

        public static void setLastSyncTimestamp(long j) {
            MemoryPreferenceHelper.putLong(PrefKeys.SYNC_LAST_SYNC_TIMESTAMP, j);
        }

        public static int getActivePullTimes() {
            return PreferenceHelper.getInt(PrefKeys.SYNC_ACTIVE_PULL_TIMES, 0);
        }

        public static void increaseActivePullTimes() {
            PreferenceHelper.putInt(PrefKeys.SYNC_ACTIVE_PULL_TIMES, getActivePullTimes() + 1);
        }

        public static void clearActivePullTimes() {
            PreferenceHelper.removeKey(PrefKeys.SYNC_ACTIVE_PULL_TIMES);
        }

        public static long getFirstSyncStartTime() {
            return PreferenceHelper.getLong(PrefKeys.SYNC_FIRST_SYNC_START_TIME, 0L);
        }

        public static void setFirstSyncStartTime(long j) {
            if (getFirstSyncStartTime() > 0 || j <= 0) {
                return;
            }
            PreferenceHelper.putLong(PrefKeys.SYNC_FIRST_SYNC_START_TIME, j);
        }

        public static int getFirstSyncFailCount() {
            return PreferenceHelper.getInt(PrefKeys.SYNC_FIRST_SYNC_FAIL_COUNT, 0);
        }

        public static void increaseFirstSyncFailCount() {
            PreferenceHelper.putInt(PrefKeys.SYNC_FIRST_SYNC_FAIL_COUNT, getFirstSyncFailCount() + 1);
        }

        public static void clearFirstSyncFailCount() {
            PreferenceHelper.removeKey(PrefKeys.SYNC_FIRST_SYNC_FAIL_COUNT);
        }

        public static String generateRequestKey(SyncType syncType) {
            return String.format(Locale.US, PrefKeys.SYNC_REQUEST_START_PREFIX, syncType.name());
        }

        public static void markRequestStartTimeIfNone(SyncType syncType) {
            String generateRequestKey = generateRequestKey(syncType);
            if (!PreferenceHelper.contains(generateRequestKey)) {
                PreferenceHelper.putLong(generateRequestKey, System.currentTimeMillis());
            }
        }

        public static long getRequestStartTime(SyncType syncType) {
            return PreferenceHelper.getLong(generateRequestKey(syncType), -1L);
        }

        public static void clearRequestStartTime() {
            for (SyncType syncType : SyncType.values()) {
                PreferenceHelper.removeKey(generateRequestKey(syncType));
            }
        }

        public static boolean isSetExemptMasterSyncAuto() {
            return AppStartupPres.isSetExemptMasterSyncAuto();
        }

        public static void setExemptMasterSyncAuto(boolean z) {
            AppStartupPres.setExemptMasterSyncAuto(z);
        }

        public static boolean hasResetCloudSetting() {
            return PreferenceHelper.getBoolean(PrefKeys.SYNC_EVER_RESET_CLOUD_SETTING, false);
        }

        public static void setHasResetCloudSetting() {
            PreferenceHelper.putBoolean(PrefKeys.SYNC_EVER_RESET_CLOUD_SETTING, true);
        }

        public static long getLastSyncTagOfResetCloudSetting() {
            return PreferenceHelper.getLong(PrefKeys.SYNC_SYNC_TAG_OF_RESET_CLOUD_SETTING, 1L);
        }

        public static void setLastSyncTagOfResetCloudSetting(long j) {
            PreferenceHelper.putLong(PrefKeys.SYNC_SYNC_TAG_OF_RESET_CLOUD_SETTING, j);
        }

        public static void setUpdateSlimProfileStatus(int i) {
            PreferenceHelper.putInt(PrefKeys.UPDATE_SLIM_PROFILE_STATUS, i);
        }

        public static int getUpdateSlimProfileStatus() {
            return PreferenceHelper.getInt(PrefKeys.UPDATE_SLIM_PROFILE_STATUS, 0);
        }

        public static void remove() {
            setHasResetCloudSetting();
            PreferenceHelper.getPreferences().edit().remove(PrefKeys.SYNC_BACKUP_ONLY_IN_WIFI).remove(PrefKeys.SYNC_COMPLETELY_FINISH).remove(PrefKeys.SYNC_SLIM_AFTER_BACKUPED).remove(PrefKeys.SYNC_EVER_SYNCED_SYSTEM_ALBUM).remove(PrefKeys.SYNC_EVER_AUTO_DOWNLOADED).remove(PrefKeys.SYNC_AUTO_DOWNLOAD_TIME).remove(PrefKeys.SYNC_DOWNLOAD_TYPE).remove(PrefKeys.SYNC_AUTO_DOWNLOAD).remove(PrefKeys.SHOW_AUTO_DOWNLOAD_DIALOG).remove(PrefKeys.SYNC_ACTIVE_PULL_TIMES).remove(PrefKeys.SYNC_FIRST_SYNC_START_TIME).remove(PrefKeys.SYNC_FIRST_SYNC_FAIL_COUNT).remove(PrefKeys.UPDATE_SLIM_PROFILE_STATUS).apply();
            AppStartupPres.getSharedPreferences().edit().remove(PrefKeys.SYNC_AUTO_DOWNLOAD).apply();
        }
    }

    /* loaded from: classes2.dex */
    public static final class CloudGuide {
        public static void remove() {
            PreferenceHelper.getPreferences().edit().remove(PrefKeys.CLOUD_GUIDE_SLIM_NOTIFICATION_TIMES).remove(PrefKeys.CLOUD_GUIDE_SLIM_NOTIFICATION_CLICKED).remove(PrefKeys.CLOUD_GUIDE_TOPBAR_LAST_AUTO_SHOW_TIME).remove(PrefKeys.CLOUD_GUIDE_TOPBAR_AUTO_SHOW_TIMES).remove(PrefKeys.CLOUD_GUIDE_TOPBAR_CLICKED).remove(PrefKeys.CLOUD_GUIDE_TOPBAR_LAST_TEXT_ID).remove(PrefKeys.CLOUD_GUIDE_EVER_NOTIFY_SLIM_RESULT).apply();
        }

        public static void setCloudGuideTopbarClicked() {
            PreferenceHelper.putBoolean(PrefKeys.CLOUD_GUIDE_TOPBAR_CLICKED, true);
        }
    }

    public static void sRemoveCloudSettings() {
        Face.remove();
        Sync.remove();
        CloudGuide.remove();
        TopBar.remove();
        Search.remove();
        CloudControl.remove();
        SettingsSync.remove();
        LocationManager.remove();
        SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
        for (String str : PreferenceHelper.getPreferences().getAll().keySet()) {
            if (BabyLock.isBabyLockWallpaperAlbumStateKey(str) || Baby.isMinServerTagOfNewPhotoKey(str)) {
                edit.remove(str);
            }
        }
        edit.apply();
        MemoryPreferenceHelper.clear();
    }

    /* loaded from: classes2.dex */
    public static class Baby {
        public static String createServerIdKeyForSaveServerTagOfNewPhoto(long j) {
            return PrefKeys.BABY_SERVER_TAG_SAVE_PREFIX + j;
        }

        public static void saveMinServerTagOfNewPhoto(long j, Long l) {
            PreferenceHelper.putLong(createServerIdKeyForSaveServerTagOfNewPhoto(j), l.longValue());
        }

        public static Long getMinServerTagOfNewPhoto(long j) {
            return Long.valueOf(PreferenceHelper.getLong(createServerIdKeyForSaveServerTagOfNewPhoto(j), 0L));
        }

        public static boolean isMinServerTagOfNewPhotoKey(String str) {
            return str != null && str.startsWith(PrefKeys.BABY_SERVER_TAG_SAVE_PREFIX);
        }

        public static boolean getHasShowAutoUpdateTipWithoutSelectingAll(String str) {
            return PreferenceHelper.getBoolean(str, false);
        }

        public static void setHasShowAutoUpdateTipWithoutSelectingAll(String str) {
            PreferenceHelper.putBoolean(str, true);
        }

        public static long getLastClickBabyPhotosRecommandationTime(String str) {
            return PreferenceHelper.getLong(str, 0L);
        }

        public static void setLastClickBabyPhotosRecommandationTime(String str) {
            PreferenceHelper.putLong(str, System.currentTimeMillis());
        }

        public static long getLastClickPeopleRecommandationTime(String str) {
            return PreferenceHelper.getLong(str, 0L);
        }

        public static void setLastClickPeopleRecommandationTime(String str) {
            PreferenceHelper.putLong(str, System.currentTimeMillis());
        }

        public static String createBabyShortcutKey(String str) {
            return "baby_shortcut_prefix" + str;
        }

        public static void recordBabyAlbumHasShortcut(String str) {
            PreferenceHelper.putString(createBabyShortcutKey(str), str);
        }

        public static boolean isBabyAlbumHasShortcutKey(String str) {
            return str != null && str.startsWith("baby_shortcut_prefix");
        }

        public static void removeBabyAlbumShortcut(String str) {
            PreferenceHelper.removeKey(createBabyShortcutKey(str));
        }

        public static ArrayList<String> getBabyAlbumsHasShortcut() {
            ArrayList<String> arrayList = new ArrayList<>();
            for (String str : PreferenceHelper.getPreferences().getAll().keySet()) {
                if (isBabyAlbumHasShortcutKey(str)) {
                    arrayList.add(PreferenceHelper.getString(str, ""));
                }
            }
            return arrayList;
        }
    }

    /* loaded from: classes2.dex */
    public static class Face {
        public static boolean isFaceSwitchSet() {
            return PreferenceHelper.contains(PrefKeys.FACE_CLOUD_STATUS);
        }

        public static boolean getFaceSwitchEnabled() {
            int i;
            return !isFaceSwitchSet() || (i = PreferenceHelper.getInt(PrefKeys.FACE_CLOUD_STATUS, 0)) == 2 || i == 4;
        }

        public static void setFaceSwitchStatus(boolean z) {
            PreferenceHelper.putInt(PrefKeys.FACE_CLOUD_STATUS, z ? 2 : 0);
        }

        public static void setFaceRecommendGroupHidden(String str, boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.FACE_RECOMMEND_GROUP_HIDDEN + str, z);
        }

        public static boolean isFaceRecommendGroupHidden(String str) {
            return PreferenceHelper.getBoolean(PrefKeys.FACE_RECOMMEND_GROUP_HIDDEN + str, false);
        }

        public static void setShowRecommendConfirmDialog(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.FACE_FIRST_CONFIRM_RECOMMEND_FACE, z);
        }

        public static boolean isShowRecommendConfirmDialog() {
            return PreferenceHelper.getBoolean(PrefKeys.FACE_FIRST_CONFIRM_RECOMMEND_FACE, true);
        }

        public static int getMarkMyselfTriggeredCount() {
            return PreferenceHelper.getInt(PrefKeys.FACE_MARK_MYSELF_TRIGGERED_COUNT, 0);
        }

        public static void onMarkMyselfTriggered() {
            PreferenceHelper.putInt(PrefKeys.FACE_MARK_MYSELF_TRIGGERED_COUNT, getMarkMyselfTriggeredCount() + 1);
        }

        public static void setMarkMyselfResult(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.FACE_MARK_MYSELF_RESULT, z);
        }

        public static boolean isFirstSyncCompleted() {
            return PreferenceHelper.getBoolean(PrefKeys.FACE_FIRST_SYNC_COMPLETED, false);
        }

        public static void setFirstSyncCompleted() {
            PreferenceHelper.putBoolean(PrefKeys.FACE_FIRST_SYNC_COMPLETED, true);
        }

        public static void remove() {
            PreferenceHelper.getPreferences().edit().remove(PrefKeys.FACE_CLOUD_STATUS).remove(PrefKeys.FACE_CLOUD_STATUS_NEXT_CHECK_TIME).remove(PrefKeys.FACE_FEATURE_SWITCH_PENDING).remove(PrefKeys.FACE_URL_FOR_QUEUING).remove(PrefKeys.FACE_URL_FOR_WAITING).remove(PrefKeys.FACE_CLOUD_STATUS_SYNC_TIME).remove(PrefKeys.FACE_MARK_MYSELF_TRIGGERED_COUNT).remove(PrefKeys.FACE_MARK_MYSELF_RESULT).remove(PrefKeys.FACE_FIRST_SYNC_COMPLETED).apply();
        }
    }

    /* loaded from: classes2.dex */
    public static class Search {
        public static boolean isUserSearchSwitchOpen(boolean z) {
            return AppStartupPres.isUserSearchSwitchOpen(z);
        }

        public static void setIsUserSearchSwitchOpen(boolean z) {
            AppStartupPres.setIsUserSearchSwitchOpen(z);
        }

        public static boolean isUserSearchSwitchSet() {
            return AppStartupPres.isUserSearchSwitchSet();
        }

        public static long getUserLastRequestOpenTime() {
            return PreferenceHelper.getLong(PrefKeys.SEARCH_USER_LAST_REQUEST_OPEN_TIME, -1L);
        }

        public static void setUserLastRequestOpenTime(long j) {
            PreferenceHelper.putLong(PrefKeys.SEARCH_USER_LAST_REQUEST_OPEN_TIME, j);
        }

        public static void removeIsUserSearchSwitchOpen() {
            PreferenceHelper.removeKey(PrefKeys.SEARCH_USER_SWITCH_STATUS);
        }

        public static String getFeedbackReportedTags() {
            return PreferenceHelper.getString(PrefKeys.SEARCH_FEEDBACK_TASK_REPORTED_TAGS, null);
        }

        public static void setFeedbackReportedTags(String str) {
            PreferenceHelper.putString(PrefKeys.SEARCH_FEEDBACK_TASK_REPORTED_TAGS, str);
        }

        public static boolean shouldShowFeedbackPolicy() {
            return PreferenceHelper.getBoolean(PrefKeys.SEARCH_FEEDBACK_SHOULD_SHOW_POLICY, true);
        }

        public static void setShouldShowFeedbackPolicy(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SEARCH_FEEDBACK_SHOULD_SHOW_POLICY, z);
        }

        public static void setIsSearchCacheStatusOpen(boolean z) {
            AppStartupPres.setIsSearchCacheStatusOpen(z);
        }

        public static void removeSearchCacheStatus() {
            AppStartupPres.removeSearchCacheStatus();
        }

        public static void remove() {
            removeIsUserSearchSwitchOpen();
            PreferenceHelper.removeKey(PrefKeys.SEARCH_FEEDBACK_TASK_REPORTED_TAGS);
            PreferenceHelper.removeKey(PrefKeys.SEARCH_USER_LAST_REQUEST_OPEN_TIME);
            removeSearchCacheStatus();
        }
    }

    /* loaded from: classes2.dex */
    public static class Secret {
        public static boolean isFirstAddSecret() {
            return PreferenceHelper.getBoolean(PrefKeys.SECRET_FIRST_ADD_SECRET, true);
        }

        public static boolean isFirstUsePrivacyPassword() {
            return PreferenceHelper.getBoolean(PrefKeys.SECRET_FIRST_USE_PRIVACY_PASSWORD, true);
        }

        public static void setIsFirstUsePrivacyPassword(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SECRET_FIRST_USE_PRIVACY_PASSWORD, z);
        }

        public static void setIsFirstAddSecret(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SECRET_FIRST_ADD_SECRET, z);
        }

        public static boolean isFirstAddSecretVideo() {
            return PreferenceHelper.getBoolean(PrefKeys.SECRET_FIRST_ADD_SECRET_VIDEO, true);
        }

        public static void setFirstAddSecretVideo(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SECRET_FIRST_ADD_SECRET_VIDEO, z);
        }

        public static void setLastModifiedTimeAddToSecret(long j) {
            PreferenceHelper.putLong(PrefKeys.LAST_MODIFIED_TIME_ADD_TO_SECRET, j);
        }

        public static long getLastModifiedTimeAddToSecret() {
            return PreferenceHelper.getLong(PrefKeys.LAST_MODIFIED_TIME_ADD_TO_SECRET, -1L);
        }

        public static void setSecretTutorialRestTime(int i) {
            PreferenceHelper.putInt(PrefKeys.SECRET_TUTORIAL_REST_TIME, i);
        }

        public static int getSecretTutorialRestTime() {
            return PreferenceHelper.getInt(PrefKeys.SECRET_TUTORIAL_REST_TIME, 5);
        }

        public static void setLastEnterPrivateAlbumTime(long j) {
            PreferenceHelper.putLong(PrefKeys.LAST_ENTER_PRIVATE_ALBUM_TIME, j);
        }

        public static long getLastEnterPrivateAlbumTime() {
            return PreferenceHelper.getLong(PrefKeys.LAST_ENTER_PRIVATE_ALBUM_TIME, -1L);
        }
    }

    /* loaded from: classes2.dex */
    public static class Delete {
        public static boolean isFirstDeleteFromHomePage() {
            return isFirstDelete(PrefKeys.DELETE_FIRST_DELETE_FROM_HOMEPAGE);
        }

        public static boolean isFirstDeleteFromAlbum() {
            return isFirstDelete(PrefKeys.DELETE_FIRST_DELETE_FROM_ALBUM);
        }

        public static boolean isFirstDelete(String str) {
            boolean z = PreferenceHelper.getBoolean(str, true);
            if (z) {
                PreferenceHelper.putBoolean(str, false);
            }
            return z;
        }
    }

    /* loaded from: classes2.dex */
    public static class MediaScanner {
        public static final long PRUNE_PROTECT_THRESHOLD_MILLIS = TimeUnit.DAYS.toMillis(3);

        public static long getLastImagesScanTime() {
            return PreferenceHelper.getLong(BaseGalleryPreferences.PrefKeys.SCANNER_LAST_IMAGES_SCAN_TIME, 0L);
        }

        public static long getLastVideosScanTime() {
            return PreferenceHelper.getLong(PrefKeys.SCANNER_LAST_VIDEOS_SCAN_TIME, 0L);
        }

        public static void setLastImagesScanTime(long j) {
            PreferenceHelper.putLong(BaseGalleryPreferences.PrefKeys.SCANNER_LAST_IMAGES_SCAN_TIME, j);
        }

        public static void setLastVideosScanTime(long j) {
            PreferenceHelper.putLong(PrefKeys.SCANNER_LAST_VIDEOS_SCAN_TIME, j);
        }

        public static long getLastImagesGeneration() {
            return PreferenceHelper.getLong(PrefKeys.SCANNER_LAST_IMAGES_GENERATION_MODIFIED, 0L);
        }

        public static long getLastVideosGeneration() {
            return PreferenceHelper.getLong(PrefKeys.SCANNER_LAST_VEDIOS_GENERATION_MODIFIED, 0L);
        }

        public static void setLastImagesGeneration(long j) {
            PreferenceHelper.putLong(PrefKeys.SCANNER_LAST_IMAGES_GENERATION_MODIFIED, j);
        }

        public static void setLastVideosGeneration(long j) {
            PreferenceHelper.putLong(PrefKeys.SCANNER_LAST_VEDIOS_GENERATION_MODIFIED, j);
        }

        public static void recordAlbumRestoreTimeMillis() {
            PreferenceHelper.putLong(PrefKeys.SCANNER_LAST_LOCAL_RESTORE_TIME, System.currentTimeMillis());
        }

        public static boolean getEverForceScanAllAlbumsForFormatExpansion() {
            return PreferenceHelper.getBoolean(PrefKeys.SCANNER_EVER_FORCE_SCAN_ALL_ALBUMS_FOR_FORMAT_EXPANSION, false);
        }

        public static void setEverForceScanAllAlbumsForFormatExpansion() {
            PreferenceHelper.putBoolean(PrefKeys.SCANNER_EVER_FORCE_SCAN_ALL_ALBUMS_FOR_FORMAT_EXPANSION, true);
        }

        public static boolean getEverCleanEmptyPathAlbum() {
            return PreferenceHelper.getBoolean(PrefKeys.SCANNER_EVER_CLEAN_EMPTY_PATH_ALBUM, false);
        }

        public static void setEverCleanEmptyPathAlbum() {
            PreferenceHelper.putBoolean(PrefKeys.SCANNER_EVER_CLEAN_EMPTY_PATH_ALBUM, true);
        }

        public static boolean getEverRefillLocationForScreenshots() {
            return PreferenceHelper.getBoolean(PrefKeys.SCANNER_EVER_REFILL_LOCATION_FOR_SCREENSHOTS, false);
        }

        public static void setEverRefillLocationForScreenshots() {
            PreferenceHelper.putBoolean(PrefKeys.SCANNER_EVER_REFILL_LOCATION_FOR_SCREENSHOTS, true);
        }
    }

    /* loaded from: classes2.dex */
    public static class LocalMode {
        public static volatile Boolean sIsLocalMode;
        public static final WeakHashMap<Object, Object> sListeners = new WeakHashMap<>();

        public static synchronized boolean isOnlyShowLocalPhoto() {
            synchronized (LocalMode.class) {
                boolean z = false;
                if (sIsLocalMode == null) {
                    Boolean valueOf = Boolean.valueOf(PreferenceHelper.getBoolean(PrefKeys.LOCAL_MODE_ONLY_SHOW_LOCAL_PHOTO, false));
                    sIsLocalMode = valueOf;
                    return valueOf.booleanValue();
                }
                if (sIsLocalMode.booleanValue() && SyncUtil.existXiaomiAccount(GalleryApp.sGetAndroidContext())) {
                    z = true;
                }
                return z;
            }
        }

        public static synchronized void setOnlyShowLocalPhoto(boolean z) {
            synchronized (LocalMode.class) {
                sIsLocalMode = Boolean.valueOf(z);
                PreferenceHelper.putBoolean(PrefKeys.LOCAL_MODE_ONLY_SHOW_LOCAL_PHOTO, z);
            }
        }

        public static boolean isOnlyShowLocalModeKey(String str) {
            return str.equals(PrefKeys.LOCAL_MODE_ONLY_SHOW_LOCAL_PHOTO);
        }

        public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            PreferenceHelper.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    /* loaded from: classes2.dex */
    public static class DataBase {
        public static boolean getEverFixedCameraAlbumAttributes() {
            return PreferenceHelper.getBoolean(PrefKeys.DATABASE_EVER_UPDATE_CAMERA_ALBUM_ATTRIBUTES, false);
        }

        public static void setEverFixedCameraAlbumAttributes() {
            PreferenceHelper.putBoolean(PrefKeys.DATABASE_EVER_UPDATE_CAMERA_ALBUM_ATTRIBUTES, true);
        }

        public static int getViewVersion(String str) {
            return PreferenceHelper.getInt(String.format(Locale.US, PrefKeys.DATABASE_VIEW_VERSION_PREFIX, str), 0);
        }

        public static void setViewVersion(String str, int i) {
            PreferenceHelper.putInt(String.format(Locale.US, PrefKeys.DATABASE_VIEW_VERSION_PREFIX, str), i);
        }

        public static boolean getEverUpgradeAlbumEditedColumns() {
            return PreferenceHelper.getBoolean(PrefKeys.DATABASE_EVER_UPGRADE_ALBUM_EDITED_COLUMNS, false);
        }

        public static void setEverUpgradeAlbumEditedColumns() {
            PreferenceHelper.putBoolean(PrefKeys.DATABASE_EVER_UPGRADE_ALBUM_EDITED_COLUMNS, true);
        }

        public static boolean getEverUpgradeDBForScreenshots() {
            return PreferenceHelper.getBoolean(PrefKeys.DATABASE_EVER_UPGRADE_DB_FOR_SCREENSHOT, false);
        }

        public static void setUpgradeDBForScreenshots() {
            PreferenceHelper.putBoolean(PrefKeys.DATABASE_EVER_UPGRADE_DB_FOR_SCREENSHOT, true);
        }
    }

    /* loaded from: classes2.dex */
    public static class SlideShow {
        public static int getSlideShowInterval() {
            return PreferenceHelper.getInt(PrefKeys.SLIDESHOW_INTERVAL, 3);
        }

        public static boolean isSlideShowLoop() {
            return PreferenceHelper.getBoolean(PrefKeys.SLIDESHOW_LOOP, false);
        }

        public static void setSlideShowInterval(int i) {
            PreferenceHelper.putInt(PrefKeys.SLIDESHOW_INTERVAL, i);
        }

        public static void setSlideShowLoop(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SLIDESHOW_LOOP, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class HiddenAlbum {
        public static boolean isShowHiddenAlbum() {
            return false;
        }

        public static void setShowHiddenAlbum(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.HIDDEN_ALBUM_SHOW, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoEditor {
        public static void setCompareTipShow() {
            PreferenceHelper.putBoolean(PrefKeys.PHOTO_EDITOR_COMPARE_TIP, true);
        }

        public static boolean shouldCompareTipShow() {
            return !PreferenceHelper.getBoolean(PrefKeys.PHOTO_EDITOR_COMPARE_TIP, false);
        }

        public static boolean isCropTipsShow() {
            return PreferenceHelper.getInt(PrefKeys.PHOTO_EDITOR_CROP_TIPS_TIMES, 0) < 3;
        }

        public static void addCropTipsShowTimes() {
            PreferenceHelper.putInt(PrefKeys.PHOTO_EDITOR_CROP_TIPS_TIMES, PreferenceHelper.getInt(PrefKeys.PHOTO_EDITOR_CROP_TIPS_TIMES, 0) + 1);
        }
    }

    /* loaded from: classes2.dex */
    public static class LocationManager {
        public static int getLocationDetailsVersion(int i) {
            return PreferenceHelper.getInt(PrefKeys.ADDRESS_VERSION, i);
        }

        public static void setLocationDetailsVersion(int i) {
            PreferenceHelper.putInt(PrefKeys.ADDRESS_VERSION, i);
        }

        public static void remove() {
            PreferenceHelper.removeKey(PrefKeys.ADDRESS_VERSION);
        }
    }

    /* loaded from: classes2.dex */
    public static class CloudControl {
        public static String getSyncToken() {
            return PreferenceHelper.getString(PrefKeys.CLOUD_CONTROL_SYNC_TOKEN, null);
        }

        public static void setSyncToken(String str) {
            PreferenceHelper.putString(PrefKeys.CLOUD_CONTROL_SYNC_TOKEN, str);
        }

        public static long getLastRequestTime() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_CONTROL_LAST_REQUEST_TIME, 0L);
        }

        public static void setLastRequestTime(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_CONTROL_LAST_REQUEST_TIME, j);
        }

        public static long getLastRequestSucceedTime() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_CONTROL_LAST_REQUEST_SUCCEED_TIME, 0L);
        }

        public static void setLastRequestSucceedTime(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_CONTROL_LAST_REQUEST_SUCCEED_TIME, j);
        }

        public static long getPushTag() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_CONTROL_PUSH_TAG, 0L);
        }

        public static void setPushTag(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_CONTROL_PUSH_TAG, j);
        }

        public static void remove() {
            PreferenceHelper.getPreferences().edit().remove(PrefKeys.CLOUD_CONTROL_SYNC_TOKEN).remove(PrefKeys.CLOUD_CONTROL_LAST_REQUEST_SUCCEED_TIME).remove(PrefKeys.CLOUD_CONTROL_PUSH_TAG).apply();
        }
    }

    /* loaded from: classes2.dex */
    public static class FileDownload {
        public static String getConnTimeoutKey(DownloadType downloadType) {
            return String.format(Locale.US, PrefKeys.FILE_DOWNLOAD_CONN_TIMEOUT_FORMAT, downloadType.name());
        }

        public static int getConnTimeout(DownloadType downloadType) {
            return clampConnTimeout(MemoryPreferenceHelper.getInt(getConnTimeoutKey(downloadType), 10000));
        }

        public static boolean setConnTimeout(DownloadType downloadType, int i) {
            int clampConnTimeout = clampConnTimeout(i);
            MemoryPreferenceHelper.putInt(getConnTimeoutKey(downloadType), clampConnTimeout);
            return i == clampConnTimeout;
        }

        public static int clampConnTimeout(int i) {
            return BaseMiscUtil.clamp(i, 10000, 30000);
        }
    }

    /* loaded from: classes2.dex */
    public static class TopBar {
        public static String getSyncStatusTipKey(SyncStatus syncStatus) {
            return PrefKeys.TOP_BAR_CANNOT_SYNC_PREFIX + syncStatus.name();
        }

        public static boolean hasShowedSyncStatusTip(SyncStatus syncStatus) {
            return PreferenceHelper.getBoolean(getSyncStatusTipKey(syncStatus), false);
        }

        public static void setHasShowedSyncStatusTip(SyncStatus syncStatus, boolean z) {
            PreferenceHelper.putBoolean(getSyncStatusTipKey(syncStatus), z);
        }

        public static void saveLastSyncStatus(SyncStatus syncStatus) {
            MemoryPreferenceHelper.putString(PrefKeys.TOP_BAR_LAST_SYNC_STATUS, syncStatus.name());
        }

        public static SyncStatus getLastSyncStatus() {
            return SyncStatus.fromName(MemoryPreferenceHelper.getString(PrefKeys.TOP_BAR_LAST_SYNC_STATUS, SyncStatus.UNKNOWN_ERROR.name()));
        }

        public static void remove() {
            SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
            for (String str : PreferenceHelper.getPreferences().getAll().keySet()) {
                if (!TextUtils.isEmpty(str) && str.startsWith(PrefKeys.TOP_BAR_CANNOT_SYNC_PREFIX)) {
                    edit.remove(str);
                }
            }
            edit.apply();
        }
    }

    /* loaded from: classes2.dex */
    public static class FeatureRedDot {
        public static boolean hasUpdateFeatureUsed(String str) {
            return PreferenceHelper.getBoolean(PrefKeys.FEATURE_EVER_DISPLAYED + str, true);
        }

        public static void setUpdateFeatureUsed(String str, boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.FEATURE_EVER_DISPLAYED + str, z);
        }

        public static void setFeatureRedDotValidTime(String str, long j, long j2) {
            PreferenceHelper.putLong(PrefKeys.FEATURE_RED_DOT_VALID_START_TIME + str, j);
            PreferenceHelper.putLong(PrefKeys.FEATURE_RED_DOT_VALID_END_TIME + str, j2);
        }

        public static boolean isFeatureRedDotValid(String str, long j) {
            if (j >= PreferenceHelper.getLong(PrefKeys.FEATURE_RED_DOT_VALID_START_TIME + str, 0L)) {
                if (j <= PreferenceHelper.getLong(PrefKeys.FEATURE_RED_DOT_VALID_END_TIME + str, 0L)) {
                    return true;
                }
            }
            return false;
        }

        public static void setRedDotSawTime(String str, long j) {
            PreferenceHelper.putLong(PrefKeys.RED_DOT_SAW_TIME + str, j);
        }

        public static long getRedDotSawTime(String str) {
            return PreferenceHelper.getLong(PrefKeys.RED_DOT_SAW_TIME + str, 0L);
        }

        public static void setLastUnreddenTime(long j) {
            PreferenceHelper.putLong(PrefKeys.LAST_UNREDDEN_TIME, j);
        }

        public static long getLastUnreddenTime() {
            return PreferenceHelper.getLong(PrefKeys.LAST_UNREDDEN_TIME, 0L);
        }
    }

    /* loaded from: classes2.dex */
    public static class UUID {
        public static String get() {
            String string = PreferenceHelper.getString(PrefKeys.UNIQUE_ID, null);
            if (TextUtils.isEmpty(string)) {
                String uuid = java.util.UUID.randomUUID().toString();
                PreferenceHelper.putString(PrefKeys.UNIQUE_ID, uuid);
                return uuid;
            }
            return string;
        }
    }

    /* loaded from: classes2.dex */
    public static class Favorites {
        public static boolean isFirstTimeAddToFavorites() {
            boolean z = PreferenceHelper.getBoolean(PrefKeys.FIRST_TIME_ADD_TO_FAVORITES, true);
            if (z) {
                PreferenceHelper.putBoolean(PrefKeys.FIRST_TIME_ADD_TO_FAVORITES, false);
            }
            return z;
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoSlim {
        public static void setIsFirstUsePhotoSlim(Boolean bool) {
            PreferenceHelper.putBoolean(PrefKeys.PHOTO_SLIM_FIRST_USE, bool.booleanValue());
        }

        public static boolean isFirstUsePhotoSlim() {
            return PreferenceHelper.getBoolean(PrefKeys.PHOTO_SLIM_FIRST_USE, true);
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoPrint {
        public static int getSilentInstallTimes() {
            return PreferenceHelper.getInt(PrefKeys.PHOTO_PRINT_SILENT_INSTALL_TIMES, 0);
        }

        public static boolean isPrintFirstClicked() {
            return PreferenceHelper.getBoolean(PrefKeys.PRINT_FIRST_CLICKED, true);
        }

        public static void setIsPrintFirstClicked(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.PRINT_FIRST_CLICKED, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class SettingsSync {
        public static boolean isDirty() {
            return PreferenceHelper.getBoolean(PrefKeys.SETTINGS_SYNC_IS_DIRTY, false);
        }

        public static void markDirty(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SETTINGS_SYNC_IS_DIRTY, z);
        }

        public static boolean isFirstUploadComplete() {
            return PreferenceHelper.getBoolean(PrefKeys.SETTINGS_SYNC_FIRST_UPLOAD_COMPLETE, false);
        }

        public static void setFirstUploadComplete() {
            PreferenceHelper.putBoolean(PrefKeys.SETTINGS_SYNC_FIRST_UPLOAD_COMPLETE, true);
        }

        public static void setLastDownloadTime(long j) {
            PreferenceHelper.putLong(PrefKeys.SETTINGS_DOWNLOAD_LAST_TIME, j);
        }

        public static long getLastDownloadTime() {
            return PreferenceHelper.getLong(PrefKeys.SETTINGS_DOWNLOAD_LAST_TIME, 0L);
        }

        public static void remove() {
            PreferenceHelper.removeKey(PrefKeys.SETTINGS_SYNC_IS_DIRTY);
            PreferenceHelper.removeKey(PrefKeys.SETTINGS_SYNC_FIRST_UPLOAD_COMPLETE);
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoFilterSkyGuide {
        public static void setPhotoFilterSkyGuided(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.PHOTO_FILTER_SKY_GUIDE_COMPLETED, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class PhotoFilterPortraitColorGuide {
        public static void setPhotoFilterPortraitColorGuided(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.PHOTO_FILTER_PORTRAIT_COLOR_GUIDE_COMPLETED, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class Assistant {
        public static long getLastTriggerTime() {
            return PreferenceHelper.getLong(PrefKeys.ASSISTANT_LAST_TRIGGER_TIME, 0L);
        }

        public static void setLastTriggerTime(long j) {
            PreferenceHelper.putLong(PrefKeys.ASSISTANT_LAST_TRIGGER_TIME, j);
        }

        public static long getLastGuaranteeTriggerTime() {
            return PreferenceHelper.getLong(PrefKeys.ASSISTANT_LAST_GUARANTEE_TRIGGER_TIME, 0L);
        }

        public static void setLastGuaranteeTriggerTime(long j) {
            PreferenceHelper.putLong(PrefKeys.ASSISTANT_LAST_GUARANTEE_TRIGGER_TIME, j);
        }

        public static boolean hasCardEver() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_HAS_CARD_EVER, false);
        }

        public static void setHasCardEver() {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_HAS_CARD_EVER, true);
        }

        public static boolean isFirstShowImageSelection() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_FIRST_SHOW_IMAGE_SELECTION, true);
        }

        public static void setIsFirstShowImageSelection(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_FIRST_SHOW_IMAGE_SELECTION, z);
        }

        public static boolean isImageSelectionFunctionOn() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_IMAGE_SELECTION_FUNCTION, true);
        }

        public static void setImageSelectionFunctionState(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_IMAGE_SELECTION_FUNCTION, z);
        }

        public static boolean isStoryFunctionOn() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_STORY_FUNCTION, true);
        }

        public static void setStoryFunctionState(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_STORY_FUNCTION, z);
        }

        public static boolean isCreativityFunctionOn() {
            if (BaseBuildUtil.isInternational()) {
                return true;
            }
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_CREATIVITY_FUNCTION, true);
        }

        public static void setCreativityFunctionState(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_CREATIVITY_FUNCTION, z);
        }

        public static void setCardSyncDirty(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_CARD_SYNC_DIRTY, z);
        }

        public static boolean isCardSyncDirty() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_CARD_SYNC_DIRTY, false);
        }

        public static void setForceRefreshLibraryInfo(boolean z, long j) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_FORCE_REFRESH_LIBRARY_INFO + j, z);
        }

        public static boolean isForceRefreshLibraryInfo(long j) {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_FORCE_REFRESH_LIBRARY_INFO + j, false);
        }

        public static void setAssistantCoverUpdated(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_COVER_UPDATED, z);
        }

        public static boolean getAssistantCoverUpdated() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_COVER_UPDATED, false);
        }

        public static void setAssistantHeatmapCalculated(List<String> list) {
            PreferenceHelper.putString(PrefKeys.ASSISTANT_CARD_HEATMAP_CALCULATED, BaseMiscUtil.isValid(list) ? Joiner.on(",").join(list) : null);
        }

        public static List<String> getAssistantHeatmapCalculated() {
            String string = PreferenceHelper.getString(PrefKeys.ASSISTANT_CARD_HEATMAP_CALCULATED, null);
            if (string != null) {
                return Splitter.on(",").splitToList(string);
            }
            return null;
        }

        public static boolean hasTriggeredNewScenario() {
            return PreferenceHelper.getBoolean(PrefKeys.ASSISTANT_HAS_TRIGGER_NEW_SCENARIO, false);
        }

        public static void setHasTriggeredNewScenario(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.ASSISTANT_HAS_TRIGGER_NEW_SCENARIO, z);
        }

        public static long getFirstTriggerTime() {
            return PreferenceHelper.getLong(PrefKeys.ASSISTANT_FIRST_TRIGGER_TIME, System.currentTimeMillis());
        }

        public static void setFirstTriggerTime(long j) {
            PreferenceHelper.putLong(PrefKeys.ASSISTANT_FIRST_TRIGGER_TIME, j);
        }
    }

    /* loaded from: classes2.dex */
    public static class MultiView {
        public static boolean hasShownTip() {
            return PreferenceHelper.getBoolean(PrefKeys.MONTH_VIEW_HAS_SHOWN_FIRST_UPGRADE_TIP, false);
        }

        public static void setHasShownTip(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.MONTH_VIEW_HAS_SHOWN_FIRST_UPGRADE_TIP, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class HomePage {
        public static String getHomePageImageIds() {
            return PreferenceHelper.getString(PrefKeys.HOME_PAGE_IMAGE_IDS, "");
        }

        public static void setHomePageImageIds(String str) {
            PreferenceHelper.putString(PrefKeys.HOME_PAGE_IMAGE_IDS, str);
        }

        public static boolean isHomePageShowAllPhotos() {
            return PreferenceHelper.getBoolean(PrefKeys.HOME_PAGE_SWITCH_ALL_PHOTOS, true);
        }

        public static void setHomePageShowAllPhotos(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.HOME_PAGE_SWITCH_ALL_PHOTOS, z);
        }

        public static int getHomePageDiscoverPhotos() {
            return PreferenceHelper.getInt(PrefKeys.HOME_PAGE_DISCOVER_PHOTOS, 0);
        }

        public static void setHomePageDiscoverPhotos(int i) {
            PreferenceHelper.putInt(PrefKeys.HOME_PAGE_DISCOVER_PHOTOS, i);
        }

        public static int getHomePageViewMode() {
            return PreferenceHelper.getInt(PrefKeys.HOME_PAGE_VIEW_MODE, 1);
        }

        public static void setHomePageViewMode(int i) {
            PreferenceHelper.putInt(PrefKeys.HOME_PAGE_VIEW_MODE, i);
        }
    }

    /* loaded from: classes2.dex */
    public static class Trash {
        public static void setUserInfo(String str) {
            PreferenceHelper.putString(PrefKeys.VIP_INFO, str);
        }

        public static String getUserInfo() {
            return PreferenceHelper.getString(PrefKeys.VIP_INFO, null);
        }

        public static void setWhite2VipTime(long j) {
            PreferenceHelper.putLong(PrefKeys.WHITE_TO_VIP_TIME, j);
        }
    }

    /* loaded from: classes2.dex */
    public static class IncompatibleMedia {
        public static boolean isIncompatibleMediaAutoConvert() {
            return PreferenceHelper.getBoolean(PrefKeys.INCOMPATIBLE_MEDIA_AUTO_CONVERT, true);
        }

        public static void setIncompatibleMediaAutoConvert(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.INCOMPATIBLE_MEDIA_AUTO_CONVERT, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class RiskControl {
        public static void markDelete(String str) {
            Set<String> stringSet = PreferenceHelper.getStringSet(PrefKeys.RISK_CONTROL_DELETE, new HashSet());
            stringSet.add(str);
            PreferenceHelper.putStringSet(PrefKeys.RISK_CONTROL_DELETE, stringSet);
        }

        public static Set<String> getDeleteMessages() {
            return PreferenceHelper.getStringSet(PrefKeys.RISK_CONTROL_DELETE, null);
        }

        public static void clearDelete(String str) {
            Set<String> stringSet = PreferenceHelper.getStringSet(PrefKeys.RISK_CONTROL_DELETE, null);
            if (stringSet != null) {
                stringSet.remove(str);
                PreferenceHelper.putStringSet(PrefKeys.RISK_CONTROL_DELETE, stringSet);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Maml {
        public static int getMamlAssertsVersion() {
            return PreferenceHelper.getInt(PrefKeys.MAML_ASSETS_VERSION, 0);
        }

        public static void setMamlAssertsVersion(int i) {
            PreferenceHelper.putInt(PrefKeys.MAML_ASSETS_VERSION, i);
        }
    }

    /* loaded from: classes2.dex */
    public static class ScreenEditorPreference {
        public static boolean isSendAndDelete() {
            return PreferenceHelper.getBoolean(PrefKeys.SCREEN_EDITOR_IS_SEND_AND_DELETE, false);
        }

        public static void setSendAndDelete(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.SCREEN_EDITOR_IS_SEND_AND_DELETE, z);
        }
    }

    /* loaded from: classes2.dex */
    public static class OnlineVideo {
        public static void userIgnoreVipTip() {
            PreferenceHelper.putBoolean(PrefKeys.HAS_IGNORE_VIP_TIP, true);
        }

        public static boolean hasIgnoredVipTip() {
            return PreferenceHelper.getBoolean(PrefKeys.HAS_IGNORE_VIP_TIP, false);
        }

        public static void updateVipTipShownTime() {
            PreferenceHelper.putLong(PrefKeys.VIP_TIP_SHOW_TIME, System.currentTimeMillis());
        }

        public static long getVipTipShownTime() {
            return PreferenceHelper.getLong(PrefKeys.VIP_TIP_SHOW_TIME, 0L);
        }
    }

    /* loaded from: classes2.dex */
    public static class LockOrientation {
        public static void hasNoticeShowed() {
            PreferenceHelper.putBoolean(PrefKeys.LOCK_ORIENTATION_NOTICE_SHOWED, true);
        }

        public static boolean isNoticeShowed() {
            return PreferenceHelper.getBoolean(PrefKeys.LOCK_ORIENTATION_NOTICE_SHOWED, false);
        }
    }

    /* loaded from: classes2.dex */
    public static class MapAlbum {
        public static void setNoticeShowed() {
            PreferenceHelper.putBoolean(PrefKeys.MAP_ALBUM_NOTICE_SHOWED, true);
        }

        public static boolean isNoticeShowed() {
            return PreferenceHelper.getBoolean(PrefKeys.MAP_ALBUM_NOTICE_SHOWED, false);
        }

        public static void setPrivacyPolicyShowed(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.MAP_SHOW_PRIVACY_POLICY, z);
        }

        public static boolean isPrivacyPolicyShowed() {
            return PreferenceHelper.getBoolean(PrefKeys.MAP_SHOW_PRIVACY_POLICY, false);
        }
    }

    /* loaded from: classes2.dex */
    public static class MiCloud {
        public static int getCloudSpaceAlmostFullTipCounts() {
            return PreferenceHelper.getInt(PrefKeys.CLOUD_FULL_TIP_COUNTS, 0);
        }

        public static void setCloudSpaceAlmostFullTipCounts(int i) {
            PreferenceHelper.putInt(PrefKeys.CLOUD_FULL_TIP_COUNTS, i);
        }

        public static int getCloudSpaceCompletelyFullTipCounts() {
            return PreferenceHelper.getInt(PrefKeys.CLOUD_COMPLETELY_FULL_TIP_COUNTS, 0);
        }

        public static void setCloudSpaceCompletelyFullTipCounts(int i) {
            PreferenceHelper.putInt(PrefKeys.CLOUD_COMPLETELY_FULL_TIP_COUNTS, i);
        }

        public static long getCloudSpaceFullTipLastCheckTime() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_FULL_LAST_CHECK_TIME, 0L);
        }

        public static void setCloudSpaceFullTipLastCheckTime(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_FULL_LAST_CHECK_TIME, j);
        }

        public static void setPrivacyPolicyAgreed(String str, boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.CLOUD_PRIVACY_POLICY_AGREED + str, z);
        }

        public static boolean isPrivacyPolicyAgreed(String str) {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return PreferenceHelper.getBoolean(PrefKeys.CLOUD_PRIVACY_POLICY_AGREED + str, false);
        }

        public static void setPrivacyPolicyRejected(String str, boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.CLOUD_PRIVACY_POLICY_REJECTED + str, z);
        }

        public static boolean isPrivacyPolicyRejected(String str) {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return PreferenceHelper.getBoolean(PrefKeys.CLOUD_PRIVACY_POLICY_REJECTED + str, false);
        }

        public static long getCloudSpaceAlmostFullTipLastPopped() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_FULL_LAST_POPPED, 0L);
        }

        public static void setCloudSpaceAlmostFullTipLastPopped(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_FULL_LAST_POPPED, j);
        }

        public static long getCloudSpaceCompletelyFullTipLastPopped() {
            return PreferenceHelper.getLong(PrefKeys.CLOUD_COMPLETELY_FULL_LAST_POPPED, 0L);
        }

        public static void setCloudSpaceCompletelyFullTipLastPopped(long j) {
            PreferenceHelper.putLong(PrefKeys.CLOUD_COMPLETELY_FULL_LAST_POPPED, j);
        }
    }

    public static void runOnce(String str, OneshotAction oneshotAction) {
        if (PreferenceHelper.getBoolean(str, false) || !oneshotAction.exec()) {
            return;
        }
        PreferenceHelper.putBoolean(str, true);
    }

    /* loaded from: classes2.dex */
    public static class AppStartupPres {
        public static final boolean DEFAULT_AUTO_BATCH_DOWNLOAD = CloudControlStrategyHelper.getSyncStrategy().isAutoDownload();

        public static SharedPreferences getSharedPreferences() {
            return GalleryApp.sGetAndroidContext().getSharedPreferences("gallery_startup_setting", 0);
        }

        public static boolean isSetExemptMasterSyncAuto() {
            SharedPreferences sharedPreferences = getSharedPreferences();
            if (!sharedPreferences.contains(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO) && PreferenceHelper.contains(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO)) {
                boolean z = PreferenceHelper.getBoolean(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO, false);
                sharedPreferences.edit().putBoolean(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO, z).apply();
                PreferenceHelper.removeKey(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO);
                return z;
            }
            return sharedPreferences.getBoolean(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO, false);
        }

        public static void setExemptMasterSyncAuto(boolean z) {
            if (!getSharedPreferences().contains(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO) && PreferenceHelper.contains(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO)) {
                PreferenceHelper.removeKey(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO);
            }
            getSharedPreferences().edit().putBoolean(PrefKeys.IS_SET_EXEMPT_MASTER_SYNC_AUTO, z).apply();
        }

        public static void setIsSearchCacheStatusOpen(boolean z) {
            if (!getSharedPreferences().contains(PrefKeys.SEARCH_CACHE_STATUS) && PreferenceHelper.contains(PrefKeys.SEARCH_CACHE_STATUS)) {
                PreferenceHelper.removeKey(PrefKeys.SEARCH_CACHE_STATUS);
            }
            getSharedPreferences().edit().putBoolean(PrefKeys.SEARCH_CACHE_STATUS, z).apply();
        }

        public static void removeSearchCacheStatus() {
            getSharedPreferences().edit().remove(PrefKeys.SEARCH_CACHE_STATUS).apply();
        }

        public static boolean isUserSearchSwitchOpen(boolean z) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            if (!sharedPreferences.contains(PrefKeys.SEARCH_USER_SWITCH_STATUS) && PreferenceHelper.contains(PrefKeys.SEARCH_USER_SWITCH_STATUS)) {
                boolean z2 = PreferenceHelper.getBoolean(PrefKeys.SEARCH_USER_SWITCH_STATUS, false);
                sharedPreferences.edit().putBoolean(PrefKeys.SEARCH_USER_SWITCH_STATUS, z2).apply();
                PreferenceHelper.removeKey(PrefKeys.SEARCH_USER_SWITCH_STATUS);
                return z2;
            }
            return sharedPreferences.getBoolean(PrefKeys.SEARCH_USER_SWITCH_STATUS, z);
        }

        public static void setIsUserSearchSwitchOpen(boolean z) {
            if (!getSharedPreferences().contains(PrefKeys.SEARCH_USER_SWITCH_STATUS) && PreferenceHelper.contains(PrefKeys.SEARCH_USER_SWITCH_STATUS)) {
                PreferenceHelper.removeKey(PrefKeys.SEARCH_USER_SWITCH_STATUS);
            }
            getSharedPreferences().edit().putBoolean(PrefKeys.SEARCH_USER_SWITCH_STATUS, z).apply();
        }

        public static boolean isUserSearchSwitchSet() {
            return getSharedPreferences().contains(PrefKeys.SEARCH_USER_SWITCH_STATUS);
        }

        public static boolean isAutoDownload() {
            SharedPreferences sharedPreferences = getSharedPreferences();
            if (!sharedPreferences.contains(PrefKeys.SYNC_AUTO_DOWNLOAD) && PreferenceHelper.contains(PrefKeys.SYNC_AUTO_DOWNLOAD)) {
                boolean z = PreferenceHelper.getBoolean(PrefKeys.SYNC_AUTO_DOWNLOAD, DEFAULT_AUTO_BATCH_DOWNLOAD);
                sharedPreferences.edit().putBoolean(PrefKeys.SYNC_AUTO_DOWNLOAD, z).apply();
                PreferenceHelper.removeKey(PrefKeys.SYNC_AUTO_DOWNLOAD);
                return z;
            }
            return sharedPreferences.getBoolean(PrefKeys.SYNC_AUTO_DOWNLOAD, DEFAULT_AUTO_BATCH_DOWNLOAD);
        }

        public static void setAutoDownload(boolean z) {
            if (!getSharedPreferences().contains(PrefKeys.SYNC_AUTO_DOWNLOAD) && PreferenceHelper.contains(PrefKeys.SYNC_AUTO_DOWNLOAD)) {
                PreferenceHelper.removeKey(PrefKeys.SYNC_AUTO_DOWNLOAD);
            }
            getSharedPreferences().edit().putBoolean(PrefKeys.SYNC_AUTO_DOWNLOAD, z).apply();
        }

        public static boolean isAlreadyRequestToday() {
            return DateUtils.isSameDay(PreferenceHelper.getLong(PrefKeys.UPDATE_LAST_REQUEST_DATE, 0L), System.currentTimeMillis());
        }

        public static boolean isForceUpdateFinish() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_FORCE_UPDATE_FINISH, true);
        }

        public static boolean checkAppIsUpdate() {
            SharedPreferences sharedPreferences = getSharedPreferences();
            if (sharedPreferences.getInt(PrefKeys.UPDATE_LAST_VERSION_CODE, 0) < 401635) {
                sharedPreferences.edit().putInt(PrefKeys.UPDATE_LAST_VERSION_CODE, 401635).apply();
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestUpdatePref {
        public static boolean isAlreadyRequestToday() {
            return AppStartupPres.isAlreadyRequestToday();
        }

        public static void setLastRequestDate(long j) {
            PreferenceHelper.putLong(PrefKeys.UPDATE_LAST_REQUEST_DATE, j);
        }

        public static void setIsDelayUpdate(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_DELAY, z);
        }

        public static boolean isDelayUpdate() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_DELAY, false);
        }

        public static void setIsIgnoreUpdate(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_IGNORE, z);
        }

        public static boolean isIgnoreUpdate() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_IGNORE, false);
        }

        public static int getNewestVersionCode() {
            return PreferenceHelper.getInt(PrefKeys.UPDATE_NEWEST_VERSION_CODE, -1);
        }

        public static long getLastDelayDate() {
            return PreferenceHelper.getLong(PrefKeys.UPDATE_LAST_DELAY_DATE, 0L);
        }

        public static void setIsFindNewVersion(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_FIND_NEW_VERSION, z);
        }

        public static boolean isFindNewVersion() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_FIND_NEW_VERSION, false);
        }

        public static void setIsConfirmNewVersion(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_CONFIRM_NEW_VERSION, z);
        }

        public static boolean isConfirmNewVersion() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_CONFIRM_NEW_VERSION, false);
        }

        public static void setIsForceUpdateFinish(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_FORCE_UPDATE_FINISH, z);
        }

        public static boolean isForceUpdateFinish() {
            return AppStartupPres.isForceUpdateFinish();
        }

        public static void setIsNeedHint(boolean z) {
            PreferenceHelper.putBoolean(PrefKeys.UPDATE_IS_NEED_HINT, z);
        }

        public static boolean isNeedHint() {
            return PreferenceHelper.getBoolean(PrefKeys.UPDATE_IS_NEED_HINT, true);
        }

        public static void saveDelayData(boolean z, int i) {
            SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
            edit.putBoolean(PrefKeys.UPDATE_IS_DELAY, true);
            edit.putBoolean(PrefKeys.UPDATE_IS_IGNORE, z);
            edit.putBoolean(PrefKeys.UPDATE_IS_FIND_NEW_VERSION, !z);
            edit.putInt(PrefKeys.UPDATE_NEWEST_VERSION_CODE, i);
            edit.putLong(PrefKeys.UPDATE_LAST_DELAY_DATE, System.currentTimeMillis());
            edit.putBoolean(PrefKeys.UPDATE_IS_CONFIRM_NEW_VERSION, z);
            edit.apply();
        }

        public static void saveRedDotShowData() {
            SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
            edit.putLong(PrefKeys.UPDATE_LAST_REQUEST_DATE, System.currentTimeMillis());
            edit.putBoolean(PrefKeys.UPDATE_IS_FIND_NEW_VERSION, true);
            edit.putBoolean(PrefKeys.UPDATE_IS_CONFIRM_NEW_VERSION, false);
            edit.putBoolean(PrefKeys.UPDATE_IS_NEED_HINT, true);
            edit.apply();
        }

        public static void checkAppIsUpdate() {
            if (AppStartupPres.checkAppIsUpdate()) {
                SharedPreferences.Editor edit = PreferenceHelper.getPreferences().edit();
                edit.putBoolean(PrefKeys.UPDATE_IS_FIND_NEW_VERSION, false);
                edit.putBoolean(PrefKeys.UPDATE_IS_FORCE_UPDATE_FINISH, true);
                edit.putBoolean("feature_ever_displayedsettings", true);
                edit.apply();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class DownloadMediaEditorAppPres {
        public static int getShowDownloadMediaEditorAppCount() {
            return PreferenceHelper.getInt(PrefKeys.KEY_DOWNLOAD_MEDIA_EDITOR_APP, 0);
        }

        public static void increaseShowDownloadMediaEditorAppCount() {
            PreferenceHelper.putInt(PrefKeys.KEY_DOWNLOAD_MEDIA_EDITOR_APP, getShowDownloadMediaEditorAppCount() + 1);
        }
    }
}
