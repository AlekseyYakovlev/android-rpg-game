package com.geek.rpg.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.actions.*;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.screens.BattleScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FlameXander on 23.11.2017.
 */

public class ActionPanel extends Group {
    private BattleScreen battleScreen;
    private Unit currentUnit;
    private List<Button> buttons;
    private List<String> actionNames;

    public ActionPanel() {
        buttons = new ArrayList<Button>();
        actionNames = new ArrayList<String>();
    }

    public void build(BattleScreen _battleScreen, Skin skin, List<BaseAction> actions) {
        buttons.clear();
        actionNames.clear();
        battleScreen = _battleScreen;
        Image image = new Image(Assets.getInstance().getAtlas().findRegion("actionPanel"));
        addActor(image);
        setPosition(1280 / 2 - 840 / 2, 5);
        for (BaseAction a : actions) {
            final BaseAction ba = a;
            Button btn = new Button(skin, a.getName());
            btn.setPosition(30 + buttons.size() * 100, 30);
            btn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (ba.action(currentUnit)) {
                        battleScreen.nextTurn();
                    }
                }
            });
            actionNames.add(a.getName());
            buttons.add(btn);
            addActor(btn);
        }
    }

    public void hide() {
        setVisible(false);
    }

    public void show(Unit unit) {
        currentUnit = unit;
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisible(false);
        }
        for (int i = 0; i < unit.getActions().size(); i++) {
            for (int j = 0; j < buttons.size(); j++) {
                if (unit.getActions().get(i).getName().equals(actionNames.get(j))) {
                    buttons.get(j).setVisible(true);
                    buttons.get(j).setPosition(30 + i * 100, 30);
                    break;
                }
            }
        }
        setVisible(true);
    }
}
