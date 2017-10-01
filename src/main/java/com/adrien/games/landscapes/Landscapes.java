package com.adrien.games.landscapes;

import com.adrien.games.bagl.core.*;
import com.adrien.games.bagl.core.math.Vector2;
import com.adrien.games.bagl.core.math.Vector3;
import com.adrien.games.bagl.rendering.BlendMode;
import com.adrien.games.bagl.rendering.light.DirectionalLight;
import com.adrien.games.bagl.rendering.light.Light;
import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.bagl.utils.FileUtils;
import com.adrien.games.landscapes.rendering.TerrainMesh;
import com.adrien.games.landscapes.rendering.TerrainRenderer;
import com.adrien.games.landscapes.terrain.HeightMap;
import com.adrien.games.landscapes.terrain.HeightMapParameters;
import com.adrien.games.landscapes.ui.Slider;
import com.adrien.games.landscapes.ui.UI;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Landscape generator.
 */
public class Landscapes implements Game {

    /**
     * Size of the terrain.
     */
    private static final int TERRAIN_SIZE = 1000;

    /**
     * Height scale of the terrain.
     */
    private static final int HEIGHT_SCALE = 128;

    /**
     * The camera.
     */
    private Camera camera;

    /**
     * The camera controller.
     */
    private CameraController cameraController;

    /**
     * The terrain renderer.
     */
    private TerrainRenderer terrainRenderer;

    /**
     * The parameters of the height map.
     */
    private HeightMapParameters mapParameters;

    /**
     * The terrain mesh.
     */
    private TerrainMesh mesh;

    /**
     * Should the mesh be regenerated ?
     */
    private boolean dirtyMesh;

    /**
     * Ambient light of the scene.
     */
    private Light ambient;

    /**
     * Sun light of the scene.
     */
    private DirectionalLight sun;

    /**
     * The text renderer.
     */
    private TextRenderer textRenderer;

    /**
     * The UI renderer.
     */
    private UIRenderer uiRenderer;

    /**
     * The font used for text rendering.
     */
    private Font font;

    /**
     * UI.
     */
    private UI ui;

    /**
     * Game state
     */
    private State state = State.UI;

    /**
     * {@inheritDoc}
     *
     * @see Game#init()
     */
    @Override
    public void init() {
        Engine.setClearColor(Color.CORNFLOWER_BLUE);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        final Configuration config = Configuration.getInstance();

        this.camera = new Camera(new Vector3(TERRAIN_SIZE / 10, HEIGHT_SCALE * 2, TERRAIN_SIZE / 10), new Vector3(1f, -1f, 1f),
                new Vector3(0f, 1f, 0f), (float) Math.toRadians(70f), (float) config.getXResolution() / config.getYResolution(),
                0.1f, 1000f);
        this.cameraController = new CameraController(this.camera);

        this.terrainRenderer = new TerrainRenderer();
        this.mapParameters = HeightMapParameters.create().width(TERRAIN_SIZE).depth(TERRAIN_SIZE).scale(HEIGHT_SCALE).frequency(0.012f).octaves(6)
                .persistence(0.4f).exponent(1.16f);
        this.mesh = new TerrainMesh(new HeightMap(this.mapParameters));
        this.dirtyMesh = false;

        this.ambient = new Light(0.3f, Color.WHITE);
        this.sun = new DirectionalLight(1f, Color.WHITE, new Vector3(1f, -1f, 1f));

        this.textRenderer = new TextRenderer();
        this.uiRenderer = new UIRenderer();
        this.font = new Font(FileUtils.getResourceAbsolutePath("/fonts/arial/arial.fnt"));
        this.ui = new UI(this.uiRenderer, this.textRenderer, this.font);
        this.setUpUI();
    }

    /**
     * Sets up UI elements.
     */
    private void setUpUI() {
        final Slider octavesSlider = new Slider("octaves", 0.005f, 0.005f, 0.4f, 0.02f, 1, 10, 1, this.mapParameters.getOctaves());
        final Slider frequencySlider = new Slider("frequency", 0.005f, 0.065f, 0.4f, 0.02f, 0, 0.1f, 0.001f, this.mapParameters.getFrequency());
        final Slider persistenceSlider = new Slider("persistence", 0.005f, 0.125f, 0.4f, 0.02f, 0, 10, 0.05f, this.mapParameters.getPersistence());
        final Slider exponentSlider = new Slider("exponent", 0.005f, 0.185f, 0.4f, 0.02f, 0.01f, 5, 0.01f, this.mapParameters.getExponent());
        this.ui.addSlider(octavesSlider, octaves -> {
            this.mapParameters.octaves((int) octaves);
            this.dirtyMesh = true;
        });
        this.ui.addSlider(frequencySlider, frequency -> {
            this.mapParameters.frequency(frequency);
            this.dirtyMesh = true;
        });
        this.ui.addSlider(persistenceSlider, persistence -> {
            this.mapParameters.persistence(persistence);
            this.dirtyMesh = true;
        });
        this.ui.addSlider(exponentSlider, exponent -> {
            this.mapParameters.exponent(exponent);
            this.dirtyMesh = true;
        });
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks whether the Tab key was pushed. If it was then the game state
     * changes from CAMERA to UI for the other way
     * <p>
     * If the game is in CAMERA state, updates the camera controller to allow
     * camera movement. If the game is is UI state then it is the ui controller
     * which is updated to allow ui interaction.
     *
     * @see Game#update(Time)
     */
    @Override
    public void update(final Time time) {
        if (Input.wasKeyPressed(GLFW.GLFW_KEY_TAB)) {
            if (this.state == State.CAMERA) {
                this.state = State.UI;
                Input.setMouseMode(MouseMode.NORMAL);
            } else {
                this.state = State.CAMERA;
                Input.setMouseMode(MouseMode.DISABLED);
            }
        }

        if (this.state == State.CAMERA) {
            this.cameraController.update(time);
        } else {
            this.ui.update();
        }

        if (Input.wasKeyReleased(GLFW.GLFW_KEY_R)) {
            this.mapParameters.octaves(6);
            this.mapParameters.frequency(0.012f);
            this.mapParameters.persistence(0.4f);
            this.mapParameters.exponent(1.16f);
            this.dirtyMesh = true;
        }

        if (this.dirtyMesh) {
            this.refresh();
        }
    }

    /**
     * Refresh the mesh.
     */
    private void refresh() {
        this.mesh.destroy();
        this.mesh = new TerrainMesh(new HeightMap(this.mapParameters));
        this.dirtyMesh = false;
    }

    /**
     * {@inheritDoc}
     *
     * @see Game#update(Time)
     */
    @Override
    public void render() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        Engine.setBlendMode(BlendMode.DEFAULT);
        this.terrainRenderer.render(this.mesh, this.camera, this.ambient, this.sun);

        this.textRenderer.render(this.state.toString() + " MODE", this.font, new Vector2(0.0f, 0.9f), 0.1f, Color.WHITE);
        this.uiRenderer.start();
        this.ui.render();
        this.uiRenderer.end();
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
        this.uiRenderer.destroy();
    }

    public static void main(final String[] args) {
        new Engine(new Landscapes(), "Landscapes").start();
    }

}
