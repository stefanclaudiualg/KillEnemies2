package org.cuatrovientos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    private static final String ARCHIVO_GUARDADO = "partidaGuardada.dat";
    private static Scanner escaner = new Scanner(System.in);

    public static void main(String[] args) {
        Hero heroe = null;
        ArrayList<Character> personajes = null;
        File archivo = new File(ARCHIVO_GUARDADO);

        if (archivo.exists()) {
            System.out.println("Partida guardada encontrada.");
            System.out.print("¿Deseas cargar la partida guardada? (s/n): ");
            if (escaner.nextLine().trim().equalsIgnoreCase("s")) {
                GameState estado = cargarPartida();
                if (estado != null) {
                    heroe = estado.getHero();
                    personajes = estado.getCharacters();
                    System.out.println("Partida cargada, Bienvenido, " + heroe.getName() + ".\n");
                }
            }
        }

        if (heroe == null || personajes == null) {
            System.out.println("===== NUEVA PARTIDA =====");
            System.out.print("Introduce el nombre de tu héroe: ");
            String nombreHeroe = escaner.nextLine().trim();
            heroe = new Hero(nombreHeroe.isEmpty() ? "Héroe" : nombreHeroe);

            personajes = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                personajes.add(new Friend());
                personajes.add(new Enemy());
            }
            Collections.shuffle(personajes);
        }

        boolean jugando = true;
        while (jugando) {
            contarPersonajes(personajes);
            mostrarPersonajes(personajes);
            heroe.printStats();

            System.out.println("\n¿Qué deseas hacer?");
            System.out.println("1. Atacar");
            System.out.println("2. Defender");
            System.out.println("3. Guardar partida");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            
            switch (escaner.nextLine().trim()) {
                case "1":
                    gestionarAtaque(heroe, personajes);
                    break;
                case "2":
                    gestionarDefensa(heroe, personajes);
                    break;
                case "3":
                    guardarPartida(new GameState(heroe, personajes));
                    break;
                case "4":
                    System.out.print("¿Deseas guardar antes de salir? (s/n): ");
                    if (escaner.nextLine().trim().equalsIgnoreCase("s")) {
                        guardarPartida(new GameState(heroe, personajes));
                    }
                    jugando = false;
                    System.out.println("¡Fin de la partida!");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
                    break;
            }

            if (personajes.isEmpty()) {
                System.out.println("\n¡Ya no quedan más personajes en la lista!");
                heroe.printStats();
                if (archivo.exists()) archivo.delete();
                jugando = false;
            }
        }
    }

    public static void contarPersonajes(ArrayList<Character> lista) {
        int amigos = 0;
        int enemigos = 0;
        for (Character character : lista) {
            if (character instanceof Enemy) {
                enemigos++;
            } else if (character instanceof Friend) {
                amigos++;
            }
        }
        System.out.println("----------------------------------------------");
        System.out.println("Personajes restantes: " + amigos + " amigo(s) y " + enemigos + " enemigo(s).");
    }

    public static void mostrarPersonajes(ArrayList<Character> lista) {
        System.out.println("\n--- Lista de Personajes ---");
        for (int i = 0; i < lista.size(); i++) {
            String tipo;
            if (lista.get(i) instanceof Enemy) {
                tipo = "Enemigo";
            } else {
                tipo = "Amigo";
            }
            System.out.println("[" + i + "] " + tipo);
        }
    }

    private static void mostrarAnimacionAtaque() {
        System.out.println("//============>");
        System.out.println("     \\\\  //");
        System.out.println("      \\\\//");
        System.out.println("      //\\\\");
        System.out.println("     //  \\\\");
    }

    private static void mostrarAnimacionDefensa() {
        System.out.println("   //========\\\\");
        System.out.println("  ||==========||");
        System.out.println("  ||==========||");
        System.out.println("   \\\\========//");
    }

    private static void gestionarAtaque(Hero heroe, ArrayList<Character> lista) {
        int indice = pedirIndice(lista.size());
        if (indice == -1) return;

        mostrarAnimacionAtaque();
        Character character = lista.remove(indice);

        if (character instanceof Enemy enemigo) {
            heroe.attack(enemigo);
        } else {
            System.out.println("¡Cuidado! Has atacado a un amigo por error. Ha muerto y ha sido eliminado.");
        }
    }

    private static void gestionarDefensa(Hero heroe, ArrayList<Character> lista) {
        int indice = pedirIndice(lista.size());
        if (indice == -1) return;

        mostrarAnimacionDefensa();
        Character character = lista.get(indice);

        if (character instanceof Friend amigo) {
            heroe.defend(amigo);
        } else {
            System.out.println("¡Has defendido a un enemigo! Se ha fortalecido y se ha duplicado en la lista.");
            lista.add(new Enemy());
        }
    }

    private static int pedirIndice(int limite) {
        System.out.print("Introduce el índice del personaje (0 a " + (limite - 1) + "): ");
        try {
            int indice = Integer.parseInt(escaner.nextLine().trim());
            if (indice < 0 || indice >= limite) {
                System.out.println("Índice fuera de rango.");
                return -1;
            }
            return indice;
        } catch (NumberFormatException e) {
            System.out.println("Por favor, introduce un número válido.");
            return -1;
        }
    }

    public static void guardarPartida(GameState estado) {
        try (ObjectOutputStream flujoSalida = new ObjectOutputStream(new FileOutputStream(ARCHIVO_GUARDADO))) {
            flujoSalida.writeObject(estado);
            System.out.println("Partida guardada correctamente en '" + ARCHIVO_GUARDADO + "'.");
        } catch (IOException e) {
            System.out.println("Error al guardar la partida: " + e.getMessage());
        }
    }

    public static GameState cargarPartida() {
        try (ObjectInputStream flujoEntrada = new ObjectInputStream(new FileInputStream(ARCHIVO_GUARDADO))) {
            return (GameState) flujoEntrada.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar la partida: " + e.getMessage());
            return null;
        }
    }
}