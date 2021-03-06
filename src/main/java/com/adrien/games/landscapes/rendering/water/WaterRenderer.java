package com.adrien.games.landscapes.rendering.water;


import com.adrienben.games.bagl.core.io.ResourcePath;
import com.adrienben.games.bagl.engine.camera.Camera;
import com.adrienben.games.bagl.engine.rendering.light.DirectionalLight;
import com.adrienben.games.bagl.engine.rendering.light.Light;
import com.adrienben.games.bagl.opengl.BlendMode;
import com.adrienben.games.bagl.opengl.OpenGL;
import com.adrienben.games.bagl.opengl.shader.Shader;
import org.lwjgl.opengl.GL11;

/**
 * Water mesh renderer
 *
 * @author adrien
 */
public class WaterRenderer {

    private final Shader shader;

    /**
     * Construct the water renderer
     */
    public WaterRenderer() {
        this.shader = Shader.pipelineBuilder()
                .vertexPath(ResourcePath.get("classpath:/shaders/water.vert"))
                .fragmentPath(ResourcePath.get("classpath:/shaders/water.frag"))
                .build();
    }

    /**
     * Render the water mesh
     *
     * @param waterMesh The mesh to render
     * @param width     The width of the rendered mesh
     * @param depth     The depth of the rendered mesh
     * @param height    The height at which to render the mesh
     * @param camera    The camera to use for rendering
     * @param ambient   The ambient light of the scene
     * @param sun       The sun light og the scene
     */
    public void render(final WaterMesh waterMesh, final float width, final float depth, final float height, final Camera camera,
                       final Light ambient, final DirectionalLight sun) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        OpenGL.setBlendMode(BlendMode.TRANSPARENCY);

        this.shader.bind();
        this.setUpShader(width, depth, height, camera, ambient, sun);
        waterMesh.bind();

        GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

        waterMesh.unbind();
        Shader.unbind();
    }

    /**
     * Set up shader for rendering
     *
     * @param width   The width of the rendered mesh
     * @param depth   The depth of the rendered mesh
     * @param height  The height at which to render the mesh
     * @param camera  The camera to use for rendering
     * @param ambient The ambient light of the scene
     * @param sun     The sun light og the scene
     */
    private void setUpShader(final float width, final float depth, final float height, final Camera camera, final Light ambient,
                             final DirectionalLight sun) {
        this.shader.setUniform("uVP", camera.getViewProj());
        this.shader.setUniform("width", width);
        this.shader.setUniform("depth", depth);
        this.shader.setUniform("height", height);
        this.shader.setUniform("eye", camera.getPosition());
        this.shader.setUniform("uAmbient.intensity", ambient.getIntensity());
        this.shader.setUniform("uAmbient.color", ambient.getColor());
        this.shader.setUniform("uSun.base.intensity", sun.getIntensity());
        this.shader.setUniform("uSun.base.color", sun.getColor());
        this.shader.setUniform("uSun.direction", sun.getDirection());
    }

    /**
     * Destroy the renderer
     */
    public void destroy() {
        this.shader.destroy();
    }

}
