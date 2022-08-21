package com.miui.gallery.storage.android28;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.storage.DocumentProviderUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.android28.SAFStoragePermissionRequester;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import com.miui.gallery.ui.NewStoragePermissionDialogFragment;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes2.dex */
public class SAFStoragePermissionRequester implements ISAFStoragePermissionRequester {
    public final RequestProcessor mRequestProcessor = mo1404createRequestProcessor();

    @Override // com.miui.gallery.storage.utils.IStorageFunction
    public boolean handles(Context context, int i, int i2) {
        switch (i) {
            case 28:
            case 29:
                return true;
            case 30:
                return i2 == 29;
            default:
                return false;
        }
    }

    /* renamed from: createRequestProcessor */
    public RequestProcessor mo1404createRequestProcessor() {
        return new RequestProcessor();
    }

    @Override // com.miui.gallery.storage.utils.ISAFStoragePermissionRequester
    public void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr) {
        this.mRequestProcessor.submit(fragmentActivity, null, str, map, permissionArr);
    }

    @Override // com.miui.gallery.storage.utils.ISAFStoragePermissionRequester
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent) {
        this.mRequestProcessor.onHandleRequestPermissionResult(fragmentActivity, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.utils.ISAFStoragePermissionRequester
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri) {
        this.mRequestProcessor.onHandleRequestPermissionResult(fragmentActivity, uri);
    }

    @Override // com.miui.gallery.storage.utils.ISAFStoragePermissionRequester
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        this.mRequestProcessor.onHandleRequestPermissionResult(fragment, uri);
    }

    /* loaded from: classes2.dex */
    public static class PermissionRequest {
        public static final PermissionRequest INVALID_REQUEST = new PermissionRequest(null, null, null, null, null, null);
        public final WeakReference<FragmentActivity> activityRef;
        public final WeakReference<Fragment> fragmentRef;
        public final Map<String, Object> params;
        public final String path;
        public final RequestProcessor requestProcessor;
        public final IStoragePermissionStrategy.Permission type;

        public PermissionRequest(RequestProcessor requestProcessor, FragmentActivity fragmentActivity, Fragment fragment, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission permission) {
            this.requestProcessor = requestProcessor;
            this.activityRef = new WeakReference<>(fragmentActivity);
            this.fragmentRef = new WeakReference<>(fragment);
            this.path = str;
            this.params = map;
            this.type = permission;
        }

        public RequestProcessor getRequestProcessor() {
            return this.requestProcessor;
        }

        public FragmentActivity getHostActivity() {
            return this.activityRef.get();
        }

        public Fragment getHostFragment() {
            return this.fragmentRef.get();
        }

        public Context getContext() {
            Fragment fragment = this.fragmentRef.get();
            if (fragment != null && fragment.getContext() != null) {
                return fragment.getContext();
            }
            return this.activityRef.get();
        }

        public boolean isHostAlive() {
            Fragment fragment = this.fragmentRef.get();
            if (fragment != null) {
                return !fragment.isDetached();
            }
            FragmentActivity fragmentActivity = this.activityRef.get();
            return fragmentActivity != null && !fragmentActivity.isDestroyed();
        }

        public void requestPermission(String str, IStoragePermissionStrategy.Permission permission) {
            Fragment fragment = this.fragmentRef.get();
            if (fragment != null) {
                DocumentProviderUtils.requestPermission(fragment, str, permission);
            } else {
                DocumentProviderUtils.requestPermission(this.activityRef.get(), str, permission);
            }
        }

        public FragmentManager getFragmentManager() {
            Fragment fragment = this.fragmentRef.get();
            if (fragment != null) {
                return fragment.getParentFragmentManager();
            }
            return this.activityRef.get().getSupportFragmentManager();
        }

        public String getPath() {
            return this.path;
        }

        public Map<String, Object> getParams() {
            return this.params;
        }

        public IStoragePermissionStrategy.Permission getType() {
            return this.type;
        }

        public PermissionRequest prepare() {
            String secondaryStoragePath = StorageUtils.getSecondaryStoragePath();
            if (TextUtils.isEmpty(secondaryStoragePath)) {
                return INVALID_REQUEST;
            }
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            StringBuilder sb = new StringBuilder();
            sb.append(secondaryStoragePath);
            String str = File.separator;
            sb.append(str);
            sb.append("test.txt");
            if (storageStrategyManager.checkPermission(sb.toString(), IStoragePermissionStrategy.Permission.INSERT).granted) {
                return INVALID_REQUEST;
            }
            RequestProcessor requestProcessor = getRequestProcessor();
            FragmentActivity hostActivity = getHostActivity();
            Fragment hostFragment = getHostFragment();
            return new PermissionRequest(requestProcessor, hostActivity, hostFragment, secondaryStoragePath + str, getParams(), getType());
        }

        public String toString() {
            return String.format("context:%s, path: %s, params: %s, type: %s", getContext(), getPath(), getParams(), getType());
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestProcessor {
        public Disposable mDisposable;
        public final ExecutorService mPrepareExecutor = Executors.newSingleThreadExecutor();
        public final PublishProcessor<PermissionRequest> mPublishProcessor = PublishProcessor.create();
        public final Deque<PermissionRequest> mPendingRequests = new LinkedList();

        public static /* synthetic */ void $r8$lambda$3mtmQOWGhXfvHC2x153_Jc621MY(RequestProcessor requestProcessor, PermissionRequest permissionRequest) {
            requestProcessor.lambda$subscribe$5(permissionRequest);
        }

        public static /* synthetic */ void $r8$lambda$t5c7f45SdAFnUfR793_DHV7izvc(RequestProcessor requestProcessor, List list) {
            requestProcessor.lambda$submit$0(list);
        }

        public void submit(FragmentActivity fragmentActivity, Fragment fragment, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr) {
            if (permissionArr == null) {
                return;
            }
            final LinkedList linkedList = new LinkedList();
            for (IStoragePermissionStrategy.Permission permission : permissionArr) {
                linkedList.add(mo1405createPermissionRequest(fragmentActivity, fragment, str, map, permission));
            }
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    SAFStoragePermissionRequester.RequestProcessor.$r8$lambda$t5c7f45SdAFnUfR793_DHV7izvc(SAFStoragePermissionRequester.RequestProcessor.this, linkedList);
                }
            });
        }

        public /* synthetic */ void lambda$submit$0(List list) {
            this.mPendingRequests.addAll(list);
            if (this.mPendingRequests.size() == list.size()) {
                show((PermissionRequest) list.get(0));
            }
        }

        /* renamed from: createPermissionRequest */
        public PermissionRequest mo1405createPermissionRequest(FragmentActivity fragmentActivity, Fragment fragment, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission permission) {
            return new PermissionRequest(this, fragmentActivity, fragment, str, map, permission);
        }

        public final void subscribe() {
            this.mDisposable = this.mPublishProcessor.observeOn(Schedulers.from(this.mPrepareExecutor)).map(SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda4.INSTANCE).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda3
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    SAFStoragePermissionRequester.RequestProcessor.$r8$lambda$3mtmQOWGhXfvHC2x153_Jc621MY(SAFStoragePermissionRequester.RequestProcessor.this, (SAFStoragePermissionRequester.PermissionRequest) obj);
                }
            });
        }

        public static /* synthetic */ PermissionRequest lambda$subscribe$1(PermissionRequest permissionRequest) throws Exception {
            DefaultLogger.d("SAFStoragePermissionRequester_", "start prepare request [%s].", permissionRequest);
            try {
                return permissionRequest.prepare();
            } finally {
                DefaultLogger.d("SAFStoragePermissionRequester_", "end prepare request [%s].", permissionRequest);
            }
        }

        public /* synthetic */ void lambda$subscribe$5(final PermissionRequest permissionRequest) throws Exception {
            if (permissionRequest == PermissionRequest.INVALID_REQUEST) {
                DefaultLogger.d("SAFStoragePermissionRequester_", "invalid request");
                pop();
            } else if (!permissionRequest.isHostAlive()) {
                DefaultLogger.d("SAFStoragePermissionRequester_", "host died");
                pop();
            } else {
                final String path = permissionRequest.getPath();
                if (TextUtils.isEmpty(path)) {
                    DefaultLogger.d("SAFStoragePermissionRequester_", "invalid path");
                    pop();
                    return;
                }
                Map<String, Object> params = permissionRequest.getParams();
                final IStoragePermissionStrategy.Permission type = permissionRequest.getType();
                if (((Integer) params.getOrDefault(nexExportFormat.TAG_FORMAT_TYPE, 0)).intValue() == 1) {
                    permissionRequest.requestPermission(path, type);
                    return;
                }
                final boolean contains = path.contains("MIUI/Gallery/cloud/secretAlbum");
                final Context context = permissionRequest.getContext();
                NewStoragePermissionDialogFragment.newInstance(context.getString(contains ? R.string.saf_secret_album_guide : R.string.scope_storage_dialog_description), context.getString(type == IStoragePermissionStrategy.Permission.INSERT_DIRECTORY ? R.string.scope_storage_operation_btn_1 : R.string.scope_storage_operation_btn_2), new GalleryDialogFragment.OnClickListener() { // from class: com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda1
                    @Override // com.miui.gallery.widget.GalleryDialogFragment.OnClickListener
                    public final void onClick(Fragment fragment, int i) {
                        SAFStoragePermissionRequester.RequestProcessor.lambda$subscribe$2(SAFStoragePermissionRequester.PermissionRequest.this, contains, context, path, type, fragment, i);
                    }
                }, new GalleryDialogFragment.OnClickListener() { // from class: com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda2
                    @Override // com.miui.gallery.widget.GalleryDialogFragment.OnClickListener
                    public final void onClick(Fragment fragment, int i) {
                        SAFStoragePermissionRequester.RequestProcessor.this.lambda$subscribe$3(fragment, i);
                    }
                }, new GalleryDialogFragment.OnCancelListener() { // from class: com.miui.gallery.storage.android28.SAFStoragePermissionRequester$RequestProcessor$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.widget.GalleryDialogFragment.OnCancelListener
                    public final void onCancel(Fragment fragment) {
                        SAFStoragePermissionRequester.RequestProcessor.this.lambda$subscribe$4(fragment);
                    }
                }).show(permissionRequest.getFragmentManager());
            }
        }

        public static /* synthetic */ void lambda$subscribe$2(PermissionRequest permissionRequest, boolean z, Context context, String str, IStoragePermissionStrategy.Permission permission, Fragment fragment, int i) {
            if (z) {
                str = StorageUtils.getVolumePath(context, str) + "/MIUI";
            }
            permissionRequest.requestPermission(str, permission);
        }

        public /* synthetic */ void lambda$subscribe$3(Fragment fragment, int i) {
            this.mPendingRequests.clear();
        }

        public /* synthetic */ void lambda$subscribe$4(Fragment fragment) {
            this.mPendingRequests.clear();
        }

        public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent) {
            if (fragmentActivity == null || i != 63) {
                return;
            }
            if (i2 != -1) {
                this.mPendingRequests.clear();
                return;
            }
            DocumentProviderUtils.onHandleRequestPermissionResult(fragmentActivity, intent, this.mPendingRequests.peek());
            pop();
        }

        public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri) {
            if (fragmentActivity == null) {
                return;
            }
            if (uri == null) {
                this.mPendingRequests.clear();
                return;
            }
            DocumentProviderUtils.onHandleRequestPermissionResult(fragmentActivity, uri);
            pop();
        }

        public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
            if (fragment == null) {
                return;
            }
            if (uri == null) {
                this.mPendingRequests.clear();
                return;
            }
            DocumentProviderUtils.onHandleRequestPermissionResult(fragment.getContext(), uri);
            pop();
        }

        public void pop() {
            DefaultLogger.d("SAFStoragePermissionRequester_", "pop");
            this.mPendingRequests.pop();
            PermissionRequest peek = this.mPendingRequests.peek();
            if (peek == null) {
                DefaultLogger.d("SAFStoragePermissionRequester_", "empty request list, return.");
            } else if (!peek.isHostAlive()) {
                DefaultLogger.d("SAFStoragePermissionRequester_", "invalid host for request [%s], skip.", peek);
                pop();
            } else {
                DefaultLogger.d("SAFStoragePermissionRequester_", "show request [%s]", peek);
                show(peek);
            }
        }

        public void show(PermissionRequest permissionRequest) {
            Disposable disposable = this.mDisposable;
            if (disposable == null || disposable.isDisposed()) {
                subscribe();
            }
            this.mPublishProcessor.onNext(permissionRequest);
        }
    }
}
