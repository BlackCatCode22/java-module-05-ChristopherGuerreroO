package Zoo;

import java.io.*;
import java.util.*;

public class ZooManager {

    private static final List<Animal> animalList = new ArrayList<>();
    private static final Map<String, Integer> speciesCount = new HashMap<>();

    public static void loadAnimals() {
        try {
            // Use resource path for reading files
            InputStream animalStream = ZooManager.class.getResourceAsStream("/resources/arrivingAnimals.txt");
            InputStream nameStream = ZooManager.class.getResourceAsStream("/resources/animalNames.txt");

            if (animalStream == null || nameStream == null) {
                System.out.println("One or more input files not found.");
                return;
            }

            Scanner animalScanner = new Scanner(animalStream);
            Scanner nameScanner = new Scanner(nameStream);

            while (animalScanner.hasNextLine() && nameScanner.hasNextLine()) {
                String line = animalScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length != 3) continue;  // Skip malformed lines

                String species = parts[0].trim();
                int age = Integer.parseInt(parts[1].trim());
                boolean specialAttribute = Boolean.parseBoolean(parts[2].trim()); // isAlpha or isAggressive

                String name = nameScanner.nextLine().trim();
                Animal animal = null;

                switch (species.toLowerCase()) {
                    case "hyena" -> animal = new Hyena(name, age, specialAttribute);
                    case "lion" -> animal = new Lion(name, age, specialAttribute);
                    case "tiger" -> animal = new Tiger(name, age, specialAttribute);
                    case "bear" -> animal = new Bear(name, age, specialAttribute);
                }

                if (animal != null) {
                    animalList.add(animal);
                    speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
                }
            }

            animalScanner.close();
            nameScanner.close();

            generateReport();

        } catch (Exception e) {
            System.out.println("Error during file reading: " + e.getMessage());
        }
    }

    private static void generateReport() {
        try (PrintWriter writer = new PrintWriter("newAnimals.txt")) {
            writer.println("Zoo Animal Report:");
            writer.println("------------------");

            for (Animal animal : animalList) {
                writer.printf("Species: %s | Name: %s | Age: %d%n",
                        animal.getSpecies(), animal.getName(), animal.getAge());
            }

            writer.println("\nTotal Count per Species:");
            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                writer.printf("%s: %d%n", entry.getKey(), entry.getValue());
            }

            System.out.println("Report generated successfully.");
        } catch (IOException e) {
            System.out.println("Failed to write report: " + e.getMessage());
        }
    }
}
