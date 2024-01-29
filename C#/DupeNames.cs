using System;
using System.Collections.Generic;

namespace Duplicate_Names_Array
{
    class DupeNames
    {
        static string[] pick_names(string[] array)
        {
            bool valid = false;
            Console.WriteLine("Please enter 10 names");

            //This loop takes a input for the array and checks whether every input is made up of only letters
            //If so, the loop ends if not it carries on
            for (int i = 0; i < 10; i++)
            {
                do
                {
                    array[i] = Console.ReadLine();
                    valid = true;
                    foreach (char character in array[i])
                    {
                        if (!(Char.IsLetter(character)))
                        {
                            valid = false;
                            break;
                        }
                    }
                } while (valid == false);
            }
            return array;
        }

        static Dictionary<string, int> find_duplicates(string[] array_with_names)
        {
            int appearances = 0;
            Dictionary<string, int> duplicates = new Dictionary<string, int>();

            //This loop goes through and picks an element of the array and compares it to every element including itself
            for (int i = 0; i < array_with_names.Length; i++)
            {
                appearances = 0;
                for (int j = 0; j < array_with_names.Length; j++)
                {
                    if (array_with_names[i] == array_with_names[j])
                    {
                        appearances++;
                    }
                }
                //Hence an easier way to find duplicates is to see if they appear more than once 
                //rather than how many times it duplicates
                if (appearances > 1)
                {
                    //A dictionary can only have one instance of a key therefore if the key is tried to be put in again
                    //an error will occur so we have to catch it
                    try
                    {
                        duplicates.Add(array_with_names[i], appearances);
                    }
                    catch (Exception) { }
                }
            }
            return duplicates;
        }
        static void Main(string[] args)
        {
            string[] names = new string[10];
            names = pick_names(names);
            Console.WriteLine("");

            Dictionary<string, int> duplicates = find_duplicates(names);

            foreach (KeyValuePair<string, int> kvp in duplicates)
            {
                Console.WriteLine($"{kvp.Key} was a duplicate and appeared {kvp.Value} times");
            }
        }
    }
}
