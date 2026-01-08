package com.tayek.util;
public class Umbrella {
    public static void main(String[] args) {
        double tangent=(sunRadius-earthRadius)/dxToSun;
        double angle=Math.atan2(sunRadius-earthRadius,dxToSun);
        //between edge of sun and edge of earth and horizontal to sun from earth.
        System.out.println(
                "angle: "+angle+" radians, "+Math.toDegrees(angle)+" degrees.");
        double required=dxToL1*tangent;
        System.out.println("required radius: "+required+" Miles.");
    }
    double umbrellaRadius=1;
    static final double sunRadius=865_370/2;
    static final double earthRadius=3_958.8/2;
    static final double dxToSun=93_118_000;
    static final double dxToL1=1_000_000;
}
