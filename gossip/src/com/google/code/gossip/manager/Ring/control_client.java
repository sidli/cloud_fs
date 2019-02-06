/**
 * control_client.java
 *
 * DHT1: Cassandra and Swift, DHT ring
 * DHT2: Ceph,rush and crush
 * DHT3: Elastic DHT: Similar to Redis 
 *
 *  (c) 2018 Li Jincheng
 */
package com.google.code.gossip.manager.Ring;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.io.Console;
import java.util.Vector;

import java.lang.String;
import java.io.File;
import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import com.google.code.gossip.manager.Ring.Hashing;
import com.google.code.gossip.manager.Ring.Command;

interface dht_table
{
    /*
    * Physical node insertion and removal
    * Virtual node insertion and removal
    * Load balance request (see load balance section)
    * Report: Access the data structures and print them to show the correctness of the implementation
     */
	public void initialize();
    public void insert_physical_node();
    public void remove_physical_node(int node);
    public Vector query_physical_node();
    public void insert_virtual_node();
    public void remove_virtual_node(int node);
    public Vector query_virtual_node();
    public Vector query_loadbalance();
    public void do_loadbalance();
    public String report_datastructure();
}

class DHT_example implements dht_table
{
    /*
    * Physical node insertion and removal
    * Virtual node insertion and removal
    * Load balance request (see load balance section)
    * Report: Access the data structures and print them to show the correctness of the implementation
     */
	public void initialize() {
		System.out.println("Starting send request dht initialized");
	}
    public void insert_physical_node()
    {
        System.out.println("Starting send request insert_physical_node");
    }
    public void remove_physical_node(int node)
    {
        System.out.println("Starting send request remove_physical_node");
    }
    public Vector query_physical_node()
    {
        System.out.println("Starting send request query_physical_node");
        return new Vector();
    }
    public void insert_virtual_node()
    {
        System.out.println("Starting send request insert_virtual_node");
    }
    public void remove_virtual_node(int node)
    {
        System.out.println("Starting send request remove_virtual_node");
    }
    public Vector query_virtual_node()
    {
        System.out.println("Starting send request query_virtual_node");
        return new Vector();
    }
    public Vector query_loadbalance()
    {
        System.out.println("Starting send request query_loadbalance");
        return new Vector();
    }
    public void do_loadbalance()
    {
        System.out.println("Starting send request do_loadbalance");
    }
    public String report_datastructure()
    {
        System.out.println("Starting send request report_datastructure");
        return "report haha";
    }
}

class DHT_Ring implements dht_table
{
    /*
    * Physical node insertion and removal
    * Virtual node insertion and removal
    * Load balance request (see load balance section)
    * Report: Access the data structures and print them to show the correctness of the implementation
     */
//	PhysicalNode ring;
	
	public void initialize() {
//		ring = new PhysicalNode();
		System.out.println("dht ring initialized");
	}
    public void insert_physical_node()
    {
    	
        System.out.println("Starting send request insert_physical_node");
    }
    public void remove_physical_node(int node)
    {
        System.out.println("Starting send request remove_physical_node");
    }
    public Vector query_physical_node()
    {
        System.out.println("Starting send request query_physical_node");
        return new Vector();
    }
    public void insert_virtual_node()
    {
        System.out.println("Starting send request insert_virtual_node");
    }
    public void remove_virtual_node(int node)
    {
        System.out.println("Starting send request remove_virtual_node");
    }
    public Vector query_virtual_node()
    {
        System.out.println("Starting send request query_virtual_node");
        return new Vector();
    }
    public Vector query_loadbalance()
    {
        System.out.println("Starting send request query_loadbalance");
        return new Vector();
    }
    public void do_loadbalance()
    {
        System.out.println("Starting send request do_loadbalance");
    }
    public String report_datastructure()
    {
        System.out.println("Starting send request report_datastructure");
        return "report haha";
    }
}


public class control_client {
    //Table initialization
	
    PrintWriter output;
    BufferedReader input;
    InputStream inputStream;
    OutputStream outputStream;
	SocketAddress socketAddress;
	Socket socket;
	
    public static dht_table initialize_DHT1()
    {
        //initialize_JSONRouter();
        return new DHT_Ring();
    }
    public static dht_table initialize_DHT2()
    {
        //initialize_JSONRouter();
        return new DHT_example();
    }
    public static dht_table initialize_DHT3()
    {
        //initialize_JSONRouter();
        return new DHT_example();
    }
    
    public boolean connectServer(String serverAddress, int port) {
//    	control_client client = new control_client();
    	int timeout = 2000;
		try {
			socketAddress = new InetSocketAddress(serverAddress, port);
			socket = new Socket();
			socket.connect(socketAddress, timeout);
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			output = new PrintWriter(outputStream, true);
			input = new BufferedReader(new InputStreamReader(inputStream));
//	        out = new PrintWriter(socket.getOutputStream(), true);
//	        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			String serverport = input.readLine();
	        
	        System.out.println("Connected to server " + serverAddress + ":" + port + ", with local port " + socket.getLocalPort());
//			socket.close();
			return true;
 
		} catch (SocketTimeoutException exception) {
//			socket.close();
			System.out.println("SocketTimeoutException " + serverAddress + ":" + port + ". " + exception.getMessage());
			return false;
		} catch (IOException exception) {
//			socket.close();
			System.out.println(
					"IOException - Unable to connect to " + serverAddress + ":" + port + ". " + exception.getMessage());
			return false;
		}
    }
    
    public void sendCommandStr(Command command, BufferedReader input, PrintWriter output) throws Exception {
    	String[] jsonCommands = {"read", "write", "data", "dht", "info", "writebatch", "updatebatch"};
    	for(String jsonCommand: jsonCommands) {
    		if (command.getAction().startsWith(jsonCommand)) {
    			sendCommandStr_JsonRes(command, input, output);
    			return;
    		}
    	}

    	System.out.println("Sending command" + " ---- " + command.getRawCommand() + " ---- " + new Date().toString());
            output.println(command.getRawCommand());
            output.flush();
        String response = input.readLine();
        System.out.println("Response received: " + response + " ---- " + new Date().toString());
    }
    
    public void sendCommandStr_JsonRes(Command command, BufferedReader input, PrintWriter output) throws Exception {
    	
    	String timeStamp = new Date().toString();
    	System.out.println("Sending command" + " ---- " + command.getRawCommand() + " ---- " + timeStamp);
        output.println(command.getRawCommand());
        output.flush();
        
        JsonObject res = parseRequest(input);
        if (res != null) {
            System.out.println();
        	System.out.println("Response received at " + timeStamp + " ---- " + res.toString());
        	parseResponse(res, command);
        	System.out.println();
         }
    }
    
    public static JsonObject parseRequest(BufferedReader br) throws Exception {
        String str;
        JsonObject jsonObject = null;

        while ((str = br.readLine()) != null) {
            JsonReader jsonReader = Json.createReader(new StringReader(str));
            jsonObject = jsonReader.readObject();
            return jsonObject;
        }
        return jsonObject;
    }
    
    public void parseResponse(JsonObject res, Command command) {
    	if (command.getAction().equals("info")) {
    		String epoch = res.getJsonObject("jsonResult").get("epoch").toString();
    		System.out.println("DHT table info");
    		System.out.println("Epoch number: " + epoch);
    		JsonArray tableResult = res.getJsonObject("jsonResult").get("table").asJsonArray();
    		for(int i = 0; i < tableResult.size(); i++) {
    			System.out.println(tableResult.get(i));
    		}
    		return;
    	}
    	
    	if (res.containsKey("status")) {
    		if (res.containsKey("message")) {
    			System.out.println(res.getString("message"));
    		}
    		if (res.containsKey("result")) {
    			System.out.println(res.getString("result"));
    		}
    		if (res.containsKey("jsonResult")) {
    			System.out.println(res.getJsonObject("jsonResult").toString());
    		}
    	}
    	else {
    		System.out.println(res.toString());
    	}
    }
    
    public void processCommandRush(String cmd) throws Exception {
    	Command command = new Command(cmd);
    	
    	String timeStamp = new Date().toString();
    	System.out.println("Sending command" + " ---- " + timeStamp);
    	System.out.println();
        
        JsonObject params = null;
        JsonObject jobj = null;
		if(command.getAction().equals("addnode")) {
			  params = Json.createObjectBuilder()
			  .add("subClusterId", command.getCommandSeries().get(0))
			  .add("ip", command.getCommandSeries().get(1))
			  .add("port", command.getCommandSeries().get(2))
			  .add("weight", command.getCommandSeries().get(3))
			  .build();
			
			  jobj = Json.createObjectBuilder()
			  .add("method", "addnode")
			  .add("parameters", params)
			  .build();
		}
		else if(command.getAction().equals("deletenode")) {
	          params = Json.createObjectBuilder()
	          .add("subClusterId", command.getCommandSeries().get(0))
	          .add("ip", command.getCommandSeries().get(1))
	          .add("port", command.getCommandSeries().get(2))
	          .build();
	
	          jobj = Json.createObjectBuilder()
	          .add("method", "deletenode")
	          .add("parameters", params)
	          .build();
		}
		else if(command.getAction().equals("getnodes")) {
            params = Json.createObjectBuilder()
                    .add("pgid", command.getCommandSeries().get(0))
                    .build();

            jobj = Json.createObjectBuilder()
                    .add("method", "getnodes")
                    .add("parameters", params)
                    .build();
		}
		else if (command.getAction().equals("help")) {
			System.out.println(getHelpText(2));
			return;
		}
		else {
			System.out.println("command not supported");
			return;
		}
    	
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonWriter writer = Json.createWriter(baos);
        writer.writeObject(jobj);
        writer.close();
        baos.writeTo(outputStream);

        outputStream.write("\n".getBytes());
        outputStream.flush();

        JsonObject res = parseRequest(input);
        if (res != null) {
            System.out.println();
        	System.out.println("Response received at " + timeStamp + " ---- " + res.toString());
            if (res.containsKey("status") && res.containsKey("message")) {
                System.out.println("REPONSE STATUS: " + res.getString("status") + ", " + "message: " + res.getString("message"));
            }
            System.out.println();
         }
    }
    
    public void processCommandRing(String cmd, int dhtType) throws Exception {
        Command command = new Command(cmd);
        if(command.getAction().equals("help"))
        {
            System.out.println(getHelpText(dhtType));
        }
        else if(command.getAction().equals("exit"))
        {
            System.exit(0);
        }
        else
        {
        	sendCommandStr(command, input, output);
        }
    }
    
    public void processCommandRing(String cmd) throws Exception {
    	processCommandRing(cmd, 1);
    }
    
    public void processCommandElastic(String cmd) throws Exception {
    	processCommandRing(cmd, 3);
    }
    
    public void processCommand(int typeDHT, String cmd) throws Exception {
    	switch(typeDHT) {
	    	case 1:
	    		processCommandRing(cmd);
	    		break;
	    	case 2:
	    		processCommandRush(cmd);
	    		break;
	    	case 3:
	    		processCommandElastic(cmd);
	    		break;
	    	default:
	    		System.out.println("no DHT type matched for the command");
	    		break;
    	}
    }
    
    public static String getHelpText(int dhtType) {
    	String tip = "";
    	switch(dhtType) {
	    	case 1:
	    		tip = "\nhelp";
	    		tip += "\nadd <IP> <Port>\nadd <IP> <Port> <hash>\nremove <hash>\nloadbalance <delta> <hash>";
	    		tip += "\ninfo";
	    		tip += "\nexit\n";
	    		break;
	    	case 2:
	    		tip = "\nhelp";
	    		tip += "\naddnode <subClusterId> <IP> <Port> <weight> | example: addnode S0 localhost 689 0.5\ndeletenode <subClusterId> <IP> <Port> | example: deletenode S0 localhost 689\ngetnodes <pgid> | example: getnodes PG1\nhelp\n";
	    		tip += "\nexit\n";
	    		break;
	    	case 3:
	    		tip = "\nhelp";
	    		tip += "\nadd <IP> <Port>\nadd <IP> <Port> <start> <end>\nremove <IP> <Port>\nloadbalance <fromIP> <fromPort> <toIP> <toPort> <numOfBuckets>\ninfo/help/exit/read file\n";
	    		tip += "\nexit\n";
	    		break;
    	}
    	
    	return tip;
    }
    
    public static void main (String args[]) throws Exception{
    	control_client client = new control_client();

    	String serverAddress = "localhost";
    	int port = 9090; 
    	
        System.out.println("==== Welcome to Control Client !!! =====");
        System.out.println("==== There are three types of DHT solutions, please select one(1,2,3) =====\n");
        System.out.println("DHT1: Cassandra and Swift, DHT ring");
        System.out.println("DHT2: Ceph,rush and crush ");
        System.out.println("DHT3: Elastic DHT: Similar to Redis\n");
        Console console = System.console();
        String dht = console.readLine("Please select from: 1 , 2 or 3:");
        String dhtName = "";
        if(dht.equals("1"))
        {
            port = 9091;
            dhtName = "Ring";
        }
        if(dht.equals("2"))
        {
            port = 8100;
            dhtName = "Rush";
        }
        if(dht.equals("3"))
        {
            port = 9093;
            dhtName = "Elastic DHT";
        }
        
        int dhtType = Integer.valueOf(dht);
        
		boolean connected = client.connectServer(serverAddress, port);
		
		if (connected) {
			System.out.println("Connected to " + dhtName + " Server ");
		}
		else {
			System.out.println("Unable to connect to server!");
			return;
		}
        
//        Vector<String> cmds = new Vector<String>();
        while(true)
        {
//            if(cmds.isEmpty() == true)
//            {
//                String cmd = console.readLine("Input your command:");
//                cmds.addElement(cmd);
//            }
//            String cmd = cmds.remove(0);
        	String cmd = console.readLine("Input your command:");
            
            client.processCommand(dhtType, cmd);
        }
    }
}
