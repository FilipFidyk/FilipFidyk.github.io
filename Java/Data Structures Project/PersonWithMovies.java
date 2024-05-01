package structures;

import stores.Person;

public class PersonWithMovies {

    private Person personInfo;
    private CustomArrayList<Integer> movieIDs;

    public PersonWithMovies (Person inPersonInfo){
        this.personInfo = inPersonInfo;
        this.movieIDs = new CustomArrayList<>();
    }

    public Person getPerson(){
        return this.personInfo;
    }

    public CustomArrayList<Integer> getMovieIDs(){
        return movieIDs;
    }

    public boolean addMovieID(int movieID){
        return this.movieIDs.add(movieID);
    }

    public boolean removeMovieID(int movieID){
        return this.movieIDs.remove(movieID);
    }
}
