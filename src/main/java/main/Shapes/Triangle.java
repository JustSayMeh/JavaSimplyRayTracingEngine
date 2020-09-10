package main.Shapes;

import main.Collision;
import main.Introspective;
import main.Material;
import main.Vector3D;

public class Triangle implements Introspective {
    private Vector3D[] points = new Vector3D[3];
    private Vector3D N;
    private Material material;
    public  Triangle(Vector3D a, Vector3D b, Vector3D c, Material material){
        points[0] = a;
        points[1] = b;
        points[2] = c;
        N = b.minus(a).cross(c.minus(a)).normalize();
        this.material = material;
    }
    private double ray_intersect(Vector3D orig, Vector3D dir) {
        Vector3D E1 = points[1].minus(points[0]);
        Vector3D E2 = points[2].minus(points[0]);
        Vector3D T = orig.minus(points[0]);
        Vector3D P = dir.cross(E2);
        Vector3D Q = T.cross(E1);
        double det = P.scalar(E1);
        if (det < 1e-8)
            return -1;
        double u = P.scalar(T);
        if (u < 0 || u > det)
            return -1;
        double v = Q.scalar(dir);
        if (v < 0 || u + v > det)
            return -1;
        return Q.scalar(E2) / det;
    }

    @Override
    public Collision get_collision(Vector3D camera, Vector3D dir) {
        double dist = ray_intersect(camera, dir);
        if (dist < 0)
            return null;
        Vector3D hit = camera.add(dir.mult(dist));
        double gg = N.scalar(dir.mult(-1));
        if (gg < 0)
            return new Collision(N.mult(-1), hit, material, dist);
        else
            return new Collision(N, hit, material, dist);
    }
}
