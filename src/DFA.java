import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Scanner;

public class DFA {
    public static Path path = Paths.get("xml_file");
    public static int currentstate ;
    public static int numberOfFinalStates;
    public static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        try{

            File file = new File(path.toAbsolutePath() + "\\input.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            Element[] elements = new Element[10];

            //number of Alphabets
            NodeList list1 = doc.getElementsByTagName("Alphabets");
            Node e1 = list1.item(0);
            elements[0] = (Element) e1;
            int numberOfAlphabets = Integer.parseInt(elements[0].getAttribute("numberOfAlphabets"));

            //number of States
            NodeList list2 = doc.getElementsByTagName("States");
            Node e2 = list2.item(0);
            elements[1] = (Element) e2;
            int numberOfStates = Integer.parseInt(elements[1].getAttribute("numberOfStates"));

            //number of Transitions
            NodeList list3 = doc.getElementsByTagName("Transitions");
            Node e3 = list3.item(0);
            elements[2] = (Element) e3;
            int numberOfTransitions = Integer.parseInt(elements[2].getAttribute("numberOfTrans"));

            //initial and final state
            currentstate = Integer.valueOf(((Element)elements[1].getElementsByTagName("initialState").item(0)).getAttribute("name"));
            numberOfFinalStates = Integer.valueOf(((Element)elements[1].getElementsByTagName("FinalStates").item(0)).getAttribute("numberOfFinalStates"));

            int[] FinalStates = new int[numberOfFinalStates];
            for (int i=0 ; i< numberOfFinalStates ; i++){
                FinalStates[i] = Integer.valueOf(((Element)((Element)elements[1].getElementsByTagName("FinalStates").item(0)).getElementsByTagName("finalState").item(i)).getAttribute("name"));
            }

            int[][] Automatainfo = new int[numberOfTransitions][4];
            int[] Alphabet = new int[numberOfAlphabets];

            //Alphabets
            for(int i=0 ;i < numberOfAlphabets ; i++){
               String x = (((Element)elements[0].getElementsByTagName("alphabet").item(i)).getAttribute("letter"));
               char[] c = x.toCharArray();
               Alphabet[i] = c[0];
            }

            //Transitions
            for(int i=0 ; i<numberOfTransitions ; i++){
                int source = Integer.valueOf(((Element)elements[2].getElementsByTagName("transition").item(i)).getAttribute("source"));
                int destination = Integer.valueOf(((Element)elements[2].getElementsByTagName("transition").item(i)).getAttribute("destination"));
                String x = (((Element)elements[2].getElementsByTagName("transition").item(i)).getAttribute("label"));
                char[] label = x.toCharArray();

                Automatainfo[i][0] = source;
                Automatainfo[i][1] = destination;
                Automatainfo[i][2] = label[0];

                for(int j=0 ; j<numberOfFinalStates ; j++){
                    if(destination == FinalStates[j])  Automatainfo[i][3] = 1;
                    else continue;
                }

            }
            for (int i=0 ; i<numberOfTransitions ; i++){

                System.out.println(Automatainfo[i][0] + "-" + Automatainfo[i][1] + "-" + Automatainfo[i][2] + "-" + Automatainfo[i][3] + "-" );

            }

            System.out.print("please enter the text : ");
            String str = input.nextLine();
            Checker(str,Automatainfo,currentstate,Alphabet,FinalStates);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void Checker(String str , int[][] info , int current , int[] Alphabet, int[] finalstate){
        char[] sequence = str.toCharArray();

        for (int i=0 ; i<sequence.length ; i++){
            if(checkAlphabet(Alphabet,sequence[i]) == true){

                if(destination(current,(int)sequence[i],info) == -1){
                    System.out.println("The input string is not accepted.");
                    System.exit(0);
                }
                else{
                    current = destination(current,(int)sequence[i],info);
                }

            }
            else{
                System.out.println("The input string is not accepted.");
                System.exit(0);
            }
        }
        for(int i=0 ; i<finalstate.length ; i++){
            if(finalstate[i] == current ){
                System.out.println("The input string is accepted.");
                System.exit(0);
            }
            else continue;
        }
        System.out.println("The input string is not accepted.");

    }
    public static int destination(int current, int label,int[][] info){
        for(int i=0 ; i< info.length; i++){
            if(current == info[i][0] && label == info[i][2]){
                return info[i][1];
            }
            else continue;
        }
        return -1;
    }
    public static boolean checkAlphabet(int[] Alphabet,char lable){
        for (int i=0;i<Alphabet.length ; i++){
            if(Alphabet[i] == (int)lable) return true;
            else continue;
        }
        return false;
    }
}