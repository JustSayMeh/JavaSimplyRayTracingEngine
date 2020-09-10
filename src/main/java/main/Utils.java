package main;

public class Utils {
    public static Vector3D getCenter(Vector3D[] vecs){
        Vector3D center = new Vector3D(0,0,0);
        for (int i = 0; i < vecs.length; ++i)
            center = center.add(vecs[i]);
        center = center.mult(1.0 / vecs.length);
        return center;
    }
    public static void rotateAroundCenterOfMass(double angle, Vector3D axis, Vector3D[] vecs){
        Vector3D center = new Vector3D(0,0,0);
        for (int i = 0; i < vecs.length; ++i)
            center = center.add(vecs[i]);
        center = center.mult(1.0 / vecs.length);
        for (int i = 0; i < vecs.length; ++i)
            vecs[i] = rotateAroundPoint(angle, axis, center, vecs[i]);
    }

    public static Vector3D rotateAroundPoint(double angle, Vector3D axis, Vector3D point, Vector3D vecs){
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double rcos = 1 - cos;
        double x = vecs.getX() - point.getX();
        double y = vecs.getY() - point.getY();
        double z = vecs.getZ() - point.getZ();
        return new Vector3D(
                (cos + axis.getX()*axis.getX()*rcos)*x +
                        (axis.getX()*axis.getY()*rcos - axis.getZ()*sin)*y +
                        (axis.getX()*axis.getZ()*rcos + axis.getY()*sin)*z + point.getX(),
                (axis.getY()*axis.getX()*rcos + axis.getZ()*sin)*x +
                        (cos + axis.getY()*axis.getY()*rcos)*y +
                        (axis.getY()*axis.getZ()*rcos - axis.getX()*sin)*z + point.getY(),
                (axis.getZ()*axis.getX()*rcos - axis.getY()*sin)*x +
                        (axis.getZ()*axis.getY()*rcos + axis.getX()*sin)*y +
                        (cos + axis.getZ()*axis.getZ()*rcos)*z + point.getZ()
        );
    }
}
