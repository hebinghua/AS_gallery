package com.baidu.mapapi.map;

import android.util.Log;
import java.util.HashSet;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ae implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ int b;
    public final /* synthetic */ int c;
    public final /* synthetic */ String d;
    public final /* synthetic */ TileOverlay e;

    public ae(TileOverlay tileOverlay, int i, int i2, int i3, String str) {
        this.e = tileOverlay;
        this.a = i;
        this.b = i2;
        this.c = i3;
        this.d = str;
    }

    @Override // java.lang.Runnable
    public void run() {
        TileProvider tileProvider;
        String str;
        String str2;
        HashSet hashSet;
        tileProvider = this.e.g;
        Tile tile = ((FileTileProvider) tileProvider).getTile(this.a, this.b, this.c);
        if (tile == null) {
            str = TileOverlay.b;
            str2 = "FileTile pic is null";
        } else if (tile.width == 256 && tile.height == 256) {
            this.e.a(this.a + "_" + this.b + "_" + this.c, tile);
            hashSet = this.e.e;
            hashSet.remove(this.d);
        } else {
            str = TileOverlay.b;
            str2 = "FileTile pic must be 256 * 256";
        }
        Log.e(str, str2);
        hashSet = this.e.e;
        hashSet.remove(this.d);
    }
}
