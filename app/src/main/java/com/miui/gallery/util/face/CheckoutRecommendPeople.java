package com.miui.gallery.util.face;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CheckResult;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.concurrent.ThreadManager;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class CheckoutRecommendPeople {
    public final Handler mHandler;
    public CheckoutStatusListener mListener;

    /* loaded from: classes2.dex */
    public interface CheckoutStatusListener {
        void onFinishCheckoutPeopleFace(int i);
    }

    public CheckoutRecommendPeople(Handler handler, CheckoutStatusListener checkoutStatusListener) {
        this.mHandler = handler;
        this.mListener = checkoutStatusListener;
    }

    public void clearListener() {
        this.mListener = null;
    }

    public void getRecommendPeopleFromNet(final String str) {
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.util.face.CheckoutRecommendPeople.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    Log.d("syncface", "getRecommendPeopleFromNet  cta not allowed");
                    return null;
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    Log.d("syncface", "getRecommendPeopleFromNet  account is null");
                    return null;
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken != null) {
                    try {
                        CheckoutRecommendPeople.this.doGetRecommendPeopleFromNet(sGetAndroidContext, xiaomiAccount, extToken, str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                Log.d("syncface", "getRecommendPeopleFromNet  token is null");
                return null;
            }
        });
    }

    public final void doGetRecommendPeopleFromNet(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str) throws Exception {
        JSONArray jSONArray;
        JSONObject fromXiaomi = CloudUtils.getFromXiaomi(HostManager.PeopleFace.getPeopleRecommendUrl(str), null, account, galleryExtendedAuthToken, 0, false);
        StringBuilder sb = new StringBuilder();
        sb.append("doGetRecommendPeopleFromNet peopleId is:");
        sb.append(str);
        sb.append("  json is:");
        sb.append(fromXiaomi == null ? "error" : fromXiaomi.toString());
        Log.d("syncface", sb.toString());
        if (CheckResult.parseErrorCode(fromXiaomi) != 0) {
            return;
        }
        JSONObject jSONObject = fromXiaomi.getJSONObject("data");
        if (!jSONObject.has("recommendPeoples") || (jSONArray = jSONObject.getJSONArray("recommendPeoples")) == null || jSONArray.length() <= 0) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("peopleServerId", str);
        contentValues.put("recommendPeoplesJson", fromXiaomi.toString());
        String[] strArr = {str};
        Cursor safeQueryPeopleRecommend = FaceDataManager.safeQueryPeopleRecommend(CloudUtils.getProjectionAll(), "peopleServerId = ?", strArr);
        if (safeQueryPeopleRecommend != null && safeQueryPeopleRecommend.getCount() > 0) {
            FaceDataManager.safeUpdatePeopleRecommend(contentValues, "peopleServerId = ?", strArr);
        } else {
            FaceDataManager.safeInsertPeopleRecommend(contentValues);
        }
        if (safeQueryPeopleRecommend != null) {
            safeQueryPeopleRecommend.close();
        }
        notifyStatus(jSONArray.length());
    }

    public final void notifyStatus(final int i) {
        CheckoutStatusListener checkoutStatusListener = this.mListener;
        if (checkoutStatusListener != null) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.post(new Runnable() { // from class: com.miui.gallery.util.face.CheckoutRecommendPeople.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CheckoutRecommendPeople.this.mListener.onFinishCheckoutPeopleFace(i);
                    }
                });
            } else {
                checkoutStatusListener.onFinishCheckoutPeopleFace(i);
            }
        }
    }
}
