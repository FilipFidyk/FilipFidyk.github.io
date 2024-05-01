package structures;

import stores.CastCredit;
import stores.CrewCredit;

public class CreditsNode {
    private int id;
    private CastCredit[] cast;
    private CrewCredit[] crew;

    public CreditsNode(CastCredit[] inCast, CrewCredit[] inCrew, int inID){
        this.id = inID;
        this.cast = inCast;
        this.crew = inCrew;
    }

    public int getFilmID(){
        return this.id;
    }

    public CastCredit[] getCast(){
        return this.cast;
    }

    public CrewCredit[] getCrew(){
        return this.crew;
    }
}
