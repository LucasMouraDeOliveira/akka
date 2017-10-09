package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import counter.OneThreadCharCounter;
import counter.AkkaCounter;
import counter.CharCounter;

public class Main {
	
	public static void main(String[] args) {
		
		if(args.length < 1) {
			System.err.println("Pas d'arguments. Fin du programme.");
			System.exit(0);
		}
		
		String fileName = args[0];
		if(!Files.exists(Paths.get(fileName))) {
			System.err.println("Le fichier n'existe pas.");
			System.exit(0);
		}
		
		File file = new File(fileName);
		CharCounter counter1 = new OneThreadCharCounter();
		CharCounter counter2 =  new AkkaCounter(5);
		
		logExecutionTime(counter1, "SimpleCounter", file, 'a');
		logExecutionTime(counter2, "AkkaCounter", file, 'a');
		
	}
	
	/**
	 * Affiche dans la console le temps d'éxecution d'un compteur de caractères.
	 * 
	 * @param counter le compteur
	 * @param counterName le nom apparent du compteur
	 * @param file le fichier à compter
	 * @param c le caractère à compter dans le fichier
	 */
	public static void logExecutionTime(CharCounter counter, String counterName, File file, char c) {
		long time = System.currentTimeMillis();
		long results = counter.countCharacters(file, c);
		time = (System.currentTimeMillis()-time);
		System.out.println("Counter " + counterName + " counted " + results + " instances of '" + c + "' in the file " + file.getName() + " in " + time + " ms");
	}

}
