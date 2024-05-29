/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wtstockhausen.utils;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * Class with static methods to produce various lists of files.
 * 
 * @author WilliamStockhausen
 */
public class FilesLister {
    /** flag to log debugging info */
    public static boolean debug=false;
    /** logger */
    private static final Logger logger = Logger.getLogger(FilesLister.class.getName());
    
    /**
     * Obtain a list of files with "similar" names to the given one in
     * the same folder. "Similar" here means with the same extension 
     * in the same folder.
     * 
     * @param fn - the file 
     * @return - String[] with names, including paths, for similar files
     * 
     * @throws java.io.FileNotFoundException - if fn is not a file
     */
    public static String[] findFilesWithSameExtension(String fn) throws java.io.FileNotFoundException {
        File f = new File(fn);
        if (f.exists()&&f.isFile()){
            String ex = FilenameUtils.getExtension(fn);
            String dn = FilenameUtils.getFullPath(fn);
            File d = new File(dn);
            String[] fns = d.list();
            if (debug){
                logger.info("FilesLister--file   : '"+fn+"'.");
                logger.info("FilesLister--folder : '"+dn+"'.");
                logger.info("FilesLister--found files:");
                for (String fn1 : fns) logger.info('\t' + fn1);
            }
            IOFileFilter ff = new SuffixFileFilter(ex);
            fns = d.list(ff);
            if (debug){
                logger.info("FilesLister--ext    : '"+ex+"'.");
                logger.info("FilesLister--found files with ext:");
            }
            for (int i=0;i<fns.length;i++) {
                fns[i] = dn+fns[i];//add full path to folder to filename
                if (debug) logger.info('\t' + fns[i]);
            }
            Arrays.sort(fns);
            if (debug){
                logger.info("sorted files:");
                for (String fn1 : fns) logger.info('\t' + fn1);
            }
            return fns;
        }
        logger.info("file '"+fn+"' DOES NOT EXIST!!");
        throw(new java.io.FileNotFoundException("FilesLister: file '"+fn+"' not found."));
    }
}
