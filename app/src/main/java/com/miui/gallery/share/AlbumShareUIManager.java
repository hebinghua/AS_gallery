package com.miui.gallery.share;

import android.accounts.Account;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.share.AlbumShareOperations;
import com.xiaomi.video.VideoDecoder;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class AlbumShareUIManager {
    public static final Handler sHandler = new Handler(Looper.getMainLooper());
    public static final JobStatusManager<Void, Void> sDefaultJobManager = new JobStatusManager<>(new ThreadPool("DefaultAlbumShareMgr"));
    public static final JobStatusManager<Path, Integer> sAlbumShareManager = new JobStatusManager<Path, Integer>(new ThreadPool(1, 1, "AlbumShareMgr")) { // from class: com.miui.gallery.share.AlbumShareUIManager.1
        @Override // com.miui.gallery.share.AlbumShareUIManager.JobStatusManager
        public Integer advanceStatus(Path path, Integer num, boolean z) {
            int advance = num != null ? AlbumShareState.advance(num.intValue(), z) : -1;
            if (!AlbumShareState.isValid(advance)) {
                advance = AlbumShareUIManager.readAlbumShareStateFromDB(path);
            }
            return Integer.valueOf(advance);
        }
    };

    /* loaded from: classes2.dex */
    public interface OnCompletionListener<K, T> {
        void onCompletion(K k, T t, int i, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface OnStatusChangedListener<K, S> {
        void onAlbumStateChanged(K k, S s);
    }

    /* loaded from: classes2.dex */
    public static final class BlockMessage {
        public final WeakReference<Activity> mActivityRef;
        public final boolean mCancelable;
        public final DialogInterface.OnCancelListener mListener;
        public final CharSequence mMessage;
        public final CharSequence mTitle;

        public BlockMessage(Activity activity, CharSequence charSequence, CharSequence charSequence2, boolean z, DialogInterface.OnCancelListener onCancelListener) {
            if (activity == null) {
                throw new IllegalArgumentException("context cannot be null");
            }
            this.mActivityRef = new WeakReference<>(activity);
            this.mTitle = charSequence;
            this.mMessage = charSequence2;
            this.mCancelable = z;
            this.mListener = onCancelListener;
        }

        public ProgressDialog buildDialog() {
            Activity activity = this.mActivityRef.get();
            if (activity == null || activity.isFinishing()) {
                return null;
            }
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle(this.mTitle);
            progressDialog.setMessage(this.mMessage);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(this.mCancelable);
            progressDialog.setOnCancelListener(this.mListener);
            return progressDialog;
        }

        public static BlockMessage create(Activity activity, CharSequence charSequence, CharSequence charSequence2) {
            return create(activity, charSequence, charSequence2, true);
        }

        public static BlockMessage create(Activity activity, CharSequence charSequence, CharSequence charSequence2, boolean z) {
            return create(activity, charSequence, charSequence2, z, null);
        }

        public static BlockMessage create(Activity activity, CharSequence charSequence, CharSequence charSequence2, boolean z, DialogInterface.OnCancelListener onCancelListener) {
            return new BlockMessage(activity, charSequence, charSequence2, z, onCancelListener);
        }
    }

    /* loaded from: classes2.dex */
    public static class JobStatusManager<K, S> {
        public final ThreadPool mThreadPool;
        public final Map<K, S> mStatusMap = Maps.newHashMap();
        public final List<OnStatusChangedListener<K, S>> mStatusChangedListeners = Lists.newArrayList();

        public S advanceStatus(K k, S s, boolean z) {
            return null;
        }

        public JobStatusManager(ThreadPool threadPool) {
            this.mThreadPool = threadPool;
        }

        public void add(K k, S s) {
            if (k != null) {
                this.mStatusMap.put(k, s);
                notifyStatus(k, s);
            }
        }

        public void remove(K k, boolean z) {
            if (k != null) {
                notifyStatus(k, advanceStatus(k, this.mStatusMap.remove(k), z));
            }
        }

        public final void notifyStatus(K k, S s) {
            for (OnStatusChangedListener<K, S> onStatusChangedListener : this.mStatusChangedListeners) {
                onStatusChangedListener.onAlbumStateChanged(k, s);
            }
        }

        public S find(K k) {
            if (k != null) {
                return this.mStatusMap.get(k);
            }
            return null;
        }

        public ThreadPool getThreadPool() {
            return this.mThreadPool;
        }
    }

    /* loaded from: classes2.dex */
    public static final class FutureListenerAdapter<K, T> implements FutureListener<AsyncResult<T>>, Runnable {
        public final K mId;
        public final JobStatusManager<K, ?> mJobManager;
        public final OnCompletionListener<K, T> mListener;
        public Future<AsyncResult<T>> mResult;

        public FutureListenerAdapter(K k, OnCompletionListener<K, T> onCompletionListener, JobStatusManager<K, ?> jobStatusManager) {
            this.mId = k;
            this.mListener = onCompletionListener;
            this.mJobManager = jobStatusManager;
        }

        @Override // com.miui.gallery.concurrent.FutureListener
        public void onFutureDone(Future<AsyncResult<T>> future) {
            this.mResult = future;
            AlbumShareUIManager.sHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            K k = this.mId;
            boolean isCancelled = this.mResult.isCancelled();
            AsyncResult<T> asyncResult = this.mResult.get();
            int i = asyncResult != null ? asyncResult.mError : -2;
            T t = asyncResult != null ? asyncResult.mData : null;
            this.mJobManager.remove(k, i == 0);
            OnCompletionListener<K, T> onCompletionListener = this.mListener;
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(k, t, i, isCancelled);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class FutureCancelledListener<K, T> implements OnCompletionListener<K, T> {
        public final Dialog mDialog;
        public final OnCompletionListener<K, T> mListener;

        public FutureCancelledListener(OnCompletionListener<K, T> onCompletionListener, Dialog dialog) {
            this.mListener = onCompletionListener;
            this.mDialog = dialog;
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(K k, T t, int i, boolean z) {
            try {
                this.mDialog.dismiss();
            } catch (IllegalArgumentException unused) {
            }
            OnCompletionListener<K, T> onCompletionListener = this.mListener;
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(k, t, i, z);
            }
        }
    }

    public static AlbumShareOperations.IncomingInvitation getIncomingInvitation(CloudSharerMediaSet cloudSharerMediaSet) {
        return AlbumShareOperations.parseInvitation(cloudSharerMediaSet.getShareUrlLong());
    }

    public static int findAlbumShareStateFromCache(Path path) {
        Integer find = sAlbumShareManager.find(path);
        if (find != null) {
            return find.intValue();
        }
        return -1;
    }

    public static int readAlbumShareStateFromDB(CloudSharerMediaSet cloudSharerMediaSet) {
        if (cloudSharerMediaSet == null) {
            return -1;
        }
        return cloudSharerMediaSet.getAlbumShareState(null);
    }

    public static int readAlbumShareStateFromDB(Path path) {
        if (path == null) {
            return -1;
        }
        return readAlbumShareStateFromDB(path.getMediaSet());
    }

    public static boolean allowOperation(CloudSharerMediaSet cloudSharerMediaSet) {
        if (cloudSharerMediaSet == null) {
            return false;
        }
        if (findAlbumShareStateFromCache(cloudSharerMediaSet.getPath()) == -1) {
            return true;
        }
        Log.e("AlbumShareUIManager", "Operation is in process, mediaSet=" + cloudSharerMediaSet);
        return false;
    }

    public static Future<?> updateInvitationAsync(Path path, OnCompletionListener<Path, String> onCompletionListener, BlockMessage blockMessage) {
        final CloudSharerMediaSet findSharerMediaSet = findSharerMediaSet(path);
        if (!allowOperation(findSharerMediaSet)) {
            notifyError(onCompletionListener);
            return null;
        } else if (TextUtils.isEmpty(findSharerMediaSet.getShareUrl())) {
            Log.e("AlbumShareUIManager", "updateInvitationAsync: Invalid shareUrl, media set=" + findSharerMediaSet);
            notifyError(onCompletionListener);
            return null;
        } else if (findSharerMediaSet.hasShareDetailInfo()) {
            Log.i("AlbumShareUIManager", "updateInvitationAsync: Already exist share detail info, media set=" + findSharerMediaSet);
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(path, findSharerMediaSet.getShareUrlLong(), 0, false);
            }
            return null;
        } else {
            int requestLongUrl = AlbumShareState.requestLongUrl(readAlbumShareStateFromDB(findSharerMediaSet));
            if (!AlbumShareState.isValid(requestLongUrl)) {
                Log.e("AlbumShareUIManager", "updateInvitationAsync: Invalid state, media set=" + findSharerMediaSet);
                notifyError(onCompletionListener);
                return null;
            }
            final String shareUrl = findSharerMediaSet.getShareUrl();
            final String shareUrlLong = findSharerMediaSet.getShareUrlLong();
            return submit(sAlbumShareManager, path, Integer.valueOf(requestLongUrl), new ThreadPool.Job<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public AsyncResult<String> mo1807run(ThreadPool.JobContext jobContext) {
                    return AlbumShareOperations.updateInvitation(AlbumShareUIManager.getContentResolver(), shareUrl, shareUrlLong, findSharerMediaSet);
                }
            }, onCompletionListener, blockMessage);
        }
    }

    public static Future<?> updateInvitationAsync(Path path, OnCompletionListener<Path, String> onCompletionListener) {
        return updateInvitationAsync(path, onCompletionListener, null);
    }

    public static void addInvitationToDBAsync(final String str, OnCompletionListener<Void, Long> onCompletionListener) {
        submit(new ThreadPool.Job<AsyncResult<Long>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<Long> mo1807run(ThreadPool.JobContext jobContext) {
                return AsyncResult.create(0, Long.valueOf(AlbumShareOperations.addInvitation(str)));
            }
        }, onCompletionListener);
    }

    public static void applyToShareAlbum(Activity activity, final Path path, final OnCompletionListener<Path, Void> onCompletionListener, final OnCompletionListener<Path, Long> onCompletionListener2, final OnCompletionListener<Path, Void> onCompletionListener3, final DialogInterface.OnCancelListener onCancelListener, final BlockMessage blockMessage, final boolean z) {
        final WeakReference weakReference = new WeakReference(activity);
        final OnCompletionListener<Path, String> onCompletionListener4 = new OnCompletionListener<Path, String>() { // from class: com.miui.gallery.share.AlbumShareUIManager.7
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public void onCompletion(Path path2, String str, int i, boolean z2) {
                final CloudSharerMediaSet findSharerMediaSet;
                final Activity activity2 = (Activity) weakReference.get();
                boolean z3 = true;
                if (i != 0 || z2 || activity2 == null || activity2.isFinishing() || (findSharerMediaSet = AlbumShareUIManager.findSharerMediaSet(path)) == null) {
                    z3 = false;
                } else {
                    AlbumShareUIManager.doAfterCloudMediaSetSetReload(new Runnable() { // from class: com.miui.gallery.share.AlbumShareUIManager.7.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass7 anonymousClass7 = AnonymousClass7.this;
                            if (z) {
                                AlbumShareUIManager.showDialogToAccept(activity2, findSharerMediaSet, onCompletionListener2, onCompletionListener3, onCancelListener);
                                return;
                            }
                            Path path3 = findSharerMediaSet.getPath();
                            Activity activity3 = activity2;
                            AnonymousClass7 anonymousClass72 = AnonymousClass7.this;
                            AlbumShareUIManager.tryToAccept(path3, activity3, onCompletionListener2, onCancelListener, null);
                        }
                    }, path, true);
                }
                if (!z3) {
                    if (i == -112) {
                        i = VideoDecoder.DecodeCallback.ERROR_QUEUE_INPUT_BUFFER;
                    }
                    AlbumShareUIManager.notifyError(onCompletionListener, i);
                }
            }
        };
        doAfterCloudMediaSetSetReload(new Runnable() { // from class: com.miui.gallery.share.AlbumShareUIManager.8
            @Override // java.lang.Runnable
            public void run() {
                AlbumShareUIManager.updateShareUrlLongAuto(Path.this, onCompletionListener, onCompletionListener4, blockMessage);
            }
        }, path, true);
    }

    public static void updateShareUrlLongAuto(Path path, OnCompletionListener<Path, Void> onCompletionListener, OnCompletionListener<Path, String> onCompletionListener2, BlockMessage blockMessage) {
        CloudSharerMediaSet findSharerMediaSet = findSharerMediaSet(path);
        if (findSharerMediaSet == null) {
            if (onCompletionListener2 == null) {
                return;
            }
            onCompletionListener2.onCompletion(path, null, -2, false);
        } else if (findSharerMediaSet.isNormalStatus()) {
            if (onCompletionListener == null) {
                return;
            }
            onCompletionListener.onCompletion(path, null, -9, false);
        } else {
            String shareUrlLong = findSharerMediaSet.getShareUrlLong();
            if (!TextUtils.isEmpty(shareUrlLong)) {
                AlbumShareOperations.IncomingInvitation parseInvitation = AlbumShareOperations.parseInvitation(shareUrlLong);
                String sharerInfo = findSharerMediaSet.getSharerInfo();
                if ((parseInvitation != null && !parseInvitation.hasSharerInfo()) || !TextUtils.isEmpty(sharerInfo)) {
                    if (onCompletionListener2 == null) {
                        return;
                    }
                    onCompletionListener2.onCompletion(path, shareUrlLong, 0, false);
                    return;
                }
            }
            updateInvitationAsync(path, onCompletionListener2, blockMessage);
        }
    }

    public static void updateShareUrlLongAuto(Path path, OnCompletionListener<Path, Void> onCompletionListener, OnCompletionListener<Path, String> onCompletionListener2) {
        updateShareUrlLongAuto(path, onCompletionListener, onCompletionListener2, null);
    }

    public static void doAfterCloudMediaSetSetReload(Runnable runnable, Path path, boolean z) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static Future<?> acceptInvitationAsync(Path path, OnCompletionListener<Path, Long> onCompletionListener, BlockMessage blockMessage) {
        final CloudSharerMediaSet findSharerMediaSet = findSharerMediaSet(path);
        if (!allowOperation(findSharerMediaSet)) {
            notifyError(onCompletionListener);
            return null;
        } else if (TextUtils.isEmpty(findSharerMediaSet.getShareUrl())) {
            Log.e("AlbumShareUIManager", "acceptInvitationAsync: Invalid shareUrl, media set=" + findSharerMediaSet);
            notifyError(onCompletionListener);
            return null;
        } else {
            int accept = AlbumShareState.accept(readAlbumShareStateFromDB(findSharerMediaSet));
            if (!AlbumShareState.isValid(accept)) {
                Log.e("AlbumShareUIManager", "acceptInvitationAsync: Invalid state, media set=" + findSharerMediaSet);
                notifyError(onCompletionListener);
                return null;
            }
            final String shareUrl = findSharerMediaSet.getShareUrl();
            return submit(sAlbumShareManager, path, Integer.valueOf(accept), new ThreadPool.Job<AsyncResult<Long>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.9
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public AsyncResult<Long> mo1807run(ThreadPool.JobContext jobContext) {
                    return AlbumShareOperations.acceptInvitation(AlbumShareUIManager.getContentResolver(), shareUrl, findSharerMediaSet);
                }
            }, onCompletionListener, blockMessage);
        }
    }

    public static Future<?> denyInvitationAsync(Path path, OnCompletionListener<Path, Void> onCompletionListener, BlockMessage blockMessage) {
        final CloudSharerMediaSet findSharerMediaSet = findSharerMediaSet(path);
        if (!allowOperation(findSharerMediaSet)) {
            notifyError(onCompletionListener);
            return null;
        } else if (TextUtils.isEmpty(findSharerMediaSet.getShareUrl())) {
            Log.e("AlbumShareUIManager", "denyInvitationAsync: Invalid shareUrl, media set=" + findSharerMediaSet);
            notifyError(onCompletionListener);
            return null;
        } else {
            int deny = AlbumShareState.deny(readAlbumShareStateFromDB(findSharerMediaSet));
            if (!AlbumShareState.isValid(deny)) {
                Log.e("AlbumShareUIManager", "denyInvitationAsync: Invalid state, media set=" + findSharerMediaSet);
                notifyError(onCompletionListener);
                return null;
            }
            final String shareUrl = findSharerMediaSet.getShareUrl();
            return submit(sAlbumShareManager, path, Integer.valueOf(deny), new ThreadPool.Job<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.10
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public AsyncResult<Void> mo1807run(ThreadPool.JobContext jobContext) {
                    return AlbumShareOperations.denyInvitation(AlbumShareUIManager.getContentResolver(), shareUrl, findSharerMediaSet);
                }
            }, onCompletionListener, blockMessage);
        }
    }

    public static void exitAlbumShareAsync(final Path path, final OnCompletionListener<Path, Void> onCompletionListener, final BlockMessage blockMessage) {
        doAfterCloudMediaSetSetReload(new Runnable() { // from class: com.miui.gallery.share.AlbumShareUIManager.11
            @Override // java.lang.Runnable
            public void run() {
                final CloudSharerMediaSet findSharerMediaSet = AlbumShareUIManager.findSharerMediaSet(Path.this);
                if (!AlbumShareUIManager.allowOperation(findSharerMediaSet)) {
                    AlbumShareUIManager.notifyError(onCompletionListener);
                    return;
                }
                int exit = AlbumShareState.exit(AlbumShareUIManager.readAlbumShareStateFromDB(findSharerMediaSet));
                if (!AlbumShareState.isValid(exit)) {
                    Log.e("AlbumShareUIManager", "Invalid state, media set=" + findSharerMediaSet);
                    AlbumShareUIManager.notifyError(onCompletionListener);
                    return;
                }
                final String shareAlbumId = findSharerMediaSet.getShareAlbumId();
                AlbumShareUIManager.submit(AlbumShareUIManager.sAlbumShareManager, Path.this, Integer.valueOf(exit), new ThreadPool.Job<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.11.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public AsyncResult<Void> mo1807run(ThreadPool.JobContext jobContext) {
                        return AlbumShareOperations.exitAlbumShare(shareAlbumId, String.valueOf(findSharerMediaSet.getId()));
                    }
                }, onCompletionListener, blockMessage);
            }
        }, path, true);
    }

    public static Future<?> requestUrlForBarcodeAsync(final String str, OnCompletionListener<Void, String> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<String> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.requestUrlForBarcode(str);
            }
        }, onCompletionListener);
    }

    public static Future<?> requestSwitchShareDevice(final String str, final String str2, final boolean z, OnCompletionListener<Void, List<String>> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<List<String>>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<List<String>> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.requestShareToDevice(str, str2, z);
            }
        }, onCompletionListener);
    }

    public static Future<?> changePublicStatusAsync(final String str, final boolean z, OnCompletionListener<Void, String> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.16
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<String> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.changePublicStatus(str, z);
            }
        }, onCompletionListener);
    }

    public static Future<?> requestPublicUrlAsync(final String str, final boolean z, OnCompletionListener<Void, String> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.17
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<String> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.requestPublicUrl(str, z);
            }
        }, onCompletionListener);
    }

    public static Future<?> requestPublicUrlForSharerAsync(String str, OnCompletionListener<Void, String> onCompletionListener) {
        return requestPublicUrlAsync(str, true, onCompletionListener);
    }

    public static Future<?> requestInvitationForSmsAsync(final String str, final String str2, final String str3, final String str4, final String str5, OnCompletionListener<Void, AlbumShareOperations.OutgoingInvitation> onCompletionListener, BlockMessage blockMessage) {
        return submit(sDefaultJobManager, null, null, new ThreadPool.Job<AsyncResult<AlbumShareOperations.OutgoingInvitation>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.19
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<AlbumShareOperations.OutgoingInvitation> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.requestInvitationForSms(str, str2, str3, str4, str5);
            }
        }, onCompletionListener, blockMessage);
    }

    public static Future<?> syncUserListForAlbumAsync(final String str, final boolean z, OnCompletionListener<Void, Void> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.21
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<Void> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.syncUserListForAlbum(str, z);
            }
        }, onCompletionListener);
    }

    public static Future<?> syncAllUserInfoFromNetworkAsync(OnCompletionListener<Void, Void> onCompletionListener) {
        return submit(new ThreadPool.Job<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.22
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<Void> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.syncAllUserInfoFromNetwork();
            }
        }, onCompletionListener);
    }

    public static Future<?> kickSharersAsync(final String str, final List<String> list, OnCompletionListener<Void, Pair<List<String>, List<String>>> onCompletionListener, BlockMessage blockMessage) {
        return submit(sDefaultJobManager, null, null, new ThreadPool.Job<AsyncResult<Pair<List<String>, List<String>>>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.23
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<Pair<List<String>, List<String>>> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.deleteSharers(AlbumShareUIManager.getContentResolver(), str, list);
            }
        }, onCompletionListener, blockMessage);
    }

    public static Future<?> tryToCreateCloudAlbumAsync(final String str, OnCompletionListener<Void, String> onCompletionListener) {
        return submit(sDefaultJobManager, null, null, new ThreadPool.Job<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareUIManager.24
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public AsyncResult<String> mo1807run(ThreadPool.JobContext jobContext) {
                return AlbumShareOperations.tryToCreateCloudAlbum(str);
            }
        }, onCompletionListener, null);
    }

    public static <T> Future<AsyncResult<T>> submit(ThreadPool.Job<AsyncResult<T>> job, OnCompletionListener<Void, T> onCompletionListener) {
        return submit(sDefaultJobManager, null, null, job, onCompletionListener);
    }

    public static <K, T, S> Future<AsyncResult<T>> submit(JobStatusManager<K, S> jobStatusManager, K k, S s, ThreadPool.Job<AsyncResult<T>> job, OnCompletionListener<K, T> onCompletionListener) {
        return submit(jobStatusManager, k, s, job, onCompletionListener, null);
    }

    public static <K, T, S> Future<AsyncResult<T>> submit(JobStatusManager<K, S> jobStatusManager, K k, S s, ThreadPool.Job<AsyncResult<T>> job, OnCompletionListener<K, T> onCompletionListener, final BlockMessage blockMessage) {
        if (blockMessage == null) {
            jobStatusManager.add(k, s);
            return jobStatusManager.getThreadPool().submit(job, new FutureListenerAdapter(k, onCompletionListener, jobStatusManager));
        }
        ProgressDialog buildDialog = blockMessage.buildDialog();
        if (buildDialog == null) {
            if (onCompletionListener != null) {
                onCompletionListener.onCompletion(k, null, -2, false);
            }
            return null;
        }
        jobStatusManager.add(k, s);
        buildDialog.show();
        final Future<AsyncResult<T>> submit = jobStatusManager.getThreadPool().submit(job, new FutureListenerAdapter(k, new FutureCancelledListener(onCompletionListener, buildDialog), jobStatusManager));
        buildDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.share.AlbumShareUIManager.25
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                Future.this.cancel();
                DialogInterface.OnCancelListener onCancelListener = blockMessage.mListener;
                if (onCancelListener != null) {
                    onCancelListener.onCancel(dialogInterface);
                }
            }
        });
        return submit;
    }

    public static ContentResolver getContentResolver() {
        return GalleryApp.sGetAndroidContext().getContentResolver();
    }

    public static void showDialogToAccept(final Activity activity, CloudSharerMediaSet cloudSharerMediaSet, final OnCompletionListener<Path, Long> onCompletionListener, final OnCompletionListener<Path, Void> onCompletionListener2, DialogInterface.OnCancelListener onCancelListener) {
        final Path path = cloudSharerMediaSet.getPath();
        AlbumShareOperations.IncomingInvitation incomingInvitation = getIncomingInvitation(cloudSharerMediaSet);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.share.AlbumShareUIManager.26
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumShareUIManager.onNegativeClick(activity, path, onCompletionListener2);
            }
        };
        DialogInterface.OnShowListener onShowListener = new DialogInterface.OnShowListener() { // from class: com.miui.gallery.share.AlbumShareUIManager.27
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(final DialogInterface dialogInterface) {
                ((AlertDialog) dialogInterface).getButton(-1).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.share.AlbumShareUIManager.27.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        AnonymousClass27 anonymousClass27 = AnonymousClass27.this;
                        AlbumShareUIManager.tryToAccept(Path.this, activity, onCompletionListener, null, (AlertDialog) dialogInterface);
                    }
                });
            }
        };
        AlbumShareOperations.SharerInfo sharerInfo = null;
        String sharerInfo2 = (incomingInvitation == null || !incomingInvitation.hasSharerInfo()) ? null : cloudSharerMediaSet.getSharerInfo();
        if (!TextUtils.isEmpty(sharerInfo2)) {
            sharerInfo = AlbumShareOperations.parseSharerInfo(sharerInfo2);
        }
        if (sharerInfo != null) {
            showDialogToAccept(activity, sharerInfo, onCancelListener, onClickListener, onShowListener);
        } else {
            showDialogToAccept(activity, incomingInvitation, onCancelListener, onClickListener, onShowListener);
        }
    }

    public static void showDialogToAccept(Activity activity, AlbumShareOperations.SharerInfo sharerInfo, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnClickListener onClickListener, DialogInterface.OnShowListener onShowListener) {
        String str = sharerInfo.mAlbumNickName;
        String str2 = sharerInfo.mOwnerRelationText;
        String str3 = sharerInfo.mSharerRelationText;
        String[] stringArray = activity.getResources().getStringArray(R.array.owner_relation);
        String[] stringArray2 = activity.getResources().getStringArray(R.array.sharer_relation_addressive);
        int i = 0;
        while (true) {
            if (i >= stringArray.length) {
                break;
            } else if (stringArray[i].equalsIgnoreCase(str3)) {
                str3 = stringArray2[i];
                break;
            } else {
                i++;
            }
        }
        View inflate = activity.getLayoutInflater().inflate(R.layout.baby_album_share_invitation_dialog_layout, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.msg)).setText(activity.getString(R.string.baby_album_sharer_accept_dialog_msg, new Object[]{str, str3, str, str2, str}));
        AlertDialog create = new AlertDialog.Builder(activity, 2131952328).setView(inflate).setPositiveButton(R.string.accept, (DialogInterface.OnClickListener) null).setNegativeButton(R.string.deny, onClickListener).setOnCancelListener(onCancelListener).create();
        create.setOnShowListener(onShowListener);
        if (!TextUtils.isEmpty(sharerInfo.mThumbnailUrl)) {
            ImageView imageView = (ImageView) inflate.findViewById(R.id.baby_photo);
            Glide.with(imageView).mo985asBitmap().mo962load(GalleryModel.of(sharerInfo.mThumbnailUrl)).mo946apply((BaseRequestOptions<?>) GlideOptions.peopleFaceOf(sharerInfo.mFaceRelativePos)).into(imageView);
        }
        create.show();
    }

    public static void showDialogToAccept(Activity activity, AlbumShareOperations.IncomingInvitation incomingInvitation, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnClickListener onClickListener, DialogInterface.OnShowListener onShowListener) {
        String str = "";
        String ownerName = incomingInvitation != null ? incomingInvitation.getOwnerName() : str;
        if (incomingInvitation != null) {
            str = incomingInvitation.getAlbumName();
        }
        AlertDialog create = new AlertDialog.Builder(activity).setTitle(R.string.share_album).setMessage(activity.getString(R.string.accept_or_deny, new Object[]{ownerName, activity.getString(R.string.quotation, new Object[]{str})})).setPositiveButton(R.string.accept, (DialogInterface.OnClickListener) null).setNegativeButton(R.string.deny, onClickListener).setOnCancelListener(onCancelListener).create();
        create.setOnShowListener(onShowListener);
        create.show();
    }

    public static void tryToAccept(final Path path, final Activity activity, final OnCompletionListener<Path, Long> onCompletionListener, DialogInterface.OnCancelListener onCancelListener, Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Runnable runnable = new Runnable() { // from class: com.miui.gallery.share.AlbumShareUIManager.28
            @Override // java.lang.Runnable
            public void run() {
                Activity activity2 = activity;
                AlbumShareUIManager.acceptInvitationAsync(path, onCompletionListener, BlockMessage.create(activity2, null, activity2.getString(R.string.operation_in_process), false));
            }
        };
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(activity);
        if (!(xiaomiAccount != null && ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider"))) {
            onCompletionListener.onCompletion(path, null, VideoDecoder.DecodeCallback.ERROR_DEQUEUE_OUTPUT_BUFFER, false);
            Intent intent = new Intent(activity, LoginAndSyncForInvitationActivity.class);
            intent.putExtra("invitation_path", path);
            activity.startActivity(intent);
            return;
        }
        runnable.run();
    }

    public static void onNegativeClick(Activity activity, Path path, OnCompletionListener<Path, Void> onCompletionListener) {
        if (onCompletionListener == null) {
            onCompletionListener = new OnCompletionListener<Path, Void>() { // from class: com.miui.gallery.share.AlbumShareUIManager.29
                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public void onCompletion(Path path2, Void r2, int i, boolean z) {
                    if (z) {
                        UIHelper.toast((int) R.string.cancel_hint);
                    } else if (ServerErrorCode.isClientError(i)) {
                        UIHelper.toast((int) R.string.operation_failed);
                    } else {
                        UIHelper.toast((int) R.string.operation_successful);
                    }
                }
            };
        }
        denyInvitationAsync(path, onCompletionListener, BlockMessage.create(activity, null, activity.getString(R.string.operation_in_process), false));
    }

    public static CloudSharerMediaSet findSharerMediaSet(Path path) {
        return path.getMediaSet();
    }

    public static void notifyError(OnCompletionListener<?, ?> onCompletionListener) {
        notifyError(onCompletionListener, -110);
    }

    public static void notifyError(OnCompletionListener<?, ?> onCompletionListener, int i) {
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(null, null, i, false);
        }
    }
}
