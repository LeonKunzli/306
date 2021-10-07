package Model;

/**
 * 306
 *
 * @author Arbias imeri
 * @version 1.2
 * @since 05.10.2021
 */
public class Messwert{
    private double absoluterWert;
    private double relativerWert;

    public Messwert(double absoluterWert, double relativerWert){
        this.absoluterWert = absoluterWert;
        this.relativerWert = relativerWert;
    }

    public double getAbsoluterWert() {
        return absoluterWert;
    }

    public double getRelativerWert() {
        return relativerWert;
    }

    public void setAbsoluterWert(double absoluterWert) {
        this.absoluterWert = absoluterWert;
    }

    public void setRelativerWert(double relativerWert) {
        this.relativerWert = relativerWert;
    }

    @Override
    public String toString() {
        return "Messwerte{" +
                "absoluterWert=" + absoluterWert +
                ", relativerWert=" + relativerWert +
                '}';
    }
}
