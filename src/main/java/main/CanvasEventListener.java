package main;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.util.*;
import java.util.concurrent.CountDownLatch;


public class CanvasEventListener implements GLEventListener {
    private int Width, Height;
    private List<Introspective> spheres;
    private List<LightSource> lights;
    private Vector3D color;
    private int smooth = 1;
    private Camera camera;
    private boolean validate = false;
    public CanvasEventListener(Camera camera, List<Introspective> s, List<LightSource> l, Vector3D c){
        this.spheres = s;
        this.lights = l;
        this.color = c;
        this.camera = camera;
    }
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClearColor(0,0,0,1);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }
    private Vector3D refract(Vector3D I, Vector3D N, double n1, double n2){
        double n1dn2 = n1 / n2;
        double costetta1 = Math.max(-1, Math.min(1, I.mult(-1).scalar(N)));
        if (costetta1 < 0)
            return refract(I, N.mult(-1), n2, n1);
        double costetta2 = 1 - n1dn2*n1dn2 * (1 - costetta1*costetta1);
        return costetta2 < 0 ? new Vector3D(0,0,0):
                I.mult(n1dn2).add(N.mult(n1dn2*costetta1 - Math.sqrt(costetta2)));
    }

    private Collision scene_intersect(Vector3D camera, Vector3D dir){
        double dist = 500000;
        Collision rs = null;
        for (Introspective th : spheres){
            Collision lrs = th.get_collision(camera, dir);
            if (lrs != null && lrs.getDist() < dist) {
                dist = lrs.getDist();
                rs = lrs;
            }
        }
        return rs;
    }
    private Vector3D cast_ray(Vector3D camera, Vector3D dir, double depth){
        Collision rs = null;

        // рекурсия распространения лучей
        if (depth > 4 || (rs = scene_intersect(camera, dir)) == null)
            return color;

        Material material = rs.getMaterial();
        Vector3D hit = rs.getHit();
        Vector3D N = rs.getN();
        // Направление отраженного луча (Зеркало)
        Vector3D reflect_dir = N.mult(N.scalar(dir.mult(-1))).mult(2).minus(dir.mult(-1)).normalize();
        // Направление преломленного луча
        Vector3D refract_dir = refract(dir, N, 1, material.getRefractive_index());

        // смещение точки отражения
        Vector3D lhit = reflect_dir.scalar(N) < 0 ? hit.minus(N.mult(1e-3)): hit.add(N.mult(1e-3));
        // кастуем луч из точки смещения в направлении отраженного луча
        Vector3D colorM = cast_ray(lhit, reflect_dir, depth + 1);

        Vector3D colorR = new Vector3D(0,0,0);
        if (refract_dir.len() != 0)
        {
            lhit = refract_dir.scalar(N) < 0 ? hit.minus(N.mult(1e-3)): hit.add(N.mult(1e-3));
            // кастуем луч из точки смещения в направлении преломленного луча
            colorR = cast_ray(lhit, refract_dir.normalize(), depth + 1);
        }

        double diffuse = 0, specular = 0;
        // идем по источникам света
        for (LightSource th: lights){
            Vector3D lightpos = th.getPosition();
            // направление на источник света
            Vector3D ldir = lightpos.minus(hit).normalize();
            double distance = lightpos.minus(hit).len();
            Collision shadow_collision = null;
            Vector3D shadow_pointer = ldir.scalar(N) < 0 ? hit.minus(N.mult(1e-3)): hit.add(N.mult(1e-3));
            // рассчет теней. Если встречаем объект, то тень
            if ((shadow_collision = scene_intersect(shadow_pointer, ldir)) != null &&
                    shadow_collision.getHit().minus(shadow_pointer).len() < distance)
                continue;

            diffuse += th.calculateDiffuse(ldir, N);
            specular += th.calculateSpecular(N, ldir, dir.mult(-1), material.getMirror_reflection_coefficient());
        }
        return material.getColor()
                .mult(diffuse * material.getDiffusion_coefficient())
                .add(new Vector3D(1,1,1).mult(specular * material.getSpecular()))
                .add(colorM.mult(material.getMirror_ratio()))
                .add(colorR.mult(material.getRefract_albedo()));
    }
    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GLU glu = new GLU();
        if (validate)
            return;
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        gl.glLoadIdentity();
        gl.glPointSize(1);
        double ycoef = Math.tan(45/2.);
        double xcoef = ycoef * Width/(double)Height ;

        Vector3D n = camera.getViewDir();
        Vector3D u = camera.getUp().cross(n);
        Vector3D v = n.cross(u);

        gl.glBegin(GL2.GL_POINTS);
      //  ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < Width; ++i)
            for (int j = 0; j < Height; ++j) {
                gl.glColor3d(0,0,0);
                Vector3D color = new Vector3D(0,0,0);
                int lb = (i - smooth >= 0) ? i - smooth: i;
                int rb = (i + smooth < Width) ? i + smooth: i;
                int tb = (j - smooth >= 0) ? j - smooth: j;
                int bb = (j + smooth < Height) ? j + smooth: j;
                int count = (rb - lb + 1) * (bb - tb + 1);
                CountDownLatch barrier = new CountDownLatch(count);
                final Collection<Vector3D> list = Collections.synchronizedList(new LinkedList<>());
                for (int ii = lb; ii <= rb; ++ii){
                    for (int jj = tb; jj <= bb; ++jj)
                    {
                        final int  lii = ii;
                        final int  ljj = jj;
                        double x =  (2*(lii + 0.5)/(float)Width  - 1)*xcoef;
                        double y = -(2*(ljj + 0.5)/(float)Height - 1)*ycoef;
                        Vector3D dir = new Vector3D(x, y, 1).normalize();
//                        dir = Utils.rotateAroundPoint(Math.toRadians(0), new Vector3D(0,1,0), camera, dir)
//                                .normalize();
                        dir = new Vector3D(n.x + x*u.x + y*v.x,
                                n.y + x*u.y + y*v.y,
                                n.z + x*u.z + y*v.z).normalize();
                        Vector3D ccolor = cast_ray(camera.getPosition(), dir, 0);
                        list.add(ccolor);
                        barrier.countDown();
                    }
                }
                for (Vector3D c : list)
                    color = color.add(c);
                color = color.mult(1.0 / count);
                gl.glColor3d(color.getX(),color.getY(),color.getZ());
                gl.glVertex3i(i, j, 0);
            }
        gl.glEnd();
        validate = true;
    }
    public void invalidate(){
        validate = false;
    }
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int width, int height) {
        invalidate();
        GL2 gl = glAutoDrawable.getGL().getGL2();
        GLU glu = new GLU();
        Width = width;
        Height = height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluOrtho2D(0, width, height,0);
        //glu.gluPerspective(45, width/height, 0.1, 100);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }
}
