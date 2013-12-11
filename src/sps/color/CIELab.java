package sps.color;

import com.badlogic.gdx.graphics.Color;

//From http://www.codeproject.com/Articles/19045/Manipulating-colors-in-NET-Part-1#rgb2
public class CIELab implements ColorSpec<CIELab> {
    public static final CIELab Empty = new CIELab(0, 0, 0);

    public static CIELab fromRGB(float red, float green, float blue) {
        CIEXYZ xyz = CIEXYZ.fromRGB(red, green, blue);
        return fromXYZ(xyz.X, xyz.Y, xyz.Z);
    }

    public static CIELab fromXYZ(double x, double y, double z) {
        CIELab lab = CIELab.Empty;

        lab.L = 116.0 * Fxyz(y / CIEXYZ.D65.Y) - 16;
        lab.A = 500.0 * (Fxyz(x / CIEXYZ.D65.X) - Fxyz(y / CIEXYZ.D65.Y));
        lab.B = 200.0 * (Fxyz(y / CIEXYZ.D65.Y) - Fxyz(z / CIEXYZ.D65.Z));

        return lab;
    }

    private static double Fxyz(double t) {
        return ((t > 0.008856) ? Math.pow(t, (1.0 / 3.0)) : (7.787 * t + 16.0 / 116.0));
    }

    public double L;
    public double A;
    public double B;

    public CIELab(double l, double a, double b) {
        this.L = l;
        this.A = a;
        this.B = b;
    }

    public Color toColor() {
        return toXYZ().toColor();
    }

    public ColorSpec average(CIELab target) {
        return new CIELab((L + target.L) / 2f, (A + target.A) / 2f, (B + target.B) / 2f);
    }

    public CIEXYZ toXYZ() {
        double delta = 6.0 / 29.0;

        double fy = (L + 16) / 116.0;
        double fx = fy + (A / 500.0);
        double fz = fy - (B / 200.0);

        return new CIEXYZ(
                (float) ((fx > delta) ? CIEXYZ.D65.X * (fx * fx * fx) : (fx - 16.0 / 116.0) * 3 * (
                        delta * delta) * CIEXYZ.D65.X),
                (float) ((fy > delta) ? CIEXYZ.D65.Y * (fy * fy * fy) : (fy - 16.0 / 116.0) * 3 * (
                        delta * delta) * CIEXYZ.D65.Y),
                (float) ((fz > delta) ? CIEXYZ.D65.Z * (fz * fz * fz) : (fz - 16.0 / 116.0) * 3 * (
                        delta * delta) * CIEXYZ.D65.Z)
        );
    }


}