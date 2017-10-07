import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class hello {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*
		HttpClient clientadd = new DefaultHttpClient();
	  HttpPost post = new HttpPost("http://localhost:8087/updatephoto");
	  File file = new File("C:\\Users\\i074667\\Desktop\\ic_launcher.png");
      
      try {
    	  FileInputStream fis = new FileInputStream(file);
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          byte[] buf = new byte[1024];
          for (int readNum; (readNum = fis.read(buf)) != -1;) {
              //Writes to this byte array output stream
              bos.write(buf, 0, readNum); 
              System.out.println("read " + readNum + " bytes,");
          }
     

      byte[] bytes = bos.toByteArray();

      ByteArrayEntity entity2 = new ByteArrayEntity(bytes);
	  post.setEntity(entity2);
	
	  post.setHeader("number", "12345");
	 
	  HttpResponse response2 = clientadd.execute(post);
	 
	  System.out.println("hi");
      } catch (Exception ex) {
          ex.printStackTrace();
      }
      */
		try{
		HttpClient client4 = new DefaultHttpClient();
		  HttpGet request4 = new HttpGet("http://localhost:8087/getphoto");
		  request4.setHeader("number", "12345");
		  HttpResponse response4 = client4.execute(request4);
		
		  byte[] bytes =  EntityUtils.toByteArray(response4.getEntity());
		  
				  ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	        Iterator<?> readers = ImageIO.getImageReadersByFormatName("png");
        //ImageIO is a class containing static methods for locating ImageReaders
        //and ImageWriters, and performing simple encoding and decoding. 
 
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; 
        ImageInputStream iis = ImageIO.createImageInputStream(source); 
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
 
        Image image = reader.read(0, param);
        //got an image file
 
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        //bufferedImage is the RenderedImage to be written
 
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
 
        File imageFile = new File("C:\\Users\\i074667\\Desktop\\ic_launcher2.png");
        ImageIO.write(bufferedImage, "jpg", imageFile);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        
	}

}
