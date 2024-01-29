using System;
using System.Collections.Generic;
using System.Text;

namespace Online_Shop
{
    class DVD: Item
    {
        protected string certificate;
        protected int length;

        public DVD(string name, string description, double price, string certificate, int length) : base(name, description, price)
        {
            this.certificate = certificate;
            this.length = length;
            Console.WriteLine("Setting Subclass (DVD) Attribute");
        }
        public string getCertificate()
        {
            return certificate;
        }
        public int getLength()
        {
            return length;
        }
    }
}
