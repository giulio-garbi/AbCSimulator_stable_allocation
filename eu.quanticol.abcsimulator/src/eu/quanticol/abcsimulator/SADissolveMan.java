package eu.quanticol.abcsimulator;

public class SADissolveMan extends ComponentMessage {
	public final int idi;
	public final int idr;
	
	public SADissolveMan(int idi, int idr) {
		super("dissolve");
		this.idi = idi;
		this.idr = idr;
	}

}
