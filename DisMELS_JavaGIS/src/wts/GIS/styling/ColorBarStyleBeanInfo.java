/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import java.beans.*;

/**
 *
 * @author wstockhausen
 */
public class ColorBarStyleBeanInfo extends SimpleBeanInfo {

    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( wts.GIS.styling.ColorBarStyle.class , wts.GIS.styling.ColorBarStyleCustomizer.class );

    // Here you can add code for customizing the BeanDescriptor.

        return beanDescriptor;     }


//     private static final int PROPERTY_abstract = 0;
//     private static final int PROPERTY_colorRamp = 1;
//     private static final int PROPERTY_max = 3;
//     private static final int PROPERTY_min = 4;
//     private static final int PROPERTY_name = 5;
//     private static final int PROPERTY_numberOfColors = 6;
//     private static final int PROPERTY_showGTMax = 7;
//     private static final int PROPERTY_showLTMin = 8;
//     private static final int PROPERTY_title = 9;

    // Property array
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[0];
//         PropertyDescriptor[] properties = new PropertyDescriptor[10];
//
//         try {
//             properties[PROPERTY_abstract] = new PropertyDescriptor ( "abstract", wts.GIS.styling.ColorBarStyle.class, "getAbstract", "setAbstract" ); // NOI18N
//             properties[PROPERTY_colorRamp] = new PropertyDescriptor ( "colorRamp", wts.GIS.styling.ColorBarStyle.class, "getColorRamp", "setColorRamp" ); // NOI18N
//             properties[PROPERTY_max] = new PropertyDescriptor ( "max", wts.GIS.styling.ColorBarStyle.class, "getMax", "setMax" ); // NOI18N
//             properties[PROPERTY_min] = new PropertyDescriptor ( "min", wts.GIS.styling.ColorBarStyle.class, "getMin", "setMin" ); // NOI18N
//             properties[PROPERTY_name] = new PropertyDescriptor ( "name", wts.GIS.styling.ColorBarStyle.class, "getName", "setName" ); // NOI18N
//             properties[PROPERTY_numberOfColors] = new PropertyDescriptor ( "numberOfColors", wts.GIS.styling.ColorBarStyle.class, "getNumberOfColors", "setNumberOfColors" ); // NOI18N
//             properties[PROPERTY_showGTMax] = new PropertyDescriptor ( "showGTMax", wts.GIS.styling.ColorBarStyle.class, "getShowGTMax", "setShowGTMax" ); // NOI18N
//             properties[PROPERTY_showLTMin] = new PropertyDescriptor ( "showLTMin", wts.GIS.styling.ColorBarStyle.class, "getShowLTMin", "setShowLTMin" ); // NOI18N
//             properties[PROPERTY_title] = new PropertyDescriptor ( "title", wts.GIS.styling.ColorBarStyle.class, "getTitle", "setTitle" ); // NOI18N
//         }
//         catch(IntrospectionException e) {
//             e.printStackTrace();
//         }

    // Here you can add code for customizing the properties array.

        return properties;     }

    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_propertyChangeListener = 0;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[1];

        try {
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( wts.GIS.styling.ColorBarStyle.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events

    // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

//     private static final int METHOD_accept0 = 0;
//     private static final int METHOD_addFeatureTypeStyle1 = 1;
//     private static final int METHOD_clone2 = 2;
//     private static final int METHOD_createColorBarStyle3 = 3;

    // Method array
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){
//         MethodDescriptor[] methods = new MethodDescriptor[4];
//
//         try {
//             methods[METHOD_accept0] = new MethodDescriptor(wts.GIS.styling.ColorBarStyle.class.getMethod("accept", new Class[] {org.geotools.styling.StyleVisitor.class})); // NOI18N
//             methods[METHOD_accept0].setDisplayName ( "" );
//             methods[METHOD_addFeatureTypeStyle1] = new MethodDescriptor(wts.GIS.styling.ColorBarStyle.class.getMethod("addFeatureTypeStyle", new Class[] {org.geotools.styling.FeatureTypeStyle.class})); // NOI18N
//             methods[METHOD_addFeatureTypeStyle1].setDisplayName ( "" );
//             methods[METHOD_clone2] = new MethodDescriptor(wts.GIS.styling.ColorBarStyle.class.getMethod("clone", new Class[] {})); // NOI18N
//             methods[METHOD_clone2].setDisplayName ( "" );
//             methods[METHOD_createColorBarStyle3] = new MethodDescriptor(wts.GIS.styling.ColorBarStyle.class.getMethod("createColorBarStyle", new Class[] {})); // NOI18N
//             methods[METHOD_createColorBarStyle3].setDisplayName ( "" );
//         }
//         catch( Exception e) {}//GEN-HEADEREND:Methods

    // Here you can add code for customizing the methods array.

        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons

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

