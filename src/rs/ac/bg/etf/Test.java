package rs.ac.bg.etf;

import rs.ac.bg.etf.automaton.Automaton;
import rs.ac.bg.etf.parser.CisPenn2011.CISPENN2011_Parser;
import rs.ac.bg.etf.parser.Parser;
import rs.ac.bg.etf.predictor.Instruction;
import rs.ac.bg.etf.predictor.Predictor;
import rs.ac.bg.etf.predictor.bimodal.Bimodal;
import rs.ac.bg.etf.predictor.bimodal.MyBimodal;
import rs.ac.bg.etf.predictor.correlation.Correlation;
import rs.ac.bg.etf.predictor.correlation.MyCorrelation;
import rs.ac.bg.etf.predictor.gshare.GShare;
import rs.ac.bg.etf.predictor.staticB.StaticBNT;
import rs.ac.bg.etf.predictor.staticB.StaticBT;
import rs.ac.bg.etf.predictor.tournament.Tournament;
import rs.ac.bg.etf.predictor.twoLevel.MyTwoLevel;
import rs.ac.bg.etf.predictor.twoLevel.TwoLevel;
import rs.ac.bg.etf.stats.Statistics;

public class Test {

    public static void main(String[] args) {
        String pathToTrace = args.length > 0 ? args[0] : "./traces/gcc-10M.trace.gz";
        Parser parcer = new CISPENN2011_Parser(pathToTrace);

        Statistics stats = new Statistics();

        // Static predictors
//        Predictor predictor = new StaticBNT();
//        Predictor predictor = new StaticBT();

        // Three bit predictors
//		Predictor predictor = new MyTwoLevel(10, Automaton.AutomatonType.THREEBITS_TYPE1);
//        Predictor predictor = new MyCorrelation(10, 3, Automaton.AutomatonType.THREEBITS_TYPE1);
//        Predictor predictor = new MyBimodal(10, 5, 10, 10, Automaton.AutomatonType.THREEBITS_TYPE1);
//        Predictor predictor = new GShare(10, 5, Automaton.AutomatonType.THREEBITS_TYPE1);
//        Predictor predictor = new Tournament(10, 5, 10, 10, Automaton.AutomatonType.THREEBITS_TYPE1);

        // Two bit predictors
//        Predictor predictor = new MyTwoLevel(10, Automaton.AutomatonType.TWOBITS_TYPE1);
//        Predictor predictor = new MyCorrelation(10, 3, Automaton.AutomatonType.TWOBITS_TYPE1);
//        Predictor predictor = new MyBimodal(10, 5, 10, 10, Automaton.AutomatonType.TWOBITS_TYPE1);
//        Predictor predictor = new GShare(10, 5, Automaton.AutomatonType.TWOBITS_TYPE1);
        Predictor predictor = new Tournament(10, 5, 10, 10, Automaton.AutomatonType.TWOBITS_TYPE1);

        // Reference predictors
//        Predictor predictor = new TwoLevel(10, Automaton.AutomatonType.TWOBITS_TYPE1);
//        Predictor predictor = new Correlation(10, 3, Automaton.AutomatonType.TWOBITS_TYPE1);
//		Predictor predictor = new Bimodal(10,5,6, Automaton.AutomatonType.TWOBITS_TYPE1);

        long start = System.currentTimeMillis();
        System.out.println("Start!");

        Instruction ins;
        int all = 0;
        while ((ins = parcer.getNext()) != null) {
            all++;
//			if (!ins.isConditional()) {
//				continue;
//			}
            if (!ins.isBranch())
                continue;
            boolean prediction = predictor.predict(ins);
            if (prediction != ins.isTaken())
                stats.incNumOfMisses();
            else
                stats.incNumOfHits();
            stats.incNumOfCondBranches();

            predictor.update(ins);
        }

        long end = System.currentTimeMillis();
        System.out.println("End!");

        long durationInMillis = end - start;
        long millis = durationInMillis % 1000;
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        String time = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);

        System.out.println("Duration: " + time);
        System.out.println("Hits: " + stats.getNumOfHits());
        System.out.println("Misses: " + stats.getNumOfMisses());
        double percent = (double) stats.getNumOfHits() / stats.getNumOfCondBranches() * 100;
        System.out.println("Percent of hits: " + percent);
        System.out.println("Sum: " + stats.getNumOfCondBranches());
        int sum = stats.getNumOfHits() + stats.getNumOfMisses();
        System.out.println("Sum check: " + sum);
        System.out.println("All: " + all);

    }

}
