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
	public static final int ONE_BYTE_PIXEL = 253;
	
	/**
	 * Alpha value used to denote only 2 bytes stored in pixel.
	 */
	public static final int TWO_BYTE_PIXEL = 254;
	
	/**
	 * Alpha value used to denote 3 bytes stored in pixel.
	 */
	public static final int THREE_BYTE_PIXEL = 255;
		
	
	private BufferedImage png;
	
	public byte[] data;
	
	private String password;
	
	/**
	 * Name of the File.
	 */
	private String fileName;
	
	public GFile(byte[] data, String name, String password) {
		fileName = name; //Initializes File name
		this.data = data; //Initializes data
		png = bytesToPNG(data); //Initializes png
		this.password = password;
	}
	
	/**
	 * Constructs a GFile based on a given file.
	 * @param file - A file to convert into a GFile
	 */
	public GFile(File file, String password) {
		fileName = file.getName(); //Initializes File name
//		byte[] nameBytes = fileName.getBytes(); //get name data
		
		//Get File data
		Path path = file.toPath();
		try {
			data = Files.readAllBytes(path); //initializes data
		} catch (IOException e) {
			e.printStackTrace();
		}
		png = bytesToPNG(data); //Initializes png
		this.password = password;
	}
	
	/**
	 * Constructs a GFile based on a pre-existing GFile PNG file
	 * @param file - An image to convert into a GFile
	 */
	public GFile(BufferedImage png, String name) {
		fileName = name; //Initializes File name
		int[][] pixelMatrix = new int[png.getWidth()][png.getHeight()];
		for(int j = 0; j < png.getWidth(); j++) {
			for(int i = 0; i < png.getHeight(); i++) {
				pixelMatrix[i][j] = png.getRGB(i, j);
			}
		}
		this.png = matrixToPNG(pixelMatrix); //Initializes png
		data = PNGtoBytes(png);  //Initializes data
	}
	
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
	
	public BufferedImage getPNG() {
		return bytesToPNG(data);
	}
	
	public static BufferedImage bytesToPNG(byte[] data) {
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
			pixelList.add(getPixel(newData[i],newData[i+1],newData[i+2],THREE_BYTE_PIXEL));
		if(leftOver == 1) //if 1 extra byte
			pixelList.add(getPixel(newData[newData.length-2],0,0,TWO_BYTE_PIXEL));
		else if(leftOver == 2) //if 2 extra bytes
			pixelList.add(getPixel(newData[newData.length-3],newData[newData.length-2],0,ONE_BYTE_PIXEL));
		return matrixToPNG(squareArray(pixelList)); //convert list to PNG
	}
	
	public static byte[] PNGtoBytes(BufferedImage png) {
		List<Byte> dataList = new ArrayList<Byte>();
		for(int j = 0; j < png.getWidth(); j++) {
			for(int i = 0; i < png.getHeight(); i++) {
				int pixel = png.getRGB(i, j);
				if(pixel != FILLER_PIXEL)
				if(getAlpha(pixel) > (byte)ONE_BYTE_PIXEL) {
					dataList.add(getRed(pixel));
					if(getAlpha(pixel) > (byte)TWO_BYTE_PIXEL) {
						dataList.add(getGreen(pixel));
						if(getAlpha(pixel) == (byte)THREE_BYTE_PIXEL) {
							dataList.add(getBlue(pixel));
						}
					}
				}
				
			}
		}
		//Convert List to array
		byte[] data = new byte[dataList.size()];
		for(int k = 0; k < dataList.size(); k++)
			data[k] = dataList.get(k);
		return data;
	}
	
	public static BufferedImage matrixToPNG(int[][] pixelMatrix) {
		BufferedImage png = new BufferedImage(pixelMatrix.length, 
				pixelMatrix[0].length, BufferedImage.TYPE_INT_ARGB);
		for(int r = 0; r < pixelMatrix[0].length; r++)
			for(int c = 0; c < pixelMatrix.length; c++)
				png.setRGB(c, r, pixelMatrix[r][c]);
		return png;
	}
	
	/**
	 * @param array - A list of pixels.
	 * @return a square matrix of pixels. 
	 *	Extra spots are filled with {@link #FILLER_PIXEL}s
	 */
	private static int[][] squareArray(List<Integer> array) {
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
	
	public static byte getAlpha(int pixel) {
		return (byte) ((pixel >> 24) & (byte)255);
	}
	
	public static byte getRed(int pixel) {
		return (byte) ((pixel >> 16) & (byte)255);
	}
	
	public static byte getGreen(int pixel) {
		return (byte) ((pixel >> 8) & (byte)255);
	}
	
	public static byte getBlue(int pixel) {
		return (byte) ((pixel) & (byte)255);
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
}