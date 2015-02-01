import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


/**
 * Source: <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 */
public final class Picture{
    private BufferedImage image;               // the rasterized image
    private String filename;                   // name of file
    private boolean isOriginUpperLeft = true;  // location of origin
    private int width, height;           // width and height

   /**
     * Initializes a blank <tt>width</tt>-by-<tt>height</tt> picture, with <tt>width</tt> columns
     * and <tt>height</tt> rows, where each pixel is black.
     */
    public Picture(int width, int height) {
        if (width  < 0) throw new IllegalArgumentException("width must be nonnegative");
        if (height < 0) throw new IllegalArgumentException("height must be nonnegative");
        this.width  = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = width + "-by-" + height;
    }

   /**
     * Initializes a new picture that is a deep copy of <tt>picture</tt>.
     */
    public Picture(Picture picture) {
        width  = picture.width();
        height = picture.height();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        filename = picture.filename;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                image.setRGB(col, row, picture.get(col, row).getRGB());
    }

   /**
     * Initializes a picture by reading in a .png, .gif, or .jpg from
     * the given filename or URL name.
     */
    public Picture(String filename) {
        this.filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) { url = new URL(filename); }
                image = ImageIO.read(url);
            }
            width  = image.getWidth(null);
            height = image.getHeight(null);
        }
        catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }
    }

   /**
     * Initializes a picture by reading in a .png, .gif, or .jpg from a File.
     */
    public Picture(File file) {
        try { image = ImageIO.read(file); }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        width  = image.getWidth(null);
        height = image.getHeight(null);
        filename = file.getName();
    }
    
    public BufferedImage getImage(){
    	return image;
    }

   /**
     * Sets the origin to be the upper left pixel. This is the default.
     */
    public void setOriginUpperLeft() {
        isOriginUpperLeft = true;
    }

   /**
     * Sets the origin to be the lower left pixel.
     */
    public void setOriginLowerLeft() {
        isOriginUpperLeft = false;
    }

   /**
     * Returns the height of the picture.
     * @return the height of the picture (in pixels)
     */
    public int height() {
        return height;
    }

   /**
     * Returns the width of the picture.
     * @return the width of the picture (in pixels)
     */
    public int width() {
        return width;
    }

   /**
     * Returns the color of pixel (<tt>col</tt>, <tt>row</tt>).
     * @return the color of pixel (<tt>col</tt>, <tt>row</tt>)
     * @throws IndexOutOfBoundsException unless both 0 &le; <tt>col</tt> &lt; <tt>width</tt>
     * and 0 &le; <tt>row</tt> &lt; <tt>height</tt>
     */
    public Color get(int col, int row) {
        if (col < 0 || col >= width())  throw new IndexOutOfBoundsException("col must be between 0 and " + (width()-1));
        if (row < 0 || row >= height()) throw new IndexOutOfBoundsException("row must be between 0 and " + (height()-1));
        if (isOriginUpperLeft) return new Color(image.getRGB(col, row));
        else                   return new Color(image.getRGB(col, height - row - 1));
    }

   /**
     * Sets the color of pixel (<tt>col</tt>, <tt>row</tt>) to given color.
     * @throws IndexOutOfBoundsException unless both 0 &le; <tt>col</tt> &lt; <tt>width</tt>
     * and 0 &le; <tt>row</tt> &lt; <tt>height</tt>
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     */
    public void set(int col, int row, Color color) {
        if (col < 0 || col >= width())  throw new IndexOutOfBoundsException("col must be between 0 and " + (width()-1));
        if (row < 0 || row >= height()) throw new IndexOutOfBoundsException("row must be between 0 and " + (height()-1));
        if (color == null) throw new NullPointerException("can't set Color to null");
        if (isOriginUpperLeft) image.setRGB(col, row, color.getRGB());
        else                   image.setRGB(col, height - row - 1, color.getRGB());
    }

   /**
     * Is this Picture equal to obj?
     * @return <tt>true</tt> if this picture is the same dimension as <tt>obj</tt>
     * and if all pixels have the same color
     */
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Picture that = (Picture) obj;
        if (this.width()  != that.width())  return false;
        if (this.height() != that.height()) return false;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                if (!this.get(col, row).equals(that.get(col, row))) return false;
        return true;
    }
    
    /**
     * This method uses the Scalr class to scale the image down since images may be too
     * big at times.
     * @param factor the int value to scale the image by.
     */
    public void resize(int factor){
    	this.image = Scalr.resize(image, width/factor, height/factor, Scalr.OP_ANTIALIAS);
    	this.width = image.getWidth();
    	this.height = image.getHeight();
    }

}