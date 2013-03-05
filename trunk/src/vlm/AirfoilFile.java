package vlm;


import java.applet.*;
import java.io.*;
import java.lang.*;
import java.net.*;

/*------------------------------------------------------------------*/
/* knows how to read and airfoil file */
class AirfoilFile {

    Applet applet;
    String fileName = null;
    String status = null;
    InputStream is = null;
    DataInputStream dis = null;
    StreamTokenizer st = null;

    //------------------------------------------------
    public AirfoilFile(String filename, Applet applet) {
        this.applet = applet;
        this.fileName = filename;

        openFile(filename);
    }

    //------------------------------------------------
    private void openFile(String filename) {
        String line = null;

        applet.showStatus("opening: " + filename + "\n");

        /* open */
        try {
            is = new URL(applet.getDocumentBase(), filename).openStream();
            dis = new DataInputStream(is);
        } catch (Exception e) {
            status = e.toString();
            System.err.println(status);
            return;
        }

        applet.showStatus("opened: " + filename + "\n");

        st = new StreamTokenizer(is);
        st.eolIsSignificant(true);
        st.wordChars(95, 95);	// '_'
        st.wordChars(46, 46);	// '.'
        st.parseNumbers();
    }

    //------------------------------------------------
    public void closeFile() {
        try {
            is.close();
        } catch (Exception e) {
            applet.showStatus(e.toString());
            return;
        }
    }

    //------------------------------------------------
    public int getToken() {
        try {
            return (st.nextToken());
        } catch (Exception e) {
            applet.showStatus(e.toString());
        }
        return 0;
    }

    //------------------------------------------------
    public int getTokenWord() {
        do {
            getToken();
        } while (st.ttype != st.TT_WORD && st.ttype != st.TT_EOF);

        return st.ttype;
    }

    //------------------------------------------------
    public int getNextLine() {
        while (st.ttype != st.TT_EOL && st.ttype != st.TT_EOF) {
            getToken();
        }

        return st.ttype;
    }

    //------------------------------------------------
    public String getTokenSval() {
        return st.sval;
    }

    //------------------------------------------------
    public double getTokenNval() {
        return st.nval;
    }

    //------------------------------------------------
    public boolean getNextAirfoil() {
        do {
            if (getToken() == st.TT_WORD) {
                if (st.sval.compareTo("Airfoil") == 0) {
                    return true;
                }
            }

        } while (getNextLine() != st.TT_EOF);

        return false;
    }
    //------------------------------------------------
}
