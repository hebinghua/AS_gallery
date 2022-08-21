package cn.kuaipan.android.kss.upload;

import android.util.Log;
import cn.kuaipan.android.http.KscHttpResponse;
import cn.kuaipan.android.kss.KssDef;
import com.baidu.platform.comapi.UIMsg;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

/* loaded from: classes.dex */
public class ServerExpect implements KssDef {
    public boolean factoryMode = false;
    public int uploadDelay = -1;
    public long nextChunkSize = -1;

    public static ServerExpect getServerExpect(KscHttpResponse kscHttpResponse) {
        Header firstHeader;
        HttpResponse response = kscHttpResponse == null ? null : kscHttpResponse.getResponse();
        if (response == null) {
            return null;
        }
        ServerExpect serverExpect = new ServerExpect();
        Header firstHeader2 = response.getFirstHeader("X-Factory-Mode");
        boolean z = false;
        boolean z2 = true;
        if (firstHeader2 != null) {
            if (getInt(firstHeader2) == 1) {
                z = true;
            }
            serverExpect.factoryMode = z;
            z = true;
        }
        Header firstHeader3 = response.getFirstHeader("X-Upload-Delay");
        if (firstHeader3 != null) {
            serverExpect.uploadDelay = getInt(firstHeader3);
            z = true;
        }
        if (response.getFirstHeader("X-Next-Chunk-Size") != null) {
            serverExpect.nextChunkSize = getInt(firstHeader);
        } else {
            z2 = z;
        }
        if (!z2) {
            return null;
        }
        return serverExpect;
    }

    public void validate() {
        long j = this.nextChunkSize;
        if (j >= 0) {
            long j2 = j - (j % 65536);
            this.nextChunkSize = j2;
            long min = Math.min(j2, KssDef.MAX_CHUNKSIZE);
            this.nextChunkSize = min;
            this.nextChunkSize = Math.max(min, 65536L);
        }
        int i = this.uploadDelay;
        if (i <= 0 || this.factoryMode) {
            return;
        }
        this.uploadDelay = Math.min(i, (int) UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
    }

    public static int getInt(Header header) {
        if (header == null) {
            return -1;
        }
        try {
            return Integer.parseInt(header.getValue());
        } catch (NumberFormatException unused) {
            Log.w("ServerExpect", "Failed parser header: " + header);
            return -1;
        }
    }
}
