package com.miui.gallery.signature.core.pen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.signature.core.bean.ControlPoint;
import com.miui.gallery.signature.core.bean.PointElement;
import com.miui.gallery.signature.core.utils.ConvertUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BasePen {
    public Context mContext;
    public ControlPoint mCurrentPoint;
    public double mLastVelocity;
    public float mOriginPenWidth;
    public Paint mPaint;
    public List<PointElement> mOriginPointList = new ArrayList();
    public List<ControlPoint> mPointList = new ArrayList();
    public ControlPoint mLastPoint = new ControlPoint(0.0f, 0.0f);
    public BezierControl mBezierControl = new BezierControl();
    public int mFirstFingerId = 0;

    public abstract void doDown(Canvas canvas);

    public abstract void doDraw(Canvas canvas);

    public abstract void doMove(double d);

    public abstract void doUp(double d);

    public BasePen(Paint paint) {
        setPaint(paint);
    }

    public void onDraw(Canvas canvas) {
        List<ControlPoint> list = this.mPointList;
        if (list == null || list.size() < 1) {
            return;
        }
        doDraw(canvas);
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void onTouchEvent(MotionEvent motionEvent, Canvas canvas) {
        int action = motionEvent.getAction() & motionEvent.getActionMasked();
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        PointElement createPointElement = createPointElement(obtain);
        this.mOriginPointList.add(createPointElement);
        if (action == 0) {
            this.mFirstFingerId = obtain.getPointerId(0);
            onDown(createPointElement, canvas);
        } else if (action != 1) {
            if (action != 2 || this.mFirstFingerId != obtain.getPointerId(obtain.getActionIndex())) {
                return;
            }
            onMove(createPointElement);
        } else if (this.mFirstFingerId == obtain.getPointerId(obtain.getActionIndex())) {
            onUp(createPointElement, canvas);
        } else {
            onDraw(canvas);
            clear();
        }
    }

    public final void onDown(PointElement pointElement, Canvas canvas) {
        this.mPointList.clear();
        ControlPoint controlPoint = new ControlPoint(pointElement.x, pointElement.y, this.mOriginPenWidth);
        controlPoint.time = pointElement.tempStamp;
        this.mCurrentPoint = controlPoint;
        this.mLastPoint = controlPoint;
        this.mBezierControl.init(controlPoint, controlPoint);
        this.mLastVelocity = SearchStatUtils.POW;
        doDown(canvas);
    }

    public final void onMove(PointElement pointElement) {
        if (pointElement.tempStamp - this.mLastPoint.time < 15) {
            return;
        }
        ControlPoint controlPoint = new ControlPoint(pointElement.x, pointElement.y);
        controlPoint.time = pointElement.tempStamp;
        float f = controlPoint.x;
        ControlPoint controlPoint2 = this.mLastPoint;
        double hypot = Math.hypot(f - controlPoint2.x, controlPoint.y - controlPoint2.y);
        controlPoint.width = (float) getCurrentWidth(hypot, pointElement.tempStamp);
        this.mBezierControl.addNode(controlPoint);
        doMove(hypot);
        this.mLastPoint = controlPoint;
    }

    public final double getCurrentWidth(double d, long j) {
        double currentVel = getCurrentVel(d, j);
        this.mLastVelocity = currentVel;
        return Math.max(this.mOriginPenWidth * Math.exp((-currentVel) * 0.1d), 4.0d);
    }

    public final double getCurrentVel(double d, long j) {
        double d2 = ((d / (j - this.mLastPoint.time)) * 0.8799999952316284d) + (this.mLastVelocity * 0.12000000476837158d);
        Log.d("BasePen", "getCurrentVel: " + d2);
        return d2;
    }

    public final void onUp(PointElement pointElement, Canvas canvas) {
        if (this.mOriginPointList.size() > 3 && this.mPointList.size() > 3) {
            calculateAddPoint(pointElement);
        }
        onDraw(canvas);
        clear();
    }

    public final void calculateAddPoint(PointElement pointElement) {
        ControlPoint controlPoint = new ControlPoint(pointElement.x, pointElement.y);
        PointElement pointElement2 = new PointElement();
        PointElement pointElement3 = new PointElement();
        for (int size = this.mOriginPointList.size() - 1; size >= 1; size--) {
            pointElement2 = this.mOriginPointList.get(size);
            pointElement3 = this.mOriginPointList.get(size - 1);
            if (pointElement2.x != pointElement3.x || pointElement2.y != pointElement3.y) {
                break;
            }
        }
        float abs = Math.abs(pointElement2.x - pointElement3.x);
        float abs2 = Math.abs(pointElement2.y - pointElement3.y);
        float dp2px = ConvertUtils.dp2px(this.mContext, 7);
        if (this.mLastPoint.width > this.mOriginPenWidth * 0.5f) {
            dp2px *= 0.5f;
        }
        double hypot = Math.hypot(abs, abs2);
        float f = (float) ((abs * dp2px) / hypot);
        float f2 = (float) ((abs2 * dp2px) / hypot);
        float f3 = pointElement2.y;
        float f4 = pointElement3.y;
        if (f3 == f4) {
            float f5 = pointElement2.x;
            float f6 = pointElement3.x;
            if (f5 > f6) {
                controlPoint.x += f;
            } else if (f5 < f6) {
                controlPoint.x -= f;
            }
        } else {
            float f7 = pointElement2.x;
            float f8 = pointElement3.x;
            if (f7 == f8) {
                if (f3 > f4) {
                    controlPoint.y += f;
                } else if (f7 < f8) {
                    controlPoint.y -= f;
                }
            } else if (f7 > f8) {
                if (f3 < f4) {
                    controlPoint.x += f;
                    controlPoint.y -= f2;
                } else {
                    controlPoint.x += f;
                    controlPoint.y += f2;
                }
            } else if (f7 < f8 && f3 < f4) {
                if (f3 > f4) {
                    controlPoint.x -= f;
                    controlPoint.y += f2;
                } else {
                    controlPoint.x -= f;
                    controlPoint.y -= f2;
                }
            }
        }
        controlPoint.width = Math.max(this.mLastPoint.width * 0.2f, 2.0f);
        this.mBezierControl.addNodeUp(controlPoint);
        doUp(dp2px);
    }

    public void clear() {
        this.mPointList.clear();
    }

    public final PointElement createPointElement(MotionEvent motionEvent) {
        return new PointElement(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getEventTime());
    }

    public void setPaint(Paint paint) {
        if (paint == null) {
            paint = new Paint();
        }
        this.mPaint = paint;
        this.mOriginPenWidth = paint.getStrokeWidth();
    }
}
