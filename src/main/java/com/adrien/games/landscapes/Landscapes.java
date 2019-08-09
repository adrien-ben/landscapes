package com.adrien.games.landscapes;


import com.adrien.games.landscapes.rendering.terrain.TerrainMesh;
import com.adrien.games.landscapes.rendering.terrain.TerrainRenderer;
import com.adrien.games.landscapes.rendering.water.WaterMesh;
import com.adrien.games.landscapes.rendering.water.WaterRenderer;
import com.adrien.games.landscapes.terrain.HeightMap;
import com.adrien.games.landscapes.terrain.HeightMapParameters;
import com.adrien.games.landscapes.ui.UI;
import com.adrien.games.landscapes.ui.controls.CheckBox;
import com.adrien.games.landscapes.ui.controls.Slider;
import com.adrienben.games.bagl.core.Color;
import com.adrienben.games.bagl.core.io.ResourcePath;
import com.adrienben.games.bagl.engine.*;
import com.adrienben.games.bagl.engine.camera.Camera;
import com.adrienben.games.bagl.engine.camera.CameraController;
import com.adrienben.games.bagl.engine.camera.FPSCameraController;
import com.adrienben.games.bagl.engine.game.Game;
import com.adrienben.games.bagl.engine.rendering.light.DirectionalLight;
import com.adrienben.games.bagl.engine.rendering.light.Light;
import com.adrienben.games.bagl.engine.rendering.shape.UIRenderer;
import com.adrienben.games.bagl.engine.rendering.text.Font;
import com.adrienben.games.bagl.engine.rendering.text.Text;
import com.adrienben.games.bagl.engine.rendering.text.TextRenderer;
import com.adrienben.games.bagl.opengl.OpenGL;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Landscape generator
 *
 * @author adrien
 */
public class Landscapes implements Game {

    private static final int TERRAIN_SIZE = 800;
    private static final int HEIGHT_SCALE = 128;

    private Camera camera;
    private CameraController cameraController;
    private TerrainRenderer terrainRenderer;
    private HeightMapParameters mapParameters;
    private TerrainMesh mesh;
    private boolean dirtyMesh;
    private WaterRenderer waterRenderer;
    private WaterMesh waterMesh;
    private boolean renderWater;
    private Light ambient;
    private DirectionalLight sun;
    private TextRenderer textRenderer;
    private UIRenderer uiRenderer;
    private Font font;
    private UI ui;
    private State state = State.UI;

    /**
     * {@inheritDoc}
     *
     * @see Game#init()
     */
    @Override
    public void init() {
        OpenGL.setClearColor(Color.CORNFLOWER_BLUE);
        GL11.glCullFace(GL11.GL_BACK);

        final var config = Configuration.getInstance();

        this.camera = new Camera(new Vector3f(TERRAIN_SIZE / 10, HEIGHT_SCALE * 2, TERRAIN_SIZE / 10), new Vector3f(1f, -1f, 1f),
                new Vector3f(0f, 1f, 0f), (float) Math.toRadians(70f), (float) config.getXResolution() / config.getYResolution(),
                0.1f, 1000f);
        this.cameraController = new FPSCameraController(this.camera);

        this.terrainRenderer = new TerrainRenderer();
        this.mapParameters = HeightMapParameters.create().width(TERRAIN_SIZE).depth(TERRAIN_SIZE).scale(HEIGHT_SCALE).frequency(0.012f).octaves(6)
                .persistence(0.4f).exponent(1.16f);
        this.mesh = new TerrainMesh(new HeightMap(this.mapParameters));
        this.dirtyMesh = false;

        this.waterRenderer = new WaterRenderer();
        this.waterMesh = new WaterMesh();
        this.renderWater = true;

        this.ambient = new Light(0.3f, Color.WHITE);
        this.sun = new DirectionalLight(1f, Color.WHITE, new Vector3f(-1f, -1f, -1f));

        this.textRenderer = new TextRenderer();
        this.uiRenderer = new UIRenderer();
        this.font = new Font(ResourcePath.get("classpath:/fonts/arial/arial.fnt"));
        this.ui = new UI(this.uiRenderer, this.textRenderer, this.font);
        this.setUpUI();
    }

    /**
     * Sets up UI elements
     */
    private void setUpUI() {
        final var octavesSlider = new Slider("octaves", "octaves", 0.005f, 0.005f, 0.4f, 0.02f, 1, 10, 1, this.mapParameters.getOctaves());
        final var frequencySlider = new Slider("frequency", "frequency", 0.005f, 0.065f, 0.4f, 0.02f, 0, 0.1f, 0.001f,
                this.mapParameters.getFrequency());
        final var persistenceSlider = new Slider("persistence", "persistence", 0.005f, 0.125f, 0.4f, 0.02f, 0, 10, 0.05f,
                this.mapParameters.getPersistence());
        final var exponentSlider = new Slider("exponent", "exponent", 0.005f, 0.185f, 0.4f, 0.02f, 0.01f, 5, 0.01f,
                this.mapParameters.getExponent());
        final var waterToggle = new CheckBox("waterToggle", "Display water", 0.005f, 0.245f, 0.04f, true);
        this.ui.add(octavesSlider, octaves -> {
            this.mapParameters.octaves((int) octaves);
            this.dirtyMesh = true;
        });
        this.ui.add(frequencySlider, frequency -> {
            this.mapParameters.frequency(frequency);
            this.dirtyMesh = true;
        });
        this.ui.add(persistenceSlider, persistence -> {
            this.mapParameters.persistence(persistence);
            this.dirtyMesh = true;
        });
        this.ui.add(exponentSlider, exponent -> {
            this.mapParameters.exponent(exponent);
            this.dirtyMesh = true;
        });
        this.ui.add(waterToggle, checked -> this.renderWater = checked);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Check whether the Tab key was pushed. If it was then the game state
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
     * Refresh the mesh
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
        this.terrainRenderer.render(this.mesh, this.camera, this.ambient, this.sun);
        if (this.renderWater) {
            this.waterRenderer.render(this.waterMesh, TERRAIN_SIZE, TERRAIN_SIZE, 46f, this.camera, this.ambient, this.sun);
        }
        final var text = Text.create(this.state.toString() + " MODE", this.font, 0.0f, 0.9f, 0.1f, Color.WHITE);
        this.textRenderer.render(text);
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
        this.waterRenderer.destroy();
        this.waterMesh.destroy();
        this.font.destroy();
        this.textRenderer.destroy();
        this.uiRenderer.destroy();
    }

    public static void main(final String[] args) {
        new Engine(new Landscapes(), "Landscapes").start();
    }

}
