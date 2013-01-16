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
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.element.Fuselage;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableFuselage extends DrawableModelElement {

    protected float widthY, widthZ;

    public DrawableFuselage(Fuselage f, DrawableModel _belongsTo) {
        super(f.getPositionDimension3D(), _belongsTo);
        width = f.getLength();
        widthY = f.getWidthY();
        widthZ = f.getWidthZ();
        filename = f.getFilename();

    }

    public DrawableFuselage(DrawableModel _belongsTo) {
        super(_belongsTo);
        width = 200;
        widthY = 35;
        widthZ = 50;
    }

    /**
     * getters adn setters
     *
     * @return
     */
    @Override
    public void computePositions() {
        //TODO calc each point for 3D view with new params
    }

    @Override
    public int getIndexInBelongsTo() {
        return 0; //only one fuselage in model for now.
    }

    /**
     * paint methods
     *
     * @param g
     */
    public Fuselage generateModel() {
        return new Fuselage(filename, width, widthY, widthZ);
    }

    @Override
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        return new ArrayList<>(); //TODO
    }

    @Override
    public void draw(Graphics2D g, VIEW_TYPE view) {
        //TODO
    }

    @Override
    public DrawableModel getBelongsTo() {
        return (DrawableModel) belongsTo;
    }

    @Override
    public String toString() {
        return "DrawableFuselage " + filename + getPositionDimension3D() + ",widthX=" + width + ",widthY=" + widthY + ",widthZ=" + widthZ;
    }

    @Override
    public String toInfoString() {
        return "Fuselage";
    }
}
