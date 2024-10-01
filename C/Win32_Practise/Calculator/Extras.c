#include <windows.h>
#include <stdio.h>
#include "Prototypes.h"

HWND hBox;
char val1[11] = "";
char val2[11] = "";
char op;
int opFinished = 0;

//Creates the controls of the calculator
void addControls(HWND hwnd)
{
    //Written in Buttons.c
    addButtons(hwnd);

    hBox = CreateWindowW
    (
        L"Static",
        L"",
        WS_VISIBLE | WS_CHILD | WS_BORDER,
        20,20,360,50,
        hwnd,
        NULL,
        NULL,
        NULL
    );
}

//Inputs numbers into the calculator textbox
void putNumIn(HWND hwnd, char strNum)
{
    //Wipes the text box if an operation was just completed
    if (opFinished){
        SetWindowText(hBox, "\0");
        opFinished = 0;
    }

    char num[11];
    GetWindowText(hBox, num, 11);

    //Ignores preceding zeroes for positive and negative numbers
    if ((strlen(num) == 0 || (strlen(num) == 0 && num[0] == '-')) && strNum == '0')
    {
        return;
    }
    //Ensures inputs aren't longer than allowed
    else if (strlen(num) == 10 )
    {
        MessageBoxW(hwnd, L"Input is too long", L"Wait!", MB_OK);
        SetWindowText(hBox, "\0");
        return;
    }
    else{
        int length = strlen(num);
        num[length] = strNum;
        num[length+1] = '\0';
        SetWindowText(hBox, num);
    }
}

//Ensures an input is present
int operationCheck(HWND hwnd)
{
    char num[11];
    GetWindowText(hBox, num, 11);
    if (strlen(num) == 0)
    {
        MessageBoxW(hwnd, L"No value", L"Wait!", MB_OK);
        return -1;
    }
    return 1;
}

//Takes the first input if valid and sets the operation
void beginOperation(HWND hwnd, char letter){
    int valid = operationCheck(hwnd);
    if(valid)
        {
            GetWindowText(hBox, val1, 11);
            op = letter;
            SetWindowText(hBox, "\0");
        }

}

//Controls all behaviours of the buttons
void manageButtons(HWND hwnd, WPARAM wp)
{
    switch(wp)
    {
    case BUTTON_ONE:
        putNumIn(hwnd, '1');
        break;
    case BUTTON_TWO:
        putNumIn(hwnd, '2');
        break;
    case BUTTON_THREE:
        putNumIn(hwnd, '3');
        break;
    case BUTTON_FOUR:
        putNumIn(hwnd, '4');
        break;
    case BUTTON_FIVE:
        putNumIn(hwnd, '5');
        break;
    case BUTTON_SIX:
        putNumIn(hwnd, '6');
        break;
    case BUTTON_SEVEN:
        putNumIn(hwnd, '7');
        break;
    case BUTTON_EIGHT:
        putNumIn(hwnd, '8');
        break;
    case BUTTON_NINE:
        putNumIn(hwnd, '9');
        break;
    case BUTTON_ZERO:
        putNumIn(hwnd, '0');
        break;
    case BUTTON_PLUS:
        beginOperation(hwnd, '+');
        break;
    case BUTTON_MINUS:
        //Ensures the minus button can also be used for negative numbers
        char buffer[11];
        GetWindowText(hBox, buffer, 11);
        if (strlen(buffer) == 0)
        {
            putNumIn(hwnd, '-'); 
        }
        else
        {
            beginOperation(hwnd, '-'); 
        }
        break;
    case BUTTON_MULTIPLY:
        beginOperation(hwnd, '*');
        break;
    case BUTTON_DIVIDE:
        beginOperation(hwnd, '/');
        break;
    case BUTTON_BACK:
        //Backspaces the last character if at least one is present
        char buffer[11];
        GetWindowText(hBox, buffer, 11);
        if (strlen(buffer) != 0)
        {
           buffer[strlen(buffer)-1] = '\0'; 
        }
        SetWindowText(hBox, buffer);
        break;
    case BUTTON_EQUALS:
        //Ensures an operation was started earlier
        if(op == '\0')
        {
            break;
        }
        
        //If a valid second value is present, execute the operation 
        if (operationCheck(hwnd))
        {
            int answer;
            char buffer[12];
            GetWindowText(hBox, val2, 11);
            //We will only have valid numbers, since the box is static, and there are "operationCheck"s to ensure theyre not empty
            //So we can use atoi
            int num1 = atoi(val1);
            int num2 = atoi(val2);
            switch (op)
            {
            case '+':
                answer = num1 + num2;
                break;
            case '-':
                answer = num1 - num2;
                break;
            case '*':
                answer = num1 * num2;
                break;
            case '/':
                //Stops a division by zero
                if (num2 == 0)
                {
                    MessageBoxW(hwnd, L"Cannot divide by zero.", L"Error", MB_OK);
                    break;
                }
                else
                {
                    answer = num1 / num2; 
                }
                break;
            default:
                MessageBoxW(hwnd, L"How", L"What", MB_OK);              
            }

            //converts the numbers into strings and puts the answer in the text box
            snprintf(buffer, sizeof(buffer), "%d", answer);
            if (strlen(buffer) <= 10)
            {
                SetWindowText(hBox, buffer);
            }
            else
            {
                MessageBoxW(hwnd, L"Answer is too big. Values and operation have been Reset", L"Error", MB_OK);
                strcpy(val1, "");
                strcpy(val2, "");
            }
            
        }
        else{
            break;
        }

        //Don't forget to wipe the operation variable and set a finish flag
        op = '\0';
        opFinished = 1;
        break;
    }
}

