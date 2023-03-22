package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DiscriminatorValue(value = "M")
public class Movie extends Item {

    private String director;

    private String actor;

}
