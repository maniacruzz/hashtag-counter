/////////////////////////////////////////////////////////////////////
// Hashtagcounter (Implemented using Max FibonacciHeap)            //
// Created by: Amogh Rao                                           //
// UFID: 13118639                                                  //
// Course : Advance Data Structures                                //
// University of Florida                                           //
/////////////////////////////////////////////////////////////////////

import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;


/* List of Functions
removeMax()
insert(val)
pairwiseCombine()
combine()
increaseKey()
cascadecut()
cut()
FindMax()
printMax()
*/

//Node Structure
class FibHeapNode{
	FibHeapNode left,right,parent,child;
	String key;
	int val;
	int degree=0;
	boolean childcut = false;
	public FibHeapNode(int val,String key)
	{
		this.right = this;
		this.left = this;
		this.val = val;
		this.key = key;
		this.child = null;
	}
	
}

//Heap class
class FibonacciHeap {
	 private FibHeapNode max; //max pointer
	public FibonacciHeap()
	{
		max=null;
	}
	
	public void printMax()
	{
		System.out.println(max.key+"-"+max.val);
	}
	
	//inserts a value and key in the root list by creating a node 
	public FibHeapNode insert(int val,String key)
	{
		FibHeapNode node = new FibHeapNode(val,key);
		node.parent=null;
		node.childcut=false;

		if ( max != null )
		{
			node.right = max;
			node.left = max.left;
			max.left.right=node;
			max.left = node;
			if (max.right==max)
			{
				max.right=node;
			}
			//update max pointer if needed
			if (node.val > max.val)
			{
				max = node;
			}
		}
		else
		{
			max = node;
		}
		return node;
	}
	
	//directly inserts the node provided in the root list
	public void insert(FibHeapNode node)
	{
		node.parent=null;
		node.childcut=false;
		node.right=node;
		node.left=node;
		if ( max != null )
		{
			node.right = max;
			node.left = max.left;
			max.left.right=node;
			max.left = node;

			if (max.right==max)
			{
				max.right=node;
			}
			//update max pointer if needed
			if (node.val > max.val)
			{
				max = node;
			}
		}
		else
		{
			max = node;
		}
	}
	
	//combines 2 nodes of same degree
	public FibHeapNode combine(FibHeapNode node1,FibHeapNode node2)
	{
		FibHeapNode node;
		
		if (node1.val>=node2.val)
		{
			node2.parent = node1;
			node2.left.right=node2.right;
			if (node2.right!=null)
			{
				node2.right.left=node2.left;
			}
			
			//update the pointers for the child list
			if (node1.degree==0 && node2.degree==0)
			{
				node1.child = node2;
				node2.left=node2;
				node2.right=node2;
			}
			else
			{
				node2.left = node1.child.left;
				node2.right =node1.child;
				node1.child.left.right=node2;
				node1.child.left = node2;
				if (node1.child.right==node1.child)
				{
					node1.child.right=node2;
				}
			}
			//increase the degree of the node
			node1.degree+=1;

			node= node1;
		}
		else 
		{
			node1.parent = node2;
			node1.left.right=node1.right;
			if (node1.right!=null)
			{
				node1.right.left=node1.left;
			}
			//update the pointers for the child list
			if (node1.degree==0 && node2.degree==0)
			{
				node2.child = node1;
				node1.left=node1;
				node1.right=node1;
			}
			else
			{
				node1.left = node2.child.left;
				node1.right =node2.child;
				node2.child.left.right=node1;
				node2.child.left = node1;
				
				if (node2.child.right==node2.child)
				{
					node2.child.right=node1;
				}
			}
			//increase the degree of the node
			node2.degree+=1;

			node= node2;
		}
		return node;
	}
	
	//detach a node from the heap and reinserts to the root list
	public void cut(FibHeapNode parent,FibHeapNode child)
	{
		if (parent.degree==1)
		{
			parent.child=null;
		}
		else 
		{	if (parent.child==child)
			{
				parent.child=child.right;
			}
			child.left.right=child.right;
			child.right.left=child.left;
		}
		parent.degree-=1;
		insert(child);
	}
	
	//checks for childcut and decides if to perform cut or not. Also updates childcut value
	public void cascadeCut(FibHeapNode node)
	{
		FibHeapNode parent= node.parent;
		if (parent!=null)
		{
			if(node.childcut==false)
			{
				node.childcut=true;
			}
			else
			{
				cut(parent, node);
				cascadeCut(parent);
			}
		}
	}

	//updates the value of a node by k and checks if cut and cascadecut is required
	public void increaseKey(FibHeapNode node, int k)
	{
		node.val+=k;
		FibHeapNode parent = node.parent;
		if (parent!=null && node.val>parent.val)
		{
			cut(parent, node);
			cascadeCut(parent);
		}
		//update the max pointer if needed
		if (node.val>max.val)
		{
			max=node;
		}
	}

	//combines nodes of same degree such that no 2 nodes of same degree exist in the root list
	public void pairwiseCombine()
	{
		//hashtable used to store the degree entries
		Hashtable<Integer, FibHeapNode> degreetable
	     = new Hashtable<Integer, FibHeapNode>();
		boolean flag=false;
		
		//convert circular list to doubly linked list
		max.left.right=null;
		max.left=null;
		FibHeapNode current=max;
		FibHeapNode prev=null;
		while(current != null)
		{	
			flag=false;
			FibHeapNode next=current.right;
			while (flag==false)
			{
				if (degreetable.containsKey(current.degree)==true)
				{	
					if (current != degreetable.get(current.degree))
					{
						if (current!=max)
							current=combine(degreetable.get(current.degree),current);
						else
							current=combine(current, degreetable.get(current.degree));
						degreetable.remove(current.degree-1);
					}
				}
				else
				{
					degreetable.put(current.degree, current);
					flag=true;
				}
			}
			prev=current;
			current=next;
		}
		
		//convert back doubly linked list to circular list
		current=max;
		while (current!=null)
		{
			prev=current;
			current=current.right;
		}
		max.left=prev;
		prev.right=max;
			
	}
	
	//find the maximum value node in the root list
	public FibHeapNode findMax(FibHeapNode node)
	{
		FibHeapNode max1=node;
		
		FibHeapNode current=node;
		do
		{	
			if (current.val>max1.val)
			{
				max1=current;
			}
			current=current.right;
		}
		while(current != node);
		return max1;
	}
	
	//remove the max node from the heap and return
	public FibHeapNode removeMax()
	{
		FibHeapNode currentmax=max;
		
		FibHeapNode removedmax_child= currentmax.child;
		FibHeapNode next=null;
		
		if(removedmax_child!=null)
		{
			if (max.right==max)
			{
				max=null;
			}
			else{
			currentmax.left.right=currentmax.right;
			currentmax.right.left=currentmax.left;
			max=max.right;
			}
			removedmax_child.left.right=null;
			removedmax_child.left=null;
			while (removedmax_child!=null)
			{	
				next=removedmax_child.right;
				insert(removedmax_child);
				removedmax_child=next;
				
			}
			max=findMax(max);
			}
		
		else
		{
			if (max.right==max)
			{
				max=null;
			}
			else
			{
			currentmax.right.left=currentmax.left;
			currentmax.left.right=currentmax.right;
			max=max.right;
			
			max=findMax(max);
			}
		}
		
		if (max!=null)
		{
		pairwiseCombine();
		}  
		return currentmax;
	}

}

public class hashtagcounter {
	
    public static void main(String[] args) {
	if (args.length != 1)
	{
		System.out.println("Filename missing\n Format : javac Hashtagcounter <filename>");
		System.exit(0);
	}
    	FibonacciHeap heap = new FibonacciHeap();
    	Hashtable<String, FibHeapNode> database
	     = new Hashtable<String, FibHeapNode>();
    	BufferedReader br = null;
      BufferedWriter bw = null;
    	String filePath = new File(args[0]).getAbsolutePath();
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(filePath));
      bw = new BufferedWriter(new FileWriter("output_file.txt"));

			while (!(sCurrentLine = br.readLine()).equalsIgnoreCase("stop")) {
				
				if (sCurrentLine.charAt(0)=='#')
				{
					String[] s=sCurrentLine.split(" ");
					s[0]=s[0].substring(1,s[0].length());
					if (database.containsKey(s[0])==false)
					{
						FibHeapNode node = heap.insert(Integer.parseInt(s[1]),s[0]);
						database.put(s[0], node);
					}
					else
					{
						FibHeapNode node = database.get(s[0]);
						heap.increaseKey(node, Integer.parseInt(s[1]));
					}
				}
				else
				{
					int n = Integer.parseInt(sCurrentLine);
					ArrayList<String> hashtags = new ArrayList<String>();
					ArrayList<Integer> hashtag_values = new ArrayList<Integer>();
          if (n>database.size())
          {
            n=database.size();
          }
					for (int i = 0; i < n; i++)
					{
						FibHeapNode max=heap.removeMax();
						hashtag_values.add(max.val);
						String key=max.key;
						hashtags.add(key); 
					}
					String out="";
					for (int i=0;i<hashtags.size();i++)
					{
						FibHeapNode node = heap.insert(hashtag_values.get(i),hashtags.get(i));
						database.put(hashtags.get(i),node );
						out+=(hashtags.get(i)+",");
					}
					bw.write(out.substring(0,out.length()-1));
					bw.newLine();
				}
			}
    br.close();
    bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

    }

}


