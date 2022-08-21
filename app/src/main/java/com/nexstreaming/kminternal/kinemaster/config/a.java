package com.nexstreaming.kminternal.kinemaster.config;

import android.content.Context;
import android.util.Log;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader;
import java.io.File;

/* compiled from: KineMasterSingleTon.java */
/* loaded from: classes3.dex */
public class a {
    private static a b;
    public Context a;
    private NexEditor c;
    private NexEditor.EditorInitException d;
    private UnsatisfiedLinkError e;
    private Object f = new Object();

    public static a a() {
        if (b == null) {
            Log.e("KineMasterSingleTon", "getApplicationInstance : Returning NULL!");
        }
        return b;
    }

    public Context b() {
        return this.a;
    }

    public a(Context context) {
        this.a = context;
        b = this;
    }

    private void e() {
        if (this.c != null) {
            return;
        }
        try {
            Log.d("KineMasterSingleTon", "Editor Instance Created");
            NexImageLoader.d dVar = new NexImageLoader.d() { // from class: com.nexstreaming.kminternal.kinemaster.config.a.1
                @Override // com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader.d
                public String a(String str) {
                    return new File(EditorGlobal.i(), str).getAbsolutePath();
                }
            };
            NexEditorDeviceProfile deviceProfile = NexEditorDeviceProfile.getDeviceProfile();
            int[] iArr = new int[7];
            iArr[0] = 2;
            int i = 1;
            iArr[1] = deviceProfile.getGLDepthBufferBits();
            iArr[2] = 1;
            if (!deviceProfile.getGLMultisample()) {
                i = 0;
            }
            iArr[3] = i;
            iArr[4] = 3;
            iArr[5] = deviceProfile.getNativeLogLevel();
            iArr[6] = 0;
            NexEditor nexEditor = new NexEditor(this.a, null, EditorGlobal.d(), EditorGlobal.c(), dVar, iArr);
            this.c = nexEditor;
            nexEditor.createProject();
        } catch (NexEditor.EditorInitException e) {
            Log.e("KineMasterSingleTon", "EditorInitException!!!");
            this.d = e;
        } catch (UnsatisfiedLinkError e2) {
            Log.e("KineMasterSingleTon", "UnsatisfiedLinkError!!!");
            this.e = e2;
        }
    }

    public NexEditor c() {
        NexEditor nexEditor;
        synchronized (this.f) {
            if (this.c == null) {
                Log.d("KineMasterSingleTon", "getEditor : creating editor instance");
                e();
            }
            if (this.c == null) {
                Log.e("KineMasterSingleTon", "getEditor : editor instance is null");
            }
            nexEditor = this.c;
        }
        return nexEditor;
    }

    public void d() {
        if (this.c != null) {
            Log.d("KineMasterSingleTon", "releaseEditor : release editor instance");
            this.c.closeProject();
            this.c.i();
            this.c = null;
        }
    }
}
