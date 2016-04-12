import java.util.Random;

public class Feature {

	public final int row[];
	public final int col[];
	public final boolean sgn[];
	
	/**
	 * The dummy feature
	 */
	public Feature(){
		row = null;
		col = null;
		sgn = null;
	}
	
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
		
	}
	

	public Feature(Feature old){
		this.row = old.row;
		this.col = old.col;
		this.sgn = old.sgn;
	}
	
	public int evaluate(boolean[][] img){
		if(row == null) return 1;
		
		int sum = 0;
		
		for(int i=0; i<4 ;i++){
			sum += img[row[i]][col[i]] == sgn[i] ? 1 : 0;
		}
		
		return sum >= 3 ? 1 : 0;
	}
	
	
	@Override
	public String toString() {
		if(row == null){
			return "Dummy feature";
		}
		
		String s = "";
		for(int i=0; i<sgn.length; i++){
			s += "("+row[i]+","+col[i]+"):" +sgn[i]+" ";
		}
		return s;
	}
	
}
