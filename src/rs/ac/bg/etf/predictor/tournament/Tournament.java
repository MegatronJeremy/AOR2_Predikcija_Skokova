package rs.ac.bg.etf.predictor.tournament;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class Tournament implements Predictor {

    private int addrMask;

    private BHR bhr;
    private int selectorMask;

    private int historySize;

    private int[] history;

    private Automaton[][] pht;

    public Tournament(int bhrBits, int addrBits, int selectorBits, int historySize, Automaton.AutomatonType type) {
        pht = new Automaton[2][];

        addrMask = (1 << addrBits) - 1;
        selectorMask = (1 << selectorBits) - 1;

        bhr = new BHR(bhrBits);

        this.historySize = historySize;

        history = new int[(1 << selectorBits)];

        // BHR
        pht[0] = Automaton.instanceArray(type, (1 << bhrBits));

        // Address
        pht[1] = Automaton.instanceArray(type, (1 << addrBits));
    }

    @Override
    public boolean predict(Instruction branch) {
        int sEntry = (int) (branch.getAddress() & selectorMask);
        int tEntry = history[sEntry] < 0 ? 0 : 1;

        if (tEntry == 0) {
            // BHR
            return pht[tEntry][bhr.getValue()].predict();
        } else {
            int pEntry = (int) (branch.getAddress() & addrMask);
            return pht[tEntry][pEntry].predict();
        }
    }

    @Override
    public void update(Instruction branch) {
        int sEntry = (int) (branch.getAddress() & selectorMask);
        int tEntry = history[sEntry] < 0 ? 0 : 1;

        boolean outcome = branch.isTaken();

        if (outcome) {
            history[sEntry] = Math.min(historySize - 1, history[sEntry] + 1);
        } else {
            history[sEntry] = Math.max(-historySize, history[sEntry] - 1);
        }

        if (tEntry == 0) {
            // BHR
            pht[tEntry][bhr.getValue()].updateAutomaton(outcome);
            bhr.insertOutcome(outcome);
        } else {
            // addr
            int pEntry = (int) (branch.getAddress() & addrMask);
            pht[tEntry][pEntry].updateAutomaton(outcome);
        }
    }
}
