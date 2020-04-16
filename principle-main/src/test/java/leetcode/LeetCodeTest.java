package leetcode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.junit.Test;

/**
 * 2020/1/6 下午3:03
 *
 * @author liangbingtian
 */
public class LeetCodeTest {

  /**
   * 第一题:求给定二叉树的最小深度。最小深度是指树的根结点到最近叶子节点的最短路径上节点的数量
   * 考点：队列或者递归
   */
  @Test
  public void solution() {
    TreeNode root = new TreeNode(1);
    Queue<TreeNode> queue = new LinkedList<TreeNode>();
    queue.add(root);
    for (int i = 0; i< 20 ; ++i){
      int length = queue.size();
      for (int a = 0; a < length; ++a) {
        TreeNode tmpNode = queue.poll();
        TreeNode left = new TreeNode(1);
        TreeNode right = new TreeNode(2);
        tmpNode.setLeft(left);
        tmpNode.setRight(right);
        queue.offer(left);
        queue.offer(right);
      }
    }
    long startTime = System.currentTimeMillis();
    System.out.println(getSolutionIteration(root));
    long endTime = System.currentTimeMillis();
    long time = endTime-startTime;
    System.out.println("耗时间:"+time);
  }

  private class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) {val = x;}

    public TreeNode getLeft() {
      return left;
    }

    public void setLeft(TreeNode left) {
      this.left = left;
    }

    public TreeNode getRight() {
      return right;
    }

    public void setRight(TreeNode right) {
      this.right = right;
    }
  }

  //运用递归的方法
  private int getSolutionRecursive(TreeNode node) {
    if (node == null) {
      return 0;
    }
    if (node.getLeft()==null) {
      return getSolutionRecursive(node.getRight())+1;
    }
    if (node.getRight()==null) {
      return getSolutionRecursive(node.getLeft())+1;
    }
    int leftDepth = getSolutionRecursive(node.getLeft())+1;
    int rightDepth = getSolutionRecursive(node.getRight())+1;
    return Math.min(leftDepth, rightDepth);
  }

  //使用迭代的方法
  private int getSolutionIteration(TreeNode node) {
    if (node == null) {
      return 0;
    }
    if (node.getLeft()==null&&node.getRight()==null) {
      return 1;
    }
    int depth = 0;
    Queue<TreeNode> queue = new LinkedList<TreeNode>();
    queue.offer(node);
    while(!queue.isEmpty()) {
      int length = queue.size();
      depth++;
      for (int i = 0; i<length;++i) {
        TreeNode treeNode = queue.poll();
        if (treeNode.getLeft()==null&&treeNode.getRight()==null) {
          return depth;
        }
        if (treeNode.getLeft()!=null) {
          queue.offer(treeNode.getLeft());
        }
        if (treeNode.getRight()!=null) {
          queue.offer(treeNode.getRight());
        }
      }
    }
    return depth;
  }

  //--------------------------------------------

  /**
   * 第二题，计算后缀表达式
   * 考点：栈
   */
  @Test
  public void solution1() {

  }

  //高级点的做法
  private int postfixExpressionHigh(String[] tokens) {
    Stack<Integer> stack = new Stack<Integer>();
    for (int i = 0;i<tokens.length;++i) {
      String str = tokens[0];
      try {
        Integer a = Integer.parseInt(str);
        stack.push(a);
      }catch (NumberFormatException e) {
        int after = stack.pop();
        int before = stack.pop();
        stack.push(getResult(before, after, str));
      }
    }
    return stack.size()==1?stack.pop():0;
  }

  private int getResult(int a, int b, String operator) {
    if (operator.equals("+")){
      return a+b;
    }else if (operator.equals("-")){
      return a-b;
    }else if (operator.equals("*")){
      return a*b;
    }else if (operator.equals("/")){
      return a/b;
    }else {
      return 0;
    }
  }


  //基础的写法
  private int postfixExpression(String[] tokens) {
    if (tokens == null || tokens.length==0){
      return 0;
    }
    Stack<Integer> s = new Stack<Integer>();
    for (int i = 0; i<tokens.length;++i) {
      String str = tokens[0];
      if (str.equals("+")||str.equals("-")||str.equals("*")||str.equals("/")) {
        if (s.size()<2) return 0;
        int after = s.pop();
        int before = s.pop();
        if (str.equals("+")){
          s.push(before+after);
        }else if (str.equals("-")){
          s.push(before-after);
        }else if (str.equals("*")){
          s.push(before*after);
        }else if (str.equals("/")){
          s.push(before/after);
        }
      }else {
        try {
          int num = Integer.parseInt(str);
          s.push(num);
        }catch (NumberFormatException e) {
          return 0;
        }
      }
    }
    return s.size()==1?s.pop() : 0;
  }

  //------------------------------------------------

  /**
   * 直线上点的最多的个数,对于给定的n个位于同一二维平面上的点，求最多能有多少个点位于同一直线
   * 做法：两重迭代嵌套，穷举
   */
  @Test
  public void solution2() {

  }

  private int maxPoint(Point[] points) {
    if (points == null) {
      return 0;
    }
    if (points.length<=2) {
      return points.length;
    }
    Map<Double, Integer> map = new HashMap<Double, Integer>();
    int result = 0;
    for (int i=0;i<points.length;++i) {
      map.clear();
      int overlap = 0;
      int vertical = 0;
      int horizon = 0;
      int max = 0;
      double rate = 0.0;
      for (int j = i+1; j<points.length; j++) {
        double gapx = points[i].getX()-points[j].getX();
        double gapy = points[i].getY()-points[j].getX();
        if (gapx==0&&gapy==0) {
          overlap++;
        }else if (gapx==0) {
          vertical++;
          max = Math.max(vertical, max);
        }else if (gapy==0) {
          horizon++;
          max = Math.max(horizon, max);
        }else {
          rate = gapy/gapx;
          map.put(rate, map.getOrDefault(rate, 1));
          max = Math.max(max, map.get(rate));
        }
      }
      result = Math.max(result, overlap+max+1);
    }
    return result;
  }

  private class Point {
    private int x;
    private int y;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }
  }

  /**
   * 在O(nlogn)的时间内使用常数级空间复杂度对链表进行排序
   * 如果用O(nlogn)的时间复杂度去排序的话，证明使用的是归并排序
   */
  @Test
  public void solution3() {

  }

  private class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
      val = x;
      next = null;
    }
  }
  //先写一下一般的归并排序的写法。先是拆分
  private void sort(int[] arr, int L, int R) {
    if (L==R) {
      return;
    }
    int mid = L + ((R - L) >> 1);
    sort(arr, L, mid);
    sort(arr, mid+1, R);
    merge(arr, L, mid, R);
  }

  //先写下一般归并排序的写法。最后是合并
  private void merge(int[] arr, int L, int mid, int R) {
    int[] temp = new int[R - L + 1];
    int i = 0;
    int p1 = L;
    int p2 = mid + 1;
    // 比较左右两个元素,哪个小，把哪个元素填入temp中
    while (p1<=mid && p2<=R) {
      temp[i++] = arr[p1]<arr[p2]?arr[p1++]:arr[p2++];
    }
    //以下两个while只有一个会执行
    while (p1<=mid) {
      temp[i++] = arr[p1++];
    }
    while (p2<=R) {
      temp[i++] = arr[p2++];
    }

    // 将最终的排序结果赋给原数组
    for (i=0;i<temp.length;++i) {
      arr[L+i] = temp[i];
    }
  }

}
