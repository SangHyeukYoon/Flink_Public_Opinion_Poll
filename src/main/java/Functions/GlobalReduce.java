package Functions;

import domain.opinion.AgeGroup;
import domain.opinion.OpinionUser;
import domain.opinion.Region;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GlobalReduce implements AllWindowFunction<OpinionUser, ArrayList<ArrayList<Integer>>, TimeWindow> {

    @Override
    public void apply(TimeWindow window, Iterable<OpinionUser> values,
                      Collector<ArrayList<ArrayList<Integer>>> out) throws Exception {
        ArrayList<ArrayList<Integer>> sum = new ArrayList<>(3);
        sum.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));
        sum.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));
        sum.add(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));

        for (OpinionUser iter: values) {
            ArrayAdder(sum, iter.questions);
        }

        out.collect(sum);
    }

    public void ArrayAdder(ArrayList<ArrayList<Integer>> acc, ArrayList<ArrayList<Integer>> in) {
        for (int outer = 0; outer < acc.size(); ++outer) {
            ArrayList<Integer> acc_inner = acc.get(outer);
            ArrayList<Integer> in_inner = in.get(outer);

            for (int i = 0; i < acc_inner.size(); ++i) {
                acc_inner.set(i, acc_inner.get(i) + in_inner.get(i));
            }
        }
    }

}
