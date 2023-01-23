package com.capdevon.effect.influencers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.EmptyParticleInfluencer;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;

/**
 *
 * @author capdevon
 */
public class CompositeInfluencer extends EmptyParticleInfluencer {

    protected SafeArrayList<ParticleInfluencer> influencers = new SafeArrayList<>(ParticleInfluencer.class);

    /**
     * For serialization only. Do not use.
     */
    protected CompositeInfluencer() {
    }

    public CompositeInfluencer(ParticleInfluencer... influencer) {
        this.influencers.addAll(Arrays.asList(influencer));
    }

    @Override
    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        for (ParticleInfluencer pi : influencers.getArray()) {
            pi.influenceParticle(particle, emitterShape);
        }
    }

    /**
     * Add an influencer to the list of influencers.
     *
     * @param influencer
     * @return CompositeInfluencer
     */
    public CompositeInfluencer addInfluencer(ParticleInfluencer influencer) {
        influencers.add(influencer);
        return this;
    }

    /**
     * Removes the first influencer that is an instance of the given class.
     *
     * @param influencerType
     * @return CompositeInfluencer
     */
    public CompositeInfluencer removeInfluencer(Class<? extends ParticleInfluencer> influencerType) {
        for (int i = 0; i < influencers.size(); i++) {
            if (influencerType.isAssignableFrom(influencers.get(i).getClass())) {
                influencers.remove(i);
                break;
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ParticleInfluencer> T getInfluencer(Class<T> influencerType) {
        for (ParticleInfluencer pi : influencers.getArray()) {
            if (influencerType.isAssignableFrom(pi.getClass())) {
                return (T) pi;
            }
        }
        return null;
    }

    public ParticleInfluencer getInfluencer(int index) {
        return influencers.get(index);
    }

    public int getNumInfluencers() {
        return influencers.size();
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

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.writeSavableArrayList(new ArrayList(influencers), "influencersList", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        influencers.addAll(0, ic.readSavableArrayList("influencersList", null));
    }

}
