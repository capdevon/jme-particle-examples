package mygame.effect;

import com.jme3.effect.Particle;
import com.jme3.math.Matrix3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Mesh;

/**
 *
 * @author capdevon
 */
public abstract class MyParticleMesh extends Mesh {

    /**
     * Initialize mesh data.
     *
     * @param emitter The emitter which will use this <code>ParticleMesh</code>.
     * @param numParticles The maximum number of particles to simulate
     */
    public abstract void initParticleData(Object emitter, int numParticles);

    /**
     * Set the images on the X and Y coordinates
     *
     * @param imagesX Images on the X coordinate
     * @param imagesY Images on the Y coordinate
     */
    public abstract void setImagesXY(int imagesX, int imagesY);

    /**
     * Update the particle visual data. Typically called every frame.
     *
     * @param particles the particles to update
     * @param cam the camera to use for billboarding
     * @param inverseRotation the inverse rotation matrix
     */
    public abstract void updateParticleData(Particle[] particles, Camera cam, Matrix3f inverseRotation);
}
