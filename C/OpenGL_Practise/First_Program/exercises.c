#include <glad/glad.h>
#include <glfw3.h>
#include <stdlib.h>
#include <stdio.h>

const char *fragmentShaderSourceYellow = "#version 330 core\n"
    "out vec4 FragColor;\n"
    "void main()\n"
    "{\n"
    "   FragColor = vec4(1.0f, 1.0f, 0.0f, 1.0f);\n"
    "}\n\0";

unsigned int* exercises(int choice, unsigned int vertexShader){
    unsigned int VBO1, VAO1, VBO2, VAO2, EBO;
    glGenBuffers(1, &EBO);
    glGenBuffers(1, &VBO1);
    glGenBuffers(1, &VBO2);
    glGenVertexArrays(1, &VAO1);
    glGenVertexArrays(1, &VAO2);

    switch(choice)
    {
    case 2:
        float vertices[] = 
        {
             0.0f,  0.0f, 0.0f,
            -0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, 0.0f,
             0.0f,  0.5f, 0.0f,
             0.5f,  0.0f, 0.0f,
        };

        unsigned int indices[] =
        {
            0, 1, 2,
            0, 3, 4
        };

        glBindVertexArray(VAO1);

        glBindBuffer(GL_ARRAY_BUFFER, VBO1);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);

        glEnableVertexAttribArray(0);
        break;

    case 3:
    case 4:
        float vertices1[] = 
        {
             0.0f,  0.0f, 0.0f,
            -0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, 0.0f,
        };

        float vertices2[] =
        {
             0.0f,  0.0f, 0.0f,
             0.0f,  0.5f, 0.0f,
             0.5f,  0.0f, 0.0f,
        };

        glBindVertexArray(VAO1);

        glBindBuffer(GL_ARRAY_BUFFER, VBO1);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices1), vertices1, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(VAO2);

        glBindBuffer(GL_ARRAY_BUFFER, VBO2);
        glBufferData(GL_ARRAY_BUFFER, sizeof(vertices2), vertices2, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
        glEnableVertexAttribArray(0);

        if (choice == 3)
        {
            unsigned int *ObjectIDs = (unsigned int*)calloc(2, sizeof(unsigned int));
            ObjectIDs[0] = VAO1;
            ObjectIDs[1] = VAO2;
            return ObjectIDs;
        }
        else{
            
            unsigned int fragmentShader;
            fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
            glShaderSource(fragmentShader, 1, &fragmentShaderSourceYellow, NULL);
            glCompileShader(fragmentShader);

            int success;
            char infoLog[512];
            glGetShaderiv(fragmentShader, GL_COMPILE_STATUS, &success);
            if(!success)
            {
                glGetShaderInfoLog(vertexShader, 512, NULL, infoLog);
                printf("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n");
                printf("%s", infoLog);
            }

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

            unsigned int *ObjectIDs = (unsigned int*)calloc(3, sizeof(unsigned int));
            ObjectIDs[0] = VAO1;
            ObjectIDs[1] = VAO2;
            ObjectIDs[2] = shaderProgram;
            return ObjectIDs;
        }
    } 
}