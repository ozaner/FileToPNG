package application;

import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.GFile;

public class Main {
	
	/**
	 * Width/Height of the Window.
	 */
	public static final int WIDTH = 300, HEIGHT = 300;
	
	/**
	 * Array of GUI buttons.
	 */
	public static JButton[] buttons = new JButton[4];
	
	/**
	 * Array of GUI Button names.
	 */
	public static String[] buttonNames =
		{"Upload a File","Upload a PNG","Save as a File","Save as a PNG"};
	
	public static MouseAdapter buttonCode = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			String buttonCommand = ((JButton)e.getSource()).getText();
			int buttonID = Arrays.asList(buttonNames).indexOf(buttonCommand);
			switch (buttonID) {
				case 0: //Upload File
				case 1: //Upload PNG
				case 2: //Save File
				case 3: //Save PNG
				default:
			}
						
		}
	};
	
	/**
	 * GUI Preview Image.
	 */
	public static JLabel PNGPreview = new JLabel();
	
	public static JFrame setUpWindow() {
		JFrame window = new JFrame("File to PNG"); //create & title window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminates on close
		window.setSize(WIDTH, HEIGHT); //set size
		
		JPanel panel = new JPanel();
		window.add(panel); //Add panel
		PNGPreview.setIcon(new ImageIcon(getRandomGFile(100000).getPNG())); //100kB of random data
		panel.add(PNGPreview,Panel.RIGHT_ALIGNMENT); //Add image preview
		
		//Initialize Buttons
		for(int i = 0; i < 4; i++){
			buttons[i] = new JButton(buttonNames[i]);
			buttons[i].addMouseListener(buttonCode);
			panel.add(buttons[i],Panel.BOTTOM_ALIGNMENT);
		}
		
		window.setVisible(true);
		return window;
	}
	
	public static GFile getRandomGFile(int bytes) {
		byte[] r = new byte[bytes];
		new Random().nextBytes(r);
		return new GFile(r,"random","random");
	}
	
	/**
	 * The main method. Runs the application.
	 * @param args - No command line arguments expected.
	 */
	public static void main(String[] args) {
		setUpWindow();
		String string = "test";
		String str = null;
		try {
			byte[] data = string.getBytes("UTF-8");
			str = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);
		
		BufferedImage ginput = null;
		File input =  new File("input2.png");
		try {
			ginput = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		GFile gfile = new GFile(ginput, "output");
		PNGPreview.setIcon(new ImageIcon(gfile.getPNG()));
		gfile.makeFile("output.png");
		File outputPNG = new File("file.png");
		try {
			ImageIO.write(gfile.getPNG(), "png", outputPNG);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		File input =  new File("input1.png");
//		GFile gfile = new GFile(input,"password");
//		PNGPreview.setIcon(new ImageIcon(gfile.getPNG()));
//		gfile.makeFile("output.png");
//		File outputPNG = new File("file.png");
//		try {
//			ImageIO.write(gfile.getPNG(), "png", outputPNG);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
