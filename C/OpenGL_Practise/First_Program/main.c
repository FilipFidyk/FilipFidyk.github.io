#include <glad/glad.h>
#include <glfw3.h>
#include <stdio.h>
#include <exercises.h>

void framebuffer_size_callback(GLFWwindow *window, int width, int height);
void processInput(GLFWwindow *window);

const char *vertexShaderSource = "#version 330 core\n"
    "layout (location = 0) in vec3 aPos;\n"
    "void main()\n"
    "{\n"
    "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n"
    "}\0";

const char *fragmentShaderSource = "#version 330 core\n"
    "out vec4 FragColor;\n"
    "void main()\n"
    "{\n"
    "   FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n"
    "}\n\0";

/*
Choice variable used to change  the things i want to render

Done for practise, to be able to render different things without making a new project

Variable Options
    1 - Draws a square using indexed buffering (LearnOpenGL Tutorial)
    2 - Draws two triangles using indexed buffering (LearnOpenGL exercise)
    3 - Draw two triangles using different VAOs and VBOs (LearnOpenGL exercise)
    4 - Draw two triangles as in 3 but with different shaders to change the colour
*/

const int choice = 4;

int main()
{
    //instantiating the glfw window
    glfwInit();
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    //glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

    //Create the window
    GLFWwindow* window = glfwCreateWindow(800, 600, "LearnOpenGL", NULL, NULL);
    if (window == NULL)
    {
        printf("Failed to create GLFW window");
        glfwTerminate();
        return -1;
    }
    glfwMakeContextCurrent(window);

    //Initialise GLAD to handle GLFW function pointers
    if (!gladLoadGLLoader((GLADloadproc)glfwGetProcAddress))
    {
        printf("Failed to initialize GLAD");
        return -1;
    }  
  
    //Set the size of the rendering window via a viewport
    glViewport(0, 0, 800, 600);

    //Register a callback function for resizing the window
    glfwSetFramebufferSizeCallback(window, framebuffer_size_callback); 

    //--------------------------------------------------------------------------------------
    /*
    COMPILING VERTEX AND FRAGMENT SHADERS
    */

   //The vertex shader
    unsigned int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
    glCompileShader(vertexShader);

    int  success;
    char infoLog[512];
    glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
    if(!success)
    {
        glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
        printf("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n");
        printf("%s", infoLog);
    }

    //The fragment shader
    unsigned int fragmentShader;
    fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
    glCompileShader(fragmentShader);

    glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
    if(!success)
    {
        glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
        printf("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n");
        printf("%s", infoLog);
    }
    
    //---------------------------------------------------------------------------------
    /*
    LINK SHADERS
    */
    unsigned int shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
    // check for linking errors
    glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
    if (!success) 
    {
        glGetProgramInfoLog(shaderProgram, 512, NULL, infoLog);
        printf("ERROR::SHADER::PROGRAM::LINKING_FAILED\n");
        printf("%s", infoLog);
    }

    //They SHOULD BE DELETED, but to reduce code repetition i will keep the vertex shader
    //to be passed into the exercises function later on
    //glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    unsigned int *ObjectIDs = NULL;
    if (choice == 1)
    {
        float *vertices = (float *)malloc(12 * sizeof(float))
        vertices[0] = 0.5f;   vertices[1] = 0.5f;   vertices[2] = 0.0f;   // Vertex 1
        vertices[3] = 0.5f;   vertices[4] = -0.5f;  vertices[5] = 0.0f;   // Vertex 2
        vertices[6] = -0.5f;  vertices[7] = -0.5f;  vertices[8] = 0.0f;   // Vertex 3
        vertices[9] = -0.5f;  vertices[10] = 0.5f;  vertices[11] = 0.0f;  // Vertex 4

        unsigned int indices[] (unsigned int *)malloc(12 * sizeof(unsigned int));
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 3;
        indices[3] = 1;
        indices[4] = 2;
        indices[5] = 3;
        //generate the vertex buffer object and vertex array object
        unsigned int VBO, VAO, EBO;
        glGenBuffers(1, &EBO);
        glGenBuffers(1, &VBO);
        glGenVertexArrays(1, &VAO);

        //Bind the vertex array, then the buffers configure the vertex attributes
        glBindVertexArray(VAO);

        //bind the VBO to GL_ARRAY_BUFFER so any change calls made to GAB will configure VBO
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

        //Allow for indexed drawing
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW); 

        //tells how to interpret the vertex data
        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
        //enable the vertex attribute, the param is the location of the first vertex attribute
        glEnableVertexAttribArray(0);

        //Unbinds the GAB from the VBO since we've passed the info we need to.
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    else
    {
        ObjectIDs = exercises(choice, vertexShader);
    }


    //Polling loop to keep the window running
    while(!glfwWindowShouldClose(window))
    {
        //Allows for checking pressed keys
        processInput(window);

        //Set the colour of the background to teal
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        //Uses the compiled shader program, uses the VAO which stores our triangles' vertex attributes
        glUseProgram(shaderProgram);
        if (choice < 3)
        {
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
        }
        else if (choice == 3)
        {
            glBindVertexArray(ObjectIDs[0]);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glBindVertexArray(ObjectIDs[1]);
            glDrawArrays(GL_TRIANGLES, 0, 3);
        }
        else{
            glBindVertexArray(ObjectIDs[0]);
            glDrawArrays(GL_TRIANGLES, 0, 3);
            glUseProgram(ObjectIDs[2]);
            glBindVertexArray(ObjectIDs[1]);
            glDrawArrays(GL_TRIANGLES, 0, 3);
        }
        //glBindVertexArray(0);

        //Swaps the buffer to show the new frame
        glfwSwapBuffers(window);
        glfwPollEvents();    
    }

    //glDeleteVertexArrays(1, &VAO);
    //glDeleteBuffers(1, &VBO);
    //glDeleteBuffers(1, &EBO);
    //glDeleteProgram(shaderProgram);

    glfwTerminate(); 
    return 0;
}

//Callback function for resizing the window
void framebuffer_size_callback(GLFWwindow *window, int width, int height)
{
    glViewport(0, 0, width, height);
}

//Input processing example function
void processInput(GLFWwindow *window)
{
    //Every frame is checked if a button is in the pressed position, escape in this case
    if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
    {
        glfwSetWindowShouldClose(window, 1);
    }
}