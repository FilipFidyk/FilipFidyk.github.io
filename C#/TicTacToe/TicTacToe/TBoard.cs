using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace TicTacToe
{
    internal class TBoard
    {
        private string[,] _board;

        public TBoard()
        {
            _board = new string[3, 3];
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    _board[i, j] = "";
                }
            }
        }

        public string this[int x, int y]
        {
            get { return _board[x, y]; }
            set
            {
                if (value == "X" || value == "O" || value == "")
                    _board[x, y] = value;
                else
                    throw new ArgumentException("Only 'X', 'O' or '' are allowed.");
            }
        }

        public string[,] GetBoard()
        {
            return _board;
        }

        public void PrintString()
        {
            Console.WriteLine("---|---|---");
            for (int i = 0; i < 3; i++)
            {
                Console.Write(" " + replaceEpsilon(_board[i, 0]) + " |");
                Console.Write(" " + replaceEpsilon(_board[i, 1]) + " |");
                Console.Write(" " + replaceEpsilon(_board[i, 2]) + " \n");
                Console.WriteLine("---|---|---");
            }
        }

        public bool CheckWin()
        {
            for (int i = 0; i < 3; i++)
            {
                if (_board[i,0] == _board[i,1] && _board[i, 1] == _board[i, 2] && _board[i, 0] != "")
                {
                    return true;
                }
                else if (_board[0,i] == _board[1, i] && _board[1, i] == _board[2, i] && _board[0, i] != "")
                {
                    return true;
                }
            }

            if ((_board[0, 0] == _board[1, 1] && _board[1, 1] == _board[2, 2] && _board[0, 0] != "") ||
                (_board[0, 2] == _board[1, 1] && _board[1, 1] == _board[2, 0] && _board[0, 2] != ""))
            {
                return true;
            }
            return false;
        }

        public bool IsFull()
        {
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 3; j++)
                {
                    if (_board[i,j] == "")
                    {
                        return false;
                    }
                }
            }
            return true;
        }

        private string replaceEpsilon(string inStr)
        {
            if (inStr == "")
            {
                return " ";
            }
            return inStr;
        }
    }
}
