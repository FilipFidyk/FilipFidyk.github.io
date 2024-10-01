#include <windows.h>
#include "Prototypes.h"

void addButtons(HWND hwnd)
{
    CreateWindowW
    (
        L"Button",
        L"7",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        20,90,75,75,
        hwnd,
        (HMENU)BUTTON_SEVEN,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"4",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        20,185,75,75,
        hwnd,
        (HMENU)BUTTON_FOUR,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"1",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        20,280,75,75,
        hwnd,
        (HMENU)BUTTON_ONE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"0",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        20,375,75,75,
        hwnd,
        (HMENU)BUTTON_ZERO,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"8",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        115,90,75,75,
        hwnd,
        (HMENU)BUTTON_EIGHT,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"5",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        115,185,75,75,
        hwnd,
        (HMENU)BUTTON_FIVE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"2",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        115,280,75,75,
        hwnd,
        (HMENU)BUTTON_TWO,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"+",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        115,375,75,75,
        hwnd,
        (HMENU)BUTTON_PLUS,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"9",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        210,90,75,75,
        hwnd,
        (HMENU)BUTTON_NINE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"6",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        210,185,75,75,
        hwnd,
        (HMENU)BUTTON_SIX,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"3",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        210,280,75,75,
        hwnd,
        (HMENU)BUTTON_THREE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"-",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        210,375,75,75,
        hwnd,
        (HMENU)BUTTON_MINUS,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"=",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        305,90,75,75,
        hwnd,
        (HMENU)BUTTON_EQUALS,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"<-",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        305,185,75,75,
        hwnd,
        (HMENU)BUTTON_BACK,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"/",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        305,280,75,75,
        hwnd,
        (HMENU)BUTTON_DIVIDE,
        NULL,
        NULL
    );

    CreateWindowW
    (
        L"Button",
        L"*",
        WS_VISIBLE | WS_CHILD | BS_CENTER | BS_VCENTER,
        305,375,75,75,
        hwnd,
        (HMENU)BUTTON_MULTIPLY,
        NULL,
        NULL
    );
}
