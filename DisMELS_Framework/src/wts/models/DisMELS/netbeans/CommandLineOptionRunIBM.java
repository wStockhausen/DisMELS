/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.netbeans;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.netbeans.api.sendopts.CommandException;
import org.netbeans.spi.sendopts.Env;
import org.netbeans.spi.sendopts.Option;
import org.netbeans.spi.sendopts.OptionProcessor;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author william.stockhausen
 */
@ServiceProvider(service=OptionProcessor.class)
public class CommandLineOptionRunIBM extends OptionProcessor {
    private final Option option1 = Option.requiredArgument(Option.NO_SHORT_NAME, "runIBM");
    private java.util.logging.Logger  logger = Logger.getLogger(CommandLineOptionRunIBM.class.getName());

    @Override
    protected Set<Option> getOptions() {
        Set<Option> set = new HashSet<>();
        set.add(option1);
        return set;
    }

    @Override
    protected void process(Env env, Map<Option,String[]> values) 
        throws CommandException {
        logger.info("--Starting to process commadline options");
        if (values.containsKey(option1)) {
              String[] fn = values.get(option1);
              logger.info("option runIBM was given as '"+fn[0]+"'.");
        }
        logger.info("--Finished processing commadline options");
    }
    
}
