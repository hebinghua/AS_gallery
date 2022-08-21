package com.miui.gallery.editor.photo.widgets.glview;

import android.opengl.GLSurfaceView;
import java.util.LinkedList;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public abstract class AbstractRender implements GLSurfaceView.Renderer {
    public final LinkedList<Runnable> mRunOnDraw = new LinkedList<>();

    public void runPendingOnDrawTasks() {
        synchronized (this.mRunOnDraw) {
            while (!this.mRunOnDraw.isEmpty()) {
                this.mRunOnDraw.removeFirst().run();
            }
        }
    }

    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        runPendingOnDrawTasks();
    }
}
