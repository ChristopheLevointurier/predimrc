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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import predimrc.model.element.WingSection;

/**
 *
 * @author Christophe Levointurier, 26 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableWingPart implements IDrawableObject {

    public static DrawableWingPart makeRoot(DrawablePoint wingConnection, WingSection get) {
        return new DrawableWingPart(wingConnection, new DrawablePoint(wingConnection.getX(), wingConnection.getY() + get.getWidth_1()));
    }
    private DrawablePoint frontPoint;
    private DrawablePoint backPoint;
    private DrawablePoint previousFrontPoint;
    private DrawablePoint previousBackPoint;
    private float viewableLength;
    private boolean ontail;

    private DrawableWingPart(DrawablePoint _frontPoint, DrawablePoint _backPoint) {
        frontPoint = _frontPoint;
        backPoint = _backPoint;
    }

    public DrawableWingPart(WingSection w, DrawableWingPart previous, boolean _ontail) {
        ontail = _ontail;
        previousFrontPoint = previous.getFrontPoint();  //comment this does not npe!!
        previousBackPoint = previous.getBackPoint();    //comment this does not npe!!
        viewableLength = (float) (w.getLenght() * (Math.cos(Math.toRadians(w.getDiedre()))));
        frontPoint = Utils.getCoordOnCircle(previousFrontPoint, w.getFleche(), viewableLength);
        backPoint = new DrawablePoint(frontPoint.getIntX(), frontPoint.getIntY() + w.getWidth_2());
    }

    @Override
    public void draw(Graphics2D g) {
        //   System.out.println("draw:" + previousFrontPoint.getIntX() + "," + previousFrontPoint.getIntY() + " - " + frontPoint.getIntX() + "," + frontPoint.getIntY());
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
}
