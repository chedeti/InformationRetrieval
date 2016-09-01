// Content Object
public class HtmlFile {

	private String URL;
	private String TITLE;
	private String META_KEYWORDS;
	private String DATE;
	private int DOC_ID;
	private String CONTENT;
	
	public void setURL(String url){
		URL = url;
	}
	
	public void setTITLE(String title){
		TITLE = title;
	}
	
	public void setMETA_KEYWORDS(String metakeywords){
		META_KEYWORDS = metakeywords;
	}
	
	public void setDATE(String date){
		 DATE = date;
	}
	
	public void setDOC_ID(int docId){
		DOC_ID = docId;
	}
	
	public void setCONTENT(String content){
		 CONTENT = content;
	}
	
	public String getURL(){
		return URL;
	}
	
	public String getTITLE(){
		return TITLE;
	}
	
	public String getMETA_KEYWORDS(){
		return META_KEYWORDS;
	}
	
	public String getDATE(){
		 return DATE;
	}
	
	public int getDOC_ID(){
		return DOC_ID;
	}
	
	public String getCONTENT(){
		 return CONTENT;
	}
	
}
