package vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class CaptureImage extends JPanel implements Runnable {
    private VideoCapture cap;    // capture
    private  byte [] b;           // pixel bytes
    private BufferedImage image; // our drawing canvas
    private Thread idx_Thread;
    private BufferedImage template = GetBufferedImage("template.gif");
    private Boolean capturepixelrgb = false;
    
    public CaptureImage() {
    	setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        init();
    }
    
    public void init() {      
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cap = new VideoCapture(0); // 1st webcam
    }

    public void start() {
        if (idx_Thread == null)    {
            idx_Thread = new Thread(this);
            idx_Thread.start();
        }
    }

    public void stop() {
        if (idx_Thread != null)    {
            idx_Thread.interrupt();
            idx_Thread = null;
        }
    }

    public void run() {
        Mat m = new Mat();
        while(true) {
            if (cap.read(m) ) {
                convert(m); // mat to BufferedImage
                repaint();
   
            } else break;
            try    {
                Thread.sleep(20);
            } catch (InterruptedException e){}
        }
    }
    public void convert(Mat m){            
        Mat m2 = new Mat();    
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            Imgproc.cvtColor(m,m2,Imgproc.COLOR_BGR2RGB);
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int blen = m.channels()*m.cols()*m.rows();
        if ( b == null || b.length != blen)
            b = new byte[blen];
        m2.get(0,0,b);
        image = new BufferedImage(m.cols(),m.rows(), type);
        image.getRaster().setDataElements(0, 0, m.cols(),m.rows(), b);   
        image = flipVertical(image);
        RescaleOp rescaleOp = new RescaleOp(2f, 20, null);
        rescaleOp.filter(image, image);  // Source and destination are the same
        
        //only run when user wants to predict colors on a cube face
        if(capturepixelrgb == true){
        	splitImage(image);
        	capturepixelrgb = false;
            
        }
    }

    public void CapturePixels(){
    	capturepixelrgb = true;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 400, 300, null);          
        g.drawImage(template, 0, 0, 310, 300, null);     
    }   
    
    public BufferedImage flipVertical(BufferedImage src){
        AffineTransform tx=AffineTransform.getScaleInstance(-1.0,1.0);  //scaling
        tx.translate(-src.getWidth(),0);  //translating
        AffineTransformOp tr=new AffineTransformOp(tx,null);  //transforming
        
        return tr.filter(src, null);  //filtering
       }
   
    public BufferedImage GetBufferedImage(String src){
    BufferedImage img = null;
    try {
        img = ImageIO.read(new File(src));
    } catch (IOException e) {
    }
    return img;
    }

    private ArrayList<SquareColor> getPixelArrayFromImage(BufferedImage image){
		ArrayList<SquareColor> colorarray = new ArrayList<SquareColor>();
    	int w = image.getWidth();
    	int h = image.getHeight();
        for (int x = 0; x < w; x++){
        	for (int y = 0; y < h; y++) {
        		SquareColor square = new SquareColor(new Color(image.getRGB(x, y)));
        		colorarray.add(square);
        	}
        }
		return  colorarray;
    }
    
    //get averafe
    private  SquareColor getAveragePixelFromImageArray( ArrayList<SquareColor> colorarray){
    	int red = 0;
    	int blue = 0;
    	int green =0;
    	int count = 0;
    	for(SquareColor c : colorarray){
    		red += c.getRed();
    		blue += c.getBlue();
    		green += c.getGreen();
    		count++;
    	}
    	
    	red = red / count;
    	blue = blue / count;
    	green = green / count;
    	
    	SquareColor avgsquarecolor = new SquareColor(red, blue, green);
    	avgsquarecolor.printcolors();
		return avgsquarecolor;
    }
    

    //split image in 9 quadrants (quadrant per cube square)
    public void splitImage(BufferedImage image) {
        int rows = 3; //You should decide the values for rows and cols variables
        int cols = 3;
        int chunks = rows * cols;

        int chunkWidth = (image.getWidth()-150) / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());
                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }

        //writing mini images into image files
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = cropImage(imgs[i]);
            SquareColor avgsquarecolor = getAveragePixelFromImageArray( getPixelArrayFromImage(imgs[i]) );
            
            //SaveImageDataToArffFile("colortrain.arff", "green",  avgsquarecolor, true); //for buuilding traijnig set only
            SaveImageDataToArffFile("weka/colortest.arff", "unknown",  avgsquarecolor, false);
            
            try {
				WekaMachineLearning w = new WekaMachineLearning();
				w.getColorarray();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
    }
    
    public BufferedImage cropImage(BufferedImage image){
    	BufferedImage croppedImage = image.getSubimage(70, 70, image.getWidth()-110, image.getHeight()-110);
		return croppedImage;	
    }
    
    
    //temp class used when creating training set for weka
    private void SaveImageDataToArffFile(String filename, String color,  SquareColor square, Boolean append){
    	//save to arff file
    	String header = "@relation color\n@attribute avgblue numeric\n@attribute avgred numeric\n@attribute avggreen numeric\n@attribute class {red,blue,green,orange,yellow,white,unknown}\n\n@data\n";
    	try{
    	    FileWriter fw = new FileWriter(filename, append); //the true will append the new data
    	    if(!append){
    	    	fw.write(header);
    	    }
    	    fw.write(square.getBlue()+","+ square.getRed()+"," +square.getGreen()+","+color+"\n");//appends the string to the file
    	    fw.close();
    	}
    	catch(IOException ioe){
    	    System.out.println("IOException: " + ioe.getMessage());
    	}
    }
    

    
    
    
}