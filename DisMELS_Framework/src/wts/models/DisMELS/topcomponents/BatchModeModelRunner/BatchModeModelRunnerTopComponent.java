/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.BatchModeModelRunner;

import com.wtstockhausen.datasource.CSVDataSource;
import com.wtstockhausen.utils.FileFilterImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.ExceptionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.LHS_Factory;
import wts.models.DisMELS.framework.ModelControllerBean;
import wts.models.DisMELS.framework.ModelTaskIF;
import wts.models.DisMELS.topcomponents.ModelRunner.ModelRunnerTopComponent;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.ObjectConverter;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.BatchModeModelRunner//BatchModeModelRunner//EN",
autostore = false)
@TopComponent.Description(preferredID = "BatchModeModelRunnerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.BatchModeModelRunner.BatchModeModelRunnerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_BatchModeModelRunnerAction",
preferredID = "BatchModeModelRunnerTopComponent")
@Messages({
    "CTL_BatchModeModelRunnerAction=Batch-mode model runner",
    "CTL_BatchModeModelRunnerTopComponent=Batch-Mode Model Runner",
    "HINT_BatchModeModelRunnerTopComponent=This is the Batch-Mode Model Runner window"
})
public final class BatchModeModelRunnerTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static BatchModeModelRunnerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "BatchModeModelRunnerTopComponent";

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized BatchModeModelRunnerTopComponent getDefault(){
        if (instance==null){
            instance = new BatchModeModelRunnerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized BatchModeModelRunnerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof ModelRunnerTopComponent) {
            return (BatchModeModelRunnerTopComponent) win;
        }
        Logger.getLogger(BatchModeModelRunnerTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** logger for operating messages */
    private static final Logger logger = Logger.getLogger(BatchModeModelRunnerTopComponent.class.getName());
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
    protected ModelControllerBean mcb;
    protected File batFile;
    
    protected transient JFileChooser jfcIO = new JFileChooser();
    
    private transient CSVDataSource csvDS;
    private transient int rowCnt;
    private transient int currRow;

    /** instance content reflecting csvLoader and csvSaver capabilities */
    private InstanceContent content;
    /** instance of the private class for loading the life stage parameters from csv or xml */
    private FileLoader fileLoader;
    /** instance of the private class for running the models */
    private ModelRunner modelRunner;
    /** instance of the private class for testing the batch run */
    private BatchRunTester modelTester;
    
    private boolean isBatchFileLoaded = false;
    private boolean isMCBFileLoaded   = false;
    
    private Timer timer;
    private ProgressHandle progressHandle;
    
    public BatchModeModelRunnerTopComponent() {
        initComponents();
        setName(Bundle.CTL_BatchModeModelRunnerTopComponent());
        setToolTipText(Bundle.HINT_BatchModeModelRunnerTopComponent());
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect FileLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        
        initComponents1();
    }
    
    private void initComponents1(){
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
        String wdFN = globalInfo.getWorkingDir();
        File wdF = new File(wdFN);
        
        jfcIO.setCurrentDirectory(wdF);
        jfcIO.addChoosableFileFilter(new FileFilterImpl("csv","batch file"));
        jfcIO.addChoosableFileFilter(new FileFilterImpl("xml","Model Controller file"));
        
        fileLoader = new FileLoader();
        modelRunner = new ModelRunner();
        modelTester = new BatchRunTester();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        csvDataTable = new com.wtstockhausen.beans.swing.JPanel_CSVDataTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mcbCustomizer = new wts.models.DisMELS.gui.ModelControllerBeanCustomizer();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(csvDataTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(BatchModeModelRunnerTopComponent.class, "BatchModeModelRunnerTopComponent.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel3.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setViewportView(mcbCustomizer);

        jPanel3.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(BatchModeModelRunnerTopComponent.class, "BatchModeModelRunnerTopComponent.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane2.setViewportView(jTextArea);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(BatchModeModelRunnerTopComponent.class, "BatchModeModelRunnerTopComponent.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.wtstockhausen.beans.swing.JPanel_CSVDataTable csvDataTable;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea;
    private wts.models.DisMELS.gui.ModelControllerBeanCustomizer mcbCustomizer;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        content.add(fileLoader);
        canEnableRunner();
        canEnableTester();
    }

    @Override
    public void componentClosed() {
        content.remove(fileLoader);
        content.remove(modelRunner);
        content.remove(modelTester);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void canEnableTester(){
        if (isBatchFileLoaded&&isMCBFileLoaded) {
            content.add(modelTester);
        } else {
            content.remove(modelTester);
        }
    }
    
    private void canEnableRunner(){
        if (isBatchFileLoaded&&isMCBFileLoaded) {
            content.add(modelRunner);
        } else {
            content.remove(modelRunner);
        }
    }
    
    /**
     * Method to test the batch loop.
     */
    public void testBatchLoop() {
        mcb.setGUIMode(false);//really only need to do this once
        if (currRow<rowCnt) {
            String colName = "";
            String type = "";
            String str = "";
            ObjectConverter oc = ObjectConverter.getInstance();
            String subDirFN = (String) csvDS.getValueAt(currRow, 0);
            str = "\nModel "+(currRow+1)+". Results in subfolder '"+subDirFN+"'.\n";
            jTextArea.append(str);
            boolean setResFN = true;
            for (int i=1;i<csvDS.getColumnCount();i++) {
                colName = csvDS.getColumnName(i);
                type = csvDS.getValueAt(currRow, i).toString();
                try {
                    if (colName.equalsIgnoreCase("startTime")) {
                        long strt = oc.to_long(csvDS.getValueAt(currRow, i));
                        mcb.setStartTime(strt);
                        CalendarIF cal = GlobalInfo.getInstance().getCalendar();
                        cal.setTimeOffset(strt);
                        str = "\t Start time = "+strt+" ("+cal.getDateTimeString()+")\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_ROMSDataset")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_ROMSDataset(fn);
                        str = "\t ROMS dataset      = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_Results")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_Results(fn);
                        setResFN = false;
                        str = "\t Results file      = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_ConnResults")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_ConnResults(fn);
                        setResFN = false;
                        str = "\t Connectivity file = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_Parameters")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_Params(fn);
                        str = "\t Parameters file   = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_InitialAttributes")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_InitialAttributes(fn);
                        str = "\t Init atts file    = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("ntEnvironModel")) {
                        int nt = oc.to_int(csvDS.getValueAt(currRow, i));
                        mcb.setNtEnvironModel(nt);
                        str = "\t Env model time steps = "+nt+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("ntBioModel")) {
                        int nt = oc.to_int(csvDS.getValueAt(currRow, i));
                        mcb.setNtBioModel(nt);
                        str = "\t Bio model time steps = "+nt+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("randomNumberSeed")) {
                        long rns = oc.to_long(csvDS.getValueAt(currRow, i));
                        mcb.setRandomNumberSeed(rns);
                        str = "\t Random number seed   = "+rns+"\n";
                        jTextArea.append(str);
                    }
                } catch (java.lang.NumberFormatException ex){
                    logger.info("Error formatting column '"+colName+"' with "+csvDS.getValueAt(currRow, i).toString()+" to type "+type);
                    throw ex;
                } catch (java.lang.ClassCastException ex){
                    logger.info("Error casting column '"+colName+"' with "+csvDS.getValueAt(currRow, i).toString()+" to type "+type);
                    throw ex;
                }
            }
            if (setResFN) {
                //results file NOT set in batch file, so create one in a NEW subdirectory using the base name                
                str = "\t Results file      = "+mcb.getFile_Results()+"\n";
                jTextArea.append(str);
                str = "\t Connectivity file = "+mcb.getFile_ConnResults()+"\n";
                jTextArea.append(str);
            }
            logger.info("\nModel "+(currRow+1)+". Results in "+subDirFN);
            logger.info("\tmcb.ocean_time start        = "+mcb.getStartTime());
            logger.info("\tmcb.ROMSDataset file        = "+mcb.getFile_ROMSDataset());
            logger.info("\tmcb.Parameters file         = "+mcb.getFile_Params());
            logger.info("\tmcb.Initial Attributes file = "+mcb.getFile_InitialAttributes());
            logger.info("\tmcb.Results file            = "+mcb.getFile_Results());
            logger.info("\tmcb.Connectivity file       = "+mcb.getFile_ConnResults());
            logger.info("\tmcb.num steps env. model    = "+mcb.getNtEnvironModel());
            logger.info("\tmcb.num steps bio model     = "+mcb.getNtBioModel());
            logger.info("\tmcb.Random number seed      = "+mcb.getRandomNumberSeed());
            currRow++;
            testBatchLoop();//recurse to pick up next row
        } else {
            JOptionPane.showMessageDialog(this, "Testing Batch run finished!!", "Good news!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Method to run the batch loop.
     */
    public void doBatchLoop() {
        mcb.setGUIMode(false);//really only need to do this once
        if (currRow<rowCnt) {
            LHS_Factory.resetID(0);
            String colName = "";
            String type = "";
            String str = "";
            ObjectConverter oc = ObjectConverter.getInstance();
            String subDirFN = (String) csvDS.getValueAt(currRow, 0);
            str = "\nModel "+(currRow+1)+". Results in subfolder '"+subDirFN+"'.\n";
            jTextArea.append(str);
            str = "";
            boolean setResFN = true;
            for (int i=1;i<csvDS.getColumnCount();i++) {
                colName = csvDS.getColumnName(i);
                type = csvDS.getValueAt(currRow, i).toString();
                try {
                    if (colName.equalsIgnoreCase("startTime")) {
                        long strt = oc.to_long(csvDS.getValueAt(currRow, i));
                        mcb.setStartTime(strt);
                        CalendarIF cal = GlobalInfo.getInstance().getCalendar();
                        cal.setTimeOffset(strt);
                        str = "\t Start time = "+strt+" ("+cal.getDateTimeString()+")\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_ROMSDataset")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_ROMSDataset(fn);
                        str = "\t ROMS dataset      = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_Results")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_Results(fn);
                        setResFN = false;
                        str = "\t Results file      = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_ConnResults")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_ConnResults(fn);
                        setResFN = false;
                        str = "\t Connectivity file = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_Parameters")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_Params(fn);
                        str = "\t Parameters file   = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("file_InitialAttributes")) {
                        String fn = (String) csvDS.getValueAt(currRow, i);
                        mcb.setFile_InitialAttributes(fn);
                        str = "\t Init atts file    = "+csvDS.getValueAt(currRow, i).toString()+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("ntEnvironModel")) {
                        int nt = oc.to_int(csvDS.getValueAt(currRow, i));
                        mcb.setNtEnvironModel(nt);
                        str = "\t Env model time steps = "+nt+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("ntBioModel")) {
                        int nt = oc.to_int(csvDS.getValueAt(currRow, i));
                        mcb.setNtBioModel(nt);
                        str = "\t Bio model time steps = "+nt+"\n";
                        jTextArea.append(str);
                    } else
                    if (colName.equalsIgnoreCase("randomNumberSeed")) {
                        long rns = oc.to_long(csvDS.getValueAt(currRow, i));
                        mcb.setRandomNumberSeed(rns);
                        str = "\t Random number seed   = "+rns+"\n";
                        jTextArea.append(str);
                    }
                } catch (java.lang.NumberFormatException ex){
                    logger.info("Error formatting column '"+colName+"' with "+csvDS.getValueAt(currRow, i).toString()+" to type "+type);
                    throw ex;
                } catch (java.lang.ClassCastException ex){
                    logger.info("Error casting column '"+colName+"' with "+csvDS.getValueAt(currRow, i).toString()+" to type "+type);
                    throw ex;
                }
            }
            if (setResFN) {
                //results file NOT set in batch file, so create one in a NEW subdirectory using the base name                
                String subDir = GlobalInfo.getInstance().getWorkingDir()+subDirFN+File.separator;
                new File(subDir).mkdir();//create subdirectory
                File f = new File(mcb.getFile_Results());
                String resFN = f.getName();//take only the file name as the base
                mcb.setFile_Results(subDirFN+File.separator+resFN);
                //and same for connectivity results file                
                File fc = new File(mcb.getFile_ConnResults());
                String resFNC = fc.getName();//take only the file name as the base
                mcb.setFile_ConnResults(subDirFN+File.separator+resFNC);
            }
            logger.info("\nModel "+(currRow+1)+". Results in "+subDirFN);
            logger.info("\tmcb.ocean_time start        = "+mcb.getStartTime());
            logger.info("\tmcb.ROMSDataset file        = "+mcb.getFile_ROMSDataset());
            logger.info("\tmcb.Parameters file         = "+mcb.getFile_Params());
            logger.info("\tmcb.Initial Attributes file = "+mcb.getFile_InitialAttributes());
            logger.info("\tmcb.Results file            = "+mcb.getFile_Results());
            logger.info("\tmcb.Connectivity file       = "+mcb.getFile_ConnResults());
            logger.info("\tmcb.num steps env. model    = "+mcb.getNtEnvironModel());
            logger.info("\tmcb.num steps bio model     = "+mcb.getNtBioModel());
            logger.info("\tmcb.Random number seed      = "+mcb.getRandomNumberSeed());
            currRow++;
            initializeModel();
        } else {
            JOptionPane.showMessageDialog(this, "Batch run finished!!", "Good news!", JOptionPane.INFORMATION_MESSAGE);
            mcb.cleanup();
        }
    }
    
    /**
     * Method called to initialize a DisMELS model run.
     */
    private void initializeModel(){
        logger.info("Initializing model");
        logger.info(mcb.getFile_ROMSDataset());
        logger.info(mcb.getFile_Params());
        logger.info(mcb.getFile_InitialAttributes());
        logger.info(mcb.getFile_Results());
        logger.info(mcb.getFile_ConnResults());
        logger.info("startTime = "+mcb.getStartTime());
        logger.info("nTimes    = "+mcb.getNtEnvironModel());
        logger.info("timeStep  = "+mcb.getTimeStep());
        logger.info("nLPTsteps = "+mcb.getNtBioModel());
        
        final ModelTaskIF task = mcb.getInitModelTask();
        progressHandle = ProgressHandleFactory.createHandle("Initializing model");        
        timer = new Timer(250,new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                progressHandle.setDisplayName("Initializing model: "+task.getMessage());
                progressHandle.progress(task.getMessage());
                if (task.isDone()) {
                    timer.stop();
                    progressHandle.finish();
                    runModel();//init finished, so run model
                }
            }
        });
        progressHandle.start();
        timer.start();
        task.go();
    }
    
    /**
     * Run the model.
     */
    private void runModel(){
        logger.info("Running model");
        final ModelTaskIF task = mcb.getRunModelTask();
        progressHandle = ProgressHandleFactory.createHandle("Running model");        
        timer = new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                progressHandle.setDisplayName("Running model: "+task.getMessage());
                progressHandle.progress(task.getMessage(),task.getCurrentValue());
                if (task.isDone()) {
                    timer.stop();
                    progressHandle.finish();
                    doBatchLoop();//do next model run
                }
            }
        });
        progressHandle.start(mcb.getNtEnvironModel()*mcb.getNtBioModel());;
        timer.start();
        task.go();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case GlobalInfo.PROP_WorkingDirFN:
                String wdFN = globalInfo.getWorkingDir();
                File wdF = new File(wdFN);
                jfcIO.setCurrentDirectory(wdF);
                break;
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for the batch file and Model Controller file.
     */
    private class FileLoader implements org.netbeans.api.actions.Openable, ExceptionListener {

        @Override
        public void open() {
            logger.info("open()");
            try {
                jfcIO.setDialogTitle("Select batch file (csv) or Model Controller file (xml) to load:");
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
            csvDS = new CSVDataSource(f);
            csvDS.setHeaderRowCount(0);
            int[] colTypes = new int[]{csvDS.TYPE_STRING,csvDS.TYPE_LONG,csvDS.TYPE_STRING};
            csvDS.readCSV(colTypes);
            rowCnt = csvDS.getRowCount();
            csvDataTable.setCSVDataSource(csvDS);
            jTabbedPane1.setSelectedIndex(0);
            isBatchFileLoaded = true;
            canEnableRunner();
            canEnableTester();
        }

        private void openXML(File f) throws InstantiationException, IllegalAccessException, IOException{
            FileInputStream fis     = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            XMLDecoder xd = new XMLDecoder(bis);
            xd.setOwner(this);
            xd.setExceptionListener(this);
            mcb = null;
            logger.info("loading ModelControllerBean");
            mcb = (ModelControllerBean) xd.readObject();
            logger.info("done loading MCB");
            xd.close();

            mcbCustomizer.setObject(mcb);
            mcbCustomizer.repaint();
            jTabbedPane1.setSelectedIndex(1);
            isMCBFileLoaded = true;
            canEnableRunner();
            canEnableTester();
        }

        /**
        * Method catches and prints the stack trace for exceptions, particularly those
        * thrown during reading/writing xml files.
        * 
        * @param e - the exception
        */
        @Override
        public void exceptionThrown(Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to run the batch.
     */
    private class ModelRunner implements wts.models.DisMELS.actions.Runnable {

        @Override
        public void run() {
            logger.info("run()");
            currRow = 0;
            jTextArea.setText("");
            doBatchLoop();
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to test the batch run.
     */
    private class BatchRunTester implements wts.models.DisMELS.actions.Testable {

        @Override
        public void test() {
            logger.info("test()");
            currRow = 0;
            jTextArea.setText("");
            testBatchLoop();
        }
    }

}
