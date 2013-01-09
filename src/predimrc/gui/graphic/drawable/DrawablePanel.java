/*This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc.gui.graphic.drawable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.IModelListener;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 23 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public abstract class DrawablePanel extends JPanel implements IModelListener {

    protected Image backgroundImage;
    protected String info = "Select an object"; //TODO change to StringBuffer in the future.
    protected String infoAction = " to modify its value."; //TODO change to StringBuffer in the future.
    public static final int MID_FRONT_SCREEN_X = 200;
    public static final int MID_FRONT_SCREEN_Y = 100;
    public static final int MID_LEFT_SCREEN_X = 200;
    public static final int MID_LEFT_SCREEN_Y = 100;
    public static final int MID_TOP_SCREEN_X = 460;
    public static final int MID_TOP_SCREEN_Y = 150;
    protected ArrayList<DrawablePoint> points = new ArrayList<>();
    protected DrawablePoint selectedPoint = new DrawablePoint(0, 0);
    protected VIEW_TYPE view;

    protected void getNearestPoint(int x, int y) {
        double dist = Double.MAX_VALUE;
        // System.out.println("getNearestPoint:" + points.size());
        for (DrawablePoint p : points) {
            double temp = PredimRC.distance(p, x, y);
            if (p.isSelectable() && temp < dist) {
                //    System.out.println("selectedPoint:" + p.toStringAll());
                dist = temp;
                selectedPoint.setSelected(false);
                selectedPoint = p;
                selectedPoint.setSelected(true);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(220, 220, 255));
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), this);
        }
        g.setColor(Color.blue);
        g.drawString(info + infoAction, 10, 20);
        g.setColor(Color.GRAY.brighter());
        if (PredimRC.initDone) {
            PredimRC.getInstanceDrawableModel().draw((Graphics2D) g, view);
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        PredimRC.logDebugln("changeModel in Panel:"+view);
        points = PredimRC.getInstanceDrawableModel().getPoints(view);
        repaint();
    }
}
