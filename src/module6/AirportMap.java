package module6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> routeList;
	private CommonMarker lastSelected;
	private CommonMarker lastClicked;
	HashMap<Integer, AirportMarker> airportsMap = new HashMap<Integer, AirportMarker>();
	
	public void setup() {
		// setting up PAppler
		size(850,650, OPENGL);
		int zoomLevel = 3;
		
		// setting up map and default events
		map = new UnfoldingMap(this, 50, 50, 750, 550);
		MapUtils.createDefaultEventDispatcher(this, map);
		map.zoomAndPanTo(zoomLevel, new Location(48.8f, 9.1f));
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
			airportsMap.put(Integer.parseInt(feature.getId()), m);
		
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
			
			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
			sl.setHidden(true);
			sl.setColor(color(255,0,0));
			routeList.add(sl);
			if (airportsMap.containsKey(source) && airportsMap.containsKey(dest)) {
				airportsMap.get(source).setRoutes(sl);
				airportsMap.get(dest).setRoutes(sl);
			}			
		}
		//AirportMarker.routes = routeList;
		
		
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		map.addMarkers(routeList);
		
		map.addMarkers(airportList);
		
	}
	
	public void draw() {
		background(0);
		map.draw();
		
	}
	
	/** Event handler that gets called automatically when the 
	 * mouse moves.
	 */
	@Override
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		selectMarkerIfHover(airportList);
	}
	
	// If there is a marker selected 
	private void selectMarkerIfHover(List<Marker> markers)
	{
		// Abort if there's already a marker selected
		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) 
		{
			CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				lastSelected = marker;
				marker.setSelected(true);
				return;
			}
		}
	}
	
	/** The event handler for mouse clicks
	 * It will display an airport and all its routes
	 */
	@Override
	public void mouseClicked()
	{
		if (lastClicked != null) {
			unhideMarkers();
			lastClicked = null;
		}
		else
		{
			CommonMarker clicked = (CommonMarker) markerClicked(mouseX, mouseY);
			if (clicked != null) {
				lastClicked = clicked;
				checkAirportsForClick(clicked);
			}
		}
	}
	
	private void checkAirportsForClick(CommonMarker m) {
		hideMarkers();
		m.setHidden(false);
		List<SimpleLinesMarker> routes = ((AirportMarker) m).getRoutes();
		if (routes.size() == 0) {
			//System.out.println("no routes");
			return;
		}
		for (SimpleLinesMarker sl : routes) {
			//System.out.println("route found");
			sl.setHidden(false);
			int source = Integer.parseInt(sl.getStringProperty("source"));
			int destination = Integer.parseInt(sl.getStringProperty("destination"));
			airportsMap.get(source).setHidden(false);
			airportsMap.get(destination).setHidden(false);
		}
		/*
		for (Marker sl : routeList) {
			SimpleLinesMarker route = (SimpleLinesMarker) sl;
			List<Location> routeLocs = route.getLocations();
			for (Location loc : routeLocs) {
				if (loc.getLat() == m.getLocation().getLat() && loc.getLon() == m.getLocation().getLon()) {
					sl.setHidden(false);
					break;
				}
			}
		}
		*/
	}
	
	// loop over and unhide all airport markers
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
		for (Marker m :routeList) {
			m.setHidden(true);
		}
	}
	
	private void hideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(true);
		}
	}
	
	private Marker markerClicked(float mouseX, float mouseY) {
		for (Marker m : this.airportList) {
			if (m.isInside(map, mouseX, mouseY)) {
				return m;
			}
		}
		return null;
	}

}
