package com.edmodo.cropper.cropwindow.handle;

import android.graphics.RectF;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.edmodo.cropper.cropwindow.edge.EdgePair;
import com.edmodo.cropper.util.AspectRatioUtil;

/* JADX WARN: Init of enum BOTTOM can be incorrect */
/* JADX WARN: Init of enum BOTTOM_LEFT can be incorrect */
/* JADX WARN: Init of enum BOTTOM_RIGHT can be incorrect */
/* JADX WARN: Init of enum LEFT can be incorrect */
/* JADX WARN: Init of enum RIGHT can be incorrect */
/* JADX WARN: Init of enum TOP can be incorrect */
/* JADX WARN: Init of enum TOP_LEFT can be incorrect */
/* JADX WARN: Init of enum TOP_RIGHT can be incorrect */
/* loaded from: classes.dex */
public enum Handle {
    TOP_LEFT(new HandleHelper(r2, r3) { // from class: com.edmodo.cropper.cropwindow.handle.CornerHandleHelper
        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            EdgePair activeEdges = getActiveEdges(f, f2, f3);
            Edge edge = activeEdges.primary;
            Edge edge2 = activeEdges.secondary;
            edge.adjustCoordinate(f, f2, rectF, f4, f3);
            edge2.adjustCoordinate(f3);
            if (edge2.isOutsideMargin(rectF, f4)) {
                edge2.snapToRect(rectF);
                edge.adjustCoordinate(f3);
            }
        }
    }),
    TOP_RIGHT(new HandleHelper(r2, r6) { // from class: com.edmodo.cropper.cropwindow.handle.CornerHandleHelper
        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            EdgePair activeEdges = getActiveEdges(f, f2, f3);
            Edge edge = activeEdges.primary;
            Edge edge2 = activeEdges.secondary;
            edge.adjustCoordinate(f, f2, rectF, f4, f3);
            edge2.adjustCoordinate(f3);
            if (edge2.isOutsideMargin(rectF, f4)) {
                edge2.snapToRect(rectF);
                edge.adjustCoordinate(f3);
            }
        }
    }),
    BOTTOM_LEFT(new HandleHelper(r9, r3) { // from class: com.edmodo.cropper.cropwindow.handle.CornerHandleHelper
        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            EdgePair activeEdges = getActiveEdges(f, f2, f3);
            Edge edge = activeEdges.primary;
            Edge edge2 = activeEdges.secondary;
            edge.adjustCoordinate(f, f2, rectF, f4, f3);
            edge2.adjustCoordinate(f3);
            if (edge2.isOutsideMargin(rectF, f4)) {
                edge2.snapToRect(rectF);
                edge.adjustCoordinate(f3);
            }
        }
    }),
    BOTTOM_RIGHT(new HandleHelper(r9, r6) { // from class: com.edmodo.cropper.cropwindow.handle.CornerHandleHelper
        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            EdgePair activeEdges = getActiveEdges(f, f2, f3);
            Edge edge = activeEdges.primary;
            Edge edge2 = activeEdges.secondary;
            edge.adjustCoordinate(f, f2, rectF, f4, f3);
            edge2.adjustCoordinate(f3);
            if (edge2.isOutsideMargin(rectF, f4)) {
                edge2.snapToRect(rectF);
                edge.adjustCoordinate(f3);
            }
        }
    }),
    LEFT(new HandleHelper(r3) { // from class: com.edmodo.cropper.cropwindow.handle.VerticalHandleHelper
        public Edge mEdge;

        {
            super(null, r2);
            this.mEdge = r2;
        }

        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            this.mEdge.adjustCoordinate(f, f2, rectF, f4, f3);
            Edge edge = Edge.TOP;
            float coordinate = edge.getCoordinate();
            Edge edge2 = Edge.BOTTOM;
            float coordinate2 = edge2.getCoordinate();
            float calculateHeight = (AspectRatioUtil.calculateHeight(Edge.getWidth(), f3) - Edge.getHeight()) / 2.0f;
            edge.setCoordinate(coordinate - calculateHeight);
            edge2.setCoordinate(coordinate2 + calculateHeight);
            if (edge.isOutsideMargin(rectF, f4) && !this.mEdge.isNewRectangleOutOfBounds(edge, rectF, f3)) {
                edge2.offset(-edge.snapToRect(rectF));
                this.mEdge.adjustCoordinate(f3);
            }
            if (!edge2.isOutsideMargin(rectF, f4) || this.mEdge.isNewRectangleOutOfBounds(edge2, rectF, f3)) {
                return;
            }
            edge.offset(-edge2.snapToRect(rectF));
            this.mEdge.adjustCoordinate(f3);
        }
    }),
    TOP(new HandleHelper(r2) { // from class: com.edmodo.cropper.cropwindow.handle.HorizontalHandleHelper
        public Edge mEdge;

        {
            super(r2, null);
            this.mEdge = r2;
        }

        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            this.mEdge.adjustCoordinate(f, f2, rectF, f4, f3);
            Edge edge = Edge.LEFT;
            float coordinate = edge.getCoordinate();
            Edge edge2 = Edge.RIGHT;
            float coordinate2 = edge2.getCoordinate();
            float calculateWidth = (AspectRatioUtil.calculateWidth(Edge.getHeight(), f3) - Edge.getWidth()) / 2.0f;
            edge.setCoordinate(coordinate - calculateWidth);
            edge2.setCoordinate(coordinate2 + calculateWidth);
            if (edge.isOutsideMargin(rectF, f4) && !this.mEdge.isNewRectangleOutOfBounds(edge, rectF, f3)) {
                edge2.offset(-edge.snapToRect(rectF));
                this.mEdge.adjustCoordinate(f3);
            }
            if (!edge2.isOutsideMargin(rectF, f4) || this.mEdge.isNewRectangleOutOfBounds(edge2, rectF, f3)) {
                return;
            }
            edge.offset(-edge2.snapToRect(rectF));
            this.mEdge.adjustCoordinate(f3);
        }
    }),
    RIGHT(new HandleHelper(r6) { // from class: com.edmodo.cropper.cropwindow.handle.VerticalHandleHelper
        public Edge mEdge;

        {
            super(null, r2);
            this.mEdge = r2;
        }

        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            this.mEdge.adjustCoordinate(f, f2, rectF, f4, f3);
            Edge edge = Edge.TOP;
            float coordinate = edge.getCoordinate();
            Edge edge2 = Edge.BOTTOM;
            float coordinate2 = edge2.getCoordinate();
            float calculateHeight = (AspectRatioUtil.calculateHeight(Edge.getWidth(), f3) - Edge.getHeight()) / 2.0f;
            edge.setCoordinate(coordinate - calculateHeight);
            edge2.setCoordinate(coordinate2 + calculateHeight);
            if (edge.isOutsideMargin(rectF, f4) && !this.mEdge.isNewRectangleOutOfBounds(edge, rectF, f3)) {
                edge2.offset(-edge.snapToRect(rectF));
                this.mEdge.adjustCoordinate(f3);
            }
            if (!edge2.isOutsideMargin(rectF, f4) || this.mEdge.isNewRectangleOutOfBounds(edge2, rectF, f3)) {
                return;
            }
            edge.offset(-edge2.snapToRect(rectF));
            this.mEdge.adjustCoordinate(f3);
        }
    }),
    BOTTOM(new HandleHelper(r9) { // from class: com.edmodo.cropper.cropwindow.handle.HorizontalHandleHelper
        public Edge mEdge;

        {
            super(r2, null);
            this.mEdge = r2;
        }

        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            this.mEdge.adjustCoordinate(f, f2, rectF, f4, f3);
            Edge edge = Edge.LEFT;
            float coordinate = edge.getCoordinate();
            Edge edge2 = Edge.RIGHT;
            float coordinate2 = edge2.getCoordinate();
            float calculateWidth = (AspectRatioUtil.calculateWidth(Edge.getHeight(), f3) - Edge.getWidth()) / 2.0f;
            edge.setCoordinate(coordinate - calculateWidth);
            edge2.setCoordinate(coordinate2 + calculateWidth);
            if (edge.isOutsideMargin(rectF, f4) && !this.mEdge.isNewRectangleOutOfBounds(edge, rectF, f3)) {
                edge2.offset(-edge.snapToRect(rectF));
                this.mEdge.adjustCoordinate(f3);
            }
            if (!edge2.isOutsideMargin(rectF, f4) || this.mEdge.isNewRectangleOutOfBounds(edge2, rectF, f3)) {
                return;
            }
            edge.offset(-edge2.snapToRect(rectF));
            this.mEdge.adjustCoordinate(f3);
        }
    }),
    CENTER(new HandleHelper() { // from class: com.edmodo.cropper.cropwindow.handle.CenterHandleHelper
        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, RectF rectF, float f3) {
            Edge edge = Edge.LEFT;
            float coordinate = edge.getCoordinate();
            Edge edge2 = Edge.TOP;
            float coordinate2 = edge2.getCoordinate();
            Edge edge3 = Edge.RIGHT;
            float coordinate3 = edge3.getCoordinate();
            Edge edge4 = Edge.BOTTOM;
            float f4 = f - ((coordinate + coordinate3) / 2.0f);
            float coordinate4 = f2 - ((coordinate2 + edge4.getCoordinate()) / 2.0f);
            edge.offset(f4);
            edge2.offset(coordinate4);
            edge3.offset(f4);
            edge4.offset(coordinate4);
            if (edge.isOutsideMargin(rectF, f3)) {
                edge3.offset(edge.snapToRect(rectF));
            } else if (edge3.isOutsideMargin(rectF, f3)) {
                edge.offset(edge3.snapToRect(rectF));
            }
            if (edge2.isOutsideMargin(rectF, f3)) {
                edge4.offset(edge2.snapToRect(rectF));
            } else if (!edge4.isOutsideMargin(rectF, f3)) {
            } else {
                edge2.offset(edge4.snapToRect(rectF));
            }
        }

        @Override // com.edmodo.cropper.cropwindow.handle.HandleHelper
        public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
            updateCropWindow(f, f2, rectF, f4);
        }
    });
    
    private HandleHelper mHelper;

    static {
        final Edge edge = Edge.TOP;
        final Edge edge2 = Edge.LEFT;
        final Edge edge3 = Edge.RIGHT;
        final Edge edge4 = Edge.BOTTOM;
    }

    Handle(HandleHelper handleHelper) {
        this.mHelper = handleHelper;
    }

    public void updateCropWindow(float f, float f2, RectF rectF, float f3) {
        this.mHelper.updateCropWindow(f, f2, rectF, f3);
    }

    public void updateCropWindow(float f, float f2, float f3, RectF rectF, float f4) {
        this.mHelper.updateCropWindow(f, f2, f3, rectF, f4);
    }
}
