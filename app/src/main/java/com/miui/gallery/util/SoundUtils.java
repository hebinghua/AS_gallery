package com.miui.gallery.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import miui.settings.commonlib.SoundDefaultValueUtil;

/* loaded from: classes2.dex */
public class SoundUtils {
    public static final LazyValue<Void, Boolean> DELETE_SOUND_EFFECT_DEFAULT_VALUE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.SoundUtils.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r1) {
            return Boolean.valueOf(SoundDefaultValueUtil.getDeleteSoundEffectDefaultValue());
        }
    };

    public static void playSoundForOperation(Context context, int i) {
        if (ThreadManager.isMainThread()) {
            ThreadManager.getMiscPool().submit(new SoundJob(context, i));
        } else {
            internalPlaySoundForOperation(context, i);
        }
    }

    public static void internalPlaySoundForOperation(Context context, int i) {
        if (!shouldPlaySoundForOperation(context, i)) {
            DefaultLogger.d("SoundUtils", "Shouldn't play sound for operation %s, just return", Integer.valueOf(i));
            return;
        }
        Uri ringtoneUriForOperation = getRingtoneUriForOperation(i);
        if (ringtoneUriForOperation == null) {
            DefaultLogger.e("SoundUtils", "No sound resource found for operation %s", Integer.valueOf(i));
            return;
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUriForOperation);
        if (ringtone == null) {
            DefaultLogger.e("SoundUtils", "Get ringtone failed for operation %s, sound source %s", Integer.valueOf(i), ringtoneUriForOperation);
            return;
        }
        setRingtoneStream(ringtone);
        ringtone.play();
    }

    public static boolean shouldPlaySoundForOperation(Context context, int i) {
        if (i == 0) {
            try {
                int i2 = Settings.System.getInt(context.getContentResolver(), "delete_sound_effect", DELETE_SOUND_EFFECT_DEFAULT_VALUE.get(null).booleanValue() ? 1 : 0);
                DefaultLogger.d("SoundUtils", "Got sound setting value %d", Integer.valueOf(i2));
                return i2 > 0;
            } catch (Exception e) {
                DefaultLogger.w("SoundUtils", e);
            }
        }
        return true;
    }

    public static Uri getRingtoneUriForOperation(int i) {
        String str = i != 0 ? null : "/system/media/audio/ui/Delete.ogg";
        if (!TextUtils.isEmpty(str)) {
            File file = new File(str);
            if (file.exists()) {
                return Uri.fromFile(file);
            }
            DefaultLogger.w("SoundUtils", "Sound file %s do not exist", file);
        }
        return null;
    }

    public static void setRingtoneStream(Ringtone ringtone) {
        if (Build.VERSION.SDK_INT >= 21) {
            ringtone.setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(1).build());
        } else {
            ringtone.setStreamType(1);
        }
    }

    /* loaded from: classes2.dex */
    public static class SoundJob implements ThreadPool.Job<Void> {
        public WeakReference<Context> mContextWeakRef;
        public int mOperationType;

        public SoundJob(Context context, int i) {
            this.mContextWeakRef = new WeakReference<>(context);
            this.mOperationType = i;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            Context context = this.mContextWeakRef.get();
            if (context != null) {
                SoundUtils.internalPlaySoundForOperation(context.getApplicationContext(), this.mOperationType);
                return null;
            }
            return null;
        }
    }
}
