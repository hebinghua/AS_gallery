package com.nexstreaming.app.common.nexasset.store.json;

import java.util.List;

/* loaded from: classes3.dex */
public class AssetStoreAPIData {
    public static final int ASSET_ENV_DRAFT = 1;
    public static final int ASSET_ENV_PRODUCTION = 3;
    public static final int ASSET_ENV_STAGING = 2;

    /* loaded from: classes3.dex */
    public static class AssetInfo {
        public List<LangString> assetName;
        public String asset_filepath;
        public int asset_filesize;
        public String asset_id;
        public int asset_sversion;
        public int asset_version;
        public List<LangString> categoryName;
        public String category_aliasName;
        public int category_idx;
        public String categoryimagePath;
        public String categoryimagePath_on;
        public int default_flag;
        public String description;
        public long expire_time;
        public int idx;
        public int language_idx;
        public String log_date;
        public String priceType;
        public List<LangString> subcategoryName;
        public int subcategory_idx;
        public List<ThumbInfo> thumb;
        public String thumbnail_path;
        public String thumbnail_path_l;
        public String thumbnail_path_s;
        public String title;
        public int update_time;
        public String videoclip_path;
    }

    /* loaded from: classes3.dex */
    public static class AuthorizeOrRefreshResponse {
        public String access_token;
        public int expires_in;
        public int scope;
    }

    /* loaded from: classes3.dex */
    public static class AuthorizeRequest {
        public String app_name;
        public String app_ucode;
        public String app_uuid;
        public String app_version;
        public String application;
        public String client_id;
        public String client_info;
        public String edition;
        public String market_id;
        public int scope;
    }

    /* loaded from: classes3.dex */
    public static class CategoryInfo {
        public List<LangString> categoryName;
        public String category_aliasName;
        public int category_idx;
        public String category_url;
        public String imagePath;
        public String imagePath_on;
        public List<SubcategoryInfo> subcategory_info;
    }

    /* loaded from: classes3.dex */
    public static class GetCategoryInfoResponse {
        public List<CategoryInfo> objList;
    }

    /* loaded from: classes3.dex */
    public static class GetCategoryListResponse {
        public String log_date;
        public List<AssetInfo> objList;
    }

    /* loaded from: classes3.dex */
    public static class GetFeaturedAssetInfoResponse {
        public String log_date;
        public List<AssetInfo> objList;
    }

    /* loaded from: classes3.dex */
    public static class GetNewAssetList {
        public String log_date;
        public List<AssetInfo> objList;
    }

    /* loaded from: classes3.dex */
    public static class LangString {
        public int default_flag;
        public String language_code;
        public String string_desc;
        public String string_title;
    }

    /* loaded from: classes3.dex */
    public static class RefreshOrDeleteAuthRequest {
        public String access_token;
        public String app_ucode;
        public String application;
    }

    /* loaded from: classes3.dex */
    public static class StandardRequest {
        public String access_token;
        public String application;
        public String edition;
        public int env;
        public String language;
    }

    /* loaded from: classes3.dex */
    public static class StatusResponse {
        public int result;
    }

    /* loaded from: classes3.dex */
    public static class SubcategoryInfo {
        public List<LangString> subcategoryName;
        public String subcategory_aliasName;
        public int subcategory_idx;
    }

    /* loaded from: classes3.dex */
    public static class ThumbInfo {
        public int asset_idx;
        public String file_path;
        public int idx;
    }

    private AssetStoreAPIData() {
    }
}
