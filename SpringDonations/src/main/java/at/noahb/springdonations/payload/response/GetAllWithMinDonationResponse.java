package at.noahb.springdonations.payload.response;

import at.noahb.springdonations.domain.Person;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class GetAllWithMinDonationResponse {

    private final Person person;
    private final int amount;



}
