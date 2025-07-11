package com.capdevon.effect;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author capdevon
 */
class MyParticleEmitterControl extends AbstractControl {

    protected MyParticleEmitterControl() {}

    @Override
    public void controlUpdate(float tpf) {
        ((MyParticleEmitter) spatial).updateFromControl(tpf);
    }

    @Override
    public void controlRender(RenderManager rm, ViewPort vp) {
        ((MyParticleEmitter) spatial).renderFromControl(rm, vp);
    }

}
