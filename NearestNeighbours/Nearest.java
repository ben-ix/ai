import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


public class Nearest {
	
	// How many neighbors to find
	private final int K = 3;

	
	public Nearest(String trainingFile, String testFile) {
		//Construct our two sets
		Set<Iris> trainingSet = createSet(trainingFile);
		Set<Iris> testingSet = createSet(testFile);
		
		//Trains and classifies the data
		classify(trainingSet, testingSet);
	}

	/**
	 * Classifies and calculates the percentage 
	 * of correct classifications in the testingSet
	 * against the training set.
	 */
	private void classify(Set<Iris> trainingSet, Set<Iris> testingSet) {
		int correct = 0;
		
		for (Iris iris : testingSet) {
			
			//Keep neighbours in queue, to get the K closest neighbours quickly
			PriorityQueue<Result> neighbours = new PriorityQueue<Result>(new Comparator<Result>() {
				@Override
				public int compare(Result a, Result b) {
					return a.DISTANCE < b.DISTANCE ? -1 : a.DISTANCE > b.DISTANCE ? 1 : 0;
				}
			});
			
			// Find the distance to every neighbor in training set
			for (Iris other : trainingSet) {
				Result result = new Result(other.CLASS, iris.distanceTo(other));
				neighbours.add(result);
			}
			
			//Take the K closest neighbours
			Result[] closestNeighbours = new Result[K];
			for(int i=0; i<K; i++){
				closestNeighbours[i] = neighbours.poll();
			}

			// Find the most common class among these K neighbours
			String type = mostCommonType(closestNeighbours);
			if(type.equals(iris.CLASS)){
				correct++;
			}
		}
		
		System.out.printf("K = %d Accuracy: %d/%d\n",K,correct,testingSet.size());
	}

	/**
	 * Returns the mode of @param list
	 * @param list
	 * @return
	 */
	private String mostCommonType(Result[] list) {
		HashMap<String, Integer> occurences = new HashMap<String, Integer>();

		int maxFrequency = 0;
		String mostFrequent = null;

		for (Result r : list) {
			String type = r.CLASS;

			// If we have not seen this type yet, add it
			if (!occurences.containsKey(type)) {
				occurences.put(type, 0);
			}

			// Increment the frequency by 1
			int frequency = occurences.get(type) + 1;
			occurences.put(type, frequency);

			// Check if we have a new most frequent type
			if (frequency > maxFrequency) {
				mostFrequent = type;
				maxFrequency = frequency;
			}

		}

		return mostFrequent;

	}

	/**
	 * Reads iris data from fileName
	 * into a set, and returns this set.
	 * 
	 * @param fileName
	 * @return
	 */
	private Set<Iris> createSet(String fileName) {
		Set<Iris> irisSet = new HashSet<>();

		try {
			Scanner sc = new Scanner(new File(fileName));
			while (sc.hasNext()) {
				Scanner line = new Scanner(sc.nextLine());
				Iris i = new Iris(line);
				irisSet.add(i);
			}
			sc.close();
			return irisSet;
		} catch (FileNotFoundException e) {
			return irisSet;
		}

	}

	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("Must be ran with two arguments - train.file test.file");
		}

		new Nearest(args[0], args[1]);
	}
	
	
	/**
	 * Wrapper class to hold the outcomes
	 * of K-NN.
	 */
	private class Result {

		public final String CLASS;
		public final double DISTANCE;

		public Result(String type, double distance) {
			this.CLASS = type;
			this.DISTANCE = distance;
		}

		@Override
		public String toString() {
			return DISTANCE + " " + CLASS;
		}
	}

}




