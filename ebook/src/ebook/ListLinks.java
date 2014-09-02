package ebook;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
* Example program to list links from a URL.
*/
public class ListLinks 
{
   public static void main(String[] args) throws IOException 
   {
    long startTime=System.currentTimeMillis();
    
    Document doc = Jsoup.connect("http://www.yi-see.com/art_14990_7513.html").timeout(0).get(); 
    Elements clicks = doc.select("a[href*=read_]"); 
    Map<Integer,String> map=new TreeMap<Integer,String>();
    FileWriter fw=new FileWriter(doc.select("span.T1 a").text());
    fw.write("<html>\r\n<head/>\r\n<body>\r\n");
    fw.write("<H1><center>"+doc.select("span.T1 a").text()+"</center></H1>\r\n");
    fw.write("<H3><center>"+doc.select("span.TA").text()+"</center></H3>\r\n");
    
    for(Element et : clicks)
    { 
     String chapternum=et.text();
     map.put(Integer.parseInt(chapternum.substring(1, chapternum.length()-1)),et.attr("abs:href"));
    } 
    
    for(Iterator it=map.entrySet().iterator();it.hasNext();)
    {
     Map.Entry entry=(Map.Entry)it.next();
     Document indoc=Jsoup.connect((String)entry.getValue()).timeout(0).get(); 
     Element inclick=indoc.select("TD[class=ART]").first(); 
     System.out.println(inclick.toString());
        fw.write("<H3><center>第"+(int)entry.getKey()+"章</center></H3>\r\n"+inclick.toString()+"\r\n"); 
        fw.flush();
    }
    
    fw.write("</body>\r\n</html>"); 
    fw.flush();
    fw.close();
    
    System.out.println("共用时:"+(System.currentTimeMillis()-startTime));
}
}
