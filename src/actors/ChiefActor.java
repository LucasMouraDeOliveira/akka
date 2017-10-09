package actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import message.CountOver;
import message.FileCountMessage;
import message.FilePartCounter;

public class ChiefActor extends AbstractActor {

	protected long totalCount;
	
	protected int actorCount, messagesRecus;

	protected Router router;
	
	protected List<Routee> routees;
	
	protected ActorRef inbox;
	
	public ChiefActor(int actorCount) {
		this.actorCount = actorCount;
		this.totalCount = 0;
		this.messagesRecus = 0;
		this.routees = new ArrayList<Routee>();
		for (int i = 0; i < actorCount; i++) {
			ActorRef r = getContext().actorOf(Props.create(SlaveActor.class));
			getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		this.router = new Router(new RoundRobinRoutingLogic(), routees);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(FileCountMessage.class, message -> handleFileCountMessage(message, getSender()))
				.match(CountOver.class, message -> handleCountOverMessage(message))
				.build();
	}
	
	/**
	 * Gère la réception d'un message de type 'FileCountMessage' qui indique que l'acteur doit compter les instances d'un caractères dans un fichier.
	 * 
	 * @param message le message
	 * @param sender l'expéditeur du message
	 */
	private void handleFileCountMessage(FileCountMessage message, ActorRef sender) {
		this.inbox = sender;
		FilePartCounter fpc;
		long size = message.getFile().length();
		for (int i = 0; i < actorCount; i++) {
			fpc = new FilePartCounter();
			fpc.setFile(message.getFile());
			fpc.setC(message.getC());
			fpc.setStart(i * (size / actorCount));
			fpc.setSize((int)(size / actorCount));
			router.route(fpc, getSelf());
		}
	}
	
	/**
	 * Gère la réception d'un message de type 'CountOver' qui indique qu'un acteur fils a fini de compter sa partie du fichier.
	 * Lorsque tout les acteurs ont fini de compter, l'acteur courant dépose un message dans l'inbox du système pour indiquer que le traitement est fini.
	 * 
	 * @param message le message de retour d'un acteur fils
	 */
	private void handleCountOverMessage(CountOver message) {
		this.messagesRecus++;
		this.totalCount+=message.getCount();
		this.router = router.removeRoutee(message.getActor());
		ActorRef r = getContext().actorOf(Props.create(SlaveActor.class));
		getContext().watch(r);
		this.router = router.addRoutee(new ActorRefRoutee(r));
		if(this.messagesRecus == this.actorCount) {
			this.inbox.tell(this.totalCount, getSelf());
		}
	}

	public static Props props(int actorCount) {
		return Props.create(ChiefActor.class, () -> new ChiefActor(actorCount));
	}
	
}