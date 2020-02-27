/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSParametersEditor;

import com.wtstockhausen.utils.FileFilterImpl;
import com.wtstockhausen.utils.ReverseListModel;
import java.awt.BorderLayout;
import java.beans.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import wts.models.DisMELS.actions.Resetable;
import wts.models.DisMELS.actions.SaveAsable;
import wts.models.DisMELS.framework.*;
import wts.models.DisMELS.gui.LHSSelector_JPanel;
import wts.models.DisMELS.gui.LifeStageParametersCustomizer;

/**
 * Top component which displays something.
 */
//@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.LHSParametersEditor//LHSParametersEditor//EN",
//autostore = false)
//@TopComponent.Description(preferredID = "LHSParametersEditorTopComponent",
////iconBase="SET/PATH/TO/ICON/HERE", 
//persistenceType = TopComponent.PERSISTENCE_ALWAYS)
//@TopComponent.Registration(mode = "editor", openAtStartup = false)
//@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.LHSParametersEditor.LHSParametersEditorTopComponent")
//@ActionReference(path = "Menu/Window" /*
// * , position = 333
// */)
//@TopComponent.OpenActionRegistration(displayName = "#CTL_LHSParametersEditorAction",
//preferredID = "LHSParametersEditorTopComponent")
//@Messages({
//    "CTL_LHSParametersEditorAction=LHSParametersEditor",
//    "CTL_LHSParametersEditorTopComponent=LHS Parameters Editor",
//    "HINT_LHSParametersEditorTopComponent=The LHS Parameters Editor"
//})
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.LHSParametersEditor//LHSParametersEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "LHSParametersEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.LHSParametersEditor.LHSParametersEditorTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_LHSParametersEditorAction",
preferredID = "LHSParametersEditorTopComponent")
@Messages({
    "CTL_LHSParametersEditorAction=LHSParametersEditor",
    "CTL_LHSParametersEditorTopComponent=IBM Sub-stage Parameters Editor",
    "HINT_LHSParametersEditorTopComponent=The editor for IBM sub-stage parameter values"
})
public final class LHSParametersEditorTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static LHSParametersEditorTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "LHSParametersEditorTopComponent";

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized LHSParametersEditorTopComponent getDefault(){
        if (instance==null){
            instance = new LHSParametersEditorTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized LHSParametersEditorTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof LHSParametersEditorTopComponent) {
            return (LHSParametersEditorTopComponent) win;
        }
        Logger.getLogger(LHSParametersEditorTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    private LHSSelector_JPanel lhsSelector;
    private Map<String,LifeStageParametersInterface> paramsMap;
    private LifeStageParametersCustomizer paramsCustomizer;
    
    private String           fnIO = "";
    private JFileChooser     jfcIO = new JFileChooser();
    private JList            lstIO = new JList();
    private ReverseListModel rlmIO = new ReverseListModel();
    
    /** instance content reflecting FileLoader, FileSaver, and Resetter capabilities */
    private InstanceContent content;
    /** instance of the private class for saving the life stage parameters to csv or xml */
    private FileSaver fileSaver;
    /** instance of the private class for loading the life stage parameters from csv or xml */
    private FileLoader fileLoader;
    /** instance of the private class for reseting all parameter values */
    private Resetter resetter;
    
    /** logger for operating messages */
    private static final Logger logger = Logger.getLogger(LHSParametersEditorTopComponent.class.getName());

    public LHSParametersEditorTopComponent() {
        initComponents();
        initComponents1();
        setName(Bundle.CTL_LHSParametersEditorTopComponent());
        setToolTipText(Bundle.HINT_LHSParametersEditorTopComponent());
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        enableSaveAction(true);
        enableLoadAction(true);
        resetter = new Resetter();
        content.add(resetter);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpSelector = new javax.swing.JPanel();
        jpCustomizer = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jpSelector.setLayout(new java.awt.BorderLayout());
        add(jpSelector, java.awt.BorderLayout.NORTH);

        jpCustomizer.setLayout(new java.awt.BorderLayout());
        add(jpCustomizer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpCustomizer;
    private javax.swing.JPanel jpSelector;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
//        logger.info("starting compnonentOpened()");
        String wdFN = GlobalInfo.getInstance().getWorkingDir();
        File wdF = new File(wdFN);
        jfcIO.setCurrentDirectory(wdF);
        createParamsMap();//do this to "reset" parameters
        enableSaveAction(true);//TODO: enable discrimination
        if ((!fnIO.isEmpty())||(!fnIO.equals(""))){
            try {
                File f = new File(fnIO);
                if (fnIO.endsWith(".csv")||fnIO.endsWith(".CSV")) {
                    fileLoader.openCSV(f);
                } else
                if (fnIO.endsWith(".xml")||fnIO.endsWith(".XML")) {
                    fileLoader.openXML(f);
                }
                enableSaveAction(true);
            } catch (InstantiationException | IllegalAccessException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            String msg = "Loaded parameters file\n"+fnIO;
            String title = "Parameters info:";
            javax.swing.JOptionPane.showMessageDialog(null, msg, title, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
        enableLoadAction(true);
//        logger.info("finished compnonentOpened()");
    }

    @Override
    public void componentClosed() {
//        GlobalInfo.getInstance().removePropertyChangeListener(this);
//        LHS_Factory.removePropertyChangeListener(this);
    }
    
    private void initComponents1() {
        GlobalInfo.getInstance().addPropertyChangeListener(this);
        LHS_Factory.addPropertyChangeListener(this);

        fileSaver = new FileSaver();
        fileLoader = new FileLoader();
        
        lhsSelector = new LHSSelector_JPanel();
        lhsSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setParametersCustomizer();
            }
        });
        jpSelector.add(lhsSelector, java.awt.BorderLayout.NORTH);

        jfcIO.addChoosableFileFilter(new FileFilterImpl("csv","CSV files"));
        jfcIO.addChoosableFileFilter(new FileFilterImpl("xml","XML files"));
        
        rlmIO.setSize(4);
        lstIO.setModel(rlmIO);
        lstIO.setVisibleRowCount(4);
        lstIO.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstIO.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                csvListSelectionChanged(evt);
            }
        });
        
        paramsMap = new LinkedHashMap<>();
    }

    private void createParamsMap() {
        Set<String> keys = LHS_Types.getInstance().getKeys(); //LHS_Factory.getTypeNames();
        paramsMap.clear();
        try {
            for (String key: keys) {
                LifeStageParametersInterface p = LHS_Factory.createParameters(key);
                paramsMap.put(key,p);
            }
            setParametersCustomizer();
        } catch( InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void setParametersCustomizer() {
        jpCustomizer.removeAll();
        String key = lhsSelector.getSelectedName();
//        logger.info("setParametersCustomizer() for key: "+key);
        if (key!=null){
            LifeStageParametersInterface paramsObject = paramsMap.get(key);
            if (paramsObject==null){
//                logger.info("createParametersCustomizer(): params for '"+key+"' not defined!!");
                JTextArea jta = new JTextArea();
                jta.setText("\n\n\t\tNo customizer available for this life stage ('"+key+"'.\n\n\t\tLife stage name is probably not valid.");
                jpCustomizer.add(jta,BorderLayout.CENTER);
                paramsCustomizer = null;
            } else {
                paramsCustomizer = getParametersCustomizer(paramsObject);
                paramsCustomizer.setObject(paramsObject);
                jpCustomizer.add(paramsCustomizer,BorderLayout.CENTER);   
                jpCustomizer.validate();
            }
        }
        jpCustomizer.repaint();
    }
    
    private LifeStageParametersCustomizer getParametersCustomizer(LifeStageParametersInterface paramsObject) {
        LifeStageParametersCustomizer customizer;
        try {
            BeanInfo bi = Introspector.getBeanInfo(paramsObject.getClass());
            BeanDescriptor bd = bi.getBeanDescriptor();
            Class cClass = bd.getCustomizerClass();
            customizer = (LifeStageParametersCustomizer) cClass.newInstance();
        } catch (IntrospectionException|InstantiationException|IllegalAccessException|NullPointerException exc){
            customizer = new LifeStageParametersCustomizer();//use the generic version
        }
        return customizer;
    }

    public void csvListSelectionChanged(ListSelectionEvent evt) {
        try {
            fnIO = (String) lstIO.getSelectedValue();
            File f = new File(fnIO);
            jfcIO.setSelectedFile(f);
            rlmIO.addElement(fnIO); //this will set wdFN to first item
            lstIO.repaint();
            if (fnIO.endsWith(".csv")){
                paramsMap = LHS_Factory.createParametersFromCSV(f);
            } else {
                paramsMap = LHS_Factory.createParametersFromXML(f);
            }
            setParametersCustomizer();
        } catch (FileNotFoundException  | InstantiationException | IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        p.setProperty("LHSParametersCSVFilename",fnIO);
    }

    void readProperties(java.util.Properties p) {
//        logger.info("starting readProperties()");
        //get version
        String version = p.getProperty("version");
        
        //get mcb file name, if available
        String s = p.getProperty("LHSParametersCSVFilename");
        if (s!=null) fnIO = s;
        
//        logger.info("finished readProperties()");
    }

    /**
     * Method reacts to PropertyChanges fired by the GlobalInfo and LHS_Factory instances.
     * For a change in the working directory:
     *  sets the current directory in jfcCSV to the new working directory 
     *  resets the lhsSelector for the new lhs types
     *  resets the paramsMap for the new lhs types
     * 
     * @param evt - the PropertyChange event 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_WorkingDirFN)){
            logger.info("PropertyChange detected: GlobalInfo.PROP_WorkingDirFN");
            String wdFN = GlobalInfo.getInstance().getWorkingDir();
            File wdF = new File(wdFN);
            fnIO = "";
            jfcIO.setCurrentDirectory(wdF);
            lhsSelector.setEnabled(false);//turns off ActionEvents for lhs selection
            lhsSelector.updateTypeNames();
            createParamsMap();
            lhsSelector.setEnabled(true);//turns on ActionEvents for lhs selection
        } else if (evt.getPropertyName().equals(LHS_Factory.PROP_RESET)){
            logger.info("PropertyChange detected: LHS_Factory.PROP_RESET");
            fnIO = "";
            lhsSelector.setEnabled(false);//turns off ActionEvents for lhs selection
            lhsSelector.updateTypeNames();
            createParamsMap();
            lhsSelector.setEnabled(true);//turns on ActionEvents for lhs selection
        }
    }
    
    /**
     * Enables ability to load parameters from csv or xml file.
     * 
     * @param canLoad 
     */
    public void enableLoadAction(boolean canLoad){
        if (canLoad){
            content.add(fileLoader);//add an Openable implementation (CSVLoader) to the instance content
//            System.out.println("----Load enabled!!");
        } else {
            content.remove(fileLoader);//remove an Openable implementation (CSVLoader) from the instance content 
//            System.out.println("----Load disabled!!");
        }
    }
    
    /**
     * Enables ability to save parameters to csv or xml file.
     * 
     * @param canSave 
     */
    public void enableSaveAction(boolean canSave){
        if (canSave){
            content.add(fileSaver);//add a Savable implementation to the instance content
//            System.out.println("----Save enabled!!");
        } else {
            content.remove(fileSaver);//remove a Saveable implementation from the instance content 
//            System.out.println("----Save disabled!!");
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for the initial attributes.
     */
    private class FileLoader implements Openable {

        @Override
        public void open() {
//            logger.info("LHSParametersEditor.FileLoader.open()");
            try {
                jfcIO.setDialogTitle("Select file (csv or xml) to load LHS parameters from:");
                int res = jfcIO.showOpenDialog(instance);
                if (res==JFileChooser.APPROVE_OPTION){
                    File openFile = jfcIO.getSelectedFile();
                    String pfnIO = openFile.getPath();
                    if (pfnIO.endsWith(".csv")||pfnIO.endsWith(".csv")){
                        openCSV(openFile);
                    }
                    if (pfnIO.endsWith(".xml")||pfnIO.endsWith(".XML")){
                        openXML(openFile);
                    }
                }
            } catch (InstantiationException | IllegalAccessException | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        private void openCSV(File f) throws InstantiationException, IllegalAccessException, IOException{
            paramsMap = LHS_Factory.createParametersFromCSV(f);
            setParametersCustomizer();
            fnIO = f.getPath();
            rlmIO.addElement(fnIO);
        }

        private void openXML(File f) throws InstantiationException, IllegalAccessException, IOException{
            logger.info("Reading parameters from XML. Keys = ");
            paramsMap = LHS_Factory.createParametersFromXML(f);
            for (String key: paramsMap.keySet()) logger.info("--'"+key+"'.");
            logger.info("--Finished reading parameters from XML.");
            setParametersCustomizer();
            fnIO = f.getPath();
            rlmIO.addElement(fnIO);
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to implement save functionality for the life stage parameters.
     */
    private class FileSaver implements Savable, SaveAsable, ExceptionListener {

        @Override
        public void save() throws IOException {
            if (fnIO.isEmpty()) {
                saveAs();
            } else {
                File saveTo = new File(fnIO);
                if (fnIO.endsWith(".csv")||fnIO.endsWith(".csv")){
                    saveCSV(saveTo);
                } else 
                if (fnIO.endsWith(".xml")||fnIO.endsWith(".XML")){
                    saveXML(saveTo);
                }
            }
        }

        @Override
        public void saveAs() throws IOException {
            jfcIO.setDialogTitle("Select file (csv or xml) to save LHS parameters to:");
            int res = jfcIO.showSaveDialog(instance);
            if (res==JFileChooser.APPROVE_OPTION){
                File saveTo = jfcIO.getSelectedFile();
                fnIO = saveTo.getPath();
                rlmIO.addElement(fnIO);
                if (fnIO.endsWith(".csv")||fnIO.endsWith(".CSV")){
                    saveCSV(saveTo);
                } else 
                if (fnIO.endsWith(".xml")||fnIO.endsWith(".XML")){
                    saveXML(saveTo);
                }
            } else {
                //do nothing
            }
        }

        private void saveCSV(File f) throws IOException {
            LHSParametersWriter.writeParametersFile(f,paramsMap);
            StatusDisplayer.getDefault().setStatusText("Saved LHS parameters to file "+f.getName());
            enableSaveAction(true);//keep it enabled
        }
        
        private void saveXML(File f) throws IOException {
            try {
                FileOutputStream fos     = new FileOutputStream(f);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                java.beans.XMLEncoder xe = new java.beans.XMLEncoder(bos);
                xe.setOwner(this);
                xe.setExceptionListener(this);
                logger.info("Saving life stage parameters to xml");
                for (String key: paramsMap.keySet()) logger.info("--"+key+".");
                xe.writeObject(paramsMap);
                logger.info("SaveAs -- Done");
                //int i = egp.getComponentCount();
                xe.close();
                if (!f.getPath().equals(fnIO)) {
                    fnIO = f.getPath();
                    rlmIO.addElement(fnIO);
                }
            } catch (FileNotFoundException exc) {
                logger.info(exc.toString());
            }
        }

        @Override
        public void exceptionThrown(Exception e) {
            Exceptions.printStackTrace(e);
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement a "reset" capability for LifeStageParameters
     */
    private class Resetter implements Resetable {

        @Override
        public void reset() {
            LHS_Factory.resetParameters();
            createParamsMap();
        }        
    }
}
