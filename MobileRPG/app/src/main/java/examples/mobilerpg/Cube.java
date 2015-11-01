package examples.mobilerpg;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.sql.ParameterMetaData;

/**
 * Created by Arjun on 10/30/2015.
 */
public class Cube {
    private FloatBuffer positionBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer normalBuffer;

    private final int bytePerFloat = 4;
    private final int mPositionDataSize = 3;
    private final int mColorDataSize = 4;
    private final int mNormalDataSize = 3;

    private final int vertexCount = 36;

    private float[] mLightModelPos = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    private float[] mLightWorldPos = new float[4];
    private float[] mLightEyePos = new float[4];
    private float[] modelMatrix;
    private float[] lightModelMatrix = new float[16];
    private float[] position;//these should affect the model matrix?
    private float[] rotation;

    // X, Y, Z
    final float[] cubePositionData =
            {
                    // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                    // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                    // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                    // usually represent the backside of an object and aren't visible anyways.

                    // Front face
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,

                    // Right face
                    1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,

                    // Back face
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,

                    // Left face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,

                    // Top face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,

                    // Bottom face
                    1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
            };

    // R, G, B, A
    final float[] cubeColorData =
            {
                    // Front face (red)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,

                    // Right face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // Back face (blue)
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,

                    // Left face (yellow)
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,

                    // Top face (cyan)
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,

                    // Bottom face (magenta)
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f
            };

    // X, Y, Z
    // The normal is used in light calculations and is a vector which points
    // orthogonal to the plane of the surface. For a cube model, the normals
    // should be orthogonal to the points of each face.
    final float[] cubeNormalData =
            {
                    // Front face
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,

                    // Right face
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,

                    // Back face
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,

                    // Left face
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,

                    // Top face
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,

                    // Bottom face
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f
            };

    private final int cVertexProgram;
    private final int cPointProgram;
    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mLightPosHandle;
    private int mPositionHandle;
    private int mColorHandle;
    private int mNormalHandle;

    private final String vertexShaderCode =
              "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
            + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
            + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.

            + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
            + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
            + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in.

            + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

            + "void main()                    \n"     // The entry point for our vertex shader.
            + "{                              \n"
            // Transform the vertex into eye space.
                    + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
            // Transform the normal's orientation into eye space.
                    + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
            // Will be used for attenuation.
                    + "   float distance = length(u_LightPos - modelViewVertex);             \n"
            // Get a lighting direction vector from the light to the vertex.
                    + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
            // Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
            // pointing in the same direction then it will get max illumination.
                    + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
            // Attenuate the light based on distance.
                    + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
            // Multiply the color by the illumination level. It will be interpolated across the triangle.
                    + "   v_Color = a_Color * diffuse;                                       \n"
            // gl_Position is a special variable used to store the final position.
            // Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                    + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                    + "}                                                                     \n";

    private final String fragmentShaderCode =
            "precision mediump float;" + //Set the default precision to medium. We don't need high precision in fragment
            "varying vec4 v_Color;" + //this is the color from the vertex shader interpolated across triangle per fragment

            "void main() {" +
                "gl_FragColor = v_Color;" + //pass color directly through the pipeline
            "}";

    // Define a simple shader program for our point.
    private final String pointVertexShaderCode =
            "uniform mat4 u_MVPMatrix;      \n"
            +	"attribute vec4 a_Position;     \n"
            + "void main()                    \n"
            + "{                              \n"
            + "   gl_Position = u_MVPMatrix   \n"
            + "               * a_Position;   \n"
            + "   gl_PointSize = 5.0;         \n"
            + "}                              \n";

    private final String pointFragmentShaderCode =
            "precision mediump float;       \n"
            + "void main()                    \n"
            + "{                              \n"
            + "   gl_FragColor = vec4(1.0,    \n"
            + "   1.0, 1.0, 1.0);             \n"
            + "}                              \n";

    public Cube(){
        positionBuffer = ByteBuffer.allocateDirect(cubePositionData.length * bytePerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        positionBuffer.put(cubePositionData).position(0);

        colorBuffer = ByteBuffer.allocateDirect(cubeColorData.length * bytePerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(cubeColorData).position(0);

        normalBuffer = ByteBuffer.allocateDirect(cubeNormalData.length * bytePerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer.put(cubeNormalData).position(0);

        int vertexShader = GameRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GameRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        int pointVertexShader = GameRenderer.loadShader(GLES20.GL_VERTEX_SHADER, pointVertexShaderCode);
        int pointFragmentShader = GameRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, pointFragmentShaderCode);

        cVertexProgram = GameRenderer.createAndLinkProgram(vertexShader, fragmentShader, null);
        cPointProgram = GameRenderer.createAndLinkProgram(pointVertexShader, pointFragmentShader, new String[]{"a_Position"} );

        position = new float[3];
        rotation = new float[16];
        modelMatrix = new float[16];


    }

    public void drawCube(float[] VMatrix, float[] PMatrix, float angleInDegrees) {
        float[] mMVPMatrix = new float[16];

        GLES20.glUseProgram(cVertexProgram);

        Matrix.setIdentityM(lightModelMatrix, 0);
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(lightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(lightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        mPositionHandle = GLES20.glGetAttribLocation(cVertexProgram, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(cVertexProgram, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(cVertexProgram, "a_Normal");
        mMVMatrixHandle = GLES20.glGetAttribLocation(cVertexProgram, "u_MVMatrix");
        mMVPMatrixHandle = GLES20.glGetAttribLocation(cVertexProgram, "u_MVPMatrix");
        mLightPosHandle = GLES20.glGetAttribLocation(cVertexProgram, "u_LightPos");

        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, positionBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        colorBuffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, 0, colorBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        normalBuffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mNormalDataSize, GLES20.GL_FLOAT, false, 0, normalBuffer);
        GLES20.glEnableVertexAttribArray(mNormalHandle);

        Matrix.multiplyMM(mMVPMatrix, 0, VMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, PMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMV(mLightWorldPos, 0, lightModelMatrix, 0, mLightModelPos, 0);
        Matrix.multiplyMV(mLightEyePos, 0, VMatrix, 0, mLightWorldPos, 0);

        GLES20.glUniform3f(mLightPosHandle, mLightEyePos[0], mLightEyePos[1], mLightEyePos[2]);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }

    public void drawLight(float[] Vmatrix, float[] Pmatrix){
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(cPointProgram, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetUniformLocation(cPointProgram, "a_Position");
        float[] mMVPMatrix = new float[16];

        GLES20.glUseProgram(cPointProgram);

        GLES20.glVertexAttrib3f(pointPositionHandle, mLightModelPos[0], mLightModelPos[1], mLightModelPos[2]);
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        Matrix.multiplyMM(mMVPMatrix, 0, Vmatrix, 0, lightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, Pmatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }
    public void set_position(float x, float y, float z){
        position[0] = x;
        position[1] = y;
        position[2] = z;

        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public void set_rotation(float a, float x, float y, float z){
        Matrix.setRotateM(rotation, 0, a, x, y, z);
        Matrix.setRotateM(modelMatrix, 0, a, x, y, z);
    }

}