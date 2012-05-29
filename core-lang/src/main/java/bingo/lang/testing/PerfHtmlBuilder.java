package bingo.lang.testing;

import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PerfHtmlBuilder {
	
	public static Document buildUpMatrixHtml(String projectName, PerfResult[][] resultMatrix) {
		Document doc = DocumentHelper.createDocument();
    	Element html = doc.addElement("html");
    	html.addAttribute("lang", "en");
    	buildUpHead(projectName, html);
    	buildUpBody(projectName, html, resultMatrix);
	    return doc;
    }
	
	private static Element buildUpHead(String projectName, Element html){
    	Element head = html.addElement("head");
    	head.addElement("title").addText("The Performance Report of \"" + projectName + "\"");
    	Element link = head.addElement("link");
    	link.addAttribute("href", "http://twitter.github.com/bootstrap/assets/css/bootstrap.css");
    	link.addAttribute("rel", "stylesheet");
    	head.addElement("style").addText("body{padding:20px}");
    	return head;
	}
	
	private static Element buildUpBody(String projectName, Element html, PerfResult[][] resultMatrix){
		Element body = html.addElement("body");
    	Element divContainer = body.addElement("div");
    	divContainer.addAttribute("class", "containner");
    	divContainer.addElement("H1").addText(projectName);
    	buildUpTable(divContainer, resultMatrix);
    	Element block = divContainer.addElement("blockquote");
    	block.addAttribute("class", "pull-right");
    	block.addElement("p").addText("Generated on " + new Date().toString() + ".");
    	block.addElement("small").addText("powered by bingo.core");
    	return body;
	}
	
	private static Element buildUpTable(Element divContainer, PerfResult[][] resultMatrix){
		Element table = divContainer.addElement("table");
    	table.addAttribute("class", "table table-bordered table-striped");
    	Element thead = table.addElement("thead");
    	Element tr = thead.addElement("tr");
    	tr.addElement("th").addText("Repeat Time\\Code");
    	for (int i = 0; i < resultMatrix[0].length; i++) {
	        tr.addElement("th").addText(resultMatrix[0][i].getName());
        }
    	buildUpTbody(table, resultMatrix);
    	return table;
	}
	
	private static Element buildUpTbody(Element table, PerfResult[][] resultMatrix){
		Element tbody = table.addElement("tbody");
		Element tr = null;
    	for (int i = 0; i < resultMatrix.length; i++) {
    		tr = tbody.addElement("tr");
    		tr.addElement("td").addText("")
    		  .addElement("strong").addText("" + resultMatrix[i][0].getRunTimes());
	        for (int j = 0; j < resultMatrix[i].length; j++) {
	        	PerfResult cell = resultMatrix[i][j];
	            tr.addElement("td").addText("")
	            .addElement("strong").addText(cell.getElapsedNanoseconds() + "")
	            .getParent().addText(" ns.(" + cell.getElapsedMilliseconds() + 
	            		" ms, " + (int)(cell.getElapsedNanoseconds()*1.0/cell.getRunTimes() + 0.5) + " ns each)");
            }
        }
    	return tbody;
	}
}
