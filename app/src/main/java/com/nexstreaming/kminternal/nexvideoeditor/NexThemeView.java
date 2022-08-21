package com.nexstreaming.kminternal.nexvideoeditor;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import ch.qos.logback.core.joran.action.ActionConst;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* loaded from: classes3.dex */
public class NexThemeView extends TextureView implements TextureView.SurfaceTextureListener {
    private static String LOG_TAG = "VideoEditor_ThemeView";
    public static final int kEventType_Available = 1;
    public static final int kEventType_Destroyed = 3;
    public static final int kEventType_SizeChanged = 2;
    private static float mAspectRatio = 1.7777778f;
    private Surface actualSurface;
    private SurfaceTexture actualSurfaceTexture;
    private boolean isAvailable;
    private boolean mBlackOut;
    private Queue<Object> mClearListenerList;
    private int mCurrentTime;
    private NexEditor mEditor;
    private boolean mHadNativeRenderSinceClear;
    private Handler mHandler;
    private RenderType mRenderType;
    private a m_captureListener;
    private int m_height;
    private int m_width;
    private b nexThemeViewCallback;
    private List<Runnable> postOnPrepareSurfaceRunnables;

    /* loaded from: classes3.dex */
    public enum RenderType {
        None,
        Native,
        Clear
    }

    /* loaded from: classes3.dex */
    public static abstract class a {
    }

    /* loaded from: classes3.dex */
    public interface b {
        void onEventNotify(int i, Object obj, int i2, int i3, int i4);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public void setNotify(b bVar) {
        this.nexThemeViewCallback = bVar;
    }

    private void updateActualSurface(SurfaceTexture surfaceTexture) {
        if (surfaceTexture != this.actualSurfaceTexture) {
            this.actualSurfaceTexture = surfaceTexture;
            if (surfaceTexture != null) {
                this.actualSurface = new Surface(this.actualSurfaceTexture);
            } else {
                this.actualSurface = null;
            }
        }
    }

    private String diagString() {
        return "[0x" + Integer.toHexString(System.identityHashCode(this)) + " " + getWidth() + "x" + getHeight() + "] ";
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        updateActualSurface(surfaceTexture);
        this.m_width = i;
        this.m_height = i2;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSurfaceTextureAvailable ");
        sb.append(diagString());
        sb.append(this.mEditor == null ? "(editor is null)" : "(editor is SET)");
        Log.d(str, sb.toString());
        NexEditor nexEditor = this.mEditor;
        if (nexEditor != null) {
            nexEditor.a(this.actualSurface);
            this.mEditor.w();
            for (Runnable runnable : this.postOnPrepareSurfaceRunnables) {
                post(runnable);
            }
            this.postOnPrepareSurfaceRunnables.clear();
        }
        b bVar = this.nexThemeViewCallback;
        if (bVar != null) {
            bVar.onEventNotify(1, null, i, i2, 0);
        }
        this.isAvailable = true;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        updateActualSurface(surfaceTexture);
        this.m_width = i;
        this.m_height = i2;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSurfaceTextureSizeChanged ");
        sb.append(diagString());
        sb.append(this.mEditor == null ? "(editor is null)" : "(editor is SET)");
        Log.d(str, sb.toString());
        NexEditor nexEditor = this.mEditor;
        if (nexEditor != null) {
            nexEditor.a(this.actualSurface);
            this.mEditor.w();
        }
        b bVar = this.nexThemeViewCallback;
        if (bVar != null) {
            bVar.onEventNotify(2, null, i, i2, 0);
        }
    }

    public void postOnPrepareSurface(Runnable runnable) {
        this.postOnPrepareSurfaceRunnables.add(runnable);
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onSurfaceTextureDestroyed ");
        sb.append(diagString());
        sb.append(this.mEditor == null ? "(editor is null)" : "(editor is SET)");
        Log.d(str, sb.toString());
        NexEditor nexEditor = this.mEditor;
        if (nexEditor != null) {
            nexEditor.a((Surface) null);
        }
        this.actualSurfaceTexture = null;
        this.actualSurface = null;
        b bVar = this.nexThemeViewCallback;
        if (bVar != null) {
            bVar.onEventNotify(3, null, 0, 0, 0);
        }
        this.isAvailable = false;
        return true;
    }

    public void capture(a aVar) {
        this.m_captureListener = aVar;
    }

    public void setBlackOut(boolean z) {
        this.mBlackOut = z;
    }

    public NexThemeView(Context context) {
        super(context);
        this.mEditor = null;
        this.mRenderType = RenderType.None;
        this.mHadNativeRenderSinceClear = false;
        this.mClearListenerList = new LinkedList();
        this.mHandler = new Handler();
        this.mBlackOut = false;
        this.isAvailable = false;
        this.m_width = 0;
        this.m_height = 0;
        this.m_captureListener = null;
        this.postOnPrepareSurfaceRunnables = new ArrayList();
        init(false, 0, 1);
    }

    public NexThemeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEditor = null;
        this.mRenderType = RenderType.None;
        this.mHadNativeRenderSinceClear = false;
        this.mClearListenerList = new LinkedList();
        this.mHandler = new Handler();
        this.mBlackOut = false;
        this.isAvailable = false;
        this.m_width = 0;
        this.m_height = 0;
        this.m_captureListener = null;
        this.postOnPrepareSurfaceRunnables = new ArrayList();
        init(false, 0, 0);
    }

    public NexThemeView(Context context, boolean z, int i, int i2) {
        super(context);
        this.mEditor = null;
        this.mRenderType = RenderType.None;
        this.mHadNativeRenderSinceClear = false;
        this.mClearListenerList = new LinkedList();
        this.mHandler = new Handler();
        this.mBlackOut = false;
        this.isAvailable = false;
        this.m_width = 0;
        this.m_height = 0;
        this.m_captureListener = null;
        this.postOnPrepareSurfaceRunnables = new ArrayList();
        init(z, i, i2);
    }

    public void linkToEditor(NexEditor nexEditor) {
        Surface surface;
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("linkToEditor ");
        sb.append(diagString());
        sb.append(" editor=");
        sb.append(nexEditor == null ? ActionConst.NULL : "not-null");
        Log.d(str, sb.toString());
        this.mEditor = nexEditor;
        if (nexEditor == null || (surface = this.actualSurface) == null) {
            return;
        }
        nexEditor.a(surface);
    }

    public static void setAspectRatio(float f) {
        mAspectRatio = f;
    }

    public static float getAspectRatio() {
        return mAspectRatio;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (mode != 1073741824 || mode2 != 1073741824) {
            if (mode == 1073741824) {
                i3 = (int) Math.min(size2, size / mAspectRatio);
                i4 = (int) (i3 * mAspectRatio);
            } else if (mode2 == 1073741824) {
                i4 = (int) Math.min(size, size2 * mAspectRatio);
                i3 = (int) (i4 / mAspectRatio);
            } else {
                float f = size;
                float f2 = size2;
                float f3 = mAspectRatio;
                if (f > f2 * f3) {
                    i4 = (int) (f2 * f3);
                } else {
                    i3 = (int) (f / f3);
                    i4 = size;
                }
            }
            setMeasuredDimension(i4, i3);
            Log.d(LOG_TAG, String.format("onMeasure(%X %X %d %d %f)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(size), Integer.valueOf(size2), Float.valueOf(mAspectRatio)));
        }
        i4 = size;
        i3 = size2;
        setMeasuredDimension(i4, i3);
        Log.d(LOG_TAG, String.format("onMeasure(%X %X %d %d %f)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(size), Integer.valueOf(size2), Float.valueOf(mAspectRatio)));
    }

    public boolean isSurfaceAvailable() {
        return this.isAvailable;
    }

    private void init(boolean z, int i, int i2) {
        setSurfaceTextureListener(this);
    }

    public void requestDraw(int i) {
        String str = LOG_TAG;
        Log.e(str, "requestDraw(" + i + ")");
        this.mRenderType = RenderType.Native;
        this.mCurrentTime = i;
    }
}
