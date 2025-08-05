using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TicTacToe
{
    internal class CPUPlayer : Player
    {
        public CPUPlayer(string inSign) : base(inSign)
        {
            base.Name = "CPU";
        }

        public override void Move(TBoard t)
        {
            Random r = new Random();
            bool valid = false;
            int row, col;

            while (!valid)
            {
                row = r.Next(0, 3);
                col = r.Next(0, 3);

                if (t[row, col] == "")
                {
                    t[row, col] = base.Sign;
                    t.PrintString();
                    Console.WriteLine("");
                    valid = true;
                }
            }
        }
    }
}
