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
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import java.io.Serializable;
import java.net.URI;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.openide.util.Exceptions;

/**
 *
 * @author William Stockhausen
 */
public class LHSTrackFeatureType 
                      implements FeatureType, Serializable {
    
    protected static final GeometryFactory geomFactory = new GeometryFactory();
    
    /* the LHS type name */
    protected String typeName;
    /* the actual feature type */
    protected FeatureType ft = null;
    /* the IBM track */
    protected LineString track;

    /**
     * Creates a new instance of LHSTrackFeatureType
     */
    protected LHSTrackFeatureType(String typeName) {
        this.typeName = typeName;
        try {
            createFeatureType();
        } catch (IllegalAttributeException | SchemaException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    protected final void createFeatureType()  
                        throws SchemaException,IllegalAttributeException {
        AttributeType[] aTypes = new AttributeType[2];
        aTypes[0] = AttributeTypeFactory.newAttributeType(
                        "geometry",
                        com.vividsolutions.jts.geom.LineString.class);
        aTypes[1] = AttributeTypeFactory.newAttributeType(
                        "lhsType",
                        String.class);
        ft = FeatureTypeFactory.newFeatureType(aTypes,"lhsTrack");        
    }
    
    public Feature createFeature() throws IllegalAttributeException {
        Feature f = ft.create(new Object[] {track,typeName});
        return f;
    }
    
    public LineString getGeometry() {
        return track;
    }
    
    public void setGeometry(double xS, double yS, double zS, 
                            double xE, double yE, double Ze) {
        Coordinate start = new Coordinate(xS,yS,zS);
        Coordinate end = new Coordinate(xE,yE,zS);
        track = geomFactory.createLineString(new Coordinate[]{start,end});
    }
    
    public void setGeometry(CoordinateSequence cs) {
        track = geomFactory.createLineString(cs);
    }
    
    public void setGeometry(Coordinate[] cv) {
        track = geomFactory.createLineString(cv);
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
        return ft.getTypeName();
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
