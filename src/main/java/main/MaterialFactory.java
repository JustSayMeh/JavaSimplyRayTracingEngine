package main;

import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class MaterialFactory {
    public static Material createMaterial(HashMap<String, String> params){
        double diffusion = 0, specular = 0, mirror_coefficient = 0,
                mirror_ratio = 0, refractive_index = 0, refractive_albedo = 0;
        Scanner r = new Scanner(params.get("color"));
        r.useLocale(Locale.US);
        Vector3D color = new Vector3D(r.nextDouble(), r.nextDouble(), r.nextDouble());
        if (params.containsKey("diffusion"))
            diffusion = Double.parseDouble(params.get("diffusion"));
        if (params.containsKey("specular"))
            specular = Double.parseDouble(params.get("specular"));
        if (params.containsKey("mirror coefficient"))
            mirror_coefficient = Double.parseDouble(params.get("mirror coefficient"));
        if (params.containsKey("mirror ratio"))
            mirror_ratio = Double.parseDouble(params.get("mirror ratio"));
        if (params.containsKey("refractive index"))
            refractive_index = Double.parseDouble(params.get("refractive index"));
        if (params.containsKey("refractive albedo"))
            refractive_albedo = Double.parseDouble(params.get("refractive albedo"));

        return new Material(color, diffusion, specular, mirror_coefficient,
                mirror_ratio, refractive_index, refractive_albedo);
    }
}
