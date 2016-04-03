package Clustering;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Iris {
	
	//How many measurements each iris has (sepal width, sepal height etc)
	private final int IRIS_POINTS = 4;
	
	//Hardcoded range values from iris.names, to save needing to calculate every time
	private double[] irisMaximums = new double[]{7.9, 4.4, 6.9,  5};
	private double[] irisMinimums = new double[]{4.3, 2.0, 1.0,  0.1};

	private double[] features = new double[IRIS_POINTS];
	
	/**
	 * Creates a new random iris within the observed range
	 */
	public Iris(){
		Random r = new Random();
		for(int i=0; i<IRIS_POINTS; i++){
			features[i] = irisMinimums[i] + (irisMaximums[i]-irisMinimums[i])*r.nextDouble();
		}
	}
	
	public Iris(Scanner sc) {
		for(int i=0; i < IRIS_POINTS; i++){
			features[i] = sc.nextDouble();
		}
	}
	
	public Iris(double[] features) {
		this.features = features;
	}

	public double getFeature(int i){
		return features[i];
	}
	
	public double distanceTo(Iris o) {
		double d = 0;
		
		for(int i=0; i<features.length; i++){
			d += distanceMeasure(features[i], o.getFeature(i), irisMaximums[i]-irisMinimums[i]);
		}

		return Math.sqrt(d);
	}

	private double distanceMeasure(double a, double b, double range) {
		return Math.pow(a - b, 2)/Math.pow(range, 2);
	}

	@Override
	public String toString() {
		String s = "";
		for(double d: features){
			s += d + " ";
		}
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(features);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Iris other = (Iris) obj;
		if (!Arrays.equals(features, other.features))
			return false;
		return true;
	}

	

	
	

	
}
