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
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import predimrc.PredimRC;
import predimrc.common.UserConfig;
import predimrc.common.Utils;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.IModelListener;
import predimrc.gui.graphic.drawable.model.DrawableModel;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.graphic.drawable.tool.DrawableInfo;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

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
    public static float zoom = 1;
    public static float panX = 0;
    public static float panY = 0;
    public static float panZ = 0;
    public static float oldPanX = 0;
    public static float oldPanY = 0;
    public static float oldPanZ = 0;
    public static float startPanX = 0;
    public static float startPanY = 0;
    public static float startPanZ = 0;

    public DrawablePanel() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                getNearestPoint(getXcur(e), getYcur(e));
                info.setInfo(selectedElement.toInfoString());
                info.setDetailedInfo(selectedPoint.toInfoString());
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
                    getNearestPoint(getXcur(e), getYcur(e));
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
                PredimRC.logDebugln("PanX=" + panX + " PanY=" + panY + " PanZ=" + panZ + " zoom=" + zoom);
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

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                float inc = e.getModifiers() == InputEvent.CTRL_MASK ? 0.5f : 0.05f;

                if (notches < 0) {
                    zoom += inc;
                } else {
                    if (zoom - inc > 0) {
                        zoom -= inc;
                    }
                }
                panX = -(Utils.REF_POINT.getX() - (Utils.REF_POINT.getX() / (float) (zoom)));
                oldPanX = panX;

                panY = -(Utils.REF_POINT.getY() - (Utils.REF_POINT.getY() / (float) (zoom)));
                oldPanY = panY;

                panZ = -(Utils.REF_POINT.getZ() - (Utils.REF_POINT.getZ() / (float) (zoom)));
                oldPanZ = panZ;
                PredimRC.logDebugln("New zoom factor:" + zoom + "scr/2*zoom=" + ((Utils.TOP_SCREEN_X / 2) / (double) (zoom)) + " panY=" + panY);
                PredimRC.repaintDrawPanels();
            }
        });


    }

    protected void getNearestPoint(float x, float y) {
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
            if (UserConfig.viewRefPoint) {
                Utils.REF_POINT.draw((Graphics2D) g, view, "REF_POINT");
            }
        }
    }

    @Override
    public void updateModel(DrawableModel m) {
        PredimRC.logDebugln("changeModel in Panel:" + view);
        points = PredimRC.getInstanceDrawableModel().getPoints(view);
        repaint();
    }

    protected float getXcur(MouseEvent e) {
        switch (view) {
            case FRONT_VIEW:
            case TOP_VIEW:
                return ((float) e.getX() / DrawablePanel.zoom) - panY;
            default:
            case LEFT_VIEW:
                return ((float) e.getX() / DrawablePanel.zoom) - panX;
        }
    }

    protected float getYcur(MouseEvent e) {
        switch (view) {
            case FRONT_VIEW:
                return ((float) e.getY() / DrawablePanel.zoom) - panZ;
            case TOP_VIEW:
                return ((float) e.getY() / DrawablePanel.zoom) - panX;
            default:
            case LEFT_VIEW:
                return ((float) e.getY() / DrawablePanel.zoom) - panZ;
        }
    }
}
