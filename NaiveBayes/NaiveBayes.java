import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NaiveBayes {
	
	private Classifier classifier;

	public NaiveBayes(String training, String testing) {

		List<Instance> trainingSet = populateSet(true, training);
		List<Instance> testingSet = populateSet(false, testing);

		//Ensure sure we have some data to train from
		if(trainingSet.isEmpty()){
			System.out.println("Training set is empty. Please load again with a new file");
			return;
		}
		
		//Assume all instances have the same Number of features
		int numFeatures = trainingSet.get(0).getFeatures().length;

		classifier = new Classifier(numFeatures, trainingSet);
		
		classifier.printOccurences();
		classifier.printProbabilities();
		classifier.classifySet(testingSet);	
	}
	


	/**
	 * Generates and returns a list of Instances from the
	 * provided file. If the file does not exist, or is empty,
	 * the returned list will be empty (not null).
	 *
	 * @param fileName
	 * @return
	 */
	private List<Instance> populateSet(boolean training, String fileName){
		List<Instance> instances = new ArrayList<Instance>();

		try {
			Scanner sc = new Scanner(new File(fileName));

			//Read all the instances
			while(sc.hasNext()){
				//Each line corresponds to one instance
				if(training){
					instances.add(new Instance(true, sc.nextLine()));
				}
				else{
					instances.add(new Instance(sc.nextLine()));
				}
			}

			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return instances;
	}



	public static void main(String[] args) {

		//Ensure appropriate command line values entered
		if(args.length!=2){
			System.out.println("Error: input format must be training.dat testing.dat");
			return;
		}

		new NaiveBayes(args[0], args[1]);
	}

}
