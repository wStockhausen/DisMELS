/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.Statement;
import java.util.Set;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 *
 * @author William.Stockhausen
 */
public class AbstractLHSParametersPersistenceDelegate extends DefaultPersistenceDelegate {
    
    private static final Logger logger = Logger.getLogger(AbstractLHSParametersPersistenceDelegate.class.getName());
    
    @Override
    protected Expression instantiate(Object oldInstance, Encoder out){
        logger.info("Starting instantiate.");
        String oldstr = "NULL_INSTANCE";
        if (oldInstance!=null) oldstr = oldInstance.toString();
        logger.info("--called instantiate on "+oldstr);
        AbstractLHSParameters p = (AbstractLHSParameters) oldInstance;
        String typeName = p.getTypeName();
        Expression expr = new Expression(oldInstance,oldInstance.getClass(),"new",new Object[]{typeName});
        logger.info("--Finished instantiate.");
        return expr;
    }
    
    @Override
    protected void initialize(Class type, Object oldInstance,Object newInstance, Encoder out){
        logger.info("Starting initialize.");
        String oldstr = "NULL_INSTANCE";
        if (oldInstance!=null) oldstr = oldInstance.toString();
        logger.info("--called initialize on "+oldstr);
        LifeStageParametersInterface p = (LifeStageParametersInterface) oldInstance;
        Set<String> pKeys = p.getIBMParameterKeys();
        for (String key: pKeys){
            logger.info("--writing IBMParameter '"+key+"' to disk.");
            IBMParameter i = p.getIBMParameter(key);
            Object o = i.getValue();
            out.writeStatement(new Statement(oldInstance,"setValue", new Object[]{key,o}));
        }
        Set<String> cats = p.getIBMFunctionCategories();
        for (String cat: cats){
            logger.info("--writing IBMFunction category '"+cat+"' to disk.");
            Set<String> fKeys = p.getIBMFunctionKeysByCategory(cat);
            IBMFunctionInterface sf = p.getSelectedIBMFunctionForCategory(cat);
            for (String fKey: fKeys){
            logger.info("----writing IBMFunction '"+fKey+"' to disk.");
                IBMFunctionInterface f = p.getIBMFunction(cat, fKey);
                out.writeStatement(new Statement(oldInstance,"setIBMFunction", new Object[]{cat,fKey,f}));
                if (sf==null) sf=f;//if no selected function, set selected function to first potential function
                if (sf.getFunctionName().equals(f.getFunctionName())){
                    logger.info("------selected function for '"+cat+"' is '"+fKey+"'.");
                    out.writeStatement(new Statement(oldInstance,"setSelectedIBMFunctionForCategory",new Object[]{cat,fKey}));
                }
            }
        }
        logger.info("Finished initialize.");
    }
    
    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance){
        String oldstr = "NULL_INSTANCE";
        if (oldInstance!=null) oldstr = oldInstance.toString();
        String newstr = "NULL_INSTANCE";
        if (newInstance!=null) newstr = newInstance.toString();
        logger.info("called mutatesTo on "+oldstr+" and "+newstr);
        boolean b = super.mutatesTo(oldInstance, newInstance);
        logger.info("--result = "+b);
        return b;
    }
    
}
