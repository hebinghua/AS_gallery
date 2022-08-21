package org.sqlite.database.trace;

import android.text.TextUtils;
import android.util.Log;
import android.util.LogPrinter;
import android.util.Printer;
import android.util.SparseArray;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public final class TraceHelper {
    public final boolean ENABLED;
    public final long FREQUENCY_THRESHOLD;
    public final String TAG;
    public final long TIME_CONSUMING_THRESHOLD;
    public SparseArray<List<Track>> mFrequencyTrackList;
    public final Printer mPrinter;
    public final ThreadLocal<Track> mThreadTrack;

    public TraceHelper(String str) {
        this(str, new LogPrinter(3, str));
    }

    public TraceHelper(String str, Printer printer) {
        this.mFrequencyTrackList = new SparseArray<>();
        this.mThreadTrack = new ThreadLocal<Track>() { // from class: org.sqlite.database.trace.TraceHelper.1
            @Override // java.lang.ThreadLocal
            public Track initialValue() {
                return new Track();
            }
        };
        this.TAG = str;
        this.mPrinter = printer;
        this.ENABLED = false;
        Log.d(str, "trace disabled");
        this.TIME_CONSUMING_THRESHOLD = Long.MAX_VALUE;
        this.FREQUENCY_THRESHOLD = Long.MAX_VALUE;
    }

    public void traceBegin(String str) {
        if (!this.ENABLED) {
            return;
        }
        Track track = this.mThreadTrack.get();
        track.mMessage = str;
        track.mStack = traceStack();
        track.mBegin = System.currentTimeMillis();
    }

    public void traceEnd() {
        if (!this.ENABLED) {
            return;
        }
        Track track = this.mThreadTrack.get();
        long currentTimeMillis = System.currentTimeMillis();
        track.mEnd = currentTimeMillis;
        if (currentTimeMillis - track.mBegin > this.TIME_CONSUMING_THRESHOLD) {
            dumpTrack("TIME_CONSUMING", Arrays.asList(track));
        }
        if (TextUtils.isEmpty(track.mMessage)) {
            return;
        }
        int hashCode = track.mMessage.hashCode();
        synchronized (this.mFrequencyTrackList) {
            List<Track> list = this.mFrequencyTrackList.get(hashCode);
            if (list == null) {
                list = new LinkedList<>();
                this.mFrequencyTrackList.put(hashCode, list);
            }
            list.add(track);
            if (System.currentTimeMillis() - list.get(0).mBegin > 1000) {
                if (list.size() > this.FREQUENCY_THRESHOLD) {
                    dumpTrack("FREQUENCY", list);
                }
                list.clear();
            }
        }
    }

    public final void dumpTrack(String str, List<Track> list) {
        synchronized (this.mPrinter) {
            this.mPrinter.println(String.format("############%s###############", str));
            for (Track track : list) {
                this.mPrinter.println(track.mMessage);
                this.mPrinter.println(track.mStack);
                Printer printer = this.mPrinter;
                printer.println("cost: " + (track.mEnd - track.mBegin));
                this.mPrinter.println("-------------------------------------------------------");
            }
        }
    }

    public static String traceStack() {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
            sb.append(stackTraceElement.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /* loaded from: classes3.dex */
    public static final class Track {
        public long mBegin;
        public long mEnd;
        public String mMessage;
        public String mStack;

        public Track() {
        }
    }
}
