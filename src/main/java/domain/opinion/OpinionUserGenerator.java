package domain.opinion;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class OpinionUserGenerator {

    private int userId;
    private Instant startTime;
    private Random random;

    public OpinionUserGenerator(int userId, Instant startTime) {
        this.userId = userId;
        this.startTime = startTime;

        random = new Random();
        random.setSeed(System.currentTimeMillis());
    }

    public OpinionUser generate() {
        AgeGroup ageGroup = AgeGroup.valueOf(random.nextInt(AgeGroup.values().length));
        Region region = Region.valueOf(random.nextInt(Region.values().length));

        // make answers
        ArrayList<ArrayList<Integer>> questions = new ArrayList<>(3);

        for (int i = 0; i < 3; ++i) {
            ArrayList<Integer> answer = new ArrayList<>(5);
            int okay = random.nextInt(5);

            for (int ok = 0; ok < 5; ++ok) {
                if (ok == okay) {
                    answer.add(1);
                } else {
                    answer.add(0);
                }
            }

            questions.add(answer);
        }

        return new OpinionUser(this.userId, ageGroup, region, questions, this.startTime);
    }

}
