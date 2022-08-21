package com.miui.gallery.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.request.HostManager;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class SettingsSearchProvider extends ContentProvider {
    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    /* loaded from: classes2.dex */
    public static class RawData {
        public String intentAction;
        public String intentTargetPackage;
        public String keywords;
        public String title;
        public String uriString;

        public RawData(String str, String str2, String str3, String str4, String str5) {
            this.title = str;
            this.keywords = str2;
            this.intentAction = str3;
            this.intentTargetPackage = str4;
            this.uriString = str5;
        }
    }

    public List<RawData> prepareData() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.backup_automatically_title)).setKeywords(TextUtils.join(";", getContext().getResources().getStringArray(R.array.backup_automatically_key_words))).build());
        if (AIAlbumStatusHelper.useNewAIAlbumSwitch()) {
            linkedList.add(new Builder().setTitle(getContext().getString(R.string.ai_album_setting_title)).setKeywords(getContext().getString(R.string.ai_album_setting_key_word)).build());
        }
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.auto_download_media_title)).build());
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.download_type_title)).setKeywords(TextUtils.join(";", new String[]{getContext().getResources().getString(R.string.download_type_origin), getContext().getResources().getString(R.string.download_type_thumbnail)})).build());
        if (MediaFeatureManager.isImageFeatureCalculationEnable()) {
            linkedList.add(new Builder().setTitle(getContext().getString(R.string.image_selection_title)).build());
            if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                linkedList.add(new Builder().setTitle(getContext().getString(R.string.generate_story_title)).build());
            }
        }
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.album_show_local_only)).build());
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.show_hidden_album_title)).build());
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.slideshow_interval_title)).build());
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.slideshow_loop_title)).build());
        if (ApplicationHelper.isCloudTrashBinFeatureOpen()) {
            linkedList.add(new Builder().setTitle(getContext().getString(R.string.trash_bin)).setKeywords(getContext().getString(R.string.trash_bin_key_word)).setIntentAction("android.intent.action.VIEW").setUriString(HostManager.getTrashBinUrl()).build());
        }
        linkedList.add(new Builder().setTitle(getContext().getString(R.string.user_agreement4)).setIntentAction("android.intent.action.VIEW").setIntentTargetPackage("").setUriString(CtaAgreement.Licence.getPrivacyUrl()).build());
        return linkedList;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public String intentAction;
        public String intentTargetPackage;
        public String keywords;
        public String title;
        public String uriString;

        public Builder() {
            this.title = "";
            this.keywords = "";
            this.intentAction = "miui.intent.action.APP_SETTINGS";
            this.intentTargetPackage = "com.miui.gallery";
            this.uriString = "";
        }

        public Builder setTitle(String str) {
            this.title = str;
            return this;
        }

        public Builder setKeywords(String str) {
            this.keywords = str;
            return this;
        }

        public Builder setIntentAction(String str) {
            this.intentAction = str;
            return this;
        }

        public Builder setIntentTargetPackage(String str) {
            this.intentTargetPackage = str;
            return this;
        }

        public Builder setUriString(String str) {
            this.uriString = str;
            return this;
        }

        public RawData build() {
            return new RawData(this.title, this.keywords, this.intentAction, this.intentTargetPackage, this.uriString);
        }
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (!TextUtils.equals(getCallingPackage(), "com.android.settings")) {
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(SettingsSearchContract.SEARCH_RESULT_COLUMNS);
        for (RawData rawData : prepareData()) {
            matrixCursor.newRow().add("title", rawData.title).add("keywords", rawData.keywords).add("intentAction", rawData.intentAction).add("intentTargetPackage", rawData.intentTargetPackage).add("uriString", rawData.uriString);
        }
        return matrixCursor;
    }
}
