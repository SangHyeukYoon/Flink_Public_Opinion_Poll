package domain.source;

import domain.opinion.AgeGroup;
import domain.opinion.OpinionUser;
import domain.opinion.OpinionUserGenerator;
import domain.opinion.Region;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OpinionUserSource implements SourceFunction<OpinionUser> {

    private volatile boolean running = true;

    @Override
    public void run(SourceContext<OpinionUser> ctx) throws Exception {
        Instant startTime = Instant.parse("2022-04-15T08:00:00Z");
        Instant endTime = Instant.parse("2022-04-15T08:01:00Z");

        Random timeRandom = new Random();
        timeRandom.setSeed(System.currentTimeMillis());

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        final long timeInterval = 30;    // second

        int userId = 0;

        while (running && startTime.isBefore(endTime)) {
            OpinionUser opinionUser = new OpinionUserGenerator(userId++, startTime).generate();

            ctx.collect(opinionUser);

            // Update time
            startTime = startTime.plusSeconds(timeRandom.nextLong() % timeInterval);

            Thread.sleep(Time.milliseconds(500).toMilliseconds());
        }
    }

    @Override
    public void cancel() {
        running = false;
    }

}
