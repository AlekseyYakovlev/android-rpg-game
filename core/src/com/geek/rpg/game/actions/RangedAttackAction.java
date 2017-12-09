package com.geek.rpg.game.actions;

import com.geek.rpg.game.Calculator;
import com.geek.rpg.game.Unit;

public class RangedAttackAction extends BaseAction {
    public RangedAttackAction() {
        super("RANGED_ATTACK", "btnRanged");
    }

    @Override
    public boolean action(Unit me) {
        if (me.getTarget() == null) return false;
        if (me.isMyTeammate(me.getTarget())) return false;
        me.setAttackAction(1.0f);
        me.setCurrentAnimation(Unit.AnimationType.ATTACK);
        if (!Calculator.isTargetEvaded(me, me.getTarget())) {
            int dmg = Calculator.getRangedDamage(me, me.getTarget());
            me.getTarget().changeHp(-dmg);
        } else {
            me.getTarget().evade();
        }
        me.getBattleScreen().getSpecialFXEmitter().setup("arrow", me, me.getTarget(), 0.5f, 1f, 1f, 0.0f, true);
        return true;
    }
}
