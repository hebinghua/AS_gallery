package com.miui.gallery.glide;

import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;

/* loaded from: classes2.dex */
public final class GlideApp {
    public static GlideRequests with(Context context) {
        return (GlideRequests) Glide.with(context);
    }

    public static GlideRequests with(FragmentActivity fragmentActivity) {
        return (GlideRequests) Glide.with(fragmentActivity);
    }

    public static GlideRequests with(Fragment fragment) {
        return (GlideRequests) Glide.with(fragment);
    }

    public static GlideRequests with(View view) {
        return (GlideRequests) Glide.with(view);
    }
}
