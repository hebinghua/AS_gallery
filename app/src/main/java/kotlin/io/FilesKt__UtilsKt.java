package kotlin.io;

import java.io.File;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Utils.kt */
/* loaded from: classes3.dex */
public class FilesKt__UtilsKt extends FilesKt__FileTreeWalkKt {
    public static final boolean deleteRecursively(File file) {
        Intrinsics.checkNotNullParameter(file, "<this>");
        while (true) {
            boolean z = true;
            for (File file2 : FilesKt__FileTreeWalkKt.walkBottomUp(file)) {
                if (file2.delete() || !file2.exists()) {
                    if (z) {
                        break;
                    }
                }
                z = false;
            }
            return z;
        }
    }
}
