import java.util.Scanner;
import java.util.regex.Pattern;

public class Image {

	private int outcome;
	private boolean[][] img;
	private boolean features[];

	public Image(Scanner sc, Feature[] features) {
		if (!sc.next().equals("P1")) System.out.println("Not a valid P1 PBM file");
		
		outcome = sc.next().substring(1).equals("Yes") ? 1 : 0;
		int rows = sc.nextInt();
		int cols = sc.nextInt();

		img = new boolean[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				img[r][c] = sc.findWithinHorizon(Pattern.compile("[01]"), 0).equals("1");
			}
		}
		
		this.features = new boolean[features.length];
		for(int i=0; i<this.features.length; i++){
			this.features[i] = features[i].evaluate(img) == 1;
		}	
	}
	
	public int hasFeature(int index){
		return features[index] ? 1 : 0;
	}
	
	
	public boolean getPixel(int row, int col){
		return img[row][col];
	}
	
	public int getDesiredClass(){
		return outcome;
	}

}
