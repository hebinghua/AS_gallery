package cn.kuaipan.android.kss.download;

import cn.kuaipan.android.kss.download.LoadMap;
import java.io.IOException;

/* loaded from: classes.dex */
public class LoadRecorder {
    public LoadMap map;
    public final LoadMap.Space space;

    public LoadRecorder(LoadMap loadMap, LoadMap.Space space) {
        this.map = loadMap;
        this.space = space;
    }

    public void add(int i) throws IOException {
        if (this.map == null) {
            throw new IOException("The recoder has been recycled");
        }
        this.space.remove(i);
        this.map.onSpaceRemoved(i);
    }

    public void recycle() {
        LoadMap loadMap = this.map;
        if (loadMap != null) {
            loadMap.recycleRecorder(this);
            this.map = null;
        }
    }

    public long getStart() {
        return this.space.getStart();
    }

    public long size() {
        return this.space.size();
    }

    public LoadMap.Space getSpace() {
        return this.space;
    }

    public void finalize() throws Throwable {
        recycle();
        super.finalize();
    }
}
