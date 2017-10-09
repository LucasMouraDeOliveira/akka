package counter;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import actors.ChiefActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import message.FileCountMessage;
import scala.concurrent.duration.Duration;

/**
 * Implémentation avec Akka d'un compteur de caractères dans un fichier.
 * Le système Akka va créer n acteurs qui découpent le fichier en part égales 
 * et calculent en parallèle le nombre d'occurence d'un caractère dans leur partie du fichier.
 * 
 * @author Lucas Moura de Oliveira
 *
 */
public class AkkaCounter implements CharCounter {
	
	protected int actorCount;
	
	protected ActorSystem system;
	
	protected ActorRef chiefActor;
	
	protected Inbox inbox;
	
	/**
	 * Créé et initialise le système d'acteurs qui va compter le fichier.
	 * 
	 * @param actorCount le nombre d'acteurs fils à utiliser pour le comptage
	 */
	public AkkaCounter(int actorCount) {
		this.actorCount = actorCount;
		Config config = ConfigFactory.load().withValue("akka.loglevel", ConfigValueFactory.fromAnyRef("OFF"));
		this.system = ActorSystem.create("system", config);
		this.chiefActor = system.actorOf(ChiefActor.props(this.actorCount), "chiefActor");
		this.inbox = Inbox.create(this.system);
	}

	@Override
	public long countCharacters(File file, char c) {
		long result = 0;
		FileCountMessage message = this.createMessage(file, c);
		inbox.send(chiefActor, message);
		try {
		  result = (long)inbox.receive(Duration.create(10, TimeUnit.SECONDS));
		} catch (java.util.concurrent.TimeoutException e) {
			e.printStackTrace();
		}
		this.system.terminate();
		return result;
	}
	
	/**
	 * Crée un message qui indique à l'acteur parent (chiefActor) qu'il doit compter les occurences d'un caractère dans un fichier.
	 * 
	 * @param file le fichier
	 * @param c le caractère à compter
	 * 
	 * @return un message de comptage de fichier
	 */
	private FileCountMessage createMessage(File file, char c) {
		FileCountMessage message = new FileCountMessage();
		message.setFile(file);
		message.setC(c);
		return message;
	}

}
