package com.baidu.platform.comapi.basestruct;

import java.io.Serializable;

/* loaded from: classes.dex */
public class MapBound implements Serializable {
    public Point leftBottomPt = new Point();
    public Point rightTopPt = new Point();

    public MapBound() {
    }

    public MapBound(int i, int i2, int i3, int i4) {
        setLeftBottomPt(i, i2);
        setRightTopPt(i3, i4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapBound)) {
            return false;
        }
        MapBound mapBound = (MapBound) obj;
        Point point = this.leftBottomPt;
        if (point == null ? mapBound.leftBottomPt != null : !point.equals(mapBound.leftBottomPt)) {
            return false;
        }
        Point point2 = this.rightTopPt;
        Point point3 = mapBound.rightTopPt;
        if (point2 != null) {
            if (point2.equals(point3)) {
                return true;
            }
        } else if (point3 == null) {
            return true;
        }
        return false;
    }

    public Point getCenterPt() {
        return new Point((this.leftBottomPt.getIntX() + this.rightTopPt.getIntX()) / 2, (this.leftBottomPt.getIntY() + this.rightTopPt.getIntY()) / 2);
    }

    public int hashCode() {
        Point point = this.leftBottomPt;
        int i = 0;
        int hashCode = (point != null ? point.hashCode() : 0) * 31;
        Point point2 = this.rightTopPt;
        if (point2 != null) {
            i = point2.hashCode();
        }
        return hashCode + i;
    }

    public void setLeftBottomPt(int i, int i2) {
        this.leftBottomPt.setTo(i, i2);
    }

    public void setLeftBottomPt(Point point) {
        this.leftBottomPt.setTo(point);
    }

    public void setRightTopPt(int i, int i2) {
        this.rightTopPt.setTo(i, i2);
    }

    public void setRightTopPt(Point point) {
        this.rightTopPt.setTo(point);
    }

    public String toQuery() {
        return String.format("(%d,%d;%d,%d)", Integer.valueOf(this.leftBottomPt.getIntX()), Integer.valueOf(this.leftBottomPt.getIntY()), Integer.valueOf(this.rightTopPt.getIntX()), Integer.valueOf(this.rightTopPt.getIntY()));
    }

    public String toString() {
        return "MapBound{leftBottomPt=" + this.leftBottomPt + ", rightTopPt=" + this.rightTopPt + '}';
    }
}
