package com.trxsh.anticheat.Combat;

import com.trxsh.anticheat.Checks.CheckResult;
import com.trxsh.anticheat.Checks.CheckType;
import com.trxsh.anticheat.utils.Distance;
import com.trxsh.anticheat.utils.Prefix;
import com.trxsh.anticheat.utils.User;

public class Criticals {

    public static final boolean flagged = false;

    public static CheckResult runChecks(User user, Distance distance) {

        if(flagged) {
            return new CheckResult(true, CheckType.CRITICALS, "player critted entity whilst on ground");
        }

        return Prefix.getPass(CheckType.CRITICALS);
    }
}//TODO: put this to use
