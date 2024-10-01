#include <stdlib.h>
#include <windows.h>
#include "Prototypes.h"

//Generic window registration, showing and message handling
HWND hMainWindow;
int WINAPI WinMain(HINSTANCE hInst, HINSTANCE hPrevInst, LPSTR lpCmdLine, int nCmdShow)
{
    WNDCLASSW wc = 
    {
        0,
        WinProc,
        0,
        0,
        hInst,
        NULL,
        LoadCursor(NULL, IDC_ARROW),
        (HBRUSH)COLOR_WINDOW,
        NULL,
        L"Calculator"
     };

     if (!RegisterClassW(&wc))
     {
        return -1;
     }

     hMainWindow = CreateWindowW
     (
        L"Calculator",
        L"Placeholder",
        WS_CAPTION | WS_POPUP | WS_SYSMENU, 
        100,100,400,490,
        NULL,
        NULL,
        hInst,
        NULL
     );

     if (hMainWindow == NULL){
        return 0;
     }

     ShowWindow(hMainWindow, nCmdShow);

    MSG msg = { };
    while (GetMessage(&msg, NULL, 0, 0) > 0)
    {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    return 0;
}

LRESULT CALLBACK WinProc(HWND hwnd, UINT uMsg, WPARAM wp, LPARAM lp)
{
    switch(uMsg)
    {
        case WM_CREATE:
            //Written in Extras.c
            addControls(hwnd);
            break;
        case WM_COMMAND:
            //Written in Extras.c 
            manageButtons(hwnd, wp);
            break;
        case WM_DESTROY:
            PostQuitMessage(0);
            break;
        default:
            DefWindowProcW(hwnd, uMsg, wp, lp);
    }
}