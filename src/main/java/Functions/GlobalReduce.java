package Functions;

import domain.opinion.AgeGroup;
import domain.opinion.Region;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.configuration.Configuration;

import java.io.IOException;
import java.util.ArrayList;

public class GlobalReduce extends RichReduceFunction<Tuple3<AgeGroup, Region, ArrayList<ArrayList<Integer>>>> {

    private ValueState<Integer> count;

    @Override
    public void open(Configuration config) throws IOException {
        count = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("counter", Integer.class));
        count.update(0);
    }

    @Override
    public Tuple3<AgeGroup, Region, ArrayList<ArrayList<Integer>>>
        reduce(Tuple3<AgeGroup, Region, ArrayList<ArrayList<Integer>>> value1,
               Tuple3<AgeGroup, Region, ArrayList<ArrayList<Integer>>> value2) throws Exception {
        return null;
    }

}
