package ee.knk.neverland.controller;

import ee.knk.neverland.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class FileUploadControllerTest {

    @Mock
    private TokenController tokenController;
    @Mock
    private ProofController proofController;
    @Mock
    private GroupController groupController;
    @Mock
    private UserController userController;
    @InjectMocks
    private FileUploadController fileUploadController;
    private final String token = "dummy";

    @Test
    public void handleProofUploadControlsToken() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.empty());
        fileUploadController.handleProofUpload(token, 0L, mock(MultipartFile.class), "dummy");
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void handleUserAvatarUploadControlsToken() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.empty());
        fileUploadController.handleUserAvatarUpload(token, mock(MultipartFile.class));
        verify(tokenController).getTokenUser(token);
    }

    @Test
    public void handleGroupAvatarUploadControlsToken() {
        when(tokenController.getTokenUser(token)).thenReturn(Optional.empty());
        fileUploadController.handleGroupAvatarUpload(token, mock(MultipartFile.class), 0L);
        verify(tokenController).getTokenUser(token);
    }
}