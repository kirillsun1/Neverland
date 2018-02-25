package ee.knk.neverland.service.impl;

import ee.knk.neverland.entity.Key;
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
    public void cleanOutByUser(Long userId) {

    }

    @Override
    public boolean checkKey(Long userId, String keyText) {
        return false;
    }

    @Override
    public List<Key> getAll() {
        return null;
    }
}
