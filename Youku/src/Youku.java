import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class Youku 
{
 static class YoukuMv
 {
  private String title;
  private String img;
  private String time;
  private String submitUser;
  private long playTimes;
  
  public String getImg(){return img;}
  public void setImg(String img){this.img=img;}
  public long getPlayTimes(){return playTimes;}
  public void setPlayTimes(long playTimes){this.playTimes=playTimes;}
  public String getSubmitUser(){return submitUser;}
  public void SetSubmitUser(String submitUser){this.submitUser=submitUser;}
  public String getTime(){return time;}
  public void setTime(String time){this.time=time;}
  public String getTitle(){return title;}
  public void setTitle(String title){this.title=title;}
  
 }
 
 public static void main(String[] args) throws IOException
 {
  Document doc=Jsoup.connect("http://www.soku.com/search_video/q_mv").timeout(10000).get();
  Elements elms=doc.select(".v");
  for(Element e : elms)
  {
   YoukuMv mv=parseMv(e);
   System.out.println(mv.getTitle()
        +"\nsubmituser="+mv.getSubmitUser()
        +"\nplaytime="+mv.getPlayTimes()
        +"\nimg="+mv.getImg()
        +"\ntime="+mv.getTime()+"\n\n");
  }
 }
 
 static YoukuMv parseMv(Element e)
 {
  YoukuMv mv=new YoukuMv();
  mv.setImg(e.select("div.v-thumb img").attr("src"));
  mv.setTitle(e.select("div.v-link a").attr("title"));
  mv.SetSubmitUser(e.select("div.v-meta-data").get(0).select("span.username a").text());
  mv.setPlayTimes(Long.parseLong(e.select("div.v-meta-data").get(1).select("span").text().replaceAll(",","")));
  mv.setTime(e.select("div.v-meta-data").get(2).select("span.pub").text());
  return mv;
 }
}
