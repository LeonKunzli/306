package Model;

import java.awt.geom.QuadCurve2D;

/**
 * 306
 *
 * @author Arbias imeri
 * @version 1.2
 * @since 05.10.2021
 */
public class Messwert{
    private double abosluterWert;
    private double relativerWert;

    public Messwert(double abosluterWert, double relativerWert){
        this.abosluterWert = abosluterWert;
        this.relativerWert = relativerWert;
    }

    public double getAbsoluterWert() {
        return abosluterWert;
    }

    public double getRelativerWert() {
        return relativerWert;
    }

    public void setAbosluterWert(double abosluterWert) {
        this.abosluterWert = abosluterWert;
    }

    public void setRelativerWert(double relativerWert) {
        this.relativerWert = relativerWert;
    }

    @Override
    public String toString() {
        return "Messwerte{" +
                "abosluterWert=" + abosluterWert +
                ", relativerWert=" + relativerWert +
                '}';
    }
}
