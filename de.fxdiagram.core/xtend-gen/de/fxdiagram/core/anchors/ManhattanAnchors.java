package de.fxdiagram.core.anchors;

import javafx.geometry.Point2D;
import javafx.geometry.Side;

@SuppressWarnings("all")
public interface ManhattanAnchors {
  Point2D getManhattanAnchor(final double x, final double y, final Side side);
  
  Point2D getDefaultAnchor(final Side side);
  
  Point2D getDefaultSnapAnchor(final Side side);
}
