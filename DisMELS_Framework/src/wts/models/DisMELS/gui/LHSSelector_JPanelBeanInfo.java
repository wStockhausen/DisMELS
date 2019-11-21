/*
 * LHSSelector_JPanelBeanInfo.java
 *
 * Created on June 5, 2006, 2:18 PM
 */

package wts.models.DisMELS.gui;

import java.beans.*;

/**
 * @author William Stockhausen
 */
public class LHSSelector_JPanelBeanInfo extends SimpleBeanInfo {
    
    // Bean descriptor//GEN-FIRST:BeanDescriptor
    /*lazy BeanDescriptor*/
    private static BeanDescriptor getBdescriptor(){
        BeanDescriptor beanDescriptor = new BeanDescriptor  ( wts.models.DisMELS.gui.LHSSelector_JPanel.class , null ); // NOI18N//GEN-HEADEREND:BeanDescriptor
        
        // Here you can add code for customizing the BeanDescriptor.
        
        return beanDescriptor;     }//GEN-LAST:BeanDescriptor
    
    
    // Property identifiers//GEN-FIRST:Properties
    private static final int PROPERTY_accessibleContext = 0;
    private static final int PROPERTY_actionListeners = 1;
    private static final int PROPERTY_actionMap = 2;
    private static final int PROPERTY_alignmentX = 3;
    private static final int PROPERTY_alignmentY = 4;
    private static final int PROPERTY_ancestorListeners = 5;
    private static final int PROPERTY_autoscrolls = 6;
    private static final int PROPERTY_background = 7;
    private static final int PROPERTY_backgroundSet = 8;
    private static final int PROPERTY_border = 9;
    private static final int PROPERTY_bounds = 10;
    private static final int PROPERTY_colorModel = 11;
    private static final int PROPERTY_component = 12;
    private static final int PROPERTY_componentCount = 13;
    private static final int PROPERTY_componentListeners = 14;
    private static final int PROPERTY_componentOrientation = 15;
    private static final int PROPERTY_componentPopupMenu = 16;
    private static final int PROPERTY_components = 17;
    private static final int PROPERTY_containerListeners = 18;
    private static final int PROPERTY_cursor = 19;
    private static final int PROPERTY_cursorSet = 20;
    private static final int PROPERTY_debugGraphicsOptions = 21;
    private static final int PROPERTY_displayable = 22;
    private static final int PROPERTY_doubleBuffered = 23;
    private static final int PROPERTY_dropTarget = 24;
    private static final int PROPERTY_enabled = 25;
    private static final int PROPERTY_focusable = 26;
    private static final int PROPERTY_focusCycleRoot = 27;
    private static final int PROPERTY_focusCycleRootAncestor = 28;
    private static final int PROPERTY_focusListeners = 29;
    private static final int PROPERTY_focusOwner = 30;
    private static final int PROPERTY_focusTraversable = 31;
    private static final int PROPERTY_focusTraversalKeys = 32;
    private static final int PROPERTY_focusTraversalKeysEnabled = 33;
    private static final int PROPERTY_focusTraversalPolicy = 34;
    private static final int PROPERTY_focusTraversalPolicyProvider = 35;
    private static final int PROPERTY_focusTraversalPolicySet = 36;
    private static final int PROPERTY_font = 37;
    private static final int PROPERTY_fontSet = 38;
    private static final int PROPERTY_foreground = 39;
    private static final int PROPERTY_foregroundSet = 40;
    private static final int PROPERTY_graphics = 41;
    private static final int PROPERTY_graphicsConfiguration = 42;
    private static final int PROPERTY_height = 43;
    private static final int PROPERTY_hierarchyBoundsListeners = 44;
    private static final int PROPERTY_hierarchyListeners = 45;
    private static final int PROPERTY_ignoreRepaint = 46;
    private static final int PROPERTY_inheritsPopupMenu = 47;
    private static final int PROPERTY_inputContext = 48;
    private static final int PROPERTY_inputMap = 49;
    private static final int PROPERTY_inputMethodListeners = 50;
    private static final int PROPERTY_inputMethodRequests = 51;
    private static final int PROPERTY_inputVerifier = 52;
    private static final int PROPERTY_insets = 53;
    private static final int PROPERTY_keyListeners = 54;
    private static final int PROPERTY_layout = 55;
    private static final int PROPERTY_lightweight = 56;
    private static final int PROPERTY_locale = 57;
    private static final int PROPERTY_location = 58;
    private static final int PROPERTY_locationOnScreen = 59;
    private static final int PROPERTY_managingFocus = 60;
    private static final int PROPERTY_maximumSize = 61;
    private static final int PROPERTY_maximumSizeSet = 62;
    private static final int PROPERTY_minimumSize = 63;
    private static final int PROPERTY_minimumSizeSet = 64;
    private static final int PROPERTY_mouseListeners = 65;
    private static final int PROPERTY_mouseMotionListeners = 66;
    private static final int PROPERTY_mousePosition = 67;
    private static final int PROPERTY_mouseWheelListeners = 68;
    private static final int PROPERTY_name = 69;
    private static final int PROPERTY_nextFocusableComponent = 70;
    private static final int PROPERTY_opaque = 71;
    private static final int PROPERTY_optimizedDrawingEnabled = 72;
    private static final int PROPERTY_paintingTile = 73;
    private static final int PROPERTY_parent = 74;
    private static final int PROPERTY_peer = 75;
    private static final int PROPERTY_preferredSize = 76;
    private static final int PROPERTY_preferredSizeSet = 77;
    private static final int PROPERTY_propertyChangeListeners = 78;
    private static final int PROPERTY_registeredKeyStrokes = 79;
    private static final int PROPERTY_requestFocusEnabled = 80;
    private static final int PROPERTY_rootPane = 81;
    private static final int PROPERTY_selectedName = 82;
    private static final int PROPERTY_showing = 83;
    private static final int PROPERTY_size = 84;
    private static final int PROPERTY_toolkit = 85;
    private static final int PROPERTY_toolTipText = 86;
    private static final int PROPERTY_topLevelAncestor = 87;
    private static final int PROPERTY_transferHandler = 88;
    private static final int PROPERTY_treeLock = 89;
    private static final int PROPERTY_UI = 90;
    private static final int PROPERTY_UIClassID = 91;
    private static final int PROPERTY_valid = 92;
    private static final int PROPERTY_validateRoot = 93;
    private static final int PROPERTY_verifyInputWhenFocusTarget = 94;
    private static final int PROPERTY_vetoableChangeListeners = 95;
    private static final int PROPERTY_visible = 96;
    private static final int PROPERTY_visibleRect = 97;
    private static final int PROPERTY_width = 98;
    private static final int PROPERTY_x = 99;
    private static final int PROPERTY_y = 100;

    // Property array 
    /*lazy PropertyDescriptor*/
    private static PropertyDescriptor[] getPdescriptor(){
        PropertyDescriptor[] properties = new PropertyDescriptor[101];
    
        try {
            properties[PROPERTY_accessibleContext] = new PropertyDescriptor ( "accessibleContext", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getAccessibleContext", null ); // NOI18N
            properties[PROPERTY_actionListeners] = new PropertyDescriptor ( "actionListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getActionListeners", null ); // NOI18N
            properties[PROPERTY_actionMap] = new PropertyDescriptor ( "actionMap", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getActionMap", "setActionMap" ); // NOI18N
            properties[PROPERTY_alignmentX] = new PropertyDescriptor ( "alignmentX", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getAlignmentX", "setAlignmentX" ); // NOI18N
            properties[PROPERTY_alignmentY] = new PropertyDescriptor ( "alignmentY", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getAlignmentY", "setAlignmentY" ); // NOI18N
            properties[PROPERTY_ancestorListeners] = new PropertyDescriptor ( "ancestorListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getAncestorListeners", null ); // NOI18N
            properties[PROPERTY_autoscrolls] = new PropertyDescriptor ( "autoscrolls", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getAutoscrolls", "setAutoscrolls" ); // NOI18N
            properties[PROPERTY_background] = new PropertyDescriptor ( "background", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getBackground", "setBackground" ); // NOI18N
            properties[PROPERTY_backgroundSet] = new PropertyDescriptor ( "backgroundSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isBackgroundSet", null ); // NOI18N
            properties[PROPERTY_border] = new PropertyDescriptor ( "border", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getBorder", "setBorder" ); // NOI18N
            properties[PROPERTY_bounds] = new PropertyDescriptor ( "bounds", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getBounds", "setBounds" ); // NOI18N
            properties[PROPERTY_colorModel] = new PropertyDescriptor ( "colorModel", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getColorModel", null ); // NOI18N
            properties[PROPERTY_component] = new IndexedPropertyDescriptor ( "component", wts.models.DisMELS.gui.LHSSelector_JPanel.class, null, null, "getComponent", null ); // NOI18N
            properties[PROPERTY_componentCount] = new PropertyDescriptor ( "componentCount", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getComponentCount", null ); // NOI18N
            properties[PROPERTY_componentListeners] = new PropertyDescriptor ( "componentListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getComponentListeners", null ); // NOI18N
            properties[PROPERTY_componentOrientation] = new PropertyDescriptor ( "componentOrientation", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getComponentOrientation", "setComponentOrientation" ); // NOI18N
            properties[PROPERTY_componentPopupMenu] = new PropertyDescriptor ( "componentPopupMenu", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getComponentPopupMenu", "setComponentPopupMenu" ); // NOI18N
            properties[PROPERTY_components] = new PropertyDescriptor ( "components", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getComponents", null ); // NOI18N
            properties[PROPERTY_containerListeners] = new PropertyDescriptor ( "containerListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getContainerListeners", null ); // NOI18N
            properties[PROPERTY_cursor] = new PropertyDescriptor ( "cursor", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getCursor", "setCursor" ); // NOI18N
            properties[PROPERTY_cursorSet] = new PropertyDescriptor ( "cursorSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isCursorSet", null ); // NOI18N
            properties[PROPERTY_debugGraphicsOptions] = new PropertyDescriptor ( "debugGraphicsOptions", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getDebugGraphicsOptions", "setDebugGraphicsOptions" ); // NOI18N
            properties[PROPERTY_displayable] = new PropertyDescriptor ( "displayable", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isDisplayable", null ); // NOI18N
            properties[PROPERTY_doubleBuffered] = new PropertyDescriptor ( "doubleBuffered", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isDoubleBuffered", "setDoubleBuffered" ); // NOI18N
            properties[PROPERTY_dropTarget] = new PropertyDescriptor ( "dropTarget", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getDropTarget", "setDropTarget" ); // NOI18N
            properties[PROPERTY_enabled] = new PropertyDescriptor ( "enabled", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isEnabled", "setEnabled" ); // NOI18N
            properties[PROPERTY_enabled].setBound ( true );
            properties[PROPERTY_focusable] = new PropertyDescriptor ( "focusable", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusable", "setFocusable" ); // NOI18N
            properties[PROPERTY_focusCycleRoot] = new PropertyDescriptor ( "focusCycleRoot", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusCycleRoot", "setFocusCycleRoot" ); // NOI18N
            properties[PROPERTY_focusCycleRootAncestor] = new PropertyDescriptor ( "focusCycleRootAncestor", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getFocusCycleRootAncestor", null ); // NOI18N
            properties[PROPERTY_focusListeners] = new PropertyDescriptor ( "focusListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getFocusListeners", null ); // NOI18N
            properties[PROPERTY_focusOwner] = new PropertyDescriptor ( "focusOwner", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusOwner", null ); // NOI18N
            properties[PROPERTY_focusTraversable] = new PropertyDescriptor ( "focusTraversable", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusTraversable", null ); // NOI18N
            properties[PROPERTY_focusTraversalKeys] = new IndexedPropertyDescriptor ( "focusTraversalKeys", wts.models.DisMELS.gui.LHSSelector_JPanel.class, null, null, "getFocusTraversalKeys", "setFocusTraversalKeys" ); // NOI18N
            properties[PROPERTY_focusTraversalKeysEnabled] = new PropertyDescriptor ( "focusTraversalKeysEnabled", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getFocusTraversalKeysEnabled", "setFocusTraversalKeysEnabled" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicy] = new PropertyDescriptor ( "focusTraversalPolicy", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getFocusTraversalPolicy", "setFocusTraversalPolicy" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicyProvider] = new PropertyDescriptor ( "focusTraversalPolicyProvider", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusTraversalPolicyProvider", "setFocusTraversalPolicyProvider" ); // NOI18N
            properties[PROPERTY_focusTraversalPolicySet] = new PropertyDescriptor ( "focusTraversalPolicySet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFocusTraversalPolicySet", null ); // NOI18N
            properties[PROPERTY_font] = new PropertyDescriptor ( "font", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getFont", "setFont" ); // NOI18N
            properties[PROPERTY_fontSet] = new PropertyDescriptor ( "fontSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isFontSet", null ); // NOI18N
            properties[PROPERTY_foreground] = new PropertyDescriptor ( "foreground", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getForeground", "setForeground" ); // NOI18N
            properties[PROPERTY_foregroundSet] = new PropertyDescriptor ( "foregroundSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isForegroundSet", null ); // NOI18N
            properties[PROPERTY_graphics] = new PropertyDescriptor ( "graphics", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getGraphics", null ); // NOI18N
            properties[PROPERTY_graphicsConfiguration] = new PropertyDescriptor ( "graphicsConfiguration", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getGraphicsConfiguration", null ); // NOI18N
            properties[PROPERTY_height] = new PropertyDescriptor ( "height", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getHeight", null ); // NOI18N
            properties[PROPERTY_hierarchyBoundsListeners] = new PropertyDescriptor ( "hierarchyBoundsListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getHierarchyBoundsListeners", null ); // NOI18N
            properties[PROPERTY_hierarchyListeners] = new PropertyDescriptor ( "hierarchyListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getHierarchyListeners", null ); // NOI18N
            properties[PROPERTY_ignoreRepaint] = new PropertyDescriptor ( "ignoreRepaint", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getIgnoreRepaint", "setIgnoreRepaint" ); // NOI18N
            properties[PROPERTY_inheritsPopupMenu] = new PropertyDescriptor ( "inheritsPopupMenu", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInheritsPopupMenu", "setInheritsPopupMenu" ); // NOI18N
            properties[PROPERTY_inputContext] = new PropertyDescriptor ( "inputContext", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInputContext", null ); // NOI18N
            properties[PROPERTY_inputMap] = new PropertyDescriptor ( "inputMap", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInputMap", null ); // NOI18N
            properties[PROPERTY_inputMethodListeners] = new PropertyDescriptor ( "inputMethodListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInputMethodListeners", null ); // NOI18N
            properties[PROPERTY_inputMethodRequests] = new PropertyDescriptor ( "inputMethodRequests", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInputMethodRequests", null ); // NOI18N
            properties[PROPERTY_inputVerifier] = new PropertyDescriptor ( "inputVerifier", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInputVerifier", "setInputVerifier" ); // NOI18N
            properties[PROPERTY_insets] = new PropertyDescriptor ( "insets", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getInsets", null ); // NOI18N
            properties[PROPERTY_keyListeners] = new PropertyDescriptor ( "keyListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getKeyListeners", null ); // NOI18N
            properties[PROPERTY_layout] = new PropertyDescriptor ( "layout", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getLayout", "setLayout" ); // NOI18N
            properties[PROPERTY_lightweight] = new PropertyDescriptor ( "lightweight", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isLightweight", null ); // NOI18N
            properties[PROPERTY_locale] = new PropertyDescriptor ( "locale", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getLocale", "setLocale" ); // NOI18N
            properties[PROPERTY_location] = new PropertyDescriptor ( "location", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getLocation", "setLocation" ); // NOI18N
            properties[PROPERTY_locationOnScreen] = new PropertyDescriptor ( "locationOnScreen", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getLocationOnScreen", null ); // NOI18N
            properties[PROPERTY_managingFocus] = new PropertyDescriptor ( "managingFocus", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isManagingFocus", null ); // NOI18N
            properties[PROPERTY_maximumSize] = new PropertyDescriptor ( "maximumSize", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMaximumSize", "setMaximumSize" ); // NOI18N
            properties[PROPERTY_maximumSizeSet] = new PropertyDescriptor ( "maximumSizeSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isMaximumSizeSet", null ); // NOI18N
            properties[PROPERTY_minimumSize] = new PropertyDescriptor ( "minimumSize", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMinimumSize", "setMinimumSize" ); // NOI18N
            properties[PROPERTY_minimumSizeSet] = new PropertyDescriptor ( "minimumSizeSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isMinimumSizeSet", null ); // NOI18N
            properties[PROPERTY_mouseListeners] = new PropertyDescriptor ( "mouseListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMouseListeners", null ); // NOI18N
            properties[PROPERTY_mouseMotionListeners] = new PropertyDescriptor ( "mouseMotionListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMouseMotionListeners", null ); // NOI18N
            properties[PROPERTY_mousePosition] = new PropertyDescriptor ( "mousePosition", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMousePosition", null ); // NOI18N
            properties[PROPERTY_mouseWheelListeners] = new PropertyDescriptor ( "mouseWheelListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getMouseWheelListeners", null ); // NOI18N
            properties[PROPERTY_name] = new PropertyDescriptor ( "name", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getName", "setName" ); // NOI18N
            properties[PROPERTY_nextFocusableComponent] = new PropertyDescriptor ( "nextFocusableComponent", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getNextFocusableComponent", "setNextFocusableComponent" ); // NOI18N
            properties[PROPERTY_opaque] = new PropertyDescriptor ( "opaque", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isOpaque", "setOpaque" ); // NOI18N
            properties[PROPERTY_optimizedDrawingEnabled] = new PropertyDescriptor ( "optimizedDrawingEnabled", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isOptimizedDrawingEnabled", null ); // NOI18N
            properties[PROPERTY_paintingTile] = new PropertyDescriptor ( "paintingTile", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isPaintingTile", null ); // NOI18N
            properties[PROPERTY_parent] = new PropertyDescriptor ( "parent", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getParent", null ); // NOI18N
            properties[PROPERTY_peer] = new PropertyDescriptor ( "peer", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getPeer", null ); // NOI18N
            properties[PROPERTY_preferredSize] = new PropertyDescriptor ( "preferredSize", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getPreferredSize", "setPreferredSize" ); // NOI18N
            properties[PROPERTY_preferredSizeSet] = new PropertyDescriptor ( "preferredSizeSet", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isPreferredSizeSet", null ); // NOI18N
            properties[PROPERTY_propertyChangeListeners] = new PropertyDescriptor ( "propertyChangeListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getPropertyChangeListeners", null ); // NOI18N
            properties[PROPERTY_registeredKeyStrokes] = new PropertyDescriptor ( "registeredKeyStrokes", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getRegisteredKeyStrokes", null ); // NOI18N
            properties[PROPERTY_requestFocusEnabled] = new PropertyDescriptor ( "requestFocusEnabled", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isRequestFocusEnabled", "setRequestFocusEnabled" ); // NOI18N
            properties[PROPERTY_rootPane] = new PropertyDescriptor ( "rootPane", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getRootPane", null ); // NOI18N
            properties[PROPERTY_selectedName] = new PropertyDescriptor ( "selectedName", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getSelectedName", "setSelectedName" ); // NOI18N
            properties[PROPERTY_showing] = new PropertyDescriptor ( "showing", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isShowing", null ); // NOI18N
            properties[PROPERTY_size] = new PropertyDescriptor ( "size", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getSize", "setSize" ); // NOI18N
            properties[PROPERTY_toolkit] = new PropertyDescriptor ( "toolkit", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getToolkit", null ); // NOI18N
            properties[PROPERTY_toolTipText] = new PropertyDescriptor ( "toolTipText", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getToolTipText", "setToolTipText" ); // NOI18N
            properties[PROPERTY_topLevelAncestor] = new PropertyDescriptor ( "topLevelAncestor", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getTopLevelAncestor", null ); // NOI18N
            properties[PROPERTY_transferHandler] = new PropertyDescriptor ( "transferHandler", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getTransferHandler", "setTransferHandler" ); // NOI18N
            properties[PROPERTY_treeLock] = new PropertyDescriptor ( "treeLock", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getTreeLock", null ); // NOI18N
            properties[PROPERTY_UI] = new PropertyDescriptor ( "UI", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getUI", "setUI" ); // NOI18N
            properties[PROPERTY_UIClassID] = new PropertyDescriptor ( "UIClassID", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getUIClassID", null ); // NOI18N
            properties[PROPERTY_valid] = new PropertyDescriptor ( "valid", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isValid", null ); // NOI18N
            properties[PROPERTY_validateRoot] = new PropertyDescriptor ( "validateRoot", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isValidateRoot", null ); // NOI18N
            properties[PROPERTY_verifyInputWhenFocusTarget] = new PropertyDescriptor ( "verifyInputWhenFocusTarget", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getVerifyInputWhenFocusTarget", "setVerifyInputWhenFocusTarget" ); // NOI18N
            properties[PROPERTY_vetoableChangeListeners] = new PropertyDescriptor ( "vetoableChangeListeners", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getVetoableChangeListeners", null ); // NOI18N
            properties[PROPERTY_visible] = new PropertyDescriptor ( "visible", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "isVisible", "setVisible" ); // NOI18N
            properties[PROPERTY_visibleRect] = new PropertyDescriptor ( "visibleRect", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getVisibleRect", null ); // NOI18N
            properties[PROPERTY_width] = new PropertyDescriptor ( "width", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getWidth", null ); // NOI18N
            properties[PROPERTY_x] = new PropertyDescriptor ( "x", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getX", null ); // NOI18N
            properties[PROPERTY_y] = new PropertyDescriptor ( "y", wts.models.DisMELS.gui.LHSSelector_JPanel.class, "getY", null ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Properties
        
        // Here you can add code for customizing the properties array.
        
        return properties;     }//GEN-LAST:Properties
    
    // EventSet identifiers//GEN-FIRST:Events
    private static final int EVENT_actionListener = 0;
    private static final int EVENT_ancestorListener = 1;
    private static final int EVENT_componentListener = 2;
    private static final int EVENT_containerListener = 3;
    private static final int EVENT_focusListener = 4;
    private static final int EVENT_hierarchyBoundsListener = 5;
    private static final int EVENT_hierarchyListener = 6;
    private static final int EVENT_inputMethodListener = 7;
    private static final int EVENT_keyListener = 8;
    private static final int EVENT_mouseListener = 9;
    private static final int EVENT_mouseMotionListener = 10;
    private static final int EVENT_mouseWheelListener = 11;
    private static final int EVENT_propertyChangeListener = 12;
    private static final int EVENT_vetoableChangeListener = 13;

    // EventSet array
    /*lazy EventSetDescriptor*/
    private static EventSetDescriptor[] getEdescriptor(){
        EventSetDescriptor[] eventSets = new EventSetDescriptor[14];
    
        try {
            eventSets[EVENT_actionListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "actionListener", java.awt.event.ActionListener.class, new String[] {"actionPerformed"}, "addActionListener", "removeActionListener" ); // NOI18N
            eventSets[EVENT_ancestorListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "ancestorListener", javax.swing.event.AncestorListener.class, new String[] {"ancestorAdded", "ancestorMoved", "ancestorRemoved"}, "addAncestorListener", "removeAncestorListener" ); // NOI18N
            eventSets[EVENT_componentListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "componentListener", java.awt.event.ComponentListener.class, new String[] {"componentHidden", "componentMoved", "componentResized", "componentShown"}, "addComponentListener", "removeComponentListener" ); // NOI18N
            eventSets[EVENT_containerListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "containerListener", java.awt.event.ContainerListener.class, new String[] {"componentAdded", "componentRemoved"}, "addContainerListener", "removeContainerListener" ); // NOI18N
            eventSets[EVENT_focusListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "focusListener", java.awt.event.FocusListener.class, new String[] {"focusGained", "focusLost"}, "addFocusListener", "removeFocusListener" ); // NOI18N
            eventSets[EVENT_hierarchyBoundsListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "hierarchyBoundsListener", java.awt.event.HierarchyBoundsListener.class, new String[] {"ancestorMoved", "ancestorResized"}, "addHierarchyBoundsListener", "removeHierarchyBoundsListener" ); // NOI18N
            eventSets[EVENT_hierarchyListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "hierarchyListener", java.awt.event.HierarchyListener.class, new String[] {"hierarchyChanged"}, "addHierarchyListener", "removeHierarchyListener" ); // NOI18N
            eventSets[EVENT_inputMethodListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "inputMethodListener", java.awt.event.InputMethodListener.class, new String[] {"caretPositionChanged", "inputMethodTextChanged"}, "addInputMethodListener", "removeInputMethodListener" ); // NOI18N
            eventSets[EVENT_keyListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "keyListener", java.awt.event.KeyListener.class, new String[] {"keyPressed", "keyReleased", "keyTyped"}, "addKeyListener", "removeKeyListener" ); // NOI18N
            eventSets[EVENT_mouseListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "mouseListener", java.awt.event.MouseListener.class, new String[] {"mouseClicked", "mouseEntered", "mouseExited", "mousePressed", "mouseReleased"}, "addMouseListener", "removeMouseListener" ); // NOI18N
            eventSets[EVENT_mouseMotionListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "mouseMotionListener", java.awt.event.MouseMotionListener.class, new String[] {"mouseDragged", "mouseMoved"}, "addMouseMotionListener", "removeMouseMotionListener" ); // NOI18N
            eventSets[EVENT_mouseWheelListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "mouseWheelListener", java.awt.event.MouseWheelListener.class, new String[] {"mouseWheelMoved"}, "addMouseWheelListener", "removeMouseWheelListener" ); // NOI18N
            eventSets[EVENT_propertyChangeListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "propertyChangeListener", java.beans.PropertyChangeListener.class, new String[] {"propertyChange"}, "addPropertyChangeListener", "removePropertyChangeListener" ); // NOI18N
            eventSets[EVENT_vetoableChangeListener] = new EventSetDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class, "vetoableChangeListener", java.beans.VetoableChangeListener.class, new String[] {"vetoableChange"}, "addVetoableChangeListener", "removeVetoableChangeListener" ); // NOI18N
        }
        catch(IntrospectionException e) {
            e.printStackTrace();
        }//GEN-HEADEREND:Events
        
        // Here you can add code for customizing the event sets array.
        
        return eventSets;     }//GEN-LAST:Events
    
    // Method identifiers//GEN-FIRST:Methods
    private static final int METHOD_action0 = 0;
    private static final int METHOD_add1 = 1;
    private static final int METHOD_addNotify2 = 2;
    private static final int METHOD_addPropertyChangeListener3 = 3;
    private static final int METHOD_applyComponentOrientation4 = 4;
    private static final int METHOD_areFocusTraversalKeysSet5 = 5;
    private static final int METHOD_bounds6 = 6;
    private static final int METHOD_checkImage7 = 7;
    private static final int METHOD_computeVisibleRect8 = 8;
    private static final int METHOD_contains9 = 9;
    private static final int METHOD_countComponents10 = 10;
    private static final int METHOD_createImage11 = 11;
    private static final int METHOD_createToolTip12 = 12;
    private static final int METHOD_createVolatileImage13 = 13;
    private static final int METHOD_deliverEvent14 = 14;
    private static final int METHOD_disable15 = 15;
    private static final int METHOD_dispatchEvent16 = 16;
    private static final int METHOD_doLayout17 = 17;
    private static final int METHOD_enable18 = 18;
    private static final int METHOD_enableInputMethods19 = 19;
    private static final int METHOD_findComponentAt20 = 20;
    private static final int METHOD_firePropertyChange21 = 21;
    private static final int METHOD_getActionForKeyStroke22 = 22;
    private static final int METHOD_getBounds23 = 23;
    private static final int METHOD_getClientProperty24 = 24;
    private static final int METHOD_getComponentAt25 = 25;
    private static final int METHOD_getComponentZOrder26 = 26;
    private static final int METHOD_getConditionForKeyStroke27 = 27;
    private static final int METHOD_getDefaultLocale28 = 28;
    private static final int METHOD_getFontMetrics29 = 29;
    private static final int METHOD_getInsets30 = 30;
    private static final int METHOD_getListeners31 = 31;
    private static final int METHOD_getLocation32 = 32;
    private static final int METHOD_getMousePosition33 = 33;
    private static final int METHOD_getPopupLocation34 = 34;
    private static final int METHOD_getPropertyChangeListeners35 = 35;
    private static final int METHOD_getSize36 = 36;
    private static final int METHOD_getToolTipLocation37 = 37;
    private static final int METHOD_getToolTipText38 = 38;
    private static final int METHOD_gotFocus39 = 39;
    private static final int METHOD_grabFocus40 = 40;
    private static final int METHOD_handleEvent41 = 41;
    private static final int METHOD_hasFocus42 = 42;
    private static final int METHOD_hide43 = 43;
    private static final int METHOD_imageUpdate44 = 44;
    private static final int METHOD_initComponents145 = 45;
    private static final int METHOD_insets46 = 46;
    private static final int METHOD_inside47 = 47;
    private static final int METHOD_invalidate48 = 48;
    private static final int METHOD_isAncestorOf49 = 49;
    private static final int METHOD_isFocusCycleRoot50 = 50;
    private static final int METHOD_isLightweightComponent51 = 51;
    private static final int METHOD_keyDown52 = 52;
    private static final int METHOD_keyUp53 = 53;
    private static final int METHOD_layout54 = 54;
    private static final int METHOD_list55 = 55;
    private static final int METHOD_locate56 = 56;
    private static final int METHOD_location57 = 57;
    private static final int METHOD_lostFocus58 = 58;
    private static final int METHOD_minimumSize59 = 59;
    private static final int METHOD_mouseDown60 = 60;
    private static final int METHOD_mouseDrag61 = 61;
    private static final int METHOD_mouseEnter62 = 62;
    private static final int METHOD_mouseExit63 = 63;
    private static final int METHOD_mouseMove64 = 64;
    private static final int METHOD_mouseUp65 = 65;
    private static final int METHOD_move66 = 66;
    private static final int METHOD_nextFocus67 = 67;
    private static final int METHOD_paint68 = 68;
    private static final int METHOD_paintAll69 = 69;
    private static final int METHOD_paintComponents70 = 70;
    private static final int METHOD_paintImmediately71 = 71;
    private static final int METHOD_postEvent72 = 72;
    private static final int METHOD_preferredSize73 = 73;
    private static final int METHOD_prepareImage74 = 74;
    private static final int METHOD_print75 = 75;
    private static final int METHOD_printAll76 = 76;
    private static final int METHOD_printComponents77 = 77;
    private static final int METHOD_putClientProperty78 = 78;
    private static final int METHOD_registerKeyboardAction79 = 79;
    private static final int METHOD_remove80 = 80;
    private static final int METHOD_removeAll81 = 81;
    private static final int METHOD_removeNotify82 = 82;
    private static final int METHOD_removePropertyChangeListener83 = 83;
    private static final int METHOD_repaint84 = 84;
    private static final int METHOD_requestDefaultFocus85 = 85;
    private static final int METHOD_requestFocus86 = 86;
    private static final int METHOD_requestFocusInWindow87 = 87;
    private static final int METHOD_resetKeyboardActions88 = 88;
    private static final int METHOD_reshape89 = 89;
    private static final int METHOD_resize90 = 90;
    private static final int METHOD_revalidate91 = 91;
    private static final int METHOD_scrollRectToVisible92 = 92;
    private static final int METHOD_setBounds93 = 93;
    private static final int METHOD_setComponentZOrder94 = 94;
    private static final int METHOD_setDefaultLocale95 = 95;
    private static final int METHOD_show96 = 96;
    private static final int METHOD_size97 = 97;
    private static final int METHOD_toString98 = 98;
    private static final int METHOD_transferFocus99 = 99;
    private static final int METHOD_transferFocusBackward100 = 100;
    private static final int METHOD_transferFocusDownCycle101 = 101;
    private static final int METHOD_transferFocusUpCycle102 = 102;
    private static final int METHOD_unregisterKeyboardAction103 = 103;
    private static final int METHOD_update104 = 104;
    private static final int METHOD_updateUI105 = 105;
    private static final int METHOD_validate106 = 106;

    // Method array 
    /*lazy MethodDescriptor*/
    private static MethodDescriptor[] getMdescriptor(){
        MethodDescriptor[] methods = new MethodDescriptor[107];
    
        try {
            methods[METHOD_action0] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("action", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_action0].setDisplayName ( "" );
            methods[METHOD_add1] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("add", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_add1].setDisplayName ( "" );
            methods[METHOD_addNotify2] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("addNotify", new Class[] {})); // NOI18N
            methods[METHOD_addNotify2].setDisplayName ( "" );
            methods[METHOD_addPropertyChangeListener3] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("addPropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class})); // NOI18N
            methods[METHOD_addPropertyChangeListener3].setDisplayName ( "" );
            methods[METHOD_applyComponentOrientation4] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("applyComponentOrientation", new Class[] {java.awt.ComponentOrientation.class})); // NOI18N
            methods[METHOD_applyComponentOrientation4].setDisplayName ( "" );
            methods[METHOD_areFocusTraversalKeysSet5] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("areFocusTraversalKeysSet", new Class[] {Integer.TYPE})); // NOI18N
            methods[METHOD_areFocusTraversalKeysSet5].setDisplayName ( "" );
            methods[METHOD_bounds6] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("bounds", new Class[] {})); // NOI18N
            methods[METHOD_bounds6].setDisplayName ( "" );
            methods[METHOD_checkImage7] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("checkImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_checkImage7].setDisplayName ( "" );
            methods[METHOD_computeVisibleRect8] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("computeVisibleRect", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_computeVisibleRect8].setDisplayName ( "" );
            methods[METHOD_contains9] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("contains", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_contains9].setDisplayName ( "" );
            methods[METHOD_countComponents10] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("countComponents", new Class[] {})); // NOI18N
            methods[METHOD_countComponents10].setDisplayName ( "" );
            methods[METHOD_createImage11] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("createImage", new Class[] {java.awt.image.ImageProducer.class})); // NOI18N
            methods[METHOD_createImage11].setDisplayName ( "" );
            methods[METHOD_createToolTip12] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("createToolTip", new Class[] {})); // NOI18N
            methods[METHOD_createToolTip12].setDisplayName ( "" );
            methods[METHOD_createVolatileImage13] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("createVolatileImage", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_createVolatileImage13].setDisplayName ( "" );
            methods[METHOD_deliverEvent14] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("deliverEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_deliverEvent14].setDisplayName ( "" );
            methods[METHOD_disable15] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("disable", new Class[] {})); // NOI18N
            methods[METHOD_disable15].setDisplayName ( "" );
            methods[METHOD_dispatchEvent16] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("dispatchEvent", new Class[] {java.awt.AWTEvent.class})); // NOI18N
            methods[METHOD_dispatchEvent16].setDisplayName ( "" );
            methods[METHOD_doLayout17] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("doLayout", new Class[] {})); // NOI18N
            methods[METHOD_doLayout17].setDisplayName ( "" );
            methods[METHOD_enable18] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("enable", new Class[] {})); // NOI18N
            methods[METHOD_enable18].setDisplayName ( "" );
            methods[METHOD_enableInputMethods19] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("enableInputMethods", new Class[] {Boolean.TYPE})); // NOI18N
            methods[METHOD_enableInputMethods19].setDisplayName ( "" );
            methods[METHOD_findComponentAt20] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("findComponentAt", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_findComponentAt20].setDisplayName ( "" );
            methods[METHOD_firePropertyChange21] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("firePropertyChange", new Class[] {java.lang.String.class, Boolean.TYPE, Boolean.TYPE})); // NOI18N
            methods[METHOD_firePropertyChange21].setDisplayName ( "" );
            methods[METHOD_getActionForKeyStroke22] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getActionForKeyStroke", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_getActionForKeyStroke22].setDisplayName ( "" );
            methods[METHOD_getBounds23] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getBounds", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_getBounds23].setDisplayName ( "" );
            methods[METHOD_getClientProperty24] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getClientProperty", new Class[] {java.lang.Object.class})); // NOI18N
            methods[METHOD_getClientProperty24].setDisplayName ( "" );
            methods[METHOD_getComponentAt25] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getComponentAt", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_getComponentAt25].setDisplayName ( "" );
            methods[METHOD_getComponentZOrder26] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getComponentZOrder", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_getComponentZOrder26].setDisplayName ( "" );
            methods[METHOD_getConditionForKeyStroke27] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getConditionForKeyStroke", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_getConditionForKeyStroke27].setDisplayName ( "" );
            methods[METHOD_getDefaultLocale28] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getDefaultLocale", new Class[] {})); // NOI18N
            methods[METHOD_getDefaultLocale28].setDisplayName ( "" );
            methods[METHOD_getFontMetrics29] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getFontMetrics", new Class[] {java.awt.Font.class})); // NOI18N
            methods[METHOD_getFontMetrics29].setDisplayName ( "" );
            methods[METHOD_getInsets30] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getInsets", new Class[] {java.awt.Insets.class})); // NOI18N
            methods[METHOD_getInsets30].setDisplayName ( "" );
            methods[METHOD_getListeners31] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getListeners", new Class[] {java.lang.Class.class})); // NOI18N
            methods[METHOD_getListeners31].setDisplayName ( "" );
            methods[METHOD_getLocation32] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getLocation", new Class[] {java.awt.Point.class})); // NOI18N
            methods[METHOD_getLocation32].setDisplayName ( "" );
            methods[METHOD_getMousePosition33] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getMousePosition", new Class[] {Boolean.TYPE})); // NOI18N
            methods[METHOD_getMousePosition33].setDisplayName ( "" );
            methods[METHOD_getPopupLocation34] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getPopupLocation", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getPopupLocation34].setDisplayName ( "" );
            methods[METHOD_getPropertyChangeListeners35] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getPropertyChangeListeners", new Class[] {java.lang.String.class})); // NOI18N
            methods[METHOD_getPropertyChangeListeners35].setDisplayName ( "" );
            methods[METHOD_getSize36] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getSize", new Class[] {java.awt.Dimension.class})); // NOI18N
            methods[METHOD_getSize36].setDisplayName ( "" );
            methods[METHOD_getToolTipLocation37] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getToolTipLocation", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getToolTipLocation37].setDisplayName ( "" );
            methods[METHOD_getToolTipText38] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("getToolTipText", new Class[] {java.awt.event.MouseEvent.class})); // NOI18N
            methods[METHOD_getToolTipText38].setDisplayName ( "" );
            methods[METHOD_gotFocus39] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("gotFocus", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_gotFocus39].setDisplayName ( "" );
            methods[METHOD_grabFocus40] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("grabFocus", new Class[] {})); // NOI18N
            methods[METHOD_grabFocus40].setDisplayName ( "" );
            methods[METHOD_handleEvent41] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("handleEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_handleEvent41].setDisplayName ( "" );
            methods[METHOD_hasFocus42] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("hasFocus", new Class[] {})); // NOI18N
            methods[METHOD_hasFocus42].setDisplayName ( "" );
            methods[METHOD_hide43] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("hide", new Class[] {})); // NOI18N
            methods[METHOD_hide43].setDisplayName ( "" );
            methods[METHOD_imageUpdate44] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("imageUpdate", new Class[] {java.awt.Image.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_imageUpdate44].setDisplayName ( "" );
            methods[METHOD_initComponents145] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("initComponents1", new Class[] {})); // NOI18N
            methods[METHOD_initComponents145].setDisplayName ( "" );
            methods[METHOD_insets46] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("insets", new Class[] {})); // NOI18N
            methods[METHOD_insets46].setDisplayName ( "" );
            methods[METHOD_inside47] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("inside", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_inside47].setDisplayName ( "" );
            methods[METHOD_invalidate48] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("invalidate", new Class[] {})); // NOI18N
            methods[METHOD_invalidate48].setDisplayName ( "" );
            methods[METHOD_isAncestorOf49] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("isAncestorOf", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_isAncestorOf49].setDisplayName ( "" );
            methods[METHOD_isFocusCycleRoot50] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("isFocusCycleRoot", new Class[] {java.awt.Container.class})); // NOI18N
            methods[METHOD_isFocusCycleRoot50].setDisplayName ( "" );
            methods[METHOD_isLightweightComponent51] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("isLightweightComponent", new Class[] {java.awt.Component.class})); // NOI18N
            methods[METHOD_isLightweightComponent51].setDisplayName ( "" );
            methods[METHOD_keyDown52] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("keyDown", new Class[] {java.awt.Event.class, Integer.TYPE})); // NOI18N
            methods[METHOD_keyDown52].setDisplayName ( "" );
            methods[METHOD_keyUp53] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("keyUp", new Class[] {java.awt.Event.class, Integer.TYPE})); // NOI18N
            methods[METHOD_keyUp53].setDisplayName ( "" );
            methods[METHOD_layout54] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("layout", new Class[] {})); // NOI18N
            methods[METHOD_layout54].setDisplayName ( "" );
            methods[METHOD_list55] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("list", new Class[] {java.io.PrintStream.class, Integer.TYPE})); // NOI18N
            methods[METHOD_list55].setDisplayName ( "" );
            methods[METHOD_locate56] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("locate", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_locate56].setDisplayName ( "" );
            methods[METHOD_location57] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("location", new Class[] {})); // NOI18N
            methods[METHOD_location57].setDisplayName ( "" );
            methods[METHOD_lostFocus58] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("lostFocus", new Class[] {java.awt.Event.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_lostFocus58].setDisplayName ( "" );
            methods[METHOD_minimumSize59] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("minimumSize", new Class[] {})); // NOI18N
            methods[METHOD_minimumSize59].setDisplayName ( "" );
            methods[METHOD_mouseDown60] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseDown", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseDown60].setDisplayName ( "" );
            methods[METHOD_mouseDrag61] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseDrag", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseDrag61].setDisplayName ( "" );
            methods[METHOD_mouseEnter62] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseEnter", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseEnter62].setDisplayName ( "" );
            methods[METHOD_mouseExit63] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseExit", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseExit63].setDisplayName ( "" );
            methods[METHOD_mouseMove64] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseMove", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseMove64].setDisplayName ( "" );
            methods[METHOD_mouseUp65] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("mouseUp", new Class[] {java.awt.Event.class, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_mouseUp65].setDisplayName ( "" );
            methods[METHOD_move66] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("move", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_move66].setDisplayName ( "" );
            methods[METHOD_nextFocus67] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("nextFocus", new Class[] {})); // NOI18N
            methods[METHOD_nextFocus67].setDisplayName ( "" );
            methods[METHOD_paint68] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("paint", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paint68].setDisplayName ( "" );
            methods[METHOD_paintAll69] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("paintAll", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paintAll69].setDisplayName ( "" );
            methods[METHOD_paintComponents70] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("paintComponents", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_paintComponents70].setDisplayName ( "" );
            methods[METHOD_paintImmediately71] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("paintImmediately", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_paintImmediately71].setDisplayName ( "" );
            methods[METHOD_postEvent72] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("postEvent", new Class[] {java.awt.Event.class})); // NOI18N
            methods[METHOD_postEvent72].setDisplayName ( "" );
            methods[METHOD_preferredSize73] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("preferredSize", new Class[] {})); // NOI18N
            methods[METHOD_preferredSize73].setDisplayName ( "" );
            methods[METHOD_prepareImage74] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("prepareImage", new Class[] {java.awt.Image.class, java.awt.image.ImageObserver.class})); // NOI18N
            methods[METHOD_prepareImage74].setDisplayName ( "" );
            methods[METHOD_print75] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("print", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_print75].setDisplayName ( "" );
            methods[METHOD_printAll76] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("printAll", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_printAll76].setDisplayName ( "" );
            methods[METHOD_printComponents77] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("printComponents", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_printComponents77].setDisplayName ( "" );
            methods[METHOD_putClientProperty78] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("putClientProperty", new Class[] {java.lang.Object.class, java.lang.Object.class})); // NOI18N
            methods[METHOD_putClientProperty78].setDisplayName ( "" );
            methods[METHOD_registerKeyboardAction79] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("registerKeyboardAction", new Class[] {java.awt.event.ActionListener.class, java.lang.String.class, javax.swing.KeyStroke.class, Integer.TYPE})); // NOI18N
            methods[METHOD_registerKeyboardAction79].setDisplayName ( "" );
            methods[METHOD_remove80] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("remove", new Class[] {Integer.TYPE})); // NOI18N
            methods[METHOD_remove80].setDisplayName ( "" );
            methods[METHOD_removeAll81] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("removeAll", new Class[] {})); // NOI18N
            methods[METHOD_removeAll81].setDisplayName ( "" );
            methods[METHOD_removeNotify82] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("removeNotify", new Class[] {})); // NOI18N
            methods[METHOD_removeNotify82].setDisplayName ( "" );
            methods[METHOD_removePropertyChangeListener83] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("removePropertyChangeListener", new Class[] {java.lang.String.class, java.beans.PropertyChangeListener.class})); // NOI18N
            methods[METHOD_removePropertyChangeListener83].setDisplayName ( "" );
            methods[METHOD_repaint84] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("repaint", new Class[] {Long.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_repaint84].setDisplayName ( "" );
            methods[METHOD_requestDefaultFocus85] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("requestDefaultFocus", new Class[] {})); // NOI18N
            methods[METHOD_requestDefaultFocus85].setDisplayName ( "" );
            methods[METHOD_requestFocus86] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("requestFocus", new Class[] {})); // NOI18N
            methods[METHOD_requestFocus86].setDisplayName ( "" );
            methods[METHOD_requestFocusInWindow87] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("requestFocusInWindow", new Class[] {})); // NOI18N
            methods[METHOD_requestFocusInWindow87].setDisplayName ( "" );
            methods[METHOD_resetKeyboardActions88] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("resetKeyboardActions", new Class[] {})); // NOI18N
            methods[METHOD_resetKeyboardActions88].setDisplayName ( "" );
            methods[METHOD_reshape89] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("reshape", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_reshape89].setDisplayName ( "" );
            methods[METHOD_resize90] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("resize", new Class[] {Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_resize90].setDisplayName ( "" );
            methods[METHOD_revalidate91] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("revalidate", new Class[] {})); // NOI18N
            methods[METHOD_revalidate91].setDisplayName ( "" );
            methods[METHOD_scrollRectToVisible92] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("scrollRectToVisible", new Class[] {java.awt.Rectangle.class})); // NOI18N
            methods[METHOD_scrollRectToVisible92].setDisplayName ( "" );
            methods[METHOD_setBounds93] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("setBounds", new Class[] {Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE})); // NOI18N
            methods[METHOD_setBounds93].setDisplayName ( "" );
            methods[METHOD_setComponentZOrder94] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("setComponentZOrder", new Class[] {java.awt.Component.class, Integer.TYPE})); // NOI18N
            methods[METHOD_setComponentZOrder94].setDisplayName ( "" );
            methods[METHOD_setDefaultLocale95] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("setDefaultLocale", new Class[] {java.util.Locale.class})); // NOI18N
            methods[METHOD_setDefaultLocale95].setDisplayName ( "" );
            methods[METHOD_show96] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("show", new Class[] {})); // NOI18N
            methods[METHOD_show96].setDisplayName ( "" );
            methods[METHOD_size97] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("size", new Class[] {})); // NOI18N
            methods[METHOD_size97].setDisplayName ( "" );
            methods[METHOD_toString98] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("toString", new Class[] {})); // NOI18N
            methods[METHOD_toString98].setDisplayName ( "" );
            methods[METHOD_transferFocus99] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("transferFocus", new Class[] {})); // NOI18N
            methods[METHOD_transferFocus99].setDisplayName ( "" );
            methods[METHOD_transferFocusBackward100] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("transferFocusBackward", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusBackward100].setDisplayName ( "" );
            methods[METHOD_transferFocusDownCycle101] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("transferFocusDownCycle", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusDownCycle101].setDisplayName ( "" );
            methods[METHOD_transferFocusUpCycle102] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("transferFocusUpCycle", new Class[] {})); // NOI18N
            methods[METHOD_transferFocusUpCycle102].setDisplayName ( "" );
            methods[METHOD_unregisterKeyboardAction103] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("unregisterKeyboardAction", new Class[] {javax.swing.KeyStroke.class})); // NOI18N
            methods[METHOD_unregisterKeyboardAction103].setDisplayName ( "" );
            methods[METHOD_update104] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("update", new Class[] {java.awt.Graphics.class})); // NOI18N
            methods[METHOD_update104].setDisplayName ( "" );
            methods[METHOD_updateUI105] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("updateUI", new Class[] {})); // NOI18N
            methods[METHOD_updateUI105].setDisplayName ( "" );
            methods[METHOD_validate106] = new MethodDescriptor ( wts.models.DisMELS.gui.LHSSelector_JPanel.class.getMethod("validate", new Class[] {})); // NOI18N
            methods[METHOD_validate106].setDisplayName ( "" );
        }
        catch( Exception e) {}//GEN-HEADEREND:Methods
        
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
}

