package TEST;

// Estructuras de datos.
import MODEL.FPincel;
import MODEL.SelectionAdapter;
import MODEL.SelectionPainter;
import java.awt.BorderLayout;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

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
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 *
 * @author eyver-dev
 */
public class Pincel {

    public Pincel() {
    }
    
    /**
     * Mapa Estatico.
     */
    public void e_01() {
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

    /**
     * Mapa Estatico con rutas.
     */
    public void e_02() {
        JXMapViewer mapViewer = new JXMapViewer();

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 2");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        /**
         * Cargar mapa centrado en: Santa Cruz de la Sierra - Bolivia.
         * Rodeando la Plaza 24 de Septiembre.
         */
        GeoPosition A = new GeoPosition(-17.784867, -63.182515);
        GeoPosition B = new GeoPosition(-17.784809, -63.181495);
        GeoPosition C = new GeoPosition(-17.782744, -63.181656);
        GeoPosition D = new GeoPosition(-17.782818, -63.182657);
        
        
        // Create a track from the geo-positions
        List<GeoPosition> track = Arrays.asList(
                A, B, C, D
            );
        FPincel fpincel = new FPincel(track);

        // Set the focus
        mapViewer.zoomToBestFit(new HashSet<>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<>(
                Arrays.asList(
                    new DefaultWaypoint(A),
                    new DefaultWaypoint(B),
                    new DefaultWaypoint(C),
                    new DefaultWaypoint(D)
                )
            );

        // Create a waypoint painter that takes all the waypoints
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<>();
        painters.add(fpincel);
        painters.add(waypointPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    protected static void updateWindowTitle(JFrame frame, JXMapViewer mapViewer){
        double lat = mapViewer.getCenterPosition().getLatitude();
        double lon = mapViewer.getCenterPosition().getLongitude();
        int zoom = mapViewer.getZoom();

        frame.setTitle(String.format(
                "JXMapviewer2 Example 3 (%.2f / %.2f) - Zoom: %d",
                lat,
                lon,
                zoom)
            );
    }
    
    /**
     * Mapa dinamico ante los eventos del [MOUSE].
     * Clic izquierdo, mover el mapa
     * Rueda del mouse :: Zoom IN / Zoom OUT
     */
    public void e_03() {
        
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

        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

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

        mapViewer.addPropertyChangeListener("center", new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent evt){
                updateWindowTitle(frame, mapViewer);
            }
        });
        updateWindowTitle(frame, mapViewer);
    }
}
