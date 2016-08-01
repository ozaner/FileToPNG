package application;

import java.awt.Panel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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
	
	public static JButton[] buttons = new JButton[4];
	
	public static String[] buttonNames =
		{"Upload a File","Upload a PNG","Save as a File","Save as a PNG"};
	
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
			panel.add(buttons[i],Panel.BOTTOM_ALIGNMENT);
		}
		
		window.setVisible(true);
		return window;
	}
	
	public static GFile getRandomGFile(int bytes) {
		byte[] r = new byte[bytes];
		new Random().nextBytes(r);
		File file = new File("RandomData.txt");
		FileOutputStream fostrm;
		try {
			fostrm = new FileOutputStream(file);
			fostrm.write(r);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new GFile(file);
	}
	
	/**
	 * The main method. Runs the application.
	 * @param args - No command line arguments expected.
	 */
	public static void main(String[] args) {
		setUpWindow();
		GFile g = new GFile(new File("RandomData.txt"));
		File outputPNG = new File("file.png");
		FileWriter writer;
		try {
			ImageIO.write(g.getPNG(), "png", outputPNG);
			writer = new FileWriter(g.getFile());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}