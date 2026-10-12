package org.cuatrovientos;

public class Enemy implements Character {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isEnemy() {
        return true;
    }

    public void kill() {
        System.out.println("Ahhhggg, me mataste, bastardo!");
    }

    @Override
    public String toString() {
        return "Enemigo";
    }
}