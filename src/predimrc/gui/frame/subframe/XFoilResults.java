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
package predimrc.gui.frame.subframe;

import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.jfree.data.xy.XYSeries;
import predimrc.gui.frame.XFoil_Frame;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
import predimrc.gui.widget.MegaLabel;

/**
 *
 * @author Christophe Levointurier, 7 juil. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class XFoilResults extends JPanel {

    private List<MegaLabel> cm0 = new ArrayList<MegaLabel>();
    private List<MegaLabel> alpha0 = new ArrayList<MegaLabel>();

    public XFoilResults() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Results:"));


        for (int i = 0; i < 3; i++) {
            MegaLabel tempC = new MegaLabel("Cm0");
            tempC.setValueBackground(XFoil_Frame.getAppropriateColor(i, 6));
            MegaLabel tempA = new MegaLabel("Alpha0");
            tempA.setValueBackground(XFoil_Frame.getAppropriateColor(i, 6));
            cm0.add(tempC);
            alpha0.add(tempA);
            add(tempC);
            add(tempA);
        }
    }

    public void set0(int indexSerie, List<DrawablePoint> cmSerie, List<DrawablePoint> alphaSerie) {
        cm0.get(indexSerie).setValue("" + getInterpolated0(cmSerie, false));
        alpha0.get(indexSerie).setValue("" + getInterpolated0(alphaSerie, true));
    }

    private double getInterpolated0(List<DrawablePoint> plist, boolean xSearched) {
        DrawablePoint p1 = plist.get(0), p2 = plist.get(0);
        if (xSearched) {
            for (DrawablePoint p : plist) {
                //        System.out.println("p.getX() :" + (p.getX()) + ", p1.getX() " + (p1.getX()));
                //        System.out.println("p.getX() < 0:" + (p.getX() < 0) + ", p1.getX()>0 " + (p1.getX() > 0) + " donne " + (p.getX() < 0 && p1.getX() > 0));
                //        System.out.println("p.getX() > 0:" + (p.getX() > 0) + ", p1.getX() < 0 " + (p1.getX() < 0) + " donne " + (p.getX() > 0 && p1.getX() < 0));
                if ((p.getY() <= 0 && p1.getY() > 0) || (p.getY() > 0 && p1.getY() <= 0)) {
                    p2 = p;
                    System.out.println("Valeur cherchée sur x, pts select:" + p1 + "    " + p2);
                    break;
                }
                p1 = p;
            }
            return (p1.getX() - p1.getY()) * (p2.getX() - p1.getX()) / (p2.getY() - p1.getY());
        } else {
            for (DrawablePoint p : plist) {
              //  System.out.println("p.getY() :" + (p.getY()) + ", p1.getY() " + (p1.getY()));
                //        System.out.println("p.getX() < 0:" + (p.getX() < 0) + ", p1.getX()>0 " + (p1.getX() > 0) + " donne " + (p.getX() < 0 && p1.getX() > 0));
                //        System.out.println("p.getX() > 0:" + (p.getX() > 0) + ", p1.getX() < 0 " + (p1.getX() < 0) + " donne " + (p.getX() > 0 && p1.getX() < 0));
                if ((p.getX() <= 0 && p1.getX() > 0) || (p.getX() > 0 && p1.getX() <= 0)) {
                    p2 = p;
                    System.out.println("Valeur cherchée sur y, pts select:" + p1 + "    " + p2);
                    break;
                }
                p1 = p;
            }
            return (p1.getY() - p1.getX()) * (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
        }
    }

    public Double getCm(int colIndex) {
        return new Double(cm0.get(colIndex).getValue());
    }

    public Double getAlpha(int colIndex) {
        return new Double(alpha0.get(colIndex).getValue());
    }
}
