/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

/**
 *
 * @author William Stockhausen
 */
public interface HSMapInterface {
    
    /**
     * Gets the current connection string.
     * 
     * @return String: the connection string
     */
    String getConnectionString();
    
    /**
     * Set the connection to the HSM.
     * 
     * @param conn
     * 
     * @return boolean: true if connection was successfully set
     */
    boolean setConnectionString(String conn);
    
    /**
     * Calculate value of the HSM at position 'pos'.
     * 
     * @param pos
     * @return Object reflecting value(s) of HSM
     */
    Object calcValue(double[] pos);
    
    /**
     * Calculate value of the HSM at position 'pos' based on additional information
     * 'xtra'.
     * 
     * @param pos
     * @param xtra
     * @return Object reflecting value(s) of HSM
     */
    Object calcValue(double[] pos, Object xtra);
}
