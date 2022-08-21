package com.miui.gallery.cloud.card;

import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.CardSyncInfo;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.card.exception.NoResultException;
import com.miui.gallery.cloud.card.model.CardCreateResult;
import com.miui.gallery.cloud.card.model.CardInfo;
import com.miui.gallery.cloud.card.network.CommonGalleryRequestHelper;
import com.miui.gallery.cloud.card.network.RequestArguments;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes.dex */
public class SyncCardToServer {
    public static /* synthetic */ void $r8$lambda$8yOdczp4n787DdtesUXtHIGVljc(long j, Card card) {
        lambda$sync$0(j, card);
    }

    public void sync() {
        List<Card> unsynchronizedCards = CardManager.getInstance().getUnsynchronizedCards(100);
        final long currentTimeMillis = System.currentTimeMillis();
        unsynchronizedCards.forEach(new Consumer() { // from class: com.miui.gallery.cloud.card.SyncCardToServer$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SyncCardToServer.$r8$lambda$8yOdczp4n787DdtesUXtHIGVljc(currentTimeMillis, (Card) obj);
            }
        });
        boolean z = false;
        if (BaseMiscUtil.isValid(unsynchronizedCards)) {
            for (Card card : unsynchronizedCards) {
                int localFlag = card.getLocalFlag();
                if (localFlag != 0) {
                    if (localFlag == 1) {
                        syncDeleteCard(card);
                    } else if (localFlag != 2) {
                        DefaultLogger.e("SyncCardToServer", "get local card flag error:" + card.getLocalFlag());
                    } else if (!syncModifyCard(card)) {
                        z = true;
                    }
                } else if (!syncCreateCard(card)) {
                    z = true;
                }
            }
        }
        GalleryPreferences.Assistant.setCardSyncDirty(z);
    }

    public static /* synthetic */ void lambda$sync$0(long j, Card card) {
        DefaultLogger.d("SyncCardToServer", "| Recommendation |time=%s,card=%s", Long.valueOf(j), card);
    }

    public final boolean syncCreateCard(Card card) {
        CardSyncInfo cardInfoFromCard = CardManager.getInstance().getCardInfoFromCard(card);
        if (cardInfoFromCard == null) {
            return false;
        }
        if (cardInfoFromCard.isEmptyCard()) {
            DefaultLogger.e("SyncCardToServer", "empty card when create to server");
            SamplingStatHelper.recordCountEvent("assistant", "empty_card");
            return true;
        }
        try {
            SamplingStatHelper.recordCountEvent("assistant", "assistant_sync_card_to_server");
            updateCard(card, (CardCreateResult) new CommonGalleryRequestHelper(new RequestArguments<CardCreateResult>() { // from class: com.miui.gallery.cloud.card.SyncCardToServer.1
                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public int getMethod() {
                    return 1002;
                }

                {
                    SyncCardToServer.this = this;
                }

                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public String getUrl() {
                    return HostManager.Story.getCreateCardUrl();
                }
            }).addParam("data", GsonUtils.toString(cardInfoFromCard)).executeSync());
        } catch (NoResultException e) {
            statFailEvent("createCard", e.toString());
            DefaultLogger.e("SyncCardToServer", "Post CreateCard failed, %s", e);
        } catch (RequestError e2) {
            statFailEvent("createCard", e2.toString());
            DefaultLogger.e("SyncCardToServer", "Post CreateCard failed, %s", e2);
        }
        return true;
    }

    public boolean syncModifyCard(Card card) {
        CardSyncInfo cardInfoFromCard = CardManager.getInstance().getCardInfoFromCard(card);
        if (cardInfoFromCard == null) {
            return false;
        }
        try {
            updateCard(card, (CardInfo) new CommonGalleryRequestHelper(new RequestArguments<CardInfo>() { // from class: com.miui.gallery.cloud.card.SyncCardToServer.2
                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public int getMethod() {
                    return 1002;
                }

                {
                    SyncCardToServer.this = this;
                }

                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public String getUrl() {
                    return HostManager.Story.getUpdateCardUrl();
                }
            }).addParam("data", GsonUtils.toString(cardInfoFromCard)).addParam("cardId", card.getServerId()).executeSync());
            return true;
        } catch (NoResultException e) {
            statFailEvent("modify", e.toString());
            DefaultLogger.e("SyncCardToServer", "Post ModifyCard failed, %s", e);
            return true;
        } catch (RequestError e2) {
            statFailEvent("modify", e2.toString());
            DefaultLogger.e("SyncCardToServer", "Post ModifyCard failed, %s", e2);
            return true;
        }
    }

    public void syncDeleteCard(Card card) {
        try {
            CardInfo cardInfo = (CardInfo) new CommonGalleryRequestHelper(new RequestArguments<CardInfo>() { // from class: com.miui.gallery.cloud.card.SyncCardToServer.3
                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public int getMethod() {
                    return 1002;
                }

                {
                    SyncCardToServer.this = this;
                }

                @Override // com.miui.gallery.cloud.card.network.RequestArguments
                public String getUrl() {
                    return HostManager.Story.getDeleteCardUrl();
                }
            }).addParam("cardId", card.getServerId()).executeSync();
            if (cardInfo != null && cardInfo.isStatusDelete()) {
                CardManager.getInstance().delete(card, false);
                CardManager.getInstance().recordCardDeleteReason("serverSynced");
            } else {
                DefaultLogger.e("SyncCardToServer", "Post DeleteCard failed!");
            }
        } catch (NoResultException e) {
            statFailEvent("delete", e.toString());
            DefaultLogger.e("SyncCardToServer", "Post DeleteCard failed, %s", e);
        } catch (RequestError e2) {
            statFailEvent("delete", e2.toString());
            DefaultLogger.e("SyncCardToServer", "Post DeleteCard failed, %s", e2);
        }
    }

    public final void updateCard(Card card, CardCreateResult cardCreateResult) {
        if (card == null || cardCreateResult == null || cardCreateResult.getGalleryCard() == null) {
            return;
        }
        if (cardCreateResult.isDuplicate()) {
            CardInfo galleryCard = cardCreateResult.getGalleryCard();
            List<Long> list = null;
            List<Long> mediaList = galleryCard.getMediaInfo() == null ? null : galleryCard.getMediaInfo().getMediaList();
            List<Long> allMediaList = galleryCard.getMediaInfo() == null ? null : galleryCard.getMediaInfo().getAllMediaList();
            if (allMediaList == null) {
                allMediaList = mediaList;
            }
            if (galleryCard.getMediaInfo() != null) {
                list = galleryCard.getMediaInfo().getCoverMediaList();
            }
            card.setUpdateTime(galleryCard.getUpdateTime());
            card.setTitle(galleryCard.getTitle());
            card.setDescription(galleryCard.getDescription());
            card.setCardExtraInfo((Card.CardExtraInfo) GsonUtils.fromJson(galleryCard.getExtraInfo(), (Class<Object>) Card.CardExtraInfo.class));
            card.setAllMediaSha1s(CardUtil.getSha1sByServerIds(allMediaList));
            card.setSelectedMediaSha1s(CardUtil.getSha1sByServerIds(mediaList), "cardCardFromServer");
            card.setCoverMediaFeatureItems(CardUtil.getCoverMediaItemsByServerIds(list));
            card.setScenarioId(galleryCard.getScenarioId());
            card.setServerId(galleryCard.getServerId());
            card.setServerTag(galleryCard.getTag());
            card.setCreateBy(!galleryCard.isAppCreate());
            card.setCreateTime(galleryCard.getSortTime());
            card.setUpdateTime(galleryCard.getUpdateTime());
        } else if (card.getServerTag() < cardCreateResult.getGalleryCard().getTag()) {
            CardInfo galleryCard2 = cardCreateResult.getGalleryCard();
            card.setServerId(galleryCard2.getServerId());
            card.setServerTag(galleryCard2.getTag());
            card.setUpdateTime(galleryCard2.getUpdateTime());
        }
        DefaultLogger.d("SyncCardToServer", "| Recommendation |updateCard:card=%s", card);
        CardManager.getInstance().update(card, false);
    }

    public final void updateCard(Card card, CardInfo cardInfo) {
        if (card == null || cardInfo == null || card.getServerTag() >= cardInfo.getTag()) {
            return;
        }
        DefaultLogger.d("SyncCardToServer", "updateCard after SyncCardToServer");
        card.setServerId(cardInfo.getServerId());
        card.setServerTag(cardInfo.getTag());
        card.setUpdateTime(cardInfo.getUpdateTime());
        CardManager.getInstance().update(card, false);
    }

    public final void statFailEvent(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("from", str);
        hashMap.put("reason", str2);
        hashMap.put("name", getClass().getSimpleName());
        SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
    }
}
