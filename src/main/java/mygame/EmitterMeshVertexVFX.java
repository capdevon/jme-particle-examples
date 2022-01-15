package mygame;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import com.jme3.util.clone.Cloner;

/**
 *
 * @author capdevon
 */
public class EmitterMeshVertexVFX implements EmitterShape {

    private Mesh source;
    private List<Integer> indexList = new ArrayList<>();

    public EmitterMeshVertexVFX(Mesh source) {
        this.source = source;
        calculateVertices();
    }

    private void calculateVertices() {
        Map<Vector3f, Integer> mapPos = new HashMap<>(source.getVertexCount());

        // Get the geometry's vertex buffer
        FloatBuffer vertexBuffer = source.getFloatBuffer(VertexBuffer.Type.Position);
        for (int i = 0; i < vertexBuffer.limit() / 3; i++) {

            Vector3f position = new Vector3f();
            BufferUtils.populateFromBuffer(position, vertexBuffer, i);

            if (!mapPos.containsKey(position)) {
                mapPos.put(position, i);
            }
        }

        indexList.addAll(mapPos.values());
        System.out.println("IndexList: " + indexList.size());
    }

    @Override
    public void getRandomPoint(Vector3f store) {
        int i = FastMath.nextRandomInt(0, indexList.size() - 1);
        int vertIndex = indexList.get(i);
        FloatBuffer vertexBuffer = source.getFloatBuffer(VertexBuffer.Type.Position);
        BufferUtils.populateFromBuffer(store, vertexBuffer, vertIndex);
    }

    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        int i = FastMath.nextRandomInt(0, indexList.size() - 1);
        int vertIndex = indexList.get(i);
        
        FloatBuffer vertBuffer = source.getFloatBuffer(VertexBuffer.Type.Position);
        BufferUtils.populateFromBuffer(store, vertBuffer, vertIndex);

        FloatBuffer normBuffer = source.getFloatBuffer(VertexBuffer.Type.Normal);
        BufferUtils.populateFromBuffer(normal, normBuffer, vertIndex);
    }

    @Override
    public EmitterShape deepClone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public Object jmeClone() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        // TODO Auto-generated method stub
    }

}
