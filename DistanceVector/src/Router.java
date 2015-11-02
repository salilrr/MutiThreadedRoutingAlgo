

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Class Router
 * Class that simulates the entire Router.
 * 
 * @author:Salil Rajadhyaksha 
 */
public class Router {

	
	public static void main(String[]args)
	{
		
		Scanner read=new Scanner(System.in);
		String subnet="255.255.255.0";
		
		System.out.println("Enter  the server's port number");
		int port=read.nextInt();
		read.nextLine();
		System.out.println("Enter Server's ip address");
		String ip=read.nextLine();
		
		System.out.println("Enter  the ip address of first Router to connect to.");
		String h1=read.nextLine();
		System.out.println("Enter port number of the first router");
		int p1=read.nextInt();
		read.nextLine();
		System.out.println("Enter link cost from current router to the first router");
		int cost1=read.nextInt();
		read.nextLine();
		
		System.out.println("Enter ip address of the second Router to connect to.Enter 0 if no connection");
		String h2=read.nextLine();
		System.out.println("Enter port number of the second router to connect to.Enter 0 if no connection");
		int p2=read.nextInt();
		read.nextLine();
		System.out.println("Enter link cost from current router to second router.Enter 0 if no connection.");
		int cost2=read.nextInt();
		read.nextLine();
		
		ConcurrentHashMap<String, ArrayList<String>>temp=new TableFile().getFile();//retrieve the file of the current Router 
		ArrayList<String>tempList=new ArrayList<>();
		
		//Inserting its own values.
		String prefix=new TableFile().networkPrefix(ip);
		tempList.add(subnet);
		tempList.add(ip);
		tempList.add("0");
		temp.put(prefix, tempList);
		new TableFile().printTable();
		Server s1=new Server(port,ip);//creating a new server thread to that listen's on the port for new connections.
		Client c1=new Client(h1,h2,p1,p2,cost1,cost2,ip);//client thread that attempts to connect to other Router's.
		s1.start();
		c1.start();
		read.close();
	}
	
	
}
