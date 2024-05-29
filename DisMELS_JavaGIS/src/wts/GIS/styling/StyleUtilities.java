/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import java.awt.Color;
import org.geotools.feature.FeatureType;
import org.geotools.filter.BetweenFilter;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;

/**
 *
 * @author wstockhausen
 */
public class StyleUtilities {

    /**
     * Creates a bin filter
     * @param ff
     * @param min
     * @param max
     * @return
     * @throws org.geotools.filter.IllegalFilterException
     */
    public static BetweenFilter createBinFilter(FilterFactory ff, FeatureType ft, String attName, double min, double max)
            throws IllegalFilterException {
        Expression expAtt = ff.createAttributeExpression(ft, attName);
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
    public static Expression createColorExpression(FilterFactory ff, Color color) {
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

}
