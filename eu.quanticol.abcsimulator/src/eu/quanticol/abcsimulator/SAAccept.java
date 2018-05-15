package eu.quanticol.abcsimulator;

public class SAAccept extends ComponentMessage {
	public final int idr;
	public final int n;
	public final int idi;
	
	public SAAccept(int idr, int n, int idi) {
		super("accept", idr, n);
		this.idr = idr;
		this.n = n;
		this.idi = idi;
	}
}
