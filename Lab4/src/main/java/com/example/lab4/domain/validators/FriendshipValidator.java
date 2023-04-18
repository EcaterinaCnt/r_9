package com.example.lab4.domain.validators;
import com.example.lab4.domain.FriendshipDB;

public class FriendshipValidator implements Validator<FriendshipDB>{

    @Override
    public void validate(FriendshipDB entity) throws ValidationException {
        if (entity.getUtilizator1() == entity.getUtilizator2())
            throw new ValidationException("Prietenii nu pot avea acelasi ID!");
    }
}
//validator de unicitate intre ID-urile dintr o prietenie