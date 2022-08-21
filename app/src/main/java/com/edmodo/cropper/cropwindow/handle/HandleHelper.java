package com.edmodo.cropper.cropwindow.handle;

import android.graphics.RectF;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.edmodo.cropper.cropwindow.edge.EdgePair;
import com.edmodo.cropper.util.AspectRatioUtil;

/* loaded from: classes.dex */
public abstract class HandleHelper {
    public EdgePair mActiveEdges;
    public Edge mHorizontalEdge;
    public Edge mVerticalEdge;

    public abstract void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4);

    public HandleHelper(Edge edge, Edge edge2) {
        this.mHorizontalEdge = edge;
        this.mVerticalEdge = edge2;
        this.mActiveEdges = new EdgePair(edge, edge2);
    }

    public void updateCropWindow(float f, float f2, RectF rectF, float f3) {
        EdgePair activeEdges = getActiveEdges();
        Edge edge = activeEdges.primary;
        Edge edge2 = activeEdges.secondary;
        if (edge != null) {
            edge.adjustCoordinate(f, f2, rectF, f3, 1.0f);
        }
        if (edge2 != null) {
            edge2.adjustCoordinate(f, f2, rectF, f3, 1.0f);
        }
    }

    public EdgePair getActiveEdges() {
        return this.mActiveEdges;
    }

    public EdgePair getActiveEdges(float f, float f2, float f3) {
        if (getAspectRatio(f, f2) > f3) {
            EdgePair edgePair = this.mActiveEdges;
            edgePair.primary = this.mVerticalEdge;
            edgePair.secondary = this.mHorizontalEdge;
        } else {
            EdgePair edgePair2 = this.mActiveEdges;
            edgePair2.primary = this.mHorizontalEdge;
            edgePair2.secondary = this.mVerticalEdge;
        }
        return this.mActiveEdges;
    }

    public final float getAspectRatio(float f, float f2) {
        Edge edge = this.mVerticalEdge;
        Edge edge2 = Edge.LEFT;
        float coordinate = edge == edge2 ? f : edge2.getCoordinate();
        Edge edge3 = this.mHorizontalEdge;
        Edge edge4 = Edge.TOP;
        float coordinate2 = edge3 == edge4 ? f2 : edge4.getCoordinate();
        Edge edge5 = this.mVerticalEdge;
        Edge edge6 = Edge.RIGHT;
        if (edge5 != edge6) {
            f = edge6.getCoordinate();
        }
        Edge edge7 = this.mHorizontalEdge;
        Edge edge8 = Edge.BOTTOM;
        if (edge7 != edge8) {
            f2 = edge8.getCoordinate();
        }
        return AspectRatioUtil.calculateAspectRatio(coordinate, coordinate2, f, f2);
    }
}
