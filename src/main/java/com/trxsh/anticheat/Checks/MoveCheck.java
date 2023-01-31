package com.trxsh.anticheat.Checks;

import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.User;

public abstract class MoveCheck extends Check {

    public MoveCheck(CheckType type) {
        super(type);
    }

    public abstract CheckResult runCheck(User user, Distance distance);
}