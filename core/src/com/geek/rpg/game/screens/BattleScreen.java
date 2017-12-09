package com.geek.rpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geek.rpg.game.ActionPanel;
import com.geek.rpg.game.Assets;
import com.geek.rpg.game.GameSession;
import com.geek.rpg.game.Hero;
import com.geek.rpg.game.InfoSystem;
import com.geek.rpg.game.MyInputProcessor;
import com.geek.rpg.game.SpecialFXEmitter;
import com.geek.rpg.game.Unit;
import com.geek.rpg.game.UnitFactory;
import com.geek.rpg.game.actions.BaseAction;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private ActionPanel actionPanel;
    private SpriteBatch batch;
    private BitmapFont font24;
    private BitmapFont font16;
    private List<Unit> units;
    private int currentUnitIndex;
    private Unit currentUnit;
    private TextureRegion textureSelector;
    private Texture textureBackground;
    private InfoSystem infoSystem;
    private UnitFactory unitFactory;
    private Vector2[][] stayPoints;
    private float animationTimer;
    private Stage stage;
    private Skin skin;
    private MyInputProcessor mip;
    private SpecialFXEmitter specialFXEmitter;

    public SpecialFXEmitter getSpecialFXEmitter() {
        return specialFXEmitter;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public Vector2[][] getStayPoints() {
        return stayPoints;
    }

    public boolean canIMakeTurn() {
        return animationTimer <= 0.0f;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public BattleScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        final int LEFT_STAYPOINT_X = 280;
        final int TOP_STAYPOINT_Y = 400;
        final int DISTANCE_BETWEEN_UNITS_X = 160;
        final int DISTANCE_BETWEEN_UNITS_Y = 120;
        final int DISTANCE_BETWEEN_TEAMS = 100;
        actionPanel = new ActionPanel();
        stayPoints = new Vector2[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = LEFT_STAYPOINT_X + i * DISTANCE_BETWEEN_UNITS_X + j * 20;
                if (i > 1) x += DISTANCE_BETWEEN_TEAMS;
                stayPoints[i][j] = new Vector2(x, TOP_STAYPOINT_Y - j * DISTANCE_BETWEEN_UNITS_Y);
            }
        }
        Hero player1 = GameSession.getInstance().getPlayer();
        Hero player2 = GameSession.getInstance().getEnemy();
        unitFactory = new UnitFactory();

        units = new ArrayList<Unit>();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (player1.getUnits()[i][j] != null) {
                    unitFactory.reloadUnit(player1.getUnits()[i][j]);
                    player1.getUnits()[i][j].setToMap(this, i, j);
                    units.add(player1.getUnits()[i][j]);
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (player2.getUnits()[i][j] != null) {
                    unitFactory.reloadUnit(player2.getUnits()[i][j]);
                    player2.getUnits()[i][j].setToMap(this, i + 2, j);
                    units.add(player2.getUnits()[i][j]);
                }
            }
        }

        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        textureBackground = Assets.getInstance().getAssetManager().get("background.png", Texture.class);
        font24 = Assets.getInstance().getAssetManager().get("zorque24.ttf", BitmapFont.class);
        font16 = Assets.getInstance().getAssetManager().get("zorque16.ttf", BitmapFont.class);

        infoSystem = new InfoSystem();
        textureSelector = Assets.getInstance().getAtlas().findRegion("selector");

//        currentUnitIndex = 0;
        currentUnitIndex = GameSession.getInstance().getCurrentUnit();
        currentUnit = units.get(currentUnitIndex);
        specialFXEmitter = new SpecialFXEmitter();
        createGUI();
        InputMultiplexer im = new InputMultiplexer(stage, mip);
        Gdx.input.setInputProcessor(im);
        animationTimer = 0.0f;
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        skin = new Skin(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("menuBtn");
        textButtonStyle.font = font24;
        skin.add("tbs", textButtonStyle);

        Button btnMenu = new TextButton("MENU", skin, "tbs");
        btnMenu.setPosition(1000, 600);
        stage.addActor(btnMenu);
        btnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSession.getInstance().saveSession(currentUnitIndex);
                ScreenManager.getInstance().switchScreen(ScreenManager.ScreenType.MENU);
            }
        });

        List<BaseAction> list = unitFactory.getActions();
        for (BaseAction o : list) {
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(o.getTextureName());
            skin.add(o.getName(), buttonStyle);
        }

        actionPanel.build(this, skin, unitFactory.getActions());
        stage.addActor(actionPanel);
        actionPanel.show(currentUnit);
    }

    public boolean isHeroTurn() {
        return currentUnit.getAutopilot() == null;
    }

    public void nextTurn() {
        do {
            currentUnitIndex++;
            if (currentUnitIndex >= units.size()) {
                currentUnitIndex = 0;
            }
        } while (!units.get(currentUnitIndex).isAlive());
        currentUnit = units.get(currentUnitIndex);
        currentUnit.getTurn();
        if (!currentUnit.isAutopiloted()) {
            actionPanel.show(currentUnit);
        } else {
            actionPanel.hide();
        }
        animationTimer = 1.0f;
    }

    public void update(float dt) {
        if (isHeroTurn() && canIMakeTurn()) {
            stage.act(dt);
        }
        if (animationTimer > 0.0f) {
            animationTimer -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (mip.isTouchedInArea(units.get(i).getRect()) && units.get(i).isAlive()) {
                currentUnit.setTarget(units.get(i));
            }
        }
        if (!isHeroTurn()) {
            if (currentUnit.getAutopilot().turn(currentUnit)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);
        specialFXEmitter.update(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(textureBackground, 0, 0);
        batch.setColor(1, 1, 0, 0.8f);
        batch.draw(textureSelector, currentUnit.getPosition().x, currentUnit.getPosition().y - 5);
        if (isHeroTurn() && currentUnit.getTarget() != null) {
            batch.setColor(1, 0, 0, 0.8f);
            batch.draw(textureSelector, currentUnit.getTarget().getPosition().x, currentUnit.getTarget().getPosition().y - 5);
        }
        batch.setColor(1, 1, 1, 1);
        for (int i = 0; i < units.size(); i++) {
            units.get(i).render(batch);
            if (units.get(i).isAlive()) {
                units.get(i).renderInfo(batch, font24);
            }
        }

        if (currentUnit.getTarget() != null) {
            currentUnit.getTarget().renderInfoPanel(batch, font24,font16);
        }

        infoSystem.render(batch, font24);
        specialFXEmitter.render(batch);
        batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}