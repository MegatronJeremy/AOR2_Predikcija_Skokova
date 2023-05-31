package rs.ac.bg.etf.automaton;

public class Automaton3bits_1 implements Automaton {
    private int state = 0;

    @Override
    public void updateAutomaton(boolean outcome) {
        if (state > 0 && !outcome) {
            state--;
        } else if (state < 7 && outcome) {
            state++;
        }
    }

    @Override
    public boolean predict() {
        return state >= 4; // 0,1,2,3 -> not taken
    }

}
