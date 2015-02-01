/**
 * This class provides a photo class which has methods to modify a copy photo
 * to be outlined or photo can also be resized. This class uses the Picture class
 * as the backend to store the image.
 * @author Tu Truong
 */

import java.awt.Color;

public class Photo {
	private Picture originalImage;

	//copy is the picture that's going to be manipulated.
	private Picture copy;
	private final int DEFAULT = 75;

	public Photo(String imagePath){
		this.originalImage = new Picture(imagePath);
		this.copy = new Picture(originalImage);
	}

	public Photo(Picture image){
		this.originalImage = new Picture(image);
		this.copy = new Picture(originalImage);
	}

	/**
	 * If image colors average falls within a certain range, it will be colored
	 * black or white. Outlines the original image copy
	 * */
	public void outline(){
		copy = new Picture(originalImage);
		setOutline(DEFAULT);
	}

	/**
	 * If image colors average falls within a certain range, it will be colored
	 * black or white. Outlines the original image copy
	 * @param outlineMask value to compare RGB average against
	 */
	public void outline(int outlineMask){
		copy = new Picture(originalImage);
		setOutline(outlineMask);
	}
	
	/**
	 * Outlines the current original photo copy.
	 * @param outlineMask value to compare RGB average against.
	 */
	public void outlineOriginalCopy(int outlineMask){
		setOutline(outlineMask);
	}
	
	private void setOutline(int outlineMask){
		for(int i = 0; i < copy.height(); i++){
			for(int j = 0; j < copy.width(); j++){
				Color temp = copy.get(j, i);
				if((temp.getBlue() + temp.getRed() + temp.getGreen())/3 <= outlineMask){
					copy.set(j, i, Color.black);
				} else {
					copy.set(j, i, Color.white);
				}
			}
		}
	}

	/**
	 * @return original image without any modifications.
	 */
	public Picture getOriginalImage(){
		return originalImage;
	}

	/**
	 * @return copy image that may or may not have any modifications.
	 */
	public Picture getCopyImage(){
		return copy;
	}

	/**
	 * This method returns a PhotoText class object that can be used to make the image
	 * into a text file.
	 * @return PhotoText object
	 */
	public PhotoText outlinedToText(){
		return new PhotoText(new Photo(originalImage), -1);
	}
	
	/**
	 * This method returns a PhotoText class object that can be used to make the image
	 * into a text file.
	 * @param outlineMask the value to compare RGB average value against.
	 * @return PhotoText object
	 */
	public PhotoText outlinedToText(int outlineMask){
		return new PhotoText(new Photo(originalImage), outlineMask);
	}

	/**
	 * This method resizes the image based on a passed in factor parameter.
	 * @param factor the integer to scale the image by
	 */
	public void resize(int factor){
		copy.resize(factor);
	}
}
