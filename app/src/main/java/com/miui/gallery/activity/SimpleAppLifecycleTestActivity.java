package com.miui.gallery.activity;

import android.os.Bundle;
import android.util.Pair;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import com.miui.gallery.app.activity.GalleryActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes.dex */
public class SimpleAppLifecycleTestActivity extends GalleryActivity {
    public static List<Pair<TestEventType, Lifecycle.Event>> sCollectedEvents = new ArrayList();
    public static CountDownLatch sLatch = new CountDownLatch(11);
    public static TestObserver sProcessObserver = new TestObserver(TestEventType.PROCESS_EVENT);

    /* loaded from: classes.dex */
    public enum TestEventType {
        PROCESS_EVENT,
        ACTIVITY_EVENT
    }

    /* loaded from: classes.dex */
    public static class TestObserver implements LifecycleObserver {
        public TestEventType mType;

        public TestObserver(TestEventType testEventType) {
            this.mType = testEventType;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void onEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            SimpleAppLifecycleTestActivity.sCollectedEvents.add(new Pair<>(this.mType, event));
            SimpleAppLifecycleTestActivity.sLatch.countDown();
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getLifecycle().addObserver(new TestObserver(TestEventType.ACTIVITY_EVENT));
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        finish();
    }
}
