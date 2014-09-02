package hotword;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class hotword extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> words=new ArrayList<String>();
	private JTextField specWord=null;
	private JTable table=null;
	private JButton btnFromTxt=null;
	private JButton btnFromDir=null;
	private JButton btnFromSpec=null;
	private int index=0;
	private int threadnum=20;
	
	public hotword()
	{
		super("lichao890427的热词搜索引擎");
		setTitle("lichao890427的热词搜索引擎");
		getContentPane().setLayout(null);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"\u5E8F\u53F7", "\u6587\u4EF6\u540D", "\u767E\u5EA6\u641C\u7D22\u70ED\u5EA6", "\u767E\u5EA6\u77E5\u9053\u641C\u7D22\u70ED\u5EA6"
			}
		) {
			Class[] columnTypes = new Class[] {
				Long.class, String.class, Long.class, Long.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(334);
		table.getColumnModel().getColumn(2).setPreferredWidth(103);
		table.getColumnModel().getColumn(3).setPreferredWidth(112);
		table.setAutoCreateRowSorter(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 42, 584, 720);
		getContentPane().add(scrollPane);
		
		btnFromTxt = new JButton("\u4ECEtxt\u6587\u4EF6\u5217\u8868\u52A0\u5165");
		btnFromTxt.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				btnFromTxt.setVisible(false);
				btnFromDir.setVisible(false);
				btnFromSpec.setVisible(false);
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int ret=fileChooser.showOpenDialog(fileChooser);
				if(ret == JFileChooser.APPROVE_OPTION)
				{
					readString(fileChooser.getSelectedFile().getAbsolutePath());
				}
				Search();
				btnFromTxt.setVisible(true);
				btnFromDir.setVisible(true);
				btnFromSpec.setVisible(true);
				index=words.size();
			}
		});

		btnFromTxt.setBounds(0, 0, 135, 23);
		getContentPane().add(btnFromTxt);
		
		btnFromDir = new JButton("\u4ECE\u78C1\u76D8\u76EE\u5F55\u52A0\u5165");
		btnFromDir.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				JFileChooser fileChooser=new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int ret=fileChooser.showOpenDialog(fileChooser);
				if(ret == JFileChooser.APPROVE_OPTION)
				{
					btnFromTxt.setVisible(false);
					btnFromDir.setVisible(false);
					btnFromSpec.setVisible(false);
					getFiles(fileChooser.getSelectedFile().getAbsolutePath());
					Search();
					btnFromTxt.setVisible(true);
					btnFromDir.setVisible(true);
					btnFromSpec.setVisible(true);
					index=words.size();
				}
			}
		});
		btnFromDir.setBounds(134, 0, 124, 23);
		getContentPane().add(btnFromDir);
		
		btnFromSpec = new JButton("\u6307\u5B9A\u641C\u7D22\u8BCD\u52A0\u5165");
		btnFromSpec.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				btnFromTxt.setVisible(false);
				btnFromDir.setVisible(false);
				btnFromSpec.setVisible(false);
				if(specWord.getText().length() > 4)
				{
					String temp1=nameClip(specWord.getText());
					if(temp1.length()>4)
						words.add(temp1);
				}
				Search();
				btnFromTxt.setVisible(true);
				btnFromDir.setVisible(true);
				btnFromSpec.setVisible(true);
				index=words.size();
			}
		});
		btnFromSpec.setBounds(0, 19, 135, 23);
		getContentPane().add(btnFromSpec);
		
		specWord = new JTextField();
		specWord.setBounds(134, 20, 450, 21);
		getContentPane().add(specWord);
		specWord.setColumns(10);
		
		JButton Clear = new JButton("\u6E05\u7A7A\u5217\u8868");
		Clear.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				words.clear();
				clearTable();
				index=0;
			}
		});
		Clear.setBounds(484, 0, 100, 23);
		getContentPane().add(Clear);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,800);
		setVisible(true);
	}
	
	private void readString(String filename)
	{
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(filename));
			String line=null;
			while((line=reader.readLine()) != null)
			{
				String temp=nameClip(line);
				if(temp.length() > 4)
					words.add(temp);
			}
			reader.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void getFiles(String path)
	{
		File[] files=new File(path).listFiles();
		for(File file:files)
		{
			if(file.isDirectory())
			{
				getFiles(file.getAbsolutePath());
			}
			else if(file.isFile())
			{
				String temp=nameClip(file.getName());
				if(temp.length() > 4)
					words.add(nameClip(temp));
			}
		}
	}
	
	private String nameClip(String obj)
	{
		int index=obj.lastIndexOf('.');
		String temp=obj;
		if(index != -1)
			temp= obj.substring(0, index);
		temp=temp.replaceAll("[()\\.:]"," ").replace("—"," ").replace("："," ");
		return temp;
	}
	
	private void Search()
	{
		DefaultTableModel tableModel=(DefaultTableModel)table.getModel();
		int num=words.size()/threadnum;
		for(int i=0;i<threadnum;i++)
		{
			new myThread(num*i,num*(i+1),tableModel).start();
		}
		new myThread(num*threadnum,words.size(),tableModel).start();
	}
	
	private class myThread extends Thread
	{
		private int begin=0;
		private int end=0;
		private DefaultTableModel model=null;
		
		public myThread(int begin,int end,DefaultTableModel model)
		{
			this.begin=begin;
			this.end=end;
			this.model=model;
		}
		
		@Override
		public void run() 
		{
			// TODO Auto-generated method stub
			try
			{
				for(int i=begin;i<end;i++)
				{
					Object[] arr=new Object[4];
					String temp = words.get(i);
					String temp1;
					arr[0]=i;
					arr[1]=words.get(i);
					temp1=getFromBaidu(temp);
					arr[2]=(!temp1.equals(""))?Integer.parseInt(temp1):0;
					temp1=getFromBaiduZhidao(temp);
					arr[3]=(!temp1.equals(""))?Integer.parseInt(temp1):0;
					model.addRow(arr);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private String getFromBaidu(String search)
	{
		try
		{
		     Document doc=Jsoup.connect("http://www.baidu.com/s?q1=&q2="+search+"&q3=&q4=&rn=10&lm=0&ct=0&ft=&q5=&q6=").timeout(0).get();
		     Elements items=doc.select(".nums");
		     return items.text().replaceAll("[^0123456789,]+","").replace(",","");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private String getFromBaiduZhidao(String search)
	{
		try
		{
		     Document doc=Jsoup.connect("http://zhidao.baidu.com/search?ct=0&pn=0&tn=ikaslist&rn=10&word="+search).timeout(0).get();
		     Elements items=doc.select(".pickerTitle");
		     return items.text().replaceAll("[^0123456789,]+","").replace(",","");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private void clearTable()
	{
		if(table != null)
		{
			table.setModel(new DefaultTableModel(new Object[][] {},new String[]
					{
						"\u6587\u4EF6\u540D", "\u767E\u5EA6\u641C\u7D22\u70ED\u5EA6","\u767E\u5EA6\u77E5\u9053\u641C\u7D22\u70ED\u5EA6"
					}
				));
		}
	}
	
	public static void main(String[] args)
	{
		new hotword();
	} 
}
