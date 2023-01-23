package com.capdevon.effect.shapes;

import java.io.IOException;

import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.FastMath;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.util.clone.Cloner;

/**
 * This emitter shape emits the particles from the given shape's faces.
 *
 * @author capdevon
 */
public class EmitterMeshFaceVFX implements EmitterShape {

    private Mesh source;
    private Triangle triStore = new Triangle();
    Vector3f p1 = new Vector3f();
    Vector3f p2 = new Vector3f();
    Vector3f p3 = new Vector3f();
    Vector3f a = new Vector3f();
    Vector3f b = new Vector3f();
    Vector3f result = new Vector3f();

    /**
     * For serialization only. Do not use.
     */
    protected EmitterMeshFaceVFX() {
    }

    /**
     * Instantiate an EmitterMeshFaceVFX.
     *
     * @param source
     */
    public EmitterMeshFaceVFX(Mesh source) {
        this.source = source;
    }

    /**
     * Randomly selects a point on a random face.
     */
    @Override
    public void getRandomPoint(Vector3f store) {
        int triangleIndex = FastMath.nextRandomInt(0, source.getTriangleCount() - 1);
        source.getTriangle(triangleIndex, triStore);
        triStore.calculateCenter();
        triStore.calculateNormal();

        p1.set(triStore.get1().subtract(triStore.getCenter()));
        p2.set(triStore.get2().subtract(triStore.getCenter()));
        p3.set(triStore.get3().subtract(triStore.getCenter()));

        a.interpolateLocal(p1, p2, 1f - FastMath.nextRandomFloat());
        b.interpolateLocal(p1, p3, 1f - FastMath.nextRandomFloat());
        result.interpolateLocal(a, b, FastMath.nextRandomFloat());

        store.set(triStore.getCenter().add(result));
    }

    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
    }

    @Override
    public Object jmeClone() {
        return null;
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
    }

    @Override
    public EmitterShape deepClone() {
        return null;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(source, "mesh", new EmitterMeshVertexVFX());
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        source = (Mesh) ic.readSavable("mesh", new EmitterMeshVertexVFX());
    }

    @Override
    public String toString() {
        return "EmitterMeshFaceVFX [Mesh Mode=" + source.getMode()
                + ", Triangles=" + source.getTriangleCount()
                + ", Vertices=" + source.getVertexCount()
                + "]";
    }

}
