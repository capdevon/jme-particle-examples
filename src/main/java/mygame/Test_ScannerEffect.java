package mygame;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.FXAAFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.CenterQuad;
import com.jme3.scene.shape.Sphere;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author capdevon
 */
public class Test_ScannerEffect extends SimpleApplication {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Test_ScannerEffect app = new Test_ScannerEffect();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        app.start();
    }

    private BitmapText hud;
    private Node objects = new Node("Objects");

    @Override
    public void simpleInitApp() {

        hud = createTextUI(ColorRGBA.Red, 20, 15);
        hud.setText("Test_ScannerEffect");

        //Press F6 to turn it on/off.
        //stateManager.attach(new DetailedProfilerState());
        
        configCamera();
        setupSky();
        createTerrain();
        addLighting();
        createFloor();
        addSinbad();

        float size = 0.5f;
        ColorRGBA color = ColorRGBA.Orange;
        instantiate(createBox("Box", size, color), new Vector3f(2, 1, 0), objects);
        instantiate(createSphere("Spehere", size, color), new Vector3f(-2, 1, 0), objects);
        rootNode.attachChild(objects);
    }

    @Override
    public void simpleUpdate(float tpf) {
        // do something...
        objects.rotate(0, 0.5f * tpf, 0);
    }

    private void configCamera() {
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(20);

        cam.setLocation(new Vector3f(0, 4, 10));
        cam.lookAt(Vector3f.UNIT_Y, Vector3f.UNIT_Y);

        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustumPerspective(45, aspect, 0.1f, 1000f);
    }

    private void addSinbad() {
        Spatial model = assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        model.setName("Sinbad");
        model.setLocalScale(0.25f);
        model.setLocalTranslation(0, 1.26f, 0);
        rootNode.attachChild(model);

        Spatial sword = assetManager.loadModel("Models/Sinbad/Sword.mesh.xml");
        sword.setName("Sword");

        SkinningControl skinningControl = model.getControl(SkinningControl.class);
        Node n = skinningControl.getAttachmentsNode("Handle.R");
        n.attachChild(sword);

        AnimComposer composer = model.getControl(AnimComposer.class);
        composer.setCurrentAction("Dance");
    }

    private void createFloor() {
        CenterQuad quad = new CenterQuad(10, 10);
        quad.scaleTextureCoordinates(new Vector2f(2, 2));
        Geometry floor = new Geometry("Floor", quad);
        Material mat = new Material(assetManager, Materials.LIGHTING);
        Texture tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        floor.setMaterial(mat);
        floor.rotate(-FastMath.HALF_PI, 0, 0);
        rootNode.attachChild(floor);
    }

    private Spatial instantiate(Spatial sp, Vector3f position, Node parent) {
        sp.setLocalTranslation(position);
        parent.attachChild(sp);
        return sp;
    }
    
    private Geometry createBox(String name, float size, ColorRGBA color) {
        Box mesh = new Box(size, size, size);
        return createMesh(name, mesh, color);
    }

    private Geometry createSphere(String name, float radius, ColorRGBA color) {
        Sphere mesh = new Sphere(32, 32, radius);
        return createMesh(name, mesh, color);
    }

    private Geometry createMesh(String name, Mesh mesh, ColorRGBA color) {
        Geometry geo = new Geometry(name, mesh);
        Material mat = createMaterialPBR(color);
        geo.setMaterial(mat);
        return geo;
    }

    private Material createMaterialPBR(ColorRGBA color) {
        Material mat = new Material(assetManager, Materials.PBR);
        mat.setColor("BaseColor", ColorRGBA.Orange);
        mat.setFloat("Metallic", 0.01f);
        mat.setFloat("Roughness", 0.3f);
        return mat;
    }
    
    /*
     * Create a shiny light material with a specified uniform color.
     */
    private Material createShiny(ColorRGBA color) {
        Material m = new Material(assetManager, Materials.LIGHTING);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Ambient", color);
        m.setColor("Diffuse", color);
//        m.setColor("Specular", ColorRGBA.White);
        m.setFloat("Shininess", 100f);
        return m;
    }

    private void addLighting() {
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        //rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, 4096, 3);
        dlsf.setLight(sun);
        dlsf.setShadowIntensity(0.4f);
        dlsf.setShadowZExtend(256);

        FXAAFilter fxaa = new FXAAFilter();
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        
        DropShadowFilter dsf = new DropShadowFilter();
        dsf.setShadowColor(ColorRGBA.Cyan);
        dsf.setShadowIntensity(0.75f);
//        dsf.setShowBox(true);

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dsf);
        fpp.addFilter(bloom);
        fpp.addFilter(dlsf);
        fpp.addFilter(fxaa);
        viewPort.addProcessor(fpp);
    }

    private void setupSky() {
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
        sky.setShadowMode(RenderQueue.ShadowMode.Off);
        rootNode.attachChild(sky);
    }
    
    private void createTerrain() {
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");

        ImageBasedHeightMap heightMap = null;
        try {
            heightMap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
            heightMap.load();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightMap.getHeightMap());
        TerrainLodControl control = new TerrainLodControl(terrain, cam);
        control.setLodCalculator(new DistanceLodCalculator(65, 2.7f));
        terrain.addControl(control);
        terrain.setMaterial(createTerrainMat());
        terrain.setShadowMode(RenderQueue.ShadowMode.Receive);

        terrain.setLocalTranslation(0, -60, 0);
        terrain.setLocalScale(2f, 0.5f, 2f);
        rootNode.attachChild(terrain);
    }

    private Material createTerrainMat() {
        // TERRAIN TEXTURE material
        Material matRock = new Material(assetManager, "Common/MatDefs/Terrain/Terrain.j3md");
        matRock.setBoolean("useTriPlanarMapping", false);

        // ALPHA map (for splat textures)
        matRock.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));

        // GRASS texture
        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("Tex1", grass);
        matRock.setFloat("Tex1Scale", 64);

        // DIRT texture
        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("Tex2", dirt);
        matRock.setFloat("Tex2Scale", 16);

        // ROCK texture
        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        matRock.setTexture("Tex3", rock);
        matRock.setFloat("Tex3Scale", 128);

        return matRock;
    }

    private BitmapText createTextUI(ColorRGBA color, float xPos, float yPos) {
        BitmapText bmp = new BitmapText(guiFont);
        bmp.setSize(guiFont.getCharSet().getRenderedSize());
        bmp.setLocalTranslation(xPos, settings.getHeight() - yPos, 0);
        bmp.setColor(color);
        guiNode.attachChild(bmp);
        return bmp;
    }

}
