package vlm;

/*
 */

import java.awt.*;
import java.lang.*;

import java.text.*;
import java.lang.Math;

//NN import Vector.*;

/*------------------------------------------------------------------*/
class WingCanvas extends Canvas {

    WingApp applet = null;
    int width, height;
    float xScale, yScale;
    int xOff, yOff;
    int textPos = 5;
    boolean planScaling = false;
    boolean equalAxisScaling = false;
    float axisMinX;
    float axisMinY;
    float axisMaxX;
    float axisMaxY;
    boolean skip = false;
    float leftOver = 0;
    Vector lastVec = new Vector(0);
    int mouseFlag;
    double zoom = (double) 2 / 3;
    int centerY;
    Color colorList[] = {Color.blue, Color.magenta,
        Color.cyan, Color.orange, Color.green, Color.pink};
    DecimalFormat f53 = new DecimalFormat(" 0.000;-0.000");
    DecimalFormat f94 = new DecimalFormat(" ####0.0000;-####0.0000");
    public boolean printFlag = false;

    /* ------------------------------------------------ */
    public WingCanvas(WingApp applet) {
        this.applet = applet;
        Rectangle r = getBounds();

        this.width = r.width;
        this.height = r.height;
    }

    /* ------------------------------------------------ */
    public void setScale(float xMin, float yMin, float xMax, float yMax) {
        Rectangle r = getBounds();

        this.width = r.width;
        this.height = r.height;

        xScale = (float) (r.width * 0.7) / Math.abs(xMax - xMin);
        yScale = (float) (r.height * 0.8) / Math.abs(yMax - yMin);

        if (equalAxisScaling) {
            xScale = yScale = Math.min(xScale, yScale);
        }

        xOff = (r.width / 10) - (int) (xMin * xScale);
        yOff = (int) (r.height * 0.9) + (int) (yMin * yScale);
    }

    public void plotLine(Graphics g, float x0, float y0, float x1, float y1) {
        g.drawLine(
                (int) (x0 * xScale) + xOff, yOff - (int) (y0 * yScale),
                (int) (x1 * xScale) + xOff, yOff - (int) (y1 * yScale));

        /* plotText (g, " "+ x0 +","+ y0 +"  "+ x1 +","+ y1); */
    }

    public void plotArrow(Graphics g, float x, float y, char dir) {
        int xPos = (int) (x * xScale) + xOff;
        int yPos = -(int) (y * yScale) + yOff;

        int wid = 3;
        int len = 8;

        switch (dir) {
            case 'n':
                g.drawLine(xPos, yPos, xPos - wid, yPos + len);
                g.drawLine(xPos, yPos, xPos + wid, yPos + len);
                break;

            case 's':
                g.drawLine(xPos, yPos, xPos - wid, yPos - len);
                g.drawLine(xPos, yPos, xPos + wid, yPos - len);
                break;

            case 'e':
                g.drawLine(xPos, yPos, xPos - len, yPos - wid);
                g.drawLine(xPos, yPos, xPos - len, yPos + wid);
                break;

            case 'w':
                g.drawLine(xPos, yPos, xPos + len, yPos - wid);
                g.drawLine(xPos, yPos, xPos + len, yPos + wid);
                break;
        }
    }

    /* ---------------------------------------------------------- */
    private float drawX(Graphics g, float x0, float y0, float x1, float y1,
            float seg, boolean skip) {

        float dx = x1 - x0;
        float dy = y1 - y0;

        // this code assumes dx > 0

        if (seg - dx > 0) {
            seg = dx;
        }

        y1 = y0 + dy * (seg / dx);
        x1 = x0 + seg;

        if (!skip) {
            plotLine(g, x0, y0, x1, y1);
        }

        return seg;
    }

    //----------------------------------------
    private float drawY(Graphics g, float x0, float y0, float x1, float y1,
            float seg, boolean skip) {

        float dx = x1 - x0;
        float dy = y1 - y0;

        if (dy > 0) {
            if (seg - dy > 0) {
                seg = dy;
            }
        } else {
            if (seg + dy > 0) {
                seg = dy;
            } else {
                seg = -seg;
            }
        }

        x1 = x0 + dx * (seg / dy);
        y1 = y0 + seg;

        if (!skip) {
            plotLine(g, x0, y0, x1, y1);
        }

        return seg;
    }

    public void plotDash(Graphics g, float x0, float y0, float x1, float y1,
            int dash) {
        plotDash(g, x0, y0, x1, y1, dash, dash);
    }

    public void plotDash(Graphics g, float x0, float y0, float x2, float y2,
            int dash, int space) {

        float dx = x2 - x0;
        float dy = y2 - y0;
        double m = dy / dx;
        float M = (dy * yScale) / (dx * xScale);
        float seg, dSeg;

        if (leftOver <= 0) {
            if (skip) {
                leftOver = space;
            } else {
                leftOver = dash;
            }
        }

        if (-1 < M && M < 1) {
            seg = leftOver / xScale;
            while (x0 < x2) {
                dSeg = drawX(g, x0, y0, x2, y2, seg, skip);
                // plotText (g, i + " seg "+ seg +" dx "+ dx);

                y0 += dy * (dSeg / dx);
                x0 += dSeg;
                seg -= dSeg;
                if (seg <= 0) {
                    skip = !skip;
                    if (skip) {
                        seg = space / xScale;
                    } else {
                        seg = dash / xScale;
                    }
                }
            }

            leftOver = seg * xScale;
        } else {
            seg = leftOver / yScale;
            while (y0 != y2) {

                // sSeg can be +/-
                dSeg = drawY(g, x0, y0, x2, y2, seg, skip);

                x0 += dx * (dSeg / dy);
                y0 += dSeg;
                seg -= (dSeg > 0 ? dSeg : -dSeg);

                if (seg <= 0) {
                    skip = !skip;
                    if (skip) {
                        seg = space / yScale;
                    } else {
                        seg = dash / yScale;
                    }
                }
            }

            leftOver = seg * yScale;
        }
    }

    /* --------------------------------------------------------------- */
    public void plotText(Graphics g, String s) {
        int xPos = (int) (width * 0.82);

        if (printFlag) {
            System.out.println();
            System.out.println(s);
        }

        if (xPos < 0) {
            xPos = 0;
        }

        g.drawString(s, xPos, textPos += 15);
    }

    public void plotText(Graphics g, String s, Color color) {
        g.setColor(color);
        plotText(g, s);
    }

    public void plotTextXY(Graphics g, String s, float x, float y, char pos) {
        int len = s.length();
        int xAdj = 0;
        int yAdj = 0;

        switch (pos) {
            case 'n':
                xAdj -= 6 * len / 2;
                break;

            case 's':
                xAdj -= 6 * len / 2;
                yAdj += 12;
                break;

            case 'e':
                yAdj += 12 / 2;
                break;

            case 'w':
                yAdj += 12 / 2;
                xAdj -= 6 * len;
                break;
        }

        g.drawString(s,
                (int) (x * xScale) + xOff + xAdj,
                yOff - (int) (y * yScale) + yAdj);
    }

    public void plotTextWH(Graphics g, String s, float x, float y, char pos) {
        int len = s.length();
        int xAdj = 0;
        int yAdj = 0;

        switch (pos) {
            case 'n':
                xAdj -= 6 * len / 2;
                break;

            case 's':
                xAdj -= 6 * len / 2;
                yAdj += 12;
                break;

            case 'e':
                yAdj += 12 / 2;
                break;

            case 'w':
                yAdj += 12 / 2;
                xAdj -= 6 * len;
                break;
        }

        g.drawString(s, (int) x, (int) y);
    }

    /* ------------------------------------------------ */
    public float setUpperLimit(float maxVal) {
        float negative = (float) 1.0;
        int expt = 0;
        float limit;

        if (maxVal == 0) {
            return maxVal;
        } else if (maxVal < 0) {
            negative = (float) -1.0;
            maxVal *= negative;
        }


        while (maxVal < 10) {
            maxVal *= 10;
            expt--;
        }
        while (maxVal > 100) {
            maxVal /= 10;
            expt++;
        }

        if (maxVal > 50) {
            limit = ((int) (maxVal / 10)) + 1;
        } else if (maxVal > 20) {
            limit = (float) 0.1 * (int) (maxVal + 2);
        } else {
            limit = (float) 0.1 * (int) (maxVal + 1);
        }

        expt++;

        limit *= Math.pow(10, expt) * negative;

        return limit;
    }

    /* ------------------------------------------------ */
    public float setLowerLimit(float maxVal) {
        float negative = (float) 1.0;
        int expt = 0;
        float limit;

        if (maxVal == 0) {
            return maxVal;
        } else if (maxVal < 0) {
            negative = (float) -1.0;
            maxVal *= negative;
        }


        while (maxVal < 10) {
            maxVal *= 10;
            expt--;
        }
        while (maxVal > 100) {
            maxVal /= 10;
            expt++;
        }

        if (maxVal > 50) {
            limit = ((int) (maxVal / 10)) - 0;
        } else if (maxVal > 20) {
            limit = (float) 0.1 * (int) (maxVal - 1);
        } else {
            limit = (float) 0.1 * (int) (maxVal - 0);
        }

        expt++;

        limit *= Math.pow(10, expt) * negative;

        return limit;
    }

    /* ------------------------------------------------ */
    public void setAxis(Vector v) {
        axisMinX = v.minX();
        axisMaxX = v.maxX();
        axisMinY = v.minY();
        axisMaxY = v.maxY();

        if (axisMinY > 0) {
            axisMinY = 0;
        }
    }

    /* ------------------------------------------------ */
    public void updateAxis(Vector v) {
        float minX = v.minX();
        float minY = v.minY();
        float maxX = v.maxX();
        float maxY = v.maxY();

        axisMinX = Math.min(axisMinX, minX);
        axisMinY = Math.min(axisMinY, minY);
        axisMaxX = Math.max(axisMaxX, maxX);
        axisMaxY = Math.max(axisMaxY, maxY);

        if (axisMinY > 0) {
            axisMinY = 0;
        }
    }

    /* ------------------------------------------------ */
    public void drawAxis(Graphics g) {
        drawAxis(g, axisMinX, axisMinY, axisMaxX, axisMaxY);
    }

    /* ------------------------------------------------ */
    public void drawAxis(Graphics g,
            float minX, float minY, float maxX, float maxY) {

        /* check for zoom-in */
        if (mouseFlag > 0) {
            double scale;
            float delta;
            float newMax;
            float newMin;

            scale = zoom;
            for (int i = 1; i <= mouseFlag; i++) {
                scale *= zoom;
            }

            delta = (maxY - minY);

            float ctrY = maxY - (delta * centerY / height);

            delta *= (float) scale;

            newMax = ctrY + delta / 2;
            newMin = ctrY - delta / 2;

            if (newMin < minY) {
                maxY = minY + delta;
            } else if (newMax > maxY) {
                minY = maxY - delta;
            } else {
                maxY = newMax;
                minY = newMin;
            }
        }

        // set axis limits
        maxX = maxX > 0 ? setUpperLimit(maxX) : setLowerLimit(maxX);
        minX = minX < 0 ? setUpperLimit(minX) : setLowerLimit(minX);

        maxY = maxY > 0 ? setUpperLimit(maxY) : setLowerLimit(maxY);
        minY = minY < 0 ? setUpperLimit(minY) : setLowerLimit(minY);


        // some initialization
        setScale(minX, minY, maxX, maxY);

        boolean grid = true;

        /* look at scale of values */
        float max = maxY > -minY ? maxY : -minY;
        DecimalFormat number;

        if (max >= 100) {
            number = new DecimalFormat(" ###0;-###0");
        } else if (max >= 10) {
            number = new DecimalFormat("##0.0;-##0.0");
        } else if (max >= 1) {
            number = new DecimalFormat("#0.00;-#0.00");
        } else {
            number = new DecimalFormat(".000;-.000");
        }

        /* y-axis */
        final int Ny = 10;
        float yDelta = (maxY - minY) / Ny;
        float yPos = minY;

        for (int i = 0; i < Ny; i++) {
            if (grid) {
                g.setColor(Color.darkGray);
                plotLine(g, minX, yPos, maxX, yPos);
            }

            g.setColor(Color.black);
            plotTextXY(g, number.format(yPos), minX, yPos, 'w');

            yPos += yDelta;
        }

        plotLine(g, minX, minY, minX, maxY);
        plotTextXY(g, number.format(maxY), minX, maxY, 'w');

        g.setColor(Color.darkGray);
        plotLine(g, minX, maxY, maxX, maxY);

        /* look at scale of values */
        max = maxX > -minX ? maxX : -minX;

        if (max >= 100) {
            number = new DecimalFormat(" ###0;-###0");
        } else if (max >= 10) {
            number = new DecimalFormat("##0.0;-##0.0");
        } else if (max >= 1) {
            number = new DecimalFormat("#0.00;-#0.00");
        } else {
            number = new DecimalFormat(".000;-.000");
        }

        /* x-axis */
        float xDelta = (float) ((maxX - minX) / 10.0);

        for (float x = minX; x < maxX; x += xDelta) {
            if (grid) {
                g.setColor(Color.darkGray);
                plotLine(g, x, minY, x, maxY);
            }

            g.setColor(Color.black);
            plotTextXY(g, number.format(x), x, minY, 's');
        }

        plotLine(g, minX, minY, maxX, minY);
        plotTextXY(g, number.format(maxX), maxX, minY, 's');

        g.setColor(Color.darkGray);
        plotLine(g, maxX, minY, maxX, maxY);

        /* clip region */
        int x = (int) (minX * xScale) + xOff + 1;
        int y = yOff - (int) (maxY * yScale) + 1;
        int w = (int) ((maxX - minX) * xScale);
        int h = (int) ((maxY - minY) * yScale);

        // g.clipRect (0, y, width, h);
//		g.clipRect (0, 0, width, h);
    }

    /* ------------------------------------------------ */
    public void drawVec(Graphics g, Vector v, Color color, int dash) {
        g.setColor(color);

        if (printFlag) {
            System.out.println(f94.format(v.x[0]) + " " + f94.format(v.y[0]));
        }

        for (int i = 1; i < v.size; i++) {
            plotDash(g, v.x[i - 1], v.y[i - 1], v.x[i], v.y[i], 4);

            if (printFlag) {
                System.out.println(f94.format(v.x[i])
                        + " " + f94.format(v.y[i]));
            }
        }
    }

    /* ------------------------------------------------ */
    public void drawVec(Graphics g, Vector v, Color color) {
        g.setColor(color);

        if (printFlag) {
            System.out.println(f94.format(v.x[0]) + " " + f94.format(v.y[0]));
        }

        for (int i = 1; i < v.size; i++) {
            plotLine(g, v.x[i - 1], v.y[i - 1], v.x[i], v.y[i]);
            if (printFlag) {
                System.out.println(f94.format(v.x[i])
                        + " " + f94.format(v.y[i]));
            }
        }
    }

    /* ------------------------------------------------ */
    public void drawVector(Graphics g, Vector v, Color color, boolean drawAxis) {
        float minX, minY, maxX, maxY;

        if (v.size == 0) {
            plotText(g, "Vector empty");
            return;
        }
        if (drawAxis) {
            setAxis(v);
            drawAxis(g);
        }

        drawVec(g, v, color);
    }

    /* ------------------------------------------------ */
    public void paint(Graphics g) {
        applet.paintScreen(g);
    }

    /* ------------------------------------------------ */
    public void updateScreen() {
        mouseFlag = 0;
        repaint();
    }

    /* ------------------------------------------------ */
    public boolean mouseDown(Event e, int x, int y) {
        centerY = y;

        if (e.modifiers == Event.META_MASK) {
            if (mouseFlag > 0) {
                mouseFlag--;
            }
        } else {
            mouseFlag++;
        }

        repaint();
        return true;
    }
}
