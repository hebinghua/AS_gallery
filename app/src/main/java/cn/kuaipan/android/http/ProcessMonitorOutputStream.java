package cn.kuaipan.android.http;

import cn.kuaipan.android.exception.KscTransferStopByCallerException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class ProcessMonitorOutputStream extends FilterOutputStream {
    public final IKscTransferListener mListener;
    public final KscSpeedMonitor mMonitor;
    public final boolean mSendMode;
    public final KssTransferStopper mStopper;

    public ProcessMonitorOutputStream(OutputStream outputStream, KscSpeedMonitor kscSpeedMonitor, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper, boolean z) {
        super(outputStream);
        this.mMonitor = kscSpeedMonitor;
        this.mListener = iKscTransferListener;
        this.mStopper = kssTransferStopper;
        this.mSendMode = z;
    }

    public final void process(long j) throws IOException {
        KssTransferStopper kssTransferStopper = this.mStopper;
        if (kssTransferStopper == null || !kssTransferStopper.checkStop()) {
            if (j < 0) {
                return;
            }
            KscSpeedMonitor kscSpeedMonitor = this.mMonitor;
            if (kscSpeedMonitor != null) {
                kscSpeedMonitor.recode(j);
            }
            IKscTransferListener iKscTransferListener = this.mListener;
            if (iKscTransferListener == null) {
                return;
            }
            if (this.mSendMode) {
                iKscTransferListener.sended(j);
                return;
            } else {
                iKscTransferListener.received(j);
                return;
            }
        }
        throw new IOException("@ ProcessMonitorOutputStream::process()", new KscTransferStopByCallerException());
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        ((FilterOutputStream) this).out.write(bArr, i, i2);
        process(i2);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        super.write(i);
        process(1L);
    }
}
