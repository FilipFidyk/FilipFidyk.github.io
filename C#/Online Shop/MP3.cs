using System;
using System.Collections.Generic;
using System.Text;

namespace Online_Shop
{
    class MP3: Item
    {
        protected string artist;

        public MP3(string name, string description, double price, string artist): base(name, description, price)
        {
            this.artist = artist;
            Console.WriteLine("Setting Subclass (MP3) Attribute");
        }
        public string getArtist()
        {
            return artist;
        }
    }
}
