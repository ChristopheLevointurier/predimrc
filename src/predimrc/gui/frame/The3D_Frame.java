
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    private boolean rotationX = true, rotationY = false, rotationZ = false, move = true, moveVertex = false;
    private JGL_3DVector toMove;
    private Config3DView config;

    public The3D_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, predimrc.PredimRC.DEFAULT_X_FRAME, predimrc.PredimRC.DEFAULT_Y_FRAME);
    }

    public The3D_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "3D";


        mouseX = 0f;
        mouseY = 0f;
        angleX = 0f;
        angleY = 0f;
        zoom = 60f;

        setBackground(new Color(0, 0, 0));
        loadData();

        Canvas screen = new Canvas();
        screen.setBackground(new Color(50, 50, 50));
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
                    toMove.x += (float) (e.getY() - mouseY) * 0.8f;
                    toMove.y += (float) (e.getX() - mouseX) * 0.8f;

                    mouseX = e.getX();
                    mouseY = e.getY();

                } else {
                    angleX += (float) (e.getY() - mouseY) * 0.8f;
                    angleY += (float) (e.getX() - mouseX) * 0.8f;

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
                    zoom += 1;
                    JGL.setPerspective(zoom);
                } else {
                    zoom -= 1;
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
                JGL.setCullFacing(config.getCull_check().isSelected());
            }
        });


        config.getSolid_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JGL.setSolidFace(config.getSolid_check().isSelected());
            }
        });

        config.getLighting_check().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JGL.setLighting(config.getLighting_check().isSelected());
            }
        });




        getContentPane().add(screen, BorderLayout.CENTER);
        getContentPane().add(config, BorderLayout.EAST);
        pack();
        JGL.setDisplayTarget(screen);
        JGL.setPerspective(zoom);
        JGL.setCullFacing(true);
        JGL.setSolidFace(true);
        JGL.setLighting(true);
        JGL.setShininess(85f);

        stop = false;
        new Thread(this).start();
    }

    private void loadData() {
        //    data = new JGL_3DMovable(new JGL_3DBsp(new JGL_Data3D(PredimRC.getResourceUrl(filename), JGL_Data3D.MILKSHAPE_ASCII).mesh));
        data = PredimRC.mergeMesh(JGL_Util.getGeosphere(4f, 1, 0, 255, 0), PredimRC.getRectangle(new JGL_3DVector(10f, 12f, 0f), new JGL_3DVector(12f, 12f, 0f), new JGL_3DVector(12f, 10f, 0f), new JGL_3DVector(10f, 10f, 0f), 200, 200, 10));
        world = new JGL_Sorter();
    }

    @Override
    public void save() {
        stop = true;
        predimrc.PredimRC.logDebugln("Save de " + title);
    }

    @Override
    public void run() {
        float depth = -85f;

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


            world.display(eye);

            // Flushes the display buffer on the screen
            JGL.swapBuffers();
        }
    }

    public static void main(String[] poj) {
        new The3D_Frame(new JButton());
    }
}
