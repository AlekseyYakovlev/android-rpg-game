package com.geek.rpg.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FlameXander on 16.11.2017.
 */

public class SpecialFXEmitter {
    private SpecialFX[] fxs;

    private HashMap<String, TextureRegion[]> regions;

    public SpecialFXEmitter() {
        this.fxs = new SpecialFX[100];
        for (int i = 0; i < fxs.length; i++) {
            this.fxs[i] = new SpecialFX();
        }
        regions = new HashMap<String, TextureRegion[]>();
        addNewEffect("explosion64");
        addNewEffect("arrow");
    }

    public void addNewEffect(String name) {
        TextureRegion[][] tr = new TextureRegion(Assets.getInstance().getAtlas().findRegion(name)).split(64, 64);

        TextureRegion[] newEffect = new TextureRegion[tr.length * tr[0].length];
        int counter = 0;
        for (int i = 0; i < tr.length; i++) {
            for (int j = 0; j < tr[0].length; j++) {
                newEffect[counter] = tr[i][j];
                counter++;
            }
        }
        regions.put(name, newEffect);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < fxs.length; i++) {
            if (fxs[i].isActive()) {
                fxs[i].render(batch);
            }
        }
    }

    public void setup(String fxName, Unit me, Unit target, float maxTime, float scaleFrom, float scaleTo, float delay, boolean oneCycle) {
        for (int i = 0; i < fxs.length; i++) {
            if (!fxs[i].isActive()) {
                fxs[i].setup(regions.get(fxName), me.getPosition().x + 45, me.getPosition().y + 75,
                        target.getPosition().x + 45, target.getPosition().y + 75,
                        maxTime, scaleFrom, scaleTo, delay, oneCycle);
                break;
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < fxs.length; i++) {
            if (fxs[i].isActive()) {
                fxs[i].update(dt);
            }
        }
    }
}
