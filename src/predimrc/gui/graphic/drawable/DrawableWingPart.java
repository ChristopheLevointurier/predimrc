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
import predimrc.model.element.Wing;

/**
 *
 * @author Christophe Levointurier, 26 déc. 2012, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableWingPart implements IDrawableObject {

    public static DrawableWingPart makeRoot(DrawablePoint wingConnection, Wing get) {
        return new DrawableWingPart(wingConnection, new DrawablePoint(wingConnection.getX(), wingConnection.getY() + get.getWidth_1()));
    }
    DrawablePoint frontPoint;
    DrawablePoint backPoint;
    DrawablePoint previousFrontPoint;
    DrawablePoint previousBackPoint;
    float viewableLength;

    private DrawableWingPart(DrawablePoint _frontPoint, DrawablePoint _backPoint) {
        frontPoint = _frontPoint;
        backPoint = _backPoint;
    }

    public DrawableWingPart(Wing w, DrawableWingPart previous) {

        previousFrontPoint = previous.getFrontPoint();  //comment this does not npe!!
        previousBackPoint = previous.getBackPoint();    //comment this does not npe!!
        double angleRad = Math.toRadians(w.getDiedre());
        viewableLength = (float) (w.getLenght() * (Math.cos(angleRad)));
        frontPoint = new DrawablePoint(previousFrontPoint.getIntX() - viewableLength, previousFrontPoint.getIntY());
        backPoint = new DrawablePoint(frontPoint.getIntX(), frontPoint.getIntY() + w.getWidth_2());
    }

    @Override
    public void draw(Graphics g) {
        //   System.out.println("draw:" + previousFrontPoint.getIntX() + "," + previousFrontPoint.getIntY() + " - " + frontPoint.getIntX() + "," + frontPoint.getIntY());
        g.setColor(Color.GRAY.brighter());
        g.drawLine(previousFrontPoint.getIntX(), previousFrontPoint.getIntY(), frontPoint.getIntX(), frontPoint.getIntY());
        g.drawLine(frontPoint.getIntX(), frontPoint.getIntY(), backPoint.getIntX(), backPoint.getIntY());
        g.drawLine(previousBackPoint.getIntX(), previousBackPoint.getIntY(), backPoint.getIntX(), backPoint.getIntY());
        frontPoint.draw(g);
        backPoint.draw(g);
        previousFrontPoint.draw(g);
        previousBackPoint.draw(g);
    }

    public DrawablePoint getFrontPoint() {
        return frontPoint;
    }

    public DrawablePoint getBackPoint() {
        return backPoint;
    }
}
