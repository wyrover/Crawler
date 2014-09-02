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


public class modify
{
	public static Database db=null;
	
	public static void main(String[] args) throws SQLite.Exception
	{
		final modify pop=new modify();
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
					{
						String sql="UPDATE symptomdata SET Check = '"+cols[11].replace("<","¡¶")+"' WHERE Name = '"+cols[0]+"';";
						db.exec(sql,null);
						System.out.println(cols[0]);
					}
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

		String sql="select * from symptomdata where Check like '%<%';";
		db.exec(sql,new TableFmt());
	} 
}
