package rs.ac.bg.etf.predictor.staticB;

import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;

public class StaticBT implements Predictor {
    @Override
    public boolean predict(Instruction branch) {
        return true;
    }

    @Override
    public void update(Instruction branch) {
        // do nothing
    }
}
