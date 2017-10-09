package actors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import akka.actor.AbstractActor;
import message.CountOver;
import message.FilePartCounter;

public class SlaveActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(FilePartCounter.class, message -> {
			getSender().tell(compterCharacteres(message), this.getSelf());
		}).build();
	}

	/**
	 * Compte le nombre d'occurences d'un caractère dans une partie de fichier
	 * 
	 * @param message le message contenant la partie du fichier à traiter et le caractère à compter
	 * 
	 * @return le nombre d'occurences du caractère
	 */
	private CountOver compterCharacteres(FilePartCounter message) {
		BufferedReader reader = null; 
		CountOver co = new CountOver();
		co.setActor(this.getSelf());
		try {
			reader = new BufferedReader(new FileReader(message.getFile()));
			reader.skip(message.getStart());
			char[] buffer = new char[(int) message.getSize()];
			int charRead = reader.read(buffer);
			int count = 0;
			for(int i = 0 ; i< charRead ;i++){
				if(buffer[i] == message.getC()) {
					count++;
				}
			}
			co.setCount(count);
		} catch(IOException e) {
			co.setCount(0);;
		} finally {
			try {
				reader.close();
			} catch (IOException e) { }
		}
		return co;
	}

}
