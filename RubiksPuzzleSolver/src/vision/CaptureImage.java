package vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class CaptureImage extends JPanel implements Runnable {
    private VideoCapture cap;    // capture
    private  byte [] b;           // pixel bytes
    private BufferedImage image; // our drawing canvas
    private Thread idx_Thread;

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
                Thread.sleep(50);
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 400, 300, null);          
    }   
    public BufferedImage flipVertical(BufferedImage src){
        AffineTransform tx=AffineTransform.getScaleInstance(-1.0,1.0);  //scaling
        tx.translate(-src.getWidth(),0);  //translating
        AffineTransformOp tr=new AffineTransformOp(tx,null);  //transforming
        
        return tr.filter(src, null);  //filtering
       }
 
}