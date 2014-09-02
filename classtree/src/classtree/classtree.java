package classtree;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;
 
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
 
public class classtree extends JFrame
{
  /**
  *
  */
  private static final long serialVersionUID = 1L;
  private JTextField toSearch=null;
  private JList<String> result=null;
  private JTree tree=null;
  public HashTreeNode root=null;
  public HashTreeNode exception=null;
  private static Queue<String> myqueue=null;
  private String folderpath="C:/Users/Administrator/Desktop/rt";
  private boolean datachanged=false;//增加了文件目录时
  private String tofind=null;
  private LinkedList<Integer> select=null;
  private LinkedList<String> excluded=null;
  JScrollPane scrollPane=null;
  DefaultMutableTreeNode uiroot=null;
  
  public class HashTreeNode
  {
    public HashTreeNode(String name)
    {
       nodename=name;
       children=new LinkedList<HashTreeNode>();
    }
    
    public synchronized HashTreeNode AddChild(String name)
    {
       HashTreeNode newnode=null;
       for(int i=0;i<children.size();i++)
       {
         if(name.equals(children.get(i).nodename))
         {
           return children.get(i);
         }
         else if(name.compareTo(children.get(i).nodename) < 0)//name < node
         {
           newnode=new HashTreeNode(name);
           children.add(i,newnode);
           return newnode;
         }
       }
       newnode=new HashTreeNode(name);
       children.add(newnode);
       return newnode;
    }
    
    public String nodename;
    public LinkedList<HashTreeNode> children;
  }
  
  public synchronized void AddNodeByString(String[] allpath)
  {
    HashTreeNode node=root;
    for(int i=1;i<allpath.length;i++)
    {
       node=node.AddChild(allpath[i].trim());
    }
  }
  
  public synchronized LinkedList<HashTreeNode> AddOneNode(String classpath)//加入节点
  {//递归找到父节点
    LinkedList<HashTreeNode> result=new LinkedList<HashTreeNode>();
    
    try
    {
//                         System.out.println(classpath);
 
       for(String exc:excluded)
       {
         if(classpath.contains(exc))
         {
           result.add(exception.AddChild(classpath));
           return result;  
         }
       }
 
       //接口看做父类
       java.lang.reflect.Type curtype=Class.forName(classpath).getGenericSuperclass();
       LinkedList<java.lang.reflect.Type> type=new LinkedList<java.lang.reflect.Type>();
       if(curtype != null)
         type.add(curtype);
       else
         result.add(root.AddChild(classpath));
       for(java.lang.reflect.Type curtype1:Class.forName(classpath).getGenericInterfaces())
       {
         type.add(curtype1);
       }
 
       for(java.lang.reflect.Type curtype2:type)
       {
         String classstr=curtype2.toString();
         if(classstr.contains("interface") || classstr.contains("class"))
           classstr=classstr.substring(classstr.indexOf(' ')+1);
         if(classstr.indexOf('<') > -1)
           classstr=classstr.substring(0,classstr.indexOf('<'));
         for(HashTreeNode node1:AddOneNode(classstr))
         {
           result.add(node1.AddChild(classpath));
         }
       }
       return result;  
    }
    catch(Exception e)
    {
       System.out.println("error:"+classpath);
       result.add(exception.AddChild(classpath));
       return result;
    }
  }
  
  public static void FindClassInfo(String dir,String classpath)
  {
    try
    {
       File file=new File(dir);
       if(file.isFile())
       {
         if(classpath.indexOf('$') < 0 && classpath.indexOf(".class") > -1)
         {
           String obj=classpath.replace(".class","");
           myqueue.add(obj);
           System.out.println(obj);
         }
       }
       else if(file.isDirectory())
       {
         for(String dirstr:file.list())
         {
           String newclasspath=classpath.equals("")?dirstr:(classpath+"."+dirstr);
           FindClassInfo(dir+"/"+dirstr,newclasspath);
         }
       }
    }
    catch(Exception e)
    {
       System.out.println("error");
    }
  }
  
  public void FindAllMatchClass(HashTreeNode node)
  {
    try
    {
       String dest=tofind.substring(tofind.lastIndexOf('.')+1);
       if(node.nodename.contains(dest))
       {
         ((DefaultListModel<String>)result.getModel()).addElement(node.nodename);
       }
       for(HashTreeNode cur:node.children)
       {
         FindAllMatchClass(cur);
       }
    }
    catch(Exception e)
    {
       System.out.println("error");
    }
  }
  
  public void AddUItree(HashTreeNode curnode,DefaultMutableTreeNode uinode)
  {
    try
    {
       for(int i=0;i<curnode.children.size();i++)
       {
         HashTreeNode cur=curnode.children.get(i);
         DefaultMutableTreeNode newnode=new DefaultMutableTreeNode(cur.nodename);
         uinode.add(newnode);
         AddUItree(cur,newnode);
       }
    }
    catch(Exception e)
    {
       System.out.println("error");
    }
  }
  
  public classtree()
  {
    super("");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);
    
    toSearch = new JTextField();
    toSearch.setBounds(107, 630, 247, 21);
    getContentPane().add(toSearch);
    toSearch.setColumns(10);
    
    JLabel label = new JLabel("\u8981\u641C\u7D22\u7684\u76EE\u6807\uFF1A");
    label.setBounds(10, 633, 87, 15);
    getContentPane().add(label);
    
    JButton search = new JButton("\u641C");
    search.setBounds(364, 629, 100, 23);
    getContentPane().add(search);
    
    scrollPane = new JScrollPane();
    scrollPane.setBounds(0, 0, 584, 496);
    getContentPane().add(scrollPane);
    
    tree = new JTree();
    tree.setScrollsOnExpand(true);
    tree.setFocusable(true);
    tree.setAutoscrolls(true);
    tree.setModel(new DefaultTreeModel(uiroot=new DefaultMutableTreeNode("JAVAHOME")));
    scrollPane.setViewportView(tree);
    
    JScrollPane scrollPane_1 = new JScrollPane();
    scrollPane_1.setBounds(0, 497, 584, 123);
    getContentPane().add(scrollPane_1);
    
    result = new JList<String>();
    scrollPane_1.setViewportView(result);
    
    JButton outputfile = new JButton("\u751F\u6210\u6587\u4EF6");
    outputfile.setBounds(474, 629, 100, 23);
    getContentPane().add(outputfile);
    
    setSize(600,700);
    setVisible(true);
    
    root=new HashTreeNode("根节点");
    exception=root.AddChild("异常");
    myqueue=new LinkedList<String>();
    select=new LinkedList<Integer>();
    excluded=new LinkedList<String>();
    excluded.add("com.sun.management.OperatingSystem");
    excluded.add("com.sun.org.apache.xml.internal.serialize.HTMLdtd");
    excluded.add("sun.awt.windows.WBufferStrategy");
    excluded.add("sun.font.FreetypeFontScaler");
    excluded.add("sun.java2d.cmm.lcms.LCMS");
    excluded.add("sun.jdbc.odbc.JdbcOdbcPlatform");
    excluded.add("sun.org.mozilla.javascript.internal.SecureCaller");
    excluded.add("sun.plugin.extension.ExtensionUtils");
    excluded.add("sun.plugin2.main.client.WDonatePrivilege");
    excluded.add("sun.plugin2.main.server.IExplorerPlugin");
    excluded.add("sun.plugin2.main.server.MozillaPlugin");
    excluded.add("sun.plugin2.os.windows.Windows");
    excluded.add("sun.plugin2.main.server.ProxySupport");
    excluded.add("sun.reflect.misc.Trampoline");
    excluded.add("sun.security.krb5.SCDynamicStoreConfig");
    excluded.add("oracle.jrockit.jfr.Process");
    excluded.add("oracle.jrockit.jfr.Timing");
    excluded.add("com.sun.deploy.uitoolkit.impl.awt.AWTClientPrintHelper");
    excluded.add("com.sun.glass.ui.mac");
    excluded.add("com.sun.glass.ui.x11.X11Timer");
    excluded.add("com.sun.javafx.logging.LoggingSupport");
    
    try
    {
       if(new File("config.txt").exists())
       {
         FileReader fr=new FileReader("config.txt");
         BufferedReader br=new BufferedReader(fr);
         FileInputStream fis=new FileInputStream("config.txt");
         String line="";
         while((line=br.readLine()) != null)
         {
//                                           System.out.println(line);
           AddNodeByString(line.substring(1,line.length()-1).split(","));
         }                
         br.close();
         fr.close();
       }
    }
    catch(Exception e)
    {
       e.printStackTrace();
    }
    
    result.addListSelectionListener(new ListSelectionListener()
    {
       public void valueChanged(ListSelectionEvent e)
       {
         for(int i=0;i < tree.getRowCount();i++)
         {
           tree.expandRow(i);
         }
         int i=select.get(result.getSelectedIndex());
         tree.setSelectionRow(i);
         Rectangle rect=tree.getRowBounds(i);
         tree.scrollRectToVisible(rect);
       }
    });
    
    search.addMouseListener(new MouseAdapter()
    {
       Thread searchthread=null;
       @Override
       public void mouseClicked(MouseEvent e)
       {
         searchthread=new Thread()
         {//开始枚举文件线程
           @Override
           public void run()
           {
              try
              {
                result.setModel(new DefaultListModel<String>());
                tofind=toSearch.getText();
                for(int i=0;i<tree.getRowCount();i++)
                {
                  tree.expandRow(i);
                }
                select=new LinkedList<Integer>();
                for(int i=0;i<tree.getRowCount();i++)
                {
                  String obj=tree.getPathForRow(i).toString();
                  if(obj.contains(tofind))
                  {
                     ((DefaultListModel<String>)result.getModel()).addElement(obj);
                     select.add(i);
                  }
                }
              }
              catch(Exception e)
              {
                e.printStackTrace();
              }
           }
         };
         
         try
         {
           searchthread.start();
           searchthread.join();                                    
         }
         catch(Exception exc)
         {
           exc.printStackTrace();
         }
       }
    });
    
    final Thread filethread=new Thread()
    {//开始枚举文件线程
       @Override
       public void run()
       {
         FindClassInfo(folderpath,"");
         datachanged=true;
       }
    };
    filethread.start();
    
    new Thread()
    {//开始处理队列线程
       @Override
       public void run()
       {
         try
         {
           filethread.join();
           while(true)
           {
              if(myqueue.isEmpty())
              {
                Thread.sleep(500);
                if(datachanged)
                {
                  uiroot=new DefaultMutableTreeNode("JAVAHOME");
                  tree.setModel(new DefaultTreeModel(uiroot));
                  AddUItree(root,uiroot);
                  datachanged=false;
                }
                else
                {
                  for(int i=0;i<tree.getRowCount();i++)
                  {
                     tree.expandRow(i);
                  }
                }
              }
              else
              {
                if(filethread.isAlive())
                  filethread.join();
                AddOneNode(myqueue.poll());
              }
           }
         }
         catch(Exception e)
         {
           e.printStackTrace();
         }       
       }
    }.start();
    
    outputfile.addMouseListener(new MouseAdapter()
    {
       @Override
       public void mouseClicked(MouseEvent e)
       {
         OutputFile();
       }
    });
    
    addWindowListener(new WindowAdapter()
    {//关闭时保存数据到文件
       @Override
       public void windowClosing(WindowEvent e)
       {
         OutputFile();
       }
    });
  }
  
  public void OutputFile()
  {
    try
    {                                   
       for(int i=0;i < tree.getRowCount();i++)
       {
         tree.expandRow(i);
       }
       FileOutputStream fos=new FileOutputStream("config.txt");
       for(int i=0;i<tree.getRowCount();i++)
       {
         String obj=tree.getPathForRow(i).toString()+"\n";
         fos.write(obj.getBytes());
       }
       fos.close();
    }
    catch(Exception e)
    {
       e.printStackTrace();
    }
  }
  
  public static void main(String[] args) throws FileNotFoundException
  {
    //System.setOut(new PrintStream("abcd.txt"));
    new classtree();
  }
}