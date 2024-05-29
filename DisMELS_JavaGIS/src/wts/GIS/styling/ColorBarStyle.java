/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import com.wtstockhausen.utils.ColorRamp;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.feature.FeatureType;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleVisitor;
import org.geotools.styling.Symbolizer;

/**
 *
 * @author wstockhausen
 */
public class ColorBarStyle implements Style, Cloneable, Serializable {

    /** property thrown when updateStyle() is called */
    public static String PROP_STYLE   = "wts.GIS.styling.ColorBarStyle:Style";
    /** property thrown when an element of the style changes and updateStyle SHOULD be called */
    public static String PROP_ELEMENT = "wts.GIS.styling.ColorBarStyle:Element";

    /** initially an instance of a vanilla default style */
    private Style style;
    private StyleFactory sf;
    private FilterFactory ff;

    /** flag indicating whether the ColorBarStyle must be updated */
    private boolean mustUpdateStyle = true;
    private boolean showLTMin = true;
    private boolean showGTMax = true;
    private double min     = Double.POSITIVE_INFINITY;
    private double max     = Double.NEGATIVE_INFINITY;
    private int numClrs    = 20;
    private String clrRamp = "jet";

    private FeatureType ft = null;
    private String geomPropName = "Geometry";
    private String attName = "Value";

    transient private PropertyChangeSupport propertySupport;

    public ColorBarStyle(StyleFactory sf, FilterFactory ff, FeatureType ft, String geomPropName, String attName) {
        if (sf==null) {
            sf = StyleFactory.createStyleFactory();
        } else {
            this.sf = sf;
        }
        if (ff==null) {
            ff = FilterFactory.createFilterFactory();
        } else {
            this.ff = ff;
        }
        this.ft = ft;
        this.geomPropName = geomPropName;
        this.attName = attName;
        init();
    }

    public ColorBarStyle(FeatureType ft, String geomPropName, String attName) {
        sf = StyleFactory.createStyleFactory();
        ff = FilterFactory.createFilterFactory();
        this.ft = ft;
        this.geomPropName = geomPropName;
        this.attName = attName;
        init();
    }

    public ColorBarStyle(FeatureType ft) {
        sf = StyleFactory.createStyleFactory();
        ff = FilterFactory.createFilterFactory();
        this.ft = ft;
        init();
    }
    
    public ColorBarStyle(){
        sf = StyleFactory.createStyleFactory();
        ff = FilterFactory.createFilterFactory();
        init();
    }

    private void init() {
        style = sf.getDefaultStyle();
        propertySupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public Object clone() {
        ColorBarStyle clone = null;
        try {
            clone = (ColorBarStyle) super.clone();
            clone.sf = sf;
            clone.ff = ff;
            clone.ft = ft;
            clone.geomPropName = geomPropName;
            clone.attName = attName;
            clone.clrRamp = clrRamp;
            clone.min = min;
            clone.max = max;
            clone.numClrs = numClrs;
            clone.mustUpdateStyle = true;
            //TODO: should property listeners be copied over or not?
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ColorBarStyle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clone;
    }

    /**
     * Actually create/update the Style. This method should be called if
     * mustUpdateStyle() is true, because style parameters have been changed
     * since the last Style was created.
     * 
     * @throws IllegalFilterException 
     */
    public void updateStyle() throws IllegalFilterException {
        if (mustUpdateStyle) {
            Style oldStyle = style;
            style = sf.getDefaultStyle();
            FeatureTypeStyle fts = sf.createFeatureTypeStyle();
            fts.setTitle(style.getName());
            fts.setName(style.getName());
//            fts.setFeatureTypeName(style.getName());//TODO: do I want to do this?

            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);

            Rule rule; PolygonSymbolizer sym;
            Color rgb; Fill fill;
            String str;

            double levBot, levTop;
            double levStp = (max-min)/(numClrs-1);
            if (showLTMin) {
                str = "< "+frmt.format(min);
                //rgb = getColorFromRamp((float)min,(float)max,(float)levBot);
                rgb = ColorRamp.createColor(0, clrRamp);
                fill = sf.createFill(StyleUtilities.createColorExpression(ff,rgb));
                sym = sf.createPolygonSymbolizer(null,fill,geomPropName);//stroke, fill, geometry property name
                rule = sf.createRule();
                rule.setFilter(StyleUtilities.createBinFilter(ff,ft,attName,Double.NEGATIVE_INFINITY,min));
                rule.setName(str);
                rule.setTitle(str);
                rule.setSymbolizers(new Symbolizer[]{sym});
                fts.addRule(rule);
            }
            for (int l=1;l<=numClrs;l++) {
                levBot = min+levStp*(l-1);
                levTop = levBot+levStp;
                str = frmt.format(levBot)+":"+frmt.format(levTop);
                //rgb = getColorFromRamp((float)min,(float)max,(float)levBot);
                rgb = ColorRamp.createColor((levBot-min)/(max-min), clrRamp);
                fill = sf.createFill(StyleUtilities.createColorExpression(ff,rgb));
                sym = sf.createPolygonSymbolizer(null,fill,geomPropName);//stroke, fill, geometry property name
                rule = sf.createRule();
                rule.setFilter(StyleUtilities.createBinFilter(ff,ft,attName,levBot,levTop));
                rule.setName(str);
                rule.setTitle(str);
                rule.setSymbolizers(new Symbolizer[]{sym});
                fts.addRule(rule);
            }
            if (showGTMax) {
                str = "> "+frmt.format(max);
                rgb = ColorRamp.createColor(1, clrRamp);
                fill = sf.createFill(StyleUtilities.createColorExpression(ff,rgb));
                sym = sf.createPolygonSymbolizer(null,fill,geomPropName);//stroke, fill, geometry property name
                rule = sf.createRule();
                rule.setFilter(StyleUtilities.createBinFilter(ff,ft,attName,max,Double.POSITIVE_INFINITY));
                rule.setName(str);
                rule.setTitle(str);
                rule.setSymbolizers(new Symbolizer[]{sym});
                fts.addRule(rule);
            }

            style.addFeatureTypeStyle(fts);
            mustUpdateStyle = false;
            propertySupport.firePropertyChange(PROP_STYLE, oldStyle, style);
        }
    }

    /**
     * Returns whether or not the method updateStyle() should be called on the
     * object (e.g., user changed parameter values since last update).
     * 
     * @return true or false
     */
    public boolean mustUpdateStyle() {
        return mustUpdateStyle;
    }

    public String getColorRamp() {
        return clrRamp;
    }

    public void setColorRamp(String rampName){
        if (!clrRamp.equalsIgnoreCase(rampName)){
            clrRamp = rampName;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double val){
        if (min!=val){
            min = val;
            mustUpdateStyle = true;
        }
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
    }

    public double getMax() {
        return max;
    }

    public void setMax(double val){
        if (max!=val){
            max = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
        }
    }

    public int getNumberOfColors() {
        return numClrs;
    }

    public void setNumberOfColors(int val){
        if (numClrs!=val){
            numClrs = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
        }
    }

    public boolean getShowLTMin() {
        return showLTMin;
    }

    public void setShowLTMin(boolean b) {
        if (showLTMin!=b){
            showLTMin = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
        }
    }

    public boolean getShowGTMax() {
        return showGTMax;
    }

    public void setShowGTMax(boolean b) {
        if (showGTMax!=b){
            showGTMax = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT,null,null);
        }
    }

    //The following are all methods required by the org.geotools.styling.Style interface
    @Override
    public String getName() {
        return style.getName();
    }

    @Override
    public void setName(String arg0) {
        style.setName(arg0);
        FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
        for (int i=0;i<fts.length;i++) {
            fts[i].setTitle(style.getName());
            fts[i].setName(style.getName());
            fts[i].setFeatureTypeName(style.getName());//TODO: do I want to do this?
        }
    }

    @Override
    public String getTitle() {
        return style.getTitle();
    }

    @Override
    public void setTitle(String arg0) {
        style.setTitle(arg0);
    }

    @Override
    public String getAbstract() {
        return style.getAbstract();
    }

    @Override
    public void setAbstract(String arg0) {
        style.setAbstract(arg0);
    }

    @Override
    public boolean isDefault() {
        return style.isDefault();
    }

    @Override
    public void setDefault(boolean arg0) {
        style.setDefault(arg0);
    }

    @Override
    public FeatureTypeStyle[] getFeatureTypeStyles() {
        return style.getFeatureTypeStyles();
    }

    @Override
    public void setFeatureTypeStyles(FeatureTypeStyle[] arg0) {
        //do nothing
    }

    @Override
    public void addFeatureTypeStyle(FeatureTypeStyle arg0) {
        //do nothing
    }

    @Override
    public void accept(StyleVisitor arg0) {
        style.accept(arg0);
    }

}
