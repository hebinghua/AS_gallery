package com.miui.gallery.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ImageSizeUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class SaveUriDialogFragment extends GalleryDialogFragment {
    public OnCompleteListener mListener;
    public ProgressDialog mProgressDialog;

    /* loaded from: classes2.dex */
    public interface OnCompleteListener {
        void onComplete(String str);
    }

    public static void show(FragmentActivity fragmentActivity, Uri uri, OnCompleteListener onCompleteListener) {
        SaveUriDialogFragment saveUriDialogFragment = new SaveUriDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("key_uri", uri);
        saveUriDialogFragment.setArguments(bundle);
        saveUriDialogFragment.setOnCompleteListener(onCompleteListener);
        saveUriDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "SaveUriDialogFragment");
    }

    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.mListener = onCompleteListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog show = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.adding_to_album), true, false);
        this.mProgressDialog = show;
        return show;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        save((Uri) getArguments().getParcelable("key_uri"));
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        setOnCompleteListener(null);
        super.onDestroy();
    }

    public final void save(final Uri uri) {
        if (uri == null) {
            OnCompleteListener onCompleteListener = this.mListener;
            if (onCompleteListener == null) {
                return;
            }
            onCompleteListener.onComplete(null);
            return;
        }
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.ui.SaveUriDialogFragment.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public String mo1807run(ThreadPool.JobContext jobContext) {
                return SaveUriDialogFragment.this.saveInternal(uri);
            }
        }, new FutureListener<String>() { // from class: com.miui.gallery.ui.SaveUriDialogFragment.2
            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(final Future<String> future) {
                if (SaveUriDialogFragment.this.getActivity() != null) {
                    SaveUriDialogFragment.this.getActivity().runOnUiThread(new Runnable() { // from class: com.miui.gallery.ui.SaveUriDialogFragment.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (SaveUriDialogFragment.this.mListener != null) {
                                SaveUriDialogFragment.this.mListener.onComplete((String) future.get());
                            }
                            if (SaveUriDialogFragment.this.isAdded()) {
                                SaveUriDialogFragment.this.dismissAllowingStateLoss();
                            }
                        }
                    });
                }
            }
        });
    }

    public final String saveInternal(Uri uri) {
        File generateJPGFileForSaving;
        FileOutputStream fileOutputStream;
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(Environment.DIRECTORY_PICTURES);
        FileOutputStream fileOutputStream2 = null;
        if (StorageSolutionProvider.get().getDocumentFile(pathInPriorStorage, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SaveUriDialogFragment", "saveInternal")) == null || (generateJPGFileForSaving = generateJPGFileForSaving(pathInPriorStorage)) == null) {
            return null;
        }
        Bitmap blockingLoad = GlideLoadingUtils.blockingLoad(Glide.with(this), uri, GlideOptions.skipCacheOf().encodeFormat(Bitmap.CompressFormat.JPEG).mo953encodeQuality(100).mo952downsample(GalleryDownsampleStrategy.AT_MOST).mo970override(ImageSizeUtils.getMaxTextureSize()));
        if (blockingLoad == null) {
            DefaultLogger.e("SaveUriDialogFragment", "fail to convert %s to bitmap", uri.toString());
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(generateJPGFileForSaving);
            try {
                try {
                    blockingLoad.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    String absolutePath = generateJPGFileForSaving.getAbsolutePath();
                    ScannerEngine.getInstance().scanFile(getContext(), absolutePath, 8);
                    MediaScannerConnection.scanFile(getContext(), new String[]{absolutePath}, new String[]{BaseFileMimeUtil.getMimeType(absolutePath)}, null);
                    BaseMiscUtil.closeSilently(fileOutputStream);
                    blockingLoad.recycle();
                    return absolutePath;
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.e("SaveUriDialogFragment", "fail to save %s %s", uri, e);
                    BaseMiscUtil.closeSilently(fileOutputStream);
                    blockingLoad.recycle();
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                fileOutputStream2 = fileOutputStream;
                BaseMiscUtil.closeSilently(fileOutputStream2);
                blockingLoad.recycle();
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            fileOutputStream = null;
        } catch (Throwable th2) {
            th = th2;
            BaseMiscUtil.closeSilently(fileOutputStream2);
            blockingLoad.recycle();
            throw th;
        }
    }

    public static File generateJPGFileForSaving(String str) {
        String str2;
        int i = 0;
        while (true) {
            String format = String.format("%s/%s", str, String.format("SAVE_%s", new SimpleDateFormat("yyyyMMdd_kkmmss").format(new Date(System.currentTimeMillis()))));
            if (i > 0) {
                str2 = format + "_" + i + ".jpg";
            } else {
                str2 = format + ".jpg";
            }
            File file = new File(str2);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    return file;
                } catch (IOException e) {
                    DefaultLogger.e("SaveUriDialogFragment", "generateJPGFileForSaving() failed %s %s", file.getAbsolutePath(), e);
                    return null;
                }
            }
            i++;
        }
    }
}
