using System;
using System.Collections.Generic;
using System.Text;

namespace Online_Shop
{
    class Item
    {
        protected string name, description;
        protected double price;

        public Item(string name, string description, double price)
        {
            this.name = name;
            this.description = description;
            this.price = price;
            Console.WriteLine("Setting Superclass Attributes");
        }
        public string getName()
        {
            return name;
        }
        public string getDescription()
        {
            return description;
        }
        public double getPrice()
        {
            return price;
        }
    }
}
