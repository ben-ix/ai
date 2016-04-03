package Perceptron;

import java.util.Random;

public class Feature {

	public final int row[];
	public final int col[];
	public final boolean sgn[];
	private double weight;
	
	public Feature(int numFeatures){
		row = new int[numFeatures];
		col = new int[numFeatures];
		sgn = new boolean[numFeatures];
		
		Random r = new Random();
		
		//Randomly choose features
		for(int i=0; i<numFeatures; i++){
			row[i] = r.nextInt(10);
			col[i] = r.nextInt(10);
			sgn[i] = r.nextBoolean();
		}
		
		weight = r.nextDouble();
	}
	
	public int evaluate(Image img){
		int sum = 0;
		
		for(int i=0; i<4 ;i++){
			sum += img.getPixel(row[i], col[i]) == sgn[i] ? 1 : 0;
		}
		
		return sum >= 3 ? 1 : 0;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public void incrementWeight(double inc){
		weight += inc;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i=0; i<sgn.length; i++){
			s += "("+row[i]+","+col[i]+"): " +sgn[i];
		}
		s+= "\n";
		return s;
	}
	
}
