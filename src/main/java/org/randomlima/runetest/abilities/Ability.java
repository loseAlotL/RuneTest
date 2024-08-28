package org.randomlima.runetest.abilities;

import org.randomlima.runetest.abilities.abilityutil.AbilityType;
import org.randomlima.runetest.objects.StaffState;

import java.util.UUID;

public interface Ability {
    void switchState(StaffState staffState);
    String getName();
    String getDisplayName();
    UUID getID();

    AbilityType getAbilityType();
    void boot();
}
