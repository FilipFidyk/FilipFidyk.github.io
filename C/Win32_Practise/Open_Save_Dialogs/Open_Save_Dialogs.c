#include <stdlib.h>
#include <windows.h>
#include "Prototypes.h"

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
        L"Open_Save_Dialogs"
     };

     if (!RegisterClassW(&wc))
     {
        return -1;
     }

     hMainWindow = CreateWindowW
     (
        L"Open_Save_Dialogs",
        L"Placeholder",
        WS_CAPTION | WS_POPUP | WS_SYSMENU, 
        100,100,500,600,
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
            addControls(hwnd);
            break;
        case WM_COMMAND:
            switch(wp){
                case OPEN_FILE:
                    openFile(hwnd);
                    break;
                case SAVE_FILE:
                    saveFile(hwnd);
                    break;
            }
            break;
        case WM_DESTROY:
            PostQuitMessage(0);
            break;
        default:
            DefWindowProcW(hwnd, uMsg, wp, lp);
    }
}