

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public class ServerConnection extends Thread{

	Socket connection;

	public ServerConnection(Socket con,int port)
	{
		//System.out.println("new connection for port"+port);
		this.connection=con;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void run() {

		ObjectInputStream objIn;
		ConcurrentHashMap<String, ArrayList<String>>otherRouterTable;
		ConcurrentHashMap<String, ArrayList<String>>thisRouterTable;
		
		try {
			objIn = new ObjectInputStream(connection.getInputStream());
			otherRouterTable=(ConcurrentHashMap<String,ArrayList<String>>) objIn.readObject();//recieve entry from other router.
			String senderIP=(String) objIn.readObject();
			String costToSender=(String)objIn.readObject();
		
			thisRouterTable=new TableFile().getFile();
			
			String senderPrefix=new TableFile().networkPrefix(senderIP);
			ArrayList<String>senderEntry=thisRouterTable.get(senderPrefix);
			
			if(senderEntry==null)//check if entry exists for the connection.IF not put the entry into the Routing table.
			{
				senderEntry=new ArrayList<>();
				senderEntry.add("255.255.255.0");
				senderEntry.add(senderIP);
				senderEntry.add(costToSender);
				//System.out.println("1.cost"+costToSender);
				thisRouterTable.put(senderPrefix, senderEntry);
				new TableFile().printTable();
			}
			else
			{
				int cost=Integer.parseInt(senderEntry.get(2));
				int c2=Integer.parseInt(costToSender);
				if(cost>c2)
				{
					senderEntry=new ArrayList<>();
					senderEntry.add("255.255.255.0");
					senderEntry.add(senderIP);
					senderEntry.add(costToSender);
					//System.out.println("2.Cost"+costToSender);
					thisRouterTable.put(senderPrefix, senderEntry);
					new TableFile().printTable();
				}
			}

			//int otherDist,thisDist;
			Iterator itr = otherRouterTable.entrySet().iterator();

			//Iterating over the Routing table recieved.
			while (itr.hasNext())
			{

				Map.Entry<String,ArrayList<String>> pair = (Entry<String, ArrayList<String>>) itr.next();
				
				String key=pair.getKey();
				if(key.equals(senderPrefix))
					continue;
				
				
				ArrayList<String>otherList=pair.getValue();
				
				
				ArrayList<String>thisList=thisRouterTable.get(key);
				
				if(thisList==null)//if the entry was not found in the current routing table.
				{
					thisList=new ArrayList<>();
					thisList.add(otherList.get(0));
					thisList.add(senderIP);
					
					Integer totalCost=Integer.parseInt(costToSender)+Integer.parseInt(otherList.get(2));
					thisList.add(totalCost.toString());
				//	System.out.println("3.Cost"+totalCost);
					
					thisRouterTable.put(key, thisList);
					new TableFile().printTable();
					continue;
					
				}
				else
				{
					int thisListCost=Integer.parseInt(thisList.get(2));
					
					int otherListCost=Integer.parseInt(otherList.get(2))+Integer.parseInt(costToSender);
					
					if(thisListCost>otherListCost)//if the cost in the table is greater than the current possible path.s
					{
						thisList=new ArrayList<>();
						thisList.add(otherList.get(0));
						thisList.add(senderIP);
						
						Integer totalCost=Integer.parseInt(costToSender)+Integer.parseInt(otherList.get(2));
						thisList.add(totalCost.toString());
					//	System.out.println("4.Cost"+totalCost);
						
						thisRouterTable.put(key, thisList);
						new TableFile().printTable();
						continue;
					}
				}
				

		}
			new TableFile().printTable();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}

}
