package main.Shapes;

import main.Collision;
import main.Introspective;
import main.Material;
import main.Vector3D;

public class Polygon implements Introspective {
    private Vector3D[] vtx;
    private Vector3D N;
    private Material material;
    private double R;
    public Polygon(Vector3D vtx[], Material material){
        Vector3D l1 = vtx[0].minus(vtx[1]);
        Vector3D l2 = vtx[2].minus(vtx[1]);
        N = l1.cross(l2);
        R = -vtx[1].getX() * (l1.getY()*l2.getZ() - l1.getZ()*l2.getY())
                + vtx[1].getY() * (l1.getX()*l2.getZ() - l2.getX()*l1.getZ())
                - vtx[1].getZ() * (l1.getX()*l2.getY() - l2.getX()*l1.getY());
        R /= N.len();
        N = N.normalize();
        R = ray_intersect(new Vector3D(0,0,0), N.mult(-1));
        this.material = material;
        this.vtx = vtx;
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
        for (int i = 0; i < vtx.length; ++i)
        {
            Vector3D l1 = hit.minus(vtx[i]);
            Vector3D l2 = vtx[i].minus(vtx[(i + 1) % vtx.length]);
            if (l2.scalar(l1) > 0)
                return null;
        }
        return new Collision(N, hit, material, dist);
    }
}
