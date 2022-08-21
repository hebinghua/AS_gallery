package cn.kuaipan.android.http;

import cn.kuaipan.android.exception.KscTransferStopByCallerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

/* loaded from: classes.dex */
public class ProcessMonitorEntity extends HttpEntityWrapper {
    public final IKscTransferListener mListener;
    public final KscSpeedMonitor mMonitor;
    public boolean mMonitorUsed;
    public final boolean mSendMode;
    public final KssTransferStopper mStopper;

    public ProcessMonitorEntity(HttpEntity httpEntity, KscSpeedMonitor kscSpeedMonitor, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper, boolean z) {
        super(httpEntity);
        this.mMonitor = kscSpeedMonitor;
        this.mListener = iKscTransferListener;
        this.mStopper = kssTransferStopper;
        this.mSendMode = z;
        this.mMonitorUsed = false;
    }

    public InputStream getContent() throws IOException {
        KssTransferStopper kssTransferStopper = this.mStopper;
        if (kssTransferStopper != null && kssTransferStopper.checkStop()) {
            throw new IOException("@ getContent()", new KscTransferStopByCallerException());
        }
        InputStream content = super.getContent();
        if (this.mMonitorUsed) {
            return content;
        }
        IKscTransferListener iKscTransferListener = this.mListener;
        if (iKscTransferListener != null) {
            if (this.mSendMode) {
                iKscTransferListener.setSendTotal(getContentLength());
            } else {
                iKscTransferListener.setReceiveTotal(getContentLength());
            }
        }
        ProcessMonitorInputStream processMonitorInputStream = new ProcessMonitorInputStream(content, this.mMonitor, this.mListener, this.mStopper, this.mSendMode);
        this.mMonitorUsed = true;
        return processMonitorInputStream;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        KssTransferStopper kssTransferStopper = this.mStopper;
        if (kssTransferStopper != null && kssTransferStopper.checkStop()) {
            throw new IOException("@ writeTo()", new KscTransferStopByCallerException());
        }
        if (!this.mMonitorUsed) {
            ProcessMonitorOutputStream processMonitorOutputStream = new ProcessMonitorOutputStream(outputStream, this.mMonitor, this.mListener, this.mStopper, this.mSendMode);
            this.mMonitorUsed = true;
            outputStream = processMonitorOutputStream;
        }
        super.writeTo(outputStream);
    }
}
