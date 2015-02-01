import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This class creates the frame to display the image on as well as provides other
 * options. 
 * @author Tu Truong
 *
 */
public class PhotoFrame {

	private JFrame frame;
	private Photo image;
	private String filename;
	private final JPanel pictureBox;
	private final JPanel sliderBox;
	private int outlineMask = 0;
	private boolean outlineImage = false;

	public PhotoFrame(Photo image){
		this.image = image;
		this.pictureBox = new JPanel();
		this.sliderBox = new JPanel();
	}

	public void display(){
		frame = new JFrame();
		frame.setJMenuBar(createMenuBar());
		JPanel everything = new JPanel(new BorderLayout());
		pictureBox.add(getJLabel(true));
		sliderBox.add(createCheckbox());
		sliderBox.add(createSlider());
		everything.add(pictureBox, BorderLayout.NORTH);
		everything.add(sliderBox, BorderLayout.SOUTH);
		frame.add(everything);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("Photo Frame");
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}


	/**
	 * Saves as new file.
	 * @param file
	 */
	private void save(File file) {
		this.filename = file.getName();
		if (frame != null) { frame.setTitle(filename); }
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")) {
			try { ImageIO.write(image.getCopyImage().getImage(), suffix, file); }
			catch (IOException e) { e.printStackTrace(); }
		}
		else {
			System.out.println("Error: filename must end in .jpg or .png");
		}
	}

	private void save(String name) {
		save(new File(name));
	}

	/**
	 * @param original check for whether geting original or image copy.
	 * @return JLabel image.
	 */
	private JLabel getJLabel(boolean original) {
		if (image == null) { return null; }        // no image available
		ImageIcon icon;
		if(original)
			icon = new ImageIcon(image.getOriginalImage().getImage());
		else{
			image.outline(outlineMask);
			icon = new ImageIcon(image.getCopyImage().getImage());
		}
		return new JLabel(icon);
	}

	/**
	 * 
	 * @return JMenuItem for saving the outlined image.
	 */
	private JMenuItem createSaveMenu(){
		JMenuItem saveMenu = new JMenuItem(" Save...   ");
		saveMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileDialog chooser = new FileDialog(frame,
						"Use a .png or .jpg extension", FileDialog.SAVE);
				chooser.setVisible(true);
				if (chooser.getFile() != null) {
					save(chooser.getDirectory() + File.separator + chooser.getFile());
				}

			}

		});
		saveMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		return saveMenu;
	}

	/**
	 * @return JCheckBox object for switching to outlined image or original image.
	 */
	private JCheckBox createCheckbox(){
		JCheckBox outline = new JCheckBox("Outline");
		outline.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(outlineImage){
					outlineImage = false;
					changeImage(true);
				} else {
					outlineImage = true;
					changeImage(false);
				}
			}

		});
		return outline;
	}

	/**
	 * @return JMenuItem for exporting image out as string text file.
	 */
	private JMenuItem exportText(){
		JMenuItem exportTextMenu = new JMenuItem(" Export to text ");
		exportTextMenu.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				FileDialog chooser = new FileDialog(frame,
						"Use .txt extension.", FileDialog.SAVE);
				chooser.setVisible(true);
				if (chooser.getFile() != null) {
					try {
						PrintWriter writer = new PrintWriter(chooser.getDirectory() + File.separator + chooser.getFile());
						PhotoText toText = image.outlinedToText(outlineMask);
						toText.texify();
						Scanner scan = new Scanner(toText.getString());
						while(scan.hasNextLine()){
							writer.println(scan.nextLine());
						}
						scan.close();
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

		});

		return exportTextMenu;
	}
	
	/**
	 * @return JMenuItem object that allows you to choose an image file from a file explorer.
	 */
	private JMenuItem openFileImage(){
		JMenuItem openImage = new JMenuItem (" Open Image from Explorer ");
		openImage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog chooser = new FileDialog(frame,
						"Use .txt extension.", FileDialog.LOAD);
				chooser.setVisible(true);
				if(chooser.getFile() != null){
					image = new Photo(chooser.getDirectory() + File.separator + chooser.getFile());
					if(!outlineImage)
						changeImage(true);
					else
						changeImage(false);
				}
			}
			
		});
		return openImage;
	}
	
	/**
	 * @return JMenuItem for option to open an image from a URL.
	 */
	private JMenuItem openURLImage(){
		JMenuItem openURL = new JMenuItem(" Open Image from URL ");
		openURL.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = JOptionPane.showInputDialog("Enter url for image.");
				if(msg == null)
					JOptionPane.showMessageDialog(null, "You didn't enter anything.");
				else {
					image = new Photo(msg);
					if(!outlineImage)
						changeImage(true);
					else
						changeImage(false);
				}
			}
		});
		return openURL;
	}

	/**
	 * @return JSlider object for adjusting the threshold.
	 */
	private JSlider createSlider(){
		final JSlider slider = new JSlider(0,255);
		slider.setValue(75);
		slider.setMajorTickSpacing(51);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		outlineMask = slider.getValue();
		slider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				outlineMask = slider.getValue();
				if(outlineImage){
					changeImage(false);
				}
			}

		});
		return slider;
	}

	/**
	 * @param change boolean value that will change the image based on swithcing it to original image or outlined.
	 */
	private void changeImage(boolean change){
		pictureBox.removeAll();
		pictureBox.add(getJLabel(change));
		frame.pack();
	}

	/**
	 * @return JMenuBar object that will have options such as save or export text file.
	 */
	private JMenuBar createMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		menu.add(createSaveMenu());
		menu.add(openFileImage());
		menu.add(openURLImage());
		menu.add(exportText());
		return menuBar;
	}
}
