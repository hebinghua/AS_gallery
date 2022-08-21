package com.miui.gallery.vlog.template.bean.effects;

import com.miui.gallery.vlog.caption.entity.HeaderTailInfoEntity;
import com.miui.gallery.vlog.caption.entity.HeaderTailPermanentEntity;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.template.TemplateProfileUtils;
import com.miui.gallery.vlog.template.bean.ClipInfo;
import com.miui.gallery.vlog.template.bean.FilterBeanProcessed;
import com.miui.gallery.vlog.template.bean.MiTemplateJson;
import com.miui.gallery.vlog.template.bean.Speed;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MiTemplateResEffect {
    public List<ClipInfo> mClipInfoList;
    public MiTemplateJson mMiTemplateJsonBean;
    public String mMusicPath;

    public void clear() {
    }

    public void parseJsonBean(MiTemplateJson miTemplateJson) {
        this.mMiTemplateJsonBean = miTemplateJson;
        this.mClipInfoList = miTemplateJson.getClipList();
        adjustForLowEnd();
        setMusicPath(miTemplateJson);
    }

    public List<ClipInfo> getClipInfoList() {
        return this.mClipInfoList;
    }

    public final void adjustForLowEnd() {
        List<ClipInfo> list = this.mClipInfoList;
        if (list == null || list.size() == 0 || !TemplateProfileUtils.isNeedTransToLowTemplate()) {
            return;
        }
        for (ClipInfo clipInfo : this.mClipInfoList) {
            float f = 0.0f;
            List<Speed> speed = clipInfo.getSpeed();
            if (speed != null) {
                for (Speed speed2 : speed) {
                    float end = speed2.getEnd() - speed2.getStart();
                    float speed0 = speed2.getSpeed0();
                    float speed1 = speed2.getSpeed1();
                    float f2 = TemplateProfileUtils.MAX_SPEED_FOR_LOW_END_TEMPLATE;
                    float f3 = speed0 > f2 ? f2 : speed0;
                    if (speed1 <= f2) {
                        f2 = speed1;
                    }
                    speed2.setSpeed0(f3);
                    speed2.setSpeed1(f2);
                    f += (((speed0 + speed1) * end) / 2.0f) - ((end * (f3 + f2)) / 2.0f);
                }
            }
            clipInfo.setOriDuration(clipInfo.getOriDuration() - f);
            List<Speed> speed4k = clipInfo.getSpeed4k();
            if (speed4k != null) {
                for (Speed speed3 : speed4k) {
                    float speed02 = speed3.getSpeed0();
                    float speed12 = speed3.getSpeed1();
                    float f4 = TemplateProfileUtils.MAX_SPEED_FOR_LOW_END_TEMPLATE;
                    if (speed02 > f4) {
                        speed02 = f4;
                    }
                    if (speed12 > f4) {
                        speed12 = f4;
                    }
                    speed3.setSpeed0(speed02);
                    speed3.setSpeed1(speed12);
                }
            }
        }
    }

    public String getFolderPath() {
        return VlogConfig.TEMPALTE_PATH + File.separator + this.mMiTemplateJsonBean.getName();
    }

    public final void setMusicPath(MiTemplateJson miTemplateJson) {
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.TEMPALTE_PATH);
        String str = File.separator;
        sb.append(str);
        sb.append(miTemplateJson.getName());
        sb.append(str);
        sb.append(miTemplateJson.getMusic());
        this.mMusicPath = sb.toString();
    }

    public String getMusicPath() {
        return this.mMusicPath;
    }

    public void setMusicPath(String str) {
        this.mMusicPath = str;
    }

    public HeaderTailInfoEntity getHeaderTailInfoEntity() {
        MiTemplateJson miTemplateJson = this.mMiTemplateJsonBean;
        if (miTemplateJson == null || miTemplateJson.getHeaderFilter() == null) {
            return null;
        }
        HeaderTailInfoEntity headerTailInfoEntity = new HeaderTailInfoEntity();
        headerTailInfoEntity.setEntity(this.mMiTemplateJsonBean.getHeaderFilter());
        return headerTailInfoEntity;
    }

    public List<HeaderTailPermanentEntity> getPermanentHeaderTailEntityList() {
        MiTemplateJson miTemplateJson = this.mMiTemplateJsonBean;
        if (miTemplateJson == null || miTemplateJson.getHeaderPermanentFilters() == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < this.mMiTemplateJsonBean.getHeaderPermanentFilters().size(); i++) {
            arrayList.add(new HeaderTailPermanentEntity(this.mMiTemplateJsonBean.getHeaderPermanentFilters().get(i)));
        }
        return arrayList;
    }

    public List<FilterBeanProcessed> getTrackFilter() {
        ArrayList arrayList = new ArrayList();
        MiTemplateJson miTemplateJson = this.mMiTemplateJsonBean;
        if (miTemplateJson != null && miTemplateJson.getTrackFilter() != null) {
            for (com.miui.gallery.vlog.template.bean.Filter filter : this.mMiTemplateJsonBean.getTrackFilter()) {
                arrayList.add(getFilterBeanProcessed(filter));
            }
        }
        return arrayList;
    }

    public List<FilterBeanProcessed> getClipFilterList(ClipInfo clipInfo) {
        ArrayList arrayList = new ArrayList();
        if (clipInfo != null && clipInfo.getFilterList() != null) {
            for (com.miui.gallery.vlog.template.bean.Filter filter : clipInfo.getFilterList()) {
                arrayList.add(getFilterBeanProcessed(filter));
            }
        }
        return arrayList;
    }

    public FilterBeanProcessed getFilterBeanProcessed(com.miui.gallery.vlog.template.bean.Filter filter) {
        FilterBeanProcessed filterBeanProcessed = new FilterBeanProcessed();
        if (filter != null) {
            filterBeanProcessed.filterName = filter.getFilterName();
            if (filter.isFilterIsFile()) {
                filterBeanProcessed.paramIsFile = true;
                filterBeanProcessed.filterParam = getPathFromName(filter.getFilterParam());
            } else {
                filterBeanProcessed.paramIsFile = false;
                filterBeanProcessed.filterParam = filter.getFilterParam();
            }
        }
        return filterBeanProcessed;
    }

    public String getFilterBeanParam(com.miui.gallery.vlog.template.bean.Filter filter) {
        if (filter != null) {
            if (filter.isFilterIsFile()) {
                return getPathFromName(filter.getFilterParam());
            }
            return filter.getFilterParam();
        }
        return null;
    }

    public String getFilterBeanName(com.miui.gallery.vlog.template.bean.Filter filter) {
        if (filter != null) {
            return filter.getFilterName();
        }
        return null;
    }

    public final String getPathFromName(String str) {
        if (this.mMiTemplateJsonBean == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(VlogConfig.TEMPALTE_PATH);
        String str2 = File.separator;
        sb.append(str2);
        sb.append(this.mMiTemplateJsonBean.getName());
        sb.append(str2);
        sb.append(str);
        return sb.toString();
    }
}
