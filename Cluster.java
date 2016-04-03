package Clustering;

import java.util.ArrayList;
import java.util.List;

public class Cluster{
	
	private Iris centroid;
	private List<Iris> points;
	
	public Cluster(Iris centroid) {
		this.centroid = centroid;
		this.points = new ArrayList<Iris>();
	}
	
	public boolean addPoint(Iris i){
		return points.add(i);
	}
	
	public Iris getMean(){
		return centroid;
	}
	
	public List<Iris> getPoints(){
		return points;
	}
	
	public void reset(){
		points.clear();
	}
	
	/**
	 * Updates the centroid to be the middle of all
	 * features
	 */
	public void updateCentroid(){			
		double[] features = new double[4];
		
		//Calculate sum of each value
		for(Iris iris: points){
			for(int i=0; i<features.length; i++){
				features[i] += iris.getFeature(i);
			}
		}
		
		//Average each value
		for(int i=0; i<features.length; i++){
			features[i] /= points.size();		
		}
		//Update to this newly found centroid
		centroid = new Iris(features);
	}
	
	@Override
	public String toString() {
		return centroid + " Size: " + points.size();
	}
}