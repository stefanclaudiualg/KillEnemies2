package org.cuatrovientos;

public class Friend implements Character {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isEnemy() {
        return false;
    }

    public void heal() {
        System.out.println("¡Te he curado!");
    }

    @Override
    public String toString() {
        return "Amigo";
    }
}