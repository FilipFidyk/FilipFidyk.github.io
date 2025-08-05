using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TicTacToe
{
    internal class HumanPlayer : Player
    {
        public HumanPlayer(string inSign) : base(inSign)
        {
        }

        public override void Move(TBoard t)
        {
            bool validMove = false;
            while (!validMove)
            {
                int r = -1, c = -1;
                bool validRow = false, validColumn = false;

                while (!validRow)
                {
                    Console.Write($"{this.Name} Enter a row: ");
                    if (int.TryParse(Console.ReadLine(), out r))
                    {
                        validRow = true;
                    }
                    else
                    {
                        Console.WriteLine("Please enter an integer");
                    }
                }

                while (!validColumn)
                {
                    Console.Write($"{this.Name} Enter a column: ");
                    if (int.TryParse(Console.ReadLine(), out c))
                    {
                        validColumn = true;
                    }
                    else
                    {
                        Console.WriteLine("Please enter an integer");
                    }
                }
                Console.WriteLine("");

                if (t[r, c] == "")
                {
                    t[r, c] = this.Sign;
                    t.PrintString();
                    Console.WriteLine("");
                    validMove = true;
                }
                else
                {
                    Console.WriteLine("That field is already taken");
                }
            }
        }
    }
}
