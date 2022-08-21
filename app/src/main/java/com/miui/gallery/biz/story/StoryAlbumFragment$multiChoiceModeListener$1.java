package com.miui.gallery.biz.story;

import android.content.Context;
import android.content.DialogInterface;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import com.android.internal.CompatHandler;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.biz.story.data.MediaStats;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.model.ImageLoadParamsKt;
import com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: StoryAlbumFragment.kt */
/* loaded from: classes.dex */
public final class StoryAlbumFragment$multiChoiceModeListener$1 implements MultiChoiceModeListener {
    public int bottomHeight;
    public MenuItem removeMenu;
    public MenuItem shareMenu;
    public final /* synthetic */ StoryAlbumFragment this$0;

    @Override // android.view.ActionMode.Callback
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(menu, "menu");
        return false;
    }

    public StoryAlbumFragment$multiChoiceModeListener$1(StoryAlbumFragment storyAlbumFragment) {
        this.this$0 = storyAlbumFragment;
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onAllItemsCheckedStateChanged(ActionMode mode, boolean z) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        updateMenuState();
    }

    @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
    public void onItemCheckedStateChanged(ActionMode mode, int i, long j, boolean z) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        updateMenuState();
    }

    @Override // android.view.ActionMode.Callback
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        SimpleThemeActionBarHelper simpleThemeActionBarHelper;
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(menu, "menu");
        DefaultLogger.d("StoryAlbumFragment", "onCreateActionMode");
        TrackController.trackExpose("403.38.1.1.11149", AutoTracking.getRef());
        mode.getMenuInflater().inflate(R.menu.story_album_action_menu, menu);
        this.removeMenu = menu.findItem(R.id.action_remove);
        this.shareMenu = menu.findItem(R.id.action_share);
        updateMenuState();
        this.this$0.isInActionMode = true;
        MediaStats value = this.this$0.mo617getViewModel().getMediaStats().getValue();
        if (value != null) {
            this.this$0.updateSliderPlayStatus(value);
        }
        CompatHandler mainHandler = ThreadManager.Companion.getMainHandler();
        final StoryAlbumFragment storyAlbumFragment = this.this$0;
        mainHandler.post(new Runnable() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$multiChoiceModeListener$1$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StoryAlbumFragment$multiChoiceModeListener$1.m623onCreateActionMode$lambda1(StoryAlbumFragment.this, this);
            }
        });
        simpleThemeActionBarHelper = this.this$0.actionBarHelper;
        if (simpleThemeActionBarHelper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
            simpleThemeActionBarHelper = null;
        }
        simpleThemeActionBarHelper.refreshTopBar(1.0f);
        return true;
    }

    /* renamed from: onCreateActionMode$lambda-1  reason: not valid java name */
    public static final void m623onCreateActionMode$lambda1(StoryAlbumFragment this$0, StoryAlbumFragment$multiChoiceModeListener$1 this$1) {
        AppCompatActivity appCompatActivity;
        GalleryRecyclerView galleryRecyclerView;
        GalleryRecyclerView galleryRecyclerView2;
        AppCompatActivity appCompatActivity2;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(this$1, "this$1");
        appCompatActivity = this$0.mActivity;
        if (appCompatActivity != null) {
            galleryRecyclerView = this$0.recyclerView;
            GalleryRecyclerView galleryRecyclerView3 = null;
            if (galleryRecyclerView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
                galleryRecyclerView = null;
            }
            this$1.bottomHeight = galleryRecyclerView.getPaddingBottom();
            galleryRecyclerView2 = this$0.recyclerView;
            if (galleryRecyclerView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            } else {
                galleryRecyclerView3 = galleryRecyclerView2;
            }
            int i = this$1.bottomHeight;
            appCompatActivity2 = this$0.mActivity;
            galleryRecyclerView3.setPadding(0, 0, 0, i + MiscUtil.getDefaultSplitActionBarHeight(appCompatActivity2));
        }
    }

    @Override // android.view.ActionMode.Callback
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        AppCompatActivity appCompatActivity;
        StoryAlbumAdapter storyAlbumAdapter;
        StoryAlbumAdapter storyAlbumAdapter2;
        Intrinsics.checkNotNullParameter(mode, "mode");
        Intrinsics.checkNotNullParameter(item, "item");
        EditableListViewWrapper editableListViewWrapper = this.this$0.editableListWrapper;
        if (editableListViewWrapper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editableListWrapper");
            editableListViewWrapper = null;
        }
        List<Integer> checkedPositions = editableListViewWrapper.getCheckedPositions();
        Intrinsics.checkNotNullExpressionValue(checkedPositions, "editableListWrapper.checkedPositions");
        ArrayList<String> arrayList = new ArrayList<>(checkedPositions.size());
        ArrayList<Long> arrayList2 = new ArrayList<>(checkedPositions.size());
        StoryAlbumFragment storyAlbumFragment = this.this$0;
        int i = Integer.MAX_VALUE;
        for (Integer pos : checkedPositions) {
            storyAlbumAdapter = storyAlbumFragment.adapter;
            if (storyAlbumAdapter == null) {
                Intrinsics.throwUninitializedPropertyAccessException("adapter");
                storyAlbumAdapter = null;
            }
            Intrinsics.checkNotNullExpressionValue(pos, "pos");
            String sha1 = storyAlbumAdapter.getSha1(pos.intValue());
            if (sha1 != null) {
                arrayList.add(sha1);
            }
            storyAlbumAdapter2 = storyAlbumFragment.adapter;
            if (storyAlbumAdapter2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("adapter");
                storyAlbumAdapter2 = null;
            }
            arrayList2.add(Long.valueOf(storyAlbumAdapter2.getItemId(pos.intValue())));
            i = RangesKt___RangesKt.coerceAtMost(i, pos.intValue());
        }
        if (i == Integer.MAX_VALUE) {
            i = 0;
        }
        if (item.getItemId() != 16908313 && item.getItemId() != 16908314) {
            appCompatActivity = this.this$0.mActivity;
            LinearMotorHelper.performHapticFeedback(appCompatActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
        }
        int itemId = item.getItemId();
        if (itemId == R.id.action_remove) {
            removeMediaBy(arrayList);
            return true;
        } else if (itemId != R.id.action_share) {
            return false;
        } else {
            gotoPreviewSelectPage(checkedPositions, arrayList2, i);
            return true;
        }
    }

    public final void removeMediaBy(final ArrayList<String> arrayList) {
        Context requireContext = this.this$0.requireContext();
        final StoryAlbumFragment storyAlbumFragment = this.this$0;
        DialogUtil.showConfirmAlertWithCancel(requireContext, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$multiChoiceModeListener$1$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                StoryAlbumFragment$multiChoiceModeListener$1.m625removeMediaBy$lambda4(StoryAlbumFragment.this, arrayList, dialogInterface, i);
            }
        }, StoryAlbumFragment$multiChoiceModeListener$1$$ExternalSyntheticLambda1.INSTANCE, this.this$0.getString(R.string.story_remove_confirm_title), this.this$0.getString(R.string.story_remove_confirm_message), this.this$0.getString(R.string.story_remove_confirm_positive), 17039360);
    }

    /* renamed from: removeMediaBy$lambda-4  reason: not valid java name */
    public static final void m625removeMediaBy$lambda4(StoryAlbumFragment this$0, ArrayList sha1s, DialogInterface dialogInterface, int i) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(sha1s, "$sha1s");
        EditableListViewWrapper editableListViewWrapper = this$0.editableListWrapper;
        if (editableListViewWrapper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editableListWrapper");
            editableListViewWrapper = null;
        }
        editableListViewWrapper.stopActionMode();
        this$0.mo617getViewModel().removeMediaBy(sha1s);
    }

    public final void gotoPreviewSelectPage(List<Integer> list, ArrayList<Long> arrayList, int i) {
        StoryAlbumAdapter storyAlbumAdapter;
        if (list.isEmpty()) {
            return;
        }
        TrackController.trackClick("403.38.1.1.11147", "403.38.1.1.11149");
        StoryAlbumViewModel mo617getViewModel = this.this$0.mo617getViewModel();
        StoryAlbumFragment storyAlbumFragment = this.this$0;
        storyAlbumAdapter = storyAlbumFragment.adapter;
        EditableListViewWrapper editableListViewWrapper = null;
        if (storyAlbumAdapter == null) {
            Intrinsics.throwUninitializedPropertyAccessException("adapter");
            storyAlbumAdapter = null;
        }
        int itemCount = storyAlbumAdapter.getItemCount();
        ImageLoadParams ImageLoadParams = ImageLoadParamsKt.ImageLoadParams(new StoryAlbumFragment$multiChoiceModeListener$1$gotoPreviewSelectPage$1(this.this$0, i));
        int size = arrayList.size();
        long[] jArr = new long[size];
        for (int i2 = 0; i2 < size; i2++) {
            Long l = arrayList.get(i2);
            Intrinsics.checkNotNullExpressionValue(l, "ids[idx]");
            jArr[i2] = l.longValue();
        }
        int size2 = list.size();
        int[] iArr = new int[size2];
        for (int i3 = 0; i3 < size2; i3++) {
            iArr[i3] = list.get(i3).intValue();
        }
        mo617getViewModel.gotoPreviewSelectPage(storyAlbumFragment, i, itemCount, ImageLoadParams, jArr, iArr);
        EditableListViewWrapper editableListViewWrapper2 = this.this$0.editableListWrapper;
        if (editableListViewWrapper2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editableListWrapper");
        } else {
            editableListViewWrapper = editableListViewWrapper2;
        }
        editableListViewWrapper.stopActionMode();
    }

    @Override // android.view.ActionMode.Callback
    public void onDestroyActionMode(ActionMode mode) {
        SimpleThemeActionBarHelper simpleThemeActionBarHelper;
        float f;
        Intrinsics.checkNotNullParameter(mode, "mode");
        this.this$0.isInActionMode = false;
        DefaultLogger.d("StoryAlbumFragment", "onDestroyActionMode");
        MediaStats value = this.this$0.mo617getViewModel().getMediaStats().getValue();
        if (value != null) {
            this.this$0.updateSliderPlayStatus(value);
        }
        CompatHandler mainHandler = ThreadManager.Companion.getMainHandler();
        final StoryAlbumFragment storyAlbumFragment = this.this$0;
        mainHandler.post(new Runnable() { // from class: com.miui.gallery.biz.story.StoryAlbumFragment$multiChoiceModeListener$1$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StoryAlbumFragment$multiChoiceModeListener$1.m624onDestroyActionMode$lambda7(StoryAlbumFragment.this, this);
            }
        });
        simpleThemeActionBarHelper = this.this$0.actionBarHelper;
        if (simpleThemeActionBarHelper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("actionBarHelper");
            simpleThemeActionBarHelper = null;
        }
        f = this.this$0.sliderProgress;
        simpleThemeActionBarHelper.refreshTopBar(f);
    }

    /* renamed from: onDestroyActionMode$lambda-7  reason: not valid java name */
    public static final void m624onDestroyActionMode$lambda7(StoryAlbumFragment this$0, StoryAlbumFragment$multiChoiceModeListener$1 this$1) {
        AppCompatActivity appCompatActivity;
        GalleryRecyclerView galleryRecyclerView;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(this$1, "this$1");
        appCompatActivity = this$0.mActivity;
        if (appCompatActivity == null) {
            return;
        }
        galleryRecyclerView = this$0.recyclerView;
        if (galleryRecyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            galleryRecyclerView = null;
        }
        galleryRecyclerView.setPadding(0, 0, 0, this$1.bottomHeight);
    }

    public final void updateMenuState() {
        EditableListViewWrapper editableListViewWrapper = this.this$0.editableListWrapper;
        if (editableListViewWrapper == null) {
            Intrinsics.throwUninitializedPropertyAccessException("editableListWrapper");
            editableListViewWrapper = null;
        }
        if (editableListViewWrapper.getCheckedItemCount() < 1) {
            MenuItem menuItem = this.shareMenu;
            Intrinsics.checkNotNull(menuItem);
            menuItem.setEnabled(false);
            MenuItem menuItem2 = this.removeMenu;
            Intrinsics.checkNotNull(menuItem2);
            menuItem2.setEnabled(false);
            return;
        }
        MenuItem menuItem3 = this.shareMenu;
        Intrinsics.checkNotNull(menuItem3);
        menuItem3.setEnabled(true);
        MenuItem menuItem4 = this.removeMenu;
        Intrinsics.checkNotNull(menuItem4);
        menuItem4.setEnabled(true);
    }
}
