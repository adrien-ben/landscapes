package com.adrien.games.landscapes.rendering.terrain;

import com.adrien.games.bagl.core.Camera;
import com.adrien.games.bagl.core.Engine;
import com.adrien.games.bagl.rendering.BlendMode;
import com.adrien.games.bagl.rendering.Shader;
import com.adrien.games.bagl.rendering.light.DirectionalLight;
import com.adrien.games.bagl.rendering.light.Light;
import org.lwjgl.opengl.GL11;

/**
 * Terrain mesh renderer
 * <p>
 * Renders {@link TerrainMesh}
 *
 * @author adrien
 */
public class TerrainRenderer {

    /** The terrain shader */
    private final Shader shader;

    /**
     * Construct the renderer
     */
    public TerrainRenderer() {
        this.shader = new Shader()
                .addVertexShader("terrain.vert")
                .addFragmentShader("terrain.frag")
                .compile();
    }

    /**
     * Renders a {@link TerrainMesh}
     *
     * @param mesh    The mesh to render
     * @param camera  The camera used for rendering
     * @param ambient The ambient light of the scene
     * @param sun     The sun light of the scene
     */
    public void render(final TerrainMesh mesh, final Camera camera, final Light ambient, final DirectionalLight sun) {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Engine.setBlendMode(BlendMode.DEFAULT);

        this.shader.bind();
        this.shader.setUniform("uVP", camera.getViewProj());
        this.shader.setUniform("uAmbient.intensity", ambient.getIntensity());
        this.shader.setUniform("uAmbient.color", ambient.getColor());
        this.shader.setUniform("uSun.base.intensity", sun.getIntensity());
        this.shader.setUniform("uSun.base.color", sun.getColor());
        this.shader.setUniform("uSun.direction", sun.getDirection());

        mesh.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getPolygonCount() * TerrainMesh.INDICES_PER_POLYGON, GL11.GL_UNSIGNED_INT, 0);
        mesh.unbind();

        Shader.unbind();
    }

    /**
     * Destroys the renderer
     */
    public void destroy() {
        this.shader.destroy();
    }

}
