package counter;

import java.io.File;

/**
 * Compteur de caractères dans un fichier.
 * 
 * @author Lucas Moura de Oliveira
 *
 */
public interface CharCounter {
	
	/**
	 * Compte le nombre d'instances d'un caractère dans un fichier texte.
	 * 
	 * @param file le fichier
	 * @param c le caractère à compter
	 * 
	 * @return le nombre d'instances du fichier dans le caractère
	 */
	public long countCharacters(File file, char c);

}
