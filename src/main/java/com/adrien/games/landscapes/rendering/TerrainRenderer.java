package com.adrien.games.landscapes.rendering;

import com.adrien.games.bagl.core.Camera;
import com.adrien.games.bagl.rendering.Shader;
import org.lwjgl.opengl.GL11;

/**
 * Terrain mesh renderer.
 * <p>
 * Renders {@link TerrainMesh}.
 */
public class TerrainRenderer {

    /**
     * The terrain shader.
     */
    private final Shader shader;

    /**
     * Constructs the renderer.
     */
    public TerrainRenderer() {
        this.shader = new Shader()
                .addVertexShader("terrain.vert")
                .addFragmentShader("terrain.frag")
                .compile();
    }

    /**
     * Renders a {@link TerrainMesh}.
     *
     * @param mesh   The mesh to render.
     * @param camera The camera used for rendering.
     */
    public void render(final TerrainMesh mesh, final Camera camera) {
        this.shader.bind();
        this.shader.setUniform("uVP", camera.getViewProj());

        mesh.bind();
        GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getPolygonCount() * TerrainMesh.INDICES_PER_POLYGON, GL11.GL_UNSIGNED_INT, 0);
        mesh.unbind();

        Shader.unbind();
    }

    /**
     * Destroys the renderer.
     */
    public void destroy() {
        this.shader.destroy();
    }

}
