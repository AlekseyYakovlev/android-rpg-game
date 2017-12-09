package com.geek.rpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.geek.rpg.game.actions.BaseAction;
import com.geek.rpg.game.effects.Effect;
import com.geek.rpg.game.screens.BattleScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable {
    public enum AnimationType {
        IDLE(0), ATTACK(1);

        int number;

        AnimationType(int number) {
            this.number = number;
        }
    }

    private Hero hero;
    transient private BattleScreen battleScreen;
    transient private Unit target;
    transient private TextureRegion texture;
    transient private TextureRegion textureHpBar;
    transient private TextureRegion[] textureInfo;
    transient private static TextureRegion blood;
    {
        blood=Assets.getInstance().getAtlas().findRegion("blood");
    }
    private int hp;
    private int maxHp;
    private int level;
    private Rectangle rect;
    private boolean autopiloted;
    transient private Autopilot autopilot;
    private Vector2 position;
    private boolean flip;
    private float attackAction;
    private float takeDamageAction;
    private Stats stats;
    private List<Effect> effects;
    transient private List<BaseAction> actions;
    private UnitFactory.UnitType type;

    transient private TextureRegion[][] frames;
    private AnimationType currentAnimation;
    private float animationTime;
    private float animationSpeed;
    private int maxFrame;
    private int maxAnimationType;
    private int animationFrame;

    private int width;
    private int height;

    // region Getters/Setters
    public void setCurrentAnimation(AnimationType currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public boolean isAutopiloted() {
        return autopiloted;
    }

    public void setAutopiloted(boolean autopiloted) {
        this.autopiloted = autopiloted;
    }

    public Autopilot getAutopilot() {
        return autopilot;
    }

    public void setAutopilot(Autopilot autopilot) {
        this.autopilot = autopilot;
    }

    public boolean isAutoPilot() {
        return autopilot != null;
    }

    public boolean isMyTeammate(Unit another) {
        return this.hero == another.hero;
    }

    public Stats getStats() {
        return stats;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public void setAttackAction(float attackAction) {
        this.attackAction = attackAction;
    }

    public Unit getTarget() {
        return target;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public BattleScreen getBattleScreen() {
        return battleScreen;
    }

    public List<BaseAction> getActions() {
        return actions;
    }

    public int getLevel() {
        return level;
    }

    public void setActions(List<BaseAction> actions) {
        this.actions = actions;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setTarget(Unit target) {
        this.target = target;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public UnitFactory.UnitType getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    // endregion

    public Unit(UnitFactory.UnitType type, TextureRegion texture, Stats stats, int width, int height) {
        this.texture = texture;
        this.type = type;
        this.effects = new ArrayList<Effect>();
        this.position = new Vector2(0, 0);
        this.actions = new ArrayList<BaseAction>();
        this.stats = stats;
        this.textureHpBar = Assets.getInstance().getAtlas().findRegion("hpBar");
        this.textureInfo = Assets.getInstance().getAtlas().findRegion("unitInfo").split(200, 200)[0];
        this.animationSpeed = 0.2f;
        this.width = width;
        this.height = height;
        this.frames = this.texture.split(width, height);
        this.maxFrame = this.frames[0].length;
        this.maxAnimationType = this.frames.length - 1;
        this.currentAnimation = AnimationType.IDLE;

    }

    public void reload(TextureRegion texture, List<BaseAction> actions, int width, int height) {
        this.textureHpBar = Assets.getInstance().getAtlas().findRegion("hpBar");
        this.textureInfo = Assets.getInstance().getAtlas().findRegion("unitInfo").split(200, 200)[0];
        this.texture = texture;
        this.frames = this.texture.split(width, height);
        this.actions = actions;
        this.effects.clear();
        this.animationSpeed = 0.2f;
        this.maxFrame = this.frames[0].length;
        this.maxAnimationType = this.frames.length - 1;
        this.currentAnimation = AnimationType.IDLE;
    }

    public void recalculateSecondaryStats() {
        this.maxHp = 5 * stats.getEndurance();
        this.hp = this.maxHp;
    }

    public void setToMap(BattleScreen battleScreen, int cellX, int cellY) {
        this.battleScreen = battleScreen;
        this.position.set(battleScreen.getStayPoints()[cellX][cellY]);
        this.rect = new Rectangle(position.x, position.y, width, height);
    }

    public void evade() {
        battleScreen.getInfoSystem().addMessage("MISS", this, FlyingText.Colors.WHITE);
    }

    public void render(SpriteBatch batch) {
        if (takeDamageAction > 0) {
            batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
        }
        float dx = (50f * (float) Math.sin((1f - attackAction) * 3.14f));
        if (flip) dx *= -1;
        float rotation = 0;
        if (!isAlive()) {
            rotation=flip?270:90;
            //rotation=extraBig?180:rotation; // Для василиска
            batch.draw(blood,position.x + dx -50, position.y+5);
        }



        int n = currentAnimation.number;
        if (n > maxAnimationType) {
            n = 0;
        }
        if (flip) {
            frames[n][animationFrame].flip(true, false);
        }
        batch.draw(frames[n][animationFrame], position.x + dx, position.y, width / 2, height/2, width, height, 1, 1, rotation);
        if (flip) {
            frames[n][animationFrame].flip(true, false);
        }
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void renderInfo(SpriteBatch batch, BitmapFont font) {
        batch.setColor(0.5f, 0, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130);
        batch.setColor(0, 1, 0, 1);
        batch.draw(textureHpBar, position.x, position.y + 130, (int) ((float) hp / (float) maxHp * textureHpBar.getRegionWidth()), 20);
        batch.setColor(1, 1, 1, 1);
        font.draw(batch, String.valueOf(hp), position.x, position.y + 149, 90, 1, false);
    }

    public void renderInfoPanel(SpriteBatch batch, BitmapFont font24, BitmapFont font) {
        batch.draw(textureInfo[0], 20, 20, 200, 200);
        if(flip) frames[0][0].flip(true,false);
        font24.draw(batch, type.toString(), 20, 210, 200, 1, false);
        batch.draw(frames[0][0], 25, 75);
        if(flip) frames[0][0].flip(true,false);
        batch.draw(textureInfo[1], 20, 20, 200, 200);


        font.draw(batch, "Str: "+getStats().getStrength(), 125, 185, 125, -1, false);
        font.draw(batch, "Dex: "+getStats().getDexterity(), 125, 165, 125, -1, false);
        font.draw(batch, "End: "+getStats().getEndurance(), 125, 145, 125, -1, false);
        font.draw(batch, "SP: "+getStats().getSpellpower(), 125, 125, 125, -1, false);
        font.draw(batch, "Def: "+getStats().getDefence(), 125, 105, 125, -1, false);

        font.draw(batch, "Hp: "+hp+"/"+maxHp, 80, 85, 190, -1, false);

    }

    public void update(float dt) {
        animationTime += dt;
        animationFrame = (int) (animationTime / animationSpeed);
        animationFrame = animationFrame % maxFrame;
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
            if(takeDamageAction <= 0) {
                currentAnimation = AnimationType.IDLE;
            }
        }
        if (attackAction > 0) {
            attackAction -= dt;
            if(attackAction <= 0) {
                currentAnimation = AnimationType.IDLE;
            }
        }
    }

    public void getTurn() {
        target = null;
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).tick();
            if (effects.get(i).isEnded()) {
                effects.get(i).end();
                effects.remove(i);
            }
        }
    }

    public void changeHp(int value) {
        int prevHp = hp;
        hp += value;
        if (hp > maxHp) {
            hp = maxHp;
        }
        if (hp < 0) {
            hp = 0;
        }
        if (value > 0) {
            battleScreen.getInfoSystem().addMessage("HP +" + (hp - prevHp), this, FlyingText.Colors.GREEN);
        } else if (value < 0) {
            takeDamageAction = 1.0f;
            battleScreen.getInfoSystem().addMessage("HP " + -(prevHp - hp), this, FlyingText.Colors.RED);
        } else {
            battleScreen.getInfoSystem().addMessage("0", this, FlyingText.Colors.WHITE);
        }
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public void setLevelTo(int newLevel) {
        this.level = newLevel;
        this.stats.recalculate(newLevel);
        this.recalculateSecondaryStats();
    }
}