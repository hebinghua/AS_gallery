package ch.qos.logback.core.rolling.helper;

import java.io.File;
import java.util.List;

/* compiled from: FileFinder.java */
/* loaded from: classes.dex */
class LiteralPathPart extends PathPart {
    public LiteralPathPart(String str) {
        super(str);
    }

    @Override // ch.qos.logback.core.rolling.helper.PathPart
    public boolean matches(File file) {
        return file.getName().equals(this.part);
    }

    @Override // ch.qos.logback.core.rolling.helper.PathPart
    public List<File> listFiles(FileProvider fileProvider) {
        return listFiles(fileProvider, this.part);
    }
}
