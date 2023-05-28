package object;

import exceptions.SpellLevelException;

public abstract class Spell extends Item{

    private int spellLevel;

    public Spell(String name, int spellLevel) throws SpellLevelException {
        super(name, ItemType.MAGIC);

        if (spellLevel > 10 || spellLevel < 1){
            throw new SpellLevelException("Spell level must be 1-9");
        }

        this.spellLevel = spellLevel;
    }

    public int getSpellLevel() {
        return spellLevel;
    }

    public abstract int getDamage();
}
