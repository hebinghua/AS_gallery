package com.miui.gallery.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.activity.facebaby.InputFaceNameActivity;
import com.miui.gallery.adapter.IgnorePeoplePageAdapter;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.util.face.PeopleCursorHelper;
import com.miui.gallery.widget.EmptyPage;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class IgnorePeoplePageFragment extends BaseFragment {
    public IgnorePeoplePageAdapter mAdapter;
    public EmptyPage mEmptyView;
    public ViewStub mEmptyViewStub;
    public GridView mIgnorePeopleGridView;
    public IgnorePeoplePageLoaderCallback mIgnorePeoplePageLoaderCallback;
    public NormalPeopleFaceMediaSet mPeopleToRecovery;
    public AlertDialog mRecoveryDialog;
    public AlertDialog mRenameOrMergeDialog;
    public Handler mHandler = new Handler();
    public IgnorePeoplePageAdapter.OnRecoveryButtonClickListener mRecoveryListener = new IgnorePeoplePageAdapter.OnRecoveryButtonClickListener() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.1
        @Override // com.miui.gallery.adapter.IgnorePeoplePageAdapter.OnRecoveryButtonClickListener
        public void onPeopleRecoveryButtonClick(final String str, final String str2, final String str3, String str4, FaceRegionRectF faceRegionRectF) {
            View inflate = LayoutInflater.from(IgnorePeoplePageFragment.this.mActivity).inflate(R.layout.ignore_to_visible_dialog, (ViewGroup) null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(IgnorePeoplePageFragment.this.mActivity);
            IgnorePeoplePageFragment.this.mRecoveryDialog = builder.setView(inflate).setPositiveButton(17039370, (DialogInterface.OnClickListener) null).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
            ImageView imageView = (ImageView) inflate.findViewById(R.id.ignore_face);
            IgnorePeoplePageFragment.this.mRecoveryDialog.show();
            Glide.with(imageView).mo985asBitmap().mo962load(GalleryModel.of(str4)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(faceRegionRectF)).into(imageView);
            final Button button = IgnorePeoplePageFragment.this.mRecoveryDialog.getButton(-1);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.1.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    button.setEnabled(false);
                    IgnorePeoplePageFragment ignorePeoplePageFragment = IgnorePeoplePageFragment.this;
                    ignorePeoplePageFragment.recoveryPeople(str, str2, str3, ignorePeoplePageFragment.mRecoveryDialog);
                }
            });
        }
    };

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "ignore_people";
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.ignore_people_page, viewGroup, false);
        this.mIgnorePeopleGridView = (GridView) inflate.findViewById(R.id.grid);
        IgnorePeoplePageAdapter ignorePeoplePageAdapter = new IgnorePeoplePageAdapter(this.mActivity);
        this.mAdapter = ignorePeoplePageAdapter;
        ignorePeoplePageAdapter.setRecoveryListener(this.mRecoveryListener);
        this.mIgnorePeopleGridView.setAdapter((ListAdapter) this.mAdapter);
        this.mEmptyViewStub = (ViewStub) inflate.findViewById(R.id.empty_view);
        return inflate;
    }

    public final void recoveryPeople(final String str, final String str2, final String str3, final AlertDialog alertDialog) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.2
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                String str4 = str3;
                if (!TextUtils.isEmpty(str4)) {
                    str4 = str3.split("-", 2)[0];
                }
                final NormalPeopleFaceMediaSet normalPeopleFaceMediaSet = new NormalPeopleFaceMediaSet(Long.parseLong(str), str2, str3);
                final PeopleFace groupByPeopleName = FaceDataManager.getGroupByPeopleName(IgnorePeoplePageFragment.this.mActivity, str4);
                IgnorePeoplePageFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        IgnorePeoplePageFragment.this.dismissDialog(alertDialog);
                    }
                });
                if (groupByPeopleName == null) {
                    IgnorePeoplePageFragment.this.doRecovery(normalPeopleFaceMediaSet, str4);
                    return null;
                }
                IgnorePeoplePageFragment.this.mHandler.post(new Runnable() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IgnorePeoplePageFragment.this.showMergeOrRenameDialog(normalPeopleFaceMediaSet, groupByPeopleName.peopleName);
                    }
                });
                return null;
            }
        });
    }

    public final void doRecovery(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, String str) {
        String l = Long.toString(normalPeopleFaceMediaSet.getBucketId());
        ArrayList arrayList = new ArrayList();
        arrayList.add(l);
        ContentValues contentValues = new ContentValues();
        if (FaceManager.getPeopleLocalFlagByLocalID(l) != 8) {
            contentValues.put("localFlag", (Integer) 14);
            contentValues.put("visibilityType", (Integer) 4);
        } else {
            contentValues.put("visibilityType", (Integer) 1);
        }
        contentValues.put("peopleName", str);
        FaceManager.safeUpdatePeopleFaceByIds(contentValues, arrayList);
    }

    public final void showMergeOrRenameDialog(final NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, final String str) {
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.3.1
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        AnonymousClass3 anonymousClass3 = AnonymousClass3.this;
                        IgnorePeoplePageFragment.this.mergeWhenRecovery(normalPeopleFaceMediaSet, str);
                        return null;
                    }
                });
            }
        };
        AlertDialog create = new AlertDialog.Builder(this.mActivity).setTitle(R.string.recovery_merge_or_rename).setMessage(R.string.recovery_merge_or_rename_message).setPositiveButton(R.string.recovery_merge, onClickListener).setNegativeButton(R.string.recovery_rename, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                IgnorePeoplePageFragment.this.mPeopleToRecovery = normalPeopleFaceMediaSet;
                Intent intent = new Intent(IgnorePeoplePageFragment.this.mActivity, InputFaceNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("original_name", str);
                intent.putExtras(bundle);
                IgnorePeoplePageFragment.this.mActivity.startActivityForResult(intent, 39);
            }
        }).create();
        this.mRenameOrMergeDialog = create;
        create.show();
    }

    public final void mergeWhenRecovery(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet, String str) {
        synchronized (FaceDataManager.PEOPLE_FACE_URI) {
            ContentValues contentValues = new ContentValues();
            if (FaceManager.getPeopleLocalFlagByLocalID(Long.toString(normalPeopleFaceMediaSet.getBucketId())) != 8) {
                contentValues.put("localFlag", (Integer) 14);
                contentValues.put("visibilityType", (Integer) 4);
            } else {
                contentValues.put("visibilityType", (Integer) 1);
            }
            FaceDataManager.safeUpdateFace(contentValues, String.format("%s = ? and %s != ? ", j.c, "localFlag"), new String[]{Long.toString(normalPeopleFaceMediaSet.getBucketId()), String.valueOf(2)});
            normalPeopleFaceMediaSet.rename((Context) this.mActivity, str, false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        setTitle();
        this.mIgnorePeoplePageLoaderCallback = new IgnorePeoplePageLoaderCallback();
        getLoaderManager().initLoader(1, null, this.mIgnorePeoplePageLoaderCallback);
    }

    public final void setTitle() {
        AppCompatActivity appCompatActivity = this.mActivity;
        if (appCompatActivity != null) {
            appCompatActivity.getAppCompatActionBar().setTitle(getString(R.string.ignore_faces_title));
        }
    }

    /* loaded from: classes2.dex */
    public class IgnorePeoplePageLoaderCallback implements LoaderManager.LoaderCallbacks {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public IgnorePeoplePageLoaderCallback() {
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(IgnorePeoplePageFragment.this.mActivity);
            cursorLoader.setUri(GalleryContract.PeopleFace.IGNORE_PERSONS_URI);
            cursorLoader.setProjection(PeopleCursorHelper.PROJECTION);
            return cursorLoader;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            Cursor cursor = (Cursor) obj;
            if (cursor == null || cursor.getCount() == 0) {
                if (IgnorePeoplePageFragment.this.mEmptyView == null) {
                    IgnorePeoplePageFragment ignorePeoplePageFragment = IgnorePeoplePageFragment.this;
                    ignorePeoplePageFragment.mEmptyView = (EmptyPage) ignorePeoplePageFragment.mEmptyViewStub.inflate();
                    IgnorePeoplePageFragment.this.mEmptyView.setTitle(R.string.no_ignore_faces);
                    IgnorePeoplePageFragment.this.mEmptyView.setActionButtonVisible(false);
                }
                IgnorePeoplePageFragment.this.mIgnorePeopleGridView.setEmptyView(IgnorePeoplePageFragment.this.mEmptyView);
            }
            IgnorePeoplePageFragment.this.mAdapter.swapCursor(cursor);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 39) {
            if (intent == null || this.mPeopleToRecovery == null) {
                return;
            }
            final String string = intent.getExtras().getString("name");
            if (!TextUtils.isEmpty(string)) {
                String checkFileNameValid = CreateGroupItem.checkFileNameValid(this.mActivity, string);
                if (!TextUtils.isEmpty(checkFileNameValid)) {
                    ToastUtils.makeText(this.mActivity, checkFileNameValid);
                    return;
                }
            }
            if (!intent.getExtras().getBoolean("is_repeat_name")) {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.5
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        IgnorePeoplePageFragment ignorePeoplePageFragment = IgnorePeoplePageFragment.this;
                        ignorePeoplePageFragment.doRecovery(ignorePeoplePageFragment.mPeopleToRecovery, string);
                        return null;
                    }
                });
                return;
            } else {
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.IgnorePeoplePageFragment.6
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run  reason: collision with other method in class */
                    public Void mo1807run(ThreadPool.JobContext jobContext) {
                        IgnorePeoplePageFragment ignorePeoplePageFragment = IgnorePeoplePageFragment.this;
                        ignorePeoplePageFragment.mergeWhenRecovery(ignorePeoplePageFragment.mPeopleToRecovery, string);
                        return null;
                    }
                });
                return;
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        dismissDialog(this.mRecoveryDialog);
        dismissDialog(this.mRenameOrMergeDialog);
        IgnorePeoplePageAdapter ignorePeoplePageAdapter = this.mAdapter;
        if (ignorePeoplePageAdapter != null) {
            ignorePeoplePageAdapter.swapCursor(null);
        }
    }

    public void dismissDialog(AlertDialog alertDialog) {
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        alertDialog.dismiss();
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int firstVisiblePosition = this.mIgnorePeopleGridView.getFirstVisiblePosition();
        if (configuration.orientation == 2) {
            this.mIgnorePeopleGridView.setNumColumns(getResources().getInteger(R.integer.people_face_grid_view_columns_land));
        } else {
            this.mIgnorePeopleGridView.setNumColumns(getResources().getInteger(R.integer.people_face_grid_view_columns));
        }
        this.mIgnorePeopleGridView.setSelection(firstVisiblePosition);
    }
}
