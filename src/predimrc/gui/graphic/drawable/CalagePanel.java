/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.graphic.drawable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import javax.swing.BorderFactory;
import predimrc.PredimRC;
import predimrc.gui.graphic.DrawablePanel;
import predimrc.model.Model;

/**
 *
 * @author Christophe Levointurier, 11 déc. 2012
 * @version
 * @see
 * @since
 */
public class CalagePanel extends DrawablePanel {

    private Point wingPos = new Point(100, 50);

    public CalagePanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));
        setSize(getPreferredSize());
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
       backgroundImage = PredimRC.getImage("pegleft.png");
 
    }

    private void movePoint(int x, int y) {


        wingPos.setLocation(x, y);
        repaint(0, 0, 400, 100);
    }

     @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.blue);
        g.drawString("Calage draw", 10, 20);
        g.setColor(Color.gray);
        g.drawRect((int) wingPos.getX(), (int) wingPos.getY(), 20, 12);
        g.setColor(Color.BLACK);
        Line2D.Double l = new Line2D.Double();
        ((Graphics2D) g).setStroke(predimrc.PredimRC.dashed);
        g.drawLine(0, 60, 400, 60);
        g.setColor(Color.BLACK);
    }

    @Override
    public void changeModel(Model m) {
    }
}
