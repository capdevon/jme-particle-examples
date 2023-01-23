package com.capdevon.effect.influencers;

import java.io.IOException;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.EmptyParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 * The color module allows you to change the particle's color over time.
 *
 * @author capdevon
 */
public class GradientColorInfluencer extends EmptyParticleInfluencer {

    private ColorRGBA startColor = new ColorRGBA(0.4f, 0.4f, 0.4f, 0.5f);
    private ColorRGBA endColor = new ColorRGBA(0.1f, 0.1f, 0.1f, 0.0f);
    private float minX = 0.5f;
    private float maxX = 1.0f;
    private final ColorRGBA stepColor = ColorRGBA.White.clone();

    /**
     * For serialization only. Do not use.
     */
    protected GradientColorInfluencer() {
    }

    /**
     * Instantiate a GradientColorInfluencer.
     *
     * @param startColor
     * @param endColor
     * @param minX
     * @param maxX
     */
    public GradientColorInfluencer(ColorRGBA startColor, ColorRGBA endColor, float minX, float maxX) {
        this.startColor.set(startColor);
        this.endColor.set(endColor);
        this.minX = minX;
        this.maxX = maxX;
    }

    @Override
    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        float b = (particle.startlife - particle.life) / particle.startlife;
        particle.color.set(getValueColor(b));
    }

    private ColorRGBA getValueColor(float percent) {
        float range = maxX - minX;
        float p = (percent - minX) / range;
        stepColor.interpolateLocal(endColor, startColor, FastMath.clamp(p, .0f, 1f));
        return stepColor;
    }

    @Override
    public void setInitialVelocity(Vector3f initialVelocity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Vector3f getInitialVelocity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVelocityVariation(float variation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getVelocityVariation() {
        throw new UnsupportedOperationException();
    }

    public ColorRGBA getStartColor() {
        return startColor;
    }

    public void setStartColor(ColorRGBA startColor) {
        this.startColor.set(startColor);
    }

    public ColorRGBA getEndColor() {
        return endColor;
    }

    public void setEndColor(ColorRGBA endColor) {
        this.endColor.set(endColor);
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(startColor, "startColor", null);
        oc.write(endColor, "endColor", null);
        oc.write(minX, "minX", 0.5f);
        oc.write(maxX, "maxX", 1.0f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        startColor = (ColorRGBA) ic.readSavable("startColor", null);
        endColor = (ColorRGBA) ic.readSavable("endColor", null);
        minX = ic.readFloat("minX", 0.5f);
        maxX = ic.readFloat("maxX", 1.0f);
    }

}
