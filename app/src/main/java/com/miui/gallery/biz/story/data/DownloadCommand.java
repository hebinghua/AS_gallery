package com.miui.gallery.biz.story.data;

import com.miui.gallery.picker.uri.Downloader;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DownloadCommand.kt */
/* loaded from: classes.dex */
public final class DownloadCommand {
    public final boolean retry;
    public final List<Downloader.DownloadTask> tasks;

    /* JADX WARN: Multi-variable type inference failed */
    public DownloadCommand(boolean z, List<? extends Downloader.DownloadTask> tasks) {
        Intrinsics.checkNotNullParameter(tasks, "tasks");
        this.retry = z;
        this.tasks = tasks;
    }

    public final boolean getRetry() {
        return this.retry;
    }

    public final List<Downloader.DownloadTask> getTasks() {
        return this.tasks;
    }
}
