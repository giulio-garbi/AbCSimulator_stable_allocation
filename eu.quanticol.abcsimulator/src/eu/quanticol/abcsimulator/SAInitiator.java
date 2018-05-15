package eu.quanticol.abcsimulator;

import java.util.Arrays;

public class SAInitiator extends ComponentBehaviour {
	
	enum ProposeStatus {
		WAIT_ACCEPT, SEND_ACK, SUCCESS, TERMINATED
	}
	
	public final int id;
	public final int demand;
	public final ProposeStatus[] proposes;
	public boolean proposeToSend = true;
	public int ref = 0;
	public int partner = -1;
	public boolean success = false;
	public boolean acking = false;
	public final int nResponders;
	public int respRemaining = 0;
	
	public SAInitiator(int id, int demand, int nResponders) {
		this.id = id;
		this.demand = demand;
		this.nResponders = nResponders;
		proposes = new ProposeStatus[this.nResponders];
	}

	@Override
	public boolean onStart(ComponentMessage msg) {
		return true;
	}

	@Override
	public boolean onMessage(ComponentMessage msg) {
		if(msg instanceof SAAccept) {
			SAAccept acc = (SAAccept) msg;
			if(acc.idi == id && !acking && !success) {
				proposes[acc.idr] = ProposeStatus.SEND_ACK;
				acking = true;
				return true;
			}
		}
		if(msg instanceof SANoAccept) {
			SANoAccept nacc = (SANoAccept) msg;
			if(nacc.idi == id) {
				proposes[nacc.idr] = ProposeStatus.TERMINATED;
				respRemaining--;
				if(respRemaining == 0 && !success) {
					ref = (ref+1)%2;
					proposeToSend = true;
					return true;
				} else
					return false;
			}
		}
		if(msg instanceof SADissolveMan) {
			SADissolveMan diss = (SADissolveMan) msg;
			if(diss.idi == id && !acking && diss.idr == partner) {
				proposeToSend = true;
				partner = -1;
				respRemaining = 0;
				success = false;
				return true;
			}
		}
		return false;
	}

	@Override
	public GetMessage getMessage() {
		if(proposeToSend) {
			proposeToSend = false;
			respRemaining = nResponders;
			Arrays.fill(proposes, ProposeStatus.WAIT_ACCEPT);
			return new GetMessage(new SAPropose(demand, id, ref, ref == 0, demand), false);
		}
		for(int i=0; i<proposes.length; i++) {
			if(proposes[i] == ProposeStatus.SEND_ACK) {
				success = true;
				acking = false;
				partner = i;
				proposes[i] = ProposeStatus.SUCCESS;
				respRemaining--;
				return new GetMessage(new SAAck(i, -1, id, demand), false);
			}
		}
		return new GetMessage(null, false);
	}

}
