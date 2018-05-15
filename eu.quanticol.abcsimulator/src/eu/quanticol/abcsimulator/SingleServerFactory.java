/**
 * 
 */
package eu.quanticol.abcsimulator;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.math3.random.RandomGenerator;
import org.cmg.ml.sam.sim.RandomGeneratorRegistry;
import org.cmg.ml.sam.sim.SimulationFactory;
import org.cmg.ml.sam.sim.sampling.Measure;

/**
 * @author loreti
 *
 */
public class SingleServerFactory implements SimulationFactory<AbCSystem>{
	
	protected int agents;
	private BiFunction<AbCNode, AbCNode, Double> sendingRate;
	private Function<AbCNode, Double> handlingRate;
	private Function<AbCNode, Double> dataRate;
	private int clusterSize;
	// xxx
	private int[] mDemand;
	private boolean[] wRating;
	// xxx
	
	public SingleServerFactory(int agents,
			int clusterSize,
		BiFunction<AbCNode, AbCNode, Double> sendingRate,
		Function<AbCNode, Double> handlingRate,
		Function<AbCNode, Double> dataRate		
	) {
		this.agents = agents;
		this.sendingRate = sendingRate;
		this.handlingRate = handlingRate;
		this.dataRate = dataRate;
		this.clusterSize = clusterSize;
		
		// xxx
		int seed = 1;
		Random rnd = new Random(seed);
		this.mDemand = new int[agents/2];
		this.wRating = new boolean[agents/2];
		for(int i=0; i<agents/2; i++) {
			this.mDemand[i] = rnd.nextBoolean()?1:0;
			this.wRating[i] = rnd.nextBoolean();
		}
		// xxx
 	}	

	@Override
	public AbCSystem getModel() {
		AbCSystem system = new AbCSystem();
		
		ClusterServer root = new ClusterServer(system, 0,clusterSize);

		
		//RandomGenerator r = RandomGeneratorRegistry.getInstance().get();
		
		ArrayList<ComponentNode> nodes = new ArrayList<>();		
		// xxx
		for( int i=0 ; i<agents/2 ; i++ ) {
			ComponentNode n = new ComponentNode(system, i+1, root,new SAInitiator(i, mDemand[i], wRating.length));
			root.addChild( n );
			nodes.add(n);
		}
		for( int i=0 ; i<agents/2 ; i++ ) {
			ComponentNode n = new ComponentNode(system, i+1+agents/2, root,new SAResponder(i, wRating[i]));
			root.addChild( n );
			nodes.add(n);
		}
		// xxx
		

		system.setRoot( root );
		system.setDataRate(dataRate);
		system.setSendingRate(sendingRate);
		system.setHandlingRate(handlingRate);
		system.setAgents(agents);
		return system;
	}

	@Override
	public Measure<AbCSystem> getMeasure(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
