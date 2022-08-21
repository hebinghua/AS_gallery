package ch.qos.logback.classic.spi;

import java.io.Serializable;

/* loaded from: classes.dex */
public class ClassPackagingData implements Serializable {
    private static final long serialVersionUID = -804643281218337001L;
    public final String codeLocation;
    private final boolean exact;
    public final String version;

    public ClassPackagingData(String str, String str2) {
        this.codeLocation = str;
        this.version = str2;
        this.exact = true;
    }

    public ClassPackagingData(String str, String str2, boolean z) {
        this.codeLocation = str;
        this.version = str2;
        this.exact = z;
    }

    public String getCodeLocation() {
        return this.codeLocation;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isExact() {
        return this.exact;
    }

    public int hashCode() {
        String str = this.codeLocation;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClassPackagingData classPackagingData = (ClassPackagingData) obj;
        String str = this.codeLocation;
        if (str == null) {
            if (classPackagingData.codeLocation != null) {
                return false;
            }
        } else if (!str.equals(classPackagingData.codeLocation)) {
            return false;
        }
        if (this.exact != classPackagingData.exact) {
            return false;
        }
        String str2 = this.version;
        if (str2 == null) {
            if (classPackagingData.version != null) {
                return false;
            }
        } else if (!str2.equals(classPackagingData.version)) {
            return false;
        }
        return true;
    }
}
