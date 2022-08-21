package ch.qos.logback.core.util;

/* loaded from: classes.dex */
class CharSequenceState {
    public final char c;
    public int occurrences = 1;

    public CharSequenceState(char c) {
        this.c = c;
    }

    public void incrementOccurrences() {
        this.occurrences++;
    }
}
