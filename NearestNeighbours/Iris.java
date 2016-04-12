import java.util.Scanner;

public class Iris {

	// How many measurements there are on an iris (ie sepal length/sepal width etc)
	private final int IRIS_POINTS = 4;

	// Hardcoded from iris.names to avoid needing to calculate it every time
	private double[] IRIS_RANGE = new double[] { 3.6, 2.2, 5.9, 2.4 };

	public final String CLASS;
	private double[] features = new double[IRIS_POINTS];

	public Iris(Scanner sc) {
		for (int i = 0; i < IRIS_POINTS; i++) {
			features[i] = sc.nextDouble();
		}

		CLASS = sc.next();
	}

	public double getFeature(int i) {
		return features[i];
	}

	/**
	 * Uses weighted euclidean distance to find
	 * the distance to the specified Iris.
	 * @param iris to compute distance to
	 * @return distance
	 */
	public double distanceTo(Iris o) {
		double d = 0;

		for (int i = 0; i < features.length; i++) {
			d += distanceMeasure(features[i], o.getFeature(i), IRIS_RANGE[i]);
		}

		return Math.sqrt(d);
	}

	private double distanceMeasure(double a, double b, double range) {
		return Math.pow(a - b, 2) / Math.pow(range, 2);
	}

	@Override
	public String toString() {
		String s = "";
		for (double feature : features) {
			s += feature + " ";
		}
		return s;
	}

}
