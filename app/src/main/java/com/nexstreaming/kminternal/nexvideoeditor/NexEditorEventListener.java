package com.nexstreaming.kminternal.nexvideoeditor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.editorwrapper.b;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.kminternal.nexvideoeditor.NexImageLoader;
import com.xiaomi.stat.b.h;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class NexEditorEventListener {
    private static Bitmap n = null;
    private static String o = "";
    private NexEditor a;
    private Handler c;
    private AudioManager g;
    private NexEditor.c h;
    public NexImageLoader mImage;
    private c b = null;
    public AudioTrack mAudioTrack = null;
    private NexEditor.PlayState d = null;
    private int e = 0;
    private boolean f = false;
    private LayerRenderer i = new LayerRenderer();
    private boolean j = false;
    private boolean k = true;
    private int l = 0;
    private ArrayList<a> m = new ArrayList<>();

    public void callbackReleaseImage() {
    }

    public void setWatermark(boolean z) {
        a();
        this.k = z;
    }

    public void setSyncMode(boolean z) {
        this.j = z;
    }

    public synchronized void ignoreEventsUntilTag(int i) {
        if (!this.f) {
            this.f = true;
            this.e = i;
        } else if (this.e < i) {
            this.e = i;
        }
    }

    private synchronized void a(int i) {
        if (this.f && i >= this.e) {
            this.f = false;
        }
    }

    public NexEditorEventListener(NexEditor nexEditor, Context context, com.nexstreaming.kminternal.nexvideoeditor.a aVar, NexImageLoader.d dVar) {
        Resources resources = null;
        this.a = null;
        this.mImage = null;
        this.c = null;
        if (context != null) {
            this.g = (AudioManager) context.getApplicationContext().getSystemService("audio");
        } else {
            this.g = null;
        }
        this.a = nexEditor;
        this.mImage = new NexImageLoader(context != null ? context.getResources() : resources, aVar, dVar, nexEditor.f(), nexEditor.g(), nexEditor.h());
        this.c = new Handler(Looper.getMainLooper());
    }

    public void setContext(Context context) {
        this.mImage.setResources(context == null ? null : context.getApplicationContext().getResources());
        if (context != null) {
            this.g = (AudioManager) context.getApplicationContext().getSystemService("audio");
        } else {
            this.g = null;
        }
    }

    public void setUIListener(c cVar) {
        this.b = cVar;
        NexEditor.PlayState playState = this.d;
        if (playState != null) {
            cVar.a(NexEditor.PlayState.NONE, playState);
        }
    }

    public int callbackCapture(final int i, final int i2, final int i3, final byte[] bArr) {
        this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.1
            @Override // java.lang.Runnable
            public void run() {
                if (NexEditorEventListener.this.a != null) {
                    NexEditorEventListener.this.a.a(i, i2, i3, bArr, true);
                }
            }
        });
        return 0;
    }

    public int callbackThumb(final int i, final int i2, final int i3, final int i4, final int i5, final int i6, final int i7, final int i8, final byte[] bArr) {
        Log.d("NexEditorEventHandler", "callbackThumb start iMode=" + i + ", iTime=" + i3 + ", iWidth=" + i4 + ", iHeight=" + i5 + ", iSize=" + i8 + ", tag=" + i2);
        this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.12
            @Override // java.lang.Runnable
            public void run() {
                if (NexEditorEventListener.this.a != null) {
                    NexEditorEventListener.this.a.a(i, i3, i4, i5, i8, bArr, i6, i7, i2);
                }
            }
        });
        Log.d("NexEditorEventHandler", "salabara callbackThumb end");
        return 0;
    }

    public int callbackHighLightIndex(final int i, final int[] iArr) {
        Log.d("NexEditorEventHandler", "callbackHighLightIndex start iCount=" + i);
        this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.23
            @Override // java.lang.Runnable
            public void run() {
                if (NexEditorEventListener.this.a != null) {
                    NexEditorEventListener.this.a.a(i, iArr);
                }
            }
        });
        Log.d("NexEditorEventHandler", "callbackHighLightIndex end");
        return 0;
    }

    public int callbackHighLightIndexForVAS(final int i, final int[] iArr, final int[] iArr2) {
        Log.d("NexEditorEventHandler", "callbackHighLightIndexForVAS start iCount=" + i);
        this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.30
            @Override // java.lang.Runnable
            public void run() {
                if (NexEditorEventListener.this.a != null) {
                    NexEditorEventListener.this.a.a(i, iArr, iArr2);
                }
            }
        });
        Log.d("NexEditorEventHandler", "callbackHighLightIndexForVAS end");
        return 0;
    }

    /* loaded from: classes3.dex */
    public class a extends AsyncTask<String, Void, NexImage> {
        private String b;

        private a() {
        }

        public void a(String str) {
            this.b = str;
        }

        @Override // android.os.AsyncTask
        /* renamed from: a */
        public NexImage doInBackground(String... strArr) {
            NexImage openThemeImage = NexEditorEventListener.this.mImage.openThemeImage(strArr[0]);
            if (openThemeImage == null) {
                return openThemeImage;
            }
            int width = openThemeImage.getWidth();
            int height = openThemeImage.getHeight();
            int loadedType = openThemeImage.getLoadedType();
            int[] iArr = new int[width * height];
            openThemeImage.getPixels(iArr);
            if (NexEditorEventListener.this.a != null) {
                NexEditorEventListener.this.a.a(strArr[0], iArr, width, height, loadedType);
            }
            return openThemeImage;
        }

        @Override // android.os.AsyncTask
        /* renamed from: a */
        public void onPostExecute(NexImage nexImage) {
            NexEditorEventListener.this.m.remove(this);
            Log.d("NexEditorEventHandler", String.format("Done:image thread queue length:%d", Integer.valueOf(NexEditorEventListener.this.m.size())));
        }

        @Override // android.os.AsyncTask
        /* renamed from: b */
        public void onCancelled(NexImage nexImage) {
            if (NexEditorEventListener.this.a == null || this.b == null) {
                return;
            }
            NexEditorEventListener.this.a.a(this.b);
        }
    }

    public NexImage callbackGetThemeImage(String str, int i) {
        if (i == 1 || i == 3) {
            if (i == 3) {
                Iterator<a> it = this.m.iterator();
                while (it.hasNext()) {
                    it.next().cancel(false);
                }
                this.m.clear();
                Log.d("NexEditorEventHandler", String.format("CLEAR:image thread queue length:%d", Integer.valueOf(this.m.size())));
            }
            a aVar = new a();
            aVar.a(str);
            this.m.add(aVar);
            Log.d("NexEditorEventHandler", String.format("NEW:image thread queue length:%d asyncmode:%d", Integer.valueOf(this.m.size()), Integer.valueOf(i)));
            aVar.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, str);
            return null;
        }
        return this.mImage.openThemeImage(str);
    }

    public int callbackCheckImageWorkDone() {
        return this.m.size();
    }

    public byte[] callbackGetThemeFile(String str) {
        String str2;
        if (str.contains(".force_effect/")) {
            str = str.replace(".force_effect/", h.g);
        }
        int indexOf = str.indexOf(47);
        if (indexOf >= 0) {
            String substring = str.substring(0, indexOf);
            str2 = str.substring(indexOf + 1);
            str = substring;
        } else {
            str2 = "";
        }
        return this.mImage.callbackReadAssetItemFile(str, str2);
    }

    public NexImage callbackGetThemeImageUsingResource(String str) {
        return this.mImage.openFile(str, 0);
    }

    public NexImage callbackGetImageUsingFile(String str, int i) {
        return this.mImage.openFile(str, i);
    }

    public NexImage callbackGetImageUsingText(String str) {
        return this.mImage.openFile(str, 0);
    }

    public AudioTrack callbackGetAudioTrack(int i, int i2) {
        Log.d("NexEditorEventHandler", String.format("callbackGetAudioTrack(SampleRate(%d) Channel(%d)", Integer.valueOf(i), Integer.valueOf(i2)));
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null) {
            audioTrack.release();
        }
        int i3 = i2 != 1 ? 3 : 2;
        AudioTrack audioTrack2 = new AudioTrack(3, i, i3, 2, AudioTrack.getMinBufferSize(i, i3, 2), 1);
        this.mAudioTrack = audioTrack2;
        return audioTrack2;
    }

    public void callbackReleaseAudioTrack() {
        Log.d("NexEditorEventHandler", String.format("callbackReleaseAudioTrack", new Object[0]));
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null) {
            audioTrack.release();
            this.mAudioTrack = null;
        }
    }

    public AudioManager callbackGetAudioManager() {
        Log.d("NexEditorEventHandler", "callbackGetAudioManager " + this.g);
        return this.g;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(NexEditor.PlayState playState, NexEditor.PlayState playState2) {
        if (this.d != playState2) {
            this.d = playState2;
            c cVar = this.b;
            if (cVar != null) {
                cVar.a(playState, playState2);
            }
            if (playState2 == NexEditor.PlayState.IDLE) {
                this.a.l();
            }
            if (playState == null || playState == NexEditor.PlayState.NONE) {
                return;
            }
            int i = AnonymousClass29.a[playState2.ordinal()];
            if (i == 1 || i == 2) {
                this.a.b(NexEditor.ErrorCode.NONE);
            } else {
                this.a.c(NexEditor.ErrorCode.NONE);
            }
        }
    }

    /* renamed from: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener$29  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass29 {
        public static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[NexEditor.PlayState.values().length];
            a = iArr;
            try {
                iArr[NexEditor.PlayState.RUN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[NexEditor.PlayState.RECORD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public int notifyEvent(int i, final int i2, final int i3, final int i4, final int i5) {
        if (i == 18) {
            Log.d("NexEditorEventHandler", "REACHED MARKER " + i2);
            a(i2);
            this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.31
                @Override // java.lang.Runnable
                public void run() {
                    if (NexEditorEventListener.this.a != null) {
                        NexEditorEventListener.this.a.b(NexEditor.ErrorCode.NONE, i2);
                    }
                }
            });
        }
        if (this.f) {
            Log.d("NexEditorEventHandler", "IGNORING EVENT iEventType=" + i + " (awaiting tag " + this.e + ")");
            return 0;
        }
        switch (i) {
            case 0:
                if (this.j) {
                    NexEditor.PlayState fromValue = NexEditor.PlayState.fromValue(i2);
                    NexEditor.PlayState fromValue2 = NexEditor.PlayState.fromValue(i3);
                    if (fromValue2 == NexEditor.PlayState.RESUME) {
                        fromValue2 = NexEditor.PlayState.RECORD;
                    }
                    a(fromValue, fromValue2);
                    break;
                } else {
                    this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.32
                        @Override // java.lang.Runnable
                        public void run() {
                            NexEditor.PlayState fromValue3 = NexEditor.PlayState.fromValue(i2);
                            NexEditor.PlayState fromValue4 = NexEditor.PlayState.fromValue(i3);
                            if (fromValue4 == NexEditor.PlayState.RESUME) {
                                fromValue4 = NexEditor.PlayState.RECORD;
                            }
                            NexEditorEventListener.this.a(fromValue3, fromValue4);
                        }
                    });
                    break;
                }
            case 1:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.33
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a == null || !NexEditorEventListener.this.a.e(i2)) {
                            if (NexEditorEventListener.this.b != null) {
                                NexEditorEventListener.this.b.a(i2);
                            }
                            if (NexEditorEventListener.this.a == null) {
                                return;
                            }
                            NexEditorEventListener.this.a.k(i2);
                        }
                    }
                });
                break;
            case 2:
            case 3:
            case 4:
            case 9:
            case 12:
            case 15:
            case 16:
            case 18:
            case 25:
            case 29:
            case 34:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            default:
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] not implement event(%d) Param(%d %d %d %d) ", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5)));
                break;
            case 5:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.34
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.a(i2, i3, i4, i5);
                        }
                    }
                });
                break;
            case 6:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.35
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.a(i2, i3);
                        }
                    }
                });
                break;
            case 7:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.f(i2);
                        }
                    }
                });
                break;
            case 8:
                break;
            case 10:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.5
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            if (i3 == 1) {
                                NexEditorEventListener.this.a.v();
                            } else {
                                NexEditorEventListener.this.a.d(i4, i5);
                            }
                        }
                        if (NexEditorEventListener.this.b != null) {
                            if (i3 == 1) {
                                NexEditorEventListener.this.b.a();
                            } else if (i2 != 0) {
                                NexEditorEventListener.this.b.a(NexEditor.ErrorCode.fromValue(i2));
                            } else {
                                NexEditorEventListener.this.b.b(i4);
                            }
                        }
                    }
                });
                break;
            case 11:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.7
                    @Override // java.lang.Runnable
                    public void run() {
                        NexEditorEventListener.this.a.s();
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.b(NexEditor.ErrorCode.fromValue(i2));
                        }
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.a(NexEditor.ErrorCode.fromValue(i2), i3);
                        }
                    }
                });
                break;
            case 13:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a == null || !NexEditorEventListener.this.a.o()) {
                            if (NexEditorEventListener.this.b != null) {
                                NexEditorEventListener.this.b.b(NexEditor.ErrorCode.NONE);
                            }
                            if (NexEditorEventListener.this.a != null) {
                                NexEditorEventListener.this.a.d(NexEditor.ErrorCode.NONE);
                            }
                            NexEditorEventListener nexEditorEventListener = NexEditorEventListener.this;
                            nexEditorEventListener.a(nexEditorEventListener.d, NexEditor.PlayState.IDLE);
                            return;
                        }
                        NexEditorEventListener nexEditorEventListener2 = NexEditorEventListener.this;
                        nexEditorEventListener2.a(nexEditorEventListener2.d, NexEditor.PlayState.IDLE);
                    }
                });
                break;
            case 14:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.4
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a == null || !NexEditorEventListener.this.a.f(NexEditor.ErrorCode.fromValue(i2))) {
                            if (NexEditorEventListener.this.b != null) {
                                NexEditorEventListener.this.b.b(NexEditor.ErrorCode.fromValue(i2));
                            }
                            if (NexEditorEventListener.this.a != null) {
                                NexEditorEventListener.this.a.d(NexEditor.ErrorCode.fromValue(i2));
                            }
                            NexEditorEventListener nexEditorEventListener = NexEditorEventListener.this;
                            nexEditorEventListener.a(nexEditorEventListener.d, NexEditor.PlayState.IDLE);
                            return;
                        }
                        NexEditorEventListener nexEditorEventListener2 = NexEditorEventListener.this;
                        nexEditorEventListener2.a(nexEditorEventListener2.d, NexEditor.PlayState.IDLE);
                    }
                });
                break;
            case 17:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.6
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.b();
                        }
                        NexEditorEventListener nexEditorEventListener = NexEditorEventListener.this;
                        nexEditorEventListener.a(nexEditorEventListener.d, NexEditor.PlayState.IDLE);
                    }
                });
                break;
            case 19:
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] VIDEO_STARTED ", new Object[0]));
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.8
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.n();
                        }
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.c();
                        }
                    }
                });
                break;
            case 20:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.13
                    @Override // java.lang.Runnable
                    public void run() {
                        Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_GETCLIPINFO_DONE delivery p1=" + i2 + " p2=" + i3);
                        if (NexEditorEventListener.this.a != null) {
                            Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_GETCLIPINFO_DONE deliver to editor");
                            NexEditorEventListener.this.a.c(NexEditor.ErrorCode.fromValue(i2), i3);
                        }
                        if (NexEditorEventListener.this.b != null) {
                            Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_GETCLIPINFO_DONE deliver to UI listener");
                            NexEditorEventListener.this.b.d();
                        }
                    }
                });
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_GETCLIPINFO_DONE p1=" + i2, new Object[0]));
                break;
            case 21:
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_CAPTURE_DONE p1=" + i2, new Object[0]));
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.9
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.e(NexEditor.ErrorCode.fromValue(i2));
                        }
                    }
                });
                break;
            case 22:
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_PREPARE_CLIP_LOADING p1=" + i2, new Object[0]));
                break;
            case 23:
                Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_TRANSCODING_DONE delivery p1=" + i2 + " p2=" + i3);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.11
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.a(NexEditor.ErrorCode.fromValue(i2), i3);
                        }
                    }
                });
                break;
            case 24:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.10
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.b(i2, i3, i4);
                        }
                    }
                });
                break;
            case 26:
                Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_FAST_OPTION_PREVIEW_DONE");
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.15
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.a(NexEditor.ErrorCode.fromValue(i2));
                        }
                    }
                });
                break;
            case 27:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_MAKE_HIGHLIGHT_DONE errcode=" + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.27
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.c(i2);
                        }
                    }
                });
                break;
            case 28:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_MAKE_HIGHLIGHT_PROGRESS_INDEX=" + i2 + ", p2=" + i3);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.25
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.a(i2, i3);
                        }
                    }
                });
                break;
            case 30:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_CHECK_DIRECT_EXPORT p1=" + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.16
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.b(NexEditor.ErrorCode.fromValue(i2), i3);
                        }
                    }
                });
                break;
            case 31:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_DIRECT_EXPORT_DONE p1=" + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.17
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.b(NexEditor.ErrorCode.fromValue(i2));
                        }
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.d(NexEditor.ErrorCode.fromValue(i2));
                        }
                        NexEditorEventListener nexEditorEventListener = NexEditorEventListener.this;
                        nexEditorEventListener.a(nexEditorEventListener.d, NexEditor.PlayState.IDLE);
                    }
                });
                break;
            case 32:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_DIRECT_EXPORT_PROGRESS=" + i2);
                break;
            case 33:
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.14
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_GETCLIPINFO_STOP_DONE deliver to editor p1=" + i2 + ", p2=" + i3);
                            NexEditorEventListener.this.a.c(NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL, i3);
                        }
                        if (NexEditorEventListener.this.b != null) {
                            Log.d("NexEditorEventHandler", "VIDEOEDITOR_EVENT_GETCLIPINFO_STOP_DONE deliver to UI listener");
                            NexEditorEventListener.this.b.d();
                        }
                    }
                });
                Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_GETCLIPINFO_STOP_DONE p1=" + i2, new Object[0]));
                break;
            case 35:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_FAST_PREVIEW_START_DONE p1= " + i2 + ", p2= " + i3 + ", p3: " + i4);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.21
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.a(NexEditor.ErrorCode.fromValue(i2), i3, i4);
                        }
                    }
                });
                break;
            case 36:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_FAST_PREVIEW_STOP_DONE p1= " + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.22
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.d(NexEditor.ErrorCode.fromValue(i2));
                        }
                    }
                });
                break;
            case 37:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_FAST_PREVIEW_TIME_DONE p1= " + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.24
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.e(NexEditor.ErrorCode.fromValue(i2));
                        }
                    }
                });
                break;
            case 38:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_HIGHLIGHT_THUMBNAIL_PROGRESS=" + i2 + ", p2=" + i3);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.18
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.a(i2, i3);
                        }
                    }
                });
                break;
            case 39:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_MAKE_REVERSE_DONE p1= " + i2 + ", p2= " + i3);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.19
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b == null || NexEditorEventListener.this.b == null) {
                            return;
                        }
                        NexEditorEventListener.this.b.c(NexEditor.ErrorCode.fromValue(i2));
                    }
                });
                break;
            case 40:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_MAKE_REVERSE_PROGRESS p1= " + i2 + ", p2= " + i3 + ", p3: " + i4);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.20
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.b(i2, 100);
                        }
                    }
                });
                break;
            case 41:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_MAKE_VAS_HIGHLIGHT_DONE errcode=" + i2);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.28
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.a != null) {
                            NexEditorEventListener.this.a.d(i2);
                        }
                    }
                });
                break;
            case 51:
                Log.i("NexEditorEventHandler", "[nexEditorEventHandler.java] VIDEOEDITOR_EVENT_PREVIEW_PEAKMETER cts=" + i2 + ", value=" + i3);
                this.c.post(new Runnable() { // from class: com.nexstreaming.kminternal.nexvideoeditor.NexEditorEventListener.26
                    @Override // java.lang.Runnable
                    public void run() {
                        if (NexEditorEventListener.this.b != null) {
                            NexEditorEventListener.this.b.c(i2, i3);
                        }
                    }
                });
                break;
        }
        return 0;
    }

    public int notifyError(int i, int i2, int i3, int i4) {
        Log.i("NexEditorEventHandler", String.format("[nexEditorEventHandler.java] event(%d) Param(%d %d) ", Integer.valueOf(i), Integer.valueOf(i3), Integer.valueOf(i4)));
        return 0;
    }

    private void a() {
        if (this.l == 0) {
            try {
                InputStream open = com.nexstreaming.kminternal.kinemaster.config.a.a().b().getAssets().open(EditorGlobal.b());
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = open.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    messageDigest.update(bArr, 0, read);
                }
                open.close();
                if (Base64.encodeToString(messageDigest.digest(), 0).startsWith("5i/mnZqgIegSRcn19oeAQavHHw9HeyJZugRi3/4ASTY=")) {
                    this.l = 2;
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            }
            this.l = 1;
        }
    }

    private void b() {
        if (this.l != 2) {
            LayerRenderer layerRenderer = this.i;
            layerRenderer.a(-65281, 0.0f, 0.0f, layerRenderer.a(), this.i.b());
        } else if (true != this.k) {
        } else {
            String language = com.nexstreaming.kminternal.kinemaster.config.a.a().b().getResources().getConfiguration().locale.getLanguage();
            if (n == null || language != o) {
                o = language;
                new BitmapFactory.Options().inScaled = false;
                try {
                    InputStream open = com.nexstreaming.kminternal.kinemaster.config.a.a().b().getAssets().open(EditorGlobal.b());
                    n = BitmapFactory.decodeStream(open);
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (n == null) {
                return;
            }
            float b = this.i.b() / 1080.0f;
            float a2 = this.i.a() * 0.96484375f;
            float b2 = this.i.b() * 0.048611112f;
            this.i.a(n, a2 - (n.getWidth() * b), b2, a2, b2 + (n.getHeight() * b));
        }
    }

    public int callbackPrepareCustomLayer(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17, int i18) {
        if (this.h == null) {
            return 1;
        }
        this.i.d((i16 & 1) != 0);
        int a2 = NexEditor.a();
        int b = NexEditor.b();
        if (NexEditor.c() == 0) {
            if (i17 > i18) {
                a2 = Math.round((i17 * 720.0f) / i18);
                b = 720;
            } else {
                b = Math.round((i18 * 720.0f) / i17);
                a2 = 720;
            }
        }
        if (this.a.d() == 1) {
            a2 *= 2;
        }
        this.i.a(a2, b);
        this.i.b(i17, i18);
        this.i.a(i2);
        this.i.m();
        this.h.a(this.i);
        b();
        this.i.n();
        return 0;
    }

    public void setCustomRenderCallback(NexEditor.c cVar) {
        this.h = cVar;
    }

    public int getLutTextWithID(int i, int i2) {
        if (this.a == null) {
            Log.d("NexEditorEventHandler", "getLutTextWithID() engine is null");
            return 0;
        }
        com.nexstreaming.kminternal.kinemaster.editorwrapper.b c = com.nexstreaming.kminternal.kinemaster.editorwrapper.b.c();
        if (c == null) {
            Log.d("NexEditorEventHandler", "getLutTextWithID() getLookUpTable is null");
            return 0;
        }
        b.C0105b a2 = c.a(i);
        if (a2 == null) {
            Log.d("NexEditorEventHandler", "getLutTextWithID() lut is null");
            return 0;
        }
        return this.i.a(a2.b(), i2);
    }

    public int getVignetteTexID(int i) {
        com.nexstreaming.kminternal.kinemaster.editorwrapper.b c = com.nexstreaming.kminternal.kinemaster.editorwrapper.b.c();
        if (c == null) {
            Log.d("NexEditorEventHandler", "getVignetteTexID() getLookUpTable is null");
            return 0;
        }
        Log.d("NexEditorEventHandler", "getVignetteTexID() call... export_flag=" + i);
        return this.i.a(c.g(), i);
    }

    public String getAssetResourceKey(String str) {
        String substring = str.substring(12);
        int indexOf = substring.indexOf(47);
        if (indexOf >= 0) {
            String substring2 = substring.substring(0, indexOf);
            String substring3 = substring.substring(indexOf + 1);
            f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).c(substring2);
            if (c == null) {
                return str;
            }
            String str2 = "9v16";
            if (!substring2.contains(str2)) {
                if (substring2.contains("2v1")) {
                    str2 = "2v1";
                } else if (substring2.contains("1v2")) {
                    str2 = "1v2";
                } else if (substring2.contains("1v1")) {
                    str2 = "1v1";
                } else if (substring2.contains("4v3")) {
                    str2 = "4v3";
                } else {
                    str2 = substring2.contains("3v4") ? "3v4" : "16v9";
                }
            }
            return "[ThemeImage]" + c.getAssetPackage().getAssetId() + h.g + str2 + h.g + substring3;
        }
        return str;
    }
}
