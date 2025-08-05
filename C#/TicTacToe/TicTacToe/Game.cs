using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TicTacToe
{
    internal class Game
    {
        private bool _move = true;
        public Game()
        {
            TBoard game = new TBoard();

            HumanPlayer player1 = new HumanPlayer("X");
            Console.Write("Player 1 Enter Name: ");
            try
            {
                player1.Name = Console.ReadLine();
            } catch (IOException)
            {
                player1.Name = "Player 1";
            }

            string choice = "";
            while (choice != "1" && choice != "2")
            {
                Console.Write("Play against a Human or CPU (1/2): ");
                choice = Console.ReadLine();

                if (choice != "1" && choice != "2")
                {
                    Console.WriteLine("Write \"1\" or \"2\"");
                }
            }

            Player player2;
            switch (choice)
            {
                case "1":
                    player2 = new HumanPlayer("O");
                    Console.Write("Player 2 Enter Name: ");
                    try
                    {
                        player2.Name = Console.ReadLine();
                    }
                    catch (IOException)
                    {
                        player2.Name = "Player 2";
                    }
                    break;
                case "2":
                    player2 = new CPUPlayer("O");
                    break;
                default:
                    throw new InvalidOperationException("Invalid choice of game value");
            }

            while (!game.CheckWin() && !game.IsFull())
            {
                if (_move)
                {
                    player1.Move(game);
                }
                else
                {
                    player2.Move(game);
                }
                _move = _move ^ true;
            }

            if (game.IsFull())
            {
                Console.WriteLine($"Draw! Well Played");
            }
            else if (!_move)
            {
                Console.WriteLine($"Congratulations {player1.Name}, you won the game");
            }
            else
            {
                if (choice == "1")
                    Console.WriteLine($"Congratulations {player2.Name}, you won the game");
                else
                    Console.WriteLine($"The computer won the game");
            }

        }

    }
}
