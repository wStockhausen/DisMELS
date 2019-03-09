/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wtstockhausen.utils;

import java.io.File;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

/**
 * Class with static methods to produce various lists of files.
 * 
 * @author WilliamStockhausen
 */
public class FilesLister {
    /**
     * Obtain a list of files with "similar" names to the given one in
     * the same folder. "Similar" here means with the same extension 
     * in the same folder.
     * 
     * @param fn - the file 
     * @return - String[] with names, including paths, for similar files
     */
    public static String[] findFilesWithSameExtension(String fn){
        File f = new File(fn);
        if (f.exists()&&f.isFile()){
            String ex = FilenameUtils.getExtension(fn);
            String dn = FilenameUtils.getFullPath(fn);
            File d = new File(dn);
            String[] fns = d.list();
            System.out.println("FilesLister--file   : '"+fn+"'.");
            System.out.println("FilesLister--folder : '"+dn+"'.");
            System.out.println("FilesLister--found files:");
            for (String fn1 : fns) System.out.println('\t' + fn1);
            IOFileFilter ff = new SuffixFileFilter(ex);
            fns = d.list(ff);
            System.out.println("FilesLister--ext    : '"+ex+"'.");
            System.out.println("FilesLister--found files with ext:");
            for (int i=0;i<fns.length;i++) {
                fns[i] = dn+fns[i];//add full path to folder to filename
                System.out.println('\t' + fns[i]);
            }
            Arrays.sort(fns);
            System.out.println("sorted files:");
            for (String fn1 : fns) System.out.println('\t' + fn1);
            return fns;
        }
        return null;
    }
}
