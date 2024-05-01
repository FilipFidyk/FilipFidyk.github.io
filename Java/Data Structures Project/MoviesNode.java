package structures;

import java.time.LocalDate;
import stores.Company;
import stores.Genre;

//The class which stores all information needed for a movie 
public class MoviesNode {
    //Attributes set by the constructor
    private int tmdbID;
    private String title;
    private String originalTitle;
    private String overview;
    private String tagline;
    private String status;
    private Genre[] genres;
    private LocalDate release;
    private long budget;
    private long revenue;
    private String[] languages;
    private String originalLanguage;
    private double runtime;
    private String homepage;
    private boolean adult;
    private boolean video;
    private String poster;

    //Attributes set by mutators
    private String imdbID;
    private Company[] productionCompanies;
    private String[] productionCountries;
    private double popularity;
    private double voteAverage;
    private int voteCount;
    
    //Collections and movies are stored independently so only a pointer to the correct collection is necessary
    private int collectionID;

    public MoviesNode(int inTmdbID, String inTitle, String inOriginalTitle, String inOverview, String inTagline, String inStatus, Genre[] inGenres, LocalDate inRelease, long inBudget, long inRevenue, String[] inLanguages, String inOriginalLanguage, double inRuntime, String inHomepage, boolean inAdult, boolean inVideo, String inPoster){
        tmdbID = inTmdbID;
        title = inTitle;
        originalTitle = inOriginalTitle;
        overview = inOverview;
        tagline = inTagline;
        status = inStatus;
        genres = inGenres;
        release = inRelease;
        budget = inBudget;
        revenue = inRevenue;
        languages = inLanguages;
        originalLanguage = inOriginalLanguage;
        runtime = inRuntime;
        homepage = inHomepage;
        adult = inAdult;
        video = inVideo;   
        poster = inPoster;

        //Production Companies and countries array sizes will increase with insertion
        //The impact on running time should be negligbly small whilst not wasting space   
        productionCompanies = new Company[0];
        productionCountries = new String[0];
        popularity = 0;
        voteAverage = 0;
        voteCount = 0;
        collectionID = -1;

    }

    public int getTmdbID(){
        return tmdbID;
    }

    public String getTitle(){
        return title;
    }

    public String getOriginalTitle(){
        return originalTitle;
    }

    public String getOverview(){
        return overview;
    }

    public String getTagline(){
        return tagline;
    }

    public String getStatus(){
        return status;
    }

    public Genre[] getGenres(){
        return genres;
    }

    public LocalDate getRelease(){
        return release;
    }

    public long getBudget(){
        return budget;
    }

    public long getRevenue(){
        return revenue;
    }

    public String[] getLanguages(){
        return languages;
    }

    public String getOriginalLanguage(){
        return originalLanguage;
    }

    public double getRuntime(){
        return runtime;
    }

    public String getHomepage(){
        return homepage;
    }

    public boolean getAdult(){
        return adult;
    }

    public boolean getVideo(){
        return video;
    }

    public String getPoster(){
        return poster;
    }

    public boolean setImdbID(String inImdbID){
        imdbID = inImdbID;
        return true;
    }

    public String getImdbID(){
        return imdbID;
    }

    public Company[] getProductionCompanies(){
        return productionCompanies;
    }

    public boolean addProductionCompany(Company comp){
        Company[] temp = productionCompanies;
        productionCompanies = new Company[productionCompanies.length + 1];

        for (int i = 0; i < temp.length; i++){
            productionCompanies[i] = temp[i];
        }

        productionCompanies[temp.length] = comp;
        return true;
    }

    public String[] getProductionCountries(){
        return productionCountries;
    }
    public boolean addProductionCountry(String comp){
        String[] temp = productionCountries;
        productionCountries = new String[productionCountries.length + 1];

        for (int i = 0; i < temp.length; i++){
            productionCountries[i] = temp[i];
        }

        productionCountries[temp.length] = comp;
        return true;
    }

    public double getPopularity(){
        return popularity;
    }

    public boolean setPopularity(double inPopularity){
        popularity = inPopularity;
        return true;
    }

    public double getVoteAverage(){
        return voteAverage;
    }

    public int getVoteCount(){
        return voteCount;
    }

    public boolean setVote(double inVoteAverage, int inVoteCount){
        voteAverage = inVoteAverage;
        voteCount = inVoteCount;
        return true;
    }

    public boolean setCollectionID(int inCollectionID){
        collectionID = inCollectionID;
        return true;
    }

    public int getCollectionID(){
        return collectionID;
    }
}
