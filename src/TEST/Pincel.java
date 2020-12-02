package TEST;

// Estructuras de datos.
import MODEL.FPincel;
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
    
    public void e_01() {
        JXMapViewer mapViewer = new JXMapViewer();
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Display the viewer in a JFrame
        JFrame frame = new JFrame("JXMapviewer2 Example 1");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

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

        GeoPosition frankfurt = new GeoPosition(50,  7, 0, 8, 41, 0);
        GeoPosition wiesbaden = new GeoPosition(50,  5, 0, 8, 14, 0);
        GeoPosition mainz     = new GeoPosition(50,  0, 0, 8, 16, 0);
        GeoPosition darmstadt = new GeoPosition(49, 52, 0, 8, 39, 0);
        GeoPosition offenbach = new GeoPosition(50,  6, 0, 8, 46, 0);

        // Create a track from the geo-positions
        List<GeoPosition> track = Arrays.asList(frankfurt, wiesbaden, mainz, darmstadt, offenbach);
        FPincel fpincel = new FPincel(track);

        // Set the focus
        mapViewer.zoomToBestFit(new HashSet<>(track), 0.7);

        // Create waypoints from the geo-positions
        Set<Waypoint> waypoints = new HashSet<>(Arrays.asList(
                new DefaultWaypoint(frankfurt),
                new DefaultWaypoint(wiesbaden),
                new DefaultWaypoint(mainz),
                new DefaultWaypoint(darmstadt),
                new DefaultWaypoint(offenbach)));

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

    protected static void updateWindowTitle(
            JFrame frame,
            JXMapViewer mapViewer){
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
    
    public void e_03() {
        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);

        // Setup local file cache
        File cacheDir = new File(System.getProperty("user.home") + File.separator + ".jxmapviewer2");
        tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

        // Setup JXMapViewer
        final JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);

        GeoPosition frankfurt = new GeoPosition(50.11, 8.68);

        // Set the focus
        mapViewer.setZoom(7);
        mapViewer.setAddressLocation(frankfurt);

        // Add interactions
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);

        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add a selection painter
        SelectionAdapter sa;
        sa = new SelectionAdapter(mapViewer);
        SelectionPainter sp = new SelectionPainter(sa);
        mapViewer.addMouseListener(sa);
        mapViewer.addMouseMotionListener(sa);
        mapViewer.setOverlayPainter(sp);

        // Display the viewer in a JFrame
        final JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        String text = "Use left mouse button to pan, mouse wheel to zoom and right mouse to select";
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
