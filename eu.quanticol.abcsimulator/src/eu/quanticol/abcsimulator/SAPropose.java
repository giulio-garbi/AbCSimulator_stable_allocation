package eu.quanticol.abcsimulator;

public class SAPropose extends ComponentMessage{
	public final int demand;
	public final int mId;
	public final int ref;
	public final boolean highRating;
	public final int mRank;
	
	public SAPropose(int demand, int mId, int ref, boolean high, int mRank) {
		super("propose", demand, mId, ref);
		this.demand = demand;
		this.mId = mId;
		this.ref = ref;
		this.highRating = high;
		this.mRank = mRank;
	}
}
