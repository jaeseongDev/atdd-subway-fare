package wooteco.subway.member.domain.agemember;

public class Baby implements AgeMember {

    private static final int BABY_AGE_UPPER_BOUNDARY = 5;
    private static final int NO_FARE = 0;

    @Override
    public boolean isInAgeRange(int age) {
        return age <= BABY_AGE_UPPER_BOUNDARY;
    }

    @Override
    public int discountFareByAge(int fare) {
        return NO_FARE;
    }
}
