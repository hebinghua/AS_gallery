package com.miui.gallery.ui.photodetail.usecase;

import android.util.Pair;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTime;
import com.miui.gallery.util.BaseFileUtils;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class EditPhotoDateTimeAndRecord extends EditPhotoDateTime {
    public long mAlbumId;
    public AbstractAlbumRepository mAlbumRepository;
    public Calendar mSourceCalendar;
    public long mSourceDateTime;
    public Calendar mUpdateCalendar;

    public EditPhotoDateTimeAndRecord(AbstractCloudRepository abstractCloudRepository, long j, long j2) {
        super(abstractCloudRepository);
        this.mAlbumId = j;
        this.mSourceDateTime = j2;
        this.mAlbumRepository = (AbstractAlbumRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.ALBUM_REPOSITORY);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<String> buildUseCaseFlowable(final EditPhotoDateTime.RequestBean requestBean) {
        return super.buildUseCaseFlowable(requestBean).flatMap(new Function<String, Publisher<Pair<String, String>>>() { // from class: com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTimeAndRecord.2
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<Pair<String, String>> mo2564apply(final String str) throws Exception {
                if (Objects.isNull(str)) {
                    throw new IllegalStateException("edit photo datetime failed");
                }
                return EditPhotoDateTimeAndRecord.this.mAlbumRepository.queryAlbumName(EditPhotoDateTimeAndRecord.this.mAlbumId).map(new Function<PageResults<String>, Pair<String, String>>() { // from class: com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTimeAndRecord.2.1
                    @Override // io.reactivex.functions.Function
                    /* renamed from: apply  reason: avoid collision after fix types in other method */
                    public Pair<String, String> mo2564apply(PageResults<String> pageResults) throws Exception {
                        return Pair.create(str, pageResults.getResult());
                    }
                });
            }
        }).map(new Function<Pair<String, String>, String>() { // from class: com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTimeAndRecord.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public String mo2564apply(Pair<String, String> pair) throws Exception {
                SamplingStatHelper.recordCountEvent("photo_detail", "set_photo_datetime", EditPhotoDateTimeAndRecord.this.generateRecordParam((String) pair.second, requestBean));
                return (String) pair.first;
            }
        });
    }

    public final Map<String, String> generateRecordParam(String str, EditPhotoDateTime.RequestBean requestBean) {
        if (this.mSourceCalendar == null) {
            this.mSourceCalendar = Calendar.getInstance();
        }
        if (this.mUpdateCalendar == null) {
            this.mUpdateCalendar = Calendar.getInstance();
        }
        this.mUpdateCalendar.setTimeInMillis(requestBean.getNewTime());
        this.mSourceCalendar.setTimeInMillis(this.mSourceDateTime);
        HashMap hashMap = new HashMap(2);
        if (this.mSourceCalendar.get(1) != this.mUpdateCalendar.get(1)) {
            hashMap.put("edit_date_year", String.valueOf(this.mUpdateCalendar.get(1)));
        }
        if (this.mSourceCalendar.get(2) != this.mUpdateCalendar.get(2)) {
            hashMap.put("edit_date_month", String.valueOf(this.mUpdateCalendar.get(2)));
        }
        if (this.mSourceCalendar.get(5) != this.mUpdateCalendar.get(5)) {
            hashMap.put("edit_date_day", String.valueOf(this.mUpdateCalendar.get(5)));
        }
        if (this.mSourceCalendar.get(11) != this.mUpdateCalendar.get(11)) {
            hashMap.put("edit_date_hour", String.valueOf(this.mUpdateCalendar.get(11)));
        }
        hashMap.put("album_type", str + " | " + BaseFileUtils.getParentFolderPath(requestBean.getPath()));
        return hashMap;
    }
}
