using System;
using System.Collections.Generic;
using System.Text;

namespace Online_Shop
{
    class DVDCollection
    {
        protected DVD[] filmcollection;
        protected int numFilms = 0;
        protected int maxFilms = 6;

        public DVDCollection()
        {
            filmcollection = new DVD[6];
        }
        public int getNumFilms()
        {
            return numFilms;
        }
        public void addFilm(DVD film)
        {
            Console.WriteLine("Adding Film");
            filmcollection[numFilms] = film;
            numFilms++;
        }
        public void showFilms()
        {
            foreach (DVD dvd in filmcollection)
            {
                Console.WriteLine($"Title: {dvd.getName()}\nCertificate: {dvd.getCertificate()}\n" +
                                  $"Length: {dvd.getLength()}\nDescription: {dvd.getDescription()}\n" +
                                  $"Price: £{dvd.getPrice()}\n");
            }
        }
    }
}
