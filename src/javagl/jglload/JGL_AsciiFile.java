//	Copyright 2008 - 2010 Nicolas Devere
//
//	This file is part of JavaGL.
//
//	JavaGL is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//
//	JavaGL is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//
//	You should have received a copy of the GNU General Public License
//	along with JavaGL; if not, write to the Free Software
//	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

package javagl.jglload;

import java.io.*;
import java.net.*;

class JGL_AsciiFile {
	
	public static String EOF = "eof";
	
	private BufferedReader lecture;
	private char lettre;
	
	/**
	 * Initialise et ouvre le flux de lecture du fichier.
	 * @param chemin : String
	 * @throws FileNotFoundException : fichier non trouv�
	 */
	public JGL_AsciiFile(BufferedReader reader) throws FileNotFoundException, IOException {
		try {
			lecture = reader;
			
			//	Amorce de la lecture.
			lettre = (char)lecture.read();
		}
		catch(FileNotFoundException e) {
			System.out.println("FichierASCII - FichierASCII(String) : " + e.getMessage());
			throw(e);
		}
		catch(IOException e) {
			System.out.println("FichierASCII - FichierASCII(String) : " + e.getMessage());
			throw(e);
		}
	}

	/**
	 * Initialise et ouvre la connexion au fichier.
	 * @param adresse : URL
	 * @throws FileNotFoundException : fichier inexistant
	 * @throws IOException : probl�me d'ouverture
	 */
	public JGL_AsciiFile(URL adresse) throws FileNotFoundException, IOException {
		try {
			URLConnection conn = adresse.openConnection();
			lecture = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			//	Amorce de la lecture.
			lettre = (char)lecture.read();
		}
		catch(FileNotFoundException e) {
			System.out.println("FichierASCII - FichierASCII(URL) : " + e.getMessage());
			throw(e);
		}
		catch(IOException e) {
			System.out.println("FichierASCII - FichierASCII(URL) : " + e.getMessage());
			throw(e);
		}
	}
	
	
	/**
	 * Ferme le fichier.
	 * @throws IOException : Probl�me de fermeture
	 */
	public void fermer() throws IOException {
		try {
			lecture.close();
		}
		catch(IOException e) {
			System.out.println("FichierASCII - fermer() : " + e.getMessage());
			throw(e);
		}		
	}
	
	
	/**
	 * Retourne le caract�re suivant dans le fichier ASCII.
	 * @return char
	 * @throws IOException : Probl�me de lecture
	 */
	public char lireCaractere() throws IOException {
		
		try {
			lettre = (char)lecture.read();
			return lettre;
		}
		catch(IOException e) {
			System.out.println("FichierASCII - lireCaractere() : " + e.getMessage());
			throw(e);
		}		
	}
	
	
	/**
	 * Retourne la cha�ne suivante d�limit�e par deux s�parateurs.
	 * @return : String
	 * @throws IOException : Probl�me de lecture
	 */
	public String lireChaine() throws IOException {
		
		String chaine = new String();
		
		try {
			
			if(!lecture.ready())
				//throw(new IOException("depassement de fin de fichier"));
				return EOF;
			
			while(estUnSeparateur(lettre)) {
				lettre = (char)lecture.read();
				if(!lecture.ready())
					//throw(new IOException("depassement de fin de fichier"));
					return EOF;
			}
			
			while(!estUnSeparateur(lettre)) {
				if(!lecture.ready())
					//throw new IOException("depassement de fin de fichier");
					return EOF;
				chaine += lettre;
				lettre = (char)lecture.read();
			}
			
			return chaine;
		}
		catch(IOException e) {
			System.out.println("FichierASCII - lireChaine() : " + e.getMessage());
			throw(e);
		}
		
	}
	
	
	
	
	/**
	 * Retourne le nombre suivant dans le fichier ASCII.
	 * @return float
	 * @throws IOException : Probl�me de lecture
	 */
	public float lireNombre() throws IOException {
		
		try {
			return Float.parseFloat(lireNombreChaine());
		}
		catch(IOException e) {
			System.out.println("FichierASCII - lireNombre() : " + e.getMessage());
			throw(e);
		}
		
	}
	
	
	
	/**
	 * Retourne la chaine contenant le nombre suivant dans le fichier ASCII.
	 * @return String
	 * @throws IOException : Probl�me de lecture
	 */
	public String lireNombreChaine() throws IOException {
		
		String nombre = new String();
	
		try {
			
			if(!lecture.ready())
				//throw(new IOException("depassement de fin de fichier"));
				return EOF;
			
			while(!estUnChiffre(lettre)) {
				lettre = (char)lecture.read();
				if(!lecture.ready())
					//throw new IOException("depassement de fin de fichier");
					return EOF;
			}
			
			while(estUnChiffre(lettre)) {
				nombre += lettre;
				lettre = (char)lecture.read();
				if(!lecture.ready())
					//throw new IOException("depassement de fin de fichier");
					return EOF;
			}
			
			return nombre;
		}
		catch(IOException e) {
			System.out.println("FichierASCII - lireNombreChaine() : " + e.getMessage());
			throw(e);
		}
	}
	
	
	
	/**
	 * Retourne la chaine contenant la ligne suivante dans le fichier ASCII.
	 * @return String
	 * @throws IOException : Probl�me de lecture
	 */
	public String lireLigne() throws IOException {
		try {
			if(lecture.ready())
				return lecture.readLine();
			else {
				return EOF;
			}
		}
		catch(IOException e) {
			System.out.println("FichierASCII - lireLigne() : " + e.getMessage());
			throw(e);
		}
	}
	
	
	
	
	

	
	/**
	 * Teste si le caract�re en param�tre est un chiffre.
	 * @param lettre : char
	 * @return boolean
	 */
	private boolean estUnChiffre(char lettre) {		
		switch(lettre) {
			case'-' : return true;
			case'0' : return true;
			case'1' : return true;
			case'2' : return true;
			case'3' : return true;
			case'4' : return true;
			case'5' : return true;
			case'6' : return true;
			case'7' : return true;
			case'8' : return true;
			case'9' : return true;
			case'.' : return true;
			default : return false;
		}
	}
	
	
	/**
	 * Teste si le caract�re en param�tre est un s�parateur.
	 * @param lettre : char
	 * @return boolean
	 */
	private boolean estUnSeparateur(char lettre) {		
		switch(lettre) {
			case' ' : return true;
			case'\b' : return true;
			case'\t' : return true;
			case'\n' : return true;
			case'\f' : return true;
			case'\r' : return true;
			case'\"' : return true;
			case'\\' : return true;
			default : return false;
		}
	}

	
}
