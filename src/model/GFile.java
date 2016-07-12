package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GFile {
	private int[][] pixelMatrix;
	
	//TEST CONSTRUCTOR
	public GFile() {
		pixelMatrix = new int[20][20];
		pixelMatrix[0][9] = getPixel(255,0,0,255);
		pixelMatrix[1][8] = getPixel(255,0,0,255);
		pixelMatrix[2][7] = getPixel(255,0,0,255);
		pixelMatrix[3][1] = getPixel(255,0,0,255);
		pixelMatrix[4][2] = getPixel(255,0,0,255);
		pixelMatrix[5][3] = getPixel(255,0,0,255);
		pixelMatrix[6][4] = getPixel(255,0,0,255);
		pixelMatrix[7][5] = getPixel(255,0,0,255);
	}
	
	/**
	 * @param file - A file to convert into a GFile
	 */
	public GFile(File file) {
		
	}
	
	/**
	 * @param file - An image to convert into a GFile
	 */
	public GFile(BufferedImage png) {
		
	}
	
	public BufferedImage getPNG() {
		BufferedImage png = new BufferedImage(pixelMatrix.length, pixelMatrix[0].length, BufferedImage.TYPE_INT_ARGB);
		
		for(int r = 0; r < pixelMatrix.length; r++) {
			for(int c = 0; c < pixelMatrix[0].length; c++) {
				png.setRGB(r, c, pixelMatrix[r][c]);
			}
		}
		return png;
	}
	
	public File getFile() {
		return null;
	}
	
	public static int getPixel(int r, int b, int g, int a) {
		return new java.awt.Color(r,g,b,a).getRGB();
	}
	
	public void draw() {
		
	}
	
	public static void main(String[] args) throws IOException {
		GFile g = new GFile();
		
		JFrame j = new JFrame("TEST");
		j.add(new JLabel(new ImageIcon(g.getPNG())));
		j.setVisible(true);
		
		File outputfile = new File("image.png");
		ImageIO.write(g.getPNG(), "png", outputfile);
	}
}