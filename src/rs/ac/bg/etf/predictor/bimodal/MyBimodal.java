package rs.ac.bg.etf.predictor.bimodal;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.predictor.BHR;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

import static java.lang.Math.min;
import static sun.swing.MenuItemLayoutHelper.max;

public class MyBimodal implements Predictor {

    private BHR bhr;

    private int bhrBits;

    private int addrMask;

    private int[] history;

    private int historySize;

    private int selectorMask;

    private Automaton[][] pht;

    public MyBimodal(int bhrBits, int addrBits, int selectorBits, int historySize, Automaton.AutomatonType type) {
        bhr = new BHR(bhrBits);
        this.bhrBits = bhrBits;
        this.addrMask = (1 << max(bhrBits, addrBits)) - 1;

        history = new int[(1 << selectorBits)];

        this.selectorMask = (1 << selectorBits) - 1;

        this.historySize = historySize;

        pht = new Automaton[2][];

        pht[0] = Automaton.instanceArray(type, (1 << max(addrBits, bhrBits)));
        pht[1] = Automaton.instanceArray(type, (1 << max(addrBits, bhrBits)));
    }

    @Override
    public boolean predict(Instruction branch) {
        int pEntry = (int) ((bhr.getValue() ^ branch.getAddress()) & addrMask);
        int sEntry = (int) (branch.getAddress() & selectorMask);
        int tEntry = history[sEntry] < 0 ? 0 : 1; // last selected entry

        return pht[tEntry][pEntry].predict();
    }

    @Override
    public void update(Instruction branch) {
        int pEntry = (int) ((bhr.getValue() ^ branch.getAddress()) & addrMask);
        int sEntry = (int) (branch.getAddress() & selectorMask);
        int tEntry = history[sEntry] < 0 ? 0 : 1; // last selected entry

        boolean outcome = branch.isTaken();

        // update history
        if (!outcome) {
            history[sEntry] = max(history[sEntry] - 1, -historySize); // -2 -1
        } else {
            // taken
            history[sEntry] = min(history[sEntry] + 1, historySize - 1); // 0, 1
        }

        // update bhr and automaton
        bhr.insertOutcome(outcome);
        pht[tEntry][pEntry].updateAutomaton(outcome);
    }
}
