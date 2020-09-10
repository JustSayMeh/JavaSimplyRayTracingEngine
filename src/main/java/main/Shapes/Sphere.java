package main.Shapes;

import main.Collision;
import main.Introspective;
import main.Material;
import main.Vector3D;

public class Sphere implements Introspective {
    private Vector3D position;
    private double radius;
    private Material material;

    public Sphere(Vector3D position, double radius, Material material){
        this.radius = radius;
        this.position = position;
        this.material = material;
    }
    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    private double ray_intersect(Vector3D orig, Vector3D dir) {
        Vector3D center = getPosition();
        double radius = getRadius();
        Vector3D L = center.minus(orig);
        double tca = L.scalar(dir);
        double d2 = L.scalar(L) - tca*tca;
        if (d2 > radius * radius) return -1;
        double thc = Math.sqrt(radius*radius - d2);
        double t0 = tca - thc;
        double t1 = tca + thc;
        if (t0 < 0) t0 = t1;
        return t0;
    }

    @Override
    public Collision get_collision(Vector3D orig, Vector3D dir) {
        double dist = ray_intersect(orig, dir);
        if (dist <= 0)
            return null;
        Vector3D lhit = orig.add(dir.mult(dist));
        Vector3D lN = lhit.minus(getPosition()).normalize();
        return new Collision( lN, lhit, material, dist);
    }
}
