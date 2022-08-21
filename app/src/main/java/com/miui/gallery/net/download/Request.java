package com.miui.gallery.net.download;

import android.net.Uri;
import android.text.TextUtils;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public final class Request {
    public boolean mAllowedOverMetered;
    public File mDestination;
    public int mHashCode;
    public volatile Listener mListener;
    public int mMaxRetryTimes = 3;
    public Uri mUri;
    public Verifier mVerifier;

    /* loaded from: classes2.dex */
    public interface Listener {
        void onComplete(int i);

        void onProgressUpdate(int i);

        void onStart();
    }

    public Request(Uri uri, File file) {
        this.mUri = uri;
        String scheme = uri.getScheme();
        if (!TextUtils.equals(scheme, "http") && !TextUtils.equals(scheme, "https")) {
            throw new IllegalArgumentException("not support scheme: " + scheme);
        }
        this.mDestination = file;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public File getDestination() {
        return this.mDestination;
    }

    public void setAllowedOverMetered(boolean z) {
        this.mAllowedOverMetered = z;
    }

    public Verifier getVerifier() {
        return this.mVerifier;
    }

    public void setVerifier(Verifier verifier) {
        this.mVerifier = verifier;
    }

    public int getMaxRetryTimes() {
        return this.mMaxRetryTimes;
    }

    public Listener getListener() {
        return this.mListener;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public int getNetworkType() {
        return !this.mAllowedOverMetered ? 1 : 0;
    }

    public String toString() {
        return String.format(Locale.US, "uri: %s, file: %s", this.mUri, this.mDestination);
    }

    public int hashCode() {
        if (this.mHashCode == 0) {
            this.mHashCode = String.format(Locale.US, "uri: %s, file: %s", this.mUri, this.mDestination).hashCode();
        }
        return this.mHashCode;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Request)) {
            return false;
        }
        Request request = (Request) obj;
        return request.mUri.equals(this.mUri) && request.mDestination.equals(this.mDestination);
    }
}
