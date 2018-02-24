package ee.knk.neverland.service;

import ee.knk.neverland.entity.Key;

import java.util.List;

public interface KeyService {

    Key addKey(Key key);
    void cleanOutByUser(Long userId);
    boolean checkKey(Long userId, String keyText);
    List<Key> getAll();
}
