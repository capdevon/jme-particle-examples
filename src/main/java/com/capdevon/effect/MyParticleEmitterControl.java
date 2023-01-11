package com.capdevon.effect;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author capdevon
 */
public class MyParticleEmitterControl extends AbstractControl {

    @Override
    public void controlUpdate(float tpf) {
        getParticleEmitter().updateFromControl(tpf);
    }

    @Override
    public void controlRender(RenderManager rm, ViewPort vp) {
        getParticleEmitter().renderFromControl(rm, vp);
    }

    protected MyParticleEmitter getParticleEmitter() {
        return (MyParticleEmitter) spatial;
    }
}
