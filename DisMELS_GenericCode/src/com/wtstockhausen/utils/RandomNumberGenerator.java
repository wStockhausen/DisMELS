/*
 * RandomNumberGenerator.java
 *
 * Created on January 16, 2004, 5:36 PM
 */

/**
 *
 * @author  William.Stockhausen
 */

package com.wtstockhausen.utils;

import java.beans.*;
import java.util.Random;
import cern.jet.random.Gamma;

public class RandomNumberGenerator extends Object implements java.io.Serializable {
    
    private static final String PROP_SEED = "Seed";
    
    private long seed = 0;
    private Random rndgen;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new RandomNumberGenerator */
    public RandomNumberGenerator() {
        propertySupport = new PropertyChangeSupport( this );
        rndgen = new Random();
    }
    
    /** Creates new RandomNumberGenerator */
    public RandomNumberGenerator(long seed) {
        propertySupport = new PropertyChangeSupport( this );
        setSeed(seed);
    }
    
    /* Generates a gamma-distributed random variable, 
     * where the gamma pdf is defined as
     *  p(x) = k * x^(alpha-1) * e^(-x/beta) 
     *         with k = 1/(g(alpha) * beta^alpha)) 
     *         and g(r) being the gamma function.
     */
    public double computeGammaVariate(double alpha, double beta) {
        double val =Gamma.staticNextDouble(alpha,beta);
        return val;
    }
    
    /* Generates a gamma-distributed random variable, 
     * where the gamma pdf is defined in the WinBUGS sense as
     *  p(x) = k * x^(r-1) * e^(-tau*x) 
     *         with k = tau^r/g(r) 
     *         and g(r) being the gamma function.
     */
    public double computeGammaVariateWB(double r, double tau) {
        double val = computeGammaVariate(r,1.0/tau);
        return val;
    }
    
    /*
     * Computes lognormal variate corresponding to the pdf
     *  p(x) = 1/sqrt(2*pi*sdev^2)*1/x*exp(-[(ln(x)-mean)^2]/(2*sdev^2))
     *      mean = mean of log-transformed variable
     *      sdev = std dev of log-transformed variable
     */
    public double computeLognormalVariate(double mean, double sdev) {
        double v = Math.exp(computeNormalVariate(mean, sdev));
        return v;
    }
    
    /*
     * Computes lognormal variate corresponding to the WinBUGS-sense pdf
     *  p(x) = sqrt(tau/(2*pi))*1/x*exp(-tau*[(ln(x)-mean)^2]/2)
     *      mean = mean of log-transformed variable
     *      tau = precision of log-transformed variable = 1/variance
     */
    public double computeLognormalVariateWB(double mean, double tau) {
        double rnd = computeNormalVariate(mean,1.0/Math.sqrt(tau));
        double val = Math.exp(rnd);
        return val;
    }
    
    public double computeNormalVariate() {
        return computeNormalVariate(0.0,1.0);
    }
    
    public double computeNormalVariate(double mean, double sdev) {
        double val = rndgen.nextGaussian()*sdev+mean;
        return val;
    }
    
    public double computeNormalVariateWB(double mean, double tau) {
        double val = rndgen.nextGaussian()/Math.sqrt(tau)+mean;
        return val;
    }
    
    public double computeUniformVariate(double min, double max) {
        double val = rndgen.nextDouble()*(max-min)+min;
        return val;
    }
    
    public long getSeed() {
        return seed;
    }
    
    public void setSeed(long value) {
        Long oldValue = value;
        seed = value;
        rndgen = new Random(seed);
        propertySupport.firePropertyChange(PROP_SEED, oldValue, seed);
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
