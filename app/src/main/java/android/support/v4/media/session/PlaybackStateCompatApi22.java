package android.support.v4.media.session;

import android.media.session.PlaybackState;
import android.os.Bundle;

/* loaded from: classes.dex */
public class PlaybackStateCompatApi22 {
    public static Bundle getExtras(Object obj) {
        return ((PlaybackState) obj).getExtras();
    }
}
