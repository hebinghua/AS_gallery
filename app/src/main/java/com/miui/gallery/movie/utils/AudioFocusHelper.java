package com.miui.gallery.movie.utils;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class AudioFocusHelper {
    public AudioFocusRequest mAudioFocusRequest;
    public AudioManager mAudioManager;

    public AudioFocusHelper(Context context) {
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    public void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= 26) {
            AudioFocusRequest build = new AudioFocusRequest.Builder(1).build();
            this.mAudioFocusRequest = build;
            build.acceptsDelayedFocusGain();
            this.mAudioManager.requestAudioFocus(this.mAudioFocusRequest);
            return;
        }
        int requestAudioFocus = this.mAudioManager.requestAudioFocus(null, 3, 1);
        if (requestAudioFocus == 1) {
            return;
        }
        DefaultLogger.w("AudioFocusHelper", "requestAudioFocus failed:%d", Integer.valueOf(requestAudioFocus));
    }

    public void abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mAudioManager.abandonAudioFocusRequest(this.mAudioFocusRequest);
            return;
        }
        int abandonAudioFocus = this.mAudioManager.abandonAudioFocus(null);
        if (abandonAudioFocus == 1) {
            return;
        }
        DefaultLogger.w("AudioFocusHelper", "abandonAudioFocus failed:%d", Integer.valueOf(abandonAudioFocus));
    }
}
