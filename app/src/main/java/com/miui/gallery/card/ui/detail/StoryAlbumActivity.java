package com.miui.gallery.card.ui.detail;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.biz.story.StoryAlbumFragment;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.movie.utils.MovieBackgroundDownloadManager;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes.dex */
public class StoryAlbumActivity extends Hilt_StoryAlbumActivity {
    public StoryAlbumFragment mStoryAlbumFragment;

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        final boolean booleanExtra = getIntent().getBooleanExtra("open_story_from_widget", false);
        final long longExtra = getIntent().getLongExtra("card_id", 0L);
        final Bundle bundleExtra = getIntent().getBundleExtra("transition_info");
        getWindow().setBackgroundDrawableResource(R.color.story_background_color);
        Card cardByCardId = CardManager.getInstance().getCardByCardId(longExtra);
        if (!isValidCard(cardByCardId)) {
            finish();
            return;
        }
        MovieBackgroundDownloadManager.getInstance().downloadTemplate(this, CardUtil.getMovieTemplateFromCard(cardByCardId));
        this.mStoryAlbumFragment = (StoryAlbumFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.card.ui.detail.StoryAlbumActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                Fragment lambda$onCreate$0;
                lambda$onCreate$0 = StoryAlbumActivity.lambda$onCreate$0(longExtra, bundleExtra, booleanExtra, str);
                return lambda$onCreate$0;
            }
        }, "StoryAlbumFragment", false, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(long j, Bundle bundle, boolean z, String str) {
        StoryAlbumFragment storyAlbumFragment = new StoryAlbumFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putLong("card_id", j);
        bundle2.putBundle("transition_info", bundle);
        bundle2.putBoolean("open_story_from_widget", z);
        storyAlbumFragment.setArguments(bundle2);
        return storyAlbumFragment;
    }

    public final boolean isValidCard(Card card) {
        return card != null && BaseMiscUtil.isValid(card.getSelectedMediaSha1s()) && card.isValid();
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        StoryAlbumFragment storyAlbumFragment = this.mStoryAlbumFragment;
        if (storyAlbumFragment != null) {
            storyAlbumFragment.onAttachDialogFragment(fragment);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            this.mStoryAlbumFragment.finish(true);
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        this.mStoryAlbumFragment.onProvideKeyboardShortcuts(list, menu, i);
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (this.mStoryAlbumFragment.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        return super.onKeyShortcut(i, keyEvent);
    }
}
