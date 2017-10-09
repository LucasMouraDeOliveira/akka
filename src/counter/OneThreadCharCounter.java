package counter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Implémentation basique d'un compteur de caractères dans un fichier (un seul thread qui lit un fichier en entier).
 * 
 * @author Lucas Moura de Oliveira
 *
 */
public class OneThreadCharCounter implements CharCounter {

	@Override
	public long countCharacters(File file, char c) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			long count = 0;
			while((line = reader.readLine()) != null) {
				for(int i = 0; i < line.length(); i++) {
					if(line.charAt(i) == c) {
						count++;
					}
				}
			}
			return count;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {}
		}
	}

}
