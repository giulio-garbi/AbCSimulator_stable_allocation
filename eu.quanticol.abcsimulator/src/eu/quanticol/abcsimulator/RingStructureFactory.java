/**
 * 
 */
package eu.quanticol.abcsimulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.cmg.ml.sam.sim.SimulationFactory;
import org.cmg.ml.sam.sim.sampling.Measure;

/**
 * @author loreti
 *
 */
public class RingStructureFactory implements SimulationFactory<AbCSystem>{
	
	protected int agents;
	private BiFunction<AbCNode, AbCNode, Double> sendingRate;
	private Function<AbCNode, Double> handlingRate;
	private Function<AbCNode, Double> dataRate;
	private int elements;
	// xxx
	private int[] mDemand;
	private boolean[] wRating;
	// xxx
	
	public RingStructureFactory(
			int elements, //Tree levels
			int agents, //Number of leaves for each node at the last level
			//int maxSender, //Max number of senders at the same time (-1 is unbound)
		BiFunction<AbCNode, AbCNode, Double> sendingRate,
		Function<AbCNode, Double> handlingRate,
		Function<AbCNode, Double> dataRate		
	) {
		this.elements = elements;
		this.agents = agents;
		this.sendingRate = sendingRate;
		this.handlingRate = handlingRate;
		this.dataRate = dataRate;
		
		// xxx
		int seed = 1;
		Random rnd = new Random(seed);
		this.mDemand = new int[elements*agents/2];
		this.wRating = new boolean[elements*agents/2];
		for(int i=0; i<elements*agents/2; i++) {
			this.mDemand[i] = rnd.nextBoolean()?1:0;
			this.wRating[i] = rnd.nextBoolean();
		}
		// xxx
 	}	

	@Override
	public AbCSystem getModel() {
		AbCSystem system = new AbCSystem();	
		SharedCounter counter = new SharedCounter();
		ArrayList<RingNode> ringElements = new ArrayList<>();
		int idCounter = 0;
		
		//RandomGenerator r = RandomGeneratorRegistry.getInstance().get();
		
		ArrayList<ComponentNode> nodes = new ArrayList<>();		
		
		// xxx
		int vtxId = 0;
		for( int e=0 ; e<elements ; e++ ) {
			RingNode n = new RingNode(system, idCounter++, counter, ringElements);
			ringElements.add(n);
			for( int i=0 ; i<agents && vtxId < mDemand.length + wRating.length; i++) {
				ComponentNode cn;
				if(vtxId < mDemand.length) {
					cn = new ComponentNode(system, idCounter++, n,new SAInitiator(vtxId, mDemand[vtxId], wRating.length));
				} else  {
					cn = new ComponentNode(system, idCounter++, n,new SAResponder(vtxId - mDemand.length, wRating[vtxId - mDemand.length]));
				}
				n.addChild(cn);
				nodes.add(cn);
				vtxId++;
			}
		}
		// xxx
		for( int e=0 ; e<elements ; e++ ) {
			RingNode n1 = ringElements.get(e);
			RingNode n2 = ringElements.get((e+1)%elements);
			n1.setNext(n2);
		}

		system.setRoot( ringElements.get(0) );
		system.setDataRate(dataRate);
		system.setSendingRate(sendingRate);
		system.setHandlingRate(handlingRate);
		system.setAgents(elements*agents);
		return system;
	}

	@Override
	public Measure<AbCSystem> getMeasure(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
