package main.Shapes;

import main.Collision;
import main.Introspective;
import main.Material;
import main.Vector3D;

public class Tetrahedron implements Introspective {
    private Triangle[] vtx;
    private Sphere sphere;
    private Material material;
    public Tetrahedron(Vector3D l1, Vector3D l2, Vector3D l3, Vector3D l4, Material material){
        vtx = new Triangle[]{
                new Triangle(l1, l3, l2, material),
                new Triangle(l1, l2, l4, material),
                new Triangle(l1, l4, l3, material),
                new Triangle(l2, l3,  l4, material)
        };
        Vector3D center = l1.add(l2).add(l3).add(l4).mult(1/4.0);
        double radius = 0;
        Vector3D pointers[] = new Vector3D[]{l1, l2, l3, l4};
        for (int i = 0; i < 4; ++i)
        {
            double r = pointers[i].minus(center).len();
            if (r > radius)
                radius = r;
        }
        sphere = new Sphere(center, radius, material);
        this.material = material;
    }


    @Override
    public Collision get_collision(Vector3D camera, Vector3D dir) {
        Collision collision = null;
        double dist = -1;
        if (sphere.get_collision(camera, dir) != null)
        {
            Collision lcollision = vtx[0].get_collision(camera, dir);

            for (int i = 0; i < 4; ++i)
            {
                lcollision = vtx[i].get_collision(camera, dir);
                if (lcollision != null && (lcollision.getDist() < dist || dist == -1))
                {
                    collision = lcollision;
                    dist = lcollision.getDist();
                }
            }
        }
        return collision;
    }
}
