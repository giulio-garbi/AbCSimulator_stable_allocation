package eu.quanticol.abcsimulator;

public class SAAck extends ComponentMessage {
	public final int toWho;
	public final int whoAck;
	public final int fromWho;
	public final int rankI;
	
	public SAAck(int whoAck, int toWho, int fromWho, int rankI) {
		super("ack", whoAck);
		this.whoAck = whoAck;
		this.toWho = toWho;
		this.fromWho = fromWho;
		this.rankI = rankI;
	}

}
