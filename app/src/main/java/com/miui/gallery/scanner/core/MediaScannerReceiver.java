package com.miui.gallery.scanner.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import androidx.collection.ArrayMap;
import androidx.core.util.Pair;
import ch.qos.logback.core.joran.action.Action;
import com.android.internal.EnvironmentCompat;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.threadpool.GallerySchedulers;
import com.miui.gallery.util.ArrayUtils;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.BackpressureOverflowStrategy;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.PublishProcessor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class MediaScannerReceiver extends BaseMediaScannerReceiver {
    public static Disposable sDisposable;
    public static final PublishProcessor<Pair<String, Uri>> sPublishProcessor = PublishProcessor.create();
    public static AtomicInteger sIdleCount = new AtomicInteger(0);

    @Override // com.miui.gallery.scanner.core.BaseMediaScannerReceiver
    public String[] getActions() {
        return (String[]) ArrayUtils.concat(super.getActions(), new String[]{"android.intent.action.MEDIA_SCANNER_FINISHED", "android.intent.action.MEDIA_SCANNER_SCAN_FILE"});
    }

    @Override // com.miui.gallery.scanner.core.BaseMediaScannerReceiver
    public boolean doOnReceive(Context context, Intent intent) {
        if (super.doOnReceive(context, intent)) {
            return true;
        }
        String action = intent.getAction();
        Uri data = intent.getData();
        if ((!"android.intent.action.MEDIA_SCANNER_FINISHED".equals(action) && !"android.intent.action.MEDIA_SCANNER_SCAN_FILE".equals(action)) || data == null) {
            return false;
        }
        ensureSubscribed();
        sPublishProcessor.onNext(new Pair<>(action, data));
        return true;
    }

    public static void ensureSubscribed() {
        Disposable disposable = sDisposable;
        if (disposable == null || disposable.isDisposed()) {
            DefaultLogger.d("MediaScannerReceiver", "subscribe");
            sIdleCount.set(0);
            sDisposable = sPublishProcessor.buffer(500L, TimeUnit.MILLISECONDS).onBackpressureBuffer(100L, null, BackpressureOverflowStrategy.DROP_OLDEST).observeOn(GallerySchedulers.misc()).takeUntil(MediaScannerReceiver$$ExternalSyntheticLambda1.INSTANCE).filter(MediaScannerReceiver$$ExternalSyntheticLambda2.INSTANCE).subscribe(MediaScannerReceiver$$ExternalSyntheticLambda0.INSTANCE);
        }
    }

    public static /* synthetic */ boolean lambda$ensureSubscribed$0(List list) throws Exception {
        return list.size() == 0 && sIdleCount.incrementAndGet() >= 20;
    }

    public static /* synthetic */ boolean lambda$ensureSubscribed$1(List list) throws Exception {
        return list.size() > 0;
    }

    public static /* synthetic */ void lambda$ensureSubscribed$2(List list) throws Exception {
        sIdleCount.set(0);
        ArrayMap arrayMap = new ArrayMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            List list2 = (List) arrayMap.get(pair.first);
            if (list2 == null) {
                list2 = new LinkedList();
                arrayMap.put((String) pair.first, list2);
            }
            list2.add((Uri) pair.second);
        }
        Iterator it2 = arrayMap.entrySet().iterator();
        while (it2.hasNext()) {
            Map.Entry entry = (Map.Entry) it2.next();
            handleBufferedUris((String) entry.getKey(), (List) entry.getValue());
        }
    }

    public static void handleBufferedUris(String str, List<Uri> list) {
        DefaultLogger.d("MediaScannerReceiver", "handleBufferedUris, action: %s, count: %d", str, Integer.valueOf(list.size()));
        if ("android.intent.action.MEDIA_SCANNER_FINISHED".equals(str)) {
            handleMediaScannerFinished(StaticContext.sGetAndroidContext(), list);
        } else if (!"android.intent.action.MEDIA_SCANNER_SCAN_FILE".equals(str)) {
        } else {
            handleMediaScannerScanFile(StaticContext.sGetAndroidContext(), list);
        }
    }

    public static void handleMediaScannerFinished(Context context, List<Uri> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Uri uri : list) {
            if (uri != null && Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
                String path = uri.getPath();
                if (!TextUtils.isEmpty(path) && new File(path).isDirectory()) {
                    arrayList.add(path);
                }
            }
        }
        if (!BaseMiscUtil.isValid(arrayList)) {
            return;
        }
        ScannerEngine.getInstance().scanPathsAsync(arrayList, 11);
    }

    public static void handleMediaScannerScanFile(Context context, List<Uri> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Uri uri : list) {
            if (uri != null && Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
                String path = uri.getPath();
                if (!TextUtils.isEmpty(path)) {
                    String path2 = Environment.getExternalStorageDirectory().getPath();
                    String legacyExternalStorageDirectory = EnvironmentCompat.getLegacyExternalStorageDirectory();
                    try {
                        String canonicalPath = new File(path).getCanonicalPath();
                        if (ExtraTextUtils.startsWithIgnoreCase(canonicalPath, legacyExternalStorageDirectory)) {
                            canonicalPath = path2 + canonicalPath.substring(legacyExternalStorageDirectory.length());
                        }
                        if (shouldHandlePath(context.getApplicationContext(), BaseFileUtils.getParentFolderPath(canonicalPath))) {
                            DefaultLogger.i("MediaScannerReceiver", "ACTION_MEDIA_SCANNER_SCAN_FILE %s", canonicalPath);
                            arrayList.add(canonicalPath);
                        } else {
                            DefaultLogger.i("MediaScannerReceiver", "ACTION_MEDIA_SCANNER_SCAN_FILE but not trigger %s", canonicalPath);
                        }
                    } catch (IOException unused) {
                        DefaultLogger.w("MediaScannerReceiver", "can't canonicalize " + path);
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            return;
        }
        ScannerEngine.getInstance().scanPathsAsync(arrayList, 11);
    }

    public static boolean shouldHandlePath(Context context, String str) {
        return !ExtraTextUtils.startsWithIgnoreCase(StorageUtils.getRelativePath(context, str), StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM);
    }
}
