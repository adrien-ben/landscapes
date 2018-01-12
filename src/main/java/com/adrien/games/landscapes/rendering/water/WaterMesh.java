package com.adrien.games.landscapes.rendering.water;

import com.adrien.games.bagl.rendering.DataType;
import com.adrien.games.bagl.rendering.vertex.VertexArray;
import com.adrien.games.bagl.rendering.vertex.VertexBuffer;
import com.adrien.games.bagl.rendering.vertex.VertexBufferParams;
import com.adrien.games.bagl.rendering.vertex.VertexElement;
import org.lwjgl.system.MemoryStack;

/**
 * Water mesh
 *
 * @author adrien
 */
public class WaterMesh {

    /** Minimum vertex x or z position */
    private static final byte MIN = 0;

    /** Maximum vertex x or z position */
    private static final byte MAX = 1;

    /** Number of byte per position */
    private static final int ELEMENTS_PER_POSITION = 2;

    /** Vertex array */
    private final VertexArray vArray;

    /** Vertex buffer */
    private final VertexBuffer vBuffer;

    /**
     * Construct the water mesh
     */
    public WaterMesh() {
        try (final MemoryStack stack = MemoryStack.stackPush()) {
            this.vBuffer = new VertexBuffer(stack.bytes(MIN, MIN, MIN, MAX, MAX, MIN, MAX, MAX), new VertexBufferParams()
                    .dataType(DataType.BYTE)
                    .element(new VertexElement(0, ELEMENTS_PER_POSITION)));
        }
        this.vArray = new VertexArray();
        this.vArray.bind();
        this.vArray.attachVertexBuffer(this.vBuffer);
        this.vArray.unbind();
    }

    /**
     * Bind the water mesh
     */
    public void bind() {
        this.vArray.bind();
    }

    /**
     * Unbind the water mesh
     */
    public void unbind() {
        this.vArray.unbind();
    }

    /**
     * Destroy the water mesh
     */
    public void destroy() {
        this.vBuffer.destroy();
        this.vArray.destroy();
    }
}
