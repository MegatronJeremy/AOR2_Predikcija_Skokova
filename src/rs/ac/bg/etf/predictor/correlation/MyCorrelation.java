package rs.ac.bg.etf.predictor.correlation;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class MyCorrelation implements Predictor {
    private BHR bhr;

    private int addrMask;

    private Automaton[][] pht;

    public MyCorrelation(int bhrSize, int addrBits, Automaton.AutomatonType type) {
        bhr = new BHR(bhrSize);
        addrMask = (1 << addrBits) - 1;

        pht = new Automaton[1 << bhrSize][];

        for (int i = 0; i < (1 << bhrSize); i++) {
            pht[i] = Automaton.instanceArray(type, (1 << addrBits));
        }
    }

    @Override
    public boolean predict(Instruction branch) {
        return pht[bhr.getValue()][(int) (branch.getAddress() & addrMask)].predict();
    }

    @Override
    public void update(Instruction branch) {
        pht[bhr.getValue()][(int) (branch.getAddress() & addrMask)].updateAutomaton(branch.isTaken());

        bhr.insertOutcome(branch.isTaken());
    }
}
