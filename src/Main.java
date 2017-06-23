import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {
	
	/**
	 * Width/Height of the Window.
	 */
	public static final int WIDTH = 800, HEIGHT = 450;
	
	/**
	 * Width and Height of preview image. Scaled to be this size always.
	 */
	public static final int PREVIEW_IMG_SIZE = 300;
	
	/**
	 * The current GFile in use. Initially 100kb of noise.
	 */
	private static GFile currentGFile = getRandomGFile(100000);
	
	/**
	 * Main application interface.
	 */
	private static JFrame window = new JFrame("File to PNG");
	
	/**
	 * Panel holds all GUI components in window.
	 */
	private static JPanel panel = new JPanel();
	
	/**
	 * GUI Preview Image.
	 */
	public static JLabel PNGPreview = new JLabel();
	
	/**
	 * Array of GUI buttons.
	 */
	private static JButton[] buttons = new JButton[4];
	
	/**
	 * GUI component that chooses file to convert.
	 */
	private static JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * Array of GUI Button names.
	 */
	public static String[] buttonNames =
		{"Upload a File","Upload a PNG","Save as a File","Save as a PNG"};
	
	/**
	 * Code for buttons to function. (Save/upload/etc.)
	 */
	public static MouseAdapter buttonCode = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			String buttonCommand = ((JButton)e.getSource()).getText();
			int buttonID = Arrays.asList(buttonNames).indexOf(buttonCommand);
			int returnVal;
			switch (buttonID) {
				case 0: //Upload File
					fileChooser.setDialogTitle("Open File...");
					returnVal = fileChooser.showOpenDialog(panel);
					
					//If valid file was chosen
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						currentGFile = new GFile(fileChooser.getSelectedFile());
						setPreviewImage(currentGFile.getPNG());
					}
					break;
				case 1: //Upload PNG
					fileChooser.setDialogTitle("Open File...");
					returnVal = fileChooser.showOpenDialog(panel);
					
					//If valid file was chosen
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						try {
							BufferedImage ginput = ImageIO.read(fileChooser.getSelectedFile());
							currentGFile = new GFile(ginput, "output");
							setPreviewImage(currentGFile.getPNG());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					break;
				case 2: //Save File
					fileChooser.setDialogTitle("Save File as...");
					returnVal = fileChooser.showSaveDialog(panel);
					
					//IF valid name chosen
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						try {
							Files.copy(currentGFile.makeFile().toPath(), fileToSave.toPath());
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					break;
				case 3: //Save PNG
					fileChooser.setDialogTitle("Save GFile PNG as...");
					returnVal = fileChooser.showSaveDialog(panel);
					
					//IF valid name chosen
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						File fileToSave = fileChooser.getSelectedFile();
						try {
							ImageIO.write(currentGFile.getPNG(), "png", fileToSave);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					break;
				default:
			}
		}
	};
	
	/**
	 * Initializes and makes the window visible.
	 */
	public static void setUpWindow() {
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminates on close
		window.setSize(WIDTH, HEIGHT); //set size
		window.setResizable(false);
		window.setLayout(new BorderLayout(WIDTH,HEIGHT));
		
		panel.setPreferredSize(new Dimension(WIDTH/2,HEIGHT));
		window.add(panel, BorderLayout.WEST); //Add panel
		
		panel.add(PNGPreview,BorderLayout.WEST); //Add image preview
		setPreviewImage(getRandomGFile(100000).getPNG()); 
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(WIDTH/2,HEIGHT));
		window.add(buttonPanel, BorderLayout.EAST);
		
		//Initialize Buttons
		for(int i = 0; i < 4; i++){
			buttons[i] = new JButton(buttonNames[i]);
			buttons[i].addMouseListener(buttonCode);
			buttonPanel.add(buttons[i],BorderLayout.SOUTH);
		}
		
		window.setVisible(true);
	}
	
	public static void setPreviewImage(BufferedImage image) {
		PNGPreview.setIcon(new ImageIcon(image.getScaledInstance
				(PREVIEW_IMG_SIZE, PREVIEW_IMG_SIZE, 0)));
	}
	
	public static GFile getRandomGFile(int bytes) {
		byte[] r = new byte[bytes];
		new Random().nextBytes(r);
		return new GFile(r,"random");
	}
	
	/**
	 * The main method. Runs the application.
	 * @param args - No command line arguments expected.
	 */
	public static void main(String[] args) {
		setUpWindow();
	}
}
