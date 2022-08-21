package com.miui.mediaeditor.api;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.biz.story.data.MediaInfo;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.util.BaseMiscUtil;
import com.xiaomi.stat.c.b;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public class MediaEditorIntentUtils {
    public static void startPickerForPhotoMovie(FragmentActivity fragmentActivity, FunctionModel functionModel) {
        if (MediaEditorApiHelper.isPhotoMovieAvailable()) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.putExtra("pick-upper-bound", functionModel.getFunctionLimitMaxSize());
            intent.putExtra("pick-lower-bound", functionModel.getFunctionLimitMinSize());
            intent.putExtra("extra_filter_media_type", new String[]{"image/x-adobe-dng"});
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.miui.mediaeditor", functionModel.getFunctionClassName1()));
            intent.putExtra("pick_intent", intent2);
            intent.putExtra("pick_close_type", 3);
            intent.putExtra("extra_from_type", b.j);
            intent.setPackage("com.miui.gallery");
            fragmentActivity.startActivity(intent);
            return;
        }
        loadLibraryInMediaEditor(fragmentActivity, functionModel.getFunctionClassName2(), functionModel.getFunctionLoadCode());
    }

    public static void startPickerForPhotoMovieTemp(FragmentActivity fragmentActivity) {
        Map<String, FunctionModel> functionModelMap = MediaEditorApiHelper.getFunctionModelMap();
        if (functionModelMap.get("photoMovie") != null) {
            startPickerForPhotoMovie(fragmentActivity, functionModelMap.get("photoMovie"));
        }
    }

    public static void startVlogFromPicker(FragmentActivity fragmentActivity, FunctionModel functionModel) {
        if (MediaEditorApiHelper.isVlogAvailable()) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("video/*");
            intent.putExtra("pick-upper-bound", functionModel.getFunctionLimitMaxSize());
            intent.putExtra("pick-lower-bound", functionModel.getFunctionLimitMinSize());
            intent.putExtra("pick_close_type", 3);
            intent.putExtra("disable_pending_transition", true);
            intent.putExtra("pick-need-origin", true);
            intent.putExtra("extra_from_type", b.i);
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.miui.mediaeditor", functionModel.getFunctionClassName1()));
            intent.putExtra("pick_intent", intent2);
            intent.putExtra("pick-need-origin", true);
            intent.setPackage("com.miui.gallery");
            fragmentActivity.startActivity(intent);
            return;
        }
        loadLibraryInMediaEditor(fragmentActivity, functionModel.getFunctionClassName2(), functionModel.getFunctionLoadCode());
    }

    public static void startVlogFromPickerTemp(FragmentActivity fragmentActivity) {
        Map<String, FunctionModel> functionModelMap = MediaEditorApiHelper.getFunctionModelMap();
        if (functionModelMap.get("vlog") != null) {
            startVlogFromPicker(fragmentActivity, functionModelMap.get("vlog"));
        }
    }

    public static void startMagicMattingFromPicker(FragmentActivity fragmentActivity, FunctionModel functionModel) {
        if (MediaEditorApiHelper.isMagicMattingAvailable()) {
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.setPackage("com.miui.gallery");
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.miui.mediaeditor", functionModel.getFunctionClassName1()));
            intent.putExtra("pick_intent", intent2);
            intent.putExtra("pick-upper-bound", functionModel.getFunctionLimitMaxSize());
            intent.putExtra("pick_close_type", 3);
            fragmentActivity.startActivity(intent);
            return;
        }
        loadLibraryInMediaEditor(fragmentActivity, functionModel.getFunctionClassName2(), functionModel.getFunctionLoadCode());
    }

    public static void startMagicMattingFromPickerTemp(FragmentActivity fragmentActivity) {
        Map<String, FunctionModel> functionModelMap = MediaEditorApiHelper.getFunctionModelMap();
        if (functionModelMap.get("magicMatting") != null) {
            startMagicMattingFromPicker(fragmentActivity, functionModelMap.get("magicMatting"));
        }
    }

    public static FunctionModel getFunctionModelWithTag(String str) {
        return MediaEditorApiHelper.getFunctionModelMap().get(str);
    }

    public static void startActivityWithTag(Activity activity, String str) {
        FunctionModel functionModelWithTag = getFunctionModelWithTag(str);
        if (functionModelWithTag != null) {
            if (TextUtils.equals(str, "idPhoto") && !MediaEditorApiHelper.isIDPhotoAvailable()) {
                loadLibraryInMediaEditor(activity, functionModelWithTag.getFunctionClassName2(), functionModelWithTag.getFunctionLoadCode());
            } else if (TextUtils.equals(str, "artStill") && !MediaEditorApiHelper.isArtStillAvailable()) {
                loadLibraryInMediaEditor(activity, functionModelWithTag.getFunctionClassName2(), functionModelWithTag.getFunctionLoadCode());
            } else if (TextUtils.equals(str, "videoPost") && !MediaEditorApiHelper.isVideoPostAvailable()) {
                loadLibraryInMediaEditor(activity, functionModelWithTag.getFunctionClassName2(), functionModelWithTag.getFunctionLoadCode());
            } else {
                startMagicFunctionGuideActivity(activity, functionModelWithTag);
            }
        }
    }

    public static void loadLibraryInMediaEditor(Activity activity, String str) {
        loadLibraryInMediaEditor(activity, "com.miui.mediaeditor.activity.DownloadLibraryActivity", str);
    }

    public static void requestSecretAlumAccessPermissionInMediaEditor(Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.mediaeditor.activity.SecretAlbumPermissionActivity"));
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadLibraryInMediaEditor(Activity activity, String str, String str2) {
        Intent intent = new Intent();
        intent.putExtra("loadType", str2);
        intent.setComponent(new ComponentName("com.miui.mediaeditor", str));
        activity.startActivity(intent);
    }

    public static void startMagicFunctionGuideActivity(Activity activity, FunctionModel functionModel) {
        if (functionModel == null || TextUtils.isEmpty(functionModel.getGuideActivity())) {
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.miui.mediaeditor", functionModel.getGuideActivity()));
        activity.startActivity(intent);
    }

    public static void startPhotoMovie(FragmentActivity fragmentActivity, List<MediaInfo> list, long j, int i, String str, String str2) {
        if (!MediaEditorApiHelper.isPhotoMovieAvailable()) {
            loadLibraryInMediaEditor(fragmentActivity, "photoMovie");
        } else if (BaseMiscUtil.isValid(list)) {
            int size = list.size();
            int i2 = 0;
            ClipData clipData = null;
            for (int i3 = 0; i3 < size && i2 < 20; i3++) {
                if (list.get(i3) != null) {
                    Uri translateToContent = GalleryOpenProvider.translateToContent(list.get(i3).getUri());
                    String sha1 = list.get(i3).getSha1();
                    if (clipData == null) {
                        clipData = new ClipData("data", new String[]{"image/*", "text/uri-list"}, new ClipData.Item(sha1, null, translateToContent));
                    } else {
                        clipData.addItem(new ClipData.Item(sha1, null, translateToContent));
                    }
                    i2++;
                }
            }
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.movie.ui.activity.MovieActivity"));
            intent.putExtra("movie_extra_preview_mode", true);
            intent.putExtra("card_id", j);
            intent.putExtra("card_title", str);
            intent.putExtra("card_sub_title", str2);
            intent.putExtra("movie_extra_template", i);
            intent.setClipData(clipData);
            fragmentActivity.startActivity(intent);
        }
    }

    public static void startFilterSkyFromPickerWithFunctionModel(Context context) {
        Map<String, FunctionModel> functionModelMap = MediaEditorApiHelper.getFunctionModelMap();
        if (functionModelMap.get("magicSky") != null) {
            FunctionModel functionModel = functionModelMap.get("magicSky");
            Intent intent = new Intent("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            intent.putExtra("pick-upper-bound", functionModel.getFunctionLimitMaxSize());
            Intent intent2 = new Intent();
            intent2.setComponent(new ComponentName("com.miui.mediaeditor", functionModel.getFunctionClassName1()));
            intent2.putExtra("editor_mode", 1);
            intent.putExtra("pick_intent", intent2);
            intent.putExtra("pick-owner", true);
            intent.putExtra("pick_close_type", 3);
            intent.setPackage("com.miui.gallery");
            context.startActivity(intent);
        }
    }

    public static boolean startEditAction(BaseDataItem baseDataItem, FragmentActivity fragmentActivity, GalleryFragment galleryFragment, boolean z, int i, ImageView imageView) {
        return startEditAction(baseDataItem, fragmentActivity, galleryFragment, true, z, i, imageView);
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x022c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean startEditAction(com.miui.gallery.model.BaseDataItem r14, androidx.fragment.app.FragmentActivity r15, com.miui.gallery.app.fragment.GalleryFragment r16, boolean r17, boolean r18, int r19, android.widget.ImageView r20) {
        /*
            Method dump skipped, instructions count: 583
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.mediaeditor.api.MediaEditorIntentUtils.startEditAction(com.miui.gallery.model.BaseDataItem, androidx.fragment.app.FragmentActivity, com.miui.gallery.app.fragment.GalleryFragment, boolean, boolean, int, android.widget.ImageView):boolean");
    }

    public static int getCollageMaxImageSize() {
        return MediaEditorApiHelper.getCollageMaxImageSize();
    }

    public static void startVlogFromStory(FragmentActivity fragmentActivity, String str, ArrayList<String> arrayList) {
        if (!MediaEditorApiHelper.isVlogAvailable()) {
            loadLibraryInMediaEditor(fragmentActivity, "vlog");
        } else if (TextUtils.isEmpty(str) || arrayList == null || arrayList.size() == 0) {
        } else {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.vlog.match.vlog.rule.VlogTemplateMatchActivity"));
            intent.putExtra("com.miui.gallery.vlog.extra.template", str);
            intent.putStringArrayListExtra("com.miui.gallery.vlog.extra.paths", arrayList);
            fragmentActivity.startActivity(intent);
        }
    }

    public static void startCollagePicker(Context context) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("pick_close_type", 3);
        intent.setPackage("com.miui.gallery");
        Intent intent2 = new Intent();
        intent2.setData(GalleryContract.Common.URI_COLLAGE_FROM_PICKER);
        intent.putExtra("pick_intent", intent2);
        intent.putExtra("pick-upper-bound", getCollageMaxImageSize());
        intent.putExtra("extra_from_type", b.k);
        context.startActivity(intent);
    }
}
