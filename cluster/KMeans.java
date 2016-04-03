package Clustering;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class KMeans {
	
	private Set<Iris> instances;

	// How many clusters to create
	private final int K = 3;
	
	public KMeans(String fileName) {
		instances = createSet(fileName);
		cluster();
	}
	
	private void cluster(){	
		//Create k clusters with random start means
		Cluster[] clusters = new Cluster[K];
		
		for(int i=0; i<K; i++){
			clusters[i] = new Cluster(new Iris());
		}
		
		boolean converged = false;
		
		while(!converged){
			
			//Empty the clusters
			for(Cluster c: clusters){
				c.reset();
			}
			
			//Save the old centroids to see if they change this iteration
			Iris[] lastCentroids = new Iris[K];
			for(int i=0; i<K; i++){
				lastCentroids[i] = clusters[i].getMean();
			}
			
			//Find the nearest cluster for each iris
			for(Iris iris: instances){
				double closestDistance = Double.MAX_VALUE;
				Cluster closestCluster = null;

				for(Cluster c: clusters){
					Iris mean = c.getMean();
					double distance = iris.distanceTo(mean);
					
					if(distance<closestDistance){
						closestDistance = distance;
						closestCluster = c;
					}
				}
				
				closestCluster.addPoint(iris);	
			}
			
			//Update the centroid of each cluster
			for(Cluster c: clusters){
				c.updateCentroid();
			}
			
			//Check for convergence
			double difference = 0;
			for(int i=0; i<K; i++){
				difference += clusters[i].getMean().distanceTo(lastCentroids[i]);	
			}
						
			converged = difference ==0 ;
		}
			
		for(Cluster c: clusters){
			System.out.println("Center of cluster " + c.getMean());
			System.out.println("Points in cluster " + c.getPoints().size());
		}
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
			
			while (sc.hasNext()){ 
				Scanner line = new Scanner(sc.nextLine());
				line.useDelimiter(",");
				irisSet.add(new Iris(line));
			}
			
			sc.close();
			return irisSet;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return irisSet;
		}

	}
	
	public static void main(String[] args) {
		new KMeans(args[0]);
	}
	


}



