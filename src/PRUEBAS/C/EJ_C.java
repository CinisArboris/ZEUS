package PRUEBAS.C;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.io.File;

// jxmapviewer
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;

/**
 *
 * @author eyver-dev
 */
public class EJ_C {

    public EJ_C() {
    }
    
    public void ejemplo01(){
        /**
         * Encapsula toda la información específica de un servidor
         * de mapas.
         * Esto incluye todo, desde la URL hasta Cargue los
         * mosaicos del mapa desde el tamaño y la profundidad
         * de los mosaicos.
         * Teóricamente, cualquier servidor de mapas se puede
         * utilizar instalando un TileFactoryInfo personalizado
         */
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        /**
         * Cargar mapa centrado en: Santa Cruz de la Sierra - Bolivia.
         */
        GeoPosition santaCruz = new GeoPosition(-17.784867, -63.182515);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(santaCruz);

        // Add interactions
        MouseInputListener mia;
        mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        PanKeyListener queso = new PanKeyListener(mapViewer);
        mapViewer.addKeyListener(queso);

        // Add a selection painter
        SelectionAdapter sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);

        // Display the viewer in a JFrame
        final JFrame frame = new JFrame();
        
        BorderLayout tmp = new BorderLayout();
        frame.setLayout(tmp);
        
        String text = "[Clic izquierdo sostenido para mover]"
                + "     "
                + "[Rueda del mouse, zoom IN / zoom OUT]";
        
        frame.add(new JLabel(text), BorderLayout.NORTH);
        frame.add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mapViewer.addPropertyChangeListener("zoom", (PropertyChangeEvent evt) -> {
            updateWindowTitle(frame, mapViewer);
        });

        mapViewer.addPropertyChangeListener("center", (PropertyChangeEvent evt) -> {
            updateWindowTitle(frame, mapViewer);
        });
        
        updateWindowTitle(frame, mapViewer);
    }
    
    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer){
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();

        frame.setTitle(String.format(
                "Santa Cruz de la Sierra (%.2f / %.2f) - Zoom: %d",
                lat,
                lon,
                zoom)
            );
    }
}
