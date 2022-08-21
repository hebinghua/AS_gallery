package com.miui.gallery.editor.photo.app.filter.res;

import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.editor.photo.core.imports.filter.FilterItem;
import com.miui.gallery.net.fetch.FetchManager;
import com.miui.gallery.net.fetch.Request;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterResourceFetcher {
    public static final FilterResourceFetcher INSTANCE = new FilterResourceFetcher();
    public static HashMap<String, Long> sResIdMap;
    public List<Request> mRequestList = new ArrayList();

    static {
        HashMap<String, Long> hashMap = new HashMap<>();
        sResIdMap = hashMap;
        hashMap.put("general_softdrink", 13668896399032352L);
        sResIdMap.put("general_japanese", 13669596662726656L);
        sResIdMap.put("general_nature", 13669608787279904L);
        sResIdMap.put("general_pink", 13669620293500960L);
        sResIdMap.put("general_fairytale", 13669632645529600L);
        sResIdMap.put("general_lilt", 13669641757589568L);
        sResIdMap.put("general_rise", 13669661252190208L);
        sResIdMap.put("general_warm", 13669668779786240L);
        sResIdMap.put("general_df", 13669688156749856L);
        sResIdMap.put("general_blackwhite", 13669697291026432L);
        sResIdMap.put("general_classic", 13669708937166880L);
        sResIdMap.put("general_koizora", 13697002405888000L);
        sResIdMap.put("general_blackice", 13697025839136768L);
        sResIdMap.put("movie_quiet", 14106039966629920L);
        sResIdMap.put("movie_summer", 13669743377317888L);
        sResIdMap.put("movie_wonderful", 13669754606321664L);
        sResIdMap.put("movie_encounter", 13669765161091136L);
        sResIdMap.put("movie_fengyin", 13669773554024480L);
        sResIdMap.put("movie_lost", 13669787133018176L);
        sResIdMap.put("movie_nordic", 13669805067993120L);
        sResIdMap.put("movie_rome", 13669811073450048L);
        sResIdMap.put("popular_cyberpunk", 14105922756280384L);
        sResIdMap.put("popular_pearl", 16551118571372672L);
        sResIdMap.put("popular_navy", 16551120261677312L);
        sResIdMap.put("popular_french", 16240434131894304L);
        sResIdMap.put("popular_polaroid", 16240431454421248L);
        sResIdMap.put("popular_cyan", 16240427986976992L);
        sResIdMap.put("popular_pinellia", 16240423177748640L);
        sResIdMap.put("popular_wakakusa", 16139635000344608L);
        sResIdMap.put("popular_sunset", 16139580504604736L);
        sResIdMap.put("popular_freshfood", 16139632720347264L);
        sResIdMap.put("popular_summercloud", 15975441934319712L);
        sResIdMap.put("popular_youth", 15975450896302176L);
        sResIdMap.put("popular_windchime", 15975448058200256L);
        sResIdMap.put("popular_gingertea", 15975414849142976L);
        sResIdMap.put("popular_mango", 15837489222844512L);
        sResIdMap.put("popular_icesnow", 15837497912787008L);
        sResIdMap.put("popular_wintersum", 15521294825357536L);
        sResIdMap.put("popular_blackred", 15521311807963264L);
        sResIdMap.put("popular_blackglod", 15521320291008704L);
        sResIdMap.put("popular_humanity", 13896140968689696L);
        sResIdMap.put("popular_bluedream", 14105904980361248L);
        sResIdMap.put("popular_shallow_summer", 14292450539667520L);
        sResIdMap.put("popular_purple", 14105916855812096L);
        sResIdMap.put("popular_kamakura", 14337122680307776L);
        sResIdMap.put("popular_dream", 14337128609808384L);
        sResIdMap.put("popular_greenorange", 14786608902766752L);
        sResIdMap.put("popular_coldsmoke", 14786605573013504L);
        sResIdMap.put("popular_autumn", 14786602835837056L);
        sResIdMap.put("classic_vivid", 14138983684243520L);
        sResIdMap.put("classic_delicious", 13896052227833888L);
        sResIdMap.put("classic_film", 13896067636723776L);
        sResIdMap.put("classic_past", 15521334444490784L);
        sResIdMap.put("portrait_protist", 16241448460288064L);
        sResIdMap.put("portrait_holiday", 16241447001129056L);
        sResIdMap.put("portrait_light_oxygen", 16241445630574784L);
        sResIdMap.put("portrait_mint", 16241444533174528L);
        sResIdMap.put("portrait_pink_orange", 16241443042230304L);
        sResIdMap.put("portrait_mojito", 14106000376922112L);
        sResIdMap.put("portrait_mist", 13896080320299008L);
        sResIdMap.put("portrait_painting", 13896093694099456L);
        sResIdMap.put("portrait_greyish", 13896111895805952L);
        sResIdMap.put("texture_shadow", 14825015979278560L);
        sResIdMap.put("food_orangeflavor", 15565471918456896L);
        sResIdMap.put("movie_dustdream", 14786438857621664L);
        sResIdMap.put("movie_fantasy", 14786435664183424L);
        sResIdMap.put("movie_latin", 14786432854327296L);
        sResIdMap.put("movie_greenoranget&o", 14786431493800032L);
        sResIdMap.put("movie_mystery", 14786429383278688L);
        sResIdMap.put("movie_carmen", 14786442124066976L);
        sResIdMap.put("movie_bbp", 14786426618183840L);
        sResIdMap.put("travel_charlotte", 15521344740261888L);
        sResIdMap.put("travel_hill", 14786649003130976L);
    }

    public void checkFetch(final FragmentActivity fragmentActivity, final FilterItem filterItem, final Request.Listener listener) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("FilterResourceFetcher", "download sdk failed ,cta not allowed");
            AgreementsUtils.showUserAgreements(fragmentActivity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.editor.photo.app.filter.res.FilterResourceFetcher$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z) {
                    FilterResourceFetcher.this.lambda$checkFetch$0(fragmentActivity, filterItem, listener, z);
                }
            });
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.filter_sky_download_failed_msg);
            DefaultLogger.d("FilterResourceFetcher", "download sky data no network");
            listener.onFail();
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(fragmentActivity, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.editor.photo.app.filter.res.FilterResourceFetcher$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    FilterResourceFetcher.this.lambda$checkFetch$1(filterItem, listener, z, z2);
                }
            });
        } else {
            fetch(filterItem, listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFetch$0(FragmentActivity fragmentActivity, FilterItem filterItem, Request.Listener listener, boolean z) {
        if (z) {
            checkFetch(fragmentActivity, filterItem, listener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$checkFetch$1(FilterItem filterItem, Request.Listener listener, boolean z, boolean z2) {
        if (z) {
            fetch(filterItem, listener);
        } else {
            listener.onFail();
        }
    }

    public void fetch(FilterItem filterItem, Request.Listener listener) {
        if (filterItem == null || filterItem.getMaterialName() == null) {
            if (listener == null) {
                return;
            }
            listener.onFail();
            return;
        }
        FilterRequest filterRequest = new FilterRequest(filterItem.getMaterialName(), getResId(filterItem));
        filterRequest.setListener(listener);
        this.mRequestList.add(filterRequest);
        FetchManager.INSTANCE.enqueue(filterRequest);
    }

    public List<? extends FilterData> getDownloadStatusData(List<? extends FilterData> list) {
        addDownloadStatus(list);
        return list;
    }

    public final void addDownloadStatus(List<? extends FilterData> list) {
        for (FilterData filterData : list) {
            if (ResourceFileConfig.exist(filterData.getMaterialName(), getResId(filterData))) {
                filterData.state = 17;
            } else {
                filterData.state = 19;
            }
        }
    }

    public void cancelAll() {
        FetchManager.INSTANCE.cancel(this.mRequestList);
    }

    public static String getMaterialPath(FilterData filterData) {
        if (ResourceFileConfig.resItemDir(filterData.getMaterialName(), getResId(filterData)) == null) {
            return null;
        }
        return ResourceFileConfig.resItemDir(filterData.getMaterialName(), getResId(filterData)).getAbsolutePath() + File.separator + filterData.getMaterialName() + ".png";
    }

    public static long getResId(FilterData filterData) {
        Long l;
        if (filterData == null || (l = sResIdMap.get(filterData.getMaterialName())) == null) {
            return Long.MIN_VALUE;
        }
        return l.longValue();
    }

    /* loaded from: classes2.dex */
    public static class ResourceFileConfig {
        public static String sResPath = GalleryApp.sGetAndroidContext().getFilesDir().getPath() + File.separator + "filter_resource";

        public static boolean exist(String str, long j) {
            File resItemDir = resItemDir(str, j);
            if (resItemDir == null) {
                return false;
            }
            return resItemDir.exists();
        }

        public static void deleteHistoricVersion(String str) {
            File resItemBaseDir = resItemBaseDir(str);
            if (resItemBaseDir == null || !resItemBaseDir.exists()) {
                return;
            }
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(resItemBaseDir.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("FilterResourceFetcher", "deleteHistoricVersion"));
            if (documentFile == null) {
                return;
            }
            documentFile.delete();
        }

        public static File resItemBaseDir(String str) {
            if (str == null) {
                return null;
            }
            return new File(sResPath, str);
        }

        public static File resItemDir(String str, long j) {
            if (resItemBaseDir(str) == null) {
                return null;
            }
            return new File(resItemBaseDir(str), String.valueOf(j));
        }

        public static File resItemZipFile(String str) {
            String str2 = sResPath;
            return new File(str2, str + ".zip");
        }
    }
}
