package main;

import main.Shapes.Plane;
import main.Shapes.Sphere;
import main.Shapes.Tetrahedron;
import main.Shapes.Triangle;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends JFrame {
    ArrayList<Introspective> shapes = new ArrayList<>();
    ArrayList<LightSource> lights = new ArrayList<>();
    PatternContainer patternContainer = new PatternContainer();
    HashMap<String, Material> materials = new HashMap<>();
    HashMap<String, Introspective> shapesMap = new HashMap<>();
    CanvasEventListener listener;
    final GLCanvas canvas;
    Camera camera = new Camera(
            new Vector3D(-30,30,-30),
            new Vector3D(3, -2,   40),
            new Vector3D(0,1,0));
    private void loadMaterialsFile(String fl) throws IOException {
        try(InputStream input = new FileInputStream(fl)){
            Scanner r = new Scanner(input);
            Pattern type = patternContainer.getPattern("type");
            Pattern params = patternContainer.getPattern("material parameters");
            HashMap<String, String> paramsMap = new HashMap<>();
            String h = r.nextLine();
            while (r.hasNextLine()) {
                paramsMap.clear();
                Matcher mt1 = type.matcher(h);
                mt1.find();
                String name = mt1.group(1);
                h = r.nextLine();
                Matcher mt2 = params.matcher(h);
                while(mt2.find()){
                    int i = 1;
                    while(mt2.group(i) == null)
                        i += 1;
                    paramsMap.put(mt2.group(i + 1), mt2.group(i + 2));
                    if (!r.hasNextLine())
                        break;
                    while(r.hasNextLine() && (h = r.nextLine()).length() == 0)
                        ;
                    mt2 = params.matcher(h);
                }
                Material material = MaterialFactory.createMaterial(paramsMap);
                material.setName(name);
                materials.put(name, material);
            }
        }
    }
    private void loadShapesFile(String fl) throws IOException {
        try(InputStream input = new FileInputStream(fl)){
            Scanner r = new Scanner(input);
            Pattern type = patternContainer.getPattern("type");
            HashMap<String, String> paramsMap = new HashMap<>();
            String h = r.nextLine();
            while (r.hasNextLine()) {
                Matcher mt1 = type.matcher(h);
                mt1.find();
                String name = mt1.group(1);
                Pattern params = patternContainer.getPattern(name);
                h = r.nextLine();
                Matcher mt2 = params.matcher(h);
                while(mt2.find()){
                    int i = 1;
                    while(mt2.group(i) == null)
                        i += 1;
                    paramsMap.put(mt2.group(i + 1), mt2.group(i + 2));
                    if (!r.hasNextLine())
                        break;
                    while(r.hasNextLine() && (h = r.nextLine()).length() == 0)
                        ;
                    mt2 = params.matcher(h);
                }
                Introspective shape = IntrospectiveFactory.getIntrspective(name, paramsMap, materials);
                shapesMap.put(paramsMap.get("name"), shape);
            }
        }
    }
    public static void main(String ...args) throws IOException {
        new Main();
    }
    public Main() throws IOException {
        patternContainer.addPattern("type", "\\[(\\w+)\\]");
        patternContainer.addPattern("Sphere",
                "((position):\\s*-?((\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)))|" +
                "((radius):\\s*((\\d+\\.?\\d*)))|" +
                "((material):\\s*(\\w+))|" +
                "((name):\\s*(\\w+))");

        patternContainer.addPattern("Plane",
                "((up):\\s*-?((\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)))|" +
                        "((distance):\\s*(-?(\\d+\\.?\\d*)))|" +
                        "((material):\\s*(\\w+))|" +
                        "((name):\\s*(\\w+))");
        patternContainer.addPattern("Triangle",
                "((position):\\s*-?(-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)))|" +
                        "((material):\\s*(\\w+))|" +
                        "((name):\\s*(\\w+))");
        patternContainer.addPattern("Tetrahedron",
                "((position):\\s*(-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)))|" +
                        "((material):\\s*(\\w+))|" +
                        "((name):\\s*(\\w+))");
        patternContainer.addPattern("Polygon",
                "((position):\\s*(-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)" +
                        "\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)\\s+-?(\\d+\\.?\\d*)))|" +
                        "((material):\\s*(\\w+))|" +
                        "((name):\\s*(\\w+))");
        patternContainer.addPattern("material parameters",
                "((color):\\s*((\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)\\s+(\\d+\\.?\\d*)))|" +
                "((diffusion):\\s*((\\d+\\.?\\d*)))|" +
                "((specular):\\s*((\\d+\\.?\\d*)))|" +
                "((mirror coefficient):\\s*((\\d+\\.?\\d*)))|" +
                "((mirror ratio):\\s*((\\d+\\.?\\d*)))|" +
                "((refractive index):\\s*((\\d+\\.?\\d*)))|" +
                "((refractive albedo):\\s*((\\d+\\.?\\d*)))");


        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setDoubleBuffered(true);
        canvas = new GLCanvas(capabilities);


        lights.add(new LightSource(new Vector3D(-20,20,-25), 1.5));
        lights.add(new LightSource(new Vector3D(30,30,25), 1.8));
        lights.add(new LightSource(new Vector3D(0,30,60), 1.8));


        listener = new CanvasEventListener(camera, shapes, lights, new Vector3D(0,0,0));

        canvas.addGLEventListener(listener);
        setSize(560, 480);
        add(canvas);
        JMenuBar menu = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu lists = new JMenu("Списки");
        JMenuItem light = new JMenuItem("Источники света");

        JMenuItem addShapes = new JMenuItem("Загрузить Фигуры...");
        addShapes.setEnabled(false);
        addShapes.addActionListener((e) -> {
            final JFileChooser fc = new JFileChooser();
            int rv = fc.showOpenDialog(this);
            if (rv == JFileChooser.APPROVE_OPTION){
                File choosefile = fc.getSelectedFile();
                try {
                    shapesMap.clear();
                    loadShapesFile(choosefile.getAbsoluteFile().getAbsolutePath());
                    shapes.clear();
                    shapes.addAll(shapesMap.values());
                    listener.invalidate();
                    canvas.repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem addMaterials = new JMenuItem("Загрузить Материалы...");
        addMaterials.addActionListener((e) -> {
            final JFileChooser fc = new JFileChooser();
            int rv = fc.showOpenDialog(this);
            if (rv == JFileChooser.APPROVE_OPTION){
                File choosefile = fc.getSelectedFile();
                try {
                    materials.clear();
                    loadMaterialsFile(choosefile.getAbsoluteFile().getAbsolutePath());
                    addShapes.setEnabled(true);

                    listener.invalidate();
                    canvas.repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuItem cameraS = new JMenuItem("Камера");

        light.addActionListener((e) -> {
            createLighstsDialog();
        });
        cameraS.addActionListener((e) -> {
            createCameraDialog();
        });

        JMenuItem materialsJ = new JMenuItem("Материалы");
        JMenuItem shapesJ = new JMenuItem("Фигуры");
        materialsJ.addActionListener((e) -> {
            createMaterialsDialog();
        });
        shapesJ.addActionListener((e) -> {
            createShapesDialog();
        });
        lists.add(materialsJ);
        lists.add(shapesJ);
        file.add(addShapes);
        file.add(addMaterials);

        edit.add(cameraS);
        edit.add(light);
        menu.add(file);
        menu.add(edit);
        menu.add(lists);

        setJMenuBar(menu);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private JDialog createdialog(String title, boolean modal){
        JDialog diag = new JDialog(this, title, modal);
        diag.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return diag;
    }
    private void createShapesDialog(){
        JDialog diag = createdialog("Фигуры", true);
        diag.setSize(640, 210);
        JPanel Pane = new JPanel();
        Pane.setLayout(new BoxLayout(Pane, BoxLayout.Y_AXIS));
        HashMap<String, JCheckBox> boxes = new HashMap<>();
        for (String th : shapesMap.keySet()){
            Introspective kth = shapesMap.get(th);
            JPanel materialPan = new JPanel();
            materialPan.setBorder(BorderFactory.createLineBorder(Color.black));
            materialPan.setLayout(new GridLayout(1, 4));

            JCheckBox box = new JCheckBox("");

            JLabel nameL = new JLabel("Name");
            JLabel nameR = new JLabel(th);
            materialPan.add(nameL);
            materialPan.add(nameR);
            if (shapes.contains(kth))
                box.setSelected(true);
            else
                box.setSelected(false);
            boxes.put(th, box);
            materialPan.add(box);

            Pane.add(materialPan);
        }
        Button saveb = new Button("Сохранить");
        saveb.setBackground(Color.LIGHT_GRAY);
        saveb.addActionListener((e) -> {
            shapes.clear();
            for (String th : boxes.keySet()){
                if (boxes.get(th).isSelected())
                    shapes.add(shapesMap.get(th));
            }
            listener.invalidate();
            canvas.repaint();
        });

        JScrollPane scrollPane = new JScrollPane(Pane);
        diag.setLayout(new BorderLayout());
        diag.add(scrollPane, BorderLayout.CENTER);
        diag.add(saveb, BorderLayout.SOUTH);
        diag.setResizable(false);
        diag.setVisible(true);
    }

    private void createMaterialsDialog(){
        JDialog diag = createdialog("Материалы", true);
        diag.setSize(640, 210);
        JPanel Pane = new JPanel();
        Pane.setLayout(new BoxLayout(Pane, BoxLayout.Y_AXIS));

        for (String th :materials.keySet()){
            JPanel materialPan = new JPanel();
            materialPan .setBorder(BorderFactory.createLineBorder(Color.black));
            materialPan.setLayout(new GridBagLayout());

            JLabel colorL = new JLabel("Color");
            JLabel nameL = new JLabel("Name");
            JLabel diffusionL = new JLabel("Diffusion");
            JLabel specularL = new JLabel("Specular");
            JLabel mirrorL = new JLabel("Mirror reflection coefficient");
            JLabel mirrorRL = new JLabel("Mirror ratio");
            JLabel refractiveIL = new JLabel("Refractive index");
            JLabel refractiveAL = new JLabel("Refractive albedo");

            Material kth = materials.get(th);

            JLabel colorR = new JLabel(kth.getColor().toString());
            JLabel nameR = new JLabel(th);
            JLabel diffusionR = new JLabel(Double.toString(kth.getDiffusion_coefficient()));
            JLabel specularR = new JLabel(Double.toString(kth.getSpecular()));
            JLabel mirrorR = new JLabel(Double.toString(kth.getMirror_ratio()));
            JLabel mirrorRLR = new JLabel(Double.toString(kth.getMirror_reflection_coefficient()));
            JLabel refractiveILR = new JLabel(Double.toString(kth.getRefractive_index()));
            JLabel refractiveALR = new JLabel(Double.toString(kth.getRefract_albedo()));

            GridBagConstraints c = new GridBagConstraints();
            for (int i = 0; i < 5; ++i)
            {
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 1.0f;
                c.gridx = i;
                c.gridy = 0;
                materialPan.add(new JLabel(""), c);
            }


            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 1;
            materialPan.add(nameL, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 2;
            materialPan.add(colorL, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 3;
            materialPan.add(diffusionL, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 4;
            materialPan.add(specularL, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 5;
            materialPan.add(mirrorL, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 6;
            materialPan.add(mirrorRL, c);
            c.insets = new Insets(5,0,0,0);
            c.gridx = 1;
            c.gridy = 7;
            materialPan.add(refractiveIL, c);
            c.insets = new Insets(5,0,10,0);
            c.gridx = 1;
            c.gridy = 8;
            materialPan.add(refractiveAL, c);


            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 1;
            materialPan.add(nameR, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 2;
            materialPan.add(colorR, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 3;
            materialPan.add(diffusionR, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 4;
            materialPan.add(specularR, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 5;
            materialPan.add(mirrorR, c);
            c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0f;
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 6;
            materialPan.add(mirrorRLR, c);
            c.insets = new Insets(5,0,0,0);
            c.gridx = 3;
            c.gridy = 7;
            materialPan.add(refractiveILR, c);
            c.insets = new Insets(5,0,10,0);
            c.gridx = 3;
            c.gridy = 8;
            materialPan.add(refractiveALR, c);
            Pane.add(materialPan);
        }
        JScrollPane scrollPane = new JScrollPane(Pane);
        diag.add(scrollPane);
        diag.setResizable(false);
        diag.setVisible(true);
    }
    private void createLighstsDialog() {

        JDialog diag = createdialog("Настройки источников", true);
        JPanel Pane = new JPanel();
        diag.setSize(640, 140);
        Pane.setLayout(new BoxLayout(Pane, BoxLayout.Y_AXIS));
        ArrayList<JTextField> fields = new ArrayList<>();
        BorderLayout bl = new BorderLayout();
        JPanel titlePan = new JPanel();
        titlePan.setLayout(new GridLayout(1, 4));
        titlePan.add(new JLabel("X"));
        titlePan.add(new JLabel("Y"));
        titlePan.add(new JLabel("Z"));
        titlePan.add(new JLabel("force"));
        Pane.add(titlePan);
        for (int i = 0; i < lights.size(); ++i)
        {
            JPanel modPan = new JPanel();
            modPan.setLayout(new GridLayout(1, 4));
            Vector3D pos = lights.get(i).getPosition();
            JTextField PosX = new JTextField(Double.toString(pos.getX()), 1);
            JTextField PosY = new JTextField(Double.toString(pos.getY()), 1);
            JTextField PosZ = new JTextField(Double.toString(pos.getZ()), 1);
            JTextField force = new JTextField(Double.toString(lights.get(i).getForce()), 1);
            fields.add(PosX);
            fields.add(PosY);
            fields.add(PosZ);
            fields.add(force);
            modPan.add(PosX);
            modPan.add(PosY);
            modPan.add(PosZ);
            modPan.add(force);
            Pane.add(modPan);
        }
        Button saveb = new Button("Сохранить");
        saveb.setBackground(Color.LIGHT_GRAY);
        saveb.addActionListener((e) -> {
            lights.clear();
            for (int i = 0; i < fields.size() / 4; ++i)
            {
                int ii = i * 4;
                Vector3D To = new Vector3D(Double.parseDouble(fields.get(ii).getText()),
                        Double.parseDouble(fields.get(ii + 1).getText()),
                        Double.parseDouble(fields.get(ii + 2).getText()));
                double force = Double.parseDouble(fields.get(ii + 3).getText());
                lights.add(new LightSource(To, force));
            }
            listener.invalidate();
            canvas.repaint();
        });
        diag.setLayout(bl);
        JScrollPane scrollPane = new JScrollPane(Pane);
        diag.add(scrollPane, BorderLayout.CENTER);
        diag.add(saveb, BorderLayout.SOUTH);
        diag.setVisible(true);
    }

    private void createCameraDialog(){
        JDialog diag = createdialog("Настройки камеры", true);
        diag.setSize(640, 140);

        JPanel modPan = new JPanel();
        modPan.setLayout(new GridLayout(3, 4));

        Vector3D cameraPos = camera.getPosition();
        Vector3D cameraUp = camera.getUp();
        Vector3D cameraTo = camera.getTo();

        JLabel cameraPosT = new JLabel("Позиция камеры");
        JTextField cameraPosX = new JTextField(Double.toString(cameraPos.getX()), 1);
        JTextField cameraPosY = new JTextField(Double.toString(cameraPos.getY()), 1);
        JTextField cameraPosZ = new JTextField(Double.toString(cameraPos.getZ()), 1);

        JLabel cameraUpT = new JLabel("Up камеры");
        JTextField cameraUpX = new JTextField(Double.toString(cameraUp.getX()), 1);
        JTextField cameraUpY = new JTextField(Double.toString(cameraUp.getY()), 1);
        JTextField cameraUpZ = new JTextField(Double.toString(cameraUp.getZ()), 1);

        JLabel cameraToT = new JLabel("Куда смотрит камера");
        JTextField cameraToX = new JTextField(Double.toString(cameraTo.getX()), 1);
        JTextField cameraToY = new JTextField(Double.toString(cameraTo.getY()), 1);
        JTextField cameraToZ = new JTextField(Double.toString(cameraTo.getZ()), 1);


        modPan.add(cameraPosT);
        modPan.add(cameraPosX);
        modPan.add(cameraPosY);
        modPan.add(cameraPosZ);

        modPan.add(cameraUpT);
        modPan.add(cameraUpX);
        modPan.add(cameraUpY);
        modPan.add(cameraUpZ);

        modPan.add(cameraToT);
        modPan.add(cameraToX);
        modPan.add(cameraToY);
        modPan.add(cameraToZ);

        JPanel contents = new JPanel();
        contents.setLayout(new BorderLayout());
        Button saveb = new Button("Сохранить");
        saveb.setBackground(Color.LIGHT_GRAY);
        saveb.addActionListener((e) -> {
            Vector3D Pos = new Vector3D(Double.parseDouble(cameraPosX.getText()),
                    Double.parseDouble(cameraPosY.getText()),
                    Double.parseDouble(cameraPosZ.getText()));
            Vector3D Up = new Vector3D(Double.parseDouble(cameraUpX.getText()),
                    Double.parseDouble(cameraUpY.getText()),
                    Double.parseDouble(cameraUpZ.getText()));
            Vector3D To = new Vector3D(Double.parseDouble(cameraToX.getText()),
                    Double.parseDouble(cameraToY.getText()),
                    Double.parseDouble(cameraToZ.getText()));
            camera.setPosition(Pos);
            camera.setUp(Up);
            camera.setTo(To);
            listener.invalidate();
            canvas.repaint();
        });
        contents.add(modPan, BorderLayout.CENTER);
        contents.add(saveb, BorderLayout.SOUTH);
        diag.add(contents);
        diag.setResizable(false);
        diag.setVisible(true);
    }

}
