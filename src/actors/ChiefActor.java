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

	protected long totalCount = 0;

	protected Router router;
	
	protected List<Routee> routees;
	
	protected int actorCount;
	
	public ChiefActor(int actorCount) {
		this.actorCount = actorCount;
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
		return receiveBuilder().match(FileCountMessage.class, message -> {
			FilePartCounter fpc;
			long size = message.getFile().length();
			for (int i = 0; i < actorCount; i++) {
				fpc = new FilePartCounter();
				fpc.setFile(message.getFile());
				fpc.setC(message.getC());
				fpc.setStart(i * (size / actorCount));
				fpc.setSize((int)(size / actorCount));
				router.route(fpc, getSender());
			}
		}).match(CountOver.class, message -> {
			totalCount+=message.getCount();
			router = router.removeRoutee(message.getActor());
			ActorRef r = getContext().actorOf(Props.create(SlaveActor.class));
			getContext().watch(r);
			router = router.addRoutee(new ActorRefRoutee(r));
			/*if(messagesRecus == this.actorCount) {
			
			}*/
		}).build();
	}

}