package Perceptron;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Image {

	private int outcome;
	private boolean[][] img;

	public Image(Scanner sc) {
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
	}
	public boolean getPixel(int row, int col){
		return img[row][col];
	}
	
	public int getDesiredClass(){
		return outcome;
	}

}
