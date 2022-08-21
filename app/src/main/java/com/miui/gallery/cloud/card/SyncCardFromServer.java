package com.miui.gallery.cloud.card;

import android.accounts.Account;
import android.text.TextUtils;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.RetryRequestHelper;
import com.miui.gallery.cloud.base.SyncTask;
import com.miui.gallery.cloud.card.exception.NoResultException;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.cloud.card.model.CardInfoList;
import com.miui.gallery.cloud.card.network.CommonGalleryRequestHelper;
import com.miui.gallery.cloud.card.network.RequestArguments;
import com.miui.gallery.cloud.card.network.ResponseCallback;
import com.miui.gallery.net.base.ErrorCode;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class SyncCardFromServer {
    public Account mAccount;

    /* loaded from: classes.dex */
    public interface OperationCardCallback {
        void onOperationCardGet(Card card);
    }

    public SyncCardFromServer(Account account) {
        this.mAccount = account;
    }

    public final GallerySyncResult sync() {
        long currentTimeMillis = System.currentTimeMillis();
        while (SyncConditionManager.check(0) != 2 && GalleryPreferences.Sync.getSyncCompletelyFinish()) {
            GallerySyncResult retryTask = RetryRequestHelper.retryTask(new SyncTask<CardInfoList>() { // from class: com.miui.gallery.cloud.card.SyncCardFromServer.1
                @Override // com.miui.gallery.cloud.base.SyncTask
                public String getIdentifier() {
                    return HostManager.Story.getCardInfosUrl();
                }

                @Override // com.miui.gallery.cloud.base.SyncTask
                public GallerySyncResult<CardInfoList> run() throws Exception {
                    long cardSyncTag = GalleryCloudSyncTagUtils.getCardSyncTag(SyncCardFromServer.this.mAccount);
                    SyncLogger.d("SyncCardFromServer", "sync story card from server, syncTag : %d", Long.valueOf(cardSyncTag));
                    SyncCardFromServer syncCardFromServer = SyncCardFromServer.this;
                    return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).setData(syncCardFromServer.getCardInfoList(cardSyncTag, GalleryCloudSyncTagUtils.getCardSyncInfo(syncCardFromServer.mAccount), 10L)).build();
                }
            });
            CardInfoList cardInfoList = (CardInfoList) retryTask.data;
            if (cardInfoList != null) {
                ArrayList<CardInfo> galleryCards = cardInfoList.getGalleryCards();
                if (BaseMiscUtil.isValid(galleryCards)) {
                    SyncLogger.d("SyncCardFromServer", "sync story card count : %d", Integer.valueOf(galleryCards.size()));
                    for (CardInfo cardInfo : galleryCards) {
                        CardManager.getInstance().updateCardFromServer(cardInfo);
                    }
                }
                SyncLogger.d("SyncCardFromServer", "sync story card from server, syncTag in result : %d", Long.valueOf(cardInfoList.getSyncTag()));
                GalleryCloudSyncTagUtils.setCardSyncTag(this.mAccount, cardInfoList.getSyncTag());
                GalleryCloudSyncTagUtils.setCardSyncInfo(this.mAccount, cardInfoList.getSyncExtraInfo());
                if (cardInfoList.isLastPage()) {
                    SyncLogger.d("SyncCardFromServer", "sync story card isLastPage true");
                    CardManager.getInstance().triggerGuaranteeScenarios(true);
                }
            }
            SyncLogger.d("SyncCardFromServer", "sync story card from server finish, cost time: " + (System.currentTimeMillis() - currentTimeMillis));
            if (retryTask.code != GallerySyncCode.OK) {
                HashMap hashMap = new HashMap();
                hashMap.put("name", getClass().getSimpleName());
                hashMap.put("result", retryTask.toString());
                SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
            }
            return retryTask;
        }
        SyncLogger.e("SyncCardFromServer", "SyncConditionManager check false, getSyncCompletelyFinish : %s", Boolean.valueOf(GalleryPreferences.Sync.getSyncCompletelyFinish()));
        return new GallerySyncResult.Builder().setCode(GallerySyncCode.CONDITION_INTERRUPTED).build();
    }

    public CardInfoList getCardInfoList(long j, String str, long j2) {
        CommonGalleryRequestHelper useCache = new CommonGalleryRequestHelper(new RequestArguments<CardInfoList>() { // from class: com.miui.gallery.cloud.card.SyncCardFromServer.2
            @Override // com.miui.gallery.cloud.card.network.RequestArguments
            public int getMethod() {
                return 1001;
            }

            @Override // com.miui.gallery.cloud.card.network.RequestArguments
            public String getUrl() {
                return HostManager.Story.getCardInfosUrl();
            }
        }).addParam("syncTag", Long.toString(j)).addParam("limit", Long.toString(j2)).addParam("syncExtraInfo", str).addParam("language", Locale.getDefault().toString()).addParam("displayStatus", "all").setUseCache(false);
        try {
            SamplingStatHelper.recordCountEvent("assistant", "assistant_sync_card_from_server");
            return (CardInfoList) useCache.executeSync();
        } catch (NoResultException e) {
            statFailEvent("getCard", e.toString());
            DefaultLogger.e("SyncCardFromServer", "Get getCardInfoList failed, %s", e);
            return null;
        } catch (RequestError e2) {
            statFailEvent("getCard", e2.toString());
            DefaultLogger.e("SyncCardFromServer", "Get getCardInfoList failed, %s", e2);
            return null;
        }
    }

    public final void statFailEvent(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("from", str);
        hashMap.put("reason", str2);
        hashMap.put("name", getClass().getSimpleName());
        SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
    }

    public void getOperationCards(final String str, final OperationCardCallback operationCardCallback) {
        final boolean z = this.mAccount != null;
        new CommonGalleryRequestHelper(new RequestArguments<CardInfoList>() { // from class: com.miui.gallery.cloud.card.SyncCardFromServer.3
            @Override // com.miui.gallery.cloud.card.network.RequestArguments
            public int getMethod() {
                return z ? 1001 : 0;
            }

            @Override // com.miui.gallery.cloud.card.network.RequestArguments
            public String getUrl() {
                return z ? HostManager.Story.getOperationCardUrl() : HostManager.Story.getOperationCardAnonymousUrl();
            }
        }).addParam("limit", Long.toString(10L)).addParam("cardId", str).setUseCache(false).execute(new ResponseCallback<CardInfoList>() { // from class: com.miui.gallery.cloud.card.SyncCardFromServer.4
            @Override // com.miui.gallery.cloud.card.network.ResponseCallback
            public void onResponseError(ErrorCode errorCode, String str2, Object obj) {
            }

            @Override // com.miui.gallery.cloud.card.network.ResponseCallback
            public void onResponse(CardInfoList cardInfoList) {
                if (cardInfoList != null) {
                    ArrayList<CardInfo> galleryCards = cardInfoList.getGalleryCards();
                    if (!BaseMiscUtil.isValid(galleryCards)) {
                        return;
                    }
                    for (CardInfo cardInfo : galleryCards) {
                        Card createOperationCardFromServer = CardManager.getInstance().createOperationCardFromServer(cardInfo);
                        if (createOperationCardFromServer != null && operationCardCallback != null && TextUtils.equals(str, createOperationCardFromServer.getServerId())) {
                            operationCardCallback.onOperationCardGet(createOperationCardFromServer);
                        }
                    }
                }
            }
        });
    }
}
