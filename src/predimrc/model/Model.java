/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.model;

import java.io.Serializable;

/**
 * This class contains all model caracteristics for I/O
 *
 * @author Christophe Levointurier, 9 déc. 2012
 * @version
 * @see
 * @since
 */
public class Model implements Serializable {

    private String name;
    private String note;

    public Model() {
        name = "";
        note = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
