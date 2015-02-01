import java.io.FileNotFoundException;

/**
 * The class that contains the main method to run everything. It will create a frame
 * and pass in the image that will take in an image path from the command line.
 * @author Tu Truong
 *
 */
public class PhotoManipulator {

	public static void main(String[] args) throws FileNotFoundException {
		Photo batman = new Photo("http://cdn3.belfasttelegraph.co.uk/migration_catalog/article25727633.ece/20322/ALTERNATES/h342/Batman");
		PhotoFrame frame = new PhotoFrame(batman);
		frame.display();
	}

}
