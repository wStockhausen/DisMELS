/*
 * LHS_TypeBeanInfo.java
 *
 * Created on April 3, 2006, 4:13 PM
 */

package wts.models.DisMELS.framework;

import java.beans.*;

/**
 * @author William Stockhausen
 */
public class LHS_TypeBeanInfo extends SimpleBeanInfo {
    
    // Bean descriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor(LHS_Type.class , 
                                                           LHS_TypeCustomizer.class ); // NOI18N
        beanDescriptor.setDisplayName ( "LHS_Type" );
        
        // Here you can add code for customizing the BeanDescriptor.
        
        return beanDescriptor;     }
    
    
    // Property identifiers                      
    private static PropertyDescriptor pDLHSClass;
    private static PropertyDescriptor pDLHSName;
    private static PropertyDescriptor pDattributesClass;
    private static PropertyDescriptor pDattributesName;
    private static PropertyDescriptor pDnextLHSClass;
    private static PropertyDescriptor pDnextLHSName;
    private static PropertyDescriptor pDspawnedLHSClass;
    private static PropertyDescriptor pDspawnedLHSName;
    private static PropertyDescriptor pDparametersClass;
    private static PropertyDescriptor pDparametersName;
    private static PropertyDescriptor pDpointFTClass;
    private static PropertyDescriptor pDpointFTName;
    private static PropertyDescriptor pDcolor;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = null;
    
        try {
            pDLHSClass = new PropertyDescriptor ( "lhsClass", LHS_Type.class, "getLHSClass", "setLHSClass" ); // NOI18N
            pDLHSClass.setBound ( true );
            pDLHSName = new PropertyDescriptor ( "lhsName", LHS_Type.class, "getLHSName", "setLHSName" ); // NOI18N
            pDLHSName.setBound ( true );
            pDnextLHSName = new PropertyDescriptor ( "nextLHSName", LHS_Type.class, "getNextLHSName", "setNextLHSName" ); // NOI18N
            pDnextLHSName.setBound ( true );
            pDnextLHSClass = new PropertyDescriptor ( "nextLHSClass", LHS_Type.class, "getNextLHSClass", "setNextLHSClass" ); // NOI18N
            pDnextLHSClass.setBound ( true );
            pDspawnedLHSName = new PropertyDescriptor ( "spawnedLHSName", LHS_Type.class, "getSpawnedLHSName", "setSpawnedLHSName" ); // NOI18N
            pDspawnedLHSName.setBound ( true );
            pDspawnedLHSClass = new PropertyDescriptor ( "spawnedLHSClass", LHS_Type.class, "getSpawnedLHSClass", "setSpawnedLHSClass" ); // NOI18N
            pDspawnedLHSClass.setBound ( true );
            pDcolor = new PropertyDescriptor ( "color", LHS_Type.class, "getColor", "setColor" ); // NOI18N
            pDcolor.setBound ( true );
            properties = new PropertyDescriptor[] {pDLHSClass,
                                                   pDLHSName,
                                                   pDnextLHSName,
                                                   pDnextLHSClass,
                                                   pDspawnedLHSName,
                                                   pDspawnedLHSClass,
                                                   pDcolor};
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }
        
        // Here you can add code for customizing the properties array.
        
        return properties;     }
    
    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_propertyChangeListener = 0;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];
    
        try {
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor(LHS_Type.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events
        
        // Here you can add code for customizing the event sets array.
        
        return eventSets;     }//GEN-LAST:Events
    
    // Method identifiers//GEN-FIRST:Methods

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[0];//GEN-HEADEREND:Methods
        
        // Here you can add code for customizing the methods array.
        
        return methods;     }//GEN-LAST:Methods
    
    
    private static final int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static final int defaultEventIndex = -1;//GEN-END:Idx
    
    
//GEN-FIRST:Superclass
    
    // Here you can add code for customizing the Superclass BeanInfo.
    
//GEN-LAST:Superclass
    
    /**
     * Gets the bean's <code>BeanDescriptor</code>s.
     *
     * @return BeanDescriptor describing the editable
     * properties of this bean.  May return null if the
     * information should be obtained by automatic analysis.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }
    
    /**
     * Gets the bean's <code>PropertyDescriptor</code>s.
     *
     * @return An array of PropertyDescriptors describing the editable
     * properties supported by this bean.  May return null if the
     * information should be obtained by automatic analysis.
     * <p>
     * If a property is indexed, then its entry in the result array will
     * belong to the IndexedPropertyDescriptor subclass of PropertyDescriptor.
     * A client of getPropertyDescriptors can use "instanceof" to check
     * if a given PropertyDescriptor is an IndexedPropertyDescriptor.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }
    
    /**
     * Gets the bean's <code>EventSetDescriptor</code>s.
     *
     * @return  An array of EventSetDescriptors describing the kinds of
     * events fired by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }
    
    /**
     * Gets the bean's <code>MethodDescriptor</code>s.
     *
     * @return  An array of MethodDescriptors describing the methods
     * implemented by this bean.  May return null if the information
     * should be obtained by automatic analysis.
     */
    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }
    
    /**
     * A bean may have a "default" property that is the property that will
     * mostly commonly be initially chosen for update by human's who are
     * customizing the bean.
     * @return  Index of default property in the PropertyDescriptor array
     * 		returned by getPropertyDescriptors.
     * <P>	Returns -1 if there is no default property.
     */
    @Override
    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }
    
    /**
     * A bean may have a "default" event that is the event that will
     * mostly commonly be used by human's when using the bean.
     * @return Index of default event in the EventSetDescriptor array
     *		returned by getEventSetDescriptors.
     * <P>	Returns -1 if there is no default event.
     */
    @Override
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }
}

