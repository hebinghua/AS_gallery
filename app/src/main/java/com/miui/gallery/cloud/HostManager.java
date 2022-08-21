package com.miui.gallery.cloud;

import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class HostManager {
    public static final String BASE_FACE_HOST;
    public static final String BASE_SEARCH_FEEDBACK_HOST;
    public static final String BASE_HOST = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/gallery/v3";
    public static final String BASE_ANONYMOUS_HOST = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryAnonymousHost() + "/mic/gallery/v3";
    public static final String BASE_SEARCH_HOST = ApplicationHelper.getMiCloudProvider().getCloudManager().getSearchHost() + "/mic/gallery/search/v1";
    public static final String BASE_SEARCH_ANONYMOUS_HOST = ApplicationHelper.getMiCloudProvider().getCloudManager().getSearchAnonymousHost() + "/mic/gallery/search/v1";
    public static final String BASE_VIP_INFO_HOST = ApplicationHelper.getMiCloudProvider().getCloudManager().getVipStatusHost() + "/mic/status/v2";

    static {
        String str = ApplicationHelper.getMiCloudProvider().getCloudManager().getFaceHost() + "/mic/gallery/face/v1";
        BASE_FACE_HOST = str;
        BASE_SEARCH_FEEDBACK_HOST = str;
    }

    /* loaded from: classes.dex */
    public static class AlbumShareOperation {
        public static String getBarcodeShareUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/shareurl/barcode", str);
        }

        public static String getSmsShareUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/shareurl/sms", str);
        }

        public static String getChangePublicUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/webshare", str);
        }

        public static String getOwnerRequestPublicUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/websharelink", str);
        }

        public static String getSharerRequestPublicUrl() {
            return HostManager.BASE_HOST + "/user/share/album/websharelink";
        }

        public static String getAcceptInvitationUrl() {
            return HostManager.BASE_HOST + "/user/share/album/shareurl/accept";
        }

        public static String getRefuseInvitationUrl() {
            return HostManager.BASE_HOST + "/anonymous/share/album/shareurl/refuse";
        }

        public static String getExitShareUrl() {
            return HostManager.BASE_HOST + "/user/share/album/sharer/delete";
        }

        public static String getDeleteSharerUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/sharer/delete", str);
        }

        public static String getRequestUserInfoUrl() {
            return HostManager.BASE_HOST + "/user/profile/basic";
        }

        public static String getDeviceShareUrl() {
            return HostManager.BASE_HOST + "/user/share/device/type";
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerAlbum {
        public static String getCreateAlbumUrl() {
            return HostManager.BASE_HOST + "/user/full/album";
        }

        public static String getDeleteAlbumUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/delete", str);
        }

        public static String getRenameAlbumUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s", str);
        }

        public static String getThumbnailInfoUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/album/%s/thumbnail", str);
        }

        public static String getEditAlbumUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s", str);
        }
    }

    /* loaded from: classes.dex */
    public static class ShareAlbum {
        public static String getEditAlbumUrl() {
            return HostManager.BASE_HOST + "/user/share/album/relation";
        }

        public static String getThumbnailInfoUrl() {
            return HostManager.BASE_HOST + "/user/share/album/thumbnail";
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerMedia {
        public static String getCommitUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s/storage", str);
        }

        public static String getRequestThumbnailUrl() {
            return HostManager.BASE_HOST + "/user/thumbnails";
        }

        public static String getUpdateUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s", str);
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerImage extends OwnerMedia {
        public static String getCreateUrl() {
            return HostManager.BASE_HOST + "/user/full";
        }

        public static String getDownloadUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s/storage", str);
        }

        public static String getCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/copy", str);
        }

        public static String getDeleteUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s/delete", str);
        }

        public static String getMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/move", str);
        }

        public static String getCreateSubUbiUrl(String str, int i) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s/subimage/%s", str, Integer.valueOf(i));
        }

        public static String getCommitSubUbiUrl(String str, int i) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s/subimage/%s/storage", str, Integer.valueOf(i));
        }

        public static String getEditUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/%s", str);
        }

        public static String getHideMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/hide/move", str);
        }

        public static String getUnHideMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/unhide/move", str);
        }

        public static String getHideCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/hide/copy", str);
        }

        public static String getUnHideCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/unhide/copy", str);
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerVideo extends OwnerMedia {
        public static String getCreateUrl() {
            return HostManager.BASE_HOST + "/user/full/video";
        }

        public static String getDownloadUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/storage", str);
        }

        public static String getCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/copy", str);
        }

        public static String getDeleteUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/delete", str);
        }

        public static String getMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/move", str);
        }

        public static String getHideMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/hide/move", str);
        }

        public static String getUnHideMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/unhide/move", str);
        }

        public static String getHideCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/hide/copy", str);
        }

        public static String getUnHideCopyUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/unhide/copy", str);
        }

        public static String getPlayInfoUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/playinfo", str);
        }
    }

    /* loaded from: classes.dex */
    public static class ShareMedia {
        public static String getCommitUrl() {
            return HostManager.BASE_HOST + "/user/share/storage";
        }

        public static String getRequestThumbnailUrl() {
            return HostManager.BASE_HOST + "/user/share/thumbnails";
        }
    }

    /* loaded from: classes.dex */
    public static class ShareImage extends ShareMedia {
        public static String getCreateUrl() {
            return HostManager.BASE_HOST + "/user/share/album";
        }

        public static String getDownloadUrl() {
            return HostManager.BASE_HOST + "/user/share/storage";
        }

        public static String getCopyUrl() {
            return HostManager.BASE_HOST + "/user/share/copy";
        }

        public static String getDeleteUrl() {
            return HostManager.BASE_HOST + "/user/share/delete";
        }

        public static String getMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/image/%s/move", str);
        }

        public static String getCreateSubUbiUrl(int i) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/share/subimage/%s", Integer.valueOf(i));
        }

        public static String getCommitSubUbiUrl(int i) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/share/subimage/%s/storage", Integer.valueOf(i));
        }
    }

    /* loaded from: classes.dex */
    public static class ShareVideo extends ShareMedia {
        public static String getCreateUrl() {
            return HostManager.BASE_HOST + "/user/share/album/video";
        }

        public static String getDownloadUrl() {
            return HostManager.BASE_HOST + "/user/share/video/storage";
        }

        public static String getCopyUrl() {
            return HostManager.BASE_HOST + "/user/share/video/copy";
        }

        public static String getDeleteUrl() {
            return HostManager.BASE_HOST + "/user/share/video/delete";
        }

        public static String getMoveUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/video/%s/move", str);
        }
    }

    /* loaded from: classes.dex */
    public static class Upgrade {
        public static String getUpgradeUrl() {
            return HostManager.BASE_HOST + "/user/upgrade";
        }
    }

    /* loaded from: classes.dex */
    public static class SyncPull {
        public static String getPullOwnerAlbumUrl() {
            return HostManager.BASE_HOST + "/user/full/album_v2";
        }

        public static String getPullOwnerAllUrl() {
            return HostManager.BASE_HOST + "/user/full";
        }

        public static String getPullOwnerPrivateUrl() {
            return HostManager.BASE_HOST + "/user/full/hide";
        }

        public static String getPullOwnerShareUserUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s/sharer", str);
        }

        public static String getPullShareAll() {
            return HostManager.BASE_HOST + "/user/share/album/changes";
        }

        public static String getPullShareAlbumImage() {
            return HostManager.BASE_HOST + "/user/share/album";
        }

        public static String getPullShareUserUrl() {
            return HostManager.BASE_HOST + "/user/share/album/sharer";
        }
    }

    /* loaded from: classes.dex */
    public static class Baby {
        public static String getUpdateBabyInfoUrl(String str) {
            return HostManager.BASE_HOST + String.format(Locale.US, "/user/full/album/%s", str);
        }
    }

    /* loaded from: classes.dex */
    public static class PeopleFace {
        public static String getPeopleFaceSyncUrl() {
            return HostManager.BASE_FACE_HOST + "/user";
        }

        public static String getPeopleCreateUrl() {
            return HostManager.BASE_FACE_HOST + "/user/people/create";
        }

        public static String getPeopleIgnoreUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/people/%s/ignore", str);
        }

        public static String getPeopleMergeUrl() {
            return HostManager.BASE_FACE_HOST + "/user/people/merge";
        }

        public static String getPeopleRenameUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/people/%s/updatename", str);
        }

        public static String getPeopleRecoveryUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/people/%s/recovery", str);
        }

        public static String getPeopleRecommendUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/people/%s/recommend", str);
        }

        public static String getPeopleFeedBackUrl() {
            return HostManager.BASE_FACE_HOST + "/user/feedback";
        }

        public static String getFaceInfoSyncUrl() {
            return HostManager.BASE_FACE_HOST + "/user/faceinfo";
        }

        public static String getFaceDeleteUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/face/%s/delete", str);
        }

        public static String getFaceMoveUrl() {
            return HostManager.BASE_FACE_HOST + "/user/face/batchmove";
        }

        public static String getPeopleUpdateInfoUrl(String str) {
            return HostManager.BASE_FACE_HOST + String.format(Locale.US, "/user/people/%s/updateinfo", str);
        }
    }

    /* loaded from: classes.dex */
    public static class CloudControl {
        public static String getUrl() {
            return HostManager.BASE_HOST + "/user/modules";
        }

        public static String getAnonymousUrl() {
            return HostManager.BASE_ANONYMOUS_HOST + "/anonymous/policies";
        }
    }

    /* loaded from: classes.dex */
    public static class RecommendList {
        public static String getUrl() {
            return HostManager.BASE_HOST + "/user/recommends";
        }

        public static String getAnonymousUrl() {
            return HostManager.BASE_ANONYMOUS_HOST + "/anonymous/recommends";
        }
    }

    /* loaded from: classes.dex */
    public static class Search {
        public static String getSearchUrlHost() {
            return HostManager.BASE_SEARCH_HOST;
        }

        public static String getSearchAnonymousUrlHost() {
            return HostManager.BASE_SEARCH_ANONYMOUS_HOST;
        }

        public static String getSearchFeedbackUrlHost() {
            return HostManager.BASE_SEARCH_FEEDBACK_HOST;
        }
    }

    /* loaded from: classes.dex */
    public static class Setting {
        public static String getSyncUrl() {
            return HostManager.BASE_HOST + "/user/setting";
        }
    }

    /* loaded from: classes.dex */
    public static class Story {
        public static String getCardInfosUrl() {
            return HostManager.BASE_HOST + "/user/cardinfo";
        }

        public static String getCreateCardUrl() {
            return HostManager.BASE_HOST + "/user/cardinfo";
        }

        public static String getUpdateCardUrl() {
            return HostManager.BASE_HOST + "/user/cardinfo/update";
        }

        public static String getDeleteCardUrl() {
            return HostManager.BASE_HOST + "/user/cardinfo/delete";
        }

        public static String getOperationCardUrl() {
            return HostManager.BASE_HOST + "/user/operationcard";
        }

        public static String getOperationCardAnonymousUrl() {
            return HostManager.BASE_ANONYMOUS_HOST + "/anonymous/operationcard";
        }
    }

    /* loaded from: classes.dex */
    public static class CloudPrivacy {
        public static String getCloudPrivacyUrl() {
            return String.format(Locale.US, "%s?_locale=%s_%s", "https://i.mi.com/gallery/intro/h5", Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        }
    }

    /* loaded from: classes.dex */
    public static class TrashBin {
        public static final String getVipInfoUrl() {
            return HostManager.BASE_VIP_INFO_HOST + "/user/privilege/info";
        }

        public static final String getDeleteListUrl() {
            return HostManager.BASE_HOST + "/user/full/deleted/list";
        }

        public static final String getRecoveryUrl() {
            return HostManager.BASE_HOST + "/user/full/batchrecovery";
        }

        public static final String getPurgeUrl() {
            return HostManager.BASE_HOST + "/user/full/batchpurge";
        }
    }

    /* loaded from: classes.dex */
    public static class RiskControl {
        public static String getConfirmUrl() {
            return HostManager.BASE_HOST + "/user/data/risk/control/delete/confirm";
        }

        public static String getManageUrlFormat(String str) {
            String str2 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryH5() + "/gallery/security/warning?params=%s&_local=%s_%s";
            try {
                str = URLEncoder.encode(str, Keyczar.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return String.format(Locale.US, str2, str, Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        }
    }

    /* loaded from: classes.dex */
    public static class Market {
        public static final String getSignatureUrl(String str) {
            return String.format(Locale.US, HostManager.BASE_HOST + "/anonymous/sign?id=%s", str);
        }
    }

    /* loaded from: classes.dex */
    public static class Slim {
        public static final String getUpdateProfileUrl() {
            return HostManager.BASE_HOST + "/user/full/video/slim/updateprofile";
        }

        public static final String getVideoplayLevelUrl() {
            return HostManager.BASE_VIP_INFO_HOST + "/user/videoplay/level";
        }
    }
}
