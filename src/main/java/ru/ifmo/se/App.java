package ru.ifmo.se;

import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class App {

    private final static String filename = System.getenv("FILENAME");
    private final static String currentdir = System.getProperty("user.dir");
    private static String filepath;
    private static File file;
    Set<Person> collec = new TreeSet<>();

    public void start() {
        this.filemaker();
        try {
            this.load();
        }catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sc;
        while (true) {
            System.out.println();
            sc = new Scanner(System.in);
            String command;
            String input;
            String[] buf;
            String data = "";
            while (true) {
                input = sc.nextLine();
                buf = input.split(" ");
                command = buf[0];
                if (buf.length > 1)
                    data = buf[1];
                switch (command) {
                    case "clear":
                        this.clear();
                        break;
                    case "load":
                        try {
                            this.load();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "add":
                        this.addObject(data);
                        break;
                    case "remove_greater":
                        this.removeGreater(data);
                        break;
                    case "quit":
                        this.quit();
                        break;
                    case "show":
                        this.show();
                        break;
                    case "describe":
                        this.describe();
                        break;
                    case "help":
                        this.help();
                        break;
                    default:
                        System.out.println("Not valid command. Try one of those:\nhelp - get help;\nclear - clear the collection;" +
                                "\nload - load the collection again;\nadd {element} - add new element to collection;" +
                                "\nshow - show current collection;\ndescribe - show objects from current collection with descriptions;" +
                                "\nremove_greater {element} - remove elements greater than given;" +
                                "\nquit - quit;");
                }
            }
        }
    }

    private static void filemaker() {
        if (currentdir.startsWith("/")) {
            filepath = currentdir + "/" + filename;
        } else
            filepath = currentdir + "\\" + filename;
        file = new File(filepath);
    }

    private void load() throws IOException {
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
                    this.collec.add(JsonConverter.jsonToObject(jsonObjectAsString, Known.class));
                }
                System.out.println("Connection has been loaded.");
            } catch (NullPointerException e) {
                System.out.println("File is empty.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Collection can not be loaded.\nFile " + filename + " is not accessible: it does not exist or permission denied.");
            e.printStackTrace();
        }
    }

    protected void save(){
        try {
            Writer writer = new FileWriter(file);
            //Server.collec.forEach(person -> writer.write(Connection.objectToJson(person)));
            for (Person person: this.collec){
                writer.write(JsonConverter.objectToJson(person));
            }
            writer.close();
            System.out.println("Collection has been saved.");
        } catch (IOException e) {
            System.out.println("Collection can not be saved.\nFile "+filename+" is not accessible: it does not exist or permission denied.");
            e.printStackTrace();
        }
    }

    private void show() {
        if (this.collec.isEmpty())
            System.out.println("Collection is empty.");
        this.collec.forEach(person -> System.out.println(person.toString()));
        System.out.println();
    }

    private void describe() {
        this.collec.forEach(person -> person.describe());
        System.out.println();
    }

    private void quit() {
        System.exit(0);
    }


    private void removeGreater(String data) {
        Person a = JsonConverter.jsonToObject(data, Known.class);
        System.out.println(a.toString());
        this.collec.removeIf(person -> a.compareTo(person) > 0);
        System.out.println("Objects greater than given have been removed.\n");
    }

    private void addObject(String data) {
        try {
            if ((JsonConverter.jsonToObject(data, Known.class).getName() != null)) {
                this.collec.add(JsonConverter.jsonToObject(data, Known.class));
                System.out.println("Object " + JsonConverter.jsonToObject(data, Known.class).toString() + " has been added.\n");
            } else System.out.println("Object null can not be added.");
        } catch (NullPointerException | JsonSyntaxException e) {
            System.out.println("Something went wrong. Check your object and try again. For example of json format see \"help\" command.\n");
            System.out.println(e.toString());
        }
    }

    private void clear() {
        if (collec.isEmpty())
            System.out.println("There is nothing to remove, collection is empty.");
        else {
            collec.clear();
            System.out.println("Collection has been cleared.");
        }
    }

    private void help() {
        System.out.println("Commands:\nclear - clear the collection;\nload - load the collection again;" +
                "\nshow - show the collection;\ndescribe - show the collection with descriptions;" +
                "\nadd {element} - add new element to collection;\nremove_greater {element} - remove elements greater than given;" +
                "\nsave - save changes on server;\nq - quit without saving;\nqw - save on server and quit;\nhelp - get help;\n" +
                "save_file - save current server collection to file;\nload_file - load collection on server from file.");
        System.out.println("\nPattern for object Person input:\n{\"name\":\"Andy\",\"last_name\":\"Black\"}");
        System.out.println("\nHow objects are compared:\nObject A is greater than B if it stands further from the door B does. (That's weird but that's the task.)\n");
        System.out.println("Collection is saved to file when server shuts down or \"save_file\" command.");
    }
}
