package module3;
import processing.core.PApplet;
import processing.core.PImage;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.AbstractMapProvider;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.providers.GeoMapApp;

public class processingTest extends  PApplet {
	
	//PImage bgImage;
	UnfoldingMap map;
	
	public void setup() {
		size(500,500, P2D);
		//bgImage = loadImage("/Users/petra/Desktop/IMG_2298.jpg", "jpg");
		AbstractMapProvider provider = new GeoMapApp.TopologicalGeoMapProvider();
		map = new UnfoldingMap(this, 50, 50, 300, 300, provider);
	}
	
	public void draw() {
		map.draw();
		/*
		bgImage.resize(0, height);			//resize loaded image to full height of canvas
		image(bgImage, 0, 0);	
		int[] rgb = sunColorSec(second());
		fill(rgb[0], rgb[1], rgb[2]);
		ellipse(width/4, height/5, width/5, height/5);
		*/
	}
	
	public int[] sunColorSec(float sec) {
		int[] rgb = new int[3];
		float diffFrom30 = Math.abs(30 - sec);		
		float ratio = diffFrom30 / 30;
		rgb[0] = (int)(255 * ratio);
		rgb[1] = (int)(255 * ratio);
		rgb[2] = 0;
		return rgb;
	}

}
