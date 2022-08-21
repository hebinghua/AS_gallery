package com.nexstreaming.kminternal.kinemaster.mediainfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import ch.qos.logback.core.CoreConstants;
import com.google.gson_nex.Gson;
import com.google.gson_nex.JsonIOException;
import com.google.gson_nex.JsonSyntaxException;
import com.nexstreaming.app.common.task.ResultTask;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.app.common.util.FileType;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.config.ExclusionList;
import com.nexstreaming.kminternal.kinemaster.config.NexEditorDeviceProfile;
import com.nexstreaming.kminternal.nexvideoeditor.NexClipInfo;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class MediaInfo {
    private static Map<String, WeakReference<MediaInfo>> a;
    private static ExclusionList b;
    private static File y;
    private static File z;
    private boolean A;
    private String c;
    private int d;
    private File e;
    private File f;
    private File g;
    private File h;
    private File i;
    private File j;
    private File k;
    private File l;
    private String m;
    private NexEditor.ErrorCode n;
    private c<h, f> o = null;
    private c<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void> p = null;
    private ResultTask<Bitmap> q = null;
    private ResultTask<Bitmap> r = null;
    private ResultTask<int[]> s = null;
    private AsyncTask<String, Integer, int[]> t = null;
    private b u;
    private boolean x;
    private static Executor v = Executors.newSingleThreadExecutor();
    private static Executor w = Executors.newSingleThreadExecutor();
    private static Deque<c<h, f>> B = new ArrayDeque();
    private static int C = 0;
    private static Deque<c<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void>> D = new ArrayDeque();
    private static Task E = null;
    private static SparseArray<c<?, ?>> F = new SparseArray<>();
    private static boolean G = false;
    private static boolean H = false;
    private static Object I = new Object();
    private static NexEditor.k J = new NexEditor.k() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.4
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.k
        public void a(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7, int i8) {
            c cVar = (c) MediaInfo.F.get(i8);
            Log.d("MediaInfo", "sThumbDoneListener onGetThumbDoneListener : tag=" + i8);
            if (cVar != null) {
                Log.d("MediaInfo", "sThumbDoneListener NOTIFY TASK!");
                cVar.a(i, i2, i3, i4, i5, bArr, i6, i7);
            }
        }
    };
    private static NexEditor.j K = new NexEditor.j() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.5
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.j
        public void a(NexEditor.ErrorCode errorCode, int i) {
            c cVar = (c) MediaInfo.F.get(i);
            Log.d("MediaInfo", "sClipInfoDoneListener onGetClipInfoDone : tag=" + i + " resultCode=" + errorCode);
            if (cVar != null) {
                Log.d("MediaInfo", "sClipInfoDoneListener NOTIFY TASK!");
                cVar.a(errorCode);
            }
        }
    };
    private static NexEditor.h L = new NexEditor.h() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.6
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.h
        public void a() {
            Log.d("MediaInfo", "sOnEditorDestroyedListener onEditorDestroyed");
            for (int i = 0; i < MediaInfo.F.size(); i++) {
                ((c) MediaInfo.F.valueAt(i)).sendFailure(NexEditor.ErrorCode.EDITOR_INSTANCE_DESTROYED);
            }
            SparseArray unused = MediaInfo.F = new SparseArray();
            Deque unused2 = MediaInfo.B = new ArrayDeque();
            int unused3 = MediaInfo.C = 0;
        }
    };
    private static NexEditor.m M = new NexEditor.m() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.7
        @Override // com.nexstreaming.kminternal.nexvideoeditor.NexEditor.m
        public void a() {
            Log.d("MediaInfo", "sIdleListener onIdle!");
            MediaInfo.R();
            MediaInfo.S();
        }
    };

    /* loaded from: classes3.dex */
    public interface d<RESULT_TYPE, PARAM_TYPE> {
        void a(c<RESULT_TYPE, PARAM_TYPE> cVar, NexEditor.ErrorCode errorCode);
    }

    /* loaded from: classes3.dex */
    public interface e {
        void a(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7);
    }

    private static int b(String str) {
        if (str != null) {
            return str.startsWith("nexasset://") ? 1 : 0;
        }
        return -1;
    }

    public static void a(File file) {
        z = file;
    }

    public static void a(Context context) {
        y = context.getApplicationContext().getCacheDir();
    }

    public static MediaInfo a(String str) {
        if (str == null) {
            return null;
        }
        return a(str, true);
    }

    public static MediaInfo a(String str, boolean z2) {
        WeakReference<MediaInfo> weakReference;
        MediaInfo mediaInfo;
        if (str == null) {
            return null;
        }
        Log.d("MediaInfo", "getInfo(" + str + ")");
        if (a == null) {
            Log.d("MediaInfo", "getInfo : init cache");
            a = new HashMap();
        }
        if (b == null) {
            Log.d("MediaInfo", "getInfo : init exclusion list");
            b = ExclusionList.exclusionListBackedBy(new File(new File(y, ".km_mediainfo"), "mediainfo_exclude.dat"));
        }
        int b2 = b(str);
        String absolutePath = b2 == 0 ? new File(str).getAbsolutePath() : str;
        if (z2 && (weakReference = a.get(absolutePath)) != null && (mediaInfo = weakReference.get()) != null) {
            Log.d("MediaInfo", "getInfo(" + str + ") using cache. duration=" + mediaInfo.p());
            return mediaInfo;
        }
        if (b2 == 1) {
            z2 = false;
        }
        MediaInfo mediaInfo2 = new MediaInfo(str, b2, z2);
        if (mediaInfo2.n == NexEditor.ErrorCode.NO_INSTANCE_AVAILABLE) {
            return mediaInfo2;
        }
        Log.d("MediaInfo", "getInfo(" + str + ") adding to cache duration=" + mediaInfo2.p());
        a.put(absolutePath, new WeakReference<>(mediaInfo2));
        return mediaInfo2;
    }

    private void M() {
        if (this.d != 0) {
            b bVar = new b();
            this.u = bVar;
            bVar.a = 9;
            return;
        }
        File file = new File(this.c);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        b bVar2 = new b();
        this.u = bVar2;
        bVar2.a = 9;
        bVar2.b = file.getAbsolutePath();
        b bVar3 = this.u;
        bVar3.d = false;
        bVar3.e = false;
        bVar3.f = true;
        bVar3.k = 0;
        bVar3.l = 0;
        bVar3.c = file.length();
        b bVar4 = this.u;
        bVar4.g = options.outWidth;
        bVar4.h = options.outHeight;
        bVar4.m = 0;
        bVar4.n = 0;
        bVar4.o = 0;
        bVar4.p = 0;
        bVar4.q = 0;
        bVar4.w = 0;
        bVar4.x = 0;
        bVar4.y = 0;
        bVar4.z = 0;
        this.x = false;
        this.n = NexEditor.ErrorCode.NONE;
    }

    private long N() {
        int i = this.d;
        if (i == 0) {
            return new File(this.c).length();
        }
        if (i != 1) {
            return 0L;
        }
        String str = this.c;
        return Integer.parseInt(str.substring(str.lastIndexOf(58) + 1));
    }

    private MediaInfo(String str, int i, boolean z2) {
        Gson gson;
        b bVar;
        FileType fromFile;
        this.d = -1;
        this.n = NexEditor.ErrorCode.NONE;
        this.A = true;
        this.c = str;
        this.d = i;
        if (i == 0 && (fromFile = FileType.fromFile(str)) != null && fromFile.isImage()) {
            if (fromFile.isSupportedFormat()) {
                M();
                return;
            } else {
                this.x = true;
                return;
            }
        }
        File file = new File(y, ".km_mediainfo");
        file.mkdirs();
        String format = String.format(Locale.US, "none_%08X", Integer.valueOf(str.hashCode()));
        if (i == 0) {
            format = b(new File(str));
        } else if (i == 1) {
            format = c(str);
        }
        Log.d("MediaInfo", "mediaInfoDir=" + file);
        this.e = new File(file, format + "_info.dat");
        this.f = new File(file, format + "_seek.dat");
        this.g = new File(file, format + "_vthumb.dat");
        this.h = new File(file, format + "_vthumb_large.dat");
        this.i = new File(file, format + "_vthumb_large_end.dat");
        this.j = new File(file, format + "_vthumb_raw.dat");
        this.k = new File(file, format + "_pcm.dat");
        this.l = file;
        this.m = format;
        this.A = z2;
        if (!z2 || !this.e.exists()) {
            gson = null;
        } else {
            Log.d("MediaInfo", "getInfo(" + str + ") info file exists -> attemptng to read");
            gson = new Gson();
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(this.e));
                bVar = (b) gson.fromJson((Reader) bufferedReader, (Class<Object>) b.class);
                try {
                    bufferedReader.close();
                } catch (JsonIOException | JsonSyntaxException | IOException unused) {
                }
            } catch (JsonIOException | JsonSyntaxException | IOException unused2) {
                bVar = null;
            }
            if (bVar != null && bVar.a == 9) {
                Log.d("MediaInfo", "getInfo(" + str + ") info from disk cache");
                this.u = bVar;
                return;
            }
        }
        ExclusionList exclusionList = b;
        if (exclusionList != null && exclusionList.isExcluded(format)) {
            Log.d("MediaInfo", "getInfo(" + str + ") skip due to exclusion");
            this.x = true;
            return;
        }
        NexClipInfo nexClipInfo = new NexClipInfo();
        NexEditor Q = Q();
        if (Q == null) {
            this.n = NexEditor.ErrorCode.NO_INSTANCE_AVAILABLE;
            Log.d("MediaInfo", "getInfo(" + str + ") failure:" + this.n);
            return;
        }
        b.add(format);
        Log.d("MediaInfo", "getInfo(" + str + ") call editor for info");
        this.n = Q.a(str, nexClipInfo, false, 0);
        Log.d("MediaInfo", "getInfo(" + str + ") returned from editor");
        b.remove(format);
        b bVar2 = new b();
        this.u = bVar2;
        if (this.n != NexEditor.ErrorCode.NONE) {
            Log.d("MediaInfo", "getInfo(" + str + ") failure:" + this.n);
            return;
        }
        bVar2.a = 9;
        bVar2.b = str;
        bVar2.d = nexClipInfo.mExistAudio != 0;
        bVar2.e = nexClipInfo.mExistVideo != 0;
        bVar2.f = false;
        bVar2.k = nexClipInfo.mAudioDuration;
        bVar2.l = nexClipInfo.mVideoDuration;
        bVar2.c = N();
        b bVar3 = this.u;
        bVar3.g = nexClipInfo.mVideoWidth;
        bVar3.h = nexClipInfo.mVideoHeight;
        bVar3.i = nexClipInfo.mDisplayVideoWidth;
        bVar3.j = nexClipInfo.mDisplayVideoHeight;
        bVar3.m = nexClipInfo.mSeekPointCount;
        bVar3.n = nexClipInfo.mFPS;
        bVar3.o = nexClipInfo.mVideoH264Profile;
        bVar3.p = nexClipInfo.mVideoH264Level;
        bVar3.q = nexClipInfo.mVideoH264Interlaced;
        bVar3.v = nexClipInfo.mVideoOrientation;
        bVar3.r = nexClipInfo.mVideoBitRate;
        bVar3.s = nexClipInfo.mAudioBitRate;
        bVar3.t = nexClipInfo.mAudioSampleRate;
        bVar3.u = nexClipInfo.mAudioChannels;
        bVar3.w = nexClipInfo.mVideoRenderType;
        bVar3.x = nexClipInfo.mVideoCodecType;
        bVar3.y = nexClipInfo.mAudioCodecType;
        bVar3.z = nexClipInfo.mVideoHDRType;
        if (z2) {
            gson = gson == null ? new Gson() : gson;
            Log.d("MediaInfo", "getInfo(" + str + ") writing:" + this.e);
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.e));
                gson.toJson(this.u, bufferedWriter);
                bufferedWriter.close();
                this.e.setReadable(true);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        Log.d("MediaInfo", "getInfo(" + str + ") out");
    }

    private static String c(String str) {
        String substring = str.substring(11);
        substring.replace(CoreConstants.COLON_CHAR, '_');
        return "nexasset_" + substring;
    }

    private static String b(File file) {
        String name = file.getName();
        int hashCode = file.getAbsolutePath().hashCode();
        long lastModified = file.lastModified();
        long length = file.length();
        if (name.length() > 32) {
            name = name.substring(0, 32);
        }
        int i = (int) ((((lastModified * 212501089) + (length * 194851861)) + hashCode) % 268435455);
        return name + "_" + String.format(Locale.US, "%08X", Integer.valueOf(i));
    }

    /* loaded from: classes3.dex */
    public static class f {
        public final String a;
        public final File b;
        public final int c;
        public final int d;
        public final int e;
        public final int f;
        public final int g;
        public final int h;
        public final int[] i;

        public f(String str, File file, int i, int i2, int i3, int i4, int i5, int i6) {
            this.a = str;
            this.b = file;
            this.c = i;
            this.d = i2;
            this.e = i3;
            this.f = i4;
            this.g = i5;
            this.h = i6;
            this.i = null;
        }

        public f(String str, File file, int i, int i2, int[] iArr, int i3) {
            this.a = str;
            this.b = file;
            this.c = i;
            this.d = i2;
            this.e = 0;
            this.f = 0;
            this.g = iArr.length;
            this.h = i3;
            this.i = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Task O() {
        final Task task = new Task();
        new File(y, ".km_mediainfo").mkdirs();
        new com.nexstreaming.kminternal.kinemaster.mediainfo.f(this.j, this.g, this.h, this.i) { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.1
            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.f
            public void a() {
                MediaInfo.this.j.delete();
                task.signalEvent(Task.Event.SUCCESS, Task.Event.COMPLETE);
            }

            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.f
            public void a(Task.TaskError taskError) {
                MediaInfo.this.j.delete();
                task.sendFailure(taskError);
            }
        }.executeOnExecutor(v, 0);
        return task;
    }

    public static boolean a() {
        NexEditor Q = Q();
        if (Q == null) {
            return false;
        }
        Log.d("MediaInfo", "cancelAllGetThumbnails : Pending size=" + B.size());
        Log.d("MediaInfo", "cancelAllGetThumbnails : Active size=" + F.size());
        for (int i = 0; i < F.size(); i++) {
            c<?, ?> valueAt = F.valueAt(i);
            valueAt.a = 0;
            valueAt.b = true;
        }
        for (c<h, f> cVar : B) {
            Log.d("MediaInfo", "cancelAllGetThumbnails : id=" + cVar.getTaskId());
            cVar.a = 0;
            cVar.b = true;
        }
        Q.l(0);
        return true;
    }

    public Task a(int i, int i2, int i3, int i4, final int i5, int i6, int[] iArr, final com.nexstreaming.kminternal.kinemaster.mediainfo.c cVar) {
        f fVar;
        boolean z2 = true;
        C++;
        File file = this.l;
        File file2 = z;
        if (file2 != null) {
            Log.d("MediaInfo", "getDetailThumbnails::temp cache dir=" + file2.getAbsolutePath());
            file = file2;
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        final File file3 = new File(file, this.m + "_detail_" + i + "_" + i2 + "_" + i3 + "_" + i4 + "_" + i5 + "_" + C);
        if (iArr != null) {
            fVar = new f(this.c, file3, i, i2, iArr, i6);
        } else {
            fVar = new f(this.c, file3, i, i2, i3, i4, i5, i6);
        }
        c<h, f> cVar2 = new c<>(this, fVar, new d<h, f>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.9
            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.d
            public void a(final c<h, f> cVar3, NexEditor.ErrorCode errorCode) {
                if (errorCode == NexEditor.ErrorCode.NONE) {
                    MediaInfo.S();
                    if ((((f) cVar3.a()).h & nexEngine.ExportHEVCMainTierLevel52) == 262144) {
                        Log.d("MediaInfo", "getDetailThumbnails::onEditorAsyncDone() no cache mode");
                        cVar3.signalEvent(Task.Event.SUCCESS, Task.Event.COMPLETE);
                        cVar3.sendResult(com.nexstreaming.kminternal.kinemaster.mediainfo.a.a());
                        return;
                    }
                    Log.d("MediaInfo", "getDetailThumbnails::onEditorAsyncDone(" + MediaInfo.this.c + ") -> " + file3);
                    new AsyncTask<Void, Void, Void>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.9.1
                        public Task.TaskError a = null;

                        @Override // android.os.AsyncTask
                        /* renamed from: a */
                        public Void doInBackground(Void... voidArr) {
                            AnonymousClass9 anonymousClass9 = AnonymousClass9.this;
                            int i7 = i5;
                            if (i7 == 0) {
                                this.a = g.a(file3, 50, cVar);
                            } else {
                                this.a = g.a(file3, i7, cVar);
                            }
                            file3.delete();
                            return null;
                        }

                        @Override // android.os.AsyncTask
                        /* renamed from: a */
                        public void onPostExecute(Void r4) {
                            Task.TaskError taskError = this.a;
                            if (taskError != null) {
                                cVar3.sendFailure(taskError);
                                return;
                            }
                            cVar3.signalEvent(Task.Event.SUCCESS, Task.Event.COMPLETE);
                            cVar3.sendResult(com.nexstreaming.kminternal.kinemaster.mediainfo.a.a());
                        }
                    }.executeOnExecutor(MediaInfo.v, new Void[0]);
                } else if (cVar3.b) {
                    Log.d("MediaInfo", "getDetailThumbnails::onEditorAsyncDone : User Cancel ID=" + cVar3.getTaskId());
                    cVar3.a = 0;
                    file3.delete();
                    cVar3.sendFailure(NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL);
                    MediaInfo.S();
                } else {
                    cVar3.a--;
                    if (cVar instanceof com.nexstreaming.kminternal.kinemaster.mediainfo.e) {
                        cVar3.a = 0;
                        Log.d("MediaInfo", "getDetailThumbnails::Raw File. No RETRYING=" + errorCode + ", ID=" + cVar3.getTaskId());
                    }
                    if (cVar3.a > 0) {
                        Log.d("MediaInfo", "getDetailThumbnails::onEditorAsyncDone : RETRYING=" + errorCode + ", ID=" + cVar3.getTaskId());
                        file3.delete();
                        NexEditor.ErrorCode errorCode2 = NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL;
                        if (errorCode != errorCode2 && !cVar3.b) {
                            MediaInfo.B.add(cVar3);
                            if (errorCode == NexEditor.ErrorCode.INPROGRESS_GETCLIPINFO) {
                                return;
                            }
                            MediaInfo.S();
                            return;
                        }
                        cVar3.a = 0;
                        cVar3.sendFailure(errorCode2);
                        MediaInfo.S();
                        return;
                    }
                    Log.d("MediaInfo", "getDetailThumbnails::onEditorAsyncDone : SEND FAILURE=" + errorCode + ", cancel=" + cVar3.b);
                    if (errorCode == NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL || cVar3.b) {
                        cVar3.a = 0;
                    }
                    file3.delete();
                    cVar3.sendFailure(errorCode);
                    MediaInfo.S();
                }
            }
        }, new e() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.10
            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.e
            public void a(int i7, int i8, int i9, int i10, int i11, byte[] bArr, int i12, int i13) {
                if (i7 != 1) {
                    Log.d("MediaInfo", "getDetailThumbnails::onThumbDone() not video. mode=" + i7);
                    return;
                }
                try {
                    g.a(bArr, i8, i9, i10, (i11 / (i9 * i10)) * 8, i12 - 1, i13, cVar);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        if (Q() == null || !B.isEmpty()) {
            z2 = false;
        }
        B.add(cVar2);
        if (z2) {
            S();
        }
        return cVar2;
    }

    public ResultTask<h> b() {
        if (this.g == null && this.j == null) {
            return ResultTask.failedResultTask(MediaInfoError.ThumbnailsNotAvailable);
        }
        Log.d("MediaInfo", "getThumbnails(" + this.c + ") sPendingThumbnailTasks=" + B.size());
        c<h, f> cVar = this.o;
        if (cVar != null && !cVar.didSignalEvent(Task.Event.FAIL)) {
            Log.d("MediaInfo", "getThumbnails(" + this.c + ") returning existing task");
            return this.o;
        }
        this.o = new c<>(this, new f(this.c, this.j, 640, 360, 0, r(), 30, 0), new AnonymousClass11());
        c(this.g).onResultAvailable(new ResultTask.OnResultAvailableListener<a>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.12
            @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
            /* renamed from: a */
            public void onResultAvailable(ResultTask<a> resultTask, Task.Event event, a aVar) {
                if (aVar == null || aVar.d == null || aVar.a <= 0 || aVar.b <= 0 || aVar.c == null) {
                    boolean z2 = MediaInfo.Q() != null && MediaInfo.B.isEmpty();
                    MediaInfo.B.add(MediaInfo.this.o);
                    if (!z2) {
                        return;
                    }
                    MediaInfo.S();
                    return;
                }
                MediaInfo.this.o.sendResult(new i(aVar.d, aVar.a, aVar.b, aVar.c));
            }
        });
        Log.d("MediaInfo", "getThumbnails(" + this.c + ") returning NEW task");
        return this.o;
    }

    /* renamed from: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo$11  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass11 implements d<h, f> {
        public AnonymousClass11() {
        }

        @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.d
        public void a(c<h, f> cVar, NexEditor.ErrorCode errorCode) {
            Log.d("MediaInfo", "getThumbnails::onEditorAsyncDone(" + MediaInfo.this.c + ") resultCode=" + errorCode + " retry=" + cVar.a);
            if (errorCode == NexEditor.ErrorCode.NONE) {
                MediaInfo.S();
                Log.d("MediaInfo", "getThumbnails::onEditorAsyncDone(" + MediaInfo.this.c + ") Start JPEG Conversion");
                MediaInfo.this.O().mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.11.2
                    @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                    public void onTaskEvent(Task task, Task.Event event) {
                        MediaInfo mediaInfo = MediaInfo.this;
                        mediaInfo.c(mediaInfo.g).onResultAvailable(new ResultTask.OnResultAvailableListener<a>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.11.2.1
                            @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                            /* renamed from: a */
                            public void onResultAvailable(ResultTask<a> resultTask, Task.Event event2, a aVar) {
                                if (aVar != null) {
                                    MediaInfo.this.o.sendResult(new i(aVar.d, aVar.a, aVar.b, aVar.c));
                                } else {
                                    MediaInfo.this.o.sendFailure(null);
                                }
                            }
                        });
                    }
                }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.11.1
                    @Override // com.nexstreaming.app.common.task.Task.OnFailListener
                    public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                        MediaInfo.this.o.sendFailure(null);
                    }
                });
                return;
            }
            int i = cVar.a - 1;
            cVar.a = i;
            if (i > 0) {
                Log.d("MediaInfo", "getThumbnails::onEditorAsyncDone : RETRYING ID=" + cVar.getTaskId());
                MediaInfo.B.add(cVar);
                if (MediaInfo.B.size() > 1 || errorCode == NexEditor.ErrorCode.INPROGRESS_GETCLIPINFO) {
                    return;
                }
                MediaInfo.S();
                return;
            }
            Log.d("MediaInfo", "getThumbnails::onEditorAsyncDone : SEND FAILURE");
            MediaInfo.this.o.sendFailure(errorCode);
        }
    }

    /* loaded from: classes3.dex */
    public static class a {
        public final int a;
        public final int b;
        public final int[] c;
        public final Bitmap d;

        public a(int i, int i2, int[] iArr, Bitmap bitmap) {
            this.a = i;
            this.b = i2;
            this.c = iArr;
            this.d = bitmap;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResultTask<a> c(File file) {
        final ResultTask<a> resultTask = new ResultTask<>();
        new AsyncTask<File, Integer, a>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.13
            @Override // android.os.AsyncTask
            /* renamed from: a */
            public a doInBackground(File... fileArr) {
                try {
                    return a(fileArr[0]);
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            }

            private a a(File file2) throws IOException {
                DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file2)));
                int readInt = dataInputStream.readInt();
                int readInt2 = dataInputStream.readInt();
                int readInt3 = dataInputStream.readInt();
                int[] iArr = new int[readInt3];
                for (int i = 0; i < readInt3; i++) {
                    iArr[i] = dataInputStream.readInt();
                }
                return new a(readInt, readInt2, iArr, BitmapFactory.decodeStream(dataInputStream));
            }

            @Override // android.os.AsyncTask
            /* renamed from: a */
            public void onPostExecute(a aVar) {
                resultTask.setResult(aVar);
                resultTask.signalEvent(Task.Event.RESULT_AVAILABLE, Task.Event.SUCCESS, Task.Event.COMPLETE);
            }
        }.executeOnExecutor(w, file);
        return resultTask;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> P() {
        final ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> resultTask = new ResultTask<>();
        new AsyncTask<MediaInfo, Integer, com.nexstreaming.kminternal.kinemaster.mediainfo.b>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.14
            @Override // android.os.AsyncTask
            /* renamed from: a */
            public com.nexstreaming.kminternal.kinemaster.mediainfo.b doInBackground(MediaInfo... mediaInfoArr) {
                MediaInfo mediaInfo = mediaInfoArr[0];
                if (mediaInfo.k.exists()) {
                    int min = (int) Math.min(mediaInfo.k.length(), 204800L);
                    try {
                        byte[] bArr = new byte[min];
                        FileInputStream fileInputStream = new FileInputStream(MediaInfo.this.k);
                        int read = fileInputStream.read(bArr);
                        fileInputStream.close();
                        if (read < min) {
                            return null;
                        }
                        return new com.nexstreaming.kminternal.kinemaster.mediainfo.b(bArr);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return null;
                    }
                }
                return null;
            }

            @Override // android.os.AsyncTask
            /* renamed from: a */
            public void onPostExecute(com.nexstreaming.kminternal.kinemaster.mediainfo.b bVar) {
                resultTask.setResult(bVar);
                resultTask.signalEvent(Task.Event.RESULT_AVAILABLE, Task.Event.SUCCESS, Task.Event.COMPLETE);
            }
        }.executeOnExecutor(w, this);
        return resultTask;
    }

    /* loaded from: classes3.dex */
    public enum MediaInfoError implements Task.TaskError {
        PCMLevelsNotAvailable,
        SeekPointsNotAvailable,
        ThumbnailsNotAvailable,
        LargeStartThumbnailNotAvailable,
        LargeEndThumbnailNotAvailable;

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return null;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return name();
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return name();
        }
    }

    public ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> c() {
        if (this.k == null) {
            return ResultTask.failedResultTask(MediaInfoError.PCMLevelsNotAvailable);
        }
        c<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void> cVar = this.p;
        if (cVar != null && !cVar.didSignalEvent(Task.Event.FAIL)) {
            return this.p;
        }
        this.p = new c<>(this, null, new d<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.15
            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.d
            public void a(final c<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void> cVar2, NexEditor.ErrorCode errorCode) {
                if (errorCode == NexEditor.ErrorCode.INVALID_STATE) {
                    MediaInfo.D.add(cVar2);
                    return;
                }
                MediaInfo.R();
                ((c) cVar2).c.P().onResultAvailable(new ResultTask.OnResultAvailableListener<com.nexstreaming.kminternal.kinemaster.mediainfo.b>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.15.1
                    @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
                    /* renamed from: a */
                    public void onResultAvailable(ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> resultTask, Task.Event event, com.nexstreaming.kminternal.kinemaster.mediainfo.b bVar) {
                        if (bVar != null) {
                            cVar2.sendResult(bVar);
                        } else {
                            cVar2.sendFailure(null);
                        }
                    }
                });
            }
        });
        P().onResultAvailable(new ResultTask.OnResultAvailableListener<com.nexstreaming.kminternal.kinemaster.mediainfo.b>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.2
            @Override // com.nexstreaming.app.common.task.ResultTask.OnResultAvailableListener
            /* renamed from: a */
            public void onResultAvailable(ResultTask<com.nexstreaming.kminternal.kinemaster.mediainfo.b> resultTask, Task.Event event, com.nexstreaming.kminternal.kinemaster.mediainfo.b bVar) {
                if (bVar != null) {
                    MediaInfo.this.p.sendResult(bVar);
                    return;
                }
                NexEditor Q = MediaInfo.Q();
                if (Q == null || !MediaInfo.D.isEmpty()) {
                    MediaInfo.D.add(MediaInfo.this.p);
                } else {
                    Q.a(MediaInfo.this.c, MediaInfo.this.k, MediaInfo.this.p.getTaskId());
                }
            }
        });
        return this.p;
    }

    public int[] d() {
        if (this.t == null) {
            e();
            if (this.t == null) {
                throw new IllegalStateException();
            }
        }
        try {
            return this.t.get();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
            return null;
        } catch (ExecutionException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public NexEditor.ErrorCode a(boolean z2, boolean z3) {
        NexEditor Q = Q();
        if (Q == null) {
            return NexEditor.ErrorCode.GENERAL;
        }
        int i = z2 ? 256 : 0;
        if (z3) {
            i |= 16;
        }
        return Q.a(this.c, new NexClipInfo(), i, 0);
    }

    public ResultTask<int[]> e() {
        if (this.f == null) {
            return ResultTask.failedResultTask(MediaInfoError.SeekPointsNotAvailable);
        }
        ResultTask<int[]> resultTask = this.s;
        if (resultTask != null && !resultTask.didSignalEvent(Task.Event.FAIL)) {
            return this.s;
        }
        Log.d("MediaInfo", "getSeekPoints(" + this.c + ")");
        this.s = new ResultTask<>();
        this.t = new AsyncTask<String, Integer, int[]>() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo.3
            @Override // android.os.AsyncTask
            /* renamed from: a */
            public int[] doInBackground(String... strArr) {
                Log.d("MediaInfo", "getSeekPoints:doInBackground(" + strArr[0] + ")");
                try {
                    return a(MediaInfo.this.f);
                } catch (IOException unused) {
                    Log.d("MediaInfo", "getSeekPoints:doInBackground(" + strArr[0] + ") -> no cache available; making");
                    NexEditor Q = MediaInfo.Q();
                    if (Q == null) {
                        return null;
                    }
                    NexClipInfo nexClipInfo = new NexClipInfo();
                    NexEditor.ErrorCode a2 = Q.a(strArr[0], nexClipInfo, true, 0);
                    if (!a2.isError() && nexClipInfo.mSeekTable != null) {
                        try {
                            a(MediaInfo.this.f, nexClipInfo.mSeekTable);
                        } catch (IOException e2) {
                            Log.d("MediaInfo", "getSeekPoints(" + MediaInfo.this.c + ") FAILED WRITING FILE");
                            e2.printStackTrace();
                        }
                        return nexClipInfo.mSeekTable;
                    }
                    Log.d("MediaInfo", "getSeekPoints(" + MediaInfo.this.c + ") FAIL -> " + a2);
                    return null;
                }
            }

            private int[] a(File file) throws IOException {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                try {
                    int min = ((int) Math.min(file.length(), 204800L)) / 4;
                    int[] iArr = new int[min];
                    for (int i = 0; i < min; i++) {
                        iArr[i] = dataInputStream.readInt();
                    }
                    Log.d("MediaInfo", "getSeekPoints():readFile : got " + min + " entries.");
                    return iArr;
                } finally {
                    dataInputStream.close();
                }
            }

            private void a(File file, int[] iArr) throws IOException {
                Log.d("MediaInfo", "getSeekPoints():writeFile(" + file + ")");
                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
                int i = 0;
                while (true) {
                    try {
                        if (i < iArr.length) {
                            dataOutputStream.writeInt(iArr[i]);
                            i++;
                        } else {
                            Log.d("MediaInfo", "getSeekPoints(" + MediaInfo.this.c + ") wrote " + iArr.length + " points");
                            return;
                        }
                    } finally {
                        dataOutputStream.close();
                        Log.d("MediaInfo", "getSeekPoints(" + MediaInfo.this.c + ") wrote file: " + file);
                        file.setReadable(true);
                    }
                }
            }

            @Override // android.os.AsyncTask
            /* renamed from: a */
            public void onPostExecute(int[] iArr) {
                if (iArr == null) {
                    Log.d("MediaInfo", "onPostExecute : FAIL");
                    MediaInfo.this.s.signalEvent(Task.Event.FAIL);
                    return;
                }
                Log.d("MediaInfo", "onPostExecute : SUCCESS " + iArr.length);
                MediaInfo.this.s.setResult(iArr);
                MediaInfo.this.s.signalEvent(Task.Event.RESULT_AVAILABLE, Task.Event.SUCCESS, Task.Event.COMPLETE);
            }
        }.executeOnExecutor(w, this.c);
        return this.s;
    }

    public int f() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.m;
    }

    public boolean g() {
        return this.n != NexEditor.ErrorCode.UNSUPPORT_MAX_RESOLUTION && o() * n() <= (NexEditorDeviceProfile.getDeviceProfile().getMaxResolution() * 110) / 100;
    }

    public boolean h() {
        return !this.x && !this.n.isError() && g();
    }

    public boolean i() {
        return this.x || this.n.isError();
    }

    /* renamed from: com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo$8  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass8 {
        public static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[NexEditor.ErrorCode.values().length];
            a = iArr;
            try {
                iArr[NexEditor.ErrorCode.UNSUPPORT_AUDIO_CODEC.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_AUDIO_PROFILE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_FORMAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_MAX_RESOLUTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_MIN_DURATION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_MIN_RESOLUTION.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_VIDEIO_PROFILE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_VIDEO_CODEC.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_VIDEO_FPS.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                a[NexEditor.ErrorCode.UNSUPPORT_VIDEO_LEVEL.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public int j() {
        if (!this.n.isError()) {
            return h() ? 0 : -1;
        }
        switch (AnonymousClass8.a[this.n.ordinal()]) {
            case 1:
                return -2;
            case 2:
                return -3;
            case 3:
                return -4;
            case 4:
                return -5;
            case 5:
                return -6;
            case 6:
                return -7;
            case 7:
                return -8;
            case 8:
                return -9;
            case 9:
                return -10;
            case 10:
                return -11;
            default:
                return -12;
        }
    }

    public static boolean k() {
        return F.size() != 0;
    }

    public boolean l() {
        if (this.x || this.n.isError()) {
            return false;
        }
        return this.u.d;
    }

    public boolean m() {
        if (this.x || this.n.isError()) {
            return false;
        }
        return this.u.e;
    }

    public int n() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.g;
    }

    public int o() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.h;
    }

    public int p() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        b bVar = this.u;
        if (bVar.d) {
            return bVar.k;
        }
        return bVar.l;
    }

    public int q() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.k;
    }

    public int r() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.l;
    }

    public int s() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.n;
    }

    public int t() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.v;
    }

    public int u() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.o;
    }

    public int v() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.p;
    }

    public int w() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.r;
    }

    public int x() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.s;
    }

    public int y() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.t;
    }

    public int z() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.u;
    }

    public int A() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.w;
    }

    public int B() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.x;
    }

    public int C() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.y;
    }

    public int D() {
        if (this.x || this.n.isError()) {
            return 0;
        }
        return this.u.z;
    }

    /* loaded from: classes3.dex */
    public static class b {
        public int a;
        public String b;
        public long c;
        public boolean d;
        public boolean e;
        public boolean f;
        public int g;
        public int h;
        public int i;
        public int j;
        public int k;
        public int l;
        public int m;
        public int n;
        public int o;
        public int p;
        public int q;
        public int r;
        public int s;
        public int t;
        public int u;
        public int v;
        public int w;
        public int x;
        public int y;
        public int z;

        private b() {
        }
    }

    /* loaded from: classes3.dex */
    public static class c<RESULT_TYPE, PARAM_TYPE> extends ResultTask<RESULT_TYPE> {
        public int a;
        public boolean b;
        private final MediaInfo c;
        private final d<RESULT_TYPE, PARAM_TYPE> d;
        private final e e;
        private final PARAM_TYPE f;

        public c(MediaInfo mediaInfo, PARAM_TYPE param_type, d<RESULT_TYPE, PARAM_TYPE> dVar) {
            this.a = 3;
            this.b = false;
            this.d = dVar;
            this.e = null;
            this.c = mediaInfo;
            this.f = param_type;
            Log.d("MediaInfo", "MediaInfoTask : add to active tasks; tag=" + getTaskId());
            MediaInfo.F.put(getTaskId(), this);
        }

        public c(MediaInfo mediaInfo, PARAM_TYPE param_type, d<RESULT_TYPE, PARAM_TYPE> dVar, e eVar) {
            this.a = 3;
            this.b = false;
            this.d = dVar;
            this.e = eVar;
            this.c = mediaInfo;
            this.f = param_type;
            Log.d("MediaInfo", "MediaInfoTask : add to active tasks; thumb tag=" + getTaskId());
            MediaInfo.F.put(getTaskId(), this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public PARAM_TYPE a() {
            return this.f;
        }

        @Override // com.nexstreaming.app.common.task.ResultTask
        public void sendResult(RESULT_TYPE result_type) {
            Log.d("MediaInfo", "MediaInfoTask::sendResult : remove from active tasks; tag=" + getTaskId());
            if (MediaInfo.F.get(getTaskId()) == this) {
                MediaInfo.F.remove(getTaskId());
                Log.d("MediaInfo", "MediaInfoTask::sendResult : tag=" + getTaskId() + " sActiveTasks.size()=" + MediaInfo.F.size());
                if (MediaInfo.F.size() == 0 && MediaInfo.E != null) {
                    Log.d("MediaInfo", "MediaInfoTask::sendResult : tag=" + getTaskId() + " mWaitNotBusyTask.signalEvent(COMPLETE)");
                    MediaInfo.E.signalEvent(Task.Event.COMPLETE);
                }
                super.sendResult(result_type);
                return;
            }
            Log.d("MediaInfo", "MediaInfoTask::sendResult : NOT THIS; tag=" + getTaskId());
        }

        @Override // com.nexstreaming.app.common.task.Task
        public void sendFailure(Task.TaskError taskError) {
            Log.d("MediaInfo", "MediaInfoTask::sendFailure : remove from active tasks; tag=" + getTaskId());
            if (MediaInfo.F.get(getTaskId()) != this) {
                Log.d("MediaInfo", "MediaInfoTask::sendFailure : NOT THIS; tag=" + getTaskId());
                return;
            }
            super.sendFailure(taskError);
            MediaInfo.F.remove(getTaskId());
            Log.d("MediaInfo", "MediaInfoTask::sendFailure : tag=" + getTaskId() + " sActiveTasks.size()=" + MediaInfo.F.size());
            if (MediaInfo.F.size() != 0 || MediaInfo.E == null) {
                return;
            }
            Log.d("MediaInfo", "MediaInfoTask::sendFailure : tag=" + getTaskId() + " mWaitNotBusyTask.signalEvent(COMPLETE)");
            MediaInfo.E.signalEvent(Task.Event.COMPLETE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(NexEditor.ErrorCode errorCode) {
            d<RESULT_TYPE, PARAM_TYPE> dVar;
            if (MediaInfo.F.get(getTaskId()) == this && (dVar = this.d) != null) {
                dVar.a(this, errorCode);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(int i, int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7) {
            e eVar;
            if (MediaInfo.F.get(getTaskId()) == this && (eVar = this.e) != null) {
                eVar.a(i, i2, i3, i4, i5, bArr, i6, i7);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static NexEditor Q() {
        if (EditorGlobal.a() == null) {
            Log.d("MediaInfo", "getEditor : NULL EDITOR");
            return null;
        }
        Log.d("MediaInfo", "getEditor : setting sClipInfoDoneListener=" + K);
        EditorGlobal.a().a(K);
        EditorGlobal.a().a(M);
        EditorGlobal.a().a(L);
        EditorGlobal.a().a(J);
        return EditorGlobal.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void R() {
        NexEditor Q;
        if (D.isEmpty() || (Q = Q()) == null) {
            return;
        }
        c<com.nexstreaming.kminternal.kinemaster.mediainfo.b, Void> remove = D.remove();
        Q.a(((c) remove).c.c, ((c) remove).c.k, remove.getTaskId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void S() {
        synchronized (I) {
            Log.d("MediaInfo", "startPendingThumbnailTask taskcount=" + B.size());
            if (!B.isEmpty()) {
                NexEditor Q = Q();
                if (Q == null) {
                    return;
                }
                c<h, f> cVar = null;
                boolean z2 = true;
                while (true) {
                    if (B.isEmpty()) {
                        break;
                    }
                    cVar = B.remove();
                    if (!cVar.b) {
                        z2 = false;
                        break;
                    }
                    Log.d("MediaInfo", "startPendingThumbnailTask cancel thumbnail TaskId=" + cVar.getTaskId());
                    cVar.sendFailure(NexEditor.ErrorCode.GETCLIPINFO_USER_CANCEL);
                }
                if (!z2) {
                    f fVar = (f) cVar.a();
                    try {
                        if (fVar.i != null) {
                            Log.d("MediaInfo", "startPendingThumbnailTask use TimeTable sThumbnailsRunTaskId=" + cVar.getTaskId());
                            String str = fVar.a;
                            File file = fVar.b;
                            int i = fVar.c;
                            int i2 = fVar.d;
                            int[] iArr = fVar.i;
                            Q.a(str, file, i, i2, iArr.length, iArr, fVar.h, cVar.getTaskId());
                        } else {
                            Log.d("MediaInfo", "startPendingThumbnailTask range time sThumbnailsRunTaskId=" + cVar.getTaskId());
                            Q.a(fVar.a, fVar.b, fVar.c, fVar.d, fVar.e, fVar.f, fVar.g, fVar.h, cVar.getTaskId());
                        }
                    } catch (IOException e2) {
                        cVar.sendFailure(Task.makeTaskError(e2));
                    }
                } else {
                    Log.d("MediaInfo", "startPendingThumbnailTask all canceled.");
                }
            } else {
                Log.d("MediaInfo", "startPendingThumbnailTask all run end.");
            }
        }
    }
}
