package at.noahb.springdonations.payload.response;

import at.noahb.springdonations.domain.Person;

public record GetAllWithMinDonationResponse(Person person, int amount) {

}
