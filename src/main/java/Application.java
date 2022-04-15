import Functions.GlobalReduce;
import Functions.OpinionKeySelector;
import Functions.VerifyProcessFunction;
import domain.opinion.AgeGroup;
import domain.opinion.OpinionUser;
import domain.opinion.Region;
import domain.source.OpinionUserSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.sink.PrintSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) throws Exception {
        SourceFunction<OpinionUser> source = new OpinionUserSource();
        SinkFunction<ArrayList<ArrayList<Integer>>> sink = new PrintSinkFunction<>();

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<OpinionUser> users = env.addSource(source).assignTimestampsAndWatermarks(
                WatermarkStrategy.<OpinionUser>forMonotonousTimestamps().withTimestampAssigner(
                        (user, t) -> user.getEventTimeMillis()));

        // Side output
        OutputTag<OpinionUser> wrongFormat = new OutputTag<OpinionUser>("WrongFormat") {};


        users.keyBy(new OpinionKeySelector())
                .process(new VerifyProcessFunction(wrongFormat))
                .windowAll(TumblingEventTimeWindows.of(Time.minutes(5)))
                .apply(new GlobalReduce())
                .addSink(sink);

        env.execute("Opinion Users");
    }

    public static class ReduceResult implements ReduceFunction<Tuple2<Integer, ArrayList<ArrayList<Integer>>>> {

        @Override
        public Tuple2<Integer, ArrayList<ArrayList<Integer>>>
            reduce(Tuple2<Integer, ArrayList<ArrayList<Integer>>> value1,
                   Tuple2<Integer, ArrayList<ArrayList<Integer>>> value2) throws Exception {

            ArrayList<ArrayList<Integer>> user_1 = value1.f1;
            ArrayList<ArrayList<Integer>> user_2 = value2.f1;

            ArrayList<ArrayList<Integer>> questionSums = new ArrayList<>();

            for (int i = 0; i < user_1.size(); ++i) {
                ArrayList<Integer> questionSum = new ArrayList<>();

                ArrayList<Integer> answer_1 = user_1.get(i);
                ArrayList<Integer> answer_2 = user_2.get(i);

                for (int ans = 0; ans < answer_1.size(); ++ans) {
                    questionSum.add(answer_1.get(ans) + answer_2.get(ans));
                }

                questionSums.add(questionSum);
            }

            return new Tuple2<>(value1.f0, questionSums);
        }
    }

}
