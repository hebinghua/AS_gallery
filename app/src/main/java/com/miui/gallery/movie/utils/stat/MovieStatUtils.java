package com.miui.gallery.movie.utils.stat;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.stat.SamplingStatHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MovieStatUtils {
    public static void statEnter(int i, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(i));
        hashMap.put("from", z ? "story" : "movieAssetsNormal");
        SamplingStatHelper.recordCountEvent("movie", "movie_enter", hashMap);
    }

    public static void statDownload(MovieResource movieResource) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, movieResource.getStatTypeString());
        hashMap.put("name", movieResource.getStatNameString());
        SamplingStatHelper.recordCountEvent("movie", "movie_download", hashMap);
    }

    public static void statDownloadResult(MovieResource movieResource, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, movieResource.getStatTypeString());
        hashMap.put("name", movieResource.getStatNameString());
        hashMap.put("result", z ? "0" : "-1");
        SamplingStatHelper.recordCountEvent("movie", "movie_download_result", hashMap);
    }

    public static void statItemChoose(MovieResource movieResource) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, movieResource.getStatTypeString());
        hashMap.put("name", movieResource.getStatNameString());
        SamplingStatHelper.recordCountEvent("movie", "movie_item_choose", hashMap);
    }

    public static void statSaveClick(boolean z, MovieInfo movieInfo) {
        HashMap hashMap = new HashMap();
        hashMap.put("page", z ? "preview" : "editor");
        hashMap.put("effect", movieInfo.template);
        hashMap.put("effect_extra", movieInfo.audio);
        SamplingStatHelper.recordCountEvent("movie", "movie_save_click", hashMap);
    }

    public static void statSaveResult(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("result", str);
        SamplingStatHelper.recordCountEvent("movie", "movie_save_result", hashMap);
    }

    public static void statShareClick(MovieInfo movieInfo) {
        HashMap hashMap = new HashMap();
        hashMap.put("effect", movieInfo.template);
        hashMap.put("effect_extra", movieInfo.audio);
        SamplingStatHelper.recordCountEvent("movie", "movie_share_click", hashMap);
    }

    public static void statDurationClick(MovieInfo movieInfo, boolean z, boolean z2) {
        HashMap hashMap = new HashMap();
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(movieInfo.imageList.size()));
        hashMap.put("page", z ? "preview" : "editor");
        hashMap.put("total_time", z2 ? "short" : "long");
        SamplingStatHelper.recordCountEvent("movie", "movie_duration_click", hashMap);
    }

    public static void statEditorMove(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.valueOf(i));
        SamplingStatHelper.recordCountEvent("movie", "movie_editor_move", hashMap);
    }

    public static void statPreviewEnterEditPage(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("total_time", z ? "short" : "long");
        SamplingStatHelper.recordCountEvent("movie", "movie_editor_preview_edit_click", hashMap);
    }

    public static void statPreviewPlayBtn(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("total_time", z ? "short" : "long");
        SamplingStatHelper.recordCountEvent("movie", "movie_editor_preview_play_click", hashMap);
    }
}
