import java.awt.Color;
/**
 * This class creates a backend object that will convert the black and white outline
 * image into a text string
 * @author Tu Truong
 *
 */
public class PhotoText {
	
	private Photo image;
	private String imageString;
	private final int DEFAULT = 75;
	
	/**
	 * Constructor will scale the image down depending on how big the image is
	 * and then the image is converted to a string otherwise, it'd be too big.
	 * @param image Photo object that will be converted to string
	 * @param outlineMask The value to compare average of RGB value against.
	 */
	public PhotoText(Photo image, int outlineMask){
		
		this.image = image;
		
		//Since image's weight or height may be bigger, 
		//it will scale the image based on that.
		int height = this.image.getOriginalImage().height(), 
			width = this.image.getOriginalImage().width(),
			base = height > width ? height : width,
			count = 0;
	
		while(base > 400){
			count++;
			base /= 2;
		}	

		if(count > 0)
			image.resize(count*2);
		
		if(outlineMask != -1)
			this.image.outlineOriginalCopy(outlineMask);
		else
			this.image.outlineOriginalCopy(DEFAULT);
		
		this.imageString = "";
		
	}
	
	/**
	 * Converts the image to a string.
	 */
	public void texify(){
		Picture copy = this.image.getCopyImage();
		for(int i = 0; i < copy.height(); i++){
			for(int j = 0; j < copy.width(); j++){
				if(copy.get(j, i).equals(Color.black))
					imageString += "*";
				else
					imageString += " ";
			}
			imageString += "\n";
		}
	}
	
	/**
	 * It will print out the image string to the command line.
	 * More than likely, the image won't fit in the command line.
	 */
	public void print(){
		System.out.println(imageString);
	}
	
	/**
	 * @return the image string
	 */
	public String getString(){
		return imageString;
	}
}
