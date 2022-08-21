package com.baidu.mapsdkplatform.comapi.map.a;

import com.baidu.platform.comapi.map.InnerOverlay;
import com.baidu.platform.comapi.map.j;
import com.baidu.platform.comapi.util.JsonBuilder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class a extends InnerOverlay {
    public final List<j> a;
    private boolean d;
    private int e;
    private int f;
    private int g;
    private boolean h;

    public a() {
        super(36);
        this.d = false;
        this.e = 0;
        this.f = 0;
        this.g = 0;
        this.a = new ArrayList();
        this.h = true;
    }

    public void a() {
        this.h = true;
        UpdateOverlay();
    }

    public void a(boolean z, int i, int i2, int i3) {
        this.d = z;
        this.e = i;
        this.f = i2;
        this.g = i3;
    }

    public boolean a(j jVar) {
        synchronized (this.a) {
            if (this.a.contains(jVar)) {
                return false;
            }
            boolean add = this.a.add(jVar);
            this.h = add;
            return add;
        }
    }

    @Override // com.baidu.platform.comapi.map.InnerOverlay
    public void clear() {
        synchronized (this.a) {
            this.a.clear();
        }
        super.clear();
    }

    @Override // com.baidu.platform.comapi.map.InnerOverlay
    public String getData() {
        if (this.h) {
            synchronized (this.a) {
                if (this.a.size() == 0) {
                    return "";
                }
                JsonBuilder jsonBuilder = new JsonBuilder();
                jsonBuilder.object();
                jsonBuilder.key("dataset").arrayValue();
                for (j jVar : this.a) {
                    jsonBuilder.objectValue(jVar.a());
                }
                jsonBuilder.endArrayValue();
                jsonBuilder.key("startValue").value(0);
                jsonBuilder.key("endValue").value(1);
                if (this.d) {
                    jsonBuilder.key("isNeedRouteAnimate").value(1);
                    jsonBuilder.key("durationTime").value(this.e);
                    jsonBuilder.key("delayTime").value(this.f);
                    jsonBuilder.key("easingCurve").value(this.g);
                    this.d = false;
                } else {
                    jsonBuilder.key("isNeedRouteAnimate").value(0);
                    jsonBuilder.key("durationTime").value(0);
                    jsonBuilder.key("delayTime").value(0);
                    jsonBuilder.key("easingCurve").value(0);
                }
                jsonBuilder.endObject();
                setData(jsonBuilder.getJson());
                this.h = false;
            }
        }
        return super.getData();
    }

    @Override // com.baidu.platform.comapi.map.InnerOverlay
    public void setData(String str) {
        super.setData(str);
        this.h = true;
    }
}
