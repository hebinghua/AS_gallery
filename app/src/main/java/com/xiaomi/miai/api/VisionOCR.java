package com.xiaomi.miai.api;

import com.xiaomi.common.Optional;
import com.xiaomi.miai.api.common.Required;
import java.util.List;

/* loaded from: classes3.dex */
public class VisionOCR {

    /* loaded from: classes3.dex */
    public enum OcrOrientation {
        UNKNOWN(-1),
        UP(0),
        LEFT(1),
        RIGHT(2),
        DOWN(3);
        
        private int id;

        OcrOrientation(int i) {
            this.id = i;
        }

        public int getId() {
            return this.id;
        }
    }

    /* loaded from: classes3.dex */
    public static class Line {
        private Optional<Double> angle = Optional.empty();
        @Required
        private List<Point> box;
        @Required
        private String text;

        public Line() {
        }

        public Line(String str, List<Point> list) {
            this.text = str;
            this.box = list;
        }

        @Required
        public Line setText(String str) {
            this.text = str;
            return this;
        }

        @Required
        public String getText() {
            return this.text;
        }

        @Required
        public Line setBox(List<Point> list) {
            this.box = list;
            return this;
        }

        @Required
        public List<Point> getBox() {
            return this.box;
        }

        public Line setAngle(double d) {
            this.angle = Optional.ofNullable(Double.valueOf(d));
            return this;
        }

        public Optional<Double> getAngle() {
            return this.angle;
        }
    }

    /* loaded from: classes3.dex */
    public static class OcrGeneralRequest {
        @Required
        private String image;
        private Optional<Boolean> detect_rotate = Optional.empty();
        private Optional<Boolean> detect_angle = Optional.empty();
        private Optional<Boolean> enhance = Optional.empty();

        public OcrGeneralRequest() {
        }

        public OcrGeneralRequest(String str) {
            this.image = str;
        }

        @Required
        public OcrGeneralRequest setImage(String str) {
            this.image = str;
            return this;
        }

        @Required
        public String getImage() {
            return this.image;
        }

        public OcrGeneralRequest setDetectRotate(boolean z) {
            this.detect_rotate = Optional.ofNullable(Boolean.valueOf(z));
            return this;
        }

        public Optional<Boolean> isDetectRotate() {
            return this.detect_rotate;
        }

        public OcrGeneralRequest setDetectAngle(boolean z) {
            this.detect_angle = Optional.ofNullable(Boolean.valueOf(z));
            return this;
        }

        public Optional<Boolean> isDetectAngle() {
            return this.detect_angle;
        }

        public OcrGeneralRequest setEnhance(boolean z) {
            this.enhance = Optional.ofNullable(Boolean.valueOf(z));
            return this;
        }

        public Optional<Boolean> isEnhance() {
            return this.enhance;
        }
    }

    /* loaded from: classes3.dex */
    public static class OcrGeneralResponse {
        @Required
        private OcrOrientation orientation;
        @Required
        private List<Region> regions;

        public OcrGeneralResponse() {
        }

        public OcrGeneralResponse(OcrOrientation ocrOrientation, List<Region> list) {
            this.orientation = ocrOrientation;
            this.regions = list;
        }

        @Required
        public OcrGeneralResponse setOrientation(OcrOrientation ocrOrientation) {
            this.orientation = ocrOrientation;
            return this;
        }

        @Required
        public OcrOrientation getOrientation() {
            return this.orientation;
        }

        @Required
        public OcrGeneralResponse setRegions(List<Region> list) {
            this.regions = list;
            return this;
        }

        @Required
        public List<Region> getRegions() {
            return this.regions;
        }
    }

    /* loaded from: classes3.dex */
    public static class Point {
        @Required
        private int x;
        @Required
        private int y;

        public Point() {
        }

        public Point(int i, int i2) {
            this.x = i;
            this.y = i2;
        }

        @Required
        public Point setX(int i) {
            this.x = i;
            return this;
        }

        @Required
        public int getX() {
            return this.x;
        }

        @Required
        public Point setY(int i) {
            this.y = i;
            return this;
        }

        @Required
        public int getY() {
            return this.y;
        }
    }

    /* loaded from: classes3.dex */
    public static class Region {
        @Required
        private List<Line> lines;
        @Required
        private List<Point> region_box;

        public Region() {
        }

        public Region(List<Point> list, List<Line> list2) {
            this.region_box = list;
            this.lines = list2;
        }

        @Required
        public Region setRegionBox(List<Point> list) {
            this.region_box = list;
            return this;
        }

        @Required
        public List<Point> getRegionBox() {
            return this.region_box;
        }

        @Required
        public Region setLines(List<Line> list) {
            this.lines = list;
            return this;
        }

        @Required
        public List<Line> getLines() {
            return this.lines;
        }
    }

    /* loaded from: classes3.dex */
    public static class VisionOcrResponse {
        @Required
        private OcrGeneralResponse content;
        @Required
        private int errorCode;
        @Required
        private String errorMsg;

        public VisionOcrResponse() {
        }

        public VisionOcrResponse(int i, String str, OcrGeneralResponse ocrGeneralResponse) {
            this.errorCode = i;
            this.errorMsg = str;
            this.content = ocrGeneralResponse;
        }

        @Required
        public VisionOcrResponse setErrorCode(int i) {
            this.errorCode = i;
            return this;
        }

        @Required
        public int getErrorCode() {
            return this.errorCode;
        }

        @Required
        public VisionOcrResponse setErrorMsg(String str) {
            this.errorMsg = str;
            return this;
        }

        @Required
        public String getErrorMsg() {
            return this.errorMsg;
        }

        @Required
        public VisionOcrResponse setContent(OcrGeneralResponse ocrGeneralResponse) {
            this.content = ocrGeneralResponse;
            return this;
        }

        @Required
        public OcrGeneralResponse getContent() {
            return this.content;
        }
    }
}
