package com.miui.gallery.widget;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.DragEvent;
import android.view.View;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.MediaFile;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.appcompat.app.Fragment;

/* loaded from: classes2.dex */
public class ViewDragListener implements View.OnDragListener {
    public WeakReference<DropReceiver> mDropReceiverWeakReference;
    public WeakReference<Fragment> mFragmentWeakReference;

    /* loaded from: classes2.dex */
    public interface DropReceiver {
        boolean canReceive();

        boolean doAfterReceived(ArrayList<String> arrayList);

        String receivePath();
    }

    public ViewDragListener(Fragment fragment) {
        this.mFragmentWeakReference = new WeakReference<>(fragment);
        if (fragment instanceof DropReceiver) {
            this.mDropReceiverWeakReference = new WeakReference<>((DropReceiver) fragment);
        }
    }

    @Override // android.view.View.OnDragListener
    public boolean onDrag(View view, DragEvent dragEvent) {
        boolean needHandleEvent = needHandleEvent(dragEvent);
        switch (dragEvent.getAction()) {
            case 1:
            case 2:
            case 4:
            case 5:
            case 6:
                break;
            default:
                return false;
            case 3:
                handleDrop(view, dragEvent);
                break;
        }
        return needHandleEvent;
    }

    public final boolean needHandleEvent(DragEvent dragEvent) {
        WeakReference<DropReceiver> weakReference = this.mDropReceiverWeakReference;
        if (weakReference == null || weakReference.get() == null) {
            return false;
        }
        return this.mDropReceiverWeakReference.get().canReceive();
    }

    public final boolean handleDrop(View view, DragEvent dragEvent) {
        WeakReference<Fragment> weakReference;
        WeakReference<DropReceiver> weakReference2;
        if ((dragEvent.getClipDescription() == null || dragEvent.getClipDescription().getLabel() == null || !"MiuiGallery".equals(dragEvent.getClipDescription().getLabel().toString())) && needHandleEvent(dragEvent) && (weakReference = this.mFragmentWeakReference) != null && weakReference.get() != null && (weakReference2 = this.mDropReceiverWeakReference) != null && weakReference2.get() != null) {
            Fragment fragment = this.mFragmentWeakReference.get();
            String receivePath = this.mDropReceiverWeakReference.get().receivePath();
            if (receivePath == null) {
                receivePath = "MiShare";
            }
            String str = File.separator;
            if (!receivePath.endsWith(str)) {
                receivePath = receivePath + str;
            }
            String str2 = StorageUtils.getPathsInExternalStorage(GalleryApp.sGetAndroidContext(), receivePath)[0];
            if (str2 != null && dragEvent.getClipData() != null && dragEvent.getClipData().getDescription() != null) {
                String[] filterMimeTypes = dragEvent.getClipData().getDescription().filterMimeTypes("image/*");
                int length = filterMimeTypes != null ? filterMimeTypes.length : 0;
                String[] filterMimeTypes2 = dragEvent.getClipData().getDescription().filterMimeTypes("video/*");
                if (length + (filterMimeTypes2 != null ? filterMimeTypes2.length : 0) < dragEvent.getClipData().getDescription().getMimeTypeCount()) {
                    ToastUtils.makeText(fragment.getContext(), fragment.getString(R.string.drop_format_not_supported));
                    return false;
                } else if ((filterMimeTypes != null && filterMimeTypes.length > 0) || (filterMimeTypes2 != null && filterMimeTypes2.length > 0)) {
                    if (dragEvent.getClipData().getItemCount() > 0) {
                        Uri[] uriArr = new Uri[dragEvent.getClipData().getItemCount()];
                        if (!ViewDragPermissionManager.getInstance().requestDragPermission(fragment.getActivity(), dragEvent)) {
                            DefaultLogger.e("ViewDragListener", "requestDragPermission failed");
                            return false;
                        }
                        for (int i = 0; i < dragEvent.getClipData().getItemCount(); i++) {
                            uriArr[i] = dragEvent.getClipData().getItemAt(i).getUri();
                            fragment.getContext().grantUriPermission(fragment.getContext().getPackageName(), uriArr[i], 1);
                        }
                        if (!StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("ViewDragListener", "handleDrop")).exists()) {
                            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT_DIRECTORY;
                            IStoragePermissionStrategy.PermissionResult checkPermission = storageStrategyManager.checkPermission(str2, permission);
                            if (!checkPermission.granted) {
                                if (!checkPermission.applicable) {
                                    return false;
                                }
                                StorageSolutionProvider.get().requestPermission(fragment.requireActivity(), str2, permission);
                            }
                        }
                        String concat = BaseFileUtils.concat(str2, "test.jpg");
                        StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
                        IStoragePermissionStrategy.Permission permission2 = IStoragePermissionStrategy.Permission.INSERT;
                        IStoragePermissionStrategy.PermissionResult checkPermission2 = storageStrategyManager2.checkPermission(concat, permission2);
                        if (checkPermission2.granted) {
                            dropFiles(uriArr, str2);
                        } else if (!checkPermission2.applicable) {
                            return false;
                        } else {
                            StorageSolutionProvider.get().requestPermission(fragment.requireActivity(), concat, permission2);
                        }
                    }
                    return true;
                } else {
                    return view.onDragEvent(dragEvent);
                }
            }
        }
        return false;
    }

    public final void dropFiles(Uri[] uriArr, String str) {
        new DropFilesTask(str, this.mDropReceiverWeakReference).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uriArr);
    }

    /* loaded from: classes2.dex */
    public static class DropFilesTask extends AsyncTask<Uri, Void, Boolean> {
        public final WeakReference<DropReceiver> mDropReceiverWeakReference;
        public final String mSaveToPath;

        public DropFilesTask(String str, WeakReference<DropReceiver> weakReference) {
            this.mSaveToPath = str;
            this.mDropReceiverWeakReference = weakReference;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), String.format(GalleryApp.sGetAndroidContext().getString(R.string.prepare_to_drop), this.mSaveToPath));
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Uri... uriArr) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (Uri uri : uriArr) {
                String saveSingleFile = saveSingleFile(GalleryApp.sGetAndroidContext(), uri, this.mSaveToPath);
                if (saveSingleFile == null) {
                    return Boolean.FALSE;
                }
                arrayList.add(saveSingleFile);
            }
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                DefaultLogger.d("ViewDragListener", "dropfiles ready to trigger scan");
                ScannerEngine.getInstance().scanFile(GalleryApp.sGetAndroidContext(), it.next(), 8);
            }
            WeakReference<DropReceiver> weakReference = this.mDropReceiverWeakReference;
            if (weakReference != null && weakReference.get() != null) {
                this.mDropReceiverWeakReference.get().doAfterReceived(arrayList);
            }
            return Boolean.TRUE;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            ViewDragPermissionManager.getInstance().releasePermission();
            if (bool.booleanValue()) {
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext().getString(R.string.drop_success));
            } else {
                ToastUtils.makeText(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext().getString(R.string.drop_failed));
            }
        }

        public String saveSingleFile(Context context, Uri uri, String str) {
            OutputStream outputStream;
            InputStream inputStream;
            String type;
            Cursor cursor;
            String str2;
            String str3;
            String extensionFromMimeType;
            String concat;
            InputStream openInputStream;
            OutputStream openOutputStream;
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ViewDragListener", "saveSingleFile");
            StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
            InputStream inputStream2 = null;
            try {
                if (Build.VERSION.SDK_INT >= 26) {
                    cursor = context.getContentResolver().query(uri, null, null, null);
                    type = null;
                } else {
                    type = context.getContentResolver().getType(uri);
                    cursor = null;
                }
                if (cursor == null || !cursor.moveToFirst()) {
                    str2 = type;
                    str3 = null;
                } else {
                    str3 = cursor.getString(cursor.getColumnIndex("_display_name"));
                    str2 = cursor.getString(cursor.getColumnIndex("mime_type"));
                }
                if (str3 == null || str3.isEmpty()) {
                    if (str2 == null || (extensionFromMimeType = MediaFile.getExtensionFromMimeType(str2)) == null) {
                        BaseMiscUtil.closeSilently(null);
                        BaseMiscUtil.closeSilently(null);
                        return null;
                    }
                    str3 = System.currentTimeMillis() + "." + extensionFromMimeType;
                }
                concat = BaseFileUtils.concat(str, str3);
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                openInputStream = context.getContentResolver().openInputStream(uri);
                try {
                    openOutputStream = StorageSolutionProvider.get().openOutputStream(documentFile);
                } catch (Exception e) {
                    outputStream = null;
                    inputStream = openInputStream;
                    e = e;
                } catch (Throwable th) {
                    outputStream = null;
                    inputStream2 = openInputStream;
                    th = th;
                }
            } catch (Exception e2) {
                e = e2;
                inputStream = null;
                outputStream = null;
            } catch (Throwable th2) {
                th = th2;
                outputStream = null;
            }
            try {
            } catch (Exception e3) {
                inputStream = openInputStream;
                e = e3;
                outputStream = openOutputStream;
                try {
                    DefaultLogger.e("ViewDragListener", "Save attachment file failed", e);
                    BaseMiscUtil.closeSilently(inputStream);
                    BaseMiscUtil.closeSilently(outputStream);
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    inputStream2 = inputStream;
                    BaseMiscUtil.closeSilently(inputStream2);
                    BaseMiscUtil.closeSilently(outputStream);
                    throw th;
                }
            } catch (Throwable th4) {
                inputStream2 = openInputStream;
                th = th4;
                outputStream = openOutputStream;
                BaseMiscUtil.closeSilently(inputStream2);
                BaseMiscUtil.closeSilently(outputStream);
                throw th;
            }
            if (GalleryUtils.copyFile(openInputStream, openOutputStream)) {
                BaseMiscUtil.closeSilently(openInputStream);
                BaseMiscUtil.closeSilently(openOutputStream);
                return concat;
            }
            BaseMiscUtil.closeSilently(openInputStream);
            BaseMiscUtil.closeSilently(openOutputStream);
            return null;
        }
    }
}
