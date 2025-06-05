import java.util.*;

public class tp2 {
    
    private String SHOW_ID;
    private String TYPE;
    private String TITLE;
    private String[] DIRECTOR;
    private String[] CAST;
    private String[] COUNTRY;
    private Date DATE_ADDED;
    private int RELEASE_YEAR;
    private String RATING;
    private String DURATION;
    private String[] LISTED_IN;

    public tp2(){ //contrutor padrão
        
        this.SHOW_ID = "";
        this.TYPE = "";
        this.TITLE = "";
        this.DIRECTOR = null;
        this.CAST = null;
        this.COUNTRY = null;
        this.DATE_ADDED = new Date();
        this.RELEASE_YEAR = 0;
        this.RATING = "";
        this.DURATION = "";
        this.LISTED_IN = null;


   }

   public tp2(String SHOW_ID, String TYPE, String TITLE, String[] DIRECTOR, String[] CAST, String[] COUNTRY, Date DATE_ADDED, int RELEASE_YEAR, String RATING, String DURATION, String[] LISTED_IN){  //contrutor com os atributos

        this.SHOW_ID = SHOW_ID;
        this.TYPE = TYPE;
        this.TITLE = TITLE;
        this.DIRECTOR = DIRECTOR;
        this.CAST = CAST;
        this.COUNTRY = COUNTRY;
        this.DATE_ADDED = DATE_ADDED;
        this.RELEASE_YEAR = RELEASE_YEAR;
        this.RATING = RATING;
        this.DURATION = DURATION;
        this.LISTED_IN = LISTED_IN;

   }
   
   // nos sets e nos gets e sets ja posso tratar o ID arrancando o "s" e tranformando o id em int.
   
   public String getSHOW_ID(){ 
      return this.SHOW_ID;
   }
     public void setSHOW_ID(String SHOW_ID){
        this.SHOW_ID = SHOW_ID;
    }

    public String getTYPE(){
        return this.TYPE;
    }
    public void setTYPE(String TYPE){
        this.TYPE = TYPE;
    }

    public String getTITLE(){
        return this.TITLE;
    }
    public void setTITLE(String TITLE){
        this.TITLE = TITLE;
    }

    public String[] getDIRECTOR(){
        return this.DIRECTOR;
    }
    public void setDIRECTOR(String[] DIRECTOR){
        this.DIRECTOR = DIRECTOR;
    }

    public String[] getCAST(){
        return this.CAST;
    }
    public void setCAST(String[] CAST){
        this.CAST = CAST;
    }

    public String[] getCOUTRY(){
        return this.COUNTRY;
    }
    public void setCOUTRY(String[] COUTRY){
        this.COUNTRY = COUTRY;
    }

    public Date getDATE_ADDED(){
        return this.DATE_ADDED;
    }
    public void setDATE_ADDED(Date DATE_ADDED){
        this.DATE_ADDED = DATE_ADDED;
    }

    public int getRELEASE_YEAR(){
        return this.RELEASE_YEAR;
    }
    public void setRELEASE_YEAR(int RELEASE_YEAR){
        this.RELEASE_YEAR = RELEASE_YEAR;
    }

    public String getRATING(){
        return this.RATING;
    }
    public void setRATING(String RATING){
        this.RATING= RATING;
    }

    public String getDURATION(){
        return this.DURATION;
    }
    public void setDURATION (String DURATION){
        this.DURATION = DURATION;
    }

    public String[] getLISTED_IN(){
        return this.LISTED_IN;
    }
    public void setLISTED_IN(String[] LISTED_IN){
        this.LISTED_IN = LISTED_IN;
    }

}
