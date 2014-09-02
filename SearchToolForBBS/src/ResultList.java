import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ResultList extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7586074484755866311L;
	public static JTable table=null;
	private JScrollPane sp=null;
	private JButton copyaddress=null;
	private JButton stop=null;
	private int index=0;
	private Queue<MySearchThread> threadqueue=null;
	private JLabel state=null;
	private String errorstring=null;
	private static ArrayList<String[]> GlobalData=new ArrayList<String[]>();
	
	class MySearchThread extends Thread
	{
		private String cururl=null;
		private boolean findsub=false;
		public MySearchThread(String cururl,boolean findsub)
		{
			super();
			this.cururl=cururl;
			this.findsub=findsub;
		}
		
		@Override
		public void run()
		{
			errorstring=null;
			try
			{			
				if(DevineSearch.StopSearch)
				{
					return;
				}
				
				Document doc=Jsoup.connect(cururl).timeout(0).get();//源1		
				Elements curresult=doc.select("table.result");
			    for(Element e:curresult)
			    {
			    	String[] arr=new String[]{""+index,"","",""};
			    	Elements rt1=e.select(".t a");
			    	if(!rt1.isEmpty())
			    	{
			    		arr[1]=RemoveEm(rt1.get(0).text());
			    		arr[3]=RemoveEm(rt1.get(0).attr("href"));
			    	}
			    	rt1=e.select(".c-abstract");
			    	if(!rt1.isEmpty())
			    	{
			    		arr[2]=RemoveEm(rt1.get(0).text());
			    	}
			    	GlobalData.add(arr);
			    	if(table != null)
			    		table.invalidate();
			    	
			    	index++;				
			    }		
			    
			    if(findsub)
			    {
					Elements otherresults=doc.select("p#page a");
					for(Element other:otherresults)
					{
						if(!other.text().contains("下一页"));
						{
							threadqueue.offer(new MySearchThread(other.attr("abs:href"),false));
						}
					}
			    }
			}
			catch(Exception e)
			{
				e.printStackTrace();
				errorstring=e.getCause().toString();
			}	
		}
	}
	
	class DataModel extends AbstractTableModel
	{	
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int getRowCount() 
		{
			// TODO Auto-generated method stub
			return index;
		}

		@Override
		public int getColumnCount() 
		{
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public String getColumnName(int column)
		{
			if(column == 0)
				return "序号";
			else if(column == 1)
				return "标题";
			else if(column == 2)
				return "其他信息";
			else 
				return "网址";
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) 
		{
			// TODO Auto-generated method stub
			return (GlobalData.get(rowIndex))[columnIndex];
		}
		
		@Override
		public void setValueAt(Object val,int row,int col)
		{
			(GlobalData.get(row))[col]=(String) val;
		}
	}
	
	public void ClearDefaultTable()
	{
		GlobalData.clear();
		index=0;
		if(table == null)
			return;
		table.setModel(new DataModel());
	}
	
	public ResultList(DevineSearch father) 
	{
		super();
		setLayout(null);
		
		stop = new JButton("\u505C\u6B62\u641C\u7D22");
		stop.setBounds(35, 567, 93, 23);
		add(stop);
		
		copyaddress = new JButton("\u590D\u5236\u94FE\u63A5");
		copyaddress.setBounds(166, 567, 93, 23);
		add(copyaddress);
		
		table = new JTable(new DataModel());
		ClearDefaultTable();
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(3).setPreferredWidth(200);
		table.setBounds(10, 10, 564, 288);
		table.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
					{
						int sel=table.getSelectedColumn();
						if(sel != -1)
						{
							Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + (GlobalData.get(sel))[3]);
							System.out.println((GlobalData.get(sel))[3]);
						}
					}
				}
				catch(Exception ex)
				{
				}
			}
		});
		sp=new JScrollPane(table);
		sp.setBounds(0, 0, 763, 557);
		add(sp);
		
		state = new JLabel("");
		state.setBounds(270, 339, 271, 19);
		add(state);
				
		try
		{
			stop.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					DevineSearch.StopSearch=true;
				}
			});
			
			copyaddress.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					if(table.getSelectedRowCount() > 0)
					{
						String cursel=(String)table.getValueAt(table.getSelectedRow(),3);
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(cursel),null);				
					}
				}
			});
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		setSize(650,600);
		setVisible(true);
		
		threadqueue=new LinkedList<MySearchThread>();
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					while(true)
					{			
						Thread.sleep(100);
						if(!threadqueue.isEmpty())
						{
							state.setText("处理中");
							MySearchThread cur=threadqueue.poll();
							if(cur == null)
								continue;
							cur.start();
							cur.join();
							if(errorstring == null)
								state.setText("处理完毕");
							else
								state.setText("出错："+errorstring);
						}
						else
							state.setText("空闲");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}	
			}
		}.start();
	}
	
	public void SearchOne(String url,boolean findsub)
	{
		threadqueue.offer(new MySearchThread(url,true));
	}
	
	private String RemoveEm(String src)
	{
		src.replace("<em>","");
		src.replace("</em>","");
		return src;
	}
	
	public static void main(String[] args)
	{
		new ResultList(null).SearchOne("",true);
	}
}
