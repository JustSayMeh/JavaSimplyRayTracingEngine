package main;

public class LightSource {
    private Vector3D position;
    private double force;
    public LightSource(Vector3D pos, double force){
        this.force = force;
        position = pos;
    }

    public double getForce() {
        return force;
    }

    public Vector3D getPosition() {
        return position;
    }

    public double calculateSpecular(Vector3D N, Vector3D L, Vector3D V, double a){
        double rs = 2 * N.scalar(L) * N.scalar(V) - V.scalar(L);
        if (rs <= 0)
            return 0;
        rs = Math.pow(rs, a)*force;
        return rs;
    }

    public double calculateDiffuse(Vector3D dir, Vector3D norm){
        return force * Math.max(0, dir.scalar(norm));
    }
}
