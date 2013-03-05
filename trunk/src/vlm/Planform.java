package vlm;

/*
 */

import java.awt.*;

import java.text.*;

//NN import AirfoilDb.*;
//NN import WingCanvas.*;
//NN import Vector.*;

/*------------------------------------------------------------------*/
class Mac {

    public static float chord(float r, float t) {
        return (float) ((2.0 / 3.0) * ((r * r) + (r * t) + (t * t)) / (r + t));
    }

    public static float spanLoc(float root, float tip) {
        float r = (float) root;
        float t = (float) tip;

        return (float) ((r + (2.0 * t)) / (3.0 * (r + t)));
    }
}

/*------------------------------------------------------------------*/
class Rib {

    float spanPos;
    float chord;
    float le;
    float te;
}

/*------------------------------------------------------------------*/
class Planform {

    final double PI = 3.141592;
    Airfoil airfoil = null;
    String airfoilName = null;
    Vector CdDist;
    Vector dragDist;
    float dragScale = 16; 		// convert to oz
    float dragTotal;
    float dragInduced;
    Vector ld = null;
    Vlm vlm = new Vlm();
    TextArea textArea = null;
    int debug = 0;
    int nPanels;
    int nSect;
    float root;
    Airfoil af[] = new Airfoil[20];
    float span[] = new float[20];
    float chord[] = new float[20];
    float sweep[] = new float[20];
    float twist[] = new float[20];
    float dihedral[] = new float[20];
    float aoa;
    float xMax;
    float yMax;
    float yMin;
    float macFrac;
    float wingSpan;
    Rib rib = new Rib();
    float area;
    float aspectRatio;
    float wingLoading;
    float weight;
    float airspeed;
    float bank;
    float dragToLift;    // zero D/L means ideal L/D of infinity
    float rho;
    float viscosity;
    float acc;			// acceleration (e.g. 9.81)
    float scale;			// 12in/ft or 100cm/m
    float flapPosL, flapPosR, flapAngL, flapAngR;
    float aileronPosL, aileronPosR, aileronAngL, aileronAngR;
    boolean english;
    DecimalFormat f53 = new DecimalFormat(" 0.000;-0.000");
    DecimalFormat f52 = new DecimalFormat(" 00.00; -00.00");
    DecimalFormat f60 = new DecimalFormat(" 000000; -000000");

    /* ------------------------------------------------ */
    public void clone(Planform pf) {
        // airfoil         = new Airfoil ();
        // airfoil.clone (pf.airfoil);

        airfoilName = pf.airfoilName;
        CdDist = pf.CdDist;
        dragScale = pf.dragScale;

        vlm = pf.vlm;
        textArea = pf.textArea;

        this.nPanels = pf.nPanels;
        this.nSect = pf.nSect;

        this.root = pf.root;

        for (int i = 0; i < 20; i++) {
            this.span[i] = pf.span[i];
            this.chord[i] = pf.chord[i];
            this.sweep[i] = pf.sweep[i];
            this.twist[i] = pf.twist[i];
            this.dihedral[i] = pf.dihedral[i];
        }

        this.aoa = pf.aoa;

        this.xMax = pf.xMax;
        this.yMax = pf.yMax;
        this.yMin = pf.yMin;

        this.macFrac = pf.macFrac;

        this.wingSpan = pf.wingSpan;
        this.rib = pf.rib;

        this.area = pf.area;
        this.aspectRatio = pf.aspectRatio;
        this.wingLoading = pf.wingLoading;

        this.weight = pf.weight;
        this.airspeed = pf.airspeed;
        this.rho = pf.rho;
        this.viscosity = pf.viscosity;
        this.acc = pf.acc;
        this.scale = pf.scale;

        this.flapPosL = pf.flapPosL;
        this.flapPosR = pf.flapPosR;
        this.flapAngL = pf.flapAngL;
        this.flapAngR = pf.flapAngR;
        this.aileronPosL = pf.aileronPosL;
        this.aileronPosR = pf.aileronPosR;
        this.aileronAngL = pf.aileronAngL;
        this.aileronAngR = pf.aileronAngR;

        this.english = pf.english;
    }

    /* ------------------------------------------------ */
    public void init() {
        nPanels = 100;
        nSect = 0;

        textArea = null;

        root = 0;
        for (int i = 0; i < 20; i++) {
            span[i] = chord[i] = sweep[i] = twist[i] = dihedral[i] = 0;
        }

        xMax = yMax = 1;
        yMin = 0;

        macFrac = (float) 0.25;
        wingSpan = 0;
        rib.spanPos = -1;

        area = aspectRatio = wingLoading = 0;

        aoa = (float) 6.0;
        bank = (float) 0.0;
        dragToLift = (float) 0.0;
        weight = (float) 0;
        airspeed = (float) 20.0;

        flapPosL = flapPosR = flapAngL = flapAngR = 0;
        aileronPosL = aileronPosR = aileronAngL = aileronAngR = 0;

        setEnglish();
    }

    /* ------------------------------------------------ */
    public Planform() {
        init();
    }

    /* ------------------------------------------------ */
    public void setEnglish() {
        acc = (float) 32.16;
        //	acc       = (float) 1.0;
        rho = (float) 0.002378;		// slugs/cu.ft
        viscosity = (float) 0.000000373;	// slugs/ft/sec
        scale = (float) 12;				// in/ft
        english = true;
    }

    public void setMetric() {
        acc = (float) 9.81;
        rho = (float) 1.225;			// kg/cu.m
        viscosity = (float) 0.0000179;		// kg/m/sec
        scale = (float) 1;
        english = false;
    }

    /* ------------------------------------------------ */
    public float getChord(float spanPos) {
        if (spanPos > wingSpan / 2) {
            spanPos = wingSpan / 2;
        }

        if (rib.spanPos == spanPos) {
            return rib.chord;
        }

        float rootCh = root;
        float rootX = 0;
        float rootLe = 0;
        float rootTe = -root;

        float tipX = rootX;
        float tipLe = rootLe;
        float tipTe = rootTe;
        float tipCh = rootCh;

        float k;

        /* locate section containing spanPos, and it's parameters */
        for (int i = 1; i < nSect; i++) {
            tipX = rootX + span[i];

            tipTe = rootTe - sweep[i];
            tipLe = tipTe + chord[i];
            tipCh = chord[i];

            if (rootX <= spanPos && spanPos <= tipX) {
                break;
            }

            rootX = tipX;
            rootLe = tipLe;
            rootTe = tipTe;
            rootCh = tipCh;
        }

        /* proportionately locate spanPos within section */
        if (tipX == rootX) {
            k = (float) 1.0;
        } else {
            k = (spanPos - rootX) / (tipX - rootX);
        }

        rib.chord = rootCh - k * (rootCh - tipCh);
        rib.le = rootLe - k * (rootLe - tipLe);
        rib.te = rib.le - rib.chord;
        rib.spanPos = spanPos;

        return rib.chord;
    }

    /* -------------------------------------- */
    public float getLe(float spanPos) {
        if (rib.spanPos != spanPos) {
            if (getChord(spanPos) < 0) {
                return -1;
            }
        }

        return rib.le;
    }

    /* -------------------------------------- */
    public float getTe(float spanPos) {
        if (rib.spanPos != spanPos) {
            if (getChord(spanPos) < 0) {
                return -1;
            }
        }

        return rib.te;
    }

    /* -------------------------------------- */
    public float getHeight(float spanPos) {
        float ang, h, y;
        int i;

        ang = h = y = 0;

        for (i = 1; i < nSect; i++) {
            if (spanPos <= (y + span[i])) {
                break;
            }

            ang += dihedral[i] * PI / 180;
            h += (float) (span[i] * Math.sin(ang));
            y += span[i];
        }

        ang += dihedral[i] * PI / 180;
        h += (float) ((spanPos - y) * Math.sin(ang));

        return h;
    }

    /* -------------------------------------- */
    public float getTrueY(float spanPos) {
        float ang, trueY, y;
        int i;

        ang = y = trueY = 0;

        for (i = 1; i < nSect; i++) {
            if (spanPos <= (y + span[i])) {
                break;
            }

            ang += dihedral[i] * PI / 180;
            y += span[i];
            trueY += span[i] * Math.cos(ang);
        }

        ang += dihedral[i] * PI / 180;
        trueY += (spanPos - y) * Math.cos(ang);

        return trueY;
    }

    /* ------------------------------------------------ */
    public void params(WingCanvas canvas, float root, int nSect,
            float[] span, float[] chord, float[] sweep) {
        this.nSect = nSect;
        this.root = root;
        this.area = 0;

        float rootChord = root;
        float yLe = 0;
        float yTe = -root;

        xMax = 0;
        yMin = -root;
        yMax = 0;

        for (int i = 1; i < nSect; i++) {
            this.span[i] = span[i];
            this.chord[i] = chord[i];
            this.sweep[i] = sweep[i];

            /* determine overall size */
            yTe -= sweep[i];
            yLe = yTe + chord[i];

            if (yMax < yLe) {
                yMax = yLe;
            }

            if (yMin > yTe) {
                yMin = yTe;
            }

            xMax += span[i];

            /* double 1/2-span area */
            area += span[i] * (rootChord + chord[i]);
            rootChord = chord[i];
        }

        wingSpan = xMax * 2;
        aspectRatio = wingSpan * wingSpan / area;

        /* update lift */
        vlm.calcVlm(canvas, this, nPanels, textArea);
    }

    // ------------------------------------------------
    //  determine interpolated airfoil drag
    //   using specified airfoil at ends of wing section
    //   at specified span postion and RE
    private float interpDrag(int pnlIdx) {
        double y = vlm.panel[pnlIdx].C.y;
        if (y < 0) {
            y *= -1;
        }

        // determine index of wing panel ends
        float y0 = 0;
        float y1 = span[1];
        int i;
        for (i = 1; i < nSect - 1; i++) {
            if (y0 <= y && y <= y1) {
                break;
            }
            y0 = y1;
            y1 += span[i + 1];
        }

        // System.out.print (" " + i);

        // determine drag for each airfoil
        float d0 = af[i - 1].airfoilCd(vlm.ClDist.y[pnlIdx],
                vlm.reynolds.y[pnlIdx]);
        float d1 = af[i].airfoilCd(vlm.ClDist.y[pnlIdx],
                vlm.reynolds.y[pnlIdx]);

        // System.out.print (", RE "+ f60.format(vlm.reynolds.y[pnlIdx]));

        // System.out.print (", " + af[i-1].name);
        // System.out.print (" "+  f53.format(d0));
        // System.out.print (", " + af[i].name);
        // System.out.print (" "+  f53.format(d1));

        // return the interpolated result
        float drag = (float) (d0 + (d1 - d0) * (y - y0) / (y1 - y0));
        // System.out.print (", "+  f53.format(drag));

        return drag;
    }

    /* ------------------------------------------------ */
    public void updateDrag() {
        // update drag vector
        int size = vlm.ClDist.size;
        CdDist = new Vector(size);

        // calculate drag vector
        // System.out.println ("# drag: ");

        for (int i = 0; i < size; i++) {
            float drag;

            // System.out.print ("    "+ f52.format(vlm.panel[i].C.y));

            drag = interpDrag(i);

            CdDist.x[i] = vlm.ClDist.x[i];
            CdDist.y[i] = drag * dragScale;

            // System.out.print ("   "+  f53.format(CdDist.y[i]));
            // System.out.println ();
        }

        // update partial (wing only) L/D vector
        final int N = 10;

        ld = new Vector(N);

        float S = area / (scale * scale);
        float q = S * rho / 2;
        float avgChord = S / (wingSpan / scale);
        float Cl = (airfoil.minCl > 0 ? airfoil.minCl : (float) 0.01);
        float dCl = (airfoil.maxCl - Cl) / (N - 1);

        // update polar vector (ld[])
        for (int i = 0; i < N; i++, Cl += dCl) {
            float V = (float) Math.sqrt(weight / (q * Cl));
            float RN = V * avgChord * rho / viscosity;

            float Cdi = Cl * Cl / ((float) PI * aspectRatio);
            float Cdp = airfoil.airfoilCd(Cl, RN);
            float Cd = Cdi + Cdp;

            ld.x[i] = Cd;
            ld.y[i] = Cl;
        }

        // calculate drag[panel] and total drag
        //    drag = 0.5 * rho * V^2 * S * Cd

        dragDist = new Vector(size);
        dragTotal = 0;

        for (int i = 0; i < size; i++) {
            dragTotal +=
                    dragDist.y[i] = (float) (0.5 * rho
                    * (vlm.chordVel[i] * vlm.chordVel[i])
                    * CdDist.y[i]
                    * (vlm.chord[i] * vlm.panelWidth)
                    / (scale * scale));
            dragDist.x[i] = CdDist.x[i];
        }

        // calculate the induced drag
        //   Inducde-Drag = (W / B)^2 / (Pi * 0.5 * rho * V^2)
        float spanLoad = weight / (wingSpan / 2 / scale);
        double PI = 3.141592;

        dragInduced = (float) (dragScale * (spanLoad * spanLoad)
                / (PI * 0.5 * rho * airspeed * airspeed));
    }

    /* ------------------------------------------------ */
    public void scale(WingCanvas canvas) {
        canvas.setScale(0, yMin, xMax, yMax);
    }

    /* ------------------------------------------------ */
    public void drawPlanform(WingCanvas c, Graphics g) {
        float rootY = 0;		// y axis is alined with fuselage
        float rootLe = 0;
        float rootTe = -root;

        g.setColor(Color.black);
        c.plotLine(g, rootY, rootLe, rootY, rootTe);

        g.setColor(Color.blue);
        for (int i = 1; i < nSect; i++) {
            float tipY = rootY + span[i];

            float tipTe = rootTe - sweep[i];
            float tipLe = tipTe + chord[i];

            float y0 = getTrueY(rootY);
            float y1 = getTrueY(tipY);

            c.plotLine(g, y0, rootLe, y1, tipLe);
            c.plotLine(g, y1, tipLe, y1, tipTe);
            c.plotLine(g, y0, rootTe, y1, tipTe);

            rootY = tipY;
            rootLe = tipLe;
            rootTe = tipTe;
        }
    }

    /* -------------------------------------- */
    public void drawDihedral(WingCanvas c, Graphics g) {
        float x0, x1;
        float span0, span1;

        g.setColor(Color.red);

        span0 = 0;
        x0 = -root;

        for (int i = 1; i < nSect; i++) {
            span1 = span0 + span[i];
            x1 = getHeight(span1) - root;

            float y0 = getTrueY(span0);
            float y1 = getTrueY(span1);

            c.plotLine(g, y0, x0, y1, x1);

            c.plotLine(g, y1, x1, y1, -root);
            c.plotArrow(g, y1, x1, 'n');
            c.plotArrow(g, y1, -root, 's');

            c.plotTextXY(g, Float.toString(y1 + root), x1, (y1 - root) / 2, 'w');

            span0 = span1;
            x0 = x1;
        }
    }

    /* -------------------------------------- */
    public void drawSpanDim(WingCanvas c, Graphics g) {
        g.setColor(Color.black);

        float trueHalfSpan = getTrueY(wingSpan / 2);

        /* halfspan */
        float yPos = yMin * (float) 1.05;
        c.plotLine(g, 0, yPos, trueHalfSpan, yPos);
        c.plotArrow(g, 0, yPos, 'w');
        c.plotArrow(g, trueHalfSpan, yPos, 'e');

        c.plotTextXY(g, Float.toString(trueHalfSpan),
                trueHalfSpan / 2, yPos, 'n');

        /* root */
        float xPos = -xMax * (float) 0.02;
        c.plotLine(g, xPos, 0, xPos, -root);
        c.plotArrow(g, xPos, 0, 'n');
        c.plotArrow(g, xPos, -root, 's');

        c.plotTextXY(g, Float.toString(root), xPos, -root / 2, 'w');

        /* tip */
        float tipTe = -root;
        for (int i = 1; i < nSect; i++) {
            tipTe -= sweep[i];
        }

        float tip = chord[nSect - 1];
        float tipLe = tipTe + tip;

        xPos = xMax * (float) 1.02;
        xPos = trueHalfSpan * (float) 1.02;
        c.plotLine(g, xPos, tipLe, xPos, tipTe);
        c.plotArrow(g, xPos, tipLe, 'n');
        c.plotArrow(g, xPos, tipTe, 's');

        c.plotTextXY(g, Float.toString(tip), xPos, (tipLe + tipTe) / 2, 'e');
    }

    /* -------------------------------------- */
    public void drawFlap(WingCanvas c, Graphics g) {
        if (flapPosR > 0) {
            float flapChL = getChord(flapPosL);
            float flapTeL = getTe(flapPosL);
            float flapLeL = flapTeL + (float) (0.1 * flapChL);

            float flapChR = getChord(flapPosR);
            float flapTeR = getTe(flapPosR);
            float flapLeR = flapTeR + (float) (0.1 * flapChR);

            g.setColor(Color.green);

            c.plotLine(g, flapPosL, flapTeL, flapPosL, flapLeL);
            c.plotLine(g, flapPosL, flapLeL, flapPosR, flapLeR);
            c.plotLine(g, flapPosR, flapTeR, flapPosR, flapLeR);

            /*
             c.plotLine (g, flapPosL, flapLeL,  flapPosR, flapTeR);
             c.plotLine (g, flapPosL, flapTeL,  flapPosR, flapLeR);
             */
        }
    }

    /* -------------------------------------- */
    public void drawAileron(WingCanvas c, Graphics g) {
        if (aileronPosR > 0) {
            float aileronChL = getChord(aileronPosL);
            float aileronTeL = getTe(aileronPosL);
            float aileronLeL = aileronTeL + (float) (0.1 * aileronChL);

            float aileronChR = getChord(aileronPosR);
            float aileronTeR = getTe(aileronPosR);
            float aileronLeR = aileronTeR + (float) (0.1 * aileronChR);

            g.setColor(Color.red);

            c.plotLine(g, aileronPosL, aileronTeL, aileronPosL, aileronLeL);
            c.plotLine(g, aileronPosL, aileronLeL, aileronPosR, aileronLeR);
            c.plotLine(g, aileronPosR, aileronTeR, aileronPosR, aileronLeR);

            /*
             c.plotLine (g, aileronPosL, aileronLe,  aileronPosR, aileronTe);
             c.plotLine (g, aileronPosL, aileronTe,  aileronPosR, aileronLe);
             */
        }
    }

    /* -------------------------------------- */
    public void drawWing(WingCanvas c, Graphics g) {
        drawPlanform(c, g);
        drawDihedral(c, g);
        drawSpanDim(c, g);
        drawFlap(c, g);
        drawAileron(c, g);
    }

    /* ------------------------------------------------ */
    public void drawMac(WingCanvas c, Graphics g) {
        float rootX = 0;
        float rootLe = 0;
        float rootTe = -root;

        float areaSct;
        float macChSum;
        float macXSum;
        float macLeSum;
        float area;

        DecimalFormat number = new DecimalFormat("###0.0;-###.0");
        DecimalFormat f7_3 = new DecimalFormat(" #0.000;-#.000");
        DecimalFormat f6_3 = new DecimalFormat(" #.000;-#.000");
        DecimalFormat f7_2 = new DecimalFormat(" ##.00;-##.00");

        g.setColor(Color.green);

        areaSct = 0;
        macChSum = 0;
        macXSum = 0;
        macLeSum = 0;
        area = 0;

        for (int i = 1; i < nSect; i++) {
            float tipX = rootX + span[i];

            float tipTe = rootTe - sweep[i];
            float tipLe = tipTe + chord[i];

            /* determine section mac */
            float macChord = Mac.chord(rootLe - rootTe, chord[i]);
            float macLoc = Mac.spanLoc(rootLe - rootTe, chord[i]);

            float macX = rootX + (span[i] * macLoc);
            float macTe = rootTe - (sweep[i] * macLoc);
            float macLe = macTe + macChord;

            /* c.plotText (g, i +"  "+ macLoc +" "+  macChord +" "+  macX); */
            c.plotLine(g, macX, macTe, macX, macLe);

            /* update total mac variables */
            areaSct = (float) ((rootLe - rootTe + chord[i]) * span[i] / 2.0);
            macChSum += areaSct * macChord;
            macXSum += areaSct * macX;
            macLeSum += areaSct * macLe;
            area += areaSct;

            /* update root with current tip */
            rootX = tipX;
            rootLe = tipLe;
            rootTe = tipTe;
        }

        /* determine mac for complete span */
        float macChord = macChSum / area;
        float macX = macXSum / area;
        float macLe = macLeSum / area;

        float macPos = (float) (macLe - (macFrac * macChord));

        g.setColor(Color.black);
        if (english) {
            c.plotText(g, "MAC = " + number.format(macChord));
        } else {
            c.plotText(g, "MAC = " + f7_3.format(macChord));
        }

        /* display Mac info graphically */
        drawPlanform(c, g);

        g.setColor(Color.red);
        c.plotLine(g, 0, macPos, macX, macPos);
        c.plotTextXY(g, " " + number.format(macPos), 0, macPos, 'w');
        c.plotLine(g, macX, macLe, macX, macLe - macChord);

        g.setColor(Color.lightGray);
        c.plotLine(g, 0, macLe, macX, macLe);
        c.plotLine(g, 0, macLe - macChord, macX, macLe - macChord);

        /* display other info */
        g.setColor(Color.black);

        if (english) {
            c.plotText(g, number.format(this.area) + " area");
            c.plotText(g, number.format(wingSpan) + " wingspan");
            c.plotText(g, number.format(aspectRatio) + " A/R");

            wingLoading = weight / (this.area / 144);
        } else {
            c.plotText(g, f7_3.format(this.area) + " area");
            c.plotText(g, f7_2.format(wingSpan) + " wingspan");
            c.plotText(g, number.format(aspectRatio) + " A/R");

            wingLoading = weight / (this.area / 1);
        }

        c.plotText(g, number.format(wingLoading) + " wing-loading");
    }
}
