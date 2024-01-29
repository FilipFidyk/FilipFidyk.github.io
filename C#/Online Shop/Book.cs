using System;
using System.Collections.Generic;
using System.Text;

namespace Online_Shop
{
    class Book: Item
    {
        protected string author;
        protected string genre;

        public Book(string name, string description, double price, string author, string genre) : base(name, description, price)
        {
            this.author = author;
            this.genre = genre;
            Console.WriteLine("Setting Subclass (Book) Attribute");
        }
        public string getAuthor()
        {
            return author;
        }
        public string getGenre()
        {
            return genre;
        }
    }
}
