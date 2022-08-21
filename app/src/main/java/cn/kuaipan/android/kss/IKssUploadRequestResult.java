package cn.kuaipan.android.kss;

/* loaded from: classes.dex */
public interface IKssUploadRequestResult {
    Block getBlock(int i);

    int getBlockCount();

    String getFileMeta();

    String[] getNodeUrls();

    byte[] getSecureKey();

    boolean isCompleted();

    /* loaded from: classes.dex */
    public static class Block {
        public boolean exist;
        public String meta;

        public Block(String str, boolean z) {
            this.meta = str;
            this.exist = z;
        }

        public boolean isComplete() {
            return this.exist;
        }
    }
}
