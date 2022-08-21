package com.miui.gallery.job;

import androidx.work.ExistingPeriodicWorkPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* compiled from: PeriodicWorkerProvider.kt */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes2.dex */
public @interface PeriodicWorkerProvider {
    ExistingPeriodicWorkPolicy existWorkPolicy() default ExistingPeriodicWorkPolicy.KEEP;

    boolean unique() default true;

    String uniqueName() default "";
}
