package com.miui.gallery.vlog.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.google.gson.Gson;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.os.FeatureHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Locale;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class VlogUtils {
    public static final Gson GSON = new Gson();

    public static float getSpeedRatioFitTrans(float f, float f2, float f3, float f4) {
        if (f <= 0.0f || f2 <= 0.0f || f3 <= 0.0f || f4 <= 0.0f) {
            return 1.0f;
        }
        float f5 = f * 2.0f;
        return f5 / ((f4 * ((f2 + f3) / 2.0f)) + f5);
    }

    public static <T extends AndroidViewModel> T getViewModel(FragmentActivity fragmentActivity, Class<T> cls) {
        return (T) new ViewModelProvider(fragmentActivity).get(cls);
    }

    public static Object newInstance(String str) {
        try {
            return Class.forName(str).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFormatedStr(String str) {
        return TextUtils.isEmpty(str) ? "" : str.replaceAll(" ", "").toLowerCase();
    }

    public static void showViews(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                view.setVisibility(0);
            }
        }
    }

    public static void hideViews(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                view.setVisibility(8);
            }
        }
    }

    public static Context getGalleryApp() {
        return ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).GetAndroidContext();
    }

    public static Uri translateToContent(String str) {
        return ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).translateToContent(str);
    }

    public static boolean is960FpsVideo(String str) {
        return ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).is960FpsVideo(str);
    }

    public static String generateOutputFilePath(File file) {
        return ((VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class)).generateOutputFilePath(file);
    }

    public static int getDimensionPixelSize(int i) {
        return getGalleryApp().getResources().getDimensionPixelSize(i);
    }

    public static String loadJsonFile(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str), Keyczar.DEFAULT_ENCODING));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(readLine);
            }
            bufferedReader.close();
        } catch (IOException unused) {
            stringBuffer.delete(0, stringBuffer.length());
        }
        return stringBuffer.toString();
    }

    public static <T> T fromJson(String str, Type type) {
        return (T) GSON.fromJson(str, type);
    }

    public static String getFormatTime(long j) {
        long j2 = j / 60000;
        long j3 = j - (60000 * j2);
        long j4 = (j2 != 0 || j3 <= 0 || j3 >= 1000) ? j3 / 1000 : 1L;
        StringBuilder sb = new StringBuilder();
        sb.append(j2);
        sb.append(":");
        if (j4 >= 10) {
            sb.append(j4);
        } else {
            sb.append("0");
            sb.append(j4);
        }
        return sb.toString();
    }

    public static String getFormatTimePrecise(long j) {
        long j2 = j / 60000;
        long j3 = (j - (60000 * j2)) / 1000;
        long j4 = ((j - ((60 * j2) * 1000)) - (1000 * j3)) / 10;
        StringBuilder sb = new StringBuilder();
        sb.append(j2);
        sb.append(":");
        if (j3 >= 10) {
            sb.append(j3);
        } else {
            sb.append("0");
            sb.append(j3);
        }
        sb.append(":");
        if (j4 >= 10) {
            sb.append(j4);
        } else {
            sb.append("0");
            sb.append(j4);
        }
        return sb.toString();
    }

    public static String getClipDuration(long j) {
        if (j <= 60000) {
            return getGalleryApp().getString(R$string.vlog_clip_duration_time, String.format(Locale.US, "%.1f", Float.valueOf(((float) j) / 1000.0f)));
        }
        return FormatUtil.formatVideoDuration(j / 1000);
    }

    public static void checkResExistWithTemplate(FragmentActivity fragmentActivity, VlogResource vlogResource, String str) {
        if (vlogResource instanceof HeaderTailData) {
            HeaderTailData headerTailData = (HeaderTailData) vlogResource;
            StringBuilder sb = new StringBuilder();
            sb.append(VlogConfig.TEMPALTE_PATH);
            String str2 = File.separator;
            sb.append(str2);
            sb.append(str);
            sb.append(str2);
            sb.append(headerTailData.getFileName());
            if (!new File(sb.toString()).exists()) {
                return;
            }
            headerTailData.setDownloadState(17);
        } else if (!(vlogResource instanceof FilterData)) {
        } else {
            FilterData filterData = (FilterData) vlogResource;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(VlogConfig.TEMPALTE_PATH);
            String str3 = File.separator;
            sb2.append(str3);
            sb2.append(str);
            sb2.append(str3);
            sb2.append(filterData.getFileName());
            String sb3 = sb2.toString();
            if (!new File(sb3).exists()) {
                return;
            }
            filterData.setDownloadState(17);
            filterData.setPath(sb3);
        }
    }

    public static void checkResourceVersionAndClear(String str, long j, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || VlogPreference.getVersion(str, 0L) == j) {
            return;
        }
        DefaultLogger.d("VlogUtils", "clear resource %s", str);
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VlogUtils", "checkResourceVersionAndClear"));
        if (documentFile != null) {
            documentFile.delete();
        }
        VlogPreference.setVersion(str, j);
    }

    public static int getVlogNameResId() {
        Locale locale;
        int i = R$string.vlog_mi_clip;
        Context galleryApp = getGalleryApp();
        if (galleryApp == null) {
            return i;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            locale = galleryApp.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = galleryApp.getResources().getConfiguration().locale;
        }
        return (locale == null || BaseBuildUtil.isInternational() || !TextUtils.equals("zh", locale.getLanguage()) || !TextUtils.equals("CN", locale.getCountry())) ? i : R$string.vlog_mi_clip_cn;
    }

    public static void cleanUpTransCodeFile() {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(VlogConfig.getTransCodePath(), IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VlogUtils", "cleanUpTransCodeFile"));
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    public static boolean isBlackShark() {
        return FeatureHelper.isBlackShark();
    }

    public static String getJsonFromAssetDir(Context context, String str) {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(context.getAssets().open(str)));
                while (true) {
                    try {
                        String readLine = bufferedReader2.readLine();
                        if (readLine == null) {
                            break;
                        }
                        sb.append(readLine);
                    } catch (IOException e) {
                        e = e;
                        bufferedReader = bufferedReader2;
                        DefaultLogger.d("VlogUtils", "getJsonFromAssetDir error1: %s", e.toString());
                        sb.delete(0, sb.length());
                        BaseMiscUtil.closeSilently(bufferedReader);
                        return sb.toString();
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader2;
                        BaseMiscUtil.closeSilently(bufferedReader);
                        throw th;
                    }
                }
                BaseMiscUtil.closeSilently(bufferedReader2);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e2) {
            e = e2;
        }
        return sb.toString();
    }

    public static boolean isLandscape(Context context) {
        return context != null && (context instanceof FragmentActivity) && ((VlogModel) getViewModel((FragmentActivity) context, VlogModel.class)).isSingleVideoEdit() && !EditorOrientationHelper.isLayoutPortrait(context);
    }

    public static int menuTopAndContentDimenId(Context context) {
        if (isLandscape(context)) {
            return R$dimen.vlog_main_landscape_menu_line_between_top_and_content;
        }
        return R$dimen.vlog_main_menu_line_between_top_and_content;
    }

    public static int menuBottomLineDimenId(Context context) {
        if (isLandscape(context)) {
            return R$dimen.vlog_main_landscape_menu_line_bottom_line;
        }
        return R$dimen.vlog_main_menu_line_bottom_line;
    }

    public static int menuContentAndBottomDimenId(Context context) {
        if (isLandscape(context)) {
            return R$dimen.vlog_main_landscape_menu_line_between_content_and_bottom;
        }
        return R$dimen.vlog_main_menu_line_between_content_and_bottom;
    }
}
