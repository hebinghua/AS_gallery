package com.miui.gallery.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GalleryGridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.InternalPhotoPageActivity;
import com.miui.gallery.adapter.TrashAdapter;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.TrashFragment;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.PhotoPageIntent;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.EmptyPage;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.editwrapper.MultiChoiceModeListener;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GridItemSpacingDecoration;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.xiaomi.stat.MiStat;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import miui.gallery.support.actionbar.ActionBarCompat;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class TrashFragment extends BaseMediaFragment {
    public Future mCountSpaceFuture;
    public View mEmptyView;
    public boolean mHasFirstLoadFinished;
    public TrashUtils.UserInfo mLastUserInfo;
    public int mOriginCount;
    public Button mPurgeAllButton;
    public GalleryRecyclerView mRecyclerView;
    public EditableListViewWrapper mRecyclerViewWrapper;
    public TrashAdapter mTrashAdapter;
    public ImageView mTrashButton;
    public TrashedPhotoLoaderCallback mTrashedPhotoLoaderCallback;
    public LinearLayout mVipInfoButton;
    public TextView mVipInfoText;
    public ChoiceModeListener mChoiceListener = new ChoiceModeListener();
    public TrashKeyboardShortcutCallback mShortcutCallback = new TrashKeyboardShortcutCallback();

    /* renamed from: $r8$lambda$Fl8tn2X4cDAtm8IcEwrqkQn-IfU */
    public static /* synthetic */ void m1569$r8$lambda$Fl8tn2X4cDAtm8IcEwrqkQnIfU(TrashFragment trashFragment, int i) {
        trashFragment.lambda$onInflateView$0(i);
    }

    public static /* synthetic */ Integer $r8$lambda$TXL25W90iuw8rkzpGyGT2TR45pk(TrashFragment trashFragment, ArrayList arrayList, Void[] voidArr) {
        return trashFragment.lambda$doPurge$2(arrayList, voidArr);
    }

    public static /* synthetic */ Integer $r8$lambda$WTwZXWwB5aU1W9Tz1OWSIDabJRA(TrashFragment trashFragment, ArrayList arrayList, Void[] voidArr) {
        return trashFragment.lambda$doRecovery$1(arrayList, voidArr);
    }

    public static /* synthetic */ void $r8$lambda$awMaeytJwNqxwsGQa93nfsqX_3g(TrashFragment trashFragment, View view) {
        trashFragment.lambda$onActivityCreated$3(view);
    }

    public static /* synthetic */ void $r8$lambda$x6fcuU_0wsBYdT9mDhepngtMHeg(TrashFragment trashFragment, DialogInterface dialogInterface, int i) {
        trashFragment.lambda$purgeAll$4(dialogInterface, i);
    }

    public final String getOrderBy() {
        return "deleteTime DESC ";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "trash";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.trash, viewGroup, false);
        this.mRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.recycler_view);
        this.mPurgeAllButton = (Button) inflate.findViewById(R.id.purge_all);
        setPurgeAllButtonLayout();
        int integer = getResources().getInteger(R.integer.thumbnail_columns);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.micro_thumb_vertical_spacing);
        this.mRecyclerView.setLayoutManager(new GalleryGridLayoutManager(this.mActivity, integer));
        this.mRecyclerView.addItemDecoration(new GridItemSpacingDecoration(this.mRecyclerView, false, getResources().getDimensionPixelOffset(R.dimen.micro_thumb_horizontal_spacing), dimensionPixelOffset));
        this.mRecyclerView.setFastScrollEnabled(true);
        this.mRecyclerView.setOnFastScrollerStateChangedListener(new FastScrollerBar.OnStateChangedListener() { // from class: com.miui.gallery.ui.TrashFragment$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.widget.recyclerview.FastScrollerBar.OnStateChangedListener
            public final void onStateChanged(int i) {
                TrashFragment.m1569$r8$lambda$Fl8tn2X4cDAtm8IcEwrqkQnIfU(TrashFragment.this, i);
            }
        });
        this.mRecyclerView.setItemAnimator(null);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_scroller_margin_top_to_time_line);
        this.mFastScrollerMarginTop = dimensionPixelSize;
        this.mRecyclerView.setFastScrollerTopMargin(dimensionPixelSize);
        TrashAdapter trashAdapter = new TrashAdapter(this.mActivity);
        this.mTrashAdapter = trashAdapter;
        trashAdapter.setSpacing(dimensionPixelOffset);
        this.mTrashAdapter.setSpanCount(integer);
        this.mRecyclerView.setScrollingCalculator(this.mTrashAdapter);
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper(this.mRecyclerView);
        this.mRecyclerViewWrapper = editableListViewWrapper;
        editableListViewWrapper.setAdapter(this.mTrashAdapter);
        this.mRecyclerViewWrapper.setHandleTouchAnimItemType(TrashGridItem.class.getSimpleName());
        this.mRecyclerViewWrapper.enableChoiceMode(true);
        this.mRecyclerViewWrapper.enterChoiceModeWithLongClick(true);
        this.mRecyclerViewWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.TrashFragment.1
            {
                TrashFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                new PhotoPageIntent.Builder(TrashFragment.this, InternalPhotoPageActivity.class).setAdapterView(TrashFragment.this.mRecyclerView).setUri(GalleryContract.TrashBin.TRASH_BIN_URI).setInitPosition(i).setCount(TrashFragment.this.mTrashAdapter.getItemCount()).setSelection(TrashFragment.this.getSelection()).setOrderBy(TrashFragment.this.getOrderBy()).setImageLoadParams(new ImageLoadParams.Builder().setKey(TrashFragment.this.mTrashAdapter.getItemKey(i)).setFilePath(TrashFragment.this.mTrashAdapter.getBindImagePath(i)).setTargetSize(Config$ThumbConfig.get().sMicroTargetSize).setRegionRect(TrashFragment.this.mTrashAdapter.getItemDecodeRectF(i)).setInitPosition(i).setMimeType(TrashFragment.this.mTrashAdapter.getMimeType(i)).setFileLength(TrashFragment.this.mTrashAdapter.getFileLength(i)).setImageWidth(TrashFragment.this.mTrashAdapter.getImageWidth(i)).setImageHeight(TrashFragment.this.mTrashAdapter.getImageHeight(i)).setCreateTime(TrashFragment.this.mTrashAdapter.getCreateTime(i)).setLocation(TrashFragment.this.mTrashAdapter.getLocation(i)).build()).setUnfoldBurst(true).setNeedConfirmPassword(TrashFragment.this.mTrashAdapter.isSecretPosition(i)).setEnterType(EnterTypeUtils.EnterType.FROM_TRASH).build().gotoPhotoPage();
                HashMap hashMap = new HashMap();
                hashMap.put("from", TrashFragment.this.getPageName());
                hashMap.put("position", Integer.valueOf(i));
                SamplingStatHelper.recordCountEvent("photo", "click_micro_thumb", hashMap);
                return true;
            }
        });
        this.mPurgeAllButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment.2
            {
                TrashFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TrashFragment.this.purgeAll();
                SamplingStatHelper.recordCountEvent("trash_bin", "click_purge_all_button");
            }
        });
        this.mRecyclerViewWrapper.setMultiChoiceModeListener(this.mChoiceListener);
        View findViewById = inflate.findViewById(16908292);
        this.mEmptyView = findViewById;
        findViewById.setVisibility(8);
        this.mRecyclerView.setEmptyView(this.mEmptyView);
        this.mVipInfoText = (TextView) inflate.findViewById(R.id.vip_info);
        this.mVipInfoButton = (LinearLayout) inflate.findViewById(R.id.vip_container);
        refreshUserInfo();
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0(int i) {
        EditableListViewWrapper editableListViewWrapper = this.mRecyclerViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    public final void setPurgeAllButtonLayout() {
        Button button = this.mPurgeAllButton;
        if (button != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) button.getLayoutParams();
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.trash_delete_all_button_margin_start_end);
            layoutParams.rightMargin = dimensionPixelOffset;
            layoutParams.leftMargin = dimensionPixelOffset;
            layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.trash_delete_all_button_margin_bottom) + ((ActivityCompat.isInFreeFormWindow(getContext()) || getActivity().isInMultiWindowMode()) ? 0 : ScreenUtils.getFullScreenNavBarHeight(getContext()));
            this.mPurgeAllButton.setLayoutParams(layoutParams);
        }
    }

    public final void refreshUserInfo() {
        configUserInfo();
        ThreadManager.getMiscPool().submit(new RequestJob(), new ConfigHandler(getActivity(), this));
    }

    /* loaded from: classes2.dex */
    public static class RequestJob implements ThreadPool.Job<Object> {
        public RequestJob() {
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Object mo1807run(ThreadPool.JobContext jobContext) {
            TrashUtils.requestVipInfo();
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static class ConfigHandler extends FutureHandler<Object> {
        public WeakReference<Activity> mActivityRef;
        public WeakReference<TrashFragment> mFragmentRef;

        public ConfigHandler(Activity activity, TrashFragment trashFragment) {
            this.mActivityRef = new WeakReference<>(activity);
            this.mFragmentRef = new WeakReference<>(trashFragment);
        }

        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<Object> future) {
            Activity activity = this.mActivityRef.get();
            TrashFragment trashFragment = this.mFragmentRef.get();
            if (future.isCancelled() || activity == null || activity.isDestroyed() || trashFragment == null || !trashFragment.isUserInfoChanged()) {
                return;
            }
            trashFragment.configUserInfo();
            trashFragment.restartLoader();
        }
    }

    public final void configUserInfo() {
        String quantityString;
        View.OnClickListener onClickListener;
        if (!isUserInfoChanged()) {
            return;
        }
        int ceil = (int) Math.ceil(((System.currentTimeMillis() - TrashUtils.getTrashBinStartMs(this.mLastUserInfo)) * 1.0d) / 8.64E7d);
        String translateVipName = TrashUtils.translateVipName(this.mActivity, this.mLastUserInfo.mVipName);
        if (AccountCache.getAccount() != null) {
            quantityString = getResources().getQuantityString(R.plurals.trash_vip_info_text_login, ceil, Integer.valueOf(ceil), translateVipName);
            if (this.mLastUserInfo.isTopLevel()) {
                getString(R.string.trash_vip_button_login_top_level);
            } else {
                getString(R.string.trash_vip_button_login);
            }
            onClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment.3
                {
                    TrashFragment.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    TrackController.trackClick("403.21.0.1.11276", AutoTracking.getRef());
                    IntentUtil.gotoMiCloudVipPage(TrashFragment.this.mActivity, new Pair("source", IntentUtil.getMiCloudVipPageSource("gallery_banner_recycle")));
                }
            };
        } else {
            quantityString = getResources().getQuantityString(R.plurals.trash_vip_info_text_unlogin, ceil, Integer.valueOf(ceil));
            getString(R.string.trash_vip_button_unlogin);
            onClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment.4
                {
                    TrashFragment.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    IntentUtil.guideToLoginXiaomiAccount(TrashFragment.this.mActivity, GalleryIntent$CloudGuideSource.TRASH_BIN);
                }
            };
        }
        this.mVipInfoText.setText(quantityString);
        this.mVipInfoButton.setOnClickListener(onClickListener);
        FolmeUtil.setDefaultTouchAnim(this.mVipInfoButton, null, false, false, true);
        this.mTrashAdapter.setUserInfo(this.mLastUserInfo);
    }

    public final boolean isUserInfoChanged() {
        TrashUtils.UserInfo lastUserInfo = TrashUtils.getLastUserInfo();
        if (lastUserInfo == null) {
            return false;
        }
        TrashUtils.UserInfo userInfo = this.mLastUserInfo;
        if (userInfo == null) {
            this.mLastUserInfo = lastUserInfo;
            return true;
        } else if (TextUtils.equals(userInfo.mLevel, lastUserInfo.mLevel)) {
            return false;
        } else {
            this.mLastUserInfo = lastUserInfo;
            return true;
        }
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        refreshUserInfo();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).resumeMaml();
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, com.miui.gallery.ui.BaseFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).pauseMaml();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        View view = this.mEmptyView;
        if (view == null || !(view instanceof EmptyPage)) {
            return;
        }
        ((EmptyPage) view).destroyMaml();
    }

    /* loaded from: classes2.dex */
    public class ChoiceModeListener implements MultiChoiceModeListener {
        public ActionMode mMode;

        /* renamed from: $r8$lambda$-MYECB29yYG-r6EMUjz8a1yvRqA */
        public static /* synthetic */ void m1570$r8$lambda$MYECB29yYGr6EMUjz8a1yvRqA(ChoiceModeListener choiceModeListener, DialogInterface dialogInterface, int i) {
            choiceModeListener.lambda$showPurgeConfirmDialog$0(dialogInterface, i);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public ChoiceModeListener() {
            TrashFragment.this = r1;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            TrashFragment.this.mPurgeAllButton.setVisibility(8);
            this.mMode = actionMode;
            actionMode.getMenuInflater().inflate(R.menu.trash_menu, menu);
            enableOrDisableMenuItem(false);
            TrashFragment.this.mTrashAdapter.enterChoiceMode();
            TrackController.trackExpose("403.21.1.1.11280", AutoTracking.getRef());
            return true;
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z) {
            enableOrDisableMenuItem(TrashFragment.this.mRecyclerViewWrapper.getCheckedItemCount() > 0);
        }

        public final void enableOrDisableMenuItem(boolean z) {
            Menu menu = this.mMode.getMenu();
            menu.findItem(R.id.recovery).setEnabled(z);
            menu.findItem(R.id.purge).setEnabled(z);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() != 16908313 && menuItem.getItemId() != 16908314) {
                LinearMotorHelper.performHapticFeedback(TrashFragment.this.mActivity, LinearMotorHelper.HAPTIC_TAP_LIGHT);
            }
            int itemId = menuItem.getItemId();
            if (itemId == R.id.purge) {
                showPurgeConfirmDialog();
                return true;
            } else if (itemId != R.id.recovery) {
                return false;
            } else {
                TimeMonitor.createNewTimeMonitor("403.21.0.1.13768");
                TrashFragment.this.doRecovery();
                actionMode.finish();
                return true;
            }
        }

        public final void showPurgeConfirmDialog() {
            DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment$ChoiceModeListener$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    TrashFragment.ChoiceModeListener.m1570$r8$lambda$MYECB29yYGr6EMUjz8a1yvRqA(TrashFragment.ChoiceModeListener.this, dialogInterface, i);
                }
            };
            int checkedItemCount = TrashFragment.this.mRecyclerViewWrapper.getCheckedItemCount();
            int i = SyncUtil.isGalleryCloudSyncable(TrashFragment.this.mActivity) ? R.plurals.trash_purge_dialog_message_cloud : R.plurals.trash_purge_dialog_message_local;
            TrashDialogFragment newInstance = TrashDialogFragment.newInstance();
            newInstance.setTitle(TrashFragment.this.getString(R.string.photo_purge_dialog_title));
            newInstance.setMessage(TrashFragment.this.getResources().getQuantityString(i, checkedItemCount, Integer.valueOf(checkedItemCount)));
            newInstance.setConfirmButton(TrashFragment.this.getString(R.string.trash_purge_positive_button), onClickListener);
            newInstance.showAllowingStateLoss(TrashFragment.this.getFragmentManager(), "TrashDialogFragment");
        }

        public /* synthetic */ void lambda$showPurgeConfirmDialog$0(DialogInterface dialogInterface, int i) {
            TrashFragment.this.doPurge(false);
            SoundUtils.playSoundForOperation(TrashFragment.this.mActivity, 0);
            this.mMode.finish();
            TimeMonitor.createNewTimeMonitor("403.21.0.1.13769");
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            TrashFragment trashFragment = TrashFragment.this;
            trashFragment.updateConfigChange(trashFragment.getResources().getConfiguration());
            TrashFragment.this.mTrashAdapter.exitChoiceMode();
        }

        @Override // com.miui.gallery.widget.editwrapper.MultiChoiceModeListener
        public void onAllItemsCheckedStateChanged(ActionMode actionMode, boolean z) {
            enableOrDisableMenuItem(TrashFragment.this.mRecyclerViewWrapper.getCheckedItemCount() > 0);
            TrackController.trackClick("403.21.1.1.11277", "403.21.1.1.11280");
        }
    }

    public final ArrayList<TrashBinItem> getSelectTrashBinItems() {
        List<Integer> checkedPositions = this.mRecyclerViewWrapper.getCheckedPositions();
        ArrayList<TrashBinItem> arrayList = new ArrayList<>(checkedPositions.size());
        for (int i = 0; i < checkedPositions.size(); i++) {
            arrayList.add(this.mTrashAdapter.getTrashBinItem(checkedPositions.get(i).intValue()));
        }
        return arrayList;
    }

    public final void doRecovery() {
        final ArrayList<TrashBinItem> selectTrashBinItems = getSelectTrashBinItems();
        if (!BaseMiscUtil.isValid(selectTrashBinItems)) {
            TimeMonitor.cancelTimeMonitor("403.21.0.1.13768");
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.TrashFragment$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public final Object doProcess(Object[] objArr) {
                return TrashFragment.$r8$lambda$WTwZXWwB5aU1W9Tz1OWSIDabJRA(TrashFragment.this, selectTrashBinItems, (Void[]) objArr);
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.ui.TrashFragment.5
            {
                TrashFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                DefaultLogger.d("TrashFragment", "recovery %d item, cost %d", Integer.valueOf(selectTrashBinItems.size()), Long.valueOf(currentTimeMillis2));
                HashMap hashMap = new HashMap();
                hashMap.put("tip", "403.21.0.1.13768");
                hashMap.put("ref_tip", "403.21.1.1.11279");
                hashMap.put(MiStat.Param.COUNT, Integer.valueOf(selectTrashBinItems.size()));
                hashMap.put("count_extra", Integer.valueOf(TrashFragment.this.mOriginCount));
                TimeMonitor.trackTimeMonitor("403.21.0.1.13768", hashMap);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("cost_time", String.valueOf(currentTimeMillis2));
                hashMap2.put(MiStat.Param.COUNT, String.valueOf(selectTrashBinItems.size()));
                SamplingStatHelper.recordCountEvent("trash_bin", "recovery_event", hashMap2);
                TrackController.trackClick("403.21.1.1.11279", "403.21.1.1.11280", selectTrashBinItems.size());
            }
        });
        processTask.showProgress(this.mActivity, getString(R.string.purge_or_recovery_processing));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public /* synthetic */ Integer lambda$doRecovery$1(ArrayList arrayList, Void[] voidArr) {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            TrashUtils.doRecovery(appCompatActivity, arrayList);
            return null;
        }
        return null;
    }

    public final void doPurge(final boolean z) {
        ArrayList<TrashBinItem> selectTrashBinItems;
        if (z) {
            selectTrashBinItems = new ArrayList<>(this.mTrashAdapter.getItemCount());
            for (int i = 0; i < this.mTrashAdapter.getItemCount(); i++) {
                selectTrashBinItems.add(this.mTrashAdapter.getTrashBinItem(i));
            }
        } else {
            selectTrashBinItems = getSelectTrashBinItems();
        }
        final ArrayList<TrashBinItem> arrayList = selectTrashBinItems;
        if (!BaseMiscUtil.isValid(arrayList)) {
            TimeMonitor.cancelTimeMonitor(z ? "403.21.0.1.13770" : "403.21.0.1.13769");
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.ui.TrashFragment$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public final Object doProcess(Object[] objArr) {
                return TrashFragment.$r8$lambda$TXL25W90iuw8rkzpGyGT2TR45pk(TrashFragment.this, arrayList, (Void[]) objArr);
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.ui.TrashFragment.6
            {
                TrashFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                DefaultLogger.d("TrashFragment", "purge %d item, cost %d", Integer.valueOf(arrayList.size()), Long.valueOf(currentTimeMillis2));
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", String.valueOf(currentTimeMillis2));
                hashMap.put(MiStat.Param.COUNT, String.valueOf(arrayList.size()));
                SamplingStatHelper.recordCountEvent("trash_bin", "purge_event", hashMap);
                if (z) {
                    TimeMonitor.trackTimeMonitor("403.21.0.1.13770", arrayList.size());
                    TrackController.trackClick("403.21.0.1.11275", AutoTracking.getRef());
                    return;
                }
                HashMap hashMap2 = new HashMap();
                hashMap2.put("tip", "403.21.0.1.13769");
                hashMap2.put("ref_tip", "403.21.1.1.11278");
                hashMap2.put(MiStat.Param.COUNT, Integer.valueOf(arrayList.size()));
                hashMap2.put("count_extra", Integer.valueOf(TrashFragment.this.mOriginCount));
                TimeMonitor.trackTimeMonitor("403.21.0.1.13769", hashMap2);
                TrackController.trackClick("403.21.1.1.11278", "403.21.1.1.11280", arrayList.size());
            }
        });
        processTask.showProgress(this.mActivity, getString(R.string.purge_or_recovery_processing));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public /* synthetic */ Integer lambda$doPurge$2(ArrayList arrayList, Void[] voidArr) {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            TrashUtils.doPurge(appCompatActivity, arrayList);
            return null;
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public class TrashedPhotoLoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<Cursor> loader) {
        }

        public TrashedPhotoLoaderCallback() {
            TrashFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(TrashFragment.this.mActivity);
            TrashFragment.this.configLoader(cursorLoader);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            TrashFragment.this.onFirstLoadFinished();
            TrashFragment.this.mTrashAdapter.swapCursor(cursor);
            if (TrashFragment.this.mTrashAdapter.getItemCount() == 0) {
                TrashFragment.this.mEmptyView.setVisibility(0);
                TrashFragment.this.mPurgeAllButton.setVisibility(8);
                TrashFragment.this.mTrashButton.setVisibility(8);
            } else {
                TrashFragment.this.mEmptyView.setVisibility(8);
                if (!TrashFragment.this.mRecyclerViewWrapper.isInActionMode()) {
                    if (TrashFragment.this.getResources().getConfiguration().orientation == 2) {
                        TrashFragment.this.mPurgeAllButton.setVisibility(8);
                        TrashFragment.this.mTrashButton.setVisibility(0);
                    } else {
                        TrashFragment.this.mPurgeAllButton.setVisibility(0);
                        TrashFragment.this.mTrashButton.setVisibility(8);
                    }
                }
            }
            TrashFragment.this.refreshCountSpace();
        }
    }

    public final void onFirstLoadFinished() {
        if (!this.mHasFirstLoadFinished) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.TrashFragment.7
                {
                    TrashFragment.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (!TrashFragment.this.isAdded() || TrashFragment.this.mHasFirstLoadFinished) {
                        return;
                    }
                    TrashFragment.this.mHasFirstLoadFinished = true;
                    Loader loader = LoaderManager.getInstance(TrashFragment.this).getLoader(1);
                    TrashFragment.this.configLoader((CursorLoader) loader);
                    loader.onContentChanged();
                }
            });
        }
    }

    public void configLoader(CursorLoader cursorLoader) {
        Uri build;
        if (this.mHasFirstLoadFinished) {
            build = GalleryContract.TrashBin.TRASH_BIN_URI;
        } else {
            build = GalleryContract.TrashBin.TRASH_BIN_URI.buildUpon().appendQueryParameter("limit", String.valueOf(30)).build();
        }
        cursorLoader.setUri(build);
        cursorLoader.setProjection(TrashAdapter.PROJECTION);
        cursorLoader.setSelection(getSelection());
        cursorLoader.setSortOrder(getOrderBy());
    }

    public final void restartLoader() {
        Loader loader;
        if (!isAdded() || (loader = getLoaderManager().getLoader(1)) == null) {
            return;
        }
        configLoader((CursorLoader) loader);
        loader.forceLoad();
    }

    public final String getSelection() {
        TrashUtils.UserInfo lastUserInfo = TrashUtils.getLastUserInfo();
        return "deleteTime>=" + TrashUtils.getTrashBinStartMs(lastUserInfo) + " AND status=0";
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initLoader();
        ImageView imageView = (ImageView) ActionBarCompat.setCustomEndViewOnly(getAppCompatActivity(), R.layout.trash_action_bar);
        this.mTrashButton = imageView;
        imageView.setBackground(getResources().getDrawable(R.drawable.trash_delete_all_btn));
        FolmeUtil.addAlphaPressAnim(this.mTrashButton);
        this.mTrashButton.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TrashFragment.$r8$lambda$awMaeytJwNqxwsGQa93nfsqX_3g(TrashFragment.this, view);
            }
        });
        this.mTrashButton.setVisibility(8);
        updateConfigChange(getResources().getConfiguration());
    }

    public /* synthetic */ void lambda$onActivityCreated$3(View view) {
        purgeAll();
    }

    public final void initLoader() {
        this.mTrashedPhotoLoaderCallback = new TrashedPhotoLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mTrashedPhotoLoaderCallback);
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfigChange(configuration);
    }

    public void updateConfigChange(Configuration configuration) {
        setPurgeAllButtonLayout();
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        this.mRecyclerView.invalidateItemDecorations();
        int i = 0;
        if (configuration.orientation == 2) {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.thumbnail_columns_land));
            this.mPurgeAllButton.setVisibility(8);
            ImageView imageView = this.mTrashButton;
            if (this.mRecyclerViewWrapper.getCheckedItemCount() > 0 || this.mTrashAdapter.getItemCount() == 0) {
                i = 8;
            }
            imageView.setVisibility(i);
        } else {
            ((GridLayoutManager) this.mRecyclerView.getLayoutManager()).setSpanCount(getResources().getInteger(R.integer.thumbnail_columns));
            this.mTrashButton.setVisibility(8);
            Button button = this.mPurgeAllButton;
            if (this.mRecyclerViewWrapper.getCheckedItemCount() > 0 || this.mTrashAdapter.getItemCount() == 0) {
                i = 8;
            }
            button.setVisibility(i);
        }
        this.mRecyclerView.scrollToPosition(findFirstVisibleItemPosition);
    }

    public final void refreshCountSpace() {
        Future future = this.mCountSpaceFuture;
        if (future != null) {
            future.cancel();
        }
        int itemCount = this.mTrashAdapter.getItemCount();
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        long j = 0;
        if (itemCount > 0) {
            long j2 = 0;
            for (int i = 0; i < itemCount; i++) {
                try {
                    long longValue = this.mTrashAdapter.getImageSize(i).longValue();
                    if (longValue < 0) {
                        if (this.mTrashAdapter.getIsOrigin(i)) {
                            String serviceId = this.mTrashAdapter.getServiceId(i);
                            if (TextUtils.isEmpty(serviceId)) {
                                String filePath = this.mTrashAdapter.getFilePath(i);
                                if (!TextUtils.isEmpty(filePath)) {
                                    arrayList2.add(filePath);
                                }
                            } else {
                                arrayList.add(serviceId);
                            }
                        } else if (!TextUtils.isEmpty(this.mTrashAdapter.getFilePath(i))) {
                            longValue = 200000;
                        } else if (!TextUtils.isEmpty(this.mTrashAdapter.getMicroThumbFilePath(i))) {
                            longValue = 15000;
                        }
                    }
                    j2 += longValue;
                } catch (CursorIndexOutOfBoundsException e) {
                    DefaultLogger.e("TrashFragment", "error when refresh count space", e);
                }
            }
            j = j2;
        }
        this.mCountSpaceFuture = ThreadManager.getMiscPool().submit(new CountSpaceJob(arrayList, arrayList2, j), new CountSpaceHandler(getActivity()));
    }

    /* loaded from: classes2.dex */
    public static class CountSpaceJob implements ThreadPool.Job<Long> {
        public long mCount;
        public ArrayList<String> mServiceIds;
        public ArrayList<String> mTrashFilePaths;

        public CountSpaceJob(ArrayList<String> arrayList, ArrayList<String> arrayList2, long j) {
            this.mCount = 0L;
            this.mCount = j;
            this.mTrashFilePaths = arrayList2;
            this.mServiceIds = arrayList;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Long mo1807run(ThreadPool.JobContext jobContext) {
            if (BaseMiscUtil.isValid(this.mTrashFilePaths)) {
                int size = this.mTrashFilePaths.size();
                for (int i = 0; i < size; i++) {
                    this.mCount += new File(this.mTrashFilePaths.get(i)).length();
                }
            }
            if (BaseMiscUtil.isValid(this.mServiceIds)) {
                this.mCount += ((Long) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE}, "serverId IN (" + TextUtils.join(",", this.mServiceIds) + ")", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Long>() { // from class: com.miui.gallery.ui.TrashFragment.CountSpaceJob.1
                    {
                        CountSpaceJob.this = this;
                    }

                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public Long mo1808handle(Cursor cursor) {
                        long j = 0;
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                j += cursor.getLong(0);
                            } while (cursor.moveToNext());
                            return Long.valueOf(j);
                        }
                        return Long.valueOf(j);
                    }
                })).longValue();
            }
            return Long.valueOf(this.mCount);
        }
    }

    /* loaded from: classes2.dex */
    public static class CountSpaceHandler extends FutureHandler<Long> {
        public WeakReference<Activity> mActivityRef;

        public CountSpaceHandler(Activity activity) {
            this.mActivityRef = new WeakReference<>(activity);
        }

        @Override // com.miui.gallery.concurrent.FutureHandler
        public void onPostExecute(Future<Long> future) {
            Activity activity;
            if (future == null || future.isCancelled() || (activity = this.mActivityRef.get()) == null || activity.isDestroyed()) {
                return;
            }
            ((AppCompatActivity) activity).getAppCompatActionBar().setTitle(String.format(activity.getString(R.string.trash_size), FormatUtil.formatFileSize(activity, future.get().longValue())));
            TimeMonitor.trackTimeMonitor("403.21.0.1.13766", future.get().longValue());
        }
    }

    public final void purgeAll() {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.TrashFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TrashFragment.$r8$lambda$x6fcuU_0wsBYdT9mDhepngtMHeg(TrashFragment.this, dialogInterface, i);
            }
        };
        int i = SyncUtil.isGalleryCloudSyncable(this.mActivity) ? R.string.trash_purge_all_cloud : R.string.trash_purge_all_local;
        TrashDialogFragment newInstance = TrashDialogFragment.newInstance();
        newInstance.setTitle(getString(R.string.trash_purge_dialog_title));
        newInstance.setMessage(getString(i));
        newInstance.setConfirmButton(getString(R.string.trash_purge_positive_button), onClickListener);
        newInstance.showAllowingStateLoss(getFragmentManager(), "TrashDialogFragment");
    }

    public /* synthetic */ void lambda$purgeAll$4(DialogInterface dialogInterface, int i) {
        doPurge(true);
        SoundUtils.playSoundForOperation(this.mActivity, 0);
        TimeMonitor.createNewTimeMonitor("403.21.0.1.13770");
    }

    @Override // com.miui.gallery.ui.BaseMediaFragment
    public List<Loader<?>> getLoaders() {
        return Collections.singletonList(LoaderManager.getInstance(this).getLoader(1));
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mHasFirstLoadFinished = false;
        TrashAdapter trashAdapter = this.mTrashAdapter;
        if (trashAdapter != null) {
            trashAdapter.swapCursor(null);
        }
        TimeMonitor.cancelTimeMonitor("403.21.0.1.13766");
        super.onDestroy();
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (this.mRecyclerViewWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class TrashKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public TrashKeyboardShortcutCallback() {
            TrashFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (TrashFragment.this.mRecyclerViewWrapper.isInActionMode()) {
                TrashFragment.this.mRecyclerViewWrapper.setAllItemsCheckState(true);
            }
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (TrashFragment.this.mRecyclerViewWrapper.isInActionMode()) {
                TrashFragment.this.mChoiceListener.showPurgeConfirmDialog();
                return true;
            }
            return true;
        }
    }
}
