package cn.wps.kmo.kmoservice_sdk.imageconverterpdf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import cn.wps.kmo.kmoservice_sdk.common.AbsCorTask;
import cn.wps.kmo.kmoservice_sdk.common.TaskData;
import cn.wps.kmo.kmoservice_sdk.common.TaskResult;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfCallback;
import cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfService;
import java.util.List;

/* loaded from: classes.dex */
public class ImageConverterPdfTask extends AbsCorTask {
    public ImageConverterPdfService mImageConverterPdfService;

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void taskFinish(TaskData taskData) {
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsCorTask, cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void init(Context context) {
        super.init(context);
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsCorTask, cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public boolean prepare(TaskData taskData) {
        super.prepare(taskData);
        List<Uri> list = taskData.mImgPaths;
        int i = taskData.mMode;
        if (list == null || list.size() <= 0 || i < 0 || i >= 3) {
            Bundle bundle = new Bundle();
            bundle.putString("MSG", "转换前的文件路径有误");
            SdkUtils.respCallback(taskData.mCallback, new TaskResult(10008, bundle));
            return true;
        } else if (list.size() <= 50) {
            return false;
        } else {
            Bundle bundle2 = new Bundle();
            bundle2.putString("MSG", "图片数量超过最大值");
            SdkUtils.respCallback(taskData.mCallback, new TaskResult(10031, bundle2));
            return true;
        }
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsCorTask, cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public boolean prepareTask(TaskData taskData) {
        return super.prepareTask(taskData);
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsCorTask, cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void startTask(IBinder iBinder, final TaskData taskData) throws RemoteException {
        super.startTask(iBinder, taskData);
        ImageConverterPdfService asInterface = ImageConverterPdfService.Stub.asInterface(iBinder);
        this.mImageConverterPdfService = asInterface;
        asInterface.imageConverterPdf(taskData.mImgPaths, taskData.mMode, new ImageConverterPdfCallback.Stub() { // from class: cn.wps.kmo.kmoservice_sdk.imageconverterpdf.ImageConverterPdfTask.1
            @Override // cn.wps.moffice.service.exposed.imageconverterpdf.ImageConverterPdfCallback
            public void callback(int i, Bundle bundle) {
                SdkUtils.respCallback(taskData.mCallback, new TaskResult(i, bundle));
            }
        });
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void destory() {
        this.mImageConverterPdfService = null;
    }
}
