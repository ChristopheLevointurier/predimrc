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
import java.util.Iterator;
import predimrc.common.Utils.USED_FOR;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.model.Model;
import predimrc.model.element.Wing;

/**
 *
 * @author Christophe Levointurier, 5 janv. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class DrawableModel extends DrawableModelElement implements IModelListener, Iterable<DrawableWing> {

    public static DrawableModel makeDefaultModel() {
        return new DrawableModel();
    }
    private String name;
    private String note;
    private ArrayList<DrawableWing> drawableWing = new ArrayList<>();
    private ArrayList<DrawableWing> drawableTail = new ArrayList<>();
    private ArrayList<DrawableWing> drawableDerive = new ArrayList<>();
    private DrawableFuselage drawableFuselage;

    /**
     * Constructor
     */
    public DrawableModel(Model me) {
        super();
        name = me.getName();
        note = me.getNote();
        for (Wing w : me.getWings()) {
            drawableWing.add(new DrawableWing(w, this));
        }
        for (Wing w : me.getTail()) {
            drawableTail.add(new DrawableWing(w, this));
        }
        for (Wing w : me.getDerive()) {
            drawableDerive.add(new DrawableWing(w, this));
        }
        drawableFuselage = new DrawableFuselage(me.getFuselage(), this);
    }

    /**
     * Constructor
     */
    public DrawableModel() {
        super();
        name = "the magnificent one";
        note = "no note here yet";
        drawableWing.add(new DrawableWing(USED_FOR.MAIN_WING, this));
        drawableTail.add(new DrawableWing(USED_FOR.HORIZONTAL_PLAN, this));
        drawableDerive.add(new DrawableWing(USED_FOR.VERTICAL_PLAN, this));
        drawableFuselage = new DrawableFuselage(this);
    }

    /**
     * getter and setters
     *
     */
    @Override
    public int getIndexInBelongsTo() {
        return 0;
    }

    @Override
    public DrawableModel getBelongsTo() {
        return this;  //yes, its not very exciting
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
        ModelController.applyChange();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<DrawableWing> getWings() {
        return drawableWing;
    }

    public ArrayList<DrawableWing> getTail() {
        return drawableTail;
    }

    public ArrayList<DrawableWing> getDerive() {
        return drawableDerive;
    }

    public DrawableFuselage getFuselage() {
        return drawableFuselage;
    }

    /**
     * Compute methods
     */
    @Override
    public void computePositions() {
        for (DrawableWing w : this) {
            w.computePositions();
        }
        drawableFuselage.computePositions();
    }

    public void setWingAmount(int _i, USED_FOR usedFor) {
        ArrayList<DrawableWing> wingList = null;
        switch (usedFor) {
            case HORIZONTAL_PLAN:
                wingList = drawableTail;
                break;
            case VERTICAL_PLAN:
                wingList = drawableDerive;
                break;
            case MAIN_WING:
            default:
                wingList = drawableWing;
                break;
        }

        predimrc.PredimRC.logDebugln("setWingAmount:" + _i + " for " + usedFor);
        ArrayList<DrawableWing> wingsTemp = new ArrayList<>();
        for (int i = 0; i < _i; i++) {
            if (!wingList.isEmpty()) {
                wingsTemp.add(wingList.remove(0));
            } else {
                //  if (wingsTemp.isEmpty()) {
                wingsTemp.add(new DrawableWing(usedFor, this));
                // } else {
                //     wingsTemp.add(new Wing(wingsTemp.get(0), this));
                //  }
            }
        }
        switch (usedFor) {
            case HORIZONTAL_PLAN:
                drawableTail = wingsTemp;
                break;
            case VERTICAL_PLAN:
                drawableDerive = wingsTemp;
                break;
            case MAIN_WING:
            default:
                drawableWing = wingsTemp;
                break;
        }
        ModelController.applyChange();
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("DrawableModel:");
        ret.append(getName());
        ret.append(", -->Fuselage:").append(drawableFuselage);
        ret.append(", -->Wings:").append(drawableWing.size());
        ret.append(", -->Tails:").append(drawableTail.size());
        ret.append(", -->Derive:").append(drawableDerive.size());
        return ret.toString();
    }

    @Override
    public String toStringAll() {
        StringBuilder ret = new StringBuilder("DrawableModel:");
        ret.append(name);
        ret.append("\n -->Fuselage:").append(drawableFuselage);
        ret.append("\n -->Wings:");
        for (DrawableWing w : this) {
            ret.append(w.toStringAll());
        }
        ret.append("\n*****NOTE****\n").append(note);
        ret.append("\n*************\n\n");
        return ret.toString();
    }

    @Override
    public final void updateModel(DrawableModel m) {
        drawableWing = new ArrayList<>();
        drawableTail = new ArrayList<>();
        drawableDerive = new ArrayList<>();
        for (DrawableWing w : m.getWings()) {
            drawableWing.add(w);
        }
        for (DrawableWing w : m.getTail()) {
            drawableTail.add(w);
        }
        for (DrawableWing w : m.getDerive()) {
            drawableDerive.add(w);
        }
    }

    @Override
    public void draw(Graphics2D g, VIEW_TYPE view) {
        drawableFuselage.draw(g, view);
        for (DrawableWing d : this) {
            d.draw(g, view);
        }
    }

    /**
     * g.setStroke(new BasicStroke(5)); DrawablePoint previous = connection; for
     * (DrawablePoint p : points) { g.drawLine((int) previous.getIntX(), (int)
     * previous.getIntY(), p.getIntX(), p.getIntY()); previous = p; } * for
     * (DrawablePoint p : points) { p.draw((Graphics2D) g); } *
     */
    @Override
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (DrawableWing w : this) {
            ret.addAll(w.getPoints(view));
        }
        ret.addAll(drawableFuselage.getPoints(view));
        return ret;
    }

    //TODO
    public Model generateModel() {
        ArrayList<Wing> realWings = new ArrayList<>();
        ArrayList<Wing> realTails = new ArrayList<>();
        ArrayList<Wing> realDerives = new ArrayList<>();
        for (DrawableWing w : drawableWing) {
            realWings.add(w.generateModel());
        }
        for (DrawableWing w : drawableTail) {
            realTails.add(w.generateModel());
        }
        for (DrawableWing w : drawableDerive) {
            realDerives.add(w.generateModel());
        }
        return new Model("", name, note, realWings, realTails, realDerives, drawableFuselage.generateModel());
    }

    @Override
    public Iterator<DrawableWing> iterator() {
        ArrayList<DrawableWing> ret = new ArrayList<>();
        ret.addAll(drawableWing);
        ret.addAll(drawableTail);
        ret.addAll(drawableDerive);
        return ret.iterator();
    }

    @Override
    public String toInfoString() { //should never be called
        return "Model";
    }
}
