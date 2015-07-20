/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.gis;

import com.vividsolutions.jts.geom.Coordinate;
import java.beans.PropertyChangeListener;
import wts.GIS.styling.VectorStyle;

/**
 *
 * @author William.Stockhausen
 */
public interface MapDataInterfaceVectorBase extends MapDataInterfaceScalarBase, PropertyChangeListener {

    /**
     * Returns the vector arrow shape for plotting as a Coordinate vector.
     * The resulting arrow shape is scaled consistent with useFixedLength,
     * useFixedArrowheadSize, style.getMapLength(), style.getStandardScale(), style.getExponentScale(), style.getArrowScale() and style.getArrowheadAngle().
     *
     * @param sVal   - unscaled vector magnitude
     * @param aVal   - vector angle
     * @param dstPts - location of vector base
     * @param aa     - pi minus the arrowhead angle
     *
     * @return - Coordinate[5] object holding map coordinates of arrow vertices
     */
    public Coordinate[] createArrow(double sVal, double aVal, double[] dstPts, double aa);

    public double getStride();

    public void setStride(double s);

    @Override
    public VectorStyle getStyle();
    
}
