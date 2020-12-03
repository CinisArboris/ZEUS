package PRUEBAS.C;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import org.jxmapviewer.painter.Painter;

/**
 *
 * @author eyver-dev
 */
public class SelectionPainter implements Painter<Object>{
    private final Color fillColor = new Color(128, 192, 255, 128);
    private final Color frameColor = new Color(0, 0, 255, 128);
    private SelectionAdapter adapter;

    public SelectionPainter(SelectionAdapter adapter) {
        this.adapter = adapter;
    }

    public SelectionAdapter getAdapter() {
        return adapter;
    }
    public void setAdapter(SelectionAdapter adapter) {
        this.adapter = adapter;
    }
    
    /**
     * 
     * @param g
     * @param t
     * @param width
     * @param height 
     */
    @Override
    public void paint(Graphics2D g,
            Object t,
            int width,
            int height
        ){
        Rectangle rc = adapter.getRectangle();
        if (rc != null){
            g.setColor(frameColor);
            g.draw(rc);
            g.setColor(fillColor);
            g.fill(rc);
        }
    }
}