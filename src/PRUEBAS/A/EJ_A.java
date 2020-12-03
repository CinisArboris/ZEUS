package PRUEBAS.A;

import javax.swing.JFrame;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author eyver-dev
 */
public class EJ_A {

    public EJ_A() {
    }
    
    public void ejemplo01(){
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        
        /**
         * Cargar mapa object.
         */
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        /**
         * Usar 8 segmentos para cargar.
         */
        tileFactory.setThreadPoolSize(8);

        /**
         * Cargar mapa centrado en: Santa Cruz de la Sierra - Bolivia.
         */
        GeoPosition santaCruz = new GeoPosition(-17.784867, -63.182515);

        mapViewer.setZoom(5);// Nivel zoom.
        mapViewer.setAddressLocation(santaCruz);

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("Santa Cruz de la Sierra");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
