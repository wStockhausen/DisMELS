/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.GIS.styling;

import org.geotools.feature.Feature;
import org.geotools.filter.Expression;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;

/**
 * A FunctionExpression that encapsulates Math.pow(a,b);
 *
 * @author wstockhausen
 */
public class PowerFunction extends FunctionExpressionImpl
    implements FunctionExpression {

    /** The array of expressions to evaluate */
    private Expression[] args = new Expression[2];//{a,b}

    /**
     * Creates a new instance of PowerFunction
     * A FunctionExpression that encapsulates Math.pow(a,b);
     * TODO: should be protected and created using a FilterFactory
     * (which possibly means moving to org.geotools.filter somehow)
     */
    public PowerFunction() {
    }

    /**
     * Creates a new instance of PowerFunction
     * A FunctionExpression that encapsulates Math.pow(a,b);
     * TODO: should be protected and created using a FilterFactory
     * (which possibly means moving to org.geotools.filter somehow)
     */
    public PowerFunction(Expression a, Expression b) {
        args[0] = a;
        args[1] = b;
    }

    public Expression getA() {
        return args[0];
    }

    public void setA(Expression a) {
        args[0] = a;
    }

    public Expression getB() {
        return args[1];
    }

    public void setB(Expression b) {
        args[1] = b;
    }

    /**
     * Returns a value for this expression.
     * @param feature Specified feature to use when returning value.
     * @return Value of the feature object.
     */
    @Override
    public Object getValue(Feature feature) {
        Double val = null;
        try {
            val = new Double(1);
            Number na = ((Number) args[0].getValue(feature));
            Number nb = ((Number) args[1].getValue(feature));
            if ((na!=null)&&(nb!=null)){
                double a = na.doubleValue();
                double b = nb.doubleValue();
                val = new Double(Math.pow(a, b));
                //System.out.println("power("+a+","+b+") = "+val.toString());
            } else {
                System.out.println("error in power: null value detected");
                return null;
            }
            return val;
        } catch (java.lang.NullPointerException exc) {
            //should probably do something here
        }
        return val;
    }

    /**
     * Gets the number of arguments that are set.
     *
     * @return the number of args.
     */
    public int getArgCount() {
        return 2;
    }

    /**
     * Gets the name of this function.
     * @return the name of the function.
     */
    public String getName() {
        return "Power";
    }

    /**
     * Sets the arguments to be evaluated by this function.
     * @param args an array of expressions to be evaluated.
     */
    public void setArgs(Expression[] args) {
        this.args = args;
    }

    /**
     * Gets the arguments to be evaluated by this function.
     * @return an array of the args to be evaluated.
     */
    public Expression[] getArgs() {
        return args;
    }

    /**
     * Return this function as a string.
     * @return String representation of this max function.
     */
    @Override
    public String toString() {
        return "Power( " + args[0].toString() + ", " + args[1].toString() + ")";
    }
}
