

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Class that attempts to connect to other Routers in the network
 */
public class Client extends Thread  {

	int p1,p2;
	String host1,host2;
	int c1, c2;
	String selfIP;

	public Client(String h1,String h2,int p1,int p2,int c1,int c2,String ip)
	{

		this.host1=h1;
		this.host2=h2;
		this.p1=p1;
		this.p2=p2;
		this.c1=c1;
		this.c2=c2;
		this.selfIP=ip;
	}

	public void run()
	{

		
		boolean flag=false;
		Socket connection1;
		Socket connection2;
		String subnet="255.255.255.0";
		ConcurrentHashMap<String, ArrayList<String>>table;
		while(true)
		{
			try {



				if(flag==false)//connecting to the first Router 
				{
					connection1=new Socket(host1,p1);

					table= new TableFile().getFile();

					String nPrefix=new TableFile().networkPrefix(host1);//retrieving the network prefix for the first router.
					ArrayList<String>values=table.get(nPrefix);
					if(values==null)//checking if the entry for this particular router already exists in the router.
					{
						//System.out.println("*****inserting in Client***********");
						String prefix=new TableFile().networkPrefix(host1);
						values=new ArrayList<>();
						values.add(subnet);
						values.add(host1);
						values.add(""+c1);
						table.put(prefix, values);//put the entry in the router table.
						new TableFile().printTable();
					}
					else
					{
						int cost=Integer.parseInt(values.get(2));//retrieve existing cost for this router entry.

						if(cost>c1)//if is more than the current link cost add the current link and link cost to the table.
						{//	System.out.println("client h1 not possi");
							String prefix=new TableFile().networkPrefix(host1);
							values=new ArrayList<>();
							values.add(subnet);
							values.add(host1);
							values.add(""+c1);
							table.put(prefix, values);
							new TableFile().printTable();

						}
					}
					ObjectOutputStream out=new ObjectOutputStream(connection1.getOutputStream());//connection to send the table to the corresponding Router.
					out.writeObject(table);
					out.flush();
					out.writeObject(new String(selfIP));
					out.flush();
					out.writeObject(new String(""+c1));
					flag=true;
					connection1.close();
					out.close();
				}

				if(!host2.equals("0"))//Connecting to the second router if a connection exists.
				{
					connection2=new Socket(host2,p2);
					//ObjectOutputStream out2=new ObjectOutputStream(connection2.getOutputStream());
					//out2.writeObject(new TableFile().getFile());
					//************************
					table= new TableFile().getFile();
					String nPrefix=new TableFile().networkPrefix(host2);//Retrieving network prefix for the second Router.
					ArrayList<String>values=table.get(nPrefix);//check if entry already exists for the second router in te current table.
					if(values==null)
					{
						//System.out.println("writing h2 in client");
						String prefix=new TableFile().networkPrefix(host2);
						values=new ArrayList<>();
						values.add(subnet);
						values.add(host2);
						values.add(""+c2);
						table.put(prefix, values);//put the value in the table if it did not exist.
						new TableFile().printTable();

					}
					else
					{
						//System.out.println("h2 not possible");
						int cost=Integer.parseInt(values.get(2));

						if(cost>c2)
						{
							String prefix=new TableFile().networkPrefix(host2);
							values=new ArrayList<>();
							values.add(subnet);
							values.add(host2);
							values.add(""+c2);
							table.put(prefix, values);
							new TableFile().printTable();

						}
					}
					ObjectOutputStream out2=new ObjectOutputStream(connection2.getOutputStream());
					out2.writeObject(table);
					out2.flush();
					out2.writeObject(selfIP);
					out2.flush();
					out2.writeObject(new String(""+c2));
					flag=false;
					connection2.close();
					out2.close();
					//*********************
				}
				else
					if(host2.equals("0"))
					{
						flag=false;
					}
				Thread.sleep(2500);

			} catch (InterruptedException e) {
				//System.out.println("interupted");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}

			} catch (UnknownHostException e) {
				//System.out.println("unknowHost");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}

			} catch (IOException e) {
				//System.out.println("IOexception");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
				}

			}
			
			
		}


	}
}
