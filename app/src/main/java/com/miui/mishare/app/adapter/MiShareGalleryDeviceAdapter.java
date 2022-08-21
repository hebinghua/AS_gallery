package com.miui.mishare.app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.mishare.MiShareDeviceModel;
import com.miui.mishare.MiShareTask;
import com.miui.mishare.R$id;
import com.miui.mishare.RemoteDevice;
import com.miui.mishare.app.model2.MiShareDevice;
import com.miui.mishare.app.view.MiShareGalleryDeviceView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes3.dex */
public class MiShareGalleryDeviceAdapter extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
    public static Pair<Long, String> sLastSendDevice;
    public OnDeviceClickListener mListener;
    public boolean mStopSort;
    public final Handler mSortHandler = new Handler();
    public final DeviceRssiComparator mComparator = new DeviceRssiComparator();
    public boolean mIsInitialAddDevice = true;
    public final Runnable mSortRunnable = new SortRunnable(this);
    public final Handler mHandler = new IdleHandler(this);
    public final List<MiShareDevice> mDevices = new ArrayList();

    /* loaded from: classes3.dex */
    public interface OnDeviceClickListener {
        void onCancelTask(MiShareDevice miShareDevice);

        void onDeviceTaskRetry(MiShareDevice miShareDevice);

        void onDeviceTaskStart(MiShareDevice miShareDevice);
    }

    public static void setLastSentDevice(Pair<Long, String> pair) {
        sLastSendDevice = pair;
    }

    /* loaded from: classes3.dex */
    public static class SortRunnable implements Runnable {
        public final WeakReference<MiShareGalleryDeviceAdapter> mAdapter;

        public SortRunnable(MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter) {
            this.mAdapter = new WeakReference<>(miShareGalleryDeviceAdapter);
        }

        @Override // java.lang.Runnable
        public void run() {
            MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter = this.mAdapter.get();
            if (miShareGalleryDeviceAdapter != null) {
                miShareGalleryDeviceAdapter.runSort();
            }
        }
    }

    public final void runSort() {
        List<MiShareDevice> list;
        if (this.mStopSort || (list = this.mDevices) == null || list.isEmpty()) {
            return;
        }
        Pair<Long, String> pair = sLastSendDevice;
        if (pair != null && ((Long) pair.first).longValue() - System.currentTimeMillis() >= 300000) {
            setLastSentDevice(null);
        }
        List<MiShareDevice> storeDevice = storeDevice(this.mDevices);
        Collections.sort(this.mDevices, this.mComparator);
        if (isNeedNotify(storeDevice, this.mDevices)) {
            notifyDataSetChanged();
        }
        startSort();
    }

    public final boolean isEnableTalkBack(Context context) {
        String string;
        return (context == null || (string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services")) == null || !string.contains("com.google.android.marvin.talkback/com.google.android.marvin.talkback.TalkBackService")) ? false : true;
    }

    public final List<MiShareDevice> storeDevice(List<MiShareDevice> list) {
        ArrayList arrayList = new ArrayList();
        for (MiShareDevice miShareDevice : list) {
            if (miShareDevice != null) {
                arrayList.add(miShareDevice);
            }
        }
        return arrayList;
    }

    public final boolean isNeedNotify(List<MiShareDevice> list, List<MiShareDevice> list2) {
        int size = list.size();
        int size2 = list2.size();
        if (size != size2) {
            return true;
        }
        for (int i = 0; i < size2; i++) {
            MiShareDevice miShareDevice = list2.get(i);
            MiShareDevice miShareDevice2 = list.get(i);
            if (miShareDevice == null || miShareDevice2 == null || !TextUtils.equals(miShareDevice.deviceId, miShareDevice2.deviceId) || !TextUtils.equals(list.get(0).deviceName, list2.get(0).deviceName)) {
                return true;
            }
        }
        return false;
    }

    public MiShareGalleryDeviceAdapter() {
        setHasStableIds(true);
    }

    public MiShareDevice getItem(int i) {
        if (i < 0 || i >= getItemCount()) {
            return null;
        }
        return this.mDevices.get(i);
    }

    public void registerListener(OnDeviceClickListener onDeviceClickListener) {
        this.mListener = onDeviceClickListener;
    }

    public final void startSort() {
        this.mStopSort = false;
        this.mSortHandler.postDelayed(this.mSortRunnable, 2000L);
    }

    public void stopSort() {
        this.mStopSort = true;
        this.mSortHandler.removeCallbacks(this.mSortRunnable);
    }

    public void setInitialAddDevice() {
        this.mIsInitialAddDevice = true;
    }

    public void setDeviceState(String str, int i, boolean z, float f) {
        for (MiShareDevice miShareDevice : this.mDevices) {
            if (miShareDevice != null && miShareDevice.remoteDevice != null && TextUtils.equals(str, miShareDevice.taskId)) {
                int i2 = miShareDevice.deviceStatus;
                if ((i2 == 2 || i2 == 5) && i == 1) {
                    return;
                }
                if (i2 == 4) {
                    setLastSentDevice(new Pair(Long.valueOf(System.currentTimeMillis()), miShareDevice.deviceId));
                    Collections.sort(this.mDevices, this.mComparator);
                }
                miShareDevice.deviceStatus = i;
                miShareDevice.showProgress = z;
                miShareDevice.progressPercent = f;
                notifyDataSetChanged();
                delayIdle(str, i);
                return;
            }
        }
    }

    public final void delayIdle(String str, int i) {
        if (i == 4 || i == 3) {
            Message message = new Message();
            message.what = 1;
            message.obj = str;
            this.mHandler.sendMessageDelayed(message, 3000L);
        }
    }

    public void clear() {
        this.mDevices.clear();
        notifyDataSetChanged();
    }

    public final boolean updateDevice(MiShareDevice miShareDevice) {
        for (int i = 0; i < this.mDevices.size(); i++) {
            MiShareDevice miShareDevice2 = this.mDevices.get(i);
            if (TextUtils.equals(miShareDevice2.deviceId, miShareDevice.deviceId)) {
                miShareDevice.deviceStatus = miShareDevice2.deviceStatus;
                miShareDevice.taskId = miShareDevice2.taskId;
                this.mDevices.set(i, miShareDevice);
                if (!TextUtils.equals(miShareDevice2.deviceName, miShareDevice.deviceName)) {
                    notifyDataSetChanged();
                }
                if (MiShareDevice.checkUwbHitUpdate(miShareDevice2.isUwbHit, miShareDevice.isUwbHit)) {
                    notifyDataSetChanged();
                }
                if (!MiShareDevice.checkGlobalDeviceUpdate(miShareDevice2.isGlobalDevice, miShareDevice.isGlobalDevice)) {
                    return true;
                }
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public void addOrUpdateDevice(MiShareDevice miShareDevice, Context context) {
        if (miShareDevice == null) {
            return;
        }
        if (!updateDevice(miShareDevice)) {
            this.mDevices.add(miShareDevice);
            notifyDataSetChanged();
        }
        if (!this.mIsInitialAddDevice || isEnableTalkBack(context)) {
            return;
        }
        this.mIsInitialAddDevice = false;
        startSort();
    }

    public void replaceTaskId(MiShareTask miShareTask) {
        if (miShareTask == null || miShareTask.device == null) {
            return;
        }
        for (int i = 0; i < this.mDevices.size(); i++) {
            MiShareDevice miShareDevice = this.mDevices.get(i);
            if (TextUtils.equals(miShareDevice.deviceId, miShareTask.device.getDeviceId())) {
                miShareDevice.taskId = miShareTask.taskId;
                return;
            }
        }
    }

    public void removeDevice(String str) {
        for (int i = 0; i < this.mDevices.size(); i++) {
            if (TextUtils.equals(this.mDevices.get(i).deviceId, str)) {
                this.mDevices.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public MiShareDevice getDeviceById(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (MiShareDevice miShareDevice : this.mDevices) {
            if (miShareDevice != null && TextUtils.equals(miShareDevice.deviceId, str)) {
                return miShareDevice;
            }
        }
        return null;
    }

    public boolean devicesEmpty() {
        List<MiShareDevice> list = this.mDevices;
        return list == null || list.size() == 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(new MiShareGalleryDeviceView(viewGroup.getContext()));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MiShareDevice item = getItem(i);
        if (item != null) {
            viewHolder.mDeviceView.setIsGloabal(item.isGlobalDevice);
            RemoteDevice remoteDevice = item.remoteDevice;
            boolean z = false;
            boolean z2 = (remoteDevice == null || remoteDevice.getExtras() == null || !item.remoteDevice.getExtras().getBoolean("nickname_has_more")) ? false : true;
            viewHolder.mDeviceView.setDeviceType(item.deviceType);
            viewHolder.mDeviceView.setDeviceName(item.deviceName, z2);
            boolean isPC = item.isPC();
            boolean isPad = item.isPad();
            if (isPC) {
                if (item.isGlobalDevice) {
                    viewHolder.mDeviceView.setDeviceStyle(4);
                    viewHolder.mDeviceView.setGlobalDeviceName(4);
                } else {
                    viewHolder.mDeviceView.setDeviceStyle(2);
                }
            } else if (isPad) {
                if (item.isGlobalDevice) {
                    viewHolder.mDeviceView.setDeviceStyle(5);
                    viewHolder.mDeviceView.setGlobalDeviceName(5);
                } else {
                    viewHolder.mDeviceView.setDeviceStyle(3);
                }
            } else if (item.isGlobalDevice) {
                viewHolder.mDeviceView.setDeviceStyle(6);
                viewHolder.mDeviceView.setGlobalDeviceName(6);
            } else {
                viewHolder.mDeviceView.setDeviceStyle(1);
            }
            viewHolder.mDeviceView.setProgressPercent(item.progressPercent);
            viewHolder.mDeviceView.setDeviceStatus(item);
            if (MiShareDeviceModel.supportUwb()) {
                MiShareGalleryDeviceView miShareGalleryDeviceView = viewHolder.mDeviceView;
                if (i == 0 && item.isUwbHit && item.deviceStatus == 1) {
                    z = true;
                }
                miShareGalleryDeviceView.toggleNotice(z);
            }
        }
        viewHolder.mDeviceView.getIconView().setTag(R$id.position, Integer.valueOf(i));
        viewHolder.mDeviceView.getIconView().setOnClickListener(this);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        MiShareDevice item = getItem(i);
        if (item == null || TextUtils.isEmpty(item.deviceId)) {
            return 0L;
        }
        return item.deviceId.hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<MiShareDevice> list = this.mDevices;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0021, code lost:
        if (r1 != 5) goto L23;
     */
    @Override // android.view.View.OnClickListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onClick(android.view.View r4) {
        /*
            r3 = this;
            int r0 = com.miui.mishare.R$id.position
            java.lang.Object r0 = r4.getTag(r0)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            com.miui.mishare.app.model2.MiShareDevice r0 = r3.getItem(r0)
            if (r0 == 0) goto L47
            int r1 = r0.deviceStatus
            r2 = 1
            if (r1 == r2) goto L38
            r2 = 2
            if (r1 == r2) goto L34
            r2 = 3
            if (r1 == r2) goto L2c
            r2 = 4
            if (r1 == r2) goto L24
            r2 = 5
            if (r1 == r2) goto L34
            goto L47
        L24:
            com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter$OnDeviceClickListener r1 = r3.mListener
            if (r1 == 0) goto L47
            r1.onDeviceTaskStart(r0)
            goto L47
        L2c:
            com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter$OnDeviceClickListener r1 = r3.mListener
            if (r1 == 0) goto L47
            r1.onDeviceTaskRetry(r0)
            goto L47
        L34:
            r3.showCancelConfirm(r0)
            goto L47
        L38:
            com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter$OnDeviceClickListener r1 = r3.mListener
            if (r1 == 0) goto L47
            java.lang.String r1 = com.miui.mishare.app.model2.MiShareDevice.generatePCTaskId()
            r0.taskId = r1
            com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter$OnDeviceClickListener r1 = r3.mListener
            r1.onDeviceTaskStart(r0)
        L47:
            com.miui.mishare.app.util.HapticUtil.performMeshNormal(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.mishare.app.adapter.MiShareGalleryDeviceAdapter.onClick(android.view.View):void");
    }

    public final void showCancelConfirm(MiShareDevice miShareDevice) {
        RemoteDevice remoteDevice;
        OnDeviceClickListener onDeviceClickListener;
        if (miShareDevice == null || (remoteDevice = miShareDevice.remoteDevice) == null || remoteDevice.getExtras() == null || (onDeviceClickListener = this.mListener) == null) {
            return;
        }
        onDeviceClickListener.onCancelTask(miShareDevice);
    }

    /* loaded from: classes3.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MiShareGalleryDeviceView mDeviceView;

        public ViewHolder(MiShareGalleryDeviceView miShareGalleryDeviceView) {
            super(miShareGalleryDeviceView);
            this.mDeviceView = miShareGalleryDeviceView;
        }
    }

    /* loaded from: classes3.dex */
    public static class IdleHandler extends Handler {
        public final WeakReference<MiShareGalleryDeviceAdapter> mOuter;

        public IdleHandler(MiShareGalleryDeviceAdapter miShareGalleryDeviceAdapter) {
            this.mOuter = new WeakReference<>(miShareGalleryDeviceAdapter);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                String str = (String) message.obj;
                if (this.mOuter.get() == null) {
                    return;
                }
                this.mOuter.get().setDeviceState(str, 1, false, 0.0f);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class DeviceRssiComparator implements Comparator<MiShareDevice> {
        public DeviceRssiComparator() {
        }

        @Override // java.util.Comparator
        public int compare(MiShareDevice miShareDevice, MiShareDevice miShareDevice2) {
            boolean z;
            int i = 0;
            if (miShareDevice == null || miShareDevice2 == null) {
                if (miShareDevice != null) {
                    return -1;
                }
                return miShareDevice2 != null ? 1 : 0;
            } else if (MiShareDeviceModel.supportUwb() && ((z = miShareDevice.isUwbHit) || miShareDevice2.isUwbHit)) {
                if (!miShareDevice2.isUwbHit) {
                    return -1;
                }
                if (z) {
                    return Integer.compare(miShareDevice.uwbRank, miShareDevice2.uwbRank);
                }
                return 1;
            } else {
                boolean z2 = miShareDevice.isGlobalDevice;
                if (!z2 && !miShareDevice2.isGlobalDevice) {
                    if (MiShareGalleryDeviceAdapter.sLastSendDevice != null) {
                        boolean equals = ((String) MiShareGalleryDeviceAdapter.sLastSendDevice.second).equals(miShareDevice.deviceId);
                        boolean equals2 = ((String) MiShareGalleryDeviceAdapter.sLastSendDevice.second).equals(miShareDevice2.deviceId);
                        if (equals || equals2) {
                            if (!equals2) {
                                return -1;
                            }
                            return !equals ? 1 : 0;
                        }
                    }
                    RemoteDevice remoteDevice = miShareDevice.remoteDevice;
                    int i2 = (remoteDevice == null || remoteDevice.getExtras() == null) ? 0 : miShareDevice.remoteDevice.getExtras().getInt("rssi");
                    RemoteDevice remoteDevice2 = miShareDevice2.remoteDevice;
                    if (remoteDevice2 != null && remoteDevice2.getExtras() != null) {
                        i = miShareDevice2.remoteDevice.getExtras().getInt("rssi");
                    }
                    return Integer.compare(i, i2);
                } else if (!miShareDevice2.isGlobalDevice) {
                    return -1;
                } else {
                    return !z2 ? 1 : 0;
                }
            }
        }
    }
}
