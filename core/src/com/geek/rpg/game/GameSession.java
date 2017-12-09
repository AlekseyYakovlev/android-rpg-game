package com.geek.rpg.game;

import com.badlogic.gdx.Gdx;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by FlameXander on 20.11.2017.
 */

public class GameSession {
    private static final GameSession ourInstance = new GameSession();

    public static GameSession getInstance() {
        return ourInstance;
    }

    private Hero player;
    private Hero enemy;
    private int currentUnit;

    private GameSession() {
    }

    public int getCurrentUnit() {
        return currentUnit;
    }

    public Hero getPlayer() {
        return player;
    }

    public Hero getEnemy() {
        return enemy;
    }

    public void startNewSession() {
        currentUnit = 0;
        player = new Hero();
        enemy = new Hero();
        makeStandartArmy();
        makeEnemyArmy();
    }

    public void saveSession(int currentUnitIndex) {
        try {
            this.currentUnit = currentUnitIndex;
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Gdx.files.local("mydata.sav").file()));
            oos.writeObject(player);
            oos.writeObject(enemy);
            oos.writeObject(new Integer(currentUnit));
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSession() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Gdx.files.local("mydata.sav").file()));
            this.player = (Hero) ois.readObject();
            this.enemy = (Hero) ois.readObject();
            currentUnit = (Integer)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeStandartArmy() {
        UnitFactory factory = new UnitFactory();
        player.setArmy(
                factory.createUnit(UnitFactory.UnitType.ARCHER, false, false, 1), factory.createUnit(UnitFactory.UnitType.KNIGHT, false, false, 1),
                factory.createUnit(UnitFactory.UnitType.MAGE, false, false, 1), factory.createUnit(UnitFactory.UnitType.SKELETON, false, false, 1),
                null, null
        );
    }

    public void makeEnemyArmy() {
        UnitFactory factory = new UnitFactory();
        enemy.setArmy(
                null, factory.createUnit(UnitFactory.UnitType.ARCHER, true, true, 5),
                factory.createUnit(UnitFactory.UnitType.SKELETON, true, true, 1), factory.createUnit(UnitFactory.UnitType.MAGE, true, true, 2),
                null, null
        );
    }
}
