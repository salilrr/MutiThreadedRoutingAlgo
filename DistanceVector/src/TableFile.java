

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Class Table File
 * Class that acts as a singleton for Router table.
 * Class also includes method to print the table.
 *Also calculates the network prefix given the ip and subnet mask.
 */
public class TableFile {

	private static ConcurrentHashMap<String, ArrayList<String>>file;
	
	/*
	 * Method to return the Router table for the curent router.
	 *
	 *	@return file :the Router table for current machine.
	 */
	public ConcurrentHashMap<String, ArrayList<String>> getFile()
	{
		if(file==null)
			file=new ConcurrentHashMap<>();
		
		return file;
	}
	
	/*
	 * Method to print the current Routing table.
	 */
	public void printTable()
	{
		
		System.out.println("*******                Router Table         *****");
		System.out.println("Destination          Subnet Mask          Next Hop           Distance");
		ConcurrentHashMap<String, ArrayList<String>>table=getFile();
		
		Iterator<Entry<String, ArrayList<String>>>itr = table.entrySet().iterator();
		while (itr.hasNext())
		{

			Map.Entry<String,ArrayList<String>> pair = (Entry<String, ArrayList<String>>) itr.next();
			
			String destination=pair.getKey();
			ArrayList<String>t=pair.getValue();
			String subnet=t.get(0);
			String nextHop=t.get(1);
			String cost=t.get(2);
			
		System.out.println(destination+"          "+subnet+"          "+nextHop+"          "+cost);
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------");	

		
		
		}

	}
	
	/*
	 * 
	 * method that calculates the network prefix.
	 * @param ip:the ip address whose prefix is to be calculated.
	 * 
	 * @return prefix:The calculated network prefix.s
	 */
	public String networkPrefix(String ip)
	{
		String subnet="255.255.255.0";
		String ipAddress[]=ip.split("\\.");
		String subnetMask[]=subnet.split("\\.");
		String prefix="";
		int tempAndFlag=0;
		
		for(int i=0;i<ipAddress.length;i++)
		{
			int ipPart=Integer.parseInt(ipAddress[i]);
			int subnetPart=Integer.parseInt(subnetMask[i]);
			
			if(subnetPart==0)
				prefix=prefix+"0";
			else
			{
				tempAndFlag=ipPart&subnetPart;
				prefix=prefix+tempAndFlag+".";
			}
			
		}
		
	
		
		return prefix;
	}
	
	/*
	 * 
	 * method that calculates the network prefix.
	 * @param ip:the ip address whose prefix is to be calculated.
	 * @param subnet:The subnet mask to be used.
	 * 
	 * @return prefix:The calculated network prefix.s
	 */
	public String networkPrefix(String ip,String subnet)
	{
		
		
		String ipAddress[]=ip.split("\\.");
		String subnetMask[]=subnet.split("\\.");
		String prefix="";
		int tempAndFlag=0;
		
		for(int i=0;i<ipAddress.length;i++)
		{
			int ipPart=Integer.parseInt(ipAddress[i]);
			int subnetPart=Integer.parseInt(subnetMask[i]);
			
			if(subnetPart==0)
				prefix=prefix+"0";
			else
			{
				tempAndFlag=ipPart&subnetPart;
				prefix=prefix+tempAndFlag+".";
			}
			
		}
		
	
		
		return prefix;
	}
}
