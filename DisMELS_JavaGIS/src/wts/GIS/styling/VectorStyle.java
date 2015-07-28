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
import java.util.logging.Logger;
import org.geotools.feature.FeatureType;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.Expression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.filter.MathExpression;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleVisitor;
import org.geotools.styling.Symbolizer;

/**
 * Instances of this class provide an implementation of a Geotools 2.0 Style
 * intended for use with a vector field.
 * 
 * @author William Stockhausen
 */
public class VectorStyle implements Style, Cloneable, Serializable {

    /** property thrown when updateStyle() is called */
    public static String PROP_STYLE   = "wts.GIS.styling.VectorStyle:Style";
    /** property thrown when an element of the style changes and updateStyle SHOULD be called */
    public static String PROP_ELEMENT = "wts.GIS.styling.VectorStyle:Element";

    /** Initially an instance of a vanilla default style */
    private Style style;
    private Stroke stroke;
    private StyleFactory sf;
    private FilterFactory ff;

    /** flag indicating whether the associated Geotools style needs to be updated */
    private boolean mustUpdateStyle = true;
    /** flag indicating whether values less than the min will be shown */
    private boolean showLTMin = true;
    /** flag indicating whether values greater than the max will be shown */
    private boolean showGTMax = true;
    
    /** exponential scale factor for vector lengths (default is 1) */
    private double expScale = 1.0;
    /** standard linear scale for vectors (default is 1) */
    private double stdScale = 1.0;
    /** length of vector (in [map units]/1000 (i.e., nominally km)) at standard scale.
     * Default is 100 (km).
     */
    private double mapLength = 100.0;
    /** flag to use fixed length (map length) for all vectors (default is false) */
    private boolean useFixedLength = false;
    
    /** vector width at standard scale or fixed width (default is 2)*/
    private Double stdWidth = 2.0;
    /** flag to use fixed widths (default is false) */
    private boolean useFixedWidth = false;
    
    /** vector color when using fixed colors (default is Black)*/
    private Color fixedColor = Color.BLACK;
    /** color bar style */
    private ColorBarStyle cbStyle = null;
    /** flag to use fixed colors (default is true) */
    private boolean useFixedColor = true;
    
    /** arrowhead scale (0-1) (default is 0.1) */
    private double arrowScale = 0.1;
    /** arrowhead angle (in deg) (default is 20)*/
    private double arrowAngle = 20.0;
    /** flag to use fixed arrowhead size (default is false) */
    private boolean useFixedArrowheadSize = false;

    /** sampling resolution (in grid units) for vector field (default is 5.0) */
    private double stride = 5.0;
    
    private FeatureType ft = null;
    private String geomPropName = "Geometry";
    private String magName = "Speed";
    private String angName = "Angle";

    transient private PropertyChangeSupport propertySupport;
    
    private static final Logger logger = Logger.getLogger(VectorStyle.class.getName());

    public VectorStyle(StyleFactory sf, FilterFactory ff, FeatureType ft, String geomPropName, String magName, String angName) {
        if (sf==null) {
            this.sf = StyleFactory.createStyleFactory();
        } else {
            this.sf = sf;
        }
        if (ff==null) {
            this.ff = FilterFactory.createFilterFactory();
        } else {
            this.ff = ff;
        }
        this.ft = ft;
        this.geomPropName = geomPropName;
        this.magName = magName;
        this.angName = angName;
        init();
    }

    public VectorStyle(FeatureType ft, String geomPropName, String magName, String angName) {
        sf = StyleFactory.createStyleFactory();
        ff = FilterFactory.createFilterFactory();
        this.ft = ft;
        this.geomPropName = geomPropName;
        this.magName = magName;
        this.angName = angName;
        init();
    }

    public VectorStyle(FeatureType ft) {
        sf = StyleFactory.createStyleFactory();
        ff = FilterFactory.createFilterFactory();
        this.ft = ft;
        init();
    }

    private void init() {
        style = sf.getDefaultStyle();
        stroke = sf.getDefaultStroke();
        this.cbStyle = new ColorBarStyle();
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
        VectorStyle clone = null;
        try {
            clone = (VectorStyle) super.clone();
            clone.sf = sf;
            clone.ff = ff;
            clone.ft = ft;
            clone.geomPropName = geomPropName;
            clone.magName = magName;
            clone.angName = angName;
//            clone.clrRamp = clrRamp;
//            clone.numClrs = numClrs;
            clone.cbStyle = (ColorBarStyle) cbStyle.clone();//TODO: check this: might have already been done by super.clone()
            clone.mustUpdateStyle = false;
            //TODO: should property listeners be copied over or not?
        } catch (CloneNotSupportedException ex) {
            logger.severe(ex.getMessage());
        }
        return clone;
    }


    private Rule createColorRule(double levBot, double levTop, Expression expWidth, String str) throws IllegalFilterException {
        stroke = sf.getDefaultStroke();
        stroke.setWidth(expWidth);
        double clrScale = (levBot-cbStyle.getMin())/(cbStyle.getMax()-cbStyle.getMin());
        if (levBot==Double.NEGATIVE_INFINITY) {
            clrScale = 0;
        } else if (levTop==Double.POSITIVE_INFINITY) {
            clrScale = 1;
        }
        Color rgb = ColorRamp.createColor(clrScale, cbStyle.getColorRamp());//create fixedColor based on scale
        stroke.setColor(StyleUtilities.createColorExpression(ff,rgb));
        Symbolizer sym = sf.createLineSymbolizer(stroke, geomPropName);
        Rule rule = sf.createRule();
        rule.setFilter(StyleUtilities.createBinFilter(ff,ft,magName,levBot,levTop));
        rule.setName(str);
        rule.setTitle(str);
        rule.setSymbolizers(new Symbolizer[]{sym});
        return rule;
    }

    /**
     * Actually create/update the Style. This method should be called if
     * mustUpdateStyle() is true, because style parameters have been changed
     * since the last Style was created.
     * 
     * @throws IllegalFilterException 
     */
    public void updateStyle() throws IllegalFilterException {
        if (mustUpdateStyle||(!useFixedColor&&cbStyle.mustUpdateStyle())) {
            Style oldStyle = style;
            style = sf.getDefaultStyle();
            FeatureTypeStyle fts = sf.createFeatureTypeStyle();
            fts.setTitle(style.getName());
            fts.setName(style.getName());
//            fts.setFeatureTypeName(style.getName());//TODO: if I do this, I think it should be the FeatureType name

            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);

            Rule rule; LineSymbolizer sym;
            String str;

            Expression expWidth = ff.createLiteralExpression(stdWidth);
            if (useFixedWidth) {
                //width = fixedWidth
                //using expWidth defined above
                logger.info("Using fixed width for stroke");
            } else {
                //width = fixedWidth*(magValue/fixedWidth)^0.25
                logger.info("Using variable width for stroke");
                AttributeExpression ae = ff.createAttributeExpression(ft, magName);
                MathExpression me1     = ff.createMathExpression(MathExpression.MATH_DIVIDE);
                PowerFunction pwr      = new PowerFunction();
                MathExpression me2     = ff.createMathExpression(MathExpression.MATH_MULTIPLY);
                me1.addLeftValue(ae);       me1.addRightValue(expWidth);
                pwr.setA(me1);              pwr.setB(ff.createLiteralExpression(0.25));
                me2.addLeftValue(expWidth); me2.addRightValue(pwr);
                expWidth = me2;
            }
            if (useFixedColor) {
                logger.info("Using fixed color");
                str = magName;
                stroke.setWidth(expWidth);
                stroke.setColor(StyleUtilities.createColorExpression(ff,fixedColor));
                sym = sf.createLineSymbolizer(stroke, geomPropName);
                rule = sf.createRule();
                if (cbStyle.getShowGTMax()&&cbStyle.getShowLTMin()){
                    rule.setFilter(null);
                } else {
                    //Note that this cuts off values < min and > max even though one of
                    //cbStyle.getShowLTMin() or cbStyle.getShowGTMax() may be true
                    rule.setFilter(StyleUtilities.createBinFilter(ff,ft,magName,cbStyle.getMin(),cbStyle.getMax()));
                }
                rule.setName(str);
                rule.setTitle(str);
                rule.setSymbolizers(new Symbolizer[]{sym});
                fts.addRule(rule);
            } else {
                logger.info("Using variable color");
                double levBot, levTop;
                double max = cbStyle.getMax();
                double min = cbStyle.getMin();
                double levStp = (max-min)/(cbStyle.getNumberOfColors()-1);
                if (cbStyle.getShowLTMin()) {
                    str = "< "+frmt.format(min);
                    fts.addRule(createColorRule(Double.NEGATIVE_INFINITY,min,expWidth,str));
                }
                for (int l=1;l<=cbStyle.getNumberOfColors();l++) {
                    levBot = min+levStp*(l-1);
                    levTop = levBot+levStp;
                    str = frmt.format(levBot)+":"+frmt.format(levTop);
                    fts.addRule(createColorRule(levBot,levTop,expWidth,str));
                }
                if (cbStyle.getShowGTMax()) {
                    str = "> "+frmt.format(max);
                    fts.addRule(createColorRule(max,Double.POSITIVE_INFINITY,expWidth,str));
                }
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
        return (mustUpdateStyle||(!useFixedColor&&cbStyle.mustUpdateStyle()));
    }

    public double getArrowheadAngle() {
        return arrowAngle;
    }

    public void setArrowAngle(Double val){
        if (val!=arrowAngle){
            arrowAngle = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getArrowScale() {
        return arrowScale;
    }

    public void setArrowScale(Double val){
        if (val!=arrowScale){
            arrowScale = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public ColorBarStyle getColorBarStyle() {
        return cbStyle;
    }

    public String getColorRamp() {
        return cbStyle.getColorRamp();
    }

    public void setColorRamp(String rampName){
        if (!cbStyle.getColorRamp().equalsIgnoreCase(rampName)){
            cbStyle.setColorRamp(rampName);
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public Color getFixedColor() {
        return fixedColor;
    }

    public void setFixedColor(Color clr) {
        if (!fixedColor.equals(clr)) {
            fixedColor = clr;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getMin() {
        return cbStyle.getMin();
    }

    public void setMin(double val){
        if (cbStyle.getMin()!=val){
            cbStyle.setMin(val);
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getMax() {
        return cbStyle.getMax();
    }

    public void setMax(double val){
        if (cbStyle.getMax()!=val){
            cbStyle.setMax(val);
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public int getNumberOfColors() {
        return cbStyle.getNumberOfColors();
    }

    public void setNumberOfColors(int val){
        if (cbStyle.getNumberOfColors()!=val){
            cbStyle.setNumberOfColors(val);
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getShowLTMin() {
        return showLTMin;
    }

    public void setShowLTMin(boolean b) {
        if (showLTMin!=b){
            showLTMin = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getShowGTMax() {
        return showGTMax;
    }

    public void setShowGTMax(boolean b) {
        if (showGTMax!=b){
            showGTMax = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    /**
     * Gets the vector length (in map units) corresponding to the standard scale.
     * 
     * @return 
     */
    public double getMapLength() {
        return mapLength;
    }

    /**
     * Sets the length on the map of a vector at the standard scale
     * 
     * @param val - the new map length 
     */
    public void setMapLength(double val) {
        if (mapLength != val) {
            mapLength = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getStandardWidth() {
        return stdWidth;
    }

    public void setStandardWidth(double wid) {
        if (stdWidth != wid) {
            stdWidth = wid;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getStride() {
        return stride;
    }

    public void setStride(double val) {
        if (stride != val) {
            stride = val;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getUseFixedColor() {
        return useFixedColor;
    }

    public void setUseFixedColor(boolean b) {
        if (useFixedColor!=b) {
            useFixedColor = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getUseFixedHeadSize() {
        return useFixedArrowheadSize;
    }

    public void setUseFixedHeadSize(boolean b) {
        if (useFixedArrowheadSize!=b){
            useFixedArrowheadSize = b;//does not impact style
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getUseFixedLength() {
        return useFixedLength;
    }

    public void setUseFixedLength(boolean b) {
        if (useFixedLength!=b) {
            useFixedLength = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public boolean getUseFixedWidth() {
        return useFixedWidth;
    }

    public void setUseFixedWidth(boolean b) {
        if (useFixedWidth!=b) {
            useFixedWidth = b;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
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

    public double getExponentScale(){
        return expScale;
    }
    
    public void setExponentScale(double scale){
        if (expScale != scale){
            expScale = scale;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
        }
    }

    public double getStandardScale(){
        return stdScale;
    }
    
    public void setStandardScale(double scale){
        if (stdScale != scale){
            stdScale = scale;
            mustUpdateStyle = true;
            propertySupport.firePropertyChange(PROP_ELEMENT, null,null);
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
