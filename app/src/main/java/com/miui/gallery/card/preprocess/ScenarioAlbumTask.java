package com.miui.gallery.card.preprocess;

import android.accounts.Account;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaFeatureItemDurationComparator;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.Scenario;
import com.miui.gallery.card.scenario.ScenarioTrigger;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.card.SyncCardFromServer;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.cloud.card.model.CardInfoList;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.micloudsdk.request.utils.CloudUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ScenarioAlbumTask extends ScenarioTask {

    /* loaded from: classes.dex */
    public enum CardResult {
        CREATED,
        INPUT_ERROR,
        HAVE_UNPROCESSED_IMAGES,
        DUPLICATED,
        NO_ENOUGH_IMAGE,
        IMAGE_PROCESS_FAIL,
        COVER_DUPLICATED,
        ASSISTANT_SELECT_MEDIA_DUPLICATE
    }

    public ScenarioAlbumTask(int i) {
        super(i);
    }

    @Override // com.miui.gallery.assistant.process.BaseImageTask
    public DownloadType onGetImageDownloadType() {
        return DownloadType.MICRO;
    }

    @Override // com.miui.gallery.card.preprocess.ScenarioTask
    public boolean onProcess(JSONObject jSONObject, long j) throws Exception {
        Record record = (Record) GalleryEntityManager.getInstance().find(Record.class, j);
        if (record == null) {
            return false;
        }
        if (isCancelled()) {
            DefaultLogger.d("ScenarioAlbumTask", "task is cancelled");
            return false;
        }
        if (GalleryPreferences.Sync.getPowerCanSync() || GalleryPreferences.Sync.getIsPlugged()) {
            DefaultLogger.d("ScenarioAlbumTask", "power meet requirements，start generate card");
            generateCard(jSONObject, null, record, true);
        } else {
            DefaultLogger.d("ScenarioAlbumTask", "power do not meet requirements，try generate card without processing images");
            if (generateCard(jSONObject, null, record, false) == CardResult.HAVE_UNPROCESSED_IMAGES) {
                DefaultLogger.d("ScenarioAlbumTask", "generate card without processing images failed , schedule charging task");
                ScenarioTask.post(10, String.valueOf(record.getRowId()), record.getRowId());
            }
        }
        return false;
    }

    public final void updateRecord(Record record, boolean z) {
        if (record != null) {
            record.setState(z ? 2 : 3);
            GalleryEntityManager.getInstance().update(record);
        }
    }

    public final void statScenarioCreateFailed() {
        HashMap hashMap = new HashMap();
        hashMap.put("reason", "No enough image");
        SamplingStatHelper.recordCountEvent("assistant", "assistant_card_create_failed", hashMap);
    }

    public CardResult generateCard(JSONObject jSONObject, Scenario scenario, Record record, boolean z) {
        List<MediaFeatureItem> list;
        int i;
        Iterator it;
        CardInfoList cardInfoList;
        if (record == null) {
            return CardResult.INPUT_ERROR;
        }
        Scenario scenarioById = scenario == null ? new ScenarioTrigger().getScenarioById(record.getScenarioId()) : scenario;
        int i2 = 0;
        if (scenarioById == null) {
            updateRecord(record, false);
            return CardResult.INPUT_ERROR;
        }
        int scenarioId = scenarioById.getScenarioId();
        DefaultLogger.d("ScenarioAlbumTask", "ScenarioAlbumTask %d begin!", Integer.valueOf(scenarioId));
        List<MediaFeatureItem> queryMediaItemByIds = BaseImageTask.queryMediaItemByIds(record.getMediaIds());
        if (!BaseMiscUtil.isValid(queryMediaItemByIds)) {
            updateRecord(record, false);
            printLogger("Generate card failed!", "generate card failed because of no media item found", scenarioById, null);
            statisticGenerateError(scenarioById.getScenarioId(), "generate card failed because of no media item found");
            statScenarioCreateFailed();
            return CardResult.INPUT_ERROR;
        }
        DefaultLogger.d("ScenarioAlbumTask", "mScenarioId=%d,the scenario media count=%s", Integer.valueOf(scenarioId), Integer.valueOf(queryMediaItemByIds.size()));
        List<MediaFeatureItem> unProcessedMediaFeatureItems = CardUtil.getUnProcessedMediaFeatureItems(queryMediaItemByIds);
        if (BaseMiscUtil.isValid(unProcessedMediaFeatureItems) && !z) {
            String str = "generated card failed,have unProcessedMediaFeatureItems=" + unProcessedMediaFeatureItems.size();
            printLogger("Generate card failed!", str, scenarioById, null);
            statisticGenerateError(scenarioById.getScenarioId(), str);
            return CardResult.HAVE_UNPROCESSED_IMAGES;
        } else if (processItems(jSONObject, unProcessedMediaFeatureItems, false, true)) {
            DefaultLogger.w("ScenarioAlbumTask", "mScenarioId=%d,process %d images success", Integer.valueOf(scenarioId), Integer.valueOf(unProcessedMediaFeatureItems.size()));
            List<MediaFeatureItem> unProcessedMediaSceneItems = CardUtil.getUnProcessedMediaSceneItems(queryMediaItemByIds);
            if (BaseMiscUtil.isValid(unProcessedMediaSceneItems) && !z) {
                String str2 = "generated card failed,have unProcessedMediaFeatureItems=" + unProcessedMediaSceneItems.size();
                printLogger("Generate card failed!", str2, scenarioById, null);
                statisticGenerateError(scenarioById.getScenarioId(), str2);
                return CardResult.HAVE_UNPROCESSED_IMAGES;
            }
            if (BaseMiscUtil.isValid(unProcessedMediaSceneItems)) {
                AnalyticFaceAndSceneManager.getInstance().getSceneTagListInBatch(unProcessedMediaSceneItems, false, false);
            }
            CardUtil.bindMediaFeatures(queryMediaItemByIds);
            CardUtil.bindMediaScene(queryMediaItemByIds);
            List<MediaFeatureItem> selectedImages = BaseImageTask.getSelectedImages(queryMediaItemByIds);
            int minSelectedImageCount = scenarioById.getMinSelectedImageCount();
            int maxSelectedImageCount = scenarioById.getMaxSelectedImageCount();
            if (selectedImages == null || selectedImages.size() < minSelectedImageCount) {
                String str3 = "generated card failed,no enough selected Images from row images,selectSize=" + (selectedImages == 0 ? 0 : selectedImages.size());
                printLogger("Generate card failed!", str3, scenarioById, null);
                updateRecord(record, false);
                statisticGenerateError(scenarioById.getScenarioId(), str3);
                statScenarioCreateFailed();
                return CardResult.NO_ENOUGH_IMAGE;
            }
            if (selectedImages.size() > maxSelectedImageCount) {
                List arrayList = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                for (MediaFeatureItem mediaFeatureItem : selectedImages) {
                    if (mediaFeatureItem.isVideo()) {
                        arrayList.add(mediaFeatureItem);
                    } else {
                        arrayList2.add(mediaFeatureItem);
                    }
                }
                if (arrayList.size() > 40) {
                    DefaultLogger.d("ScenarioAlbumTask", "mScenarioId=%d,videos are more than 40,so sort by duration", Integer.valueOf(scenarioId));
                    Collections.sort(arrayList, new MediaFeatureItemDurationComparator());
                    arrayList = arrayList.subList(0, 40);
                }
                Collections.sort(arrayList2);
                selectedImages.clear();
                selectedImages.addAll(arrayList);
                selectedImages.addAll(arrayList2);
                while (selectedImages.size() > maxSelectedImageCount) {
                    selectedImages.remove(maxSelectedImageCount);
                }
            }
            double d = SearchStatUtils.POW;
            int i3 = 0;
            for (MediaFeatureItem mediaFeatureItem2 : selectedImages) {
                if (mediaFeatureItem2.isVideo()) {
                    i3++;
                }
                MediaFeature mediaFeature = mediaFeatureItem2.getMediaFeature();
                if (mediaFeature != null) {
                    d += mediaFeature.getScore();
                }
            }
            boolean z2 = i3 >= 4;
            int size = selectedImages.size();
            double d2 = d / size;
            ArrayList arrayList3 = new ArrayList(size);
            for (int i4 = 0; i4 < size; i4++) {
                arrayList3.add(Long.valueOf(selectedImages.get(i4).getMediaId()));
            }
            DefaultLogger.d("ScenarioAlbumTask", "mScenarioId=%d,the generated card result selectId contains %s", Integer.valueOf(scenarioId), TextUtils.join(",", arrayList3));
            Uri albumUri = CardUtil.getAlbumUri("album");
            Card build = new Card.Builder(GalleryApp.sGetAndroidContext()).setTitle(scenarioById.generateTitle(record, selectedImages)).setDescription(scenarioById.generateDescription(record, selectedImages)).setDeletable(scenarioById.isDeletable()).setType(0).setImageUri(null).setDetailUrl(albumUri == null ? null : albumUri.toString()).setUniqueKey(record.getUniqueKey()).setAllMediaSha1s(CardUtil.getSha1sFromImages(queryMediaItemByIds)).setSelectedMediaSha1s(CardUtil.getSha1sFromImages(selectedImages)).setCoverMediaFeatureItems(CardUtil.getCardCovers(selectedImages)).setScenarioId(record.getScenarioId()).setCreateBy(0).setValidStartTime(0L).setValidEndTime(Long.MAX_VALUE).setShowVideo(z2).setDisplayStatus("hidden").setPriority(scenarioById.getPriority()).setScore(d2).build();
            List<Card> query = GalleryEntityManager.getInstance().query(Card.class, "ignored = 0 AND scenarioId = " + record.getScenarioId() + " AND createTime > " + (System.currentTimeMillis() - CoreConstants.MILLIS_IN_ONE_WEEK), null, "createTime desc", null);
            if (BaseMiscUtil.isValid(query)) {
                for (Card card : query) {
                    if (CardUtil.isDuplicated(build, card)) {
                        updateRecord(record, false);
                        printLogger("Generate card failed!", "generate card failed because of existing the duplicate local card", scenarioById, null);
                        statisticGenerateError(scenarioById.getScenarioId(), "generate card failed because of existing the duplicate local card");
                        return CardResult.DUPLICATED;
                    }
                }
            }
            Account xiaomiAccount = CloudUtils.getXiaomiAccount();
            if (xiaomiAccount != null && SyncUtil.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext()) && (cardInfoList = new SyncCardFromServer(xiaomiAccount).getCardInfoList(GalleryCloudSyncTagUtils.getCardSyncTag(xiaomiAccount), GalleryCloudSyncTagUtils.getCardSyncInfo(xiaomiAccount), 10L)) != null) {
                ArrayList<CardInfo> galleryCards = cardInfoList.getGalleryCards();
                if (BaseMiscUtil.isValid(galleryCards)) {
                    for (CardInfo cardInfo : galleryCards) {
                        if (cardInfo != null && !cardInfo.isStatusDelete() && CardUtil.isDuplicated(build, cardInfo)) {
                            updateRecord(record, false);
                            SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(68L).build());
                            printLogger("Generate card failed!", "generated card failed,create a local card duplicated with server", scenarioById, null);
                            statisticGenerateError(scenarioById.getScenarioId(), "generated card failed,create a local card duplicated with server");
                            SamplingStatHelper.recordCountEvent("assistant", "request_sync_card_scenario");
                            return CardResult.DUPLICATED;
                        }
                    }
                }
            }
            List query2 = GalleryEntityManager.getInstance().query(Card.class, Card.BASE_USABLE_CARD_SELECTION, null, "createTime desc", String.format(Locale.US, "%s,%s", 0, 15));
            if (!BaseMiscUtil.isValid(build.getCoverMediaFeatureItems())) {
                return CardResult.INPUT_ERROR;
            }
            ArrayList arrayList4 = new ArrayList(build.getCoverMediaFeatureItems());
            ArrayList arrayList5 = new ArrayList();
            HashSet hashSet = new HashSet(build.getSelectedMediaSha1s());
            int size2 = (int) (hashSet.size() * 0.8f);
            if (BaseMiscUtil.isValid(query2)) {
                Iterator it2 = query2.iterator();
                int i5 = 0;
                int i6 = 0;
                while (true) {
                    if (!it2.hasNext()) {
                        list = selectedImages;
                        i = i6;
                        break;
                    }
                    Card card2 = (Card) it2.next();
                    List<MediaFeatureItem> coverMediaFeatureItems = card2.getCoverMediaFeatureItems();
                    MediaFeatureItem mediaFeatureItem3 = BaseMiscUtil.isValid(coverMediaFeatureItems) ? coverMediaFeatureItems.get(i2) : null;
                    List<String> selectedMediaSha1s = card2.getSelectedMediaSha1s();
                    if (i5 < 6 && mediaFeatureItem3 != null) {
                        Iterator it3 = arrayList4.iterator();
                        while (it3.hasNext()) {
                            it = it2;
                            MediaFeatureItem mediaFeatureItem4 = (MediaFeatureItem) it3.next();
                            if (mediaFeatureItem4 == null) {
                                it3.remove();
                                list = selectedImages;
                                break;
                            }
                            list = selectedImages;
                            if (TextUtils.equals(mediaFeatureItem4.getSha1(), mediaFeatureItem3.getSha1())) {
                                arrayList5.add(mediaFeatureItem4);
                                it3.remove();
                                break;
                            }
                            it2 = it;
                            selectedImages = list;
                        }
                    }
                    list = selectedImages;
                    it = it2;
                    if (arrayList4.size() == 0) {
                        i = 0;
                        break;
                    }
                    i = 0;
                    for (String str4 : selectedMediaSha1s) {
                        if (hashSet.contains(str4)) {
                            i++;
                        }
                    }
                    if (i >= size2) {
                        break;
                    }
                    i5++;
                    it2 = it;
                    selectedImages = list;
                    i6 = i;
                    i2 = 0;
                }
                if (arrayList4.size() == 0) {
                    printLogger("Generate card failed!", "generated card failed,cover images duplicate", scenarioById, null);
                    statisticGenerateError(scenarioById.getScenarioId(), "generated card failed,cover images duplicate");
                    SamplingStatHelper.recordCountEvent("assistant", "assistant_card_cover_duplicate");
                    return CardResult.COVER_DUPLICATED;
                }
                arrayList4.addAll(arrayList5);
                build.setCoverMediaFeatureItems(arrayList4);
                if (i >= size2) {
                    printLogger("Generate card failed!", "generated card failed,select images duplicate", scenarioById, null);
                    statisticGenerateError(scenarioById.getScenarioId(), "generated card failed,select images duplicate");
                    SamplingStatHelper.recordCountEvent("assistant", "assistant_select_media_duplicate");
                    return CardResult.ASSISTANT_SELECT_MEDIA_DUPLICATE;
                }
            } else {
                list = selectedImages;
            }
            CardManager.getInstance().add(build, true);
            printLogger("Generate card success!", null, scenarioById, list);
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.38.0.1.16473");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(scenarioById.getScenarioId()));
            long lastCardCreateTime = CardUtil.getLastCardCreateTime();
            if (lastCardCreateTime > 0) {
                hashMap.put("value", String.valueOf(GalleryDateUtils.daysBetween(lastCardCreateTime, System.currentTimeMillis())));
            }
            TrackController.trackStats(hashMap);
            updateRecord(record, true);
            return CardResult.CREATED;
        } else {
            printLogger("Generate card failed!", "generated card failed because of processing images failed", scenarioById, null);
            statisticGenerateError(scenarioById.getScenarioId(), "generated card failed because of processing images failed");
            return CardResult.IMAGE_PROCESS_FAIL;
        }
    }

    public final void printLogger(String str, String str2, Scenario scenario, List<MediaFeatureItem> list) {
        String str3 = BaseMiscUtil.isValid(list) ? (String) list.stream().map(ScenarioAlbumTask$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.joining(",")) : "";
        Object[] objArr = new Object[5];
        int i = 0;
        objArr[0] = str;
        objArr[1] = Integer.valueOf(scenario.getScenarioId());
        objArr[2] = str3;
        if (list != null) {
            i = list.size();
        }
        objArr[3] = Integer.valueOf(i);
        objArr[4] = str2;
        DefaultLogger.fd("ScenarioAlbumTask", "| Recommendation |%s |scenarioId:%s| |selectIds:%s| |number:%s| |cause:%s|", objArr);
    }

    public final void statisticGenerateError(int i, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.38.0.1.16471");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(i));
        hashMap.put("error", str);
        TrackController.trackError(hashMap);
    }
}
