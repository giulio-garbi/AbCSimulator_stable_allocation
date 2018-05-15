/**
 * 
 */
package eu.quanticol.abcsimulator;

import java.util.LinkedList;

import org.cmg.ml.sam.sim.sampling.Measure;

/**
 * @author loreti
 *
 */
public class NumberOfDeliveredMessages implements Measure<AbCSystem> {

	@Override
	public double measure(AbCSystem t) {
		return t.getDeliveryTime().size();
	}

	@Override
	public String getName() {
		return "NUMBER OF DELIVERED MESSAGES";
	}

}
