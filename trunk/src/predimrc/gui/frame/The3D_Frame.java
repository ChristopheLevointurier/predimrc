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
package predimrc.gui.frame;

import java.awt.Image;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import predimrc.PredimRC;
import predimrc.gui.ExternalFrame;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.SwingUtilities;

import jglcore.JGL;
import jglcore.JGL_3DBsp;
import jglcore.JGL_3DMesh;
import jglcore.JGL_3DMovable;
import jglcore.JGL_3DVector;
import jglcore.JGL_Sorter;
import jglcore.JGL_Time;
import jglcore.JGL_Util;
import jglload.JGL_Data3D;
import predimrc.gui.graphic.Config3DView;
import predimrc.model.element.loader.AirfoilLoader;
import predimrc.model.element.loader.FuselageLoader;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class The3D_Frame extends ExternalFrame implements Runnable {

    private boolean stop;
    private JGL_3DMesh data;
    private JGL_Sorter world;
    private float mouseX, mouseY;
    private float angleX, angleY, angleZ;
    private float zoom;
    private boolean rotationX = false, rotationY = true, rotationZ = false, move = true, moveVertex = false;
    private boolean cullface = false, solid = false, light = true;
    private JGL_3DVector toMove;
    private Config3DView config;

    public The3D_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, 1024, 800);
    }

    public The3D_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "3D";

        mouseX = 0f;
        mouseY = 0f;
        angleX = 192f;
        angleY = 18f;
        angleZ = 175f;
        zoom = 3;

        setBackground(new Color(0, 0, 0));
        loadData();

        Canvas screen = new Canvas();
        screen.setBackground(new Color(75, 75, 75));
        screen.setVisible(true);

        screen.addMouseListener(new MouseListener() {
            public final void mouseClicked(MouseEvent e) {
            }

            public final void mouseEntered(MouseEvent e) {
            }

            public final void mouseExited(MouseEvent e) {
            }

            public final void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                move = false;
                //if (SwingUtilities.isMiddleMouseButton(e)) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    toMove = PredimRC.getNearestVertex(data, new JGL_3DVector(e.getX(), e.getY(), 0f));
                    moveVertex = true;
                }
            }

            public final void mouseReleased(MouseEvent e) {
                move = true;
                moveVertex = false;
            }
        });

        screen.addMouseMotionListener(new MouseMotionListener() {
            public final void mouseMoved(MouseEvent e) {
            }

            public final void mouseDragged(MouseEvent e) {

                if (moveVertex) {
                    toMove.x += (float) (e.getY() - mouseY) * 0.005f;
                    toMove.y += (float) (e.getX() - mouseX) * 0.005f;

                    mouseX = e.getX();
                    mouseY = e.getY();

                } else {
                    angleX += (float) (e.getY() - mouseY) * 0.5f;
                    angleY -= (float) (e.getX() - mouseX) * 0.5f;
                    angleY %= 360;
                    angleX %= 360;

                    mouseX = e.getX();
                    mouseY = e.getY();
                }
            }
        });

        screen.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    zoom += 0.1;
                    JGL.setPerspective(zoom);
                } else {
                    zoom -= 0.1;
                    zoom = zoom < 0 ? 0 : zoom;
                    JGL.setPerspective(zoom);
                }
            }
        });
        screen.validate();
        screen.setSize(getContentPane().getSize());
        config = new Config3DView();




        config.getRotX_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotationX = config.getRotX_check().isSelected();
            }
        });
        config.getRotY_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotationY = config.getRotY_check().isSelected();
            }
        });
        config.getRotZ_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rotationZ = config.getRotZ_check().isSelected();
            }
        });

        config.getCull_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cullface = config.getCull_check().isSelected();
                JGL.setCullFacing(cullface);
            }
        });


        config.getSolid_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solid = config.getSolid_check().isSelected();
                JGL.setSolidFace(solid);
            }
        });

        config.getLighting_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                light = config.getLighting_check().isSelected();
                JGL.setLighting(light);
            }
        });




        getContentPane().add(screen, BorderLayout.CENTER);
        getContentPane().add(config, BorderLayout.EAST);
        pack();
        JGL.setDisplayTarget(screen);
        JGL.setPerspective(zoom);
        JGL.setCullFacing(cullface);
        JGL.setSolidFace(solid);
        JGL.setLighting(light);
        JGL.setShininess(50f);

        config.getRotX_check().setSelected(rotationX);
        config.getRotY_check().setSelected(rotationY);
        config.getRotZ_check().setSelected(rotationZ);
        config.getLighting_check().setSelected(light);
        config.getSolid_check().setSelected(solid);
        config.getCull_check().setSelected(cullface);
        stop = false;
        new Thread(this).start();
    }

    private void loadData() {
        //    data = new JGL_3DMovable(new JGL_3DBsp(new JGL_Data3D(PredimRC.getResourceUrl(filename), JGL_Data3D.MILKSHAPE_ASCII).mesh));
        //   data = PredimRC.mergeMesh(JGL_Util.getGeosphere(4f, 1, 0, 255, 0), PredimRC.getRectangle(new JGL_3DVector(10f, 12f, 0f), new JGL_3DVector(12f, 12f, 0f), new JGL_3DVector(12f, 10f, 0f), new JGL_3DVector(10f, 10f, 0f), 200, 200, 10));
        //Airfoil a = new AirfoilLoader("fad05.dat", 175, 175, 255);
        //  data = a.getWingPart(0.5f, 0.45f, 2);
        FuselageLoader a = new FuselageLoader("Glider1", 105, 175, 255);
        //  FuselageLoader a = new FuselageLoader("Glider1_top.dat", 105, 175, 255);
        data = a.getFuselage(5);
        world = new JGL_Sorter();
    }

    @Override
    public void save() {
        stop = true;
        predimrc.PredimRC.logDebugln("Save de " + title);
    }

    @Override
    public void run() {
        float depth = -50f;

        // Initializes an eye position
        JGL_3DVector eye = new JGL_3DVector();
        JGL_3DMovable temp = new JGL_3DMovable(data);
        // Initializes the real-time system
        JGL_Time.initTimer();
        world.add(temp, eye);


        //*****************************//
        //********  MAIN LOOP  ********//
        //*****************************//
        while (!stop) {

            // Sets the real-time factor for this frame
            JGL_Time.setTimer();

            // Updates rotation angles
            if (move && rotationX) {
                angleX += 1.2f * JGL_Time.getTimer();
                angleX %= 360;
            }
            if (move && rotationY) {
                angleY += 1.2f * JGL_Time.getTimer();
                angleY %= 360;
            }
            if (move && rotationZ) {
                angleZ += 1.2f * JGL_Time.getTimer();
                angleZ %= 360;
            }

            // Clears the display buffer
            JGL.clearBuffer();

            temp.identity();
            temp.translate(0f, 0f, depth);
            temp.rotate(angleX, 1f, 0f, 0f, true);
            temp.rotate(angleY, 0f, 1f, 0f, true);
            temp.rotate(angleZ, 0f, 0f, 1f, true);
            config.getDegX_label().setValue((int) angleX + "°");
            config.getDegY_label().setValue((int) angleY + "°");
            config.getDegZ_label().setValue((int) angleZ + "°");
            config.getZoom_label().setValue((int) (zoom * 10) + " ");


            world.display(eye);

            // Flushes the display buffer on the screen
            JGL.swapBuffers();
        }
    }

    public static void main(String[] poj) {
        new The3D_Frame(new JButton());
    }
}
