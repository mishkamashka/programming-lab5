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
 * Класс, реализующий консольное приложение для работы
 * с коллекцией в интерактиивном режиме.
 */

public class App {
    private final String filename = System.getenv("FILENAME");
    private final String filepath = "C:\\files\\" + filename;
    Set<Person> collec = new TreeSet<>();
    File file = new File(filepath);

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
     * Очищает коллекцию, в случае пустой коллекции выводит сообщение об этом.
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
     * Добавляет в коллекцию переданный в формате json объект.
     *
     * @param sc для получения внутри метода объекта, который нужно добавить в коллекцию.
     */
    public void addObject(Scanner sc) {
        StringBuilder tempString = new StringBuilder();
        tempString.append(sc.next());
        try {
            this.collec.add(this.jsonToPerson(tempString.toString(), Known.class));
            System.out.println("Object has been added.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Check your object and try again. For example of json format see \"help\" command.");
            e.printStackTrace();
        }
    }

    /**
     * Загружает объекты в коллекцию из файла.
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
                    this.collec.add(this.jsonToPerson(jsonObjectAsString, Known.class));
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
     * Удаляет из коллекции все элементы, превышающие заданный, переданный в формате json.
     *
     * @param sc для получения внутри метода объекта, c которым производится сравнение.
     */
    public void remove_greater(Scanner sc) {
        StringBuilder tempString = new StringBuilder();
        tempString.append(sc.next());
        try {
            Person a = this.jsonToPerson(tempString.toString(), Known.class);
            this.collec.removeIf(person -> a.compareTo(person) > 0);
            System.out.println("Objects greater than given have been removed.");
        } catch (Exception e) {
            System.out.println("Something went wrong. Check your object and try again.\nFor example of json format try \"help\" command.");
            e.printStackTrace();
        }
    }

    /**
     * Осуществляет сохранение коллекции в файл и останавливает работу приложения.
     *
     * @param sc передается для закрытия.
     */
    public void quit(Scanner sc) {
        try (Writer writer = new FileWriter(file)) {
            Gson json = new GsonBuilder().create();
            for (Person person : this.collec) {
                writer.write(json.toJson(person));
                // writer.write("end");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sc.close();
            System.exit(0);
        }
    }


    public <T> T jsonToPerson(String tempString, Class<T> classT) {
        Gson json = new GsonBuilder().create();
        T obj = json.fromJson(tempString, classT);
        return obj;
    }

    /**
     * Выводит текущее содержимое коллекции.
     */
    public void showCollection() {
        if (this.collec.isEmpty())
            System.out.println("Collection is empty.");
        for (Person person : this.collec) {
            System.out.println(person.toString());
        }
    }

    /**
     * Выводит список команд, шаблон json формата объекта для ввода.
     */
    public void help() {
        System.out.println("Commands:\nclear - clear the collection;\nload - load the collection again;" +
                "\nadd {element} - add new element to collection;\nremove_greater {element} - remove elements greater than given;" +
                "\nquit - quit;\nhelp - get help;");
        System.out.println("\nPattern for object Person input:\n{\"name\":\"Jack\",\"last_name\":\"Black\",\"age\":45}");
        System.out.println("\nHow objects are compared:\nObject A is greater than B if its name and last_name are greater than Bs.");
    }
}


        /*Person person = new Known("Frank");
        person.setAge(5);
        person.setLast_name("Heity");
        person.addGeneralClothes(new Jacket("blue", Season.SUMMER), person.getGeneralClothes());
        person.addGeneralClothes(new Shirt("yellow"), person.getGeneralClothes());
        person.addGeneralClothes(new Jeans("black with stripes"), person.getGeneralClothes());
        person.addShoes(new Trainers("green"), person.getShoes());
        this.collec.add(person);
        Person person1 = new Known("Kolh");
        person.setAge(45);
        person.setLast_name("Veity");
        this.collec.add(person1);*/
