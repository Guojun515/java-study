package com.jun.data.structure.tree;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * <p>
 * 	BST树（二分搜索树），不包含重复数据<br>
 * 	1、二分搜索树是一颗二叉树<br>
 * 	2、所有的左子节点都比根节点小<br>
 * 	3、所有的右子节点都比根节点大<br>
 * <p>
 * @author Guojun
 * @Date 2018年9月2日 下午6:45:46
 *
 */
public class BinarySearchTree<E extends Comparable<E>> {

	/**
	 *  树节点
	 */
	private class Node {
		/**
		 * 值
		 */
		public E data;
		
		/**
		 * 左子树
		 */
		public Node left;
		
		/**
		 * 右子树
		 */
		public Node right;
		
		public Node(E data) {
			this.data = data;
		}
	}
	
	/**
	 * 根节点
	 */
	private Node root;
	
	/**
	 * 大小
	 */
	private int size;
	
	/**
	 * 新增节点
	 * @param data
	 */
	public void add(E data) {
		root = this.add(root, data);
	}
	
	/**
	 * 使用递归新增节点
	 * @param parent
	 * @param data
	 */
	private Node add(Node parent, E data) {
		if (parent == null ) {
			parent = new Node(data);
			size ++;
		}
		
		if (parent.data.compareTo(data) > 0) {
			parent.left = this.add(parent.left, data);
		} else if (parent.data.compareTo(data) < 0) {
			parent.right = this.add(parent.right, data);
		}
		
		return parent;
	}
	
	/**
	 * 查找元素是否存在
	 * @param data
	 * @return
	 */
	public boolean contains(E data) {
		return this.contains(root, data);
	}
	
	/**
	 * 使用递归方法查找
	 * @param parent
	 * @param data
	 * @return
	 */
	private boolean contains(Node parent, E data) {
		if (parent == null) {
			return false;
		}
		
		if (parent.data.compareTo(data) == 0) {
			return true;
		}
		
		if (parent.data.compareTo(data) > 0) {
			return this.contains(parent.left, data);
		} else {
			return this.contains(parent.right, data);
		}
	}
	
	/**
	 * 使用循环的方式去查找
	 * @param parent
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean containsForCycle(Node parent, E data) {
		if (parent == null) {
			return false;
		}
		
		Node temp = parent;
		while(temp != null && temp.data.compareTo(data) != 0) {
			if (temp.data.compareTo(data) > 0) {
				temp = temp.left;
			} else if (temp.data.compareTo(data) < 0) {
				temp = temp.right;
			}
		}
		if (temp == null) {
			return false;
		} else {
			return temp.data.compareTo(data) == 0;
		}
	}
	
	/**
	 *         5
	 *       /   \
	 *      3     7 
	 *     / \   / \
	 *    2   4 6   9
	 * 树的前序遍历，先遍历根节点，再遍历左子树，最后遍历右子树:5,3,2,4,7,6,9
	 */		
	public void beforeTraverse() {
		this.beforeTraverse(root);
		System.out.println("*********");
		this.beforeTraverseNr(root);
	}
	
	/**
	 * 使用递归方法遍历
	 * @param parent
	 */
	private void beforeTraverse(Node parent) {
		if (parent == null) {
			return;
		}

		// 输出节点数据
		System.out.println(parent.data);
		// 遍历左子树
		this.beforeTraverse(parent.left);
		// 左子树遍历完后遍历右子树
		this.beforeTraverse(parent.right);
	}
	
	/**
	 * 非递归的方法实现,使用栈实现
	 * @param parent
	 */
	private void beforeTraverseNr(Node parent) {
		Stack<Node> stack = new Stack<>();
		stack.push(parent);
		while(!stack.isEmpty()) {
			Node temp = stack.pop();
			System.out.println(temp.data);
			
			if (temp.right != null) {
				stack.push(temp.right);
			}
			
			if (temp.left != null) {
				stack.push(temp.left);
			}
		}
	}
	
	/**
	 *         5
	 *       /   \
	 *      3     7 
	 *     / \   / \
	 *    2   4 6   9
	 * 树的中序遍历，先遍历左子树，再遍历父节点，后遍历右子树:2,3,4,5,6,7,9
	 */		
	public void centerTraverse() {
		this.centerTraverse(root);
		System.out.println("*****");
		this.centerTraverseNr(root);
	}
	
	/**
	 * 使用递归方法遍历
	 * @param parent
	 */
	private void centerTraverse(Node parent) {
		if (parent == null) {
			return;
		}
		
 		// 遍历左子树
		this.centerTraverse(parent.left);
		// 输出节点数据
		System.out.println(parent.data);
		// 左子树遍历完后遍历右子树
		this.centerTraverse(parent.right);
	}
	
	/**
	 * 非递归的方法实现,使用栈实现
	 * @param parent
	 */
	private void centerTraverseNr(Node parent) {
		Stack<Node> stack = new Stack<>();
		Set<Node> resultSet = new HashSet<>();
		
		Node currentNode = parent;
		while(true) {
			/*
			 * 中序遍历，先遍历左边的节点在遍历中间的节点，后遍历后边的节点，所以需要先把左边的节点全部压入栈中
			 * 然后从左子树的叶子节点往回遍历，得到左节点后再从栈中压出父节点，然后从根节点获取右节点
			 */
			
			if (currentNode.left == null || resultSet.contains(currentNode.left)) {
				System.out.println(currentNode.data);
				resultSet.add(currentNode);
				
				if (currentNode.right != null) {
					currentNode = currentNode.right;
				} else if (currentNode.right == null && !stack.isEmpty()){
					currentNode = stack.pop();
				} else {
					currentNode = null;
				}
			} else {
				stack.push(currentNode);
				currentNode = currentNode.left;
			}
			
			if (currentNode == null) {
				break;
			}
		}
	}
	
	/**
	 *         5
	 *       /   \
	 *      3     7 
	 *     / \   / \
	 *    2   4 6   9
	 * 树的后序遍历，先遍历左子树，再遍历右子树，最后遍历根节点:2,4,3,6,9,7,5
	 */		
	public void afterTraverse() {
		this.afterTraverse(root);
		System.out.println("********");
		this.afterTraverseNr(root);
	}
	
	/**
	 * 使用递归方法遍历
	 * @param parent
	 */
	private void afterTraverse(Node parent) {
		if (parent == null) {
			return;
		}
		
		// 遍历左子树
		this.afterTraverse(parent.left);
		// 左子树遍历完后遍历右子树
		this.afterTraverse(parent.right);
		// 输出节点数据
		System.out.println(parent.data);
	}
	
	/**
	 * 非递归的方法实现,使用栈实现
	 * @param parent
	 */
	private void afterTraverseNr(Node parent) {
		Stack<Node> stack = new Stack<>();
		Set<Node> resultSet = new HashSet<>();
		
		Node currentNode = parent;
		while(true) {
			/*
			 * 后序遍历，先遍历左边的节点再遍历右边的节点，最后遍历中间的节点，所以需要先把左边的节点全部压入栈中
			 * 然后从左子树的叶子节点往回遍历，得到左节点后再从栈中压出父节点，判断是否有右节点，有就先输出，没有输出根节点
			 */
			if ((currentNode.left == null || resultSet.contains(currentNode.left))) {
				if (currentNode.right == null || resultSet.contains(currentNode.right)) {
					System.out.println(currentNode.data);
					resultSet.add(currentNode);
					
					currentNode = stack.isEmpty() ? null : stack.pop();
				} else {
					stack.push(currentNode);
					currentNode = currentNode.right;
				}
				
			} else {
				stack.push(currentNode);
				currentNode = currentNode.left;
			}
			
			if (currentNode == null) {
				break;
			}
		}
	}
	
	public static void main(String[] args) {
		BinarySearchTree<Integer> tree = new BinarySearchTree<>();

		Integer[] arr = new Integer[] {5,3,2,4,7,6,9,10,20,1};
		for (Integer data : arr) {
			tree.add(data);
		}
		
		tree.afterTraverse();
	}

}
