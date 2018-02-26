package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Key;
import ee.knk.neverland.entity.User;
import ee.knk.neverland.repository.KeyRepository;
import ee.knk.neverland.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class KeyServiceImpl implements KeyService{

    private final KeyRepository keyRepository;

    @Autowired
    public KeyServiceImpl(KeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    @Override
    public Key addKey(Key key) {
        return keyRepository.saveAndFlush(key);
    }

    @Override
    public void cleanOutByUser(User user) {
        keyRepository.clearUpOutOfDateKeys(user);
    }

    @Override
    public boolean checkKey(User user, String keyValue) {
        return keyRepository.exists(user, keyValue);
    }

    @Override
    public List<Key> getAll() {
        return keyRepository.findAll();
    }
}
