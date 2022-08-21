package androidx.lifecycle;

import java.io.Closeable;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ViewModelX.kt */
/* loaded from: classes.dex */
public final class ViewModelUtils {
    public static final <T extends Closeable> T closeOnClear(ViewModel viewModel, T closeable) {
        Intrinsics.checkNotNullParameter(viewModel, "<this>");
        Intrinsics.checkNotNullParameter(closeable, "closeable");
        CloseableContainer closeableContainer = (CloseableContainer) viewModel.getTag("com.miui.gallery.arch.CloseableContainer.TAG_KEY");
        if (closeableContainer == null) {
            closeableContainer = (CloseableContainer) viewModel.setTagIfAbsent("com.miui.gallery.arch.CloseableContainer.TAG_KEY", new CloseableContainer());
        }
        if (closeableContainer != null) {
            closeableContainer.add(closeable);
        }
        return closeable;
    }
}
