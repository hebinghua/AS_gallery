package com.miui.gallery.request;

import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.tracing.Trace;
import cn.wps.kmo.kmoservice_sdk.common.TaskResult;
import cn.wps.kmo.kmoservice_sdk.imageconverterpdf.ImageConverterPdfControl;
import cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper;
import cn.wps.kmo.kmoservice_sdk.utils.HandlerUtil;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class PicToPdfHelper {
    public static final LazyValue<Void, Boolean> SUPPORT_PIC_TO_PDF = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.request.PicToPdfHelper.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit */
        public Boolean mo1272onInit(Void r4) {
            if (Rom.IS_INTERNATIONAL) {
                DefaultLogger.fd("PicToPdfHelper", "is international rom, not support to pdf");
                return Boolean.FALSE;
            } else if (Rom.IS_MIUI) {
                PicToPdfHelper.getInstance().initWpsSdk();
                boolean z = PicToPdfHelper.getInstance().mIsWpsSupport;
                DefaultLogger.d("PicToPdfHelper", "initWpsSdk : is support to pdf [%b]", Boolean.valueOf(z));
                return Boolean.valueOf(z);
            } else {
                DefaultLogger.fd("PicToPdfHelper", "is not miui rom, not support to pdf");
                return Boolean.FALSE;
            }
        }
    };
    public static PicToPdfHelper sPicToPdfHelper;
    public OnSavePdfCompleteListener mOnSavePdfCompleteListener;
    public long mOnSaveStartTime;
    public volatile boolean mIsWpsInit = false;
    public volatile boolean mIsWpsSupport = false;
    public String mNoteText = "记录转化情况：\n";
    public final ImageConverterPdfControl mImageConverterPdfControl = ImageConverterPdfControl.getInstance();

    /* loaded from: classes2.dex */
    public interface OnSavePdfCompleteListener {
        void onSavePdfComplete(String str);
    }

    /* renamed from: $r8$lambda$-mbnSugi0RaAz2HL3Nx624MOVg4 */
    public static /* synthetic */ void m1261$r8$lambda$mbnSugi0RaAz2HL3Nx624MOVg4(PicToPdfHelper picToPdfHelper, List list, String str, boolean z) {
        picToPdfHelper.lambda$onSavePdf$1(list, str, z);
    }

    /* renamed from: $r8$lambda$IGR6KtFk3Zg-2GQD0DflhyY6F9w */
    public static /* synthetic */ void m1262$r8$lambda$IGR6KtFk3Zg2GQD0DflhyY6F9w(PicToPdfHelper picToPdfHelper, String str, TaskResult taskResult) {
        picToPdfHelper.lambda$imgConvertPdfTask$6(str, taskResult);
    }

    /* renamed from: $r8$lambda$W-imGAkTma9ymy_N77UQpDkMUpc */
    public static /* synthetic */ void m1263$r8$lambda$WimGAkTma9ymy_N77UQpDkMUpc(PicToPdfHelper picToPdfHelper) {
        picToPdfHelper.lambda$onSavePdf$0();
    }

    /* renamed from: $r8$lambda$e1XzkwuQOMNaQkkk364aX-Gq1BA */
    public static /* synthetic */ void m1264$r8$lambda$e1XzkwuQOMNaQkkk364aXGq1BA(PicToPdfHelper picToPdfHelper, Future future) {
        picToPdfHelper.lambda$imgConvertPdfTask$4(future);
    }

    /* renamed from: $r8$lambda$iIJqWRPniuk-8eEJKZ7HODfZwsY */
    public static /* synthetic */ void m1265$r8$lambda$iIJqWRPniuk8eEJKZ7HODfZwsY(PicToPdfHelper picToPdfHelper, Future future) {
        picToPdfHelper.lambda$imgConvertPdfTask$5(future);
    }

    /* renamed from: $r8$lambda$myR5myx9JV0IvLdgQqVOS-E6Q5k */
    public static /* synthetic */ void m1266$r8$lambda$myR5myx9JV0IvLdgQqVOSE6Q5k(PicToPdfHelper picToPdfHelper, OnSavePdfCompleteListener onSavePdfCompleteListener) {
        picToPdfHelper.lambda$onSavePdf$2(onSavePdfCompleteListener);
    }

    /* renamed from: $r8$lambda$z1iA69_ghetLDVJ-zvubPy00mmI */
    public static /* synthetic */ String m1267$r8$lambda$z1iA69_ghetLDVJzvubPy00mmI(int i, String str, String str2, ThreadPool.JobContext jobContext) {
        return lambda$imgConvertPdfTask$3(i, str, str2, jobContext);
    }

    public static synchronized PicToPdfHelper getInstance() {
        PicToPdfHelper picToPdfHelper;
        synchronized (PicToPdfHelper.class) {
            if (sPicToPdfHelper == null) {
                sPicToPdfHelper = new PicToPdfHelper();
            }
            picToPdfHelper = sPicToPdfHelper;
        }
        return picToPdfHelper;
    }

    public static boolean isPicToPdfSupport() {
        return SUPPORT_PIC_TO_PDF.get(null).booleanValue();
    }

    public final void initWpsSdk() {
        if (!this.mIsWpsInit) {
            Trace.beginSection("initWpsSdk");
            try {
                long uptimeMillis = SystemClock.uptimeMillis();
                this.mImageConverterPdfControl.init(GalleryApp.sGetAndroidContext());
                this.mIsWpsSupport = this.mImageConverterPdfControl.checkAppInvalid().mResultCode == 10001;
                this.mIsWpsInit = true;
                DefaultLogger.i("PicToPdfHelper", String.format(Locale.US, "wps init time is %dms", Long.valueOf(SystemClock.uptimeMillis() - uptimeMillis)));
            } finally {
                Trace.endSection();
            }
        }
    }

    public void onSavePdf(final List<String> list, final String str, final OnSavePdfCompleteListener onSavePdfCompleteListener) {
        initWpsSdk();
        this.mOnSavePdfCompleteListener = onSavePdfCompleteListener;
        this.mOnSaveStartTime = SystemClock.uptimeMillis();
        TaskResult checkAppInvalid = this.mImageConverterPdfControl.checkAppInvalid();
        int i = checkAppInvalid.mResultCode;
        if (i == 10001) {
            this.mImageConverterPdfControl.tryToBindService();
            this.mImageConverterPdfControl.requestPermission(new CorServiceHelper.OnPermissionListener() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda0
                @Override // cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper.OnPermissionListener
                public final void requestPermission(boolean z) {
                    PicToPdfHelper.m1261$r8$lambda$mbnSugi0RaAz2HL3Nx624MOVg4(PicToPdfHelper.this, list, str, z);
                }
            });
            return;
        }
        Bundle bundle = checkAppInvalid.mBundle;
        this.mNoteText += (bundle != null ? bundle.getString("MSG") : "") + "---------------------------" + i + '\n';
        HandlerUtil.runOnUiThread(new Runnable() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PicToPdfHelper.m1266$r8$lambda$myR5myx9JV0IvLdgQqVOSE6Q5k(PicToPdfHelper.this, onSavePdfCompleteListener);
            }
        });
    }

    public /* synthetic */ void lambda$onSavePdf$1(List list, String str, boolean z) {
        if (z) {
            imgConvertPdfTask(list, str, 1);
            return;
        }
        this.mNoteText += "还没有读写权限-----------------\n";
        HandlerUtil.runOnUiThread(new Runnable() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PicToPdfHelper.m1263$r8$lambda$WimGAkTma9ymy_N77UQpDkMUpc(PicToPdfHelper.this);
            }
        });
    }

    public /* synthetic */ void lambda$onSavePdf$0() {
        DefaultLogger.i("PicToPdfHelper", this.mNoteText);
        OnSavePdfCompleteListener onSavePdfCompleteListener = this.mOnSavePdfCompleteListener;
        if (onSavePdfCompleteListener != null) {
            onSavePdfCompleteListener.onSavePdfComplete(null);
            this.mOnSavePdfCompleteListener = null;
        }
    }

    public /* synthetic */ void lambda$onSavePdf$2(OnSavePdfCompleteListener onSavePdfCompleteListener) {
        DefaultLogger.i("PicToPdfHelper", this.mNoteText);
        if (onSavePdfCompleteListener != null) {
            onSavePdfCompleteListener.onSavePdfComplete(null);
        }
    }

    public final void imgConvertPdfTask(List<String> list, final String str, int i) {
        SdkUtils.ICallback iCallback = new SdkUtils.ICallback() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda1
            @Override // cn.wps.kmo.kmoservice_sdk.utils.SdkUtils.ICallback
            public final void callback(TaskResult taskResult) {
                PicToPdfHelper.m1262$r8$lambda$IGR6KtFk3Zg2GQD0DflhyY6F9w(PicToPdfHelper.this, str, taskResult);
            }
        };
        ArrayList arrayList = new ArrayList();
        for (String str2 : list) {
            Uri translateToContent = GalleryOpenProvider.translateToContent(str2);
            GalleryApp.sGetAndroidContext().grantUriPermission("cn.wps.moffice_eng.xiaomi.lite", translateToContent, 1);
            arrayList.add(translateToContent);
        }
        this.mImageConverterPdfControl.startTask(arrayList, i, iCallback);
    }

    public /* synthetic */ void lambda$imgConvertPdfTask$6(final String str, TaskResult taskResult) {
        long uptimeMillis = SystemClock.uptimeMillis();
        int i = 0;
        DefaultLogger.i("PicToPdfHelper", String.format(Locale.US, "wps convert time is %dms", Long.valueOf(uptimeMillis - this.mOnSaveStartTime)));
        this.mOnSaveStartTime = uptimeMillis;
        final int i2 = taskResult.mResultCode;
        Bundle bundle = taskResult.mBundle;
        final String valueOf = bundle != null ? String.valueOf(bundle.getParcelable("URI")) : "";
        Bundle bundle2 = taskResult.mBundle;
        if (bundle2 != null) {
            i = bundle2.getInt("SUCCESS_IMAGE_CODE", 0);
        }
        this.mNoteText += valueOf + "--------------" + i2 + "--------------" + i + '\n';
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return PicToPdfHelper.m1267$r8$lambda$z1iA69_ghetLDVJzvubPy00mmI(i2, valueOf, str, jobContext);
            }
        }, new FutureListener() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.concurrent.FutureListener
            public final void onFutureDone(Future future) {
                PicToPdfHelper.m1265$r8$lambda$iIJqWRPniuk8eEJKZ7HODfZwsY(PicToPdfHelper.this, future);
            }
        });
    }

    public static /* synthetic */ String lambda$imgConvertPdfTask$3(int i, String str, String str2, ThreadPool.JobContext jobContext) {
        OutputStream outputStream;
        String str3;
        InputStream inputStream = null;
        String str4 = null;
        inputStream = null;
        if (i == 11000) {
            Uri parse = Uri.parse(str);
            try {
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("PicToPdfHelper", "imgConvertPdfTask");
                InputStream openInputStream = GalleryApp.sGetAndroidContext().getContentResolver().openInputStream(parse);
                try {
                    File createOrRenameFile = createOrRenameFile(new File(str2));
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(createOrRenameFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag);
                    if (documentFile == null) {
                        BaseMiscUtil.closeSilently(openInputStream);
                        BaseMiscUtil.closeSilently(null);
                        return null;
                    }
                    outputStream = StorageSolutionProvider.get().openOutputStream(documentFile);
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = openInputStream.read(bArr);
                            if (read < 0) {
                                outputStream.flush();
                                str4 = createOrRenameFile.getPath();
                                StorageSolutionProvider.get().apply(documentFile);
                                BaseMiscUtil.closeSilently(openInputStream);
                                BaseMiscUtil.closeSilently(outputStream);
                                return str4;
                            }
                            outputStream.write(bArr, 0, read);
                        }
                    } catch (Exception e) {
                        e = e;
                        String str5 = str4;
                        inputStream = openInputStream;
                        str3 = str5;
                        try {
                            e.printStackTrace();
                            BaseMiscUtil.closeSilently(inputStream);
                            BaseMiscUtil.closeSilently(outputStream);
                            return str3;
                        } catch (Throwable th) {
                            th = th;
                            BaseMiscUtil.closeSilently(inputStream);
                            BaseMiscUtil.closeSilently(outputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        inputStream = openInputStream;
                        BaseMiscUtil.closeSilently(inputStream);
                        BaseMiscUtil.closeSilently(outputStream);
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    outputStream = null;
                    inputStream = openInputStream;
                    str3 = null;
                } catch (Throwable th3) {
                    th = th3;
                    outputStream = null;
                }
            } catch (Exception e3) {
                e = e3;
                str3 = null;
                outputStream = null;
            } catch (Throwable th4) {
                th = th4;
                outputStream = null;
            }
        } else {
            return null;
        }
    }

    public /* synthetic */ void lambda$imgConvertPdfTask$5(final Future future) {
        HandlerUtil.runOnUiThread(new Runnable() { // from class: com.miui.gallery.request.PicToPdfHelper$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PicToPdfHelper.m1264$r8$lambda$e1XzkwuQOMNaQkkk364aXGq1BA(PicToPdfHelper.this, future);
            }
        });
    }

    public /* synthetic */ void lambda$imgConvertPdfTask$4(Future future) {
        DefaultLogger.i("PicToPdfHelper", this.mNoteText);
        DefaultLogger.i("PicToPdfHelper", String.format(Locale.US, "output file cost time is %dms", Long.valueOf(SystemClock.uptimeMillis() - this.mOnSaveStartTime)));
        OnSavePdfCompleteListener onSavePdfCompleteListener = this.mOnSavePdfCompleteListener;
        if (onSavePdfCompleteListener != null) {
            onSavePdfCompleteListener.onSavePdfComplete((String) future.get());
            this.mOnSavePdfCompleteListener = null;
        }
    }

    public void dispose() {
        this.mIsWpsInit = false;
        this.mIsWpsSupport = false;
        this.mOnSavePdfCompleteListener = null;
        this.mImageConverterPdfControl.dispose(true);
    }

    public static boolean prepareGotoPicToPdfPreviewPage(FragmentActivity fragmentActivity, List<CheckableAdapter.CheckedItem> list) {
        ArrayList arrayList = new ArrayList(list.size());
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            String mimeType = list.get(i2).getMimeType();
            CheckableAdapter.CheckedItem checkedItem = list.get(i2);
            if (isPicToPdfSupportType(mimeType)) {
                arrayList.add(checkedItem);
            } else {
                i++;
            }
        }
        if (arrayList.size() > 50) {
            ToastUtils.makeText(fragmentActivity, ResourceUtils.getQuantityString(R.plurals.pic_to_pdf_support_max_count, 50, 50));
            return false;
        } else if (arrayList.isEmpty()) {
            ToastUtils.makeText(fragmentActivity, ResourceUtils.getString(R.string.pic_to_pdf_all_not_support));
            return false;
        } else {
            if (i > 0) {
                ToastUtils.makeText(fragmentActivity, ResourceUtils.getQuantityString(R.plurals.pic_to_pdf_filter_count, i, Integer.valueOf(i)));
            }
            IntentUtil.gotoPicToPdfPreviewPage(fragmentActivity, (List<CheckableAdapter.CheckedItem>) arrayList);
            SamplingStatHelper.recordCountEvent("pic_to_pdf", "pic_to_pdf_picker_click");
            return true;
        }
    }

    public static boolean isPicToPdfSupportType(String str) {
        return BaseFileMimeUtil.isPngImageFromMimeType(str) || BaseFileMimeUtil.isJpegImageFromMimeType(str) || BaseFileMimeUtil.isBmpFromMimeType(str) || BaseFileMimeUtil.isWebpFromMimeType(str) || BaseFileMimeUtil.isHeifMimeType(str);
    }

    public static File createOrRenameFile(File file) {
        String[] fileInfo = getFileInfo(file);
        return createOrRenameFile(file, fileInfo[0], fileInfo[1]);
    }

    public static File createOrRenameFile(File file, String str, String str2) {
        File parentFile = file.getParentFile();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("PicToPdfHelper", "createOrRenameFile");
        if (!parentFile.exists() && StorageSolutionProvider.get().getDocumentFile(parentFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) != null) {
            DefaultLogger.v("PicToPdfHelper", "Created directory " + parentFile.getAbsolutePath());
        }
        String replaceAll = Pattern.compile("(\\s\\(\\d+\\))").matcher(str).replaceAll("");
        File file2 = new File(parentFile, replaceAll + str2);
        for (int i = 1; file2.exists() && i < Integer.MAX_VALUE; i++) {
            file2 = new File(parentFile, replaceAll + " (" + i + ")" + str2);
        }
        return file2;
    }

    public static String[] getFileInfo(File file) {
        String substring;
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(46);
        if (lastIndexOf == -1) {
            substring = "";
        } else {
            String substring2 = name.substring(0, lastIndexOf);
            substring = name.substring(lastIndexOf);
            name = substring2;
        }
        return new String[]{name, substring};
    }
}
