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

/**
 *
 * @author Christophe Levointurier, 13 févr. 2013, (UTF-8)
 * @version
 * @see
 * @since
 */
public class UserConfig {

    public static boolean warnClosePopup = true;
    /**
     *
     */
    public static String airfoilsDirectory = System.getProperty("user.home") + "\\PredimRCFiles\\";
    public static String filename = "";
    //panels config 
    public static boolean viewNeutralPoints = true, viewCG = true, viewRefPoint = true, manipStab = true, manipFuse = true, manipFin = true, manipWing = true;
    //3D config
    public static boolean cull = false, solid = false, light = true, rotX = false, rotY = true, rotZ = false;
}
