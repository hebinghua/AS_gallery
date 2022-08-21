package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.map.MapController;

/* loaded from: classes.dex */
class n implements Runnable {
    public final /* synthetic */ MapController.b a;

    public n(MapController.b bVar) {
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (MapController.this.mListeners != null) {
            for (int i = 0; i < MapController.this.mListeners.size(); i++) {
                ak akVar = MapController.this.mListeners.get(i);
                if (akVar != null) {
                    akVar.b();
                }
            }
        }
    }
}
