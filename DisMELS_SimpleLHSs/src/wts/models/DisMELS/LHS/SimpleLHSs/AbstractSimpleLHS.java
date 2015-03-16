/*
 * AbstractSimpleLHS.java
 *
 * Created on January 19, 2006, 1:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.SimpleLHSs;

import wts.models.DisMELS.framework.AbstractLHS;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

/**
 *
 * @author William Stockhausen
 */
public abstract class AbstractSimpleLHS extends AbstractLHS {
    
    //Instance fields
    /* the LHS attributes */
    protected AbstractSimpleLHSAttributes atts = null;
    
    //fields that reflect new attribute values
    protected boolean attached=false;
    protected double size=0;
    protected double salinity=-1;
    protected double temperature=-1;
    
    /**
     * Creates a new instance of AbstractLHS
     */
    protected AbstractSimpleLHS(String typeName) {
        super(typeName);
    }
    
    /**
     * Subclasses can use this method to make sure the attributes object from
     * this superclass refers to the one from their class.
     * 
     * @param subAtts 
     */
    protected void setAttributesFromSubClass(AbstractSimpleLHSAttributes subAtts){
        atts = subAtts;
        super.setAttributesFromSubClass(atts);
    }
    
//  Methods inherited from LifeHistoryStageIF  
    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * This provides a default implementation of getReport() that returns
     * the attributes (via AbstractLHSAttributes.getCSV()) and the track (in
     * geographic coordinates) as a CSV formatted string.
     *
     * @return - the attributes and track as a csv-formatted String
     */
    @Override
    public String getReport() {
        updateAttributes();
        return atts.getCSV()+cc+getTrackAsString(COORDINATE_TYPE_GEOGRAPHIC);
    }

     /**
     * This provides a default implementation of getReportHeader() that returns
     * the attribute names (via AbstractLHSAttributes.getCSVHeaderShortNames())
     * and "track" as a CSV formatted string.
     *
     * @return - the header names as a csv-formatted String
     */

    @Override
    public String getReportHeader() {
        return atts.getCSVHeaderShortNames()+cc+"track";
    }

    /**
     * Sets the attributes for the instance by copying values from the input.
     * This does NOT change the typeName of the LHS instance (or the associated 
     * LHSAttributes instance) on which the method is called.
     * Note that ALL attributes are copied, so id, parentID, and origID are copied
     * as well. 
     *  Side effects:
     *      updateVariables() is called to update instance variables.
     *      Instance field "id" is also updated.
     * @param newAtts - should be instance of SimplePelagicLHSAttributes
     */
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof AbstractSimpleLHSAttributes){
            super.setAttributes(newAtts);
            AbstractSimpleLHSAttributes spAtts = (AbstractSimpleLHSAttributes) newAtts;
            atts.setValue(AbstractSimpleLHSAttributes.PROP_attached,spAtts.getValue(AbstractSimpleLHSAttributes.PROP_attached));
            atts.setValue(AbstractSimpleLHSAttributes.PROP_salinity,spAtts.getValue(AbstractSimpleLHSAttributes.PROP_salinity));
            atts.setValue(AbstractSimpleLHSAttributes.PROP_size,    spAtts.getValue(AbstractSimpleLHSAttributes.PROP_size));
            atts.setValue(AbstractSimpleLHSAttributes.PROP_temp,    spAtts.getValue(AbstractSimpleLHSAttributes.PROP_temp));
        }
//        updateVariables(); <-don't need to update the superclass variables again, so don't call this
        attached   = atts.getValue(AbstractSimpleLHSAttributes.PROP_attached,attached);
        size       = atts.getValue(AbstractSimpleLHSAttributes.PROP_size,size);
        temperature= atts.getValue(AbstractSimpleLHSAttributes.PROP_temp,temperature);
        salinity   = atts.getValue(AbstractSimpleLHSAttributes.PROP_salinity,salinity);

    }
    
    @Override
    protected void updateAttributes() {
        //note that the following do not need to be updated
        //  id, parentID, origID, startTime, horizType, vertType
        super.updateAttributes();
        atts.setValue(AbstractSimpleLHSAttributes.PROP_attached,attached);
        atts.setValue(AbstractSimpleLHSAttributes.PROP_size,size);
        atts.setValue(AbstractSimpleLHSAttributes.PROP_temp,temperature);
        atts.setValue(AbstractSimpleLHSAttributes.PROP_salinity,salinity);
    }

    /**
     * Updates local variables from the attributes.  
     * The following are NOT updated here:
     *  id, parentID, origID, hType, vType
     */
    @Override
    protected void updateVariables() {
        super.updateVariables();
        attached   = atts.getValue(AbstractSimpleLHSAttributes.PROP_attached,attached);
        size       = atts.getValue(AbstractSimpleLHSAttributes.PROP_size,size);
        temperature= atts.getValue(AbstractSimpleLHSAttributes.PROP_temp,temperature);
        salinity   = atts.getValue(AbstractSimpleLHSAttributes.PROP_salinity,salinity);
    }    
}
