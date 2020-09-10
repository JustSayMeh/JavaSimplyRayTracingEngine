package main;

public class Material {
    private Vector3D color;
    private double specular, mirror_ratio, diffusion_coefficient,
            mirror_reflection_coefficient, refractive_index,
    refract_albedo;
    private String name;

    public Material(Vector3D color,
                    double diffusion_coefficient,
                    double specular,
                    double mirror_reflection_coefficient,
                    double mirror_ratio,
                    double refractive_index
                    ) {
        this( color,
                diffusion_coefficient,
                specular,
                mirror_reflection_coefficient,
                mirror_ratio,
                refractive_index,
                0);
    }

    public Material(Vector3D color,
                    double diffusion_coefficient,
                    double specular,
                    double mirror_reflection_coefficient,
                    double mirror_ratio,
                    double refractive_index,
                    double refract_albedo
    ){
        this.color = color;
        this.specular = specular;
        this.mirror_ratio = mirror_ratio;
        this.diffusion_coefficient = diffusion_coefficient;
        this.mirror_reflection_coefficient = mirror_reflection_coefficient;
        this.refractive_index = refractive_index;
        this.refract_albedo = refract_albedo;
    }

    public Vector3D getColor() {
        return color;
    }

    public void setColor(Vector3D color) {
        this.color = color;
    }

    public double getSpecular() {
        return specular;
    }

    public void setSpecular(double specular) {
        this.specular = specular;
    }

    public double getMirror_ratio() {
        return mirror_ratio;
    }

    public void setMirror_ratio(double mirror_ratio) {
        this.mirror_ratio = mirror_ratio;
    }

    public double getDiffusion_coefficient() {
        return diffusion_coefficient;
    }

    public void setDiffusion_coefficient(double diffusion_coefficient) {
        this.diffusion_coefficient = diffusion_coefficient;
    }

    public double getMirror_reflection_coefficient() {
        return mirror_reflection_coefficient;
    }

    public void setMirror_reflection_coefficient(double mirror_reflection_coefficient) {
        this.mirror_reflection_coefficient = mirror_reflection_coefficient;
    }

    public double getRefractive_index() {
        return refractive_index;
    }

    public void setRefractive_index(double refractive_index) {
        this.refractive_index = refractive_index;
    }

    public double getRefract_albedo() {
        return refract_albedo;
    }

    public void setRefract_albedo(double refract_albedo) {
        this.refract_albedo = refract_albedo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
