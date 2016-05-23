import java.util.List;

public class Classifier {
	
	private final int NUM_FEATURES;
	
	private int[][] occurences;
	private double[][] probabilities;
	
	private List<Instance> trainingSet;
	
	public Classifier(int numFeatures, List<Instance> trainingSet) {
		this.NUM_FEATURES = numFeatures;
		this.trainingSet = trainingSet;
		
		countOccurences();
	}

	
	public void classifySet(List<Instance> testingSet){
		System.out.println("---------------------------------");
		System.out.println("Classifying set");
		System.out.println("---------------------------------");
		double truePrior = 0;
		double falsePrior = 0;
		
		//Count true/false outcomes from training 
		for(int feature=0; feature<occurences.length; feature++){
			truePrior += occurences[feature][1]+occurences[feature][3];
			falsePrior += occurences[feature][0]+occurences[feature][2];;
		}
		
		//Convert to a probability
		truePrior /= (double)trainingSet.size();
		falsePrior /= (double)trainingSet.size();
		
		for(Instance instance: testingSet){
			
			double trueLikelihood = 1;
			double falseLikelihood = 1;
			
			for(int feature=0; feature<NUM_FEATURES; feature++){
				int featureOutcome = instance.getFeature(feature) ? 0 : 2;
				//+1 as [feature][1] and [feature][3] are the Spam columns
				trueLikelihood *= probabilities[feature][featureOutcome + 1];
				falseLikelihood *= probabilities[feature][featureOutcome];
			}
			
			//Overall predicted probability for true/false
			double trueProbability = truePrior*trueLikelihood;
			double falseProbability = falsePrior*falseLikelihood;
			
			System.out.printf("Input: %s, Spam: %f, !Spam: %f, Outcome: %s\n", instance, trueProbability, falseProbability, (trueProbability > falseProbability ? "spam" : "not spam"));
			
			
		}
	}

	/**
	 * Creates a probability table for
	 * each attribute and outcome.
	 */
	private void countOccurences() {
		
		//X = Attribute X
		//[X][0] = Attribute = true, Not spam
		//[X][1] = Attribute = true, Spam
		//[X][2] = Attribute = false, Not spam
		//[X][3] = Attribute = false, Spam
		occurences = new int[NUM_FEATURES][4];
		
		//Start everything at one (to avoid any 0 occurences).
		for(int feature=0; feature<occurences.length; feature++){
			for(int outcome=0; outcome<occurences[feature].length; outcome++){
				occurences[feature][outcome] = 1;
			}
		}

		//For every instance
		for(Instance instance: trainingSet){

			//Get the outcome and features
			boolean[] features = instance.getFeatures();
			boolean spam = instance.getOutcome();

			//For every feature
			for(int feature=0; feature<features.length; feature++){

				boolean featureOutcome = features[feature];

				//Find the appropriate index
				int index = (featureOutcome ? 0 : 2) + (spam ? 1: 0);

				//Increment the counts for this feature
				occurences[feature][index]++;
			}
			
		}
		//Turn these occurences into probabilities
		calculateProbabilities();
	
	}
	
	/**
	 * Converts all occurence counts to a probability
	 */
	private void calculateProbabilities(){
		probabilities = new double[NUM_FEATURES][4];
		
		for(int feature=0; feature<occurences.length; feature++){
			
			int trueOutcomes = occurences[feature][0]+occurences[feature][2];
			int falseOutcomes = occurences[feature][1]+occurences[feature][3];
			
			for(int outcome=0; outcome<occurences[feature].length; outcome++){
				int total = outcome % 2 == 0 ? trueOutcomes : falseOutcomes;
				probabilities[feature][outcome] = occurences[feature][outcome]/(float)total;
			}
		}
	
		
	}
	
	public void printProbabilities(){
		//Title
		System.out.println("---------------------------------");
		System.out.println("Probailities from training");
		System.out.println("---------------------------------");
		
		//Header of table
		System.out.println("_________________________________");
		System.out.printf("|%5s\t|%5s\t|%5s\t|%5s\t|%n", "T+!S","T+S", "F+!S", "F+S");
		System.out.println("_________________________________");

		//Table body
		for(int feature=0; feature<probabilities.length; feature++){
			System.out.printf("|");
			for(int outcome=0; outcome<probabilities[feature].length; outcome++){
				System.out.printf("%.3f\t|", probabilities[feature][outcome]);
			}
			System.out.println("\n---------------------------------");
		}
	}
	


	/**
	 * Prints the occurences of each feature in a human
	 * readable table format.
	 */
	public void printOccurences(){
		//Title
		System.out.println("---------------------------------");
		System.out.println("Occurences from training");
		System.out.println("---------------------------------");
		
		//Header of table
		System.out.println("_________________________________");
		System.out.printf("|%5s\t|%5s\t|%5s\t|%5s\t|%n", "T+!S","T+S", "F+!S", "F+S");
		System.out.println("_________________________________");

		//Table body
		for(int feature=0; feature<occurences.length; feature++){
			System.out.printf("|");
			int total = 0;
			for(int outcome=0; outcome<occurences[feature].length; outcome++){
				System.out.printf("%5d\t|", occurences[feature][outcome]);
				total+=occurences[feature][outcome];
			}
			System.out.println("\n---------------------------------");
		}
	}

}
