package com.adobe.xmp.options;

/* loaded from: classes.dex */
public final class ParseOptions extends Options {
    @Override // com.adobe.xmp.options.Options
    public int getValidOptions() {
        return 61;
    }

    public ParseOptions() {
        setOption(24, true);
    }

    public boolean getRequireXMPMeta() {
        return getOption(1);
    }

    public boolean getStrictAliasing() {
        return getOption(4);
    }

    public boolean getFixControlChars() {
        return getOption(8);
    }

    public boolean getAcceptLatin1() {
        return getOption(16);
    }

    public boolean getOmitNormalization() {
        return getOption(32);
    }
}
