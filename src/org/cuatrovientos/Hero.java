package org.cuatrovientos;

import java.io.Serializable;

public class Hero implements Character, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int enemiesKilled;
    private int friendsDefended;

    public Hero(String name) {
        this.name = name;
        this.enemiesKilled = 0;
        this.friendsDefended = 0;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    public void attack(Enemy enemy) {
        System.out.println("¡He atacado a un enemigo!");
        enemy.kill();
        this.enemiesKilled++;
    }

    public void defend(Friend friend) {
        System.out.println("¡He defendido a un amigo!");
        friend.heal();
        this.friendsDefended++;
    }

    public String getName() {
        return name;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public int getFriendsDefended() {
        return friendsDefended;
    }

    public void printStats() {
        System.out.println("\n--- Estadísticas de " + name + " ---");
        System.out.println("Enemigos eliminados: " + enemiesKilled);
        System.out.println("Amigos defendidos: " + friendsDefended);
    }
}