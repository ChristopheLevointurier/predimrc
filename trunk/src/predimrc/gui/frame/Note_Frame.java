/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package predimrc.gui.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledEditorKit;
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
public class Note_Frame extends ExternalFrame {

    private JTextPane textPane;

    public Note_Frame(AbstractButton _caller) {
        this(_caller, predimrc.PredimRC.icon, Utils.DEFAULT_X_FRAME, Utils.DEFAULT_Y_FRAME);
    }

    public Note_Frame(AbstractButton _caller, Image _icon, int _x, int _y) {
        super(_caller, _icon, _x, _y);
        title = "Note";
        setTitle(title);



        //Create an editor pane.

        textPane = new JTextPane();
        textPane.setDocument(drawableModel.getNote());

        JScrollPane paneScrollPane = new JScrollPane(textPane);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(250, 155));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));



        JMenuBar menu = new JMenuBar();
        JMenu fontMenu = new JMenu("Font");
        fontMenu.add(new StyledEditorKit.FontFamilyAction("Serif", "Serif"));
        fontMenu.add(new StyledEditorKit.FontFamilyAction("SansSerif", "SansSerif"));
        fontMenu.addSeparator();
        fontMenu.add(new StyledEditorKit.BoldAction());
        fontMenu.add(new StyledEditorKit.ItalicAction());
        fontMenu.add(new StyledEditorKit.UnderlineAction());
        menu.add(fontMenu);


        JMenu sizeMenu = new JMenu("Size");
        sizeMenu.add(new StyledEditorKit.FontSizeAction("12", 12));
        sizeMenu.add(new StyledEditorKit.FontSizeAction("14", 14));
        sizeMenu.add(new StyledEditorKit.FontSizeAction("18", 18));
        sizeMenu.add(new StyledEditorKit.FontSizeAction("20", 20));
        sizeMenu.add(new StyledEditorKit.FontSizeAction("25", 25));
        sizeMenu.add(new StyledEditorKit.FontSizeAction("30", 30));
        menu.add(sizeMenu);



        JMenu colorMenu = new JMenu("Color");
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Red", Color.red)));
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Blue", Color.blue)));
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Black", Color.black)));
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Yellow", Color.yellow)));
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Green", Color.green)));
        colorMenu.add(new JMenuItem(new StyledEditorKit.ForegroundAction("Gray", Color.gray)));
        menu.add(colorMenu);



        setJMenuBar(menu);
        setLayout(new BorderLayout());
        getContentPane().add(paneScrollPane, BorderLayout.CENTER);


        InputMap inputMap = textPane.getInputMap();
        //bold
        KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
        inputMap.put(key, new StyledEditorKit.BoldAction());

        //underline
        key = KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK);
        inputMap.put(key, new StyledEditorKit.UnderlineAction());

        //italic
        key = KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK);
        inputMap.put(key, new StyledEditorKit.ItalicAction());

    }

    @Override
    public void save() {
        predimrc.PredimRC.logln("Save from " + title);
        drawableModel.setNote((DefaultStyledDocument) textPane.getStyledDocument());
        caller.setSelected(false);
        dispose();
    }

    @Override
    public void updateModel(DrawableModel m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
