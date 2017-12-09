package com.geek.rpg.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.geek.rpg.game.Assets;
import com.geek.rpg.game.RpgGame;


public class ScreenManager {
    public enum ScreenType {
        MENU, BATTLE
    }

    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private RpgGame rpgGame;
    private Viewport viewport;
    private BattleScreen battleScreen;
    private LoadingScreen loadingScreen;
    private MenuScreen menuScreen;

    private Screen targetScreen;

    public Viewport getViewport() {
        return viewport;
    }

    public void init(RpgGame rpgGame, SpriteBatch batch) {
        this.rpgGame = rpgGame;
        this.loadingScreen = new com.geek.rpg.game.screens.LoadingScreen(batch);
        this.battleScreen = new BattleScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.viewport = new FitViewport(1280, 720);
        this.viewport.update(1280, 720, true);
        this.viewport.apply();
    }

    public void onResize(int width, int height) {
        viewport.update(width, height, true);
        viewport.apply();
    }

    private ScreenManager() {
    }

    public void switchScreen(ScreenType type) {
        Screen screen = rpgGame.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }
        switch (type) {
            case MENU:
                rpgGame.setScreen(loadingScreen);
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case BATTLE:
                rpgGame.setScreen(loadingScreen);
                targetScreen = battleScreen;
                Assets.getInstance().loadAssets(ScreenType.BATTLE);
                break;
        }
    }

    public void goToTarget() {
        rpgGame.setScreen(targetScreen);
    }

    public void dispose() {
        Assets.getInstance().getAssetManager().dispose();
    }
}
