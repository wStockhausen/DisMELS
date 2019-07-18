/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;

/**
 *
 * @author William Stockhausen
 */
public interface HSM_Interface {
    
    /**
     * Calculate value of the HSM at a location.
     * 
     * @param posInfo - Object that contains the desired location
     * 
     * @return Object with the value of the HSM
     * 
     * @throws IOException, Exception
     */
    Object calcValue(Object posInfo) throws IOException, Exception;
    
    /**
     * Calculate smoothed value of the HSM at a location.
     * 
     * @param posInfo
     * @return Object with the value of the HSM
     * 
     * @throws IOException, Exception
     */
    Object interpolateValue(Object posInfo) throws IOException, Exception;
}
