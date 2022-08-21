package androidx.constraintlayout.motion.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import ch.qos.logback.classic.spi.CallerData;

@SuppressLint({"LogConditional"})
/* loaded from: classes.dex */
public class Debug {
    public static String getName(View view) {
        try {
            return view.getContext().getResources().getResourceEntryName(view.getId());
        } catch (Exception unused) {
            return "UNKNOWN";
        }
    }

    public static String getName(Context context, int id) {
        if (id != -1) {
            try {
                return context.getResources().getResourceEntryName(id);
            } catch (Exception unused) {
                StringBuilder sb = new StringBuilder(12);
                sb.append(CallerData.NA);
                sb.append(id);
                return sb.toString();
            }
        }
        return "UNKNOWN";
    }
}
