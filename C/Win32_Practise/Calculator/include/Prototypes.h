#ifndef PROTOTYPES
#define PROTOTYPES

#include <windows.h>

#define BUTTON_ONE 10
#define BUTTON_TWO 11
#define BUTTON_THREE 12
#define BUTTON_FOUR 13
#define BUTTON_FIVE 14
#define BUTTON_SIX 15
#define BUTTON_SEVEN 16
#define BUTTON_EIGHT 17
#define BUTTON_NINE 18
#define BUTTON_ZERO 19
#define BUTTON_PLUS 20
#define BUTTON_MINUS 21
#define BUTTON_MULTIPLY 22
#define BUTTON_DIVIDE 23
#define BUTTON_EQUALS 24
#define BUTTON_BACK 25

LRESULT CALLBACK WinProc(HWND, UINT, WPARAM, LPARAM);
void addControls(HWND);
void addButtons(HWND);
void manageButtons(HWND, WPARAM);
void putNumIn(HWND, char);

#endif