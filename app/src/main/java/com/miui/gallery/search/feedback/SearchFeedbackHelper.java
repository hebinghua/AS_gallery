package com.miui.gallery.search.feedback;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.core.source.server.ServerSearchRequest;
import com.miui.gallery.search.feedback.SearchFeedbackHelper;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.search.utils.SearchUtils;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SearchFeedbackHelper {
    public static boolean CAN_USE_CACHE = false;

    /* loaded from: classes2.dex */
    public static class FeedbackReportResponseData {
        @SerializedName("failureImageId")
        public Set<Long> failureImageIds;
    }

    /* loaded from: classes2.dex */
    public static class FeedbackTaskResponseData {
        @SerializedName("needHandleImageNum")
        public int needHandleImageNum = 0;
        @SerializedName("finishImageNum")
        public int finishImageNum = 0;
        @SerializedName("awardInfo")
        public String awardInfo = null;
    }

    /* loaded from: classes2.dex */
    public interface OnFeedbackCompleteListener {
        void onComplete(int i);
    }

    /* loaded from: classes2.dex */
    public enum FeedbackType {
        FALSE_POSITIVE("FP"),
        FALSE_NEGATIVE("FN");
        
        private final String mValue;

        FeedbackType(String str) {
            this.mValue = str;
        }

        public String getValue() {
            return this.mValue;
        }
    }

    public static boolean supportFeedbackTask() {
        return CloudControlManager.getInstance().queryFeatureStatus("search-feedback-task") == FeatureProfile.Status.ENABLE;
    }

    public static Bundle getFeedbackTaskInfo() {
        String xiaomiId = SearchUtils.getXiaomiId();
        if (xiaomiId == null) {
            SearchLog.d("SearchFeedbackHelper", "Not logged in!");
            return null;
        } else if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(GalleryApp.sGetAndroidContext())) {
            SearchLog.d("SearchFeedbackHelper", "Cloud privacy agreement denied");
            return null;
        } else {
            ServerSearchRequest build = new ServerSearchRequest.Builder().setQueryPathPrefix(HostManager.Search.getSearchFeedbackUrlHost()).setUserPath(ServerSearchRequest.Builder.getDefaultUserPath(xiaomiId)).setUserId(xiaomiId).setQueryAppendPath("tag/feedback/task/num").setResultDataType(FeedbackTaskResponseData.class).setUseCache(CAN_USE_CACHE).build();
            try {
                Object[] executeSync = build.executeSync();
                if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof FeedbackTaskResponseData)) {
                    FeedbackTaskResponseData feedbackTaskResponseData = (FeedbackTaskResponseData) executeSync[0];
                    Bundle bundle = new Bundle();
                    bundle.putInt("need_handle_image_num", feedbackTaskResponseData.needHandleImageNum);
                    bundle.putInt("finish_image_num", feedbackTaskResponseData.finishImageNum);
                    if (!TextUtils.isEmpty(feedbackTaskResponseData.awardInfo)) {
                        bundle.putString("task_award_info", feedbackTaskResponseData.awardInfo);
                    }
                    build.setCacheExpires(300000L);
                    CAN_USE_CACHE = true;
                    return bundle;
                }
            } catch (RequestError e) {
                SearchLog.e("SearchFeedbackHelper", "Querying feedback info failed, %s", e);
            }
            SearchLog.e("SearchFeedbackHelper", "Empty feedback task info");
            return null;
        }
    }

    public static void goToFeedbackPolicyPage(FragmentActivity fragmentActivity) {
        Object[] objArr = new Object[1];
        objArr[0] = Locale.getDefault().getLanguage().equals("zh") ? "cn" : "en";
        goToHybridPage(fragmentActivity, String.format("https://i.mi.com/static2?filename=MicloudWebBill/event/gallery/privacy/%s.html", objArr));
    }

    public static void goToHybridPage(FragmentActivity fragmentActivity, String str) {
        ActionURIHandler.handleUri(fragmentActivity, GalleryContract.Common.URI_HYBRID_PAGE.buildUpon().appendQueryParameter(MapBundleKey.MapObjKey.OBJ_URL, str).build());
    }

    public static boolean needToQueryLikelyImagesForFeedbackTask(String str) {
        return !hasReportedTag(str);
    }

    public static void reportFalsePositiveImages(FragmentActivity fragmentActivity, String str, boolean z, ArrayList<String> arrayList, OnFeedbackCompleteListener onFeedbackCompleteListener) {
        reportInDialog(fragmentActivity, str, z, arrayList, FeedbackType.FALSE_POSITIVE, onFeedbackCompleteListener);
    }

    public static void reportFalseNegativeImages(FragmentActivity fragmentActivity, String str, boolean z, ArrayList<String> arrayList, OnFeedbackCompleteListener onFeedbackCompleteListener) {
        reportInDialog(fragmentActivity, str, z, arrayList, FeedbackType.FALSE_NEGATIVE, onFeedbackCompleteListener);
    }

    public static void reportInDialog(FragmentActivity fragmentActivity, String str, boolean z, ArrayList<String> arrayList, FeedbackType feedbackType, OnFeedbackCompleteListener onFeedbackCompleteListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
        if (z || supportFeedbackTask()) {
            builder.setCheckBox(z, fragmentActivity.getString(R.string.search_feedback_contribute));
        }
        builder.setTitle(getFeedbackDialogText(fragmentActivity, str, feedbackType)).setPositiveButton(R.string.ok, new AnonymousClass1(str, arrayList, feedbackType, fragmentActivity, onFeedbackCompleteListener)).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        try {
            builder.create().show();
        } catch (IllegalStateException e) {
            SearchLog.w("SearchFeedbackHelper", "Ignore exception: %s", e);
        }
    }

    /* renamed from: com.miui.gallery.search.feedback.SearchFeedbackHelper$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements DialogInterface.OnClickListener {
        public final /* synthetic */ FragmentActivity val$activity;
        public final /* synthetic */ OnFeedbackCompleteListener val$completeListener;
        public final /* synthetic */ FeedbackType val$feedbackType;
        public final /* synthetic */ ArrayList val$imageServerIds;
        public final /* synthetic */ String val$tagName;

        public static /* synthetic */ Integer $r8$lambda$OY3jFpEXYGHjhlNxncqg8ToU0ug(String str, ArrayList arrayList, FeedbackType feedbackType, boolean z, Void[] voidArr) {
            return lambda$onClick$0(str, arrayList, feedbackType, z, voidArr);
        }

        public AnonymousClass1(String str, ArrayList arrayList, FeedbackType feedbackType, FragmentActivity fragmentActivity, OnFeedbackCompleteListener onFeedbackCompleteListener) {
            this.val$tagName = str;
            this.val$imageServerIds = arrayList;
            this.val$feedbackType = feedbackType;
            this.val$activity = fragmentActivity;
            this.val$completeListener = onFeedbackCompleteListener;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            final boolean isChecked = ((AlertDialog) dialogInterface).isChecked();
            final String str = this.val$tagName;
            final ArrayList arrayList = this.val$imageServerIds;
            final FeedbackType feedbackType = this.val$feedbackType;
            ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.search.feedback.SearchFeedbackHelper$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public final Object doProcess(Object[] objArr) {
                    return SearchFeedbackHelper.AnonymousClass1.$r8$lambda$OY3jFpEXYGHjhlNxncqg8ToU0ug(str, arrayList, feedbackType, isChecked, (Void[]) objArr);
                }
            });
            processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.search.feedback.SearchFeedbackHelper.1.1
                {
                    AnonymousClass1.this = this;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                public void onCompleteProcess(Object obj) {
                    int intValue = obj == null ? 0 : ((Integer) obj).intValue();
                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                    ToastUtils.makeText(AnonymousClass1.this.val$activity, SearchFeedbackHelper.getFeedbackResultText(anonymousClass1.val$activity, anonymousClass1.val$tagName, anonymousClass1.val$feedbackType, intValue, isChecked));
                    OnFeedbackCompleteListener onFeedbackCompleteListener = AnonymousClass1.this.val$completeListener;
                    if (onFeedbackCompleteListener != null) {
                        onFeedbackCompleteListener.onComplete(intValue);
                    }
                }
            });
            FragmentActivity fragmentActivity = this.val$activity;
            processTask.showProgress(fragmentActivity, fragmentActivity.getString(R.string.operation_in_process));
            processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        public static /* synthetic */ Integer lambda$onClick$0(String str, ArrayList arrayList, FeedbackType feedbackType, boolean z, Void[] voidArr) {
            return Integer.valueOf(SearchFeedbackHelper.reportFalseImages(str, arrayList, feedbackType, z));
        }
    }

    /* renamed from: com.miui.gallery.search.feedback.SearchFeedbackHelper$2 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType;

        static {
            int[] iArr = new int[FeedbackType.values().length];
            $SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType = iArr;
            try {
                iArr[FeedbackType.FALSE_POSITIVE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType[FeedbackType.FALSE_NEGATIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static String getFeedbackDialogText(Context context, String str, FeedbackType feedbackType) {
        int i;
        int i2 = AnonymousClass2.$SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType[feedbackType.ordinal()];
        if (i2 == 1) {
            i = R.string.search_feedback_false_positive_text;
        } else if (i2 != 2) {
            throw new IllegalArgumentException("Invalid feedback type " + feedbackType.getValue());
        } else {
            i = R.string.search_feedback_false_negative_text;
        }
        return context.getString(i, str);
    }

    public static String getFeedbackResultText(Context context, String str, FeedbackType feedbackType, int i, boolean z) {
        int i2;
        if (i > 0) {
            int i3 = AnonymousClass2.$SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType[feedbackType.ordinal()];
            if (i3 == 1) {
                i2 = z ? R.plurals.search_feedback_done_mark_and_contribute_false_positive : R.plurals.search_feedback_done_mark_false_positive;
            } else if (i3 != 2) {
                throw new IllegalArgumentException("Invalid feedback type " + feedbackType.getValue());
            } else {
                i2 = z ? R.plurals.search_feedback_done_mark_and_contribute_false_negative : R.plurals.search_feedback_done_mark_false_negative;
            }
            return context.getResources().getQuantityString(i2, i, Integer.valueOf(i), str);
        }
        int i4 = AnonymousClass2.$SwitchMap$com$miui$gallery$search$feedback$SearchFeedbackHelper$FeedbackType[feedbackType.ordinal()];
        if (i4 == 1) {
            return context.getString(R.string.search_failed_mark_feedback_false_positive);
        }
        if (i4 == 2) {
            return context.getString(R.string.search_failed_mark_feedback_false_negative);
        }
        throw new IllegalArgumentException("Invalid feedback type " + feedbackType.getValue());
    }

    public static int reportFalseImages(String str, List<String> list, FeedbackType feedbackType, boolean z) {
        if (TextUtils.isEmpty(str) || list == null || list.isEmpty()) {
            SearchLog.d("SearchFeedbackHelper", "Invalid false image params!");
            return 0;
        }
        String xiaomiId = SearchUtils.getXiaomiId();
        if (xiaomiId == null) {
            SearchLog.d("SearchFeedbackHelper", "Not logged in!");
            return 0;
        }
        int i = 0;
        int i2 = 0;
        while (i < list.size()) {
            int min = Math.min(list.size(), i + 10);
            i2 += reportFalseImagesBatch(xiaomiId, str, list.subList(i, min), feedbackType, z);
            i = min;
        }
        if (i2 > 0) {
            CAN_USE_CACHE = false;
            if (feedbackType.equals(FeedbackType.FALSE_NEGATIVE)) {
                addFeedbackReportedTag(str);
            }
        }
        return i2;
    }

    public static int reportFalseImagesBatch(String str, String str2, List<String> list, FeedbackType feedbackType, boolean z) {
        FeedbackReportResponseData feedbackReportResponseData;
        Set<Long> set;
        if (!PrivacyAgreementUtils.isCloudServiceAgreementEnable(GalleryApp.sGetAndroidContext())) {
            SearchLog.d("SearchFeedbackHelper", "Cloud privacy agreement denied");
            return 0;
        }
        int size = list.size();
        String join = TextUtils.join(",", list);
        SearchLog.d("SearchFeedbackHelper", "Reporting false image info [%s: %s: %s]", feedbackType, str2, list);
        try {
            Object[] executeSync = new ServerSearchRequest.Builder().setQueryPathPrefix(HostManager.Search.getSearchFeedbackUrlHost()).setQueryAppendPath("tag/feedback").setUserPath(ServerSearchRequest.Builder.getDefaultUserPath(str)).setMethod(1002).setUserId(str).setResultDataType(FeedbackReportResponseData.class).addQueryParam("feedbackType", feedbackType.getValue()).addQueryParam("tagName", str2).addQueryParam("imageIds", join).addQueryParam("isDonate", String.valueOf(z)).build().executeSync();
            if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof FeedbackReportResponseData) && (set = (feedbackReportResponseData = (FeedbackReportResponseData) executeSync[0]).failureImageIds) != null) {
                SearchLog.d("SearchFeedbackHelper", "[%s] report failed!", set);
                return size - feedbackReportResponseData.failureImageIds.size();
            }
            SearchLog.d("SearchFeedbackHelper", "Done report batch!");
            return size;
        } catch (RequestError e) {
            SearchLog.e("SearchFeedbackHelper", "On report batch error! %s", e);
            return 0;
        }
    }

    public static void addFeedbackReportedTag(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String feedbackReportedTags = GalleryPreferences.Search.getFeedbackReportedTags();
        if (!TextUtils.isEmpty(str)) {
            str = feedbackReportedTags + "," + str;
        }
        GalleryPreferences.Search.setFeedbackReportedTags(str);
    }

    public static boolean hasReportedTag(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String feedbackReportedTags = GalleryPreferences.Search.getFeedbackReportedTags();
        if (!TextUtils.isEmpty(feedbackReportedTags)) {
            for (String str2 : feedbackReportedTags.split(",")) {
                if (str.equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
