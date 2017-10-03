package counter;

import java.io.File;

import actors.ChiefActor;
import message.FileCountMessage;

public class AkkaCounter implements CharCounter {
	
	protected int actorCount;
	
	public AkkaCounter(int actorCount) {
		this.actorCount = actorCount;
	}

	@Override
	public long countCharacters(File file, char c) {
		long fileSize = file.length();
		ChiefActor chief = new ChiefActor(100);
		FileCountMessage message = new FileCountMessage();
		message.setFile(file);
		message.setC(c);
		//chief.getSelf().
		//chief.getSelf().tell(message, null);
		//return chief.recevoirResultatJeNeSaisPasComment();
		return 0;
	}

}
