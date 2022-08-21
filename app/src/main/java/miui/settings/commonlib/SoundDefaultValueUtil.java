package miui.settings.commonlib;

import android.content.res.Resources;
import android.util.Log;

/* loaded from: classes3.dex */
public class SoundDefaultValueUtil {
    public static boolean getDeleteSoundEffectDefaultValue() {
        try {
            return Resources.getSystem().getBoolean(Resources.getSystem().getIdentifier("default_file_delete_sound_is_on", "bool", "android.miui"));
        } catch (Exception e) {
            Log.w("SoundDefaultValueUtil", "getDeleteSoundEffectDefaultValue error =  " + e.toString());
            return true;
        }
    }
}
