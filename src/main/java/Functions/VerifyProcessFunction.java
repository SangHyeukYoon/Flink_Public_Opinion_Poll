package Functions;

import domain.opinion.OpinionUser;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.IOException;
import java.util.ArrayList;

public class VerifyProcessFunction extends KeyedProcessFunction<Tuple2<Integer, Integer>, OpinionUser, OpinionUser> {

    private ValueState<Integer> count;

    private OutputTag<OpinionUser> wrongFormat;

    public VerifyProcessFunction(OutputTag<OpinionUser> wrongFormat) {
        super();

        this.wrongFormat = wrongFormat;
    }

    @Override
    public void open(Configuration conf) {
        count = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("counter", Integer.class));
    }

    @Override
    public void processElement(OpinionUser value, Context ctx, Collector<OpinionUser> out) throws Exception {
        // verify answers
        boolean isWrongFormat = false;
        if (value.questions.size() != 3) {
            isWrongFormat = true;
        } else {
            for (ArrayList<Integer> answer: value.questions) {
                if (answer.size() != 5) {
                    isWrongFormat = true;
                } else {
                    int sum = 0;
                    for (int v: answer) {
                        sum += v;
                    }

                    if (sum != 1) {
                        isWrongFormat = true;
                    }
                }
            }
        }

        // If it has wrong format, put to side output.
        if (isWrongFormat) {
            ctx.output(wrongFormat, value);
            return;
        }

//        int counter = count.value();
//        count.update(counter + 1);
        out.collect(value);
    }
}
