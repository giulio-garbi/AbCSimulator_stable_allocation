package eu.quanticol.abcsimulator;

import java.util.ArrayList;

public class SAResponder extends ComponentBehaviour {
	
	public final ArrayList<SAPropose> proposes;
	public int rank = 2;
	public int partner = -1;
	public int exPartner = -1;
	public final int id;
	public final boolean ranking;
	public boolean dissolveEx = false;
	public boolean waitingAck = false;
	
	public SAResponder(int id, boolean ranking) {
		this.id = id;
		this.ranking = ranking;
		this.proposes = new ArrayList<>();
	}

	@Override
	public boolean onStart(ComponentMessage msg) {
		return onMessage(msg);
	}

	@Override
	public boolean onMessage(ComponentMessage msg) {
		if(msg instanceof SAPropose) {
			proposes.add((SAPropose)msg);
			return true;
		}
		if(msg instanceof SAAck) {
			SAAck ack = (SAAck)msg;
			if(!proposes.isEmpty() && (ack.toWho == -1 || ack.toWho == id) && proposes.get(0).mId == ack.fromWho) {
				waitingAck = false;
				if(ack.whoAck == id) {
					exPartner = partner;
					partner = ack.fromWho;
					dissolveEx = exPartner != -1;
					rank = ack.rankI;
					proposes.remove(0);
					return dissolveEx || !proposes.isEmpty();
				} else {
					proposes.remove(0);
					return dissolveEx || !proposes.isEmpty();
				}
			}
		}
		return dissolveEx || !proposes.isEmpty();
	}

	@Override
	public GetMessage getMessage() {
		if(dissolveEx) {
			dissolveEx = false;
			return new GetMessage(new SADissolveMan(exPartner, id), !proposes.isEmpty() && !waitingAck);
		}
		if(!proposes.isEmpty() && !waitingAck) {
			SAPropose propose = proposes.get(0);
			if(propose.highRating != ranking || propose.mRank >= rank) {
				proposes.remove(0);
				return new GetMessage(new SANoAccept(id, propose.ref, propose.mId), !proposes.isEmpty());
			} else {
				waitingAck = true;
				return new GetMessage(new SAAccept(id, propose.ref, propose.mId), false);
			}
		}
		return new GetMessage(null, false);
	}

}
