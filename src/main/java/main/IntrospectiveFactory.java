package main;

import main.Shapes.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class IntrospectiveFactory {
    public static Introspective getIntrspective(String type,
                                                HashMap<String, String> mp,
                                                HashMap<String, Material> materials){
        Introspective rs = null;
        switch (type){
            case "Sphere":
                double radius = Double.parseDouble(mp.get("radius"));
                Scanner r = new Scanner(mp.get("position"));
                r.useLocale(Locale.US);
                Vector3D pos = new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                rs = new Sphere(pos, radius, materials.get(mp.get("material")));
                break;
            case "Plane":
                r = new Scanner(mp.get("up"));
                r.useLocale(Locale.US);
                double d = Double.parseDouble(mp.get("distance"));
                Vector3D up = new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                rs = new Plane(up, d, materials.get(mp.get("material")));
                break;
            case "Polygon":
                r = new Scanner(mp.get("position"));
                Vector3D l1= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                Vector3D l2= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                Vector3D l3= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                Vector3D l4= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                rs = new Polygon(new Vector3D[]{l1, l2, l3, l4}, materials.get(mp.get("material")));
                break;
            case "Tetrahedron":
                r = new Scanner(mp.get("position"));
                l1= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                l2= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                l3= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                l4= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                rs = new Tetrahedron(l1, l2, l3, l4, materials.get(mp.get("material")));
                break;
            case "Triangle":
                r = new Scanner(mp.get("position"));
                l1= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                l2= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                l3= new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
                rs = new Triangle(l1, l2, l3, materials.get(mp.get("material")));
                break;
        }
        return rs;
    }
}
