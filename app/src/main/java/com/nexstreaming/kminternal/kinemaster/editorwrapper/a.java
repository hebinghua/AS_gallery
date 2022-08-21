package com.nexstreaming.kminternal.kinemaster.editorwrapper;

import android.content.Intent;
import com.google.gson_nex.Gson;
import com.nexstreaming.kminternal.kinemaster.editorwrapper.KMIntentData;
import java.util.ArrayList;

/* compiled from: KMIntentBuilder.java */
/* loaded from: classes3.dex */
public class a {
    private KMIntentData a = new KMIntentData();

    public g a() {
        KMIntentData.VisualClip visualClip = new KMIntentData.VisualClip();
        this.a.project.visualClips.add(visualClip);
        return new g(visualClip, this);
    }

    public b b() {
        KMIntentData.VisualClip visualClip = new KMIntentData.VisualClip();
        this.a.project.visualClips.add(visualClip);
        return new b(visualClip, this);
    }

    public e c() {
        KMIntentData.VisualClip visualClip = new KMIntentData.VisualClip();
        this.a.project.visualClips.add(visualClip);
        return new e(visualClip, this);
    }

    public C0104a d() {
        KMIntentData.AudioClip audioClip = new KMIntentData.AudioClip();
        this.a.project.audioClips.add(audioClip);
        return new C0104a(audioClip, this);
    }

    public f e() {
        KMIntentData.Layer layer = new KMIntentData.Layer();
        layer.layerType = KMIntentData.LayerType.Text;
        this.a.project.layers.add(layer);
        return new f(layer, this);
    }

    public Intent f() {
        Gson gson = new Gson();
        Intent intent = new Intent("com.kinemaster.intent.NEW_PROJECT");
        intent.setPackage("com.nexstreaming.app.kinemasterfree");
        intent.putExtra("com.kinemaster.intent.projectData", gson.toJson(this.a));
        intent.setFlags(268468224);
        return intent;
    }

    /* compiled from: KMIntentBuilder.java */
    /* renamed from: com.nexstreaming.kminternal.kinemaster.editorwrapper.a$a  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public static class C0104a {
        public final KMIntentData.AudioClip a;
        public final a b;

        public C0104a(KMIntentData.AudioClip audioClip, a aVar) {
            this.a = audioClip;
            this.b = aVar;
        }

        public C0104a a(String str) {
            this.a.path = str;
            return this;
        }

        public C0104a a(int i) {
            this.a.startTrim = i;
            return this;
        }

        public C0104a b(int i) {
            this.a.endTrim = i;
            return this;
        }

        public C0104a c(int i) {
            this.a.startTime = i;
            return this;
        }

        public C0104a d(int i) {
            this.a.endTime = i;
            return this;
        }

        public C0104a a(boolean z) {
            this.a.loop = z;
            return this;
        }

        public C0104a b(boolean z) {
            this.a.background = z;
            return this;
        }

        public C0104a c(boolean z) {
            this.a.mute = z;
            return this;
        }

        public C0104a e(int i) {
            this.a.volume = i;
            return this;
        }

        public a a() {
            return this.b;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class d<T> {
        public final KMIntentData.Layer a;
        public final a b;
        public final T c = this;

        /* JADX WARN: Multi-variable type inference failed */
        public d(KMIntentData.Layer layer, a aVar) {
            this.a = layer;
            this.b = aVar;
        }

        public T a(int i) {
            this.a.startTime = i;
            return this.c;
        }

        public T b(int i) {
            this.a.endTime = i;
            return this.c;
        }

        public T a(String str) {
            this.a.animationIn = str;
            return this.c;
        }

        public T b(String str) {
            this.a.animationOut = str;
            return this.c;
        }

        public T c(String str) {
            this.a.animationOverall = str;
            return this.c;
        }

        public T c(int i) {
            this.a.animationInDuration = i;
            return this.c;
        }

        public T d(int i) {
            this.a.animationOutDuration = i;
            return this.c;
        }

        public c<T> a() {
            KMIntentData.Layer layer = this.a;
            if (layer.keyFrames == null) {
                layer.keyFrames = new ArrayList();
            }
            KMIntentData.KeyFrame keyFrame = new KMIntentData.KeyFrame();
            this.a.keyFrames.add(keyFrame);
            return new c<>(keyFrame, this);
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class c<T> {
        public final KMIntentData.KeyFrame a;
        public final d<T> b;

        public c(KMIntentData.KeyFrame keyFrame, d<T> dVar) {
            this.a = keyFrame;
            this.b = dVar;
        }

        public c<T> a(float f) {
            this.a.time = f;
            return this;
        }

        public c<T> b(float f) {
            this.a.angle = f;
            return this;
        }

        public c<T> c(float f) {
            this.a.alpha = f;
            return this;
        }

        public c<T> d(float f) {
            this.a.scale = f;
            return this;
        }

        public c<T> a(float f, float f2) {
            KMIntentData.KeyFrame keyFrame = this.a;
            keyFrame.x = f;
            keyFrame.y = f2;
            return this;
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [T, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$d<T>] */
        public T a() {
            return this.b;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class f extends d<f> {
        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f a(int i) {
            return super.a(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f a(String str) {
            return super.a(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f b(int i) {
            return super.b(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f b(String str) {
            return super.b(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f c(int i) {
            return super.c(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f c(String str) {
            return super.c(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$f, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ f d(int i) {
            return super.d(i);
        }

        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.d
        public /* bridge */ /* synthetic */ c<f> a() {
            return super.a();
        }

        public f(KMIntentData.Layer layer, a aVar) {
            super(layer, aVar);
            layer.textLayerAttributes = new KMIntentData.TextLayerAttributes();
        }

        public f d(String str) {
            this.a.textLayerAttributes.text = str;
            return this;
        }

        public f a(float f) {
            this.a.textLayerAttributes.textSize = f;
            return this;
        }

        public f e(int i) {
            this.a.textLayerAttributes.textColor = i;
            return this;
        }

        public f f(int i) {
            this.a.textLayerAttributes.shadowColor = i;
            return this;
        }

        public f g(int i) {
            this.a.textLayerAttributes.glowColor = i;
            return this;
        }

        public f h(int i) {
            this.a.textLayerAttributes.outlineColor = i;
            return this;
        }

        public f e(String str) {
            this.a.textLayerAttributes.fontId = str;
            return this;
        }

        public a b() {
            return this.b;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class h<T> {
        public final KMIntentData.VisualClip a;
        public final a b;
        public final T c = this;

        /* JADX WARN: Multi-variable type inference failed */
        public h(KMIntentData.VisualClip visualClip, a aVar) {
            this.a = visualClip;
            this.b = aVar;
        }

        public T e(int i) {
            this.a.duration = i;
            return this.c;
        }

        public T f(int i) {
            this.a.transitionDuration = i;
            return this.c;
        }

        public T b(String str) {
            this.a.clipEffectId = str;
            return this.c;
        }

        public T c(String str) {
            this.a.transitionEffectId = str;
            return this.c;
        }

        public T g(int i) {
            this.a.volume = i;
            return this.c;
        }

        public T a(boolean z) {
            this.a.mute = z;
            return this.c;
        }

        public T d(int i) {
            this.a.rotation = i;
            return this.c;
        }

        public T h(int i) {
            this.a.brightness = i;
            return this.c;
        }

        public T i(int i) {
            this.a.contrast = i;
            return this.c;
        }

        public T j(int i) {
            this.a.saturation = i;
            return this.c;
        }

        public T d(String str) {
            this.a.colorFilterId = str;
            return this.c;
        }

        public T b(boolean z) {
            this.a.vignette = z;
            return this.c;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class g extends h<g> {
        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g a(boolean z) {
            return super.a(z);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g b(String str) {
            return super.b(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g b(boolean z) {
            return super.b(z);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g c(String str) {
            return super.c(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g d(String str) {
            return super.d(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g e(int i) {
            return super.e(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g f(int i) {
            return super.f(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g g(int i) {
            return super.g(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g h(int i) {
            return super.h(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g i(int i) {
            return super.i(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$g, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ g j(int i) {
            return super.j(i);
        }

        public g(KMIntentData.VisualClip visualClip, a aVar) {
            super(visualClip, aVar);
        }

        public g a(String str) {
            this.a.path = str;
            return this;
        }

        public g a(int i) {
            this.a.startTrim = i;
            return this;
        }

        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        /* renamed from: b */
        public g d(int i) {
            this.a.rotation = i;
            return this;
        }

        public g c(int i) {
            this.a.playbackSpeed = i;
            return this;
        }

        public g a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            KMIntentData.VisualClip visualClip = this.a;
            visualClip.cropStartLeft = f;
            visualClip.cropStartTop = f2;
            visualClip.cropStartRight = f3;
            visualClip.cropStartBottom = f4;
            visualClip.cropEndLeft = f5;
            visualClip.cropEndTop = f6;
            visualClip.cropEndRight = f7;
            visualClip.cropEndBottom = f8;
            return this;
        }

        public a a() {
            return this.b;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class b extends h<b> {
        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b b(String str) {
            return super.b(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b b(boolean z) {
            return super.b(z);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b c(String str) {
            return super.c(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b d(int i) {
            return super.d(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b d(String str) {
            return super.d(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b e(int i) {
            return super.e(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b f(int i) {
            return super.f(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b h(int i) {
            return super.h(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b i(int i) {
            return super.i(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.lang.Object, com.nexstreaming.kminternal.kinemaster.editorwrapper.a$b] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ b j(int i) {
            return super.j(i);
        }

        public b(KMIntentData.VisualClip visualClip, a aVar) {
            super(visualClip, aVar);
        }

        public b a(String str) {
            this.a.path = str;
            return this;
        }

        public b a(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            KMIntentData.VisualClip visualClip = this.a;
            visualClip.cropStartLeft = f;
            visualClip.cropStartTop = f2;
            visualClip.cropStartRight = f3;
            visualClip.cropStartBottom = f4;
            visualClip.cropEndLeft = f5;
            visualClip.cropEndTop = f6;
            visualClip.cropEndRight = f7;
            visualClip.cropEndBottom = f8;
            return this;
        }

        public a a() {
            return this.b;
        }
    }

    /* compiled from: KMIntentBuilder.java */
    /* loaded from: classes3.dex */
    public static class e extends h<e> {
        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e b(String str) {
            return super.b(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e b(boolean z) {
            return super.b(z);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e c(String str) {
            return super.c(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e d(int i) {
            return super.d(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e d(String str) {
            return super.d(str);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e e(int i) {
            return super.e(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e f(int i) {
            return super.f(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e h(int i) {
            return super.h(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e i(int i) {
            return super.i(i);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [com.nexstreaming.kminternal.kinemaster.editorwrapper.a$e, java.lang.Object] */
        @Override // com.nexstreaming.kminternal.kinemaster.editorwrapper.a.h
        public /* bridge */ /* synthetic */ e j(int i) {
            return super.j(i);
        }

        public e(KMIntentData.VisualClip visualClip, a aVar) {
            super(visualClip, aVar);
        }

        public e a(int i) {
            String hexString = Long.toHexString((i & 4294967295L) | 68719476736L);
            KMIntentData.VisualClip visualClip = this.a;
            visualClip.path = "@solid:" + hexString.substring(hexString.length() - 8) + ".jpg";
            return this;
        }

        public a a() {
            return this.b;
        }
    }
}
