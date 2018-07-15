package bros.manage.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TelegraphAnalyser {

	static final int OUTOFTELEGRAPH=1;
	static final int INZCZCTELEGRAPH=2;
	static final int INSOHTELEGRAPH=3;
	int tokenizeStatus=OUTOFTELEGRAPH;
	
	int stopbit;
	static final int RUN=0;
	static final int SAFELYSTOP=1;
	static final int STOPNOW=2;
	static final int ERROR=3;
	
	int tempnum;
	
	char tempChar;
	String teleContent;
	String tempWord;
	BufferedReader reader; 
	String teleflag;
	InputStream bytestream;
	
	

	public TelegraphAnalyser(InputStream in){
//		try {
			bytestream = in;
			reader=new BufferedReader(new InputStreamReader(in));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
		tokenizeStatus=OUTOFTELEGRAPH;
		teleContent="";
		teleflag="";
		tempWord="";
		tempChar=0;
		stopbit=RUN;
		
	}
	
	public boolean analysisNextChar(){
		try {
//			tempnum = bytestream.read();
			tempnum=reader.read();
			tempChar=(char)tempnum;
//			System.out.print("("+tempChar+")");
//			MainWindow.mainBoard.addMsg(String.valueOf(tempnum), LocalBoard.INFO_ERROR);
			//tempChar=(char)reader.read();
		} catch (IOException e) {
			e.printStackTrace();
			stopbit=ERROR;
			return false;
		}
		catch(Exception ex){
			ex.printStackTrace();
			stopbit = ERROR;
			return false;
		}
		if (tempnum<0){
			stopbit=ERROR;
			return false;
		}
		if(tokenizeStatus==OUTOFTELEGRAPH){
			if ((tempChar=='Z'&&(!tempWord.equalsIgnoreCase("ZC")))||tempChar=='S')
				tempWord="";
			tempWord=tempWord+tempChar;
	//		MainWindow.mainBoard.addMsg("head:"+tempWord, LocalBoard.INFO_ERROR);
			if (tempWord.equalsIgnoreCase("ZCZC")){
					teleContent="ZCZC";
					tempWord="";
					tokenizeStatus=INZCZCTELEGRAPH;
				}
			else if (tempWord.equalsIgnoreCase("SOH")){
					teleContent="SOH";
					tempWord="";
					tokenizeStatus=INSOHTELEGRAPH; 
				}
		}		
		else if (tokenizeStatus==INZCZCTELEGRAPH){
			if(tempChar=='N'&&(!tempWord.equalsIgnoreCase("N"))
					&&(!tempWord.equalsIgnoreCase("NN"))
					&&(!tempWord.equalsIgnoreCase("NNN"))){
				teleContent=teleContent+tempWord;
				//MainWindow.mainBoard.addMsg("content:"+teleContent, LocalBoard.INFO_ERROR);
				tempWord="";
			}
			tempWord=tempWord+tempChar;	
			if (tempWord.equalsIgnoreCase("NNNN")){
				teleContent=teleContent+"NNNN";
				teleflag="ZCZC";
				tokenizeStatus=OUTOFTELEGRAPH;
				if (stopbit==SAFELYSTOP)
					stopbit=STOPNOW;
				return true;
			}

		}
		else if (tokenizeStatus==INSOHTELEGRAPH){
			if(tempChar=='E'){
				teleContent=teleContent+tempWord;
				tempWord="";
			}
			tempWord=tempWord+tempChar;	
			if (tempWord.equalsIgnoreCase("ETX")){
				teleContent=teleContent+"ETX";
				teleflag="SOH";
				tokenizeStatus=OUTOFTELEGRAPH;
				if (stopbit==SAFELYSTOP)
					stopbit=STOPNOW;
				return true;
			}
		}
		return false;
	}
	
	public String getTeleContent(){
		return teleContent;
	}
	
	public String getTeleflag(){
		return teleflag;
	}
	
	public int getStopbit(){
		return stopbit;
	}
	
	public void StopSafely(){
		stopbit=SAFELYSTOP;
	}

	public boolean getWorkingStatus(){
		return  (tokenizeStatus==INZCZCTELEGRAPH)||(tokenizeStatus==INSOHTELEGRAPH);
	}
}
