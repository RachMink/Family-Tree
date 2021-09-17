// import java.io.File;
// import java.io.PrintStream;
// import java.util.ArrayList;
// import java.util.Scanner;


// public class Homework4{
//     public static void main(String[] args) throws Exception{
//         Scanner sc = new Scanner(new File ("input.txt"));
//         PrintStream ps = new PrintStream(new File("output.txt"));

//         ArrayList<Node> inputArrayList = new ArrayList<>();// how to access the Node idea from the main method
//         while(sc.hasNext()){
//             String name = sc.next();
//             int numChildren = sc.nextInt();
            
//             inputArrayList.add(new Node(name, numChildren));
//         }
        
//         FamilyTree tree = new FamilyTree(inputArrayList);
//         //newTree.printTree(newTree);
//     }
// }