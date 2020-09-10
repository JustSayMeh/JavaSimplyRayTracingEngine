package main.Shapes;

import main.Collision;
import main.Introspective;
import main.Material;
import main.Vector3D;

public class Plane implements Introspective {

    private Vector3D[] points = new Vector3D[3];
    private Vector3D N;
    private Material material;
    private double R;
    public Plane(Vector3D n, double r, Material material){
        R = r;
        N = n.normalize();
        this.material = material;
    }
    private double ray_intersect(Vector3D orig, Vector3D dir) {
        double an = orig.scalar(N);
        double dn = dir.scalar(N);
        if (dn == 0)
            return -1;
        return (R - an) / dn;
    }

    @Override
    public Collision get_collision(Vector3D camera, Vector3D dir) {
        double dist = ray_intersect(camera, dir);
        if (dist < 0)
            return null;
        Vector3D hit = camera.add(dir.mult(dist));
        return new Collision(N, hit, material, dist);
    }
}
