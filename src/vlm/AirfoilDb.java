package vlm;


import java.applet.*;
import java.awt.*;
import java.awt.List;
import java.io.*;
import java.text.*;

//NN import Airfoil.*;
//NN import AirfoilFile.*;

/*------------------------------------------------------------------*/
class AirfoilDb  {
		final int   EOF         = StreamTokenizer.TT_EOF;
		Applet      applet      = null;
		String      airfoilDir  = null;
		List        airfoilList = null;
		TextArea    textArea;

		AirfoilFile afFile      = null;
		Airfoil     af          = null;

	//--------------------------------------------------------------------
		public AirfoilDb (String airfoilDir, List list, Applet applet,
					TextArea textArea) {
			this.applet           = applet;
			this.airfoilList      = list;
			this.airfoilDir       = airfoilDir;
			this.textArea         = textArea;

		/* scan file and create airfoil list */
			AirfoilFile afFile = new AirfoilFile (airfoilDir+"catalog", applet);
			int         n = 0;

			do  {
				if (afFile.getTokenWord() != StreamTokenizer.TT_EOF)   {
					airfoilList.addItem (afFile.getTokenSval());
					n++;
				}
			} while (afFile.getNextLine() != StreamTokenizer.TT_EOF);

			airfoilList.select (n/2);

			afFile.closeFile ();
		}

	//--------------------------------------------------------------------
		public Airfoil getAirfoil (String airfoilName)  {
			AirfoilFile afFile = new AirfoilFile (airfoilDir+"catalog", applet);

			af = null;

			do  {
				if (afFile.getTokenWord() != EOF)  {
					String s = afFile.getTokenSval ();
					if (s.compareTo (airfoilName) == 0)  {
						applet.showStatus ("AirfoilDb.getAirfoil: " + s);

						if (afFile.getTokenWord() != EOF)
							readAirfoil (s, afFile.getTokenSval ());
						break;
					}
				}
			} while (afFile.getNextLine() != StreamTokenizer.TT_EOF);

			afFile.closeFile ();

			return af;
		}

	//------------------------------------------------
		private void readAirfoil (String afName, String afFilename)  {
			afFile = new AirfoilFile (airfoilDir+afFilename, applet);

			if (! findAirfoil (afName))  {
				applet.showStatus (
					"readAirfoil: "+ afName +" not found in "+ afFilename);
			}
			else  {
				// applet.showStatus ("ok");

				if (! readAirfoilData (afName))
					applet.showStatus ("readAirfoil: failed");
				else
					applet.showStatus ("readAirfoil: complete");
			}


			afFile.closeFile ();
		}

	//------------------------------------------------
		private boolean findAirfoil (String afName)  {
			do  {
				if (afFile.getTokenWord() != EOF)  {
					String  s = afFile.getTokenSval();
					if (s.compareTo ("Airfoil") == 0)  {
						if (afFile.getTokenWord() != EOF)
							s = afFile.getTokenSval();
							if (s.compareTo (afName) == 0)
								return true;
					}
				}
			} while (afFile.getNextLine() != EOF);

			return false;
		}

	//------------------------------------------------
		private boolean getLeadValue ()  {
			do  {
				if (afFile.getToken() == StreamTokenizer.TT_NUMBER)
					return true;
				// textArea.append (afFile.getTokenSval() +"\n");
			} while (afFile.getNextLine() != EOF);

			return false;
		}

	//------------------------------------------------
		private boolean readAirfoilData (String afName)  {
			if (! getLeadValue())  {
				return false;
			}

			int nPolars = (int) afFile.getTokenNval();
			af          = new Airfoil (afName, nPolars);

			for (int i = 0; i < nPolars; i++)  {
				if (! getLeadValue())  {
					System.out.println ("Error: readPolar - read reynolds");
					return false;
				}

				int reynolds = (int) afFile.getTokenNval();

				if (! getLeadValue())  {
					System.out.println ("Error: readPolar - read nPts");
					return false;
				}

				int nPts = (int) afFile.getTokenNval();

				if (nPts <= 0)  {
					System.out.println ("Error: readPolar - nPts <= 0");
					return false;
				}

				float cl [] = new float [nPts];
				float cd [] = new float [nPts];
				float a []  = new float [nPts];

				for (int j = 0; j < nPts; j++)  {
					if (! getLeadValue())  {
						System.out.println ("Error: readPolar - read Cl");
						return false;
					}
					cl[j] = (float) afFile.getTokenNval();

					afFile.getToken();
					cd[j] = (float) afFile.getTokenNval();

					afFile.getToken();
					a[j]  = (float) afFile.getTokenNval();
				}

				if (! af.loadPolar (i, reynolds, nPts, cl, cd, a))  {
					System.out.println ("Error: airfoil.loadPolar "+ i);
					return false;
				}
			}

			return true;
		}
}
