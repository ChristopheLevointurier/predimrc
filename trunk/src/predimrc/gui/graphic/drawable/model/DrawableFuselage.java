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

import java.awt.Graphics2D;
import java.util.ArrayList;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.Model;
import predimrc.model.ModelElement;
import predimrc.model.element.Fuselage;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableFuselage extends DrawableModelElement {

    protected float length;
    protected String filename = "not yet defined";

    public DrawableFuselage(Fuselage f, DrawableModel _belongsTo) {
        super(f.getPositionDimension3D(), _belongsTo);
        length = f.getLength();
        filename = f.getFilename();
    }

    public DrawableFuselage(DrawableModel _belongsTo) {
        super(_belongsTo);
        length = 200;
    }

    /**
     * getters adn setters
     *
     * @return
     */
    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
        apply();
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
    }

    @Override
    public int getIndexInBelongsTo() {
        return 0; //only one fuselage in model for now.
    }

    @Override
    public String toString() {
        return "DrawableFuselage " + filename + getPositionDimension3D() + ",length=" + length;
    }

    /**
     * paint methods
     *
     * @param g
     */
    @Override
    public void drawTop(Graphics2D g) {
        System.out.println("drawTop in DrawableFuselage");
    }

    @Override
    public void drawLeft(Graphics2D g) {
        System.out.println("drawLeft DrawableFuselage ");
    }

    @Override
    public void drawFront(Graphics2D g) {
        System.out.println("drawFront in DrawableFuselage");
    }

    @Override
    public ArrayList<DrawablePoint> getFrontPoints() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<DrawablePoint> getBackPoints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<DrawablePoint> getTopPoints() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Fuselage generateModel() {
        return new Fuselage(filename, length);
    }
}
