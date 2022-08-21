package ch.qos.logback.classic.turbo;

import ch.qos.logback.classic.Level;

/* loaded from: classes.dex */
public class MDCValueLevelPair {
    private Level level;
    private String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public Level getLevel() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
}
