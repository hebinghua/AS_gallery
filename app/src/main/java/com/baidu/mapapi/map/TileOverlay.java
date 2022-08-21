package com.baidu.mapapi.map;

import android.util.Log;
import com.baidu.mapapi.common.Logger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/* loaded from: classes.dex */
public final class TileOverlay {
    private static final String b = "TileOverlay";
    private static int f;
    public BaiduMap a;
    private TileProvider g;
    private HashMap<String, Tile> d = new HashMap<>();
    private HashSet<String> e = new HashSet<>();
    private ExecutorService c = Executors.newFixedThreadPool(1);

    public TileOverlay(BaiduMap baiduMap, TileProvider tileProvider) {
        this.a = baiduMap;
        this.g = tileProvider;
    }

    private synchronized Tile a(String str) {
        if (this.d.containsKey(str)) {
            Tile tile = this.d.get(str);
            this.d.remove(str);
            return tile;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(String str, Tile tile) {
        this.d.put(str, tile);
    }

    private synchronized boolean b(String str) {
        return this.e.contains(str);
    }

    private synchronized void c(String str) {
        this.e.add(str);
    }

    public Tile a(int i, int i2, int i3) {
        String str;
        String str2;
        String str3 = i + "_" + i2 + "_" + i3;
        Tile a = a(str3);
        if (a != null) {
            return a;
        }
        BaiduMap baiduMap = this.a;
        if (baiduMap != null && f == 0) {
            WinRound winRound = baiduMap.getMapStatus().a.j;
            f = (((winRound.right - winRound.left) / 256) + 2) * (((winRound.bottom - winRound.top) / 256) + 2);
        }
        if (this.d.size() > f) {
            a();
        }
        if (b(str3) || this.c.isShutdown()) {
            return null;
        }
        try {
            c(str3);
            this.c.execute(new ae(this, i, i2, i3, str3));
            return null;
        } catch (RejectedExecutionException unused) {
            str = b;
            str2 = "ThreadPool excepiton";
            Log.e(str, str2);
            return null;
        } catch (Exception unused2) {
            str = b;
            str2 = "fileDir is not legal";
            Log.e(str, str2);
            return null;
        }
    }

    public synchronized void a() {
        Logger.logE(b, "clearTaskSet");
        this.e.clear();
        this.d.clear();
    }

    public void b() {
        this.c.shutdownNow();
    }

    public boolean clearTileCache() {
        BaiduMap baiduMap = this.a;
        if (baiduMap == null) {
            return false;
        }
        return baiduMap.b();
    }

    public void removeTileOverlay() {
        BaiduMap baiduMap = this.a;
        if (baiduMap == null) {
            return;
        }
        baiduMap.a(this);
    }
}
