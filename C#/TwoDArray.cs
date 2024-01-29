/*
function definition inside main is an old thing i did every now and then but that is bad practise  
*/

using System;

namespace _2D_Array
{
    class TwoDArray
    {
        static void Main(string[] args)
        {
            //Initialising 2D array
            int[,] myArray = new int[10, 10];

            //The following functions are used to fill out the array, the initialised array is passed by
            //reference so that the actual variable changes as opposed to a copy that would have to be returned
            void Fill2DArrayWithRandom(ref int[,] arr)
            {
                Random rnd = new Random();
                for (int row = 0; row <= arr.GetUpperBound(0); row++)
                {
                    for (int column = 0; column <= arr.GetUpperBound(1); column++)
                    {
                        arr[row, column] = rnd.Next(0, 101);
                    }
                }
            }
            void Fill2DArrayWithAddition(ref int[,] arr)
            {
                for (int row = 0; row <= arr.GetUpperBound(0); row++)
                {
                    for (int column = 0; column <= arr.GetUpperBound(1); column++)
                    {
                        arr[row, column] = row + column;
                        

                    }
                }
            }
            void Fill2DArrayWithMult(ref int[,] arr)
            {
                for (int row = 0; row <= arr.GetUpperBound(0); row++)
                {
                    for (int column = 0; column <= arr.GetUpperBound(1); column++)
                    {
                        arr[row, column] = row * column;
                    }
                }
            }
            void Fill2DArrayWithCount(ref int[,] arr)
            {
                for (int row = 0; row <= arr.GetUpperBound(1); row++)
                {
                    for (int column = 0; column <= arr.GetUpperBound(1); column++)
                    {
                        arr[row, column] = (row * 10) + column;
                    }
                }
            }
            void Display2DArray(ref int[,] arr)
            {
                for (int row = 0; row <= arr.GetUpperBound(0); row++)
                {
                    for (int column = 0; column <= arr.GetUpperBound(1); column++)
                    {
                        Console.Write(arr[row, column] + " ");
                        //Extra space to maintain alignment when single digit numbers are printed
                        if (arr[row, column] < 10)
                        {
                            Console.Write(" ");
                        }
                    }
                    Console.WriteLine();
                }
            }
            //Fill2DArrayWithRandom(ref myArray);
            Display2DArray(ref myArray);
            Console.WriteLine();

            Fill2DArrayWithAddition(ref myArray);
            Display2DArray(ref myArray);
            Console.WriteLine();
            
            Fill2DArrayWithMult(ref myArray);
            Display2DArray(ref myArray);
            Console.WriteLine();

            Fill2DArrayWithCount(ref myArray);
            Display2DArray(ref myArray);
            Console.WriteLine();
        }
    }
}
