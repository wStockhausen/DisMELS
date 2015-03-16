/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import com.wtstockhausen.utils.ColorRamp;
import java.awt.Color;
import java.text.NumberFormat;
import org.geotools.feature.FeatureType;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;

/**
 *
 * @author wstockhausen
 */
public class StyleCreator {
    
    NumberFormat frmt = NumberFormat.getNumberInstance();
    
    FilterFactory ff = FilterFactory.createFilterFactory();
    StyleFactory sf = StyleFactory.createStyleFactory();
    
    /**
     * 
     * @param minDigits
     * @param maxDigits
     */
    public void setNumberFormat(int minDigits, int maxDigits){
        frmt.setMinimumFractionDigits(minDigits);
        frmt.setMaximumFractionDigits(maxDigits);
    }

    /**
     * 
     * @param ff
     * @param min
     * @param max
     * @return
     * @throws org.geotools.filter.IllegalFilterException
     */
    public BetweenFilter createBinFilter(FeatureType ft,double min, double max)
            throws IllegalFilterException {
        Expression expAtt = ff.createAttributeExpression(ft, "Value");
        Expression expBot = ff.createLiteralExpression(min);
        Expression expTop = ff.createLiteralExpression(max);
        BetweenFilter filter = ff.createBetweenFilter();
        filter.addLeftValue(expBot);
        filter.addMiddleValue(expAtt);
        filter.addRightValue(expTop);
        return filter;
    }

    /**
     * convert an awt color in to a literal expression representing the color
     * @param color the color to encode
     * @return the expression
     */
    public Expression createColorExpression(FilterFactory ff, Color color) {
        if (color == null) return null;

        String redCode = Integer.toHexString(color.getRed());
        String grnCode = Integer.toHexString(color.getGreen());
        String bluCode = Integer.toHexString(color.getBlue());

        if (redCode.length() == 1) redCode = "0" + redCode;
        if (grnCode.length() == 1) grnCode = "0" + grnCode;
        if (bluCode.length() == 1) bluCode = "0" + bluCode;

        String colorCode = "#" + redCode + grnCode + bluCode;

        return ff.createLiteralExpression(colorCode.toUpperCase());
    }

    /**
     * Returns a
     * @param ft
     * @param geomPropName
     * @param name
     * @param clrRamp
     * @param min
     * @param max
     * @param nbins
     * @return
     * @throws org.geotools.filter.IllegalFilterException
     */
    public ColorBarStyle createColorBarStyle(FeatureType ft, String geomPropName, String attName,
                                             String clrRamp, double min, double max, int nbins)
                                             throws IllegalFilterException {
        ColorBarStyle style = new ColorBarStyle(sf,ff,ft,geomPropName,attName);
        style.setMax(max);
        style.setMin(min);
        style.setColorRamp(clrRamp);
        style.setNumberOfColors(nbins);
        style.updateStyle();
        return style;
    }

}
