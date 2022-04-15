package Functions;

import domain.opinion.OpinionUser;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;

public class OpinionKeySelector implements KeySelector<OpinionUser, Tuple2<Integer, Integer>> {

    @Override
    public Tuple2<Integer, Integer> getKey(OpinionUser value) throws Exception {
        return Tuple2.of(value.ageGroup.getKey(), value.region.getKey());
    }

}
