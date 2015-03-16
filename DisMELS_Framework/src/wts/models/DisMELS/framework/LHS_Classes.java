/*
 * LHS_Classes.java
 *
 * Created on March 20, 2012.
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instances of this class provide information for the various classes (e.g., 
 * parameters, attributes) associated with each LHS class found in the Lookup.
 * 
 * @author William Stockhausen
 */
public class LHS_Classes {
    
    /** HashMap linking LHS types (the keys) to associated classes as LHS_ClassInfo objects (the values) */
    private final HashMap<String,LHS_ClassInfo> map = new HashMap<>();
    
    /** Creates a new instance of LHS_Classes */
    public LHS_Classes(Set<Class<? extends LifeStageInterface>> clazzes) {
        updateInfo(clazzes);
    }

    public final void updateInfo(Set<Class<? extends LifeStageInterface>> clazzes){
        LHS_ClassInfo lhs;
        try {
            for (Iterator<Class<? extends LifeStageInterface>> it = clazzes.iterator(); it.hasNext();) {
                Class<? extends LifeStageInterface> clazz = it.next();
                lhs = new LHS_ClassInfo(clazz);
                map.put(lhs.lhsClass, lhs);
            }
        } catch (Exception ex) {
            Logger.getLogger(LHS_Classes.class.getName()).log(Level.WARNING, null, ex);
        }
    }
    
    public HashMap<String,LHS_ClassInfo> getMap() {
        return map;
    }
    
    public Set<String> getKeys() {
        return map.keySet();
    }
    
    public LHS_ClassInfo getClassInfo(String className) {
        return map.get(className);
    }
    
    protected class LHS_ClassInfo {
        /** name of LHS class */
        public String lhsClass;
        /** name of associated LHS attributes class */
        public String attributesClass;
        /** name of associated LHS parameters class */
        public String parametersClass;
        /** name of associated LHS point feature type class */
        public String pointFTClass;
        /** name of associated LHS classes that can represent 'next' stages */
        public String[] nextLHSClasses;
        /** name of associated LHS classes that can be 'spawned' */
        public String[] spawnedLHSClasses;
        
        protected LHS_ClassInfo() {
        }
        
        protected LHS_ClassInfo(Class<? extends LifeStageInterface> lhsClass) {
            try {
                Field f;
                this.lhsClass = lhsClass.getName();
                /* get the attributes class name */
                f = lhsClass.getField("attributesClass");
                this.attributesClass = (String) f.get(null);
                /* get the parameters class name */
                f = lhsClass.getField("parametersClass");
                this.parametersClass = (String) f.get(null);
                /* get the point feature type class name and the associated class */
                f = lhsClass.getField("pointFTClass");
                this.pointFTClass = (String) f.get(null);
                /* get the potential 'next' LHS classes names and associated classes */
                f = lhsClass.getField("nextLHSClasses");
                this.nextLHSClasses = (String[]) f.get(null);
                /* get the spawned LHS class names and associated classes */
                f = lhsClass.getField("spawnedLHSClasses");
                this.spawnedLHSClasses = (String[]) f.get(null);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(LHS_Classes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        protected LHS_ClassInfo(String lhsClass,
                                String attributesClass,
                                String parametersClass,
                                String pointFTClass,
                                String nextLHSClass,
                                String spawnedLHSClass) {
            this.lhsClass        = lhsClass;
            this.attributesClass = attributesClass;
            this.parametersClass = parametersClass;
            this.pointFTClass    = pointFTClass;
            this.nextLHSClasses    = new String[]{nextLHSClass};            
            this.spawnedLHSClasses = new String[]{spawnedLHSClass};            
        }
        
        protected LHS_ClassInfo(String lhsClass,
                                String attributesClass,
                                String parametersClass,
                                String pointFTClass,
                                String[] nextLHSClasses,
                                String[] spawnedLHSClasses) {
            this.lhsClass          = lhsClass;
            this.attributesClass   = attributesClass;
            this.parametersClass   = parametersClass;
            this.pointFTClass      = pointFTClass;
            this.nextLHSClasses    = nextLHSClasses;            
            this.spawnedLHSClasses = spawnedLHSClasses;            
        }
    }
}
