import java.util.Arrays;
import java.util.Scanner;

public class Instance {

	//The attributes of this instance
	private final boolean[] features;

	//True if outcome was spam
	private boolean outcome;

	public Instance(String line){
		
		//Space seperated
		String[] instance = line.split("\\s+");

		//First item is blank space due to file structure, remove it
		instance = Arrays.copyOfRange(instance, 1, instance.length);

		//One of the items is the outcome, remainder must be features
		int numFeatures = instance.length;

		features = new boolean[numFeatures];

		//Populate the features
		for(int i=0; i<numFeatures; i++){
			//Features are represented as 1s or, so convert to boolean
			features[i] = instance[i].equals("1");
		}
				
	}

	/**
	 * Creates an instance with no outcome,
	 * ie a training instance.
	 * @param line
	 */
	public Instance(boolean hasOutcome, String line){
		//Space seperated
		String[] instance = line.split("\\s+");

		//First item is blank space due to file structure, remove it
		instance = Arrays.copyOfRange(instance, 1, instance.length);

		//One of the items is the outcome, remainder must be features
		int numFeatures = instance.length - 1;

		features = new boolean[numFeatures];

		//Populate the features
		for(int i=0; i<numFeatures; i++){
			//Features are represented as 1s or, so convert to boolean
			features[i] = instance[i].equals("1");
		}

		//Last element is outcome
		outcome = instance[numFeatures].equals("1");

	}

	public boolean getFeature(int index){
		return features[index];
	}

	public boolean[] getFeatures(){
		return features;
	}

	/**
	 * Returns true if the outcome
	 * is Spam, False if not.
	 *
	 * @return
	 */
	public boolean getOutcome(){
		return outcome;
	}

	@Override
	public String toString() {
		String r = "(";

		for(boolean b: features){
			r += (b ? "1" : "0") + ",";
		}

		r += outcome ? "1" : "0";
		return r+")";
	}



}
