package com.mastercard.navigator.util;

	import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
	 
	public class RouteFinderUtility
	{
	    private Map<String, HashSet<String>> map = new HashMap<>();
	    
	    public RouteFinderUtility(Map<String, HashSet<String>> map) {
	    	this.map = map;
	    }
	    public RouteFinderUtility() {
	    }
	 
	    public void addEdge(String node1, String node2)
	    {
	        HashSet<String> adjacent = map.get(node1);
	        if (adjacent == null)
	        {
	            adjacent = new HashSet<>();
	            map.put(node1, adjacent);
	        }
	        adjacent.add(node2);
	    }
	 
	    public void addTwoWayVertex(String node1, String node2)
	    {
	        addEdge(node1, node2);
	        addEdge(node2, node1);
	    }
	 
	    public boolean isConnected(String node1, String node2)
	    {
	        Set<String> adjacent = map.get(node1);
	        if (adjacent == null)
	        {
	            return false;
	        }
	        return adjacent.contains(node2);
	    }
	 
	    public LinkedList<String> adjacentNodes(String last)
	    {
	        HashSet<String> adjacent = map.get(last);
	        if (adjacent == null)
	        {
	            return new LinkedList();
	        }
	        return new LinkedList<String>(adjacent);
	    }
	 
	    private static String  START;
	    private static String  END;
	    private static boolean flag;
	 
	    public static boolean isRouteExists(Map<String,String> args,String source,String destination,Map<String, HashSet<String>> map)
	    {
	    	RouteFinderUtility graph = new RouteFinderUtility();
	    	
	        // this graph is directional
			  args.entrySet().parallelStream()
			  .forEach(mapper -> graph.addEdge(mapper.getKey(), mapper.getValue()));
	        LinkedList<String> visited = new LinkedList<>();
	        System.out.println("Enter the source node:");
	        START = source;
	        System.out.println("Enter the destination node:");
	        END = destination;
	 
	        visited.add(START);
	        new RouteFinderUtility().breadthFirst(graph, visited);
	        
	    	//RouteFinderUtility graph1 = new RouteFinderUtility(map);
	    	
	    	//new RouteFinderUtility().breadthFirst(graph1, visited);
	    //	System.out.println(" graph1.flag "+ graph1.flag);
	    	System.out.println(" graph.flag "+ graph.flag);
	    	
	    	return  graph.flag;
	    }
	 
	    private void breadthFirst(RouteFinderUtility graph,
	            LinkedList<String> visited)
	    {
	        LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
	 
	        for (String node : nodes)
	        {
	            if (visited.contains(node))
	            {
	                continue;
	            }
	            if (node.equals(END))
	            {
	                visited.add(node);
	                printPath(visited);
	                flag = true;
	                visited.removeLast();
	                break;
	            }
	        }
	 
	        for (String node : nodes)
	        {
	            if (visited.contains(node) || node.equals(END))
	            {
	                continue;
	            }
	            visited.addLast(node);
	            breadthFirst(graph, visited);
	            visited.removeLast();
	        }
	        if (flag == false)
	        {
	            System.out.println("No path Exists between " + START + " and "
	                    + END);
	            flag = true;
	        }
	 
	    }
	 
	    private boolean printPath(LinkedList<String> visited)
	    {
	        if (flag == false)
	            System.out.println("Yes there exists a path between " + START
	                    + " and " + END);
	        System.out.println();
	        return flag;
	    }
	}