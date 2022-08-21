package com.miui.gallery.gallerywidget.ui;

import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.activity.GallerySettingsActivity;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.ui.detail.StoryAlbumActivity;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class RecommendDispatchActivity extends BaseActivity {
    public final String TAG = "RecommendDispatchActivity";
    public Future<Card> mFuture;
    public long mSelectedCardId;
    public String mSelectedPicPath;
    public int mWidgetId;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        DefaultLogger.d("RecommendDispatchActivity", "---log---getTaskId>" + getTaskId());
        super.onCreate(bundle);
        this.mWidgetId = getIntent().getIntExtra("gallery_app_widget_id", -1);
        this.mSelectedCardId = getIntent().getLongExtra("selected_card_id", -1L);
        this.mSelectedPicPath = getIntent().getStringExtra("selected_pic_path");
        dispatchActivity();
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Future<Card> future = this.mFuture;
        if (future != null) {
            future.cancel();
        }
        super.onDestroy();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finishAndRemoveTask();
    }

    @Override // android.app.Activity, android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent) {
        intent.addFlags(268468224);
        super.startActivity(intent);
    }

    public final void dispatchActivity() {
        DefaultLogger.d("dispatchActivity", "---log--- dispatchActivity onCreate ");
        if (!GalleryPreferences.Assistant.isStoryFunctionOn()) {
            Intent intent = new Intent(this, GallerySettingsActivity.class);
            intent.putExtra("use_dialog", true);
            startActivity(intent);
            finish();
            return;
        }
        Card cardByCardId = CardManager.getInstance().getCardByCardId(this.mSelectedCardId);
        if (cardByCardId != null) {
            DefaultLogger.d("dispatchActivity", "---log---CardManager find card in mCardCache > " + cardByCardId.getRowId());
            jumpToStory(cardByCardId);
            return;
        }
        this.mFuture = ThreadManager.getMiscPool().submit(new SpecificJob(this.mSelectedCardId), new SpecificFutureListener(this));
    }

    public final void jumpToAssistant() {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra("extra_start_page", 2);
        startActivity(intent);
        finish();
    }

    public final void jumpToStory(Card card) {
        Intent intent = new Intent(this, StoryAlbumActivity.class);
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        String title = card.getTitle();
        String description = card.getDescription();
        String str = this.mSelectedPicPath;
        Bundle bundle = new Bundle();
        bundle.putInt("x", 0);
        bundle.putInt("y", screenHeight / 2);
        bundle.putInt(nexExportFormat.TAG_FORMAT_WIDTH, screenWidth);
        bundle.putInt(nexExportFormat.TAG_FORMAT_HEIGHT, screenWidth / 2);
        bundle.putString("title", title);
        bundle.putString("description", description);
        bundle.putInt("current_index", 0);
        bundle.putInt("info_launcher_orientation", getResources().getConfiguration().orientation);
        bundle.putString("current_uri", str);
        intent.putExtra("transition_info", bundle);
        intent.putExtra("card_id", this.mSelectedCardId);
        intent.putExtra("open_story_from_widget", true);
        startActivity(intent);
        finish();
    }

    /* loaded from: classes2.dex */
    public static class SpecificJob implements ThreadPool.Job<Card> {
        public final long cardId;

        public SpecificJob(long j) {
            this.cardId = j;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Card mo1807run(ThreadPool.JobContext jobContext) {
            DefaultLogger.d("SpecificJob", "---log---SpecificJob find card > %d", Long.valueOf(this.cardId));
            return CardManager.getInstance().findHasShowCard(this.cardId);
        }
    }

    /* loaded from: classes2.dex */
    public static class SpecificFutureListener implements FutureListener<Card> {
        public WeakReference<RecommendDispatchActivity> reference;

        public SpecificFutureListener(RecommendDispatchActivity recommendDispatchActivity) {
            this.reference = new WeakReference<>(recommendDispatchActivity);
        }

        @Override // com.miui.gallery.concurrent.FutureListener
        public void onFutureDone(Future<Card> future) {
            RecommendDispatchActivity recommendDispatchActivity = this.reference.get();
            if (recommendDispatchActivity == null) {
                return;
            }
            if (recommendDispatchActivity.mFuture != null && recommendDispatchActivity.mFuture.isCancelled()) {
                return;
            }
            Card card = future.get();
            DefaultLogger.d("SpecificJob", "---log---onFutureDone card >" + card);
            if (card != null) {
                recommendDispatchActivity.jumpToStory(card);
            } else {
                recommendDispatchActivity.jumpToAssistant();
            }
        }
    }
}
