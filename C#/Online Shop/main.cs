using System;
using System.IO;

namespace Online_Shop
{
    class Program
    {
        static void Main(string[] args)
        {
            //DVD myDVD = new DVD("Venom", "The Black Spiderman", 6.99, "8/10", 132);
            //Console.WriteLine($"Title: {myDVD.getName()}\nCertificate: {myDVD.getCertificate()}\n" +
            //                  $"Length: {myDVD.getLength()}\nDescription: {myDVD.getDescription()}\n" +
            //                  $"Price: £{myDVD.getPrice()}\n");

            //MP3 myMP3 = new MP3("Until Next Time", "One the better tunes of 21st century", 6.99, "NF");
            //Console.WriteLine($"Title: {myMP3.getName()}\nArtist: {myMP3.getArtist()}\n" +
            //                  $"Description: {myMP3.getDescription()}\nPrice: £{myDVD.getPrice()}\n");

            //Book myBook = new Book("Star Wars Aftermath", "The following events of the empire falling", 10.99, "Chuck Wendig", "Dystopian Action");
            //Console.WriteLine($"Title: {myBook.getName()}\nAuthor: { myBook.getAuthor()}\n" +
            //                  $"Genre: {myBook.getGenre()}\nDescription: {myBook.getDescription()}\n" +
            //                  $"Price: £{myBook.getPrice()}\n");

            string fileName = "filmDetail.txt";
            string strDVDInfo = string.Empty;
            string[] arrSeparateFilmDets = new string[5];

            string film, description, certificate;
            double price;
            int length;

            DVDCollection DVDs = new DVDCollection();

            StreamReader sr = new StreamReader(fileName);
            
            while ((strDVDInfo=sr.ReadLine()) != null)
            {
                arrSeparateFilmDets = strDVDInfo.Split(" ");
                
                film = arrSeparateFilmDets[0];
                description = arrSeparateFilmDets[1];
                price = Convert.ToDouble(arrSeparateFilmDets[2]);
                certificate = arrSeparateFilmDets[3];
                length = Convert.ToInt32(arrSeparateFilmDets[4]);

                DVD dvd = new DVD(film, description, price, certificate, length);
                DVDs.addFilm(dvd);
            }

            sr.Close();

            if(DVDs.getNumFilms() > 0)
            {
                DVDs.showFilms();
            }
        }
    }
}
