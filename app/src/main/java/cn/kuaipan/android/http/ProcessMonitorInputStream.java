package cn.kuaipan.android.http;

import cn.kuaipan.android.exception.KscTransferStopByCallerException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class ProcessMonitorInputStream extends FilterInputStream {
    public long mCurrent;
    public final IKscTransferListener mListener;
    public long mMarkPos;
    public final KscSpeedMonitor mMonitor;
    public final boolean mSendMode;
    public final KssTransferStopper mStopper;

    public ProcessMonitorInputStream(InputStream inputStream, KscSpeedMonitor kscSpeedMonitor, IKscTransferListener iKscTransferListener, KssTransferStopper kssTransferStopper, boolean z) {
        super(inputStream);
        this.mCurrent = 0L;
        this.mMarkPos = 0L;
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
            this.mCurrent += j;
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
        throw new IOException("@ ProcessMonitorInput::process()", new KscTransferStopByCallerException());
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int read = super.read();
        if (read != -1) {
            process(1L);
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = super.read(bArr, i, i2);
        if (read > 0) {
            process(read);
        }
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) throws IOException {
        long skip = super.skip(j);
        if (skip > 0) {
            process(skip);
        }
        return skip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int i) {
        super.mark(i);
        this.mMarkPos = this.mCurrent;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        super.reset();
        long j = this.mMarkPos;
        this.mCurrent = j;
        IKscTransferListener iKscTransferListener = this.mListener;
        if (iKscTransferListener != null) {
            if (this.mSendMode) {
                iKscTransferListener.setSendPos(j);
            } else {
                iKscTransferListener.setReceivePos(j);
            }
        }
    }
}
