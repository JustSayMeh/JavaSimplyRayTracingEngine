package main;

import java.util.Scanner;

public class Vector3D {
    public double x, y, z, w;
    public Vector3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vector3D(Scanner r){
        x = r.nextDouble();
        y = r.nextDouble();
        z = r.nextDouble();
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public Vector3D mult(double a){return new Vector3D(x*a, y*a, z*a);}
    public Vector3D add(Vector3D a){
        return new Vector3D(x + a.x, y + a.y, z + a.z);
    }
    public Vector3D minus(Vector3D a){
        return new Vector3D(x - a.x, y - a.y, z - a.z);
    }
    public Vector3D cross(Vector3D a) {
        return new Vector3D(y * a.z - z*a.y, z*a.x - x*a.z, x*a.y - y*a.x);
    }

    public double len() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double scalar(Vector3D a) {
        return a.x*x + a.y*y + a.z*z;
    }

    public double getAngle(Vector3D a){
        if (a.len() == 0)
            return 0;
        return Math.acos(scalar(a) / (a.len() * len()));
    }

    public Vector3D normalize(){
        double ln = len();
        return new Vector3D(x/ln, y/ln, z/ln);
    }
    public String toString(){
        return "{" + x + ", " + y + ", " + z + " }";
    }
}
