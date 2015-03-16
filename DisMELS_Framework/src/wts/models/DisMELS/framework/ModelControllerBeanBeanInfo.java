/*
 * ModelControllerBeanBeanInfo.java
 *
 * Created on January 4, 2006, 2:34 PM
 */

package wts.models.DisMELS.framework;

import wts.models.DisMELS.gui.ModelControllerBeanCustomizer;
import java.beans.*;
import org.openide.util.Exceptions;

/**
 * @author William Stockhausen
 */
public class ModelControllerBeanBeanInfo extends SimpleBeanInfo {
    private static Class mcbClass = ModelControllerBean.class;
    
    // Bean descriptor information will be obtained from introspection.//GEN-FIRST:BeanDescriptor
    private static BeanDescriptor beanDescriptor = null;
    private static BeanDescriptor getBdescriptor(){
//GEN-HEADEREND:BeanDescriptor
        
        // Here you can add code for customizing the BeanDescriptor.
        beanDescriptor = new BeanDescriptor(mcbClass,
                                            ModelControllerBeanCustomizer.class);
        
        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    
    
    // Properties information will be obtained from introspection.//GEN-FIRST:Properties
    private static PropertyDescriptor[] properties = null;
    private static PropertyDescriptor[] getPdescriptor(){
//GEN-HEADEREND:Properties
        
        // Here you can add code for customizing the properties array.
        try {
            PropertyDescriptor pfn = 
                    new PropertyDescriptor("file_Params",
                                           mcbClass);
            pfn.setBound(true);
            pfn.setConstrained(false);
            pfn.setDisplayName("LHS parameters file");
            
            PropertyDescriptor dfn = 
                    new PropertyDescriptor("file_ROMSDataset",
                                           mcbClass);
            dfn.setBound(true);
            dfn.setConstrained(false);
            dfn.setDisplayName("ROMS model dataset file");
            
            PropertyDescriptor iafn = 
                    new PropertyDescriptor("file_InitialAttributes",
                                           mcbClass);
            iafn.setBound(true);
            iafn.setConstrained(false);
            iafn.setDisplayName("Initial attributes file");
            
            PropertyDescriptor rfn = 
                    new PropertyDescriptor("file_Results",mcbClass);
            rfn.setBound(true);
            rfn.setConstrained(false);
            rfn.setDisplayName("Results file");
            
            PropertyDescriptor cnfn = 
                    new PropertyDescriptor("file_ConnResults",mcbClass);
            rfn.setBound(true);
            rfn.setConstrained(false);
            rfn.setDisplayName("Connectivity results file");
            
            PropertyDescriptor startTime = 
                    new PropertyDescriptor("startTime",mcbClass);
            startTime.setBound(true);
            startTime.setConstrained(false);
            startTime.setDisplayName("Start time (s)");
            
            PropertyDescriptor nTimes = 
                    new PropertyDescriptor("ntEnvironModel",mcbClass);
            nTimes.setBound(true);
            nTimes.setConstrained(false);
            nTimes.setDisplayName("# of env. model time steps");
            
            PropertyDescriptor dt = 
                    new PropertyDescriptor("timeStep",mcbClass);
            dt.setBound(true);
            dt.setConstrained(false);
            dt.setDisplayName("Env. model time step (s)");
            
            PropertyDescriptor nLPTsteps = 
                    new PropertyDescriptor("ntBioModel",mcbClass);
            nLPTsteps.setBound(true);
            nLPTsteps.setConstrained(false);
            nLPTsteps.setDisplayName("# of bio. model time steps");
            
            PropertyDescriptor animationRate = 
                    new PropertyDescriptor("animationRate",mcbClass);
            animationRate.setBound(true);
            animationRate.setConstrained(false);
            animationRate.setDisplayName("Rate for displaying animation");
            
            PropertyDescriptor resultsRate = 
                    new PropertyDescriptor("resultsRate",mcbClass);
            resultsRate.setBound(true);
            resultsRate.setConstrained(false);
            resultsRate.setDisplayName("Rate for writing results to disk");
            
            PropertyDescriptor lhsStageTransRate = 
                    new PropertyDescriptor("lhsStageTransRate",
                                           mcbClass,
                                           "getStageTransitionRate",
                                           "setStageTransitionRate");
            lhsStageTransRate.setBound(true);
            lhsStageTransRate.setConstrained(false);
            lhsStageTransRate.setDisplayName(ModelControllerBean.PROP_lhsStageTransRate);
            
            PropertyDescriptor randomNumberSeed = 
                    new PropertyDescriptor("randomNumberSeed",mcbClass);
            randomNumberSeed.setBound(true);
            randomNumberSeed.setConstrained(false);
            randomNumberSeed.setDisplayName(ModelControllerBean.PROP_randomNumberSeed);
            
            PropertyDescriptor runForward = 
                    new PropertyDescriptor("runForward",mcbClass);
            runForward.setBound(true);
            runForward.setConstrained(false);
            runForward.setDisplayName(ModelControllerBean.PROP_runForward);
            
            PropertyDescriptor fixedEnvironment = 
                    new PropertyDescriptor("fixedEnvironment",mcbClass);
            runForward.setBound(true);
            runForward.setConstrained(false);
            runForward.setDisplayName(ModelControllerBean.PROP_fixedEnvironment);
            
            PropertyDescriptor interpolateLikeROMS = 
                    new PropertyDescriptor("interpolateLikeROMS",mcbClass);
            interpolateLikeROMS.setBound(true);
            interpolateLikeROMS.setConstrained(false);
            interpolateLikeROMS.setDisplayName(ModelControllerBean.PROP_interpolateLikeROMS);
            
            PropertyDescriptor maskLikeROMS = 
                    new PropertyDescriptor("maskLikeROMS",mcbClass);
            maskLikeROMS.setBound(true);
            maskLikeROMS.setConstrained(false);
            maskLikeROMS.setDisplayName(ModelControllerBean.PROP_maskLikeROMS);
            
            PropertyDescriptor noVerticalMotion = 
                    new PropertyDescriptor("noVerticalMotion",mcbClass);
            noVerticalMotion.setBound(true);
            noVerticalMotion.setConstrained(false);
            noVerticalMotion.setDisplayName(ModelControllerBean.PROP_noVerticalMotion);
            
            PropertyDescriptor noAdvection = 
                    new PropertyDescriptor("noAdvection",mcbClass);
            noAdvection.setBound(true);
            noAdvection.setConstrained(false);
            noAdvection.setDisplayName(ModelControllerBean.PROP_noAdvection);
            
            PropertyDescriptor updateLayerDepthsLikeROMS = 
                    new PropertyDescriptor("updateLayerDepthsLikeROMS",mcbClass);
            updateLayerDepthsLikeROMS.setBound(true);
            updateLayerDepthsLikeROMS.setConstrained(false);
            updateLayerDepthsLikeROMS.setDisplayName(ModelControllerBean.PROP_updateLayerDepthsLikeROMS);
            
            PropertyDescriptor showDeadIndivs = 
                    new PropertyDescriptor("showDeadIndivs",mcbClass);
            updateLayerDepthsLikeROMS.setBound(true);
            updateLayerDepthsLikeROMS.setConstrained(false);
            updateLayerDepthsLikeROMS.setDisplayName(ModelControllerBean.PROP_showDeadIndivs);
            
            PropertyDescriptor includeTracks = 
                    new PropertyDescriptor("includeTracksInOutput",mcbClass);
            updateLayerDepthsLikeROMS.setBound(true);
            updateLayerDepthsLikeROMS.setConstrained(false);
            updateLayerDepthsLikeROMS.setDisplayName(ModelControllerBean.PROP_includeTracksInOutput);
            
            properties = new PropertyDescriptor[] 
                             {pfn,dfn,iafn,rfn,cnfn,
                              startTime,nTimes,dt,nLPTsteps,
                              animationRate,resultsRate,lhsStageTransRate,
                              randomNumberSeed,runForward,
                              fixedEnvironment, interpolateLikeROMS, 
                              maskLikeROMS, noVerticalMotion, noAdvection,
                              updateLayerDepthsLikeROMS,showDeadIndivs,includeTracks};
                             
            System.out.println("PropertyDescriptors");
        } catch (IntrospectionException e) {
            System.out.println(e.toString());
            properties = null;
        }
        System.out.println("PropertyDescriptors");
        return properties;     }//GEN-LAST:Properties
    
    // Event set information will be obtained from introspection.//GEN-FIRST:Events
    private static EventSetDescriptor[] eventSets = null;
    private static EventSetDescriptor[] getEdescriptor(){
//GEN-HEADEREND:Events
        
        // Here you can add code for customizing the event sets array.
        try {
            EventSetDescriptor prop = 
                    new EventSetDescriptor (mcbClass, 
                                            "propertyChangeListener", 
                                             java.beans.PropertyChangeListener.class, 
                                             new String[] {"propertyChange"}, 
                                             "addPropertyChangeListener", 
                                             "removePropertyChangeListener" );
            eventSets = new EventSetDescriptor[] {prop};
        }
        catch(IntrospectionException e) {
            Exceptions.printStackTrace(e);
        }
        
        return eventSets;     }//GEN-LAST:Events
    
    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){
//GEN-HEADEREND:Methods
        
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
    
    private static int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static int defaultEventIndex = -1;//GEN-END:Idx
    
    
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
    @Override
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

