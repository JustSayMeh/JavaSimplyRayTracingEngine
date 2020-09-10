package main;

public class Camera {
    private Vector3D pos, to, up;
    public Camera(Vector3D pos, Vector3D to, Vector3D up){
        this.pos = pos;
        this.to = to;
        this.up = up.normalize();
    }
    public Vector3D getPosition(){
        return pos;
    }
    public void setPosition(Vector3D pos){
        this.pos = pos;
    }

    public Vector3D getTo() {
        return to;
    }

    public void setTo(Vector3D to) {
        this.to = to;
    }

    public Vector3D getUp() {
        return up;
    }

    public void setUp(Vector3D up) {
        this.up = up;
    }
    public Vector3D getViewDir() {
        return to.minus(pos).normalize();
    }



}
