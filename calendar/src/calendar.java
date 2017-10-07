import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;


public class calendar {

	/**
	 * @param args
	 * @throws ParserException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ParserException {
		// TODO Auto-generated method stub
		
		 
		  
		  //Creating a new calendar
		  net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();
		  
		
		  URL website = new URL("https://calendar.google.com/calendar/ical/sanansania%40gmail.com/private-f89614bc7d773eeb371ee76f198cbeb1/basic.ics");
		  ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		  FileOutputStream fos = new FileOutputStream("C:\\Users\\i074667\\Desktop\\cal.ics");
		  fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		  
		FileInputStream fin = new FileInputStream("C:\\Users\\i074667\\Desktop\\cal.ics");

		  CalendarBuilder builder = new CalendarBuilder();

		  calendar = builder.build(fin);
		  
		  //Iterating over a Calendar
		  for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
		      Component component = (Component) i.next();
		      //System.out.println("Component [" + component.getName() + "]");
		      System.out.println("");

		      for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
		          Property property = (Property) j.next();
		          if((property.getName().compareToIgnoreCase("DTSTART")==0)||((property.getName().compareToIgnoreCase("DTEND")==0)||(property.getName().compareToIgnoreCase("SUMMARY")==0)))
		          System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
		      }
		  }//for

	}

}
