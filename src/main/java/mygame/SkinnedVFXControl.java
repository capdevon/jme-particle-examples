package mygame;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.CenterQuad;
import com.jme3.util.BufferUtils;

/**
 * 
 * @author capdevon
 */
public class SkinnedVFXControl extends AbstractControl {

    private final AssetManager assetManager;
    private final List<Integer> indexList = new ArrayList<>();
    private final Vector3f origin = new Vector3f();

    private Mesh source;
    private float size = 0.001f;
    private float timer;

    public Material mat;
    public Spatial target;
    public float refreshRate = 0.04f;

    /**
     * Constructor.
     * 
     * @deprecated Use {@link com.capdevon.effect.MyParticleEmitter} instead.
     * @param assetManager
     * @param source
     */
    @Deprecated
    public SkinnedVFXControl(AssetManager assetManager, Mesh source) {
        this.assetManager = assetManager;
        this.source = source;
        setupMaterial();
    }

    /**
     * Initialize VFX material
     */
    private void setupMaterial() {
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setName("MAT_SkinnedMesh");
        mat.setColor("Color", ColorRGBA.White);
        mat.setColor("GlowColor", ColorRGBA.Cyan);
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setBlendMode(BlendMode.Additive);
        mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    }

    @Override
    public void setSpatial(Spatial sp) {
        super.setSpatial(sp);
        if (spatial != null) {
            calculateVertices();
        }
    }

    private void calculateVertices() {
        CenterQuad quad = new CenterQuad(size, size);
        Map<Vector3f, Integer> mapPos = new HashMap<>(source.getVertexCount());

        // Get the geometry's vertex buffer
        FloatBuffer vertexBuffer = source.getFloatBuffer(VertexBuffer.Type.Position);
        for (int i = 0; i < vertexBuffer.limit() / 3; i++) {

            Vector3f position = new Vector3f();
            BufferUtils.populateFromBuffer(position, vertexBuffer, i);

            if (!mapPos.containsKey(position)) {
                mapPos.put(position, i);
                createVertexMesh(i, position, quad);
            }
        }

        indexList.addAll(mapPos.values());
        System.out.println("IndexList: " + indexList.size());
    }

    private void createVertexMesh(int vertIndex, Vector3f vertPos, Mesh vertMesh) {
        Geometry geo = new Geometry("QuadMesh_" + vertIndex, vertMesh);
        geo.setMaterial(mat);
        geo.setShadowMode(ShadowMode.Off);
        geo.setQueueBucket(Bucket.Transparent);
        geo.setLocalTranslation(vertPos);
        ((Node) spatial).attachChild(geo);
    }

    @Override
    protected void controlUpdate(float tpf) {
        timer += tpf;
        if (timer > refreshRate) {
            timer = 0;

            int j = 0;
            FloatBuffer vertexBuffer = source.getFloatBuffer(VertexBuffer.Type.Position);
            for (Integer index : indexList) {
                BufferUtils.populateFromBuffer(origin, vertexBuffer, index);
                ((Node) spatial).getChild(j).setLocalTranslation(origin);
                j++;
            }
        }

        if (target != null) {
            spatial.setLocalTransform(target.getWorldTransform());
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

}
