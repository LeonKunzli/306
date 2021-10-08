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

    /**
     * Konstruktor für den Messwert
     * @param absoluterWert
     * @param relativerWert
     */
    public Messwert(double absoluterWert, double relativerWert){
        this.absoluterWert = absoluterWert;
        this.relativerWert = relativerWert;
    }

    /**
     * getter für den absoluten Wert
     * @return
     */
    public double getAbsoluterWert() {
        return absoluterWert;
    }

    /**
     * getter für den relativen Wert
     * @return
     */
    public double getRelativerWert() {
        return relativerWert;
    }

    /**
     * settet den absoluten Wert
     * @param absoluterWert
     */
    public void setAbsoluterWert(double absoluterWert) {
        this.absoluterWert = absoluterWert;
    }

    /**
     * toString Methode für den Messwert
     * @return
     */
    @Override
    public String toString() {
        return "Messwerte{" +
                "absoluterWert=" + absoluterWert +
                ", relativerWert=" + relativerWert +
                '}';
    }
}
