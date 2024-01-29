using System;

namespace TikTakToe
{
    class Program
    {
        static void ShowBoard(char[,] board)
        {
            Console.WriteLine($"| {board[0,0]} | {board[0, 1]} | {board[0, 2]} |");
            Console.WriteLine($"| {board[1, 0]} | {board[1, 1]} | {board[1, 2]} |");
            Console.WriteLine($"| {board[2, 0]} | {board[2, 1]} | {board[2, 2]} |");
        }
        static void GetPlayers(ref string player1, ref string player2)
        {
            Console.Write("Enter you name player 1 : ");
            player1 = Console.ReadLine();
            Console.Write("Enter you name player 2 : ");
            player2 = Console.ReadLine();
        }
        static void WinCondition(ref bool win, char[,] board)
        {
            if (board[0, 0] == 'o' || board[0, 0] == 'x')
            {
                if (board[0, 0] == board[0, 1] && board[0, 0] == board[0, 2]) //first row
                {
                    win = true;
                }
                else if (board[0, 0] == board[1, 0] && board[0, 0] == board[2, 0]) //first column
                {
                    win = true;
                }
                else if (board[0, 0] == board[1, 1] && board[0, 0] == board[2, 2]) // top left to bottom right
                {
                    win = true;
                }
            }
            if (board[1, 0] == 'o' || board[1, 0] == 'x')
            {
                if (board[1, 0] == board[1, 1] && board[1, 0] == board[1, 2]) // second row
                {
                    win = true;
                }
            }
            if (board[2, 0] == 'o' || board[2, 0] == 'x')
            {
                if (board[2, 0] == board[2, 1] && board[2, 0] == board[2, 2]) //third row
                {
                    win = true;
                }
                else if (board[2, 0] == board[1, 1] && board[2, 0] == board[0, 2]) //bottom left to top right
                {
                    win = true;
                }
            }
            if (board[0, 1] == 'o' || board[0, 1] == 'x')
            {
                if (board[0, 1] == board[1, 1] && board[0, 1] == board[2, 1]) //second column
                {
                    win = true;
                }
            }
            if (board[0, 2] == 'o' || board[0, 2] == 'x')
            {
                if (board[0, 2] == board[1, 2] && board[0, 2] == board[2, 2]) //third column
                {
                    win = true;
                }
            }
        }

            static void Main(string[] args)
        {
                char[,] board = new char[3, 3]
                        { { ' ', ' ', ' ' },
                          { ' ', ' ', ' ' },
                          { ' ', ' ', ' ' }
                        };
            string player1 = "", player2 = "";
            bool win = false;
            int turns = 0, x = 0, y = 0;
            

            Console.WriteLine(" _____ _ _  _____     _  _____           \n" +
                              "|_   _(_) ||_   _|   | ||_   _|          \n" +
                              "  | |  _| | _| | __ _| | _| | ___   ___  \n" +
                              "  | | | | |/ / |/ _` | |/ / |/ _ \\ / _ \\ \n" +
                              "  | | | |   <| | ( | |   <| | ( ) |    / \n" +
                              "  \\_/ |_|_|\\_\\_/\\__,_|_|\\_\\_/\\___/ \\___| \n");

            Console.WriteLine("- To win you need to get three in a row\n" +
                              "- To pick a place you need to pick the cooordinates between 1,1 and 3,3\n" +
                              "  First you pick the x-coordinate then the y-coordinate\n" +
                              "- Player 1 is always noughts, Player 2 is always crosses\n");
            GetPlayers(ref player1, ref player2);
            
            ShowBoard(board);
            while (turns < 9)
            {
                Console.WriteLine($"{player1} pick a spot : ");
                x = Convert.ToInt16(Console.ReadLine());
                y = Convert.ToInt16(Console.ReadLine());
                board[y - 1, x - 1] = 'o';
                ShowBoard(board);
                WinCondition(ref win, board);
                if (win)
                {
                    Console.WriteLine($"{player1} you win!");
                    break;
                }
                turns++;

                Console.WriteLine($"{player2} pick a spot : ");
                x = Convert.ToInt16(Console.ReadLine());
                y = Convert.ToInt16(Console.ReadLine());
                board[y - 1, x - 1] = 'x';
                ShowBoard(board);
                WinCondition(ref win, board);
                if (win) 
                {
                    Console.WriteLine($"{player2} you win!");
                    break;
                }
                turns++;
            }
            if (!win)
                Console.WriteLine("It is a draw");

            
            

        }
    }
}
