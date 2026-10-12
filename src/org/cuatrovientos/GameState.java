package org.cuatrovientos;

import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Hero hero;
    private ArrayList<Character> characters;

    public GameState(Hero hero, ArrayList<Character> characters) {
        this.hero = hero;
        this.characters = characters;
    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }
}