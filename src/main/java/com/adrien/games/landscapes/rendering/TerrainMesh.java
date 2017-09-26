package com.adrien.games.landscapes.rendering;

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
     * Vertex buffer stride.
     */
    private static final int STRIDE = 3 * Float.SIZE / 8;

    /**
     * Number of elements per positions.
     */
    private static final int ELEMENTS_PER_POSITION = 3;

    /**
     * Index of the positions in the vertex array.
     */
    private static final int POSITION_ELEMENTS_INDEX = 0;

    /**
     * Number of indices per polygons.
     */
    public static final int INDICES_PER_POLYGON = 3;

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
        final int vertexCount = map.getWidth() * map.getDepth();
        final FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertexCount * ELEMENTS_PER_POSITION);
        for (int i = 0; i < vertexCount; i++) {
            final int x = i / map.getDepth();
            final int z = i % map.getDepth();
            vertexData.put(x);
            vertexData.put(map.getHeight(x, z));
            vertexData.put(z);
        }
        vertexData.flip();

        this.vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(this.vao);

        this.vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        GL20.glEnableVertexAttribArray(POSITION_ELEMENTS_INDEX);
        GL20.glVertexAttribPointer(POSITION_ELEMENTS_INDEX, ELEMENTS_PER_POSITION, GL11.GL_FLOAT, false, STRIDE, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        MemoryUtil.memFree(vertexData);

        this.polygonCount = (map.getWidth() - 1) * (map.getDepth() - 1) * 2;
        final IntBuffer indexData = MemoryUtil.memAllocInt(this.polygonCount * INDICES_PER_POLYGON);
        for (int x = 0; x < map.getWidth() - 1; x++) {
            for (int z = 0; z < map.getDepth() - 1; z++) {
                // FIXME : winding seems inverted...
                final int index0 = x * map.getDepth() + z;
                final int index1 = (x + 1) * map.getDepth() + z;
                final int index2 = x * map.getDepth() + z + 1;
                final int index3 = (x + 1) * map.getDepth() + z + 1;
                indexData.put(index0);
                indexData.put(index2);
                indexData.put(index1);
                indexData.put(index1);
                indexData.put(index2);
                indexData.put(index3);
            }
        }
        indexData.flip();

        this.ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexData, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        MemoryUtil.memFree(indexData);
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
