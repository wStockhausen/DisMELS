/*
 * LHSPointFeatureType
 *
 * Created on January 12, 2006, 10:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import java.io.Serializable;
import java.net.URI;
import org.geotools.feature.*;
import wts.roms.gis.AlbersNAD83;

/**
 * An implementation of a point FeatureType without LHSAttributes attached.
 * 
 * @author William Stockhausen
 */
public class LHSPointFeatureTypeNoAtts 
                      implements FeatureType, Serializable {
    
    protected static final GeometryFactory geomFactory = new GeometryFactory();
    
    /* the LHS type name */
    protected String typeName;
    /* the actual feature type */
    protected FeatureType ft = null;
    /* the geometry for the feature type */
    protected Point point;
    
    /**
     * Creates a new instance of LHSPointFeatureTypeNoAtts
     */
    public LHSPointFeatureTypeNoAtts(LifeStageAttributesInterface aLHS) {
        this.typeName = aLHS.getTypeName();
        try {
            createFeatureType();
        } catch (IllegalAttributeException | SchemaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new instance of LHSPointFeatureTypeNoAtts
     */
    protected LHSPointFeatureTypeNoAtts(String typeName) throws InstantiationException, 
                                                       IllegalAccessException {
        this.typeName = typeName;
        try {
            createFeatureType();
        } catch (IllegalAttributeException | SchemaException ex) {
            ex.printStackTrace();
        }
    }
    
    protected final void createFeatureType()  
                        throws SchemaException,IllegalAttributeException {
        AttributeType[] aTypes = new AttributeType[2];
        aTypes[0] = AttributeTypeFactory.newAttributeType(
                        "geometry",
                        com.vividsolutions.jts.geom.Point.class);
        aTypes[1] = AttributeTypeFactory.newAttributeType(
                        "lhsType",
                        String.class);
        ft = FeatureTypeFactory.newFeatureType(aTypes,typeName); //set super variable       
    }
    
    public Feature createFeature() throws IllegalAttributeException {
        Feature f = null;
        f = ft.create(new Object[] {point,typeName});
        return f;
    }
    
    public Point getGeometry() {
        return point;
    }
    
    /**
     * Sets the next feature geometry to the input Point. The input coordinates
     * should be AlbersNAD83 (Greenwich Prime Meridian, [-180,180]).
     *
     * @param pt - Point in coordinates as returned from a MapGUI_JPanel
     * 
     */
    public void setGeometryFromMap(Point pt) {
        point = pt;
    } 
    
    /**
     * Sets the next feature geometry to the input (xp,yp) and
     * updates the associated LHSAttributesIF instance.  
     * 
     * The input coordinates should be AlbersNAD83.
     *
     * @param xp
     * @param yp
     * 
     */
    public void setGeometryFromMap(double xp, double yp) {
        point = geomFactory.createPoint(new Coordinate(xp,yp));
    } 
        
    /**
     * Sets the next feature geometry from an input LifeStageAttributesInterface instance. 
     * The feature position coordinates will be AlbersNAD83.  
     */
    public void setGeometryFromAttributes(LifeStageAttributesInterface aLHS) {
        double[] xp = AlbersNAD83.transformGtoP(aLHS.getGeometry());
        point = geomFactory.createPoint(new Coordinate(xp[0],xp[1],xp[2]));
    } 
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    
//          Wrapper functions required to implement FeatureType
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>    
    @Override
    public URI getNamespace() {
        return ft.getNamespace();
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public boolean isDescendedFrom(URI nsURI, String typeName) {
        return ft.isDescendedFrom(nsURI,typeName);
    }

    @Override
    public boolean isDescendedFrom(FeatureType type) {
        return ft.isDescendedFrom(type);
    }

    @Override
    public boolean isAbstract() {
        return ft.isAbstract();
    }

    @Override
    public FeatureType[] getAncestors() {
        return ft.getAncestors();
    }

    @Override
    public GeometryAttributeType getDefaultGeometry() {
        return ft.getDefaultGeometry();
    }

    @Override
    public int getAttributeCount() {
        return ft.getAttributeCount();
    }

    @Override
    public AttributeType getAttributeType(String xPath) {
        return ft.getAttributeType(xPath);
    }

    @Override
    public int find(AttributeType type) {
        return ft.find(type);
    }

    @Override
    public int find(String attName) {
        return ft.find(attName);
    }

    @Override
    public AttributeType getAttributeType(int position) {
        return ft.getAttributeType(position);
    }

    @Override
    public AttributeType[] getAttributeTypes() {
        return ft.getAttributeTypes();
    }

    @Override
    public boolean hasAttributeType(String xPath) {
        return ft.hasAttributeType(xPath);
    }

    @Override
    public Feature duplicate(Feature feature) throws IllegalAttributeException {
        return ft.duplicate(feature);
    }

    @Override
    public Feature create(Object[] attributes) throws IllegalAttributeException {
        return ft.create(attributes);
    }

    @Override
    public Feature create(Object[] attributes, String featureID) throws IllegalAttributeException {
        return ft.create(attributes,featureID);
    }
        
}
