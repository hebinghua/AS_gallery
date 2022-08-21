package com.miui.gallery.util.glide;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.joran.action.Action;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collection;
import java.util.Map;

/* loaded from: classes2.dex */
public class GlideRequestManagerHelper {
    public static final ArrayMap<View, Fragment> tempViewToSupportFragment = new ArrayMap<>();
    public static final ArrayMap<View, android.app.Fragment> tempViewToFragment = new ArrayMap<>();
    public static final Bundle tempBundle = new Bundle();

    public static RequestManager safeGet(View view) {
        if (Util.isOnBackgroundThread()) {
            return Glide.with(view.getContext().getApplicationContext());
        }
        Preconditions.checkNotNull(view);
        Context context = view.getContext();
        if (context == null) {
            DefaultLogger.w("RequestManagerHelper", "Unable to obtain a request manager for a view without a Context");
            return null;
        }
        Activity findActivity = findActivity(context);
        if (findActivity == null) {
            return Glide.with(context.getApplicationContext());
        }
        if (findActivity.isDestroyed()) {
            DefaultLogger.i("RequestManagerHelper", "Unable to obtain a request manager while activity is destroyed");
            return null;
        } else if (findActivity instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) findActivity;
            Fragment findSupportFragment = findSupportFragment(view, fragmentActivity);
            if (findSupportFragment == null) {
                return Glide.with(fragmentActivity);
            }
            if (findSupportFragment.getActivity() == null || findSupportFragment.getActivity().isDestroyed()) {
                DefaultLogger.i("RequestManagerHelper", "Unable to obtain a request manager before it is attached");
                return null;
            }
            return Glide.with(findSupportFragment);
        } else {
            android.app.Fragment findFragment = findFragment(view, findActivity);
            if (findFragment == null) {
                return Glide.with(findActivity);
            }
            if (findFragment.getActivity() == null || findFragment.getActivity().isDestroyed()) {
                DefaultLogger.i("RequestManagerHelper", "Unable to obtain a request manager while fragment is detached");
                return null;
            }
            return Glide.with(findFragment);
        }
    }

    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (!(context instanceof ContextWrapper)) {
            return null;
        }
        return findActivity(((ContextWrapper) context).getBaseContext());
    }

    public static Fragment findSupportFragment(View view, FragmentActivity fragmentActivity) {
        ArrayMap<View, Fragment> arrayMap = tempViewToSupportFragment;
        arrayMap.clear();
        findAllSupportFragmentsWithViews(fragmentActivity.getSupportFragmentManager().getFragments(), arrayMap);
        View findViewById = fragmentActivity.findViewById(16908290);
        Fragment fragment = null;
        while (!view.equals(findViewById) && (fragment = tempViewToSupportFragment.get(view)) == null && (view.getParent() instanceof View)) {
            view = (View) view.getParent();
        }
        tempViewToSupportFragment.clear();
        return fragment;
    }

    public static void findAllSupportFragmentsWithViews(Collection<Fragment> collection, Map<View, Fragment> map) {
        if (collection == null) {
            return;
        }
        for (Fragment fragment : collection) {
            if (fragment != null && fragment.getView() != null) {
                map.put(fragment.getView(), fragment);
                findAllSupportFragmentsWithViews(fragment.getChildFragmentManager().getFragments(), map);
            }
        }
    }

    public static android.app.Fragment findFragment(View view, Activity activity) {
        ArrayMap<View, android.app.Fragment> arrayMap = tempViewToFragment;
        arrayMap.clear();
        findAllFragmentsWithViews(activity.getFragmentManager(), arrayMap);
        View findViewById = activity.findViewById(16908290);
        android.app.Fragment fragment = null;
        while (!view.equals(findViewById) && (fragment = tempViewToFragment.get(view)) == null && (view.getParent() instanceof View)) {
            view = (View) view.getParent();
        }
        tempViewToFragment.clear();
        return fragment;
    }

    @TargetApi(26)
    public static void findAllFragmentsWithViews(FragmentManager fragmentManager, ArrayMap<View, android.app.Fragment> arrayMap) {
        if (Build.VERSION.SDK_INT >= 26) {
            for (android.app.Fragment fragment : fragmentManager.getFragments()) {
                if (fragment.getView() != null) {
                    arrayMap.put(fragment.getView(), fragment);
                    findAllFragmentsWithViews(fragment.getChildFragmentManager(), arrayMap);
                }
            }
            return;
        }
        findAllFragmentsWithViewsPreO(fragmentManager, arrayMap);
    }

    public static void findAllFragmentsWithViewsPreO(FragmentManager fragmentManager, ArrayMap<View, android.app.Fragment> arrayMap) {
        int i = 0;
        while (true) {
            Bundle bundle = tempBundle;
            int i2 = i + 1;
            bundle.putInt(Action.KEY_ATTRIBUTE, i);
            android.app.Fragment fragment = null;
            try {
                fragment = fragmentManager.getFragment(bundle, Action.KEY_ATTRIBUTE);
            } catch (Exception unused) {
            }
            if (fragment == null) {
                return;
            }
            if (fragment.getView() != null) {
                arrayMap.put(fragment.getView(), fragment);
                findAllFragmentsWithViews(fragment.getChildFragmentManager(), arrayMap);
            }
            i = i2;
        }
    }
}
