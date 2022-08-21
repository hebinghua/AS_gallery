package com.miui.gallery.permission.core;

import android.content.ComponentName;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.cn.SystemCTAAgreement;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.permission.PermissionIntroductionUtils;
import com.miui.gallery.permission.cn.SystemCTAPermissionInjector;
import com.miui.gallery.permission.cn.legacy.CTAPermissionIntroduction;
import com.miui.gallery.permission.cn.legacy.CtaPermissions;
import com.miui.gallery.permission.korea.RuntimePermissionsIntroduction;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class PermissionCheckHelper {
    public PermissionCheckCallback mCallback;
    public Fragment mHost;
    public boolean mIsShowWhenLocked;
    public HashMap<String, CheckResult> mTmpResults;

    public PermissionCheckHelper(Fragment fragment, boolean z, PermissionCheckCallback permissionCheckCallback) {
        if (fragment == null || permissionCheckCallback == null) {
            throw new RuntimeException("PermissionCheckHelper: activity or callback can't be null");
        }
        this.mHost = fragment;
        this.mIsShowWhenLocked = z;
        this.mCallback = permissionCheckCallback;
        this.mTmpResults = new HashMap<>();
    }

    public void checkPermission() {
        Permission[] runtimePermissions = this.mCallback.getRuntimePermissions();
        if (runtimePermissions == null || runtimePermissions.length <= 0) {
            this.mCallback.onPermissionsChecked(runtimePermissions, new int[0], new boolean[0]);
        } else {
            showPermissionIntroduction(initResults(runtimePermissions));
        }
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 46 || strArr == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        boolean z = true;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            CheckResult checkResult = this.mTmpResults.get(strArr[i2]);
            if (checkResult != null) {
                checkResult.grantResult = iArr[i2];
                checkResult.newGranted = true;
                if (iArr[i2] != 0 && checkResult.permission.mRequired) {
                    arrayList.add(strArr[i2]);
                    z = false;
                }
                BaseGalleryPreferences.PermissionIntroduction.setCtaPrivacyPermissionsAllowed(strArr[i2], iArr[i2] == 0);
            }
        }
        if (z) {
            callbackResult();
        } else if (this.mHost.getActivity() != null) {
            dealWithDeny(arrayList, this.mIsShowWhenLocked);
        }
    }

    public final void showPermissionIntroduction(final CheckResult[] checkResultArr) {
        final FragmentActivity activity = this.mHost.getActivity();
        if (activity == null) {
            DefaultLogger.e("PermissionCheckHelper", "host has detached?");
        } else if (AgreementsUtils.isKoreaRegion()) {
            DefaultLogger.d("PermissionCheckHelper", "[Permission] local: korea");
            if (BaseGalleryPreferences.PermissionIntroduction.isRuntimePermissionsIntroduced()) {
                DefaultLogger.d("PermissionCheckHelper", "[Permission] pass through introduction");
                grantAllAndRequest(checkResultArr);
                return;
            }
            new RuntimePermissionsIntroduction().introduce(activity, null, new OnPermissionIntroduced() { // from class: com.miui.gallery.permission.core.PermissionCheckHelper.1
                @Override // com.miui.gallery.permission.core.OnPermissionIntroduced
                public void onPermissionIntroduced(boolean z) {
                    if (z) {
                        PermissionCheckHelper.this.grantAllAndRequest(checkResultArr);
                    } else {
                        activity.finish();
                    }
                }
            });
        } else if (!BaseBuildUtil.isInternational()) {
            DefaultLogger.d("PermissionCheckHelper", "[Permission] local: china");
            boolean z = true;
            boolean z2 = false;
            for (CheckResult checkResult : checkResultArr) {
                z2 = z2 || BaseGalleryPreferences.PermissionIntroduction.containCtaPrivacyPermission(checkResult.permission.mName);
                z = z && CtaPermissions.isPrivacyAllowed(checkResult.permission.mName);
            }
            if (PermissionUtils.CUSTOM_REQUEST_PERMISSION.get(activity).booleanValue() && SystemCTAAgreement.SUPPORT_SYSTEM_CTA.get(activity).booleanValue()) {
                if (z2 || BaseGalleryPreferences.CTA.hasShownNetworkingAgreements() || BaseGalleryPreferences.PermissionIntroduction.isRuntimePermissionsIntroduced() || BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    grantAllAndRequest(checkResultArr);
                    DefaultLogger.d("PermissionCheckHelper", "[Permission] pass through introduction");
                } else if (!BaseGalleryPreferences.CTA.hasUsedGalleryBefore() || (BaseGalleryPreferences.CTA.hasSystemCTAPermissionShown() && !BaseGalleryPreferences.CTA.isSystemCTAPermissionAllowed())) {
                    BaseGalleryPreferences.CTA.setSystemCTAPermissionShown(true);
                    SystemCTAPermissionInjector.getInstance(this.mIsShowWhenLocked).invoke(activity, new OnAgreementInvokedListener() { // from class: com.miui.gallery.permission.core.PermissionCheckHelper.2
                        @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                        public void onAgreementInvoked(boolean z3) {
                            BaseGalleryPreferences.CTA.setSystemCTAPermissionAllowed(z3);
                            HashMap hashMap = new HashMap();
                            hashMap.put("tip", "403.58.1.1.14897");
                            if (z3) {
                                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sure");
                                TrackController.trackClick(hashMap);
                                BaseGalleryPreferences.PermissionIntroduction.setRuntimePermissionsIntroduced(true);
                                PermissionCheckHelper.this.grantAllAndRequest(checkResultArr);
                                return;
                            }
                            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "cancel");
                            TrackController.trackClick(hashMap);
                            activity.finish();
                        }
                    });
                } else {
                    showPermissionIntroductionOld(checkResultArr, 0);
                }
            } else {
                if (z2 && z) {
                    grantAllAndRequest(checkResultArr);
                    DefaultLogger.d("PermissionCheckHelper", "[Permission] pass through introduction");
                    return;
                }
                showPermissionIntroduction(checkResultArr, 0);
            }
        } else {
            DefaultLogger.d("PermissionCheckHelper", "[Permission] local: not china and korea. pass through introduction");
            grantAllAndRequest(checkResultArr);
        }
    }

    public final void showPermissionIntroductionOld(final CheckResult[] checkResultArr, final int i) {
        final FragmentActivity activity = this.mHost.getActivity();
        if (activity == null) {
            DefaultLogger.e("PermissionCheckHelper", "host has detached?");
        } else {
            PermissionIntroductionUtils.showPermissionIntroduction(activity, checkResultArr[i].permission.mName, new OnPermissionIntroduced() { // from class: com.miui.gallery.permission.core.PermissionCheckHelper.3
                @Override // com.miui.gallery.permission.core.OnPermissionIntroduced
                public void onPermissionIntroduced(boolean z) {
                    if (z) {
                        checkResultArr[i].grantResult = 0;
                    } else {
                        CheckResult[] checkResultArr2 = checkResultArr;
                        int i2 = i;
                        if (!checkResultArr2[i2].permission.mRequired) {
                            checkResultArr2[i2].grantResult = -1;
                        } else {
                            activity.finish();
                            return;
                        }
                    }
                    int i3 = i;
                    CheckResult[] checkResultArr3 = checkResultArr;
                    if (i3 < checkResultArr3.length - 1) {
                        PermissionCheckHelper.this.showPermissionIntroductionOld(checkResultArr3, i3 + 1);
                    } else {
                        PermissionCheckHelper.this.requestPermissions(checkResultArr3);
                    }
                }
            });
        }
    }

    public final void dealWithDeny(List<String> list, boolean z) {
        FragmentActivity activity = this.mHost.getActivity();
        if (activity == null) {
            return;
        }
        if (!BaseBuildUtil.isInternational()) {
            if (!activity.getClass().getName().equals("com.miui.gallery.activity.HomePageActivity")) {
                Intent makeMainActivity = Intent.makeMainActivity(new ComponentName(activity.getPackageName(), "com.miui.gallery.activity.HomePageActivity"));
                makeMainActivity.addFlags(335544320);
                makeMainActivity.putExtra("StartActivityWhenLocked", z);
                activity.startActivity(makeMainActivity);
                activity.finish();
                return;
            }
            callbackResult();
            return;
        }
        PermissionDeniedActivity.startActivity(activity, list, z);
        activity.finish();
    }

    public final void grantAllAndRequest(CheckResult[] checkResultArr) {
        for (CheckResult checkResult : checkResultArr) {
            checkResult.grantResult = 0;
        }
        requestPermissions(checkResultArr);
    }

    public final void showPermissionIntroduction(final CheckResult[] checkResultArr, final int i) {
        final FragmentActivity activity = this.mHost.getActivity();
        if (activity == null) {
            DefaultLogger.e("PermissionCheckHelper", "host has detached?");
        } else {
            new CTAPermissionIntroduction().introduce(activity, checkResultArr[i].permission.mName, new OnPermissionIntroduced() { // from class: com.miui.gallery.permission.core.PermissionCheckHelper.4
                @Override // com.miui.gallery.permission.core.OnPermissionIntroduced
                public void onPermissionIntroduced(boolean z) {
                    if (z) {
                        checkResultArr[i].grantResult = 0;
                    } else {
                        CheckResult[] checkResultArr2 = checkResultArr;
                        int i2 = i;
                        if (!checkResultArr2[i2].permission.mRequired) {
                            checkResultArr2[i2].grantResult = -1;
                        } else {
                            activity.finish();
                            return;
                        }
                    }
                    int i3 = i;
                    CheckResult[] checkResultArr3 = checkResultArr;
                    if (i3 < checkResultArr3.length - 1) {
                        PermissionCheckHelper.this.showPermissionIntroduction(checkResultArr3, i3 + 1);
                    } else {
                        PermissionCheckHelper.this.requestPermissions(checkResultArr3);
                    }
                }
            });
        }
    }

    public final void requestPermissions(CheckResult[] checkResultArr) {
        FragmentActivity activity = this.mHost.getActivity();
        if (activity == null) {
            DefaultLogger.e("PermissionCheckHelper", "host has detached?");
            return;
        }
        for (CheckResult checkResult : checkResultArr) {
            this.mTmpResults.put(checkResult.permission.mName, checkResult);
        }
        String[] ungrantedPermissions = PermissionUtils.getUngrantedPermissions(activity, filterResults(checkResultArr, 0));
        if (ungrantedPermissions != null && ungrantedPermissions.length > 0) {
            Permission[] permissionArr = new Permission[ungrantedPermissions.length];
            for (int i = 0; i < ungrantedPermissions.length; i++) {
                permissionArr[i] = this.mTmpResults.get(ungrantedPermissions[i]).permission;
            }
            PermissionUtils.requestPermissions(this.mHost, permissionArr, 46);
            return;
        }
        DefaultLogger.d("PermissionCheckHelper", "[Permission] all permission permit by user have already granted ");
        callbackResult();
    }

    public final void callbackResult() {
        Permission[] permissionArr = new Permission[this.mTmpResults.size()];
        int[] iArr = new int[this.mTmpResults.size()];
        boolean[] zArr = new boolean[this.mTmpResults.size()];
        int i = 0;
        for (CheckResult checkResult : this.mTmpResults.values()) {
            permissionArr[i] = checkResult.permission;
            iArr[i] = checkResult.grantResult;
            zArr[i] = checkResult.newGranted;
            i++;
        }
        this.mCallback.onPermissionsChecked(permissionArr, iArr, zArr);
    }

    public final String[] filterResults(CheckResult[] checkResultArr, int i) {
        LinkedList linkedList = new LinkedList();
        for (CheckResult checkResult : checkResultArr) {
            if (checkResult.grantResult == i) {
                linkedList.add(checkResult.permission.mName);
            }
        }
        return (String[]) linkedList.toArray(new String[linkedList.size()]);
    }

    public final CheckResult[] initResults(Permission[] permissionArr) {
        CheckResult[] checkResultArr = new CheckResult[permissionArr.length];
        this.mTmpResults.clear();
        for (int i = 0; i < permissionArr.length; i++) {
            checkResultArr[i] = new CheckResult();
            checkResultArr[i].permission = permissionArr[i];
            checkResultArr[i].grantResult = -1;
            checkResultArr[i].newGranted = false;
            this.mTmpResults.put(permissionArr[i].mName, checkResultArr[i]);
        }
        return checkResultArr;
    }
}
