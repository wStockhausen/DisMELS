/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author William.Stockhausen
 */
public class AbstractIBMFunctionPersistenceDelegate extends DefaultPersistenceDelegate {
    
    private static final Logger logger = Logger.getLogger(AbstractIBMFunctionPersistenceDelegate.class.getName());
    
    @Override
    protected Expression instantiate(Object oldInstance, Encoder out){
        logger.info("called instantiate on "+oldInstance.toString());
        IBMFunctionInterface f = (IBMFunctionInterface) oldInstance;
        Expression expr = new Expression(oldInstance,oldInstance.getClass(),"new",new Object[]{});
        return expr;
    }
    
    @Override
    protected void initialize(Class type, Object oldInstance,Object newInstance, Encoder out){
        logger.info("called initialize on "+oldInstance.toString());
        IBMFunctionInterface f = (IBMFunctionInterface) oldInstance;
        String fType = f.getFunctionType();
        out.writeStatement(new Statement(oldInstance,"setFunctionType", new Object[]{fType}));
        String fName = f.getFunctionName();
        out.writeStatement(new Statement(oldInstance,"setFunctionName", new Object[]{fName}));
        String fDescr = f.getDescription();
        out.writeStatement(new Statement(oldInstance,"setDescription", new Object[]{fDescr}));
        String fFullD = f.getFullDescription();
        out.writeStatement(new Statement(oldInstance,"setFullDescription", new Object[]{fFullD}));
        Set<String> pKeys = f.getParameterNames();
        for (String key: pKeys){
            IBMParameter i = f.getParameter(key);
            Object o = i.getValue();
            out.writeStatement(new Statement(oldInstance,"setParameterValue", new Object[]{key,o}));
            String d = i.getDescription();
            out.writeStatement(new Statement(oldInstance,"setParameterDescription", new Object[]{key,d}));
        }
        Set<String> subFuncs = f.getSubfunctionNames();
        for (String subFunc: subFuncs){
            IBMFunctionInterface sf = f.getSubfunction(subFunc);
            out.writeStatement(new Statement(oldInstance,"setSubfunction",new Object[]{subFunc,sf}));
        }
    }
    
    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance){
//        logger.info("called mutatesTo on "+oldInstance.toString());
        boolean b = super.mutatesTo(oldInstance, newInstance);
//        logger.info("result = "+b);
        return b;
    }
}
