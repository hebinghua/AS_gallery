package miuix.androidbasewidget.widget;

/* loaded from: classes3.dex */
public class SpringOperator {
    public final double damping;
    public final double tension;

    public SpringOperator(float f, float f2) {
        double d = f2;
        this.tension = Math.pow(6.283185307179586d / d, 2.0d);
        this.damping = (f * 12.566370614359172d) / d;
    }

    public double updateVelocity(double d, float f, double d2, double d3) {
        double d4;
        return (d * (1.0d - (this.damping * f))) + ((float) (this.tension * (d2 - d3) * d4));
    }
}
