package Application;

import Loader.*;
import jdk.jshell.execution.LoaderDelegate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.IOException;

public class Gui extends JFrame {
    private JTextField textField1;
    private JButton SearchButton;
    private JButton ClearButton;
    private JTextArea textArea1;
    private javax.swing.JPanel jpanel;


    public Gui(Corpus corpus, Clusterer clusterer) {

        setContentPane(jpanel);
        setTitle("Webpage Comparator");
        setSize(450,300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        WebScraper ws = new WebScraper();

        //User inputs a URL; Try to connect to URL, scrape text, save as a string.
        //
        //Display most similar URL from DB file
        SearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String input_url = textField1.getText();
                String[] words;
                try {
                    words = ws.scrape(input_url);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                PageProperties userPage = new PageProperties(input_url.substring(30));
                userPage.setParsed_words(words);
                corpus.addToWordCount(words.length);
                ExtendableHashTable input_page_eht = new ExtendableHashTable();

                for (String word:words) {
                    input_page_eht.add(word);
                    corpus.getEHT().add(word);
                    if ( !corpus.getGlobal_dictionary().contains(word) ) { corpus.getGlobal_dictionary().add(word); }
                }

                TFIDFCalculator tfidfCalculator = new TFIDFCalculator(null, corpus.getGlobal_dictionary(), corpus);
                corpus.setTFIDFToIDF(); //update idf values in corpus

                userPage.setEHT(input_page_eht); //instantiate tfidf values in userPage

                TFIDFCalculator.setPagetfidfScore(userPage); //set tfidf scores for the userpage
                for(PageProperties m : clusterer.allMedoids){ //update tfidf with new idf
                    TFIDFCalculator.setPagetfidfScore(m);
                }

                double bestCosSim = 0; int indexBest = 0;
                for(Cluster c : clusterer.clusterArrayList){ // find the cluster userPage belongs to
                    double newCosSim = 0; clusterer.findCosSim(userPage, c.medoid);
                    if(newCosSim > bestCosSim){
                        bestCosSim = newCosSim;
                        indexBest = clusterer.clusterArrayList.indexOf(c);
                    }
                }

                Cluster userCluster = clusterer.clusterArrayList.get(indexBest);
                userCluster.clusterList.add(userPage); // create that cluster and put user page in it

                for(PageProperties p : userCluster.clusterList){
                    TFIDFCalculator.setPagetfidfScore(p); //update tfidf scores
                }

                double bestCosSim2 = 0; int indexBest2 = 0;
                for(PageProperties page : userCluster.clusterList){ // find which in cluster is best match to userPage
                    if( page != userPage){
                        double newCosSim2 = clusterer.findCosSim(page, userPage);
                        if(newCosSim2 > bestCosSim2){
                            bestCosSim2 = newCosSim2;
                            indexBest2 = userCluster.clusterList.indexOf(page);
                        }
                    }
                }

                PageProperties userBestMatch = userCluster.clusterList.get(indexBest2); //assign

                //set text to pretty 
                textArea1.append("You entered " + userPage.getName() + ".\n" +
                        "The closest match is " + userBestMatch.getName() + ".\n" +
                        "It belong to the cluster " + userCluster.medoid.getName() +
                        ", which also contains: \n" + userCluster.clusterList.toString());



            }
        });

        //Clicking the "clear" button will reset the panel to default values
        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText("Enter a URL here...");
                textArea1.setText("");
            }
        });

        //Clicking in the text field will clear it for the user
        textField1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                textField1.setText("");
            }
        });
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Loader.Corpus corpus;
        Loader.Clusterer clusterer;
        // import Corpus object from file
        FileInputStream fil = new FileInputStream("src/corpus");
        ObjectInputStream ois = new ObjectInputStream(fil);
        corpus = (Corpus) ois.readObject();

        fil = new FileInputStream("src/clusterer");
        ois = new ObjectInputStream(fil);
        clusterer = (Clusterer) ois.readObject();


        Gui myFrame = new Gui(corpus, clusterer);
    }
}
