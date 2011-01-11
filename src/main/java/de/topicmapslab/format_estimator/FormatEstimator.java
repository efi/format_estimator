package de.topicmapslab.format_estimator;

import static de.topicmapslab.format_estimator.FormatEstimator.Format.*;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 
 * Estimates the format of a Topic Map File
 * 
 * @author Thomas Efer <mail@thomasefer.de>
 * 
 * Documented and managed by Uta Schulze <uta.schulze@informatik.uni-leipzig.de>
 *
 */
public class FormatEstimator {

	// Be sure to use the is() method for unversioned format checking!
	public static enum Format {
		FORMAT_UNKNOWN, // default and worst case
		ASTMA, ASTMA_1_0, ASTMA_2_0,
		BTM, BTM_1_0,
		CTM, CTM_1_0, CTM_PRE_1_0,
		CXTM,
		JTM, JTM_1_0, JTM_1_1,
		LTM, LTM_1_0, LTM_1_1, LTM_1_2, LTM_1_3,
		RDF, RDF_N3, RDF_XML,
		TM_JSON,
		TM_XML,
		XTM, XTM_1_0, XTM_1_1, XTM_2_0, XTM_2_1;
		
		// like equals() but with "supertype" matching
		public boolean is(Format f) { Format t = this;
			if (t==f) return true;
			else switch (f) {
				case ASTMA: return t==ASTMA_1_0||t==ASTMA_2_0;
				case BTM:   return t==BTM_1_0;
				case CTM:   return t==CTM_1_0||t==CTM_PRE_1_0;
				case JTM:   return t==JTM_1_0||t==JTM_1_1;
				case LTM:   return t==LTM_1_0||t==LTM_1_1||t==LTM_1_2||t==LTM_1_3;
				case RDF:   return t==RDF_N3||t==RDF_XML;
				case XTM:   return t==XTM_1_0||t==XTM_1_1||t==XTM_2_0||t==XTM_2_1;
				default:    return false; 
		}}
	}

	
	// List of RegEx Patterns and their corresponding Formats 
	private static List<Entry<Pattern,Format>> regexes = new ArrayList<Entry<Pattern, Format>>(){{
		add(entry("%version\\s+1.0", CTM_1_0));
		add(entry("DTD XML Topic Map[s]? \\(XTM\\) 1[.]0", XTM_1_0));
		add(entry("DTD XML Topic Maps \\(XTM\\) 1[.]1", XTM_1_1));
		add(entry("DTD XML Topic Maps \\(XTM\\) 2[.]0", XTM_2_0));
		add(entry("DTD XML Topic Maps \\(XTM\\) 2[.]1", XTM_2_1));
		add(entry("xmlns\\s*=\\s*[\"']http://www[.]topicmaps[.]org/xtm/1[.]0/", XTM_1_0));
		add(entry("xmlns:tm\\s*=\\s*[\"']http://psi[.]ontopia[.]net/xml/tm-xml/", TM_XML));
		add(entry("xtm1[.]dtd", XTM_1_0));
		add(entry("@(base|prefix|keywords)",RDF_N3));
		add(entry("version\\s*=\\s*[\"']2[.]0[\"']", XTM_2_0));
		add(entry("version\\s*=\\s*[\"']2[.]1[\"']", XTM_2_1));
		add(entry("\"?version\"?\\s*:\\s*\"1[.]0\"?", JTM_1_0));
		add(entry("\"?version\"?\\s*:\\s*\"1[.]1\"?", JTM_1_1));
		add(entry("\"?item_type\"?\\s*:\\s*\"", JTM));
		add(entry("(\\s(ako|iko))|(%(prefix|mergemap))\\s", CTM_PRE_1_0));
		add(entry("rdf:Description",RDF_XML));
		add(entry("http://www[.]w3[.]org/1999/02/22-rdf-syntax-ns#",RDF));
		add(entry("#VERSION \"1[.]3\"", LTM_1_3));
		add(entry("#VERSION \"1[.]2\"", LTM_1_2));
		add(entry("#VERSION \"1[.]1\"", LTM_1_1));
		add(entry("#VERSION \"1[.]0\"", LTM_1_0));
		add(entry("#TOPICMAP\\s", LTM));
		
	}};
	
	// Delivers Map.Entry Objects to be used in lists for ordered execution of patterns
	private static Entry<Pattern,Format> entry(String patternString, Format format) {
		final String outerPatternString = patternString; final Format outerFormat = format;
		return new Entry<Pattern,Format>(){
			String innerPatternString; Pattern pattern; Format innerFormat;
			{
				innerPatternString=outerPatternString;
				pattern=Pattern.compile(this.innerPatternString, Pattern.MULTILINE & Pattern.DOTALL);
				innerFormat=outerFormat;
			}
			public Pattern getKey() { return pattern; }
			public Format getValue() { return innerFormat; }
			public Format setValue(Format value) {return value;}
		};
	}
	
	// Executes ordered RegEx Patterns to find certain format specialities
	public static Format guessFormat( Reader reader ) throws IOException{
		return guessFormat(reader, 1024);
	}
	public static Format guessFormat( Reader reader, int byteCount ) throws IOException{
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < byteCount && reader.ready(); i++) sb.append((char)reader.read());
		String string = sb.toString(); sb = null;
		
		for (Entry<Pattern,Format> entry: regexes){
			if (entry.getKey().matcher(string).find()) return entry.getValue();
		}		
		return FORMAT_UNKNOWN;
	}
	
}
