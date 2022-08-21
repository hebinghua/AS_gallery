package com.miui.gallery.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.R;
import com.miui.gallery.ui.SyncWidget;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.OnAppFocusedListener;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.PanelBar;
import com.miui.gallery.widget.PanelItem;
import com.miui.gallery.widget.PanelManager;
import com.miui.gallery.widget.tsd.DrawerAnimEndListener;
import com.miui.gallery.widget.tsd.DrawerState;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.InestedScrollerStateListener;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/* loaded from: classes2.dex */
public class SyncDownloadManager implements OnAppFocusedListener {
    public Activity mActivity;
    public Runnable mDelayShowBarRunnable;
    public DownloadManager mDownloadManager;
    public INestedTwoStageDrawer mDrawer;
    public PanelItemManager mItemManager;
    public boolean mPanelItemAutoShowEnable = true;
    public SyncManager mSyncManager;
    public SyncTextLine mSyncTextLine;

    public SyncDownloadManager(Activity activity, INestedTwoStageDrawer iNestedTwoStageDrawer) {
        this.mActivity = activity;
        this.mSyncManager = new SyncManager(activity, 0);
        this.mDownloadManager = new DownloadManager(activity, 1);
        this.mDrawer = iNestedTwoStageDrawer;
        iNestedTwoStageDrawer.addScrollerStateListener(new InestedScrollerStateListener() { // from class: com.miui.gallery.ui.SyncDownloadManager.1
            public int triggerDistance;

            {
                this.triggerDistance = SyncDownloadManager.this.mActivity.getResources().getDimensionPixelOffset(R.dimen.sync_text_line_padding_top);
            }

            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerStateChanged(DrawerState drawerState, int i) {
                if (drawerState == DrawerState.OPEN) {
                    SyncWidget.TrackStatusType.trackExpose(SyncDownloadManager.this.mSyncTextLine.mSyncManager.getCurSyncStatus(), false);
                }
            }

            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerUpdate(DrawerState drawerState, int i, int i2) {
                if (SyncDownloadManager.this.mSyncTextLine == null) {
                    return;
                }
                int i3 = this.triggerDistance;
                if (i <= (-i3)) {
                    SyncDownloadManager.this.mSyncTextLine.showOrHideSyncView(false);
                } else if (i <= (-i3)) {
                } else {
                    SyncDownloadManager.this.mSyncTextLine.showOrHideSyncView(true);
                }
            }
        });
        this.mItemManager = new PanelItemManager((PanelBar) ((View) this.mDrawer).findViewById(R.id.home_page_top_bar));
        SyncTextLine syncTextLine = new SyncTextLine(this.mActivity, 0, this.mItemManager);
        this.mSyncTextLine = syncTextLine;
        syncTextLine.setManager(this.mSyncManager, this.mDownloadManager);
        this.mSyncManager.setSyncStatusListener(this.mSyncTextLine);
        this.mDownloadManager.setStatusListener(this.mSyncTextLine);
        this.mSyncTextLine.refreshByStatus();
    }

    public void setPhotoCountAndVideosCount(int i, int i2) {
        SyncTextLine syncTextLine = this.mSyncTextLine;
        if (syncTextLine != null) {
            syncTextLine.setPhotoCountAndVideoCount(i, i2);
        }
    }

    public void onResume() {
        this.mSyncManager.onResume();
        this.mDownloadManager.onResume();
    }

    public void onPause() {
        this.mSyncManager.onPause();
        this.mDownloadManager.onPause();
    }

    public void setPermanent(boolean z) {
        this.mSyncManager.setIsPermanent(z);
    }

    public void setEnable(boolean z, float f) {
        PanelItemManager panelItemManager = this.mItemManager;
        if (panelItemManager != null) {
            panelItemManager.setItemEnable(z, f);
        }
    }

    @Override // com.miui.gallery.util.OnAppFocusedListener
    public void onAppFocused() {
        this.mSyncManager.onAppFocused();
    }

    public final void showPanelBar(boolean z, DrawerAnimEndListener drawerAnimEndListener) {
        removeShowPanelBarMessage();
        this.mDrawer.setDrawerState(DrawerState.OPEN, z, drawerAnimEndListener);
    }

    public final void closePanelBar(boolean z, DrawerAnimEndListener drawerAnimEndListener) {
        removeShowPanelBarMessage();
        if (this.mDrawer.isDrawerOpen()) {
            this.mDrawer.setDrawerState(DrawerState.HALF_OPEN, z, drawerAnimEndListener);
        }
    }

    public final boolean isPanelBarOpened() {
        return this.mDrawer.isDrawerOpen();
    }

    public final boolean isPanelBarAniming() {
        return this.mDrawer.isAnimating();
    }

    public final void delayShowPanelBar(int i) {
        if (this.mDelayShowBarRunnable == null) {
            this.mDelayShowBarRunnable = new Runnable() { // from class: com.miui.gallery.ui.SyncDownloadManager.2
                @Override // java.lang.Runnable
                public void run() {
                    if (SyncDownloadManager.this.mPanelItemAutoShowEnable) {
                        SyncDownloadManager.this.showPanelBar(true, null);
                    }
                }
            };
        }
        removeShowPanelBarMessage();
        ThreadManager.getMainHandler().postDelayed(this.mDelayShowBarRunnable, i);
    }

    public final void removeShowPanelBarMessage() {
        ThreadManager.getMainHandler().removeCallbacks(this.mDelayShowBarRunnable);
    }

    public void setPanelItemAutoShowEnable(boolean z) {
        this.mPanelItemAutoShowEnable = z;
        if (z || !this.mDrawer.isAnimating()) {
            return;
        }
        this.mDrawer.cancelDrawerAnim();
    }

    /* loaded from: classes2.dex */
    public static abstract class HomePageWidget implements PanelItem {
        public final Context mContext;

        public HomePageWidget(Context context) {
            this.mContext = context;
        }

        public Context getContext() {
            return this.mContext;
        }
    }

    /* loaded from: classes2.dex */
    public class PanelItemManager implements PanelManager {
        public Comparator<PanelItem> mComparator;
        public PanelItem mCurrentItem;
        public boolean mIsItemEnable = true;
        public PanelBar mPanelBar;
        public PriorityQueue<PanelItem> mWaitingItems;

        public PanelItemManager(PanelBar panelBar) {
            this.mPanelBar = panelBar;
            this.mComparator = new Comparator<PanelItem>() { // from class: com.miui.gallery.ui.SyncDownloadManager.PanelItemManager.1
                @Override // java.util.Comparator
                public int compare(PanelItem panelItem, PanelItem panelItem2) {
                    if (panelItem == null && panelItem2 == null) {
                        return 0;
                    }
                    if (panelItem == null) {
                        return -1;
                    }
                    if (panelItem2 != null) {
                        return panelItem.getPriority() - panelItem2.getPriority();
                    }
                    return 1;
                }
            };
            this.mWaitingItems = new PriorityQueue<>(10, this.mComparator);
        }

        public void setItemEnable(boolean z, float f) {
            if (z != this.mIsItemEnable) {
                this.mIsItemEnable = z;
                PanelItem panelItem = this.mCurrentItem;
                if (panelItem != null) {
                    panelItem.setEnable(z);
                    this.mCurrentItem.setClickable(z);
                    this.mCurrentItem.setAlpha(f);
                }
                if (!BaseMiscUtil.isValid(this.mWaitingItems)) {
                    return;
                }
                Iterator<PanelItem> it = this.mWaitingItems.iterator();
                while (it.hasNext()) {
                    PanelItem next = it.next();
                    next.setEnable(z);
                    next.setClickable(z);
                }
            }
        }

        @Override // com.miui.gallery.widget.PanelManager
        public boolean addItem(PanelItem panelItem, boolean z) {
            if (panelItem != null) {
                panelItem.setClickable(this.mIsItemEnable);
                panelItem.setEnable(this.mIsItemEnable);
            }
            PanelItem panelItem2 = this.mCurrentItem;
            if (panelItem2 == null) {
                this.mCurrentItem = panelItem;
            } else if (panelItem2 != panelItem) {
                if (this.mComparator.compare(panelItem2, panelItem) >= 0) {
                    if (!this.mWaitingItems.contains(panelItem)) {
                        this.mWaitingItems.offer(panelItem);
                    }
                    return false;
                }
                if (!this.mWaitingItems.contains(this.mCurrentItem)) {
                    this.mWaitingItems.offer(this.mCurrentItem);
                }
                this.mCurrentItem = panelItem;
            }
            if (z && SyncDownloadManager.this.mPanelItemAutoShowEnable) {
                if (panelItem2 != null && panelItem2 != this.mCurrentItem && SyncDownloadManager.this.isPanelBarOpened() && !SyncDownloadManager.this.isPanelBarAniming()) {
                    SyncDownloadManager.this.closePanelBar(true, new DrawerAnimEndListener() { // from class: com.miui.gallery.ui.SyncDownloadManager.PanelItemManager.2
                        @Override // com.miui.gallery.widget.tsd.DrawerAnimEndListener
                        public void onDrawerAnimEnd() {
                            PanelItemManager.this.mPanelBar.replaceItem(PanelItemManager.this.mCurrentItem, false);
                            SyncDownloadManager.this.showPanelBar(true, null);
                        }
                    });
                } else {
                    PanelItem panelItem3 = this.mCurrentItem;
                    if (panelItem2 != panelItem3) {
                        this.mPanelBar.replaceItem(panelItem3, SyncDownloadManager.this.isPanelBarAniming());
                    }
                    SyncDownloadManager.this.delayShowPanelBar(panelItem2 == null ? UIMsg.MSG_MAP_PANO_DATA : UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
                }
            } else {
                PanelItem panelItem4 = this.mCurrentItem;
                if (panelItem2 != panelItem4) {
                    this.mPanelBar.replaceItem(panelItem4, false);
                }
            }
            return true;
        }
    }
}
