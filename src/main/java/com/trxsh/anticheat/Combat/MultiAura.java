package com.trxsh.anticheat.Combat;

import org.bukkit.entity.Entity;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.Settings;
import com.trxsh.anticheat.utils.User;

public class MultiAura {

    public static CheckResult runCheck(User user, Entity entity) {
        user.addEntity(entity.getEntityId());
        int entities = user.getEntities();
        return entities > Settings.MAX_ENTITIES ? new CheckResult(true, CheckType.MULTIAURA, "tried to hit: " + entities + " different entities max(" + Settings.MAX_ENTITIES + ")") : Prefix.getPass(CheckType.MULTIAURA);
    }

}