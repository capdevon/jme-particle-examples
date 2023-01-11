package com.capdevon.effect.influencers;

import com.jme3.effect.Particle;
import com.jme3.effect.influencers.EmptyParticleInfluencer;
import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.effect.shapes.EmitterShape;
import com.jme3.math.Vector3f;
import com.jme3.util.SafeArrayList;

/**
 *
 * @author capdevon
 */
public class CompositeInfluencer extends EmptyParticleInfluencer {

    protected SafeArrayList<ParticleInfluencer> influencers = new SafeArrayList<>(ParticleInfluencer.class);

    @Override
    public void influenceParticle(Particle particle, EmitterShape emitterShape) {
        for (ParticleInfluencer pi : influencers.getArray()) {
            pi.influenceParticle(particle, emitterShape);
        }
    }

    /**
     * Add an influencer to the list of influencers.
     * @param influencer
     */
    public CompositeInfluencer addInfluencer(ParticleInfluencer influencer) {
        influencers.add(influencer);
        return this;
    }

    /**
     * Removes the first influencer that is an instance of the given class.
     * @param influencerType
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

}
