package mygame;

import java.util.LinkedList;
import java.util.Queue;

import com.capdevon.effect.shapes.EmitterMeshVertexVFX;
import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
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
import com.jme3.scene.shape.CenterQuad;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;

/**
 *
 * @author capdevon
 */
public class Test_JmeVFX extends SimpleApplication implements ActionListener {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Test_JmeVFX app = new Test_JmeVFX();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1024, 768);
        settings.setRenderer(AppSettings.LWJGL_OPENGL32);
        app.setSettings(settings);
        app.setPauseOnLostFocus(false);
        app.setShowSettings(false);
        app.start();
    }

    private boolean autoRotate = false;
    private Node myModel;
    private AnimComposer animComposer;
    private Queue<String> animsQueue = new LinkedList<>();
    private BitmapText emitUI;
    private ParticleEmitter emit;

    @Override
    public void simpleInitApp() {

        BitmapText hud = createTextUI(ColorRGBA.White, 20, 15);
        hud.setText("NextAnim: KEY_RIGHT, AutoRotate: KEY_RETURN, InWorldSpace: KEY_I");

        emitUI = createTextUI(ColorRGBA.Blue, 20, 15 * 2);

        //Press F6 to turn it on/off.
        //stateManager.attach(new DetailedProfilerState());

        configCamera();
        setupLights();
        setupScene();
        setupCharacter();
        setupKeys();
    }

    private void setupCharacter() {
        // Add model to the scene
        myModel = (Node) assetManager.loadModel("Models/Mixamo/ybot.j3o");
        rootNode.attachChild(myModel);

        animComposer = GameObject.findControl(myModel, AnimComposer.class);
        animsQueue.addAll(animComposer.getAnimClipsNames());
        System.out.println(animsQueue);

        SkinningControl skControl = GameObject.findControl(myModel, SkinningControl.class);
        skControl.setHardwareSkinningPreferred(false);

        // 1. Alpha_Surface
        // 2. Alpha_Joints
        Geometry geo = (Geometry) myModel.getChild("Alpha_Surface");
        //vertexMode(geo);
        emit = particleMode(geo);
    }

    private void vertexMode(Geometry source) {
        Node node = new Node("SkinnedMesh");
        SkinnedVFXControl skinVFX = new SkinnedVFXControl(source.getMesh(), 0.001f);
        skinVFX.mat.setColor("GlowColor", ColorRGBA.Magenta);
        skinVFX.target = source.getParent();
        skinVFX.refreshRate = 0.01f;
        node.addControl(skinVFX);
        rootNode.attachChild(node);
    }

    private ParticleEmitter particleMode(Geometry geo) {
        ParticleEmitter emitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 300);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
        //mat.setColor("GlowColor", ColorRGBA.Magenta);
        emitter.setMaterial(mat);
        emitter.setLowLife(1);
        emitter.setHighLife(1);
        emitter.setImagesX(15);
        emitter.setStartSize(0.02f);
        emitter.setEndSize(0.02f);
        emitter.setStartColor(ColorRGBA.Blue);
        emitter.setEndColor(ColorRGBA.Cyan);
        emitter.setParticlesPerSec(100);
        emitter.setGravity(0, 0, 0);
        emitter.setInWorldSpace(true);
//        emitter.getParticleInfluencer().setVelocityVariation(1);
//        emitter.getParticleInfluencer().setInitialVelocity(new Vector3f(0, .5f, 0));
        emitter.setShape(new EmitterMeshVertexVFX(geo.getMesh()));
//        emit.setShape(new EmitterMeshFaceVFX(geo.getMesh()));
//        emit.setParticleInfluencer(new NewtonianParticleInfluencer());
        geo.getParent().attachChild(emitter);
        return emitter;
    }

    private void setupKeys() {
        addMapping("autoRotate", new KeyTrigger(KeyInput.KEY_RETURN));
        addMapping("nextAnim", new KeyTrigger(KeyInput.KEY_RIGHT));
        addMapping("InWorldSpace", new KeyTrigger(KeyInput.KEY_I));
    }

    private void addMapping(String mappingName, Trigger... triggers) {
        inputManager.addMapping(mappingName, triggers);
        inputManager.addListener(this, mappingName);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (name.equals("nextAnim") && isPressed) {
            String anim = animsQueue.poll();
            animsQueue.add(anim);
            animComposer.setCurrentAction(anim);

        } else if (name.equals("autoRotate") && isPressed) {
            autoRotate = !autoRotate;
            
        } else if (name.equals("InWorldSpace") && isPressed) {
            boolean worldSpace = emit.isInWorldSpace();
            emit.setInWorldSpace(!worldSpace);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (autoRotate) {
            myModel.rotate(0, tpf, 0);
        }
        emitUI.setText("InWorldSpace: " + emit.isInWorldSpace());
    }

    private void configCamera() {
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(10);

        cam.setLocation(new Vector3f(0, 2, 5));
        cam.lookAt(Vector3f.UNIT_Y, Vector3f.UNIT_Y);

        float aspect = (float) cam.getWidth() / cam.getHeight();
        cam.setFrustumPerspective(45, aspect, 0.1f, 1000f);
    }

    private void setupScene() {
        CenterQuad quad = new CenterQuad(8, 8);
        quad.scaleTextureCoordinates(new Vector2f(2, 2));
        Geometry floor = createMesh("Floor", quad);
        floor.rotate(-FastMath.HALF_PI, 0, 0);
        rootNode.attachChild(floor);
    }

    private Geometry createMesh(String name, Mesh mesh) {
        Geometry geo = new Geometry(name, mesh);
        Material mat = new Material(assetManager, Materials.LIGHTING);
        Texture tex = assetManager.loadTexture("Interface/Logo/Monkey.jpg");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        geo.setMaterial(mat);
        return geo;
    }

    private void setupLights() {
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

        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(bloom);
        fpp.addFilter(dlsf);
        fpp.addFilter(fxaa);
        viewPort.addProcessor(fpp);
    }

    private BitmapText createTextUI(ColorRGBA color, float xPos, float yPos) {
        BitmapFont font = assetManager.loadFont("Interface/Fonts/Console.fnt");
        BitmapText bmp = new BitmapText(font);
        bmp.setSize(font.getCharSet().getRenderedSize());
        bmp.setLocalTranslation(xPos, settings.getHeight() - yPos, 0);
        bmp.setColor(color);
        guiNode.attachChild(bmp);
        return bmp;
    }

}
