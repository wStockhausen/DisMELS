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
        logger.info("called instantiate on "+oldInstance.toString());
        AbstractLHSParameters p = (AbstractLHSParameters) oldInstance;
        String typeName = p.getTypeName();
        Expression expr = new Expression(oldInstance,oldInstance.getClass(),"new",new Object[]{typeName});
        return expr;
    }
    
    @Override
    protected void initialize(Class type, Object oldInstance,Object newInstance, Encoder out){
        logger.info("called initialize on "+oldInstance.toString());
        LifeStageParametersInterface p = (LifeStageParametersInterface) oldInstance;
        Set<String> pKeys = p.getIBMParameterNames();
        for (String key: pKeys){
            IBMParameter i = p.getIBMParameter(key);
            Object o = i.getValue();
            out.writeStatement(new Statement(oldInstance,"setValue", new Object[]{key,o}));
        }
        Set<String> cats = p.getIBMFunctionCategories();
        for (String cat: cats){
            Set<String> fKeys = p.getIBMFunctionNamesByCategory(cat);
            IBMFunctionInterface sf = p.getSelectedIBMFunctionForCategory(cat);
            for (String fKey: fKeys){
                IBMFunctionInterface f = p.getIBMFunction(cat, fKey);
                out.writeStatement(new Statement(oldInstance,"setIBMFunction", new Object[]{cat,fKey,f}));
                if (sf==null) sf=f;//if no selected function, set selected function to first potential function
                if (sf.getFunctionName().equals(f.getFunctionName())) 
                    out.writeStatement(new Statement(oldInstance,"selectIBMFunctionForCategory",new Object[]{cat,fKey}));
            }
        }
    }
    
    @Override
    protected boolean mutatesTo(Object oldInstance, Object newInstance){
        logger.info("called mutatesTo on "+oldInstance.toString()+" and \n\t\t"+newInstance.toString());
        boolean b = super.mutatesTo(oldInstance, newInstance);
        logger.info("result = "+b);
        return b;
    }
    
}
