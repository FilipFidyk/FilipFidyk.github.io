//Count up to or down from a number
using System;

namespace UpOrDown
{
    class Program
    {
        static void Main(string[] args)
        {
            string answer = "apple";
            int number;
            Console.Write("Up or down : ");
            do
            {
                answer = Console.ReadLine().Trim().ToLower();
                if (answer == "up")
                {
                    Console.Write("Enter a number to count to : ");
                    try
                    {
                        number = Convert.ToInt16(Console.ReadLine());
                    }
                    catch (Exception)
                    {
                        Console.Write("Enter a digit : ");
                        number = Convert.ToInt16(Console.ReadLine());
                    }

                    for (int i = 1; i <= number; i++)
                    {
                        Console.WriteLine(i);
                    }
                    break;
                }
                else if (answer == "down")
                {
                    Console.Write("Enter a number to count down to from 20: ");
                    try
                    {
                        number = Convert.ToInt16(Console.ReadLine());
                    } catch(Exception) 
                    {
                        Console.Write("Enter a digit : ");
                        number = Convert.ToInt16(Console.ReadLine());
                    }

                    for (int i = 20; i >= number; i--)
                    {
                        Console.WriteLine(i);
                    }
                    break;
                }
                else
                {
                    Console.WriteLine("\nI dont understand");
                }

            } while (answer != "up" || answer != "down");
            
        }
    }
}
