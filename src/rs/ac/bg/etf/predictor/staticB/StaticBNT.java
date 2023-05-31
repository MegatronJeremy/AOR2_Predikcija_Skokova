package rs.ac.bg.etf.predictor.staticB;

import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class StaticBNT implements Predictor {
    @Override
    public boolean predict(Instruction branch) {
        return false;
    }

    @Override
    public void update(Instruction branch) {
        // do nothing
    }
}
