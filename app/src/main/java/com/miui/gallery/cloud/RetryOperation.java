package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.RetryRequestHelper;
import com.miui.gallery.cloud.base.SyncTask;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.UploadStatusController;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.opensdk.exception.AuthenticationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class RetryOperation {
    public static boolean isInterruptedException(Exception exc) {
        return (exc instanceof InterruptedException) || (exc instanceof AuthenticationException);
    }

    public static <T> GallerySyncResult<T> tryAGroupItems(final Account account, final GalleryExtendedAuthToken galleryExtendedAuthToken, ArrayList<? extends RequestItemBase> arrayList, final Operation<T> operation) {
        GallerySyncResult<T> build = new GallerySyncResult.Builder().setCode(GallerySyncCode.UNKNOWN).build();
        Iterator<? extends RequestItemBase> it = arrayList.iterator();
        while (it.hasNext()) {
            final RequestItemBase next = it.next();
            if (next.getStatus() != 0) {
                it.remove();
                SyncLogger.e("RetryOperation", "The item of %s whose status of %s is invalid", next, Integer.valueOf(next.getStatus()));
            } else {
                UploadStatusController.getInstance().start(next);
                try {
                    final String simpleName = operation.getClass().getSimpleName();
                    GallerySyncResult<T> retryTask = RetryRequestHelper.retryTask(new SyncTask<T>() { // from class: com.miui.gallery.cloud.RetryOperation.1
                        @Override // com.miui.gallery.cloud.base.SyncTask
                        public String getIdentifier() {
                            return simpleName;
                        }

                        @Override // com.miui.gallery.cloud.base.SyncTask
                        public GallerySyncResult<T> run() throws Exception {
                            return operation.execute(account, galleryExtendedAuthToken, null, next);
                        }
                    });
                    it.remove();
                    if (retryTask.code == GallerySyncCode.OK) {
                        next.compareAndSetStatus(0, 2);
                    } else {
                        next.compareAndSetStatus(0, 1);
                        GallerySyncCode gallerySyncCode = retryTask.code;
                        if (gallerySyncCode != GallerySyncCode.NOT_CONTINUE_ERROR && gallerySyncCode != GallerySyncCode.CANCEL && gallerySyncCode != GallerySyncCode.CONDITION_INTERRUPTED) {
                            if (retryTask.exception != null) {
                                SyncStateManager.getInstance().handleException(retryTask.exception);
                                if (isInterruptedException(retryTask.exception)) {
                                    SyncLogger.e("RetryOperation", "cancel all items for operation %s because of %s", simpleName, retryTask.exception);
                                    next.compareAndSetStatus(0, 1);
                                    setAllStatus(it, 1);
                                    return retryTask;
                                }
                            }
                        }
                        SyncLogger.e("RetryOperation", "cancel all items for operation %s because of %s", simpleName, gallerySyncCode);
                        next.compareAndSetStatus(0, 3);
                        setAllStatus(it, 3);
                        return retryTask;
                    }
                    UploadStatusController.getInstance().end(next);
                    build = retryTask;
                } finally {
                    UploadStatusController.getInstance().end(next);
                }
            }
        }
        return build;
    }

    public static void setAllStatus(Iterator<? extends RequestItemBase> it, int i) {
        while (it.hasNext()) {
            it.next().compareAndSetStatus(0, i);
        }
    }

    public static <T> GallerySyncResult<T> doOperation(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List<? extends RequestItemBase> list, Operation<T> operation) {
        boolean z;
        GallerySyncResult<T> tryAGroupItems;
        ArrayList arrayList = new ArrayList();
        GallerySyncResult<T> gallerySyncResult = null;
        int i = 0;
        while (i < list.size()) {
            RequestItemBase requestItemBase = list.get(i);
            if (arrayList.isEmpty() || requestItemBase.isInSameAlbum((RequestItemBase) arrayList.get(arrayList.size() - 1))) {
                arrayList.add(requestItemBase);
                if (arrayList.size() >= requestItemBase.getRequestLimitAGroup() || i >= list.size() - 1) {
                    z = false;
                } else {
                    i++;
                }
            } else {
                DefaultLogger.d("RetryOperation", "requestItem in this group, try this group and add this item later.");
                z = true;
            }
            DefaultLogger.d("RetryOperation", "do " + operation.getClass() + " a group items:" + i);
            if (requestItemBase.supportMultiRequest()) {
                RequestMultiItem requestMultiItem = new RequestMultiItem(arrayList, requestItemBase.priority);
                ArrayList arrayList2 = new ArrayList();
                arrayList2.add(requestMultiItem);
                tryAGroupItems = tryAGroupItems(account, galleryExtendedAuthToken, arrayList2, operation);
            } else {
                tryAGroupItems = tryAGroupItems(account, galleryExtendedAuthToken, arrayList, operation);
            }
            DefaultLogger.v("RetryOperation", "finish one loop for upload");
            arrayList.clear();
            if (z) {
                if (i < list.size() - 1) {
                    arrayList.add(requestItemBase);
                } else {
                    i--;
                }
            }
            gallerySyncResult = tryAGroupItems;
            i++;
        }
        return gallerySyncResult;
    }
}
