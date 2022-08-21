package miui.cloud.sdk;

/* loaded from: classes3.dex */
public class Dependency {
    public Level level;
    public String name;
    public int type;

    public void setName(String str) {
        this.name = str;
    }

    public void setType(int i) {
        this.type = i;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    /* loaded from: classes3.dex */
    public static class Level {
        public int maxLevel;
        public int minLevel;
        public int targetLevel;

        public int getMinLevel() {
            return this.minLevel;
        }

        public void setMinLevel(int i) {
            this.minLevel = i;
        }

        public void setTargetLevel(int i) {
            this.targetLevel = i;
        }

        public void setMaxLevel(int i) {
            this.maxLevel = i;
        }
    }
}
