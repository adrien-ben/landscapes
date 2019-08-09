package com.adrien.games.landscapes.rendering.terrain;


import com.adrien.games.landscapes.terrain.HeightMap;
import com.adrienben.games.bagl.core.Color;
import com.adrienben.games.bagl.opengl.buffer.BufferUsage;
import com.adrienben.games.bagl.opengl.vertex.*;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * 3D mesh of a terrain
 * <p>
 * The mesh is generated from a {@link HeightMap}. It generates one vertex per
 * point in the height map
 *
 * @author adrien
 */
public class TerrainMesh {

    private static final int ELEMENTS_PER_VERTEX = 9;
    private static final int ELEMENTS_PER_POSITION = 3;
    private static final int ELEMENTS_PER_NORMAL = 3;
    private static final int ELEMENTS_PER_COLOR = 3;
    private static final int POSITION_ELEMENTS_INDEX = 0;
    private static final int NORMAL_ELEMENTS_INDEX = 1;
    private static final int COLOR_ELEMENTS_INDEX = 2;
    public static final int INDICES_PER_POLYGON = 3;

    private static final Color SAND = new Color(0.93f, 0.79f, 0.69f);
    private static final Color GRASS = new Color(0.2f, 0.5f, 0.0f);
    private static final Color DIRT = new Color(0.61f, 0.46f, 0.32f);
    private static final Color SNOW = new Color(1.0f, 1.0f, 1.0f);

    private final VertexArray vArray;
    private final VertexBuffer vBuffer;
    private final IndexBuffer iBuffer;
    private final int polygonCount;

    /**
     * Generate a new mesh
     *
     * @param map The height map from which to generate the mesh
     */
    public TerrainMesh(final HeightMap map) {
        this.polygonCount = (map.getWidth() - 1) * (map.getDepth() - 1) * 2;

        final var vertexData = this.generateVertexData(map);
        this.vBuffer = new VertexBuffer(vertexData, VertexBufferParams.builder()
                .element(new VertexElement(POSITION_ELEMENTS_INDEX, ELEMENTS_PER_POSITION))
                .element(new VertexElement(NORMAL_ELEMENTS_INDEX, ELEMENTS_PER_NORMAL))
                .element(new VertexElement(COLOR_ELEMENTS_INDEX, ELEMENTS_PER_COLOR)).build());
        MemoryUtil.memFree(vertexData);

        this.vArray = new VertexArray();
        this.vArray.bind();
        this.vArray.attachVertexBuffer(this.vBuffer);
        this.vArray.unbind();

        final var indexData = this.generateIndexData(map);
        this.iBuffer = new IndexBuffer(indexData, BufferUsage.STATIC_DRAW);
        MemoryUtil.memFree(indexData);
    }

    /**
     * Generate the float buffer containing vertex data to send to the gpu
     *
     * @param map The height map containing terrain data
     * @return The generated buffer
     */
    private FloatBuffer generateVertexData(final HeightMap map) {
        final int vertexCount = map.getWidth() * map.getDepth();
        final var vertexData = MemoryUtil.memAllocFloat(vertexCount * ELEMENTS_PER_VERTEX);
        for (int i = 0; i < vertexCount; i++) {
            final int x = i / map.getDepth();
            final int z = i % map.getDepth();
            final float height = map.getHeight(x, z);
            vertexData.put(i * ELEMENTS_PER_VERTEX, x);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 1, height);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 2, z);

            final var normal = this.computeVertexNormal(map, x, z);
            vertexData.put(i * ELEMENTS_PER_VERTEX + 3, normal.x());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 4, normal.y());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 5, normal.z());

            final var color = this.computeColor(height, map.getScale());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 6, color.getRed());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 7, color.getGreen());
            vertexData.put(i * ELEMENTS_PER_VERTEX + 8, color.getBlue());
        }
        return vertexData;
    }

    /**
     * Generate vertex index data
     *
     * @param map The height map containing terrain data
     * @return he generated buffer
     */
    private IntBuffer generateIndexData(final HeightMap map) {
        final var indexData = MemoryUtil.memAllocInt(this.polygonCount * INDICES_PER_POLYGON);
        int nextIndex = 0;
        for (int x = 0; x < map.getWidth() - 1; x++) {
            for (int z = 0; z < map.getDepth() - 1; z++) {
                final int index0 = x * map.getDepth() + z;
                final int index1 = (x + 1) * map.getDepth() + z;
                final int index2 = x * map.getDepth() + z + 1;
                final int index3 = (x + 1) * map.getDepth() + z + 1;
                indexData.put(nextIndex++, index0);
                indexData.put(nextIndex++, index2);
                indexData.put(nextIndex++, index1);
                indexData.put(nextIndex++, index1);
                indexData.put(nextIndex++, index2);
                indexData.put(nextIndex++, index3);
            }
        }
        return indexData;
    }

    /**
     * Compute the normal of a vertex by averaging the normals of the surrounding faces
     *
     * @param map The height map
     * @param x   The x index of the vertex
     * @param z   The z index of the vertex
     * @return The averaged normal of the vertex
     */
    private Vector3f computeVertexNormal(final HeightMap map, final int x, final int z) {
        final var normal = new Vector3f();
        if (x > 0 && z > 0) {
            normal.add(this.computeFaceNormal(map, x, z, -1));
        }
        if (x < map.getWidth() - 1 && z > 0) {
            normal.add(this.computeFaceNormal(map, x, z - 1, 1));
            normal.add(this.computeFaceNormal(map, x + 1, z, -1));
        }
        if (x < map.getWidth() - 1 && x < map.getDepth() - 1) {
            normal.add(this.computeFaceNormal(map, x, z, 1));
        }
        if (x > 0 && z < map.getDepth() - 1) {
            normal.add(this.computeFaceNormal(map, x, z + 1, -1));
            normal.add(this.computeFaceNormal(map, x - 1, z, 1));
        }
        return normal.normalize();
    }

    /**
     * Compute the orientation of a face from the elevation delta with its neighbors
     *
     * @param map    The height map
     * @param x      The x index of the vertex
     * @param z      The y index of the vertex
     * @param offset Amount to offset the current vertex index to create the face
     * @return The computed normal vector
     */
    private Vector3f computeFaceNormal(final HeightMap map, final int x, final int z, final int offset) {
        final float height = map.getHeight(x, z);
        final int adjacentX = x + offset;
        final int adjacentZ = z + offset;
        final float adjacentHeightX = map.getHeight(adjacentX, z);
        final float adjacentHeightZ = map.getHeight(x, adjacentZ);

        final var xVector = new Vector3f(offset, adjacentHeightX - height, 0f).normalize();
        final var zVector = new Vector3f(0f, adjacentHeightZ - height, offset).normalize();
        return new Vector3f(zVector).cross(xVector).normalize();
    }

    /**
     * Generate the color of a vertex from its elevate and its orientation
     *
     * @param height The height of the vertex
     * @return The computed color
     */
    private Color computeColor(final float height, final int scale) {
        final float sandLimit = 0.35f * scale;
        final float grassLimit = 0.44f * scale;
        final float dirtLimit = 0.65f * scale;
        final float transitionHeight = 0.08f * scale;

        if (height < sandLimit) {
            return SAND;
        }
        if (height < sandLimit + transitionHeight) {
            final float blendFactor = (height - sandLimit) / transitionHeight;
            final var blended = new Color(0f, 0f, 0f);
            Color.blend(GRASS, SAND, blendFactor, blended);
            return blended;
        }
        if (height < grassLimit) {
            return GRASS;
        }
        if (height < grassLimit + transitionHeight) {
            final float blendFactor = (height - grassLimit) / transitionHeight;
            final var blended = new Color(0f, 0f, 0f);
            Color.blend(DIRT, GRASS, blendFactor, blended);
            return blended;
        }
        if (height < dirtLimit) {
            return DIRT;
        }
        if (height < dirtLimit + transitionHeight) {
            final float blendFactor = (height - dirtLimit) / transitionHeight;
            final var blended = new Color(0f, 0f, 0f);
            Color.blend(SNOW, DIRT, blendFactor, blended);
            return blended;
        }
        return SNOW;
    }

    /**
     * Bind the current mesh
     */
    public void bind() {
        this.vArray.bind();
        this.iBuffer.bind();
    }

    /**
     * Unbind the current mesh
     */
    public void unbind() {
        this.iBuffer.unbind();
        this.vArray.unbind();
    }

    /**
     * Destroy the current mesh
     * <p>
     * Delete all OGL buffers
     */
    public void destroy() {
        this.iBuffer.destroy();
        this.vBuffer.destroy();
        this.vArray.destroy();
    }

    public int getPolygonCount() {
        return polygonCount;
    }

}
