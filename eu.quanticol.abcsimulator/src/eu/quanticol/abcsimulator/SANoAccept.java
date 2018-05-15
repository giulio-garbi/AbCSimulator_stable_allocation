package eu.quanticol.abcsimulator;

public class SANoAccept extends ComponentMessage {
	public final int idr;
	public final int n;
	public final int idi;
	
	public SANoAccept(int idr, int n, int idi) {
		super("no_accept", idr, n);
		this.idr = idr;
		this.n = n;
		this.idi = idi;
	}
}