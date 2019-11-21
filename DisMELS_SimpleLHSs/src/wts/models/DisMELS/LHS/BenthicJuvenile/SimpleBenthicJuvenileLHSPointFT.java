/*
 * GenericLHSPointFT.java
 *
 * Created on March 8, 2006, 11:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.BenthicJuvenile;

import wts.models.DisMELS.framework.*;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;

/**
 *
 * @author William Stockhausen
 */
public class SimpleBenthicJuvenileLHSPointFT extends LHSPointFeatureType {
    
    /**
     * Creates a new instance of GenericLHSPointFT
     */
    public SimpleBenthicJuvenileLHSPointFT(SimpleBenthicJuvenileLHSAttributes aLHS) {
        super(aLHS);
//        this.aLHS = aLHS; //set super variable
//        try {
//            createFeatureType();
//        } catch (IllegalAttributeException ex) {
//            ex.printStackTrace();
//        } catch (SchemaException ex) {
//            ex.printStackTrace();
//        }
    }
    
    public SimpleBenthicJuvenileLHSPointFT(String typeName) throws InstantiationException, 
                                                       IllegalAccessException {
        super(typeName);
//        this.aLHS = (SimpleBenthicJuvenileLHSAttributes) LHS_Factory.createAttributes(typeName);
//        try {
//            createFeatureType();
//        } catch (IllegalAttributeException ex) {
//            ex.printStackTrace();
//        } catch (SchemaException ex) {
//            ex.printStackTrace();
//        }
    }
    
//    @Override
//    protected void createFeatureType()  
//                        throws SchemaException,IllegalAttributeException {
//        AttributeType[] aTypes = new AttributeType[3];
//        aTypes[0] = AttributeTypeFactory.newAttributeType(
//                        "geometry",
//                        com.vividsolutions.jts.geom.Point.class);
//        aTypes[1] = AttributeTypeFactory.newAttributeType(
//                        "Attributes",
//                        aLHS.getClass());
//        aTypes[2] = AttributeTypeFactory.newAttributeType(
//                        "lhsType",
//                        String.class);
//        ft = FeatureTypeFactory.newFeatureType(aTypes,typeName); //set super variable       
//    }
    
}
