package org.example.maze.game;

import java.util.ArrayList;
import java.util.List;


public class GameController {

    private final List<Controllable> entities = new ArrayList<>();

    private static int speedPercentToDelay(int pct) {
        pct = Math.max(1, Math.min(100, pct));
        return (int) (2000 - (1990.0 * pct / 100.0));
    }


    public void register(Controllable entity) {
        entities.add(entity);
    }

    public void registerAll(List<? extends Controllable> list) {
        entities.addAll(list);
    }


    public String execute(String rawCommand) {
        if (rawCommand == null) return "";
        String cmd = rawCommand.trim().toLowerCase();
        if (cmd.isEmpty()) return "";

        String[] tokens = rawCommand.trim().split("\\s+");

        switch (tokens[0].toLowerCase()) {

            case "help":
                return buildHelp();

            case "speed": {
                if (tokens.length < 3) return "❌ Usage: speed <all|bunny|robot <name>> <1-100>";
                try {

                    int pct;
                    List<Controllable> targets;

                    if (tokens[1].equalsIgnoreCase("all")) {
                        pct = Integer.parseInt(tokens[2]);
                        targets = allEntities();
                    } else if (tokens[1].equalsIgnoreCase("bunny")) {
                        pct = Integer.parseInt(tokens[2]);
                        targets = findByName("bunny");
                    } else if (tokens[1].equalsIgnoreCase("robot")) {
                        if (tokens.length < 4) return "❌ Usage: speed robot <name> <1-100>";
                        pct = Integer.parseInt(tokens[tokens.length - 1]);
                        String robotName = joinMiddle(tokens, 2, tokens.length - 1);
                        targets = findByName(robotName);
                    } else {
                        return "❌ Target necunoscut: " + tokens[1];
                    }

                    if (targets.isEmpty()) return "❌ Nicio entitate găsită: " + tokens[1];
                    int delay = speedPercentToDelay(pct);
                    targets.forEach(e -> e.setStepDelay(delay));
                    return "✅ Viteză " + pct + "% (" + delay + "ms) aplicată pe: " + names(targets);

                } catch (NumberFormatException ex) {
                    return "❌ Viteza trebuie să fie un număr între 1 și 100.";
                }
            }

            case "pause": {
                if (tokens.length < 2) return "❌ Usage: pause <all|bunny|robot <name>>";
                List<Controllable> targets = resolveTarget(tokens, 1);
                if (targets.isEmpty()) return "❌ Nicio entitate găsită.";
                targets.forEach(Controllable::pause);
                return "⏸ Pausat: " + names(targets);
            }

            case "resume": {
                if (tokens.length < 2) return "❌ Usage: resume <all|bunny|robot <name>>";
                List<Controllable> targets = resolveTarget(tokens, 1);
                if (targets.isEmpty()) return "❌ Nicio entitate găsită.";
                targets.forEach(Controllable::resume);
                return "▶ Reluat: " + names(targets);
            }

            default:
                return "❌ Comandă necunoscută: \"" + tokens[0] + "\". Scrie 'help' pentru ajutor.";
        }
    }


    private List<Controllable> resolveTarget(String[] tokens, int startIndex) {
        if (tokens.length <= startIndex) return List.of();
        String targetType = tokens[startIndex].toLowerCase();
        if (targetType.equals("all"))   return allEntities();
        if (targetType.equals("bunny")) return findByName("bunny");
        if (targetType.equals("robot")) {
            if (tokens.length <= startIndex + 1) return List.of();
            String robotName = joinMiddle(tokens, startIndex + 1, tokens.length);
            return findByName(robotName);
        }
        return List.of();
    }

    private List<Controllable> allEntities() {
        return new ArrayList<>(entities);
    }

    private List<Controllable> findByName(String name) {
        List<Controllable> result = new ArrayList<>();
        for (Controllable e : entities) {
            if (e.getName().equalsIgnoreCase(name)
                    || (name.equalsIgnoreCase("bunny") && e instanceof Bunny)) {
                result.add(e);
            }
        }
        return result;
    }

    private String joinMiddle(String[] tokens, int from, int to) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < to; i++) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(tokens[i]);
        }
        return sb.toString();
    }

    private String names(List<Controllable> list) {
        StringBuilder sb = new StringBuilder();
        for (Controllable e : list) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(e.getName());
        }
        return sb.toString();
    }

    private String buildHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("──── Comenzi disponibile ────\n");
        sb.append("speed  all <1-100>           → viteza tuturor\n");
        sb.append("speed  bunny <1-100>          → viteza iepurelui\n");
        sb.append("speed  robot <Nume> <1-100>   → viteza unui robot\n");
        sb.append("pause  all                    → pauza tuturor\n");
        sb.append("pause  bunny                  → pauza iepurelui\n");
        sb.append("pause  robot <Nume>           → pauza unui robot\n");
        sb.append("resume all                    → reia toți\n");
        sb.append("resume bunny                  → reia iepurele\n");
        sb.append("resume robot <Nume>           → reia un robot\n");
        sb.append("─────────────────────────────\n");
        sb.append("Entități înregistrate: ").append(names(entities));
        return sb.toString();
    }

    public List<Controllable> getEntities() {
        return new ArrayList<>(entities);
    }
}