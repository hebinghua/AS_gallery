package cn.wps.moffice.service.exposed.imageconverterpdf;

import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface ImageConverterPdfService extends IInterface {
    void imageConverterPdf(List<Uri> list, int i, ImageConverterPdfCallback imageConverterPdfCallback) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ImageConverterPdfService {
        public static ImageConverterPdfService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof ImageConverterPdfService)) {
                return (ImageConverterPdfService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* loaded from: classes.dex */
        public static class Proxy implements ImageConverterPdfService {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfService
            public void imageConverterPdf(List<Uri> list, int i, ImageConverterPdfCallback imageConverterPdfCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfService");
                    obtain.writeTypedList(list);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(imageConverterPdfCallback != null ? imageConverterPdfCallback.asBinder() : null);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
