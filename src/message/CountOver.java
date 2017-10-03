package message;

import java.io.Serializable;

import akka.actor.ActorRef;

public class CountOver implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4708888679686961858L;

	protected ActorRef actor;
	
	protected int count;
	
	public CountOver() {
		
	}

	public ActorRef getActor() {
		return actor;
	}

	public void setActor(ActorRef actor) {
		this.actor = actor;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
