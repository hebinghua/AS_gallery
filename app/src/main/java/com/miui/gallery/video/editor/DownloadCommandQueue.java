package com.miui.gallery.video.editor;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DownloadCommandQueue {
    public List<DownloadCommand> mCommands = new ArrayList();

    public boolean put(DownloadCommand downloadCommand) {
        if (this.mCommands.contains(downloadCommand) || this.mCommands.size() >= 5) {
            DefaultLogger.d("DownloadCommandQueue", "the queue add fail.");
            return false;
        }
        return this.mCommands.add(downloadCommand);
    }

    public DownloadCommand get() {
        if (this.mCommands.size() > 0) {
            return this.mCommands.get(0);
        }
        return null;
    }

    public DownloadCommand get(int i) {
        if (!isEmpty() && i >= 0 && i <= this.mCommands.size()) {
            return this.mCommands.get(i);
        }
        return null;
    }

    public boolean remove(DownloadCommand downloadCommand) {
        return this.mCommands.remove(downloadCommand);
    }

    public int clear() {
        this.mCommands.clear();
        return 0;
    }

    public boolean isEmpty() {
        return this.mCommands.isEmpty();
    }

    public int getCapacity() {
        return this.mCommands.size();
    }
}
