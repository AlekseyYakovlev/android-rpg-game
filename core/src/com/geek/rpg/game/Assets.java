package com.geek.rpg.game;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

/**
 * Created by FlameXander on 13.11.2017.
 */

public class Assets {
    private static final Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;
    private TextureAtlas textureAtlas;

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(com.geek.rpg.game.screens.ScreenManager.ScreenType type) {
        switch (type) {
            case MENU:
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("background.png", Texture.class);
                assetManager.load("menuBtn.png", Texture.class);
                //assetManager.load("blood.png", Texture.class);

                FileHandleResolver resolver = new InternalFileHandleResolver();
                assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
                assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
                FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameters32 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                fontParameters32.fontFileName = "zorque.ttf";
                fontParameters32.fontParameters.size = 32;
                fontParameters32.fontParameters.shadowOffsetX = -1;
                fontParameters32.fontParameters.shadowOffsetY = 1;
                fontParameters32.fontParameters.borderWidth = 1;
                fontParameters32.fontParameters.borderColor = Color.BLACK;
                assetManager.load("zorque32.ttf", BitmapFont.class, fontParameters32);

                FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameters96 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                fontParameters96.fontFileName = "zorque.ttf";
                fontParameters96.fontParameters.size = 96;
                fontParameters96.fontParameters.shadowOffsetX = -1;
                fontParameters96.fontParameters.shadowOffsetY = 1;
                fontParameters96.fontParameters.borderWidth = 1;
                fontParameters96.fontParameters.borderColor = Color.BLACK;
                assetManager.load("zorque96.ttf", BitmapFont.class, fontParameters96);
                break;
            case BATTLE:
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("background.png", Texture.class);
                assetManager.load("btnMeleeAttack.png", Texture.class);
                assetManager.load("btnHeal.png", Texture.class);
                assetManager.load("btnDefence.png", Texture.class);
                assetManager.load("actionPanel.png", Texture.class);


                FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameters24 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                fontParameters24.fontFileName = "zorque.ttf";
                fontParameters24.fontParameters.size = 24;
                fontParameters24.fontParameters.shadowOffsetX = -1;
                fontParameters24.fontParameters.shadowOffsetY = 1;
                fontParameters24.fontParameters.borderWidth = 1;
                fontParameters24.fontParameters.borderColor = Color.BLACK;
                assetManager.load("zorque24.ttf", BitmapFont.class, fontParameters24);

                FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameters16 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
                fontParameters16.fontFileName = "zorque.ttf";
                fontParameters16.fontParameters.size = 16;
                fontParameters16.fontParameters.shadowOffsetX = -1;
                fontParameters16.fontParameters.shadowOffsetY = 1;
                fontParameters16.fontParameters.borderWidth = 1;
                fontParameters16.fontParameters.borderColor = Color.BLACK;
                assetManager.load("zorque16.ttf", BitmapFont.class, fontParameters16);
                break;
        }
    }

    public void makeLinks() {
        textureAtlas = assetManager.get("rpg.pack", TextureAtlas.class);
    }

    public void clear() {
        assetManager.clear();
    }
}
