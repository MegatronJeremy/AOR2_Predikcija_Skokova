package rs.ac.bg.etf.predictor.twoLevel;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class MyTwoLevel implements Predictor {

    private int bhr;
    private final int mask;

    private final Automaton[] pht;

    public MyTwoLevel(int bhrBits, Automaton.AutomatonType type) {
        int bhrSize = (1 << bhrBits);

        pht = Automaton.instanceArray(type, bhrSize);

        mask = bhrSize - 1;
    }


    @Override
    public boolean predict(Instruction branch) {
        return pht[bhr].predict();
    }

    @Override
    public void update(Instruction branch) {
        boolean outcome = branch.isTaken();
        pht[bhr].updateAutomaton(outcome);

        bhr = ((bhr << 1) | (outcome ? 1 : 0)) & mask;
    }
}
