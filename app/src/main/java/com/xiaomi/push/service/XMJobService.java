package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.xiaomi.push.eu;

/* loaded from: classes3.dex */
public class XMJobService extends Service {
    public static Service a;

    /* renamed from: a  reason: collision with other field name */
    public IBinder f819a = null;

    @TargetApi(21)
    /* loaded from: classes3.dex */
    public static class a extends JobService {
        public Binder a;

        /* renamed from: a  reason: collision with other field name */
        public Handler f820a;

        /* renamed from: com.xiaomi.push.service.XMJobService$a$a  reason: collision with other inner class name */
        /* loaded from: classes3.dex */
        public static class HandlerC0112a extends Handler {
            public JobService a;

            public HandlerC0112a(JobService jobService) {
                super(jobService.getMainLooper());
                this.a = jobService;
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (message.what != 1) {
                    return;
                }
                JobParameters jobParameters = (JobParameters) message.obj;
                com.xiaomi.channel.commonutils.logger.b.m1859a("Job finished " + jobParameters.getJobId());
                this.a.jobFinished(jobParameters, false);
                if (jobParameters.getJobId() != 1) {
                    return;
                }
                eu.a(false);
            }
        }

        public a(Service service) {
            this.a = null;
            this.a = (Binder) com.xiaomi.push.bk.a((Object) this, "onBind", new Intent());
            com.xiaomi.push.bk.a((Object) this, "attachBaseContext", service);
        }

        @Override // android.app.job.JobService
        public boolean onStartJob(JobParameters jobParameters) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Job started " + jobParameters.getJobId());
            Intent intent = new Intent(this, XMPushService.class);
            intent.setAction("com.xiaomi.push.timer");
            intent.setPackage(getPackageName());
            startService(intent);
            if (this.f820a == null) {
                this.f820a = new HandlerC0112a(this);
            }
            Handler handler = this.f820a;
            handler.sendMessage(Message.obtain(handler, 1, jobParameters));
            return true;
        }

        @Override // android.app.job.JobService
        public boolean onStopJob(JobParameters jobParameters) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Job stop " + jobParameters.getJobId());
            return false;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        IBinder iBinder = this.f819a;
        return iBinder != null ? iBinder : new Binder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 21) {
            this.f819a = new a(this).a;
        }
        a = this;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        a = null;
    }
}
