package com.miui.gallery.projection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class DeviceListController implements DialogInterface.OnClickListener {
    public static String MOBILE_NAME;
    public WeakReference<Activity> mActivityWeakReference;
    public DeviceSelectorAdapter mDeviceAdapter;
    public AlertDialog mDeviceListDialog;
    public AlertDialog mDeviceNotFoundDialog;
    public ProgressDialog mDeviceScanDialog;
    public OnItemClickListener mItemClickListener;
    public int mActiveDeviceIndex = -1;
    public AtomicBoolean mNeedShowDeviceList = new AtomicBoolean(false);
    public Runnable mScanTimeoutRunnable = new Runnable() { // from class: com.miui.gallery.projection.DeviceListController.5
        {
            DeviceListController.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            DeviceListController deviceListController = DeviceListController.this;
            deviceListController.dismissSafely(deviceListController.mDeviceScanDialog);
            if (!DeviceListController.this.hasAirkanDevice()) {
                DeviceListController.this.showDeviceNotFoundDialog();
            }
        }
    };
    public ArrayList<String> mDeviceList = new ArrayList<>();

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClicked(String str);
    }

    public static /* synthetic */ void $r8$lambda$85DtRzuPaEbMTjWp9zgpo7RsKxw(DeviceListController deviceListController, DialogInterface dialogInterface) {
        deviceListController.lambda$showDeviceNotFoundDialog$0(dialogInterface);
    }

    public void attachActivity(WeakReference<Activity> weakReference) {
        if (weakReference == null || weakReference.get() == null) {
            DefaultLogger.e("projection_device_list", "null activity for device list controller");
            return;
        }
        this.mActivityWeakReference = weakReference;
        this.mDeviceAdapter = null;
        this.mDeviceListDialog = null;
        this.mDeviceScanDialog = null;
        this.mDeviceNotFoundDialog = null;
        MOBILE_NAME = weakReference.get().getString(R.string.projection_local_mobile);
    }

    public void detachActivity(WeakReference<Activity> weakReference) {
        if (this.mActivityWeakReference == null) {
            return;
        }
        if (weakReference == null || weakReference.get() == null || !this.mActivityWeakReference.get().equals(weakReference.get())) {
            DefaultLogger.e("projection_device_list", "null activity for device list controller");
            return;
        }
        this.mActivityWeakReference = null;
        dismiss();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public synchronized boolean hasAirkanDevice() {
        return this.mDeviceList.size() > 0;
    }

    public synchronized void addNewDevice(String str, boolean z) {
        ThreadManager.getMainHandler().removeCallbacks(this.mScanTimeoutRunnable);
        if (this.mDeviceList.contains(str)) {
            return;
        }
        this.mDeviceList.add(str);
        ProgressDialog progressDialog = this.mDeviceScanDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mDeviceScanDialog.dismiss();
        }
        if (z) {
            this.mActiveDeviceIndex = this.mDeviceList.size() - 1;
        }
        invalidate();
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0019, code lost:
        r4.mDeviceList.remove(r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0020, code lost:
        if (r4.mActiveDeviceIndex != r2) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0022, code lost:
        r4.mActiveDeviceIndex = -1;
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0027, code lost:
        invalidate();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized boolean removeDevice(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.ArrayList<java.lang.String> r0 = r4.mDeviceList     // Catch: java.lang.Throwable -> L30
            int r0 = r0.size()     // Catch: java.lang.Throwable -> L30
            r1 = 0
            r2 = r1
        L9:
            if (r2 >= r0) goto L2e
            java.util.ArrayList<java.lang.String> r3 = r4.mDeviceList     // Catch: java.lang.Throwable -> L30
            java.lang.Object r3 = r3.get(r2)     // Catch: java.lang.Throwable -> L30
            java.lang.String r3 = (java.lang.String) r3     // Catch: java.lang.Throwable -> L30
            boolean r3 = r3.equals(r5)     // Catch: java.lang.Throwable -> L30
            if (r3 == 0) goto L2b
            java.util.ArrayList<java.lang.String> r0 = r4.mDeviceList     // Catch: java.lang.Throwable -> L30
            r0.remove(r5)     // Catch: java.lang.Throwable -> L30
            int r5 = r4.mActiveDeviceIndex     // Catch: java.lang.Throwable -> L30
            if (r5 != r2) goto L27
            r5 = -1
            r4.mActiveDeviceIndex = r5     // Catch: java.lang.Throwable -> L30
            r5 = 1
            r1 = r5
        L27:
            r4.invalidate()     // Catch: java.lang.Throwable -> L30
            goto L2e
        L2b:
            int r2 = r2 + 1
            goto L9
        L2e:
            monitor-exit(r4)
            return r1
        L30:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.projection.DeviceListController.removeDevice(java.lang.String):boolean");
    }

    public void removeActive() {
        if (this.mActiveDeviceIndex != -1) {
            DefaultLogger.v("projection_device_list", "~~~~remove the active for device");
            this.mActiveDeviceIndex = -1;
        }
    }

    public final boolean activityAlive() {
        WeakReference<Activity> weakReference = this.mActivityWeakReference;
        return weakReference != null && weakReference.get() != null && !this.mActivityWeakReference.get().isDestroyed() && !this.mActivityWeakReference.get().isFinishing();
    }

    public final void showDeviceListDialog() {
        if (!activityAlive()) {
            return;
        }
        AlertDialog alertDialog = this.mDeviceListDialog;
        int i = R.string.projection_stop;
        if (alertDialog == null) {
            if (this.mDeviceAdapter == null) {
                this.mDeviceAdapter = new DeviceSelectorAdapter(this.mActivityWeakReference.get(), this.mDeviceList);
            }
            AlertDialog.Builder onCancelListener = new AlertDialog.Builder(this.mActivityWeakReference.get()).setTitle(R.string.projection_multi_title).setSingleChoiceItems(this.mDeviceAdapter, this.mActiveDeviceIndex, this).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.projection.DeviceListController.2
                {
                    DeviceListController.this = this;
                }

                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    DeviceListController.this.mNeedShowDeviceList.set(false);
                }
            });
            if (!isActive()) {
                i = R.string.projection_cancel;
            }
            this.mDeviceListDialog = onCancelListener.setNegativeButton(i, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.projection.DeviceListController.1
                {
                    DeviceListController.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    if (DeviceListController.this.isActive()) {
                        DeviceListController.this.switchTo(-1);
                    }
                }
            }).create();
        } else {
            Button button = alertDialog.getButton(-2);
            if (!isActive()) {
                i = R.string.projection_cancel;
            }
            button.setText(i);
        }
        if (!this.mDeviceListDialog.isShowing()) {
            this.mDeviceListDialog.show();
        }
        ListView listView = this.mDeviceListDialog.getListView();
        listView.setItemChecked(this.mActiveDeviceIndex, true);
        listView.setSelection(this.mActiveDeviceIndex);
    }

    public final boolean isActive() {
        return this.mActiveDeviceIndex != -1;
    }

    public final void showDeviceNotFoundDialog() {
        if (!activityAlive()) {
            return;
        }
        if (this.mDeviceNotFoundDialog == null) {
            this.mDeviceNotFoundDialog = new AlertDialog.Builder(this.mActivityWeakReference.get()).setTitle(R.string.cast_devices_unavailable_title).setMessage(R.string.cast_devices_unavailable_desc).setCancelable(true).setPositiveButton(R.string.have_known, (DialogInterface.OnClickListener) null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.projection.DeviceListController$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    DeviceListController.$r8$lambda$85DtRzuPaEbMTjWp9zgpo7RsKxw(DeviceListController.this, dialogInterface);
                }
            }).create();
        }
        this.mDeviceNotFoundDialog.show();
    }

    public /* synthetic */ void lambda$showDeviceNotFoundDialog$0(DialogInterface dialogInterface) {
        this.mNeedShowDeviceList.set(false);
    }

    public synchronized void show() {
        dismiss();
        this.mNeedShowDeviceList.set(true);
        if (!BaseMiscUtil.isValid(this.mDeviceList)) {
            showScanDialog();
        } else {
            showDeviceListDialog();
        }
    }

    public final void showScanDialog() {
        if (!activityAlive()) {
            return;
        }
        if (this.mDeviceScanDialog == null) {
            ProgressDialog progressDialog = new ProgressDialog(this.mActivityWeakReference.get());
            this.mDeviceScanDialog = progressDialog;
            progressDialog.setIndeterminate(true);
            this.mDeviceScanDialog.setMessage(this.mActivityWeakReference.get().getString(R.string.searching_cast_devices));
            this.mDeviceScanDialog.setCancelable(true);
            this.mDeviceScanDialog.setCanceledOnTouchOutside(false);
            this.mDeviceScanDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.projection.DeviceListController.3
                {
                    DeviceListController.this = this;
                }

                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    ThreadManager.getMainHandler().removeCallbacks(DeviceListController.this.mScanTimeoutRunnable);
                    DeviceListController.this.mNeedShowDeviceList.set(false);
                }
            });
        }
        this.mDeviceScanDialog.show();
        ThreadManager.getMainHandler().removeCallbacks(this.mScanTimeoutRunnable);
        ThreadManager.getMainHandler().postDelayed(this.mScanTimeoutRunnable, 5000L);
    }

    public synchronized void dismiss() {
        ThreadManager.getMainHandler().removeCallbacks(this.mScanTimeoutRunnable);
        this.mNeedShowDeviceList.set(false);
        dismissSafely(this.mDeviceScanDialog);
        dismissSafely(this.mDeviceListDialog);
        dismissSafely(this.mDeviceNotFoundDialog);
    }

    public final void invalidate() {
        ThreadManager.getMainHandler().removeCallbacks(this.mScanTimeoutRunnable);
        ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.projection.DeviceListController.4
            {
                DeviceListController.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (DeviceListController.this.mDeviceAdapter != null) {
                    DeviceListController.this.mDeviceAdapter.notifyDataSetChanged();
                }
                if (DeviceListController.this.mNeedShowDeviceList.get()) {
                    if (DeviceListController.this.hasAirkanDevice()) {
                        DeviceListController deviceListController = DeviceListController.this;
                        deviceListController.dismissSafely(deviceListController.mDeviceScanDialog);
                        DeviceListController deviceListController2 = DeviceListController.this;
                        deviceListController2.dismissSafely(deviceListController2.mDeviceNotFoundDialog);
                        DeviceListController.this.showDeviceListDialog();
                        return;
                    }
                    DeviceListController deviceListController3 = DeviceListController.this;
                    deviceListController3.dismissSafely(deviceListController3.mDeviceListDialog);
                    if (DeviceListController.this.mDeviceNotFoundDialog != null && DeviceListController.this.mDeviceNotFoundDialog.isShowing()) {
                        return;
                    }
                    DeviceListController.this.showDeviceNotFoundDialog();
                }
            }
        });
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        switchTo(i);
    }

    public final synchronized void switchTo(int i) {
        if (this.mItemClickListener == null) {
            return;
        }
        if (i >= this.mDeviceList.size()) {
            dismissSafely(this.mDeviceListDialog);
            return;
        }
        this.mActiveDeviceIndex = i;
        this.mItemClickListener.onItemClicked(i == -1 ? MOBILE_NAME : this.mDeviceList.get(i));
    }

    public final void dismissSafely(Dialog dialog) {
        if (dialog == null || !dialog.isShowing()) {
            return;
        }
        dialog.dismiss();
    }

    /* loaded from: classes2.dex */
    public static class DeviceSelectorAdapter extends BaseAdapter {
        public ArrayList<String> mDeviceList;
        public LayoutInflater mLayoutInflater;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public DeviceSelectorAdapter(Context context, ArrayList<String> arrayList) {
            this.mDeviceList = arrayList;
            this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        @Override // android.widget.Adapter
        public int getCount() {
            ArrayList<String> arrayList = this.mDeviceList;
            if (arrayList != null) {
                return arrayList.size();
            }
            return 0;
        }

        @Override // android.widget.Adapter
        public String getItem(int i) {
            ArrayList<String> arrayList = this.mDeviceList;
            if (arrayList == null || i >= arrayList.size()) {
                return null;
            }
            return this.mDeviceList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mLayoutInflater.inflate(R.layout.miuix_appcompat_select_dialog_singlechoice, viewGroup, false);
            }
            ((TextView) view.findViewById(16908308)).setText(getItem(i));
            return view;
        }
    }
}
