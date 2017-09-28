package com.adrien.games.landscapes.rendering;

import com.adrien.games.bagl.core.Color;
import com.adrien.games.bagl.core.math.Vector3;
import com.adrien.games.landscapes.terrain.HeightMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * 3D mesh of a terrain.
 * <p>
 * The mesh is generated from a {@link HeightMap}. It generates one vertex per
 * point in the height map.
 */
public class TerrainMesh {

    /**
     * Number of elements in one vertex.
     */
    private static final int ELEMENTS_PER_VERTEX = 9;

    /**
     * Vertex buffer stride.
     */
    private static final int STRIDE = ELEMENTS_PER_VERTEX * Float.SIZE / 8;

    /**
     * Number of elements per positions.
     */
    private static final int ELEMENTS_PER_POSITION = 3;

    /**
     * Number of elements per normal.
     */
    private static final int ELEMENTS_PER_NORMAL = 3;

    /**
     * Number of elements per color.
     */
    private static final int ELEMENTS_PER_COLOR = 3;

    /**
     * Byte offset of positions in buffer
     */
    private static final int POSITION_OFFSET = 0;

    /**
     * Index of the positions in the vertex array.
     */
    private static final int POSITION_ELEMENTS_INDEX = 0;

    /**
     * Index of the normals in the vertex array.
     */
    private static final int NORMAL_ELEMENTS_INDEX = 1;

    /**
     * Byte offset of normals in buffer
     */
    private static final int NORMAL_OFFSET = ELEMENTS_PER_POSITION * Float.SIZE / 8;

    /**
     * Index of the colors in the vertex array.
     */
    private static final int COLOR_ELEMENTS_INDEX = 2;

    /**
     * Byte offset of colors in buffer
     */
    private static final int COLOR_OFFSET = (ELEMENTS_PER_NORMAL + ELEMENTS_PER_POSITION) * Float.SIZE / 8;

    /**
     * Number of indices per polygons.
     */
    public static final int INDICES_PER_POLYGON = 3;

    /**
     * Deep water color.
     */
    private static final Color DEEP_WATER = new Color(0.0f, 0.0f, 1.0f);

    /**
     * Deep water color.
     */
    private static final Color WATER = new Color(0.0f, 0.5f, 1.0f);

    /**
     * Water color.
     */
    private static final Color SAND = new Color(0.93f, 0.79f, 0.69f);

    /**
     * Grass color.
     */
    private static final Color GRASS = new Color(0.2f, 0.5f, 0.0f);

    /**
     * Dirt color.
     */
    private static final Color DIRT = new Color(0.61f, 0.46f, 0.32f);

    /**
     * Snow color.
     */
    private static final Color SNOW = new Color(1.0f, 1.0f, 1.0f);

    /**
     * OGL vertex array.
     */
    private final int vao;

    /**
     * OGL vertex buffer.
     */
    private final int vbo;

    /**
     * OGL index buffer.
     */
    private final int ibo;

    /**
     * The number of polygon composing the terrain.
     */
    private final int polygonCount;

    /**
     * Generates a new mesh.
     *
     * @param map The height map from which to generate the mesh.
     */
    public TerrainMesh(final HeightMap map) {
        this.polygonCount = (map.getWidth() - 1) * (map.getDepth() - 1) * 2;

        this.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vao);

        final FloatBuffer vertexData = this.generateVertexData(map);
        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(POSITION_ELEMENTS_INDEX);
        GL20.glVertexAttribPointer(POSITION_ELEMENTS_INDEX, ELEMENTS_PER_POSITION, GL11.GL_FLOAT, false, STRIDE, POSITION_OFFSET);

        GL20.glEnableVertexAttribArray(NORMAL_ELEMENTS_INDEX);
        GL20.glVertexAttribPointer(NORMAL_ELEMENTS_INDEX, ELEMENTS_PER_NORMAL, GL11.GL_FLOAT, false, STRIDE, NORMAL_OFFSET);

        GL20.glEnableVertexAttribArray(COLOR_ELEMENTS_INDEX);
        GL20.glVertexAttribPointer(COLOR_ELEMENTS_INDEX, ELEMENTS_PER_COLOR, GL11.GL_FLOAT, false, STRIDE, COLOR_OFFSET);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        MemoryUtil.memFree(vertexData);
        
        final IntBuffer indexData = this.generateIndexData(map);
        this.ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(indexData);
    }

    /**
     * Generates the float buffer containing vertex data to send to the gpu.
     *
     * @param map The height map containing terrain data.
     * @return The generated buffer.
     */
    private FloatBuffer generateVertexData(final HeightMap map) {
        final int vertexCount = map.getWidth() * map.getDepth();
        final FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertexCount * ELEMENTS_PER_VERTEX);
        for (int i = 0; i < vertexCount; i++) {
            final int x = i / map.getDepth();
            final int z = i % map.getDepth();
            final float height = map.getHeight(x, z);
            vertexData.put(i * ELEMENTS_PER_VERTEX, x);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 1, height);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 2, z);

            final Vector3 normal = this.computeNormal(map, x, z, height);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 3, normal.getX());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 4, normal.getY());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 5, normal.getZ());

            final Color color = this.computeColor(height, map.getScale(), normal);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 6, color.getRed());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 7, color.getGreen());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 8, color.getBlue());
        }
        return vertexData;
    }

    /**
     * Generates vertex index data.
     *
     * @param map The height map containing terrain data.
     * @return he generated buffer.
     */
    public IntBuffer generateIndexData(final HeightMap map) {
        final IntBuffer indexData = MemoryUtil.memAllocInt(this.polygonCount * INDICES_PER_POLYGON);
        int nextIndex = 0;
        for (int x = 0; x < map.getWidth() - 1; x++) {
            for (int z = 0; z < map.getDepth() - 1; z++) {
                final int index0 = x * map.getDepth() + z;
                final int index1 = (x + 1) * map.getDepth() + z;
                final int index2 = x * map.getDepth() + z + 1;
                final int index3 = (x + 1) * map.getDepth() + z + 1;
                indexData.put(nextIndex++, index0);
                indexData.put(nextIndex++, index2);
                indexData.put(nextIndex++, index3);
                indexData.put(nextIndex++, index3);
                indexData.put(nextIndex++, index1);
                indexData.put(nextIndex++, index0);
            }
        }
        return indexData;
    }

    /**
     * Computes the orientation of a vertex from the elevation delta with its neighbors.
     *
     * @param map    The height map.
     * @param x      The x index of the vertex.
     * @param z      The y index of the vertex.
     * @param height The height of the current vertex.
     * @return The computed normal vector.
     */
    private Vector3 computeNormal(final HeightMap map, final int x, final int z, final float height) {
        final int adjacentX = x < map.getWidth() - 1 ? x + 1 : x - 1;
        final int adjacentZ = z < map.getDepth() - 1 ? z + 1 : z - 1;
        final float adjacentHeightX = map.getHeight(adjacentX, z);
        final float adjacentHeightZ = map.getHeight(x, adjacentZ);

        final Vector3 xVector = new Vector3(1f, adjacentHeightX - height, 0f).normalise();
        final Vector3 zVector = new Vector3(0f, adjacentHeightZ - height, 1f).normalise();
        return Vector3.cross(zVector, xVector).normalise();
    }

    /**
     * Generates the color of a vertex from its elevate and its orientation.
     *
     * @param height The height of the vertex.
     * @param normal The orientation of the vertex.
     * @return The computed color.
     */
    private Color computeColor(final float height, final int scale, final Vector3 normal) {
        final float angle = Vector3.UP.dot(normal);
        if (height < 0.3 * scale) {
            return DEEP_WATER;
        }
        if (height < 0.35 * scale) {
            return WATER;
        }
        if (height < 0.38 * scale) {
            return SAND;
        }
        if (height < 0.45 * scale) {
            return GRASS;
        }
        if (height < 0.55 * scale) {
            if (angle > 0.8) {
                return GRASS;
            }
            return DIRT;
        }
        if (height < 0.65 * scale) {
            return DIRT;
        }
        if (angle > 0.8) {
            return SNOW;
        }
        return DIRT;
    }

    /**
     * Binds the current mesh.
     */
    public void bind() {
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
    }

    /**
     * Unbinds the current mesh.
     */
    public void unbind() {
        GL30.glBindVertexArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Destroys the current mesh.
     * <p>
     * Deletes all OGL buffers.
     */
    public void destroy() {
        GL30.glDeleteVertexArrays(this.vao);
        GL15.glDeleteBuffers(this.vbo);
        GL15.glDeleteBuffers(this.ibo);
    }

    public int getPolygonCount() {
        return polygonCount;
    }

}
