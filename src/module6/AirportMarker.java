package module6;

import java.util.ArrayList;

import de.fhpotsdam.unfolding.marker.Marker;
import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	private List<SimpleLinesMarker> routes = new ArrayList<SimpleLinesMarker>();
	//public static List<Marker> routes;
	private final int RADIUS = 5;
	
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		pg.fill(11);
		pg.ellipse(x, y, 5, 5);
		
		
	}

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		 // show rectangle with title
		String name = "Code: " + getCode() + " ";
		String loc =  getCity() + ", " + getCountry() + " ";
		pg.pushStyle();	
		
		pg.fill(255, 255, 255);
		pg.textSize(12);
		pg.rectMode(PConstants.CORNER);
		pg.rect(x, y-RADIUS-39, Math.max(pg.textWidth(name), pg.textWidth(loc)) + 6, 39);
		pg.fill(0, 0, 0);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.text(name, x+3, y-RADIUS-33);
		pg.text(loc, x+3, y - RADIUS -18);
		
		pg.popStyle();
		
		//System.out.println(this.routes);
		// show routes
		
		
	}
	
	public void setRoutes(SimpleLinesMarker sl) {
		if (!this.routes.contains(sl)) {
			this.routes.add(sl);
		}		
	}
	
	public List<SimpleLinesMarker> getRoutes() {
		return this.routes;
	}
	
	private String getCity()
	{
		String s = getStringProperty("city");
		s = s.substring(1, s.length()-1);		
		return s;
	}
	
	private String getCountry()
	{
		String s = getStringProperty("country");
		s = s.substring(1, s.length()-1);		
		return s;
	}
	
	private String getCode()
	{
		String s = getStringProperty("code");
		s = s.substring(1, s.length()-1);		
		return s;
	}
}
