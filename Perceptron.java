package Perceptron;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Perceptron {

	private final int RANDOM_FEATURES = 50;
	private final int NUM_FEATURES = 4;
	private final int MAX_EPOCHS = 1000;
	private final double LEARNING_RATE = 0.025;

	private List<Image> images;
	private Feature[] features;

	public Perceptron(String file) {
		loadImages(file);
		loadFeatures();
		train();
		evaluate();
	}

	private void train() {

		for (int i = 0; i < MAX_EPOCHS; i++) {

			double correct = 0;

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

				// Otherwise we must have been incorrect, update weights
				// accordingly
				for (Feature f : features) {
					f.incrementWeight(error * LEARNING_RATE * f.evaluate(img));
				}

			}

			// If we classified all images correctly
			if (correct == images.size()) {
				System.out.println("Converged after " + i + " epochs");
				break;
			}
		}
	}

	private void evaluate() {
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

		for (Feature f : features) {
			sum += f.getWeight() * f.evaluate(img);
		}

		return sum > 0 ? 1 : 0;

	}

	private void loadImages(String file) {
		images = new ArrayList<Image>();
		try {
			Scanner sc = new Scanner(new File(file));
			while (sc.hasNext()) {
				images.add(new Image(sc));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadFeatures() {
		features = new Feature[RANDOM_FEATURES];
		for (int i = 0; i < RANDOM_FEATURES; i++) {
			features[i] = new Feature(NUM_FEATURES);
		}
	}

	public static void main(String[] args) {
		new Perceptron(args[0]);
	}

}
