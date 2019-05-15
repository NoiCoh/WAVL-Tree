
/**
 *
 * WAVLTree
 *
 *Inbal Avivi  inbalavivi 316266121
 *Noy Cohen noy1 205476542
 *
 *
 * An implementation of a WAVL Tree.
 * (Haupler, Sen & Tarajan ‘15)
 *
 */

public class WAVLTree {

	private WAVLNode root;
	WAVLNode external = new WAVLNode(-1,null);
	int rebalanceCount=0;
	private WAVLNode min;
	private WAVLNode max;
	
	/**
	 * public WAVLTree()
	 * 
	 * the constructor of WAVL Tree.
	 */
	public WAVLTree(){
		this.root=null;
		external.setRank(-1);
		}
	
	  /**
	   * public boolean empty()
	   *
	   * returns true if and only if the tree is empty
	   *
	   */	  
	public boolean empty() {
	  if(this.root==null) {
		  return true;
	  }
    return false; 
  }
/**
 * public WAVLNode Position(int k)
 * @param k is the key of the node we search for
 * @return WAVLNode with key k, if there is no key k, we return his father if key k was exist.
 */
  public WAVLNode Position(int k) {
	  WAVLNode x= this.getRoot();
	  WAVLNode y = this.getRoot();
	  while (x!=external) {
		  y=x;
		  if (x.getKey()==k) {
			  return x;
		  }
		  else if(k>x.getKey()) {
			 x=x.getRight(); 
		  }
		  else {
			  x=x.getLeft();
		  } 
	  }
        return y; 
  } 
  
 /**
   * public String search(int k)
   *
   * returns the info of an item with key k if it exists in the tree
   * otherwise, returns null
   */
  public String search(int k)
  {
	  if (empty()) {
		  return null;
	  }
	  if (this.Position(k).getKey()==k) {
		  return this.Position(k).value;
	  }
	  else {
		  return null;
	  }
  }
/**
 * public boolean type(int l,int r,WAVLNode node)
 * @param l = (node's Rank) - (node Left's Rank)
 * @param r = (node's Rank) - (node Right's Rank)
 * @param node, the node that we search for it's type
 * @return if the node type similar to (l,r)
 */
  public boolean type(int l,int r,WAVLNode node) { // return node type when first in is left and second in is right
	  int L = node.getRank()-node.getLeft().getRank();
	  int R = node.getRank()-node.getRight().getRank(); 
	  if (R==r & L==l) {
		  return true;
	  }
	  return false;
  }
  /**
   * public void demote(WAVLNode node)
   * demote node's rank in 1
   * @param node, the node we want to demote it's rank
   */
  public void demote(WAVLNode node) {
	  node.setRank(node.getRank() - 1); 
  }
  /**
   * public void promote(WAVLNode node)
   * promote node's rank in 1
   * @param node, the node we want to promote it's rank
   */
  public void promote(WAVLNode node) {
	  node.setRank(node.getRank() + 1); 
  }
  /**
   * public int rebalanceDeleteNum (WAVLNode node)
   * @param node is the node that changed during delete operation
   * @return the number of rebalance operation we need to committee after deletion in other to get a proper WAVL tree
   * 
   * during the count of rebalance operation, the function rebalance the tree.
   **/
  public int rebalanceDeleteNum (WAVLNode node) {
	  if (node==null) {  // recursion stop point for non terminal demotion
		  int result = rebalanceCount;
		  rebalanceCount=0;
		  return result;
	  }
	  if(node.isleaf() && (type(2,2,node)) ) { // if the node is a leaf of type(2,2), we need to change it's rank to 0
		  node.setRank(0);
		  node.updatesize();
		  return rebalanceDeleteNum(node.parent);
	  }
	  if ((type(3,2,node))||(type(2,3,node))){ //non terminal demote ,case 1
		  rebalanceCount++;
		  demote(node);
		  return rebalanceDeleteNum(node.parent);
	  }
	  if((type(3,1,node))){ //double demote or rotate
		  if(type(2,2,node.right)) { //double demote ,case 2
			  rebalanceCount= rebalanceCount+2;	 
			  demote(node);
			  demote(node.right);
			  return rebalanceDeleteNum(node.parent);	 
		  }
		  else if ((type(1,1,node.getRight()))||(type(2,1,node.getRight()))) { //case 3
			  leftRotate(node);
			  demote(node);
			  promote(node.parent);
			  node.updatesize();
			  if (type(2,2,node) & node.isleaf()) {
				  demote(node);
				  //rebalanceCount++;
			  }
			  rebalanceCount=rebalanceCount+3;
		  }
		  else if ((type(1,2,node.getRight()))) { //case 4
			  rightRotate(node.getRight());
			  updatesizes(node);
			  leftRotate(node);
			  demote(node);
			  demote(node);
			  promote(node.getParent());
			  promote(node.getParent());
			  demote(node.getParent().getRight());
			  node.updatesize();
			  node.getParent().getRight().updatesize();
			  rebalanceCount= rebalanceCount+5;
		  }
			   
	  }
	  else if((type(1,3,node))){ // double demote or rotate
		  if(type(2,2,node.left)) { //double demote ,case 2
		  rebalanceCount= rebalanceCount+2;	 
		  demote(node);
		  demote(node.left);
		  node.getRight().updatesize();
		  return rebalanceDeleteNum(node.parent); 
		  }
		  else if ((type(1,1,node.getLeft()))||(type(1,2,node.getLeft()))) { //case 3
			  rightRotate(node);
			  demote(node);
			  promote(node.parent);
			  node.updatesize();
			  if (type(2,2,node) & node.isleaf()) {
				  demote(node);
				  //rebalanceCount++;
			  }
			  rebalanceCount=rebalanceCount+3;
		  }
		  else if ((type(2,1,node.getLeft()))) { //case 4
			  leftRotate(node.getLeft());
			  rightRotate(node);
			  demote(node);
			  demote(node);
			  promote(node.getParent());
			  promote(node.getParent());
			  demote(node.getParent().getLeft());
			  node.updatesize();
			  node.getParent().getLeft().updatesize();
			  rebalanceCount= rebalanceCount+5;
		  }
		  
	  }
	  int result = rebalanceCount;
	  rebalanceCount=0;
	  return result;
  } 
  /**
   * public int rebalanceInsertNum (WAVLNode node)
   * @param node is the node that changed during insert operation
   * @return the number of rebalance operation we need to committee after insertion in other to get a proper WAVL tree
   * 
   * during the count of rebalance operation, the function rebalance the tree.
   */
  public int rebalanceInsertNum (WAVLNode node) { 
	  
	  if (node==null) { // recursion stop point for non terminal promote
		  int result = rebalanceCount;
		  rebalanceCount=0;
		  return result;
	  }
	  if(type(0,1,node) || type(1,0,node) ) { // promote, case 1
		  promote(node);
		  node.updatesize();
		  rebalanceCount++;
		  return rebalanceInsertNum(node.parent);
	  }
	  
	  if(type(2,0,node)) { // single left rotate, case 2
		  if (type(2,1,node.getRight())) {
			  leftRotate(node);
			  node.updatesize();
			  demote(node);
			  rebalanceCount=rebalanceCount+2;
		  }
		  else if(type(1,2,node.getRight())) { // double rotate, case 3
			  rightRotate(node.getRight());
			  leftRotate(node);
			  demote(node);
			  promote(node.getParent());
			  demote(node.getParent().getRight());
			  node.updatesize();
			  node.getParent().getRight().updatesize();
			  rebalanceCount= rebalanceCount+5 ;
		  }
	  }
	  if(type(0,2,node)) {
		  if (type(1,2,node.getLeft())) { //single right rotate, case 2
			  rightRotate(node);
			  node.updatesize();
			  demote(node);
			  rebalanceCount=rebalanceCount+2;
		  }
		  else if(type(2,1,node.getLeft())) { //double rotate, case 3
			  leftRotate(node.getLeft());
			  rightRotate(node);
			  demote(node);
			  node.updatesize();
			  node.getParent().getLeft().updatesize();
			  promote(node.getParent());
			  demote(node.getParent().getLeft());
			  rebalanceCount= rebalanceCount+5 ;
		  }  
	  }
	  int result = rebalanceCount;
	  rebalanceCount=0;
	  return result;
  }
  /**
   * public void rightRotate(WAVLNode z)
   * @param z the position of the rotate
   * rotate the tree in right direction starting from z
   */
  public void rightRotate(WAVLNode z) {  
	  WAVLNode b = z.getLeft().getRight();
	  WAVLNode x= z.getLeft();
	  WAVLNode zPar = z.getParent();
	  
	  if(root.getKey()==z.getKey()) { // if z is a root , we need to update the new root
		  z.setLeft(b);
		  root = x;
		  x.setRight(z);
		  z.setParent(root);
		  root.setParent(null);
		  }
	  else {
		  z.setLeft(b);
		  x.setRight(z);
		  z.setParent(x);
		  x.setParent(zPar);
  
	  }
	  z.updatesize();
	  z.getParent().updatesize();
	  this.root.updatesize();
	  
  }
  /**
   * public void leftRotate(WAVLNode z)
   * @param z the position of the rotate
   * rotate the tree in left direction starting from z
   */
  public void leftRotate(WAVLNode z) {  
	  WAVLNode x = z.getRight();
	  WAVLNode b = x.getLeft();
	  WAVLNode zPar = z.getParent();
	  
	  if(root.getKey()==z.getKey()) { // if z is a root , we need to update the new root
		  z.setRight(b);
		  root=x;
		  root.setLeft(z);
		  z.setParent(x);
		  root.setParent(null);
		  
	  	}
	  else {
		  
		  z.setRight(b);
		  x.setLeft(z);
		  z.setParent(x);
		  x.setParent(zPar);
	  }
	  z.updatesize();
	  x.updatesize();
	  if(z.getParent().getParent()!=null) {
		  z.getParent().getParent().updatesize();
	  }
	  this.root.updatesize();
	 }
	  
  
  /**
   * public int insert(int k, String i)
   *
   * inserts an item with key k and info i to the WAVL tree.
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
   * returns -1 if an item with key k already exists in the tree.
   */
 
   public int insert(int k, String i) {
	   WAVLNode newnode =new WAVLNode (k, i) ;
	   newnode.size=1;
	   if (empty()) { //if tree is empty, update root, min and max.
		   this.root=newnode;
		   min=root;
		   max=root;
		   return 0;
	   }
	   
	   WAVLNode position = this.Position(k);
	   if (position.getKey()==k) {  //if key k exist in the tree return -1
		   return -1;
	   }
	   newnode.setParent(position);
	   if(k>max.getKey()) { //update max if needed
 		  max=newnode;
 	  }
	   if(k<min.getKey()) {//update min if needed
		   min=newnode;
	   }
	   updatesizes(newnode);
       return rebalanceInsertNum(position);   
   }

   /**
    * public void updatesizes(WAVLNode newnode)
    * The function updates the sub-tree sizes of anyone who is an ancestor of the node inserted until you reach the root. 
    * @param newnode the node we need to update it's ancestors
    */
   public void updatesizes(WAVLNode newnode) { 
	   while (newnode!= null) {
		   newnode.updatesize();
		   newnode = newnode.parent;
	   }
   }
   
   
   /**
   * public int delete(int k)
   *
   * deletes an item with key k from the binary tree, if it is there;
   * the tree must remain valid (keep its invariants).
   * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
   * returns -1 if an item with key k was not found in the tree.
   */
   public int delete(int k)
   {
	   if (empty()) { //if tree is empty  return -1
		   return -1;
	   }
	   
	   WAVLNode position = this.Position(k);
	   WAVLNode parentP = position.getParent();
	   if(k==max.getKey()) { //update the max if needed
		   if(position.getLeft()!=external) {   
			   max=position.getLeft();
		   }
		   else {
			  max=parentP; 
		   }   
	   }
	   if(k==min.getKey()) { //update the min if needed
		   if(position.getRight()!=external) {  
			   min=position.getRight();
		   }
		   else {
			   min=parentP;
		   }
	   }
	   if (position.key!=k) { //key k is not found return -1
		   return -1;
	   }
	   else { 
		  if(position.getRight()==external || position.getLeft()==external) { //unary or leaf
			  delete_unary_or_leaf(position);
			  updatesizes(parentP);
			  return rebalanceDeleteNum(parentP);
		  }
		  else { // have 2 sons,look for the successor 
			  WAVLNode successor = successor(position);
			  WAVLNode successorP = successor.getParent();
			  position.value=successor.value;
			  int newkey=successor.key;
			  delete_unary_or_leaf(successor);
			  position.key=newkey;
			  position.updatesize();
			  updatesizes(successorP);
			  return rebalanceDeleteNum(successorP); 
		  	}
	   }
   }
   /**
    * delete_unary_or_leaf(WAVLNode node)
    * delete unary or leaf WAVL node
    * @param node- unary or leaf node that we want to delete 
    */
   public void delete_unary_or_leaf(WAVLNode node) {
	   if (node.getKey()==root.getKey()) {  //node = root
		   if(node.getRight()!=external ) { //unary root with right child
			   root = node.getRight();
			   node.getRight().setParent(null);
			}
			else if(node.getLeft()!=external) { //unary with left child
				root = node.getLeft();
				node.getLeft().setParent(null);
			}	
			else { //leaf
				root=null;
			} 
	   }
	   else if(node.getParent().getKey() > node.getKey()) { // left son
		   if(node.getRight()!=external ) { //unary with right child
			   node.getParent().setLeft(node.getRight());
			}
			else if(node.getLeft()!=external) { //unary with left child
				node.getParent().setLeft(node.getLeft());
			}	
			else { //leaf
				node.getParent().setLeft(external);
			}
		}
		else { // right son
			if(node.getRight()!=external ) { //unary with right child
				node.getParent().setRight(node.getRight());
			}
			else if(node.getLeft()!=external) { //unary with left child
				node.getParent().setRight(node.getLeft()); 
			}
			else {
				node.getParent().setRight(external);
			}
		}	
   	}
   /**
    * public WAVLNode maxNode(WAVLNode node)
    * @param node - root of subtree
    * @return the maximum node in subtree
    */
   public WAVLNode maxNode(WAVLNode node) {
	   while (node.right!=external) {
		   node=node.right;
	   }
	   return node;
   }
   /**
    * public WAVLNode minNode(WAVLNode node)
    * @param node - root of subtree
    * @return the minimum node in subtree
    */ 
   public WAVLNode minNode(WAVLNode node) {
	   while (node.left!=external) {
		   node=node.left;
	   }
	   return node;
   }
  /**
   * public WAVLNode successor (WAVLNode node)
   * The function receives a node and returns its successor
   * @param node that we look for its successor
   * @return successor
   */
   public WAVLNode successor (WAVLNode node) {
	   if(node.getKey()==max.getKey()) { //if nood = maximum node there is no successor
		   return null;
	   }
	   if (node.right!=external) {
		   return minNode(node.getRight());
	   } 
	   WAVLNode y =node.parent;
	   while (y!=null & node==y.right) { //no right child
		   node=y;
		   y=node.parent;
	   }
	   return y;
   }

   /**
    * public String min()
    *
    * Returns the info of the item with the smallest key in the tree,
    * or null if the tree is empty
    */
   public String min(){
	   
	   if(empty()) {
		   return null;
	   }
	   else {
		   return min.getValue();
	   }
   }

   /**
    * public String max()
    *
    * Returns the info of the item with the largest key in the tree,
    * or null if the tree is empty
    */
   public String max(){
	   if(empty()) {
		   return null;
	   }
	   else {
		   return max.getValue();
	   }
   }

   /**
   * public int[] keysToArray()
   *
   * Returns a sorted array which contains all keys in the tree,
   * or an empty array if the tree is empty.
   */
   public int[] keysToArray()
   {
	   int[] r = new int [this.size()];
	   if(empty()) {
		   return r;
	   }
	   int size =this.size();
	   WAVLNode node = this.min;
	   for(int i=0;i<size;i++) {
			 r[i]=node.getKey();
			 node = successor(node); 
	   }
	   return r;
   }

   /**
   * public String[] infoToArray()
   *
   * Returns an array which contains all info in the tree,
   * sorted by their respective keys,
   * or an empty array if the tree is empty.
   */
   public String[] infoToArray()
   {
	   String[] arr = new String[this.size()]; 
       if(empty()) {
		   return arr;
       }
       	WAVLNode node = this.min;
       	for (int i=0;i<this.size();i++) {
       		arr[i]=node.getValue();
       		node=successor(node);
       	}
       	return arr;
       }
   

   /**
    * public int size()
    *
    * Returns the number of nodes in the tree.
    *
    */
   public int size()
   {
	   if(empty()) {
		   return 0;
	   }
           return this.root.getSubtreeSize();
   }
   
     /**
    * public WAVLNode getRoot()
    *
    * Returns the root WAVL node, or null if the tree is empty
    *
    */
   public WAVLNode getRoot()
   {
           return this.root;
   }
     /**
    * public int select(int i)
    *
    * Returns the value of the i'th smallest key (return -1 if tree is empty)
    * Example 1: select(1) returns the value of the node with minimal key 
        * Example 2: select(size()) returns the value of the node with maximal key 
        * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor  
    *
    */   
   public String select(int i)
   {
	  	if(i>size()||i<=0) {
			return null;
		}	
		return findselect(root,i-1).getValue();	  
 }  
   /**
    * public WAVLNode findselect(WAVLNode curr, int i) 
    * @return the node with the @param i'th smallest key  
    */
   public WAVLNode findselect(WAVLNode curr, int i) {
		int r=curr.getLeft().getSubtreeSize();
		if(r>i) {
			return findselect(curr.getLeft(),i); 
		}
		else if(r<i) {
			return findselect(curr.getRight(),i-r-1);
		}
		return curr;
	}

   /**
   * public class WAVLNode
   */
  public class WAVLNode{
	  
	  private int key;
	  private String value;
	  private WAVLNode left;
	  private WAVLNode right;
	  private WAVLNode parent;
	  private int rank;
	  private int size;
	 
	 /**
	  * public WAVLNode(int key ,Object value )
	  * constructor of WAVL node 
	  * @param key of WAVL node
	  * @param value of WAVL node
	  */
	  public WAVLNode(int key ,Object value ) {
		  this.key= key;
		  this.value=(String)value;
		  this.left=external;
		  this.right=external;
		  this.rank=0;
		  this.size=0;
		  
	  }
			  /**
			   * public int getKey()
			   * @return the WAVL node's key
			   */
				public int getKey()
                {
					return this.key;
                }
				/**
				   * public String getValue()
				   * @return the WAVL node's value
				   */
    
                public String getValue()
                {
                      return this.value; 
                }
                /**
				   * public WAVLNode getLeft()
				   * @return the WAVL node's left node
				   */
                public WAVLNode getLeft()
                {
                        return this.left; 
                }
                /**
				   * public WAVLNode getRight()
				   * @return the WAVL node's right node
				   */
                public WAVLNode getRight()
                {
                        return this.right;
                }
                /**
				   * public boolean isInnerNode()
				   * @return if node is not external node
				   */
                public boolean isInnerNode()
                {
                	return (this!=external);
                }
				/**
				 * public int getSubtreeSize()
				 * @return sub tree size
				 */
                public int getSubtreeSize()
                {
         
                        return this.size;
                }
                /**
                 * public void setRank(int rank)
                 * @param rank the new rank of the WAVL node
                 */
                public void setRank(int rank) {
                	this.rank=rank;
                }
                /**
                 * public boolean isleaf()
                 * @return if node is a leaf
                 */
                public boolean isleaf()
                {
                	if(this.left==external & this.right==external) {
                		return true;
                	}
                	return false;
                }
                /**
                 * public int getRank() 
                 * @return the rank of this WAVL node
                 */
                public int getRank() { 
                	return this.rank;
                }
                /**
                 * public void setRight(WAVLNode node)
                 * set a new right child and update the size of his father
                 * @param node the new right child of this WAVL node
                 */
                public void setRight(WAVLNode node) {
                	this.right=node;
                	if (node!= external) {
                		node.parent=this;
                	}
                	this.updatesize();
                }
                /**
                 * public void setLeft(WAVLNode node)
                 * set a new left child and update the size of his father
                 * @param node the new left child of this WAVL node
                 */
                public void setLeft(WAVLNode node) {
                	this.left=node;
                	if(node!= external) {
                		node.parent=this;
                	}
                	this.updatesize();
                }
                /**
                 * public void setParent(WAVLNode node)
                 * check if the this WAVL node is a left or right child of @param node
                 * @param node the new parent of this WAVL node
                 */
                public void setParent(WAVLNode node) {
                	if (node != null & node!= external) {
                		if(this.getKey() > node.getKey()) {
                			node.setRight(this);
                		}
                		else {
                			node.setLeft(this);
                		}
                		this.parent=node;
                		this.parent.updatesize();
                	}
                	else if (node == null){ 
                		this.parent=node;
                	}
                }
                /**
                 * public WAVLNode getParent() {
                 * @return the parent of this WAVL node
                 */
                public WAVLNode getParent() {
                	return this.parent;
                }
                /**
                 * public void SetSize(int size)
                 * set size for this WAVL node
                 * @param size, the new size of WAVL node
                 */
                public void SetSize(int size){
                	if (this != external) {
                		this.size= size;
                	}
                }
                /**
                 * public void updatesize()
                 * the function update the size of this WAVL node according to his sons's sizes
                 */
                public void updatesize() {
             	   if (this!=external & this!=null) {
             		   this.size= 1+ this.getLeft().getSubtreeSize() + this.getRight().getSubtreeSize();
             	   }
                }
                
  }

}

