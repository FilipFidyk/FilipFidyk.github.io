#include <windows.h>
#include <stdio.h>
#include "Prototypes.h"

HWND hbuttonHandler, hBox;

void addControls(HWND hwnd){
    //Open File button
    CreateWindowW
    (
        L"Button",
        L"Open File",
        WS_VISIBLE | WS_CHILD,
        50,50,100,50,
        hwnd,
        (HMENU)OPEN_FILE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"Save File",
        WS_VISIBLE | WS_CHILD,
        170,50,100,50,
        hwnd,
        (HMENU)SAVE_FILE,
        NULL,
        NULL
    );

    hBox = CreateWindowW
    (
        L"Edit",
        L"",
        WS_VISIBLE | WS_CHILD | WS_BORDER | ES_MULTILINE | WS_VSCROLL | WS_HSCROLL,
        50,105,400,400,
        hwnd,
        NULL,
        NULL,
        NULL
    );
}

void openFile(HWND hwnd)
{
    OPENFILENAME ofn;
    char fileName[100];

    ZeroMemory(&ofn, sizeof(OPENFILENAME));

    ofn.lStructSize = sizeof(OPENFILENAME);
    ofn.hwndOwner = hwnd;
    ofn.lpstrFile = fileName;
    ofn.lpstrFile[0] = '\0';
    ofn.nMaxFile = 100;
    ofn.lpstrFilter = "All files\0*.*\0Source files\0*.C\0Text files\0*.TXT\0";
    ofn.nFilterIndex = 1;

    GetOpenFileName(&ofn);

    displayFile(ofn.lpstrFile);
}

void displayFile(char* path){
    FILE *file;
    file = fopen(path, "rb");
    fseek(file, 0, SEEK_END);
    int _size = ftell(file);
    rewind(file);
    char *data = (char*)calloc(_size + 1, sizeof(char));
    fread(data, _size, 1, file);
    data[_size] = '\0';

    SetWindowText(hBox, data);

    fclose(file);
}

void saveFile(HWND hwnd)
{
    OPENFILENAME ofn;
    char fileName[100];

    ZeroMemory(&ofn, sizeof(OPENFILENAME));

    ofn.lStructSize = sizeof(OPENFILENAME);
    ofn.hwndOwner = hwnd;
    ofn.lpstrFile = fileName;
    ofn.lpstrFile[0] = '\0';
    ofn.nMaxFile = 100;
    ofn.lpstrFilter = "All files\0*.*\0Source files\0*.C\0Text files\0*.TXT\0";
    ofn.nFilterIndex = 1;

    GetSaveFileName(&ofn);

    writeFile(ofn.lpstrFile);
}

void writeFile(char *path){
    FILE *file;
    file = fopen(path, "w");
    
    int _size = GetWindowTextLength(hBox);
    char *data = (char*)calloc(_size + 1, sizeof(char));
    data[_size] = '\0';
    GetWindowText(hBox, data, _size + 1);
    fwrite(data, _size+1, 1, file);

    SetWindowText(hBox, data);
}