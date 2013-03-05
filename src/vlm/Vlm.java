package vlm;


/*
 */
import java.awt.*;

import java.text.*;
import java.lang.Math;

//NN import Planform.*;
//NN import Vector.*;

/* ----------------------------------------------------------------- */
class Pt {

    double x, y, z;

    public Pt() {
        this.x = this.y = this.z = 0.0;
    }

    public void clone(Pt pt) {
        this.x = pt.x;
        this.y = pt.y;
        this.z = pt.z;
    }

    public void swap(Pt a, Pt b) {
        Pt p = new Pt();

        p.x = a.x;
        a.x = b.x;
        b.x = p.x;
        p.y = a.y;
        a.y = b.y;
        b.y = p.y;
        p.z = a.z;
        a.z = b.z;
        b.z = p.z;
    }
}

/* --------------------------------------------- */
class Pnl {

    Pt A, B, C;

    public Pnl() {
        A = new Pt();
        B = new Pt();
        C = new Pt();
    }

    public void clone(Pnl pnl) {
        this.A = pnl.A;
        this.B = pnl.B;
        this.C = pnl.C;
    }
}

/* ----------------------------------------------------------------- */
class Vlm {

    final double PI = 3.141592;
    int nPnl = 0;
    Pnl panel[];
    Pt C[][];
    Pt W[];
    double chordVel[];
    double gamma[];
    double chord[];
    float panelWidth;
    float totalLift = 0;
    float goodness = 0;
    float liftScale[];
    Vector liftDist;
    Vector idealDist;
    Vector ClDist;
    Vector shear;
    Vector bendMoment;
    Vector alpha;
    Vector reynolds;
    float maxReynolds = 0;
    float minReynolds = 0;
    float avgCl = 0;
    DecimalFormat intFmt = new DecimalFormat(" #00");
    DecimalFormat fltFmt = new DecimalFormat("  ###0.00; -###0.00");
    DecimalFormat fmt73f = new DecimalFormat("  00.000; -00.000");
    DecimalFormat fmt74f = new DecimalFormat("  0.0000; -0.0000");
    DecimalFormat fmt75f = new DecimalFormat("  0.00000; -0.00000");


    /* --------------------------------------------- */
    public void clone(Vlm vlm) {
        this.nPnl = nPnl;

        for (int i = 0; i < nPnl; i++) {
            this.panel[i] = panel[i];
            this.gamma[i] = gamma[i];
            this.chord[i] = chord[i];
        }

        this.panelWidth = panelWidth;
        this.totalLift = totalLift;
        this.goodness = goodness;

        this.liftDist = liftDist;
        this.idealDist = idealDist;
        this.ClDist = ClDist;
        this.shear = shear;
        this.bendMoment = bendMoment;
        this.alpha = alpha;
        this.reynolds = reynolds;

        this.maxReynolds = maxReynolds;
        this.minReynolds = minReynolds;
        this.avgCl = avgCl;
    }

    /* --------------------------------------------- */
    private void createPanels(Planform pf) {
        float spanPos;
        float chord, le, te;

        panelWidth = pf.wingSpan / nPnl;
        spanPos = 0;

        chord = pf.getChord(spanPos);
        le = pf.getLe(spanPos);

        for (int i = nPnl / 2; i < nPnl; i++) {

            panel[i].A.x = -(le - chord * 0.25);
            panel[i].A.y = pf.getTrueY((float) spanPos);
            panel[i].A.z = pf.getHeight((float) panel[i].A.y);

            panel[i].C.x = -(le - chord * 0.75);
            panel[i].C.y = pf.getTrueY((float) (spanPos + panelWidth / 2.0));
            panel[i].C.z = pf.getHeight((float) panel[i].C.y);

            spanPos += panelWidth;

            chord = pf.getChord(spanPos);
            le = pf.getLe(spanPos);

            if (false) {
                System.out.print("createPanels: ");
                System.out.print(" " + fltFmt.format(spanPos));
                System.out.print(" " + fltFmt.format(chord));
                System.out.println(" " + fltFmt.format(le));
            }

            panel[i].B.x = -(le - chord * 0.25);
            panel[i].B.y = pf.getTrueY((float) spanPos);
            panel[i].B.z = pf.getHeight((float) panel[i].B.y);

            panel[i].C.x = (panel[i].C.x - (le - chord * 0.75)) / 2.0;
        }

        /* mirror panel on opposite wing */
        for (int i = nPnl / 2; i < nPnl; i++) {
            int j = nPnl - 1 - i;

            panel[j].A.x = panel[i].B.x;
            panel[j].B.x = panel[i].A.x;
            panel[j].C.x = panel[i].C.x;

            panel[j].A.y = -panel[i].B.y;
            panel[j].B.y = -panel[i].A.y;
            panel[j].C.y = -panel[i].C.y;

            panel[j].A.z = -panel[i].B.z;
            panel[j].B.z = -panel[i].A.z;
            panel[j].C.z = -panel[i].C.z;
        }
    }

    /* ----------------------------------------------------------------- */
    private Pt calcElem(int m, int n) {
        double x, x1, x2;
        double d1x, d2x, d12x;
        double y, y1, y2;
        double d1y, d2y, d12y;
        double num1, num2, denom1, denom2;
        double psi, omega, abSeg, aInf, bInf;
        Pt elem = new Pt();

        x1 = panel[n].A.x;
        y1 = panel[n].A.y;
        x2 = panel[n].B.x;
        y2 = panel[n].B.y;
        x = panel[m].C.x;
        y = panel[m].C.y;

        d1x = x - x1;
        d2x = x - x2;
        d12x = x2 - x1;
        d1y = y - y1;
        d2y = y - y2;
        d12y = y2 - y1;

        psi = 1.0 / ((d1x * d2y) - (d2x * d1y));

        num1 = (d12x * d1x) + (d12y * d1y);
        num2 = (d12x * d2x) + (d12y * d2y);

        denom1 = Math.sqrt((d1x * d1x) + (d1y * d1y));
        denom2 = Math.sqrt((d2x * d2x) + (d2y * d2y));

        omega = (num1 / denom1) - (num2 / denom2);
        abSeg = psi * omega;

        aInf = (1.0 / -d1y) * (1.0 + (d1x / denom1));
        bInf = -(1.0 / -d2y) * (1.0 + (d2x / denom2));

        elem.z = abSeg + aInf + bInf;

        return elem;
    }

    /* ------------------------- */
    private Pt calcElem3d(int m, int n) {
        double x, x1, x2, d1x, d2x, d12x;
        double y, y1, y2, d1y, d2y, d12y;
        double z, z1, z2, d1z, d2z, d12z;

        double coef;
        Pt psi = new Pt();
        double num1, num2, denom1, denom2, omega;

        Pt abSeg = new Pt();
        Pt aInf = new Pt();
        Pt bInf = new Pt();
        Pt elem = new Pt();

        /* retrieve coordinates */
        x1 = panel[n].A.x;
        y1 = panel[n].A.y;
        z1 = panel[n].A.z;
        x2 = panel[n].B.x;
        y2 = panel[n].B.y;
        z2 = panel[n].B.z;
        x = panel[m].C.x;
        y = panel[m].C.y;
        z = panel[m].C.z;

        /* calc deltas */
        d1x = x - x1;
        d2x = x - x2;
        d12x = x2 - x1;
        d1y = y - y1;
        d2y = y - y2;
        d12y = y2 - y1;
        d1z = z - z1;
        d2z = z - z2;
        d12z = z2 - z1;

        /* psi calc */
        coef = 1;

        psi.x = ((d1y * d2z) - (d2y * d1z)) / coef;
        psi.y = ((d1x * d2z) - (d2x * d1z)) / coef;
        psi.z = ((d1x * d2y) - (d2x * d1y)) / coef;

        coef = (psi.x * psi.x + psi.y * psi.y + psi.z * psi.z);

        psi.x /= coef;
        psi.y /= coef;
        psi.z /= coef;

        /* omega calc */
        num1 = (d12x * d1x) + (d12y * d1y) + (d12z * d1z);
        num2 = (d12x * d2x) + (d12y * d2y) + (d12z * d2z);;

        denom1 = Math.sqrt((d1x * d1x) + (d1y * d1y) + (d1z * d1z));
        denom2 = Math.sqrt((d2x * d2x) + (d2y * d2y) + (d2z * d2z));

        omega = (num1 / denom1) - (num2 / denom2);

        /* calc vortex segments */
        abSeg.x = psi.x * omega;
        abSeg.y = psi.y * omega;
        abSeg.z = psi.z * omega;

        coef = (1.0 + (d1x / denom1));
        aInf.y = (1.0 / d1z) * coef;
        aInf.z = (1.0 / -d1y) * coef;

        coef = (1.0 + (d2x / denom2));
        bInf.y = -(1.0 / d2z) * coef;
        bInf.z = -(1.0 / -d2y) * coef;

        /* combine segments */
        elem.x = abSeg.x + aInf.x + bInf.x;
        elem.y = abSeg.y + aInf.y + bInf.y;
        elem.z = abSeg.z + aInf.z + bInf.z;

        return elem;
    }

    /* ------------------------- */
    private void buildC() {
        int n, m;

        for (m = 0; m < nPnl; m++) {
            for (n = 0; n < nPnl; n++) {
                C[m][n] = calcElem3d(m, n);
            }
        }
    }

    /* ----------------------------------------------------------------- */
    /* calculate alpha, angle-of-attack, at mid-point of each panel */
    /*      accounting for twist, flaps and ailerons */
    private void setAlpha(Planform pf) {
        int sect = 1;
        double spanPos = pf.span[sect];
        double twistDelta = -pf.twist[sect] / pf.span[sect];

        double angle = -pf.aoa;
        double delta;
        double panelWidth = pf.wingSpan / nPnl;
        double panelPos = panelWidth / 2;
        double tPos = 0;

        // determine twist at each panel-position
        for (int i = nPnl / 2; i < nPnl; i++) {
            while (tPos < panelPos) {
                if (spanPos < panelPos) {
                    delta = spanPos - tPos;
                    angle += twistDelta * delta;

                    spanPos += pf.span[++sect];
                    twistDelta = -pf.twist[sect] / pf.span[sect];
                } else {
                    delta = panelPos - tPos;
                    angle += twistDelta * delta;
                }

                tPos += delta;
            }

            // set alpha at current panel position on both wings
            alpha.x[i] = (float) panel[i].C.y;
            alpha.y[i] = (float) angle;

            int j = nPnl - 1 - i;
            alpha.x[j] = -alpha.x[i];
            alpha.y[j] = alpha.y[i];

            // adjust panel position
            panelPos += panelWidth;
        }

        /* add flap deflection */
        if (pf.flapPosR > 0) {
            int i = nPnl / 2 + (int) (pf.flapPosL / panelWidth);
            int L = nPnl / 2 + (int) (pf.flapPosR / panelWidth);

            double angR = -pf.flapAngR;
            double angL = -pf.flapAngL;

            for (; i < L; i++) {
                alpha.y[i] += angR;
                alpha.y[nPnl - 1 - i] += angL;
            }
        }

        /* add aileron deflection */
        if (pf.aileronPosR > 0) {
            int i = nPnl / 2 + (int) (pf.aileronPosL / panelWidth);
            int L = nPnl / 2 + (int) (pf.aileronPosR / panelWidth);

            double angR = -pf.aileronAngR;
            double angL = -pf.aileronAngL;

            for (; i < L; i++) {
                alpha.y[i] += angR;
                alpha.y[nPnl - 1 - i] += angL;
            }
        }
    }

    /* ----------------------------------------------------------------- */
    private void setW(Planform pf) {
        double radius, vel, r;
        double bRad = pf.bank * PI / 180;
        double cosB = (float) 1.0;
        double aoa;
        boolean dbg = false;

        radius = (float) -1.0;
        vel = pf.airspeed;

        if (pf.bank > 0) {
            radius = (vel * vel / (pf.acc * Math.tan(bRad)));
            cosB = Math.cos(bRad);

        } else {
            cosB = 1.0;
        }

        if (dbg) {
            System.out.println(" setW: "
                    + fmt73f.format(pf.bank) + " bank  "
                    + fmt73f.format(radius) + " radius  "
                    + fmt73f.format(vel) + " vel  "
                    + fmt73f.format(bRad) + " bRad  "
                    + fmt73f.format(cosB) + " cosB  "
                    + fmt73f.format(pf.acc) + " G");

            System.out.println(" setW:"
                    + "      y" + "  dRad(y)"
                    + "   aoa(y)" + " alpha(y)"
                    + "   vel(y)" + "     W(y)");

            System.out.println("");
        }

        for (int i = 0; i < nPnl; i++) {
            W[i] = new Pt();

            if (bRad > 0) {
                r = (radius + (alpha.x[i] * cosB / pf.scale)) / radius;
                chordVel[i] = vel * r;
            } else {
                r = 1.0;
                chordVel[i] = vel;
            }

            aoa = alpha.y[i];

            /* finally, combine all right-hand terms into W[] */
            W[i].z = (float) 4 * PI * chordVel[i] * (alpha.y[i] * PI / 180);

            if (dbg) {
                System.out.println(" " + intFmt.format(i)
                        + " " + fmt73f.format(alpha.x[i])
                        + " " + fmt73f.format(r)
                        + " " + fmt73f.format(aoa)
                        + " " + fmt73f.format(alpha.y[i])
                        + " " + fmt73f.format(chordVel[i])
                        + " " + fmt73f.format(W[i].z));
            }
        }
        if (dbg) {
            System.out.println("");
        }
    }

    /* ----------------------------------------------------------------- */
    private void dumpC() {
        System.out.println("C:");
        for (int i = 0; i < nPnl; i++) {
            System.out.print("  " + intFmt.format(i));

            for (int j = 0; j < nPnl; j++) {
                System.out.print(fmt73f.format(C[i][j].z));
            }

            System.out.print(fltFmt.format(W[i].z));
            System.out.println("");
        }
        System.out.println("");
    }

    /*----------------------------------------*/
    private void dumpGamma() {
        System.out.println("gammas:");
        for (int i = 0; i < nPnl; i++) {
            System.out.println(" " + intFmt.format(i)
                    + " " + fmt73f.format(panel[i].C.y)
                    + " " + fmt75f.format(gamma[i])
                    + " " + fmt75f.format(gamma[i]
                    / (4 * PI * chordVel[i] * (alpha.y[i] * PI / 180)))
                    + " " + fmt75f.format(liftDist.y[i]));
        }
    }

    /*----------------------------------------*/
    private void dumpAlpha() {
        System.out.println("W:");
        for (int i = 0; i < nPnl; i++) {
            System.out.println(" " + intFmt.format(i)
                    + ": " + fltFmt.format(W[i].x)
                    + " " + fltFmt.format(W[i].y)
                    + " " + fltFmt.format(W[i].z));
        }
    }

    /* ----------------------------------------------------------------- */
    /* solution of simultaneous equations */
    private void solve() {
        int i, j, k, p;
        double m, sum;

        for (k = 0; k < nPnl - 1; k++) {
            for (j = k + 1; j < nPnl; j++) {
                m = C[j][k].z / C[k][k].z;

                for (p = k; p < nPnl; p++) {
                    C[j][p].z -= m * C[k][p].z;
                }

                W[j].z -= m * W[k].z;
            }
        }


        for (i = nPnl - 1; i >= 0; i--) {
            sum = 0;
            for (j = i + 1; j < nPnl; j++) {
                sum += C[i][j].z * gamma[j];
            }

            gamma[i] = (W[i].z - sum) / C[i][i].z;
        }
    }

    /* ----------------------------------------------------------------- */
    /* the result in gamma[] is a normalize velocity density
     /* that needs to be properly scale for flight conditions
     /* (i.e. airspeed, air-density) and wing panel area
     /* 
     /* the scale factor is needed to convert several values, including
     /* gamma and panelWidth, from inches to feet which rho is based on.
     /*/
    public void calcLiftDist(Planform pf) {
        double maxGamma = 0;
        double dy;

        /* calculate actual lift-distribution for required airspeed */
        double K = pf.rho / pf.scale;	/* gamma needs to be scaled */

        //	System.out.println ("K = "+ K);

        totalLift = 0;
        for (int i = 0; i < nPnl; i++) {
            liftDist.x[i] = (float) panel[i].C.y;
            liftDist.y[i] = (float) (K * chordVel[i] * gamma[i]);

            dy = (panel[i].B.y - panel[i].A.y) / pf.scale;
            liftScale[i] = (float) dy;

            totalLift += liftDist.y[i] * dy;

            if (maxGamma < gamma[i]) {
                maxGamma = gamma[i];
            }
        }

        /* display intermediate results for require airspeed */
        if (false) {
            System.out.println("");
            System.out.println("maxGamma = " + maxGamma);
            System.out.println("panelWid = " + panelWidth / pf.scale);
            System.out.println("calcVlm: total normalized lift = " + totalLift);

            if (pf.english) {
                System.out.println("calcVlm: "
                        + pf.airspeed + " airspeed (ft/sec)  "
                        + pf.airspeed * 3600 / 5280 + " airspeed (mph)");
            } else {
                System.out.println("calcVlm: "
                        + pf.airspeed + " airspeed (m/sec)  "
                        + pf.airspeed * 3600 / 1000 + " airspeed (kph)");
            }

        }

        /* calculate average Cl */
        avgCl = totalLift / ((float) 0.5 * pf.rho * pf.airspeed * pf.airspeed
                * pf.area / (pf.scale * pf.scale));

        if (false) {
            System.out.println("");
            System.out.println("calcVlm: " + totalLift + "total lift (lb)");
            System.out.println("calcVlm: " + avgCl + "average Cl");
        }
    }

    /* ----------------------------------------------------------------- */
    /* calc Cl-dist vector */
    /*
     /* the lift for each panel is rho * V * gamma * dy
     /* both gamma and dy need to be scaled (inches-ft)
     /*
     /* Cl is lift / (1/2 rho v^2 S)  where S is area
     /* reducing terms and realizing that both the gamma and the chord
     /* need to be scaled, but whihc cancel, 
     /* the local Cl is 2 * gamma / (v * chord)
     /*/
    private void calcClDist(Planform pf) {
        float spanPos = panelWidth / 2;
        float maxCl = 0;

        for (int i = nPnl / 2; i < nPnl; i++) {
            //	chord[i]    = pf.getChord (spanPos) / pf.scale;
            chord[i] = pf.getChord(spanPos);

            ClDist.x[i] = (float) panel[i].C.y;
            ClDist.y[i] = (float) (2 * gamma[i] / (chordVel[i] * chord[i]));

            if (maxCl < ClDist.y[i]) {
                maxCl = ClDist.y[i];
            }

            // mirror
            int j = nPnl - 1 - i;

            chord[j] = chord[i];

            ClDist.x[j] = (float) panel[j].C.y;
            ClDist.y[j] = (float) (2 * gamma[j] / (chordVel[j] * chord[i]));

            if (maxCl < ClDist.y[j]) {
                maxCl = ClDist.y[j];
            }

            spanPos += panelWidth;
        }

        goodness = avgCl / maxCl;
    }

    /* ----------------------------------------------------------------- */
    /* create elliptic vector  */
    private void calcGoodness(Planform pf) {
        float A = -liftDist.x[0];
        float B = (float) (2 * totalLift / (A * PI)) * pf.scale;
        //	float  B = (float) (2 * totalLift / (A * PI));

        for (int i = 0; i < nPnl / 2; i++) {
            int j = nPnl - (i + 1);
            float x = liftDist.x[i];
            float y = (float) (B * Math.sqrt(1 - ((x / A) * (x / A))));

            idealDist.x[i] = x;
            idealDist.x[j] = -x;

            idealDist.y[i] = idealDist.y[j] = y;

            float delta = idealDist.y[i] - liftDist.y[i];
        }
    }

    /* ----------------------------------------------------------------- */
    /* this is redundant of creatPanels */
    private void calcReynolds(Planform pf) {
        float spanPos;
        float k = pf.rho / pf.viscosity / pf.scale;

        panelWidth = pf.wingSpan / nPnl;
        spanPos = panelWidth / 2;

        minReynolds = maxReynolds = 0;

        for (int i = nPnl / 2; i < nPnl; i++) {
            reynolds.y[i] = (float) (chordVel[i] * chord[i] * k);
            reynolds.x[i] = spanPos;

            if (maxReynolds < reynolds.y[i]) {
                maxReynolds = reynolds.y[i];
            }
            if (minReynolds > reynolds.y[i] || minReynolds == 0) {
                minReynolds = reynolds.y[i];
            }

            int j = nPnl - 1 - i;

            reynolds.y[j] = (float) (chordVel[j] * chord[i] * k);
            reynolds.x[j] = -reynolds.x[i];

            if (maxReynolds < reynolds.y[j]) {
                maxReynolds = reynolds.y[j];
            }
            if (minReynolds > reynolds.y[j] || minReynolds == 0) {
                minReynolds = reynolds.y[j];
            }

            spanPos += panelWidth;
        }
    }

    /* ----------------------------------------------------------------- */
    /* ----------------------------------------------------------------- */
    public void calcVlm(WingCanvas c, Planform pf,
            int nPanels, TextArea txt) {

        int disp = pf.debug;
        nPnl = nPanels;

        /* alloc and initialize panels */
        panel = new Pnl[nPnl];
        for (int i = 0; i < nPnl; i++) {
            panel[i] = new Pnl();
        }

        C = new Pt[nPnl][nPnl];
        W = new Pt[nPnl];
        gamma = new double[nPnl];
        chord = new double[nPnl];

        chordVel = new double[nPnl];

        liftScale = new float[nPnl];

        liftDist = new Vector(nPnl);
        idealDist = new Vector(nPnl);

        ClDist = new Vector(nPnl);
        shear = new Vector(nPnl);
        bendMoment = new Vector(nPnl);
        alpha = new Vector(nPnl);

        reynolds = new Vector(nPnl);

        /* ---------------------------- */
        /* run algorithm */

        createPanels(pf);
        setAlpha(pf);
        setW(pf);

        buildC();

        /* ---------------------------- */

        if ((disp & 1) != 0) {
            System.out.println("panels:");
            for (int i = 0; i < nPnl; i++) {
                System.out.print("  " + i);
                System.out.print("\t  " + fltFmt.format(panel[i].A.x));
                System.out.print("  " + fltFmt.format(panel[i].A.y));

                System.out.print("    " + fltFmt.format(panel[i].B.x));
                System.out.print("  " + fltFmt.format(panel[i].B.y));

                System.out.print("    " + fltFmt.format(panel[i].C.x));
                System.out.print("  " + fltFmt.format(panel[i].C.y));
                System.out.println("");
            }
            System.out.println("");

        }

        /* ---------------------------- */
        if ((disp & 2) != 0) {
            dumpC();
        }

        solve();

        /* ---------------------------- */
        calcLiftDist(pf);
        calcGoodness(pf);
        calcClDist(pf);

        // calcReynolds needs airspeed from calcLift & chord from calcCl
        calcReynolds(pf);

        if ((disp & 4) != 0) {
            dumpC();
            dumpGamma();
        }

        /* ---------------------------- */
        /* calc shear vector */

        for (int i = 0; i < nPnl; i++) {
            shear.x[i] = liftDist.x[i];
        }

        shear.y[(nPnl / 2) - 1] = shear.y[nPnl / 2] = totalLift / 2;

        for (int i = (nPnl / 2) - 1; i > 0; i--) {
            shear.y[i - 1] = shear.y[i] - (liftDist.y[i] * liftScale[i]);
        }

        for (int i = nPnl / 2; i < nPnl - 1; i++) {
            shear.y[i + 1] = shear.y[i] - (liftDist.y[i] * liftScale[i]);
        }

        /* ---------------------------- */
        /* calc bending-moment vector */

        for (int i = 0; i < nPnl; i++) {
            bendMoment.x[i] = shear.x[i];
        }

        bendMoment.y[0] = shear.y[0] * panelWidth;
        for (int i = 1; i < nPnl / 2; i++) {
            bendMoment.y[i] = bendMoment.y[i - 1] + (shear.y[i] * panelWidth);
        }

        bendMoment.y[nPnl - 1] = shear.y[nPnl - 1] * panelWidth;
        for (int i = nPnl - 2; i >= nPnl / 2; i--) {
            bendMoment.y[i] = bendMoment.y[i + 1] + (shear.y[i] * panelWidth);
        }

        /* ---------------------------- */
        if (txt != null) {
            txt.appendText("\n");
            txt.appendText("lift calculations\n");

            txt.appendText("pnl");
            txt.appendText("  y-pos");
            txt.appendText("  x-pos");
            txt.appendText("  gamma");
            txt.appendText("   lift");
            txt.appendText("     Cl");
            txt.appendText("\n");

            for (int i = 0; i < nPnl; i++) {
                txt.appendText(intFmt.format(i));
                txt.appendText(fmt73f.format((float) panel[i].C.x));
                txt.appendText(fmt73f.format((float) panel[i].C.y));

                txt.appendText(fmt73f.format((float) panel[i].A.x));
                txt.appendText(fmt73f.format((float) panel[i].A.y));

                txt.appendText(fmt73f.format((float) panel[i].B.x));
                txt.appendText(fmt73f.format((float) panel[i].B.y));

                txt.appendText(fmt75f.format(gamma[i]));
                txt.appendText(fmt74f.format(liftDist.y[i]));
                txt.appendText(fmt74f.format(ClDist.y[i]));
                txt.appendText("\n");
            }
        }
    }

    /* ----------------------------------------------------------------- */
    public void drawPanels(WingCanvas c, Graphics g) {

        for (int i = nPnl / 2; i < nPnl; i++) {
            float ya = (float) -panel[i].A.x;
            float xa = (float) panel[i].A.y;

            float yb = (float) -panel[i].B.x;
            float xb = (float) panel[i].B.y;

            float yc = (float) -panel[i].C.x;
            float xc = (float) panel[i].C.y;

            g.setColor(Color.black);
            c.plotLine(g, xa, ya, xb, yb);
            // g.setColor(Color.blue);
            c.plotLine(g, xb, yb, xc, yc);
            // g.setColor(Color.red);
            c.plotLine(g, xa, ya, xc, yc);

            if (false) {
                System.out.print("  " + i);
                System.out.print("  " + fltFmt.format(xa) + " " + fltFmt.format(ya));
                System.out.print(", " + fltFmt.format(xb) + " " + fltFmt.format(yb));
                System.out.print(", " + fltFmt.format(xc) + " " + fltFmt.format(yc));
                System.out.println("");
            }
        }
    }
}
