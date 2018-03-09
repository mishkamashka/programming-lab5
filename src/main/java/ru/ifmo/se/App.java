package ru.ifmo.se;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 * The App class implements the application which allow user
 * to interact with the collection using console.
 */

public class App {
    private final String filename = System.getenv("FILENAME");
    private final String filepath = "C:\\files\\" + filename;
    Set<Person> collec = new TreeSet<>();
    File file = new File(filepath);

    /**
     * Starts the app.
     */
    public void start() {
        this.load();
        while (true) {
            Scanner comin = new Scanner(System.in);
            String command = comin.next();
            System.out.println();
            switch (command) {
                case "clear":
                    this.clear();
                    break;
                case "load":
                    this.load();
                    break;
                case "add":
                    this.addObject(comin);
                    break;
                case "remove_greater":
                    this.remove_greater(comin);
                    break;
                case "quit":
                    this.quit(comin);
                    break;
                case "show":
                    this.showCollection();
                    break;
                case "help":
                    this.help();
                    break;
                default:
                    System.out.println("Not valid command. Try one of those:\nhelp - get help;\nclear - clear the collection;" +
                            "\nload - load the collection again;\nadd {element} - add new element to collection;" +
                            "\nshow - show current collection;\nremove_greater {element} - remove elements greater than given;" +
                            "\nquit - quit;");
            }
        }
    }

    /**
     * Clears the collection and prints the message about it.
     */
    public void clear() {
        if (collec.isEmpty())
            System.out.println("There is nothing to remove, collection is empty.");
        else {
            collec.clear();
            System.out.println("Collection has been cleared.");
        }
    }

    /**
     * Adds new element given in json format to the collection.
     *
     * @param sc needed to get object which has to be added to the collection.
     */
    public void addObject(Scanner sc) {
        StringBuilder tempString = new StringBuilder();
        tempString.append(sc.next());
        try {
            this.collec.add(this.jsonToObject(tempString.toString(), Known.class));
            System.out.println("Object has been added.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Check your object and try again. For example of json format see \"help\" command.");
            e.printStackTrace();
        }
    }

    /**
     * Loads objects to the collection from the file.
     */
    public void load() {
        try (Scanner sc = new Scanner(file)) {
            StringBuilder tempString = new StringBuilder();
            tempString.append('[');
            sc.useDelimiter("}\\{");
            while (sc.hasNext()) {
                tempString.append(sc.next());
                if (sc.hasNext())
                    tempString.append("},{");
            }
            sc.close();
            JSONArray jsonArray = new JSONArray(tempString.append(']').toString());
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String jsonObjectAsString = jsonObject.toString();
                    this.collec.add(this.jsonToObject(jsonObjectAsString, Known.class));
                }
                System.out.println("Collection has been loaded.");
            } catch (NullPointerException e) {
                System.out.println("File is empty.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all elements which are greater than given in json format one.
     *
     * @param sc needed to get object which objects from th collection have to be compared to.
     */
    public void remove_greater(Scanner sc) {
        StringBuilder tempString = new StringBuilder();
        tempString.append(sc.next());
        try {
            Person a = this.jsonToObject(tempString.toString(), Known.class);
            this.collec.removeIf(person -> a.compareTo(person) > 0);
            System.out.println("Objects greater than given have been removed.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Check your object and try again.\nFor example of json format try \"help\" command.");
            e.printStackTrace();
        }
    }

    /**
     * Saves current collection to the file and quits the app.
     *
     * @param sc needed to be closed.
     */
    public void quit(Scanner sc) {
        sc.close();
        System.exit(0);
    }

    /**
     * Saves current collection to the file.
     */
    public void save(){
        try (Writer writer = new FileWriter(file)) {
            Gson json = new GsonBuilder().create();
            for (Person person : this.collec) {
                writer.write(json.toJson(person));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts json string to object of T class.
     *
     * @param tempString string in json format.
     * @param classT class which string is needed to be converted to.
     * @param <T> type of returned object.
     * @return object of T class.
     */
    public <T> T jsonToObject(String tempString, Class<T> classT) {
        Gson json = new GsonBuilder().create();
        return json.fromJson(tempString, classT);
    }

    /**
     * Shows the current content of the collection.
     */
    public void showCollection() {
        if (this.collec.isEmpty())
            System.out.println("Collection is empty.");
        for (Person person : this.collec) {
            System.out.println(person.toString());
        }
    }

    /**
     * Shows the list of commands and json-pattern for object input.
     */
    public void help() {
        System.out.println("Commands:\nclear - clear the collection;\nload - load the collection again;" +
                "\nadd {element} - add new element to collection;\nremove_greater {element} - remove elements greater than given;" +
                "\nquit - quit;\nhelp - get help;");
        System.out.println("\nPattern for object Person input:\n{\"name\":\"Andy\",\"last_name\":\"Killins\",\"age\":45," +
                "\"generalClothes\":[{\"colour\":\"white\",\"patches\":[\"WHITE_PATCH\",\"BLACK_PATCH\",\"NONE\",\"NONE\",\"NONE\"]," +
                "\"material\":\"NONE\"}],\"shoes\":[{\"colour\":\"awesome\",\"patches\":[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
                "\"material\":\"NONE\"}],\"accessories\":[],\"state\":\"NEUTRAL\"}");
        System.out.println("\nHow objects are compared:\nObject A is greater than B if its name and last_name are greater than Bs.");
    }
}
