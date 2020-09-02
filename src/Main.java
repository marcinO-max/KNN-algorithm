import com.sun.xml.internal.ws.addressing.WsaActionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {


    public static void main(String [] args) throws FileNotFoundException {
        ArrayList<Specimen> all_specimens = new ArrayList<>();
        ArrayList<Centroid> all_centroids = new ArrayList<>();
        int num_of_arguments=4;

        Scanner sc = new Scanner(new File("src/iris_training.txt"));

        while(sc.hasNext()){
            all_specimens.add(new Specimen(sc.nextLine(),Boolean.FALSE));
        }



        //get all types
        ArrayList<String> all_types = new ArrayList<>();
        for (Specimen s : all_specimens){
            if (!all_types.contains(s.nazwa)) {
                all_types.add(s.nazwa);
            }
        }


        for (String type : all_types){
            Centroid temp_centroid = new Centroid(type,new Double[num_of_arguments]);
            //fill with 0
            Arrays.fill(temp_centroid.arguments, 0.0);


            //num of elements assigned to centroid
            int num_of_elements = 0;

            for (Specimen spec : all_specimens ){
                if(spec.nazwa.equals(type)){

                    num_of_elements++;
                    for (int i=0;i<temp_centroid.arguments.length;i++){
                        temp_centroid.arguments[i]+=spec.wartosci[i];
                    }

                }
            }

            //dividing by num_of_elements
            for (int i=0;i<temp_centroid.arguments.length;i++){
                temp_centroid.arguments[i]/=num_of_elements;
            }

            all_centroids.add(temp_centroid);

        }

//test checking for one specimen
      //  assignSpecimen(all_centroids, new Specimen(" 5,1    \t 3,5    \t 1,4    \t 0,2    \t Iris-setosa",Boolean.TRUE));






        double correct=0;
        double incorrect=0;
        double all = 0;
        Scanner test_sc = new Scanner(new File("src/iris_test.txt"));
        while(test_sc.hasNextLine()){
            if (assignSpecimen(all_centroids,new Specimen(test_sc.nextLine(),true))){
                correct++;
                all++;
            }else{
                incorrect++;
                all++;
            }
        }

        System.out.println("Correct: " + correct + " Incorrect: " + incorrect);
        double percentage = (correct)/(all);
        System.out.println("Percentage accuracy: " + percentage*100 +"%");





    }


    public static double getDistance(Centroid c,Specimen s){

        double distance=0;

        for(int i=0;i<s.wartosci.length;i++){
            distance+=Math.pow(s.wartosci[i]-c.arguments[i],2);
        }
//        if(distance<0){
//            distance *= -1;
//        }
        return Math.sqrt(distance);
    }

    public static Boolean assignSpecimen(ArrayList<Centroid> centroids, Specimen s){
        Double distance = getDistance(centroids.get(0),s);
        Centroid closest_centroid=centroids.get(0);

        for(Centroid temp_centroid:centroids){

            double temp_dist = getDistance(temp_centroid,s);
            if(temp_dist<distance){
                distance=temp_dist;
                closest_centroid=temp_centroid;
            }

        }

        System.out.println("Closest centroid " + closest_centroid.nazwa + " ("+ distance+")" + " ---> " + s.nazwa);

        return s.nazwa.equals(closest_centroid.nazwa);


    }





}