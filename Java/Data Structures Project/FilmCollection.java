package structures;

//A class that stores all information regarding a film collection
public class FilmCollection {
    
    private int id;
    private String name;
    private String posterPath;
    private String backdropPath;
    private int[] films;
    private int noOfFilms;


    public FilmCollection(int collectionID, String collectionName, String collectionPosterPath, String collectionBackdropPath) {
        id = collectionID;
        name = collectionName;
        posterPath = collectionPosterPath;
        backdropPath = collectionBackdropPath;
        //A size should fit most collections
        films = new int[10];
        noOfFilms = 0;

        for (int i = 0; i < films.length; i++){
            films[i] = -1;
        }
    }

    //If there are more than 10 films, it can't be that much more in general so the array will be increased by a linear amount
    private void increaseArraySize(){
        int[] temp = new int[films.length + 10];

        for (int i = 0; i < films.length; i++){
            temp[i] = films[i];
        }

        films = temp;
    }

    public boolean addFilm(int filmID){
        if (noOfFilms == films.length){
            increaseArraySize();
        }

        //every entry has to be unique
        for (int i = 0; i < films.length; i++){
            if (filmID == films[i]){
                return false;
            }
        }

        films[noOfFilms] = filmID;
        noOfFilms++;
        return true;
    }

    public int getCollectionID(){
        return id;
    }

    public String getCollectionName(){
        return name;
    }

    public String getCollectionPosterPath(){
        return posterPath;
    }

    public String getCollectionBackdropPath(){
        return backdropPath;
    }

    //the array has to be resized
    public int[] getCollectionFilms(){
        int[] output = new int[noOfFilms];

        for (int i = 0; i < noOfFilms; i++){
            output[i] = films[i];
        }

        return output;
    }
}
