package cn.kuaipan.android.kss;

/* loaded from: classes.dex */
public interface IKssDownloadRequestResult {
    Block getBlock(int i);

    int getBlockCount();

    String[] getBlockUrls(long j);

    String getHash();

    String getMessage();

    long getModifyTime();

    byte[] getSecureKey();

    int getStatus();

    long getTotalSize();

    /* loaded from: classes.dex */
    public static class Block {
        public final String sha1;
        public final long size;
        public final String[] urls;

        public Block(String str, String[] strArr, long j) {
            this.sha1 = str;
            this.urls = strArr;
            this.size = j;
        }
    }
}
