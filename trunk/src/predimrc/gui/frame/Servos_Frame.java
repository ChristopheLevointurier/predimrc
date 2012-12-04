/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package predimrc.gui.frame;

import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import predimrc.gui.ExternalFrame;

/**
 *
 * @author Christophe Levointurier,  4 déc. 2012
 * @version
 * @see
 * @since 
 */
public class Servos_Frame extends ExternalFrame{

     public Servos_Frame(AbstractButton _caller)
    {
        this(_caller, predimrc.PredimRC.icon, predimrc.PredimRC.DEFAULT_X_FRAME, predimrc.PredimRC. DEFAULT_Y_FRAME);
    }

    public Servos_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title="Servos";
        setTitle(title);
        getContentPane().add(new JTextArea("Zone de "+title));
    }
    
}
