#ifndef PROTOTYPES
#define PROTOTYPES

#include <windows.h>

#define OPEN_FILE 1
#define SAVE_FILE 2

LRESULT CALLBACK WinProc(HWND, UINT, WPARAM, LPARAM);
void addMenus(HWND);
void addControls(HWND);
void openFile(HWND);
void displayFile(char*);
void saveFile(HWND);

#endif