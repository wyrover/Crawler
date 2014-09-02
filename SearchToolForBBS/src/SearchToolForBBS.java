import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JScrollPane;

public class SearchToolForBBS extends JApplet
{
	public void init()
	{
		Container cp=getContentPane();
		DevineSearch cursearch=new DevineSearch();
		DevineSearch.result=new ResultList(cursearch);
		setLayout(null);
		cursearch.setBounds(0,0,800,500);
		DevineSearch.result.setBounds(0,500,800,1500);
		cp.add(cursearch);
		cp.add(DevineSearch.result);
	}
}
