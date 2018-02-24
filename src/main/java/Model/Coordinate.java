/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import static Model.Coordinate.pigreco;
import static Model.Coordinate.raggio_quadratico_medio;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 *
 * @author leo
 */
public class Coordinate {

    double minLatP;
    double minLonP;
    double maxLatP;
    double maxLonP;
    double minLatA;
    double minLonA;
    double maxLatA;
    double maxLonA;

    double latPart;
    double lonPart;
    double latArr;
    double lonArr;
    double distanza;
    double distanzaMax;
    double distanzaMaxMetri;
    public static final double raggio_quadratico_medio = 6372.795477598;
    public static final double pigreco = 3.14159265;

    public static final int DISTANZA_URBANA = 25000;
    public static final int DISTANZA_PROVINCIALE = 100000;

    public static final int DIAMETRO_PIEDI = 1000; //diametro = 150*2=300
    public static final int DIAMETRO_MEDIO = 10000;
    public static final int DIAMETRO_GRANDE = 25000;

    int id;

    public Coordinate(double latPart, double lonPart, double latArr, double lonArr) {
        this.latPart = latPart;
        this.lonPart = lonPart;
        this.latArr = latArr;
        this.lonArr = lonArr;
    }

    public Coordinate(double latPart, double lonPart, double latArr, double lonArr, int distanza) {
        this.latPart = latPart;
        this.lonPart = lonPart;
        this.latArr = latArr;
        this.lonArr = lonArr;
        setArea(distanza);
    }

    public Coordinate(double latPart, double lonPart, double latArr, double lonArr, int distanza_tra, int distanza) {
        this.latPart = latPart;
        this.lonPart = lonPart;
        this.latArr = latArr;
        this.lonArr = lonArr;
        this.distanza = distanza_tra;
        setArea(distanza);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMinLatP(double minLatP) {
        this.minLatP = minLatP;
    }

    public void setMinLonP(double minLonP) {
        this.minLonP = minLonP;
    }

    public void setMaxLatP(double maxLatP) {
        this.maxLatP = maxLatP;
    }

    public void setMaxLonP(double maxLonP) {
        this.maxLonP = maxLonP;
    }

    public void setMinLatA(double minLatA) {
        this.minLatA = minLatA;
    }

    public void setMinLonA(double minLonA) {
        this.minLonA = minLonA;
    }

    public void setMaxLatA(double maxLatA) {
        this.maxLatA = maxLatA;
    }

    public void setMaxLonA(double maxLonA) {
        this.maxLonA = maxLonA;
    }

    public void setLatPart(double latPart) {
        this.latPart = latPart;
    }

    public void setLonPart(double lonPart) {
        this.lonPart = lonPart;
    }

    public void setLatArr(double latArr) {
        this.latArr = latArr;
    }

    public void setLonArr(double lonArr) {
        this.lonArr = lonArr;
    }

    public double getMinLatP() {
        return minLatP;
    }

    public double getMinLonP() {
        return minLonP;
    }

    public double getMaxLatP() {
        return maxLatP;
    }

    public double getMaxLonP() {
        return maxLonP;
    }

    public double getMinLatA() {
        return minLatA;
    }

    public double getMinLonA() {
        return minLonA;
    }

    public double getMaxLatA() {
        return maxLatA;
    }

    public double getMaxLonA() {
        return maxLonA;
    }

    public double getLatPart() {
        return latPart;
    }

    public double getLonPart() {
        return lonPart;
    }

    public double getLatArr() {
        return latArr;
    }

    public double getLonArr() {
        return lonArr;
    }

    public double getDistanza() {
        return distanza;
    }

    public void setArea(int dist) {
        double d = 0;
        if (dist == 0) {
            d = 1;
        }
        if (dist == 1) {
            d = 1.3;
        }
        if (dist == 2) {
            d = 2;
        }
        //distanza = CalcolaDistanza(latPart, latArr, lonPart, lonArr);
        if (distanza < DISTANZA_URBANA) {
            double diametroAumentato = DIAMETRO_PIEDI * d;
            distanzaMax = 0.0124 * diametroAumentato / 1000;
            distanzaMaxMetri = diametroAumentato;
        } else if (distanza < DISTANZA_PROVINCIALE) {
            double diametroAumentato = DIAMETRO_MEDIO * d;
            distanzaMax = 0.0124 * diametroAumentato / 1000;
            distanzaMaxMetri = diametroAumentato;
        } else {
            double diametroAumentato = DIAMETRO_GRANDE * d;
            distanzaMax = 0.0124 * diametroAumentato / 1000;
            distanzaMaxMetri = diametroAumentato;
        }

        minLatP = latPart - (distanzaMax) * d;
        minLonP = lonPart - (distanzaMax) * d;
        maxLatP = latPart + (distanzaMax) * d;
        maxLonP = lonPart + (distanzaMax) * d;

        minLatA = latArr - (distanzaMax) * d;
        minLonA = lonArr - (distanzaMax) * d;
        maxLatA = latArr + (distanzaMax) * d;
        maxLonA = lonArr + (distanzaMax) * d;
    }

    public static double CalcolaDistanza(Double latA, Double latB, Double lonA, Double lonB) {
        //Converte gradi in radianti
        double lat_alfa = pigreco * latA / 180.0;
        double lat_beta = pigreco * latB / 180.0;
        double lon_alfa = pigreco * lonA / 180.0;
        double lon_beta = pigreco * lonB / 180.0;
//
        /* Calcola l'angolo compreso fi */
        double fi = abs(lon_alfa - lon_beta);
        /* Calcola il terzo lato del triangolo sferico */
        double p = acos(sin(lat_beta) * sin(lat_alfa)
                + cos(lat_beta) * cos(lat_alfa) * cos(fi));
        /* Calcola la distanza sulla superficie
      terrestre R = ~6371 km */
        return p * raggio_quadratico_medio * 1000;
    }

    public boolean CalcolaDistanzaDaArrivo(Double latA, Double lonA) {
        //Converte gradi in radianti
        double lat_alfa = pigreco * latA / 180.0;
        double lat_beta = pigreco * latArr / 180.0;
        double lon_alfa = pigreco * lonA / 180.0;
        double lon_beta = pigreco * lonArr / 180.0;
//
        /* Calcola l'angolo compreso fi */
        double fi = abs(lon_alfa - lon_beta);
        /* Calcola il terzo lato del triangolo sferico */
        double p = acos(sin(lat_beta) * sin(lat_alfa)
                + cos(lat_beta) * cos(lat_alfa) * cos(fi));
        /* Calcola la distanza sulla superficie
      terrestre R = ~6371 km */
        double distanzaMetri = p * raggio_quadratico_medio * 1000;
        if (distanzaMetri < distanzaMaxMetri) {
            return true;
        } else {
            return false;
        }
    }
}
