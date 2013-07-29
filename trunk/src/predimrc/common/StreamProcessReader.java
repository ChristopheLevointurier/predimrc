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
package predimrc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import predimrc.PredimRC;

/**
 *
 * @author Christophe Levointurier, 22 mars 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class StreamProcessReader extends Thread {

    private InputStream is;
    private boolean error;
    private boolean hadError = false;

    public boolean isHadError() {
        return hadError;
    }

    public StreamProcessReader(InputStream _is, boolean _error) {
        is = _is;
        error = _error;
    }

    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (error) {
                    PredimRC.logln(" xfoil error >" + line);
                    hadError = true;
                }
                //else {
                //     System.out.println("" + line);
                // }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}
