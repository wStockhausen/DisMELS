/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.models.utilities;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 *
 * @author wstockhausen
 */
public class DateTimeBeanInfo extends SimpleBeanInfo {
    private static Class clazz = DateTime.class;
    
    // Bean descriptor information will be obtained from introspection.
    private static BeanDescriptor beanDescriptor = null;
    private static BeanDescriptor getBdescriptor(){
        
        // Here you can add code for customizing the BeanDescriptor.
        beanDescriptor = new BeanDescriptor(clazz,
                                            DateTimeCustomizer.class);
        
        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    
    
    // Properties information will be obtained from introspection.//GEN-FIRST:Properties
    private static PropertyDescriptor[] properties = null;
    private static PropertyDescriptor[] getPdescriptor(){
//GEN-HEADEREND:Properties
        
        // Here you can add code for customizing the properties array.
        try {
            PropertyDescriptor pdYear = 
                    new PropertyDescriptor("year",
                                           clazz);
            pdYear.setBound(true);
            pdYear.setConstrained(false);
            pdYear.setDisplayName("Year");
            
            PropertyDescriptor pdDOY = 
                    new PropertyDescriptor("doy",
                                           clazz);
            pdDOY.setBound(true);
            pdDOY.setConstrained(false);
            pdDOY.setDisplayName("Day of year");
            
            PropertyDescriptor pdMonth = 
                    new PropertyDescriptor("month",
                                           clazz);
            pdMonth.setBound(true);
            pdMonth.setConstrained(false);
            pdMonth.setDisplayName("Month");
            
            PropertyDescriptor pdDOM = 
                    new PropertyDescriptor("dom",
                                           clazz);
            pdDOM.setBound(true);
            pdDOM.setConstrained(false);
            pdDOM.setDisplayName("Day of month");
            
            PropertyDescriptor pdHour = 
                    new PropertyDescriptor("hour",
                                           clazz);
            pdHour.setBound(true);
            pdHour.setConstrained(false);
            pdHour.setDisplayName("Hour");
            
            PropertyDescriptor pdMin = 
                    new PropertyDescriptor("min",clazz);
            pdMin.setBound(true);
            pdMin.setConstrained(false);
            pdMin.setDisplayName("Minute");
            
            PropertyDescriptor pdSec = 
                    new PropertyDescriptor("sec",clazz);
            pdSec.setBound(true);
            pdSec.setConstrained(false);
            pdSec.setDisplayName("Second");
                        
            properties = new PropertyDescriptor[] 
                             {pdYear,pdMonth,pdDOY,pdDOM,
                              pdHour,pdMin,pdSec};
                             
            System.out.println("PropertyDescriptors");
        } catch (IntrospectionException e) {
            System.out.println(e.toString());
            properties = null;
        }
        System.out.println("PropertyDescriptors");
        return properties;     }//GEN-LAST:Properties
    
    // Event set information will be obtained from introspection.
    private static EventSetDescriptor[] eventSets = null;
    private static EventSetDescriptor[] getEdescriptor(){
        
        // Here you can add code for customizing the event sets array.
        try {
            EventSetDescriptor prop = 
                    new EventSetDescriptor (clazz, 
                                            "propertyChangeListener", 
                                             java.beans.PropertyChangeListener.class, 
                                             new String[] {"propertyChange"}, 
                                             "addPropertyChangeListener", 
                                             "removePropertyChangeListener"  );
            eventSets = new EventSetDescriptor[] {prop};
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }
        
        return eventSets;     }
    
    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){
        // Here you can add code for customizing the methods array.
        return methods;     }
    
    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons
    
    private static int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static int defaultEventIndex = -1;//GEN-END:Idx
    
    
    // Here you can add code for customizing the Superclass BeanInfo.
    
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
    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }
    
    /**
     * This method returns an image object that can be used to
     * represent the bean in toolboxes, toolbars, etc.   Icon images
     * will typically be GIFs, but may in future include other formats.
     * <p>
     * Beans aren't required to provide icons and may return null from
     * this method.
     * <p>
     * There are four possible flavors of icons (16x16 color,
     * 32x32 color, 16x16 mono, 32x32 mono).  If a bean choses to only
     * support a single icon we recommend supporting 16x16 color.
     * <p>
     * We recommend that icons have a "transparent" background
     * so they can be rendered onto an existing background.
     *
     * @param  iconKind  The kind of icon requested.  This should be
     *    one of the constant values ICON_COLOR_16x16, ICON_COLOR_32x32,
     *    ICON_MONO_16x16, or ICON_MONO_32x32.
     * @return  An image object representing the requested icon.  May
     *    return null if no suitable icon is available.
     */
    public java.awt.Image getIcon(int iconKind) {
        switch ( iconKind ) {
            case ICON_COLOR_16x16:
                if ( iconNameC16 == null )
                    return null;
                else {
                    if( iconColor16 == null )
                        iconColor16 = loadImage( iconNameC16 );
                    return iconColor16;
                }
            case ICON_COLOR_32x32:
                if ( iconNameC32 == null )
                    return null;
                else {
                    if( iconColor32 == null )
                        iconColor32 = loadImage( iconNameC32 );
                    return iconColor32;
                }
            case ICON_MONO_16x16:
                if ( iconNameM16 == null )
                    return null;
                else {
                    if( iconMono16 == null )
                        iconMono16 = loadImage( iconNameM16 );
                    return iconMono16;
                }
            case ICON_MONO_32x32:
                if ( iconNameM32 == null )
                    return null;
                else {
                    if( iconMono32 == null )
                        iconMono32 = loadImage( iconNameM32 );
                    return iconMono32;
                }
            default: return null;
        }
    }

}
