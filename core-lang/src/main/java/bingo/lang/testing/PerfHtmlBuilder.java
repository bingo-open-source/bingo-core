package bingo.lang.testing;

import java.util.Date;
import java.util.List;

import bingo.lang.xml.XmlAttribute;
import bingo.lang.xml.XmlElement;
import bingo.lang.xml.XmlText;

class PerfHtmlBuilder {
	
	public static XmlElement buildUpMatrixHtml(String projectName, PerfResult[][] resultMatrix) {
		XmlElement html = new XmlElement("html", new XmlAttribute("lang", "en"));
    	buildUpHead(projectName, html);
    	buildUpMatrixBody(projectName, html, resultMatrix);
	    return html;
    }
	
	public static XmlElement buildUpGroupHtml(String projectName, List<PerfResult> resultList) {
		XmlElement html = new XmlElement("html", new XmlAttribute("lang", "en"));
    	buildUpHead(projectName, html);
    	buildUpGroupBody(projectName, html, resultList);
	    return html;
	}
	
    private static XmlElement buildUpGroupBody(String projectName, XmlElement html, List<PerfResult> resultList){
    	XmlElement tbody = null;
    	XmlElement body = new XmlElement("body", 
    		new XmlElement("h2", new XmlText(projectName)),
    		new XmlElement("table", 
    			new XmlAttribute("class", "table table-bordered table-striped"),
    			new XmlElement("thead", 
    				new XmlElement("tr", 
    					new XmlElement("th", new XmlText("Name")),
    					new XmlElement("th", new XmlText("Run Times")),
    					new XmlElement("th", new XmlText("ms")),
    					new XmlElement("th", new XmlText("ns"))
    					)),
    			tbody = new XmlElement("tbody")));

    	buildUpGroupTbody(resultList, tbody);
    	html.add(body);
    	return body;
    }
    
    private static void buildUpGroupTbody(List<PerfResult> resultList, XmlElement tbody){
    	for (PerfResult per : resultList) {
        	XmlElement tr = new XmlElement("tr", 
            		new XmlElement("td", new XmlText(per.getName())),
            		new XmlElement("td", new XmlText(per.getRunTimes() + "")),
            		new XmlElement("td", new XmlText(per.getElapsedMilliseconds() + "")),
            		new XmlElement("td", new XmlText(per.getElapsedNanoseconds() + ""))
        		);
        	tbody.add(tr);
        }
    }
	
	private static XmlElement buildUpHead(String projectName, XmlElement html){
    	XmlElement head = 
    		new XmlElement("head", 
    			new XmlElement("title", 
    				new XmlText("The Performance Report of \"" + projectName + "\"")),
				new XmlElement("link", 
	    			new XmlAttribute("href", "http://twitter.github.com/bootstrap/assets/css/bootstrap.css"),
	    			new XmlAttribute("rel", "stylesheet")),
		    	new XmlElement("style", new XmlText("body{padding:20px}")));
    	html.add(head);
    	return head;
	}
	
	private static XmlElement buildUpMatrixBody(String projectName, XmlElement html, PerfResult[][] resultMatrix){
		XmlElement divContainer = null;
		XmlElement body = 
			new XmlElement("body", 
 divContainer = new XmlElement("div", 
				new XmlAttribute("class", "containner"),
				new XmlElement("h1", new XmlText(projectName))));
    	buildUpMatrixTable(divContainer, resultMatrix);
    	divContainer.add(
	    	new XmlElement("blockquote",
	    		new XmlAttribute("class", "pull-right"),
	    		new XmlElement("p", new XmlText("Generated on " + new Date().toString() + ".")),
	    		new XmlElement("small", new XmlText("powered by bingo.core"))));
    	html.add(body);
    	return body;
	}
	
	private static XmlElement buildUpMatrixTable(XmlElement divContainer, PerfResult[][] resultMatrix){
		XmlElement tr = null;
		XmlElement table = 
			new XmlElement("table", 
				new XmlAttribute("class", "table table-bordered table-striped"),
				new XmlElement("thead", 
					tr = new XmlElement("tr")));
		tr.add(new XmlElement("th", new XmlText("Repeat Time\\Code")));
    	for (int i = 0; i < resultMatrix[0].length; i++) {
	        tr.add(new XmlElement("th", new XmlText(resultMatrix[0][i].getName())));
        }
    	buildUpMatrixTbody(table, resultMatrix);
    	divContainer.add(table);
    	return table;
	}
	
	private static XmlElement buildUpMatrixTbody(XmlElement table, PerfResult[][] resultMatrix){
		XmlElement tbody = new XmlElement("tbody");
		XmlElement tr = null;
    	for (int i = 0; i < resultMatrix.length; i++) {
    		tr = new XmlElement("tr");
    		tr.add(new XmlElement("td", 
    				new XmlElement("strong", new XmlText(resultMatrix[i][0].getRunTimes() + ""))));
	        for (int j = 0; j < resultMatrix[i].length; j++) {
	        	PerfResult cell = resultMatrix[i][j];
	    		tr.add(new XmlElement("td", 
	    				new XmlElement("strong", new XmlText(cell.getElapsedNanoseconds() + "")),
	    				new XmlText(" ns.(" + cell.getElapsedMilliseconds() + " ms, "
	    						+ (int)(cell.getElapsedNanoseconds()*1.0/cell.getRunTimes() + 0.5)
	    						+ " ns each)")));
            }
	        tbody.add(tr);
        }
    	table.add(tbody);
    	return tbody;
	}
}
