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
package predimrc.gui.frame.subframe.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import predimrc.PredimRC;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;

/**
 *
 * @author Christophe Levointurier, 7 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilResults extends JPanel {

    private List<XFoilResultPanel> panels = new ArrayList<>();

    public XFoilResults() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Results:"));


        for (int i = 0; i < 3; i++) {
            XFoilResultPanel temp = new XFoilResultPanel(0, 0, "", XFoil_Frame.getAppropriateColor(i, 6));
            panels.add(temp);
            add(temp);
        }
    }

    public void set0(int indexSerie, List<DrawablePoint> cmSerie, List<DrawablePoint> alphaSerie) {
        panels.get(indexSerie).update("" + getInterpolated0(cmSerie, false), "" + getInterpolated0(alphaSerie, true), PredimRC.getInstanceDrawableModel().getXfoilConfig().getFoilName(indexSerie));
    }

    public void set0Error(int colIndex) {
        panels.get(colIndex).update("waiting data", "waiting data", PredimRC.getInstanceDrawableModel().getXfoilConfig().getFoilName(colIndex));
    }

    private double getInterpolated0(List<DrawablePoint> plist, boolean xSearched) {
        DrawablePoint p1 = plist.get(0), p2 = plist.get(0);
        if (xSearched) {
            for (DrawablePoint p : plist) {
                if ((p.getY() <= 0 && p1.getY() > 0) || (p.getY() > 0 && p1.getY() <= 0)) {
                    p2 = p;
                    //    System.out.println("Valeur cherchée sur x, pts select:" + p1 + "    " + p2);
                    break;
                }
                p1 = p;
            }
            return (p1.getX() - p1.getY() * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY()));
        } else {
            for (DrawablePoint p : plist) {
                if ((p.getX() <= 0 && p1.getX() > 0) || (p.getX() > 0 && p1.getX() <= 0)) {
                    p2 = p;
                    //     System.out.println("Valeur cherchée sur y, pts select:" + p1 + "    " + p2);
                    break;
                }
                p1 = p;
            }
            return (p1.getY() - p1.getX() * (p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
        }
    }

    public Double getCm(int colIndex) {
        return panels.get(colIndex).getCm0Value();
    }

    public Double getAlpha(int colIndex) {
        return panels.get(colIndex).getAlpha0Value();
    }

    public void clear() {
        for (XFoilResultPanel p : panels) {
            p.clear();
        }
    }
}
