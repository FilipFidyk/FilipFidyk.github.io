//Header Files
#include <windows.h>
#include <stdlib.h>

#define TOP_MENU_HELP 1
#define FILE_MENU_EXIT 2
#define FILE_MENU_NEW 3
#define GENERATE_BUTTON 4


HWND hEditIn1, hEditIn2, hEditOut1, hMainWindow;

HBITMAP hLogoImage, hGenerateImage;

//Next three functions display, handle messages and register the new dialog window respectively. More robust than a messagebox
void displayDialog(HWND hwnd)
{
    //Creates the dialog itself and a handle for
    HWND hdlg = CreateWindowW(
        L"Sample Dialog Class",
        L"Dialog",
        WS_VISIBLE | WS_OVERLAPPEDWINDOW,
        400,400,200,200,
        hwnd,
        NULL,
        NULL,
        NULL
        );
    //Creates a button on the dialog
    CreateWindowW(  
        L"Button",
        L"Close",
        WS_VISIBLE | WS_CHILD,
        20,20,100,40,
        hdlg,
        (HMENU) 1,
        NULL,
        NULL);
    //Enabling and disabling the window allowing for modal and modeless dialog boxes
    EnableWindow(hMainWindow, FALSE);
    
}

LRESULT CALLBACK dialogProc(HWND hwnd, UINT msg, WPARAM wp, LPARAM lp)
{
    switch(msg)
    {
        case WM_COMMAND:
        switch(wp)
        {
            case 1:
                EnableWindow(hMainWindow, TRUE);
                DestroyWindow(hwnd);
                break;
        }
        case WM_CLOSE:
            EnableWindow(hMainWindow, TRUE);
            DestroyWindow(hwnd);
            break;
        default:
            DefWindowProcW(hwnd, msg, wp, lp);
    }
}

void registerDialogClass(HINSTANCE hInst)
{
    WNDCLASSW dialog = {0};
    dialog.style         = 0;
    dialog.lpfnWndProc   = dialogProc;
    dialog.cbClsExtra    = 0;
    dialog.cbWndExtra    = 0;
    dialog.hInstance     = hInst;
    dialog.hIcon         = NULL;
    dialog.hCursor       = LoadCursor(NULL, IDC_ARROW);
    dialog.hbrBackground = (HBRUSH)COLOR_WINDOW;
    dialog.lpszMenuName  = NULL;
    dialog.lpszClassName = L"Sample Dialog Class";

    RegisterClassW(&dialog);
}

//Just loading images in, global handles so they can be easily used
void loadImages()
{
    //wchar_t logoPath[68] = L"C:\\Users\\filip\\Desktop\\C\\Chess\\alpignano.bmp";
    //wchar_t buttonPath[67] = L"C:\\Users\\filip\\Desktop\\C\\Chess\\generate.bmp";
    hLogoImage      = (HBITMAP)LoadImageW(NULL, L"alpignano.bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
    hGenerateImage  = (HBITMAP)LoadImageW(NULL, L"generate.bmp", IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
}

//Function to create controls that are children of the main window, so they don't have to be registered independently
void addControls(HWND hwnd)
{
    //createwindow outputs a handle for the controls so they can be manipulated via other functions
    hEditIn1 = CreateWindowW
    (   
        L"edit", 
        L"",
        WS_VISIBLE | WS_CHILD | WS_BORDER,
        200,50,100,38,
        hwnd,
        NULL,
        NULL,
        NULL 
    );

    hEditIn2 = CreateWindowW
    (   
        L"Edit", 
        L"",
        WS_VISIBLE | WS_CHILD | WS_BORDER,
        200,102,100,38,
        hwnd,
        NULL,
        NULL,
        NULL 
    );

    CreateWindowW
    (  
        L"Static", 
        L"Name:",
        WS_VISIBLE | WS_CHILD,
        100,50,100,38,
        hwnd,
        NULL,
        NULL,
        NULL 
    );

    CreateWindowW
    (  
        L"Static", 
        L"Age:",
        WS_VISIBLE | WS_CHILD,
        100,102,100,38,
        hwnd,
        NULL,
        NULL,
        NULL 
    );
    
    HWND hBut = CreateWindowW
    (  
        L"Button", 
        NULL,
        WS_VISIBLE | WS_CHILD | BS_BITMAP,
        200,150,100,38,
        hwnd,
        (HMENU)GENERATE_BUTTON,
        NULL,
        NULL 
    );
    SendMessageW(hBut, BM_SETIMAGE, IMAGE_BITMAP, (LPARAM)hGenerateImage);

    hEditOut1 = CreateWindowW
    (  
        L"Edit", 
        L"",
        WS_VISIBLE | WS_CHILD | WS_BORDER,
        100,200,300,200,
        hwnd,
        NULL,
        NULL,
        NULL 
    );

    HWND hLogo = CreateWindowW
    (  
        L"Static", 
        NULL,
        WS_VISIBLE | WS_CHILD | SS_BITMAP,
        350,60,100,100,
        hwnd,
        NULL,
        NULL,
        NULL 
    );
    SendMessageW(hLogo, STM_SETIMAGE, IMAGE_BITMAP, (LPARAM)hLogoImage);

}

void addMenus(HWND hwnd)
{
    /*
    Four parameters in AppendMenuW, the desired menu, Type of option, extra info and text
    */
    
    //Create a the top menu, visible at the top, a drop-down menu for file and a submenu in file
    HMENU hMenu = CreateMenu();
    HMENU hFileMenu = CreateMenu();
    HMENU hSubMenu = CreateMenu();

    //The Open submenu, most nested
    AppendMenuW(hSubMenu, MF_STRING, 0, L"SubMenu Item");
    AppendMenuW(hSubMenu, MF_STRING, 0, L"Change Title");
    
    //Options in file
    AppendMenuW(hFileMenu, MF_STRING, FILE_MENU_NEW, L"New");
    AppendMenuW(hFileMenu, MF_POPUP, (UINT_PTR)hSubMenu, L"Open");
    AppendMenuW(hFileMenu, MF_STRING, FILE_MENU_EXIT, L"EXIT");
    AppendMenuW(hFileMenu, MF_SEPARATOR, 0, (LPCWSTR)NULL);
    
    //The visible main menu
    AppendMenuW(hMenu, MF_POPUP, (UINT_PTR)hFileMenu, L"File");
    AppendMenuW(hMenu, MF_STRING, TOP_MENU_HELP, L"Help");

    //Set the desired menu in the correct window
    SetMenu(hwnd, hMenu);
}

//Message handling procedure
LRESULT CALLBACK WinProc(HWND hwnd, UINT uMsg, WPARAM wParam, LPARAM lParam)
{
    int val;
    
    //Main switch statement that does cases for possible messages
    switch(uMsg)
    {
        //WM_COMMAND message is sent when a button in menus are pressed
        case WM_COMMAND:
            
            //When COMMAND is sent, wparam we have the message we passed through
            //This switch switches between them and gives buttons and menus functionality
            switch(wParam)
            {
                case TOP_MENU_HELP:
                    MessageBeep(MB_OK);
                    break;
                case FILE_MENU_EXIT:
                    //Messages boxes return values that can be worked with and give different behaviours
                    val = MessageBoxW(hwnd, L"Are you sure?", L"Wait!", MB_YESNO | MB_ICONEXCLAMATION);
                    if (val == IDYES)
                    {
                        DestroyWindow(hwnd);
                    }
                    break;
                case FILE_MENU_NEW:
                    displayDialog(hwnd);
                    break;
                case GENERATE_BUTTON:
                    char name[30], age[10], out[50];

                    GetWindowText(hEditIn1, name, 30);
                    GetWindowText(hEditIn2, age, 10);

                    if (strcmp(name, "") == 0 || strcmp(age, "") == 0)
                    {
                        val = MessageBoxW(hwnd, L"You didn't enter anything!", NULL, MB_ABORTRETRYIGNORE);
                        //Use a switch statment if a mbox has multiple buttons
                        switch(val)
                        {
                            case IDABORT:
                                DestroyWindow(hwnd);
                                break;
                            case IDRETRY:
                                return 0;
                            case IDIGNORE:
                                break;
                        }
                    }

                    strcpy(out, name);
                    strcat(out, " is ");
                    strcat(out, age);
                    strcat(out, " years old.");

                    //Change the text in a control by passing the controls handle
                    SetWindowText(hEditOut1, out);
                    break;
            }
            break;

        //WM_CREATE is sent only once when the window is being created
        case WM_CREATE:
            loadImages();
            addMenus(hwnd);
            addControls(hwnd);
            break;
        case WM_DESTROY:
            PostQuitMessage(0);
            break;
        default:
            //Default window procedure for messages we don't handle 
            return DefWindowProcW(hwnd, uMsg, wParam, lParam);
    }
    return 0;
    
};
//Main entry function
int WINAPI WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, PSTR pCmdLine, int nCmdShow)
{

    WNDCLASSW class = {0};
    class.style         = 0;
    class.lpfnWndProc   = WinProc;
    class.cbClsExtra    = 0;
    class.cbWndExtra    = 0;
    class.hInstance     = hInstance;
    class.hIcon         = NULL;
    class.hCursor       = LoadCursor(NULL, IDC_ARROW);
    class.hbrBackground = (HBRUSH)COLOR_WINDOW;
    class.lpszMenuName  = NULL;
    class.lpszClassName = L"Sample Window Class";

    if (!RegisterClassW(&class))
    {
        return -1;
    };

    registerDialogClass(hInstance);

    //Creating the window

    hMainWindow = CreateWindowW(
            L"Sample Window Class",                //Window class
            L"Desktop Application",                //Window text
            WS_CAPTION | WS_POPUP | WS_SYSMENU,    //Window style
            50, 50, 500, 500,                      //Size and position
            NULL,                                  //Parent Window
            NULL,                                  //Menu parameter
            hInstance,                             //Instance handle
            NULL                                   //additional data
    );

    if (hMainWindow == NULL)
    {
       return 0;
    }

    //Show the window
    ShowWindow(hMainWindow, nCmdShow);

    // Run the message loop.

    MSG msg = { };
    while (GetMessage(&msg, NULL, 0, 0) > 0)
    {
        TranslateMessage(&msg);
        DispatchMessage(&msg);
    }

    return 0;
}
