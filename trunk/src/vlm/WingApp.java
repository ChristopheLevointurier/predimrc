package vlm;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.List;

import java.text.*;
import java.util.*;

//NN import AirfoilDb.*;
//NN import WingCanvas.*;

/*------------------------------------------------------------------*/
public class WingApp extends Applet {

    WingCanvas canvas;     // The drawing area to display arcs
    Planform planform;
    Screen screen;

    public void init() {
        setLayout(new BorderLayout());

        add("Center", canvas = new WingCanvas(this));

        planform = new Planform();
        add("South", screen = new Screen(this, canvas, planform));
    }

    public void paintScreen(Graphics g) {
        screen.paintScreen(g);
    }
}

/*------------------------------------------------------------------*/
class AoaSeq {

    float beg;
    float end;
    float step;

    /* ------------------------------------------------ */
    public AoaSeq() {
        this.beg = (float) 0.0;
        this.end = (float) 0.0;
        this.step = (float) 0.0;
    }
}

/*------------------------------------------------------------------*/
class Screen extends Panel implements ActionListener, TextListener {

    Applet applet = null;
    WingCanvas canvas;
    Planform pf;
    boolean saved = false;
    Vector saveLift;
    Vector saveCl;
    Vector saveCd;
    Vector saveRN;
    float saveTotalLift;
    float saveGoodness;
    float saveAirspeed;
    AoaSeq aSeq;
    PopupMenu lrMenu = null;
    PopupMenu structMenu = null;
    PopupMenu airfoilMenu = null;
    PopupMenu saveMenu = null;
    Color colors[] = new Color[20];
    int nColors = 0;
    TextArea textArea;
    List airfoilList;
    AirfoilDb airfoilDb = null;
    Airfoil airfoil = null;
    String view = null;
    boolean textChanged = true;
    Panel pb = null;
    String line[] = {
        "english;",
        "aseq=1.0,10.0,2.0;",
        "bank=30.0; dragToLift=0.00;",
        "weight=1.35;",
        "root=8.2;                   airfoil:0=AG_16;",
        "chord:1=7.5;    span:1=15;  airfoil:1=AG_17;",
        "chord:2=5.8;    span:2=16;  airfoil:2=AG_18;",
        "chord:3=3.4;    span:3=8.3; airfoil:3=AG_19;",
        "nPanels=32;",
        "printFlag=0;"
    };
    DecimalFormat f8_0 = new DecimalFormat(" #######0;-#######0");
    DecimalFormat f7_4 = new DecimalFormat(" #.0000;-#.0000");
    DecimalFormat f7_3 = new DecimalFormat(" #0.000;-#.000");
    DecimalFormat f7_2 = new DecimalFormat(" ##0.00;-##.00");
    DecimalFormat f7_1 = new DecimalFormat(" ###0.0;-###.0");
    DecimalFormat f6_3 = new DecimalFormat(" ##0.000;-##0.000");
    DecimalFormat f6_1 = new DecimalFormat(" ##0.0;-##0.0");

    // ------------------------------------------------------------
    private void buildPopup(PopupMenu popup, String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            MenuItem mi = new MenuItem(strings[i]);
            mi.setActionCommand(strings[i]);
            mi.addActionListener(this);
            popup.add(mi);
        }

        this.add(popup);
    }

    // ------------------------------------------------------------
    private void buildMenus() {
        /* add buttons */
        Button b = null;

        Panel p = new Panel();
        p.setLayout(new GridLayout(7, 2, 6, 0));

        String[] buttons = new String[]{
            "Structure", "Lift-Dist", "Airfoils", "Save", "Clear", "Refresh"
        };

        for (int i = 0; i < buttons.length; i++) {
            b = new Button(buttons[i]);
            b.addActionListener(this);
            p.add(b);
        }

        this.add(p);

        /* add Structure menu */
        String[] structCmds = new String[]{
            "Draw", "MAC", "Shear", "Bend", "Panels"};
        structMenu = new PopupMenu();
        buildPopup(structMenu, structCmds);

        /* add Lift-Dist menu */
        String[] lrCmds = new String[]{
            "Alpha", "Lift", "Cl", "Cd", "Cl & Cd", "Drag",
            "Reynolds", "Chord-Vel"};
        lrMenu = new PopupMenu();
        buildPopup(lrMenu, lrCmds);

        /* add Airfoils menu */
        String[] airfoilCmds = new String[]{
            "Polars", "L/D", "Polars L/D"};
        airfoilMenu = new PopupMenu();
        buildPopup(airfoilMenu, airfoilCmds);

        /* add Save menu */
        String[] saveCmds = new String[]{
            "Save", "Clear"};
        saveMenu = new PopupMenu();
        buildPopup(saveMenu, saveCmds);
    }

    // ------------------------------------------------------------
    public Screen(Applet applet, WingCanvas canvas, Planform pf) {

        this.applet = applet;
        this.canvas = canvas;
        this.pf = pf;

        /* add menus */
        buildMenus();

        /* add text area */
        String spec = "";
        //	for (int i = 0; i < nLine; i++)
        for (int i = 0; i < line.length; i++) {
            spec += line[i] + "\n";
        }

        /* create airfoil list and create airfoil-Database */
        airfoilList = new List(11, false);
        this.add(airfoilList);

        airfoilDb = new AirfoilDb("", airfoilList, applet, textArea);

        /* create text panel for planform spec */
        textArea = new TextArea(spec, 10, 40);
        textArea.addTextListener(this);
        this.add(textArea);

        // create color list
        colors[nColors++] = Color.red;
        colors[nColors++] = Color.blue;
        colors[nColors++] = Color.orange;
        colors[nColors++] = Color.green;
        colors[nColors++] = Color.yellow;

        // colors [nColors++] = Color.lightGray;
        colors[nColors++] = Color.pink;
        colors[nColors++] = Color.cyan;
        colors[nColors++] = Color.magenta;

        /* perform initial processing */
        if (true) {
            textChanged = false;
            parseArea();
            view = "Draw";
            canvas.updateScreen();
        }
    }

    // ------------------------------------------------------------
    public void textValueChanged(TextEvent ev) {
        textChanged = true;
    }

    // ------------------------------------------------
    public void actionPerformed(ActionEvent ev) {
        String label = ev.getActionCommand();

        // System.out.println ("actionPerformed: " + label);

        if (label.equals("Airfoils")) {
            airfoilMenu.show(this, 100, 0);
        } else if (label.equals("Lift-Dist")) {
            lrMenu.show(this, 100, 0);
        } else if (label.equals("Structure")) {
            structMenu.show(this, 100, 0);
        } else if (label.equals("Save")) {
            Save();
            updateCanvas(view);
        } else if (label.equals("Clear")) {
            Clear();
            updateCanvas(view);
        } else if (label.equals("Refresh")) {
            System.out.println("#------------ refresh --------------");
            updateCanvas(view);
        } else {
            updateCanvas(label);
        }
    }

    // --------------------------------------------------
    private void updateCanvas(String label) {
        if (airfoilList.getSelectedItem() != null) {
            updateDrag();
        }

        if (textChanged) {
            parseArea();
            textChanged = false;
        }

        view = label;
        canvas.updateScreen();
    }

    // --------------------------------------------------
    private void updateDrag() {
        String name = airfoilList.getSelectedItem();

        if (pf.weight > 0) {
            pf.vlm.calcVlm(canvas, pf, pf.nPanels, null);

            if (pf.vlm.totalLift > 0) {
                pf.airspeed *= Math.sqrt(pf.weight / pf.vlm.totalLift);
                pf.vlm.calcVlm(canvas, pf, pf.nPanels, null);

                if (false) {
                    System.out.println("updateDrag: "
                            + "  weight " + pf.weight
                            + ", lift " + pf.vlm.totalLift
                            + ", airSpd " + pf.airspeed
                            + ", mult "
                            + Math.sqrt(pf.weight / pf.vlm.totalLift));
                }
            }
        }

        if (name == null) {
            System.out.println("updateDrag: no airfoil selected");
            return;
        }

        pf.airfoilName = name;
        pf.airfoil = airfoilDb.getAirfoil(pf.airfoilName);
        pf.updateDrag();
    }

    // ------------------------------------------------
    public void paintScreen(Graphics g) {
        canvas.textPos = 5;

        if (view.equals("Draw")) {
            drawWing(g);
        } else if (view.equals("MAC")) {
            drawMac(g);
        } else if (view.equals("Lift")) {
            drawLift(g);
        } else if (view.equals("Cl")) {
            drawCl(g);
        } else if (view.equals("Cd")) {
            drawCd(g);
        } else if (view.equals("Cl & Cd")) {
            drawClCd(g);
        } else if (view.equals("Drag")) {
            drawDrag(g);
        } else if (view.equals("Shear")) {
            drawShear(g);
        } else if (view.equals("Bend")) {
            drawBend(g);
        } else if (view.equals("Alpha")) {
            drawAlpha(g);
        } else if (view.equals("Reynolds")) {
            drawReynolds(g);
        } else if (view.equals("Chord-Vel")) {
            drawChordVel(g);
        } else if (view.equals("Panels")) {
            drawPanels(g);
        } else if (view.equals("Polars")) {
            drawPolars(g);
        } else if (view.equals("L/D")) {
            drawLd(g);
        } else if (view.equals("Polars L/D")) {
            drawPolarsLd(g);
        } else {
            canvas.plotText(g, "Unknown view: " + view);
        }
    }

    // ------------------------------------------------------------
    private void Save() {
        saveLift = new Vector(pf.vlm.liftDist.size);
        saveLift = pf.vlm.liftDist;

        saveCl = new Vector(pf.vlm.ClDist.size);
        saveCl = pf.vlm.ClDist;

        saveCd = new Vector(pf.CdDist.size);
        saveCd = pf.CdDist;

        saveRN = new Vector(pf.vlm.reynolds.size);
        saveRN = pf.vlm.reynolds;

        saveTotalLift = pf.vlm.totalLift;
        saveGoodness = pf.vlm.goodness;
        saveAirspeed = pf.airspeed;

        saved = true;
    }

    // ------------------------------------------------------------
    private void Clear() {

        saved = false;
    }

    // ------------------------------------------------------------
    private void drawWing(Graphics g) {
        if (canvas.planScaling) {
            canvas.equalAxisScaling = true;
        }
        pf.scale(canvas);
        pf.drawWing(canvas, g);
        canvas.equalAxisScaling = false;

        canvas.plotText(g, "");
        canvas.plotText(g, "Wing Analysis");
        canvas.plotText(g, "Ver. 2.3.1 (080617)");
    }

    // ------------------------------
    private void drawMac(Graphics g) {
        pf.scale(canvas);
        pf.drawMac(canvas, g);
    }

    // ------------------------------
    private void drawLift(Graphics g) {
        canvas.plotText(g, "Lift Distribution");

        // Update axis before drawing multiple curves
        canvas.setAxis(pf.vlm.idealDist);
        canvas.updateAxis(pf.vlm.liftDist);
        if (saved) {
            canvas.updateAxis(saveLift);
        }
        canvas.drawAxis(g);

        canvas.drawVec(g, pf.vlm.idealDist, Color.magenta, 5);
        canvas.plotText(g, "Elliptic");

        canvas.drawVector(g, pf.vlm.liftDist, Color.blue, false);

        if (pf.english) {
            canvas.plotText(g, f7_1.format(pf.vlm.totalLift)
                    + " total-lift (lbs)");
            canvas.plotText(g, f7_2.format(pf.airspeed) + " ft/sec");
        } else {
            canvas.plotText(g, f7_2.format(pf.vlm.totalLift)
                    + " total-lift (kgs)");
            canvas.plotText(g, f7_2.format(pf.airspeed) + " m/sec");
        }

        canvas.plotText(g, f6_3.format(pf.vlm.avgCl) + " avg Cl");

        if (saved) {
            canvas.drawVector(g, saveLift, Color.cyan, false);

            if (pf.english) {
                canvas.plotText(g, f7_1.format(saveTotalLift)
                        + " total-lift (lbs)");
                canvas.plotText(g, f7_2.format(saveAirspeed) + " ft/sec");
            } else {
                canvas.plotText(g, f7_2.format(saveTotalLift)
                        + " total-lift (kgs)");
                canvas.plotText(g, f7_2.format(saveAirspeed) + " m/sec");
            }

            canvas.plotText(g, f6_3.format(saveCl.avgY()) + " avg Cl");
        }
    }

    // ------------------------------
    private void drawCl(Graphics g) {
        canvas.plotText(g, "Cl Distribution");

        // Update axis before drawing multiple curves
        canvas.setAxis(pf.vlm.ClDist);
        if (saved) {
            canvas.updateAxis(saveCl);
        }
        canvas.drawAxis(g);

        canvas.drawVector(g, pf.vlm.ClDist, Color.blue, false);

        canvas.plotText(g, f6_3.format(pf.vlm.goodness) + " avg/max Cl");
        canvas.plotText(g, f6_3.format(pf.vlm.ClDist.maxY()) + " max Cl");
        canvas.plotText(g, f6_3.format(pf.vlm.ClDist.avgY()) + " avg Cl");
        canvas.plotText(g, f6_3.format(pf.vlm.ClDist.minY()) + " min Cl");

        if (saved) {
            canvas.drawVector(g, saveCl, Color.cyan, false);

            canvas.plotText(g, f6_3.format(saveGoodness) + " avg/max Cl");
            canvas.plotText(g, f6_3.format(saveCl.maxY()) + " max Cl");
            canvas.plotText(g, f6_3.format(saveCl.avgY()) + " avg Cl");
            canvas.plotText(g, f6_3.format(saveCl.minY()) + " min Cl");
        }
    }

    // ------------------------------
    private void drawCd(Graphics g) {
        canvas.plotText(g, "Cd Distribution");

        // Update axis before drawing multiple curves
        canvas.setAxis(pf.CdDist);
        if (saved) {
            canvas.updateAxis(saveCd);
        }
        canvas.drawAxis(g);

        canvas.drawVector(g, pf.CdDist, Color.blue, false);

        canvas.plotText(g, f6_3.format(pf.CdDist.maxY()) + " max Cd");
        canvas.plotText(g, f6_3.format(pf.CdDist.avgY()) + " avg Cd");
        canvas.plotText(g, f6_3.format(pf.CdDist.minY()) + " min Cd");

        if (saved) {
            canvas.drawVector(g, saveCd, Color.cyan, false);

            canvas.plotText(g, f6_3.format(saveCd.maxY()) + " max Cd");
            canvas.plotText(g, f6_3.format(saveCd.avgY()) + " avg Cd");
            canvas.plotText(g, f6_3.format(saveCd.minY()) + " max Cd");
        }
    }

    // ------------------------------
    private void drawDrag(Graphics g) {
        canvas.plotText(g, "Drag Profile");
        canvas.plotText(g, " (drag in oz)");
        canvas.plotText(g, " drag   Di  airspeed");

        // original code that draw a single vector
        if (aSeq == null) {
            canvas.drawVector(g, pf.dragDist, Color.blue, true);

            canvas.plotText(g, f6_3.format(pf.dragTotal)
                    + " " + f6_3.format(pf.dragInduced)
                    + " " + f6_1.format(pf.airspeed));
        } // for aoa series, collect set of vectors
        else {

            // save current aoa so it can be restored
            float savedAoa = pf.aoa;

            int i = 0;
            for (float aoa = aSeq.beg; aoa <= aSeq.end; aoa += aSeq.step) {
                pf.aoa = aoa;

                updateDrag();

                canvas.drawVector(g, pf.dragDist, colors[i],
                        pf.aoa == aSeq.beg);

                if (false) {
                    canvas.plotText(g, f6_3.format(pf.dragTotal)
                            + " total (oz)");
                    canvas.plotText(g, f6_1.format(pf.aoa)
                            + " AOA");
                    canvas.plotText(g, f6_1.format(pf.airspeed)
                            + " airSpd (ft/sec)");
                } else {
                    canvas.plotText(g, f6_3.format(pf.dragTotal)
                            + " " + f6_3.format(pf.dragInduced)
                            + " " + f6_1.format(pf.airspeed));
                }

                i = (i + 1) % nColors;
            }

            // restore aoa and reset the data vectors
            pf.aoa = savedAoa;

            updateDrag();
        }
    }

    // ------------------------------
    private void drawChordVel(Graphics g) {
        canvas.plotText(g, "Chord Velocity");

        Vector vel = new Vector(pf.vlm.nPnl);
        for (int i = 0; i < pf.vlm.nPnl; i++) {
            vel.x[i] = pf.vlm.ClDist.x[i];
            vel.y[i] = (float) pf.vlm.chordVel[i];
        }

        canvas.drawVector(g, vel, Color.blue, true);

        canvas.plotText(g, f6_1.format(vel.maxY()) + " max");
        canvas.plotText(g, f6_1.format(vel.avgY()) + " avg");
        canvas.plotText(g, f6_1.format(vel.minY()) + " min");
    }

    // ------------------------------
    private void drawClCd(Graphics g) {

        // Update axis before drawing multiple curves
        canvas.setAxis(pf.vlm.ClDist);
        if (saved) {
            canvas.updateAxis(saveCl);
        }
        canvas.updateAxis(pf.CdDist);
        if (saved) {
            canvas.updateAxis(saveCd);
        }
        canvas.drawAxis(g);

        // cross-reference with Cl
        canvas.drawVector(g, pf.vlm.ClDist, Color.blue, false);

        canvas.plotText(g, "Cl Distribution");
        canvas.plotText(g, f6_3.format(pf.vlm.goodness) + " avg/max Cl");
        canvas.plotText(g, f6_3.format(pf.vlm.ClDist.maxY()) + " max Cl");
        canvas.plotText(g, f6_3.format(pf.vlm.avgCl) + " avg Cl");

        if (saved) {
            canvas.drawVector(g, saveCl, Color.cyan, false);

            canvas.plotText(g, f6_3.format(saveGoodness) + " avg/max Cl");
            canvas.plotText(g, f6_3.format(saveCl.maxY()) + " max Cl");
            canvas.plotText(g, f6_3.format(saveCl.avgY()) + " avg Cl");
        }

        // plot drag vector
        if (pf.airfoilName == null) {
            System.out.println("drawCd: no airfoil selected");
            return;
        }

        canvas.drawVector(g, pf.CdDist, Color.red, false);

        canvas.plotText(g, "Cd Distribution");
        canvas.plotText(g, f6_3.format(pf.CdDist.maxY()) + " max Cd");
        canvas.plotText(g, f6_3.format(pf.CdDist.avgY()) + " avg Cd");

        if (saved) {
            canvas.drawVector(g, saveCd, Color.cyan, false);

            canvas.plotText(g, f6_3.format(saveCd.maxY()) + " max Cd");
            canvas.plotText(g, f6_3.format(saveCd.avgY()) + " avg Cd");
        }
    }

    // ------------------------------
    private void drawReynolds(Graphics g) {
        canvas.plotText(g, "Reynolds");

        // Update axis before drawing multiple curves
        canvas.setAxis(pf.vlm.reynolds);
        if (saved) {
            canvas.updateAxis(saveRN);
        }
        canvas.drawAxis(g);

        canvas.drawVector(g, pf.vlm.reynolds, Color.blue, false);

        float maxY = pf.vlm.reynolds.maxY();
        float avgY = pf.vlm.reynolds.avgY();
        canvas.plotText(g, f8_0.format(maxY) + " max");
        canvas.plotText(g, f8_0.format(avgY) + " avg");

        if (saved) {
            canvas.drawVector(g, saveRN, Color.cyan, false);

            canvas.plotText(g, f8_0.format(saveRN.maxY()) + " max");
            canvas.plotText(g, f8_0.format(saveRN.avgY()) + " avg");
        }
    }

    // ------------------------------
    private void drawAlpha(Graphics g) {
        canvas.plotText(g, "Alpha");
        canvas.drawVector(g, pf.vlm.alpha, Color.red, true);
    }

    // ------------------------------
    private void drawPanels(Graphics g) {
        canvas.plotText(g, "Draw Panels");
        canvas.plotText(g, f7_2.format(pf.vlm.panelWidth)
                + " panelWidth");
        pf.scale(canvas);
        pf.drawWing(canvas, g);
        pf.vlm.drawPanels(canvas, g);
    }

    // ------------------------------
    private void drawShear(Graphics g) {
        canvas.plotText(g, "Shear Distribution");
        canvas.plotText(g, "weight " + pf.weight);
        canvas.drawVector(g, pf.vlm.shear, Color.blue, true);
    }

    // ------------------------------
    private void drawBend(Graphics g) {
        canvas.plotText(g, "Bending Moment");
        canvas.plotText(g, "weight " + pf.weight);
        canvas.drawVector(g, pf.vlm.bendMoment, Color.blue, true);
    }

    // ------------------------------
    private void drawPolars(Graphics g) {
        float minX = 0;
        float maxX = 0;
        float minY = 0;
        float maxY = 0;

        Color colorList[] = {Color.blue, Color.magenta,
            Color.cyan, Color.orange, Color.green, Color.pink};

        String name = airfoilList.getSelectedItem();
        Airfoil airfoil = airfoilDb.getAirfoil(name);

        canvas.plotText(g, "Airfoil Polars");
        canvas.plotText(g, "airfoil: " + name);
        canvas.plotText(g, f8_0.format(pf.vlm.maxReynolds)
                + " max RN");
        canvas.plotText(g, f8_0.format(pf.vlm.minReynolds)
                + " min RN");

        for (int i = airfoil.nPolars - 1; i >= 0; i--) {
            for (int j = 0; j < airfoil.polar[i].nPts; j++) {
                if (minX > airfoil.polar[i].cd[j]) {
                    minX = airfoil.polar[i].cd[j];
                }
                if (maxX < airfoil.polar[i].cd[j]) {
                    maxX = airfoil.polar[i].cd[j];
                }

                if (minY > airfoil.polar[i].cl[j]) {
                    minY = airfoil.polar[i].cl[j];
                }
                if (maxY < airfoil.polar[i].cl[j]) {
                    maxY = airfoil.polar[i].cl[j];
                }
            }
        }

        canvas.drawAxis(g, minX, minY, maxX, maxY);

        for (int i = airfoil.nPolars - 1; i >= 0; i--) {
            drawPolar(g, airfoil.polar[i], colorList[i % 5], false);
            canvas.plotText(g, " " + airfoil.polar[i].reynolds);
        }
    }

    // ----------------------------------------
    public void drawPolar(Graphics g, AirfoilPolar polar,
            Color color, boolean drawAxis) {
        Vector v = new Vector(polar.nPts);

        for (int i = 0; i < polar.nPts; i++) {
            v.x[i] = polar.cd[i];
            v.y[i] = polar.cl[i];
        }

        canvas.drawVector(g, v, color, drawAxis);
    }

    // ----------------------------------------
    public void drawPolarsLd(Graphics g) {
        drawPolars(g);

        canvas.drawVector(g, pf.ld, Color.black, false);

        canvas.plotText(g, "  ");
        canvas.plotText(g, "Partial L/D");
        canvas.plotText(g, "  (wing only)");
    }

    // ----------------------------------------
    public void drawLd(Graphics g) {
        canvas.drawVector(g, pf.ld, Color.black, true);

        canvas.plotText(g, "Partial L/D");
        canvas.plotText(g, "  (wing only)");
        canvas.plotText(g, "");
        canvas.plotText(g, "Experimental");
    }

    /* ---------------------------------------------------------- */
    public void parseArea() {
        float root = 0;
        float span[] = new float[20];
        float chord[] = new float[20];
        float sweep[] = new float[20];
        int nSect = 0;
        boolean endConfig = false;

        pf.init();
        aSeq = null;

        String str = textArea.getText();
        StringTokenizer field = new StringTokenizer(str, ";");
        int nField = field.countTokens();

        for (int i = 0; i < nField; i++) {
            String name = "";
            int index = 0;
            int nTok = 0;
            boolean echo = false;

            String nameVal = field.nextToken().trim();

            if (echo) {
                textArea.appendText(nameVal + "\n");
            }

            if (nameVal.equals("")) {
                continue;
            }

            StringTokenizer nV = new StringTokenizer(nameVal, "=");
            nTok = nV.countTokens();
            String nameArr = nV.nextToken().trim();

            /* process keywords */

            if (nTok == 1) {
                if (nameArr.equals("english")) {
                    pf.setEnglish();
                } else if (nameArr.equals("metric")) {
                    pf.setMetric();
                } else if (nameArr.equals("show-results")) {
                    pf.textArea = textArea;
                } else if (nameArr.equals("end-config")) {
                    endConfig = true;
                    break;
                } else {
                    textArea.insertText(
                            "Error: missing equal " + nTok + " -- " + nameVal + "\n", 0);
                }

                continue;
            }

            /* --------- */
            StringTokenizer nArr = new StringTokenizer(nameArr, ":");

            switch (nArr.countTokens()) {
                case 1:
                    name = nameArr;
                    index = -1;
                    break;

                case 2:
                    name = nArr.nextToken();
                    index = Integer.parseInt(nArr.nextToken());
                    break;

                default:
                    textArea.insertText("Error: too many ':' - " + nameArr + "\n", 0);
                    break;
            }

            /* analyse RHS (right-hand side of assignment) */
            String rhs = new String(nV.nextToken());

            StringTokenizer rhsFlds = new StringTokenizer(rhs, ",");
            int nRhsToks = rhsFlds.countTokens();


            if (nRhsToks > 1) {
                if ((nRhsToks == 3) && (name.equals("aseq"))) {
                    String s = new String(rhsFlds.nextToken());
                    Float f = new Float(0.0);

                    if (aSeq == null) {
                        aSeq = new AoaSeq();
                    }

                    // System.out.println ("ParseArea: "+ s);
                    f = Float.valueOf(s);
                    aSeq.beg = f.floatValue();

                    s = rhsFlds.nextToken();
                    // System.out.println ("ParseArea: "+ s);
                    f = Float.valueOf(s);
                    aSeq.end = f.floatValue();

                    s = rhsFlds.nextToken();
                    // System.out.println ("ParseArea: "+ s);
                    f = Float.valueOf(s);
                    aSeq.step = f.floatValue();

                    continue;
                }

                textArea.insertText("ParseArea: " + nRhsToks + " nRhsToks\n", 0);
                continue;
            }

            /* process assignments */
            char c = rhs.charAt(0);
            Float f = new Float(0.0);

            if (('0' <= c) && (c <= '9')) {
                f = Float.valueOf(rhs);
            } else if (c == '-') {
                f = Float.valueOf(rhs);
            }

            if (nSect < index) {
                nSect = index;
            }

            if (echo) {
                if (index < 0) {
                    textArea.appendText(name + "=" + f + "\n");
                } else {
                    textArea.appendText(name + "[" + index + "]=" + f + "\n");
                }
            }

            if (name.equals("root")) {
                root = f.floatValue();
            } else if (name.equals("printFlag")) {
                if (f.floatValue() > 0) {
                    canvas.printFlag = true;
                } else {
                    canvas.printFlag = false;
                }
            } else if (name.equals("planScaling")) {
                if (f.floatValue() > 0) {
                    canvas.planScaling = true;
                } else {
                    canvas.planScaling = false;
                }
            } else if (name.equals("nPanels")) {
                pf.nPanels = (int) f.floatValue();
            } else if (name.equals("acc")) {
                pf.acc = f.floatValue();
            } else if (name.equals("airspeed")) {
                pf.airspeed = f.floatValue();
            } else if (name.equals("rho")) {
                pf.rho = f.floatValue();
            } else if (name.equals("air_density")) {
                pf.rho = f.floatValue();
            } else if (name.equals("viscosity")) {
                pf.viscosity = f.floatValue();
            } else if (name.equals("aoa")) {
                pf.aoa = f.floatValue();
            } else if (name.equals("bank")) {
                pf.bank = f.floatValue();
            } else if (name.equals("dragToLift")) {
                pf.dragToLift = f.floatValue();
            } else if (name.equals("debug")) {
                pf.debug = (int) f.floatValue();
            } else if (name.equals("mac")) {
                pf.macFrac = f.floatValue();
            } else if (name.equals("airfoil")) {
                pf.af[index] = airfoilDb.getAirfoil(rhs);
                if (pf.af[index] == null) {
                    textArea.insert("Error: unknown airfoil - " + rhs + "\n", 0);
                }
            } else if (name.equals("chord")) {
                chord[index] = f.floatValue();
            } else if (name.equals("dihedral")) {
                pf.dihedral[index] = f.floatValue();
            } else if (name.equals("span")) {
                span[index] = f.floatValue();
            } else if (name.equals("sweep")) {
                sweep[index] = f.floatValue();
//				System.out.println (" " + index + " index" + " " + sweep[index]);
            } else if (name.equals("twist")) {
                pf.twist[index] = f.floatValue();
            } else if (name.equals("weight")) {
                pf.weight = f.floatValue();
            } else if (name.equals("aileronPosL")) {
                pf.aileronPosL = f.floatValue();
            } else if (name.equals("aileronPosR")) {
                pf.aileronPosR = f.floatValue();
            } else if (name.equals("aileronAngL")) {
                pf.aileronAngL = f.floatValue();
            } else if (name.equals("aileronAngR")) {
                pf.aileronAngR = f.floatValue();
            } else if (name.equals("aileronAng")) {
                pf.aileronAngR = f.floatValue();
                pf.aileronAngL = -f.floatValue();
            } else if (name.equals("flapPosL")) {
                pf.flapPosL = f.floatValue();
            } else if (name.equals("flapPosR")) {
                pf.flapPosR = f.floatValue();
            } else if (name.equals("flapAngL")) {
                pf.flapAngL = f.floatValue();
            } else if (name.equals("flapAngR")) {
                pf.flapAngR = f.floatValue();
            } else if (name.equals("flapAng")) {
                pf.flapAngL = pf.flapAngR = f.floatValue();
            } else {
                textArea.insertText(
                        "Error: unknown parameter -- " + nameArr + "\n", 0);
            }
        }

        if (!endConfig) {
            textArea.appendText("end-config;\n");
        }

        view = "Lift-Dist";
        pf.params(canvas, root, nSect + 1, span, chord, sweep);

        updateDrag();

        // dump airfoil info
        if (false) {
            System.out.println();
            System.out.println("aifoil info:");
            System.out.println(" " + (nSect + 1) + " index");

            for (int i = 0; i <= nSect; i++) {
                if (pf.af[i] == null) {
                    System.out.println("   " + i + " null");
                } else {
                    System.out.println("   " + i + " " + pf.af[i].name);
                }
            }
        }
    }
}
