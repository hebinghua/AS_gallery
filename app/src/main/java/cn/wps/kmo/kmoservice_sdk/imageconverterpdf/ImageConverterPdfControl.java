package cn.wps.kmo.kmoservice_sdk.imageconverterpdf;

import android.net.Uri;
import cn.wps.kmo.kmoservice_sdk.common.AbsTaskControl;
import cn.wps.kmo.kmoservice_sdk.common.ICorTask;
import cn.wps.kmo.kmoservice_sdk.common.TaskData;
import cn.wps.kmo.kmoservice_sdk.utils.SdkUtils;
import cn.wps.kmo.kmoservice_sdk.utils.TypeUtils;
import java.util.List;

/* loaded from: classes.dex */
public class ImageConverterPdfControl extends AbsTaskControl {
    public static ImageConverterPdfControl mImageConverterPdfControl;
    public ImageConverterPdfTask mImageConverterPdfTask;

    public static synchronized ImageConverterPdfControl getInstance() {
        ImageConverterPdfControl imageConverterPdfControl;
        synchronized (ImageConverterPdfControl.class) {
            if (mImageConverterPdfControl == null) {
                mImageConverterPdfControl = new ImageConverterPdfControl();
            }
            imageConverterPdfControl = mImageConverterPdfControl;
        }
        return imageConverterPdfControl;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsTaskControl
    public String getApptype() {
        return TypeUtils.KMO_LITE;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsTaskControl
    public String getTaskName() {
        return TypeUtils.IMAGE_CONVERTER_PDF;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.AbsTaskControl
    public ICorTask getCorTask() {
        ImageConverterPdfTask imageConverterPdfTask = new ImageConverterPdfTask();
        this.mImageConverterPdfTask = imageConverterPdfTask;
        return imageConverterPdfTask;
    }

    public void startTask(List<Uri> list, int i, SdkUtils.ICallback iCallback) {
        super.startTask(new TaskData(list, i, iCallback));
    }
}
