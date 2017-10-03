package main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import counter.OneThreadCharCounter;
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
		long start = System.currentTimeMillis();
		long count = counter1.countCharacters(file, 'a');
		long time = (System.currentTimeMillis()-start);
		System.out.println(count);
		System.out.println("Temps comptage  : " + time + " ms");
		
		
	}

}
