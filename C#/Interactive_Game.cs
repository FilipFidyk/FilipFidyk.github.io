using System;
using System.Threading;

namespace October_Half_Term_Task
{
    class Interactive_Game
    {
        //I'm gonna use the sleep command for the program to pause but i want to make it easier to write
        static void wait(int ms_amount)
        {
            Thread.Sleep(ms_amount);
        }
        //The introduction, nothing earth-shattering
        static void Initial_text()
        {
            Console.Write("What is your name? ");
            string name = Console.ReadLine();
            Console.WriteLine($"There is something lurking in  the shadows {name}, Which weapons will you choose?\n");
        }
        //The weapons selection
        static int Weapon_Selection()
        {
            int choice = 0;
            bool valid = false;

            while (!valid) 
            {
                Console.WriteLine("1. Silver bullet\n2. Wooden stake\n3. Exorcism spell");

                //Makes sure the input is an integer by trying to convert it
                valid = true;
                try
                {
                    choice = Convert.ToInt16(Console.ReadLine());
                }
                catch (Exception)
                {
                    valid = false;
                    Console.WriteLine("You need to input a number\n");
                } 
            }
            switch (choice)
            {
                case 1:
                    Console.WriteLine("You have chosen the silver bullet");
                    break;
                case 2:
                    Console.WriteLine("You have chosen the wooden stake");
                    break;
                case 3:
                    Console.WriteLine("You have chosen the exorcism spell");
                    break;
                default:
                    Console.WriteLine("You have chose to not arm yourself, best of luck");
                    break;
            }
            return choice;
        }
        //The dangerous confrontation, plus the wait function i made earlier
        static void Cat_Confrontation()
        {
            Console.WriteLine("The shape in the shadows moves towards you.");
            wait(2000);
            Console.WriteLine("Closer and closer it comes until you realise...");
            wait(2000);
            Console.WriteLine("It is a cat!!");
            wait(500);
            Console.WriteLine("You take a breathe, sheathe your weapon and relax. Then the cat stands up on to two legs and pulls out a knife\nRUN!");
        }
        //Part 5 extension
        static void Monster_Confrontation(int weapon)
        {
            var rand = new Random();
            string[] weapons = { "Silver bullet", "Wooden stake", "Exorcism spell" };
            string[] monsters = { "Werewolf", "Vampire", "Ghost" };
            int random_monster = rand.Next(0, 3);

            Console.WriteLine("\nThe shape in the shadows moves towards you.");
            wait(2000);
            Console.WriteLine("Closer and closer it comes until you realise...");
            wait(2000);
            Console.WriteLine($"It is a {monsters[random_monster].ToLower()}!!\n");

            //The ouput message if the player picked the right weapon
            try
            {
                //Weapons and monster are arranged in the order so that a weapon is super effective against a
                //monster who is at the same index
                if ((weapon - 1) == random_monster)
                {
                    Console.WriteLine($"{weapons[weapon - 1]} is super effective against {monsters[random_monster].ToLower()}, you live to see another day");
                }
                else
                {
                    Console.WriteLine($"{weapons[weapon - 1]} is useless against {monsters[random_monster].ToLower()}, I'll say a few words at your funeral");
                }
            }
            //any integer can be input and if it isn't 0,1 or 2 it is outside the the early arrays meaning this would be throw up an error 
            catch (Exception)
            {
                Console.WriteLine($"Should've chose a weapon, huh? The {monsters[random_monster].ToLower()} has an easy feast");
            }
        }
        static void Main(string[] args)
        {
            Initial_text();
            int weapon = Weapon_Selection();
            //Cat_Confrontation();
            Monster_Confrontation(weapon);
        }
    }
}
