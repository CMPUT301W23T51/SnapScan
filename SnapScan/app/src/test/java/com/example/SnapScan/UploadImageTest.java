package com.example.SnapScan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import android.net.Uri;

import com.example.SnapScan.ui.qr.PostScanFragment;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicMarkableReference;

@RunWith(MockitoJUnitRunner.class)
public class UploadImageTest {
    @Mock
    private FirebaseStorage firebaseStorage;

    @Mock
    private StorageReference storageReference;

    @Mock
    private Uri uri;

    private PostScanFragment fragment;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fragment = new PostScanFragment();
        fragment.storage = firebaseStorage;
        uri = Mockito.mock(Uri.class);

    }

    @Test
    public void testUploadImage() {
        // Mock FirebaseStorage and StorageReference
        when(firebaseStorage.getReference()).thenReturn(storageReference);
        StorageReference childReference = mock(StorageReference.class);
        when(storageReference.child(anyString())).thenReturn(childReference);
        UploadTask uploadTask = mock(UploadTask.class);
        when(childReference.putFile(any(Uri.class))).thenReturn(uploadTask);

        fragment.imageUri = uri;
        fragment.uploadImage();

        // Verify that methods were called on mock objects
        verify(firebaseStorage, times(1)).getReference();
        verify(childReference, times(1)).putFile(eq(uri));
    }
}
