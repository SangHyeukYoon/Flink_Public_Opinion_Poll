package domain.opinion;

import org.apache.flink.annotation.VisibleForTesting;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

public class OpinionUser implements Serializable {

    public int userId;
    public AgeGroup ageGroup;
    public Region region;

    public ArrayList<ArrayList<Integer>> questions;

    public Instant startTime;

    public OpinionUser(int userId, AgeGroup ageGroup, Region region,
                       ArrayList<ArrayList<Integer>> questions, Instant startTime) {
        this.userId = userId;
        this.ageGroup = ageGroup;
        this.region = region;
        this.questions = questions;
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        StringBuilder questionStr = new StringBuilder();

        for (ArrayList<Integer> choice: this.questions) {
            questionStr.append("[");
            for (int c: choice) {
                if (c == 0) {
                    questionStr.append("X");
                } else {
                    questionStr.append("O");
                }
            }
            questionStr.append("], ");
        }

        return "[" + ageGroup.toString() + ", " + region.toString() + "] " + questionStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpinionUser user = (OpinionUser) o;

        return ageGroup.equals(user.ageGroup) &&
                region.equals(user.region) &&
                questions.equals(user.questions) &&
                startTime.equals(user.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ageGroup, region, questions, startTime);
    }

    public long getEventTimeMillis() {
        return startTime.toEpochMilli();
    }

    @VisibleForTesting
    public StreamRecord<OpinionUser> asStreamRecord() {
        return new StreamRecord<>(this, this.getEventTimeMillis());
    }

}
