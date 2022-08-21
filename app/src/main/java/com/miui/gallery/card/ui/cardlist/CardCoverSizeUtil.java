package com.miui.gallery.card.ui.cardlist;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CardCoverSizeUtil {
    public static final int[] FIRST_STAGE_MODEL = {0, 1, 2, 0, 0, 1, 2, 0, 0, 1, 2, 1, 2};
    public static final int[] SECOND_STAGE_MODEL = {0, 1, 2, 0};
    public static final int[] FIRST_STAGE_MODEL_PAD = {1, 7, 7, 2, 5, 5, 3, 2};
    public static final int[] SECOND_STAGE_MODEL_PAD = {1, 7, 7, 2};
    public static final int[] ONE_REMAIN_STAGE_MODEL_PAD = {0};
    public static final int[] TWO_REMAIN_STAGE_MODEL_PAD = {8, 9};
    public static final int[] THREE_REMAIN_STAGE_MODEL_PAD = {4, 6};

    public static CoverModel getCoverModel(List<Card> list) {
        if (isPadModel()) {
            return new PadCoverModel(list);
        }
        return new CoverModel(list);
    }

    public static int getSpanCount() {
        return isPadModel() ? 6 : 2;
    }

    public static boolean isPadModel() {
        return GalleryApp.sGetAndroidContext().getResources().getInteger(R.integer.assistant_page_model) == 2;
    }

    /* loaded from: classes.dex */
    public static class PadCoverModel extends CoverModel {
        public int[] mRemainStage;

        public PadCoverModel(List<Card> list) {
            super(list);
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil.CoverModel
        public void initBaseParam(int i) {
            this.mTotalCount = i;
            int length = i % CardCoverSizeUtil.FIRST_STAGE_MODEL_PAD.length;
            this.mFirstStageRemain = length;
            int length2 = length % CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD.length;
            this.mSecondStageRemain = length2;
            if (length2 == 1) {
                this.mRemainStage = CardCoverSizeUtil.ONE_REMAIN_STAGE_MODEL_PAD;
            } else if (length2 == 2) {
                this.mRemainStage = CardCoverSizeUtil.TWO_REMAIN_STAGE_MODEL_PAD;
            } else if (length2 != 3) {
            } else {
                this.mRemainStage = CardCoverSizeUtil.THREE_REMAIN_STAGE_MODEL_PAD;
            }
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil.CoverModel
        public CoverItemInfo getCoverItemInfo(int i) {
            int i2;
            int i3 = this.mFirstStageRemain;
            int i4 = (this.mTotalCount - 1) - i3;
            if (i <= i4) {
                i2 = CardCoverSizeUtil.FIRST_STAGE_MODEL_PAD[i % CardCoverSizeUtil.FIRST_STAGE_MODEL_PAD.length];
            } else {
                int i5 = i - (i4 + 1);
                if (i3 <= this.mSecondStageRemain || i5 >= CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD.length) {
                    if (i5 > CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD.length) {
                        i5 -= CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD.length;
                    }
                    if (i5 >= 0) {
                        int[] iArr = this.mRemainStage;
                        if (i5 < iArr.length) {
                            i2 = iArr[i5];
                        }
                    }
                    i2 = 0;
                } else {
                    i2 = CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD[i5 % CardCoverSizeUtil.SECOND_STAGE_MODEL_PAD.length];
                }
            }
            return getCoverItemInfoByStage(i2);
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil.CoverModel
        public CoverItemInfo getCoverItemInfoByStage(int i) {
            int i2 = 2;
            int i3 = 1;
            switch (i) {
                case 0:
                    i2 = 6;
                    break;
                case 1:
                case 2:
                case 3:
                    break;
                case 4:
                    i2 = 4;
                    break;
                case 5:
                case 6:
                case 7:
                    i3 = 2;
                    break;
                case 8:
                case 9:
                    i2 = 3;
                    break;
                default:
                    i2 = 0;
                    break;
            }
            CoverItemInfo coverItemInfo = new CoverItemInfo(i2);
            coverItemInfo.mCoverSize = i3;
            return coverItemInfo;
        }
    }

    /* loaded from: classes.dex */
    public static class CoverModel {
        public List<CardData> mCardData;
        public int mFirstStageRemain;
        public int mSecondStageRemain;
        public int mTotalCount;

        public CoverModel(List<Card> list) {
            init(list);
        }

        public void init(List<Card> list) {
            initBaseParam(BaseMiscUtil.isValid(list) ? list.size() : 0);
            initCardData(list);
        }

        public void initBaseParam(int i) {
            this.mTotalCount = i;
            int length = i % CardCoverSizeUtil.FIRST_STAGE_MODEL.length;
            this.mFirstStageRemain = length;
            this.mSecondStageRemain = length % CardCoverSizeUtil.SECOND_STAGE_MODEL.length;
        }

        public void initCardData(List<Card> list) {
            this.mCardData = new ArrayList();
            if (BaseMiscUtil.isValid(list)) {
                int i = 0;
                while (i < list.size()) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(list.get(i));
                    CoverItemInfo coverItemInfo = getCoverItemInfo(i);
                    for (int i2 = 1; i2 < coverItemInfo.mCoverSize; i2++) {
                        i++;
                        if (i < list.size()) {
                            arrayList.add(list.get(i));
                        }
                    }
                    this.mCardData.add(new CardData(coverItemInfo, arrayList));
                    i++;
                }
            }
        }

        public CoverItemInfo getCoverItemInfo(int i) {
            int i2;
            int i3 = 0;
            if (this.mTotalCount > 4 && i >= (i2 = this.mSecondStageRemain)) {
                int i4 = this.mFirstStageRemain;
                i3 = i < i4 ? CardCoverSizeUtil.SECOND_STAGE_MODEL[(i - i2) % CardCoverSizeUtil.SECOND_STAGE_MODEL.length] : CardCoverSizeUtil.FIRST_STAGE_MODEL[(i - i4) % CardCoverSizeUtil.FIRST_STAGE_MODEL.length];
            }
            return getCoverItemInfoByStage(i3);
        }

        public CoverItemInfo getCoverItemInfoByStage(int i) {
            if (i == 0) {
                return new CoverItemInfo(2, true);
            }
            return new CoverItemInfo(1, false);
        }

        public List<CardData> getCardDataList() {
            return this.mCardData;
        }
    }

    /* loaded from: classes.dex */
    public static class CoverItemInfo {
        public int mCoverSize;
        public boolean mIsLargeCard;
        public int mSpanSize;

        public CoverItemInfo(int i) {
            this.mCoverSize = 1;
            this.mSpanSize = i;
            this.mIsLargeCard = true;
        }

        public CoverItemInfo(int i, boolean z) {
            this.mCoverSize = 1;
            this.mSpanSize = i;
            this.mIsLargeCard = z;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }

        public int getHeight(Context context) {
            int dimensionPixelSize;
            if (this.mIsLargeCard) {
                dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.card_cover_height_large);
            } else {
                dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.card_cover_height_small);
            }
            if (this.mCoverSize > 1) {
                int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.card_divider_height);
                int i = this.mCoverSize;
                return (dimensionPixelSize - (dimensionPixelSize2 * (i - 1))) / i;
            }
            return dimensionPixelSize;
        }

        public int getCoverTitleSize(Context context) {
            return this.mIsLargeCard ? context.getResources().getDimensionPixelSize(R.dimen.card_title_textsize_large) : context.getResources().getDimensionPixelSize(R.dimen.card_title_textsize_small);
        }

        public int getCoverSubTitleSize(Context context) {
            return this.mIsLargeCard ? context.getResources().getDimensionPixelSize(R.dimen.card_subtitle_textsize_large) : context.getResources().getDimensionPixelSize(R.dimen.card_subtitle_textsize_small);
        }
    }

    /* loaded from: classes.dex */
    public static class CardData {
        public List<Card> mCards;
        public CoverItemInfo mCoverItemInfo;

        public CardData(CoverItemInfo coverItemInfo, List<Card> list) {
            this.mCoverItemInfo = coverItemInfo;
            this.mCards = list;
        }

        public long getUniqueId() {
            if (!BaseMiscUtil.isValid(this.mCards)) {
                return -1L;
            }
            if (this.mCards.size() == 1) {
                return this.mCards.get(0).getRowId();
            }
            long j = 0;
            for (Card card : this.mCards) {
                j += card.getRowId();
            }
            return Long.MAX_VALUE - j;
        }

        public String getContentIdentifier() {
            String str = "";
            if (!BaseMiscUtil.isValid(this.mCards)) {
                return str;
            }
            if (this.mCards.size() == 1) {
                List<MediaFeatureItem> coverMediaFeatureItems = this.mCards.get(0).getCoverMediaFeatureItems();
                if (!BaseMiscUtil.isValid(coverMediaFeatureItems)) {
                    return null;
                }
                return coverMediaFeatureItems.get(0).getMediaSha1();
            }
            for (Card card : this.mCards) {
                List<MediaFeatureItem> coverMediaFeatureItems2 = card.getCoverMediaFeatureItems();
                str = str + (BaseMiscUtil.isValid(coverMediaFeatureItems2) ? coverMediaFeatureItems2.get(0).getMediaSha1() : null);
            }
            return str;
        }

        public boolean isDividerItem() {
            return this.mCoverItemInfo.mCoverSize > 1;
        }
    }
}
