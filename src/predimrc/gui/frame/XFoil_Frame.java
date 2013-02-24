/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import predimrc.common.Utils;
import predimrc.gui.ExternalFrame;

/**
 *
 * @author Christophe Levointurier, 4 déc. 2012
 * @version
 * @see
 * @since
 */
public class XFoil_Frame extends ExternalFrame {
    
    private JPanel  panel           = new JPanel();
    private JPanel  panel_choice    = new JPanel();
    private JPanel  panel_choice1   = new JPanel();
    private JPanel  panel_choice2   = new JPanel();
    private JPanel  panel_choice3   = new JPanel();
    private JButton button_airfoil1 = new JButton("Airfoil 1");
    private JButton button_airfoil2 = new JButton("Airfoil 2");
    private JButton button_airfoil3 = new JButton("Airfoil 3");
    private JLabel  label_airfoil1  = new JLabel();
    private JLabel  label_airfoil2  = new JLabel();    
    private JLabel  label_airfoil3  = new JLabel();
    
    
    
    public XFoil_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public XFoil_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "xFoil";
        setTitle(title);
        
        label_airfoil1.setText("Airfoil 1");
        label_airfoil2.setText("Airfoil 2");
        label_airfoil3.setText("Airfoil 3");
   
        label_airfoil1.setForeground(Color.red);
        label_airfoil2.setForeground(Color.green);
        label_airfoil3.setForeground(Color.blue);
        
        panel_choice1.setLayout(new BoxLayout(panel_choice1,BoxLayout.LINE_AXIS));
        panel_choice2.setLayout(new BoxLayout(panel_choice2,BoxLayout.LINE_AXIS));
        panel_choice3.setLayout(new BoxLayout(panel_choice3,BoxLayout.LINE_AXIS));
        panel_choice.setLayout(new BoxLayout(panel_choice,BoxLayout.PAGE_AXIS));
        panel.setLayout(new BorderLayout());
        
        panel_choice1.add(button_airfoil1);
        panel_choice1.add(label_airfoil1);
        
        panel_choice2.add(button_airfoil2);
        panel_choice2.add(label_airfoil2);
        
        panel_choice3.add(button_airfoil3);
        panel_choice3.add(label_airfoil3);
        
        panel_choice.add(panel_choice1);
        panel_choice.add(panel_choice2);
        panel_choice.add(panel_choice3);
        
        panel.add(panel_choice,BorderLayout.WEST);
        
        getContentPane().add(panel);
        
        button_airfoil1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_airfoil1.setText(getAirFoil());
            }
        });
        
        button_airfoil2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_airfoil2.setText(getAirFoil());
            }
        });
        
        button_airfoil3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_airfoil3.setText(getAirFoil());
            }
        });
 
    }

    
    @Override
    public void save() {
        predimrc.PredimRC.logDebugln("Save de " + title);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    private static String getAirFoil() {
        String fileName = "0";
        JFileChooser chooser = new JFileChooser(new File(".\\src\\resource\\AirFoils"));
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle("Select a airfoil file.");

        final int returnVal = chooser.showOpenDialog(null);
        // to do : filter and check data 
        
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           fileName = chooser.getSelectedFile().getAbsolutePath();
        }

        return fileName;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
