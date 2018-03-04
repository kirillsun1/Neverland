package ee.knk.neverland.service;

import ee.knk.neverland.entity.Key;
import ee.knk.neverland.entity.User;

import java.util.List;

public interface KeyService {

    Key addKey(Key key);
    void cleanOutByUser(User user);
    boolean checkKey(User user, String keyText);
    List<Key> getAll();
}
