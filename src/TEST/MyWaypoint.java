package TEST;

import java.awt.Color;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

/**
 *
 * @author eyver-dev
 */
class MyWaypoint extends DefaultWaypoint{
    private final String label;
    private final Color color;

    /**
     * @param label the text
     * @param color the color
     * @param coord the coordinate
     */
    public MyWaypoint(String label, Color color, GeoPosition coord){
        super(coord);
        this.label = label;
        this.color = color;
    }

    /**
     * @return the label text
     */
    public String getLabel(){
        return label;
    }

    /**
     * Obtener el color del pin.
     * @return 
     */
    public Color getColor(){
        return color;
    }
    
}
