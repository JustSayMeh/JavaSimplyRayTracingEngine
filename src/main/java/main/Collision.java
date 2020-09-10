package main;

public class Collision {
    private Vector3D N, hit;
    private double dist;
    private Material material;

    public Collision(Vector3D n, Vector3D hit, Material material, double dist) {
        N = n;
        this.hit = hit;
        this.dist = dist;
        this.material = material;
    }

    public Vector3D getHit() {
        return hit;
    }

    public void setHit(Vector3D hit) {
        this.hit = hit;
    }

    public Vector3D getN() {
        return N;
    }

    public void setN(Vector3D n) {
        N = n;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
