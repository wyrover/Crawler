package symptom;

import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import symptom.querySQL.TableFmt;
import symptom.zzqm120com.ELE;
import SQLite.Callback;
import SQLite.Database;


public class getPopularity
{
	public static int threadnum=256;
	private static Queue<String> dataQueue=new LinkedList<String>();//ÈÈ´Ê¶ÓÁÐ
	public static Database db=null;
	
	public int getFromBaidu(String search)
	{
		String result="";
		int ret=0;
		try
		{
		     Document doc=Jsoup.connect("http://www.baidu.com/s?q1=&q2="+search+"&q3=&q4=&rn=10&lm=0&ct=0&ft=&q5=&q6=").timeout(0).get();
		     Elements items=doc.select(".nums");
		     result= items.text().replaceAll("[^0123456789,]+","").replace(",","");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(!result.equals(""))
		{
			ret=Integer.parseInt(result);
		}
		return ret;
	}
	
	public void process(String str)
	{
		if(str == null)
			return;
		threadnum--;
		try 
		{
			int popularity=getFromBaidu(str);
			String sql="UPDATE symptomdata SET Popularity = '"+popularity+"' WHERE Name = '"+str+"';";
			db.exec(sql,null);
			System.out.println(str+"\tpop:"+popularity+"\tleft:"+dataQueue.size());
			Thread.sleep(200);
		} 
		catch (SQLite.Exception e)
		{
			e.printStackTrace();
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			threadnum++;
		}
	}
	
	public static void main(String[] args) throws SQLite.Exception
	{
		final getPopularity pop=new getPopularity();
		db=new Database();
		db.open("J:/symptom.db",0666);
		
		class TableFmt implements Callback
		{	
			@Override
			public void columns(String[] cols) 
			{
			}

			@Override
			public boolean newrow(String[] cols) 
			{
				try
				{
					if(cols.length != 0)
						dataQueue.add(cols[0]);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				return false;
			}

			@Override
			public void types(String[] cols) 
			{
			}
		}

		String sql="select distinct Name from symptomdata where Popularity = '0';";
		db.exec(sql,new TableFmt());
		
		new Thread()
		{
			@Override
			public void run()
			{
				try 
				{
					sleep(1000);
					while(!dataQueue.isEmpty())
					{
						while(threadnum > 0 && !dataQueue.isEmpty())
						{
							new Thread()
							{
								@Override
								public void run()
								{
									pop.process(dataQueue.poll());
								}
							}.start();
						}
					}
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}.start();
	} 
}
