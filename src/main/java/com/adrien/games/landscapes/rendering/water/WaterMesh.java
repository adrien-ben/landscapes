package com.adrien.games.landscapes.rendering.water;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;

/**
 * Water mesh.
 */
public class WaterMesh {

    /**
     * Minimum vertex x or z position.
     */
    private static final byte MIN = 0;

    /**
     * Maximum vertex x or z position.
     */
    private static final byte MAX = 1;

    /**
     * Number of byte per position.
     */
    private static final int ELEMENTS_PER_POSITION = 2;

    /**
     * Data stride of the position buffer.
     */
    private static final int STRIDE = ELEMENTS_PER_POSITION * Byte.SIZE / 8;

    /**
     * OGL vertex array.
     */
    private final int vao;

    /**
     * OGL vertex buffer.
     */
    private final int vbo;

    /**
     * Constructs the water mesh.
     */
    public WaterMesh() {
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);

        try (final MemoryStack stack = MemoryStack.stackPush()) {
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, stack.bytes(MIN, MIN, MIN, MAX, MAX, MIN, MAX, MAX), GL15.GL_STATIC_DRAW);
        }

        GL20.glEnableVertexAttribArray(0);
        GL30.glVertexAttribIPointer(0, ELEMENTS_PER_POSITION, GL11.GL_BYTE, STRIDE, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    /**
     * Binds the water mesh.
     */
    public void bind() {
        GL30.glBindVertexArray(this.vao);
    }

    /**
     * Unbinds the water mesh.
     */
    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    /**
     * Destroys the water mesh.
     */
    public void destroy() {
        GL30.glDeleteVertexArrays(this.vao);
        GL15.glDeleteBuffers(this.vbo);
    }

}
