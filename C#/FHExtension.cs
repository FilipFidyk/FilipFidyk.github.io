/*
Another file from when i was younger and didn't comment my code. Also the extension.txt file doesn't exist so
I do not know what it is meant to do
*/

using System;
using System.IO;
using System.Collections.Generic;

namespace FH_Extension
{
    class Program
    {
        
        static void Main(string[] args)
        {
            void BubbleSort(ref string[] namesArr, ref int[] scoresArr)
            {
                bool swaps = true;
                int intTemp;
                string strTemp;

                while (swaps == true)
                {
                    swaps = false;
                    for (int i = 0; i < scoresArr.Length - 2; i++)
                    {
                        if (scoresArr[i] < scoresArr[i+1])
                        {
                            intTemp = scoresArr[i + 1];
                            scoresArr[i + 1] = scoresArr[i];
                            scoresArr[i] = intTemp;

                            strTemp = namesArr[i + 1];
                            namesArr[i + 1] = namesArr[i];
                            namesArr[i] = strTemp;

                            swaps = true;
                        }
                    }
                }
            }
            TextReader tr = new StreamReader("Extension.txt");
            
            int numOfLines = File.ReadAllLines("Extension.txt").Length;
            int[] scores = new int[numOfLines / 2];
            string[] names = new string[numOfLines / 2];

            for (int i = 0; i < numOfLines / 2; i++)
            {
                names[i] = tr.ReadLine();
                scores[i] = Convert.ToInt16(tr.ReadLine());
            }
            tr.Close();

            BubbleSort(ref names, ref scores);

            TextWriter tw = new StreamWriter("Extension.txt");
            for (int i = 0; i < scores.Length; i++)
            {
                tw.WriteLine(i + " " + names[i] + "\n" + scores[i]);
            }
            tw.Close();
        }
    }
}
