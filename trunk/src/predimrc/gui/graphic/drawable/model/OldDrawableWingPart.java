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
package predimrc.gui.graphic.drawable.model;

import predimrc.gui.graphic.drawable.model.DrawablePoint;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import predimrc.gui.graphic.drawable.panel.TopPanel;
import predimrc.gui.graphic.drawable.Utils;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 26 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class OldDrawableWingPart implements IDrawableObject {

    public static OldDrawableWingPart makeRoot(DrawablePoint wingConnection, WingSection get) {
        return new OldDrawableWingPart(wingConnection, new DrawablePoint(wingConnection.getX(), wingConnection.getY() + get.getWidth_1()), get);
    }
    private DrawablePoint frontPoint;
    private DrawablePoint backPoint;
    private DrawablePoint previousFrontPoint;
    private DrawablePoint previousBackPoint;
    private float viewableLength;
    private boolean ontail;
    private WingSection section;

    private OldDrawableWingPart(DrawablePoint _frontPoint, DrawablePoint _backPoint, WingSection _section) {
        frontPoint = _frontPoint;
        backPoint = _backPoint;
        section = _section;
    }

    public OldDrawableWingPart(WingSection w, OldDrawableWingPart previous, boolean _ontail) {
        ontail = _ontail;
        section = w;
        previousFrontPoint = previous.getFrontPoint();  //comment this does not npe!!
        previousBackPoint = previous.getBackPoint();    //comment this does not npe!!
        viewableLength = (float) (w.getLenght() * (Math.cos(Math.toRadians(w.getDiedre()))));
        frontPoint = Utils.getCoordOnCircle(previousFrontPoint, w.getFleche(), viewableLength);
        backPoint = new DrawablePoint(frontPoint.getIntX(), frontPoint.getIntY() + w.getWidth_2());
    }

    @Override
    public void drawTop(Graphics2D g) {
        //   System.out.println("drawTop:" + previousFrontPoint.getIntX() + "," + previousFrontPoint.getIntY() + " - " + frontPoint.getIntX() + "," + frontPoint.getIntY());
        g.setStroke(new BasicStroke(6));
        g.setColor(Color.GRAY.brighter());
        g.drawLine(previousFrontPoint.getIntX(), previousFrontPoint.getIntY(), frontPoint.getIntX(), frontPoint.getIntY());
        g.drawLine(frontPoint.getIntX(), frontPoint.getIntY(), backPoint.getIntX(), backPoint.getIntY());
        g.drawLine(previousBackPoint.getIntX(), previousBackPoint.getIntY(), backPoint.getIntX(), backPoint.getIntY());
        //miror
        g.drawLine(TopPanel.MID_SCREEN_X * 2 - previousFrontPoint.getIntX(), previousFrontPoint.getIntY(), TopPanel.MID_SCREEN_X * 2 - frontPoint.getIntX(), frontPoint.getIntY());
        g.drawLine(TopPanel.MID_SCREEN_X * 2 - frontPoint.getIntX(), frontPoint.getIntY(), TopPanel.MID_SCREEN_X * 2 - backPoint.getIntX(), backPoint.getIntY());
        g.drawLine(TopPanel.MID_SCREEN_X * 2 - previousBackPoint.getIntX(), previousBackPoint.getIntY(), TopPanel.MID_SCREEN_X * 2 - backPoint.getIntX(), backPoint.getIntY());
        frontPoint.draw(g);
        backPoint.draw(g);
        previousFrontPoint.draw(g);
        previousBackPoint.draw(g);
    }

    public DrawablePoint getFrontPoint() {
        return frontPoint;
    }

    public DrawablePoint getPreviousFrontPoint() {
        return previousFrontPoint;
    }

    public DrawablePoint getPreviousBackPoint() {
        return previousBackPoint;
    }

    public DrawablePoint getBackPoint() {
        return backPoint;
    }

    public boolean isOntail() {
        return ontail;
    }

    public WingSection getSection() {
        return section;
    }

    
    
    @Override
    public String toString() {
        return "DrawableWingPart:" + frontPoint + "," + backPoint + ", previous= " + previousFrontPoint + "," + previousBackPoint + " l=" + viewableLength;
    }

    @Override
    public void drawLeft(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void drawFront(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
