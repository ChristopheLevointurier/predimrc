/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.graphic.drawable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class DiedrePanel extends JPanel {

    private Point wingExtremum = new Point(10, 10);
  
    public DiedrePanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                movePoint(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                movePoint(e.getX(), e.getY());
            }
        });

    }

    private void movePoint(int x, int y) {


        wingExtremum.setLocation(x, y);
        repaint(0, 0, 200, 100);
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 100);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        g.setColor(Color.blue);
        g.drawString("Diedre draw", 10, 20);
        g.setColor(Color.GRAY);
        g.drawLine((int) wingExtremum.getX(), (int) wingExtremum.getY(), 180, 80);
        g.drawLine((int) wingExtremum.getX(), (int) wingExtremum.getY(), 182, 72);
        g.setColor(Color.BLACK);
        Line2D.Double l = new Line2D.Double();
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 80, 200, 80);
        g.setColor(Color.BLACK);
    }
}
