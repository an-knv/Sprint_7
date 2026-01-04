package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CourierCreds {
    private String login;
    private String password;


    public static CourierCreds credsFrom(Courier courier) {
        return new CourierCreds(courier.getLogin(), courier.getPassword());
    }
}
