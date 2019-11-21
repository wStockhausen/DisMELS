/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.actions;

import java.io.IOException;

/**
 *
 * @author William.Stockhausen
 */
public interface SaveAsable {
    /**
     * Method called to implement "Save As..." functionality.
     */
    public void saveAs() throws IOException;
}
