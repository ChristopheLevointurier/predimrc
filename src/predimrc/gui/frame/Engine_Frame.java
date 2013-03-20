
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.JTextArea;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;
import predimrc.gui.graphic.drawable.model.DrawableModel;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class Engine_Frame extends ExternalFrame {

    public Engine_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public Engine_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "Engine";
        setTitle(title);
        getContentPane().add(new JTextArea("Zone de " + title));
    }

    @Override
    public void save() {
        predimrc.PredimRC.logln("Save from " + title);
    }

    @Override
    public void updateModel(DrawableModel m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
