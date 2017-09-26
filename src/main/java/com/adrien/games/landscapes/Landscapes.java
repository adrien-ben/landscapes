package com.adrien.games.landscapes;

import com.adrien.games.bagl.core.*;
import com.adrien.games.bagl.core.math.Vector2;
import com.adrien.games.bagl.core.math.Vector3;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.bagl.utils.FileUtils;
import com.adrien.games.landscapes.rendering.TerrainMesh;
import com.adrien.games.landscapes.rendering.TerrainRenderer;
import com.adrien.games.landscapes.terrain.HeightMap;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Landscape generator.
 */
public class Landscapes implements Game {

    /**
     * Scale of the renderer text.
     */
    private static final float TEXT_SCALE = 0.08f;

    /**
     * Size of the terrain.
     */
    private static final int TERRAIN_SIZE = 100;

    /**
     * Noise frequency.
     */
    private float frequency = 0.1f;

    /**
     * Noise octave count.
     */
    private int octaves = 1;

    /**
     * Noise persistence.
     */
    private float persistence = 1f;

    /**
     * Noise exponent.
     */
    private float exponent = 1f;

    /**
     * Should the mesh be regenerated ?
     */
    private boolean dirty = false;

    /**
     * The terrain mesh.
     */
    private TerrainMesh mesh;

    /**
     * The terrain renderer.
     */
    private TerrainRenderer terrainRenderer;

    /**
     * The camera.
     */
    private Camera camera;

    /**
     * The camera controller.
     */
    private CameraController controller;

    /**
     * The font used for text rendering.
     */
    private Font font;

    /**
     * The text renderer.
     */
    private TextRenderer textRenderer;

    /**
     * {@inheritDoc}
     *
     * @see Game#init()
     */
    @Override
    public void init() {
        Engine.setClearColor(Color.CORNFLOWER_BLUE);
        Input.setMouseMode(MouseMode.DISABLED);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        final Configuration config = Configuration.getInstance();
        this.mesh = new TerrainMesh(new HeightMap(TERRAIN_SIZE, TERRAIN_SIZE, this.frequency, this.octaves, this.persistence, this.exponent));
        this.terrainRenderer = new TerrainRenderer();
        this.camera = new Camera(new Vector3(-1, 12, -1), new Vector3(1f, -1f, 1f), new Vector3(0f, 1f, 0f),
                (float) Math.toRadians(70f), (float) config.getXResolution() / config.getYResolution(), 0.1f, 1000f);
        this.controller = new CameraController(this.camera);
        this.font = new Font(FileUtils.getResourceAbsolutePath("/fonts/arial/arial.fnt"));
        this.textRenderer = new TextRenderer();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates the camera controller and checks keyboard inputs to change
     * the terrain generation parameters. If one of these parameter changes
     * then the mesh is regenerated.
     *
     * @see Game#update(Time)
     */
    @Override
    public void update(final Time time) {
        this.controller.update(time);
        if (Input.wasKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            this.octaves = Math.max(this.octaves - 1, 1);
            this.dirty = true;
        } else if (Input.wasKeyPressed(GLFW.GLFW_KEY_UP)) {
            this.octaves++;
            this.dirty = true;
        }

        if (Input.wasKeyPressed(GLFW.GLFW_KEY_DELETE)) {
            this.persistence = Math.max(this.persistence - 0.05f, 0.0f);
            this.dirty = true;
        } else if (Input.wasKeyPressed(GLFW.GLFW_KEY_INSERT)) {
            this.persistence += 0.05f;
            this.dirty = true;
        }

        if (Input.wasKeyPressed(GLFW.GLFW_KEY_LEFT)) {
            this.frequency = Math.max(this.frequency - 0.01f, 0);
            this.dirty = true;
        } else if (Input.wasKeyPressed(GLFW.GLFW_KEY_RIGHT)) {
            this.frequency = Math.min(this.frequency + 0.01f, 1.0f);
            this.dirty = true;
        }

        if (Input.wasKeyPressed(GLFW.GLFW_KEY_PAGE_DOWN)) {
            this.exponent = Math.max(this.exponent - 0.01f, 0.01f);
            this.dirty = true;
        } else if (Input.wasKeyPressed(GLFW.GLFW_KEY_PAGE_UP)) {
            this.exponent += 0.01f;
            this.dirty = true;
        }

        if (Input.wasKeyReleased(GLFW.GLFW_KEY_R)) {
            this.frequency = 0.1f;
            this.octaves = 1;
            this.persistence = 1f;
            this.exponent = 1f;
            this.dirty = true;
        }

        if (this.dirty) {
            this.refresh();
        }
    }

    /**
     * Refresh the mesh.
     */
    private void refresh() {
        this.mesh.destroy();
        this.mesh = new TerrainMesh(new HeightMap(TERRAIN_SIZE, TERRAIN_SIZE, this.frequency, this.octaves, this.persistence, this.exponent));
        this.dirty = false;
    }

    /**
     * {@inheritDoc}
     *
     * @see Game#update(Time)
     */
    @Override
    public void render() {
        this.terrainRenderer.render(this.mesh, this.camera);
        float textOffset = 0;
        this.textRenderer.render("frequency : " + this.frequency, this.font, new Vector2(0, textOffset += TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("octaves : " + this.octaves, this.font, new Vector2(0, textOffset += TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("persistence : " + this.persistence, this.font, new Vector2(0, textOffset += TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("exponent : " + this.exponent, this.font, new Vector2(0, textOffset + TEXT_SCALE), TEXT_SCALE, Color.BLACK);
    }

    /**
     * {@inheritDoc}
     *
     * @see Game#destroy()
     */
    @Override
    public void destroy() {
        this.mesh.destroy();
        this.terrainRenderer.destroy();
        this.font.destroy();
        this.textRenderer.destroy();
    }

    public static void main(final String[] args) {
        new Engine(new Landscapes(), "Landscapes").start();
    }

}
