/*
 * AlbersNAD83.java
 *
 * Created on June 27, 2006, 2:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.ct.MathTransform;

/**
 *
 * @author William Stockhausen
 */
public class AlbersNAD83 {
    
    private static GeographicCoordinateSystem gcs = null;
    private static ProjectedCoordinateSystem  pcs = null;
    private static MathTransform mt = null;
    private static MathTransform mtinv = null;
    
    /**
     * Creates a new instance of AlbersNAD83
     */
    private AlbersNAD83() {
    }
    
    public static GeographicCoordinateSystem getNAD83() {
        if (gcs==null) createObjects();
        return gcs;
    }
    
    public static ProjectedCoordinateSystem getAlbers() {
        if (gcs==null) createObjects();
        return pcs;
    }
    
    public static MathTransform getGtoPtransform() {
        if (gcs==null) createObjects();
        return mt;
    }
    
    public static MathTransform getPtoGtransform() {
        if (gcs==null) createObjects();
        return mtinv;
    }
    
    public static void createObjects() {
        try {
            gcs = CSCreator.createNAD_83();
            pcs = CSCreator.createAlbersPCS(CSCreator.NAD_83);
            mt  = CSCreator.getTransformation(gcs,pcs).getMathTransform();
            mtinv = mt.inverse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Transform a point from geographic (NAD83) to projected (Albers).  If the
     *  source point is 3d, the x,y coordinates are transformed and the z coordinate
     *  is simply copied.
     *  @param src - double array of source coordinates (2d or 3d)
     *  @return    - transformed (NAD83) point
     */
    public static double[] transformGtoP(double[] src){
        double[] dest = new double[src.length];
        transformGtoP(src,0,dest,0,1);
        if (src.length>2) dest[2] = src[2]; //copy z dimension
        return dest;
    }

    /**
     *  Transform a point from projected (Albers) to geographic (NAD83).  If the
     *  source point is 3d, the x,y coordinates are transformed and the z coordinate
     *  is simply copied.
     *  @param src - double array of source coordinates (2d or 3d)
     *  @return    - transformed (NAD83) point
     */
    public static double[] transformPtoG(double[] src){
        double[] dest = new double[src.length];
        transformPtoG(src,0,dest,0,1);
        if (src.length>2) dest[2] = src[2]; //copy z dimension
        return dest;
    }

    /**
     *  Transform points from geographic (NAD83) to projected (Albers).  Note
     *  that srcPts and dstPts can be the same array.  Dimension of points is
     *  determined by the coordinate systems on which the transform is based.
     *  I THINK the NAD83<->Albers CS's defined herein have dimension 2.  If you
     *  have 3d coordinates, the transform "works" if you only transform 1 point,
     *  but the destination z value is 0.
     *
     *  @param srcPts - double array of source (geo) points, with coordinates
     *                  packed: {x1,y1,z1,x2,y2,z2}
     *  @param i      - offset into srcPts array at which to start
     *  @param dstPts - double array of destination (projected) points
     *  @param i0     - offset into dstPts array at which to start recording results
     *  @param i1     - no. of points to transform
     */
    public static void transformGtoP(double[] srcPts, int i, double[] dstPts, int i0, int i1) {
        try {
            if (gcs==null) createObjects();
            mt.transform(srcPts,i,dstPts,i0,i1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Transform points from projected (Albers) to geographic (NAD83).  Note
     *  that srcPts and dstPts can be the same array.  Dimension of points is
     *  determined by the coordinate systems on which the transform is based.
     *  I THINK the NAD83<->Albers CS's defined herein have dimension 2.  If you
     *  have 3d coordinates, the transform "works" if you only transform 1 point,
     *  but the destination z value is 0.
     *
     *  @param srcPts - double array of source (geo) points, with coordinates
     *                  packed: {x1,y1,z1,x2,y2,z2}
     *  @param i      - offset into srcPts array at which to start
     *  @param dstPts - double array of destination (projected) points
     *  @param i0     - offset into dstPts array at which to start recording results
     *  @param i1     - no. of points to transform
     */
    public static void transformPtoG(double[] srcPts, int i, double[] dstPts, int i0, int i1) {
        try {
            if (gcs==null) createObjects();
            mtinv.transform(srcPts,i,dstPts,i0,i1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
