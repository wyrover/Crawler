package symptom;

import SQLite.Callback;
import SQLite.Database;

public class querySQL 
{
	private StringBuilder sb=null;
	
	class TableFmt implements Callback
	{	
		@Override
		public void columns(String[] cols) 
		{
		}

		@Override
		public boolean newrow(String[] cols) 
		{
			sb.append("\t<item>\n");
			sb.append("\t\t<name>"+cols[0]+"</name>\n");
			sb.append("\t\t<issymptom>"+cols[2]+"</issymptom>\n");
			sb.append("\t\t<keshi>"+cols[4]+"</keshi>\n");
			sb.append("\t\t<ddescription>"+cols[6]+"</ddescription>\n");
			if(cols[2].equals("1"))
				sb.append("\t\t<data>"+cols[9]+"</data>\n");
			else
				sb.append("\t\t<data>"+cols[7]+"</data>\n");
			sb.append("\t</item>\n");
			return false;
		}

		@Override
		public void types(String[] cols) 
		{
		}
	}
	
	public String querySQLforname(String tosearch) throws SQLite.Exception
	{
		sb=new StringBuilder();
		Database db=new Database();
		db.open("J:/symptom.db",0666);
		String sql="select * from symptomdata where Name like '%"+tosearch+"%' order by Popularity desc limit 100;";
		db.exec(sql,new TableFmt());
		return sb.toString();
	}
	
	public String querySQLforbody(String tosearch) throws SQLite.Exception
	{
		sb=new StringBuilder();
		Database db=new Database();
		db.open("J:/symptom.db",0666);
		String sql="select * from symptomdata where BodyPart like '%"+tosearch+"%' order by Popularity desc limit 100;";
		db.exec(sql,new TableFmt());
		return sb.toString();
	}

	public static void main(String[] args) throws Exception
	{
		System.out.println(new querySQL().querySQLforname("¿œƒÍ»À"));
	}
}
 