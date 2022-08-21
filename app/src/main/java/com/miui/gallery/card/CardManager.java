package com.miui.gallery.card;

import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.spi.ComponentTracker;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.model.MediaFeature;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.assistant.model.MediaScene;
import com.miui.gallery.assistant.process.BaseImageTask;
import com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneTask;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.card.scenario.Record;
import com.miui.gallery.card.scenario.ScenarioConstants;
import com.miui.gallery.card.scenario.ScenarioTrigger;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.AssistantScenarioStrategy;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.movie.utils.MovieBackgroundDownloadManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.reddot.DisplayStatusManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public class CardManager {
    public static final String[] CONVERT_PROJECTION = {"serverId", "sha1"};
    public static CardManager sInstance;
    public CardCache mCardCache;
    public final CardObserverHolder mObserverHolder = new CardObserverHolder();

    /* loaded from: classes.dex */
    public interface CardObserver {
        void onCardAdded(int i, Card card);

        void onCardDeleted(int i, Card card);

        void onCardUpdated(int i, Card card);

        void onCardsToShow(List<Card> list);
    }

    public static /* synthetic */ boolean $r8$lambda$BtJRQJj2sVuUWza3RZ7JHODUI5s(Set set, Long l) {
        return set.contains(l);
    }

    public static /* synthetic */ void $r8$lambda$g3jSI2IYIQXEkY39QPIuCiQeGNU(CardManager cardManager, List list) {
        cardManager.lambda$selectToShowCards$12(list);
    }

    public static /* synthetic */ boolean $r8$lambda$iHgAOHEEyctJrkYFtiy7Aije2Zs(List list, Long l) {
        return list.contains(l);
    }

    public static synchronized CardManager getInstance() {
        CardManager cardManager;
        synchronized (CardManager.class) {
            if (sInstance == null) {
                sInstance = new CardManager();
            }
            cardManager = sInstance;
        }
        return cardManager;
    }

    public void registerObserver(CardObserver cardObserver) {
        this.mObserverHolder.observers.add(cardObserver);
    }

    public void unregisterObserver(CardObserver cardObserver) {
        this.mObserverHolder.observers.remove(cardObserver);
    }

    public synchronized void add(Card card, boolean z) {
        if (card != null) {
            if (!card.isEmpty()) {
                if (z) {
                    card.setLocalFlag(0);
                    long currentTimeMillis = DateUtils.getCurrentTimeMillis();
                    card.setCreateTime(currentTimeMillis);
                    card.setUpdateTime(currentTimeMillis);
                } else if (card.isIgnored()) {
                    card.setLocalFlag(2);
                } else if (card.getLocalFlag() != 4) {
                    card.setLocalFlag(3);
                }
                addInternal(card);
                GalleryPreferences.Assistant.setHasCardEver();
            }
        }
    }

    public final synchronized void addInternal(final Card card) {
        if (card == null) {
            return;
        }
        boolean z = true;
        if (getCardByServerId(card.getServerId(), true) != null) {
            DefaultLogger.e("CardManager", "Card %s exist in db, do not insert again!", card.getServerId());
            return;
        }
        if (-1 == GalleryEntityManager.getInstance().insert(card)) {
            z = false;
        }
        if (z) {
            DisplayStatusManager.updateFeature("story_album");
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.card.CardManager.1
                {
                    CardManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    int addCard;
                    if (CardManager.this.mCardCache == null || card.isIgnored() || !card.isValid() || card.isOutOfDate() || card.isBackup() || (addCard = CardManager.this.mCardCache.addCard(card)) < 0) {
                        return;
                    }
                    CardManager.this.mObserverHolder.onCardAdded(addCard, card);
                    GalleryWidgetUtils.updateRecommendWidgetStatus("story_change_add", card.getRowId());
                }
            });
        }
        DefaultLogger.d("CardManager", "add result %s", Boolean.valueOf(z));
    }

    public synchronized void delete(Card card, boolean z) {
        if (card == null) {
            return;
        }
        DefaultLogger.d("CardManager", "delete card %s", Long.valueOf(card.getRowId()));
        if (z) {
            if (card.getLocalFlag() != 0 && card.isSyncable()) {
                card.setUpdateTime(System.currentTimeMillis());
                card.setLocalFlag(1);
                updateInternal(card);
                SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(68L).build());
                SamplingStatHelper.recordCountEvent("assistant", "request_sync_card_delete");
            }
            deleteInternal(card, false);
        } else {
            deleteInternal(card, false);
        }
    }

    public final synchronized void deleteInternal(final Card card, boolean z) {
        boolean z2;
        if (card == null) {
            return;
        }
        if (z) {
            z2 = GalleryEntityManager.getInstance().delete(card);
        } else {
            card.setLocalFlag(4);
            z2 = GalleryEntityManager.getInstance().update(card) > 0;
        }
        if (z2) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.card.CardManager.2
                {
                    CardManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    int findIndexOfCard;
                    if (CardManager.this.mCardCache == null || (findIndexOfCard = CardManager.this.mCardCache.findIndexOfCard(card.getRowId())) < 0) {
                        return;
                    }
                    CardManager.this.mCardCache.removeCard(card.getRowId());
                    CardManager.this.mObserverHolder.onCardDeleted(findIndexOfCard, card);
                    GalleryWidgetUtils.updateRecommendWidgetStatus("story_change_delete", card.getRowId());
                }
            });
        }
        DefaultLogger.d("CardManager", "delete result %s", Boolean.valueOf(z2));
    }

    public void updateCard(final Card card, boolean z) {
        if (card != null) {
            if (z) {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.card.CardManager$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public final Object mo1807run(ThreadPool.JobContext jobContext) {
                        Object lambda$updateCard$0;
                        lambda$updateCard$0 = CardManager.this.lambda$updateCard$0(card, jobContext);
                        return lambda$updateCard$0;
                    }
                });
            } else {
                update(card, true);
            }
        }
    }

    public /* synthetic */ Object lambda$updateCard$0(Card card, ThreadPool.JobContext jobContext) {
        update(card, true);
        return null;
    }

    public synchronized void update(Card card, boolean z) {
        if (card == null) {
            return;
        }
        DefaultLogger.d("CardManager", "Update card id: %s,By local: %b", Long.valueOf(card.getRowId()), Boolean.valueOf(z));
        if (card.isEmpty()) {
            delete(card, true);
            recordCardDeleteReason("updateCardEmpty");
        } else if (card.getRowId() < 0) {
            DefaultLogger.e("CardManager", "Update a card with no card Id!");
        } else {
            if (z) {
                card.setUpdateTime(System.currentTimeMillis());
                if (card.getLocalFlag() != 0) {
                    card.setLocalFlag(2);
                }
            } else {
                Card cardByServerId = getCardByServerId(card.getServerId(), true);
                if (cardByServerId != null && cardByServerId.getRowId() != card.getRowId() && cardByServerId.isValid()) {
                    deleteInternal(card, true);
                    recordCardDeleteReason("localExistSameCard");
                    return;
                }
                card.setLocalFlag(3);
            }
            updateCardCoversIfNecessary(card);
            updateInternal(card);
            if (z) {
                SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(68L).build());
                SamplingStatHelper.recordCountEvent("assistant", "request_sync_card_update");
            }
        }
    }

    public final synchronized void updateInternal(final Card card) {
        if (card == null) {
            return;
        }
        boolean z = GalleryEntityManager.getInstance().update(card) > 0;
        if (z) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.card.CardManager.3
                {
                    CardManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    int findIndexOfCard;
                    if (CardManager.this.mCardCache == null || (findIndexOfCard = CardManager.this.mCardCache.findIndexOfCard(card.getRowId())) < 0) {
                        return;
                    }
                    if (card.isLocalDeleted()) {
                        CardManager.this.mCardCache.removeCard(card.getRowId());
                        CardManager.this.mObserverHolder.onCardDeleted(findIndexOfCard, card);
                        GalleryWidgetUtils.updateRecommendWidgetStatus("story_change_delete", card.getRowId());
                        return;
                    }
                    CardManager.this.mCardCache.updateCard(card);
                    CardManager.this.mObserverHolder.onCardUpdated(CardManager.this.mCardCache.findIndexOfCard(card.getRowId()), card);
                    GalleryWidgetUtils.updateRecommendWidgetStatus("story_change_update", card.getRowId());
                }
            });
        }
        DefaultLogger.d("CardManager", "update result %s", Boolean.valueOf(z));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01e0 A[Catch: all -> 0x0285, TryCatch #1 {, blocks: (B:63:0x0001, B:65:0x001f, B:67:0x0050, B:69:0x006b, B:70:0x0071, B:72:0x00ad, B:74:0x00b3, B:75:0x00bc, B:85:0x0108, B:87:0x010e, B:90:0x0166, B:96:0x0179, B:98:0x0199, B:105:0x01d5, B:107:0x01e0, B:109:0x0201, B:110:0x0220, B:94:0x0171, B:81:0x00d7, B:83:0x00f4, B:71:0x00a6, B:84:0x00fd), top: B:118:0x0001, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:109:0x0201 A[Catch: all -> 0x0285, TryCatch #1 {, blocks: (B:63:0x0001, B:65:0x001f, B:67:0x0050, B:69:0x006b, B:70:0x0071, B:72:0x00ad, B:74:0x00b3, B:75:0x00bc, B:85:0x0108, B:87:0x010e, B:90:0x0166, B:96:0x0179, B:98:0x0199, B:105:0x01d5, B:107:0x01e0, B:109:0x0201, B:110:0x0220, B:94:0x0171, B:81:0x00d7, B:83:0x00f4, B:71:0x00a6, B:84:0x00fd), top: B:118:0x0001, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0199 A[Catch: all -> 0x0285, TryCatch #1 {, blocks: (B:63:0x0001, B:65:0x001f, B:67:0x0050, B:69:0x006b, B:70:0x0071, B:72:0x00ad, B:74:0x00b3, B:75:0x00bc, B:85:0x0108, B:87:0x010e, B:90:0x0166, B:96:0x0179, B:98:0x0199, B:105:0x01d5, B:107:0x01e0, B:109:0x0201, B:110:0x0220, B:94:0x0171, B:81:0x00d7, B:83:0x00f4, B:71:0x00a6, B:84:0x00fd), top: B:118:0x0001, inners: #0 }] */
    /* JADX WARN: Type inference failed for: r11v53, types: [java.util.List] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void selectToShowCards(final long r11) {
        /*
            Method dump skipped, instructions count: 648
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.card.CardManager.selectToShowCards(long):void");
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$1(long j, Card card) {
        return card.getCreateTime() > j && card.getCreateTime() < System.currentTimeMillis();
    }

    public static /* synthetic */ String lambda$selectToShowCards$3(Card card) {
        return String.valueOf(card.getScenarioId());
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$4(long j, Card card) {
        int timeType = card.getTimeType();
        if (timeType != 1400) {
            if (timeType != 1500) {
                if (timeType != 1700) {
                    if (timeType != 1800) {
                        if (timeType != 1900) {
                            if (timeType == 2000) {
                                return j == 21;
                            } else if (timeType != 2100) {
                                if (timeType != 2200) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            return j == 14;
        }
        return j == 7;
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$6(Card card) {
        return card.getLocalFlag() != 0;
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$7(Card card) {
        return card.getLocalFlag() == 0;
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$8(Card card) {
        return card.getLocalFlag() != 0;
    }

    public static /* synthetic */ void lambda$selectToShowCards$9(long j, Card card) {
        DefaultLogger.d("CardManager", "| Recommendation |select Cards,has Sync:time=%s,card=%s", Long.valueOf(j), card);
    }

    public static /* synthetic */ boolean lambda$selectToShowCards$10(Card card) {
        return card.getLocalFlag() == 0;
    }

    public static /* synthetic */ void lambda$selectToShowCards$11(long j, Card card) {
        DefaultLogger.d("CardManager", "| Recommendation |select Cards,has unSync:time=%s,card=%s", Long.valueOf(j), card);
    }

    public /* synthetic */ void lambda$selectToShowCards$12(List list) {
        CardCache cardCache = this.mCardCache;
        if (cardCache != null) {
            cardCache.addCards(list);
        }
        this.mObserverHolder.onCardsToShow(list);
        DefaultLogger.d("CardManager", "| Recommendation |page card generation completed!");
    }

    public static /* synthetic */ void lambda$selectToShowCards$13(Card card) {
        Object[] objArr = new Object[7];
        int i = 0;
        objArr[0] = DateUtils.getDateStamp(card.getCreateTime());
        objArr[1] = card.getTitle();
        objArr[2] = Integer.valueOf(card.getScenarioId());
        objArr[3] = Integer.valueOf(card.getPriority());
        objArr[4] = Double.valueOf(card.getScore());
        if (card.getSelectedMediaSha1s() != null) {
            i = card.getSelectedMediaSha1s().size();
        }
        objArr[5] = Integer.valueOf(i);
        objArr[6] = card.getSelectedMediaSha1s();
        DefaultLogger.d("CardManager", " | Recommendation | card displayed on the day|time:%s| |cardTitle:%s| |scenarioId:%s| |priority:%s| |score:%s| |number:%s| |mediaSha1:%s|", objArr);
    }

    public static /* synthetic */ String lambda$selectToShowCards$14(Card card) {
        return String.valueOf(card.getScenarioId());
    }

    public final void statisticTriggerError() {
        float f;
        List<MediaFeatureItem> queryMediaItem = BaseImageTask.queryMediaItem(ScenarioConstants.IMAGE_SCENARIO_SELECTION);
        long j = 0;
        float f2 = 1.0f;
        if (BaseMiscUtil.isValid(queryMediaItem)) {
            final List<Long> allProcessedSuccessFeatureImages = BaseImageTask.getAllProcessedSuccessFeatureImages();
            f = ((float) (BaseMiscUtil.isValid(allProcessedSuccessFeatureImages) ? queryMediaItem.stream().map(CardManager$$ExternalSyntheticLambda9.INSTANCE).filter(new Predicate() { // from class: com.miui.gallery.card.CardManager$$ExternalSyntheticLambda13
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return CardManager.$r8$lambda$iHgAOHEEyctJrkYFtiy7Aije2Zs(allProcessedSuccessFeatureImages, (Long) obj);
                }
            }).count() : 0L)) / queryMediaItem.size();
        } else {
            f = 1.0f;
        }
        List<MediaFeatureItem> queryMediaItem2 = BaseImageTask.queryMediaItem(ScenarioConstants.ALL_MEDIA_SCENARIO_CALCULATION_SELECTION);
        if (BaseMiscUtil.isValid(queryMediaItem2)) {
            final Set<Long> allProcessedSuccessMedias = ExistAnalyticFaceAndSceneTask.getAllProcessedSuccessMedias();
            if (BaseMiscUtil.isValid(allProcessedSuccessMedias)) {
                j = queryMediaItem2.stream().map(CardManager$$ExternalSyntheticLambda9.INSTANCE).filter(new Predicate() { // from class: com.miui.gallery.card.CardManager$$ExternalSyntheticLambda14
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return CardManager.$r8$lambda$BtJRQJj2sVuUWza3RZ7JHODUI5s(allProcessedSuccessMedias, (Long) obj);
                    }
                }).count();
            }
            f2 = ((float) j) / queryMediaItem2.size();
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.38.0.1.18796");
        hashMap.put("value", Float.valueOf(f2));
        hashMap.put("description", Float.valueOf(f));
        TrackController.trackError(hashMap);
    }

    public synchronized Card findHasShowCard(long j) {
        CardCache cardCache = this.mCardCache;
        Card card = cardCache != null ? cardCache.getCard(j) : null;
        if (card != null) {
            return card;
        }
        Card card2 = (Card) GalleryEntityManager.getInstance().find(Card.class, Card.BASE_UI_CARD_SELECTION + " AND " + String.format(Locale.US, "%s = %s", j.c, Long.valueOf(j)), null);
        if (this.mCardCache == null) {
            this.mCardCache = new CardCache();
        }
        this.mCardCache.addCard(card2);
        return card2;
    }

    public List<Card> getLoadedCard() {
        return this.mCardCache;
    }

    public Card getCardByCardId(long j) {
        CardCache cardCache = this.mCardCache;
        if (cardCache != null) {
            return cardCache.getCard(j);
        }
        return null;
    }

    public synchronized List<Card> loadMoreCard() {
        List<Card> query;
        CardCache cardCache = this.mCardCache;
        query = GalleryEntityManager.getInstance().query(Card.class, Card.BASE_UI_CARD_SELECTION, null, "createTime desc", String.format(Locale.US, "%s,%s", Integer.valueOf(cardCache != null ? cardCache.size() : 0), 20));
        if (this.mCardCache == null) {
            this.mCardCache = new CardCache();
        }
        if (query != null && !query.isEmpty()) {
            this.mCardCache.addCards(query);
        }
        return query;
    }

    public synchronized void initCovers() {
        if (!GalleryPreferences.Assistant.getAssistantCoverUpdated()) {
            GalleryPreferences.Assistant.setAssistantCoverUpdated(true);
            List<Card> query = GalleryEntityManager.getInstance().query(Card.class, Card.BASE_UI_CARD_SELECTION, null, "createTime desc", null);
            if (BaseMiscUtil.isValid(query)) {
                ArrayList arrayList = new ArrayList(query.size());
                for (Card card : query) {
                    List<MediaFeatureItem> coverMediaFeatureItems = card.getCoverMediaFeatureItems();
                    if (BaseMiscUtil.isValid(coverMediaFeatureItems)) {
                        List<MediaFeatureItem> arrayList2 = new ArrayList<>(coverMediaFeatureItems);
                        ArrayList arrayList3 = new ArrayList();
                        Iterator<MediaFeatureItem> it = arrayList2.iterator();
                        boolean z = false;
                        while (it.hasNext()) {
                            MediaFeatureItem next = it.next();
                            if (next == null) {
                                it.remove();
                            } else {
                                for (int size = arrayList.size() - 1; size >= arrayList.size() - 6 && size >= 0; size--) {
                                    if (TextUtils.equals((CharSequence) arrayList.get(size), next.getSha1())) {
                                        DefaultLogger.d("CardManager", "cover dup index %d", Integer.valueOf(size));
                                        arrayList3.add(next);
                                        it.remove();
                                    }
                                }
                            }
                            z = true;
                        }
                        if (z && BaseMiscUtil.isValid(arrayList2)) {
                            DefaultLogger.d("CardManager", "update cover, id: %d", Long.valueOf(card.getRowId()));
                            arrayList2.addAll(arrayList3);
                            card.setCoverMediaFeatureItems(arrayList2);
                            arrayList.add(arrayList2.get(0).getSha1());
                            updateInternal(card);
                        } else {
                            arrayList.add(coverMediaFeatureItems.get(0).getSha1());
                        }
                    }
                }
            }
        }
    }

    public final synchronized boolean updateCardCoversIfNecessary(final Card card) {
        if (card.isCoversNeedRefresh()) {
            final List<String> selectedMediaSha1s = card.getSelectedMediaSha1s();
            if (selectedMediaSha1s != null && selectedMediaSha1s.size() > 0) {
                Uri build = GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build();
                SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), build, MediaFeatureItem.MEDIA_PROJECTION, "(localGroupId!=-1000) AND " + String.format("%s IN ('%s')", "sha1", TextUtils.join("','", selectedMediaSha1s)), (String[]) null, "alias_create_time DESC", new SafeDBUtil.QueryHandler<List<MediaFeatureItem>>() { // from class: com.miui.gallery.card.CardManager.4
                    {
                        CardManager.this = this;
                    }

                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public List<MediaFeatureItem> mo1808handle(Cursor cursor) {
                        if (cursor != null) {
                            Map mediaFeatureMap = CardManager.this.getMediaFeatureMap(selectedMediaSha1s);
                            ArrayList arrayList = new ArrayList(cursor.getCount());
                            ArrayList<MediaFeatureItem> arrayList2 = new ArrayList();
                            ArrayList arrayList3 = new ArrayList();
                            while (cursor.moveToNext()) {
                                try {
                                    MediaFeatureItem mediaFeatureItem = new MediaFeatureItem(cursor);
                                    arrayList.add(mediaFeatureItem.getSha1());
                                    mediaFeatureItem.setMediaFeature((MediaFeature) mediaFeatureMap.get(mediaFeatureItem.getSha1()));
                                    arrayList2.add(mediaFeatureItem);
                                    arrayList3.add(Long.valueOf(mediaFeatureItem.getId()));
                                } catch (Throwable th) {
                                    BaseMiscUtil.closeSilently(cursor);
                                    throw th;
                                }
                            }
                            if (BaseMiscUtil.isValid(arrayList2)) {
                                Map mediaSceneMap = CardManager.this.getMediaSceneMap(arrayList3);
                                for (MediaFeatureItem mediaFeatureItem2 : arrayList2) {
                                    mediaFeatureItem2.setMediaSceneResult((List) mediaSceneMap.get(Long.valueOf(mediaFeatureItem2.getId())));
                                }
                            }
                            BaseMiscUtil.closeSilently(cursor);
                            Collections.sort(arrayList2);
                            ArrayList arrayList4 = new ArrayList();
                            for (int i = 0; i < Math.min(5, arrayList2.size()); i++) {
                                arrayList4.add((MediaFeatureItem) arrayList2.get(i));
                            }
                            card.setSelectedMediaSha1s(arrayList, "updateCover");
                            if (CardUtil.isCoverMediaPathEmpty(arrayList4)) {
                                CardUtil.downloadCoverThumb(arrayList4);
                            }
                            card.setCoverMediaFeatureItems(arrayList4);
                            return null;
                        }
                        return null;
                    }
                });
            }
            return true;
        } else if (!CardUtil.isCoverMediaPathEmpty(card.getCoverMediaFeatureItems())) {
            return false;
        } else {
            List<MediaFeatureItem> coverMediaFeatureItems = card.getCoverMediaFeatureItems();
            CardUtil.downloadCoverThumb(coverMediaFeatureItems);
            card.setCoverMediaFeatureItems(coverMediaFeatureItems);
            return true;
        }
    }

    public synchronized void updateCardCovers() {
        List<Card> query = GalleryEntityManager.getInstance().query(Card.class, Card.BASE_UI_CARD_SELECTION, null, "createTime desc", null);
        if (BaseMiscUtil.isValid(query)) {
            for (Card card : query) {
                if (updateCardCoversIfNecessary(card)) {
                    updateInternal(card);
                }
            }
        }
    }

    public final Map<String, MediaFeature> getMediaFeatureMap(List<String> list) {
        HashMap hashMap = new HashMap();
        if (list != null) {
            List<MediaFeature> query = GalleryEntityManager.getInstance().query(MediaFeature.class, String.format("sha1 IN ('%s') ", TextUtils.join("','", list)), null, "score desc", null);
            if (BaseMiscUtil.isValid(query)) {
                for (MediaFeature mediaFeature : query) {
                    hashMap.put(mediaFeature.getSha1(), mediaFeature);
                }
            }
        }
        return hashMap;
    }

    public final Map<Long, List<MediaScene>> getMediaSceneMap(List<Long> list) {
        HashMap hashMap = new HashMap();
        if (list != null) {
            List<MediaScene> query = GalleryEntityManager.getInstance().query(MediaScene.class, String.format("mediaId IN ('%s') ", TextUtils.join("','", list)), null, null, null);
            if (BaseMiscUtil.isValid(query)) {
                for (MediaScene mediaScene : query) {
                    List list2 = (List) hashMap.get(Long.valueOf(mediaScene.getMediaId()));
                    if (list2 == null) {
                        list2 = new ArrayList();
                    }
                    list2.add(mediaScene);
                    hashMap.put(Long.valueOf(mediaScene.getMediaId()), list2);
                }
            }
        }
        return hashMap;
    }

    public synchronized void onAccountDelete() {
        DefaultLogger.d("CardManager", "onAccountDelete");
        try {
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            String format = String.format("%s IS NOT NULL AND  %s != ''", "serverId", "serverId");
            final List query = galleryEntityManager.query(Card.class, format, null, null, null);
            if (BaseMiscUtil.isValid(query)) {
                GalleryEntityManager.getInstance().delete(Card.class, format, null);
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.card.CardManager.5
                    {
                        CardManager.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (CardManager.this.mCardCache != null) {
                            for (Card card : query) {
                                int findIndexOfCard = CardManager.this.mCardCache.findIndexOfCard(card.getRowId());
                                if (findIndexOfCard >= 0) {
                                    CardManager.this.mCardCache.removeCard(card.getRowId());
                                    CardManager.this.mObserverHolder.onCardDeleted(findIndexOfCard, card);
                                    GalleryWidgetUtils.updateRecommendWidgetStatus("story_change_delete", card.getRowId());
                                }
                            }
                        }
                    }
                });
            }
            GalleryEntityManager.getInstance().deleteAll(SyncTag.class);
        } catch (Exception e) {
            DefaultLogger.e("CardManager", "onAccountDelete occur error.\n", e);
        }
    }

    public synchronized void triggerScenarios() {
        if (!MediaFeatureManager.isStoryGenerateEnable()) {
            DefaultLogger.d("CardManager", "Card funtion disable");
            return;
        }
        long lastTriggerTime = GalleryPreferences.Assistant.getLastTriggerTime();
        if (Math.abs(System.currentTimeMillis() - lastTriggerTime) < ComponentTracker.DEFAULT_TIMEOUT || DateUtils.getDateTime(lastTriggerTime) == DateUtils.getDateTime(System.currentTimeMillis())) {
            DefaultLogger.d("CardManager", "| Recommendation |triggerScenarios too often");
            return;
        }
        try {
            new ScenarioTrigger().trigger();
            GalleryPreferences.Assistant.setLastTriggerTime(System.currentTimeMillis());
        } catch (Exception e) {
            DefaultLogger.e("CardManager", "trigger scenario occur error.\n", e);
        }
    }

    public void triggerGuaranteeScenarios(final boolean z) {
        DefaultLogger.d("CardManager", "Try trigger Guarantee Scenario");
        if (!MediaFeatureManager.isStoryGenerateEnable()) {
            DefaultLogger.d("CardManager", "Card function disable");
            return;
        }
        long lastGuaranteeTriggerTime = GalleryPreferences.Assistant.getLastGuaranteeTriggerTime();
        if (DateUtils.getDaysBetween(GalleryPreferences.Assistant.getFirstTriggerTime(), System.currentTimeMillis()) < 5 || DateUtils.getDateTime(lastGuaranteeTriggerTime) == DateUtils.getDateTime(System.currentTimeMillis())) {
            DefaultLogger.d("CardManager", "triggerScenarios too often");
        } else {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.card.CardManager.6
                {
                    CardManager.this = this;
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Object mo1807run(ThreadPool.JobContext jobContext) {
                    CardManager.this.triggerGuaranteeScenariosInternal(z);
                    return null;
                }
            });
        }
    }

    public final synchronized void triggerGuaranteeScenariosInternal(boolean z) {
        if (BaseMiscUtil.isValid(GalleryEntityManager.getInstance().query(Card.class, "ignored = 0", null, null, String.format(Locale.US, "%s,%s", 0, 1)))) {
            DefaultLogger.d("CardManager", "Card exists,no need trigger guarantee card!");
            return;
        }
        try {
            new ScenarioTrigger().triggerGuaranteeScenario();
            if (z) {
                GalleryPreferences.Assistant.setLastGuaranteeTriggerTime(System.currentTimeMillis());
            }
        } catch (Exception e) {
            DefaultLogger.e("CardManager", "trigger scenario occur error.\n", e);
        }
    }

    public void updateCardFromServer(CardInfo cardInfo) {
        if (cardInfo == null) {
            return;
        }
        Card cardByServerId = getCardByServerId(cardInfo.getServerId(), true);
        if (cardByServerId == null) {
            Card duplicatedCard = getDuplicatedCard(cardInfo);
            if (duplicatedCard != null) {
                if (cardInfo.isStatusDelete()) {
                    delete(duplicatedCard, false);
                    recordCardDeleteReason("serverDeleteDupCard");
                    return;
                }
                mergeCardFromServer(duplicatedCard, cardInfo);
            } else if (!MediaFeatureManager.isStoryGenerateEnable()) {
            } else {
                createNewCardFromServer(cardInfo);
            }
        } else if (cardInfo.isStatusDelete()) {
            delete(cardByServerId, false);
            recordCardDeleteReason("serverDeleteExistCard");
        } else if (cardByServerId.isLocalDeleted()) {
        } else {
            updateCardFromServerInternal(cardByServerId, cardInfo);
        }
    }

    public Card createOperationCardFromServer(CardInfo cardInfo) {
        Card cardByServerId = getCardByServerId(cardInfo.getServerId(), true);
        return cardByServerId == null ? createNewCardFromServer(cardInfo) : cardByServerId;
    }

    public final void mergeCardFromServer(Card card, CardInfo cardInfo) {
        if (card == null || cardInfo == null) {
            return;
        }
        List<Long> list = null;
        List<Long> mediaList = cardInfo.getMediaInfo() == null ? null : cardInfo.getMediaInfo().getMediaList();
        if (cardInfo.getMediaInfo() != null) {
            list = cardInfo.getMediaInfo().getAllMediaList();
        }
        if (list == null) {
            list = mediaList;
        }
        card.setCreateTime(cardInfo.getSortTime());
        card.setUpdateTime(cardInfo.getUpdateTime());
        card.setCardExtraInfo((Card.CardExtraInfo) GsonUtils.fromJson(cardInfo.getExtraInfo(), (Class<Object>) Card.CardExtraInfo.class));
        card.setAllMediaSha1s(mergeSha1s(card.getAllMediaSha1s(), CardUtil.getSha1sByServerIds(list)));
        card.setSelectedMediaSha1s(mergeSha1s(card.getSelectedMediaSha1s(), CardUtil.getSha1sByServerIds(mediaList)), "mergeFromServerDupCard");
        card.setScenarioId(cardInfo.getScenarioId());
        card.setServerId(cardInfo.getServerId());
        card.setServerTag(cardInfo.getTag());
        card.setCreateBy(!cardInfo.isAppCreate());
        DefaultLogger.d("CardManager", "| Recommendation |mergeCardFromServer:card=%s", card);
        update(card, false);
    }

    public final List<String> mergeSha1s(List<String> list, List<String> list2) {
        if (BaseMiscUtil.isValid(list) && BaseMiscUtil.isValid(list2)) {
            for (String str : list2) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        return list;
    }

    public final Card getDuplicatedCard(CardInfo cardInfo) {
        if (cardInfo.isAppCreate()) {
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            List<Card> query = galleryEntityManager.query(Card.class, "ignored = 0 AND scenarioId = " + cardInfo.getScenarioId() + " AND localFlag = 0", null, "createTime desc", null);
            if (!BaseMiscUtil.isValid(query)) {
                return null;
            }
            for (Card card : query) {
                if (CardUtil.isDuplicated(card, cardInfo)) {
                    return card;
                }
            }
            return null;
        }
        return null;
    }

    public final Card createNewCardFromServer(final CardInfo cardInfo) {
        double d;
        boolean z;
        int i;
        Record.UniqueKey uniqueKey;
        AssistantScenarioStrategy assistantScenarioStrategy;
        if (cardInfo == null) {
            return null;
        }
        boolean z2 = cardInfo.getScenarioId() == 11000;
        Card.Builder top = new Card.Builder(StaticContext.sGetAndroidContext()).setTitle(cardInfo.getTitle()).setDescription(cardInfo.getDescription()).setDeletable(true).setScenarioId(cardInfo.getScenarioId()).setServerId(cardInfo.getServerId()).setServerTag(cardInfo.getTag()).setCreateBy(!cardInfo.isAppCreate()).setCreateTime(cardInfo.getSortTime()).setUpdateTime(cardInfo.getUpdateTime()).setValidStartTime(cardInfo.getValidStartDate()).setValidEndTime(cardInfo.getValidEndDate()).setTop(cardInfo.isTop());
        if (!z2) {
            Card.CardExtraInfo cardExtraInfo = (Card.CardExtraInfo) GsonUtils.fromJson(cardInfo.getExtraInfo(), (Class<Object>) Card.CardExtraInfo.class);
            String str = "";
            if (cardExtraInfo != null) {
                uniqueKey = cardExtraInfo.uniqueKey;
                z = cardExtraInfo.isIgnored;
                if (!TextUtils.isEmpty(cardExtraInfo.displayStatus)) {
                    str = cardExtraInfo.displayStatus;
                }
                i = cardExtraInfo.priority;
                d = cardExtraInfo.score;
            } else {
                d = 0.0d;
                z = false;
                i = 0;
                uniqueKey = null;
            }
            if (!z && !cardInfo.isAppCreate() && (assistantScenarioStrategy = CloudControlStrategyHelper.getAssistantScenarioStrategy()) != null) {
                List<AssistantScenarioStrategy.ScenarioRule> localScenarioRules = assistantScenarioStrategy.getLocalScenarioRules();
                if (BaseMiscUtil.isValid(localScenarioRules)) {
                    for (AssistantScenarioStrategy.ScenarioRule scenarioRule : localScenarioRules) {
                        if (scenarioRule.getScenarioId() == cardInfo.getScenarioId()) {
                            z = true;
                        }
                    }
                }
            }
            if (cardInfo.getMediaInfo() == null) {
                return null;
            }
            List<Long> mediaList = cardInfo.getMediaInfo().getMediaList();
            List<Long> allMediaList = cardInfo.getMediaInfo().getAllMediaList();
            if (allMediaList == null) {
                allMediaList = mediaList;
            }
            List<String> sha1sByServerIds = CardUtil.getSha1sByServerIds(mediaList);
            if (!BaseMiscUtil.isValid(sha1sByServerIds)) {
                return null;
            }
            boolean booleanValue = ((Boolean) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"serverType"}, String.format(Locale.US, "%s in ('%s')", "sha1", TextUtils.join("','", sha1sByServerIds)), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Boolean>() { // from class: com.miui.gallery.card.CardManager.7
                {
                    CardManager.this = this;
                }

                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Boolean mo1808handle(Cursor cursor) {
                    boolean z3 = false;
                    int i2 = 0;
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(0) == 2) {
                                i2++;
                            }
                        }
                    }
                    DefaultLogger.d("CardManager", "card name is %s,videoCount=%s", cardInfo.getTitle(), Integer.valueOf(i2));
                    if (i2 >= 4) {
                        z3 = true;
                    }
                    return Boolean.valueOf(z3);
                }
            })).booleanValue();
            DefaultLogger.d("CardManager", "card name is %s,isShowVideo=%s", cardInfo.getTitle(), Boolean.valueOf(booleanValue));
            top.setType(0).setImageUri(null).setDetailUrl(CardUtil.getAlbumUri("album").toString()).setUniqueKey(uniqueKey).setAllMediaSha1s(CardUtil.getSha1sByServerIds(allMediaList)).setSelectedMediaSha1s(sha1sByServerIds).setCoverMediaFeatureItems(CardUtil.getCoverMediaItemsByServerIds(cardInfo.getMediaInfo().getCoverMediaList())).setShowVideo(booleanValue).setSyncable(true).setIsIgnored(z).setDisplayStatus(str).setPriority(i).setScore(d);
        } else {
            top.setOperationInfo(cardInfo.getOperationInfo()).setDetailUrl(CardUtil.getAlbumUri("operation").toString()).setSyncable(false).setType(2);
        }
        Card build = top.build();
        DefaultLogger.d("CardManager", "| Recommendation | createNewCardFromServer:card=%s", build);
        updateCardCoversIfNecessary(build);
        if (cardInfo.isStatusDelete()) {
            build.setLocalFlag(4);
        }
        DefaultLogger.d("CardManager", "card name is %s,isShowVideo=%s", build.getTitle(), Boolean.valueOf(build.isShowVideo()));
        add(build, false);
        MovieBackgroundDownloadManager.getInstance().downloadTemplate(StaticContext.sGetAndroidContext(), CardUtil.getMovieTemplateFromCard(build));
        HashMap hashMap = new HashMap();
        if (build.getType() == 2) {
            hashMap.put("server_id", String.valueOf(build.getServerId()));
        }
        hashMap.put("scenario_id", String.valueOf(build.getScenarioId()));
        SamplingStatHelper.recordCountEvent("assistant", "assistant_card_server_card_created", hashMap);
        return build;
    }

    public final void updateCardFromServerInternal(Card card, CardInfo cardInfo) {
        if (cardInfo == null || card.getServerTag() >= cardInfo.getTag()) {
            return;
        }
        List<Long> list = null;
        List<Long> mediaList = cardInfo.getMediaInfo() == null ? null : cardInfo.getMediaInfo().getMediaList();
        List<Long> allMediaList = cardInfo.getMediaInfo() == null ? null : cardInfo.getMediaInfo().getAllMediaList();
        if (allMediaList == null) {
            allMediaList = mediaList;
        }
        if (cardInfo.getMediaInfo() != null) {
            list = cardInfo.getMediaInfo().getCoverMediaList();
        }
        card.setTitle(cardInfo.getTitle());
        card.setDescription(cardInfo.getDescription());
        card.setScenarioId(cardInfo.getScenarioId());
        card.setServerId(cardInfo.getServerId());
        card.setUpdateTime(cardInfo.getUpdateTime());
        card.setAllMediaSha1s(CardUtil.getSha1sByServerIds(allMediaList));
        card.setSelectedMediaSha1s(CardUtil.getSha1sByServerIds(mediaList), "updateCardFromServer");
        card.setCoverMediaFeatureItems(CardUtil.getCoverMediaItemsByServerIds(list));
        card.setServerTag(cardInfo.getTag());
        card.setCardExtraInfo((Card.CardExtraInfo) GsonUtils.fromJson(cardInfo.getExtraInfo(), (Class<Object>) Card.CardExtraInfo.class));
        DefaultLogger.d("CardManager", "| Recommendation |updateCardFromServerInternal:card=%s", card);
        update(card, false);
    }

    public Card getCardByServerId(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            String format = String.format("serverId = %s", str);
            if (!z) {
                format = format + " AND ignored = 0";
            }
            List query = galleryEntityManager.query(Card.class, format, null, null, null);
            if (!BaseMiscUtil.isValid(query)) {
                return null;
            }
            return (Card) query.get(0);
        }
        return null;
    }

    public List<Card> getUnsynchronizedCards(int i) {
        if (i <= 0) {
            i = 10;
        }
        return GalleryEntityManager.getInstance().query(Card.class, Card.BASE_UNSYNC_CARD_SELECTION, null, "createTime desc", String.format(Locale.US, "%s,%s", 0, Integer.valueOf(i)));
    }

    public CardSyncInfo getCardInfoFromCard(Card card) {
        List<ServerIdSha1Pair> serverIdSha1PairBySha1s = getServerIdSha1PairBySha1s(card.getSelectedMediaSha1s());
        List<ServerIdSha1Pair> serverIdSha1PairBySha1s2 = getServerIdSha1PairBySha1s(card.getAllMediaSha1s());
        if (!checkMediaServerId(serverIdSha1PairBySha1s) || !checkMediaServerId(serverIdSha1PairBySha1s2)) {
            return null;
        }
        return CardSyncInfo.newBuilder().setName(card.getTitle()).setScenarioId(card.getScenarioId()).setStatus("normal").setDuplicateKey(card.generateDuplicateKey()).setDescription(card.getDescription()).setMediaList(ServerIdSha1Pair.getServerIds(serverIdSha1PairBySha1s)).setAllMediaList(ServerIdSha1Pair.getServerIds(serverIdSha1PairBySha1s2)).setCoverMediaList(ServerIdSha1Pair.getServerIdsOfCover(serverIdSha1PairBySha1s, card.getCoverMediaFeatureItems())).setExtraInfo(GsonUtils.toString(card.getCardExtraInfo())).setSortTime(card.getCreateTime()).build();
    }

    public final boolean checkMediaServerId(List<ServerIdSha1Pair> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return true;
        }
        for (ServerIdSha1Pair serverIdSha1Pair : list) {
            if (!TextUtils.isEmpty(serverIdSha1Pair.sha1) && serverIdSha1Pair.serverId <= 0) {
                return false;
            }
        }
        return true;
    }

    public List<ServerIdSha1Pair> getServerIdSha1PairBySha1s(List<String> list) {
        return (List) SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), GalleryContract.Media.URI.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(true)).build(), CONVERT_PROJECTION, String.format("%s IN ('%s')", "sha1", TextUtils.join("','", list)), (String[]) null, String.format("%s DESC", "alias_create_time"), new SafeDBUtil.QueryHandler<List<ServerIdSha1Pair>>() { // from class: com.miui.gallery.card.CardManager.9
            {
                CardManager.this = this;
            }

            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public List<ServerIdSha1Pair> mo1808handle(Cursor cursor) {
                ArrayList arrayList = new ArrayList(cursor != null ? cursor.getCount() : 0);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        arrayList.add(new ServerIdSha1Pair(Long.valueOf(cursor.getLong(0)).longValue(), cursor.getString(1)));
                    }
                }
                return arrayList;
            }
        });
    }

    public void onDeleteImages(List<String> list) {
        if (BaseMiscUtil.isValid(list)) {
            long currentTimeMillis = System.currentTimeMillis();
            CardCache cardCache = this.mCardCache;
            if (cardCache != null) {
                Iterator<Card> it = cardCache.iterator();
                while (it.hasNext()) {
                    Card next = it.next();
                    if (next.removeImages(list)) {
                        if (next.isEmpty()) {
                            recordCardImageEmptyReason("ImageDeleteOutside");
                        }
                        update(next, true);
                    }
                    currentTimeMillis = next.getCreateTime();
                }
            }
            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
            List<Card> query = galleryEntityManager.query(Card.class, "ignored = 0 AND localFlag NOT IN (1,-2,-1,4) AND createTime<" + currentTimeMillis, null, "createTime desc", null);
            if (!BaseMiscUtil.isValid(query)) {
                return;
            }
            for (Card card : query) {
                if (card.removeImages(list)) {
                    if (card.isEmpty()) {
                        recordCardImageEmptyReason("ImageDeleteOutside");
                    }
                    update(card, true);
                }
            }
        }
    }

    public void recordCardImageEmptyReason(String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("from", str);
        StatHelper.recordCountEvent("assistant", "assistant_card_remove_all_image", hashMap);
        DefaultLogger.d("CardManager", Log.getStackTraceString(new Throwable()));
    }

    public void recordCardDeleteReason(String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("reason", str);
        StatHelper.recordCountEvent("assistant", "assistant_card_delete_card_reason", hashMap);
        DefaultLogger.d("CardManager", new Throwable());
    }

    /* loaded from: classes.dex */
    public static class CardCache extends CopyOnWriteArrayList<Card> {
        private CardCache() {
        }

        public synchronized Card getCard(long j) {
            Iterator<Card> it = iterator();
            while (it.hasNext()) {
                Card next = it.next();
                if (next.getRowId() == j) {
                    return next;
                }
            }
            return null;
        }

        public synchronized void removeCard(long j) {
            Iterator it = iterator();
            while (it.hasNext()) {
                Card card = (Card) it.next();
                if (card.getRowId() == j) {
                    remove(card);
                }
            }
        }

        public synchronized void updateCard(Card card) {
            int size = size();
            for (int i = 0; i < size; i++) {
                Card card2 = get(i);
                if (card2 != card && card2.getRowId() == card.getRowId()) {
                    card2.copyFrom(card);
                }
            }
        }

        public int findIndexOfCard(long j) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getRowId() == j) {
                    return i;
                }
            }
            return -1;
        }

        public int addCard(Card card) {
            if (contains(card)) {
                return -1;
            }
            int findIndexToInsert = findIndexToInsert(card);
            if (findIndexToInsert >= 0 && findIndexToInsert <= size()) {
                add(findIndexToInsert, card);
            }
            return findIndexToInsert;
        }

        public void addCards(Collection<? extends Card> collection) {
            if (BaseMiscUtil.isValid(collection)) {
                for (Card card : collection) {
                    if (!card.isIgnored()) {
                        addCard(card);
                    }
                }
            }
        }

        public final int findIndexToInsert(Card card) {
            if (card != null) {
                int i = 0;
                if (isEmpty()) {
                    return 0;
                }
                int size = size() - 1;
                while (i <= size) {
                    int i2 = (i + size) >>> 1;
                    int compareTo = get(i2).compareTo(card);
                    if (i == size) {
                        return compareTo < 0 ? i + 1 : compareTo > 0 ? size : i2;
                    } else if (compareTo < 0) {
                        i = i2 + 1;
                    } else if (compareTo <= 0) {
                        return i2;
                    } else {
                        size = i2 - 1;
                    }
                }
                return (get(i).compareTo(card) > 0 || i >= size()) ? i : i + 1;
            }
            return -1;
        }
    }

    /* loaded from: classes.dex */
    public static class CardObserverHolder implements CardObserver {
        public final CopyOnWriteArraySet<CardObserver> observers;

        public CardObserverHolder() {
            this.observers = new CopyOnWriteArraySet<>();
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardsToShow(List<Card> list) {
            Iterator<CardObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onCardsToShow(list);
            }
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardAdded(int i, Card card) {
            Iterator<CardObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onCardAdded(i, card);
            }
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardDeleted(int i, Card card) {
            Iterator<CardObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onCardDeleted(i, card);
            }
        }

        @Override // com.miui.gallery.card.CardManager.CardObserver
        public void onCardUpdated(int i, Card card) {
            Iterator<CardObserver> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onCardUpdated(i, card);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ServerIdSha1Pair {
        public final long serverId;
        public final String sha1;

        public ServerIdSha1Pair(long j, String str) {
            this.serverId = j;
            this.sha1 = str;
        }

        public static List<Long> getServerIds(List<ServerIdSha1Pair> list) {
            if (BaseMiscUtil.isValid(list)) {
                ArrayList arrayList = new ArrayList(list.size());
                for (ServerIdSha1Pair serverIdSha1Pair : list) {
                    arrayList.add(Long.valueOf(serverIdSha1Pair.serverId));
                }
                return arrayList;
            }
            return null;
        }

        public static List<Long> getServerIdsOfCover(List<ServerIdSha1Pair> list, List<MediaFeatureItem> list2) {
            if (!BaseMiscUtil.isValid(list) || !BaseMiscUtil.isValid(list2)) {
                return null;
            }
            ArrayList arrayList = new ArrayList(list2.size());
            for (MediaFeatureItem mediaFeatureItem : list2) {
                for (ServerIdSha1Pair serverIdSha1Pair : list) {
                    if (TextUtils.equals(mediaFeatureItem.getSha1(), serverIdSha1Pair.sha1)) {
                        arrayList.add(Long.valueOf(serverIdSha1Pair.serverId));
                    }
                }
            }
            return arrayList;
        }
    }
}
