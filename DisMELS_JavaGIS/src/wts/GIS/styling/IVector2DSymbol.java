/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import org.geotools.filter.Expression;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleVisitor;
import org.geotools.styling.Symbol;

/**
 *
 * @author wstockhausen
 */
public interface IVector2DSymbol extends Symbol {

    /**
     * This paramterer defines the color used to render the vector.
     * @return The Expression used to define the color of the vector.
     **/
    Expression getColor();
    /**
     * This paramterer defines the color used to render the vector.
     * @param The Expression used to define the color of the vector.
     **/
    void setColor(Expression clr);

    /**
     * This paramterer defines the physical size (magnitude) of the vector.
     * @return The Expression used for the physical size of the vector.
     **/
    Expression getMagnitude();
    /**
     * This paramterer defines the physical size (magnitude) of the vector.
     * @param stroke The Expression used for the physical size of the vector.
     **/
    void setMagnitude(Expression mag);

    /**
     * This paramterer defines the rotation (radians CCW from horizontal) to use
     * when rendering the vector.
     * @return The Expression used for the rotation of the vector.
     **/
    Expression getRotation();
    /**
     * This paramterer defines the rotation (radians CCW from horizontal) to use
     * when rendering the vector.
     * @param The Expression used for the rotation of the vector.
     **/
    void setRotation(Expression rotation);

    /**
     * This paramterer defines the scale used to render the vector.
     * @return The Expression to use for the scale when rendering the vector.
     **/
    Expression getScale();
    /**
     * This paramterer defines the scale used to render the vector.
     * @param stroke The Expression for the scale when rendering the vector.
     **/
    void setScale(Expression scale);

    /**
     * This paramterer defines which stroke style should be used when
     * rendering the vector.
     * @return The Stroke definition to use when rendering the vector.
     **/
    Stroke getStroke();
    /**
     * This paramterer defines which stroke style should be used when
     * rendering the vector.
     * @param stroke The Stroke definition to use when rendering the vector.
     **/
    void setStroke(Stroke stroke);

    /**
     * This paramterer defines the width used to render the vector.
     * @return The Expression used to define the width of the vector.
     **/
    Expression getWidth();
    /**
     * This paramterer defines the width used to render the vector.
     * @param The Expression used to define the width of the vector.
     **/
    void setWidth(Expression clr);

    void accept(StyleVisitor visitor);
}
