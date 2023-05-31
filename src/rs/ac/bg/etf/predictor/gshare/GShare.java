package rs.ac.bg.etf.predictor.gshare;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class GShare implements Predictor {
    private BHR bhr;

    private int addrMask;

    private Automaton[] pht;

    public GShare(int bhrBits, int addrBits, Automaton.AutomatonType type) {
        int bits = Math.max(bhrBits, addrBits);

        addrMask = (1 << bits) - 1;

        bhr = new BHR(bhrBits);

        pht = Automaton.instanceArray(type, (1 << bits));
    }

    @Override
    public boolean predict(Instruction branch) {
        int entry = (int) ((branch.getAddress() ^ bhr.getValue()) & addrMask);

        return pht[entry].predict();
    }

    @Override
    public void update(Instruction branch) {
        int entry = (int) ((branch.getAddress() ^ bhr.getValue()) & addrMask);

        boolean outcome = branch.isTaken();
        bhr.insertOutcome(outcome);
        pht[entry].updateAutomaton(outcome);
    }
}
