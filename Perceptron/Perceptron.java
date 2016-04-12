import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Perceptron {

	//Constants for the perceptron
	private final int RANDOM_FEATURES = 50;
	private final int NUM_FEATURES = 4;
	private final int MAX_EPOCHS = 2000;
	private final double LEARNING_RATE = 0.025;

	//Images to train and classify
	private List<Image> images;
	//Perceptrons features (dummy feature is at index 0)
	private Feature[] features;
	//Weights for each feature (matching indices to features)
	private double[] weights;

	public Perceptron(String file) {
		loadFeatures();
		loadImages(file);
		train();
		classify();
		report();
	}
	
	private void report(){
		System.out.println("Features:");
		for(int i=0; i<features.length; i++){
			System.out.printf("\tWeight: %.2f Feature: %s\n", weights[i],features[i]);
		}
	}

	private void train() {
		
		//Pocket algorithm that stores highest features when dealing with case of non-linear seperability
		double highestAccuracy = 0;
		Feature[] pocket = new Feature[features.length];

		for (int epoch = 0; epoch < MAX_EPOCHS; epoch++) {

			int correct = 0;

			// Classify each image
			for (Image img : images) {

				int outcome = classify(img);
				int desired = img.getDesiredClass();

				double error = desired - outcome;

				// Skip if we were correct
				if (error == 0) {
					correct++;
					continue;
				}

				// Otherwise we must have been incorrect, update weights accordingly
				for (int featureIndex = 0; featureIndex < features.length; featureIndex++) {
					weights[featureIndex] += error * LEARNING_RATE * img.hasFeature(featureIndex);
				}

			}
			
			//Save this into our "pocket" if its the highest accuracy
			if(correct > highestAccuracy){
				highestAccuracy = correct;
				
				//Make copies of the feature, so we don't accidentally update the stored feature
				for(int i = 0; i<features.length; i++){
					pocket[i] = new Feature(features[i]); 
				}
				
			}
			

			// If we classified all images correctly, exit
			if (correct == images.size()) {
				System.out.println("Converged: " + epoch + " epochs");
				return;
			}
		}
		
		//At this point we must not have converged, so update our features to the best we found
		features = pocket;
		System.out.println("Did not converge. Using best stored features." );
	}

	private void classify() {
		int correct = 0;

		for (Image img : images) {

			double outcome = classify(img);
			double desired = img.getDesiredClass();

			correct += desired == outcome ? 1 : 0;
		}

		System.out.println("Correct: " + correct + "/" + images.size());
	}

	/**
	 * Classifies an image as either 1: Yes or 0: No according to the features
	 * 
	 * 
	 * @param img
	 * @return classification
	 */
	private int classify(Image img) {
		double sum = 0;

		for (int featureIndex = 0; featureIndex < features.length; featureIndex++) {
			sum += weights[featureIndex] * img.hasFeature(featureIndex);
		}

		return sum > 0 ? 1 : 0;

	}

	private void loadImages(String file) {
		images = new ArrayList<Image>();
		try {
			Scanner sc = new Scanner(new File(file));
			while (sc.hasNext()) {
				images.add(new Image(sc, features));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadFeatures() {
		features = new Feature[RANDOM_FEATURES + 1];
		weights = new double[features.length];
		
		Random r = new Random();
		
		//Dummmy feature
		features[0] = new Feature();
		weights[0] = -1;
		
		for (int i = 1; i < features.length; i++) {
			features[i] = new Feature(NUM_FEATURES);
			//Random weight between -.5 and .5
			weights[i] = -.5 + r.nextDouble();
		}
	}

	public static void main(String[] args) {
		new Perceptron(args[0]);
	}

}
