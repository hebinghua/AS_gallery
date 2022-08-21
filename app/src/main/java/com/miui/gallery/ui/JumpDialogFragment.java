package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import com.miui.account.AccountHelper;
import com.miui.gallery.R;
import com.miui.gallery.activity.AlbumDetailGroupingActivity;
import com.miui.gallery.activity.facebaby.BabyAlbumDetailActivity;
import com.miui.gallery.loader.AlbumConvertCallback;
import com.miui.gallery.loader.AlbumSnapshotLoader;
import com.miui.gallery.loader.AsyncContentLoader;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.util.AlbumsCursorHelper;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class JumpDialogFragment extends GalleryDialogFragment {
    public HandleIntentCallback mObtainIntentCallBack;
    public HandleIntentCallback mHandleIntentCallback = new HandleIntentCallback() { // from class: com.miui.gallery.ui.JumpDialogFragment.1
        @Override // com.miui.gallery.ui.JumpDialogFragment.HandleIntentCallback
        public void onHandleIntent(final Intent intent) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.JumpDialogFragment.1.1
                @Override // java.lang.Runnable
                public void run() {
                    if (JumpDialogFragment.this.getActivity() == null) {
                        return;
                    }
                    if (JumpDialogFragment.this.mObtainIntentCallBack != null) {
                        JumpDialogFragment.this.mObtainIntentCallBack.onHandleIntent(intent);
                    } else {
                        Intent intent2 = intent;
                        if (intent2 != null) {
                            JumpDialogFragment.this.startActivity(intent2);
                        }
                    }
                    JumpDialogFragment.this.dismissAllowingStateLoss();
                }
            });
        }

        @Override // com.miui.gallery.ui.JumpDialogFragment.HandleIntentCallback
        public void onHandleFailed(final Context context, final String str) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.JumpDialogFragment.1.2
                @Override // java.lang.Runnable
                public void run() {
                    if (JumpDialogFragment.this.getActivity() == null) {
                        return;
                    }
                    if (JumpDialogFragment.this.mObtainIntentCallBack != null) {
                        JumpDialogFragment.this.mObtainIntentCallBack.onHandleFailed(context, str);
                    } else if (!TextUtils.isEmpty(str)) {
                        ToastUtils.makeText(JumpDialogFragment.this.getActivity(), str);
                    }
                    JumpDialogFragment.this.dismissAllowingStateLoss();
                }
            });
        }
    };
    public Runnable mDelayVisibleRunnable = new Runnable() { // from class: com.miui.gallery.ui.JumpDialogFragment.2
        @Override // java.lang.Runnable
        public void run() {
            if (JumpDialogFragment.this.getActivity() == null) {
                return;
            }
            JumpDialogFragment jumpDialogFragment = JumpDialogFragment.this;
            jumpDialogFragment.setDialogAlpha(jumpDialogFragment.getDialog(), 1.0f);
        }
    };

    /* loaded from: classes2.dex */
    public interface HandleIntentCallback {
        void onHandleFailed(Context context, String str);

        void onHandleIntent(Intent intent);
    }

    public static void showAlbumPage(FragmentActivity fragmentActivity, Uri uri, Bundle bundle) {
        JumpDialogFragment jumpDialogFragment = new JumpDialogFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable(CallMethod.ARG_URI, uri);
        bundle2.putInt("pageType", 0);
        if (bundle != null) {
            bundle2.putAll(bundle);
        }
        jumpDialogFragment.setArguments(bundle2);
        jumpDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "JumpDialogFragment");
    }

    public static void showAlbumPage(FragmentActivity fragmentActivity, Uri uri) {
        showAlbumPage(fragmentActivity, uri, null);
    }

    public static void obtainAlbumIntent(FragmentActivity fragmentActivity, Uri uri, HandleIntentCallback handleIntentCallback) {
        JumpDialogFragment jumpDialogFragment = new JumpDialogFragment();
        Bundle bundle = new Bundle();
        if (uri.equals(GalleryContract.Favorites.SHORTCUT_URI)) {
            uri = GalleryContract.Favorites.SHORTCUT_FULL_URI;
        }
        bundle.putParcelable(CallMethod.ARG_URI, uri);
        bundle.putInt("pageType", 0);
        jumpDialogFragment.setArguments(bundle);
        jumpDialogFragment.setHandleIntentCallback(handleIntentCallback);
        jumpDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "JumpDialogFragment");
    }

    public static void showPeoplePage(FragmentActivity fragmentActivity, String str) {
        JumpDialogFragment jumpDialogFragment = new JumpDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("serverId", str);
        bundle.putInt("pageType", 1);
        jumpDialogFragment.setArguments(bundle);
        jumpDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "JumpDialogFragment");
    }

    public static void enterPrivateAlbum(FragmentActivity fragmentActivity) {
        JumpDialogFragment jumpDialogFragment = new JumpDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pageType", 2);
        jumpDialogFragment.setArguments(bundle);
        jumpDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "JumpDialogFragment");
    }

    public void setHandleIntentCallback(HandleIntentCallback handleIntentCallback) {
        this.mObtainIntentCallBack = handleIntentCallback;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog show = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.loading_dots), true, false);
        setDialogAlpha(show, 0.0f);
        setCancelable(false);
        return show;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ThreadManager.getMainHandler().postDelayed(this.mDelayVisibleRunnable, getResources().getInteger(17694722));
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        ThreadManager.getMainHandler().removeCallbacks(this.mDelayVisibleRunnable);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        String str;
        String str2;
        super.onActivityCreated(bundle);
        int i = getArguments().getInt("pageType", -1);
        if (i != 0) {
            if (i == 1) {
                DefaultLogger.d("JumpDialogFragment", "Jump to people page, people serverId = %s", getArguments().getString("serverId"));
                new PeopleJumpHelper(this).startLoading(this.mHandleIntentCallback, getArguments().getString("serverId"));
                return;
            } else if (i == 2) {
                DefaultLogger.d("JumpDialogFragment", "Jump secret album");
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SECRET);
                LoginAndSyncCheckFragment.checkLoginAndSyncState(this, bundle2);
                return;
            } else {
                DefaultLogger.e("JumpDialogFragment", "Invalid page type %d", Integer.valueOf(i));
                dismiss();
                return;
            }
        }
        Album album = (Album) getArguments().getParcelable("album_source");
        DefaultLogger.d("JumpDialogFragment", "Jump to album page, album serverId = %s", getArguments().getString("serverId"));
        Uri uri = (Uri) getArguments().getParcelable(CallMethod.ARG_URI);
        String str3 = null;
        if (uri != null) {
            String queryParameter = uri.getQueryParameter("id");
            String queryParameter2 = uri.getQueryParameter("query");
            str2 = uri.getQueryParameter("querySelection");
            str = queryParameter;
            str3 = queryParameter2;
        } else {
            str = null;
            str2 = null;
        }
        if (str3 != null && !str3.isEmpty() && str != null && (str.equals(String.valueOf(2147383646)) || str.equals(String.valueOf(2147383645)))) {
            Intent intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
            intent.putExtra("album_name", str3);
            intent.putExtra("album_id", Long.valueOf(str));
            intent.putExtra("photo_selection", str2);
            String queryParameter3 = uri.getQueryParameter("extra_from_type");
            if (!TextUtils.isEmpty(queryParameter3)) {
                intent.putExtra("extra_from_type", Integer.valueOf(queryParameter3));
            }
            startActivity(intent);
            dismissAllowingStateLoss();
            return;
        }
        AlbumJumpHelper albumJumpHelper = new AlbumJumpHelper(this, this.mHandleIntentCallback);
        if (album == null) {
            albumJumpHelper.startLoading(uri);
        } else {
            albumJumpHelper.gotoAlbumDetailPage(album);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            this.mHandleIntentCallback.onHandleIntent(null);
        } else if (i != 29) {
            if (i == 36) {
                IntentUtil.enterPrivateAlbum(getActivity());
                this.mHandleIntentCallback.onHandleIntent(null);
            }
        } else if (AccountHelper.getXiaomiAccount(getActivity()) != null) {
            AuthenticatePrivacyPasswordFragment.startAuthenticatePrivacyPassword(this);
        }
        super.onActivityResult(i, i2, intent);
    }

    public final void setDialogAlpha(Dialog dialog, float f) {
        if (dialog == null || dialog.getWindow() == null) {
            return;
        }
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.alpha = f;
        dialog.getWindow().setAttributes(attributes);
    }

    /* loaded from: classes2.dex */
    public static class AlbumJumpHelper implements LoaderManager.LoaderCallbacks {
        public Album mAlbum;
        public long mAlbumLoadTime;
        public Fragment mFragment;
        public HandleIntentCallback mHandleIntentCallback;
        public Cursor mShareAlbumCursor;
        public Uri mUri;
        public Bundle mUriParameters;

        public AlbumJumpHelper(Fragment fragment, HandleIntentCallback handleIntentCallback) {
            this.mFragment = fragment;
            this.mHandleIntentCallback = handleIntentCallback;
        }

        public void startLoading(Uri uri) {
            this.mUri = uri;
            String queryParameter = uri.getQueryParameter("serverId");
            String queryParameter2 = this.mUri.getQueryParameter("id");
            String queryParameter3 = this.mUri.getQueryParameter("local_path");
            long longValue = !TextUtils.isEmpty(queryParameter2) ? Long.valueOf(queryParameter2).longValue() : -1L;
            if (longValue < 0 && TextUtils.isEmpty(queryParameter) && TextUtils.isEmpty(queryParameter3)) {
                this.mHandleIntentCallback.onHandleFailed(this.mFragment.getActivity(), this.mFragment.getString(R.string.album_jump_failed));
                return;
            }
            Intent shortCutIntent = getShortCutIntent(longValue, queryParameter3);
            if (shortCutIntent != null) {
                this.mHandleIntentCallback.onHandleIntent(shortCutIntent);
                return;
            }
            Bundle bundle = new Bundle();
            this.mUriParameters = bundle;
            bundle.putString("serverId", queryParameter);
            this.mUriParameters.putLong("id", longValue);
            this.mUriParameters.putString("localPath", queryParameter3);
            this.mFragment.getLoaderManager().initLoader(3, this.mUriParameters, this);
        }

        public final String generateAlbumSelection(Bundle bundle, boolean z) {
            if (bundle.getLong("id", -1L) >= 0) {
                Object[] objArr = new Object[2];
                objArr[0] = z ? "album_id" : j.c;
                objArr[1] = Long.valueOf(bundle.getLong("id"));
                return String.format("%s=%s", objArr);
            } else if (!TextUtils.isEmpty(bundle.getString("serverId"))) {
                return String.format("%s='%s'", "serverId", bundle.getString("serverId"));
            } else {
                return String.format("%s='%s' COLLATE NOCASE", "localPath", bundle.getString("localPath"));
            }
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            if (i == 1) {
                this.mAlbumLoadTime = System.currentTimeMillis();
                AsyncContentLoader asyncContentLoader = new AsyncContentLoader(this.mFragment.getActivity(), new AlbumConvertCallback());
                asyncContentLoader.setUri(GalleryContract.Album.URI_QUERY_ALL_AND_EXCEPT_DELETED);
                asyncContentLoader.setProjection(AlbumManager.QUERY_ALBUM_PROJECTION);
                asyncContentLoader.setSelection(generateAlbumSelection(bundle, false));
                return asyncContentLoader;
            } else if (i == 2) {
                CursorLoader cursorLoader = new CursorLoader(this.mFragment.getActivity());
                cursorLoader.setUri(GalleryContract.Album.URI_SHARE_ALL);
                cursorLoader.setProjection(AlbumManager.SHARED_ALBUM_PROJECTION);
                cursorLoader.setSelection(String.format("%s>0 AND %s=%s", MiStat.Param.COUNT, j.c, Long.valueOf(bundle.getLong("id"))));
                return cursorLoader;
            } else if (i != 3) {
                return null;
            } else {
                this.mAlbumLoadTime = System.currentTimeMillis();
                AlbumSnapshotLoader albumSnapshotLoader = new AlbumSnapshotLoader(this.mFragment.getActivity());
                albumSnapshotLoader.setSelection(generateAlbumSelection(bundle, true));
                return albumSnapshotLoader;
            }
        }

        public final void loadShareData() {
            Bundle bundle = new Bundle();
            bundle.putLong("id", this.mAlbum.getAlbumId());
            this.mFragment.getLoaderManager().initLoader(2, bundle, this);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader loader, Object obj) {
            int id = loader.getId();
            List<Album> list = null;
            if (id == 1) {
                if (obj != null) {
                    list = (List) obj;
                }
                onLoadFinished(list, false);
            } else if (id == 2) {
                this.mShareAlbumCursor = (Cursor) obj;
                ShareAlbumCacheManager.getInstance().putSharedAlbums(this.mShareAlbumCursor);
                gotoAlbumDetailPage(this.mAlbum);
            } else if (id != 3) {
            } else {
                if (obj != null) {
                    list = (List) obj;
                }
                onLoadFinished(list, true);
            }
        }

        public final void gotoAlbumDetailPage(Album album) {
            Intent createJumpIntent = createJumpIntent(album);
            if (createJumpIntent != null) {
                this.mHandleIntentCallback.onHandleIntent(createJumpIntent);
            } else {
                this.mHandleIntentCallback.onHandleFailed(this.mFragment.getActivity(), this.mFragment.getString(R.string.album_jump_failed));
            }
        }

        public final void onLoadFinished(List<Album> list, boolean z) {
            if (list != null && list.size() > 0) {
                this.mAlbum = list.get(0);
                loadShareData();
            } else if (z) {
                this.mFragment.getLoaderManager().initLoader(1, this.mUriParameters, this);
            } else {
                this.mHandleIntentCallback.onHandleFailed(this.mFragment.getActivity(), this.mFragment.getString(R.string.album_jump_failed));
            }
            DefaultLogger.d("JumpDialogFragment", "load album from [%s] cost %d", z ? "snapshot" : "db", Long.valueOf(System.currentTimeMillis() - this.mAlbumLoadTime));
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
            Cursor cursor;
            if (loader.getId() == 2 && (cursor = this.mShareAlbumCursor) != null) {
                cursor.close();
            }
        }

        public final Intent createJumpIntent(Album album) {
            Intent intent;
            if (album == null) {
                return null;
            }
            long albumId = album.getAlbumId();
            Intent shortCutIntent = getShortCutIntent(albumId, album.getLocalPath());
            if (shortCutIntent != null) {
                return shortCutIntent;
            }
            long attributes = album.getAttributes();
            String serverId = album.getServerId();
            String displayedAlbumName = album.getDisplayedAlbumName();
            String localPath = album.getLocalPath();
            boolean equals = String.valueOf(-2147483645L).equals(serverId);
            if (!TextUtils.isEmpty(album.getBabyInfo())) {
                intent = new Intent(this.mFragment.getActivity(), BabyAlbumDetailActivity.class);
                intent.putExtra("people_id", album.getPeopleId());
                intent.putExtra("baby_info", album.getBabyInfo());
                intent.putExtra("thumbnail_info_of_baby", album.getThumbnailInfoOfBaby());
                intent.putExtra("baby_sharer_info", album.getShareInfo());
            } else if (equals) {
                intent = new Intent(this.mFragment.getActivity(), AlbumDetailGroupingActivity.class);
                intent.putExtra("group_first_album_id", AlbumCacheManager.getInstance().getScreenshotsAlbumId());
                intent.putExtra("group_second_album_id", AlbumCacheManager.getInstance().getScreenRecordersAlbumId());
                intent.putExtra("group_first_album_name", this.mFragment.getString(R.string.album_screenshot_name));
                intent.putExtra("group_second_album_name", this.mFragment.getString(R.string.album_screen_record));
            } else {
                intent = new Intent("com.miui.gallery.action.VIEW_ALBUM_DETAIL");
            }
            boolean equals2 = String.valueOf(2L).equals(serverId);
            boolean equals3 = String.valueOf(-2147483647L).equals(serverId);
            boolean isOtherShareAlbumId = ShareAlbumHelper.isOtherShareAlbumId(album.getAlbumId());
            boolean isOwnerShareAlbum = album.isOwnerShareAlbum();
            boolean isAutoUploadedAlbum = album.isAutoUploadedAlbum();
            intent.putExtra("other_share_album", isOtherShareAlbumId);
            intent.putExtra("owner_share_album", isOwnerShareAlbum);
            intent.putExtra("is_local_album", isAutoUploadedAlbum);
            intent.putExtra("screenshot_album", equals2);
            intent.putExtra("screenshot_recorder_album", equals);
            intent.putExtra("video_album", equals3);
            intent.putExtra("album_id", albumId);
            intent.putExtra("album_name", displayedAlbumName);
            intent.putExtra("album_unwriteable", album.isImmutable());
            if (equals2 || equals) {
                String queryParameter = this.mUri.getQueryParameter("screenshotAppName");
                if (!TextUtils.isEmpty(queryParameter)) {
                    intent.putExtra("screenshot_app_name", queryParameter);
                    intent.putExtra("album_name", queryParameter);
                    intent.putExtra("album_unwriteable", true);
                }
            }
            intent.putExtra("album_server_id", serverId);
            intent.putExtra("attributes", attributes);
            intent.putExtra("album_local_path", localPath);
            return intent;
        }

        public final Intent getShortCutIntent(long j, String str) {
            if (AlbumsCursorHelper.isAllPhotoAlbum(j)) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(GalleryContract.RecentAlbum.VIEW_PAGE_URI.buildUpon().appendQueryParameter("source", "album_page").build());
                intent.setPackage(this.mFragment.getActivity().getPackageName());
                return intent;
            } else if (!AlbumsCursorHelper.isFavoritesAlbum(j)) {
                return null;
            } else {
                Intent intent2 = new Intent("android.intent.action.VIEW");
                intent2.putExtra("album_id", 2147483642L);
                intent2.putExtra("album_name", this.mFragment.getString(R.string.album_favorites_name));
                intent2.putExtra("album_server_id", String.valueOf(-2147483642L));
                intent2.setPackage(this.mFragment.getActivity().getPackageName());
                return intent2;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PeopleJumpHelper implements LoaderManager.LoaderCallbacks {
        public final String[] PROJECTION = {j.c, "peopleName", "relationType", "microthumbfile", "thumbnailFile", "localFile", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "serverId"};
        public Fragment mFragment;
        public HandleIntentCallback mHandleIntentCallback;

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader loader) {
        }

        public PeopleJumpHelper(Fragment fragment) {
            this.mFragment = fragment;
        }

        public void startLoading(HandleIntentCallback handleIntentCallback, String str) {
            this.mHandleIntentCallback = handleIntentCallback;
            Bundle bundle = new Bundle();
            bundle.putString("serverId", str);
            this.mFragment.getLoaderManager().initLoader(1, bundle, this);
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader onCreateLoader(int i, Bundle bundle) {
            CursorLoader cursorLoader = new CursorLoader(this.mFragment.getActivity());
            cursorLoader.setUri(GalleryContract.PeopleFace.PEOPLE_COVER_URI.buildUpon().appendQueryParameter("serverId", bundle.getString("serverId")).build());
            cursorLoader.setProjection(this.PROJECTION);
            return cursorLoader;
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x00ad  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x00b3  */
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onLoadFinished(androidx.loader.content.Loader r9, java.lang.Object r10) {
            /*
                r8 = this;
                r9 = 0
                if (r10 == 0) goto Lab
                android.database.Cursor r10 = (android.database.Cursor) r10
                boolean r0 = r10.moveToFirst()     // Catch: java.lang.Exception -> La8
                if (r0 == 0) goto Lab
                android.os.Bundle r0 = new android.os.Bundle     // Catch: java.lang.Exception -> La8
                r0.<init>()     // Catch: java.lang.Exception -> La8
                r1 = 10
                java.lang.String r1 = r10.getString(r1)     // Catch: java.lang.Exception -> La8
                r2 = 0
                java.lang.String r2 = r10.getString(r2)     // Catch: java.lang.Exception -> La8
                java.lang.String r3 = "server_id_of_album"
                r0.putString(r3, r1)     // Catch: java.lang.Exception -> La8
                java.lang.String r1 = "local_id_of_album"
                r0.putString(r1, r2)     // Catch: java.lang.Exception -> La8
                r1 = 1
                java.lang.String r1 = r10.getString(r1)     // Catch: java.lang.Exception -> La8
                boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> La8
                if (r2 == 0) goto L39
                androidx.fragment.app.Fragment r1 = r8.mFragment     // Catch: java.lang.Exception -> La8
                r2 = 2131888366(0x7f1208ee, float:1.9411365E38)
                java.lang.String r1 = r1.getString(r2)     // Catch: java.lang.Exception -> La8
            L39:
                java.lang.String r2 = "album_name"
                r0.putString(r2, r1)     // Catch: java.lang.Exception -> La8
                java.lang.String r1 = "relationType"
                r2 = 2
                int r2 = r10.getInt(r2)     // Catch: java.lang.Exception -> La8
                r0.putInt(r1, r2)     // Catch: java.lang.Exception -> La8
                r1 = 4
                java.lang.String r1 = r10.getString(r1)     // Catch: java.lang.Exception -> La8
                boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> La8
                if (r2 == 0) goto L58
                r1 = 3
                java.lang.String r1 = r10.getString(r1)     // Catch: java.lang.Exception -> La8
            L58:
                boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: java.lang.Exception -> La8
                if (r2 == 0) goto L63
                r1 = 5
                java.lang.String r1 = r10.getString(r1)     // Catch: java.lang.Exception -> La8
            L63:
                java.lang.String r2 = "face_album_cover"
                r0.putString(r2, r1)     // Catch: java.lang.Exception -> La8
                java.lang.String r1 = "face_position_rect"
                android.graphics.RectF r2 = new android.graphics.RectF     // Catch: java.lang.Exception -> La8
                r3 = 6
                float r4 = r10.getFloat(r3)     // Catch: java.lang.Exception -> La8
                r5 = 7
                float r6 = r10.getFloat(r5)     // Catch: java.lang.Exception -> La8
                float r3 = r10.getFloat(r3)     // Catch: java.lang.Exception -> La8
                r7 = 8
                float r7 = r10.getFloat(r7)     // Catch: java.lang.Exception -> La8
                float r3 = r3 + r7
                float r5 = r10.getFloat(r5)     // Catch: java.lang.Exception -> La8
                r7 = 9
                float r7 = r10.getFloat(r7)     // Catch: java.lang.Exception -> La8
                float r5 = r5 + r7
                r2.<init>(r4, r6, r3, r5)     // Catch: java.lang.Exception -> La8
                r0.putParcelable(r1, r2)     // Catch: java.lang.Exception -> La8
                android.content.Intent r1 = new android.content.Intent     // Catch: java.lang.Exception -> La8
                r1.<init>()     // Catch: java.lang.Exception -> La8
                r1.putExtras(r0)     // Catch: java.lang.Exception -> La7
                androidx.fragment.app.Fragment r9 = r8.mFragment     // Catch: java.lang.Exception -> La7
                androidx.fragment.app.FragmentActivity r9 = r9.getActivity()     // Catch: java.lang.Exception -> La7
                java.lang.Class<com.miui.gallery.activity.facebaby.FacePageActivity> r0 = com.miui.gallery.activity.facebaby.FacePageActivity.class
                r1.setClass(r9, r0)     // Catch: java.lang.Exception -> La7
                r9 = r1
                goto Lab
            La7:
                r9 = r1
            La8:
                r10.close()
            Lab:
                if (r9 == 0) goto Lb3
                com.miui.gallery.ui.JumpDialogFragment$HandleIntentCallback r10 = r8.mHandleIntentCallback
                r10.onHandleIntent(r9)
                goto Lc7
            Lb3:
                com.miui.gallery.ui.JumpDialogFragment$HandleIntentCallback r9 = r8.mHandleIntentCallback
                androidx.fragment.app.Fragment r10 = r8.mFragment
                androidx.fragment.app.FragmentActivity r10 = r10.getActivity()
                androidx.fragment.app.Fragment r0 = r8.mFragment
                r1 = 2131888362(0x7f1208ea, float:1.9411357E38)
                java.lang.String r0 = r0.getString(r1)
                r9.onHandleFailed(r10, r0)
            Lc7:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.JumpDialogFragment.PeopleJumpHelper.onLoadFinished(androidx.loader.content.Loader, java.lang.Object):void");
        }
    }
}
