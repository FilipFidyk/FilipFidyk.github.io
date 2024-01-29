/*
I don't remember this code but I'm pretty sure it writes "text" to a file, then reads it and orders it 
according to the scores
*/

using System;
using System.Collections.Generic;
using System.IO;

namespace ExFile_Score_Sorting
{
    class FileScoreSorting
    {
        static void Main(string[] args)
        {

            string path = @"c:\Users\Lukasz\OneDrive\Praca\Coding\C#\Exfile_Score_Sorting\scores.txt";
            string text = "John 93\n" +
                          "Alfie 51\n" +
                          "Lucy 67\n" +
                          "Eleanor 22\n" +
                          "Owen 44\n";

            if (!File.Exists(path))
            {
                File.WriteAllText(path, text);
            }


            string[] readtext = File.ReadAllLines(path);
            /*
            foreach (string line in readtext){
                Console.WriteLine(line);
            }*/

            //sort by name
            /*Array.Sort(readtext);
            foreach (string line in readtext)
            {
                Console.WriteLine(line);
            }
            Console.WriteLine("");*/

            int x = 0; int y = 0;
            List<string> ordertext = new List<string> { };
            ordertext.Add(readtext[0]);
            string var = readtext[x]; string var2 = ordertext[y];

            for (int i = 0; i < 2 ; i++)
            {
                
                Console.WriteLine(var2[var2.Length - 2] + " " + var[var.Length - 2]);

                foreach (string xyx in ordertext)
                {
                    var = readtext[x]; var2 = ordertext[y];
                    if (Convert.ToInt32(var2[var2.Length - 2]) > Convert.ToInt32(var[var.Length - 2]))
                    {
                        ordertext[0] = var2;
                        ordertext.Add(var);
                    }
                    else
                    {
                        ordertext.Add(var);
                    }

                    x++; y++;
                }
                
                
            }

            foreach (string line in ordertext)
            {
                Console.WriteLine(line);
            }
        } 
    }
}