//Created By Jordan Waddell
//Final year project
//Solving A Rubiks Cube Using Robotics And Vision

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
	private static final long serialVersionUID = 1L;
	private VideoCapture cap;    // capture
    private  byte [] b;           // pixel bytes
    private BufferedImage image; // our drawing canvas
    private Thread idx_Thread;
    private BufferedImage template = GetBufferedImage("images/template.gif");
    private Boolean capturepixelrgb = false;
    private  ArrayList<Color> colorarray = new ArrayList<Color>();
    private Boolean previewready = false;
    private Boolean training;
    private Color trainingcolor;
    private Boolean webcamavailable = false;
    

	public CaptureImage() {
    	setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        init();
    }
    
    public void init() {      
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        cap = new VideoCapture(0); // 1st webcam
        if(cap.isOpened()){
        	webcamavailable = true;
        } 
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

    
    //keep refreshing image from camera to show video
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
    
    //prepare image and wait for user request
    private void convert(Mat m){            
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
        
        //flip image 
        image = flipVertical(image);
        image = image.getSubimage(60, 0, image.getWidth()-140, image.getHeight());
        
        //increase brightness and contrast on image
        RescaleOp rescaleOp = new RescaleOp(2f, 20, null);
        rescaleOp.filter(image, image);  // Source and destination are the same
        
        //if user requests to capture a cube face state
        if(capturepixelrgb == true){
        	//split image and run weka using created trainging set
        	BufferedImage imgs[] = splitImage(image,3 ,3 );
        	int count=0;
        	for(BufferedImage b : imgs){
            	File outputfile = new File(count + "image.jpg");
            	try {
					ImageIO.write(b, "jpg", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
            	count++;
        	}

        	PrepareArffFiles(imgs);
        	capturepixelrgb = false;
            try {
    			WekaMachineLearning w = new WekaMachineLearning();
    			colorarray = w.getColorarray();
    			setPreviewready(true);
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
        }
    }

    //reinitiate class variables 
    public void CapturePixels(Boolean training, Color trainingcolor){
    	this.training = training;
    	this.trainingcolor=trainingcolor;
    	capturepixelrgb = true;
    	
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 305, 305, null);          
        g.drawImage(template, 0, 0, 310, 300, null);     
    }   
    
    //flip image vertical
    private BufferedImage flipVertical(BufferedImage src){
        AffineTransform tx=AffineTransform.getScaleInstance(-1.0,1.0);  //scaling
        tx.translate(-src.getWidth(),0);  //translating
        AffineTransformOp tr=new AffineTransformOp(tx,null);  //transforming
        
        return tr.filter(src, null);  //filtering
       }
   
    //get image form file
    public BufferedImage GetBufferedImage(String src){
    BufferedImage img = null;
    try {
        img = ImageIO.read(new File(src));
    } catch (IOException e) {
    }
    return img;
    }

    //get an array of pixels for an image represented as RGB
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
    



    
    //split image in 9 quadrants (quadrant per cube square)
    private  BufferedImage[] splitImage(BufferedImage image, int rows, int cols) {
        int chunks = rows * cols;

        int chunkWidth = (image.getWidth()) / cols; // determines the chunk width and height
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
        return imgs;
    }
    
    
    //prepare arff files used by weka for machine learning
    private void PrepareArffFiles(BufferedImage imgs[]){
    	
        //writing images to .Arff file for either traning or test set
        for (int i = 0; i < imgs.length; i++) {
           // imgs[i] = cropImage(imgs[i]);
            ArrayList<SquareColor> colorarray = getPixelArrayFromImage(imgs[i]);
            
            if(training){
                SaveImageDataToArffFile("weka/colortrain.arff",  ColorToString(trainingcolor),  colorarray, true); //for buuilding traijnig set only
            }

            if(i == 0){
                SaveImageDataToArffFile("weka/colortest.arff", "unknown",  colorarray, false);
            }else{
            	SaveImageDataToArffFile("weka/colortest.arff", "unknown",  colorarray, true);
            }
        }
    }
    
    //crop image
    private BufferedImage cropImage(BufferedImage image){
    	BufferedImage croppedImage = image.getSubimage(70, 70, image.getWidth()-110, image.getHeight()-110);
		return croppedImage;	
    }
    
    public ArrayList<Color> GetColorArray(){
    	return colorarray;
    }
    
    //save prepares string to an .arff files based on criteria ( arg )
    private void SaveImageDataToArffFile(String filename, String color,  ArrayList<SquareColor> colorarray, Boolean append){
    	
    	//get statistics from pixel array
        Statistics s = new Statistics(colorarray);
        SquareColor avg = s.getMean();
        SquareColor varience = s.getVarience();
        
    	String header = "@relation color\n"+
    	"@attribute avgblue numeric\n@attribute avgred numeric\n@attribute avggreen numeric\n" + 
    	"@attribute varblue numeric\n@attribute varred numeric\n@attribute vargreen numeric\n" + 
    			"\n@attribute class {red,blue,green,orange,yellow,white,unknown}\n"+
    			"\n@data\n";
    	try{
    	    FileWriter fw = new FileWriter(filename, append); //the true will append the new data
    	    if(!append){
    	    	fw.write(header);
    	    }
    	    
    	    fw.write(avg.getBlue()+","+ avg.getRed()+"," +avg.getGreen()+",");//appends the string to the file
    	    fw.write(varience.getBlue()+","+ varience.getRed()+"," +varience.getGreen()+","+color+"\n");//appends the string to the file
    	    
    	    fw.close();
    	}
    	catch(IOException ioe){
    	    System.out.println("IOException: " + ioe.getMessage());
    	}
    }

	//convert color to a string that can be parsed //used for arff files
	public String ColorToString(Color color){
		
		if(color == Color.white){
			return "white";
		}else if(color == Color.red){
			return "red";
		}else if(color == Color.green){
			return "green";
		}else if(color == Color.yellow){
			return "yellow";
		}else if(color == Color.orange){
			return "orange";
		}else if(color == Color.blue){
			return "blue";
		}
		return null;
		
	}

	public Boolean getWebcamavailable() {
		return webcamavailable;
	}
	
    public Boolean getPreviewready() {
		return previewready;
	}

	public void setPreviewready(Boolean previewready) {
		this.previewready = previewready;
	}
}