import lombok.*;

@Getter
@ToString
class Name{
    String common, official;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Country {

    private Name name;
    private String region;
    private String subregion;
    private int population;
    private String[] capital;
    private String status;
    private String unMember;

}
