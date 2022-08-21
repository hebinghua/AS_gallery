package cn.kuaipan.android.http;

import android.util.Log;

/* loaded from: classes.dex */
public interface IKscTransferListener {
    void received(long j);

    void sended(long j);

    void setReceivePos(long j);

    void setReceiveTotal(long j);

    void setSendPos(long j);

    void setSendTotal(long j);

    /* loaded from: classes.dex */
    public static abstract class KscTransferListener implements IKscTransferListener {
        public long sendTotal = -1;
        public long receiveTotal = -1;
        public long sended = 0;
        public long received = 0;

        public abstract void onDataReceived(long j, long j2);

        public abstract void onDataSended(long j, long j2);

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void setSendTotal(long j) {
            this.sendTotal = j;
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void setReceiveTotal(long j) {
            this.receiveTotal = j;
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void sended(long j) {
            long j2 = this.sended + j;
            this.sended = j2;
            try {
                onDataSended(j2, this.sendTotal);
            } catch (Error e) {
                throw e;
            } catch (Throwable th) {
                Log.e("KscTransferListener", "Meet exception in onDataSended()", th);
            }
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void received(long j) {
            long j2 = this.received + j;
            this.received = j2;
            try {
                onDataReceived(j2, this.receiveTotal);
            } catch (Error e) {
                throw e;
            } catch (Throwable th) {
                Log.e("KscTransferListener", "Meet exception in onDataReceived()", th);
            }
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void setSendPos(long j) {
            if (j == this.sended) {
                return;
            }
            this.sended = j;
            onDataSended(j, this.sendTotal);
        }

        @Override // cn.kuaipan.android.http.IKscTransferListener
        public final void setReceivePos(long j) {
            if (j == this.received) {
                return;
            }
            this.received = j;
            onDataReceived(j, this.receiveTotal);
        }
    }
}
