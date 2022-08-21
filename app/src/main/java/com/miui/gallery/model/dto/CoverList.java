package com.miui.gallery.model.dto;

import com.miui.gallery.model.dto.BaseAlbumCover;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class CoverList<T extends BaseAlbumCover> {
    public List<T> covers;
    public long id;

    public CoverList(List<T> list) {
        this.covers = list;
    }

    public CoverList() {
    }

    public List<T> getCovers() {
        return this.covers;
    }

    public void setCovers(List<T> list) {
        this.covers = list;
    }

    public long getId() {
        return this.id;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof CoverList)) {
            CoverList coverList = (CoverList) obj;
            if (coverList.getId() != getId()) {
                return false;
            }
            return (this.covers == null && coverList.getCovers() == null) || !(this.covers == null || coverList.getCovers() == null || !this.covers.equals(coverList.getCovers()));
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.id), this.covers);
    }

    public String toString() {
        return "CoverList{id=" + this.id + ", covers=" + this.covers + '}';
    }

    public boolean isValid() {
        List<T> list = this.covers;
        return list != null && !list.isEmpty();
    }
}
