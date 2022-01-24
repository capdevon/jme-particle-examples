# Game-Jam-01

The goal of this game jam is to reproduce the effect of this Unity [tutorial](https://www.youtube.com/watch?v=ePbeaYuMNK4) with the jMonkeyEngine. 

The test case you can use is [Test_JmeVFX2](https://github.com/capdevon/Game-Jam-01/blob/main/src/main/java/mygame/Test_JmeVFX2.java). 

If you solve the problem, send me a pull request or write me on the jme fourm at this [topic](https://hub.jmonkeyengine.org/t/jmonkeyengine-vfx-game-jam-01/45221/20). 

Thanks for your help.

##  Some useful info:
By changing the ParticleEmitter.setInWorldSpace() option, you get interesting results.

1. ParticleEmitter.setInWorldSpace(true)
  - PROS: the particles move more naturally, creating a more realistic trail that conforms to the character’s movement.
  - CONS: particles are almost always generated at the character’s point of origin and not from the vertices of the 3d model.

2. ParticleEmitter.setInWorldSpace(false)
  - PROS: the particles are correctly emitted from the vertices of the 3d model.
  - CONS: the particles rotate and move with the character and the trail effect is no longer realistic.

The result I would like to obtain is to have only the PROs of the two scenarios, excluding the CONS.

## Youtube video:
[demo](https://www.youtube.com/watch?v=Y4CuL_qEowQ)
