
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package predimrc;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * This class for file filtering in gui for I/O
 *
 * @author Christophe Levointurier, 22 déc. 2012
 * @version
 * @see
 * @since
 */
public class PredimFileFilter extends FileFilter {

    private String extension = "";
    private String description = "";

//constructeurs
    public PredimFileFilter() {
    }

    public PredimFileFilter(String extension) {
        this(extension, "");
    }

    public PredimFileFilter(String _extension, String _description) {
        this();  //construit la hashtable
        extension = _extension;
        description = _description;
    }

    @Override
    public boolean accept(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                return false;
            }

            String filename = f.getName();
            String selectedExtension = "";
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                selectedExtension = filename.substring(i + 1).toLowerCase();
            }
            if (selectedExtension.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return (description);
    }
}