package com.github.tetrisanalyzer.gui;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Zoomer {
    private int zoomedOutCnt;
    private ZoomWindow windowOverview = new ZoomWindow();

    private Iterator<ZoomWindow> windowsZoomOut;
    private Iterator<ZoomWindow> windowsZoomIn;

    public static Zoomer zoomOut(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        return new Zoomer(Collections.<ZoomWindow>emptyList(), WindowZoomer.zoomOut(to, from, zoomSpeed), 0);
    }

    public static Zoomer zoomIn(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        return new Zoomer(WindowZoomer.zoomIn(from, to, zoomSpeed), Collections.<ZoomWindow>emptyList(), 0);
    }

    public static Zoomer zoomOutAndIn(ZoomWindow from, ZoomWindow to, double zoomSpeed) {
        ZoomWindow overview = new ZoomWindow();
        List<ZoomWindow> zoomOut = WindowZoomer.zoomOut(overview, from, zoomSpeed);
        List<ZoomWindow> zoomIn = WindowZoomer.zoomIn(overview, to, zoomSpeed);

        return new Zoomer(zoomIn, zoomOut, zoomOut.size() / 3);
    }

    private Zoomer(List<ZoomWindow> zoomIn, List<ZoomWindow> zoomOut, int zoomOutCnt) {
        windowsZoomIn = zoomIn.iterator();
        windowsZoomOut = zoomOut.iterator();
        this.zoomedOutCnt = zoomOutCnt;
    }

    public boolean isZooming() {
        return windowsZoomOut.hasNext() || windowsZoomIn.hasNext();
    }

    public ZoomWindow zoom() {
        if (windowsZoomOut.hasNext()) {
            return windowsZoomOut.next();
        }
        if (zoomedOutCnt > 0) {
            zoomedOutCnt--;
        } else if (windowsZoomIn.hasNext()) {
            return windowsZoomIn.next();
        }
        return windowOverview;
    }
}
