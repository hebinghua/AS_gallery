package com.miui.gallery.card.ui.cardlist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.ui.cardlist.CardAdapter;
import com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil;
import com.miui.gallery.card.ui.detail.StoryAlbumActivity;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.CardCoverView;
import com.miui.gallery.widget.CardSlideView;
import com.miui.gallery.widget.DebounceClickListener;
import com.miui.gallery.widget.ICardView;
import com.miui.gallery.widget.RoundedFrameLayout;
import com.miui.gallery.widget.recyclerview.LifecycleAwareViewHolder;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final AppCompatActivity mContext;
    public CardCoverSizeUtil.CoverModel mCoverModel;
    public View mEmptyView;
    public View mFooterView;
    public AssistantPageFragment mFragment;
    public View mHeaderView;
    public Lifecycle mLifecycle;
    public View mTranView;
    public Bundle mTransitionInfo;
    public UserVisibleChangedListener mUserVisibleChangedListener;
    public boolean mHideFooterWhenEmpty = false;
    public boolean mHideHeaderWhenEmpty = false;
    public List<CardCoverSizeUtil.CardData> mDataList = null;
    public boolean mUserVisible = false;
    public final DataBinder mDataBinder = new DataBinder();
    public ClickCardViewHolder mCardViewHolder = new ClickCardViewHolder(this);

    /* loaded from: classes.dex */
    public interface UserVisibleChangedListener {
        void onUserVisibleChanged(boolean z);
    }

    public CardAdapter(AssistantPageFragment assistantPageFragment, Lifecycle lifecycle) {
        this.mFragment = assistantPageFragment;
        this.mContext = assistantPageFragment.getAppCompatActivity();
        this.mLifecycle = lifecycle;
    }

    public void updateDataList(List<Card> list) {
        CardCoverSizeUtil.CoverModel coverModel = CardCoverSizeUtil.getCoverModel(list);
        this.mCoverModel = coverModel;
        this.mDataList = coverModel.getCardDataList();
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
    }

    public void setFooter(View view, boolean z) {
        this.mFooterView = view;
        this.mHideFooterWhenEmpty = z;
    }

    public int getDataItemSize() {
        List<CardCoverSizeUtil.CardData> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public CardCoverSizeUtil.CoverModel getCoverModel() {
        return this.mCoverModel;
    }

    public CardCoverSizeUtil.CardData getDataItem(int i) {
        int i2;
        if ((!isHeaderVisible() || i != 0) && BaseMiscUtil.isValid(this.mDataList) && (i2 = i - (isHeaderVisible() ? 1 : 0)) < this.mDataList.size()) {
            return this.mDataList.get(i2);
        }
        return null;
    }

    public List<CardCoverSizeUtil.CardData> getDataList() {
        return this.mDataList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (!isHeaderVisible() || i != 0) {
            if (isHeaderVisible() && i == 1) {
                return 4;
            }
            if (getDataItemSize() == 0 && this.mEmptyView != null && i == isHeaderVisible()) {
                return 3;
            }
            if (isFooterVisible() && i == getItemCount() - 1) {
                return 2;
            }
            if (!isHeaderVisible() && i == 0) {
                return 4;
            }
            CardCoverSizeUtil.CardData dataItem = getDataItem(i);
            return (dataItem == null || !dataItem.isDividerItem()) ? 0 : 5;
        }
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        int dataItemSize = getDataItemSize();
        return dataItemSize == 0 ? (isHeaderVisible() ? 1 : 0) + 1 : dataItemSize + (isHeaderVisible() ? 1 : 0) + (isFooterVisible() ? 1 : 0);
    }

    public final boolean isHeaderVisible() {
        return this.mHeaderView != null && (getDataItemSize() != 0 || !this.mHideHeaderWhenEmpty);
    }

    public final boolean isFooterVisible() {
        return this.mFooterView != null && (getDataItemSize() != 0 || !this.mHideFooterWhenEmpty);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new ViewHolderImpl(this.mHeaderView);
        }
        if (i == 2) {
            return new ViewHolderImpl(this.mFooterView);
        }
        if (i == 3) {
            return new ViewHolderImpl(this.mEmptyView);
        }
        if (i == 4) {
            return new SlideCardHolder(new CardSlideView(this.mContext), this.mLifecycle);
        }
        if (i == 5) {
            return new DividerCardHolder(LayoutInflater.from(this.mContext).inflate(R.layout.card_divider_item_layout, viewGroup, false), this.mLifecycle);
        }
        return new CardHolder(LayoutInflater.from(this.mContext).inflate(R.layout.card_item_layout, viewGroup, false), this.mLifecycle);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0 || itemViewType == 4 || itemViewType == 5) {
            ((CardHolder) viewHolder).bindData(getDataItem(i), i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof CardHolder) {
            ((CardHolder) viewHolder).recycle();
        }
        super.onViewRecycled(viewHolder);
    }

    public void onUserVisibleChanged(boolean z) {
        this.mUserVisible = z;
        UserVisibleChangedListener userVisibleChangedListener = this.mUserVisibleChangedListener;
        if (userVisibleChangedListener != null) {
            userVisibleChangedListener.onUserVisibleChanged(z);
        }
    }

    public void backTranslation(Intent intent) {
        AssistantPageFragment assistantPageFragment;
        Bundle bundleExtra;
        CardCoverView cardCoverView;
        if (intent == null || (assistantPageFragment = this.mFragment) == null || !assistantPageFragment.isVisible() || (bundleExtra = intent.getBundleExtra("transition_info")) == null || this.mTransitionInfo == null || this.mCardViewHolder == null || bundleExtra.getInt("info_launcher_orientation") == 2) {
            return;
        }
        View poll = this.mCardViewHolder.poll();
        this.mTranView = poll;
        if (poll == null) {
            this.mCardViewHolder.cacheInfo(intent);
            return;
        }
        int i = bundleExtra.getInt(nexExportFormat.TAG_FORMAT_WIDTH);
        int i2 = bundleExtra.getInt(nexExportFormat.TAG_FORMAT_HEIGHT);
        int i3 = bundleExtra.getInt("current_index");
        int i4 = this.mTransitionInfo.getInt("x");
        int i5 = this.mTransitionInfo.getInt("y");
        int i6 = this.mTransitionInfo.getInt(nexExportFormat.TAG_FORMAT_WIDTH);
        int i7 = this.mTransitionInfo.getInt(nexExportFormat.TAG_FORMAT_HEIGHT);
        ActionBar appCompatActionBar = this.mContext.getAppCompatActionBar();
        if (appCompatActionBar != null && !BaseBuildUtil.isLargeScreenDevice()) {
            i5 -= appCompatActionBar.getHeight();
        }
        hideTitleAndDesc();
        View view = this.mTranView;
        if (view instanceof CardSlideView) {
            CardSlideView cardSlideView = (CardSlideView) view;
            cardSlideView.setLoadIndex(i3);
            cardSlideView.onResume();
            cardCoverView = cardSlideView.getSlideShowView();
        } else {
            if (view instanceof CardCoverView) {
                cardCoverView = (CardCoverView) view;
            } else {
                cardCoverView = (CardCoverView) view.findViewById(R.id.card_cover);
            }
            cardCoverView.setOptionWidthAndHeight(i6, i7);
        }
        ViewGroup viewGroup = (ViewGroup) cardCoverView.getParent();
        int indexOfChild = viewGroup.indexOfChild(cardCoverView);
        viewGroup.removeView(cardCoverView);
        View view2 = new View(this.mContext);
        viewGroup.addView(view2, new ViewGroup.LayoutParams(i6, i7));
        AppCompatActivity appCompatActivity = this.mContext;
        RoundedFrameLayout roundedFrameLayout = new RoundedFrameLayout(appCompatActivity, (int) appCompatActivity.getResources().getDimension(R.dimen.card_cover_layout_imageview_radius));
        roundedFrameLayout.addView(cardCoverView);
        ViewGroup viewGroup2 = (ViewGroup) this.mContext.findViewById(16908290);
        viewGroup2.addView(roundedFrameLayout, new ViewGroup.LayoutParams(i, i2));
        View view3 = new View(this.mContext);
        view3.setClickable(true);
        viewGroup2.addView(view3, new ViewGroup.LayoutParams(-1, -1));
        Folme.clean(roundedFrameLayout);
        AnimState animState = new AnimState("start");
        ViewProperty viewProperty = ViewProperty.WIDTH;
        AnimState add = animState.add(viewProperty, i);
        ViewProperty viewProperty2 = ViewProperty.HEIGHT;
        AnimState add2 = add.add(viewProperty2, i2);
        ViewProperty viewProperty3 = ViewProperty.X;
        AnimState add3 = add2.add(viewProperty3, SearchStatUtils.POW);
        ViewProperty viewProperty4 = ViewProperty.Y;
        AnimState add4 = add3.add(viewProperty4, SearchStatUtils.POW);
        AnimState add5 = new AnimState("to").add(viewProperty, i6).add(viewProperty2, i7).add(viewProperty3, i4).add(viewProperty4, i5);
        AnimConfig animConfig = new AnimConfig();
        animConfig.addListeners(new AnonymousClass1(roundedFrameLayout, cardCoverView, viewGroup, view2, viewGroup, indexOfChild, viewGroup2, view3));
        animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
        Folme.useAt(roundedFrameLayout).state().fromTo(add4, add5, animConfig);
    }

    /* renamed from: com.miui.gallery.card.ui.cardlist.CardAdapter$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends TransitionListener {
        public final /* synthetic */ View val$coverView;
        public final /* synthetic */ ViewGroup val$decorView;
        public final /* synthetic */ int val$finalIndex;
        public final /* synthetic */ ViewGroup val$finalParent;
        public final /* synthetic */ View val$pView;
        public final /* synthetic */ ViewGroup val$parent;
        public final /* synthetic */ View val$tView;
        public final /* synthetic */ ViewGroup val$transitionView;

        /* renamed from: $r8$lambda$t-PYoDG8XXL3yIAvRvwoLA8gRFg */
        public static /* synthetic */ void m657$r8$lambda$tPYoDG8XXL3yIAvRvwoLA8gRFg(AnonymousClass1 anonymousClass1) {
            anonymousClass1.lambda$onComplete$0();
        }

        public AnonymousClass1(ViewGroup viewGroup, View view, ViewGroup viewGroup2, View view2, ViewGroup viewGroup3, int i, ViewGroup viewGroup4, View view3) {
            CardAdapter.this = r1;
            this.val$transitionView = viewGroup;
            this.val$tView = view;
            this.val$parent = viewGroup2;
            this.val$pView = view2;
            this.val$finalParent = viewGroup3;
            this.val$finalIndex = i;
            this.val$decorView = viewGroup4;
            this.val$coverView = view3;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (CardAdapter.this.mTranView != null) {
                CardAdapter.this.mTranView.setVisibility(0);
                CardAdapter.this.mTranView.post(new Runnable() { // from class: com.miui.gallery.card.ui.cardlist.CardAdapter$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        CardAdapter.AnonymousClass1.m657$r8$lambda$tPYoDG8XXL3yIAvRvwoLA8gRFg(CardAdapter.AnonymousClass1.this);
                    }
                });
            }
            this.val$transitionView.removeView(this.val$tView);
            this.val$parent.removeView(this.val$pView);
            this.val$finalParent.addView(this.val$tView, this.val$finalIndex);
            this.val$decorView.removeView(this.val$transitionView);
            this.val$decorView.removeView(this.val$coverView);
        }

        public /* synthetic */ void lambda$onComplete$0() {
            CardAdapter.this.appearAnim();
        }
    }

    public final void hideTitleAndDesc() {
        View view = this.mTranView;
        if (view == null) {
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.title);
        TextView textView2 = (TextView) this.mTranView.findViewById(R.id.description);
        if (textView == null || textView2 == null) {
            return;
        }
        textView.setAlpha(0.0f);
        textView2.setAlpha(0.0f);
    }

    public final void appearAnim() {
        View view = this.mTranView;
        if (view == null) {
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.title);
        TextView textView2 = (TextView) this.mTranView.findViewById(R.id.description);
        if (textView == null || textView2 == null) {
            return;
        }
        FolmeUtil.setCustomVisibleAnim(textView, true, null, null);
        FolmeUtil.setCustomVisibleAnim(textView2, true, null, null);
    }

    /* loaded from: classes.dex */
    public static class ViewHolderImpl extends RecyclerView.ViewHolder {
        public ViewHolderImpl(View view) {
            super(view);
        }
    }

    /* loaded from: classes.dex */
    public static class DataBinder {
        public void bindSlideCardData(CardCoverSizeUtil.CoverItemInfo coverItemInfo, boolean z, Card card, CardSlideView cardSlideView, TextView textView, TextView textView2) {
            if (card == null || cardSlideView == null) {
                return;
            }
            if (z) {
                cardSlideView.onResume();
            } else {
                cardSlideView.onPause();
            }
            cardSlideView.update(card.getSelectedMediaSha1s(), card.getCoverMediaFeatureItems());
            if (textView == null || textView2 == null) {
                return;
            }
            textView.setText(card.getTitle());
            if (TextUtils.isEmpty(card.getDescription())) {
                textView2.setVisibility(8);
            } else {
                textView2.setVisibility(0);
                textView2.setText(card.getDescription());
            }
            bindTextStyle(coverItemInfo, textView, textView2);
        }

        public void bindCardCoverData(CardCoverSizeUtil.CoverItemInfo coverItemInfo, Card card, CardCoverView cardCoverView, TextView textView, TextView textView2, Activity activity) {
            if (card == null || cardCoverView == null) {
                return;
            }
            cardCoverView.setCoverItemInfo(coverItemInfo);
            int imageResId = card.getImageResId(activity);
            Uri imageUri = card.getImageUri();
            if (imageResId != 0) {
                cardCoverView.update(imageResId, GlideOptions.largeThumbOf());
            } else if (BaseMiscUtil.isValid(card.getCoverMediaFeatureItems())) {
                List<MediaFeatureItem> coverMediaFeatureItems = card.getCoverMediaFeatureItems();
                if (coverMediaFeatureItems != null) {
                    cardCoverView.update(coverMediaFeatureItems, GlideOptions.largeThumbOf());
                }
            } else {
                CardManager.getInstance().updateCard(card, true);
                if (imageUri != null) {
                    cardCoverView.update(imageUri.toString(), GlideOptions.largeThumbOf());
                }
            }
            if (textView == null || textView2 == null) {
                return;
            }
            textView.setText(card.getTitle());
            if (TextUtils.isEmpty(card.getDescription())) {
                textView2.setVisibility(8);
            } else {
                textView2.setVisibility(0);
                textView2.setText(card.getDescription());
            }
            bindTextStyle(coverItemInfo, textView, textView2);
        }

        public final void bindTextStyle(CardCoverSizeUtil.CoverItemInfo coverItemInfo, TextView textView, TextView textView2) {
            textView.setTextSize(0, coverItemInfo.getCoverTitleSize(textView.getContext()));
            textView2.setTextSize(0, coverItemInfo.getCoverSubTitleSize(textView.getContext()));
        }
    }

    public void updateConfig() {
        if (BaseMiscUtil.isValid(this.mDataList)) {
            ArrayList arrayList = new ArrayList();
            for (CardCoverSizeUtil.CardData cardData : this.mDataList) {
                arrayList.addAll(cardData.mCards);
            }
            updateDataList(arrayList);
            notifyDataSetChanged();
        }
    }

    /* loaded from: classes.dex */
    public static class ClickCardViewHolder {
        public Intent mCacheInfo;
        public final WeakReference<CardAdapter> mCardAdapter;
        public long mCardId = -1;
        public View mCardView;

        public ClickCardViewHolder(CardAdapter cardAdapter) {
            this.mCardAdapter = new WeakReference<>(cardAdapter);
        }

        public void update(Long l, View view) {
            long j = this.mCardId;
            if (j == -1 || view == null || j != l.longValue()) {
                return;
            }
            this.mCardView = view;
            if (this.mCacheInfo == null || this.mCardAdapter.get() == null) {
                return;
            }
            this.mCardAdapter.get().backTranslation(this.mCacheInfo);
        }

        public void save(Long l, View view) {
            if (view == null || l.longValue() == -1) {
                return;
            }
            this.mCardId = l.longValue();
            this.mCardView = view;
        }

        public void clear(Long l) {
            if (this.mCardView == null || l.longValue() == -1 || this.mCardId != l.longValue()) {
                return;
            }
            this.mCardView = null;
            this.mCacheInfo = null;
            this.mCardId = -1L;
        }

        public void cacheInfo(Intent intent) {
            this.mCacheInfo = intent;
        }

        public View poll() {
            View view = this.mCardView;
            clear(Long.valueOf(this.mCardId));
            return view;
        }
    }

    /* loaded from: classes.dex */
    public class SlideCardHolder extends CardHolder {
        public final CardSlideView mSlideView;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SlideCardHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            CardAdapter.this = r1;
            this.mSlideView = (CardSlideView) view;
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder
        public void bindData(CardCoverSizeUtil.CardData cardData, int i) {
            if (cardData == null || !BaseMiscUtil.isValid(cardData.mCards)) {
                return;
            }
            this.mCard = cardData.mCards.get(0);
            CardAdapter.this.mDataBinder.bindSlideCardData(CardAdapter.this.mCoverModel.getCoverItemInfo(0), CardAdapter.this.mUserVisible, this.mCard, this.mSlideView, this.mTitle, this.mDescription);
            CardAdapter.this.mUserVisibleChangedListener = new UserVisibleChangedListener() { // from class: com.miui.gallery.card.ui.cardlist.CardAdapter.SlideCardHolder.1
                {
                    SlideCardHolder.this = this;
                }

                @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.UserVisibleChangedListener
                public void onUserVisibleChanged(boolean z) {
                    CardSlideView cardSlideView = SlideCardHolder.this.mSlideView;
                    if (cardSlideView == null) {
                        return;
                    }
                    if (z) {
                        cardSlideView.onResume();
                    } else {
                        cardSlideView.onPause();
                    }
                }
            };
            CardAdapter.this.mCardViewHolder.update(Long.valueOf(this.mCard.getRowId()), this.itemView);
            Folme.useAt(this.mSlideView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(this.mSlideView, new AnimConfig[0]);
        }

        @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
        public void onStart(LifecycleOwner lifecycleOwner) {
            if (CardAdapter.this.mUserVisible) {
                this.mSlideView.onResume();
            } else {
                this.mSlideView.onPause();
            }
        }

        @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
        public void onStop(LifecycleOwner lifecycleOwner) {
            this.mSlideView.onPause();
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder
        public void recycle() {
            onRecycle();
            this.mSlideView.onPause();
        }

        @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
        public void onDestroy(LifecycleOwner lifecycleOwner) {
            this.mSlideView.onDestroy();
        }
    }

    /* loaded from: classes.dex */
    public class DividerCardHolder extends CardHolder {
        public CardCoverSizeUtil.CardData mCardData;
        public TextView mFirstDescription;
        public CardCoverView mFirstDividerView;
        public TextView mFirstTitle;
        public TextView mSecondDescription;
        public CardCoverView mSecondDividerView;
        public TextView mSecondTitle;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DividerCardHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            CardAdapter.this = r1;
            this.mFirstDividerView = (CardCoverView) view.findViewById(R.id.card_cover1);
            this.mSecondDividerView = (CardCoverView) view.findViewById(R.id.card_cover2);
            this.mFirstTitle = (TextView) view.findViewById(R.id.title1);
            this.mFirstDescription = (TextView) view.findViewById(R.id.description1);
            this.mSecondTitle = (TextView) view.findViewById(R.id.title2);
            this.mSecondDescription = (TextView) view.findViewById(R.id.description2);
            this.mFirstDividerView.setOnClickListener(this.mDebounceClickListener);
            this.mSecondDividerView.setOnClickListener(this.mDebounceClickListener);
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder
        public void bindData(CardCoverSizeUtil.CardData cardData, int i) {
            if (cardData == null || !BaseMiscUtil.isValid(cardData.mCards)) {
                return;
            }
            this.mCardData = cardData;
            if (this.mFirstDividerView != null) {
                Card card = cardData.mCards.get(0);
                CardAdapter.this.mDataBinder.bindCardCoverData(cardData.mCoverItemInfo, card, this.mFirstDividerView, this.mFirstTitle, this.mFirstDescription, CardAdapter.this.mContext);
                CardAdapter.this.mCardViewHolder.update(Long.valueOf(card.getRowId()), this.mFirstDividerView);
                Folme.useAt(this.mFirstDividerView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(this.mFirstDividerView, new AnimConfig[0]);
            }
            if (this.mSecondDividerView == null || cardData.mCards.size() <= 1) {
                return;
            }
            Card card2 = cardData.mCards.get(1);
            CardAdapter.this.mDataBinder.bindCardCoverData(cardData.mCoverItemInfo, card2, this.mSecondDividerView, this.mSecondTitle, this.mSecondDescription, CardAdapter.this.mContext);
            CardAdapter.this.mCardViewHolder.update(Long.valueOf(card2.getRowId()), this.mSecondDividerView);
            Folme.useAt(this.mSecondDividerView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(this.mSecondDividerView, new AnimConfig[0]);
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder
        public void deliveryAction(View view) {
            Card card;
            if (view == this.mFirstDividerView) {
                card = this.mCardData.mCards.get(0);
            } else {
                card = this.mCardData.mCards.get(1);
            }
            if (card != null) {
                String detailUrl = card.getDetailUrl();
                List<String> selectedMediaSha1s = card.getSelectedMediaSha1s();
                if (!TextUtils.isEmpty(detailUrl) && BaseMiscUtil.isValid(selectedMediaSha1s)) {
                    CardAdapter.this.mCardViewHolder.save(Long.valueOf(card.getRowId()), view);
                    int[] iArr = new int[2];
                    view.getLocationInWindow(iArr);
                    int width = view.getWidth();
                    int height = view.getHeight();
                    String title = card.getTitle();
                    String description = card.getDescription();
                    CardCoverView cardCoverView = (CardCoverView) view;
                    int currentIndex = cardCoverView.getCurrentIndex();
                    String currentLocalPath = cardCoverView.getCurrentLocalPath();
                    FolmeUtil.setFakeTouchAnim(view, 0.95f, 200L);
                    CardAdapter.this.mTransitionInfo = new Bundle();
                    CardAdapter.this.mTransitionInfo.putInt("x", iArr[0]);
                    CardAdapter.this.mTransitionInfo.putInt("y", iArr[1]);
                    CardAdapter.this.mTransitionInfo.putInt(nexExportFormat.TAG_FORMAT_WIDTH, width);
                    CardAdapter.this.mTransitionInfo.putInt(nexExportFormat.TAG_FORMAT_HEIGHT, height);
                    CardAdapter.this.mTransitionInfo.putString("title", title);
                    CardAdapter.this.mTransitionInfo.putString("description", description);
                    CardAdapter.this.mTransitionInfo.putInt("current_index", currentIndex);
                    CardAdapter.this.mTransitionInfo.putInt("info_launcher_orientation", CardAdapter.this.mContext.getResources().getConfiguration().orientation);
                    CardAdapter.this.mTransitionInfo.putString("current_uri", currentLocalPath);
                    Intent intent = new Intent(CardAdapter.this.mContext, StoryAlbumActivity.class);
                    intent.putExtra("card_id", card.getRowId());
                    intent.putExtra("transition_info", CardAdapter.this.mTransitionInfo);
                    CardAdapter.this.mContext.startActivityFromFragment(CardAdapter.this.mFragment, intent, 100);
                    recordCardClick(card);
                    return;
                }
                DefaultLogger.e("CardAdapter", "deliveryAction empty url = %s , mediaSha1s isValid = %b", detailUrl, Boolean.valueOf(BaseMiscUtil.isValid(selectedMediaSha1s)));
                return;
            }
            DefaultLogger.e("CardAdapter", "card object is null");
        }

        @Override // com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder
        public void recycle() {
            onRecycle();
            CardCoverView cardCoverView = this.mFirstDividerView;
            if (cardCoverView != null) {
                cardCoverView.unbind();
            }
            CardCoverView cardCoverView2 = this.mSecondDividerView;
            if (cardCoverView2 != null) {
                cardCoverView2.unbind();
            }
        }
    }

    /* loaded from: classes.dex */
    public class CardHolder extends LifecycleAwareViewHolder {
        public Card mCard;
        public final CardCoverView mCardCoverView;
        public final DebounceClickListener mDebounceClickListener;
        public final TextView mDescription;
        public final TextView mTitle;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CardHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
            CardAdapter.this = r1;
            DebounceClickListener debounceClickListener = new DebounceClickListener() { // from class: com.miui.gallery.card.ui.cardlist.CardAdapter.CardHolder.1
                {
                    CardHolder.this = this;
                }

                @Override // com.miui.gallery.widget.DebounceClickListener
                public void onClickConfirmed(View view2) {
                    view2.getId();
                    CardHolder.this.deliveryAction(view2);
                }
            };
            this.mDebounceClickListener = debounceClickListener;
            this.mCardCoverView = (CardCoverView) view.findViewById(R.id.card_cover);
            this.mTitle = (TextView) view.findViewById(R.id.title);
            this.mDescription = (TextView) view.findViewById(R.id.description);
            view.setOnClickListener(debounceClickListener);
        }

        public void bindData(CardCoverSizeUtil.CardData cardData, int i) {
            if (this.mCardCoverView == null) {
                return;
            }
            this.mCard = cardData.mCards.get(0);
            CardAdapter.this.mDataBinder.bindCardCoverData(cardData.mCoverItemInfo, this.mCard, this.mCardCoverView, this.mTitle, this.mDescription, CardAdapter.this.mContext);
            CardAdapter.this.mCardViewHolder.update(Long.valueOf(this.mCard.getRowId()), this.itemView);
            Folme.useAt(this.mCardCoverView).hover().setEffect(IHoverStyle.HoverEffect.FLOATED).handleHoverOf(this.mCardCoverView, new AnimConfig[0]);
        }

        public void deliveryAction(View view) {
            ICardView iCardView;
            Card card = this.mCard;
            if (card != null) {
                String detailUrl = card.getDetailUrl();
                List<String> selectedMediaSha1s = this.mCard.getSelectedMediaSha1s();
                if (!TextUtils.isEmpty(detailUrl) && BaseMiscUtil.isValid(selectedMediaSha1s)) {
                    CardAdapter.this.mCardViewHolder.save(Long.valueOf(this.mCard.getRowId()), view);
                    int[] iArr = new int[2];
                    view.getLocationInWindow(iArr);
                    int width = view.getWidth();
                    int height = view.getHeight();
                    String title = this.mCard.getTitle();
                    String description = this.mCard.getDescription();
                    if (view instanceof ICardView) {
                        iCardView = (ICardView) view;
                    } else {
                        iCardView = (ICardView) view.findViewById(R.id.card_cover);
                    }
                    int currentIndex = iCardView == null ? 0 : iCardView.getCurrentIndex();
                    String currentLocalPath = iCardView == null ? "" : iCardView.getCurrentLocalPath();
                    FolmeUtil.setFakeTouchAnim(view, 0.95f, 200L);
                    CardAdapter.this.mTransitionInfo = new Bundle();
                    CardAdapter.this.mTransitionInfo.putInt("x", iArr[0]);
                    CardAdapter.this.mTransitionInfo.putInt("y", iArr[1]);
                    CardAdapter.this.mTransitionInfo.putInt(nexExportFormat.TAG_FORMAT_WIDTH, width);
                    CardAdapter.this.mTransitionInfo.putInt(nexExportFormat.TAG_FORMAT_HEIGHT, height);
                    CardAdapter.this.mTransitionInfo.putString("title", title);
                    CardAdapter.this.mTransitionInfo.putString("description", description);
                    CardAdapter.this.mTransitionInfo.putInt("current_index", currentIndex);
                    CardAdapter.this.mTransitionInfo.putInt("info_launcher_orientation", CardAdapter.this.mContext.getResources().getConfiguration().orientation);
                    CardAdapter.this.mTransitionInfo.putString("current_uri", currentLocalPath);
                    Intent intent = new Intent(CardAdapter.this.mContext, StoryAlbumActivity.class);
                    intent.putExtra("card_id", this.mCard.getRowId());
                    intent.putExtra("transition_info", CardAdapter.this.mTransitionInfo);
                    CardAdapter.this.mContext.startActivityFromFragment(CardAdapter.this.mFragment, intent, 100);
                    recordCardClick(this.mCard);
                    return;
                }
                DefaultLogger.e("CardAdapter", "deliveryAction empty url = %s , mediaSha1s isValid = %b", detailUrl, Boolean.valueOf(BaseMiscUtil.isValid(selectedMediaSha1s)));
                return;
            }
            DefaultLogger.e("CardAdapter", "card object is null");
        }

        public void recordCardClick(Card card) {
            if (card == null) {
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put("scenario_id", String.valueOf(card.getScenarioId()));
            SamplingStatHelper.recordCountEvent("assistant", "assistant_card_click", hashMap);
            HashMap hashMap2 = new HashMap();
            hashMap2.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(card.getScenarioId()));
            hashMap2.put("tip", "403.8.4.1.11145");
            hashMap2.put("ref_tip", "403.8.0.1.11136");
            hashMap2.put("value", card.getTitle());
            TrackController.trackClick(hashMap2);
        }

        public void recycle() {
            onRecycle();
            CardCoverView cardCoverView = this.mCardCoverView;
            if (cardCoverView != null) {
                cardCoverView.unbind();
            }
        }
    }
}
