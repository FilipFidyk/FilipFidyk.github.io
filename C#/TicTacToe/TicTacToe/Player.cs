using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TicTacToe
{
    internal abstract class Player
    {
        public string Sign { get; private set; }
        public string Name { get; set; } = "";

        protected Player(string sign)
        {
            Sign = sign;
        }

        public abstract void Move(TBoard t);
    }
}
