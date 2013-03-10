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
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import predimrc.common.Utils;
import predimrc.common.Utils.USED_FOR;
import predimrc.common.Utils.VIEW_TYPE;
import predimrc.controller.IModelListener;
import predimrc.controller.ModelController;
import predimrc.gui.graphic.drawable.model.abstractClasses.DrawableModelElement;
import predimrc.gui.graphic.drawable.tool.DrawableGravityCenter;
import predimrc.gui.graphic.drawable.tool.DrawablePoint;
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
    private DefaultStyledDocument note;
    private ArrayList<DrawableWing> drawableWing = new ArrayList<>();
    private ArrayList<DrawableWing> drawableTail = new ArrayList<>();
    private ArrayList<DrawableWing> drawableDerive = new ArrayList<>();
    private DrawableFuselage drawableFuselage;
    private DrawableGravityCenter gravityCenter;
    private DrawableGravityCenter gravityCenterLeft;
    private float staticMarginRatio = 0.03f;
    private float czAdjustment = 0f;
    private float cm0 = 0f;
    private float alpha0a = 0f;
    private float alpha0s = 0f;
    private double vStab = 0f;
    private double stabLever = 0f;
    /**
     * * computed values *
     */
    private double czAStab = 0f;
    private double alphaWing = 0f;
    private double alphaStab = 0f;

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
        staticMarginRatio = me.getStaticMargin();
        gravityCenter = new DrawableGravityCenter(this, VIEW_TYPE.TOP_VIEW);
        gravityCenterLeft = new DrawableGravityCenter(this, VIEW_TYPE.LEFT_VIEW);
        czAdjustment = me.getCzAdjustment();

    }

    /**
     * Constructor
     */
    public DrawableModel() {
        super();
        name = "the magnificent one";
        note = initDocument();
        drawableWing.add(new DrawableWing(USED_FOR.MAIN_WING, this));
        drawableTail.add(new DrawableWing(USED_FOR.HORIZONTAL_PLAN, this));
        drawableDerive.add(new DrawableWing(USED_FOR.VERTICAL_PLAN, this));
        drawableFuselage = new DrawableFuselage(this);
        gravityCenter = new DrawableGravityCenter(this, VIEW_TYPE.TOP_VIEW);
        gravityCenterLeft = new DrawableGravityCenter(this, VIEW_TYPE.LEFT_VIEW);
    }

    protected final DefaultStyledDocument initDocument() {
        String initString[] = {"\n\n          Here you can type ", "styled", " notes", " related to your", " model"};
        SimpleAttributeSet[] attrs = new SimpleAttributeSet[5];
        attrs[0] = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrs[0], "SansSerif");
        StyleConstants.setFontSize(attrs[0], 20);
        attrs[1] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setBold(attrs[1], true);
        attrs[2] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setItalic(attrs[2], true);
        attrs[3] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[3], 20);
        attrs[4] = new SimpleAttributeSet(attrs[0]);
        StyleConstants.setFontSize(attrs[4], 12);
        DefaultStyledDocument ret = new DefaultStyledDocument();

        try {
            for (int i = 0; i < initString.length; i++) {
                ret.insertString(ret.getLength(), initString[i], attrs[i]);
            }
        } catch (BadLocationException ble) {
            //dont care
        }
        return ret;
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

    public DefaultStyledDocument getNote() {
        return note;
    }

    public void setNote(DefaultStyledDocument _note) {
        note = _note;
    }

    public final ArrayList<DrawableWing> getWings() {
        return drawableWing;
    }

    public boolean hasStab() {
        return (!drawableTail.get(0).isFake());
    }

    public boolean hasFuse() {
        return (!drawableFuselage.isFake());
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

    public float getStaticMarginRatio() {
        return staticMarginRatio;
    }

    public void setStaticMarginRatio(float _margeStatiqueDeCentrage) {
        staticMarginRatio = Utils.round(_margeStatiqueDeCentrage);
    }

    public float getCzAdjustment() {
        return czAdjustment;
    }

    public void setCzAdjustment(float czAdjustment) {
        this.czAdjustment = czAdjustment;
    }

    public float getCm0() {
        return cm0;
    }

    public void setCm0(float cm0) {
        this.cm0 = cm0;
    }

    public float getAlpha0a() {
        return alpha0a;
    }

    public void setAlpha0a(float alpha0a) {
        this.alpha0a = alpha0a;
    }

    public float getAlpha0s() {
        return alpha0s;
    }

    public void setAlpha0s(float alpha0s) {
        this.alpha0s = alpha0s;
    }

    public double getCzAStab() {
        return czAStab;
    }

    public void setCzAStab(double czAStab) {
        this.czAStab = czAStab;
    }

    public double getAlphaWing() {
        return alphaWing;
    }

    public void setAlphaWing(double alphaWing) {
        this.alphaWing = alphaWing;
    }

    public double getAlphaStab() {
        return alphaStab;
    }

    public void setAlphaStab(double alphaStab) {
        this.alphaStab = alphaStab;
    }

    public double getStabLever() {
        return stabLever;
    }

    public double getvStab() {
        return vStab;
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
        Utils.REF_POINT.setX(getWings().get(0).getPositionDimension3D().getX());
        Utils.REF_POINT.setZ(getWings().get(0).getPositionDimension3D().getZ());

        /**
         * Center of gravity computation
         */
        DrawableWing mainWing = getWings().get(0);
        DrawableWing stab = getTail().get(0);

        DrawableWing fin = getDerive().size() > 0 ? getDerive().get(0) : DrawableWing.MakeEmptyWing(USED_FOR.VERTICAL_PLAN); //todo if winglet it will fail!
        stabLever = stab.getXF() - mainWing.getXF();
        double XDd = fin.getXF() - mainWing.getXF();
        double XDf = (getFuselage().getNeutralPointRatio() / 100) * getFuselage().getWidth() + getFuselage().getxPos() - mainWing.getXF(); // should become "Fuselage.getXF() - mainWing.getXF()" like wing or others components
        vStab = (stabLever * stab.getArea() * Math.cos(stab.get(0).getDihedral() * Math.PI / 180) * Math.cos(stab.get(0).getDihedral() * Math.PI / 180)) / (mainWing.getMeanCord() * mainWing.getArea());  //stab volume

        double Aa = mainWing.getAspectRatio() / (2 + mainWing.getAspectRatio());// wing efficiency
        double As = stab.getAspectRatio() / (2 + stab.getAspectRatio());//  stab efficiency

        double Af = 0.4;
        if (vStab < -0.1 && getFuselage().getArea() > 0) {
            Af = 0.2 * (1 + stab.getAspectRatio() / getFuselage().getWidthY());
        }
        if (vStab > -0.1 && getFuselage().getArea() > 0) {
            Af = 0.2 * (1 + mainWing.getAspectRatio() / getFuselage().getWidthY());
        }

        double E = vStab <= 0 ? 0 : (1 / (2 + mainWing.getAspectRatio()) * (4.5 - (stabLever + 5 * (mainWing.getzPos() - stab.getzPos())) / (mainWing.getAspectRatio() * mainWing.getMeanCord())));

        double xF = 0.25 + (stabLever * stab.getArea() * Math.cos(stab.get(0).getDihedral() * Math.PI / 180) * Math.cos(stab.get(0).getDihedral() * Math.PI / 180) * As * (1 - E) - XDf * getFuselage().getArea() * Af) / (mainWing.getMeanCord() * (mainWing.getArea() * Aa + getFuselage().getArea() * Af + stab.getArea() * Math.cos(stab.get(0).getDihedral() * Math.PI / 180) * Math.cos(stab.get(0).getDihedral() * Math.PI / 180) * As * (1 - E)));
        XF = mainWing.getXF() + (xF - 0.25) * mainWing.getMeanCord();
        double xCG = xF - staticMarginRatio;
        double XCG = mainWing.getXF() + (xCG - 0.25) * mainWing.getMeanCord();
        gravityCenter.setLocation(Utils.TOP_SCREEN_X / 2, XCG);
        gravityCenterLeft.setLocation(XCG, Utils.REF_POINT.getZ());

        alphaWing = (9.1f * czAdjustment / Aa) + alpha0a;
        czAStab = (czAdjustment * (xCG - 0.25f) + cm0) / vStab;
        alphaStab = 9.1f * (E * czAdjustment / Aa + czAStab / As) + alpha0s + mainWing.getAngle() - alphaWing;
    }

    public void setFuseOnOff(boolean on) {
        if (on && drawableFuselage.isFake()) {
            drawableFuselage = new DrawableFuselage(this);
        }
        if (!on && !drawableFuselage.isFake()) {
            drawableFuselage = DrawableFuselage.makeFake();
        }
        ModelController.applyChange();
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
            if (!wingList.isEmpty() && !wingList.get(0).isFake()) {
                wingsTemp.add(wingList.remove(0));
            } else {
                wingsTemp.add(new DrawableWing(usedFor, this));
            }
        }
        if (wingsTemp.isEmpty() && !usedFor.equals(USED_FOR.VERTICAL_PLAN)) {
            wingsTemp.add(DrawableWing.MakeEmptyWing(used_for));
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
        if (view.equals(VIEW_TYPE.TOP_VIEW)) {
            gravityCenter.draw(g);
        }
        if (view.equals(VIEW_TYPE.LEFT_VIEW)) {
            gravityCenterLeft.draw(g);
        }
    }

    @Override
    public ArrayList<DrawablePoint> getPoints(VIEW_TYPE view) {
        ArrayList<DrawablePoint> ret = new ArrayList<>();
        for (DrawableWing w : this) {
            ret.addAll(w.getPoints(view));
        }
        if (!drawableFuselage.isFake()) {
            ret.addAll(drawableFuselage.getPoints(view));
        }
        if (view.equals(VIEW_TYPE.TOP_VIEW)) {
            ret.add(gravityCenter);
        }
        if (view.equals(VIEW_TYPE.LEFT_VIEW)) {
            ret.add(gravityCenterLeft);
        }
        return ret;
    }

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
        return new Model("", name, note, realWings, realTails, realDerives, drawableFuselage.generateModel(), staticMarginRatio, czAdjustment);
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
        return "Model ";
    }
}
