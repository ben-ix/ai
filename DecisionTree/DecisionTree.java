import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class DecisionTree {
	
	private List<String> categoryNames;
	private ArrayList<String> attrNames;
	private Set<Instance> trainingInstances;
	private Node rootNode;
	
	public DecisionTree(String trainingFile, String testFile) {
		
		readDataFile(trainingFile);
		
		//Make a copy of the attributes, so we do not remove from the original list
		List<String> startingAttrnames = (ArrayList<String>) attrNames.clone();
		
		//Construct the tree
		rootNode = buildTree(trainingInstances, startingAttrnames);
		
		//Print out the tree
		rootNode.report(" ");
		
		//Run test instances against training instances
		classifyTestingSet(testFile);
	}
	
	private void classifyTestingSet(String testFile){
		try {
			Scanner s = new Scanner(new File(testFile));
			
			//Skip the two descriptor lines
			s.nextLine(); s.nextLine();
			
			Set<Instance> testingInstances = readInstances(s);
			
			int correct = 0;
			
			//Classify every instance
			for(Instance i: testingInstances){
				String guessed = findCategoryFromTree(rootNode, i);
				String desired = categoryNames.get(i.getCategory());
				correct += guessed.equals(desired) ? 1 : 0;
			}

			System.out.println("\nAccuracy " + correct +" /" + testingInstances.size());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		
		
	}
	
	private String findCategoryFromTree(Node node, Instance instance){
		if(node instanceof InternalNode){
			int attribute = ((InternalNode)node).getAttr();
			//If the attribute is true, go left down the tree
			
			if(instance.getAtt(attribute)){
				return findCategoryFromTree(((InternalNode) node).left, instance);
			}
			//Otherwise go right
			else {
				return findCategoryFromTree(((InternalNode) node).right, instance);
			}
			
		}
		
		//We are at the bottom of the tree, so just return the category
		return ((LeafNode)node).getCategory();
		
	}
	
	private Node buildTree(Set<Instance> instances, List<String> attributes){
		
		//Base line predictor
		if(instances.isEmpty()){
			return mostProbableNode(trainingInstances);
		}
		
		//If all categories are the same, return the category
		if(isPure(instances)){
			int category = instances.iterator().next().getCategory();
			return new LeafNode(category, 1);
		}
		
		//If we are at the bottom of the tree,  return mode of categories
		if(attributes.isEmpty()){
			return mostProbableNode(instances);
		}
		
		int bestAtt = -1;
		double bestWeightedPurity = -1;
		Set<Instance> bestInstsTrue = new HashSet<Instance>();
		Set<Instance> bestInstsFalse = new HashSet<Instance>();;
		
		//Find the 'best' attribute
		for(int attribute = 0; attribute<attributes.size(); attribute++){
			
			//Get the original index (from the file) of this attribute
			int attributeIndex = attrNames.indexOf(attributes.get(attribute));
			
			Set<Instance> trueOutcome = new HashSet<Instance>();
			Set<Instance> falseOutcome = new HashSet<Instance>();
			
			//Seperate each instance into true/false set
			for(Instance instance: instances){
				if(instance.getAtt(attributeIndex)) trueOutcome.add(instance);
				else falseOutcome.add(instance);
			}
			
			//Calculate purity for the sets
			double truePurity = impurity(trueOutcome);
			double falsePurity = impurity(falseOutcome);

			double weightedPurity = trueOutcome.size()/(double)instances.size() * truePurity +
					falseOutcome.size()/(double)instances.size() * falsePurity;
			
			//Check if this is the most pure attribute so far
			if(weightedPurity > bestWeightedPurity){
				bestAtt = attributeIndex;
				bestWeightedPurity = weightedPurity;
				bestInstsTrue  = trueOutcome;
				bestInstsFalse = falseOutcome;
			}
			
		}
		
		//Remove the best attribute, so we don't keep choosing the same attribute
		String bestAttribute = (String) attrNames.get(bestAtt);
		attributes.remove(bestAttribute);
		
		//Construct child trees
		Node left = buildTree(bestInstsTrue, attributes);
		Node right = buildTree(bestInstsFalse, attributes);
		
		return new InternalNode(bestAtt, left, right);
		
	}
	
	private double impurity(Set<Instance> instances) {
		Map<Integer, Integer> categoryCounts = calculateCategoryFrequencies(instances);
		
		double impurity = 1;
		
		for(Integer category: categoryCounts.keySet()){
			double frequency = categoryCounts.get(category);
			impurity *= frequency / instances.size();
		}

		return impurity;
	}
	
	private Node mostProbableNode(Set<Instance> instances){
		Map<Integer, Integer> categoryCounts = calculateCategoryFrequencies(instances);

		int maxFrequency = -1;
		int majorityCategory = -1;
		
		for(Integer category : categoryCounts.keySet()){
			int count = categoryCounts.get(category);
			if(count > maxFrequency){
				maxFrequency = count;
				majorityCategory = category;
			}
		}
		
		//Calculate the probability
		double probability = maxFrequency/(double)instances.size();
		
		return new LeafNode(majorityCategory, probability);
	}
	
	/**
	 * Returns a map which has the category as the key, 
	 * and the frequency of the category as the value
	 * 
	 * @param instances
	 * @return
	 */
	private Map<Integer, Integer> calculateCategoryFrequencies(Set<Instance> instances){
		Map<Integer, Integer> categoryCounts = new HashMap<Integer, Integer>();
		
		for(Instance i: instances){
			int category = i.getCategory();
			
			if(!categoryCounts.containsKey(category)){
				categoryCounts.put(category, 0);
			}
			
			categoryCounts.put(category, categoryCounts.get(category) + 1);
		}
		return categoryCounts;
	}

	/**
	 * Returns true iff every Instance in the set
	 * has the same category.
	 * 
	 * @param instances
	 * @return
	 */
	private boolean isPure(Set<Instance> instances){
		int lastCategory = instances.iterator().next().getCategory();
		for(Instance i : instances){
			if(i.getCategory()!=lastCategory) return false;
			lastCategory = i.getCategory();
		}
		return true;
	}

	private void readDataFile(String filename){
		try {
			categoryNames = new ArrayList<String>();
			attrNames = new ArrayList<String>();
			
			Scanner sc = new Scanner(new File(filename));
			
			for (Scanner s = new Scanner(sc.nextLine()); s.hasNext();) categoryNames.add(s.next());
			for (Scanner s = new Scanner(sc.nextLine()); s.hasNext();) attrNames.add(s.next());

			trainingInstances = readInstances(sc);
			sc.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Data File caused IO exception");
		}
	}
		
	private Set<Instance> readInstances(Scanner sc){
		Set<Instance> instances = new HashSet<Instance>();
		
		while (sc.hasNext()){ 
			Scanner line = new Scanner(sc.nextLine());
			instances.add(new Instance(categoryNames.indexOf(line.next()),line));
		}
		
		return instances;
	}

	public static void main(String[] args) {
		if(args.length!=2){
			System.out.println("Requires two params: trainingFile testingFile");
			return;
		}
		new DecisionTree(args[0], args[1]);	
	}
	
	
	/** Classes used for the Tree from here on*/
	
	private class Instance{
		
		private int category;
		private List<Boolean> attr;

		public Instance(int cat, Scanner s){
			category = cat;
			attr = new ArrayList<Boolean>();
			while (s.hasNextBoolean()) attr.add(s.nextBoolean());
		}
		
		public boolean getAtt(int index){
			return attr.get(index);
		}

		public int getCategory(){
			return category;
		}

		public String toString(){
			StringBuilder ans = new StringBuilder(categoryNames.get(category));
			ans.append(" ");
			for (Boolean val : attr)
				ans.append(val?"true  ":"false ");
			return ans.toString();
		}

	}
	
	interface Node{
		public void report(String indent);
	}
	
		
	private class InternalNode implements Node{
		
		private int attr;
		private Node left;
		private Node right;
		
		public InternalNode(int attr, Node left, Node right) {
			this.attr = attr;
			this.left = left;
			this.right = right;
		}
		
		public int getAttr(){
			return attr;
		}
		
		@Override
		public void report(String indent){
			System.out.format("%s%s = True:\n", indent, attrNames.get(attr));
			left.report(indent+"   ");
			System.out.format("%s%s = False:\n", indent, attrNames.get(attr));
			right.report(indent+"   ");
		}

	}
	
	private class LeafNode implements Node{
		
		private int category;
		private double probability;
		
		public LeafNode(int category, double probability) {
			this.category = category;
			this.probability = probability;
		}
		
		public String getCategory(){
			return categoryNames.get(category);
		}
		
		@Override
		public void report(String indent) {
			System.out.format("%sClass %s, prob=%4.2f\n", indent, categoryNames.get(category), probability);
		}
	
	}
	
	
	
}
