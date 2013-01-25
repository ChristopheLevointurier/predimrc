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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import predimrc.PredimRC;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.IModelListener;
import predimrc.gui.graphic.drawable.model.DrawableInfo;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.DrawablePoint;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;

/**
 *
 * @author Christophe Levointurier, 23 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public abstract class DrawablePanel extends JPanel implements IModelListener {

    //   protected Image backgroundImage;
    protected DrawableInfo info = new DrawableInfo();
    protected ArrayList<DrawablePoint> points = new ArrayList<>();
    protected DrawablePoint selectedPoint = new DrawablePoint();
    protected VIEW_TYPE view;
    protected DrawableModelElement selectedElement;
    public static int panX = 0;
    public static int panY = 0;
    public static int panZ = 0;
    public static int oldPanX = 0;
    public static int oldPanY = 0;
    public static int oldPanZ = 0;
    public static int startPanX = 0;
    public static int startPanY = 0;
    public static int startPanZ = 0;

    public DrawablePanel() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                getNearestPoint(e.getX(), e.getY());
                info.setInfo(selectedElement.toInfoString());
                info.setDetailedInfo("");
                for (DrawablePoint p : points) {
                    p.draw((Graphics2D) getGraphics());
                }
                info.draw(getGraphics());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    //detect nearest point;
                    getNearestPoint(e.getX(), e.getY());
                    info.setInfo(selectedElement.toInfoString());
                    info.setDetailedInfo("");
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                startPanX = 0;
                startPanY = 0;
                startPanZ = 0;
                oldPanX = panX;
                oldPanY = panY;
                oldPanZ = panZ;
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                selectedPoint.setSelected(false);
                selectedPoint.draw((Graphics2D) getGraphics());
                info.undraw(getGraphics());
            }
        });
    }

    protected void getNearestPoint(int x, int y) {
        double dist = Double.MAX_VALUE;
        // System.out.println("getNearestPoint:" + points.size());
        for (DrawablePoint p : points) {
            double temp = Utils.distance(p, x, y);
            if (p.isSelectable() && temp < dist) {
                //    System.out.println("selectedPoint:" + p.toStringAll());
                dist = temp;
                selectedPoint.setSelected(false);
                selectedPoint = p;
                selectedPoint.setSelected(true);
            }
        }
        selectedElement = selectedPoint.getBelongsTo();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        //  if (backgroundImage != null) {
        //       g.drawImage(backgroundImage, 0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(), this);
        //   }
        info.draw(g);
        g.setColor(Color.GRAY.brighter());
        if (PredimRC.initDone) {
            PredimRC.getInstanceDrawableModel().draw((Graphics2D) g, view);
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        PredimRC.logDebugln("changeModel in Panel:" + view);
        points = PredimRC.getInstanceDrawableModel().getPoints(view);
        repaint();
    }
}
