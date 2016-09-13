package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GFile {
	
	/**
	 * Used to denote no data in pixel.
	 */
	public static final int FILLER_PIXEL = getPixel((byte)0,(byte)0,(byte)0,(byte)0);
	
	/**
	 * Alpha value used to denote only 1 byte stored in pixel.
	 */
	public static final int ONE_BYTE_EXTRA = 254;
	
	/**
	 * Alpha value used to denote only 2 bytes stored in pixel.
	 */
	public static final int TWO_BYTE_EXTRA = 253;
	
	/**
	 * Matrix of Pixels that comprise png.
	 */
	private int[][] pixelMatrix;
	
	private byte[] data;
	
	/**
	 * Name of the File.
	 */
	public String fileName;
	
	public GFile(byte[] data, String name) {
		fileName = name; //get File name
		fillMatrix(data); //Initializes pixelMatrix
	}
	
	/**
	 * Constructs a GFile based on a given file.
	 * @param file - A file to convert into a GFile
	 */
	public GFile(File file) {
		fileName = file.getName(); //get File name
//		byte[] nameBytes = fileName.getBytes(); //get name data
		
		//Get File data
		Path path = file.toPath();
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fillMatrix(data); //Initializes pixelMatrix
	}
	
	/**
	 * Constructs a GFile based on a pre-existing GFile PNG file
	 * @param file - An image to convert into a GFile
	 */
	public GFile(BufferedImage png, String name) {
		pixelMatrix = new int[png.getWidth()][png.getHeight()];
		for(int i = 0; i < png.getWidth(); i++) {
			for(int j = 0; j < png.getHeight(); j++) {
				pixelMatrix[j][i] = png.getRGB(i, j);
			}
		}
	}
	
	public void fillMatrix(byte[] data) {
		List<Integer> pixelList = new ArrayList<Integer>(); //Create new list to hold pixels.
		
		//Convert bytes to unsigned int
		int[] newData = new int[data.length];
		for(int i = 0; i < newData.length; i++)
			newData[i] = getUnsignedInt(data[i]);
		
//		//Adds name pixels
//		int fullPixels = nameBytes.length/3;
//		int leftOver = nameBytes.length%3;
		
		//Adds data to list
		int leftOver = newData.length%3;
		for(int i = 0; i < newData.length-leftOver; i+=3)
			pixelList.add(getPixel(newData[i],newData[i+1],newData[i+2],255));
		if(leftOver == 1) //if 1 extra byte
			pixelList.add(getPixel(newData[newData.length-2],0,0,ONE_BYTE_EXTRA));
		else if(leftOver == 2) //if 2 extra bytes
			pixelList.add(getPixel(newData[newData.length-3],newData[newData.length-2],0,TWO_BYTE_EXTRA));
		pixelMatrix = arrayToMatrix(pixelList); //convert list to matrix
	}
	
	/**
	 * @return the converted PNG representation of this GFile
	 */
	public BufferedImage getPNG() {
		BufferedImage png = new BufferedImage(pixelMatrix.length, 
				pixelMatrix[0].length, BufferedImage.TYPE_INT_ARGB);
		for(int r = 0; r < pixelMatrix[0].length; r++)
			for(int c = 0; c < pixelMatrix.length; c++)
				png.setRGB(c, r, pixelMatrix[r][c]);
		return png;
	}
	
	/**
	 * @return the original file representation of this GFile.
	 */
	public void makeFile(String name) {
	    //Create and write bytes to file
	    File file = new File(name);
		FileOutputStream fostrm;
		try {
			fostrm = new FileOutputStream(file);
			fostrm.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see java.awt.Color#getRGB()
	 * @param r - Red Value (0-255)
	 * @param g - Green Value (0-255)
	 * @param b - Blue Value (0-255)
	 * @param a - Alpha Value (0-255)
	 * @return Returns an integer representation of a pixel given a,r,g,b.
	 */
	public static int getPixel(int r, int g, int b, int a) {
		return new java.awt.Color(r,g,b,a).getRGB(); //a<<24 + r<<16 + g<<8 + b;
	}
	
	/**
	 * @param x - a byte [-128,127]
	 * @return the unsigned integer representation of the byte. [0,255]
	 */
	public static int getUnsignedInt(byte x) {
	    return x & 0xFF;
	}
	
	/**
	 * @param array - A list of pixels.
	 * @return a square matrix of pixels. 
	 *	Extra spots are filled with {@link #FILLER_PIXEL}s
	 */
	private static int[][] arrayToMatrix(List<Integer> array) {
		int size = 0;
		if(Math.sqrt(array.size()) == (int)Math.sqrt(array.size()))
			size = (int) Math.sqrt(array.size());
		else
			size = (int)Math.sqrt(array.size())+1;
		
		int[][] matrix = new int[size][size];
		int index = 0;
		for(int r = 0; r < size; r++)
			for(int c = 0; c < size; c++)
				if(index < array.size()) {
					matrix[r][c] = array.get(index);
					index++;
				}
				else
					matrix[r][c] = FILLER_PIXEL;
		return matrix;
	}
}