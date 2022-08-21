package com.miui.gallery.analytics;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.function.BiConsumer;

/* loaded from: classes.dex */
public class FirebaseHelper implements ITrackHelper {
    public FirebaseAnalytics mFirebaseAnalytics;
    public ContentObserver mFirebaseAnalyticsObserver;
    public FirebaseCrashlytics mFirebaseCrashlytics;
    public boolean mUploadLogPref;

    public static /* synthetic */ void $r8$lambda$VP37z50DoCzoIgGY3CNqxxVr0Yk(Bundle bundle, String str, Object obj) {
        lambda$track$0(bundle, str, obj);
    }

    @Override // com.miui.gallery.analytics.ITrackHelper
    public void init(Context context) {
        FirebaseApp.initializeApp(context);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        this.mFirebaseCrashlytics = FirebaseCrashlytics.getInstance();
        boolean isCheckedImprovementProgram = BaseMiscUtil.isCheckedImprovementProgram(context);
        this.mUploadLogPref = isCheckedImprovementProgram;
        this.mFirebaseAnalytics.setAnalyticsCollectionEnabled(isCheckedImprovementProgram);
        this.mFirebaseCrashlytics.setCrashlyticsCollectionEnabled(this.mUploadLogPref);
        this.mFirebaseAnalyticsObserver = new FirebaseAnalyticsObserver(this, context, ThreadManager.getMainHandler());
        context.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("upload_log_pref"), true, this.mFirebaseAnalyticsObserver);
    }

    @Override // com.miui.gallery.analytics.ITrackHelper
    public void track(String str, Map<String, Object> map) {
        final Bundle bundle = new Bundle();
        map.forEach(new BiConsumer() { // from class: com.miui.gallery.analytics.FirebaseHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                FirebaseHelper.$r8$lambda$VP37z50DoCzoIgGY3CNqxxVr0Yk(bundle, (String) obj, obj2);
            }
        });
        this.mFirebaseAnalytics.logEvent(str, bundle);
    }

    public static /* synthetic */ void lambda$track$0(Bundle bundle, String str, Object obj) {
        String valueOf = String.valueOf(obj);
        String[] split = valueOf.split("\\.");
        if (split.length >= 5) {
            String str2 = split[1];
            String str3 = split[2];
            str.hashCode();
            if (str.equals("tip")) {
                bundle.putString("tip_page_id", str2);
                bundle.putString("tip_module_id", str3);
            } else if (str.equals("ref_tip")) {
                bundle.putString("ref_tip_page_id", str2);
                bundle.putString("ref_tip_module_id", str3);
            } else {
                DefaultLogger.d("FireBaseHelper", "Wrong string ==> string = %s" + str);
            }
        }
        bundle.putString(str, valueOf);
    }

    public final void onImprovementProgramChanged(boolean z) {
        if (this.mUploadLogPref != z) {
            this.mUploadLogPref = z;
            this.mFirebaseAnalytics.setAnalyticsCollectionEnabled(z);
        }
    }

    /* loaded from: classes.dex */
    public static class FirebaseAnalyticsObserver extends ContentObserver {
        public WeakReference<Context> mContext;
        public WeakReference<FirebaseHelper> mFirebaseHelperRef;

        public FirebaseAnalyticsObserver(FirebaseHelper firebaseHelper, Context context, Handler handler) {
            super(handler);
            this.mFirebaseHelperRef = new WeakReference<>(firebaseHelper);
            this.mContext = new WeakReference<>(context);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            FirebaseHelper firebaseHelper = this.mFirebaseHelperRef.get();
            Context context = this.mContext.get();
            if (firebaseHelper == null || context == null) {
                return;
            }
            firebaseHelper.onImprovementProgramChanged(BaseMiscUtil.isCheckedImprovementProgram(context));
        }
    }
}
