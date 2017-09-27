package com.adrien.games.landscapes;

import com.adrien.games.bagl.core.*;
import com.adrien.games.bagl.core.math.Vector2;
import com.adrien.games.bagl.core.math.Vector3;
import com.adrien.games.bagl.rendering.BlendMode;
import com.adrien.games.bagl.rendering.shape.UIRenderer;
import com.adrien.games.bagl.rendering.text.Font;
import com.adrien.games.bagl.rendering.text.TextRenderer;
import com.adrien.games.bagl.utils.FileUtils;
import com.adrien.games.landscapes.rendering.TerrainMesh;
import com.adrien.games.landscapes.rendering.TerrainRenderer;
import com.adrien.games.landscapes.terrain.HeightMap;
import com.adrien.games.landscapes.ui.Slider;
import com.adrien.games.landscapes.ui.UI;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Landscape generator.
 */
public class Landscapes implements Game {

    /**
     * Scale of the renderer text.
     */
    private static final float TEXT_SCALE = 0.04f;

    /**
     * Size of the terrain.
     */
    private static final int TERRAIN_SIZE = 1000;

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
    private CameraController cameraController;

    /**
     * The font used for text rendering.
     */
    private Font font;

    /**
     * UI.
     */
    private UI ui;

    /**
     * The text renderer.
     */
    private TextRenderer textRenderer;

    /**
     * Control to modify the number of octaves of the generator.
     */
    private Slider octaveSlider;

    /**
     * Control to modify the frequency of the generator.
     */
    private Slider frequencySlider;

    /**
     * Control to modify the persistence of the generator.
     */
    private Slider persistenceSlider;

    /**
     * Control to modify the exponent of the generator.
     */
    private Slider exponentSlider;

    /**
     * The UI renderer.
     */
    private UIRenderer uiRenderer;

    /**
     * Game state
     */
    private State state = State.CAMERA;

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

        this.textRenderer = new TextRenderer();
        this.uiRenderer = new UIRenderer();

        this.ui = new UI(this.uiRenderer);
        this.octaveSlider = new Slider("octaves", 0.1f, 0.1f, 0.8f, 0.02f, 1, 10, 1, 1);
        this.frequencySlider = new Slider("frequency", 0.1f, 0.14f, 0.8f, 0.02f, 0, 1, 0.001f, 0.1f);
        this.persistenceSlider = new Slider("persistence", 0.1f, 0.18f, 0.8f, 0.02f, 0, 10, 0.05f, 1f);
        this.exponentSlider = new Slider("exponent", 0.1f, 0.22f, 0.8f, 0.02f, 0.01f, 5, 0.01f, 1f);
        this.ui.addSlider(this.octaveSlider, newValue -> this.refresh());
        this.ui.addSlider(this.frequencySlider, newValue -> this.refresh());
        this.ui.addSlider(this.persistenceSlider, newValue -> this.refresh());
        this.ui.addSlider(this.exponentSlider, newValue -> this.refresh());

        final Configuration config = Configuration.getInstance();
        this.mesh = new TerrainMesh(new HeightMap(TERRAIN_SIZE, TERRAIN_SIZE, this.frequencySlider.getValue(), (int) this.octaveSlider.getValue(),
                this.persistenceSlider.getValue(), this.exponentSlider.getValue()));
        this.terrainRenderer = new TerrainRenderer();
        this.camera = new Camera(new Vector3(-1, 12, -1), new Vector3(1f, -1f, 1f), new Vector3(0f, 1f, 0f),
                (float) Math.toRadians(70f), (float) config.getXResolution() / config.getYResolution(), 0.1f, 1000f);
        this.cameraController = new CameraController(this.camera);
        this.font = new Font(FileUtils.getResourceAbsolutePath("/fonts/arial/arial.fnt"));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Checks whether the Tab key was pushed. If it was then the game state
     * changes from CAMERA to UI for the other way
     * <p>
     * If the game is in CAMERA state, updates the camera controller. If the game
     * is is UI state then it is the ui controller which is updated.
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
            this.frequencySlider.setValue(0.1f);
            this.octaveSlider.setValue(1);
            this.persistenceSlider.setValue(1f);
            this.exponentSlider.setValue(1f);
            this.refresh();
        }

    }

    /**
     * Refresh the mesh.
     */
    private void refresh() {
        this.mesh.destroy();
        this.mesh = new TerrainMesh(new HeightMap(TERRAIN_SIZE, TERRAIN_SIZE, this.frequencySlider.getValue(), (int) this.octaveSlider.getValue(),
                this.persistenceSlider.getValue(), this.exponentSlider.getValue()));
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
        this.terrainRenderer.render(this.mesh, this.camera);


        float textOffset = 1;
        this.textRenderer.render("exponent : " + this.exponentSlider.getValue(), this.font, new Vector2(0, textOffset -= TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("persistence : " + this.persistenceSlider.getValue(), this.font, new Vector2(0, textOffset -= TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("frequency : " + this.frequencySlider.getValue(), this.font, new Vector2(0, textOffset -= TEXT_SCALE), TEXT_SCALE, Color.BLACK);
        this.textRenderer.render("octaves : " + this.octaveSlider.getValue(), this.font, new Vector2(0, textOffset - TEXT_SCALE), TEXT_SCALE, Color.BLACK);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        Engine.setBlendMode(BlendMode.TRANSPARENCY);
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
