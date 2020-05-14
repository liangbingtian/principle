该题有个需要注意的地方是，合起来的数字过大会造成溢出，不要尝试用基本变量接收真实数据大小。```
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int num1=0;
        int num2=0;
        ListNode a=l1;
        ListNode b=l2;
        while(a!=null){
            num1++;
            a=a.next;
        }
        while (b!=null){
            num2++;
            b=b.next;
        }
        int max=num1>=num2?num1+1:num2+1;
        int n1[]=new int[max];
        int n2[]=new int[max];
        int res[]=new int[max];
        for(int i=0;i<num1;i++){
            n1[i]+=l1.val;
            l1=l1.next;
        }
        for(int i=0;i<num2;i++){
            n2[i]+=l2.val;
            l2=l2.next;
        }
        for(int i=0;i<max;i++){
            int temp=n1[i]+n2[i];
                res[i]+=temp;
                int j=i;
                while (res[j]>=10){
                    res[j]=res[j]-10;
                    res[j+1]++;
                    j++;
                }
            }
        ListNode ans=new ListNode(res[0]);
        ListNode temp=ans;
        for(int i=1;i<max;i++){
            if(i==max-1&&res[i]==0){
                continue;
            }
            temp.next=new ListNode(res[i]);
            temp=temp.next;
        }
        return ans;
        }
