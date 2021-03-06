/**
 * 
 */
package eu.quanticol.abcsimulator.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;

import org.cmg.ml.sam.sim.SimulationEnvironment;
import org.cmg.ml.sam.sim.sampling.SamplingCollection;
import org.cmg.ml.sam.sim.sampling.SimulationTimeSeries;
import org.cmg.ml.sam.sim.sampling.StatisticSampling;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.ToolbarArmedXYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import eu.quanticol.abcsimulator.AbCSystem;
import eu.quanticol.abcsimulator.AverageDeliveryTime;
import eu.quanticol.abcsimulator.AverageInputQueueSize;
import eu.quanticol.abcsimulator.AverageMessageInterval;
import eu.quanticol.abcsimulator.AverageWaitingQueueSize;
import eu.quanticol.abcsimulator.MaxDeliveryTime;
import eu.quanticol.abcsimulator.MaxMessageInterval;
import eu.quanticol.abcsimulator.MinDeliveryTime;
import eu.quanticol.abcsimulator.MinMessageInterval;
import eu.quanticol.abcsimulator.NumberOfDeliveredMessages;
import eu.quanticol.abcsimulator.P2PStructureFactory;
import eu.quanticol.abcsimulator.RingStructureFactory;
import eu.quanticol.abcsimulator.SingleServerFactory;
import eu.quanticol.abcsimulator.TravellingMessages;
import eu.quanticol.abcsimulator.TreeStructureFactory;

/**
 * @author loreti
 *
 */
public class SimulationBatch {

	public static double TRANSMISSION_RATE = 15.0;
	
	public static double SENDIND_RATE = 1.0;
	
	public static double HANDLING_RATE = 1000.0;
	
	public static String T_PREFIX = "results/T";

	public static String R_PREFIX = "results/R";

	public static String C_PREFIX = "results/C";

	public static double simulationTime = Double.POSITIVE_INFINITY;//30000;
	public static int samples = 100;
	public static int replications = 100;

	
	public static void runTreeSimulation( int levels , int children , int agents ) throws FileNotFoundException {
//		TreeStructureFactory factory = new TreeStructureFactory(3,5,5,10, (x,y) -> 15.0 , x -> 1000.0 , x -> 1.0 );
		P2PStructureFactory factory = new P2PStructureFactory(levels,children,agents, (x,y) -> TRANSMISSION_RATE , x -> HANDLING_RATE , x -> SENDIND_RATE );
		SimulationEnvironment<AbCSystem> env = new SimulationEnvironment<>(factory);
		StatisticSampling<AbCSystem> averageDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new AverageDeliveryTime());
		StatisticSampling<AbCSystem> maxDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MaxDeliveryTime());
		StatisticSampling<AbCSystem> minDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MinDeliveryTime());
		StatisticSampling<AbCSystem> numberOfDeliveredMessages = new StatisticSampling<>(samples, simulationTime/samples, new NumberOfDeliveredMessages());
//		StatisticSampling<AbCSystem> averageWaitingSize = new StatisticSampling<>(samples, simulationTime/samples, new AverageWaitingQueueSize());
		StatisticSampling<AbCSystem> averageTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new AverageMessageInterval());
		StatisticSampling<AbCSystem> maxTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MaxMessageInterval());
		StatisticSampling<AbCSystem> minTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MinMessageInterval());
		SamplingCollection<AbCSystem> collection = new SamplingCollection<>( averageDeliveryTime, maxDeliveryTime, minDeliveryTime, numberOfDeliveredMessages, averageTimeInterval,maxTimeInterval,minTimeInterval);
		
//		averageWaitingSize.printTimeSeries(System.out);
		env.setSampling(collection);
		env.simulate(replications,simulationTime);			
		saveSimulationData( T_PREFIX+"_"+levels+"_"+children+"_"+agents ,
			averageDeliveryTime ,
			maxDeliveryTime ,
			minDeliveryTime ,
			numberOfDeliveredMessages ,
			averageTimeInterval ,
			maxTimeInterval ,
			minTimeInterval,
			env.getAvgExecTime(),
			env.getStimDevStdExecTime()
		);
	}

	public static void runClusterSimulation( int cluster , int agents ) throws FileNotFoundException {
//		TreeStructureFactory factory = new TreeStructureFactory(3,5,5,10, (x,y) -> 15.0 , x -> 1000.0 , x -> 1.0 );
		SingleServerFactory factory = new SingleServerFactory(agents, cluster, (x,y) -> TRANSMISSION_RATE , x -> HANDLING_RATE , x -> SENDIND_RATE );
		SimulationEnvironment<AbCSystem> env = new SimulationEnvironment<>(factory);
		StatisticSampling<AbCSystem> averageDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new AverageDeliveryTime());
		StatisticSampling<AbCSystem> maxDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MaxDeliveryTime());
		StatisticSampling<AbCSystem> minDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MinDeliveryTime());
		StatisticSampling<AbCSystem> numberOfDeliveredMessages = new StatisticSampling<>(samples, simulationTime/samples, new NumberOfDeliveredMessages());
//		StatisticSampling<AbCSystem> averageWaitingSize = new StatisticSampling<>(samples, simulationTime/samples, new AverageWaitingQueueSize());
		StatisticSampling<AbCSystem> averageTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new AverageMessageInterval());
		StatisticSampling<AbCSystem> maxTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MaxMessageInterval());
		StatisticSampling<AbCSystem> minTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MinMessageInterval());
		SamplingCollection<AbCSystem> collection = new SamplingCollection<>( averageDeliveryTime, maxDeliveryTime, minDeliveryTime, numberOfDeliveredMessages, averageTimeInterval,maxTimeInterval,minTimeInterval);
		
//		averageWaitingSize.printTimeSeries(System.out);
		env.setSampling(collection);
		env.simulate(replications,simulationTime);			
		saveSimulationData( C_PREFIX+"_"+cluster+"_"+agents ,
			averageDeliveryTime ,
			maxDeliveryTime ,
			minDeliveryTime ,
			numberOfDeliveredMessages ,
			averageTimeInterval ,
			maxTimeInterval ,
			minTimeInterval,
			env.getAvgExecTime(),
			env.getStimDevStdExecTime()
		);
	}

	public static void runRingSimulation( int elements , int agents) throws FileNotFoundException {
//		TreeStructureFactory factory = new TreeStructureFactory(3,5,5,10, (x,y) -> 15.0 , x -> 1000.0 , x -> 1.0 );
		RingStructureFactory factory = new RingStructureFactory(elements,agents, (x,y) -> TRANSMISSION_RATE , x -> HANDLING_RATE , x -> SENDIND_RATE );
		SimulationEnvironment<AbCSystem> env = new SimulationEnvironment<>(factory);
		StatisticSampling<AbCSystem> averageDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new AverageDeliveryTime());
		StatisticSampling<AbCSystem> maxDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MaxDeliveryTime());
		StatisticSampling<AbCSystem> minDeliveryTime = new StatisticSampling<>(samples, simulationTime/samples, new MinDeliveryTime());
		StatisticSampling<AbCSystem> numberOfDeliveredMessages = new StatisticSampling<>(samples, simulationTime/samples, new NumberOfDeliveredMessages());
//		StatisticSampling<AbCSystem> averageWaitingSize = new StatisticSampling<>(samples, simulationTime/samples, new AverageWaitingQueueSize());
		StatisticSampling<AbCSystem> averageTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new AverageMessageInterval());
		StatisticSampling<AbCSystem> maxTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MaxMessageInterval());
		StatisticSampling<AbCSystem> minTimeInterval = new StatisticSampling<>(samples, simulationTime/samples, new MinMessageInterval());
		SamplingCollection<AbCSystem> collection = new SamplingCollection<>();// averageDeliveryTime, maxDeliveryTime, minDeliveryTime, numberOfDeliveredMessages, averageTimeInterval,maxTimeInterval,minTimeInterval);
		
//		averageWaitingSize.printTimeSeries(System.out);
		env.setSampling(collection);
		env.simulate(replications,simulationTime);			
		saveSimulationData( R_PREFIX+"_"+elements+"_"+agents ,
			averageDeliveryTime ,
			maxDeliveryTime ,
			minDeliveryTime ,
			numberOfDeliveredMessages ,
			averageTimeInterval ,
			maxTimeInterval ,
			minTimeInterval,
			env.getAvgExecTime(),
			env.getStimDevStdExecTime()
		);
	}

	
	private static void saveSimulationData(String name, StatisticSampling<AbCSystem> averageDeliveryTime,
			StatisticSampling<AbCSystem> maxDeliveryTime, StatisticSampling<AbCSystem> minDeliveryTime,
			StatisticSampling<AbCSystem> numberOfDeliveredMessages, StatisticSampling<AbCSystem> averageTimeInterval,
			StatisticSampling<AbCSystem> maxTimeInterval, StatisticSampling<AbCSystem> minTimeInterval,
			double avgExecTime, double stimDevStdExecTime) throws FileNotFoundException {
		/*doSave( name+"_avg_dt_.dat",averageDeliveryTime);
		doSave( name+"_min_dt_.dat",minDeliveryTime);
		doSave( name+"_max_dt_.dat",maxDeliveryTime);
		doSave( name+"_avg_ti_.dat",averageTimeInterval);
		doSave( name+"_min_ti_.dat",minTimeInterval);
		doSave( name+"_max_ti_.dat",maxTimeInterval);
		doSave( name+"_dm_.dat",numberOfDeliveredMessages);*/
		
		File execTimeFile = new File(name+"_exec_time.txt");
		PrintStream pw = new PrintStream(execTimeFile);
		double deltaET = stimDevStdExecTime * 1.96;
		pw.println("" + avgExecTime + "\t" + (-deltaET)+"\t"+deltaET);
		pw.close();
	}


	private static void doSave(String string, StatisticSampling<AbCSystem> stat) throws FileNotFoundException {
		File file = new File(string);
		PrintStream pw = new PrintStream(file);
		stat.getSimulationTimeSeries(replications).get(0).printTimeSeries(pw);
	}

	public static void batch(int k) throws FileNotFoundException {
		int n = 31*k;
		/*System.out.println("C[10,"+n+"] ");
		runClusterSimulation(10, n);
		System.out.println("C[20,"+n+"] ");
		runClusterSimulation(20, n);*/
		System.out.println("C[1,"+n+"] ");
		runClusterSimulation(1, n);
		System.out.println("C[31,"+n+"] ");
		runClusterSimulation(31, n);
		//System.out.println("R["+k+",31]");
		//runRingSimulation(k, 31);
		System.out.println("R[31,"+k+"]");
		runRingSimulation(31, k);
		//System.out.println("T[5,2,"+k+"]");
		//runTreeSimulation(5, 2, k);
		System.out.println("T[3,5,"+k+"]");
		runTreeSimulation(3, 5, k);
	}
	
	public static void main(String[] argv) throws FileNotFoundException {
		int[] ks = new int[] {4,6,8};
		for(int i=0; i<ks.length; i++) {
			batch(ks[i]);
		}
	}
}
