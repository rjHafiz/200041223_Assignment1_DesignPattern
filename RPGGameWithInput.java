import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface AttackStrategy {
    void executeAttack();
}

class MeleeAttack implements AttackStrategy {
    @Override
    public void executeAttack() {
        System.out.println("Performing melee attack with weapon!");
    }
}

class MagicAttack implements AttackStrategy {
    @Override
    public void executeAttack() {
        System.out.println("Casting a powerful magic spell!");
    }
}


interface AttackEnhancer extends AttackStrategy {
    String getDescription();
}


class BasicAttack implements AttackEnhancer {
    private AttackStrategy strategy;

    public BasicAttack(AttackStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void executeAttack() {
        strategy.executeAttack();
    }

    @Override
    public String getDescription() {
        return "Basic Attack";
    }
}


abstract class AttackDecorator implements AttackEnhancer {
    protected AttackEnhancer enhancedAttack;

    public AttackDecorator(AttackEnhancer enhancedAttack) {
        this.enhancedAttack = enhancedAttack;
    }

    @Override
    public void executeAttack() {
        enhancedAttack.executeAttack();
    }

    @Override
    public String getDescription() {
        return enhancedAttack.getDescription();
    }
}


class FireDamage extends AttackDecorator {
    public FireDamage(AttackEnhancer enhancedAttack) {
        super(enhancedAttack);
    }

    @Override
    public void executeAttack() {
        super.executeAttack();
        System.out.println("Adding fire damage to the attack!");
    }

    @Override
    public String getDescription() {
        return enhancedAttack.getDescription() + ", Fire Damage";
    }
}


class PoisonDamage extends AttackDecorator {
    public PoisonDamage(AttackEnhancer enhancedAttack) {
        super(enhancedAttack);
    }

    @Override
    public void executeAttack() {
        super.executeAttack();
        System.out.println("Adding poison damage to the attack!");
    }

    @Override
    public String getDescription() {
        return enhancedAttack.getDescription() + ", Poison Damage";
    }
}


abstract class Character {
    private AttackEnhancer attackEnhancer;
    private List<String> actions = new ArrayList<>();

    public final void prepareForCombat() {
        equipArmor();
        train();
        selectWeapon();
        configureAttackStrategy();
        logPreparation();
    }

    abstract void equipArmor();
    abstract void train();
    abstract void selectWeapon();

    void configureAttackStrategy() {
        setAttackStrategy(new MeleeAttack()); 
    }

    public void setAttackStrategy(AttackStrategy strategy) {
        this.attackEnhancer = new BasicAttack(strategy);
    }


    public void addAttackEnhancer(String enhancerType) {
        if (enhancerType.equalsIgnoreCase("fire")) {
            this.attackEnhancer = new FireDamage(this.attackEnhancer);
        } else if (enhancerType.equalsIgnoreCase("poison")) {
            this.attackEnhancer = new PoisonDamage(this.attackEnhancer);
        }
    }

    public void performAttack() {
        System.out.println("Attack: " + attackEnhancer.getDescription());
        attackEnhancer.executeAttack();
    }

  
    protected void addAction(String action) {
        actions.add(action);
    }

    private void logPreparation() {
        System.out.println("Preparation complete for " + this.getClass().getSimpleName() + ":");
        actions.forEach(System.out::println);
    }
}


class Knight extends Character {
    @Override
    void equipArmor() {
        addAction("Knight equips plate armor.");
    }

    @Override
    void train() {
        addAction("Knight trains in swordsmanship.");
    }

    @Override
    void selectWeapon() {
        addAction("Knight selects a broadsword.");
    }

    @Override
    void configureAttackStrategy() {
        setAttackStrategy(new MeleeAttack());
    }
}


class Sorcerer extends Character {
    @Override
    void equipArmor() {
        addAction("Sorcerer equips enchanted robes.");
    }

    @Override
    void train() {
        addAction("Sorcerer studies arcane spells.");
    }

    @Override
    void selectWeapon() {
        addAction("Sorcerer selects a magic staff.");
    }

    @Override
    void configureAttackStrategy() {
        setAttackStrategy(new MagicAttack());
    }
}


class CharacterFactory {
    public static Character createCharacter(String type) {
        switch (type.toLowerCase()) {
            case "1":
                return new Knight();
            case "2":
                return new Sorcerer();
            default:
                throw new IllegalArgumentException("Invalid input: " + type);
        }
    }
}


public class RPGGameWithInput {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

       
        System.out.println("Choose your character:");
        System.out.println("1. Knight");
        System.out.println("2. Sorcerer");
        System.out.print("Enter choice (1 or 2): ");
        String characterChoice = scanner.nextLine();
        Character character;
        try {
            character = CharacterFactory.createCharacter(characterChoice);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            scanner.close();
            return;
        }

      
        System.out.println("\nChoose your attack strategy:");
        System.out.println("1. Melee Attack");
        System.out.println("2. Magic Attack");
        System.out.print("Enter choice (1 or 2): ");
        String strategyChoice = scanner.nextLine();
        try {
            if (strategyChoice.equals("1")) {
                character.setAttackStrategy(new MeleeAttack());
            } else if (strategyChoice.equals("2")) {
                character.setAttackStrategy(new MagicAttack());
            } else {
                throw new IllegalArgumentException("Invalid attack strategy choice: " + strategyChoice);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
            scanner.close();
            return;
        }

       
        while (true) {
            System.out.println("\nAdd an attack enhancer:");
            System.out.println("1. Fire Damage");
            System.out.println("2. Poison Damage");
            System.out.println("0. None (proceed to combat)");
            System.out.print("Enter choice (0, 1, or 2): ");
            String enhancerChoice = scanner.nextLine();
            if (enhancerChoice.equals("0")) {
                break;
            }
            try {
                if (enhancerChoice.equals("1")) {
                    character.addAttackEnhancer("fire");
                } else if (enhancerChoice.equals("2")) {
                    character.addAttackEnhancer("poison");
                } else {
                    throw new IllegalArgumentException("Invalid enhancer choice: " + enhancerChoice);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("\n=== Preparing for Combat ===");
        character.prepareForCombat();

        System.out.println("\n=== Performing Attack ===");
        character.performAttack();

        scanner.close();
    }
}